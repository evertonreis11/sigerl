package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculo;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculohistorico;
import br.com.linkcom.wms.geral.bean.Acompanhamentoveiculostatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.dao.AcompanhamentoveiculohistoricoDAO;
import br.com.linkcom.wms.util.WmsException;

public class AcompanhamentoveiculohistoricoService extends GenericService<Acompanhamentoveiculohistorico>{

	private AcompanhamentoveiculohistoricoDAO acompanhamentoveiculohistoricoDAO;

	public void setAcompanhamentoveiculohistoricoDAO(AcompanhamentoveiculohistoricoDAO acompanhamentoveiculohistoricoDAO) {
		this.acompanhamentoveiculohistoricoDAO = acompanhamentoveiculohistoricoDAO;
	}

	/**
	 * 
	 * @param av
	 * @param msg
	 */
	public void criarHistorico(Acompanhamentoveiculo av, String msg, Usuario usuario) {

		if(av==null || av.getAcompanhamentoveiculostatus()==null || av.getAcompanhamentoveiculostatus().getCdstatusrav()==null){
			throw new WmsException("Parâmetros Inválidos");
		}
		
		Acompanhamentoveiculohistorico acompanhamentoveiculohistorico = new Acompanhamentoveiculohistorico();
		if(av.getAcompanhamentoveiculostatus().getDescricao()==null || av.getAcompanhamentoveiculostatus().getDescricao().isEmpty()){
			Acompanhamentoveiculostatus status = new Acompanhamentoveiculostatus(av.getAcompanhamentoveiculostatus().getCdstatusrav());
			av.setAcompanhamentoveiculostatus(status);
		}
		
		acompanhamentoveiculohistorico.setAcompanhamentoveiculo(av);
		acompanhamentoveiculohistorico.setDescricao(msg!=null?msg:av.getAcompanhamentoveiculostatus().getDescricao());
		acompanhamentoveiculohistorico.setDtaltera(new Timestamp(System.currentTimeMillis()));
		acompanhamentoveiculohistorico.setUsuarioAltera(usuario);
		acompanhamentoveiculohistorico.setRecebimento(av.getRecebimento());
		acompanhamentoveiculohistorico.setAcompanhamentoveiculostatus(av.getAcompanhamentoveiculostatus());
		
		this.saveOrUpdate(acompanhamentoveiculohistorico);
	}
	
	/* singleton */
	private static AcompanhamentoveiculohistoricoService instance;
	public static AcompanhamentoveiculohistoricoService getInstance(){
		if(instance == null){
			instance = Neo.getObject(AcompanhamentoveiculohistoricoService.class);
		}
		return instance;
	}
	
}
