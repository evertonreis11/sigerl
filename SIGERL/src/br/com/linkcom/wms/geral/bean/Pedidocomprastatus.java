package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_pedidocomprastatus", sequenceName = "sq_pedidocomprastatus")
public class Pedidocomprastatus {
	
	//Constantes
	public static final Pedidocomprastatus DISPONIVEL = new Pedidocomprastatus(1, "Disponível");
	public static final Pedidocomprastatus LIBERADO = new Pedidocomprastatus(2, "Liberado");
	public static final Pedidocomprastatus RECEBIDO = new Pedidocomprastatus(3, "Recebido");
	public static final Pedidocomprastatus CANCELADO = new Pedidocomprastatus(4, "Cancelado");
	public static final Pedidocomprastatus AGENDADO = new Pedidocomprastatus(5, "Agendado");
	public static final Pedidocomprastatus LIBERADO_TOTAL = new Pedidocomprastatus(6, "Liberado total");
	
	protected Integer cdpedidocomprastatus;
	protected String nome;
	
	/* ------------------------------ construtores -----------------------------*/
	
	public Pedidocomprastatus() {
		
	}
	
	public Pedidocomprastatus(Integer cdpedidocomprastatus, String nome) {
		this.cdpedidocomprastatus = cdpedidocomprastatus;
		this.nome = nome;
	}
	
	/* ------------------------------ metodos get´s e set´s -----------------------------*/
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidocomprastatus")
	public Integer getCdpedidocomprastatus() {
		return cdpedidocomprastatus;
	}
	
	@DescriptionProperty
	@DisplayName("nome")
	@MaxLength(20)
	public String getNome() {
		return nome;
	}
	
	public void setCdpedidocomprastatus(Integer cdpedidocomprastatus) {
		this.cdpedidocomprastatus = cdpedidocomprastatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pedidocomprastatus) {
			return ((Pedidocomprastatus) obj).getCdpedidocomprastatus().equals(this.getCdpedidocomprastatus());
		} 
		return super.equals(obj);
	}
	
}
