package br.com.linkcom.wmsconsole.components.validator;

import net.wimpi.telnetd.io.toolkit.InputValidator;

public class IntegerValidator implements InputValidator{

	public boolean validate(String str) {
		try{
			int parseInt = Integer.parseInt(str);
			if(parseInt < 0)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
