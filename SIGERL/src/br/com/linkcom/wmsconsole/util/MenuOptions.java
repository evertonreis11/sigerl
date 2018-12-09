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
 * Arr com as opções do menu.
 * 
 * @author Pedro Gonçalves
 *
 */
public enum MenuOptions {
	MENU_PRINCIPAL("Retornar"),
	CONFERENCIA_RECEBIMENTO("Conferência recebimento",ConferenciaRecebimentoWindow.class, Ordemtipo.CONFERENCIA_RECEBIMENTO),
	RECONFERENCIA_RECEBIMENTO("Reconferência recebimento",ReconferenciaRecebimentoWindow.class, Ordemtipo.RECONFERENCIA_RECEBIMENTO),
	MAPA_SEPARACAO("Executar separação",MapaSeparacaoWindow.class, Ordemtipo.MAPA_SEPARACAO),
	CONFERENCIA_EXPEDICAO("Conferência expedição",ConferenciaExpedicaoWindow.class, Ordemtipo.CONFERENCIA_EXPEDICAO_1, Ordemtipo.CONFERENCIA_EXPEDICAO_2),
	RECONFERENCIA_EXPEDICAO("Reconferência expedição",ReconferenciaExpedicaoWindow.class, Ordemtipo.RECONFERENCIA_EXPEDICAO_1, Ordemtipo.RECONFERENCIA_EXPEDICAO_2),
	CONFERENCIA_CHECKOUT("Conferência de checkout",ConferenciaCheckoutWindow.class, Ordemtipo.CONFERENCIA_CHECKOUT),
	ENDERECAMENO_MANUAL("Endereçamento manual",EnderecamentoManualWindow.class, Ordemtipo.ENDERECAMENTO_PADRAO),
	ENDERECAMENO_AUTOMATICO("Endereçamento automático",EnderecamentoAutomaticoWindow.class, Ordemtipo.ENDERECAMENTO_PADRAO),
	EXECUTAR_TRANSFERENCIA("Executar transferência",TransferenciaWindow.class, Ordemtipo.TRANSFERENCIA),
	EXECUTAR_TRANSFERENCIA_MANUAL("Transferência manual",TransferenciaManualWindow.class, Ordemtipo.TRANSFERENCIA),
	EXECUTAR_REABASTECIMENTO("Executar reabastecimento",ReabastecimentoWindow.class, Ordemtipo.REABASTECIMENTO_PICKING),
	EXECUTAR_CONTAGEM("Contagem de inventário",ContagemInventarioWindow.class, Ordemtipo.CONTAGEM_INVENTARIO),
	EXECUTAR_RECONTAGEM("Recontagem de inventário",RecontagemInventarioWindow.class, Ordemtipo.RECONTAGEM_INVENTARIO),
	LIBERACAO_VEICULO("Registrar Entrada/Saída de Veículo",AcompanhamentoVeiculoWindow.class, Ordemtipo.ACOMPANHAMENTO_VEICULO),
	LIBERAR_MANIFESTO("Liberação de Saida (Manifesto)", ManifestoEntregaWindow.class, Ordemtipo.MANIFESTO),
	ENTRADA_MANIFESTO("Liberação de Entrada (Manifesto)", ManifestoEntradaWindow.class, Ordemtipo.MANIFESTO);	
	
	
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
