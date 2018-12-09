package br.com.linkcom.wms.geral.dao;

import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Acaopapel;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.util.WmsException;

public class AcaopapelDAO extends GenericDAO<Acaopapel> {
	
	/**
	 * Pega todos os ação papel a partir de um papel especificado.
	 * @param papel
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Acaopapel> findAllBy(Papel papel){
		if(papel == null || papel.getCdpapel() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("acaopapel.cdacaopapel, acao.cdacao,acao.key, acao.descricao, acaopapel.permitido")
				.join("acaopapel.acao acao")
				.join("acaopapel.papel papel")
				.where("papel=?",papel)
				.orderBy("acao.descricao")
				.list();
	}
	
	/**
	 * Pega todos os ação papel a partir de um papel especificado.
	 * @param papel
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Acaopapel> findAllPermissionsByPapel(Set<Usuariopapel> lista){
		if(lista == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("acaopapel.cdacaopapel, acao.cdacao, acao.descricao,acao.key, acaopapel.permitido")
				.join("acaopapel.acao acao")
				.join("acaopapel.papel papel")
				.whereIn("papel.cdpapel",CollectionsUtil.listAndConcatenate(lista, "papel.cdpapel",","))
				.list();
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
		if(usuario == null || usuario.getCdpessoa() == null || acao == null || acao.isEmpty())
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
			.join("acaopapel.papel papel")
			.join("papel.listaUsuariopapel usuariopapel")
			.where("acaopapel.acao.key = ?", acao)
			.where("usuariopapel.usuario = ?", usuario)
			.where("acaopapel.permitido is true")
			.setMaxResults(1)
			.unique() != null;
	}
	
}
