package br.com.linkcom.wmsconsole.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputFilter;
import net.wimpi.telnetd.io.toolkit.Statusbar;
import net.wimpi.telnetd.io.toolkit.Titlebar;
import br.com.linkcom.neo.util.Util;


/**
 * Classe utilitária de interface. Todas as interfaces genéricas estarão aqui.
 * 
 * @author Pedro Gonçalves
 */
public class TelnetUtil {
	public static final int LIMITE = 29;
	/**
	 * Monta a barra de status padrão do WMSConsole.
	 * 
	 * @param termIO
	 * @param title
	 * @throws IOException
	 */
	public void drawStatusBar(BasicTerminalIO termIO, String title) throws IOException{
		Statusbar sb=new Statusbar(termIO,"status 1");
		sb.setStatusText(title);
		sb.setAlignment(Statusbar.ALIGN_RIGHT);
		sb.setBackgroundColor(ColorHelper.BLUE);
		sb.setForegroundColor(ColorHelper.YELLOW);
		sb.draw();
	}
	
	/**
	 * Desenha a barra de título
	 * 
	 * @param termIO
	 * @param title - título da aplicação.
	 * @throws IOException
	 */
	public void drawTitleBar(BasicTerminalIO termIO,String title) throws IOException{
		Titlebar tb = new Titlebar(termIO,"titlebar");
		tb.setTitleText(title);
		tb.setAlignment(Titlebar.ALIGN_CENTER);
		tb.setBackgroundColor(ColorHelper.BLUE);
		tb.setForegroundColor(ColorHelper.YELLOW);
		tb.draw();
	}
	
	
	/**
	 * Monta a tela de título com o texto WMS CONSOLE como padrão.
	 * 
	 * @see #drawTitleBar(BasicTerminalIO termIO, String title)
	 * @param termIO
	 * @throws IOException
	 */
	public void drawTitleBar(BasicTerminalIO termIO) throws IOException{
		drawTitleBar(termIO, "WMS CONSOLE");
	}
	
	/**
	 * Imprime repeitando os limites da tela com cores diferentes.
	 * 
	 * @see ColorHelper
	 * 
	 * @param termIO
	 * @param color - Cor do texto. 
	 * @param text - texto a ser escrito.
	 * @throws IOException
	 */
	public void writeColor(BasicTerminalIO termIO,String color, String text) throws IOException{
		String truncate = Util.strings.truncate(text, LIMITE);
		termIO.write(ColorHelper.colorizeText(truncate, color));
	}
	
	/**
	 * Imprime na tela com cores diferentes, sem limitar o tamanho da string.
	 * 
	 * @see ColorHelper
	 * 
	 * @param termIO
	 * @param color - Cor do texto. 
	 * @param text - texto a ser escrito.
	 * @throws IOException
	 */
	public void writeColorNoWrap(BasicTerminalIO termIO,String color, String text) throws IOException{
		termIO.write(ColorHelper.colorizeText(text, color) + BasicTerminalIO.CRLF);
	}
	
	/**
	 * Monta o esqueleto com a barra de título e barra de status. Seta o valor clear como true.
	 * 
	 * @see #drawEsqueleto(BasicTerminalIO termIO, String status, Boolean clear) 
	 * @param termIO
	 * @throws IOException
	 */
	public void drawEsqueleto(BasicTerminalIO termIO, String status) throws IOException{
		drawEsqueleto(termIO, status, true);
	}
	
	/**
	 * Monta o esqueleto com a barra de título e barra de status. Seta o valor clear como true.
	 * 
	 * Caso o título seja nulo ou em branco, aparecerá WMS CONSOLE
	 * 
	 * @see #drawEsqueleto(BasicTerminalIO termIO,String titulo, String status, Boolean clear)
	 * @see #drawEsqueleto(BasicTerminalIO termIO, String status, Boolean clear) 
	 * @param termIO
	 * @throws IOException
	 */
	public void drawEsqueleto(BasicTerminalIO termIO,String titulo, String status) throws IOException{
		if(titulo == null || titulo.equals(""))
			drawEsqueleto(termIO,status, true);
		else
			drawEsqueleto(termIO,titulo, status, true);
	}
	
