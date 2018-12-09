package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ProdutoclasseFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("produtoclasse.nome")
public class ProdutoclasseDAO extends GenericDAO<Produtoclasse> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Produtoclasse> query,	FiltroListagem _filtro) {
		ProdutoclasseFiltro filtro = (ProdutoclasseFiltro)_filtro;
		query
			.select("produtoclasse.cdprodutoclasse,produtoclasse.nome")
			.whereLikeIgnoreAll("produtoclasse.nome",filtro.getNome());
	}
	
	/**
	 * Localiza as classes de produto para a tela de agenda de verbas.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Produtoclasse> findForAgendaverba() {
		StringBuilder sql = new StringBuilder();
		sql
		.append("SELECT distinct cla.numero AS classeproduto, cla.cdprodutoclasse AS cdprodutoclasse, cla.nome AS nome ")
		.append("FROM V_produtoclasse v ")
		.append("JOIN Produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ")
		.append("ORDER BY cla.nome ");
		return getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Produtoclasse produtoclasse = new Produtoclasse();
				produtoclasse.setCdprodutoclasse(rs.getInt("cdprodutoclasse"));
				produtoclasse.setNumero(rs.getString("classeproduto"));
				produtoclasse.setNome(rs.getString("nome"));
				return produtoclasse;
			}
		});
		
/*
		return query()
			.where("length(produtoclasse.numero) <= 2")
			.orderBy("produtoclasse.numero")
			.list();
*/			
	}

	/**
	 * Busca a classe do produto pelo número.
	 *
	 * @param numero
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Produtoclasse findByNumero(String numero) {
		if(numero == null){
			throw new WmsException("Número não pode ser nulo.");
		}
		
		return query()
					.select("produtoclasse.cdprodutoclasse, produtoclasse.nome, produtoclasse.numero")
					.where("produtoclasse.numero like ?", numero)
					.unique();
	}

	public Produtoclasse loadWithV_produtoclasse(Produtoclasse produtoclasse) {
		return query()
		.joinFetch("produtoclasse.v_produtoclasse v_produtoclasse")
		.joinFetch("v_produtoclasse.produtoclasse produtoclassePai")
		.entity(produtoclasse)
		.unique();
	}

	/**
	 * 
	 * @return
	 */
	public List<Produtoclasse> findByDepartamentoGerenciador() {
		return query()
			.select("produtoclasse.cdprodutoclasse, produtoclasse.nome, produtoclasse.numero")
			.where("LENGTH(produtoclasse.numero) = 3")
			.list();
	}

	/**
	 * 
	 * @return
	 */
	public List<Produtoclasse> findAllByControleVerba() {
		return query()
			.select("produtoclasse.cdprodutoclasse, produtoclasse.nome, produtoclasse.numero")
			.where("produtoclasse.controlaverba = 1")
			.list();
	}
	
}
