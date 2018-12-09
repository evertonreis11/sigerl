package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_pedidovendatipo", sequenceName = "sq_pedidovendatipo")
public class Pedidovendatipo {

	protected Integer cdpedidovendatipo;
	protected String nome;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidovendatipo")
	public Integer getCdpedidovendatipo() {
		return cdpedidovendatipo;
	}
	public void setCdpedidovendatipo(Integer id) {
		this.cdpedidovendatipo = id;
	}

	
	@Required
	@MaxLength(30)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}

	
	public void setNome(String nome) {
		this.nome = nome;
	}

}