	/**
	 * Monta o esqueleto com a barra de título e barra de status.
	 * 
	 * @see #drawTitleBar(BasicTerminalIO termIO)
	 * @see #drawStatusBar(BasicTerminalIO, String)
	 * @see #resetPointer(BasicTerminalIO) 
	 * 
	 * @param termIO - Conexão
	 * @param status - Texto para ser adicionado na barra de status.
	 * @param clear - Limpa a tela.
	 * @throws IOException
	 */
	public void drawEsqueleto(BasicTerminalIO termIO, String status,Boolean clear) throws IOException{
		if(clear) termIO.eraseScreen();
		drawTitleBar(termIO);
		drawStatusBar(termIO, status);
		resetPointer(termIO);
	}
	
	/**
	 * Monta o esqueleto com a barra de título alterada de acordo com parâmetro e barra de status.
	 * 
	 * @see #drawTitleBar(BasicTerminalIO termIO, String)
	 * @see #drawStatusBar(BasicTerminalIO, String)
	 * @see #resetPointer(BasicTerminalIO) 
	 * 
	 * @param termIO - Conexão
	 * @param titulo - Texto para ser adicionado na barra de título
	 * @param status - Texto para ser adicionado na barra de status.
	 * @param clear - Limpa a tela.
	 * @throws IOException
	 */
	public void drawEsqueleto(BasicTerminalIO termIO,String titulo, String status,Boolean clear) throws IOException{
		if(clear) termIO.eraseScreen();
		drawTitleBar(termIO,titulo);
		drawStatusBar(termIO, status);
		resetPointer(termIO);
	}
	
	/**
	 * Reseta a tela e seta o curso na linha 2.
	 * 
	 * @param termIO
	 * @throws IOException
	 */
	public void resetPointer(BasicTerminalIO termIO) throws IOException{
		termIO.flush();
		termIO.setCursor(2,1);
	}
	
	/**
	 * Imprime na tela com o limite máximo na tela.
	 * 
	 * @param termIO
	 * @param text - Texto a ser enviado.
	 * @throws IOException
	 */
	public void write(BasicTerminalIO termIO,String text) throws IOException{
		String truncate = Util.strings.truncate(text, LIMITE);
		termIO.write(truncate);
	}
	
	/**
	 * Imprime um menu na tela atravéz do HashMap passado como parâmetro
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param termIO
	 * @param mapaMenu
	 * @param espacos - Posição de linhas em branco para separar itens do menu
	 * @throws IOException 
	 */
	/* Esse array é para possibilitar que o menu não tenha que 
	 * obrigatóriamente ser com números sequênciais
	 */
	public void drawMenuSemOrdem(BasicTerminalIO termIO, HashMap<Integer, String> mapaMenu,int [] keys) throws IOException{
		for(int i : keys){
			if(mapaMenu.containsKey(i))
				if(!mapaMenu.get(i).equals(""))
				write(termIO, i+" - "+mapaMenu.get(i));
			termIO.write(BasicTerminalIO.CRLF);
		}
		termIO.write(BasicTerminalIO.CRLF);
	}
	
	/**
	 * Imprime um menu na tela atravéz do HashMap passado como parâmetro
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param termIO
	 * @param mapaMenu
	 * @param keys
	 * @throws IOException
	 */
	public void drawMenu(BasicTerminalIO termIO, HashMap<Integer, String> mapaMenu) throws IOException{
		
		Set<Entry<Integer, String>> entrySet = mapaMenu.entrySet();
		for (Entry<Integer, String> entry : entrySet) {
			if(!entry.getValue().equals(""))
				write(termIO, entry.getKey()+" - "+entry.getValue());
			termIO.write(BasicTerminalIO.CRLF);
		}
		termIO.write(BasicTerminalIO.CRLF);
	}
	
	/**
	 * Imprime uma mensagem no centro da linha indicada
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param message
	 * @param termIO
	 * @param row
	 * 
	 * @return
	 * @throws IOException 
	 * @throws IOException 
	 */
	public void writeOnCenter(BasicTerminalIO termIO,String message,int row) throws IOException{
		writeOnCenter(termIO, message, row, null);
	}
	
