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
@DisplayName("Tela")
@SequenceGenerator(name = "sq_tela", sequenceName = "sq_tela")
public class Tela{

	protected Integer cdtela;
	protected String descricao;
	protected String path;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_tela")
	public Integer getCdtela() {
		return cdtela;
	}
	public void setCdtela(Integer id) {
		this.cdtela = id;
	}

	
	@MaxLength(100)
	@DescriptionProperty
	public String getDescricao() {
		return descricao;
	}
	
	@Required
	@MaxLength(100)
	public String getPath() {
		return path;
	}

	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

}
