package com.visus.central.application.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.visus.central.domain.model.AplicacionPago;
import com.visus.central.domain.model.Articulo;
import com.visus.central.infraestructure.util.FormatoUtils;
import com.visus.central.domain.model.Caja;
import com.visus.central.domain.model.Cliente;
import com.visus.central.domain.model.Comprobante;
import com.visus.central.domain.model.EstadoPlanPago;
import com.visus.central.domain.model.EstadoVenta;
import com.visus.central.domain.model.Item;
import com.visus.central.domain.model.ItemPago;
import com.visus.central.domain.model.MovimientoCaja;
import com.visus.central.domain.model.NotaCredito;
import com.visus.central.domain.model.OrigenMovimiento;
import com.visus.central.domain.model.Pago;
import com.visus.central.domain.model.PlanPago;
import com.visus.central.domain.model.TipoPago;
import com.visus.central.domain.model.Venta;
import com.visus.central.domain.port.in.FacturacionUseCase;
import com.visus.central.domain.port.out.AplicacionPagoRepository;
import com.visus.central.domain.port.out.ArticuloRepository;
import com.visus.central.domain.port.out.CajaRepository;
import com.visus.central.domain.port.out.ComprobanteRepository;
import com.visus.central.domain.port.out.MovimientoCajaRepository;
import com.visus.central.domain.port.out.NotaCreditoRepository;
import com.visus.central.domain.port.out.PagoRepository;
import com.visus.central.domain.port.out.PlanPagoRepository;
import com.visus.central.domain.port.out.VentaRepository;
import jakarta.transaction.Transactional;

@Service
public class FacturacionServiceImpl implements FacturacionUseCase {

	Logger logger = LoggerFactory.getLogger(FacturacionServiceImpl.class);

	private final ArticuloRepository articuloRepository;
	private final ComprobanteRepository comprobanteRepository;
	private final VentaRepository ventaRepository;
	private final PlanPagoRepository planPagoRepository;
	private final PagoRepository pagoRepository;
	private final AplicacionPagoRepository aplicacionPagoRepository;
	private final CajaRepository cajaRepo;
	private final MovimientoCajaRepository movimientoRepo;
	private final NotaCreditoRepository notaCreditoRepository;

	public FacturacionServiceImpl(ArticuloRepository articuloRepository, ComprobanteRepository comprobanteRepository,
			VentaRepository ventaRepository, PlanPagoRepository planPagoRepository, PagoRepository pagoRepository,
			AplicacionPagoRepository aplicacionPagoRepository, MovimientoCajaRepository movimientoRepo,
			CajaRepository cajaRepo, NotaCreditoRepository notaCreditoRepository) {
		this.articuloRepository = articuloRepository;
		this.comprobanteRepository = comprobanteRepository;
		this.ventaRepository = ventaRepository;
		this.planPagoRepository = planPagoRepository;
		this.pagoRepository = pagoRepository;
		this.aplicacionPagoRepository = aplicacionPagoRepository;
		this.cajaRepo = cajaRepo;
		this.movimientoRepo = movimientoRepo;
		this.notaCreditoRepository = notaCreditoRepository;
	}

	@Override
	public Venta iniciarNuevaVenta() {
		Venta venta = new Venta();
		venta.setFechaVenta(LocalDate.now());
		venta.setEsBonificado(false);
		venta.setBonificacion(BigDecimal.ZERO);
		venta.setEstado(EstadoVenta.PENDIENTE); // según el enum de dominio
		venta.setItems(new ArrayList<>());

		return venta;
	}

	private Comprobante determinarComprobante(Cliente cliente) {
		// TODO: Implementar lógica real de determinación según situación fiscal del
		// cliente.
		// Por ahora, siempre se usa REMITO para todas las ventas.
		String nombreCorto = "REM";

		// Lógica según situación fiscal del cliente (ajusta según tu modelo)
		/*
		 * if (cliente.getSituacionFiscal() ==
		 * SituacionFiscal.IVA_Responsable_Inscripto) { nombreCorto = "FAA"; // Factura
		 * A } else if (cliente.getSituacionFiscal() ==
		 * SituacionFiscal.Consumidor_Final) { nombreCorto = "FAB"; // Factura B } else
		 * { nombreCorto = "FAC"; // Factura C (por defecto para monotributista, etc.) }
		 */

		return comprobanteRepository.findByNombreCorto(nombreCorto)
				.orElseThrow(() -> new RuntimeException("No se encontró el comprobante: " + nombreCorto));
	}

