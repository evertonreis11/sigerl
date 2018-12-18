package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.EstoqueProdutoLoja;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.TipoEstoque;
import br.com.linkcom.wms.geral.dao.EstoqueProdutoLojaDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EstoqueProdutoLojaService extends GenericService<EstoqueProdutoLoja> {
	
	private EstoqueProdutoLojaDAO estoqueProdutoLojaDAO;
	
	public void setEstoqueProdutoLojaDAO(EstoqueProdutoLojaDAO estoqueProdutoLojaDAO) {
		this.estoqueProdutoLojaDAO = estoqueProdutoLojaDAO;
	}

	/**
	 * Atualizar estoque loja recebimento.
	 *
	 * @param listaRecebimentoRetiraLojaProduto the lista recebimento retira loja produto
	 * @param cdDeposito the cd deposito
	 */
	public void atualizarEstoqueLojaRecebimento(List<RecebimentoRetiraLojaProduto> listaRecebimentoRetiraLojaProduto, Integer cdDeposito) {
		EstoqueProdutoLoja estoqueProdutoLoja = null;
		
		for (RecebimentoRetiraLojaProduto recebimentoProduto : listaRecebimentoRetiraLojaProduto) {
			if (!TipoEstoque.EXTRAVIADO.equals(recebimentoProduto.getTipoEstoque())){
				estoqueProdutoLoja = estoqueProdutoLojaDAO.recuperarEstoqueProduto(recebimentoProduto.getProduto().getCdproduto(), 
												recebimentoProduto.getTipoEstoque(), cdDeposito);
				
				if (estoqueProdutoLoja == null){
					estoqueProdutoLoja = gerarEstoqueProdutoLoja(recebimentoProduto, cdDeposito);
				}else{
					estoqueProdutoLoja.setQtde(Math.addExact(estoqueProdutoLoja.getQtde(), recebimentoProduto.getQtde()));
					estoqueProdutoLoja.setDtAlteracao(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				}
				
				saveOrUpdate(estoqueProdutoLoja);
			}
		}
		
	}

	/**
	 * Gerar estoque produto loja.
	 *
	 * @param recebimentoProduto the recebimento produto
	 * @param cdDeposito the cd deposito
	 * @return the estoque produto loja
	 */
	public EstoqueProdutoLoja gerarEstoqueProdutoLoja(RecebimentoRetiraLojaProduto recebimentoProduto, Integer cdDeposito) {
		EstoqueProdutoLoja estoqueProdutoLoja = new EstoqueProdutoLoja();
		estoqueProdutoLoja.setDeposito(new Deposito(cdDeposito));
		estoqueProdutoLoja.setDtAlteracao(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		estoqueProdutoLoja.setDtInclusao(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		estoqueProdutoLoja.setProduto(recebimentoProduto.getProduto());
		estoqueProdutoLoja.setQtde(recebimentoProduto.getQtde());
		estoqueProdutoLoja.setTipoEstoque(recebimentoProduto.getTipoEstoque());
		
		return estoqueProdutoLoja;
		
		
	}

	/**
	 * Validar estoque produto.
	 *
	 * @param cdproduto the cdproduto
	 * @param qtde the qtde
	 * @return true, if successful
	 */
	public boolean validarEstoqueProduto(Integer cdproduto, Integer qtde) {
		EstoqueProdutoLoja estoqueProdutoLoja = estoqueProdutoLojaDAO.recuperaEstoqueProdutoDepositoLogado(cdproduto);
		return estoqueProdutoLoja != null && estoqueProdutoLoja.getQtde().equals(qtde);
	}

	/**
	 * Atualizar estoque loja expedicao.
	 *
	 * @param registros the registros
	 * @param cddeposito the cddeposito
	 */
	public void atualizarEstoqueLojaExpedicao(List<ExpedicaoRetiraLojaProduto> registros, Integer cddeposito) {
		EstoqueProdutoLoja estoqueProdutoLoja = null;

		for (ExpedicaoRetiraLojaProduto expedicaoProduto : registros) {
			estoqueProdutoLoja = estoqueProdutoLojaDAO.recuperarEstoqueProduto(expedicaoProduto.getProduto().getCdproduto(), 
					TipoEstoque.PERFEITO, cddeposito);

			if (estoqueProdutoLoja == null){
				throw new WmsException("Erro ao baixar estoque do produto" + expedicaoProduto.getProduto().getDescricao());
			}else{
				if (Math.subtractExact(estoqueProdutoLoja.getQtde(), expedicaoProduto.getQtde()) > 0 ){
					estoqueProdutoLoja.setQtde(Math.subtractExact(estoqueProdutoLoja.getQtde(), expedicaoProduto.getQtde()));
					estoqueProdutoLoja.setDtAlteracao(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					saveOrUpdate(estoqueProdutoLoja);
				}else{
					delete(estoqueProdutoLoja);
				}
			}

		}

	}

}
