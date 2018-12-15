package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.ConferenciaExpedicaoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLoja;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.Notafiscalsaidaproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.ExpedicaoLojaVO;
import br.com.linkcom.wms.geral.dao.ExpedicaoRetiraLojaProdutoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ExpedicaoRetiraLojaProdutoService extends GenericService<ExpedicaoRetiraLojaProduto> {
	
	private ExpedicaoRetiraLojaProdutoDAO expedicaoRetiraLojaProdutoDAO;
	private EstoqueProdutoLojaService estoqueProdutoLojaService;
	
	public void setExpedicaoRetiraLojaProdutoDAO(ExpedicaoRetiraLojaProdutoDAO expedicaoRetiraLojaProdutoDAO) {
		this.expedicaoRetiraLojaProdutoDAO = expedicaoRetiraLojaProdutoDAO;
	}
	
	public void setEstoqueProdutoLojaService(EstoqueProdutoLojaService estoqueProdutoLojaService) {
		this.estoqueProdutoLojaService = estoqueProdutoLojaService;
	}

	/**
	 * Criar registros expedicao loja produto.
	 *
	 * @param vo the vo
	 * @param expedicao the expedicao
	 * @return the expedicao retira loja produto
	 */
	public ExpedicaoRetiraLojaProduto criarRegistrosExpedicaoLojaProduto(ExpedicaoLojaVO vo,
			ExpedicaoRetiraLoja expedicao) {
		
		ExpedicaoRetiraLojaProduto expedicaoRetiraLojaProduto = new ExpedicaoRetiraLojaProduto();
		
		expedicaoRetiraLojaProduto.setConferenciaExpedicaoRetiraLojaStatus(ConferenciaExpedicaoRetiraLojaStatus.AGUARDANDO_CONFERENCIA);
		expedicaoRetiraLojaProduto.setExpedicaoRetiraLoja(expedicao);
		expedicaoRetiraLojaProduto.setQtde(vo.getQtde());
		expedicaoRetiraLojaProduto.setNotaFiscalSaidaProduto(new Notafiscalsaidaproduto(vo.getCdNotaFiscalSaidaProduto()));
		
		Produto produto = new Produto(vo.getCdProduto());
		produto.setCodigo(vo.getCodigoProduto());
		produto.setDescricao(vo.getDescricaoProduto());
		
		expedicaoRetiraLojaProduto.setProduto(produto);
		
		return expedicaoRetiraLojaProduto;
	}

	/**
	 * Conferir produto.
	 *
	 * @param codigoBarras the codigo barras
	 * @param cdExpedicaoRetiraLoja the cd expedicao retira loja
	 * @param cdnotafiscalsaida the cdnotafiscalsaida
	 * @return the string
	 */
	public String conferirProduto(String codigoBarras, Integer cdExpedicaoRetiraLoja, Integer cdnotafiscalsaida) {
		
		String msg = null;
		
		ExpedicaoRetiraLojaProduto expedicaoRetiraLojaProduto = recuperaProdutoExpedicaoPorEan(codigoBarras, cdExpedicaoRetiraLoja, cdnotafiscalsaida);
		
		if (expedicaoRetiraLojaProduto == null){
			msg = "O codigo de barras informado é invalido ou não pertence a esse produto.";
		}else if (!estoqueProdutoLojaService.validarEstoqueProduto(expedicaoRetiraLojaProduto.getProduto().getCdproduto(), expedicaoRetiraLojaProduto.getQtde())){
			msg = "O produto não tem está disponivel no estoque da filial. Gentileza verificar se houve recebimento deste item.";
		}else{
			confirmarConferenciaProduto(expedicaoRetiraLojaProduto);
			
			msg = "A conferência do produto foi realizada com sucesso.";
		}
		
		return msg;
	}

	/**
	 * Recupera produto expedicao por ean.
	 *
	 * @param codigoBarras the codigo barras
	 * @param cdExpedicaoRetiraLoja the cd expedicao retira loja
	 * @param cdnotafiscalsaida the cdnotafiscalsaida
	 * @return the expedicao retira loja produto
	 */
	public ExpedicaoRetiraLojaProduto recuperaProdutoExpedicaoPorEan(String codigoBarras, Integer cdExpedicaoRetiraLoja, Integer cdnotafiscalsaida) {
		return  expedicaoRetiraLojaProdutoDAO.recuperaProdutoExpedicaoPorEan(codigoBarras, cdExpedicaoRetiraLoja, cdnotafiscalsaida);
	}
	
	/**
	 * Recupera produtos sem conferencia por expedicao.
	 *
	 * @param cdExpedicaoRetiraLoja the cd expedicao retira loja
	 * @return the list
	 */
	public List<ExpedicaoRetiraLojaProduto> recuperaProdutosSemConferenciaPorExpedicao(Integer cdExpedicaoRetiraLoja) {
		return expedicaoRetiraLojaProdutoDAO.recuperaProdutosSemConferenciaPorExpedicao(cdExpedicaoRetiraLoja);
	}

	/**
	 * Confirmar a conferencia do produto na expedicao.
	 *
	 * @param expedicaoRetiraLojaProduto the expedicao retira loja produto
	 */
	private void confirmarConferenciaProduto(ExpedicaoRetiraLojaProduto expedicaoRetiraLojaProduto) {
		expedicaoRetiraLojaProduto.setConferenciaExpedicaoRetiraLojaStatus(ConferenciaExpedicaoRetiraLojaStatus.CONFERIDO);
		
		saveOrUpdate(expedicaoRetiraLojaProduto);
		
	}

}
