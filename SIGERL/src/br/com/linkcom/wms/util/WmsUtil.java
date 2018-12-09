package br.com.linkcom.wms.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Acaopapel;
import br.com.linkcom.wms.geral.bean.Configuracao;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.service.AcaopapelService;
import br.com.linkcom.wms.geral.service.AcompanhamentoveiculoService;
import br.com.linkcom.wms.geral.service.AgendaService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.PapelService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;


public class WmsUtil {
	
	private static final String WMSEXPEDICAO = "wmsexpedicao";
	private static final String WMSRECEBIMENTO = "wmsrecebimento";
	private static final String WMSRE = "wmsre";
	private static final String WMSCS = "wmscs";
	private static DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	public static final String DEPOSITO_KEY = "WMS_SELECTED_DEPOSITO"; 
	public static final String EMPRESA_KEY = "WMS_SELECTED_EMPRESA";	
	public static final String WMS_CACHE_ACAOPERMISSION = "wms_cache_acaopermissions"; 
	private final static String DATE_PATTERN = "\\d{1,2}/\\d{1,2}/\\d{4}";
	
	private static String CONTEXTO = null;
	private static List<String> modulosRecebimento = Arrays.asList( new String[]{"recebimento", "sistema", "adm"} );
	private static List<String> modulosExpedicao = Arrays.asList( new String[]{"expedicao", "sistema", "adm"} );
	
	/**
	 * M�todo para pegar o usu�rio logado no sistema
	 * 
	 * @return user
	 * @author Jo�o Paulo Zica
	 */
	public static Usuario getUsuarioLogado(){
		User user = Neo.getRequestContext().getUser();
		if(user instanceof Usuario){
			Usuario usuario=(Usuario) user;
			List<Papel> listaPapel = PapelService.getInstance().carregaPapel(usuario);
			usuario.setListaPapel(listaPapel);
			return usuario;
		} else {
			return null;
		}
	}
	
	/**
	 * Verifica se o usu�rio logado � administrador do sistema
	 * 
	 * @throws WmsException - Se o usu�rio n�o estiver logado
	 * @return TRUE - Se o usu�rio conter em um de seus pap�is o perfil de adminstrador.
	 * 		   FALSE - Se o usu�rio n�o conter algum de seus pap�is o perfil de administrador. 
	 */
	public static Boolean isUsuarioLogadoAdministrador(){
		Usuario usuarioLogado = WmsUtil.getUsuarioLogado();
		if (usuarioLogado == null) 
			throw new WmsException("N�o foi encontrado usu�rio logado.");
		
		Set<Usuariopapel> listaUsuariopapel = usuarioLogado.getListaUsuariopapel();
		boolean isadmin = false;
		if(listaUsuariopapel != null){
			for (Usuariopapel usuariopapel : listaUsuariopapel) {
				if(usuariopapel.getPapel().getAdministrador()){
					isadmin = true;
					break;
				}
			}
		}
		return isadmin;
	}
	
	/**
	 * M�todo para pegar o usu�rio logado no sistema
	 * 
	 * @return user
	 * @author Jo�o Paulo Zica
	 */
	public static Usuario getUsuarioLogado(Usuario user){
		if(user instanceof Usuario){
			Usuario usuario=(Usuario) user;
			List<Papel> listaPapel = PapelService.getInstance().carregaPapel(usuario);
			usuario.setListaPapel(listaPapel);
			return usuario;
			
		} else {
			return null;
		}
	}
	
	/**
	 * Verifica se o usu�rio logado � administrador do sistema
	 * 
	 * @throws WmsException - Se o usu�rio n�o estiver logado
	 * @return TRUE - Se o usu�rio conter em um de seus pap�is o perfil de adminstrador.
	 * 		   FALSE - Se o usu�rio n�o conter algum de seus pap�is o perfil de administrador. 
	 */
	public static Boolean isUsuarioLogadoAdministrador(Usuario user){
		Usuario usuarioLogado = WmsUtil.getUsuarioLogado(user);
		if (usuarioLogado == null) 
			throw new WmsException("N�o foi encontrado usu�rio logado.");
		
		Set<Usuariopapel> listaUsuariopapel = usuarioLogado.getListaUsuariopapel();
		boolean isadmin = false;
		if(listaUsuariopapel != null){
			for (Usuariopapel usuariopapel : listaUsuariopapel) {
				if(usuariopapel.getPapel().getAdministrador()){
					isadmin = true;
					break;
				}
			}
		}
		return isadmin;
	}
	
	
	
