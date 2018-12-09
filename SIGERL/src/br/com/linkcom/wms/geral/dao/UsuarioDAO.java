package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.modulo.sistema.controller.crud.filtro.UsuarioFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("usuario.login")
public class UsuarioDAO extends GenericDAO<Usuario> {
						
	@Override
	public void updateListagemQuery(QueryBuilder<Usuario> query, FiltroListagem _filtro) {
		UsuarioFiltro filtro = (UsuarioFiltro) _filtro;
		query.leftOuterJoinFetch("usuario.listaUsuarioDeposito usuariodeposito")
				.leftOuterJoinFetch("usuariodeposito.deposito deposito")
				.leftOuterJoin("usuario.listaUsuariopapel listaUsuariopapel")
				.leftOuterJoin("listaUsuariopapel.papel papel")
				.leftOuterJoin("usuario.listaUsuarioLinhaSeparacao listaUsuarioLinhaSeparacao")
				.leftOuterJoin("listaUsuarioLinhaSeparacao.linhaseparacao linhaseparacao")
				.whereLikeIgnoreAll("usuario.nome", filtro.getNome())			
				.whereLikeIgnoreAll("usuario.login",filtro.getLogin())
				.where("usuario.ativo = ?",filtro.getAtivo());
		if(!WmsUtil.isUsuarioLogadoAdministrador() && !WmsUtil.isUserHasAction("VINCULAR_PAPEL_USUARIO")){
			query.where("usuario=?",WmsUtil.getUsuarioLogado());
		}
		query.where("papel =?",filtro.getPapel())
			 .where("linhaseparacao = ?",filtro.getLinhaseparacao())
			 .where("deposito =? ",filtro.getDeposito());
		
		super.updateListagemQuery(query, _filtro);
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Usuario> query) {
		query
			.select("usuario.cdpessoa, usuario.nome, usuario.documento, usuario.login, usuario.ativo," +
					"usuario.senha, deposito.cddeposito, deposito.nome, papel.cdpapel, papel.nome, usuario.logincoletor," +
					"linhaseparacao.nome,linhaseparacao.cdlinhaseparacao,listaUsuarioLinhaSeparacao.cdusuariolinhaseparacao")
			.leftOuterJoin("usuario.listaUsuarioDeposito listaUsuarioDeposito")
			.leftOuterJoin("usuario.listaUsuariopapel listaUsuariopapel")
			.leftOuterJoin("usuario.listaUsuarioLinhaSeparacao listaUsuarioLinhaSeparacao")
			.leftOuterJoin("listaUsuarioDeposito.deposito deposito")
			.leftOuterJoin("listaUsuariopapel.papel papel")
			.leftOuterJoin("listaUsuarioLinhaSeparacao.linhaseparacao linhaseparacao");
			
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaUsuariopapel");
		save.saveOrUpdateManaged("listaUsuarioDeposito");
		save.saveOrUpdateManaged("listaUsuarioLinhaSeparacao");
	}
	
	/**
	 * Carrega o usu�rio e carrega o dep�sito.
	 * 
	 * @param usuario
	 * @return null se n�o encontrar
	 * @author Jo�o Paulo Zica
	 * 		   Pedro Gon�alves - Modificado para carregar os dep�sitos do usu�rio.
	 * @param dep 
	 */
	public Usuario carregarUsuario(Usuario usuario, Deposito dep) {
		if(usuario == null || usuario.getCdpessoa() == null ){
			throw new WmsException("O login n�o pode ser nulo");
		}
		return
			query()
				.leftOuterJoinFetch("usuario.listaUsuarioDeposito usuariodeposito")
				.leftOuterJoinFetch("usuariodeposito.deposito deposito")
				.leftOuterJoinFetch("usuario.listaUsuariopapel usuariopapel")
				.leftOuterJoinFetch("usuariopapel.papel papel")
				.where("deposito=?",dep)
				.entity(usuario)
				.unique();
	}
	
	/**
	 * M�todo para usu�rio que n�o s�o administradores e s� podem fazer altera��o de senha
	 * 
	 * @param usuario
	 * @author Jo�o Paulo Zica
	 */
	public void alterarSenhaUsuario(Usuario usuario){
		if(usuario == null || usuario.getCdpessoa() == null){
			throw new WmsException("O usuario n�o pode ser nulo");
		}
		Usuario load = this.load(usuario);
		load.setSenha(usuario.getSenha());
		save(load).execute();
		getHibernateTemplate().flush();
	}	
	
	@Override
	public Usuario load(Usuario bean) {
		if(bean == null || bean.getCdpessoa() == null){
			throw new WmsException("O usuario n�o deve ser nulo");
		}
		return query()
					.select("usuario.cdpessoa,usuario.login,usuario.senha,usuario.trocasenha," +
							"usuario.nome,usuario.pessoanatureza,usuario.documento,usuario.ativo,papel.cdpapel, usuario.logincoletor")
					.leftOuterJoin("usuario.listaUsuariopapel listaUsuariopapel")
					.leftOuterJoin("listaUsuariopapel.papel papel")
					.where("usuario.cdpessoa=?",bean.getCdpessoa())
					.unique();
	}
	
