package com.visus.central.infraestructure.persistence.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lineas")
public class JpaLineaEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlineas")
    private Integer idLinea;

	@Column(name = "descripcion", nullable = false, length = 60)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idrubros", referencedColumnName = "idrubro", foreignKey = @ForeignKey(name = "fk_lineas_rubros"))
    private JpaRubroEntity rubro;

	public Integer getIdLinea() {
		return idLinea;
	}

	public void setIdLinea(Integer idLinea) {
		this.idLinea = idLinea;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public JpaRubroEntity getRubro() {
		return rubro;
	}

	public void setRubro(JpaRubroEntity rubro) {
		this.rubro = rubro;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, idLinea, rubro);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JpaLineaEntity other = (JpaLineaEntity) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(idLinea, other.idLinea)
				&& Objects.equals(rubro, other.rubro);
	}
    
    

}
