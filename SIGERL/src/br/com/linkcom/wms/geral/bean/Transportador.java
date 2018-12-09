package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.persistence.DefaultOrderBy;

@Entity
@DefaultOrderBy("nome")
public class Transportador extends Pessoa {
	
	protected Deposito deposito;
	private Pessoaendereco pessoaendereco;
	private List<Transportadordeposito> listaTransDeposito = new ArrayList<Transportadordeposito>();
	private List<Deposito> listadeposito = new ArrayList<Deposito>();
	private String cddepositos;

	public Transportador() {
		//inicializando um valor default, estava dando um erro estranho ao criar novo
		this.ativo = true;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@DisplayName("Depósito")
	@JoinColumn(name = "cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoaendereco")
	@DisplayName("Endereço do cliente")
	public Pessoaendereco getPessoaendereco() {
		return pessoaendereco;
	}
	
	@OneToMany(mappedBy="transportador")
	public List<Transportadordeposito> getListaTransDeposito() {
		return listaTransDeposito;
	}	
	
	@Transient
	public List<Deposito> getListadeposito() {
		return listadeposito;
	}

	public void setListadeposito(List<Deposito> listadeposito) {
		this.listadeposito = listadeposito;
	}

	public void setListaTransDeposito(List<Transportadordeposito> listaTransDeposito) {
		this.listaTransDeposito = listaTransDeposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setPessoaendereco(Pessoaendereco pessoaendereco) {
		this.pessoaendereco = pessoaendereco;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transportador) {
			Transportador transportador = (Transportador) obj;
			if (transportador.getCdpessoa() != null
					&& this.getCdpessoa() != null) {
				return transportador.getCdpessoa().equals(this.getCdpessoa());
			}
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		if (cdpessoa != null) {
			return cdpessoa.hashCode();
		} else {
			return super.hashCode();
		}
	}

	@Transient
	public String getCddepositos() {
		return cddepositos;
	}

	public void setCddepositos(String cddepositos) {
		this.cddepositos = cddepositos;
	}

	@Transient
	public String getIdNomeDocumento(){
		
		StringBuilder autocomplete = new StringBuilder();
		
		if(cdpessoa!=null){
			autocomplete.append(cdpessoa).append(" - ");
		}else{
			autocomplete.append("N/A").append(" - ");
		}
		
		if(nome!=null && !nome.isEmpty()){
			autocomplete.append(nome).append(" - ");
		}else{
			autocomplete.append("N/A").append(" - ");
		}
		
		if(documento!=null && !documento.isEmpty()){
			autocomplete.append(documento);
		}else{
			autocomplete.append("N/A");
		}
		
		return autocomplete.toString();
	}
	
}
