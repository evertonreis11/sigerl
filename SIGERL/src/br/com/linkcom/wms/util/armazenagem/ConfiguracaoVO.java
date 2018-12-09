package br.com.linkcom.wms.util.armazenagem;

import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Tipooperacao;

/**
 * Classe intermediária utilizada para salvar a configuração
 * 
 * @author Leornardo Guimarães
 */
public class ConfiguracaoVO {

	public static final String PERCENTUAL_RETENCAO_DESCAGA = "PERCENTUAL_RETENCAO_DESCARGA";
	public static final String SEPARAR_CLIENTEFILIAL_LOJA = "SEPARACAO_CLIENTE_LOJA";
	public static final String SEPARAR_CLIENTEFILIAL_MOSTRUARIO = "SEPARACAO_CLIENTE_MOSTRUARIO";
	public static final String SEPARAR_CLIENTEFILIAL_CLIENTE = "SEPARACAO_CLIENTE_CLIENTE";
	public static final String TIPO_SEPARACAO_LOJA = "TIPO_SEPARACAO_LOJA";
	public static final String TIPO_SEPARACAO_MOSTRUARIO = "TIPO_SEPARACAO_MOSTRUARIO";
	public static final String TIPO_SEPARACAO_CLIENTE = "TIPO_SEPARACAO_CLIENTE";
	public static final String LINHA_SEPARACAO_PADRAO = "LINHA_SEPARACAO_PADRAO";
	public static final String COLETOR_EXIGE_CODIGOBARRAS = "COLETOR_EXIGE_CODIGOBARRAS";
	public static final String PORTAPALETE_EXIGE_PICKING = "PORTAPALETE_EXIGE_PICKING";
	public static final String OPERACAO_CD_CD_DUPLICA_PEDIDO = "OPERACAO_CD_CD_DUPLICA_PEDIDO";
	public static final String ENVIAR_RECEBIMENTO_ENDERECADO = "ENVIAR_RECEBIMENTO_ENDERECADO";
	public static final String MAX_ENDERECOS_CRIADOS_POR_VEZ = "MAX_ENDERECOS_CRIADOS_POR_VEZ";
	public static final String SEPARACAO_VIA_COLETOR = "SEPARACAO_VIA_COLETOR";
	public static final String ENDERECAMENTO_AUTOMATICO = "ENDERECAMENTO_AUTOMATICO";
	public static final String MAPA_IGNORA_LINHA = "MAPA_IGNORA_LINHA";
	public static final String CONVOCACAO_ATIVA_COLETOR = "CONVOCACAO_ATIVA_COLETOR";
	public static final String VALIDAR_VERBA_AGENDAMENTO = "VALIDAR_VERBA_AGENDAMENTO";
	public static final String CURVA_ABC_NIVEL_A = "CURVA_ABC_NIVEL_A";
	public static final String CURVA_ABC_NIVEL_B = "CURVA_ABC_NIVEL_B";
	public static final String CURVA_ABC_NIVEL_C = "CURVA_ABC_NIVEL_C";
	public static final String FILTRAR_POR_DEPOSITO = "FILTRAR_POR_DEPOSITO";
	public static final String FINALIZADO_BAIXA_ESTOQUE = "FINALIZADO_BAIXA_ESTOQUE";
	public static final String EXIGIR_SEGUNDA_CONFERENCIA = "EXIGIR_SEGUNDA_CONFERENCIA";
	public static final String QUEBRAR_POR_CARREGAMENTO  = "QUEBRAR_POR_CARREGAMENTO";
	public static final String OPERACAO_EXPEDICAO_POR_BOX  = "OPERACAO_EXPEDICAO_POR_BOX";
	public static final String UTILIZAR_CAIXA_MESTRE = "UTILIZAR_CAIXA_MESTRE";
	
	public static final String DATA_ULTIMO_ACESSO = "DATA_ULTIMO_ACESSO";
	public static final String NUM_DIAS_AGENDA_CANCELAMENTO = "NUM_DIAS_AGENDA_CANCELAMENTO";
	public static final String VALIDACAO_VERBA_DEPOSITO = "VALIDACAO_VERBA_DEPOSITO";
	public static final String VALIDACAO_VERBA_LIBERACAO_RECEBIMENTO = "VALID_VERBA_LIB_RECEBIMENTO";
	public static final String VALIDACAO_VERBA_LIBERACAO_FINANCEIRA = "VALID_VERBA_LIBO_FINANCEIRA";
	public static final String VALIDACAO_VERBA_AGENDAMENTO_RECEBIMENTO = "VALID_VERBA_AGEND_RECEBIMENTO";
	public static final String VALIDACAO_VERBA_AGENDAMENTO_FINANCEIRA = "VALID_VERBA_AGEND_FINANCEIRA";

