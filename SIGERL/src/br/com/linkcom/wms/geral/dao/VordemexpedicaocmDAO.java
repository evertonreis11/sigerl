package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaocm;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VordemexpedicaocmDAO extends GenericDAO<Vordemexpedicaocm>{

	/**
	 * Busca todos os dados da view através do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Vordemexpedicaocm> findByCarregamento(Carregamento carregamento){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Dados inválidos no carregamento.");
		return query()
					 .select("vordemexpedicaocm.cdcarregamento, vordemexpedicaocm.cdcliente, vordemexpedicaocm.cdcliente, " +
					 		"vordemexpedicaocm.cdtipooperacao, vordemexpedicaocm.cdordemservico, vordemexpedicaocm.cddeposito, " +
					 		"vordemexpedicaocm.qtde, produtoembalagem.cdprodutoembalagem, vordemexpedicaocm.cdcarregamentoitem, " +
					 		"vordemexpedicaocm.cdproduto")
					 .join("vordemexpedicaocm.produtoembalagem produtoembalagem")
					 .where("vordemexpedicaocm.cdcarregamento = ?",carregamento.getCdcarregamento())
					 .where("vordemexpedicaocm.qtde > 0")
					 .orderBy("vordemexpedicaocm.cdcarregamento,vordemexpedicaocm.cdordemservico,vordemexpedicaocm.cdcliente," +
					 			"vordemexpedicaocm.cdtipooperacao, vordemexpedicaocm.cdtipooperacao, vordemexpedicaocm.cdprodutoprincipal")
					 .list();
	}
}
