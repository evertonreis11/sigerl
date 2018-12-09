package br.com.linkcom.wmsconsole.system;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wmsconsole.components.validator.IntegerFilter;
import br.com.linkcom.wmsconsole.util.TelnetUtil;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputFilter;
import net.wimpi.telnetd.io.toolkit.InputValidator;
import net.wimpi.telnetd.io.toolkit.Label;
import net.wimpi.telnetd.io.toolkit.Statusbar;
import net.wimpi.telnetd.io.toolkit.Titlebar;
import net.wimpi.telnetd.net.Connection;

public abstract class TelnetWindow {
	
	protected Connection connection;
	private BasicTerminalIO termIO;
	protected Usuario usuario;
	protected Deposito deposito;
	protected Empresa empresa;
	private boolean convocacaoAtiva;
	
	protected boolean exibirMenu = false;
	
	/**
	 * Exibe uma mensagem de erro na tela e aguarda o usuário pressionar ENTER.
	 * 
	 * @see TelnetUtil#alertError(BasicTerminalIO, String)
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param message
	 * @param eraseScreen
	 * @throws IOException
	 */
	protected void alertError(String message, boolean eraseScreen) throws IOException {
		if (!eraseScreen){
			writeLine("");
			writeLine("");
		}
		
		bell();
		
		writeOnCenter(message, ColorHelper.RED, eraseScreen, true);	
	}
	
	/**
	 * Exibe uma mensagem de erro na tela e aguarda o usuário pressionar ENTER.
	 * 
	 * @see TelnetWindow#alertError(String, boolean)
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param message
	 * @throws IOException
	 */
	protected void alertError(String message) throws IOException {
		alertError(message, false);	
	}
	
