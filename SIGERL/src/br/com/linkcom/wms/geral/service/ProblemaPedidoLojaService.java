package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.PontoControle;
import br.com.linkcom.wms.geral.bean.ProblemaPedidoLoja;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.dao.ProblemaPedidoLojaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.ricardoeletro.sigerl.expedicao.process.filtro.GestaoPedidoFiltro;

public class ProblemaPedidoLojaService extends GenericService<ProblemaPedidoLoja> {
	
	private NotafiscalsaidaService notafiscalsaidaService;
	private ProblemaPedidoLojaDAO problemaPedidoLojaDAO;
	private RecebimentoRetiraLojaProdutoService recebimentoRetiraLojaProdutoService;
	private EstoqueProdutoLojaService estoqueProdutoLojaService;

	public void setNotafiscalsaidaService(NotafiscalsaidaService notafiscalsaidaService) {
		this.notafiscalsaidaService = notafiscalsaidaService;
	}
	
	public void setProblemaPedidoLojaDAO(ProblemaPedidoLojaDAO problemaPedidoLojaDAO) {
		this.problemaPedidoLojaDAO = problemaPedidoLojaDAO;
	}
	
	public void setRecebimentoRetiraLojaProdutoService(
			RecebimentoRetiraLojaProdutoService recebimentoRetiraLojaProdutoService) {
		this.recebimentoRetiraLojaProdutoService = recebimentoRetiraLojaProdutoService;
	}
	
	public void setEstoqueProdutoLojaService(EstoqueProdutoLojaService estoqueProdutoLojaService) {
		this.estoqueProdutoLojaService = estoqueProdutoLojaService;
	}
	
	
	public void informarProblemaPedido(GestaoPedidoFiltro filtro) {
		ProblemaPedidoLoja problema = loadForEntrada(new ProblemaPedidoLoja(filtro.getCdProblemaPedidoLoja()));
		
		String[] notas = filtro.getNotasInfoProblema().split(",");
		
		for (int i = 0; i < notas.length; i++) {
			Notafiscalsaida nota = notafiscalsaidaService.recuperaNotaSaidaPorNumero(notas[i]);
			
			if(problema.getPontoControle() != null){
				criarTrakkingNota(nota, problema.getPontoControle());
			}
			
			if (problema.getTipoEstoque() != null){
				List<RecebimentoRetiraLojaProduto> produtosRecebidos = recebimentoRetiraLojaProdutoService
						.recuperaProdutosRecebimentoPorNota(nota.getCdnotafiscalsaida());
				
				for (RecebimentoRetiraLojaProduto produtoRecebido : produtosRecebidos) {
					estoqueProdutoLojaService.alterarTipoEstoqueProduto(produtoRecebido, problema.getTipoEstoque());
				}
			}
		}
		
	}

	public void criarTrakkingNota(Notafiscalsaida nota, PontoControle pontoControle) {
		problemaPedidoLojaDAO.criarTrakkingNota(nota, pontoControle);
		
	}

}