	@Override
	public void agregarItem(Venta venta, String codigoBarra, Integer cantidad) {
		Articulo prod = articuloRepository.findByCodigoBarra(codigoBarra)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado"));
		if (!prod.isDisponible()) {
			throw new RuntimeException("Producto no habilitado para venta");
		}
		if (prod.getStock().compareTo(cantidad) < 0)
			throw new RuntimeException("Stock insuficiente");
		if (prod.getStock().compareTo(prod.getStock_minimo()) <= 0) {
			// Aviso al usuario (la UI mostrará el mensaje)
			throw new RuntimeException("Stock mínimo alcanzado para: " + prod.getDescripcion());
		}

		BigDecimal gravamen = prod.getAlicuota() != null && prod.getAlicuota().getGravamen() != null
				? prod.getAlicuota().getGravamen() : BigDecimal.ZERO;
		BigDecimal precioVenta = FormatoUtils.calcularPrecioVenta(prod.getPrecioCosto(), prod.getMargenUtilidad(), gravamen);
		Item item = new Item(prod, cantidad, precioVenta);

		venta.getItems().add(item);
	}

	@Override
	public void eliminarItem(Venta venta, int index) {
		if (index >= 0 && index < venta.getItems().size()) {
			venta.getItems().remove(index);
		}
	}

	@Override
	public void aplicarDescuento(Venta venta, BigDecimal porcentaje) {
		venta.setEsBonificado(porcentaje.compareTo(BigDecimal.ZERO) > 0);
		venta.setBonificacion(porcentaje);
	}

	@Override
	@Transactional
	public void guardarVenta(Venta venta, boolean imprimir) {
		// 1. Validar cliente habilitado
		Cliente cliente = venta.getCliente();
		if (cliente == null || !cliente.isHabilitado())
			throw new RuntimeException("Cliente no habilitado para comprar");

		// 2. Validar fecha
		LocalDate ultimaFecha = obtenerUltimaFechaFactura();
		LocalDate hoy = LocalDate.now();
		if (venta.getFechaVenta().isBefore(ultimaFecha) || venta.getFechaVenta().isAfter(hoy))
			throw new RuntimeException("Fecha inválida: debe estar entre " + ultimaFecha + " y " + hoy);

		// 3. Validar límite de comprobante (antes de guardar)
		Comprobante comp = determinarComprobante(venta.getCliente());
		Integer proximoNumero = comp.getNumeroActual() + 1;

		if (proximoNumero > comp.getNumeroFinal()) {
			throw new RuntimeException(
					String.format("Se alcanzó el límite máximo de comprobantes (%d). Debe solicitar un nuevo rango.",
							comp.getNumeroFinal()));
		}

		// Asignar a la venta
		venta.setComprobante(comp);
		venta.setNumeroComprobante(proximoNumero);

		// 4. Guardar venta (incluye items)
		logger.info("=== Guardando venta ===");
		logger.info("Venta antes de guardar - ID: {}", venta.getId());

		Venta ventaGuardada = ventaRepository.save(venta);

		logger.info("Venta después de guardar - ID: {}", ventaGuardada.getId());
		logger.info("Items en la venta: {}", ventaGuardada.getItems().size());

		// 5. Actualizar stock de cada producto
		for (Item item : ventaGuardada.getItems()) {
			Articulo prod = item.getArticulo();
			prod.setStock(prod.getStock() - item.getCantidad());
			articuloRepository.save(prod);
		}

		// 6. Incrementar número actual del comprobante (después de guardar la venta)
		comp.setNumeroActual(proximoNumero);
		comprobanteRepository.save(comp);

		// 7. Procesar pagos mixtos (incluye registro de movimientos de caja)
		venta.setId(ventaGuardada.getId());
		procesarPagosMixtos(venta);

		// 8. Actualizar estado de la venta según los planes de pago generados
		List<PlanPago> planes = planPagoRepository.findByVentaId(venta.getId());
		boolean todosPagados = planes.stream().allMatch(p -> p.getEstado() == EstadoPlanPago.PAGADA);
		if (todosPagados) {
			venta.setEstado(EstadoVenta.COBRADA);
			ventaRepository.save(venta);
			logger.info("Venta ID {} marcada como COBRADA (sin deuda pendiente)", venta.getId());
		}

		// 9. Imprimir si corresponde
		if (imprimir) {
			System.out.println("Imprimiendo comprobante N° " + ventaGuardada.getNumeroComprobante());
		}
	}

