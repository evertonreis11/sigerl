package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipovenda;
import br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO;
import br.com.linkcom.wms.geral.bean.vo.ManifestoTransbordoVO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
import br.com.ricardoeletro.sigerl.expedicao.crud.filtro.ManifestoFiltro;
import br.com.ricardoeletro.sigerl.expedicao.process.filtro.GestaoPedidoFiltro;

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

	public List<GestaoPedidoVO> findForGestaoPedido(GestaoPedidoFiltro filtro) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
		Deposito deposito = WmsUtil.getDeposito();
		List<Object> args = new ArrayList<Object>();

		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append(" SELECT DISTINCT NF.NUMERO NRO_NF,                                                           ");
			sql.append("        NF.NUMEROPEDIDO PEDIDO,                                                              ");
			sql.append("        NF.DTEMISSAO DT_EMISSAO_NF,                                                          ");
			sql.append("        RL.DTRECEBIMENTO AS DT_RECEBIMENTO_LOJA,                                             ");
			sql.append("        CL.NOME AS CLIENTE,                                                                  ");
			sql.append("        CASE                                                                                 ");
			sql.append("          WHEN ES.CDEXPEDICAORETLOJASTATUS IS NOT NULL                                        ");
			sql.append("             THEN ES.NOME                                                                    ");
			sql.append("          WHEN RS.CDRECEBRETIRALOJASTATUS IS NOT NULL AND ES.CDEXPEDICAORETLOJASTATUS IS NULL ");
			sql.append("             THEN RS.NOME                                                                    ");
			sql.append("          ELSE                                                                               ");
			sql.append("           'EM TRANSFERENCIA PARA A LOJA'                                                    ");
			sql.append("        END AS SITUACAO                                                                      ");
			sql.append("   FROM NOTAFISCALSAIDA NF                                                                   ");
			sql.append("   JOIN PESSOA CL                                                                            ");
			sql.append("     ON CL.CDPESSOA = NF.CDCLIENTE                                                           ");
			sql.append("   JOIN NOTAFISCALSAIDAPRODUTO NP                                                            ");
			sql.append("     ON NP.CDNOTAFISCALSAIDA = NF.CDNOTAFISCALSAIDA                                          ");
			sql.append("   JOIN PRODUTO PR                                                                           ");
			sql.append("     ON PR.CDPRODUTO = NP.CDPRODUTO                                                          ");
			sql.append("   LEFT JOIN RECEBRETIRALOJAPRODUTO RP                                                       ");
			sql.append("     ON RP.CDNOTAFISCALSAIDA = NF.CDNOTAFISCALSAIDA                                          ");
			sql.append("   LEFT JOIN RECEBIMENTORETIRALOJA RL                                                        ");
			sql.append("     ON RL.CDRECEBIMENTORETIRALOJA = RP.CDRECEBIMENTORETIRALOJA                              ");
			sql.append("   LEFT JOIN RECEBRETIRALOJASTATUS RS                                                        ");
			sql.append("     ON RS.CDRECEBRETIRALOJASTATUS = RL.CDRECEBRETIRALOJASTATUS                              ");
			sql.append("   LEFT JOIN EXPEDICAORETIRALOJA EL                                                          ");
			sql.append("     ON EL.CDNOTAFISCALSAIDA = NF.CDNOTAFISCALSAIDA                                          ");
			sql.append("   LEFT JOIN EXPEDICAORETLOJASTATUS ES                                                       ");
			sql.append("     ON ES.CDEXPEDICAORETLOJASTATUS = EL.CDEXPEDICAORETLOJASTATUS                            ");
			sql.append("  WHERE NF.CDTIPONF = 4																	     ");
			sql.append("    AND NF.CDTIPOVENDA = 2																	 ");
			sql.append("    AND NF.NRO_LOJA_RETIRADA = ? 															 ");
			
			args.add(deposito.getCodigoerp());		
			
			if (StringUtils.isNotBlank(filtro.getNumeroPedido())){
				sql.append("    AND NF.NUMEROPEDIDO = ? ");
				args.add(filtro.getNumeroPedido());
			}
			
			if (StringUtils.isNotBlank(filtro.getNumeroNota())){
				sql.append("    AND NF.NUMERO = ? ");
				args.add(filtro.getNumeroNota());
			}
			
			if (filtro.getDtChegadaInicial() != null){
				sql.append("    AND TRUNC(NF.DTEMISSAO) >= TO_DATE(?, 'DD/MM/YYYY')");
				args.add(sdf.format(filtro.getDtChegadaInicial()));
			}
			
			if (filtro.getDtChegadaFinal() != null){
				sql.append("    AND TRUNC(NF.DTEMISSAO) < TO_DATE(?, 'DD/MM/YYYY') + 1 ");
				args.add(sdf.format(filtro.getDtChegadaFinal()));
			}
			
			if (StringUtils.isNotBlank(filtro.getCodigoProduto())){
				sql.append("    AND PR.CODIGO = ? ");
				args.add(filtro.getCodigoProduto().toUpperCase());
			}
			
			if (StringUtils.isNotBlank(filtro.getNomeCliente())){
				sql.append("    AND CL.NOME LIKE ? ");
				args.add("%".concat(filtro.getNomeCliente().toUpperCase()).concat("%"));
			}
			
			
			@SuppressWarnings("unchecked")
			List<GestaoPedidoVO> dados = (List<GestaoPedidoVO>) getJdbcTemplate().query(sql.toString(), args.toArray(), new ResultSetExtractor() {
				
				@Override
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

					List<GestaoPedidoVO> registros = new ArrayList<GestaoPedidoVO>();

					while (rs.next()) {
						GestaoPedidoVO vo = new GestaoPedidoVO();
						
						vo.setNumeroNota(rs.getString("NRO_NF"));
						vo.setNumeroPedido(rs.getString("PEDIDO"));
						vo.setDataPedido(rs.getTimestamp("DT_EMISSAO_NF"));
						vo.setDataChegada(rs.getTimestamp("DT_RECEBIMENTO_LOJA"));
						vo.setCliente(rs.getString("CLIENTE"));
						vo.setSituacao(rs.getString("SITUACAO"));

						registros.add(vo);
					}

					return registros;
				}
			});


			return dados;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WmsException("Erro ao recuperar dados para a gestão de pedidos. Erro: " + e.getMessage(), e.getCause());
		}
	}
	
	

	/**
	 * Recupera nota saida do site por numero.
	 *
	 * @param numeroNota the numero nota
	 * @return the notafiscalsaida
	 */
	public Notafiscalsaida recuperaNotaSaidaPorNumero(final String numeroNota) {
	   
		Integer cdNotaFiscalSaida =  (Integer) getHibernateTemplate().execute(new HibernateCallback(){
			 
			public Object doInHibernate(Session session) throws HibernateException, SQLException    {
				StringBuilder hql = new StringBuilder();
				
				hql.append("select max(notafiscalsaida.cdnotafiscalsaida)	   				   ");
				hql.append("  from Notafiscalsaida notafiscalsaida             				   ");
				hql.append("  inner join notafiscalsaida.tipovenda tipovenda   				   ");
				hql.append(" where notafiscalsaida.numero = :numeronota                    		");
				hql.append("   and notafiscalsaida.numeroLojaRetirada = :numeroLojaRetirada    ");
				hql.append("   and notafiscalsaida.tipovenda.cdtipovenda = :cdtipovenda        ");

				Query query = session.createQuery(hql.toString());
				query.setParameter("numeronota", numeroNota);
				query.setParameter("numeroLojaRetirada", WmsUtil.getDeposito().getCodigoerp());
				query.setParameter("cdtipovenda", Tipovenda.SITE.getCdtipovenda());
				
				return query.uniqueResult();	
			}
			
		});
		
		Notafiscalsaida notaFiscalSaida = new Notafiscalsaida();
		
		notaFiscalSaida.setCdnotafiscalsaida(cdNotaFiscalSaida);
		
		return notaFiscalSaida;  
	}
	
	/**
	 * 
	 * @param filtro
	 * @param isMultiCDByCodigoERP 
	 * @return
	 */
	public List<Notafiscalsaida> findForListagemPopUp(ManifestoFiltro filtro, Boolean isMultiCDByCodigoERP) {
		
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
				.join("notafiscalsaida.cliente cliente");
				if(!isMultiCDByCodigoERP)
					query.where("deposito = ?",WmsUtil.getDeposito());
			query.where("notafiscalsaida.vinculado = 0")		
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
					query.where("notafiscaltipo.cdnotafiscaltipo in (3,5,6)");
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
	 * @param listaNotafiscalsaida
	 * @param deposito 
	 */
	public void atualizarDepositoNota(List<Notafiscalsaida> listaNotafiscalsaida, Deposito deposito) {

		if(listaNotafiscalsaida==null || listaNotafiscalsaida.isEmpty()){
			throw new WmsException("Parametros Invalidos. Nenhuma nota está dispoinvel para essa operação.");
		}else if(deposito == null || deposito.getCddeposito() == null){
			throw new WmsException("Parametros Invalidos. Nenhum depositio foi selecionado para essa operação.");
		}
		
		StringBuilder sql = new StringBuilder();
			
			sql.append(" update Notafiscalsaida nfs set nfs.deposito.id = ").append(deposito.getCddeposito());
			sql.append(" where nfs.id in ( ");
			sql.append(CollectionsUtil.listAndConcatenate(listaNotafiscalsaida, "cdnotafiscalsaida", ","));
			sql.append(" ) "); 
			
		getHibernateTemplate().bulkUpdate(sql.toString());
		
	}
	
	
	/**
	 * Find by importacao carga.
	 *
	 * @param cdsImportacaoCarga the cds importacao carga
	 * @return the list
	 */
	public List<Notafiscalsaida> findByImportacaoCarga(String cdsImportacaoCarga) {
		
		QueryBuilder<Notafiscalsaida> query = query();
		
		createQueryFindNotaFiscal(query);
		
		query.whereIn("importacaocarga.cdimportacaocarga",cdsImportacaoCarga!=null?cdsImportacaoCarga.trim() : null);
		
		return query.list();
	}
	
	/**
	 * Creates the query find nota fiscal.
	 *
	 * @param query the query
	 * @return the query builder
	 */
	public QueryBuilder<Notafiscalsaida> createQueryFindNotaFiscal(QueryBuilder<Notafiscalsaida> query) {
		
		return query.select("notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.chavenfe, " +
					"notafiscalsaida.dtemissao, notafiscalsaida.vlrtotalnf, notafiscalsaida.qtdeitens, notafiscalsaida.valorfretecliente, " + 
					"notafiscalsaida.notaautorizada, notafiscalsaida.numeropedido, notafiscalsaida.lojapedido, deposito.cddeposito, deposito.nome, " +
					"cliente.cdpessoa, cliente.nome, pedidovenda.cdpedidovenda, notafiscalsaida.temtroca, " +
					"pedidovendaproduto.cdpedidovendaproduto, carregamentoitem.cdcarregamentoitem, carregamento.cdcarregamento," +
					"carregamentostatus.cdcarregamentostatus, rota.cdrota, rota.nome, rota.temDepositoTransbordo, depositotransbordo.cddeposito," +
					"depositotransbordo.nome, notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.nome, praca.cdpraca, praca.nome, tipovenda.cdtipovenda," + 
					"importacaocarga.cdimportacaocarga, importacaocarga.cdcarga ")
				 .join("notafiscalsaida.deposito deposito")
				 .join("notafiscalsaida.tipovenda tipovenda")
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
				 .leftOuterJoin("notafiscalsaida.importacaocarga importacaocarga")
				 .where("deposito = ?",WmsUtil.getDeposito())
				 .where("notafiscalsaida.vinculado = 0");
	}
	
	
	/*
	 * Busca uma notafiscalsaida de devolucao
	 * 
	 * @param notafiscalsaida
	 * 
	 * @return @notafiscalsaida
	 */
	public Notafiscalsaida findNotaDevolucao(Notafiscalsaida notafiscalsaida) {
		
		QueryBuilder<Notafiscalsaida> query = query();
		
		StringBuilder fields = new StringBuilder();
		fields.append(" notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.chavenfe, ");
		fields.append(" notafiscalsaida.dtemissao ,notafiscalsaida.vlrtotalnf ,notafiscalsaida.qtdeitens, deposito.cddeposito, ");
		fields.append(" notafiscalsaida.numeropedido, notafiscalsaida.lojapedido, notafiscalsaida.temtroca, deposito.nome, ");
		fields.append(" cliente.cdpessoa, cliente.nome, ");
		fields.append(" rota.cdrota, rota.nome, rota.temDepositoTransbordo, ");
		fields.append(" depositotransbordo.cddeposito, depositotransbordo.nome, ");
		fields.append(" notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.nome ");
		
		query.select(fields.toString())
		.join("notafiscalsaida.deposito deposito")
		.join("notafiscalsaida.cliente cliente")
		.leftOuterJoin("notafiscalsaida.praca praca")
		.leftOuterJoin("praca.listaRotapraca rotapraca")
		.leftOuterJoin("rotapraca.rota rota")
		.leftOuterJoin("rota.depositotransbordo depositotransbordo")
		.leftOuterJoin("notafiscalsaida.notafiscaltipo notafiscaltipo")
		.where("notafiscalsaida.numeropedido = ? ", notafiscalsaida.getNumeropedido())
		.where("notafiscalsaida.lojapedido = ?", notafiscalsaida.getLojapedido())
		.where("notafiscaltipo.cdnotafiscaltipo =3")
		.where("notafiscalsaida.vinculado =0");
		
		if(notafiscalsaida.getListNotafiscalsaidaproduto() != null && notafiscalsaida.getListNotafiscalsaidaproduto().get(0).getPedidovendaproduto() != null){
			List<Carregamentoitem> item =new ArrayList<Carregamentoitem>(notafiscalsaida.getListNotafiscalsaidaproduto().get(0).getPedidovendaproduto().getListaCarregamentoitem());
			query.where("notafiscalsaida.carregamento != ?", item.get(0).getCarregamento());
		}
				
		
		return query.unique();
	}

	/*
	 * Como saber se o pedido é de troca
	 * 
	 * @param cdnotafiscalsaida
	 * 
	 * @return boolean
	 */
	public boolean isPedidoTroca(Integer cdnotafiscalsaida) {
		return query()
				.join("notafiscalsaida.listNotafiscalsaidaproduto notafiscalsaidaproduto")
				.join("notafiscalsaidaproduto.pedidovendaproduto pedidovendaproduto")
				.join("pedidovendaproduto.pedidovenda pedidovenda")
				.where("notafiscalsaida.cdnotafiscalsaida = ?", cdnotafiscalsaida)
				.where("pedidovenda.troca=1")
				.setMaxResults(1)
				.unique() != null;
	}
	
	public void autorizarNotasSemFreteCliente(String whereIn) {
		StringBuilder sql = new StringBuilder();

		sql.append(" update Notafiscalsaida nfs set nfs.notaautorizada = 1");
		sql.append(" where nfs.id in ( ").append(whereIn).append(" ) "); 

		getHibernateTemplate().bulkUpdate(sql.toString());

	}
	
	@SuppressWarnings("unchecked")
	public List<ManifestoTransbordoVO> recuperaNotasTransbordoPopUp(final Integer cdmanifesto) {
		return getHibernateTemplate().executeFind(new HibernateCallback(){
			 
			public Object doInHibernate(Session session) throws HibernateException, SQLException    {
				StringBuilder hql = new StringBuilder();
				
				hql.append(" select new br.com.linkcom.wms.geral.bean.vo.ManifestoTransbordoVO (manifestonotafiscal.cdmanifestonotafiscal, ");
				hql.append(" 	   notafiscalsaida.numero,                                                            ");
				hql.append(" 	   notafiscalsaida.serie,                                                             ");
				hql.append(" 	   notafiscalsaida.dtemissao,                                                         ");
				hql.append(" 	   notafiscalsaida.numeropedido,                                                      ");
				hql.append(" 	   notafiscalsaida.lojapedido,                                                        ");
				hql.append(" 	   rota.nome,                                                                         ");
				hql.append(" 	   rotaconsolidacao.nome,                                                             ");
				hql.append(" 	   manifestonotafiscal.temDepositoTransbordo,                                         ");
				hql.append(" 	   rota.temDepositoTransbordo,                                                        ");
				hql.append(" 	   rotaconsolidacao.temDepositoTransbordo,                                            ");
				hql.append(" 	   depositotransbordomanifesto.cddeposito,                                            ");
				hql.append(" 	   depositotransbordorota.cddeposito,												  ");
				hql.append(" 	   depositotransbordorotaconsolidacao.cddeposito)									  ");
				hql.append("   from Manifesto manifesto                                                               ");
				hql.append("   inner join manifesto.listaManifestonotafiscal manifestonotafiscal                      ");
				hql.append("   inner join manifestonotafiscal.notafiscalsaida notafiscalsaida                         ");
				hql.append("   inner join manifesto.tipoentrega tipoentrega                                           ");
				hql.append("   inner join notafiscalsaida.tipovenda tipovenda                                         ");
				hql.append("   left outer Join manifestonotafiscal.depositotransbordo depositotransbordomanifesto     ");
				hql.append("   left outer Join notafiscalsaida.praca praca                                            ");
				hql.append("   left outer Join praca.listaRotapraca rotapraca                                         ");
				hql.append("   left outer Join rotapraca.rota rota                                                    ");
				hql.append("   left outer Join rota.depositotransbordo depositotransbordorota                         ");
				hql.append("   left outer Join notafiscalsaida.pracaconsolidacao pracaconsolidacao                    ");
				hql.append("   left outer Join pracaconsolidacao.listaRotapraca rotapracaconsolidacao                 ");
				hql.append("   left outer Join rotapracaconsolidacao.rota rotaconsolidacao                            ");
				hql.append("   left outer Join rotaconsolidacao.depositotransbordo depositotransbordorotaconsolidacao ");
				hql.append("  where manifesto.cdmanifesto = :manifesto                                                ");
				hql.append("    and (                                                                                 ");
				hql.append(" 		  (tipovenda = :tipovendaLoja                                                     ");
				hql.append(" 		  	and rota.temDepositoTransbordo = :temDepositoTransbordo                       ");
				hql.append(" 		  	and depositotransbordorota is not null                                        ");
				hql.append(" 		  )                                                                            	  ");
				hql.append(" 		  or                                                                              ");
				hql.append(" 		  (tipovenda = :tipovendaSite                                                     ");
				hql.append(" 		  	and rotaconsolidacao.temDepositoTransbordo = :temDepositoTransbordo           ");
				hql.append(" 		  	and depositotransbordorotaconsolidacao is not null                            ");
				hql.append(" 		  )                                                                            	  ");
				hql.append(" 		  or                                                                              ");
				hql.append(" 		  (depositotransbordomanifesto is not null  									  ");
				hql.append(" 		    and manifestonotafiscal.temDepositoTransbordo = :temDepositoTransbordo 		  ");
				hql.append(" 		   ) 																			  ");
				hql.append(" 	    )                                                                                 ");
				hql.append("    and tipoentrega in (:tipos)		                                                      ");

				Query query = session.createQuery(hql.toString());
				query.setParameter("manifesto", cdmanifesto);
				query.setParameter("temDepositoTransbordo", Boolean.TRUE);
				query.setParameter("tipovendaLoja", Tipovenda.LOJA_FISICA);
				query.setParameter("tipovendaSite", Tipovenda.SITE);
				query.setParameterList("tipos", new ArrayList<Tipoentrega>(Arrays.asList(Tipoentrega.CONSOLIDACAO, Tipoentrega.TRANSFERENCIA)));
				
				return query.list();	
			}
			
		});
	}
	
}
