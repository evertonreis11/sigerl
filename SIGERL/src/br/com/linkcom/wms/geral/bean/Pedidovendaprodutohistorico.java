package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_pedvendaprodutohistorico", sequenceName = "sq_pedvendaprodutohistorico")
public class Pedidovendaprodutohistorico {
	
	private Integer cdpedidovendaprodutohistorico;
	private Pedidovendaproduto pedidovendaproduto;
	private Pedidovendaprodutostatus pedidovendaprodutostatus;
	private Usuario usuario;
	private Date data;
	private String historico;
	private Pedidovendahistoricomotivo pedidovendahistoricomotivo;
	
	//Transients
	private List<Pedidovendaprodutohistorico> listaHistorico;
	private String motivo;
	
	
	
	//Get's
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedvendaprodutohistorico")	
	public Integer getCdpedidovendaprodutohistorico() {
		return cdpedidovendaprodutohistorico;
	}
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidovendaproduto")
	public Pedidovendaproduto getPedidovendaproduto() {
		return pedidovendaproduto;
	}
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidovendaprodutostatus")	
	public Pedidovendaprodutostatus getPedidovendaprodutostatus() {
		return pedidovendaprodutostatus;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuario")		
	public Usuario getUsuario() {
		return usuario;
	}
	public Date getData() {
		return data;
	}
	public String getHistorico() {
		return historico;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpedidovendahistoricomotivo")
	public Pedidovendahistoricomotivo getPedidovendahistoricomotivo() {
		return pedidovendahistoricomotivo;
	}
	
	
	//Set's
	public void setCdpedidovendaprodutohistorico(Integer cdpedidovendaprodutohistorico) {
		this.cdpedidovendaprodutohistorico = cdpedidovendaprodutohistorico;
	}
	public void setPedidovendaproduto(Pedidovendaproduto pedidovendaproduto) {
		this.pedidovendaproduto = pedidovendaproduto;
	}
	public void setPedidovendaprodutostatus(Pedidovendaprodutostatus pedidovendaprodutostatus) {
		this.pedidovendaprodutostatus = pedidovendaprodutostatus;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	public void setPedidovendahistoricomotivo(Pedidovendahistoricomotivo pedidovendahistoricomotivo) {
		this.pedidovendahistoricomotivo = pedidovendahistoricomotivo;
	}
	
	
	//Transient
	@Transient
	public List<Pedidovendaprodutohistorico> getListaHistorico() {
		return listaHistorico;
	}
	public void setListaHistorico(List<Pedidovendaprodutohistorico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}
	@Transient
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}