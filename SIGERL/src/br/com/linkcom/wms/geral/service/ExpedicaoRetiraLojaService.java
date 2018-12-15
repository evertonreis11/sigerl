package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLoja;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.vo.ExpedicaoLojaVO;
import br.com.linkcom.wms.geral.dao.ExpedicaoRetiraLojaDAO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ExpedicaoRetiraLojaService extends GenericService<ExpedicaoRetiraLoja> {
	
	private ExpedicaoRetiraLojaDAO expedicaoRetiraLojaDAO;
	private ExpedicaoRetiraLojaProdutoService expedicaoRetiraLojaProdutoService;
	
	public void setExpedicaoRetiraLojaDAO(ExpedicaoRetiraLojaDAO expedicaoRetiraLojaDAO) {
		this.expedicaoRetiraLojaDAO = expedicaoRetiraLojaDAO;
	}
	
	public void setExpedicaoRetiraLojaProdutoService(ExpedicaoRetiraLojaProdutoService expedicaoRetiraLojaProdutoService) {
		this.expedicaoRetiraLojaProdutoService = expedicaoRetiraLojaProdutoService;
	}
	
	public ExpedicaoRetiraLoja findExpedicaoLoja(String chaveNota) {
		ExpedicaoRetiraLoja expedicao = null;
		
		expedicao = expedicaoRetiraLojaDAO.recuperaExpedicaoRetiraLojaPorChaveNota(chaveNota);
		
		if (expedicao == null){
			criaExpedicaoRetiraLoja(expedicao, chaveNota);
		}
		
		return expedicao;
	}

	private void criaExpedicaoRetiraLoja(ExpedicaoRetiraLoja expedicao, String chaveNota) {
		expedicao = new ExpedicaoRetiraLoja();
		
		List<ExpedicaoLojaVO> registros = recuperarDadosPraCriacaoExpedicao(chaveNota);
		
		if(registros != null && !registros.isEmpty()){
			List<ExpedicaoRetiraLojaProduto> expedicaoProdutos = new ArrayList<ExpedicaoRetiraLojaProduto>();
			
			expedicao.setDeposito(WmsUtil.getDeposito());
			expedicao.setDtExpedicao(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			expedicao.setExpedicaoRetiraLojaStatus(ExpedicaoRetiraLojaStatus.EM_CONFERENCIA);
			expedicao.setUsuario(WmsUtil.getUsuarioLogado());
			
			Notafiscalsaida nota = new Notafiscalsaida();
			
			nota.setCdnotafiscalsaida(registros.get(0).getCdNotaFiscalSaida());
			nota.setNumero(registros.get(0).getNumeroNota());
			nota.setSerie(registros.get(0).getSerieNota());
			nota.setChavenfe(registros.get(0).getChaveNotaFiscal());
			
			expedicao.setNotaFiscalSaida(nota);
			
			for (ExpedicaoLojaVO vo : registros) {
				expedicaoProdutos.add(expedicaoRetiraLojaProdutoService.criarRegistrosExpedicaoLojaProduto(vo,expedicao));
			}
			
			expedicao.setListaExpedicaoRetiraLojaProduto(new ListSet<ExpedicaoRetiraLojaProduto>(ExpedicaoRetiraLojaProduto.class, expedicaoProdutos));
			
			saveOrUpdate(expedicao);
		}
		
	}

	/**
	 * Recuperar dados pra criacao expedicao.
	 *
	 * @param chaveNota the chave nota
	 * @return the list
	 */
	public List<ExpedicaoLojaVO> recuperarDadosPraCriacaoExpedicao(String chaveNota) {
		return expedicaoRetiraLojaDAO.recuperarDadosPraCriacaoExpedicao(chaveNota);
	}

	/**
	 * Finalizar expedicao.
	 *
	 * @param expedicaoRetiraLoja the expedicao retira loja
	 */
	public void finalizarExpedicao(ExpedicaoRetiraLoja expedicaoRetiraLoja) {
		expedicaoRetiraLoja = loadForEntrada(expedicaoRetiraLoja);
		
		expedicaoRetiraLoja.setExpedicaoRetiraLojaStatus(ExpedicaoRetiraLojaStatus.CONCLUIDO);
		
		saveOrUpdate(expedicaoRetiraLoja);
	}

}
