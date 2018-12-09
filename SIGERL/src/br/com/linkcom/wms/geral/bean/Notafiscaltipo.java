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
@SequenceGenerator(name = "sq_notafiscaltipo", sequenceName = "sq_notafiscaltipo")
public class Notafiscaltipo {

	public static Notafiscaltipo FORNECEDOR_COMPRA = new Notafiscaltipo(1,"Fornecedor - Compra",true,"F");
	public static Notafiscaltipo FORNECEDOR_BONIFICACAO = new Notafiscaltipo(2,"Fornecedor - Bonificação",true,"B");
	public static Notafiscaltipo DEVOLUCAO = new Notafiscaltipo(3,"Devolução",false,"D");
	public static Notafiscaltipo TRANSFERENCIA = new Notafiscaltipo(4,"Transferência",false,"T");
	public static Notafiscaltipo OUTROS = new Notafiscaltipo(5,"Outros",false,"O");
	public static Notafiscaltipo VENDA = new Notafiscaltipo(6,"Venda",false,"V");
	
	protected Integer cdnotafiscaltipo;
	protected String nome;
	protected Boolean exigeagenda;
	protected String codigoerp;
	protected Boolean remanifestavel;

	public Notafiscaltipo(){		
	}
	
	public Notafiscaltipo(Integer cdnotafiscaltipo, String nome, Boolean exigeagenda, String codigoerp){
		this.cdnotafiscaltipo = cdnotafiscaltipo;
		this.nome = nome;
		this.exigeagenda = exigeagenda;
		this.codigoerp = codigoerp;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_notafiscaltipo")
	public Integer getCdnotafiscaltipo() {
		return cdnotafiscaltipo;
	}
	public void setCdnotafiscaltipo(Integer id) {
		this.cdnotafiscaltipo = id;
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
	
	@DisplayName("Exige agendamento?")
	public Boolean getExigeagenda() {
		return exigeagenda;
	}
	public void setExigeagenda(Boolean exigeagenda) {
		this.exigeagenda = exigeagenda;
	}
	
	public String getCodigoerp() {
		return codigoerp;
	}
	public void setCodigoerp(String codigoerp) {
		this.codigoerp = codigoerp;
	}

	public Boolean getRemanifestavel() {
		return remanifestavel;
	}

	public void setRemanifestavel(Boolean remanifestavel) {
		this.remanifestavel = remanifestavel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Notafiscaltipo){
			Notafiscaltipo nft = (Notafiscaltipo) obj;
			if(nft.getCdnotafiscaltipo().equals(this.getCdnotafiscaltipo())){
				return true;
			}
		}
		return false;
	}
	
}