	/**
	 * Carrega o usu�rio a partir do Login.
	 * 
	 * @param login - login do usu�rio.
	 * @return
	 * @author Pedro Gon�alves
	 */
	public Usuario findByLogin(String login) {
		if(login == null || login.equals("")){
			throw new WmsException("O usuario n�o deve ser nulo");
		}
		return query()
					.leftOuterJoinFetch("usuario.listaUsuariopapel listaUsuariopapel")
					.leftOuterJoinFetch("listaUsuariopapel.papel papel")
					.leftOuterJoinFetch("usuario.listaUsuarioDeposito usuariodeposito")
					.leftOuterJoinFetch("usuariodeposito.deposito deposito")
					.where("usuario.logincoletor = ?",login)
					.where("deposito.ativo=1")
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * Busca os usuario pelo login
	 * 
	 * @author Leonardo Guimar�es
	 *  
	 * @param login
	 * @return
	 */
	public Usuario findByLoginUsuario(String login){
		if(login == null){
			throw new WmsException("O usuario n�o deve ser nulo");
		}
		return query()
			.select("usuario.cdpessoa, usuario.nome, usuario.documento, usuario.login, usuario.ativo," +
			"usuario.senha, deposito.cddeposito, deposito.nome, papel.cdpapel, papel.nome, papel.administrador, usuario.logincoletor")
			.leftOuterJoin("usuario.listaUsuarioDeposito listaUsuarioDeposito")
			.leftOuterJoin("usuario.listaUsuariopapel listaUsuariopapel")
			.leftOuterJoin("listaUsuarioDeposito.deposito deposito")
			.leftOuterJoin("listaUsuariopapel.papel papel")
			.where("usuario.login = ?", login)
			.unique();
	}
	/**
	 * Desativa o usuario encontrado atrav�s do cdpessoa
	 * @author Leonardo Guimar�es
	 * @param bean
	 */
	public void desativa(Usuario bean) {
		if(bean == null || bean.getCdpessoa() == null ){
			throw new WmsException("O usu�rio ou cdpessoa n�o deve ser nulo");
		}
		getJdbcTemplate().update("update pessoa set ativo=? where cdpessoa=?",new Object[]{false,bean.getCdpessoa()});		
	}
	
	/**
	 * 
	 * Metodo que recupera as informa��es de pessoa de acordo com um papel
	 * 
	 * @param nomePapel
	 * @return
	 * 
	 * @author Arantes
	 * 
	 */
	public List<Usuario> findForPapel(Papel papel) {
		return baseForCombo()
					.leftOuterJoin("usuario.listaUsuariopapel usuariopapel")
					.join("usuariopapel.papel papel")
					.leftOuterJoin("usuario.listaUsuarioDeposito usuariodeposito")
					.join("usuariodeposito.deposito deposito")
					.where("papel = ?", papel)
					.where("deposito = ?", WmsUtil.getDeposito())
					.orderBy("usuario.nome")
					.list();
	}
	
	/**
	 * <p>Fornece a senha de um usu�rio.</p>
	 * @param usuario
	 * @return
	 * @author Hugo Ferreira
	 */
	public String carregarSenha(Usuario usuario) {
		if (usuario == null || usuario.getCdpessoa() == null) {
			throw new WmsException("Par�metros inv�lidos em usu�rio.");
		}
		
		return newQueryBuilderWithFrom(String.class)
			.select("usuario.senha")
			.where("usuario = ?", usuario)
			.setUseTranslator(false)
			.unique();
	}

	/* singleton */
	private static UsuarioDAO instance;
	public static UsuarioDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(UsuarioDAO.class);
		}
		return instance;
	}
	
	/**
	 * Busca os nomes de todos os usu�rios ativos
	 * @return
	 */
	public List<Usuario> findAllAtivos() {
		return query()
				.select("usuario.cdpessoa, usuario.login")
				.where("usuario.ativo is true")
				.orderBy("usuario.login")
				.list();
	}	
	
	@Override
	public List<Usuario> findForCombo(String orderby, String... extraFields) {
		return baseForCombo()
					.orderBy("usuario.nome")
					.list();
					
					
	}
	
	/**
	 * Query base para a cria��o de combos de usu�rio
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @return
	 */
	private QueryBuilder<Usuario> baseForCombo() {
		return query()
					.select("usuario.cdpessoa, usuario.nome")
					.where("usuario.ativo is true");
	}
	
	/**
	 * 
	 * M�todo que recupera uma lista de usu�rios que possuem ordens de servi�o.
	 * 
	 * @author Arantes
	 * 
	 * @return List<Usuario>
	 * 
	 */
	public List<Usuario> findAllForProdutividade() {
		return baseForCombo()
			.join("usuario.listaOrdemservicousuario ordemservicousuario")
			.orderBy("usuario.nome")
			.list();
	}

	/**
	 * Busca os nomes de todos os usu�rios ativos
	 * @return
	 */
	public List<Usuario> findAllAtivosByDeposito(Deposito deposito) {
		return query()
				.select("usuario.cdpessoa, usuario.login")
				.join("usuario.listaUsuarioDeposito listaUsuarioDeposito")
				.join("listaUsuarioDeposito.deposito deposito")
				.where("usuario.ativo is true")
				.where("deposito = ?",deposito)
				.orderBy("usuario.login")
				.list();
	}	
	
}