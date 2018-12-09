package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.controller.popup.filter.FornecedorFiltro;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("fornecedor.nome")
public class FornecedorDAO extends GenericDAO<Fornecedor> {
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Fornecedor> query) {
		query.joinFetch("fornecedor.pessoanatureza");
		query.leftOuterJoinFetch("fornecedor.pessoaendereco");
	}

	@Override
	public void updateListagemQuery(QueryBuilder<Fornecedor> query, FiltroListagem _filtro) {
		FornecedorFiltro filtro = (FornecedorFiltro) _filtro;
		query.joinFetch("fornecedor.pessoanatureza pessoanatureza");
		query.whereLikeIgnoreAll("fornecedor.nome", filtro.getNome());
		query.where("fornecedor.ativo = ?", filtro.getAtivo());
		query.where("fornecedor.pessoanatureza = ?", filtro.getPessoanatureza());
	}
	
	/**
	 * Retorna os fornecedores com o documento
	 * @param documento
	 * @return
	 * @author Cìntia Nogueira
	 */
	public List<Fornecedor> findByDocumento(String documento){
		if(documento==null || documento.equals("")){
			throw new WmsException("O documento não pode ser nulo.");
		}
		
		return query()
				.select("fornecedor.cdpessoa, fornecedor.nome, fornecedor.documento")
				.where("fornecedor.documento=?", documento)
				.list();
	}
	
	
	/**
	 * Método que encontra um fornecedor pela pk
	 * @author Leonardo Guimarães
	 * @param cd
	 * @return
	 */
	public Fornecedor findByCd(Integer cd){
		if(cd == null){
			throw new WmsException("O cd não deve ser nulo");
		}
		return query()
					.select("fornecedor.cdpessoa,fornecedor.nome,pessoanatureza.cdpessoanatureza,fornecedor.documento," +
							"fornecedor.ativo")
					.join("fornecedor.pessoanatureza pessoanatureza")
					.where("fornecedor.cdpessoa=?",cd)
					.unique();
	}
	
	public Fornecedor findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		.select("fornecedor.cdpessoa, fornecedor.nome, fornecedor.documento, " +
				"fornecedor.ativo, fornecedor.codigoerp ")
		.where("fornecedor.codigoerp = ? ", codigoERP)
		.unique();
	}
	
}
