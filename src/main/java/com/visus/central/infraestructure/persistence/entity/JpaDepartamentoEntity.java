package com.visus.central.infraestructure.persistence.entity;

import java.util.List;

import com.visus.central.infraestructure.converter.ProvinciaConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "departamentos")
public class JpaDepartamentoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iddepartamento")
	private Integer id;

	@Column(length = 60, nullable = false)
	private String nombre;

	@Convert(converter = ProvinciaConverter.class) // Esto le dice a JPA: “Usá el conversor para guardar y leer como VARCHAR”.
	@Column(name = "provincia", nullable = false, length = 35)
	private Provincia provincia;

	public enum Provincia {
		Buenos_Aires("Buenos Aires"), Catamarca("Catamarca"), Chaco("Chaco"), Chubut("Chubut"), Córdoba("Córdoba"),
		Corrientes("Corrientes"), Entre_Ríos("Entre Ríos"), Formosa("Formosa"), Jujuy("Jujuy"), La_Pampa("La Pampa"),
		La_Rioja("La Rioja"), Mendoza("Mendoza"), Misiones("Misiones"), Neuquén("Neuquén"), Río_Negro("Río Negro"),
		Salta("Salta"), San_Juan("San Juan"), San_Luis("San Luis"), Santa_Cruz("Santa Cruz"), Santa_Fe("Santa Fe"),
		Santiago_del_Estero("Santiago del Estero"), Tierra_del_Fuego("Tierra del Fuego"), Tucumán("Tucumán"),
		Ciudad_Autónoma_de_Buenos_Aires("Ciudad Autónoma de Buenos Aires");

		private final String label;

		Provincia(String label) {
			this.label = label;
		}
		
		@Override
	    public String toString() {
	        return label;
	    }

		public String getLabel() {
			return label;
		}

		public static Provincia fromLabel(String label) {
			for (Provincia p : values()) {
				if (p.label.equalsIgnoreCase(label)) {
					return p;
				}
			}
			throw new IllegalArgumentException("Provincia inválida: " + label);
		}
		
	}
	
	@OneToMany(mappedBy = "departamento")
	private List<JpaLocalidadEntity> localidades;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}