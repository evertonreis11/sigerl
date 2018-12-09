package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Motivoretornoentrega;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.geral.dao.MotivoretornoentregaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class MotivoretornoentregaService extends GenericService<Motivoretornoentrega>{
	
	private MotivoretornoentregaDAO motivoretornoentregaDAO;
	
	public void setMotivoretornoentregaDAO(MotivoretornoentregaDAO motivoretornoentregaDAO) {
		this.motivoretornoentregaDAO = motivoretornoentregaDAO;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object findAllExibeObservacao() {
		
		List<Motivoretornoentrega> lista = motivoretornoentregaDAO.findAllExibeObservacao();
		
		StringBuilder listaExibeObservacao = new StringBuilder();
		for (Motivoretornoentrega motivoretornoentrega : lista) {
			if(motivoretornoentrega.getExigeobservacao()){
				listaExibeObservacao.append(motivoretornoentrega.getCdmotivoretornoentrega());
				listaExibeObservacao.append(",");
			}
		}
		
		try{
			return listaExibeObservacao.substring(0, listaExibeObservacao.length()-1);
		}catch (Exception e) {
			return null;
		}
		
	}

	/**
	 * 
	 * @param statusconfirmacaoentrega
	 * @return
	 */
	public List<Motivoretornoentrega> findByStatusconfirmacaoentrega(Statusconfirmacaoentrega statusconfirmacaoentrega) {
		return motivoretornoentregaDAO.findByStatusconfirmacaoentrega(statusconfirmacaoentrega);
	}
	
}
