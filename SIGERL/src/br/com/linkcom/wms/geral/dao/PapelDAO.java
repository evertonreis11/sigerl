package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.modulo.sistema.controller.crud.filtro.PapelFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("papel.nome")
public class PapelDAO extends GenericDAO<Papel> {

	@Override
	public void updateListagemQuery(QueryBuilder<Papel> query, FiltroListagem _filtro) {
		if(_filtro == null){
			throw new WmsException("O filtro n�o pode ser nulo");
		}
		PapelFiltro filtro = (PapelFiltro) _filtro;
		query
			.select("papel.cdpapel, papel.nome, papel.administrador, papel.descricao, papel.ativo, papel.somenteadm")
			.whereLikeIgnoreAll("papel.nome", filtro.getNome())
			.where("papel.administrador = ?",filtro.getAdministrador())
			.where("papel.ativo = ?",filtro.getAtivo())
			.where("papel.cdpapel > 100")
			.orderBy("papel.nome");
	}
	
	/**
	 * Desativa o papel ao inv�s de exclui-lo
	 * @author Leonardo Guimar�es
	 * @param papel
	 */
	public void disable(Papel papel){
		if(papel == null || papel.getCdpapel() == null){
			throw new WmsException("O papel ou cdpapel n�o deve ser nulo");
		}
		getJdbcTemplate().update("update papel set ativo=? where cdpapel=?",new Object[]{false,papel.getCdpapel()});
	}

	/**
	 * 
	 * @return
	 */
	public List<Papel> findAllWithoutAdm() {
		
		QueryBuilder<Papel> query = query();
		
			query.select(" papel.cdpapel, papel.nome, papel.administrador, papel.descricao, papel.ativo ");
			if(!WmsUtil.isUsuarioLogadoAdministrador()){
				query.where(" papel.somenteadm = 0 ");
			}
		
		return query.list();
	}
	
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	public Papel isUsuarioMaster(Usuario usuario) {
		
		QueryBuilder<Papel> query = query();
		
			query.select("papel.cdpapel, papel.nome")
				.join("papel.listaUsuariopapel usuariopapel")
				.join("usuariopapel.usuario usuario")
				.where("usuario = ?",usuario)
				.whereLikeIgnoreAll("papel.nome", "Usu�rio Master");
				
		return query.unique();
	}
	
	/**
	 * 
	 */
	@Override
	public List<Papel> findForCombo(String orderBy, String... extraFields) {
		
		QueryBuilder<Papel> query = query();
		
			query.select("papel.cdpapel, papel.nome ");
			query.orderBy("papel.nome");
		
		return query.list();
	}
	
}