package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Cpf;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
public class Fornecedor extends Pessoa {
	
	private Pessoaendereco pessoaendereco;
	private Long codigoerp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoaendereco")
	@DisplayName("Endereço do cliente")
	public Pessoaendereco getPessoaendereco() {
		return pessoaendereco;
	}
	
	public void setPessoaendereco(Pessoaendereco pessoaendereco) {
		this.pessoaendereco = pessoaendereco;
	}
	
	@Override
	@Required
	@Transient
	public Cpf getCpf() {		
		return super.getCpf();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Fornecedor){
			Fornecedor fornecedor = (Fornecedor) obj;
			if(this.cdpessoa!=null && fornecedor.getCdpessoa()!=null){
				return this.cdpessoa.equals(fornecedor.getCdpessoa());
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {	
		if(this.getCdpessoa()!=null){
			return this.getCdpessoa().hashCode();
		}
		return super.hashCode();
	}
	
	public Long getCodigoerp() {
		return codigoerp;
	}
	
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
}
