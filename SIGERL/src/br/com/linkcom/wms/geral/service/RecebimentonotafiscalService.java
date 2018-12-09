package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.dao.RecebimentonotafiscalDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.RecebimentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class RecebimentonotafiscalService extends GenericService<Recebimentonotafiscal> {

	private RecebimentonotafiscalDAO recebimentonotafiscalDAO;
	private NotafiscalentradaService notafiscalentradaService;
	
	public void setRecebimentonotafiscalDAO(RecebimentonotafiscalDAO recebimentonotafiscalDAO) {
		this.recebimentonotafiscalDAO = recebimentonotafiscalDAO;
	}
	
	public void setNotafiscalentradaService(NotafiscalentradaService notafiscalentradaService) {
		this.notafiscalentradaService = notafiscalentradaService;
	}
	
	/**
	 * Pega todas as notas fiscais do veículo cujo recebimento não tenha os status "Disponível, Em andamento ou Concluído", 
	 * ou que ainda ainda não foi emitido nenhum recebimento para esta, e salva na entidade recebimento nota fiscal.
	 * 
	 * Adiciona ao objeto veículo uma lista atualizada das notas fiscais.
	 * 
	 * @see br.com.linkcom.wms.geral.service.VeiculoService#veiculoInfoNf
	 * 
	 * @param veiculo
	 * @param recebimento
	 * @author Pedro Gonçalves
	 */
	public List<Notafiscalentrada> salvarRecebimentoNotaFiscal(RecebimentoFiltro filtro) {
		Recebimento recebimento = filtro.getRecebimento();
		if(recebimento == null || recebimento.getCdrecebimento() == null){
			throw new WmsException("Parâmetros incorretos");
		}
		
		List<Notafiscalentrada> listaNotaFiscal = notafiscalentradaService.findBy(filtro);
		if(listaNotaFiscal != null){
			Recebimentonotafiscal recebimentonotafiscal = null;
			for (Notafiscalentrada notafiscalentrada : listaNotaFiscal) {
				recebimentonotafiscal = new Recebimentonotafiscal();
				recebimentonotafiscal.setRecebimento(recebimento);
				recebimentonotafiscal.setNotafiscalentrada(notafiscalentrada);
				saveOrUpdate(recebimentonotafiscal);
			}
		}
		return listaNotaFiscal;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentonotafiscalDAO#deleteByRecebimento(Recebimento recebimento)
	 * 
	 * @param recebimento
	 */
	public void deleteByRecebimento(Recebimento recebimento) {
		recebimentonotafiscalDAO.deleteByRecebimento(recebimento);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Busca as notas ficas de um recebimento
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentonotafiscalDAO#findByRecebimento(Recebimento)
	 * 
	 * @author Arantes
	 * 
	 * @param recebimento
	 * @return List<Recebimentonotafiscal>
	 * 
	 */
	public List<Recebimentonotafiscal> findByRecebimento(Recebimento filtro) {
		return recebimentonotafiscalDAO.findByRecebimento(filtro);
	}

	/* singleton */
	private static RecebimentonotafiscalService instance;
	public static RecebimentonotafiscalService getInstance() {
		if(instance == null){
			instance = Neo.getObject(RecebimentonotafiscalService.class);
		}
		return instance;
	}

	public List<Recebimentonotafiscal> loadRecebimentonotafiscal(String listaStatus, Integer cddeposito) {
		return recebimentonotafiscalDAO.loadRecebimentonotafiscal(listaStatus, cddeposito);
	}
}
