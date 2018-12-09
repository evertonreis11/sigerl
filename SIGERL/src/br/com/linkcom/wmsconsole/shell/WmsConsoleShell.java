package br.com.linkcom.wmsconsole.shell;

import java.io.IOException;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.shell.Shell;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;
import br.com.linkcom.wmsconsole.system.TelnetWindow;
import br.com.linkcom.wmsconsole.system.WmsConsoleBase;
import br.com.linkcom.wmsconsole.util.MenuOptions;
import br.com.linkcom.wmsconsole.window.ConferenciaExpedicaoWindow;
import br.com.linkcom.wmsconsole.window.ConferenciaRecebimentoWindow;
import br.com.linkcom.wmsconsole.window.ConsultaProdutoWindow;
import br.com.linkcom.wmsconsole.window.ContagemInventarioWindow;
import br.com.linkcom.wmsconsole.window.EnderecamentoAutomaticoWindow;
import br.com.linkcom.wmsconsole.window.EnderecamentoManualWindow;
import br.com.linkcom.wmsconsole.window.LoginWindow;
import br.com.linkcom.wmsconsole.window.MapaSeparacaoWindow;
import br.com.linkcom.wmsconsole.window.MenuPrincipalWindow;
import br.com.linkcom.wmsconsole.window.MenuWindow;
import br.com.linkcom.wmsconsole.window.ReabastecimentoWindow;
import br.com.linkcom.wmsconsole.window.ReconferenciaExpedicaoWindow;
import br.com.linkcom.wmsconsole.window.ReconferenciaRecebimentoWindow;
import br.com.linkcom.wmsconsole.window.RecontagemInventarioWindow;
import br.com.linkcom.wmsconsole.window.TransferenciaWindow;

public class WmsConsoleShell extends WmsConsoleBase{

	@Override
	public void execute() throws Exception {
		termIO.resetAttributes();
		termIO.setAutoflushing(true);
		do {
			//executa o a tela de autenticação caso o usuário ainda não foi definido.
			if (this.usuario == null || this.deposito == null) {
				doLogin();
			}
			termIO.eraseScreen();
			
			showMenuPrincipal();
			
		} while (true);
	}

