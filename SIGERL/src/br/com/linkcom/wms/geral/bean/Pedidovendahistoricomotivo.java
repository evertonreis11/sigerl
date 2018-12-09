package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Pedidovendahistoricomotivo {
	
	public static Pedidovendahistoricomotivo JURIDICO = new Pedidovendahistoricomotivo(1,"Jurídico");
	public static Pedidovendahistoricomotivo CANCELAMENTO = new Pedidovendahistoricomotivo(2,"Cancelamento");
	public static Pedidovendahistoricomotivo TROCA = new Pedidovendahistoricomotivo(3,"Troca");
	public static Pedidovendahistoricomotivo EXCLUSAO = new Pedidovendahistoricomotivo(4,"Exclusão");

	
	private Integer cdpedidovendahistoricomotivo;
	private String nome;
	
	public Pedidovendahistoricomotivo(){

	}
	
	public Pedidovendahistoricomotivo(Integer cdpedidovendahistoricomotivo, String nome){
		this.cdpedidovendahistoricomotivo = cdpedidovendahistoricomotivo;
		this.nome = nome;
	}	
	
	@Id
	public Integer getCdpedidovendahistoricomotivo() {
		return cdpedidovendahistoricomotivo;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdpedidovendahistoricomotivo(Integer cdpedidovendahistoricomotivo) {
		this.cdpedidovendahistoricomotivo = cdpedidovendahistoricomotivo;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