	/**
	 * M�todo para trazer a descri��o completa do m�dulo a partir da URL
	 * 
	 * @author Pedro Gon�alves
	 * @return nome
	 */
	public static String getNomeModulo() {
		String modulo = (String) NeoWeb.getRequestContext().getAttribute("NEO_MODULO");
		
		if ("adm".equals(modulo)) modulo = "Administra��o";
		else if ("sistema".equals(modulo)) modulo = "Sistema";
		else if ("expedicao".equals(modulo)) modulo = "Expedi��o";
		else if ("logistica".equals(modulo)) modulo = "Log�stica";
		else if ("recebimento".equals(modulo)) modulo = "Recebimento";
		
		return modulo;
	}
	
	public static String getContex() {
		return NeoWeb.getRequestContext().getServletRequest().getContextPath();
	}
	
	/**
	 * M�todo usado para se validar codigo de barras
	 * @author Leonardo Guimar�es
	 * @param cb
	 * @return true se for v�lido e false caso n�o seja v�lido
	 */
	public static boolean validaCodigoDeBarras(String cb){
		return calculaDigitoVerificadorCodigoDeBarras(cb.substring(0, cb.length()-1)) == Integer.parseInt(cb.substring(cb.length()-1, cb.length()));
	}
	
	/**
	 * M�todo usado para se calcular d�gito verificador de codigo de barras
	 * @author Fabr�cio
	 * @param cb
	 * @return d�gito verificador
	 */
	public static int calculaDigitoVerificadorCodigoDeBarras(String cb){
		int aux = 3;
		int soma = 0;
		cb = cb.trim();
		for(int i = cb.length();i>0;i--){
			soma += Integer.parseInt(cb.substring(i-1,i)) * aux;
			aux = 4-aux;
		}
		int dv=(1000-soma)%10;
		if(dv==10){
			dv=0;
		}
		return dv;
	}
	
	public static Deposito getDeposito(){
		Deposito deposito = (Deposito) NeoWeb.getRequestContext().getSession().getAttribute(WmsUtil.DEPOSITO_KEY);
		deposito.setListaUsuarioDeposito(null);
		deposito.setListaRecebimento(null);
		deposito.setListaTipoendereco(null);
		deposito.setListaArea(null);
		return deposito;
	}
	
	/**
	 * M�todo usado para obter uma data de uma string de par�metro
	 * @author Leonardo Guimar�es
	 * @param request
	 * @return
	 */
	public static Date getData(String request){
		String[] aux=request.split("/");
		GregorianCalendar data = new GregorianCalendar(Integer.parseInt(aux[2]),Integer.parseInt(aux[1])-1,
													  Integer.parseInt(aux[0]));
		return data.getTime();
	}
	
	/**
	 * M�todo usado para obter o cd de uma string de par�metro
	 * @author Leonardo Guimar�es
	 * @param request
	 * @return
	 */
	public static Integer getCd(String request){
		if((request.equals("<null>"))||(request==null)){
			return 0;
		}
		request=request.substring(request.indexOf("=")+1,request.indexOf("]"));
		return Integer.parseInt(request);
	}
	
	/**
	 * Concatena o vararg e monta a string do select
	 * @param vetor
	 * @author Pedro Gon�alves
	 * @return
	 */
	public static String makeSelectClause(String clause,String [] vetor){
		if(vetor.length > 0)			
			return CollectionsUtil.concatenate(Arrays.asList(vetor),", ");
		else return clause;
	}
	
	/**
	 * M�todo que retorna true caso o valor seja "true" ou null,
	 * false caso o valor seja "false" ou null caso valor seja "nulo"
	 * @author Leonardo Guimar�es
	 * @param valor
	 * @return
	 */
	public static Boolean getBoolean(String valor) {
		if(valor != null){
			if(valor.equals("true")){
				return Boolean.TRUE;
			}
			else{
				if(valor.equals("nulo") || valor.equals("null")){
					return null;
				} else{
					if(valor.equals("false")){
						return Boolean.FALSE;
					}
				}
			}											
		}
		return Boolean.FALSE;
	}
	
