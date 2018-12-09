package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Boxstatus;
import br.com.linkcom.wms.geral.dao.BoxDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class BoxService extends GenericService<Box> {
	
	private BoxDAO boxDAO;
	private static BoxService instance;

	public static BoxService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(BoxService.class);
		}
		return instance;
	}
	
	public void setBoxDAO(BoxDAO boxDAO) {
		this.boxDAO = boxDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.BoxDAO#findAllDisponiveis()
	 * @author Pedro Gonçalves
	 * @return
	 */
	public List<Box> findAllDisponiveis() {
		return boxDAO.findAllDisponiveis();
	} 
	
	/**
	 * Método que carrega box's para preencher combo no flex
	 * 
	 * @return
	 */
	public List<Box> findAllFoxFlex() {
		return boxDAO.findForCombo(null, new String[]{});
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param whereIn
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Box> getAllBoxesForSeparacao(String whereIn) {
		return boxDAO.getAllBoxesForSeparacao(whereIn);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param box
	 * @param boxstatus 
	 * @author Tomás Rabelo
	 */
	public void updateStatusBox(Box box, Boxstatus boxstatus) {
		boxDAO.updateStatusBox(box, boxstatus);
	}
}