	public static final String VALIDAR_PEDIDOS_AGENDAMENTO = "VALIDAR_PEDIDOS_AGENDAMENTO";
	public static final String VALIDAR_PRODUTOS_AGENDAMENTO = "VALIDAR_PRODUTOS_AGENDAMENTO";

	public static final String EXIGIR_DUAS_CONTAGENS_INVENTARIO = "EXIGIR_DUAS_CONT_INVENTARIO";
	public static final String BLOQUEAR_MOVIMENTACAO_AREA_BOX = "BLOQUEAR_MOVIMENTACAO_AREA_BOX";
	public static final String BLOQUEAR_SAIDA_AREA_VIRTUAL = "BLOQUEAR_SAIDA_AREA_VIRTUAL";
	public static final String EXIGIR_ESTOQUE_PARA_SEPARACAO = "EXIGIR_ESTOQUE_PARA_SEPARACAO";
	public static final String SEGUNDANOTA_CDCLIENTE_ENTREGA = "SEGUNDANOTA_CDCLIENTE_ENTREGA";
	public static final String BORDERO_RATEIRO_EMPRESA = "BORDERO_RATEIRO_EMPRESA";
	
	public static final String COLETAR_APENAS_COMO_FRACIONADA = "COLETAR_APENAS_COMO_FRACIONADA";
	/**
 	 * Este parâmetro é salvo no formato cd1,cd2,cd3
	 */
	public static final String ETIQUETA_SEPARACAO = "ETIQUETA_SEPARACAO";
	
	public static final String WMSDL = "integrador.wsdl";
	public static final String SENHA_INTEGRADOR = "integrador.senha";
	public static final String EMPRESA = "integrador.empresa";
	public static final String PAUSA = "integrador.pausa";
	public static final String REMETENTE_EMAIL_WMS = "REMETENTE_EMAIL_WMS";
	
	public static final String NUM_DIAS_RAV_CANCELAMENTO = "NUM_DIAS_RAV_CANCELAMENTO";
	public static final String COLETA_QUANT_AUTO_RECEB = "COLETA_QUANT_AUTO_RECEB";


	protected Double percentualDescarga = 15.0;
	protected Boolean separarPorClienteMostruario = Boolean.FALSE;
	protected Boolean separarPorClienteLoja = Boolean.FALSE;
	protected Boolean separarPorClienteCliente = Boolean.FALSE;
	protected Boolean tipoSeparacaoMostruario = Boolean.TRUE;
	protected Boolean tipoSeparacaoLoja = Boolean.TRUE;
	protected Boolean tipoSeparacaoCliente = Boolean.TRUE;
	protected Long maxEnderecos;
	protected List<Tipooperacao> listaEtiquetaEspedicao;
	protected String wmsdl;
	protected String senhaIntegrador;
	protected String empresa;
	protected String pausa;
	protected Linhaseparacao linhaseparacaoPadrao;
	protected Boolean coletorExigeCodigoBarras = Boolean.FALSE;
	protected Boolean operacaoCdCdDuplicaPedido = Boolean.FALSE;
	protected Boolean portapaleteExigePicking = Boolean.FALSE;
	protected Boolean enviarRecebimentosEnderecados = Boolean.FALSE;
	protected Boolean enderecamentoAutomatico = Boolean.TRUE;
	protected Boolean exigirSegundaConferencia = Boolean.FALSE;
	protected Boolean quebrarCarregamento = Boolean.FALSE;
	protected Boolean operacaoExpedicaoBox = Boolean.FALSE;
	protected Boolean bloquearMovimentacoesAreaBox = Boolean.FALSE;
	protected Boolean bloquearMovimentacoesAreaVirtual = Boolean.FALSE;
	protected Boolean exigirEstoqueParaSeparacao = Boolean.FALSE;
	protected Boolean coletarApenasComoFracionada = Boolean.FALSE;
	protected Boolean utilizarCaixaMestre = Boolean.FALSE;
	protected Boolean segundanotaCdclienteEntrega = Boolean.FALSE;
	protected Boolean borderoRateioEmpresa = Boolean.FALSE;