	/**
     * Abrevia um nome com base no tamanho m�ximo fornecido como par�metro
     * 
     * @author Leonardo Guimar�es
     * 
     * @see br.com.linkcom.wms.util.WmsUtil.ContainsDe(String nome)
     * 
     * @param nome - Nome que ter� os sobrenomes abreviados
     * @param maxLength - Tamanho m�ximo que o nome abreviado poder� ter
     * @return Nome abreviado
	 * @throws WmsException - Quando o nome for nulo
     * @throws WmsException - Quando maxLenght for menor que zero
     */
    public static String abreviaNome(String nome,int maxLength) throws WmsException{
    	if(nome == null){
    		throw new WmsException("O par�metro nome n�o deve ser nulo.");
    	}
        nome = nome.trim();
        if(maxLength < 0){
            throw new WmsException("O par�metro maxLenght n�o deve ser menor que zero.");
        }
        if(nome.length() <= maxLength){
            return nome;
        }
        // Se o nome possui espa�os
        if(nome.indexOf(" ") != -1){
        	nome = nome.replaceAll("( ) +"," ");
            List<String> sobrenomes = new ArrayList<String>();
            int indexOf = nome.indexOf(" ");
            int indexOf2;
            String aux;
            // Adiciona os sobrenomes em uma lista
            while(indexOf != -1){
                indexOf2 = nome.indexOf(" ",indexOf+1) == -1 ? nome.length() : nome.indexOf(" ",indexOf+1);
                aux = nome.substring(indexOf,indexOf2);
                if(ContainsDe(aux) || indexOf2 == nome.length()){
                	indexOf = nome.indexOf(" ",indexOf2);
                	continue;
                }
                sobrenomes.add(nome.substring(indexOf,indexOf2));
                indexOf = nome.indexOf(" ",indexOf2);
                
            }
            // Oredena a lista de sobrenomes em ordem decrescente
            Collections.sort(sobrenomes,new Comparator<String>(){
                public int compare(String string , String string2) {
                    if(string.length() > string2.length())
                        return -1;
                        else if(string.length() == string2.length())
                            return 0;
                        else return 1;
                }
            });
            /*
             * Adiciona o �ltimo sobrenome no final da lista para que ele so seja
             * abreviado em �ltimo caso
             */            
            String ultimoNome = nome.substring(nome.lastIndexOf(" "), nome.length());
			sobrenomes.add(ultimoNome);
            /*
             * Abrevia os sobrenomes e verifica se o tamanho do nome e menor ou igual
             * ao tamanho passado como par�metro
             */
            for (String sobrenome : sobrenomes) {
                String inicioNome = nome.substring(0,nome.indexOf(sobrenome));
                String fimNome = nome.substring(inicioNome.length() + sobrenome.length(),nome.length());
                nome = inicioNome+sobrenome.substring(0,2).toUpperCase()+"."+fimNome;
                if(nome.length() <= maxLength)
                    return nome;
            }
            /*
             * Caso depois de abreviar todos os sobrenomes o nome ainda for maior que o necess�rio
             * ser�o retirados os sobrenomes abreviados at� que o nome tenha o tamanho almejado
             */
            while(nome.length() > maxLength){
                if(nome.indexOf(".") == -1){
                    nome = nome.substring(0,maxLength-3)+"...";
                    break;
                }
                nome = nome.substring(0,nome.lastIndexOf(".")-2);
            }
            if(ContainsDe(nome.substring(nome.lastIndexOf(" "), nome.length()))){
            	nome = nome.substring(0,nome.lastIndexOf(" "));
            }
            return nome;            
        }else //Se o nome n�o possui nenhum espa�o
            return nome.substring(0,maxLength-3)+"...";
    }
	
