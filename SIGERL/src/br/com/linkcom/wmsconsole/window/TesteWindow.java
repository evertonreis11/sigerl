package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.HashMap;

import br.com.linkcom.wmsconsole.system.TelnetWindow;

public class TesteWindow extends TelnetWindow {
	
	
	public void draw() throws IOException{
		HashMap<String, Object> hashMenu = new HashMap<String, Object>();
		hashMenu.put("OS 001", "001");
		hashMenu.put("OS 002", "002");
		hashMenu.put("OS 003", "003");
		hashMenu.put("OS 004", "004");
		hashMenu.put("OS 005", "005");
		hashMenu.put("OS 006", "006");
		hashMenu.put("OS 007", "007");
		hashMenu.put("OS 008", "008");
		hashMenu.put("OS 009", "009");
		hashMenu.put("OS 010", "010");
		hashMenu.put("OS 011", "011");
		hashMenu.put("OS 012", "012");
		hashMenu.put("OS 013", "013");
		hashMenu.put("OS 014", "014");
		hashMenu.put("OS 015", "015");
		hashMenu.put("OS 016", "016");
		hashMenu.put("OS 017", "017");
		hashMenu.put("OS 018", "018");
		hashMenu.put("OS 019", "019");
		hashMenu.put("OS 020", "020");
		hashMenu.put("OS 021", "021");
		hashMenu.put("OS 022", "022");
		
		makeMenuByHash(hashMenu, "Selecione uma OS",5);
	}
	
	
	
}
