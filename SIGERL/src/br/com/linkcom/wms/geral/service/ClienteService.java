package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.dao.ClienteDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ClienteService extends GenericService<Cliente> {

	private ClienteDAO clienteDAO;
	
	public void setClienteDAO(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param cd
	 * @return
	 */
	public Cliente findByCd(Integer cd){
		return clienteDAO.findByCd(cd); 
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ClienteDAO#findByVformacaoCarga() 
	 *
	 * @return
	 */
	public List<Cliente> findByVformacaoCarga(){
		return clienteDAO.findByVformacaoCarga();
	}
	
	/* singleton */
	private static ClienteService instance;
	public static ClienteService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ClienteService.class);
		}
		return instance;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ClienteDAO#findFilialEntrega(Ordemservico os)
	 * 
	 * @param os
	 * @return
	 */
	public Cliente findFilialEntrega(Ordemservico os) {
		return clienteDAO.findFilialEntrega(os);
	}

	/**
	 * Método com referência no DAO
	 * @param cliente
	 * @return
	 */
	public List<Cliente> findAllFiliais(Cliente cliente) {
		return clienteDAO.findAllFiliais(cliente);
	}

	/**
	 * Retorna os clientes com o documento
	 * @param documento
	 * @return
	 * @author Cìntia Nogueira
	 */
	public List<Cliente> findByDocumento(String documento){
		return clienteDAO.findByDocumento(documento);
	}
	
	/**
	 * Retorna cliente com seu endereço
	 * @param cliente
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Cliente loadWithEndereco(Cliente cliente){
		return clienteDAO.loadWithEndereco(cliente);
	}

	/**
	 * Localiza os nomes das filiais para o mapa de separação.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<String> findNomeFilialEntrega(Ordemservico ordemservico) {
		return clienteDAO.findNomeFilialEntrega(ordemservico);
	}

	/**
	 * Localiza os nomes dos clientes para o mapa de separação.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<String> findNomeCliente(Ordemservico ordemservico) {
		return clienteDAO.findNomeCliente(ordemservico);
	}

	public Cliente findByCodigoERP(Long codigoERP){
		return clienteDAO.findByCodigoERP(codigoERP);
	}
	
	public Cliente findByCodigoERPFilial1(Long codigoERP){
		return clienteDAO.findByCodigoERPFilial1(codigoERP);
	}

	/**
	 * 
	 * @return
	 */
	public List<Cliente> findFilialByDepositoLogado() {
		return clienteDAO.findFilialByDepositoLogado();
	}
	
	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Long getCodigoerp(Cliente filial) {
		if(filial!=null){
			Cliente cliente = clienteDAO.getCodigoerp(filial);
			if(cliente!=null){
				return cliente.getCodigoerp();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
}
