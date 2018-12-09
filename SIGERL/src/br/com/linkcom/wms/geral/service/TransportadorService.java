package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.dao.TransportadorDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TransportadorService extends GenericService<Transportador> {

	private TransportadorDAO transportadorDAO;
	
	public void setTransportadorDAO(TransportadorDAO transportadorDAO) {
		this.transportadorDAO = transportadorDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @param cd
	 * @return
	 */
	public Transportador findByCd(Integer cd) {
		return transportadorDAO.findTransportadorByCd(cd);
	}
	
	@Override
	public void delete(Transportador bean) {
		transportadorDAO.desativa(bean);
	}

	/**
	 * Método que carrega transportadores para preencher combo no flex
	 * 
	 * @return
	 */
	public List<Transportador> findAllFlex(){
		return transportadorDAO.findForCombo(null, new String [] {});
	}
	
	/**
	 * Retorna os transportadores com o nome
	 * @param nome
	 * @return
	 * @author Cíntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.TransportadorDAO#findByName(String)
	 */
	public List<Transportador> findByName(String nome){
		return transportadorDAO.findByName(nome);
	}
	
	/**
	 * Retorna os transportadores com o documento
	 * @param documento
	 * @return
	 * @author Cìntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.TransportadorDAO#findByDocumento(String)
	 */
	public List<Transportador> findByDocumento(String documento){
		return transportadorDAO.findByDocumento(documento);
	}
	
	/* singleton */
	private static TransportadorService instance;
	public static TransportadorService getInstance() {
		if(instance == null){
			instance = Neo.getObject(TransportadorService.class);
		}
		return instance;
	}
	
	public Transportador findByDoc(String documento){
		return transportadorDAO.findByDoc(documento);
	}
	
	public List<Transportador> findForAutocompleteWithDepositoLogado (String param){
		return transportadorDAO.findForAutocompleteWithDepositoLogado(param);
	}
	
	public List<Transportador> findByDeposito (Deposito deposito){
		return transportadorDAO.findByDeposito(deposito);
	}
	
	
	
}
