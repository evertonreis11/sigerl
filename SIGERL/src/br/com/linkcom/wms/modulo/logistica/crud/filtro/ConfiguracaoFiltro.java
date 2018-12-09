package br.com.linkcom.wms.modulo.logistica.crud.filtro;

import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

public class ConfiguracaoFiltro extends FiltroListagem {
	
	private Deposito deposito;
	private List<Tipooperacao> listaOperacaoSeparacliente;
	private List<Tipooperacao> listaOperacaoImprimeetiqueta;
	private ConfiguracaoVO configuracaoVO = new ConfiguracaoVO();
	private Boolean separacaoViaColetor = Boolean.FALSE;
	private Boolean mapaIgnoraLinha;
	private Boolean convocacaoAtivaColetor;
	private List<Notafiscaltipo> listaExigeAgendamento;
	private Integer numeroDiasCancelamentoAgenda;
//	private Boolean validarVerbaAgendamento;
	private Boolean validarVerbaDeposito;
	private Boolean validarVerbaLiberacaoRecebimento;
	private Boolean validarVerbaLiberacaoFinanceira;
	private Boolean validarVerbaAgendamentoRecebimento;
	private Boolean validarVerbaAgendamentoFinanceira;
	private Boolean validarPedidosAgendamento;
	private Boolean validarProdutosAgendamento;
	private Boolean exigirDuasContagensInventario;
	private Boolean coletarApenasComoFracionada;
	private Boolean utilizarCaixaMestre;
	
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	public ConfiguracaoVO getConfiguracaoVO() {
		return configuracaoVO;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setConfiguracaoVO(ConfiguracaoVO configuracaoVO) {
		this.configuracaoVO = configuracaoVO;
	}
	
	@DisplayName("O mapa de separação não deve quebrar por linha de separação")
	public Boolean getMapaIgnoraLinha() {
		return mapaIgnoraLinha;
	}

	public List<Tipooperacao> getListaOperacaoSeparacliente() {
		return listaOperacaoSeparacliente;
	}

	public void setListaOperacaoSeparacliente(
			List<Tipooperacao> listaOperacaoSeparacliente) {
		this.listaOperacaoSeparacliente = listaOperacaoSeparacliente;
	}

	public List<Tipooperacao> getListaOperacaoImprimeetiqueta() {
		return listaOperacaoImprimeetiqueta;
	}

	public void setListaOperacaoImprimeetiqueta(
			List<Tipooperacao> listaOperacaoImprimeetiqueta) {
		this.listaOperacaoImprimeetiqueta = listaOperacaoImprimeetiqueta;
	}

	public void setMapaIgnoraLinha(Boolean mapaIgnoraLinha) {
		this.mapaIgnoraLinha = mapaIgnoraLinha;
	}

	public Boolean getConvocacaoAtivaColetor() {
		return convocacaoAtivaColetor;
	}

	public void setConvocacaoAtivaColetor(Boolean convocacaoAtivaColetor) {
		this.convocacaoAtivaColetor = convocacaoAtivaColetor;
	}
	
	@DisplayName("Executar separação via RF")
	public Boolean getSeparacaoViaColetor() {
		return separacaoViaColetor;
	}

	public void setSeparacaoViaColetor(Boolean separacaoViaColetor) {
		this.separacaoViaColetor = separacaoViaColetor;
	}
	
	public List<Notafiscaltipo> getListaExigeAgendamento() {
		return listaExigeAgendamento;
	}
	
	public void setListaExigeAgendamento(
			List<Notafiscaltipo> listaExigeAgendamento) {
		this.listaExigeAgendamento = listaExigeAgendamento;
	}

	@Required
	@MaxLength(8)
	@DisplayName("Número de dias para o cancelamento automático dos agendamentos não finalizados")
	public Integer getNumeroDiasCancelamentoAgenda() {
		return numeroDiasCancelamentoAgenda;
	}

	public void setNumeroDiasCancelamentoAgenda(Integer numeroDiasCancelamentoAgenda) {
		this.numeroDiasCancelamentoAgenda = numeroDiasCancelamentoAgenda;
	}
	
/*	@DisplayName("Validação de verba na data do agendamento")
	public Boolean getValidarVerbaAgendamento() {
		return validarVerbaAgendamento;
	}*/
	
	@DisplayName("Validação de verba por depósito")
	public Boolean getValidarVerbaDeposito() {
		return validarVerbaDeposito;
	}
	
	@DisplayName("Validação de verba de recebimento na liberação")
	public Boolean getValidarVerbaLiberacaoRecebimento() {
		return validarVerbaLiberacaoRecebimento;
	}

	@DisplayName("Validação de verba financeira na liberação")
	public Boolean getValidarVerbaLiberacaoFinanceira() {
		return validarVerbaLiberacaoFinanceira;
	}

	@DisplayName("Validação de verba de recebimento no agendamento")
	public Boolean getValidarVerbaAgendamentoRecebimento() {
		return validarVerbaAgendamentoRecebimento;
	}

	@DisplayName("Validação de verba financeira no agendamento")
	public Boolean getValidarVerbaAgendamentoFinanceira() {
		return validarVerbaAgendamentoFinanceira;
	}
	
	@DisplayName("Validar pedidos do agendamento")
	public Boolean getValidarPedidosAgendamento() {
		return validarPedidosAgendamento;
	}
	
	@DisplayName("Validar produtos do agendamento")
	public Boolean getValidarProdutosAgendamento() {
		return validarProdutosAgendamento;
	}

	@DisplayName("Exigir 2 contagens no inventário")
	public Boolean getExigirDuasContagensInventario() {
		return exigirDuasContagensInventario;
	}
	
	@DisplayName("Coletar apenas como fracionada")
	public Boolean getColetarApenasComoFracionada() {
		return coletarApenasComoFracionada;
	}
	
	@DisplayName("Utilizar Caixa Mestre")
	public Boolean getUtilizarCaixaMestre() {
		return utilizarCaixaMestre;
	}

	public void setUtilizarCaixaMestre(Boolean utilizarCaixaMestre) {
		this.utilizarCaixaMestre = utilizarCaixaMestre;
	}

	public void setColetarApenasComoFracionada(Boolean coletarApenasComoFracionada) {
		this.coletarApenasComoFracionada = coletarApenasComoFracionada;
	}

	public void setExigirDuasContagensInventario(
			Boolean exigirDuasContagensInventario) {
		this.exigirDuasContagensInventario = exigirDuasContagensInventario;
	}
	
	public void setValidarVerbaLiberacaoRecebimento(
			Boolean validarVerbaLiberacaoRecebimento) {
		this.validarVerbaLiberacaoRecebimento = validarVerbaLiberacaoRecebimento;
	}

	public void setValidarVerbaLiberacaoFinanceira(Boolean validarVerbaLiberacaoFinanceira) {
		this.validarVerbaLiberacaoFinanceira = validarVerbaLiberacaoFinanceira;
	}

	public void setValidarVerbaAgendamentoRecebimento(
			Boolean validarVerbaAgendamentoRecebimento) {
		this.validarVerbaAgendamentoRecebimento = validarVerbaAgendamentoRecebimento;
	}

	public void setValidarVerbaAgendamentoFinanceira(
			Boolean validarVerbaAgendamentoFinanceira) {
		this.validarVerbaAgendamentoFinanceira = validarVerbaAgendamentoFinanceira;
	}

	public void setValidarVerbaDeposito(Boolean validarVerbaDeposito) {
		this.validarVerbaDeposito = validarVerbaDeposito;
	}
	
	public void setValidarPedidosAgendamento(Boolean validarPedidosAgendamento) {
		this.validarPedidosAgendamento = validarPedidosAgendamento;
	}
	
	public void setValidarProdutosAgendamento(Boolean validarProdutosAgendamento) {
		this.validarProdutosAgendamento = validarProdutosAgendamento;
	}

/*	public void setValidarVerbaAgendamento(Boolean validarVerbaAgendamento) {
		this.validarVerbaAgendamento = validarVerbaAgendamento;
	}*/
	
}
