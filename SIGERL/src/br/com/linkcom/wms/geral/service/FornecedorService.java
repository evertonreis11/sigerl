package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.dao.FornecedorDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class FornecedorService extends GenericService<Fornecedor> {

	private FornecedorDAO fornecedorDAO;
	
	public void setFornecedorDAO(FornecedorDAO fornecedorDAO) {
		this.fornecedorDAO = fornecedorDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @param cd
	 * @return
	 */
	public Fornecedor findByCd(Integer cd){
		return fornecedorDAO.findByCd(cd); 
	}
	
	/**
	 * Retorna os fornecedores com o documento
	 * @param documento
	 * @return
	 * @author Cìntia Nogueira
	 * @see  br.com.linkcom.wms.geral.dao.FornecedorDAO#findByDocumento(String)
	 */
	public List<Fornecedor> findByDocumento(String documento){
		return fornecedorDAO.findByDocumento(documento)	;	
	}
	
	/* singleton */
	private static FornecedorService instance;
	public static FornecedorService getInstance() {
		if(instance == null){
			instance = Neo.getObject(FornecedorService.class);
		}
		return instance;
	}
	
	public Fornecedor findByCodigoERP(Long codigoERP){
		return fornecedorDAO.findByCodigoERP(codigoERP);
	}
}