	/**
	 * Imprime uma mensagem no centro da linha indicada
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param message
	 * @param termIO
	 * @param row
	 * @param color - caso queira a cor padrao deixe null
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void writeOnCenter(BasicTerminalIO termIO,String message,int row,String color) throws IOException{
		if(message != null && !"".equals(message)){
			int inicio = 0;
			int fim = LIMITE - 1;
			int size = message.length();
			int flag = 0;
			String text = "";
			do{
				if(size >= fim){
					text = message.substring(inicio,fim);
					inicio = fim;
					fim = message.substring(fim,size).length();//Obtem o tamanho restante
					if(fim > LIMITE - 1)//Se o tamanho da mensagem restante é maior que o número de colunas
						fim = inicio + LIMITE - 1;
					else{
						fim = size;
						flag++;
					}
				}else{
					text = message;
					flag = 2;
				}
				
				termIO.setCursor(row, getCenterPosition(text));
				
				if(color != null && !color.equals(""))//Caso tenha alguma cor preferida
					writeColor(termIO, color, text);
				else
					write(termIO, text); // Imprime na cor padrão
				
				row ++;
			}while(flag < 2);
		}
	}

	/**
	 * Retorna 
	 * @param message
	 * @return
	 */
	private int getCenterPosition(String message) {
		int pos = 0;
		if(message != null && !"".equals(message)){
			pos = (LIMITE - message.length()) / 2 + 1;
		}
		return pos;
	}
	
	/**
	 * Imprime uma mensagem no centro da tela do coletor e na linha número 10
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param termIO
	 * @param message
	 * @throws IOException 
	 */
	public void writeOnCenter(BasicTerminalIO termIO,String message) throws IOException{
		writeOnCenter(termIO, message,10,null);
	}
	
	/**
	 * Imprime uma mensagem centralizada com a cor especificada
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param termIO
	 * @param color
	 * @param text
	 * @throws IOException 
	 */
	public void writeColorOnCenter(BasicTerminalIO termIO,String color, String message,int row) throws IOException{
		writeOnCenter(termIO, message, row, color);
	}
	
	/**
	 * Imprime uma mensagem de erro emitindo dois bipes na decima linha
	 * 
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #alertError(BasicTerminalIO, String, int)
	 * 
	 * @param termIO
	 * @param message
	 * @throws IOException 
	 */
	public void alertError(BasicTerminalIO termIO,String message) throws IOException{
		alertError(termIO, message,10,true,true);	
	}
	
	/**
	 * Imprime uma mensagem de erro emitindo dois bipes
	 * 
	 * @param termIO
	 * @param message
	 * @param row
	 * @param useBeep
	 * 
	 * @throws IOException
	 */
	public void alertError(BasicTerminalIO termIO,String message, int row,boolean useBeep) throws IOException{
		alertError(termIO, message, row, useBeep, true);
	}
	
	

	/**
	 * Exibir uma mensagem de erro e permite escolher se o usuário deverá 
	 * pressionar ENTER para voltar à tela anterior
	 * 
	 * @see TelnetUtil#alertError(BasicTerminalIO, String)
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param termIO
	 * @param message
	 * @param esperarEnter
	 * @throws IOException
	 */
	public void alertError(BasicTerminalIO termIO, String message,int row,boolean useBeep, boolean esperarEnter) throws IOException {
		
		if(useBeep)
			for(int i = 0; i < 3; i++){
				termIO.bell();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
		writeColorOnCenter(termIO, ColorHelper.RED, message,row);	
		
		if (esperarEnter){
			//Foi solicitado que a mensagem de erro só sumisse após pressionar o Enter.
			//O código a seguir faz isto
			final Editfield produtoEdf = new Editfield(termIO,"edf",0);
			produtoEdf.registerInputFilter(new InputFilter(){

				public int filterInput(int key) throws IOException {
					if(key == 10)
						return BasicTerminalIO.ENTER;
					else
						return InputFilter.INPUT_HANDLED;
				}
				
			});
			
			produtoEdf.run();

		}
	}

	private static TelnetUtil instance;
	
	public static TelnetUtil getInstance() {
		if(instance == null)
			instance = new TelnetUtil();
		
		return instance;
	}
}
