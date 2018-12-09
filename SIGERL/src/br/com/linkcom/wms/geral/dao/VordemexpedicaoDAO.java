package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Vordemexpedicao;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VordemexpedicaoDAO extends GenericDAO<Vordemexpedicao> {
	
	/**
	 * Busca todos os dados da view através do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @param listaOS 
	 * @return
	 */
	public List<Vordemexpedicao> findByCarregamento(Carregamento carregamento){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Dados inválidos no carregamento.");
		return query()
				.select("vordemexpedicao.cdcarregamento,vordemexpedicao.cdordemservico,vordemexpedicao.cdcliente, vordemexpedicao.cdtipooperacao, vordemexpedicao.cddeposito")
				.where("vordemexpedicao.cdcarregamento = ?",carregamento.getCdcarregamento())
				.orderBy("vordemexpedicao.cdcarregamento,vordemexpedicao.cdordemservico,vordemexpedicao.cdcliente,vordemexpedicao.cdtipooperacao")
				.list();
	}

}