	private void procesarPagosMixtos(Venta venta) {
		if (venta.getMediosPago() == null || venta.getMediosPago().isEmpty()) {
			logger.warn("La venta ID {} no tiene medios de pago asociados", venta.getId());
			return;
		}

		logger.info("=== Procesando pagos mixtos para venta ID: {}", venta.getId());

		// Obtener la caja abierta (se lanzará excepción si no existe)
		Caja caja = cajaRepo.findCajaAbierta().orElseThrow(
				() -> new IllegalStateException("No hay caja abierta. No se pueden registrar movimientos de pago."));

		for (ItemPago itemPago : venta.getMediosPago()) {
			// 1. Registrar el pago (cabecera)
			Pago pago = new Pago();
			pago.setIdCliente(venta.getCliente().getId());
			pago.setIdVenta(venta.getId());
			pago.setFecha(venta.getFechaVenta());
			pago.setMontoTotal(itemPago.getMonto());
			pago.setIdTipoPago(itemPago.getTipoPago().getId());
			pago.setAplicado(false);
			pago = pagoRepository.save(pago);

			TipoPago tipoPago = itemPago.getTipoPago();
			boolean generaDeuda = Boolean.TRUE.equals(tipoPago.getGenera_deuda());
			boolean requiereCoeficiente = Boolean.TRUE.equals(tipoPago.getRequiere_coeficiente());

			if (generaDeuda) {
				// ---------- CUENTA CORRIENTE (genera deuda) ----------
				if (!requiereCoeficiente || itemPago.getCoeficiente() == null) {
					throw new RuntimeException(
							"El tipo de pago " + tipoPago.getDescripcion() + " requiere un coeficiente");
				}
				Short cuotas = itemPago.getCoeficiente().getCuotas();
				BigDecimal coeficienteValor = itemPago.getCoeficiente().getCoeficiente();
				BigDecimal totalFinanciado = itemPago.getMonto().multiply(coeficienteValor);
				BigDecimal montoCuota = totalFinanciado.divide(BigDecimal.valueOf(cuotas), 2, RoundingMode.HALF_UP);

				for (int i = 1; i <= cuotas; i++) {
					LocalDate fechaVencimiento = calcularFechaVencimiento(venta.getFechaVenta(), i,
							Boolean.TRUE.equals(tipoPago.getEs_pronto_pago()));
					PlanPago planPago = new PlanPago();
					planPago.setIdVenta(venta.getId());
					planPago.setIdCliente(venta.getCliente().getId());
					planPago.setIdTipoPago(tipoPago.getId());
					planPago.setIdCoeficiente(itemPago.getCoeficiente().getId());
					planPago.setMontoOriginal(montoCuota);
					planPago.setNroCuota((short) i);
					planPago.setFechaVencimiento(fechaVencimiento);
					planPago.setMontoPagado(BigDecimal.ZERO);
					planPago.setMontoDescuentoTotal(BigDecimal.ZERO);
					planPago.setFechaPago(null);
					planPago.setEstado(EstadoPlanPago.PENDIENTE);
					planPagoRepository.save(planPago);
					logger.info("Cuota {} - Vencimiento: {}, Monto: {}", i, fechaVencimiento, montoCuota);
				}

				// Auto-aplicar saldo a favor (notas de crédito) a la primera cuota
				aplicarSaldoFavorPrimeraCuota(venta, tipoPago.getId());
			} else {
				// ---------- PAGO CONTADO (no genera deuda) ----------
				PlanPago planPago = new PlanPago();
				planPago.setIdVenta(venta.getId());
				planPago.setIdCliente(venta.getCliente().getId());
				planPago.setIdTipoPago(tipoPago.getId());
				planPago.setIdCoeficiente(null);
				planPago.setMontoOriginal(itemPago.getMonto());
				planPago.setNroCuota((short) 0);
				planPago.setFechaVencimiento(null);
				planPago.setMontoPagado(itemPago.getMonto());
				planPago.setMontoDescuentoTotal(BigDecimal.ZERO);
				planPago.setFechaPago(venta.getFechaVenta());
				planPago.setEstado(EstadoPlanPago.PAGADA);
				planPago = planPagoRepository.save(planPago);

				// Crear aplicación de pago para vincular el pago con la cuota
				AplicacionPago aplicacion = new AplicacionPago();
				aplicacion.setIdPago(pago.getIdPago());
				aplicacion.setIdPlanPago(planPago.getIdPlanPago());
				aplicacion.setMontoAplicado(itemPago.getMonto());
				aplicacion.setMontoNetoPagado(itemPago.getMonto());
				aplicacion.setPctProntoPago(BigDecimal.ZERO);
				aplicacion.setMontoDescuentoAplicado(BigDecimal.ZERO);
				aplicacionPagoRepository.save(aplicacion);

				// Marcar el pago como aplicado
				pago.setAplicado(true);
				pagoRepository.save(pago);
				logger.info("Pago contado - Monto: {}, Cuota 0 (pagada)", itemPago.getMonto());
			}

			// ---------- REGISTRAR MOVIMIENTO DE CAJA (para tipos que afectan caja)
			// ----------
			if (Boolean.TRUE.equals(tipoPago.getAfecta_caja())) {
				registrarMovimientoCaja(venta, itemPago, caja);
			}
		}

		// Verificación de totales
		BigDecimal totalPagado = pagoRepository.sumMontoTotalByVentaId(venta.getId());
		BigDecimal totalVenta = calcularTotal(venta).setScale(2, RoundingMode.HALF_UP);
		if (totalPagado.setScale(2, RoundingMode.HALF_UP).compareTo(totalVenta) != 0) {
			logger.warn("Total pagado ({}) no coincide con total venta ({})", totalPagado, totalVenta);
		}
	}

