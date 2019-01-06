package br.com.linkcom.wms.geral.dao;
 
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Pessoanatureza;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.TransportadorFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

@DefaultOrderBy("transportador.nome")
public class TransportadorDAO extends GenericDAO<Transportador> {

	/* singleton */
	private static TransportadorDAO instance;
	public static TransportadorDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(TransportadorDAO.class);
		}
		return instance;
	}
	
	/**
	 * Método que encontra um transportador pela pk
	 * @author Leonardo Guimarães
	 * @param cd
	 * @return
	 */
	public Transportador findTransportadorByCd(Integer cd){
		if(cd == null){
			throw new WmsException("O cd não deve ser nulo");
		}
		return query()
					.select("transportador.cdpessoa,transportador.nome,transportador.pessoanatureza,transportador.documento, transportador.ativo")
					.where("transportador.cdpessoa=?",cd)
					.unique();
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Transportador> query) {
		super.updateEntradaQuery(query);
			query.joinFetch("transportador.pessoanatureza pessoanatureza")
			.leftOuterJoinFetch("transportador.listaTransDeposito listaTransDeposito")
			.leftOuterJoinFetch("listaTransDeposito.deposito deposito")
			.leftOuterJoinFetch("transportador.pessoaendereco");
	}

	@Override
	public void updateListagemQuery(QueryBuilder<Transportador> query,
			FiltroListagem _filtro) {
		
		TransportadorFiltro filtro = (TransportadorFiltro) _filtro;
		query.joinFetch("transportador.pessoanatureza pessoanatureza");
		query.whereLikeIgnoreAll("transportador.nome", filtro.getNome());
		query.where("transportador.ativo = ?", filtro.getAtivo());
		query.where("transportador.pessoanatureza = ?", filtro.getPessoanatureza());
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaTransDeposito");
	}
	
	@Override
	public List<Transportador> findForCombo(String orderby, String... extraFields) {
		return query()
				.select("transportador.cdpessoa, transportador.nome, listaTransDeposito.cdtransportadordeposito, depositoTransportador.cddeposito")
				.leftOuterJoin("transportador.listaTransDeposito listaTransDeposito")
				.leftOuterJoin("listaTransDeposito.deposito depositoTransportador")
				.where("depositoTransportador = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
				.where("transportador.ativo is true")
				.orderBy("transportador.nome")
				.list();
	}

	public void desativa(Transportador bean) {
		if(bean == null || bean.getCdpessoa() == null){
			throw new WmsException("O bean ou cdpessoa não deve ser nulo");
		}
		Object[] obj = new Object[] { false,bean.getCdpessoa() };
		getJdbcTemplate().update("update pessoa set ativo=? where pessoa.cdpessoa=?",obj);
	}
	
	/**
	 * Retorna os transportadores com o nome
	 * @param nome
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Transportador> findByName(String nome){
		if(nome==null || nome.isEmpty()){
			throw new WmsException("Nome não pode ser nulo em TransportadorDAO.");
		}
		return query()
				.select("transportador.cdpessoa, transportador.nome")
				.where("transportador.nome=?", nome)
				.where("transportador.ativo=1")
				.list();
	}

	/**
	 * Retorna os transportadores com o documento
	 * @param documento
	 * @return
	 * @author Cìntia Nogueira
	 */
	public List<Transportador> findByDocumento(String documento){
		if(documento==null || documento.equals("")){
			throw new WmsException("O documento não pode ser nulo.");
		}
		
		return query()
				.select("transportador.cdpessoa, transportador.nome, transportador.documento")
				.where("transportador.documento=?", documento)
				.where("transportador.ativo = 1")
				.list();
	}
	
	/**
	 * 
	 * @param documento
	 * @return
	 */
	public Transportador findByDoc(String documento){
		if(documento==null || documento.equals("")){
			throw new WmsException("O documento não pode ser nulo.");
		}
		
		return query()
		.select("transportador.cdpessoa, transportador.nome, transportador.documento")
		.where("transportador.documento=?", documento)
		.unique();
	}
	
	/**
	 * 
	 * @param deposito
	 * @return
	 */
	public List<Transportador> findByDeposito(Deposito deposito){
		
		QueryBuilder<Transportador> query = query();
		
			query.select("transportador.cdpessoa, transportador.nome")
				.join("transportador.listaTransDeposito listaTransDeposito")
				.join("listaTransDeposito.deposito deposito")
				.where("deposito = ?",deposito)
				.where("transportador.ativo = 1")
				.where("transportador.pessoanatureza = ?",Pessoanatureza.JURIDICA)
				.orderBy("transportador.nome");
		
		return query.list();
		
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public List<Transportador> findForAutocompleteWithDepositoLogado (String param){
		
		QueryBuilder<Transportador> query = query();
			
			query.select("transportador.cdpessoa, transportador.nome, transportador.documento")
				.join("transportador.listaTransDeposito listaTransDeposito")
				.join("listaTransDeposito.deposito deposito")
				.openParentheses()
					.whereLikeIgnoreAll("transportador.cdpessoa",param)
					.or()
					.whereLikeIgnoreAll("transportador.nome",param)
					.or()
					.whereLikeIgnoreAll("transportador.cdpessoa", param)
				.closeParentheses()
			.where("deposito = ?",WmsUtil.getDeposito())
			.where("transportador.ativo = 1")
			.setMaxResults(10);
			
		return query.list();
	}
	
	
}