	private void executarConvocacaoAtiva() throws IOException {
		Ordemservico proximaOrdem = OrdemservicoService.getInstance().associarProximaOrdem(deposito, usuario);
		
		termIO.setCursor(0, 0);
		
		if (proximaOrdem == null || proximaOrdem.getCdordemservico() == null || proximaOrdem.getCdordemservico() <= 0){
			
			String msg = "Não existem ordens de serviço disponíveis.";
			
			TelnetWindow.writeOnCenter(termIO, msg, ColorHelper.RED, true, true);
			
			//Forçando um logout
			this.usuario = null;
			this.deposito = null;
			
			return;
		}
		
		proximaOrdem = OrdemservicoService.getInstance().loadOrdemAndOrigem(proximaOrdem);

		ExecucaoOSWindow window = null;
		
		if (Ordemtipo.CONFERENCIA_EXPEDICAO_1.equals(proximaOrdem.getOrdemtipo()))
			window = new ConferenciaExpedicaoWindow();
		else if (Ordemtipo.RECONFERENCIA_EXPEDICAO_1.equals(proximaOrdem.getOrdemtipo()))
			window = new ReconferenciaExpedicaoWindow();
		else if (Ordemtipo.MAPA_SEPARACAO.equals(proximaOrdem.getOrdemtipo()))
			window = new MapaSeparacaoWindow();
		else if (Ordemtipo.CONFERENCIA_RECEBIMENTO.equals(proximaOrdem.getOrdemtipo()))
			window = new ConferenciaRecebimentoWindow();
		else if (Ordemtipo.RECONFERENCIA_RECEBIMENTO.equals(proximaOrdem.getOrdemtipo()))
			window = new ReconferenciaRecebimentoWindow();
		else if (Ordemtipo.REABASTECIMENTO_PICKING.equals(proximaOrdem.getOrdemtipo()))
			window = new ReabastecimentoWindow();
		else if (Ordemtipo.TRANSFERENCIA.equals(proximaOrdem.getOrdemtipo()))
			window = new TransferenciaWindow();
		else if ((Ordemtipo.ENDERECAMENTO_AVARIADO.equals(proximaOrdem.getOrdemtipo()) || 
				Ordemtipo.ENDERECAMENTO_FRACIONADO.equals(proximaOrdem.getOrdemtipo()) ||
				Ordemtipo.ENDERECAMENTO_PADRAO.equals(proximaOrdem.getOrdemtipo())) &&
				Tipoenderecamento.AUTOMATICO.equals(proximaOrdem.getRecebimento().getTipoenderecamento()))
			window = new EnderecamentoAutomaticoWindow();
		else if ((Ordemtipo.ENDERECAMENTO_AVARIADO.equals(proximaOrdem.getOrdemtipo()) || 
				Ordemtipo.ENDERECAMENTO_FRACIONADO.equals(proximaOrdem.getOrdemtipo()) ||
				Ordemtipo.ENDERECAMENTO_PADRAO.equals(proximaOrdem.getOrdemtipo())) &&
				Tipoenderecamento.MANUAL.equals(proximaOrdem.getRecebimento().getTipoenderecamento()))
			window = new EnderecamentoManualWindow();
		else if (Ordemtipo.CONTAGEM_INVENTARIO.equals(proximaOrdem.getOrdemtipo()))
			window = new ContagemInventarioWindow();
		else if (Ordemtipo.RECONTAGEM_INVENTARIO.equals(proximaOrdem.getOrdemtipo()))
			window = new RecontagemInventarioWindow();
		else{
			termIO.eraseScreen();
			String msg = "O.S. não suportada.";
			termIO.setCursor(termIO.getRows() / 2, termIO.getColumns() / 2 - msg.length() / 2);
			termIO.write(msg);
			termIO.write(BasicTerminalIO.CRLF);
			termIO.read();
			return;
		}
		
		configureWindow(window);
		window.executarOrdem(proximaOrdem);
		//Se o usuário pediu pra fazer logout
		if (window.getUsuario() == null){
			this.usuario = null;
			this.deposito = null;
		}
	}

	private void showMenuPrincipal() throws Exception {
		MenuPrincipalWindow loadWindow2 = loadWindow(MenuPrincipalWindow.class);
		
		switch (loadWindow2.getAcaoDesejada()) {
			case SAIR:
				this.usuario = null;
				this.deposito = null;
				break;
			case EXECUTAR_OPERACAO:
				if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.CONVOCACAO_ATIVA_COLETOR, null))
					executarConvocacaoAtiva();
				else
					escolherOperacao();
				break;
			case CONSULTAR_PRODUTO:
				loadWindow(ConsultaProdutoWindow.class);
				break;
		}
	}

	private void escolherOperacao() throws Exception {
		MenuWindow loadWindow2 = loadWindow(MenuWindow.class);
		MenuOptions selectedOption = loadWindow2.selectedOption;
		switch (selectedOption) {
		case MENU_PRINCIPAL:
			//não nada, o loop principal vai desenhar o menu principal outra vez
			break;
		default:
			loadWindow(selectedOption.getLoadWindow());
		break;
		}
	}
	
	/**
	 * Efetua a autenticação do usuário e seta o usuário e senha no escopo
	 * das telas.
	 * 
	 * @author Pedro Gonçalves
	 * @throws Exception
	 */
	private void doLogin() throws Exception{
		
		//verifica se existe mais de uma conexão para o mesmo IP.
		//se existir vou fechar as outras.
		ConnectionData connectionData = getConnection().getConnectionData();
		Connection[] connections = connectionData.getManager().getConnectionsByAdddress(connectionData.getInetAddress());
		if (connections != null && connections.length > 1){
			for (Connection con : connections)
				if (!con.equals(getConnection()))
					con.close();
		}

		LoginWindow loadWindow = loadWindow(LoginWindow.class);
		this.usuario = loadWindow.user;
		this.deposito = loadWindow.deposito;
	}
	
	public static Shell createShell() {
		return new WmsConsoleShell();
	}

}
