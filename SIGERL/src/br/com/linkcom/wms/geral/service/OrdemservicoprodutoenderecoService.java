package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.dao.OrdemservicoprodutoenderecoDAO;

public class OrdemservicoprodutoenderecoService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Ordemservicoprodutoendereco> {
	
	private OrdemservicoprodutoenderecoDAO ordemservicoprodutoenderecoDAO;
	
	public void setOrdemservicoprodutoenderecoDAO(OrdemservicoprodutoenderecoDAO ordemservicoprodutoenderecoDAO) {
		this.ordemservicoprodutoenderecoDAO = ordemservicoprodutoenderecoDAO;
	}
	
	/**
	 * excluir todos os Ordemservicoprodutoendereco através da ordemservico produto.
	 * @author Pedro gonçalves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		ordemservicoprodutoenderecoDAO.deleteAllBy(listaOSP);
	}
	
	/* singleton */
	private static OrdemservicoprodutoenderecoService instance;
	public static OrdemservicoprodutoenderecoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemservicoprodutoenderecoService.class);
		}
		return instance;
	}
	
	/**
	 * Retorna a lista de produtos de endereços da ordem de servico
	 * @param ordemservico
	 * @return
	 * @author Cíntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoenderecoDAO#findByOrdemServicoProduto(Ordemservico)
	 */
	public List<Ordemservicoprodutoendereco> findByOrdemServicoProduto(Ordemservicoproduto ordemservicoproduto){
		return ordemservicoprodutoenderecoDAO.findByOrdemServicoProduto(ordemservicoproduto);
	}
	

	/**
	 * Retorna a lista de produtos de endereços da ordem de servico
	 * @param ordemservico
	 * @return
	 * @author Giovane Freitas
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoenderecoDAO#findByOrdemServico(Ordemservico)
	 */
	public List<Ordemservicoprodutoendereco> findByOrdemServico(Ordemservico ordemservico){
		return ordemservicoprodutoenderecoDAO.findByOrdemServico(ordemservico);
	}

}
