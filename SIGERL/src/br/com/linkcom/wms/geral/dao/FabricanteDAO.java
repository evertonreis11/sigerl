package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.wms.geral.bean.Fabricante;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("fabricante.nome")
public class FabricanteDAO extends GenericDAO<Fabricante> {

	/**
	 * Método que encontra um fabricante pela pk
	 * @author Leonardo Guimarães
	 * @param cd
	 * @return
	 */
	public Fabricante findByCd(Integer cd){
		if(cd == null){
			throw new WmsException("O cd não deve ser nulo");
		}
		return query()
					.select("fabricante.cdpessoa,fabricante.nome,pessoanatureza.cdpessoanatureza,fabricante.documento," +
							"fabricante.ativo")
					.join("fabricante.pessoanatureza pessoanatureza")
					.where("fabricante.cdpessoa=?",cd)
					.unique();
	}
	
}
