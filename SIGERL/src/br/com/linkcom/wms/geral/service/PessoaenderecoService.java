package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Pessoa;
import br.com.linkcom.wms.geral.bean.Pessoaendereco;
import br.com.linkcom.wms.geral.dao.PessoaenderecoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PessoaenderecoService extends GenericService<Pessoaendereco> {

	protected PessoaenderecoDAO pessoaenderecoDAO;
	
	public void setPessoaenderecoDAO(PessoaenderecoDAO pessoaenderecoDAO) {
		this.pessoaenderecoDAO = pessoaenderecoDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * @param form
	 * @return
	 */
	public List<Pessoaendereco> findByPessoa(Pessoa pessoa) {
		return pessoaenderecoDAO.findByPessoa(pessoa);
	}

	
	/* singleton */
	private static PessoaenderecoService instance;
	public static PessoaenderecoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PessoaenderecoService.class);
		}
		return instance;
	}
	
	
	public Pessoaendereco findByCodigoERP(Long codigoERP){
		return pessoaenderecoDAO.findByCodigoERP(codigoERP);
	}

	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Cep findCepByPessoa(Cliente filial) {
		Pessoaendereco pessoaendereco = pessoaenderecoDAO.findCepByPessoa(filial);
		if(pessoaendereco!=null && pessoaendereco.getCep()!=null){
			return pessoaendereco.getCep();
		}
		return null;
	}
}
