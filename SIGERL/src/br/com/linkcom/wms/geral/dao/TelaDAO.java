package br.com.linkcom.wms.geral.dao;

import java.util.HashMap;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Tela;
import br.com.linkcom.wms.modulo.sistema.controller.crud.filtro.TelaFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class TelaDAO extends GenericDAO<Tela> {
	protected HashMap<String, String> mapTelas;
	
	@Override
	public void updateListagemQuery(QueryBuilder<Tela> query, FiltroListagem _filtro) {
		TelaFiltro filtro = (TelaFiltro)_filtro;
		query.whereLikeIgnoreAll("descricao", filtro.getDescricao());		 
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Tela> query) {
		//query.leftOuterJoinFetch("funcao.listaPermissao listaPermissao");
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		//save.saveOrUpdateManaged("listaPermissao");
	}
	
	/**
	 * Captura a descrição cadastrada no banco a partir de uma url
	 * A url deve estar no formado /modulo/tela ( a mesma cadastrada no @controller na propriedade path)
	 * @param url
	 * @return Descrição da tela cadastrada no banco
	 * @author Pedro Gonçalves
	 */
	public String getTelaDescriptionByUrl(String url) {
		if(mapTelas == null){
			List<Tela> telas = findAll();
			HashMap<String, String> mapa = new HashMap<String, String>();
			for (Tela tela : telas) {
				mapa.put(tela.getPath(), tela.getDescricao());
			}
			mapTelas = mapa;
		}
		return mapTelas.get(url);
	}
	
	/**
	 * Limpa o cache de telas
	 * @author Pedro Gonçalves
	 */
	public void clearTelaCache(){
		mapTelas = null;
	}
	

	/* singleton */
	private static TelaDAO instance;
	public static TelaDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(TelaDAO.class);
		}
		return instance;
	}
}