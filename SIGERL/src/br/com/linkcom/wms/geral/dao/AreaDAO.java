package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.AreaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("area.codigo")
public class AreaDAO extends GenericDAO<Area>{

		
	@Override
	public void updateListagemQuery(QueryBuilder<Area> query,FiltroListagem _filtro) {
		AreaFiltro filtro = (AreaFiltro) _filtro;
		query
			.select("area.cdarea, area.nome, area.codigo, deposito.cddeposito, deposito.nome")
			.join("area.deposito deposito")
			.whereLikeIgnoreAll("area.nome",filtro.getNome())
			.where("deposito=?",WmsUtil.getDeposito())
			.where("area.virtual is false")
			.where("area.box is false")
			.where("area.avaria is false");
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Area> query) {
		query
			 .select("area.cdarea, area.nome, area.observacao, area.codigo, deposito.cddeposito, deposito.nome")
			 .join("area.deposito deposito");
	}
	
	@Override
	public List<Area> findForCombo(String orderby, String... extraFields) {
		return query()
					.select("area.cdarea,area.codigo")
					.join("area.deposito deposito")
					.where("deposito =?",WmsUtil.getDeposito())
					.orderBy("area.codigo")
					.list();
	}
	
	/**
	 * 
	 * Método que retorna todas as áreas que não são virtuais
	 *  
	 * @author Arantes
	 * 
	 * @return List<Area>
	 * 
	 */
	public List<Area> findAllNaovirtual() {
		return query()
					.select("area.cdarea, area.codigo")
					.join("area.deposito deposito")
					.where("deposito = ?", WmsUtil.getDeposito())
					.where("area.virtual is false")
					.where("area.avaria is false")
					.where("area.box is false")
					.orderBy("area.codigo")
					.list();
	}
	
	/**
	 * 
	 * Método que retorna todas as áreas para o depósito atual
	 *  
	 * @author Giovane Freitas
	 * 
	 * @return List<Area>
	 * 
	 */
	public List<Area> findAllByDepositoLogado() {
		return query()
					.join("area.deposito deposito")
					.where("deposito = ?", WmsUtil.getDeposito())
					.orderBy("area.codigo")
					.list();
	}
	
	/**
	 * Busca a área pelo código
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param area
	 * @return
	 */
	public Area findByCodigo(Long codigo, Deposito deposito) {
		if(codigo == null)
			throw new WmsException("O código da área não deve ser nulo.");
		
		if(deposito == null)
			deposito = WmsUtil.getDeposito();
		
		return query()
					.join("area.deposito deposito")
					.where("area.codigo = ?",codigo)
					.where("deposito = ?",deposito)
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * Localica uma área a partir do endereço.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @return
	 */
	public Area findByEndereco(Endereco endereco) {
		return new QueryBuilder<Area>(getHibernateTemplate())
			.from(Area.class)
			.joinFetch("area.listaEndereco endereco")
			.joinFetch("area.deposito deposito")
			.setUseTranslator(false)
			.where("endereco = ?", endereco)
			.unique();
	}

	
}