	/**
	 * Verifica se o nome � igual a
	 * DA, DAS, DE, DO ,DOS e E
	 * @author Leonardo Guimar�es
	 * @param nome
	 * @return
	 */
	public static Boolean ContainsDe(String nome){
		if(nome!= null && !nome.equals("") && nome.length() < 4){
			List<String> listaDe = new ArrayList<String>();
			listaDe.add("DA");
			listaDe.add("DAS");
			listaDe.add("DE");
			listaDe.add("DO");
			listaDe.add("DOS");
			listaDe.add("E");
			for (String string : listaDe) {
				if(nome.toUpperCase().endsWith(string))
						return Boolean.TRUE;
			}
			
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Concatena uma lista de propriedades gerando uma 
	 * string
	 * 
	 * @author Pedro Gon�alves
	 * @param lista - Lista contendo as propriedades
	 * @param property - Nome ou caminho da propriedade
	 * @param total - N�mero de propriedades que ser�o concatenadas na string
	 * @return
	 */
	public static String concatenateWithLimit(Collection<?> lista,String property, int total) {
		if(lista == null)
			throw new WmsException("A lista n�o deve ser nula");
		else if(lista.isEmpty())
				return "";
		
		int size = lista.size();
		int ntotal = -1;

		if(total > size)
			ntotal = size;
		else 
			ntotal = total;
		
		List<?> listProperty = CollectionsUtil.getListProperty(lista, property);
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < ntotal; i++) {
			sb.append(listProperty.get(i));
			if(i+1 < ntotal)
				sb.append(", ");
		}
		
		if(size > total)
			sb.append("...");
		
		return sb.toString();
	}
	
	/**
	 * Faz o c�lculo da cubagem atrav�s do produto
	 * @see br.com.linkcom.wms.util.WmsUtil#calcularCubagemMetro(double altura, double largura, double profundidade)
	 *
	 * @author Leonardo Guimar�es
	 * 
	 * 
	 * @param produto
	 * @return - Resultado � levado em considera��o que os par�metros s�o em cm.
	 */
	public static Double calcularCubagem(double altura, double largura, double profundidade){
		return calcularCubagemMetro(altura,largura,profundidade) / 1000000;
	}
	
	/**
	 * Faz o c�lculo da cubagem atrav�s do produto
	 *
	 * @author Pedro Gon�alves
	 * 
	 * @param produto
	 * @return - Resultado � levado em considera��o que os par�metros s�o em metros.
	 */
	public static Double calcularCubagemMetro(double altura, double largura, double profundidade){
		return altura * largura * profundidade;
	}
	
	/**
	 * Encontra o config a partir de uma key.
	 * 
	 * @see br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO
	 * @see br.com.linkcom.wms.geral.service.ConfiguracaoService.getConfigValue(String key)
	 * 
	 * @param key
	 * @return a configura��o do dep�sito
	 * @author Pedro Gon�alves
	 */
	public static String getConfig(String key) {
		return ConfiguracaoService.getInstance().getConfigValue(key, getDeposito());
	}
	
	/**
	 * Encontra o config a partir de uma key.
	 * 
	 * @see br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO
	 * @see br.com.linkcom.wms.geral.service.ConfiguracaoService.getConfigValue(String key)
	 * 
	 * @param key
	 * @return a configura��o do dep�sito
	 * @author Pedro Gon�alves
	 */
	public static Boolean getBooleanConfig(String key) {
		return ConfiguracaoService.getInstance().isTrue(key, getDeposito());
	}
	
	/**
	 * Concatena os n�meros em ordem.
	 * 
	 * Ex: parametros = 1,20,2
	 * 	   retorno = 1202
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param array
	 * 
	 * @return 0 caso um long n�o suporte o n�mero concatenado 
	 */
	public static long concatenateNumbers(int ... array) {
		String numero = ""; 
		for (int j = 0; j < array.length; j++) {
			if(j != 2)
				numero += array[j] < 10 ? "00" : array[j] < 100 ? "0" : "";
			else
				numero += array[j] < 10 ? "0" : "";
			numero += array[j];
		}
		try{
			return Long.valueOf(numero);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Limpa o cache de config.
	 * 
	 * @author Pedro Gon�alves
	 */
	public static void clearCache() {
		ConfiguracaoService.getInstance().clearCache();
	}
	
	/**
	 * Mescla dois campos data e hora em um timestamp
	 * @param date
	 * @param hora
	 * @return null - Caso a data ou a hora sejam nulass
	 * 
	 * @author Pedro Gon�alves
	 */
	public static Timestamp convertDateTimeInTimestamp(java.sql.Date date, Hora hora){
		if(date != null){
			
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);
			if(hora != null){
				Calendar calendarHora = GregorianCalendar.getInstance();
				calendarHora.setTimeInMillis(hora.getTime());
				
				calendar.set(Calendar.HOUR_OF_DAY, calendarHora.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendarHora.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendarHora.get(Calendar.SECOND));
				calendar.set(Calendar.MILLISECOND, calendarHora.get(Calendar.MILLISECOND));
			}else{
				calendar.clear(Calendar.HOUR_OF_DAY);
				calendar.clear(Calendar.MINUTE);
				calendar.clear(Calendar.SECOND);
				calendar.clear(Calendar.MILLISECOND);				
			}
			
			return new Timestamp(calendar.getTimeInMillis());
			
		}
		return null;
	}

	/**
	 * Verifica se um valor foi definido. Utilizado em chamadas feitas via javascript.
	 * 
	 * @author Giovane Freitas
	 * @param value
	 * @return <code>false</code> se o value for <code>null</code>, empty ou igual a undefined. 
	 * Retornar� <code>true</code> para todos os demais casos.
	 */
	public static boolean isDefined(String value) {
		if (value == null || value.trim().isEmpty() || value.trim().equals("undefined") || value.trim().equals("null") || value.trim().equals("<null>") || value.trim().equals("nulo"))
			return false;
		else
			return true;
	}
	
	/**
	 * Retorna a descri��o do per�odo formatada de acordo com o padr�o, para
	 * ser colocado em relat�rios.
	 * 
	 * @param dtInicio
	 * @param dtFim
	 * @return
	 * @author Hugo Ferreira
	 */
	public static String getDescricaoPeriodo(Date dtInicio, Date dtFim) {
		String periodo = null;
		
		if (dtInicio != null && dtFim != null) {
			periodo = WmsUtil.toString(dtInicio) + " � " + WmsUtil.toString(dtFim);
		} else if (dtInicio != null) {
			periodo = "A partir de " + WmsUtil.toString(dtInicio);
		} else if (dtFim != null) {
			periodo = "At� " + WmsUtil.toString(dtFim);
		}
		
		return periodo;
	}

	/**
	 * Verifica se o n�vel do usu�rio tem permiss�o de acesso a a��o
	 * 
	 * @author Pedro Gon�alves
	 * 			Rodrigo Freitas - Ajsutes para ignorar as permiss�es se o usu�rio � administrador.
	 * @param acao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Boolean isUserHasAction(String acao){
		acao = acao.toUpperCase();
		HttpSession session = NeoWeb.getRequestContext().getSession();
		Object attribute = session.getAttribute(WMS_CACHE_ACAOPERMISSION);
		if(attribute == null) {
			HashMap<String, Boolean> hashModulePermission = new HashMap<String, Boolean>();
			Set<Usuariopapel> listaUsuariopapel = getUsuarioLogado().getListaUsuariopapel();
			Boolean admin = false;
			for (Usuariopapel usuariopapel : listaUsuariopapel) {
				if (usuariopapel.getPapel().getAdministrador()) {
					admin = true;
				}
			}
			
			//se � admin j� tem permiss�o a tudo.
			if (admin) {
				return true;
			} else {
				List<Acaopapel> findAllPermissionsByPapel = AcaopapelService.getInstance().findAllPermissionsByPapel(listaUsuariopapel);
				for (Acaopapel acaopapel : findAllPermissionsByPapel) {
						hashModulePermission.put(acaopapel.getAcao().getKey(), acaopapel.getPermitido());
				}
				session.setAttribute(WMS_CACHE_ACAOPERMISSION, hashModulePermission);
			}

			if(hashModulePermission.containsKey(acao)){
				return hashModulePermission.get(acao);
			} else {
				return false;
			}
		} else {
			HashMap<String, Boolean> hashModulePermission = (HashMap<String, Boolean>) attribute;
			if(hashModulePermission.containsKey(acao)){
				return hashModulePermission.get(acao);
			} else {
				return false;
			}
		}
	}

	/**
	 * Verifica se o n�vel do usu�rio tem permiss�o de acesso a a��o
	 * 
	 * @author Pedro Gon�alves
	 * 			Rodrigo Freitas - Ajsutes para ignorar as permiss�es se o usu�rio � administrador.
	 * @param acao
	 * @return
	 */
	public static Boolean isUserHasAction(String acao, Usuario user){
		
		acao = acao.toUpperCase();
		HashMap<String, Boolean> hashModulePermission = new HashMap<String, Boolean>();
		Set<Usuariopapel> listaUsuariopapel = user.getListaUsuariopapel();
		Boolean admin = false;
		
		for (Usuariopapel usuariopapel : listaUsuariopapel) {
			if (usuariopapel.getPapel().getAdministrador()) {
				admin = true;
				break;
			}
		}
		
		//se � admin j� tem permiss�o a tudo.
		if (admin) {
			return true;
		} else {
			List<Acaopapel> findAllPermissionsByPapel = AcaopapelService.getInstance().findAllPermissionsByPapel(listaUsuariopapel);
			for (Acaopapel acaopapel : findAllPermissionsByPapel) {
				hashModulePermission.put(acaopapel.getAcao().getKey(), acaopapel.getPermitido());
			}
		}

		if(hashModulePermission.containsKey(acao)){
			return hashModulePermission.get(acao);
		} else {
			return false;
		}
	}

	
	/**
	 * <p>Passa uma data para String no formato dd/MM/yyyy.</p>
	 * 
	 * @param date
	 * @return
	 * @author Hugo Ferreira
	 */
	public static String toString(java.util.Date date) {
		return format.format(date);
	}

	/**
	 * Retorna uma nova data no final do dia
	 * 
	 * @param data
	 * @return
	 * @author Tom�s Rabelo
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
	 * Retorna uma nova data no come�o do dia
	 * 
	 * @param data
	 * @return
	 * @author Tom�s Rabelo
	 */
	public static Date dateToBeginOfDay(Date data) {
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
	 * Pega o nome do cliente que contratou a linkcom, assumindo que o cliente possui
	 * uma URL personalizada para acessar o sistema seguindo o padr�o "http://nomecliente.wmsweb.com.br/wms",
	 * ou seja, a primeira parte da URL de acesso � o nome do cliente.
	 * 
	 * @author Giovane Freitas
	 * @param request
	 * @return
	 */
	public static String getClienteFromUrl(HttpServletRequest request){
		String url = request.getRequestURL().toString();
		String[] urlDividida = url.split("/");
		String nomeCliente = urlDividida[2];
		urlDividida = nomeCliente.split("\\.");
		if (urlDividida.length <= 1) {
			urlDividida = nomeCliente.split(":");
			if (urlDividida.length > 0) {
				nomeCliente = urlDividida[0];
			}
		}else
			nomeCliente = urlDividida[0];
		
		return nomeCliente;
	}
	
	/**
	 * Obt�m a logomarca que deve ser exibida no relat�rio, assumindo que cada
	 * cliente ter� uma URL espec�fica.
	 * 
	 * @return
	 */
	public static String getLogoForReport() {
		HttpServletRequest servletRequest = NeoWeb.getRequestContext().getServletRequest();

		return "/imagens/" + servletRequest.getAttribute("tema_wms") + "/LogoForReport.jpg";
	}

	/**
	 * M�todo que retorna a menor data 
	 * 
	 * @param dtentrada
	 * @param dtentrada2
	 * @return
	 * @author Tom�s Rabelo
	 */
	public static Date leastDate(java.sql.Date dtentrada, java.sql.Date dtentrada2) {
		if(dtentrada == null && dtentrada2 == null)
			return null;
		else if(dtentrada == null)
			return dtentrada2;
		else if(dtentrada2 == null)
			return dtentrada;
		else
			return dateToBeginOfDay(dtentrada).before(dateToBeginOfDay(dtentrada2)) ? dtentrada : dtentrada2;
	}

	/**
	 * M�todo que verifica quantos registros ser�o retornados a partir de um query builder.
	 * 
	 * @author Giovane Freitas
	 * @param queryBuilder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static int getNumeroRegistros(QueryBuilder<Endereco> queryBuilder) {
		//C�digo copiado da classe ListagemResult do Neo
		
		QueryBuilder<Long> countQueryBuilder = new QueryBuilder<Long>(queryBuilder.getHibernateTemplate());
        countQueryBuilder.select("count(*)");
		
		QueryBuilder.From from = queryBuilder.getFrom();
        countQueryBuilder.from(from);
        
        List<QueryBuilder<?>.Join> joins = queryBuilder.getJoins();
        for (QueryBuilder.Join join : joins) {
        	//quando estiver contando nao precisa de fazer fetch do join
			countQueryBuilder.join(join.getJoinMode(), false, join.getPath());
		}
        QueryBuilder.Where where = queryBuilder.getWhere();
		countQueryBuilder.where(where);
		countQueryBuilder.setUseTranslator(false);
		Long numeroResultados = countQueryBuilder.unique();
		if (numeroResultados != null)
			return numeroResultados.intValue();
		else
			return 0;
	}
	
	/**
	 * Verifica se as consultas devem filtrar os resultados por dep�sito.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public static boolean isFiltraDeposito(){
		return true;
	}

	/**
	 * Verifica se o estoque do carregamento deve ser baixado quando � finalizado.
	 * 
	 * @return
	 */
	public static boolean isFinalizadoBaixaEstoque() {
		return false;
	}
	
	public static java.sql.Date firstDateOfMonth(Date dataRef){
		Calendar firstData = Calendar.getInstance();
		if(dataRef != null){
			firstData.setTimeInMillis(dataRef.getTime());
		}
		firstData.set(Calendar.DAY_OF_MONTH, firstData.getActualMinimum(Calendar.DAY_OF_MONTH));
		java.sql.Date data = new java.sql.Date(firstData.getTimeInMillis());
		return data;
	}
	
	/**
	 * M�todo para obter a �ltima data do m�s atual.
	 * 
	 * @return java.sql.Date
	 * @author Flavio
	 */
	public static java.sql.Date lastDateOfMonth(Date dataRef){
		Calendar lastData = Calendar.getInstance();
		if(dataRef != null){
			lastData.setTimeInMillis(dataRef.getTime());
		}
		lastData.set(Calendar.DAY_OF_MONTH, lastData.getActualMaximum(Calendar.DAY_OF_MONTH));
		lastData.set(Calendar.HOUR_OF_DAY, lastData.getActualMaximum(Calendar.HOUR_OF_DAY));
		lastData.set(Calendar.MINUTE, lastData.getActualMaximum(Calendar.MINUTE));
		lastData.set(Calendar.SECOND, lastData.getActualMaximum(Calendar.SECOND));
		lastData.set(Calendar.MILLISECOND, lastData.getActualMaximum(Calendar.MILLISECOND));
		
		java.sql.Date data = new java.sql.Date(lastData.getTimeInMillis());
		return data;
	}
	
	/**
	 * Ap�s o primeiro login o sistema ir� atualizar informa��es no banco de dados.
	 *
	 * @param request
	 * @author Rodrigo Freitas
	 */
	public static void atualizaInformacoesPrimeiroLoginDia(HttpServletRequest request) {
		if(Neo.getUser() != null){
			if (request.getSession().getAttribute(ConfiguracaoVO.DATA_ULTIMO_ACESSO) == null){
				
				Configuracao config = ConfiguracaoService.getInstance().findByName(null, ConfiguracaoVO.DATA_ULTIMO_ACESSO);
				String valor = new SimpleDateFormat("dd/MM/yyyy").format(WmsUtil.currentDate());
				
				if(config == null){
					
					Configuracao configuracao = new Configuracao();
					configuracao.setNome(ConfiguracaoVO.DATA_ULTIMO_ACESSO);
					configuracao.setValor(valor);
					ConfiguracaoService.getInstance().saveOrUpdate(configuracao);
					
					AgendaService.getInstance().cancelaAgendamentosAntigos();
					AcompanhamentoveiculoService.getInstance().cancelaRAVsAntigos();
					
				} else {
					java.sql.Date data;
					try {
						data = WmsUtil.stringToDate(config.getValor());
					} catch (ParseException e) { 
						return;
					}
					
					if (WmsUtil.beforeIgnoreHour(data, WmsUtil.currentDate())) { 
						AgendaService.getInstance().cancelaAgendamentosAntigos();
						AcompanhamentoveiculoService.getInstance().cancelaRAVsAntigos();
						
						config.setValor(valor);
						ConfiguracaoService.getInstance().saveOrUpdate(config);
					}
				}
				
				request.getSession().setAttribute(ConfiguracaoVO.DATA_ULTIMO_ACESSO, config);
			}
		}
	}
	
	/**
	 * Verifica se a primeira data � antes da segunda data ignorando as horas.
	 *
	 * @param d1
	 * @param d2
	 * @return
	 * @author Rodrigo Freitas
	 */
	public static boolean beforeIgnoreHour(java.sql.Date d1, java.sql.Date d2) {
		if (d1 == null || d2 == null) {
			throw new NullPointerException("As datas n�o podem ser nulas");
		}
		
		Calendar c1 = javaSqlDateToCalendar(d1);
		Calendar c2 = javaSqlDateToCalendar(d2);
		
		return c1.before(c2);
	}
	
	/**
	 * Passa de java.sql.Date para Calendar. 
	 *
	 * @param date
	 * @return
	 * @author Rodrigo Freitas
	 */
	public static Calendar javaSqlDateToCalendar(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		
		
		return calendar;
	}
	
	/**
	 * Retorna a data atual.
	 *
	 * @return
	 * @author Rodrigo Freitas
	 */
	public static java.sql.Date currentDate(){
		return new java.sql.Date(System.currentTimeMillis());
	}
	
	/**
	 * Retorna a data atual.
	 *
	 * @return
	 * @author Rodrigo Freitas
	 */
	public static java.util.Date diaAtual(){
		return new java.util.Date(System.currentTimeMillis());
	}
	
	/**
	 * Transforma a string passada por par�metro para o padr�o dd/MM/yyyy.
	 *
	 * @param strDate
	 * @return
	 * @throws ParseException
	 * @author Rodrigo Freitas
	 */
	public static java.sql.Date stringToDate(String strDate) throws ParseException{
		return stringToDate(strDate, "dd/MM/yyyy");
	}
	
	/**
	 * Transforma a string passada por par�metro para o padr�o tamb�m passado por par�metro.
	 *
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 * @author Rodrigo Freitas
	 */
	public static java.sql.Date stringToDate(String strDate, String pattern) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(pattern);  
		java.sql.Date data = new java.sql.Date(format.parse(strDate).getTime()); 
		return data;
	}
             
	/**
	 * Converte a data e seu "Formato Original" (ex: yyyy-MM-dd) para o padr�o do "Formato Padr�o de Data" (dd/MM/yyyy)
	 * 
	 * @param strDate - String da data
	 * @param formatoAtual - Formato original da data
	 * @return String - data no formato padr�o
	 * @author Filipe Santos
	 */
	public static String stringToDefaulDateFormat(String strDate,String formatoAtual){
		SimpleDateFormat formatoOriginal = new SimpleDateFormat(formatoAtual); //"yyyy-MM-dd hh:mm:ss.SSS"  
		try {
			Date dataAux = new Date(formatoOriginal.parse(strDate).getTime());
			String dataAjustada = WmsUtil.format.format(dataAux);
			return dataAjustada;
		} catch (ParseException e) {
			e.printStackTrace();
			return strDate;
		} 
	}
	
	/**
	 * Transforma a string passada por par�metro para o padr�o tamb�m passado por par�metro.
	 *
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 * @author Rodrigo Freitas
	 */
	public static String dateFormatString(String strDate){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");  
		Date data = null;
		try {
			data = new Date(formato.parse(strDate).getTime());
			return data.toString();
		} catch (ParseException e) {
			e.printStackTrace();
			return strDate;
		} 
	}
	
	public static String joinWithScape(java.util.Iterator<String> iterator, String separator, String scape){
		if (iterator == null) {
            return null;
        }
        
        StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(scape);
                buf.append(obj);
                buf.append(scape);
            }
            if ((separator != null) && iterator.hasNext()) {
            	buf.append(separator);
            }
        }
        return buf.toString();
	}
	
