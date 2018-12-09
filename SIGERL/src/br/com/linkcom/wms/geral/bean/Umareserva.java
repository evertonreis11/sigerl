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

@Entity
@SequenceGenerator(name = "sq_umareserva", sequenceName = "sq_umareserva")
public class Umareserva {

	protected Integer cdumareserva;
	protected Ordemservicoprodutoendereco ordemservicoprodutoendereco;
	protected Enderecoproduto enderecoproduto;
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_umareserva")
	public Integer getCdumareserva() {
		return cdumareserva;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdordemservicoprodutoendereco")
	public Ordemservicoprodutoendereco getOrdemservicoprodutoendereco() {
		return ordemservicoprodutoendereco;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdenderecoproduto")
	public Enderecoproduto getEnderecoproduto() {
		return enderecoproduto;
	}
	
	public void setCdumareserva(Integer cdusuariodeposito) {
		this.cdumareserva = cdusuariodeposito;
	}
	
	public void setOrdemservicoprodutoendereco(Ordemservicoprodutoendereco ordemservicoprodutoendereco) {
		this.ordemservicoprodutoendereco = ordemservicoprodutoendereco;
	}
	
	public void setEnderecoproduto(Enderecoproduto enderecoproduto) {
		this.enderecoproduto = enderecoproduto;
	}
	
}
