package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipoendereco;
import br.com.linkcom.wms.geral.dao.DadologisticoDAO;
import br.com.linkcom.wms.util.WmsUtil;

public class DadologisticoService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Dadologistico>{
	
	private DadologisticoDAO dadologisticoDAO;
	
	public void setDadologisticoDAO(DadologisticoDAO dadologisticoDAO) {
		this.dadologisticoDAO = dadologisticoDAO;
	}
	
	/**
	 * Método de referência ado DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.DadologisticoDAO.findByProduto(Produto produto)
	 * @param produto
	 * @return
	 */
	public Dadologistico findByProduto(Produto produto,Deposito deposito) {
		return dadologisticoDAO.findByProduto(produto,deposito);
	}
	
	public Dadologistico loadByTipoendereco(Tipoendereco tipoendereco) {
		return dadologisticoDAO.loadByTipoendereco(tipoendereco);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.DadologisticoDAO#findByEndereco(Endereco endereco)
	 * 
	 * @param endereco
	 * @return
	 */
	public List<Dadologistico> findByEndereco(Endereco endereco) {
		return dadologisticoDAO.findByEndereco(endereco);
	}
	
	public boolean verificaLarguraExcedente(List<Dadologistico> listaDado){
		for(Dadologistico dado :listaDado){
			if(dado.getLarguraexcedente()!=null && dado.getLarguraexcedente() && dado.getDeposito()!=null 
					&& dado.getDeposito().getCddeposito()!=null && 
					dado.getDeposito().getCddeposito().equals(WmsUtil.getDeposito().getCddeposito())){
				return true;
			}
		}
		return false;
	}
	
	/* singleton */
	private static DadologisticoService instance;
	public static DadologisticoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(DadologisticoService.class);
		}
		return instance;
	}

	/**
	 * Atualiza o campo 'NormaVolume' de um determinado produto
	 * em todos os depósitos cadastrados.
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param normavolume
	 */
	public void updateNormavolume(Produto produto, boolean normavolume) {
		dadologisticoDAO.updateNormavolume(produto, normavolume);
	}

}
