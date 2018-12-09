package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.dao.UsuariodepositoDAO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class UsuariodepositoService extends GenericService<Usuariodeposito> {
	
	private UsuariodepositoDAO usuariodepositoDAO;

	/* singleton */
	private static UsuariodepositoService instance;
	public static UsuariodepositoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(UsuariodepositoService.class);
		}
		return instance;
	}

	public void setUsuariodepositoDAO(UsuariodepositoDAO usuariodepositoDAO) {
		this.usuariodepositoDAO = usuariodepositoDAO;
	}
	
	/**
	 * Cria uma lista de depositos através de 
	 * uma lista de usuariodeposito
	 * @author Leonardo Guimarães
	 * @param listaUsuarioDeposito
	 * @param usuario
	 * @return
	 */
	public void carregaDeposito(Usuario usuario) {
		Set<Usuariodeposito> listaUsuarioDeposito = new ListSet<Usuariodeposito>(Usuariodeposito.class);
		List<Deposito> listadeposito = new ArrayList<Deposito>();
		if(usuario.getCdpessoa() == null){
			listadeposito.add(WmsUtil.getDeposito());
			usuario.setListadeposito(listadeposito);
		}else{
			if(usuario.getListaUsuarioDeposito() != null){
				listaUsuarioDeposito = usuario.getListaUsuarioDeposito();
				for(Usuariodeposito usuariodeposito:listaUsuarioDeposito){
					listadeposito.add(usuariodeposito.getDeposito());
				}
			}
			usuario.setListadeposito(listadeposito);
		}
	}	
	

	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.UsuariodepositoDAO#findybyuser
	 * @author Leonardo Guimarães
	 * @param usuario
	 * @return
	 */
	public List<Usuariodeposito> findByUser(Usuario usuario){
		return usuariodepositoDAO.findByUser(usuario);
	}
	

}
