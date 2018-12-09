package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.geral.dao.MotoristaDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class MotoristaService extends GenericService<Motorista>{

	private MotoristaDAO motoristaDAO;

	public void setMotoristaDAO(MotoristaDAO motoristaDAO) {
		this.motoristaDAO = motoristaDAO;
	}
	
	public List<Motorista> findForComboByTransportador(Transportador transportador,Deposito deposito){
		return motoristaDAO.findForComboByTransportador(transportador,deposito);
	}

	/**
	 * 
	 * @param motorista
	 * @return
	 */
	public List<Veiculo> findForCarregamentoComboByMotorista(Motorista motorista) {
		
		List<Motorista> lista = motoristaDAO.findForCarregamentoComboByMotorista(motorista);
		List<Veiculo> listaVeiculo = new ArrayList<Veiculo>();
		
		if(lista!=null && !lista.isEmpty()){
			for (Motorista m : lista) {
				listaVeiculo.add(m.getVeiculo());
			}
		}
		
		return listaVeiculo;
	} 
	
}
