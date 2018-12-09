package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.OcupacaoEnderecoHistorico;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.ExtratoOcupacaoVO;
import br.com.linkcom.wms.geral.dao.OcupacaoEnderecoHistoricoDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ExtratoOcupacaoFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsUtil;

public class OcupacaoEnderecoHistoricoService extends GenericService<OcupacaoEnderecoHistorico> {

	private OcupacaoEnderecoHistoricoDAO historicoDAO;
	
	public void setHistoricoDAO(OcupacaoEnderecoHistoricoDAO historicoDAO) {
		this.historicoDAO = historicoDAO;
	}
	
	/**
	 * Gera o relatório de extrato de ocupação.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public IReport criarRelatorioExtratoOcupacao(ExtratoOcupacaoFiltro filtro) {
		Report report = new Report("RelatorioExtratoOcupacao");
		report.addParameter("DEPOSITO", WmsUtil.getDeposito().getNome());
		report.addParameter("PERIODO", DateUtil.getDescricaoPeriodo(filtro.getInicio(), filtro.getTermino()));
		List<ExtratoOcupacaoVO> listaHistorico = findForReportExtratoOcupacao(filtro);
		List<ExtratoOcupacaoVO> listaOrganizada = new ArrayList<ExtratoOcupacaoVO>();
		
		Integer cdocupacaoenderecohistorico = 0;
		ExtratoOcupacaoVO parProdutoEnderecoAnterior = null;
		long totalizadorSaldoParcial = 0L;
		for (ExtratoOcupacaoVO item : listaHistorico){
			
			//Devido a possibilidade de haver dois usuários associados à mesma ordem de serviço
			//E como a consulta faz join com a tabela de usuários, poderá ocorrer de duplicar a 
			//informação de histórico
			if (cdocupacaoenderecohistorico.equals(item.getCdocupacaoenderecohistorico()))
				continue;
			
			if(parProdutoEnderecoAnterior == null || !item.isProdutoEnderecoEquals(parProdutoEnderecoAnterior)){
				parProdutoEnderecoAnterior = new ExtratoOcupacaoVO();
				parProdutoEnderecoAnterior.setCdendereco(item.getCdendereco());
				parProdutoEnderecoAnterior.setCdproduto(item.getCdproduto());
				parProdutoEnderecoAnterior.setDtentrada(item.getDtentrada());
				
				parProdutoEnderecoAnterior.setSaldoParcial(historicoDAO.getSaldoAnterior(filtro.getInicio(), 
						new Endereco(item.getCdendereco()), new Produto(item.getCdproduto())));
				totalizadorSaldoParcial = parProdutoEnderecoAnterior.getSaldoParcial();

				parProdutoEnderecoAnterior.setAcumula(1);
				parProdutoEnderecoAnterior.setCodigoProduto(item.getCodigoProduto());
				parProdutoEnderecoAnterior.setDescricaoProduto(item.getDescricaoProduto());
				parProdutoEnderecoAnterior.setEndereco(item.getEndereco());
				parProdutoEnderecoAnterior.setTipoOperacao("SALDO ANTERIOR");
				
				listaOrganizada.add(parProdutoEnderecoAnterior);
			}
			
			if (item.getAcumula() == 0){
				if (totalizadorSaldoParcial > item.getQtde())
					item.setSaida(totalizadorSaldoParcial - item.getQtde());
				else
					item.setEntrada(item.getQtde() - totalizadorSaldoParcial);
					
				totalizadorSaldoParcial = item.getQtde();
			}else{
				if (item.getQtde() > 0)
					item.setEntrada(item.getQtde());
				else if (item.getQtde() < 0)
					item.setSaida(item.getQtde() * -1);
				
				totalizadorSaldoParcial += item.getQtde();
			}

			item.setSaldoParcial(totalizadorSaldoParcial);
			
			listaOrganizada.add(item);
		}
		
		report.setDataSource(listaOrganizada);
		return report;
	}
	
	/**
	 * Lista os históricos de ocupação para o relatório de extrato de ocupação.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<ExtratoOcupacaoVO> findForReportExtratoOcupacao(ExtratoOcupacaoFiltro filtro) {
		return historicoDAO.findForReportExtratoOcupacao(filtro);
	}
	
}
