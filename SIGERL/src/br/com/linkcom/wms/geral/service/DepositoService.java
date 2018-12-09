package br.com.linkcom.wms.geral.service;
 
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.Cnpj;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.dao.DepositoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class DepositoService extends GenericService<Deposito> {
	
	protected DepositoDAO depositoDAO;
	
	public void setDepositoDAO(DepositoDAO depositoDAO) {
		this.depositoDAO = depositoDAO;
	}
	
	/**
	 * Cria uma lista de usuariodeposito atr�ves de uma lista de dep�sitos e um usu�rio
	 * @author Leonardo Guimar�es
	 * @param listadeposito
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Usuariodeposito> makeListaUsuarioDeposito(List<Deposito> listadeposito, Usuario bean) {
		List<Usuariodeposito> listaUsuarioDeposito = new ArrayList<Usuariodeposito>(); 
		if(listadeposito != null && bean != null){
			for(Deposito deposito:listadeposito){
				Usuariodeposito usuariodeposito = new Usuariodeposito();
				usuariodeposito.setDeposito(deposito);
				usuariodeposito.setUsuario(bean);
				listaUsuarioDeposito.add(usuariodeposito);
			}
		}
		
		return listaUsuarioDeposito;
	}
	
	/* singleton */
	private static DepositoService instance;
	public static DepositoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(DepositoService.class);
		}
		return instance;
	}
	
	/**
	 * Retorna os dep�sitos dos usu�rios
	 * @param usuario
	 * @return
	 * @author C�ntia Nogueira
	 */
	public List<Deposito> findDepositos(Usuario usuario){
		return depositoDAO.findDepositos(usuario);
	}

	/**
	 * 
	 * @param cnpj
	 * @return
	 */
	public Deposito findByCNPJ(Cnpj cnpj){
		return depositoDAO.findByCNPJ(cnpj);
	}
	
	/**
	 * Depositos ativos nos quais o usu�rio tem permiss�o
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivos(Usuario usuario){		
		return depositoDAO.findAtivos(usuario);
	}
	
	/**
	 * Depositos ativos para pagina de Index de autoriza��o
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivosIndex(){		
		return depositoDAO.findAtivosIndex();
	}
	
	/**
	 * Depositos ativos para pagina de Index de autoriza��o
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivosIndexByUsuario(Usuario usuario){		
		return depositoDAO.findAtivosIndexByUsuario(usuario);
	}
	
	/**
	 * 
	 * @param deposito
	 * @return
	 */
	public Empresa findEmpresaByDeposito(Deposito deposito) {
		deposito = depositoDAO.findEmpresaByDeposito(deposito);
		return deposito.getEmpresa();
	}

	/**
	 * 
	 * @return
	 */
	public List<Deposito> findAtivos(){
		return depositoDAO.findAtivos();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Deposito> findAllCdsAtivo() {
		return depositoDAO.findAllCdsAtivo();
	}

	/**
	 * 
	 * @return
	 */
	public List<Deposito> findAllCdasAtivo() {
		return depositoDAO.findAllCdasAtivo();
	}
}
