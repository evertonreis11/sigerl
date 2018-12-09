package br.com.ricardoeletro.coletor.geral.as;

import java.io.IOException;

import br.com.linkcom.neo.core.standard.Neo;

public class ColetorAS {
	/**
	 * Escreve uma linha tracejada usando '-' para separar conteúdos.
	 * 
	 * @throws IOException
	 */
	protected void writeSeparator(StringBuilder dados){
		for (int i = 0; i < 80;i++)
			dados.append('-');
		
		dados.append(System.lineSeparator());
	}
	

	/**
	 * Escreve uma mensagem e faz uma quebra de linha.
	 * 
	 * @param msg
	 * @throws IOException
	 */
	protected void writeLine(StringBuilder dados, String msg){
		dados.append(msg);
		dados.append(System.lineSeparator());
		
	}
	
	public static Object getService(Class<?> service){
		return Neo.getObject(service);
	}
	
	/**
	 * Responsável por interpretar códigos ean 128.
	 * 
	 * Caso não seja um código ean128, a mesma string de entrada será retornada.
	 * 
	 * @param codigo
	 * @author Pedro Gonçalves
	 * @return
	 */
	protected String getCodigoProduto(String codigo){
		//se não for um código ean passar direto.
		if(codigo == null || codigo.equals("")){
			return codigo;
		}
		
		//Verifica se não é um código EAN128
		if(!codigo.startsWith("]C")){
			
			//Códigos EAN128 com defeito
			if(codigo.startsWith("8006") && codigo.length() > 20){
				String codigoTemp = codigo.substring(5,22);
				
				//verifica se possui volume
				if ("0101".equals(codigo.substring(21,25)))
					codigoTemp = codigoTemp.substring(0,13);
				
				return codigoTemp;
			} else
				return codigo;
		} else {
			if(codigo.startsWith("]C1010")) 
				return codigo.substring(6,19);
			else if(codigo.startsWith("]C") && codigo.length() > 20){
			
				String codigoTemp = codigo.substring(8,25);
				
				//verifica se possui volume
				if ("0101".equals(codigo.substring(21,25)))
					codigoTemp = codigoTemp.substring(0,13);
				
				return codigoTemp;
			} else 
				return ".";//Retorna um valor para que seja dado como inválido
		}
	}
}