	private void aplicarSaldoFavorPrimeraCuota(Venta venta, Integer idTipoPago) {
		List<NotaCredito> creditos = notaCreditoRepository.findNoConsumidosByClienteId(venta.getCliente().getId());
		if (creditos.isEmpty())
			return;

		BigDecimal totalCredito = creditos.stream().map(NotaCredito::getMonto)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		List<PlanPago> cuotas = planPagoRepository.findByVentaId(venta.getId());

		PlanPago primeraCuota = cuotas.stream()
				.filter(c -> c.getEstado() == EstadoPlanPago.PENDIENTE)
				.min(java.util.Comparator.comparing(PlanPago::getNroCuota))
				.orElse(null);

		if (primeraCuota == null)
			return;

		BigDecimal montoAplicar = totalCredito.min(primeraCuota.getMontoOriginal());
		if (montoAplicar.compareTo(BigDecimal.ZERO) <= 0)
			return;

		primeraCuota.setMontoOriginal(primeraCuota.getMontoOriginal().subtract(montoAplicar));
		if (primeraCuota.getMontoOriginal().compareTo(BigDecimal.ZERO) == 0) {
			primeraCuota.setEstado(EstadoPlanPago.PAGADA);
			primeraCuota.setFechaPago(venta.getFechaVenta());
		}
		planPagoRepository.save(primeraCuota);

		// Registrar pago por la nota de crédito para que aparezca en pagos aplicados
		Pago pagoNC = new Pago();
		pagoNC.setIdCliente(venta.getCliente().getId());
		pagoNC.setIdVenta(venta.getId());
		pagoNC.setFecha(venta.getFechaVenta());
		pagoNC.setMontoTotal(montoAplicar);
		pagoNC.setIdTipoPago(idTipoPago);
		pagoNC.setAplicado(true);
		pagoNC.setObservaciones("Aplicación saldo a favor - Nota de Crédito");
		pagoNC = pagoRepository.save(pagoNC);

		AplicacionPago apNC = new AplicacionPago();
		apNC.setIdPago(pagoNC.getIdPago());
		apNC.setIdPlanPago(primeraCuota.getIdPlanPago());
		apNC.setMontoAplicado(montoAplicar);
		apNC.setMontoNetoPagado(montoAplicar);
		apNC.setPctProntoPago(BigDecimal.ZERO);
		apNC.setMontoDescuentoAplicado(BigDecimal.ZERO);
		aplicacionPagoRepository.save(apNC);

		logger.info("Saldo a favor aplicado a cuota {} de venta ID {}: ${}",
				primeraCuota.getNroCuota(), venta.getId(), montoAplicar);

		BigDecimal resto = montoAplicar;
		for (NotaCredito nc : creditos) {
			if (resto.compareTo(BigDecimal.ZERO) <= 0)
				break;
			if (nc.getMonto().compareTo(resto) <= 0) {
				nc.setConsumido(true);
				resto = resto.subtract(nc.getMonto());
			} else {
				nc.setMonto(nc.getMonto().subtract(resto));
				resto = BigDecimal.ZERO;
			}
			notaCreditoRepository.save(nc);
		}
	}

