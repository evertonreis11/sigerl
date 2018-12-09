package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Pessoa;
import br.com.linkcom.wms.geral.bean.Pessoaendereco;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PessoaenderecoDAO extends GenericDAO<Pessoaendereco> {
	
	/**
	 * Busca os endereços da Pessoa
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param form
	 * @return
	 */
	public List<Pessoaendereco> findByPessoa(Pessoa pessoa) {
		return query()
				.select("pessoaendereco.cdpessoaendereco, pessoaendereco.cep,pessoaendereco.logradouro," +
						"pessoaendereco.numero,pessoaendereco.complemento,pessoaendereco.bairro," +
						"pessoaendereco.municipio,pessoaendereco.uf")
				.join("pessoaendereco.pessoa pessoa")
				.where("pessoa = ?",pessoa)
				.list()
				;
	}
	
	public Pessoaendereco findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		.select("pessoaendereco.cdpessoaendereco, pessoaendereco.cep,pessoaendereco.logradouro," +
						"pessoaendereco.numero,pessoaendereco.complemento,pessoaendereco.bairro," +
						"pessoaendereco.municipio,pessoaendereco.uf, pessoaendereco.codigoERP")
		.where("pessoaendereco.codigoERP = ? ", codigoERP)
		.unique();
	}

	/**
	 * 
	 * @param filial
	 */
	public Pessoaendereco findCepByPessoa(Cliente filial) {
		
		QueryBuilder<Pessoaendereco> query = query();
		
		query.select("pessoaendereco.cep");
		query.where("pessoaendereco.pessoa = ?",filial);
		query.setMaxResults(1);
		
		return query.unique();
 	}
	
}
