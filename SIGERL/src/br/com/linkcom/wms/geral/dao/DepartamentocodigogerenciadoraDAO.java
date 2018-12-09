package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Departamentocodigogerenciadora;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class DepartamentocodigogerenciadoraDAO extends GenericDAO<Departamentocodigogerenciadora>{

	/**
	 * 
	 * @return
	 */
	public List<Departamentocodigogerenciadora> findByListagem(){
		
		QueryBuilder<Departamentocodigogerenciadora> query = query();
		
			query.select("departamentocodigogerenciadora.cddepto, produtoclasse.cdprodutoclasse, produtoclasse.nome, " +
						 "departamentogerenciadora.cdprod, departamentogerenciadora.dsproduto, departamentocodigogerenciadora.descdepto, " +
						 "departamentocodigogerenciadora.descdeptogerenciadora, departamentocodigogerenciadora.dtinclusao, " +
						 "departamentocodigogerenciadora.dtalteracao");
			query.join("departamentocodigogerenciadora.produtoclasse produtoclasse");
			query.join("departamentocodigogerenciadora.departamentogerenciadora departamentogerenciadora");
		
		return query.list();
	}

	/**
	 * 
	 * @param cdprodutoclasse
	 * @param cddepartamentogerenciadora
	 * @return
	 */
	public void updateProdutoclasseAndDepartamentogerenciadora(Integer cdprodutoclasse, Integer cddepartamentogerenciadora, Integer cddepto){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" update depto_x_codgerenciadora dc ")
			.append(" set dc.cdprodutoclasse = ").append(cdprodutoclasse).append(", dc.cddepartamentogerenciadora = ").append(cddepartamentogerenciadora)
			.append(" where dc.cddepto = ").append(cddepto); 
		
		getJdbcTemplate().execute(sql.toString());
	}

}
