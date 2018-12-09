package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipoendereco;
import br.com.linkcom.wms.geral.bean.vo.TipoenderecoVO;
import br.com.linkcom.wms.geral.dao.TipoenderecoDAO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TipoenderecoService extends GenericService<Tipoendereco> {
	
	private TipoenderecoDAO tipoenderecoDAO;
	
	public void setTipoenderecoDAO(TipoenderecoDAO tipoenderecoDAO) {
		this.tipoenderecoDAO = tipoenderecoDAO;
	}
	
	private static TipoenderecoService instance;
	public static TipoenderecoService getInstance() {
		if (instance == null)
			instance = Neo.getObject(TipoenderecoService.class);
		
		return instance;
	}

	/**
	 * 
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @param filtro
	 * @return uma lista do tipo endere�o
	 * @see br.com.linkcom.wms.geral.dao.TipoenderecoDAO#findByDeposito(Deposito)
	 * 
	 * @author Arantes
	 */
	public List<Tipoendereco> findByDeposito(Deposito filtro) {
		return tipoenderecoDAO.findByDeposito(filtro);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @param deposito
	 * @see br.com.linkcom.wms.geral.dao.TipoenderecoDAO#saveList(Deposito)
	 * 
	 * @author Pedro Gon�alves
	 * 
	 */
	public void saveList(Deposito deposito){
		tipoenderecoDAO.saveList(deposito);
	}
	
	/**
	 * Lista os tipos de endere�os cadastrados para o dep�sito atual.
	 * 
	 * @return
	 */
	public List<Tipoendereco> findByDepositoAtual(){
		return tipoenderecoDAO.findByDeposito(WmsUtil.getDeposito());
	}

	/**
	 * Coleta estat�sticas dos tipos de endere�os.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public List<TipoenderecoVO> findEstatisticas(Deposito deposito) {
		return tipoenderecoDAO.findEstatisticas(deposito);
	}
}
