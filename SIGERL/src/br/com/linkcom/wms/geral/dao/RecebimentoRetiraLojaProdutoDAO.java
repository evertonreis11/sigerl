package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RecebimentoRetiraLojaProdutoDAO extends GenericDAO<RecebimentoRetiraLojaProduto> {

	/**
	 * Altera a situação do produto.
	 *
	 * @param cdRecebimentoRetiraLojaProduto the cd recebimento retira loja produto
	 * @param tipoEstoque the tipo estoque
	 */
	public void alterarSituacaoProduto(Integer cdRecebimentoRetiraLojaProduto, TipoEstoque tipoEstoque) {
		getHibernateTemplate().bulkUpdate("update RecebimentoRetiraLojaProduto set tipoEstoque = ? where cdRecebimentoRetiraLojaProduto = ? ", 
				new Object[]{tipoEstoque, cdRecebimentoRetiraLojaProduto});
	}

}
