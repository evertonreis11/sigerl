package br.com.linkcom.wms.geral.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.vo.RotaOcupacaoVO;
import br.com.linkcom.wms.geral.dao.RotaDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.RotaFiltro;
import br.com.linkcom.wms.util.DatabaseError;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */
public class RotaService extends GenericService<Rota> {
	
	private static RotaService instance;
	private RotaDAO rotaDAO;
	private RotadiadeentregaService rotadiadeentregaService;
	private RotapracaService rotapracaService;
	private RotaturnoextraService rotaturnoextraService;
	private RotaturnodeentregaService rotaturnodeentregaService;
	private TransactionTemplate transactionTemplate;

	public void setRotaDAO(RotaDAO rotaDAO) {
		this.rotaDAO = rotaDAO;
	}
	
	public void setRotadiadeentregaService(RotadiadeentregaService rotadiadeentregaService) {
		this.rotadiadeentregaService = rotadiadeentregaService;
	}

	public void setRotapracaService(RotapracaService rotapracaService) {
		this.rotapracaService = rotapracaService;
	}

	public void setRotaturnoextraService(RotaturnoextraService rotaturnoextraService) {
		this.rotaturnoextraService = rotaturnoextraService;
	}

	public void setRotaturnodeentregaService(RotaturnodeentregaService rotaturnodeentregaService) {
		this.rotaturnodeentregaService = rotaturnodeentregaService;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public static RotaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(RotaService.class);
		}
		return instance;
	}

	@Override
	public void saveOrUpdate(Rota bean) {
		try {
			bean.setDeposito(WmsUtil.getDeposito());
			
			super.saveOrUpdate(bean);
			
		} catch (DataIntegrityViolationException e) {			
			if(DatabaseError.isKeyPresent(e, "IDX_ROTA_NOME")) {
				throw new WmsException("Rota já cadastrada no sistema.");				
			}else
				throw e;
		}
	}
	
	@Override
	public void delete(final Rota rota) {
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status){
				rotadiadeentregaService.deleteByRota(rota);
				rotapracaService.deleteByRota(rota);
				rotaturnoextraService.deleteByRota(rota);
				rotaturnodeentregaService.deleteByRota(rota);
				rotaDAO.delete(rota);
				return null;
			}
		});
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.RotaDAO.findAllForCarregamento()
	 * @return
	 */
	public List<Rota> findAllForCarregamento(){
		return rotaDAO.findAllForCarregamento();
	}
	
	/**
	 * Método que carrega rotas para preencher combo no flex
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Rota> findAllForFlex(){
		List<Rota> listaRotas = rotaDAO.findAllForFlex();
		listaRotas.add(Rota.ROTA_NAO_ENCONTRADA);
		Collections.sort(listaRotas, new BeanComparator("nome"));
		return listaRotas;
	}
	
	/**
	 * 
	 * @param rota
	 * @return
	 */
	public List<RotaOcupacaoVO> getInfoRotaReloja(JdbcTemplate jdbcTemplate, RotaFiltro filtro) {
		return rotaDAO.getInfoRotaReloja(jdbcTemplate,filtro);
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<Integer, Date> findRotasCortadas(String whereIn) {		
		return rotaDAO.findRotasCortadas(whereIn);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Rota> findRotaWithTipoAutocomplete(String param){
		return rotaDAO.findRotaWithTipoAutocomplete(param);
	}
	
	/**
	 * Recupera rota por praca para validacao transbordo.
	 *
	 * @param praca the praca
	 * @return the rota
	 */
	public Rota recuperaRotaPorPracaParaValidacaoTransbordo(Praca praca){
		return rotaDAO.recuperaRotaPorPracaParaValidacaoTransbordo(praca);
	}
} 
