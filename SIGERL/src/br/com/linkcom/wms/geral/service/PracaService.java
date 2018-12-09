package br.com.linkcom.wms.geral.service;

import java.util.Iterator;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.dao.PracaDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PracaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */
public class PracaService extends GenericService<Praca> {
	
	private PracaDAO pracaDAO;
	
	public void setPracaDAO(PracaDAO pracaDAO) {
		this.pracaDAO = pracaDAO;
	}
	
	@Override
	public void saveOrUpdate(Praca bean) {
		bean.setDeposito(WmsUtil.getDeposito());
		super.saveOrUpdate(bean);
	}
	
	/**
	 * @author Guilherme Arantes de Oliveira
	 * 
	 * Metodo de validacao do valor do cep informado pelo usuario
	 * 
	 * @see br.com.linkcom.wms.geral.service.PracaService#validarBean(bean)  
	 * @param bean
	 * @return
	 */
	public boolean validarEntradaCep(Praca bean) {
		validarBean(bean);
		boolean result = true;
		int beanCepInicio = Integer.parseInt(bean.getCepinicio().getValue());
		int beanCepFim = Integer.parseInt(bean.getCepfim().getValue());
		
		if(!(beanCepInicio <= beanCepFim))
			result = false;
		
		return result;
	}
	
	/**
	 * @author Guilherme Arantes de Oliveira
	 * 
	 * Metodo de validacao para verificar se houve interseccao dos dados informados pelo usuario 
	 * com os dados cadastrados no banco de dados.
	 * 
	 * @see br.com.linkcom.wms.geral.service.PracaService#validarBean(bean)
	 * @see br.com.linkcom.wms.geral.dao.PracaDAO#findAll()  
	 * @param bean
	 * @return
	 */
	public boolean validarRangeCep(Praca bean) {
		
		validarBean(bean);
		
		if(bean.getTiporotapraca()!=null && bean.getTiporotapraca().getCdtiporotapraca()!=null && bean.getTiporotapraca().getCdtiporotapraca()==1){
			return true;
		}
		
		int beanCepInicio = -1;
		int beanCepFim = -1;
		int pracaCepInico = -1;
		int pracaCepFim = -1;
		List<Praca> pracas = pracaDAO.findAllByTiporotapraca(bean);

		for(Iterator<Praca> iterator = pracas.iterator(); iterator.hasNext();) {
			Praca praca = (Praca) iterator.next();
			beanCepInicio = Integer.parseInt(bean.getCepinicio().getValue()); 
			beanCepFim = Integer.parseInt(bean.getCepfim().getValue());
			pracaCepInico = Integer.parseInt(praca.getCepinicio().getValue());
			pracaCepFim = Integer.parseInt(praca.getCepfim().getValue());
			
			if(bean.getCdpraca() == null) {
				if(!((beanCepFim < pracaCepInico) || (beanCepInicio > pracaCepFim))) {
					return false;
				}	
			
			} else {
				if((beanCepInicio != pracaCepInico) && (beanCepFim != pracaCepFim)) {
					if(!((beanCepFim < pracaCepInico) || (beanCepInicio > pracaCepFim))) {				
						return false;
					}	
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @author Guilherme Arantes de Oliveira
	 * 
	 * Metodo de validacao que verifica se o bean, nome, cep fim ou cep inicio sao nulos
	 *  
	 * @param bean
	 */
	private void validarBean(Praca bean) {
		if((bean == null) || (bean.getNome() == null) || (bean.getCepfim() == null) || (bean.getCepinicio() == null))
			throw new WmsException("Parâmetros inválidos.");
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PracaDAO#findBy(Rota)
	 * @author Pedro Gonçalves
	 * @return
	 */
	public List<Praca> findBy(Rota rota) {
		return pracaDAO.findBy(rota);
	}
	
	
	private static PracaService instance;
	public static PracaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PracaService.class);
		}
		return instance;
	}
	
	/**
	 * Método que faz listagem padrão do flex
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Praca> findForListagemFlex(PracaFiltro pracaFiltro) {
		return pracaDAO.findForListagem(pracaFiltro).list();
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.PracaService#findByPraca(Praca praca)
	 * 
	 * @param praca
	 * @return
	 */
	public Praca findByPraca(Praca praca) {
		return pracaDAO.findByPraca(praca);
	}
	
	public List<Praca> findPracasByDeposito(Deposito deposito){
		return pracaDAO.findPracasByDeposito(deposito);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param nome
	 * @return
	 * @author Tomás Rabelo
	 */
//	public List<Praca> findByNomeAutoComplete(String nome){
//		return pracaDAO.findByNomeAutoComplete(nome);
//	}

	/**
	 * @param nome
	 * @return
	 * @author Marcus Vinicius
	 */
	public List<Praca> findByRotaPracaAutoComplete(String nome){
	  	return pracaDAO.findByRotaPracaAutoComplete(nome);
	}
	
	/**
	 * 
	 * @param praca
	 * @return
	 */
	public List<Praca> findAllByTiporotapraca (Praca praca){
		return pracaDAO.findAllByTiporotapraca(praca);
	}

	/**
	 * 
	 * @param rota
	 * @param cepinicio
	 * @param cepfim
	 * @param nome
	 * @param tipoentrega 
	 * @return
	 */
	public List<Praca> findByTabelaFrete(Rota rota, Cep cepinicio, Cep cepfim, String nome, Tipoentrega tipoentrega){
		return pracaDAO.findByTabelaFrete(rota, cepinicio, cepfim, nome, tipoentrega);
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Praca> findPracaByNotaAutocomplete(String param){
		return pracaDAO.findPracaByNotaAutocomplete(param);
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Praca> findByFiltro(PracaFiltro filtro) {
		return pracaDAO.findByFiltro(filtro);
	}

	/**
	 * 
	 * @param cdrota
	 * @return
	 */
	public List<Praca> findByRota(Integer cdrota) {
		return pracaDAO.findByRota(cdrota);
	}
	
	/**
	 * 
	 * @param filtro
	 * @param pracasParaExcluir
	 * @return
	 */
	public List<Praca> findPracasDisponiveis(Praca filtro, String pracasParaExcluir){
		return pracaDAO.findPracasDisponiveis(filtro, pracasParaExcluir);
	}
}
