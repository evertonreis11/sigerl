package br.com.linkcom.wms.geral.service;

import org.springframework.jdbc.core.JdbcTemplate;

import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotacorte;
import br.com.linkcom.wms.geral.dao.RotacorteDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class RotacorteService extends GenericService<Rotacorte>{

	private RotacorteDAO rotacorteDAO;

	public void setRotacorteDAO(RotacorteDAO rotacorteDAO) {
		this.rotacorteDAO = rotacorteDAO;
	}

	/**
	 * Método responsável pela enfileração dos registros.
	 * Confirma se existe algum corte realizado na data de hoje.
	 * Caso não exista vou adicionar o registro na fila de integração.
	 * 
	 * @see TMS_RE.BPEL_INT_TMS_RE_ROTA_CORTA
	 * @param cdrota
	 * @return
	 */
	public Boolean isIntegrado(Rota rota){
		
		if(rota==null || rota.getCdrota()==null)
			throw new WmsException("Parâmetros inválidos");
		
		Rotacorte rotacorte = rotacorteDAO.findByRota(rota);
		
		if(rotacorte!=null){
			if(rotacorte.getFlag_status().equals("C"))
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}else{
			Rotacorte rc = new Rotacorte(rota.getCdrota(),WmsUtil.getDeposito().getCodigoerp());
			this.saveOrUpdate(rc);
			return Boolean.FALSE;
		}
		
	}

	/**
	 * 
	 * @param jdbcTemplateRELO
	 * @return
	 */
	public boolean corteDiarioRealizado(JdbcTemplate jdbcTemplateRELO) {
		
		Rotacorte rotacorte = rotacorteDAO.corteDiarioRealizado();
		
		if(rotacorte!=null){
			return true;
		}else{
			return false;	
		}
		
	}
	
}
