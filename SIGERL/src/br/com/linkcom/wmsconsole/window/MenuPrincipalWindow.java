package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.LinkedHashMap;

import br.com.linkcom.wmsconsole.system.TelnetWindow;

public class MenuPrincipalWindow extends TelnetWindow {

	public static enum AcaoDesejada {SAIR, EXECUTAR_OPERACAO, CONSULTAR_PRODUTO}

	private AcaoDesejada acaoDesejada;
	
	@Override
	public void draw() throws IOException {
		this.acaoDesejada = null;
		
		do {
			drawEsqueleto("Logado.");
	
			writeLine("");
			writeLine("Selecione uma opção:");
			writeLine("");
			
			LinkedHashMap<Integer, String> opcoes = new LinkedHashMap<Integer, String>();
			opcoes.put(0, "Sair");
			opcoes.put(1, "Executar operação");
			opcoes.put(2, "Consultar produto");
			
			drawMenu(opcoes);
			
			int opcaoEscolhida = readInteger("");
			
			if (opcaoEscolhida == 0)
				this.acaoDesejada = AcaoDesejada.SAIR;
			else if (opcaoEscolhida == 1)
				this.acaoDesejada = AcaoDesejada.EXECUTAR_OPERACAO;
			else if (opcaoEscolhida == 2)
				this.acaoDesejada = AcaoDesejada.CONSULTAR_PRODUTO;
			
		} while (this.acaoDesejada == null);
	}
	
	public AcaoDesejada getAcaoDesejada() {
		return acaoDesejada;
	}

}