	/**
	 * Method that sends a signal to the user. This is defined for
	 * <b>ANY</b> NVT which is part of the internet protocol standard.
	 * The effect on the terminal might differ by the terminal type or
	 * telnet client/terminal emulator implementation.
	 */
	protected void bell() throws IOException {
		for(int i = 0; i < 3; i++){
			termIO.bell();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public abstract void draw() throws IOException;
	
	
	/**
	 * Limpa a tela e monta o esqueleto com a barra de título exibindo o texto padrão 
	 * e a barra de status exibindo um texto personalizado.
	 * 
	 * @param status A mensagem que deve ser exibida na barra de status.
	 * @throws IOException
	 */
	protected void drawEsqueleto(String status) throws IOException{
		drawEsqueleto("WMS CONSOLE", status);
	}
	
	
	/**
	 * Limpa a tela e monta o esqueleto com a barra de título e barra de status.
	 * 
	 * @param titulo
	 * @param status
	 * @throws IOException
	 */
	protected void drawEsqueleto(String titulo, String status) throws IOException{
		termIO.flush();
		termIO.eraseScreen();

		//Criando a barra de título
		Titlebar tb = new Titlebar(termIO,"titlebar");
		tb.setTitleText(titulo != null ? titulo : "");
		tb.setAlignment(Titlebar.ALIGN_CENTER);
		tb.setBackgroundColor(ColorHelper.BLUE);
		tb.setForegroundColor(ColorHelper.YELLOW);
		tb.draw();
		
		//Criando a barra de status
		Statusbar sb = new Statusbar(termIO,"status 1");
		sb.setStatusText(status != null ? status : "");
		sb.setAlignment(Statusbar.ALIGN_RIGHT);
		sb.setBackgroundColor(ColorHelper.BLUE);
		sb.setForegroundColor(ColorHelper.YELLOW);
		sb.draw();

		//reset pointer
		termIO.flush();
		termIO.setCursor(2,1);
	}
	
	/**
	 * Imprime um menu na tela atravéz do HashMap passado como parâmetro
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param mapaMenu
	 * @param keys
	 * @throws IOException
	 */
	public void drawMenu(LinkedHashMap<Integer, String> mapaMenu) throws IOException{
		
		Set<Entry<Integer, String>> entrySet = mapaMenu.entrySet();
		for (Entry<Integer, String> entry : entrySet) {
			if(!entry.getValue().equals(""))
				writeLine(entry.getKey() + " - " + entry.getValue());
			else
				writeLine("");
		}
		termIO.write(BasicTerminalIO.CRLF);
	}
	
	/**
	 * Monta um menu paginado a partir de um hash.
	 * 
	 * @param menuOptions
	 * @param message
	 * @param linhasPorPagina
	 * @return
	 * @throws IOException
	 * @author Pedro Gonçalves
	 */
	@SuppressWarnings("unchecked")
	protected Object makeMenuByHash(final HashMap<String, Object> menuOptions,String message, int linhasPorPagina) throws IOException{
		String valorSelecionado = "";
		
		int currentPage = 0;
		
		Entry[] arr = menuOptions.entrySet().toArray(new Entry[menuOptions.size()]);
		//Ordena os ítens do array
		Arrays.sort(arr, new Comparator<Entry>(){
			@Override
			public int compare(Entry o1, Entry o2) {
				return o1.getKey().toString().compareTo(o2.getKey().toString());
			}
		});
		int numberOfPages = menuOptions.size() / linhasPorPagina;
		if(menuOptions.size() % linhasPorPagina != 0)
			numberOfPages ++;
		
		while(true) {
			int start = linhasPorPagina * currentPage;
			int end = start + linhasPorPagina;
			if(end > arr.length)
				end = arr.length;
			
			final int inicio = start;
			final int fim = end;
			
			drawEsqueleto("Digite 0 para sair.");
			termIO.write(message);
			termIO.write(BasicTerminalIO.CRLF);
			termIO.write(BasicTerminalIO.CRLF);
			
			writeLine("0 - Sair do menu");
			writeSeparator();
			
			for (int i = start; i < end; i++) {
				Entry<String, Object> entry = (Entry<String, Object>)arr[i];
				writeLine((i+1)+" - "+entry.getKey());
			}
			writeSeparator();
			termIO.write("Página " + (currentPage + 1) + " de " + numberOfPages);
			termIO.write(BasicTerminalIO.CRLF);
			termIO.write(BasicTerminalIO.CRLF);
			Editfield optDep=new Editfield(termIO, "editfield 3", 10);
			optDep.registerInputValidator(new InputValidator(){
				@Override
				public boolean validate(String str) {
					try{
						if(str.equals("") || str.equals("0"))
							return true;
					
						int option = Integer.parseInt(str);
						if(option <= fim && option > inicio){
							return true;
						}
					} catch (Exception e){
						return false;
					}
					return false;
				}
			});
			
			optDep.run();
			
			if(optDep.getValue().equals("")){
				currentPage ++;
				if(end == menuOptions.size())
					currentPage = 0;
				continue;
			} else {
				valorSelecionado = optDep.getValue();
				break;
			}
		}
		
		if(valorSelecionado.equals("0"))
			return null;
		
		Object value = arr[Integer.parseInt(valorSelecionado) - 1].getValue();
		termIO.moveUp(1);
		
		return value;
	}
	
	/**
	   * Method that retrieves Input from the underlying
	   * Stream, translating Terminal specific escape
	   * sequences and returning a (constant defined) key,
	   * or a character.
	   *
	   * @return int that represents a constant defined key.
	   */
	protected int read() throws IOException{
		return termIO.read();
	}
	
	/**
	 * Para a execução do programa e so libera após um enter ser pressionado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @throws IOException
	 */
	protected void readEnter() throws IOException{
		TelnetWindow.readEnter(termIO);
	}
	
	/**
	 * Para a execução do programa e so libera após um enter ser pressionado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @throws IOException
	 */
	protected static void readEnter(BasicTerminalIO termIO) throws IOException{
		final Editfield produtoEdf = new Editfield(termIO,"edf",0);
		produtoEdf.registerInputFilter(new InputFilter(){

			@Override
			public int filterInput(int key) throws IOException {
				if(key == 35 || key == 10)
					return BasicTerminalIO.ENTER;
				else
					return InputFilter.INPUT_HANDLED;
			}
			
		});
		
		produtoEdf.run();
	}
	
	/**
	 * Aguarda a leitura de um número inteiro. 
	 * Ele deve pressionar Enter para que a leitura seja realizada.
	 * 
	 * @param msg
	 * @return
	 * @throws IOException 
	 */
	protected Integer readInteger(String msg, final int maxValue) throws IOException{
		exibirMenu = false;
		
		writeLine(msg);
		termIO.write("#");
		
		final Editfield edf = new Editfield(termIO,"edf",9);
		edf.registerInputValidator(new InputValidator(){
			@Override
			public boolean validate(String str) {
				try{
					int value = Integer.parseInt(str);
					if(value >= 0 && value <= maxValue){
						return true;
					}
				} catch (Exception e){
					return false;
				}
				return false;
			}
		});

		edf.registerInputFilter(new IntegerFilter(termIO));

		edf.run();
		
		String value = edf.getValue();
		
		//Fazendo uma quebra de linha, para que a próxima informação não fique na mesma linha.
		writeLine("");
		
		if ("0".equals(value))
			exibirMenu = true;
		
		return value != null ? Integer.valueOf(value) : null;
	}

	/**
	 * Aguarda a leitura de um número inteiro. 
	 * Ele deve pressionar Enter para que a leitura seja realizada.
	 * 
	 * @param msg
	 * @return
	 * @throws IOException 
	 */
	protected Integer readInteger(String msg) throws IOException{
		return readInteger(msg, Integer.MAX_VALUE);
	}
	
	/**
	 * Aguarda a leitura de dados fornecidos pelo usuário. 
	 * Ele deve pressionar Enter para que a leitura seja realizada.
	 * 
	 * @param msg
	 * @return
	 * @throws IOException 
	 */
	protected String readLine(String msg) throws IOException{
		exibirMenu = false;
		
		writeLine(msg);
		termIO.write("#");
		
		final Editfield edf = new Editfield(termIO,"edf",50);
		
		edf.registerInputFilter(new InputFilter(){

			@Override
			public int filterInput(int key) throws IOException {
				if(key == 35) {
					return BasicTerminalIO.ENTER;
				}
				return key;
			}
			
		});
		edf.run();
		
		String value = edf.getValue();
		if ("0".equals(value))
			exibirMenu = true;
		
		//Fazendo uma quebra de linha, para que a próxima informação não fique na mesma linha.
		writeLine("");
		
		return value;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setTermIO(BasicTerminalIO termIO) {
		this.termIO = termIO;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * Escreve uma mensagem no terminal e faz uma quebra de linha.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	protected static void writeLine(BasicTerminalIO terminalIO, String msg) throws IOException{
		terminalIO.write(msg);
		terminalIO.write(BasicTerminalIO.CRLF);		
	}
	
	/**
	 * Escreve uma mensagem no terminal e faz uma quebra de linha.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	protected void writeLine(String msg) throws IOException{
		TelnetWindow.writeLine(termIO, msg);
	}
	
	
	protected void writeLine(String msg, String color, boolean truncarTexto) throws IOException{
		TelnetWindow.writeLine(termIO, msg, color, truncarTexto);
	}
	
	protected static void writeLine(BasicTerminalIO termIO, String msg, String color, boolean truncarTexto) throws IOException{
		if (truncarTexto)
			msg = Util.strings.truncate(msg, termIO.getColumns());
		
		if(color != null && !color.equals(""))//Caso tenha alguma cor preferida
			termIO.write(ColorHelper.colorizeText(msg, color));
		else
			termIO.write(msg); // Imprime na cor padrão
		
		termIO.write(BasicTerminalIO.CRLF);
	}

	/**
	 * Imprime uma mensagem centralizada na horizontal.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param message
	 * @param color - caso queira a cor padrao deixe null
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void writeOnCenter(String message, String color, boolean eraseScreen, boolean readEnter) throws IOException{
		TelnetWindow.writeOnCenter(this.termIO, message, color, eraseScreen, readEnter);
	}

	/**
	 * Imprime uma mensagem centralizada na horizontal.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param message
	 * @param color - caso queira a cor padrao deixe null
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static void writeOnCenter(BasicTerminalIO termIO, String message, String color, boolean eraseScreen, boolean readEnter) throws IOException{		
		if(message != null && !"".equals(message)){
			String[] linhas = wrapText(message, termIO.getColumns());
			
			//Se é para limpar a tela, limpa ela e posiciona o cursor no centro
			if (eraseScreen){
				termIO.eraseScreen();
				termIO.flush();
				termIO.setCursor(termIO.getRows()/2 - linhas.length/2,1);
			}else//se não é para limpar a tela limpa a linha, para que o texto realmente seja impresso no centro
				termIO.eraseLine();

			for (String text : linhas){
				text = StringUtils.center(text, termIO.getColumns());
				
				if(color != null && !color.equals(""))//Caso tenha alguma cor preferida
					TelnetWindow.writeLine(termIO, ColorHelper.colorizeText(text, color));
				else
					TelnetWindow.writeLine(termIO, text); // Imprime na cor padrão
			}
			
			if (readEnter)
				readEnter(termIO);
		}
	}
	
	protected BasicTerminalIO getTermIO() {
		return termIO;
	}
	
	/**
	 * Faz uma mensagem de confirmação
	 * 
	 * @param message
	 * @return true - Caso o autor confirme
	 * 		   false - Caso o autor cancele
	 * @throws IOException
	 */
	protected boolean confirmAction(String message) throws IOException{
		drawEsqueleto("");
		
		Label lb1 = new Label(termIO,"lb1",message);
		lb1.draw();
		termIO.write(BasicTerminalIO.CRLF);
		termIO.write(BasicTerminalIO.CRLF);
		termIO.write("1 - Sim" + BasicTerminalIO.CRLF);
		termIO.write("0 - Não" + BasicTerminalIO.CRLF);
		termIO.write(BasicTerminalIO.CRLF);
		

		final Editfield confirmEdf = new Editfield(termIO,"edf",1);		
		confirmEdf.registerInputValidator(new InputValidator(){

			@Override
			public boolean validate(String str) {
				return "1".equals(str) || "0".equals(str);
			}
			
		});
		confirmEdf.run();
		String valueProduto = confirmEdf.getValue();
		
		if(valueProduto.equals("1"))
			return true;
		
		return false;
	}
	
	/**
	 * Quebra um texto em várias linhas
	 * 
	 * @param text
	 * @param width
	 * @return
	 */
	public static String[] wrapText(String text, int width) {
	    StringBuffer buf = new StringBuffer(text);
	    int lastspace = -1;
	    int linestart = 0;
	    int i = 0;

	    while (i < buf.length()) {
	       if ( buf.charAt(i) == ' ' ) lastspace = i;
	       if ( buf.charAt(i) == '\n' ) {
	          lastspace = -1;
	          linestart = i+1;
	          }
	       if (i > linestart + width - 1 ) {
	          if (lastspace != -1) {
	             buf.setCharAt(lastspace,'\n');
	             linestart = lastspace+1;
	             lastspace = -1;
	             }
	          else {
	             buf.insert(i,'\n');
	             linestart = i+1;
	             }
	          }
	        i++;
	       }
	    return buf.toString().split("\n");
	 }

	/**
	 * Escreve uma linha tracejada usando '-' para separar conteúdos na tela.
	 * 
	 * @throws IOException
	 */
	protected void writeSeparator() throws IOException{
		for (int i = 0; i < termIO.getColumns();i++)
			termIO.write('-');
		termIO.write(BasicTerminalIO.CRLF);
	}

	/**
	 * Efetua logout do coletor
	 */
	public void logout(){
		//Forçando um logout
		this.usuario = null;
		this.deposito = null;
		this.empresa = null;
	}

	public boolean isConvocacaoAtiva() {
		return convocacaoAtiva;
	}

	public void setConvocacaoAtiva(boolean convocacaoAtiva) {
		this.convocacaoAtiva = convocacaoAtiva;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public Deposito getDeposito() {
		return deposito;
	}
		
}
