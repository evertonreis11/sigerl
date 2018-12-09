package br.com.linkcom.wms.geral.service;

import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Acaopapel;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.dao.AcaopapelDAO;

public class AcaopapelService extends GenericService<Acaopapel> {
	
	private AcaopapelDAO acaopapelDAO;
	
	public void setAcaopapelDAO(AcaopapelDAO acaopapelDAO) {
		this.acaopapelDAO = acaopapelDAO;
	}
	
	
	/**
	 * Método de referência ao DAO.
	 * Pega todos os ação papel a partir de um papel especificado.
	 * 
	 * @see br.com.linkcom.sined.geral.dao.AcaopapelDAO#findAllBy(Papel papel)
	 * @param papel
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Acaopapel> findAllBy(Papel papel){
		return acaopapelDAO.findAllBy(papel);
	}
	
	/**
	 * Pega todos os ação papel a partir de um papel especificado.
	 * @param papel
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Acaopapel> findAllPermissionsByPapel(Set<Usuariopapel> lista){
		return acaopapelDAO.findAllPermissionsByPapel(lista);
	}
	
	/* singleton */
	private static AcaopapelService instance;
	
	public static AcaopapelService getInstance() {
		if(instance == null){
			instance = Neo.getObject(AcaopapelService.class);
		}
		return instance;
	}
	
	/**
	 * Verifica se um dado usuário tem permissão para executar uma ação.
	 * 
	 * @author Giovane Freitas
	 * @param usuario 
	 * @param string
	 * @return
	 */
	public boolean isUserHasAction(Usuario usuario, String acao) {
		for (Usuariopapel usuariopapel : usuario.getListaUsuariopapel())
			if (usuariopapel.getPapel().isAdmin())
				return true;
		
		return acaopapelDAO.isUserHasAction(usuario, acao);
	}
	
}