	/**
	 * M�todo que adiciona m�s a uma determinada Data
	 * 
	 * @param dtReferencia
	 * @param meses
	 * @return
	 * @author Taidson
	 */
	public static Date addMesData(Date dtReferencia, int meses) {
		Calendar dtLimiteAux = Calendar.getInstance();
		dtLimiteAux.setTimeInMillis(dtReferencia.getTime());
		dtLimiteAux.add(Calendar.MONTH, meses);
		
		return new Date(dtLimiteAux.getTimeInMillis());
	}

	public static java.sql.Date validadeDate(String data) throws ParseException {
		if(data != null && !data.equals("")){
			if(!data.matches(DATE_PATTERN))
				data = "";
			return new java.sql.Date(format.parse(data).getTime());
		}
		return null;
	}
	
	public static boolean isWMSRecebimento(){
		return CONTEXTO.equals(WMSRECEBIMENTO);		
	}

	public static boolean isWMSExpedicao(){
		return CONTEXTO.equals(WMSEXPEDICAO);		
	}
	
	public static boolean isWMSRE(){
		return (CONTEXTO.equals(WMSRE)||CONTEXTO.equals(WMSCS));		
	}
	
	public static void setContexto(String servletContext) {
		if(CONTEXTO==null)
			CONTEXTO=servletContext;
		else throw new RuntimeException("Voc� s� pode definir o contexto uma vez");
		
	}

