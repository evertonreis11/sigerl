package br.com.linkcom.wms.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
	
	private static DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Limpa os campos de segundo, milisegundo, hora e minuto
	 * @param data
	 * @return
	 * @author Cíntia Nogueira
	 */
	public static Date limpaMinSegHora(Date data){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return  new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Retorna o tempo no último segundo do dia
	 * @param data
	 * @return
	 * @author Cíntia Nogueira
	 */
	public static Date getUltimoSegDia(Date data){
		java.util.Date date2 = incrementaDia(data, 1);
		data = new Date(date2.getTime());
		data= limpaMinSegHora(data);
		data= incrementaSegundo(data, -1);
		return data;
		
	}
	
	/**
	 * Acrescenta uma quantidade de dias em uma determinada data
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param numeroDias
	 * @return data
	 */	
	public static Date incrementaDia(Date data, int numeroDias) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.DAY_OF_MONTH, numeroDias);
		return new Date(calendar.getTime().getTime());
	}

	/**
	 * Decrementa uma quantidade de meses em uma determinada data
	 * @author Filipe Santos
	 * @param data
	 * @param numeroMes
	 * @return data
	 */	
	public static Date decrementaMes(Date data, int numeroMes) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.MONTH, numeroMes*(-1));
		return new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Converte um java.sql.Date para Calendar
	 * 
	 * @author Bruno Eustáquio
	 * @param java.sql.Date
	 * @return Calendar
	 */
	public static Calendar dateToCalendar(java.util.Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
	
	/**
	 * Incrementa segundo
	 * @param data
	 * @param numero
	 * @return
	 * @author Cíntia Nogueira
	 */
	public static Date incrementaSegundo(Date data, int numero) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.SECOND, numero);
		return new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Formata a Data, a Hora e o Minuto
	 * 
	 * @author Rodrigo Alvarenga
	 * @param dataHora
	 * @return String (Data,Hora e Minuto formatados)
	 */	
	public static String formataDataHoraMinuto(Timestamp dataHora) {
		if (dataHora == null) {
			return "";
		}
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formatador.format(dataHora);
	}	
	
	/**
	 * Formata a Data no padrão dd/MM/yyyy
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @return String (Data formatada)
	 */	
	public static String formataData(Date data) {
		return formataData(data,"dd/MM/yyyy");
	}	
	
	/**
	 * Formata a Data usando o padrão especificado em pattern
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param pattern
	 * @return String (Data formatada)
	 */	
	public static String formataData(Date data, String pattern) {
		if (data == null) {
			return "";
		}
		if (pattern == null || pattern.equals("")) {
			return null;
		}
		SimpleDateFormat formatador = new SimpleDateFormat(pattern);
		return formatador.format(data);
	}	
	
	/**
	 * Retorna uma nova data no começo do dia
	 * 
	 * @param data
	 * @return
	 * @author Tomás Rabelo
	 */
	public static Date dataToBeginOfDay(Date data) {
		if(data != null){
			Calendar dtAux = Calendar.getInstance();
			dtAux.setTime(data);
			dtAux.set(Calendar.HOUR_OF_DAY, 0);
			dtAux.set(Calendar.MINUTE, 0);
			dtAux.set(Calendar.SECOND, 0);
			dtAux.set(Calendar.MILLISECOND, 0);
			
			return new Date(dtAux.getTimeInMillis());
		}else{
			return null;
		
		}
	}

	/**
	 * Retorna uma nova data no final do dia
	 * 
	 * @param data
	 * @return
	 * @author Tomás Rabelo
	 */
	public static Date dataToEndOfDay(Date data) {
		if(data != null){
			Calendar dtAux = Calendar.getInstance();
			dtAux.setTime(data);
			dtAux.set(Calendar.HOUR_OF_DAY, 23);
			dtAux.set(Calendar.MINUTE, 59);
			dtAux.set(Calendar.SECOND, 59);
			dtAux.set(Calendar.MILLISECOND, 999);
			
			return new Date(dtAux.getTimeInMillis());
		}else{
			return null;
		}
	}
	
	/**
	 * Retorna a descrição do período formatada de acordo com o padrão, para
	 * ser colocado em relatórios.
	 * 
	 * @param dtInicio
	 * @param dtFim
	 * @return
	 * @author Hugo Ferreira
	 */
	public static String getDescricaoPeriodo(Date dtInicio, Date dtFim) {
		String periodo = null;
		
		if (dtInicio != null && dtFim != null) {
			periodo = format.format(dtInicio) + " a " + format.format(dtFim);
		} else if (dtInicio != null) {
			periodo = "A partir de " + format.format(dtInicio);
		} else if (dtFim != null) {
			periodo = "Até " + format.format(dtFim);
		}
		
		return periodo;
	}
	
	 /**
	  * Calcula a diferença em dias entre duas datas.
	  * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long daysBetween(Date d1, Date d2){
		return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
}
