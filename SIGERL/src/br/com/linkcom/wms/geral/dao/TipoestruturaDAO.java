package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Tipoestrutura;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("tipoestrutura.nome")
public class TipoestruturaDAO extends GenericDAO<Tipoestrutura> {
	
	/**
	 * Verifica se o tipo de estrutura utiliza picking ou não.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param tipoestrutura
	 * @return
	 */
	public Boolean utilizaPicking(Tipoestrutura tipoestrutura){
		if(tipoestrutura == null || tipoestrutura.getCdtipoestrutura() == null)
			throw new WmsException("Dados inválidos em tipoEstrutura.");
		return newQueryBuilderWithFrom(Boolean.class)
				.select("tipoestrutura.utilizapicking")
				.entity(tipoestrutura)
				.setUseTranslator(false)
				.unique();
	}
}