	public static boolean podeAcessarURL(String uri) {
		System.out.println("URI" + uri);
		String modulo = uri.split("/")[2];
		return podeAcessarModulo(modulo);
		
	}
	
	public static boolean podeAcessarModulo(String modulo) {
		if(CONTEXTO.equals(WMSRE) || CONTEXTO.equals(WMSCS)){
			return true;
		}else if(CONTEXTO.equals(WMSRECEBIMENTO) && modulosRecebimento.contains(modulo)){
			return true;
		}else if(CONTEXTO.equals(WMSEXPEDICAO) && modulosExpedicao.contains(modulo)){
			return true;
		}
			
		return false;
	}
	
	public static int getMes(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int mes = cal.get(Calendar.MONTH);
		return mes+1;
	}
	
	public static int getAno(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int ano = cal.get(Calendar.YEAR);
		return ano;
	}
	
	public static String getServerNameWithPortAndContext() {
		StringBuilder url = new StringBuilder();
			url.append(NeoWeb.getRequestContext().getServletRequest().getServerName());
			url.append(getContex());
		return url.toString();
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrlProducao() {
		
		String url = WmsUtil.getServerNameWithPortAndContext();
		
		String WMSRE = "wmsmqv.maquinadevendas.corp/wmsre";
		String TMSMV = "tmsmqv.maquinadevendas.corp/tmsmv";
		String SUPPLYMV = "supplymqv.maquinadevendas.corp/supplymv";
		
		if(url.equals(WMSRE) || url.equals(TMSMV) || url.equals(SUPPLYMV)){
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public static Empresa getEmpresa() {
		Empresa empresa = (Empresa) NeoWeb.getRequestContext().getSession().getAttribute(WmsUtil.EMPRESA_KEY);
		return empresa;
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	/**
	 * 
	 * @param campo
	 * @return
	 */
	public static String formatarStringToMoney(String campo){
		
		if(campo==null || campo.isEmpty())
			return null;
		else if(campo.equals("0"))
			return "0,00";
		else if(campo.length()==1){
			campo = "00"+campo;
		}else if(campo.length()==2){
			campo= "0"+campo;
		}
		
		String valor = campo.substring(0,campo.length()-2)+","+campo.substring(campo.length()-2,campo.length());
		return valor;
	}
	
}
