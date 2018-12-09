package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_tipooperacao", sequenceName = "sq_tipooperacao")
public class Tipooperacao {

	protected Integer cdtipooperacao;
	protected String nome;
	protected Boolean separacliente;
	protected Boolean imprimeetiqueta;
	protected String sigla;
	protected Boolean ativo;
	protected List<Pedidovendaproduto> listaPedidoVendaProduto = new ArrayList<Pedidovendaproduto>();
	
	public static final Tipooperacao ENTREGA_CD_CLIENTE = new Tipooperacao(1);
	public static final Tipooperacao MONSTRUARIO_LOJA = new Tipooperacao(2);
	public static final Tipooperacao TRANSFERENCIA_FILIAL_ENTREGA = new Tipooperacao(3);
	public static final Tipooperacao RETIRA_NO_CD = new Tipooperacao(4);
	public static final Tipooperacao ENTREGA_CD_CD_CLIENTE = new Tipooperacao(5);
	public static final Tipooperacao TRANSFERENCIA_CD_CD_CLIENTE = new Tipooperacao(6);
	public static final Tipooperacao AUTORIZACAO = new Tipooperacao(7);
	
	public Tipooperacao(Integer cdtipoopercao){
		this.cdtipooperacao = cdtipoopercao;
	}
	
	public Tipooperacao() {
	}

	public Tipooperacao(Integer cdtipooperacao, String tipooperacaonome) {
		this.cdtipooperacao = cdtipooperacao;
		this.nome = tipooperacaonome;
	}

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tipooperacao")
	public Integer getCdtipooperacao() {
		return cdtipooperacao;
	}
	public void setCdtipooperacao(Integer id) {
		this.cdtipooperacao = id;
	}

	
	@Required
	@MaxLength(30)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Separa por cliente")
	public Boolean getSeparacliente() {
		return separacliente;
	}
	
	public Boolean getImprimeetiqueta() {
		return imprimeetiqueta;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	@OneToMany(mappedBy="tipooperacao")
	public List<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}
	
	public void setListaPedidoVendaProduto(List<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}

	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tipooperacao){
			return this.cdtipooperacao.equals(((Tipooperacao)obj).getCdtipooperacao());
		}
		return super.equals(obj);
	}
	
	public void setSeparacliente(Boolean separacliente) {
		this.separacliente = separacliente;
	}
	
	public void setImprimeetiqueta(Boolean imprimeetiqueta) {
		this.imprimeetiqueta = imprimeetiqueta;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	@Override
	public int hashCode() {
		return getCdtipooperacao().hashCode();
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
