package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.vo.RecebimentoLojaVO;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RecebimentoRetiraLojaDAO extends GenericDAO<RecebimentoRetiraLoja> {

	public RecebimentoRetiraLoja findRecebimentoLojaWithCodigoEan(String codigoEan) {
		QueryBuilder<RecebimentoRetiraLojaProduto> subQuery =  new QueryBuilder<RecebimentoRetiraLojaProduto>(getHibernateTemplate());
		
		subQuery.select("recebimentoRetiraLoja.cdRecebimentoRetiraLoja")
				.from(RecebimentoRetiraLojaProduto.class)
				.join("recebimentoRetiraLojaProduto.recebimentoRetiraLoja recebimentoRetiraLoja")
				.join("recebimentoRetiraLojaProduto.produto produto")
				.join("produto.listaProdutoCodigoDeBarras produtoCodigoDeBarras")
				.where("produtoCodigoDeBarras.codigo = ?", codigoEan)
				.where("recebimentoRetiraLojaStatus = ?", RecebimentoRetiraLojaStatus.EM_CONFERENCIA);
		
		
		QueryBuilder<RecebimentoRetiraLoja> qb = query();
		
		qb.select("recebimentoRetiraLoja.cdRecebimentoRetiraLoja, recebimentoRetiraLoja.dtRecebimento, " +
				  "usuario.cdpessoa, usuario.nome, usuario.login, deposito.cddeposito, manifesto.cdmanifesto, " +
				  "recebimentoRetiraLojaStatus.cdRecebimentoRetiraLojaStatus, recebimentoRetiraLojaStatus.nome, " +
				  "recebimentoRetiraLojaProduto.cdRecebimentoRetiraLojaProduto, recebimentoRetiraLojaProduto.qtde, " +
				  "produto.cdproduto, produto.codigo, produto.descricao,notaFiscalSaida.cdnotafiscalsaida, "+
				  "produtoCodigoDeBarras.cdprodutocodigobarras,produtoCodigoDeBarras.codigo,"+
				  "pedidovenda.cdpedidovenda, pedidovenda.numero, tipoEstoque.cdTipoEstoque, tipoEstoque.descricao ")
			.join("recebimentoRetiraLoja.listaRecebimentoRetiraLojaProduto recebimentoRetiraLojaProduto")
			.join("recebimentoRetiraLoja.recebimentoRetiraLojaStatus recebimentoRetiraLojaStatus")
			.join("recebimentoRetiraLoja.manifesto manifesto")
			.join("recebimentoRetiraLoja.usuario usuario")
			.join("recebimentoRetiraLoja.deposito deposito")
		 	.join("recebimentoRetiraLojaProduto.produto produto")
		 	.join("produto.listaProdutoCodigoDeBarras produtoCodigoDeBarras")
		 	.join("recebimentoRetiraLojaProduto.notaFiscalSaida notaFiscalSaida")
		 	.join("notaFiscalSaida.pedidovenda pedidovenda")
		 	.join("recebimentoRetiraLojaProduto.tipoEstoque tipoEstoque")
		 	.where("recebimentoRetiraLoja.cdRecebimentoRetiraLoja in ?", subQuery);
		
		return qb.unique();
	}

	public List<RecebimentoLojaVO> recuperaDadosRecebimentoRetiraLoja(String codigoEan) {
		return null;
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaRecebimentoRetiraLojaProduto");
	}

}
