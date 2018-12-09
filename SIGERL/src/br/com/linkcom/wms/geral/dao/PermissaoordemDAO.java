package br.com.linkcom.wms.geral.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Permissaoordem;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.util.WmsException;

public class PermissaoordemDAO extends GenericDAO<Permissaoordem> {
	
	/**
	 * Pega todos os permissao ordem a partir de um papel especificado.
	 * @param papel
	 * @return
	 * @author Thiers Euller
	 */
	public List<Permissaoordem> findAllBy(Set<Usuariopapel> lista){
		if(lista == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("permissaoordem.cdpermissaoordem, ordemtipo.cdordemtipo, ordemtipo.nome")
				.join("permissaoordem.ordemtipo ordemtipo")
				.join("permissaoordem.papel papel")
				.whereIn("papel.cdpapel",CollectionsUtil.listAndConcatenate(lista, "papel.cdpapel",","))
				.orderBy("ordemtipo.nome")
				.list();
	}
	
	/**
	 * Pega todos os permissao ordem a partir de uma ordem tipo especificada.
	 * @param papel
	 * @return
	 * @author Thiers Euller
	 */
	public List<Permissaoordem> findAllPermissionsByOrdemtipo(Ordemtipo ordemtipo){
		if(ordemtipo == null || ordemtipo.getCdordemtipo() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("permissaoordem.cdpermissaoordem, ordemtipo.cdordemtipo, papel.cdpapel, papel.nome, papel.descricao,papel.administrador, papel.ativo")
				.join("permissaoordem.papel papel")
				.join("permissaoordem.ordemtipo ordemtipo")
				.where("ordemtipo.cdordemtipo = ?", ordemtipo.getCdordemtipo())
				.list();
	}

	public List<Permissaoordem> findAllWithOrdemTipo(Papel papel) {
		return query()
		.joinFetch("permissaoordem.ordemtipo ordemtipo")
		.join("permissaoordem.papel papel")
		.where("papel.cdpapel = ?", papel.getCdpapel())
		.list();
	}

	/**
	 * Busca todos os tipos de ordem de serviço que estão associadas a um usuário.
	 * 
	 * @param usuario
	 * @return
	 */
	public Set<Ordemtipo> findOrdemtipo(Usuario usuario) {
		if (usuario == null || usuario.getCdpessoa() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Ordemtipo> list = new QueryBuilder<Ordemtipo>(getHibernateTemplate())
			.select("permissaoordem.ordemtipo")
			.from(Permissaoordem.class)
			.join("permissaoordem.papel papel")
			.join("papel.listaUsuariopapel usuariopapel")
			.where("usuariopapel.usuario = ?", usuario)
			.setUseTranslator(false)
			.list();
		
		return new HashSet<Ordemtipo>(list);
	}
	
}
