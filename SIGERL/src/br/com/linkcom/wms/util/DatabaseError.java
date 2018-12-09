package br.com.linkcom.wms.util;



public class DatabaseError {
	
	public static Boolean isKeyPresent(Exception e,String key) {
		String message = e.getMessage();
		
		if (message != null && !"".equals(message)) 
			message = message.toUpperCase();			
		
		if (key != null && !"".equals(key)) 
			key = key.toUpperCase();
		
		if (message != null)
			return message.contains(key);
		else
			return false;
	}
}	
