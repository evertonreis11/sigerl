package br.com.linkcom.wms.geral.service;

import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Pessoanatureza;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.dao.UsuarioDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class UsuarioService extends GenericService<Usuario> {

	private UsuarioDAO usuarioDAO;
	private PapelService papelService;
	private DepositoService depositoService;
	private PessoanaturezaService pessoanaturezaService;
	private LinhaseparacaoService linhaseparacaoService;
	
	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
	
	public void setPapelService(PapelService papelService) {
		this.papelService = papelService;
	}
	
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	
	public void setPessoanaturezaService(PessoanaturezaService pessoanaturezaService) {
		this.pessoanaturezaService = pessoanaturezaService;
	}
	
	public void setLinhaseparacaoService(LinhaseparacaoService linhaseparacaoService) {
		this.linhaseparacaoService = linhaseparacaoService;
	}
	
	/**
	 * Método de referência ao dao
	 * @param email
	 * @param login
	 * @return
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#carregarUsuario
	 * @author João Paulo Zica
	 * @param dep 
	 */
	public Usuario carregarUsuario(Usuario usuario, Deposito dep) {
		return usuarioDAO.carregarUsuario(usuario,dep);
	}
	 	
	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#alterarSenhaUsuario
	 * @author Leonardo Guimarães
	 * @param usuario
	 */
	public void alterarSenhaUsuario(Usuario usuario){
		usuarioDAO.alterarSenhaUsuario(usuario);
	}

	
	/* singleton */
	private static UsuarioService instance;
	public static UsuarioService getInstance() {
		if(instance == null){
			instance = Neo.getObject(UsuarioService.class);
		}
		return instance;
	}
	
	/**
	 * Seta a listaUsuarioPapel, ListaUsuarioDeposito e a natureza
	 * de um bean de usuário
	 * @author Leonardo Guimarães
	 * @param bean
	 * @return
	 */
	public void preparaBean(Usuario bean) {
		if(bean == null){
			throw new WmsException("O usuario não deve ser nulo");
		}
		List<Papel> listaPapel = bean.getListaPapel();
		bean.setListaUsuariopapel(new ListSet<Usuariopapel>(Usuariopapel.class,papelService.saveUsuariopapel(listaPapel, bean)));
		bean.setListaUsuarioDeposito(new ListSet<Usuariodeposito>(Usuariodeposito.class,depositoService.makeListaUsuarioDeposito(bean.getListadeposito(),bean)));
		bean.setListaUsuarioLinhaSeparacao((linhaseparacaoService.makeListaUsuarioLinhaSeparacao(bean)));
		bean.setPessoanatureza(pessoanaturezaService.findPessoanatureza(Pessoanatureza.FISICA));
		bean.setTrocasenha(false);
		if(bean.getCdpessoa() == null){
			bean.setAtivo(true);
		}
	}
	
	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#desativa(Usuario bean)
	 * @author Leonardo Guimarães
	 * @param bean
	 */
	public void desativa(Usuario bean) {
		if(bean.getCdpessoa().equals(WmsUtil.getUsuarioLogado().getCdpessoa())){
			throw new WmsException("Não foi possível excluir \""+WmsUtil.getUsuarioLogado().getLogin()+
			"\" pois esse perfil possui referências à ele(a)");
			
		} else usuarioDAO.desativa(bean);	
	}
	
	@Override
	public void delete(Usuario bean) {
		desativa(bean);
	}
	
	/**
	 * Salva os dados do usuário
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.util.neo.persistence.GenericService.saveOrUpdate(Usuario bean)
	 * @param bean
	 */
	public void salvarUsuario(final WebRequestContext request, final Usuario bean){
		preparaBean(bean);
		saveOrUpdate(bean);
		request.addMessage("Registro(s) salvo(s) com sucesso.");
	}
	
	/**
	 * 
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#findForPapel(String)
	 * @param nomePapel
	 * @return
	 * 
	 * @author Arantes
	 * 
	 */
	public List<Usuario> findForPapel(Papel papel) {
		return usuarioDAO.findForPapel(papel);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#findAll(orderBy)
	 * @param orderBy
	 * @return
	 * 
	 * @author Ramon Brazil
	 */
	public List<Usuario> findAll(String orderBy) {
		return usuarioDAO.findAll(orderBy);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#carregarSenha(Usuario)
	 * @param usuario
	 * @return
	 * @author Hugo Ferreira
	 * 
	 */
	public String carregarSenha(Usuario usuario) {
		return usuarioDAO.carregarSenha(usuario);
	}
	
	/**
	 * Método de referência ao DAO.
	 * Dependendo do parâmetro passado em encrypt, criptografa a senha antes de salvar.
	 *
	 * @see #encryptPassword(Usuario)
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#alterarSenhaUsuario
	 * @param usuario
	 * @param encrypt
	 * @author Flávio Tavares
	 */
	public void alterarSenhaUsuario(Usuario usuario, boolean encrypt){
		if(encrypt)
			encryptPassword(usuario);
		
		usuarioDAO.alterarSenhaUsuario(usuario);
	}
	
	/**
	 * Método responsável por criptografar a senha do usuario.
	 * Criptografa somente quando ela é alterada ou se é registro novo
	 * 
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#load(Usuario)
	 * @param bean
	 * 
	 * @author Flávio Tavares
	 * 
	 */
	private void encryptPassword(Usuario bean){
		Usuario load = usuarioDAO.load(bean);
		
		if(load == null || !load.getSenha().equals(bean.getSenha())) {
			StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
			bean.setSenha(encryptor.encryptPassword(bean.getSenha()));
		}
	}
	
	/**
	 * Método de referência ao DAO.
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#findByLogin(String login)
	 * @param login
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Usuario findByLogin(String login) {
		return usuarioDAO.findByLogin(login);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO.findAllAtivos()
	 * 
	 * @return
	 */
	public List<Usuario> findAllAtivos() {
		return usuarioDAO.findAllAtivos();
	}

	
	public Usuario findByLoginUsuario(String login) {
		return usuarioDAO.findByLoginUsuario(login);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que recupera uma lista de usuários que possuem ordens de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.UsuarioDAO#findAllForProdutividade()
	 * 
	 * @author Arantes
	 * 
	 * @return List<Usuario>
	 * 
	 */
	public List<Usuario> findAllForProdutividade() {
		return usuarioDAO.findAllForProdutividade();
	}

}