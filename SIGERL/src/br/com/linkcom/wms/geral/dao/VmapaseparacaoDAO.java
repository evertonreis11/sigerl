package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vmapaseparacao;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VmapaseparacaoDAO extends GenericDAO<Vmapaseparacao>{
	
	/**
	 * Recupera todos os resultados da view "vmapaseparacao" a partir do carregamento
	 * especificado.
	 * 
	 * @param carregamento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Vmapaseparacao> findAllBy(Carregamento carregamento){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
				.where("cdcarregamento=?",carregamento.getCdcarregamento())
				.list();
	}
}
