package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO;
import br.com.linkcom.wms.geral.bean.vo.RecebimentoLojaVO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.ManifestoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
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
		/*Deposito deposito = WmsUtil.getDeposito();
		List<Object> args = new ArrayList<Object>();

		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append(" SELECT DISTINCT NF.NUMERO NRO_NF,                                                           ");
			sql.append("        NF.NUMEROPEDIDO PEDIDO,                                                              ");
			sql.append("        NF.DTEMISSAO DT_EMISSAO_NF,                                                          ");
			sql.append("        RL.DTRECEBIMENTO AS DT_RECEBIMENTO_LOJA,                                             ");
			sql.append("        CL.NOME AS CLIENTE,                                                                  ");
			sql.append("        CASE                                                                                 ");
			sql.append("          WHEN ES.CDRECEBRETIRALOJASTATUS IS NOT NULL                                        ");
			sql.append("             THEN ES.NOME                                                                    ");
			sql.append("          WHEN RS.CDRECEBRETIRALOJASTATUS IS NOT NULL AND ES.CDRECEBRETIRALOJASTATUS IS NULL ");
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
			sql.append("   LEFT JOIN RECEBRETIRALOJASTATUS ES                                                        ");
			sql.append("     ON ES.CDRECEBRETIRALOJASTATUS = EL.CDEXPEDICAORETLOJASTATUS                             ");
			sql.append("  WHERE NF.NRO_LOJA_RETIRADA = 579                                                           ");
			sql.append("    AND NF.NUMEROPEDIDO = ?                                                                  ");
			sql.append("    AND NF.NUMERO = ?                                                                        ");
			sql.append("    AND TRUNC(NF.DTEMISSAO) >= TO_DATE(?, 'DD/MM/YYYY')                                      ");
			sql.append("    AND TRUNC(NF.DTEMISSAO) < TO_DATE(?, 'DD/MM/YYYY') + 1                                   ");
			sql.append("    AND PR.CODIGO = ?                                                                        ");
			sql.append("    AND CL.NOME LIKE '%%'                                                                    ");
			
			
			args.add(deposito.getCodigoerp());		
			args.add(codigoEan);		
					
					
			@SuppressWarnings("unchecked")
			List<RecebimentoLojaVO> dados = (List<RecebimentoLojaVO>) getJdbcTemplate().query(sql.toString(), args.toArray(), new ResultSetExtractor() {
				
				@Override
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

					List<RecebimentoLojaVO> registros = new ArrayList<RecebimentoLojaVO>();

					while (rs.next()) {
						RecebimentoLojaVO vo = new RecebimentoLojaVO();
						
						vo.setCdManifesto(rs.getInt("CDMANIFESTO"));
						vo.setCdProduto(rs.getInt("CDPRODUTO"));
						vo.setCodigoProduto(rs.getString("CODIGO_PRODUTO"));
						vo.setDescricaoProduto(rs.getString("DESCRICAO_PRODUTO"));
						vo.setCdProdutoCodigoBarras(rs.getInt("CDPRODUTOCODIGOBARRAS"));
						vo.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
						vo.setCdNotaFiscalSaida(rs.getInt("CDNOTAFISCALSAIDA"));
						vo.setNumeroPedido(rs.getString("NUMERO_PEDIDO"));
						vo.setQtde(rs.getInt("QTDE"));

						registros.add(vo);
					}

					return registros;
				}
			});


			return dados;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WmsException("Erro ao recuperar produtos para recebimento. Erro: " + e.getMessage(), e.getCause());
		}*/
		
		return null;
	}
	
}
