package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.dao.PapelDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PapelService extends GenericService<Papel> {
	
	private PapelDAO papelDAO;
	
	public void setPapelDAO(PapelDAO papelDAO) {
		this.papelDAO = papelDAO;
	}
	/**
	 * Método para salvar a lista de papel em usuário papel	 * 
	 * @param listaPapel
	 * @param bean
	 * @return
	 * @author Joao Paulo Zica
	 */
	public List<Usuariopapel> saveUsuariopapel(List<Papel> listaPapel, Usuario bean){
		List<Usuariopapel> listaUsuariopapel = new ListSet<Usuariopapel>(Usuariopapel.class);
		if (listaPapel != null && bean != null) {
			for (Papel papel : listaPapel) {
				Usuariopapel usuariopapel = new Usuariopapel();
				usuariopapel.setPapel(papel);
				usuariopapel.setUsuario(bean);
				listaUsuariopapel.add(usuariopapel);
			}
		}
		return listaUsuariopapel;
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @param papel
	 */
	public void disable(Papel papel){
		if(WmsUtil.getUsuarioLogado().getListaPapel() != null && WmsUtil.getUsuarioLogado().getListaPapel().contains(papel)){
			throw new WmsException("O(s) registro(s) não pode(m) ser excluído(s), já possui(em) referências em outros registros do sistema.");
		}
		else papelDAO.disable(papel);
	}
	
	/* singleton */
	private static PapelService instance;
	public static PapelService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PapelService.class);
		}
		return instance;
	}
	
	/**
	 * Cria uma lista de papel através de uma lista 
	 * de usuariopapel
	 * @author Leonardo Guimarães
	 * @param listaUsuariopapel
	 * @param usuario
	 * @return
	 */
	public List<Papel> carregaPapel(Usuario usuario){
		if(usuario == null){
			throw new WmsException("O usuario não deve ser nulo");
		}
		Set<Usuariopapel> listaUsuariopapel = new ListSet<Usuariopapel>(Usuariopapel.class);
		List<Papel> listaPapel = new ArrayList<Papel>();
		if (usuario.getListaUsuariopapel() != null) {
			listaUsuariopapel = usuario.getListaUsuariopapel();
			for (Usuariopapel usuariopapel : listaUsuariopapel) {
				listaPapel.add(usuariopapel.getPapel());
			}
		}
		return listaPapel;
	}	
	
	@Override
	public void delete(Papel bean) {
		disable(bean);
	}
	
	@Override
	public void saveOrUpdate(Papel bean) {
		super.saveOrUpdate(bean);
	}

	/**
	 * 
	 * @return
	 */
	public List<Papel> findAllWithoutAdm() {
		return papelDAO.findAllWithoutAdm();
	}
	
	/***
	 * 
	 * @param usuario
	 * @return
	 */
	public Papel isUsuarioMaster(Usuario usuario) {
		return papelDAO.isUsuarioMaster(usuario);
	}
}
