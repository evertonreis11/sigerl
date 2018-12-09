package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoerrado;
import br.com.linkcom.wms.geral.dao.OrdemservicoprodutoerradoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class OrdemservicoprodutoerradoService extends GenericService<Ordemservicoprodutoerrado> {

	private static OrdemservicoprodutoerradoService instance;
	private OrdemservicoprodutoerradoDAO ordemservicoprodutoerradoDAO;

	public static OrdemservicoprodutoerradoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(OrdemservicoprodutoerradoService.class);
		}
		return instance;
	}
	
	public void setOrdemservicoprodutoerradoDAO(OrdemservicoprodutoerradoDAO ordemservicoprodutoerradoDAO) {
		this.ordemservicoprodutoerradoDAO = ordemservicoprodutoerradoDAO;
	}

	/**
	 * Localiza os bloqueios pendentes de autorização para uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoprodutoerrado> findBloqueiosAtivos(Ordemservico ordemservico) {
		return ordemservicoprodutoerradoDAO.findBloqueiosAtivos(ordemservico);
	}

	
	/**
	 * Excluir todos os registros associados a um determinado carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	public void deleteByCarregamento(Carregamento carregamento) {
		ordemservicoprodutoerradoDAO.deleteByCarregamento(carregamento);
	}
	
}
