package br.com.linkcom.wmsconsole.window;

import java.io.IOException;

import net.wimpi.telnetd.io.terminal.ColorHelper;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestocodigobarras;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.service.ManifestoService;
import br.com.linkcom.wms.geral.service.ManifestocodigobarrasService;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

public class ManifestoEntradaWindow extends ExecucaoOSWindow{
	
	private static String MSG_ERRO_INVALIDO = "C�digo inv�lido, tente novamente.";
	private static String MSG_ERRO_PROCESSAMENTO = "Ocorreu um erro de processamento. Por favor, tente novamente.";
	private static String MSG_SUCESSO = "Entrada do veiculo liberada.";
	
	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {}
	
	@Override
	public String getTitulo() {
		return "Liberar Entrada de Ve�culo (Manifesto)";
	}

	@Override
	public void draw() throws IOException {
		do{
			drawEsqueleto(DIGITE_0_PARA_SAIR);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			writeLine("");
			String valor = readBarcode("C�digo de Barras: ");
			
			if(valor!=null && !valor.isEmpty() && "0".equals(valor)){
				break;
			}else if(valor!=null && !valor.isEmpty()){
				Manifestocodigobarras mcb = ManifestocodigobarrasService.getInstance().findByCodigo(valor,deposito,Manifestostatus.ENTREGA_EM_ANDAMENTO);
				if(mcb!=null){
					Manifesto manifesto = mcb.getManifesto();
					printInfoManifesto(manifesto);
					processarEtiqueta(mcb);
					continue;
				}else{
					writeLine("");
					writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);
				}
			}else{
				writeLine("");
				writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);
				break;
			}
		}while(true);
	}
	
	/**
	 * 
	 * @param numeroManifesto
	 * @param opcao
	 * @throws IOException
	 */
	public void processarEtiqueta(Manifestocodigobarras mcb) throws IOException{
		if(mcb!=null && mcb.getCdmanifestocodigobarras()!=null){
			try{
				Integer kmfinal = readInteger("Informe a Kilometragem atual do ve�culo.");
				if(kmfinal != 0){
					Manifesto manifesto = mcb.getManifesto();
					String msgHistorico = "Retorno do ve�culo, aguardando Presta��o de Contas.";
					ManifestoService.getInstance().updateManifestoStatus(manifesto, Manifestostatus.AGUARANDO_PRESTACAO, msgHistorico, usuario);
					ManifestoService.getInstance().updateKmFinalManifesto(manifesto,kmfinal.longValue());
					writeOnCenter(MSG_SUCESSO, ColorHelper.GREEN, false, true);
				}
			}catch (Exception e) {
				e.printStackTrace();
				writeLine("");
				writeOnCenter(MSG_ERRO_PROCESSAMENTO, ColorHelper.RED, false, true);
			}
		}else{
			writeLine("");
			writeOnCenter(MSG_ERRO_INVALIDO, ColorHelper.RED, false, true);			
		}
	}
	
	/**
	 * 
	 * @param manifesto
	 * @throws IOException
	 */
	private void printInfoManifesto(Manifesto manifesto) throws IOException{
		writeLine("");
		writeLine("Placa do Veiculo: " + (manifesto.getVeiculo() != null && manifesto.getVeiculo().getPlaca()!=null ? manifesto.getVeiculo().getPlaca() : "N�o definida."));
		writeLine("N�mero do Manifesto: " + (manifesto.getCdmanifesto() != null ? manifesto.getCdmanifesto() : "N�o definida."));
		writeLine("Motorista: " + (manifesto.getMotorista() != null && manifesto.getMotorista().getNome()!=null ? manifesto.getMotorista().getNome() : "N�o definida."));
		writeLine("Transportador: " + (manifesto.getTransportador() != null && manifesto.getTransportador().getNome()!=null ? manifesto.getTransportador().getNome() : "N�o definida."));
		writeLine("Lacre Lateral: " + (manifesto.getLacrelateral() != null ? manifesto.getLacrelateral() : "N�o definida."));
		writeLine("Lacre Traseiro: " + (manifesto.getLacretraseiro() != null ? manifesto.getLacretraseiro() : "N�o definida."));
		writeLine("");
	}
}