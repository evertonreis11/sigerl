package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO;
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
	
}
