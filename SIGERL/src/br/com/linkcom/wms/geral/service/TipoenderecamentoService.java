package br.com.linkcom.wms.geral.service;

import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.dao.TipoenderecamentoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TipoenderecamentoService extends GenericService<Tipoenderecamento>{
	
	protected TipoenderecamentoDAO tipoenderecamentoDAO;
	
	public void setTipoenderecamentoDAO(TipoenderecamentoDAO tipoenderecamentoDAO) {
		this.tipoenderecamentoDAO = tipoenderecamentoDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TipoenderecamentoDAO#atualizarTipoEnderecamentoRecebimento(Recebimento recebimento, Tipoenderecamento tipoenderecamento)
	 * 
	 * @param recebimento
	 * @param tipoenderecamento
	 */
	public void atualizarTipoEnderecamentoRecebimento(Recebimento recebimento,Tipoenderecamento tipoenderecamento) {
		tipoenderecamentoDAO.atualizarTipoEnderecamentoRecebimento(recebimento,tipoenderecamento);
	}

}
