package br.com.linkcom.wms.geral.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.geral.bean.vo.RecebimentoLojaVO;
import br.com.linkcom.wms.geral.dao.ProdutoDAO;
import br.com.linkcom.wms.geral.dao.RecebimentoRetiraLojaProdutoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RecebimentoRetiraLojaProdutoService extends GenericService<RecebimentoRetiraLojaProduto> {
	
	private ProdutoDAO produtoDAO;
	
	private RecebimentoRetiraLojaProdutoDAO recebimentoRetiraLojaProdutoDAO;
	
	public void setProdutoDAO(ProdutoDAO produtoDAO) {
		this.produtoDAO = produtoDAO;
	}
	
	public void setRecebimentoRetiraLojaProdutoDAO(RecebimentoRetiraLojaProdutoDAO recebimentoRetiraLojaProdutoDAO) {
		this.recebimentoRetiraLojaProdutoDAO = recebimentoRetiraLojaProdutoDAO;
	}
	

	public String confirmarProdutoRecebido(String codigoEan,
			List<RecebimentoRetiraLojaProduto> listaRecebimentoRetiraLojaProduto, TipoEstoque tipoEstoque) {

		Produto produto = produtoDAO.loadByCodigoBarras(codigoEan);

		if (produto == null)
			return "O código do produto informado não foi encontrado.";

		RecebimentoRetiraLojaProduto recebimentoRetiraLojaProduto = (RecebimentoRetiraLojaProduto) CollectionUtils.find(listaRecebimentoRetiraLojaProduto,
				new BeanPropertyValueEqualsPredicate("produto.cdproduto", produto.getCdproduto()));

		if (recebimentoRetiraLojaProduto == null)
			return "O produto informado não pertence ao recebimento em conferência.";

		recebimentoRetiraLojaProduto.setTipoEstoque(tipoEstoque);
		saveOrUpdate(recebimentoRetiraLojaProduto);


		return null;
	}


	/**
	 * Cria recebimento loja produto.
	 *
	 * @param vo the vo
	 * @param recebimento 
	 * @return the recebimento retira loja produto
	 */
	public RecebimentoRetiraLojaProduto criaRecebimentoLojaProduto(RecebimentoLojaVO vo, RecebimentoRetiraLoja recebimento) {
		
		RecebimentoRetiraLojaProduto produtoRetiraLoja = new RecebimentoRetiraLojaProduto();
		
		produtoRetiraLoja.setTipoEstoque(TipoEstoque.EXTRAVIADO);
		produtoRetiraLoja.setQtde(vo.getQtde());
		
		Produto produto = new Produto(vo.getCdProduto(), vo.getDescricaoProduto());
		
		produto.setCodigo(vo.getCodigoProduto());
		
		Produtocodigobarras produtocodigobarras = new Produtocodigobarras();
		produtocodigobarras.setCdprodutocodigobarras(vo.getCdProdutoCodigoBarras());
		produtocodigobarras.setCodigo(vo.getCodigoBarras());
		produto.setListaProdutoCodigoDeBarras(new ListSet<Produtocodigobarras>(Produtocodigobarras.class, Arrays.asList(produtocodigobarras)));
		
		produtoRetiraLoja.setProduto(produto);
		
		Notafiscalsaida notaFiscalSaida = new Notafiscalsaida();
		notaFiscalSaida.setCdnotafiscalsaida(vo.getCdNotaFiscalSaida());		
		notaFiscalSaida.setNumeropedido(vo.getNumeroPedido());
		
		produtoRetiraLoja.setNotaFiscalSaida(notaFiscalSaida);
		
		produtoRetiraLoja.setRecebimentoRetiraLoja(recebimento);
		
		return produtoRetiraLoja;
	}


	/**
	 * Altera a situacao do produto.
	 *
	 * @param cdRecebimentoRetiraLojaProduto the cd recebimento retira loja produto
	 * @param cdTipoEstoque the cd tipo estoque
	 */
	public void alterarSituacaoProduto(Integer cdRecebimentoRetiraLojaProduto, Integer cdTipoEstoque) {
		TipoEstoque tipoEstoque = TipoEstoque.getTipoEstoque(cdTipoEstoque);
		
		if (TipoEstoque.AVARIADO.equals(tipoEstoque))
			tipoEstoque = TipoEstoque.PERFEITO;
		else
			tipoEstoque = TipoEstoque.AVARIADO;
		
		if (tipoEstoque != null)
			recebimentoRetiraLojaProdutoDAO.alterarSituacaoProduto(cdRecebimentoRetiraLojaProduto, tipoEstoque);
	}

}
