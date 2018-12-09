package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestohistorico;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ManifestohistoricoDAO extends GenericDAO<Manifestohistorico>{

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Manifestohistorico> findByManifesto(Manifesto manifesto) {
		
		QueryBuilder<Manifestohistorico> query = query();
		
		query.select("manifestohistorico.cdmanifestohistorico, manifestohistorico.dtalteracao, manifestohistorico.motivo," +
						"manifesto.cdmanifesto, manifestostatus.cdmanifestostatus, manifestostatus.nome, usuario.cdpessoa, usuario.nome ")
				.join("manifestohistorico.manifesto manifesto")
				.join("manifestohistorico.manifestostatus manifestostatus")
				.join("manifestohistorico.usuario usuario")
				.where("manifesto = ?",manifesto)
				.orderBy(" manifestohistorico.dtalteracao DESC");
				
		
		return query.list();
	}

	/**
	 * 
	 * @param manifesto
	 */
	public void deleteByManifesto(String whereIn) {
		
		if(whereIn == null || whereIn.isEmpty()){
			throw new WmsException("Parametros inválidos, erro ao excluir o histório do manifesto.");
		}
		
		getHibernateTemplate().bulkUpdate("delete from Manifestohistorico mh where mh.manifesto.id in ("+whereIn+") ");
	}
	
}