	/**
	 * Registra un movimiento automático de caja por un item de pago que afecta
	 * caja.
	 */
	private void registrarMovimientoCaja(Venta venta, ItemPago itemPago, Caja caja) {
		MovimientoCaja mov = new MovimientoCaja();
		mov.setCaja(caja);
		mov.setFecha(venta.getFechaVenta() != null ? venta.getFechaVenta() : LocalDate.now());
		mov.setHora(LocalTime.now());
		
		String tipoComprobante = venta.getComprobante() != null ? 
	            venta.getComprobante().getNombreCorto().name() : "N/A";
	    mov.setDescripcion(String.format("%s #%d - %s", 
	            tipoComprobante,
	            venta.getNumeroComprobante(),
	            itemPago.getTipoPago().getDescripcion()));
		
		mov.setOrigen(OrigenMovimiento.AUTOMATICO);
		mov.setDebe(itemPago.getMonto()); // ingreso
		mov.setHaber(BigDecimal.ZERO);
		mov.setVenta(venta); // si el modelo tiene relación
		mov.setTipoPago(itemPago.getTipoPago()); // si el modelo tiene relación
		mov.setComprobante(venta.getComprobante());
	    mov.setNumeroComprobante(venta.getNumeroComprobante());

		logger.info("ID del movimiento antes de guardar: {}", mov.getId());
		movimientoRepo.save(mov);
		logger.info("Movimiento de caja registrado: Venta #{} - {} por ${}", venta.getNumeroComprobante(),
				itemPago.getTipoPago().getDescripcion(), itemPago.getMonto());
	}

	private LocalDate calcularFechaVencimiento(LocalDate fechaEmision, int numeroCuota, Boolean esMesesCompletos) {
		if (Boolean.TRUE.equals(esMesesCompletos)) {
			return fechaEmision.plusMonths(numeroCuota);
		} else {
			return fechaEmision.plusDays(30L * numeroCuota);
		}
	}

	@Override
	public BigDecimal calcularSubtotal(Venta venta) {
		return venta.getItems().stream().map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public BigDecimal calcularTotal(Venta venta) {
		BigDecimal subtotal = calcularSubtotal(venta);
		if (venta.getEsBonificado() && venta.getBonificacion() != null) {
			BigDecimal descuento = subtotal.multiply(venta.getBonificacion().divide(BigDecimal.valueOf(100)));
			return subtotal.subtract(descuento).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
		}
		return subtotal.setScale(2, RoundingMode.HALF_UP);
	}

	private LocalDate obtenerUltimaFechaFactura() {
		// Implemente según su lógica. Ejemplo: consultar la última venta guardada
		// Puede inyectar un puerto específico o hacer una consulta directa.
		// Por simplicidad, asumimos que no hay restricción de fecha anterior.
		return LocalDate.now().minusYears(1);
	}
	
	@Override
	public boolean hayCajaAbierta() {
	    return cajaRepo.findCajaAbierta().isPresent();
	}

}