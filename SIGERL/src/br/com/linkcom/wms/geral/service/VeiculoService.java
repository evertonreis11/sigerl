package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.geral.dao.VeiculoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class VeiculoService extends GenericService<Veiculo> {

	private VeiculoDAO veiculoDAO;
	
	public void setVeiculoDAO(VeiculoDAO veiculoDAO) {
		this.veiculoDAO = veiculoDAO;
	}
	
	/* singleton */
	private static VeiculoService instance;
	public static VeiculoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(VeiculoService.class);
		}
		return instance;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.VeiculoDAO#findForCarregamentoCombo(Tipoveiculo tipoveiculo, Boolean onlyDisponiveis)
	 * 
	 * @param transportador
	 * @param onlyDisponiveis
	 * @return
	 */
	public List<Veiculo> findForCarregamentoCombo(Transportador transportador) {
		return veiculoDAO.findForCarregamentoCombo(transportador);
	}
	
	/**
	 * Método que carrega veiculos para preencher combo no flex
	 * 
	 * @return
	 */
	public List<Veiculo> findAllFlex(){
		return veiculoDAO.findForCombo(null, new String [] {});
	}
	
	/**
	 * Método com referência no DAO
	 * Este método é chamado a partir da tela de carregamento flex
	 * 
	 * @param veiculo
	 * @return
	 */
	public Veiculo carregar(Veiculo veiculo){
		if(veiculo == null)
			return null;
		else
			return veiculoDAO.carregar(veiculo);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param tipoveiculo
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Veiculo> findVeiculosByTipoVeiculo(Tipoveiculo tipoveiculo) {
		return veiculoDAO.findVeiculosByTipoVeiculo(tipoveiculo);
	}

	/**
	 * 
	 * @param transportador
	 * @param deposito
	 * @return
	 */
	public List<Veiculo> findForComboByTransportador(Transportador transportador, Deposito deposito) {
		return veiculoDAO.findForComboByTransportador(transportador,deposito);
	}
	
}
