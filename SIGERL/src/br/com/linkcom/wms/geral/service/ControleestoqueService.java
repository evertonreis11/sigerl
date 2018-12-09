package br.com.linkcom.wms.geral.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Controleestoque;
import br.com.linkcom.wms.geral.bean.Controleestoqueproduto;
import br.com.linkcom.wms.geral.bean.Controleestoquestatus;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.SaldoProdutoVO;
import br.com.linkcom.wms.geral.dao.ControleestoqueDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ControleestoqueFiltro;
import br.com.linkcom.wms.util.WmsUtil;

public class ControleestoqueService extends GenericService<Controleestoque> {

	private static ControleestoqueService instance;
	private ControleestoqueDAO controleestoqueDAO;

	public void setControleestoqueDAO(ControleestoqueDAO controleestoqueDAO) {
		this.controleestoqueDAO = controleestoqueDAO;
	}

	public static ControleestoqueService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ControleestoqueService.class);
		}
		return instance;
	}

	/**
	 * Compara o saldo de estoque retornado pelo ERP com o saldo de estoque do
	 * WMS e gera um registro de Controleestoque contendo a lista de produtos
	 * com estoque divergente.
	 * 
	 * @author Giovane Freitas
	 * @param list
	 *            A lista de estoque retornada pelo ERP
	 * @return Um Controleestoque contendo todos os produtos com estoque
	 *         divergente.
	 */
	public Controleestoque gerarControleestoque(Map<Long, Long> produtoEstoque) {

		Controleestoque controleestoque = new Controleestoque();
		controleestoque.setControleestoquestatus(Controleestoquestatus.EM_ANDAMENTO);
		controleestoque.setDtcontroleestoque(new Timestamp(System.currentTimeMillis()));
		controleestoque.setDeposito(WmsUtil.getDeposito());

		List<SaldoProdutoVO> listaSaldo = ProdutoService.getInstance().findSaldoProduto(WmsUtil.getDeposito());

		saveOrUpdate(controleestoque);

		controleestoque.setControleestoquestatus(Controleestoquestatus.CONCLUIDO);

		for (SaldoProdutoVO saldo : listaSaldo) {
			Long qtdeEstoque = 0L;
			
			if (produtoEstoque.containsKey(saldo.getCodigoerp())) {
				qtdeEstoque = produtoEstoque.get(saldo.getCodigoerp());
			}else{
				//Se este produto não veio na posição de estoque do ERP e o estoque no WMS é ZERO
				//Então provavelmente este item não é mais vendido, vou ignorar ele
				//senão o relatório terá mais de 15.000 itens (e aumentando a cada dia...)
				if (saldo.getQtde().equals(0L))
					continue;
			}

			if (!qtdeEstoque.equals(saldo.getQtde())) {
				controleestoque.setControleestoquestatus(Controleestoquestatus.CONCLUIDO_DIVERGENCIA);
			}

			Controleestoqueproduto itemControle = new Controleestoqueproduto();
			itemControle.setControleestoque(controleestoque);
			itemControle.setProduto(new Produto(saldo.getCdproduto()));
			itemControle.setQtde(qtdeEstoque);
			itemControle.setQtdeesperada(saldo.getQtde());
			itemControle.setQtdeavaria(saldo.getAvaria());
			itemControle.setQtdevolumesdivergentes(saldo.getVolumesdivergentes());
			controleestoque.getListaControleestoqueproduto().add(itemControle);
		}

		saveOrUpdate(controleestoque);

		return controleestoque;
	}

	/**
	 * Gera o relatório de controle de estoque, contendo os produtos com estoque
	 * divergente.
	 * 
	 * @author Giovane Freitas
	 * @param controleestoque
	 * @return
	 */
	public IReport criarRelatorio(Controleestoque controleestoque) {
		Report report = new Report("RelatorioControleestoque");
		report.addParameter("TOTAL_PRODUTOS", controleestoque.getListaControleestoqueproduto().size());
		report.addParameter("DATA_IMPORTACAO", controleestoque.getDtcontroleestoque());
		report.addParameter("STATUS", controleestoque.getControleestoquestatus().getNome());
		report.addParameter("DEPOSITO", controleestoque.getDeposito().getNome());
		report.setDataSource(controleestoque.getListaControleestoqueproduto());
		return report;
	}

	/**
	 * Busca os {@link Controleestoque} baseado no filtro informado.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<Controleestoque> find(ControleestoqueFiltro filtro) {
		return controleestoqueDAO.find(filtro);
	}

	/**
	 * Busca o {@link Controleestoque} e todos os seus itens para o relatório.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public Controleestoque findForReport(ControleestoqueFiltro filtro) {
		return controleestoqueDAO.findForReport(filtro);
	}

	/**
	 * Retorna o saldo total da última importação realizada.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public long getTotalUltimaImportacao(Produto produto) {
		return controleestoqueDAO.getTotalUltimaImportacao(produto);
	}
	
	/**
	 * Retorna a data da última importação realizada.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public Date getDataUltimaImportacao() {
		return controleestoqueDAO.getDataUltimaImportacao();
	}

}
