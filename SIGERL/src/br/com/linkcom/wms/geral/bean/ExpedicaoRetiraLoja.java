package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.types.ListSet;

@Entity
@SequenceGenerator(name = "sq_expedicaoretiraloja", sequenceName = "sq_expedicaoretiraloja")
public class ExpedicaoRetiraLoja {
	
	private Integer cdExpedicaoRetiraLoja;
	
	private Timestamp dtExpedicao;
	
	private Usuario usuario;
	
	private Deposito deposito;
	
	private Notafiscalsaida notaFiscalSaida;
	
	private ExpedicaoRetiraLojaStatus expedicaoRetiraLojaStatus;
	
	private Boolean termoImpresso;
	
	private List<ExpedicaoRetiraLojaProduto> listaExpedicaoRetiraLojaProduto= new ListSet<ExpedicaoRetiraLojaProduto>(ExpedicaoRetiraLojaProduto.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_expedicaoretiraloja")
	public Integer getCdExpedicaoRetiraLoja() {
		return cdExpedicaoRetiraLoja;
	}

	public void setCdExpedicaoRetiraLoja(Integer cdExpedicaoRetiraLoja) {
		this.cdExpedicaoRetiraLoja = cdExpedicaoRetiraLoja;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdusuario")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public Timestamp getDtExpedicao() {
		return dtExpedicao;
	}

	public void setDtExpedicao(Timestamp dtExpedicao) {
		this.dtExpedicao = dtExpedicao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdnotafiscalsaida")
	public Notafiscalsaida getNotaFiscalSaida() {
		return notaFiscalSaida;
	}

	public void setNotaFiscalSaida(Notafiscalsaida notaFiscalSaida) {
		this.notaFiscalSaida = notaFiscalSaida;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdexpedicaoretiralojastatus")
	public ExpedicaoRetiraLojaStatus getExpedicaoRetiraLojaStatus() {
		return expedicaoRetiraLojaStatus;
	}

	public void setExpedicaoRetiraLojaStatus(ExpedicaoRetiraLojaStatus expedicaoRetiraLojaStatus) {
		this.expedicaoRetiraLojaStatus = expedicaoRetiraLojaStatus;
	}
	
	public Boolean getTermoImpresso() {
		return termoImpresso;
	}

	public void setTermoImpresso(Boolean termoImpresso) {
		this.termoImpresso = termoImpresso;
	}
	
	@OneToMany(mappedBy="expedicaoRetiraLoja")
	public List<ExpedicaoRetiraLojaProduto> getListaExpedicaoRetiraLojaProduto() {
		return listaExpedicaoRetiraLojaProduto;
	}

	public void setListaExpedicaoRetiraLojaProduto(List<ExpedicaoRetiraLojaProduto> listaExpedicaoRetiraLojaProduto) {
		this.listaExpedicaoRetiraLojaProduto = listaExpedicaoRetiraLojaProduto;
	}

}
