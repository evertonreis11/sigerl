package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_pedidovendaprodutostatus", sequenceName = "sq_pedidovendaprodutostatus")
public class Pedidovendaprodutostatus {
	
	public static Pedidovendaprodutostatus DISPONIVEL = new Pedidovendaprodutostatus(1,"Dispoível","D");
	public static Pedidovendaprodutostatus EM_CARREGAMENTO = new Pedidovendaprodutostatus(2,"Em carregamento","C");
	public static Pedidovendaprodutostatus EM_FATURAMENTO = new Pedidovendaprodutostatus(3,"Em Faturamento","EF");
	public static Pedidovendaprodutostatus FATURADO = new Pedidovendaprodutostatus(4,"Faturado","F");
	public static Pedidovendaprodutostatus MANIFESTADO = new Pedidovendaprodutostatus(5,"Manifestado","M");
	public static Pedidovendaprodutostatus EM_ENTREGA = new Pedidovendaprodutostatus(6,"Em Entrega","E");
	public static Pedidovendaprodutostatus PRODUTO_ENTREGUE = new Pedidovendaprodutostatus(7,"Produto Entregue","PE");
	public static Pedidovendaprodutostatus PRODUTO_RETORNADO = new Pedidovendaprodutostatus(8,"Produto Retornado","R");
	public static Pedidovendaprodutostatus EXCLUIDO = new Pedidovendaprodutostatus(9,"Excluido","EX");
	public static Pedidovendaprodutostatus ENTREGA_TRAVADA = new Pedidovendaprodutostatus(10,"Entrega Travada","ET");

	private Integer cdpedidovendaprodutostatus;
	private String nome;
	private String codigoerp;
	
	public Pedidovendaprodutostatus(){
	}
	
	public Pedidovendaprodutostatus (Integer cdpedidovendaprodutostatus, String nome, String codigoerp){
		this.cdpedidovendaprodutostatus = cdpedidovendaprodutostatus;
		this.nome = nome;
		this.codigoerp = codigoerp;
	}
	

	//Get's
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pedidovendaprodutostatus")	
	public Integer getCdpedidovendaprodutostatus() {
		return cdpedidovendaprodutostatus;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public String getCodigoerp() {
		return codigoerp;
	}
	
	
	//Set's
	public void setCdpedidovendaprodutostatus(Integer cdpedidovendaprodutostatus) {
		this.cdpedidovendaprodutostatus = cdpedidovendaprodutostatus;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCodigoerp(String codigoerp) {
		this.codigoerp = codigoerp;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Pedidovendaprodutostatus){
			Pedidovendaprodutostatus pvps = (Pedidovendaprodutostatus) obj;
			try {
				if(pvps.getCdpedidovendaprodutostatus().equals(this.getCdpedidovendaprodutostatus()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
