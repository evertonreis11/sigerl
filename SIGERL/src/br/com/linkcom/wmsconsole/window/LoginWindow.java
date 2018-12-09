package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;
import net.wimpi.telnetd.io.toolkit.Label;

import org.apache.commons.beanutils.BeanComparator;
import org.jasypt.util.password.StrongPasswordEncryptor;

import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.service.UsuarioService;
import br.com.linkcom.wmsconsole.system.TelnetWindow;

/**
 * Monta a tela de login do sistema.
 * 
 * @author Pedro Gonçalves
 */
public class LoginWindow extends TelnetWindow {
	
	public Usuario user;
	public Deposito deposito;
	
	/**
	 * Monta a tela de login com os campos login e senha para edição.
	 * @see br.com.linkcom.wmsconsole.window.LoginWindow#verificaUser(String login, String senha)
	 */
	public void draw() throws IOException{
		
		
		String login = "";
		String senha = "";
		String msg = "";
		
		Boolean connected = false;
		
		do {
			getTermIO().eraseScreen();
			drawEsqueleto("".equals(msg) ? "Não conectado." : msg);
			
			Label lb1 = new Label(getTermIO(),"lb1","Login: ");
			lb1.draw();
			
			Editfield loginEdf=new Editfield(getTermIO(),"editfield 1",17);
			loginEdf.run();
			
			getTermIO().write(BasicTerminalIO.CRLF);
			
			Label lb2 = new Label(getTermIO(),"lb2","Senha: ");
			lb2.draw();
			
			Editfield senhaEdf = new Editfield(getTermIO(),"editfield 2",8);
			senhaEdf.setPasswordField(true);
			senhaEdf.run();
			
			login = loginEdf.getValue();
			senha = senhaEdf.getValue();
			
			connected = verificaUser(login, senha);
			
			if(!connected) {
				msg="Login ou senha incorretos.";
				getTermIO().bell();
			}
			
		} while (!connected);
		
		getTermIO().eraseScreen();
		getTermIO().setCursor(2, 1);
		drawEsqueleto("Logado.");
		
		getTermIO().setCursor(2, 1);
		doSelectionDeposito();
		getTermIO().flush();
		
		drawEsqueleto("Aguarde...");
	}
	
	/**
	 * Faz a autenticação do usuário.
	 * 
	 * @see br.com.linkcom.wms.geral.service.UsuarioService#findByLogin(String login)
	 * @param login
	 * @param senha
	 * @return
	 */
	private Boolean verificaUser(String login, String senha){
		if(login == null || login.equals("") || senha == null || senha.equals("")){
			return false;
		}
		this.user = UsuarioService.getInstance().findByLogin(login);
		
		if(user == null)
			return false;
		else{
						
			if(user.getAtivo().equals(false))
				return false;
				
			return verifyPassword(user, senha);
		}
	}
	
	/**
	 * Faz a seleção do menu do deposito
	 * 
	 * Os depositos que aparecem são todos que o usuário logado pertence.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void doSelectionDeposito() throws IOException{
		List<Usuariodeposito> listaUsuarioDeposito = new ArrayList<Usuariodeposito>(user.getListaUsuarioDeposito());
		final HashMap<Integer, Deposito> mapa = new HashMap<Integer, Deposito>();
		
		if(listaUsuarioDeposito != null && listaUsuarioDeposito.size() == 1){
			Usuariodeposito next = listaUsuarioDeposito.iterator().next();
			this.deposito = next.getDeposito();
		} else {
		
			Collections.sort(listaUsuarioDeposito, new BeanComparator("deposito.nome"));
			
			writeLine("Escolha o depósito:");
			writeLine("");
			
			int i = 1;
			for (Usuariodeposito usuariodeposito : listaUsuarioDeposito) {
				mapa.put(i, usuariodeposito.getDeposito());
				writeLine((i++)+" - "+usuariodeposito.getDeposito().getNome());
			}
			
			getTermIO().write(BasicTerminalIO.CRLF);
			Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
			optDep.registerInputValidator(new InputValidator(){
				public boolean validate(String str) {
					try{
						int option = Integer.parseInt(str);
						if(option <= mapa.size() && option > 0){
							return true;
						}
					} catch (Exception e){
						return false;
					}
					return false;
				}
			});
			optDep.run();
			getTermIO().write(BasicTerminalIO.CRLF);
			this.deposito = mapa.get(Integer.parseInt(optDep.getValue()));
		}
		writeLine("Depósito selecionado:"+BasicTerminalIO.CRLF);
		writeLine(deposito.getNome());
	}
	
	//Método copiado do Neo. O método do Neo devia estar acessível daqui.
    protected boolean verifyPassword(User user, String password) {
    	boolean passwordMatch = false;
		boolean updatePassword = false;
		
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		
		// se o usuário existe e a senha foi passada
		if (user != null && user.getPassword() != null) {
			if(user.getPassword().matches("[a-zA-Z0-9\\+/]{64}")) {
				// Jasypt Strong Encryption
				if (passwordEncryptor.checkPassword(password, user.getPassword())) {
					passwordMatch = true;
				}
			}
			else if(user.getPassword().matches("[0-9a-f]{32}")) {
				// MD5 simples
				updatePassword = true;
				if (Util.crypto.makeHashMd5(password).equals(user.getPassword())) {
					passwordMatch = true;
				}
			}
			else {
				// Senha pura
				updatePassword = true;
				if (user.getPassword().equals(password)) {
					passwordMatch = true;
				}
			}
		}
		
		if (passwordMatch) {
			String hashSenha = Util.crypto.makeHashJasypt(user.getPassword());
			if(updatePassword) {
				updatePasswordOnDataBase(user,hashSenha);
			}
		}
			
		return passwordMatch;
    }

	private void updatePasswordOnDataBase(User user, String hashSenha) {
		if (user instanceof Usuario) {
			Usuario usuario = (Usuario) user;
			usuario.setSenha(hashSenha);
			UsuarioService.getInstance().alterarSenhaUsuario(usuario);			
		}		
	}
    
}
