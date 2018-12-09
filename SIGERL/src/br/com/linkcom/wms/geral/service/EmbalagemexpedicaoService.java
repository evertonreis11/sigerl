package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.wms.geral.bean.Embalagemexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.dao.EmbalagemexpedicaoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EmbalagemexpedicaoService extends GenericService<Embalagemexpedicao> {
	
	private static EmbalagemexpedicaoService instance;
	private EmbalagemexpedicaoDAO embalagemexpedicaoDAO;

	public static EmbalagemexpedicaoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(EmbalagemexpedicaoService.class);
		}
		return instance;
	}
	
	public void setEmbalagemexpedicaoDAO(
			EmbalagemexpedicaoDAO embalagemexpedicaoDAO) {
		this.embalagemexpedicaoDAO = embalagemexpedicaoDAO;
	}

	/**
	 * Exclui todas as embalagens associadas a uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public void deleteByOrdem(Ordemservico ordemservico) {
		embalagemexpedicaoDAO.deleteByOrdem(ordemservico);
	}

	/**
	 * Localiza a última embalagem que ainda não foi finalizada.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Embalagemexpedicao findEmbalagemAberta(Ordemservico ordemservico) {
		return embalagemexpedicaoDAO.findEmbalagemAberta(ordemservico);
	}

	/**
	 * Gera a impressão das etiquetas de embalagens de expedição.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public IReport createReportEmbalagem(Ordemservico ordemservico) {
		Report report = new Report("RelatorioEtiquetaEmbalagem");

		List<Embalagemexpedicao> list = embalagemexpedicaoDAO.findByOrdem(ordemservico);
		report.setDataSource(list);
		return report;
	}

	/**
	 * Localiza as embalagens de produtos que usam checkout que vão pra o mesmo cliente.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Embalagemexpedicao> findByPrimeiraConferencia(Ordemservico ordemservico){
		ordemservico = OrdemservicoService.getInstance().loadTipostatuscarregamento(ordemservico);
		return embalagemexpedicaoDAO.findByPrimeiraConferencia(ordemservico);
	}
	
	/**
	 * Verifica se existem mais de uma embalagem aberta sem lacre na OS.
	 * 
	 * @author Filipe Santos
	 * @param ordemservico
	 * @since 30/01/2012
	 * @return
	 */	
	public List<Embalagemexpedicao> findEmabalgemAbertas(Ordemservico ordemservico){
		return embalagemexpedicaoDAO.findEmabalgemAbertas(ordemservico);
	}
	
	/**
	 * Retorna a embalagem com o lacre a ordemservico correspondente.
	 * 
	 * @author Filipe
	 * @param embalagem
	 * @return {@link Embalagemexpedicao}
	 */
	public Embalagemexpedicao findByLacre(Embalagemexpedicao embalagem){
		return embalagemexpedicaoDAO.findByLacre(embalagem);
	}
	
}