	@MaxLength(6)
	@DisplayName("Percentual de retenção de descarga")
	public Double getPercentualDescarga() {
		return percentualDescarga;
	}

	@DisplayName("Separar por cliente/Filial")
	public Boolean getSepararPorClienteCliente() {
		return separarPorClienteCliente;
	}

	@DisplayName("Separar por cliente/Filial")
	public Boolean getSepararPorClienteLoja() {
		return separarPorClienteLoja;
	}

	@DisplayName("Separar por cliente/Filial")
	public Boolean getSepararPorClienteMostruario() {
		return separarPorClienteMostruario;
	}

	@DisplayName("Tipo de separação")
	public Boolean getTipoSeparacaoCliente() {
		return tipoSeparacaoCliente;
	}

	@DisplayName("Tipo de separação")
	public Boolean getTipoSeparacaoLoja() {
		return tipoSeparacaoLoja;
	}

	@DisplayName("Tipo de separação")
	public Boolean getTipoSeparacaoMostruario() {
		return tipoSeparacaoMostruario;
	}

	@DisplayName("Número máximo de endereços criados por vez")
	@MaxLength(11)
	public Long getMaxEnderecos() {
		return maxEnderecos;
	}

	@DisplayName("Etiqueta de separação")
	public List<Tipooperacao> getListaEtiquetaEspedicao() {
		return listaEtiquetaEspedicao;
	}

	@DisplayName("Linha de separação padrão")
	public Linhaseparacao getLinhaseparacaoPadrao() {
		return linhaseparacaoPadrao;
	}

	public Boolean getColetorExigeCodigoBarras() {
		return coletorExigeCodigoBarras;
	}

	public String getWmsdl() {
		return wmsdl;
	}

