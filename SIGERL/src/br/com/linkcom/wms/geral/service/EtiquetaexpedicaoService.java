package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.vo.ConferenciaVolumeVO;
import br.com.linkcom.wms.geral.dao.EtiquetaexpedicaoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.LancarcorteFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EtiquetaprodutoseparacaoFiltro;
import br.com.linkcom.wms.util.expedicao.EtiquetasProdutoSeparacaoVO;

public class EtiquetaexpedicaoService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Etiquetaexpedicao> {
	
	private EtiquetaexpedicaoDAO etiquetaexpedicaoDAO;
	
	public void setEtiquetaexpedicaoDAO(EtiquetaexpedicaoDAO etiquetaexpedicaoDAO) {
		this.etiquetaexpedicaoDAO = etiquetaexpedicaoDAO;
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Atualiza o campo quantidade coletor da etiqueta.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EtiquetaexpedicaoDAO#updateQtdecoletor(Etiquetaexpedicao etiqueta)
	 * @author Pedro Gonçalves
	 * 
	 * @param etiqueta
	 * 
	 */
	public void updateQtdecoletor(Etiquetaexpedicao etiqueta) {
		etiquetaexpedicaoDAO.updateQtdecoletor(etiqueta);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EtiquetaexpedicaoDAO#findForReport(Carregamento carregamento) 
	 * 
	 * @param carregamento
	 * @param object
	 * @return
	 */
	public List<EtiquetasProdutoSeparacaoVO> findForReport(EtiquetaprodutoseparacaoFiltro filtro) {
		return etiquetaexpedicaoDAO.findForReport(filtro);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Reseta a quantidade coletada.
	 * 
	 * @author Pedro Gonçalves
	 * @see br.com.linkcom.wms.geral.dao.EtiquetaexpedicaoDAO#resetQtdecoletor(Ordemservico)
	 * 
	 * @param etiqueta
	 * 
	 */
	public void resetQtdecoletor(Ordemservico ordemservico) {
		etiquetaexpedicaoDAO.resetQtdecoletor(ordemservico);
	}
		
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EtiquetaexpedicaoDAO#removeEtiquetasCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 */
	public void removeEtiquetasCarregamento(Carregamento carregamento) {
		etiquetaexpedicaoDAO.removeEtiquetasCarregamento(carregamento);
	}
	
	/* singleton */
	private static EtiquetaexpedicaoService instance;
	public static EtiquetaexpedicaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(EtiquetaexpedicaoService.class);
		}
		return instance;
	}

	public String getEnderecoOrigem(Etiquetaexpedicao etiqueta) {
		return etiquetaexpedicaoDAO.getEnderecoOrigem(etiqueta);
	}

	public List<Etiquetaexpedicao> findByOrdemservico(Ordemservico ordemservico, boolean conferenciaBox, LancarcorteFiltro filtro) {
		return etiquetaexpedicaoDAO.findByOrdemservico(ordemservico, conferenciaBox, filtro);
	}
	
	/**
	 * Localiza as quantidades que foram conferidas para cada volume de um dado produto.
	 * Quando o usuário realiza corte de um único volume de um produto o sistema localiza
	 * os outros volumes e define as etiquetas que serão cortadas de forma automática.
	 * 
	 * @author Giovane Freitas
	 * @param carregamentoitem
	 * @return
	 */
	public List<ConferenciaVolumeVO> findConferenciaVolume(final Carregamentoitem carregamentoitem){
		return etiquetaexpedicaoDAO.findConferenciaVolume(carregamentoitem);
	}

	/**
	 * Seleciona as etiquetas dos volumes para realizar o corte automático, identificando as etiquetas para o volume desejado e que pertencem
	 * ao mesmo carregamentoitem.
	 * 
	 * @param carregamentoitem
	 * @param volume
	 * @param numeroEtiquetas
	 * @return
	 */
	public List<Etiquetaexpedicao> findEtiquetasParaCorte(final int cdcarregamentoitem, final int cdvolume, final int numeroEtiquetas) {
		return etiquetaexpedicaoDAO.findEtiquetasParaCorte(cdcarregamentoitem, cdvolume, numeroEtiquetas);
	}

	/**
	 * 
	 * @param bean
	 * @param whereIn
	 */
	public void deleteByCarregamento(Carregamento carregamento, String whereIn) {
		if(whereIn!=null && !whereIn.isEmpty()){
			etiquetaexpedicaoDAO.deleteByCarregamentoWhereIn(whereIn);	
		}else{
			etiquetaexpedicaoDAO.deleteByCarregamento(carregamento);
		}
	}
}
