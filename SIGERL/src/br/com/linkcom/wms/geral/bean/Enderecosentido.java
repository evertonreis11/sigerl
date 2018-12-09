package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

/**
 * 
 * @author Arantes
 *
 */
@Entity
@SequenceGenerator(name = "sq_enderecosentido", sequenceName = "sq_enderecosentido")
public class Enderecosentido {
	protected Integer cdenderecosentido;
	protected Area area;
	protected Integer rua;
	protected Integer sentido;
	
	// --------------------------- CONSTRUTOR --------------------------
	public Enderecosentido() {
	
	}
	
	public Enderecosentido(Integer cd) {
		this.cdenderecosentido = cd;
		
	}
	
	// ------------------------ METODOS GET E SET ----------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_enderecosentido")
	public Integer getCdenderecosentido() {
		return cdenderecosentido;
	}

	@DisplayName("Área")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdarea")
	public Area getArea() {
		return area;
	}
	
	@DisplayName("Rua")
	public Integer getRua() {
		return rua;
	}
	
	@DisplayName("Sentido")
	public Integer getSentido() {
		return sentido;
	}
	
	public void setCdenderecosentido(Integer cdenderecosentido) {
		this.cdenderecosentido = cdenderecosentido;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public void setRua(Integer rua) {
		this.rua = rua;
	}
	
	public void setSentido(Integer sentido) {
		this.sentido = sentido;
	}
}
