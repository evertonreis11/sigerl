package br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro;

import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.ricardoeletro.coletor.geral.filtro.ColetorFiltro;

public class ConferenciaRecebimentoFiltro extends ColetorFiltro {
	
	public enum TipoColeta { PADRAO, FRACIONADA, AVARIA};
	
	private Recebimento recebimento;
	private String veiculo;
	private Ordemservico ordemServico;
	private OrdemservicoUsuario ordemservicoUsuario;
	private Ordemprodutohistorico ordemprodutohistorico;
	private TipoColeta tipoColeta = TipoColeta.FRACIONADA;
	private Boolean iniciarColeta;
	private String codigoBarrasConferencia;
	
	public Recebimento getRecebimento() {
		return recebimento;
	}
	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}
	public String getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}
	public Ordemservico getOrdemServico() {
		return ordemServico;
	}
	public void setOrdemServico(Ordemservico ordemServico) {
		this.ordemServico = ordemServico;
	}
	public OrdemservicoUsuario getOrdemservicoUsuario() {
		return ordemservicoUsuario;
	}
	public void setOrdemservicoUsuario(OrdemservicoUsuario ordemservicoUsuario) {
		this.ordemservicoUsuario = ordemservicoUsuario;
	}
	public TipoColeta getTipoColeta() {
		return tipoColeta;
	}
	public void setTipoColeta(TipoColeta tipoColeta) {
		this.tipoColeta = tipoColeta;
	}
	public Boolean getIniciarColeta() {
		return iniciarColeta;
	}
	public void setIniciarColeta(Boolean iniciarColeta) {
		this.iniciarColeta = iniciarColeta;
	}
	public Ordemprodutohistorico getOrdemprodutohistorico() {
		return ordemprodutohistorico;
	}
	public void setOrdemprodutohistorico(Ordemprodutohistorico ordemprodutohistorico) {
		this.ordemprodutohistorico = ordemprodutohistorico;
	}
	public String getCodigoBarrasConferencia() {
		return codigoBarrasConferencia;
	}
	public void setCodigoBarrasConferencia(String codigoBarrasConferencia) {
		this.codigoBarrasConferencia = codigoBarrasConferencia;
	}

}
