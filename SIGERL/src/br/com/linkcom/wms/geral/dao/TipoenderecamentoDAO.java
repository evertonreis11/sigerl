package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class TipoenderecamentoDAO extends GenericDAO<Tipoenderecamento> {

	/**
	 * Muda o tipo de enderecamento do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @param tipoenderecamento
	 */
	public void atualizarTipoEnderecamentoRecebimento(Recebimento recebimento,Tipoenderecamento tipoenderecamento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null || 
		   tipoenderecamento == null || tipoenderecamento.getCdtipoenderecamento() == null){
			throw new WmsException("Não foi possível atualizar o recebimento.");
		}
		getHibernateTemplate().bulkUpdate("update Recebimento rec set tipoenderecamento = ? where rec = ?",new Object[]{
										 	tipoenderecamento,recebimento});
		
	}

}
