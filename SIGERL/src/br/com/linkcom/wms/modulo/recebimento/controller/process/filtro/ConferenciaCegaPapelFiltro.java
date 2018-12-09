package br.com.linkcom.wms.modulo.recebimento.controller.process.filtro;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Usuario;

public class ConferenciaCegaPapelFiltro {
	
	protected Recebimento recebimento;
	protected Set<Ordemprodutohistorico> listaOrdemProdutoHistorico = new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class);
	// tipo é Calendar
	protected Usuario pessoaPapel;
	protected Date dtinicio;
	protected Date dtfim;
	protected Hora hrinicio;
	protected Hora hrfim;
	protected Timestamp dthrinicio;
	protected Timestamp dthrfim;
	protected Ordemservico ordemservico;
	protected Boolean consultar;
	protected Boolean geraReconferencia;
	protected Long ultimaOs;
	protected Usuario usuariofinalizacao;
	protected Boolean finalizarSemConferencia;
	
	protected Boolean skipSaveOSU = false;
	
	@Required
	public Recebimento getRecebimento() {
		return recebimento;
	}
	
	public Set<Ordemprodutohistorico> getListaOrdemProdutoHistorico() {
		return listaOrdemProdutoHistorico;
	}
	
	@Required
	@DisplayName("Conferente")
	public Usuario getPessoaPapel() {
		return pessoaPapel;
	}
	
	@DisplayName("Data início")	
	public Date getDtinicio() {
		return dtinicio;
	}
	
	@DisplayName("Data fim")
	public Date getDtfim() {
		return dtfim;
	}
	
	@DisplayName("Hora início")
	public Hora getHrinicio() {
		return hrinicio;
	}
	
	@DisplayName("Hora fim")
	public Hora getHrfim() {
		return hrfim;
	}
	
	public Usuario getUsuariofinalizacao() {
		return usuariofinalizacao;
	}
	
	public Boolean getFinalizarSemConferencia() {
		return finalizarSemConferencia;
	}
	
	public Boolean getConsultar() {
		return consultar;
	}
	
	public Ordemservico getOrdemservico() {
		return ordemservico;
	}
	
	public Timestamp getDthrinicio() {
		return dthrinicio;
	}

	public Timestamp getDthrfim() {
		return dthrfim;
	}
	
	public Boolean getGeraReconferencia() {
		return geraReconferencia;
	}
	
	public Long getUltimaOs() {
		return ultimaOs;
	}
	
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	
	public void setListaOrdemProdutoHistorico(Set<Ordemprodutohistorico> listaOrdemProdutoHistorico) {
		this.listaOrdemProdutoHistorico = listaOrdemProdutoHistorico;
	}
	
	public void setPessoaPapel(Usuario pessoaPapel) {
		this.pessoaPapel = pessoaPapel;
	}

	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}

	public void setDtfim(Date dtfim) {
		this.dtfim = dtfim;
	}

	public void setHrinicio(Hora hrinicio) {
		this.hrinicio = hrinicio;
	}

	public void setHrfim(Hora hrfim) {
		this.hrfim = hrfim;
	}
	
	public void setDthrinicio(Timestamp dthrinicio) {
		this.dthrinicio = dthrinicio;
	}

	public void setDthrfim(Timestamp dthrfim) {
		this.dthrfim = dthrfim;
	}

	public void setOrdemservico(Ordemservico ordemservico) {
		this.ordemservico = ordemservico;
	}
	public void setConsultar(Boolean consultar) {
		this.consultar = consultar;
	}
	
	public void setSkipSaveOSU(Boolean skipSaveOSU) {
		this.skipSaveOSU = skipSaveOSU;
	}
	
	public Boolean getSkipSaveOSU() {
		return skipSaveOSU;
	}
	
	public void setGeraReconferencia(Boolean geraReconferencia) {
		this.geraReconferencia = geraReconferencia;
	}
	
	public void setUltimaOs(Long ultimaOs) {
		this.ultimaOs = ultimaOs;
	}
	
	public void setUsuariofinalizacao(Usuario usuariofinalizacao) {
		this.usuariofinalizacao = usuariofinalizacao;
	}
	
	public void setFinalizarSemConferencia(Boolean finalizarSemConferencia) {
		this.finalizarSemConferencia = finalizarSemConferencia;
	}
	
}
