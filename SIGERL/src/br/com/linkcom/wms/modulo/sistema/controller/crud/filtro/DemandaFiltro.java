package br.com.linkcom.wms.modulo.sistema.controller.crud.filtro;

import java.sql.Date;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;

public class DemandaFiltro extends FiltroListagem{
	
    private String funcionalidade;
    private String enderecourl;
    private Date dtprevisao;
    private Date dthomologacao;
    private Date dtimplantacao;       
    private Integer numeroversao;
    
    
    //Get's
    @DisplayName("Funcionalidade")
	public String getFuncionalidade() {
		return funcionalidade;
	}
    @DisplayName("Link da Base de Conhecimento")
	public String getEnderecourl() {
		return enderecourl;
	}
    @DisplayName("Data de Previsão")
	public Date getDtprevisao() {
		return dtprevisao;
	}
    @DisplayName("Data de Homologação")
	public Date getDthomologacao() {
		return dthomologacao;
	}
    @DisplayName("Data da Implantação")
	public Date getDtimplantacao() {
		return dtimplantacao;
	}
    @DisplayName("Número da Versão")
	public Integer getNumeroversao() {
		return numeroversao;
	}
	
	
	//Set's
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
	public void setNumeroversao(Integer numeroversao) {
		this.numeroversao = numeroversao;
	}
    
}
