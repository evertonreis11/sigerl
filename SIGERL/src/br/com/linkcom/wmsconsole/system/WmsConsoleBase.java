package br.com.linkcom.wmsconsole.system;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

public abstract class WmsConsoleBase implements Shell {

	private Connection connection;
	protected BasicTerminalIO termIO;
	
	protected Usuario usuario;
	protected Deposito deposito;
	
	public final void run(Connection connection) {
		this.connection = connection;
		try {
			termIO = connection.getTerminalIO();
			connection.addConnectionListener(this);
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void execute() throws Exception;

	public void connectionIdle(ConnectionEvent ce) {
	}

	public void connectionLogoutRequest(ConnectionEvent ce) {
	}
	
	public void connectionSentBreak(ConnectionEvent ce) {
	}

	public void connectionTimedOut(ConnectionEvent ce) {
	}

	protected <T extends TelnetWindow> T loadWindow(Class<T> clazz) throws Exception {
		T ins = (T) clazz.newInstance();
		if(ins instanceof TelnetWindow){
			configureWindow(ins);
			ins.draw();
			return ins;
		} else {
			throw new IllegalArgumentException("Somente classes que estendem de TelnetWindow podem ser utilizadas como janela.");
		}
	}

	protected void configureWindow(TelnetWindow window) {
		window.setConnection(connection);
		window.setTermIO(termIO);
		window.setUsuario(usuario);
		window.setDeposito(deposito);
		window.setConvocacaoAtiva(ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.CONVOCACAO_ATIVA_COLETOR, null));
	}

	public Connection getConnection() {
		return connection;
	}
	
}
