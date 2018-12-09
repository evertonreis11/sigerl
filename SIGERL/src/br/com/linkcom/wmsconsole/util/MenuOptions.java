package br.com.linkcom.wmsconsole.util;

import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wmsconsole.system.TelnetWindow;
import br.com.linkcom.wmsconsole.window.AcompanhamentoVeiculoWindow;
import br.com.linkcom.wmsconsole.window.ConferenciaCheckoutWindow;
import br.com.linkcom.wmsconsole.window.ConferenciaExpedicaoWindow;
import br.com.linkcom.wmsconsole.window.ConferenciaRecebimentoWindow;
import br.com.linkcom.wmsconsole.window.ContagemInventarioWindow;
import br.com.linkcom.wmsconsole.window.EnderecamentoAutomaticoWindow;
import br.com.linkcom.wmsconsole.window.EnderecamentoManualWindow;
import br.com.linkcom.wmsconsole.window.ManifestoEntradaWindow;
import br.com.linkcom.wmsconsole.window.ManifestoEntregaWindow;
import br.com.linkcom.wmsconsole.window.MapaSeparacaoWindow;
import br.com.linkcom.wmsconsole.window.ReabastecimentoWindow;
import br.com.linkcom.wmsconsole.window.ReconferenciaExpedicaoWindow;
import br.com.linkcom.wmsconsole.window.ReconferenciaRecebimentoWindow;
import br.com.linkcom.wmsconsole.window.RecontagemInventarioWindow;
import br.com.linkcom.wmsconsole.window.TransferenciaManualWindow;
import br.com.linkcom.wmsconsole.window.TransferenciaWindow;

/**
 * Arr com as op��es do menu.
 * 
 * @author Pedro Gon�alves
 *
 */
public enum MenuOptions {
	MENU_PRINCIPAL("Retornar"),
	CONFERENCIA_RECEBIMENTO("Confer�ncia recebimento",ConferenciaRecebimentoWindow.class, Ordemtipo.CONFERENCIA_RECEBIMENTO),
	RECONFERENCIA_RECEBIMENTO("Reconfer�ncia recebimento",ReconferenciaRecebimentoWindow.class, Ordemtipo.RECONFERENCIA_RECEBIMENTO),
	MAPA_SEPARACAO("Executar separa��o",MapaSeparacaoWindow.class, Ordemtipo.MAPA_SEPARACAO),
	CONFERENCIA_EXPEDICAO("Confer�ncia expedi��o",ConferenciaExpedicaoWindow.class, Ordemtipo.CONFERENCIA_EXPEDICAO_1, Ordemtipo.CONFERENCIA_EXPEDICAO_2),
	RECONFERENCIA_EXPEDICAO("Reconfer�ncia expedi��o",ReconferenciaExpedicaoWindow.class, Ordemtipo.RECONFERENCIA_EXPEDICAO_1, Ordemtipo.RECONFERENCIA_EXPEDICAO_2),
	CONFERENCIA_CHECKOUT("Confer�ncia de checkout",ConferenciaCheckoutWindow.class, Ordemtipo.CONFERENCIA_CHECKOUT),
	ENDERECAMENO_MANUAL("Endere�amento manual",EnderecamentoManualWindow.class, Ordemtipo.ENDERECAMENTO_PADRAO),
	ENDERECAMENO_AUTOMATICO("Endere�amento autom�tico",EnderecamentoAutomaticoWindow.class, Ordemtipo.ENDERECAMENTO_PADRAO),
	EXECUTAR_TRANSFERENCIA("Executar transfer�ncia",TransferenciaWindow.class, Ordemtipo.TRANSFERENCIA),
	EXECUTAR_TRANSFERENCIA_MANUAL("Transfer�ncia manual",TransferenciaManualWindow.class, Ordemtipo.TRANSFERENCIA),
	EXECUTAR_REABASTECIMENTO("Executar reabastecimento",ReabastecimentoWindow.class, Ordemtipo.REABASTECIMENTO_PICKING),
	EXECUTAR_CONTAGEM("Contagem de invent�rio",ContagemInventarioWindow.class, Ordemtipo.CONTAGEM_INVENTARIO),
	EXECUTAR_RECONTAGEM("Recontagem de invent�rio",RecontagemInventarioWindow.class, Ordemtipo.RECONTAGEM_INVENTARIO),
	LIBERACAO_VEICULO("Registrar Entrada/Sa�da de Ve�culo",AcompanhamentoVeiculoWindow.class, Ordemtipo.ACOMPANHAMENTO_VEICULO),
	LIBERAR_MANIFESTO("Libera��o de Saida (Manifesto)", ManifestoEntregaWindow.class, Ordemtipo.MANIFESTO),
	ENTRADA_MANIFESTO("Libera��o de Entrada (Manifesto)", ManifestoEntradaWindow.class, Ordemtipo.MANIFESTO);	
	
	
	private String descricao;
	private Class<? extends TelnetWindow> loadWindow;
	private Ordemtipo[] tipos;
	
	MenuOptions(String descricao,Class<? extends TelnetWindow> loadWindow, Ordemtipo... tipos){
		this.descricao = descricao;
		this.loadWindow = loadWindow;
		this.tipos = tipos;
	}
	
	MenuOptions(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public Class<? extends TelnetWindow> getLoadWindow(){
		return this.loadWindow;
	}
	
	public Ordemtipo[] getTipos() {
		return tipos;
	}
	
}	