	public String getSenhaIntegrador() {
		return senhaIntegrador;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getPausa() {
		return pausa;
	}

	@DisplayName("Endereçamento Automático")
	public Boolean getEnderecamentoAutomatico() {
		return enderecamentoAutomatico;
	}
	
	@DisplayName("Exigir execução de uma segunda conferência (conferência de box) na expedição.")
	public Boolean getExigirSegundaConferencia() {
		return exigirSegundaConferencia;
	}
	
	@DisplayName("Quebrar carregamento.")
	public Boolean getQuebrarCarregamento() {
		return quebrarCarregamento;
	}
	
	@DisplayName("Operação de expedição por box")
	public Boolean getOperacaoExpedicaoBox() {
		return operacaoExpedicaoBox;
	}
	
	@DisplayName("Bloquear movimentações de estoque da área de box")
	public Boolean getBloquearMovimentacoesAreaBox() {
		return bloquearMovimentacoesAreaBox;
	}
	
	@DisplayName("Bloquear movimentações de estoque da área virtual")
	public Boolean getBloquearMovimentacoesAreaVirtual() {
		return bloquearMovimentacoesAreaVirtual;
	}
	
	@DisplayName("Somente gerar separação quando houver estoque suficiente")
	public Boolean getExigirEstoqueParaSeparacao() {
		return exigirEstoqueParaSeparacao;
	}
	
	@DisplayName("Coletar apenas como fracionada")
	public Boolean getColetarApenasComoFracionada() {
		return coletarApenasComoFracionada;
	}
	
	@DisplayName("Utilizar Caixa Mestre")
	public Boolean getUtilizarCaixaMestre() {
		return utilizarCaixaMestre;
	}
	
	public Boolean getSegundanotaCdclienteEntrega() {
		return segundanotaCdclienteEntrega;
	}
	
	public Boolean getBorderoRateioEmpresa() {
		return borderoRateioEmpresa;
	}

	
	
	public void setUtilizarCaixaMestre(Boolean utilizarCaixaMestre) {
		this.utilizarCaixaMestre = utilizarCaixaMestre;
	}

	public void setColetarApenasComoFracionada(Boolean coletarApenasComoFracionada) {
		this.coletarApenasComoFracionada = coletarApenasComoFracionada;
	}

	public void setQuebrarCarregamento(Boolean quebrarCarregamento) {
		this.quebrarCarregamento = quebrarCarregamento;
	}
	
	public void setPercentualDescarga(Double percentualDescarga) {
		this.percentualDescarga = percentualDescarga;
	}

	public void setSepararPorClienteCliente(Boolean separarPorClienteCliente) {
		this.separarPorClienteCliente = separarPorClienteCliente;
	}

	public void setSepararPorClienteLoja(Boolean separarPorClienteLoja) {
		this.separarPorClienteLoja = separarPorClienteLoja;
	}

	public void setSepararPorClienteMostruario(Boolean separarPorClienteMostruario) {
		this.separarPorClienteMostruario = separarPorClienteMostruario;
	}

	public void setTipoSeparacaoCliente(Boolean tipoSeparacaoCliente) {
		this.tipoSeparacaoCliente = tipoSeparacaoCliente;
	}

	public void setTipoSeparacaoLoja(Boolean tipoSeparacaoLoja) {
		this.tipoSeparacaoLoja = tipoSeparacaoLoja;
	}

	public void setTipoSeparacaoMostruario(Boolean tipoSeparacaoMostruario) {
		this.tipoSeparacaoMostruario = tipoSeparacaoMostruario;
	}

	public void setMaxEnderecos(Long maxEnderecos) {
		this.maxEnderecos = maxEnderecos;
	}

	public void setListaEtiquetaEspedicao(List<Tipooperacao> listaEtiquetaEspedicao) {
		this.listaEtiquetaEspedicao = listaEtiquetaEspedicao;
	}

	public void setLinhaseparacaoPadrao(Linhaseparacao linhaseparacaoPadrao) {
		this.linhaseparacaoPadrao = linhaseparacaoPadrao;
	}

	public void setColetorExigeCodigoBarras(Boolean coletorExigeCodigoBarras) {
		this.coletorExigeCodigoBarras = coletorExigeCodigoBarras;
	}
	
	public void setOperacaoExpedicaoBox(Boolean operacaoExpedicaoBox) {
		this.operacaoExpedicaoBox = operacaoExpedicaoBox;
	}

	public void setBloquearMovimentacoesAreaBox(Boolean permitirMovimentarAreaBox) {
		this.bloquearMovimentacoesAreaBox = permitirMovimentarAreaBox;
	}
	
	public void setBloquearMovimentacoesAreaVirtual(Boolean permitirMovimentarAreaVirtual) {
		this.bloquearMovimentacoesAreaVirtual = permitirMovimentarAreaVirtual;
	}
	
	public void setExigirEstoqueParaSeparacao(Boolean exigirEstoqueParaSeparacao) {
		this.exigirEstoqueParaSeparacao = exigirEstoqueParaSeparacao;
	}
	
	public void setWmsdl(String wmsdl) {
		this.wmsdl = wmsdl;
	}

	public void setSenhaIntegrador(String senhaIntegrador) {
		this.senhaIntegrador = senhaIntegrador;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public void setPausa(String pausa) {
		this.pausa = pausa;
	}

	public Boolean getOperacaoCdCdDuplicaPedido() {
		return operacaoCdCdDuplicaPedido;
	}

	public Boolean getPortapaleteExigePicking() {
		return portapaleteExigePicking;
	}

	public void setOperacaoCdCdDuplicaPedido(Boolean operacaoCdCdDuplicaPedido) {
		this.operacaoCdCdDuplicaPedido = operacaoCdCdDuplicaPedido;
	}

	public void setPortapaleteExigePicking(Boolean portapaleteExigePicking) {
		this.portapaleteExigePicking = portapaleteExigePicking;
	}

	public Boolean getEnviarRecebimentosEnderecados() {
		return enviarRecebimentosEnderecados;
	}

	public void setEnviarRecebimentosEnderecados(Boolean enviarRecebimentosEnderecados) {
		this.enviarRecebimentosEnderecados = enviarRecebimentosEnderecados;
	}

	public void setEnderecamentoAutomatico(Boolean enderecamentoAutomatico) {
		this.enderecamentoAutomatico = enderecamentoAutomatico;
	}
	
	public void setExigirSegundaConferencia(Boolean exigirSegundaConferencia) {
		this.exigirSegundaConferencia = exigirSegundaConferencia;
	}

	public void setSegundanotaCdclienteEntrega(Boolean segundanotaCdclienteEntrega) {
		this.segundanotaCdclienteEntrega = segundanotaCdclienteEntrega;
	}

	public void setBorderoRateioEmpresa(Boolean borderoRateioEmpresa) {
		this.borderoRateioEmpresa = borderoRateioEmpresa;
	}
	
}
