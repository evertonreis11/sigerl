package br.com.linkcom.wms.geral.service;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Embalagemexpedicaoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.dao.EmbalagemexpedicaoprodutoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EmbalagemexpedicaoprodutoService extends GenericService<Embalagemexpedicaoproduto> {
	
	private static EmbalagemexpedicaoprodutoService instance;
	private EmbalagemexpedicaoprodutoDAO embalagemexpedicaoprodutoDAO;

	public static EmbalagemexpedicaoprodutoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(EmbalagemexpedicaoprodutoService.class);
		}
		return instance;
	}
	
	public void setEmbalagemexpedicaoprodutoDAO(
			EmbalagemexpedicaoprodutoDAO embalagemexpedicaoprodutoDAO) {
		this.embalagemexpedicaoprodutoDAO = embalagemexpedicaoprodutoDAO;
	}

	/**
	 * Exclui todas as embalagens associadas a uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public void deleteByOrdem(Ordemservico ordemservico) {
		embalagemexpedicaoprodutoDAO.deleteByOrdem(ordemservico);
	}

}
