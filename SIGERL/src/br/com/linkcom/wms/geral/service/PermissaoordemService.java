package br.com.linkcom.wms.geral.service;

import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Permissaoordem;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.dao.PermissaoordemDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

public class PermissaoordemService extends GenericService<Permissaoordem> {
	
	private PermissaoordemDAO permissaoordemDAO;
	
	public void setPermissaoordemDAO(PermissaoordemDAO permissaoordemDAO) {
		this.permissaoordemDAO = permissaoordemDAO;
	}
	
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * Pega todos os permiassaoordem partir de um papel especificado.
	 * 
	 * @param papel
	 * @return
	 * @author Thiers Euller
	 */
	public List<Permissaoordem> findAllBy(Set<Usuariopapel> lista){
		return permissaoordemDAO.findAllBy(lista);
	}
	
	/**
	 * Pega todos os permissaoordem a partir de uma ordemtipo especificada.
	 * @param ordemtipo
	 * @return
	 * @author Thiers Euller
	 */
	public List<Permissaoordem> findAllPermissionsByOrdemtipo(Ordemtipo ordemtipo){
		return permissaoordemDAO.findAllPermissionsByOrdemtipo(ordemtipo);
	}
	
	/* singleton */
	private static PermissaoordemService instance;
	
	public static PermissaoordemService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PermissaoordemService.class);
		}
		return instance;
	}


	public List<Permissaoordem> findAllWithOrdemTipo(Papel papel) {
		if(papel == null || papel.getCdpapel() == null){
			throw new WmsException("Papel n�o pode ser nulo no m�todo permissaoordemService.findAllWithOrdemTipo(Papel papel)");
		}
		return permissaoordemDAO.findAllWithOrdemTipo(papel);
	}


	/**
	 * Busca todos os tipos de ordem de servi�o que est�o associadas a um usu�rio.
	 * 
	 * @param usuario
	 * @return
	 */
	public Set<Ordemtipo> findOrdemtipo(Usuario usuario) {
		return permissaoordemDAO.findOrdemtipo(usuario);
	}


	/**
	 * Verifica se um determinado usu�rio possui o perfil necess�rio para ser
	 * associado a uma determinada ordem de servi�o.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param usuario
	 * @return
	 */
	public boolean isAssociacaoValida(Ordemtipo ordemtipo, Usuario usuario) {
		if(WmsUtil.isUsuarioLogadoAdministrador(usuario))
			return true;
		else{
			Set<Ordemtipo> ordensPermitidas = this.findOrdemtipo(usuario);
			
			if (ordensPermitidas.contains(ordemtipo))
				return true;
			else
				return false;
		}
	}
	
	/**
	 * Verifica se um dos tipos de ordem de servi�o pode ser associada ao usu�rio.
	 * 
	 * @see OrdemservicousuarioService#isAssociacaoValida(Ordemtipo, Usuario)
	 * @param tipos
	 * @param usuario
	 * @return
	 */
	public boolean isAssociacaoValida(Ordemtipo[] tipos, Usuario usuario) {
		if(WmsUtil.isUsuarioLogadoAdministrador(usuario))
			return true;
		else{
			Set<Ordemtipo> ordensPermitidas = this.findOrdemtipo(usuario);
			
			for (Ordemtipo tipo : tipos)
				if (ordensPermitidas.contains(tipo))
					return true;
			
			return false;
		}
	}

	
}
