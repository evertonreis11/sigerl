package br.com.linkcom.wmsconsole.components.validator;

import java.io.IOException;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.toolkit.InputFilter;

public class IntegerFilter implements InputFilter{

	private BasicTerminalIO termIO;
	
	public int filterInput(int key) throws IOException {
		if((key < 48 || key > 57) && (key != 1202 && key != 10 && key !=1303 && key !=1302 && key != 1004 && key != 1003)){
			termIO.bell();
			return 1002;
		}
		return key;
	}
	
	public IntegerFilter(BasicTerminalIO term) {
		this.termIO = term;
	}

}
