package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.linkcom.wms.geral.service.AcaopapelService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.PermissaoordemService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.TelnetWindow;
import br.com.linkcom.wmsconsole.util.MenuOptions;

/**
 * Monta a tela de menu do sistema.
 * 
 * @author Pedro Gonçalves
 */
public class MenuWindow extends TelnetWindow {
	
	public MenuOptions selectedOption;
	
	public void draw() throws IOException{
		
		drawEsqueleto("Logado.");
		HashMap<Integer, MenuOptions> mapa = new HashMap<Integer, MenuOptions>();
		
		writeLine("Selecione uma opção:");
		writeLine("");
		
		boolean separacaoViaColetor = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.SEPARACAO_VIA_COLETOR, null);
		
		MenuOptions[] values = MenuOptions.values();
		List<MenuOptions> opcoes = new ArrayList<MenuOptions>(Arrays.asList(values));
		if (!separacaoViaColetor)
			opcoes.remove(MenuOptions.MAPA_SEPARACAO);
		if (!AcaopapelService.getInstance().isUserHasAction(usuario, "EXECUTAR_TRANSFERENCIA_MANUAL"))
			opcoes.remove(MenuOptions.EXECUTAR_TRANSFERENCIA_MANUAL);
		
		int index = 0;
		for (int i = 0; i < opcoes.size(); i++) {
			if (opcoes.get(i).getTipos() == null || opcoes.get(i).getTipos().length == 0 || PermissaoordemService.getInstance().isAssociacaoValida(opcoes.get(i).getTipos(), usuario)){
				mapa.put(index, opcoes.get(i));
				writeLine(index + " - " + opcoes.get(i).getDescricao());
				index++;
			}
		}
		
		int opcaoSelecionada = readInteger("", mapa.size());
		this.selectedOption = mapa.get(opcaoSelecionada);
	}

}
