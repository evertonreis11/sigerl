package br.com.linkcom.wms.geral.service;

import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariolinhaseparacao;
import br.com.linkcom.wms.geral.dao.LinhaseparacaoDAO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class LinhaseparacaoService extends GenericService<Linhaseparacao> {

		
	protected LinhaseparacaoDAO linhaseparacaoDAO;
	
	public void setLinhaseparacaoDAO(LinhaseparacaoDAO linhaseparacaoDAO) {
		this.linhaseparacaoDAO = linhaseparacaoDAO;
	}
		
	@Override
	public void saveOrUpdate(Linhaseparacao bean) {
		bean.setDeposito(WmsUtil.getDeposito());
		super.saveOrUpdate(bean);
	}
	
	/**
	 * Cria uma lista de usuariolinhaseparacao atráves do usuário e uma lista de linhas de separação
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param usuario
	 * @return
	 */
	public Set<Usuariolinhaseparacao> makeListaUsuarioLinhaSeparacao(Usuario usuario) {
		Set<Usuariolinhaseparacao> listaUsuarioLinhaSeparacao = new ListSet<Usuariolinhaseparacao>(Usuariolinhaseparacao.class);
		
		if(usuario != null && usuario.getListaLinhaSeparacao() != null)
			for(Linhaseparacao linhaseparacao : usuario.getListaLinhaSeparacao()){
				Usuariolinhaseparacao usuariolinhaseparacao = new Usuariolinhaseparacao();
				usuariolinhaseparacao.setUsuario(usuario);
				usuariolinhaseparacao.setLinhaseparacao(linhaseparacao);
				listaUsuarioLinhaSeparacao.add(usuariolinhaseparacao);
			}
		
		return listaUsuarioLinhaSeparacao;
	}
	
	/**
	 * Busca as linhas de separação associadas ao usuário
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param usuario
	 * @param deposito 
	 * @return
	 */
	public List<Linhaseparacao> findByUsuario(Usuario usuario, Deposito deposito) {
		return linhaseparacaoDAO.findByUsuario(usuario, deposito);
	}
	
	/**
	 * Busca as linhas de separação associadas ao depósito
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param deposito
	 * @return
	 */
	public List<Linhaseparacao> findByDeposito(Deposito deposito) {
		return linhaseparacaoDAO.findByDeposito(deposito);
	}

	/* singleton */
	private static LinhaseparacaoService instance;
	public static LinhaseparacaoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(LinhaseparacaoService.class);
		}
		return instance;
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param ordemservico
	 * @return
	 * @author Tomás Rabelo
	 */
	public Linhaseparacao findByOrdemservico(Ordemservico ordemservico) {
		return linhaseparacaoDAO.findByOrdemservico(ordemservico);
	}
}
