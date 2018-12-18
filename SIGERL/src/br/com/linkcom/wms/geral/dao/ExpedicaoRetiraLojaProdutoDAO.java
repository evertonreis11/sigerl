package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.ConferenciaExpedicaoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaProduto;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ExpedicaoRetiraLojaProdutoDAO extends GenericDAO<ExpedicaoRetiraLojaProduto> {

	public ExpedicaoRetiraLojaProduto recuperaProdutoExpedicaoPorEan(String codigoBarras, Integer cdExpedicaoRetiraLoja,
			Integer cdnotafiscalsaida) {
		
		QueryBuilder<ExpedicaoRetiraLojaProduto> qb = criaQueryBuliderGenerico();
		
		qb.where("produtoCodigoBarras.codigo = ?", codigoBarras);
		qb.where("expedicaoRetiraLoja.cdExpedicaoRetiraLoja = ?", cdExpedicaoRetiraLoja);
		qb.where("notaFiscalSaida.cdnotafiscalsaida = ?", cdnotafiscalsaida);
		qb.where("conferenciaExpedicaoRetiraLojaStatus = ?", ConferenciaExpedicaoRetiraLojaStatus.AGUARDANDO_CONFERENCIA);
		
		return qb.unique();
	}

	private QueryBuilder<ExpedicaoRetiraLojaProduto> criaQueryBuliderGenerico() {
		QueryBuilder<ExpedicaoRetiraLojaProduto> qb = query();

		qb.select("expedicaoRetiraLojaProduto.cdExpedicaoRetiraLojaProduto, expedicaoRetiraLojaProduto.qtde, " +
				 "conferenciaExpedicaoRetiraLojaStatus.cdConfExpedicaoRetLojaStatus, conferenciaExpedicaoRetiraLojaStatus.nome, "+
				 "expedicaoRetiraLoja.cdExpedicaoRetiraLoja, notaFiscalSaida.cdnotafiscalsaida, notaFiscalSaida.chavenfe, "+
				 "produto.cdproduto, produto.codigo, produto.descricao, produtoCodigoBarras.cdprodutocodigobarras, " + 
				 "produtoCodigoBarras.codigo, notaFiscalSaidaProduto.cdnotafiscalsaidaproduto");
		
		qb.join("expedicaoRetiraLojaProduto.expedicaoRetiraLoja expedicaoRetiraLoja")
		  .join("expedicaoRetiraLoja.notaFiscalSaida notaFiscalSaida")
		  .join("expedicaoRetiraLojaProduto.conferenciaExpedicaoRetiraLojaStatus conferenciaExpedicaoRetiraLojaStatus")
		  .join("expedicaoRetiraLojaProduto.produto produto")
		  .join("expedicaoRetiraLojaProduto.notaFiscalSaidaProduto notaFiscalSaidaProduto")
		  .join("produto.listaProdutoCodigoDeBarras produtoCodigoBarras");
		
		return qb;
	}

	public List<ExpedicaoRetiraLojaProduto> recuperaProdutosSemConferenciaPorExpedicao(Integer cdExpedicaoRetiraLoja) {
		
		QueryBuilder<ExpedicaoRetiraLojaProduto> qb = criaQueryBuliderGenerico();
		
		qb.where("expedicaoRetiraLoja.cdExpedicaoRetiraLoja = ?", cdExpedicaoRetiraLoja);
		qb.where("conferenciaExpedicaoRetiraLojaStatus = ?", ConferenciaExpedicaoRetiraLojaStatus.AGUARDANDO_CONFERENCIA);
		
		return qb.list();
	}

}
