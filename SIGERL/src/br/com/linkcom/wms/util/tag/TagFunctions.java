package br.com.linkcom.wms.util.tag;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.service.PessoaService;
import br.com.linkcom.wms.geral.service.TelaService;


public class TagFunctions {
	
	public static String truncate(String texto,Integer size) {
		if(texto != null && texto.length() >= size)
			texto = texto.substring(0, size)+" ...";
		
		return texto;
	}

	public static String getTelaDescricao() {
		return TelaService.getInstance().getTelaDescriptionByUrl(getPartialURL());
	}
	
	public static String getPartialURL(){
		return Util.web.getFirstUrl();
	}
	
	public static String getTotalPage(FiltroListagem filtro){
		if((filtro.getCurrentPage() + 1) == filtro.getNumberOfPages())
			return String.valueOf(filtro.getNumberOfResults());
		else
			return String.valueOf((filtro.getCurrentPage() + 1) * filtro.getPageSize());
	}
	
	public static String findUserByCd(Integer code){
		return PessoaService.getInstance().findPessoaByCodigo(code).getNome();
	}
	
	public static String formataData(Date data){
		if(data!=null)		{
			return new SimpleDateFormat("dd/MM/yyyy").format(data);
		}
		else{
			return "";
		}
	}
	
	public static String formataData(Date data, String pattern){
		if(data!=null)		{
			return new SimpleDateFormat(pattern).format(data);
		}
		else{
			return "";
		}
	}
	
	public static Long getQtde(Long qtde, Produtoembalagem produtoembalagem){
		if (produtoembalagem != null)
			return qtde / produtoembalagem.getQtde();
		else
			return qtde;
	}
	
	public static String getMessage(String key){
		return Util.locale.getBundleKey(key);
	}
	
	/**
	 * Concatena os valores de uma ou mais propriedades utilizando um separador.
	 * Para informar mais de uma propridade separe elas por vírgula.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param lista
	 *            Uma lista de objetos.
	 * @param propriedade
	 *            A propriedade que deve ser concatenada.
	 * @param separador
	 *            O separador que será utilizado.
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static String concatenar(List<?> lista, String propriedade, String separador) {
		
		if (lista == null)
			return "";
		
		StringBuilder builder = new StringBuilder();
		try{
			String[] campos = propriedade.split(",");
			
			boolean incluirSeparador = false;
			for (Object item : lista){
				
				String str = "";
				for (int i = 0; i < campos.length; i++){
					Object value = PropertyUtils.getNestedProperty(item, campos[i]);
					if (value != null && !value.toString().isEmpty()){
						if (i > 0 && str.length() > 0)
							str += " - ";
						str += value;
					}
					
					if (!str.isEmpty()){
						if (incluirSeparador && builder.length() > 0)
							builder.append(separador);
						builder.append(str);
						incluirSeparador = true;
					}
				}
			}
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao ler a propriedade.";
		}
	}
	
	/**
	 * Formata a data para exibir apenas o semestre e o ano.
	 * 
	 * @author Giovane Freitas
	 * @param date
	 * @return
	 */
	public static String formataSemestre(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (calendar.get(Calendar.MONTH) <= calendar.JUNE ? "1" : "2") + "/" + calendar.get(Calendar.YEAR);
	}
}
