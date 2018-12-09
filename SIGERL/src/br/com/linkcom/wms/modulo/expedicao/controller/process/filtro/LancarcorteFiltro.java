package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Usuario;

public class LancarcorteFiltro extends FiltroListagem {
	
	protected Integer cdordemservico;
	protected Usuario usuario;
	protected Date data;
	protected Hora hrinicio;
	protected Hora hrfim;
	protected List<Etiquetaexpedicao> listaEtiquetas;
	protected List<Etiquetaexpedicao> listaEtiquetasCortadas;
	protected OrdemservicoUsuario ordemservicoUsuario;
	protected Boolean visualizacao;
	protected Boolean primeiraVisualizacao;
		
	@MaxLength(7)	
	@DisplayName("Ordem de serviço")
	public Integer getCdordemservico() {
		return cdordemservico;
	}
	
	@DisplayName("Responsável")
	@Required
	public Usuario getUsuario() {
		return usuario;
	}

	@DisplayName("Data")
	public Date getData() {
		return data;
	}

	@DisplayName("Hora início")
	public Hora getHrinicio() {
		return hrinicio;
	}

	@DisplayName("Hora fim")
	public Hora getHrfim() {
		return hrfim;
	}

	public Boolean getVisualizacao() {
		return visualizacao;
	}

	public List<Etiquetaexpedicao> getListaEtiquetas() {
		return listaEtiquetas;
	}
	
	public List<Etiquetaexpedicao> getListaEtiquetasCortadas() {
		return listaEtiquetasCortadas;
	}

	public OrdemservicoUsuario getOrdemservicoUsuario() {
		return ordemservicoUsuario;
	}
	
	public Boolean getPrimeiraVisualizacao() {
		return primeiraVisualizacao;
	}

	public void setPrimeiraVisualizacao(Boolean primeiraVisualizacao) {
		this.primeiraVisualizacao = primeiraVisualizacao;
	}

	public void setVisualizacao(Boolean visualizacao) {
		this.visualizacao = visualizacao;
	}

	public void setCdordemservico(Integer cdordemservico) {
		this.cdordemservico = cdordemservico;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setHrinicio(Hora hrinicio) {
		this.hrinicio = hrinicio;
	}

	public void setHrfim(Hora hrfim) {
		this.hrfim = hrfim;
	}
	
	public void setListaEtiquetas(List<Etiquetaexpedicao> listaEtiquetas) {
		this.listaEtiquetas = listaEtiquetas;
	}
	
	public void setListaEtiquetasCortadas(
			List<Etiquetaexpedicao> listaEtiquetasCortadas) {
		this.listaEtiquetasCortadas = listaEtiquetasCortadas;
	}

	public void setOrdemservicoUsuario(OrdemservicoUsuario ordemservicoUsuario) {
		this.ordemservicoUsuario = ordemservicoUsuario;
	}
	
}
