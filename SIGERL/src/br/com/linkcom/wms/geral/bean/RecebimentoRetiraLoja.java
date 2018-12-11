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
@SequenceGenerator(name = "sq_recebimentoretiraloja", sequenceName = "sq_recebimentoretiraloja")
public class RecebimentoRetiraLoja {
	
	private Integer cdRecebimentoRetiraLoja;
	
	private Timestamp dtRecebimento;
	
	private Usuario usuario;
	
	private Deposito deposito;
	
	private Manifesto manifesto;
	
	private RecebimentoRetiraLojaStatus recebimentoRetiraLojaStatus;
	
	private List<RecebimentoRetiraLojaProduto> listaRecebimentoRetiraLojaProduto = new ListSet<RecebimentoRetiraLojaProduto>(RecebimentoRetiraLojaProduto.class);;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_recebimentoretiraloja")
	public Integer getCdRecebimentoRetiraLoja() {
		return cdRecebimentoRetiraLoja;
	}

	public void setCdRecebimentoRetiraLoja(Integer cdRecebimentoRetiraLoja) {
		this.cdRecebimentoRetiraLoja = cdRecebimentoRetiraLoja;
	}

	public Timestamp getDtRecebimento() {
		return dtRecebimento;
	}

	public void setDtRecebimento(Timestamp dtRecebimento) {
		this.dtRecebimento = dtRecebimento;
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdmanifesto")
	public Manifesto getManifesto() {
		return manifesto;
	}

	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdrecebretiralojastatus")
	public RecebimentoRetiraLojaStatus getRecebimentoRetiraLojaStatus() {
		return recebimentoRetiraLojaStatus;
	}

	public void setRecebimentoRetiraLojaStatus(RecebimentoRetiraLojaStatus recebimentoRetiraLojaStatus) {
		this.recebimentoRetiraLojaStatus = recebimentoRetiraLojaStatus;
	}
	
	@OneToMany(mappedBy="recebimentoRetiraLoja")
	public List<RecebimentoRetiraLojaProduto> getListaRecebimentoRetiraLojaProduto() {
		return listaRecebimentoRetiraLojaProduto;
	}

	public void setListaRecebimentoRetiraLojaProduto(List<RecebimentoRetiraLojaProduto> listaRecebimentoRetiraLojaProduto) {
		this.listaRecebimentoRetiraLojaProduto = listaRecebimentoRetiraLojaProduto;
	}
	
}
