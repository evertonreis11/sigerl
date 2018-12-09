package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Descargapreco;
import br.com.linkcom.wms.geral.bean.Descargaprecoveiculo;
import br.com.linkcom.wms.geral.bean.Descargatipocalculo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.dao.DescargaprecoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class DescargaprecoService extends GenericService<Descargapreco> {
	
	DescargaprecoDAO descargaprecoDAO;
	
	public void setDescargaprecoDAO(DescargaprecoDAO descargaprecoDAO) {
		this.descargaprecoDAO = descargaprecoDAO;
	}
	
	/**
	 * Caso o objeto a ser persistido nao seja um veiculo, a sua lista de descarga preco veiculo recebe 
	 * o valor null.
	 * 
	 * @param Desacargapreco
	 * @return void 
	 * @author Guilhernme Arantes de Oliveira
	 * 
	 */
	@Override
	public void saveOrUpdate(Descargapreco bean) {
		if(bean != null && !bean.getDescargatipocalculo().getCddescargatipocalculo().equals(Descargatipocalculo.VEICULO.getCddescargatipocalculo())) {
			bean.setListaDescargaPrecoVeiculo(null);
		}
		super.saveOrUpdate(bean);
	}
	
	/**
	 * Método para validacao da existencia de tipos de veiculos repetidos na listagem de tipos de veiculos 	 
	 * 
	 * @param bean
	 * @return
	 * @author Guilherme Arantes de Oliveira
	 */
	public boolean validarTipoVeiculoPresente(Descargapreco bean) {
		List<Descargaprecoveiculo> listaDescargaPrecoVeiculo = bean.getListaDescargaPrecoVeiculo();
		List<Descargaprecoveiculo> listaAux = bean.getListaDescargaPrecoVeiculo();
		
		for (int i=0; i<listaDescargaPrecoVeiculo.size(); i++) {
			if(listaDescargaPrecoVeiculo.get(i) != null) {
				Tipoveiculo tipoVeiculo = listaDescargaPrecoVeiculo.get(i).getTipoveiculo();
				
				for (int j=i+1; j<listaAux.size(); j++) {
					Tipoveiculo tipoVeiculoAux = listaDescargaPrecoVeiculo.get(j).getTipoveiculo();
					
					if(tipoVeiculo != null && tipoVeiculo.equals(tipoVeiculoAux))
						return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.DescargaprecoDAO#findByProduto(Produto)
	 * @param produto
	 * @return
	 * 
	 * @author Arantes
	 */
	public List<Descargapreco> findByProduto(Produto produto) {		
		return descargaprecoDAO.findByProduto(produto);
		
	}
	
	/**
	 * Se a lista do findByProduto for vazia busca todos os descargapreco
	 * @param produto
	 * @return
	 */
	public List<Descargapreco> findForCalculoCobranca(Produto produto){
		List<Descargapreco> list = findByProduto(produto);
		if(list == null || list.isEmpty())
			return findAll();
		return list;
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.DescargaprecoDAO#loadFull
	 * @param descargapreco
	 * @return
	 * @author Arantes
	 */
	public Descargapreco loadFull(Descargapreco descargapreco) {
		return descargaprecoDAO.loadFull(descargapreco);
	}
}