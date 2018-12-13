package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.vo.RecebimentoLojaVO;
import br.com.linkcom.wms.geral.dao.RecebimentoRetiraLojaDAO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RecebimentoRetiraLojaService extends GenericService<RecebimentoRetiraLoja> {

	private RecebimentoRetiraLojaDAO recebimentoRetiraLojaDAO;
	private RecebimentoRetiraLojaProdutoService recebimentoRetiraLojaProdutoService;
	private EstoqueProdutoLojaService estoqueProdutoLojaService;
	
	
	public void setRecebimentoRetiraLojaDAO(RecebimentoRetiraLojaDAO recebimentoRetiraLojaDAO) {
		this.recebimentoRetiraLojaDAO = recebimentoRetiraLojaDAO;
	}
	
	public void setRecebimentoRetiraLojaProdutoService(
			RecebimentoRetiraLojaProdutoService recebimentoRetiraLojaProdutoService) {
		this.recebimentoRetiraLojaProdutoService = recebimentoRetiraLojaProdutoService;
	}
	
	public void setEstoqueProdutoLojaService(EstoqueProdutoLojaService estoqueProdutoLojaService) {
		this.estoqueProdutoLojaService = estoqueProdutoLojaService;
	}
	
	/**
	 * Find recebimento loja.
	 *
	 * @param codigoEan the codigo ean
	 * @param cdRecebimentoRetiraLoja the cd recebimento retira loja
	 * @return the recebimento retira loja
	 */
	public RecebimentoRetiraLoja findRecebimentoLoja(String codigoEan, Integer cdRecebimentoRetiraLoja) {
		RecebimentoRetiraLoja recebimentoRetiraLoja = null;
		
		if (cdRecebimentoRetiraLoja != null){
			recebimentoRetiraLoja = recebimentoRetiraLojaDAO.findRecebimentoLoja(cdRecebimentoRetiraLoja);
		}else{
			recebimentoRetiraLoja = recebimentoRetiraLojaDAO.findRecebimentoLojaWithCodigoEan(codigoEan);
		}
		
		if(codigoEan != null && recebimentoRetiraLoja == null){
			recebimentoRetiraLoja = criaRecebimentoLoja(codigoEan);
		}
		
		return recebimentoRetiraLoja;
	}

	/**
	 * Cria recebimento loja.
	 *
	 * @param codigoEan the codigo ean
	 * @return the recebimento retira loja
	 */
	public RecebimentoRetiraLoja criaRecebimentoLoja(String codigoEan) {
		RecebimentoRetiraLoja recebimento = null;
		
		List<RecebimentoRetiraLojaProduto> produtos = null;
		
		List<RecebimentoLojaVO> registros = recebimentoRetiraLojaDAO.recuperaDadosRecebimentoRetiraLoja(codigoEan);
		
		if (registros != null && !registros.isEmpty()){
			recebimento = new RecebimentoRetiraLoja();
			recebimento.setDeposito(WmsUtil.getDeposito());
			recebimento.setDtRecebimento(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			recebimento.setUsuario(WmsUtil.getUsuarioLogado());
			recebimento.setRecebimentoRetiraLojaStatus(RecebimentoRetiraLojaStatus.EM_CONFERENCIA);
			recebimento.setManifesto(new Manifesto(registros.get(0).getCdManifesto()));
			
			produtos = new ArrayList<RecebimentoRetiraLojaProduto>();
			
			for (RecebimentoLojaVO vo : registros) {
				produtos.add(recebimentoRetiraLojaProdutoService.criaRecebimentoLojaProduto(vo));
			}
			
			recebimento.setListaRecebimentoRetiraLojaProduto(new ListSet<RecebimentoRetiraLojaProduto>(RecebimentoRetiraLojaProduto.class, produtos));
			
			saveOrUpdate(recebimento);
		}
		
		return recebimento;
		
	}

	/**
	 * Finalizar recebimento.
	 *
	 * @param recebimentoRetiraLoja the recebimento retira loja
	 */
	public void finalizarRecebimento(RecebimentoRetiraLoja recebimentoRetiraLoja) {
		recebimentoRetiraLoja = recebimentoRetiraLojaDAO.findRecebimentoLoja(recebimentoRetiraLoja.getCdRecebimentoRetiraLoja());
		
		estoqueProdutoLojaService.atualizarEstoqueLojaRecebimento(recebimentoRetiraLoja.getListaRecebimentoRetiraLojaProduto(), 
				recebimentoRetiraLoja.getDeposito().getCddeposito());
		
		recebimentoRetiraLoja.setRecebimentoRetiraLojaStatus(RecebimentoRetiraLojaStatus.CONCLUIDO);
		
		saveOrUpdate(recebimentoRetiraLoja);
		
		
		
	}

	
}
