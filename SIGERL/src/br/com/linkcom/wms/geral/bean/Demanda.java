package br.com.linkcom.wms.geral.bean;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_demanda", sequenceName = "sq_demanda")
public class Demanda {
	
    private Integer cddemanda;
    private String funcionalidade;
    private String enderecourl;
    private Date dtprevisao;
    private Date dthomologacao;
    private Date dtimplantacao;       
    private Double numeroversao;
    
    
    //Get's
    @Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_demanda")
	public Integer getCddemanda() {
		return cddemanda;
	}
    @Required
    @DisplayName("Funcionalidade")
	public String getFuncionalidade() {
		return funcionalidade;
	}
    @Required
    @DisplayName("Link da Base de Conhecimento")
	public String getEnderecourl() {
		return enderecourl;
	}
    @Required
    @DisplayName("Data de Previsão")
	public Date getDtprevisao() {
		return dtprevisao;
	}
    @DisplayName("Data da Homologação")
	public Date getDthomologacao() {
		return dthomologacao;
	}
    @DisplayName("Data da Implantação")
	public Date getDtimplantacao() {
		return dtimplantacao;
	}
    @Required
    @DisplayName("Número da Versão")
	public Double getNumeroversao() {
		return numeroversao;
	}
	
	
	//Set's
	public void setCddemanda(Integer cddemanda) {
		this.cddemanda = cddemanda;
	}
	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}
	public void setEnderecourl(String enderecourl) {
		this.enderecourl = enderecourl;
	}
	public void setDtprevisao(Date dtprevisao) {
		this.dtprevisao = dtprevisao;
	}
	public void setDthomologacao(Date dthomologacao) {
		this.dthomologacao = dthomologacao;
	}
	public void setDtimplantacao(Date dtimplantacao) {
		this.dtimplantacao = dtimplantacao;
	}
	public void setNumeroversao(Double numeroversao) {
		this.numeroversao = numeroversao;
	}
	
}
