package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import net.wimpi.telnetd.io.terminal.ColorHelper;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculoService;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculohistoricoService;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

public class AcompanhamentoVeiculoWindow extends ExecucaoOSWindow{

	private static String MSG_ERRO_INVALIDO = "C�digo inv�lido, tente novamente.";
	private static String MSG_SUCESSO_ENTRADA = "Ve�culo Autorizado: Entrada Permitida.";
	private static String MSG_SUCESSO_SAIDA = "Ve�culo Autorizado: Sa�da Permitida.";
	private static String MSG_ERRO_CONCLUIDO = "Este c�digo j� est� confirmado. Por favor, confirme se o n� Rav est� correto.";
	private static String MSG_ERRO_PROCESSAMENTO = "Ocorreu um erro de processamento. Por favor, tente novamente.";
	private static String MSG_NAO_LIBERADO = "N�o � poss�vel emitir a sa�da de ve�culo que ainda n�o foi liberado.";

	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {}
	
	@Override
	public String getTitulo() {
		return "Registrar Entrada/Sa�da de Ve�culos";
	}

	@Override
	public void draw() throws IOException {
		do{
			drawEsqueleto(DIGITE_0_PARA_SAIR);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			Integer opcao = -1;
			do{
				drawEsqueleto(DIGITE_0_PARA_SAIR);
				writeOnCenter(getTitulo(), null, false, false);
				writeSeparator();
				
				writeLine("");
				writeLine("Selecione uma op��o:");
				writeLine("");
				
				LinkedHashMap<Integer, String> opcoes = new LinkedHashMap<Integer, String>();
				opcoes.put(1, "Registrar Entrada");
				opcoes.put(2, "Registrar Saida");
				
				drawMenu(opcoes);
				opcao = readInteger("");
			}while(opcao!=0 && opcao!=1 && opcao!=2);
			
			if(opcao==0)
				break;
			
			writeLine("");
			String valor = readBarcode("C�digo de Barras (Entrada/Sa�da): ");
			
			if(valor!=null && !valor.isEmpty() && "0".equals(valor)){
				break;
			}else if(valor!=null && !valor.isEmpty()){
				processarEtiqueta(valor,opcao);
			}else{
				writeLine("");
				writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);
				break;
			}
		}while(true);
	}
	
	/**
	 * 
	 * @param numeroRav
	 * @throws IOException
	 */
	public void processarEtiqueta(String numeroRav, Integer opcao) throws IOException{
		
		Acompanhamentoveiculo av = AcompanhamentoveiculoService.getInstance().findByNumeroRav(numeroRav);
		
		if(av!=null && av.getCdacompanhamentoveiculo()!=null){
			Recebimento recebimentoAux = av.getRecebimento();
			if(av.getDataentrada()!=null && av.getDatasaida()!=null){
				writeLine("");
				writeOnCenter(MSG_ERRO_CONCLUIDO, ColorHelper.RED, false, true);
			}else if(av.getDataentrada()!=null && av.getDatasaida()==null && opcao==2){
				try {
					if(recebimentoAux!=null && recebimentoAux.getSaidaliberada()!=null && recebimentoAux.getSaidaliberada()){
						if(av.getAcompanhamentoveiculostatus().equals(Acompanhamentoveiculostatus.LIBERADO) && av.getDataentrada()!=null){
							String msg = AcompanhamentoveiculoService.getInstance().callAtualizaRAV(av,"SP");
							av.setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus.SAIDA);
							AcompanhamentoveiculohistoricoService.getInstance().criarHistorico(av, null, usuario);
							if(msg.equals("OK")){
								writeOnCenter(MSG_SUCESSO_SAIDA, ColorHelper.GREEN, false, true);
							}else{
								writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);
							}
						}else{
							writeOnCenter("A libera��o de sa�da s� poder� ser realizada em RAV's com status: 'Recebimento Finalizado' "
									+ "e mediante libera��o de entrada na portaria.", ColorHelper.RED, false, true);	
						}
					}else{
						writeOnCenter(MSG_NAO_LIBERADO, ColorHelper.RED, false, true);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					writeOnCenter(MSG_ERRO_PROCESSAMENTO, ColorHelper.RED, false, true);	
				}
			}else if(av.getDataentrada()==null && av.getDatasaida()==null && opcao==1){
				try {
					if(av.getAcompanhamentoveiculostatus().equals(Acompanhamentoveiculostatus.RECEBIMENTO_GERADO)){
						String msg = AcompanhamentoveiculoService.getInstance().callAtualizaRAV(av,"EP");
						av.setAcompanhamentoveiculostatus(Acompanhamentoveiculostatus.ENTRADA);
						AcompanhamentoveiculohistoricoService.getInstance().criarHistorico(av, null, usuario);
						if(msg.equals("OK")){
							writeOnCenter(MSG_SUCESSO_ENTRADA, ColorHelper.GREEN, false, true);
						}else{
							writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);	
						}
					}else{
						writeOnCenter("A libera��o de entrada s� poder� ser realizada em RAV's com status: 'Gerado'", ColorHelper.RED, false, true);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					writeOnCenter(MSG_ERRO_PROCESSAMENTO, ColorHelper.RED, false, true);	
				}
			}else{
				writeLine("");
				writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);
			}
		}else{
			writeLine("");
			writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);
		}
	}
	
}
