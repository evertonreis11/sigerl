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
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLoja;
import br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaStatus;
import br.com.linkcom.wms.geral.bean.vo.ExpedicaoLojaVO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ExpedicaoRetiraLojaDAO extends GenericDAO<ExpedicaoRetiraLoja> {

	/**
	 * Recupera expedicao retira loja por chave nota.
	 *
	 * @param chaveNota the chave nota
	 * @return the expedicao retira loja
	 */
	public ExpedicaoRetiraLoja recuperaExpedicaoRetiraLojaPorChaveNota(String chaveNota, ExpedicaoRetiraLojaStatus status) {
		QueryBuilder<ExpedicaoRetiraLoja> qb = query();
		
		qb.select("expedicaoRetiraLoja.cdExpedicaoRetiraLoja, expedicaoRetiraLoja.termoImpresso, notaFiscalSaida.cdnotafiscalsaida, " + 
				" notaFiscalSaida.numero, notaFiscalSaida.serie, notaFiscalSaida.chavenfe, notaFiscalSaida.dtemissao, cliente.cdpessoa, " + 
				" cliente.nome, cliente.documento, expedicaoRetiraLojaProduto.cdExpedicaoRetiraLojaProduto, produto.cdproduto, produto.codigo, " +
				" produto.descricao, conferenciaExpedicaoRetiraLojaStatus.cdConfExpedicaoRetLojaStatus, conferenciaExpedicaoRetiraLojaStatus.nome ");
		
		qb.join("expedicaoRetiraLoja.listaExpedicaoRetiraLojaProduto expedicaoRetiraLojaProduto")
			.join("expedicaoRetiraLoja.notaFiscalSaida notaFiscalSaida")
			.join("expedicaoRetiraLoja.expedicaoRetiraLojaStatus expedicaoRetiraLojaStatus")
			.join("expedicaoRetiraLoja.deposito deposito")
			.join("notaFiscalSaida.cliente cliente")
			.join("expedicaoRetiraLojaProduto.produto produto")
			.join("expedicaoRetiraLojaProduto.conferenciaExpedicaoRetiraLojaStatus conferenciaExpedicaoRetiraLojaStatus");
		
		qb.where("notaFiscalSaida.chavenfe = ?", chaveNota)
			.where("deposito = ? ", WmsUtil.getDeposito())
			.where("expedicaoRetiraLojaStatus = ?", status);
		
		return qb.unique();
	}

	public List<ExpedicaoLojaVO> recuperarDadosPraCriacaoExpedicao(String chaveNota) {
		Deposito deposito = WmsUtil.getDeposito();
		List<Object> args = new ArrayList<Object>();

		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("SELECT NFS.CDNOTAFISCALSAIDA,                                                                                              ");
			sql.append("       NFS.NUMERO,                                                                                                         ");
			sql.append("       NFS.SERIE,                                                                                                          ");
			sql.append("       NFS.CHAVENFE,                                                                                                       ");
			sql.append("       PES.CDPESSOA,                                                                                                       ");
			sql.append("       PES.NOME,                                                                                                           ");
			sql.append("       PES.DOCUMENTO,                                                                                                      ");
			sql.append("       NFP.CDNOTAFISCALSAIDAPRODUTO,                                                                                       ");
			sql.append("       PRO.CDPRODUTO,                                                                                                      ");
			sql.append("       PRO.CODIGO,                                                                                                         ");
			sql.append("       PRO.DESCRICAO,                                                                                                      ");
			sql.append("       NFP.QTDE                                                                                                            ");
			sql.append("  FROM NOTAFISCALSAIDA NFS                                                                                                 ");
			sql.append("  INNER JOIN CLIENTE CLI ON NFS.CDCLIENTE = CLI.CDPESSOA                                                                   ");
			sql.append("  INNER JOIN PESSOA PES ON PES.CDPESSOA = CLI.CDPESSOA                                                                     ");
			sql.append("  INNER JOIN NOTAFISCALSAIDAPRODUTO NFP ON NFP.CDNOTAFISCALSAIDA = NFS.CDNOTAFISCALSAIDA                                   ");
			sql.append("  INNER JOIN PRODUTO PRO ON PRO.CDPRODUTO = NFP.CDPRODUTO                                                                  ");
			sql.append("  INNER JOIN DEPOSITO DEP ON NFS.NRO_LOJA_RETIRADA = DEP.CODIGOERP                                                         ");
			sql.append("  INNER JOIN MANIFESTONOTAFISCAL MNF ON NFS.CDNOTAFISCALSAIDA = MNF.CDNOTAFISCALSAIDA                                      ");
			sql.append("  INNER JOIN MANIFESTO MAN ON MNF.CDMANIFESTO = MAN.CDMANIFESTO                                                            ");
			sql.append("  INNER JOIN RECEBRETIRALOJAPRODUTO RLP ON RLP.CDNOTAFISCALSAIDA = NFS.CDNOTAFISCALSAIDA AND NFP.CDPRODUTO = RLP.CDPRODUTO ");
			sql.append("  INNER JOIN RECEBIMENTORETIRALOJA RRL ON RRL.CDRECEBIMENTORETIRALOJA = RLP.CDRECEBIMENTORETIRALOJA                        ");
			sql.append("WHERE NFS.CDTIPONF = 4                                                                                                     ");
			sql.append("  AND MNF.CDDEPOSITOTRANSBORDO IS NULL                                                                                     ");
			sql.append("  AND MAN.CDTIPOENTREGA <> 4                                                                                               ");
			sql.append("  AND MAN.CDMANIFESTOSTATUS NOT IN (1,2,3,11)                                                                              ");
			sql.append("  AND RRL.CDRECEBRETIRALOJASTATUS = 2                                                                                      ");
			sql.append("  AND RLP.CDTIPOESTOQUE = 1                                                                                                ");
			sql.append("  AND NOT EXISTS (SELECT 1 																								   ");
			sql.append("            		FROM EXPEDICAORETIRALOJA ERL																		   ");
			sql.append("          		   WHERE ERL.CDNOTAFISCALSAIDA = NFS.CDNOTAFISCALSAIDA )												   ");
			sql.append("  AND NFS.CHAVENFE = ?                                                                                                     ");
			sql.append("  AND DEP.CDDEPOSITO = ?                                                                                                   ");

			args.add(chaveNota);		
			args.add(deposito.getCddeposito());		


			@SuppressWarnings("unchecked")
			List<ExpedicaoLojaVO> dados = (List<ExpedicaoLojaVO>) getJdbcTemplate().query(sql.toString(), args.toArray(), new ResultSetExtractor() {

				@Override
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

					List<ExpedicaoLojaVO> registros = new ArrayList<ExpedicaoLojaVO>();

					while (rs.next()) {
						ExpedicaoLojaVO vo = new ExpedicaoLojaVO();

						vo.setCdNotaFiscalSaida(rs.getInt("CDNOTAFISCALSAIDA"));
						vo.setNumeroNota(rs.getString("NUMERO"));
						vo.setSerieNota(rs.getString("SERIE"));
						vo.setChaveNotaFiscal(rs.getString("CHAVENFE"));
						vo.setCdPessoa(rs.getInt("CDPESSOA"));
						vo.setNomePessoa(rs.getString("NOME"));
						vo.setDocumentoPessoa(rs.getString("DOCUMENTO"));
						vo.setCdNotaFiscalSaidaProduto(rs.getInt("CDNOTAFISCALSAIDAPRODUTO"));
						vo.setCdProduto(rs.getInt("CDPRODUTO"));
						vo.setCodigoProduto(rs.getString("CODIGO"));
						vo.setDescricaoProduto(rs.getString("DESCRICAO"));
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
		save.saveOrUpdateManaged("listaExpedicaoRetiraLojaProduto");
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<ExpedicaoRetiraLoja> query) {
		query.joinFetch("expedicaoRetiraLoja.notaFiscalSaida notaFiscalSaida")
		.joinFetch("expedicaoRetiraLoja.deposito deposito")
		.joinFetch("expedicaoRetiraLoja.usuario usuario")
		.joinFetch("expedicaoRetiraLoja.expedicaoRetiraLojaStatus expedicaoRetiraLojaStatus")
		.joinFetch("expedicaoRetiraLoja.listaExpedicaoRetiraLojaProduto expedicaoRetiraLojaProduto")
		.joinFetch("expedicaoRetiraLojaProduto.produto produto");
	}
	
	/**
	 * Atualizar flag impressao do termo ao finalizar a expedicao.
	 *
	 * @param cdExpedicaoRetiraLoja the cd expedicao retira loja
	 */
	public void atualizarFlagImpressaoTermoExpedicao(Integer cdExpedicaoRetiraLoja) {
		getHibernateTemplate().bulkUpdate("update ExpedicaoRetiraLoja set termoImpresso = ? where cdExpedicaoRetiraLoja = ? ", new Object[]{Boolean.TRUE, cdExpedicaoRetiraLoja});		
	}


}
