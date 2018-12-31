package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLoja;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.util.WmsUtil;
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

	/**
	 * Recupera produtos recebimento por nota.
	 *
	 * @param cdnotafiscalsaida the cdnotafiscalsaida
	 * @return the list
	 */
	public List<RecebimentoRetiraLojaProduto> recuperaProdutosRecebimentoPorNota(Integer cdnotafiscalsaida) {
		QueryBuilder<ExpedicaoRetiraLoja> subQuery =  new QueryBuilder<ExpedicaoRetiraLoja>(getHibernateTemplate());

		subQuery.select("expedicaoRetiraLoja.cdExpedicaoRetiraLoja")
		.from(ExpedicaoRetiraLoja.class)
		.join("expedicaoRetiraLoja.notaFiscalSaida notaFiscalSaidaExpedicao")
		.join("expedicaoRetiraLoja.expedicaoRetiraLojaStatus expedicaoRetiraLojaStatus")
		.join("expedicaoRetiraLoja.deposito deposito")
		.where("notaFiscalSaidaExpedicao.cdnotafiscalsaida = notaFiscalSaida.cdnotafiscalsaida")
		.where("expedicaoRetiraLojaStatus.cdExpedicaoRetiraLojaStatus = 2")
		.where("deposito.cddeposito = ".concat(WmsUtil.getDeposito().getCddeposito().toString()));
		
		return query()
				.select("recebimentoRetiraLojaProduto.cdRecebimentoRetiraLojaProduto, produto.cdproduto, " +
					    "recebimentoRetiraLojaProduto.qtde, tipoEstoque.cdTipoEstoque, tipoEstoque.descricao, " + 
					    "recebimentoRetiraLoja.cdRecebimentoRetiraLoja, deposito.cddeposito ")
				.join("recebimentoRetiraLojaProduto.produto produto")
				.join("recebimentoRetiraLojaProduto.recebimentoRetiraLoja recebimentoRetiraLoja ")
				.join("recebimentoRetiraLojaProduto.tipoEstoque tipoEstoque ")
				.join("recebimentoRetiraLojaProduto.notaFiscalSaida notaFiscalSaida")
				.join("recebimentoRetiraLoja.recebimentoRetiraLojaStatus recebimentoRetiraLojaStatus")
				.join("recebimentoRetiraLoja.deposito deposito")
				.where("notaFiscalSaida.cdnotafiscalsaida = ?", cdnotafiscalsaida)
				.where("recebimentoRetiraLojaStatus = ? ", RecebimentoRetiraLojaStatus.CONCLUIDO)
				.where("deposito = ?", WmsUtil.getDeposito())
				.where("not exists ?", subQuery)
				.list();
	}

}
