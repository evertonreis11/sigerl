package br.com.linkcom.wms.geral.bean;

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
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.neo.validation.annotation.MinValue;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@DisplayName("Janela de agendamento")
@SequenceGenerator(name = "sq_agendajanela", sequenceName = "sq_agendajanela")
public class Agendajanela {

	private Integer cdagendajanela;
	private Deposito deposito;
	private Integer ocorrencias;
	private Integer disponivel;
	private List<Agendajanelaclasse> listaAgendajanelaclasse = new ListSet<Agendajanelaclasse>(Agendajanelaclasse.class);
	private Usuario usuario;
	private List<Produtoclasse> listaProdutoclasse = new ListSet<Produtoclasse>(Produtoclasse.class);

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendajanela")
	public Integer getCdagendajanela() {
		return cdagendajanela;
	}

	@Required
	@DisplayName("Depósito")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}

	@Required
	@MinValue(1)
	@MaxValue(9999)
	@MaxLength(4)
	@DisplayName("Nº de ocorrências")
	public Integer getOcorrencias() {
		return ocorrencias;
	}

	@Required
	@DisplayName("Classes de produtos")
	@OneToMany(fetch=FetchType.LAZY, mappedBy="agendajanela")
	public List<Agendajanelaclasse> getListaAgendajanelaclasse() {
		return listaAgendajanelaclasse;
	}
	
	@Transient
	public Integer getDisponivel() {
		return disponivel;
	}

	@Transient
	@DisplayName("Classes de produtos")
	public List<Produtoclasse> getListaProdutoclasse() {
		return listaProdutoclasse;
	}

	@DisplayName("Usuário")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpessoa")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setCdagendajanela(Integer cdagendajanela) {
		this.cdagendajanela = cdagendajanela;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setOcorrencias(Integer ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	
	public void setDisponivel(Integer preenchidas) {
		this.disponivel = preenchidas;
	}

	public void setListaAgendajanelaclasse(
			List<Agendajanelaclasse> listaAgendajanelaclasse) {
		this.listaAgendajanelaclasse = listaAgendajanelaclasse;
	}

	public void setListaProdutoclasse(List<Produtoclasse> listaProdutoclasse) {
		this.listaProdutoclasse = listaProdutoclasse;
	}

}
