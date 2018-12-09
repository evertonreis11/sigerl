package br.com.linkcom.wms.geral.service;

import java.util.Iterator;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Equipamento;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.dao.EquipamentoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class EquipamentoService extends GenericService<Equipamento>{
	
	private EquipamentoDAO equipamentoDAO;
	
	public void setEquipamentoDAO(EquipamentoDAO equipamentoDAO) {
		this.equipamentoDAO = equipamentoDAO;
	}

	/**
	 * 
	 * @param motorista
	 * @return
	 */
	public List<Equipamento> findWithOutMotoristaVinculado(Deposito deposito, Integer cdmotorista) {
		
		List<Equipamento> listaEquipamento = equipamentoDAO.findWithOutMotoristaVinculado(deposito);
		Iterator<Equipamento> iterator = listaEquipamento.iterator();
		
		while (iterator.hasNext()){
			Equipamento equipamento = iterator.next();
			Motorista m = equipamento.getMotorista();
			if (m!=null && m.getCdmotorista()!=null && !m.getCdmotorista().equals(cdmotorista))
				iterator.remove();
		}
		
		return listaEquipamento;
	}
	
	/**
	 * 
	 * @param deposito
	 * @return
	 */
	public List<Equipamento> findByDeposito (Deposito deposito){
		return equipamentoDAO.findByDeposito(deposito);
	} 
}
