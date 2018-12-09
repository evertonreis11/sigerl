package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.ManifestoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class NotafiscalsaidaDAO extends GenericDAO<Notafiscalsaida>{

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Notafiscalsaida> findForListagemPopUp(ManifestoFiltro filtro) {
		
		QueryBuilder<Notafiscalsaida> query = query();
			
			query.select("notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.chavenfe, " +
					"notafiscalsaida.dtemissao ,notafiscalsaida.vlrtotalnf ,notafiscalsaida.qtdeitens, deposito.cddeposito, deposito.nome, " +
					"cliente.cdpessoa, cliente.nome, notafiscalsaida.numeropedido, notafiscalsaida.lojapedido ");
					 if(filtro.getCdcarregamento()!=null){
						 query.select(query.getSelect().getValue()+",carregamento.cdcarregamento, carregamentostatus.cdcarregamentostatus ")
						 		.join("notafiscalsaida.carregamento carregamento")	
						 		.join("carregamento.carregamentostatus carregamentostatus");
					 }
			query.join("notafiscalsaida.deposito deposito")
				.join("notafiscalsaida.notafiscaltipo notafiscaltipo")
				.join("notafiscalsaida.cliente cliente")
			    .where("deposito = ?",WmsUtil.getDeposito())
				.where("notafiscalsaida.vinculado = 0")		
				.where("notafiscalsaida.filialfaturamento = ?",filtro.getFilial())
				.openParentheses();
					if(filtro.getCdcarregamento()!=null){
						query.where("carregamentostatus.cdcarregamentostatus = 6 and carregamento.cdcarregamento = ?",filtro.getCdcarregamento());				 
						query.or();
					}
					query.where("notafiscalsaida.chavenfe = ?",filtro.getChavenfe())
						.where("notafiscalsaida.numero = ?",filtro.getNroNotaSaida())
						.where("notafiscalsaida.serie = ?",filtro.getSerieNota()!=null ? filtro.getSerieNota().toUpperCase() : null)
					 	.where("trunc(notafiscalsaida.dtemissao) = ?", filtro.getDtemissaoNotaSaida())				 
			 	.closeParentheses();
			if(filtro!=null && filtro.getTipoentrega()!=null){
				if(filtro.getTipoentrega().getCdtipoentrega() == 1){
					query.where("notafiscaltipo.cdnotafiscaltipo in (4,5)");
				}else if(filtro.getTipoentrega().getCdtipoentrega() == 2){
					query.where("notafiscaltipo.cdnotafiscaltipo in (3,6)");
				}
			}
			//queiroz - 16/07/15 incluindo filtro de carga do erp
			 if((filtro.getCdcargaerp()!=null)&&(!(filtro.getCdcargaerp().equalsIgnoreCase("")))){
				 query.where("notafiscalsaida.cdcargaerp = ?",filtro.getCdcargaerp());
			 }
			
		return query.list();
		
	}
	
	/**
	 * 
	 * @param selectCdnotafiscalsaida
	 * @return
	 */
	public List<Notafiscalsaida> findByWhereIn(String selectCdnotafiscalsaida) {
		
		QueryBuilder<Notafiscalsaida> query = query();
		
		query.select("notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.chavenfe, " +
					"notafiscalsaida.dtemissao ,notafiscalsaida.vlrtotalnf ,notafiscalsaida.qtdeitens, deposito.cddeposito, deposito.nome, " +
					"cliente.cdpessoa, cliente.nome, pedidovenda.cdpedidovenda, notafiscalsaida.numeropedido, notafiscalsaida.lojapedido, " +
					"pedidovendaproduto.cdpedidovendaproduto, carregamentoitem.cdcarregamentoitem, carregamento.cdcarregamento," +
					"carregamentostatus.cdcarregamentostatus, rota.cdrota, rota.nome, rota.temDepositoTransbordo, depositotransbordo.cddeposito," +
					"depositotransbordo.nome, notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.nome")
				 .join("notafiscalsaida.deposito deposito")
				 .join("notafiscalsaida.cliente cliente")
				 .join("notafiscalsaida.notafiscaltipo notafiscaltipo")
				 .leftOuterJoin("notafiscalsaida.listNotafiscalsaidaproduto notafiscalsaidaproduto")
				 .leftOuterJoin("notafiscalsaidaproduto.pedidovenda pedidovenda")
				 .leftOuterJoin("notafiscalsaidaproduto.pedidovendaproduto pedidovendaproduto")
				 .leftOuterJoin("pedidovendaproduto.listaCarregamentoitem carregamentoitem")
				 .leftOuterJoin("carregamentoitem.carregamento carregamento")
				 .leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
				 .leftOuterJoin("notafiscalsaida.praca praca")
				 .leftOuterJoin("praca.listaRotapraca rotapraca")
				 .leftOuterJoin("rotapraca.rota rota")
				 .leftOuterJoin("rota.depositotransbordo depositotransbordo")
				 .where("deposito = ?",WmsUtil.getDeposito())
				 .where("notafiscalsaida.vinculado = 0")
				 .whereIn("notafiscalsaida.cdnotafiscalsaida",selectCdnotafiscalsaida!=null?selectCdnotafiscalsaida.trim() : null);
		
		return query.list();
	}

	/***
	 * 
	 * @param listAndConcatenate
	 */
	public void desvincularNotas(String whereIn) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update notafiscalsaida nfs set nfs.vinculado = 0");
			sql.append(" where nfs.cdnotafiscalsaida in ( ").append(whereIn).append(" ) "); 

		getJdbcTemplate().execute(sql.toString());
	}

	/**
	 * 
	 * @param cdnotafiscalsaida
	 * @param cdpraca
	 */
	public void vincularPraca(Integer cdnotafiscalsaida, Integer cdpraca) {
		
		if(cdnotafiscalsaida==null || cdpraca==null){
			throw new WmsException("Parametros Invalidos.");
		}
		
		StringBuilder sql = new StringBuilder();
			
			sql.append(" update notafiscalsaida nfs set nfs.cdpraca = ").append(cdpraca);
			sql.append(" where nfs.cdnotafiscalsaida = ").append(cdnotafiscalsaida); 
			
		getJdbcTemplate().execute(sql.toString());
		
	}

	/**
	 * 
	 * @param listaNotasDevolucao
	 */
	public List<Notafiscalsaida> rehabilitarNotasDevolucao(List<Notafiscalsaida> listaNotasDevolucao){
		
		String whereIn = null;
		
		if(listaNotasDevolucao!=null && !listaNotasDevolucao.isEmpty()){
			whereIn = CollectionsUtil.listAndConcatenate(listaNotasDevolucao, "cdnotafiscalsaida", ",");
		}
		
		QueryBuilder<Notafiscalsaida> query = query();
		
			query.select(" notafiscalsaida.cdnotafiscalsaida, deposito.cddeposito, cliente.cdpessoa, notafiscalsaida.codigoerp, " +
						 " notafiscalsaida.numero, notafiscalsaida.chavenfe, notafiscalsaida.ativo, notafiscalsaida.dtemissao, " +
						 " notafiscalsaida.dt_inclusao, notafiscalsaida.dt_alteracao, notafiscalsaida.vlrtotalnf, notafiscalsaida.qtdeitens, " +
						 " notafiscalsaida.serie, pedidovenda.cdpedidovenda, notafiscalsaida.vinculado, depositoDestino.cddeposito, " +
						 " notafiscalsaida.numeropedido, notafiscalsaida.lojapedido, notafiscalsaida.cdcargaerp, carregamento.cdcarregamento," +
						 " filialfaturamento.cdpessoa, pessoaendereco.cdpessoaendereco, pessoaendereco.cep, notafiscaltipo.cdnotafiscaltipo," +
						 " praca.cdpraca ")
				.join("notafiscalsaida.deposito deposito")
				.join("notafiscalsaida.cliente cliente")
				.join("notafiscalsaida.listNotafiscalsaidaproduto notafiscalsaidaproduto")
				.join("notafiscalsaida.notafiscaltipo notafiscaltipo")
				.join("notafiscalsaida.praca praca")
				.join("notafiscalsaida.pessoaendereco pessoaendereco")
				.leftOuterJoin("notafiscalsaida.depositoDestino depositoDestino")
				.leftOuterJoin("notafiscalsaida.carregamento carregamento")
				.leftOuterJoin("notafiscalsaida.filialfaturamento filialfaturamento")
				.leftOuterJoin("notafiscalsaida.pedidovenda pedidovenda")
				.whereIn("notafiscalsaida.cdnotafiscalsaida", whereIn);
		
		return query.list();
		
	}

	/**
	 * 
	 * @param listaNotasRemanifestada
	 */
	public void habilitarRemanifestacao(List<Notafiscalsaida> listaNotasRemanifestada) {
		
		if(listaNotasRemanifestada!=null && !listaNotasRemanifestada.isEmpty() ){
		
			StringBuilder sql = new StringBuilder();
			
				sql.append(" update notafiscalsaida nfs set nfs.vinculado = 0 ");
				sql.append(" where nfs.cdnotafiscalsaida = ");
				sql.append(CollectionsUtil.listAndConcatenate(listaNotasRemanifestada, "cdnotafiscalsaida", ",")); 
			
			getJdbcTemplate().execute(sql.toString());
		
		}else{
			throw new WmsException("Não foi possivel rehabilitar as notas desse manifesto. Por favor, tente novamente.");
		}
	}
	
}
