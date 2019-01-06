package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLoja;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto;
import br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.vo.RecebimentoLojaVO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class RecebimentoRetiraLojaDAO extends GenericDAO<RecebimentoRetiraLoja> {

	public RecebimentoRetiraLoja findRecebimentoLojaWithCodigoEan(String codigoEan) {
		QueryBuilder<RecebimentoRetiraLojaProduto> subQuery =  new QueryBuilder<RecebimentoRetiraLojaProduto>(getHibernateTemplate());
		
		subQuery.select("recebimentoRetiraLoja.cdRecebimentoRetiraLoja")
				.from(RecebimentoRetiraLojaProduto.class)
				.join("recebimentoRetiraLojaProduto.recebimentoRetiraLoja recebimentoRetiraLoja")
				.join("recebimentoRetiraLojaProduto.produto produto")
				.join("produto.listaProdutoCodigoDeBarras produtoCodigoDeBarras")
				.join("recebimentoRetiraLoja.deposito deposito")
				.where("produtoCodigoDeBarras.codigo = ".concat(codigoEan))
				.where("recebimentoRetiraLojaStatus.cdRecebimentoRetiraLojaStatus = ".concat(RecebimentoRetiraLojaStatus.EM_RECEBIMENTO.getCdRecebimentoRetiraLojaStatus().toString()))
				.where("deposito.cddeposito = ".concat(WmsUtil.getDeposito().getCddeposito().toString()));
		
		
		QueryBuilder<RecebimentoRetiraLoja> qb = criaConsultaRecebimentoRetiraLoja();
		 	
		qb.where("recebimentoRetiraLoja.cdRecebimentoRetiraLoja in ?", subQuery);
		
		return qb.unique();
	}

	/**
	 * Cria consulta recebimento retira loja.
	 *
	 * @return the query builder
	 */
	private QueryBuilder<RecebimentoRetiraLoja> criaConsultaRecebimentoRetiraLoja() {
		QueryBuilder<RecebimentoRetiraLoja> qb = query();
		
		qb.select("recebimentoRetiraLoja.cdRecebimentoRetiraLoja, recebimentoRetiraLoja.dtRecebimento, " +
				  "usuario.cdpessoa, usuario.nome, usuario.login, deposito.cddeposito, manifesto.cdmanifesto, " +
				  "recebimentoRetiraLojaStatus.cdRecebimentoRetiraLojaStatus, recebimentoRetiraLojaStatus.nome, " +
				  "recebimentoRetiraLojaProduto.cdRecebimentoRetiraLojaProduto, recebimentoRetiraLojaProduto.qtde, recebimentoRetiraLoja2.cdRecebimentoRetiraLoja," +
				  "produto.cdproduto, produto.codigo, produto.descricao, notaFiscalSaida.cdnotafiscalsaida, notaFiscalSaida.numeropedido, "+
				  "produtoCodigoDeBarras.cdprodutocodigobarras,produtoCodigoDeBarras.codigo, tipoEstoque.cdTipoEstoque, tipoEstoque.descricao ")
			.join("recebimentoRetiraLoja.listaRecebimentoRetiraLojaProduto recebimentoRetiraLojaProduto")
			.join("recebimentoRetiraLoja.recebimentoRetiraLojaStatus recebimentoRetiraLojaStatus")
			.join("recebimentoRetiraLoja.manifesto manifesto")
			.join("recebimentoRetiraLoja.usuario usuario")
			.join("recebimentoRetiraLoja.deposito deposito")
		 	.join("recebimentoRetiraLojaProduto.produto produto")
		 	.join("recebimentoRetiraLojaProduto.recebimentoRetiraLoja recebimentoRetiraLoja2")
		 	.join("produto.listaProdutoCodigoDeBarras produtoCodigoDeBarras")
		 	.join("recebimentoRetiraLojaProduto.notaFiscalSaida notaFiscalSaida")
		 	.join("recebimentoRetiraLojaProduto.tipoEstoque tipoEstoque");
		
		return qb;
	}
	
	
	/**
	 * Find recebimento loja.
	 * @param codigoEan 
	 *
	 * @param cdRecebimentoRetiraLoja the cd recebimento retira loja
	 * @return the recebimento retira loja
	 */
	public RecebimentoRetiraLoja findRecebimentoLoja(Integer cdRecebimentoRetiraLoja) {
		
		QueryBuilder<RecebimentoRetiraLoja> qb = criaConsultaRecebimentoRetiraLoja();
		
		qb.where("recebimentoRetiraLoja.cdRecebimentoRetiraLoja = ?", cdRecebimentoRetiraLoja)
		.where("recebimentoRetiraLojaStatus = ?", RecebimentoRetiraLojaStatus.EM_RECEBIMENTO);
		
		return qb.unique();
	}

	/**
	 * Recupera dados recebimento retira loja.
	 *
	 * @param codigoEan the codigo ean
	 * @return the list
	 */
	public List<RecebimentoLojaVO> recuperaDadosRecebimentoRetiraLoja(String codigoEan) {
		Deposito deposito = WmsUtil.getDeposito();
		List<Object> args = new ArrayList<Object>();

		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append(" SELECT MAN.CDMANIFESTO,														   ");
			sql.append("       PRO.CDPRODUTO,															   ");
			sql.append("       PRO.CODIGO AS CODIGO_PRODUTO,											   ");
			sql.append("       PRO.DESCRICAO AS DESCRICAO_PRODUTO, 										   ");
			sql.append("       PCB.CDPRODUTOCODIGOBARRAS, 												   ");
			sql.append("       PCB.CODIGO AS CODIGO_BARRAS,												   ");
			sql.append("       NFS.CDNOTAFISCALSAIDA,													   ");
			sql.append("       NFS.NUMEROPEDIDO AS NUMERO_PEDIDO,										   ");
			sql.append("       NFP.QTDE                                                                    ");
			sql.append("  FROM PRODUTOCODIGOBARRAS PCB                                                     ");
			sql.append(" INNER JOIN PRODUTO PRO                                                            ");
			sql.append("    ON PCB.CDPRODUTO = PRO.CDPRODUTO                                               ");
			sql.append(" INNER JOIN NOTAFISCALSAIDAPRODUTO NFP                                             ");
			sql.append("    ON NFP.CDPRODUTO = PRO.CDPRODUTO                                               ");
			sql.append(" INNER JOIN NOTAFISCALSAIDA NFS                                                    ");
			sql.append("    ON NFS.CDNOTAFISCALSAIDA = NFP.CDNOTAFISCALSAIDA                               ");
			sql.append(" INNER JOIN MANIFESTONOTAFISCAL MNF                                                ");
			sql.append("    ON MNF.CDNOTAFISCALSAIDA = NFS.CDNOTAFISCALSAIDA                               ");
			sql.append(" INNER JOIN MANIFESTO MAN                                                          ");
			sql.append("    ON MNF.CDMANIFESTO = MAN.CDMANIFESTO                                           ");
			sql.append(" WHERE PRO.CDTIPOVENDA = NFS.CDTIPOVENDA                                           ");
			sql.append("   AND NFS.CDTIPOVENDA = 2                                                         ");
			sql.append("    AND NFS.CDTIPONF = 4                                                           ");
			sql.append("    AND MAN.CDTIPOENTREGA <> 4                                                     ");
			sql.append("   AND MNF.CDDEPOSITOTRANSBORDO IS NULL                                            ");
			sql.append("   AND NFS.NRO_LOJA_RETIRADA = ?                                                   ");
			sql.append("    AND MNF.CDMANIFESTO IN(SELECT MNX.CDMANIFESTO                                  ");
			sql.append("                             FROM MANIFESTONOTAFISCAL MNX,                         ");
			sql.append("                                  MANIFESTO MX ,                                   ");
			sql.append("                                  NOTAFISCALSAIDA NFX,                             ");
			sql.append("                                  NOTAFISCALSAIDAPRODUTO NFPX,                     ");
			sql.append("                                  PRODUTOCODIGOBARRAS PBX                          ");
			sql.append("                            WHERE MNX.CDMANIFESTO = MX.CDMANIFESTO                 ");
			sql.append("                            AND   MNX.CDNOTAFISCALSAIDA = NFX.CDNOTAFISCALSAIDA    ");
			sql.append("                            AND   NFX.CDNOTAFISCALSAIDA = NFPX.CDNOTAFISCALSAIDA   ");
			sql.append("                            AND   NFPX.CDPRODUTO = PBX.CDPRODUTO                   ");
			sql.append("                            AND   NFX.NRO_LOJA_RETIRADA = NFS.NRO_LOJA_RETIRADA    ");
			sql.append("                            AND   MX.CDMANIFESTOSTATUS NOT IN (1,2,3,11)           ");
			sql.append("							AND   NOT EXISTS (SELECT RLP.CDNOTAFISCALSAIDA 		   ");
			sql.append("        										FROM RECEBRETIRALOJAPRODUTO RLP    ");
			sql.append(" 												JOIN RECEBIMENTORETIRALOJA RRL     ");
			sql.append("											      ON RLP.CDRECEBIMENTORETIRALOJA = RRL.CDRECEBIMENTORETIRALOJA ");
			sql.append(" 											   WHERE RLP.CDNOTAFISCALSAIDA = NFX.CDNOTAFISCALSAIDA)		");	
			sql.append("                            AND   PBX.CODIGO = ? )                        		   ");
			
			
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
		}
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaRecebimentoRetiraLojaProduto");
	}

}
