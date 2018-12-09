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
@SequenceGenerator(name = "sq_controleestoquestatus", sequenceName = "sq_controleestoquestatus")
public class Controleestoquestatus {

	protected Integer cdcontroleestoquestatus;
	protected String nome;

	public static final Controleestoquestatus EM_ANDAMENTO = new Controleestoquestatus(1, "Em andamento");
	public static final Controleestoquestatus CONCLUIDO = new Controleestoquestatus(2, "Concluído");
	public static final Controleestoquestatus CONCLUIDO_DIVERGENCIA = new Controleestoquestatus(3, "Concluído com divergência");

	public Controleestoquestatus(){
		
	}
	
	public Controleestoquestatus(Integer cdControleestoquestatus) {
		this.cdcontroleestoquestatus = cdControleestoquestatus;
	}
	
	public Controleestoquestatus(Integer cdControleestoquestatus, String nome) {
		this.cdcontroleestoquestatus = cdControleestoquestatus;
		this.nome = nome;
	}
	
	//Metodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_controleestoquestatus")
	public Integer getCdcontroleestoquestatus() {
		return cdcontroleestoquestatus;
	}
	
	@Required
	@MaxLength(30)
	@DescriptionProperty
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
	
	public void setCdcontroleestoquestatus(Integer cdcontroleestoquestatus) {
		this.cdcontroleestoquestatus = cdcontroleestoquestatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
