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
				.where("produtoCodigoDeBarras.codigo = ?", codigoEan)
				.where("recebimentoRetiraLojaStatus = ?", RecebimentoRetiraLojaStatus.EM_CONFERENCIA);
		
		
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
				  "recebimentoRetiraLojaProduto.cdRecebimentoRetiraLojaProduto, recebimentoRetiraLojaProduto.qtde, " +
				  "produto.cdproduto, produto.codigo, produto.descricao,notaFiscalSaida.cdnotafiscalsaida, "+
				  "produtoCodigoDeBarras.cdprodutocodigobarras,produtoCodigoDeBarras.codigo, tipoEstoque.cdTipoEstoque, tipoEstoque.descricao ")
			.join("recebimentoRetiraLoja.listaRecebimentoRetiraLojaProduto recebimentoRetiraLojaProduto")
			.join("recebimentoRetiraLoja.recebimentoRetiraLojaStatus recebimentoRetiraLojaStatus")
			.join("recebimentoRetiraLoja.manifesto manifesto")
			.join("recebimentoRetiraLoja.usuario usuario")
			.join("recebimentoRetiraLoja.deposito deposito")
		 	.join("recebimentoRetiraLojaProduto.produto produto")
		 	.join("produto.listaProdutoCodigoDeBarras produtoCodigoDeBarras")
		 	.join("recebimentoRetiraLojaProduto.notaFiscalSaida notaFiscalSaida")
		 	.join("recebimentoRetiraLojaProduto.tipoEstoque tipoEstoque");
		
		return qb;
	}
	
	
	/**
	 * Find recebimento loja.
	 *
	 * @param cdRecebimentoRetiraLoja the cd recebimento retira loja
	 * @return the recebimento retira loja
	 */
	public RecebimentoRetiraLoja findRecebimentoLoja(Integer cdRecebimentoRetiraLoja) {
		
		QueryBuilder<RecebimentoRetiraLoja> qb = criaConsultaRecebimentoRetiraLoja();
		
		qb.where("recebimentoRetiraLoja.cdRecebimentoRetiraLoja = ", cdRecebimentoRetiraLoja)
		.where("recebimentoRetiraLojaStatus = ?", RecebimentoRetiraLojaStatus.EM_CONFERENCIA);
		
		return qb.unique();
	}

	public List<RecebimentoLojaVO> recuperaDadosRecebimentoRetiraLoja(String codigoEan) {
		Deposito deposito = WmsUtil.getDeposito();
		List<Object> args = new ArrayList<Object>();

		try {
			StringBuilder sql = new StringBuilder();
			
			//TODO montar SQL
//			StringBuilder sql = createSqlPrevisaoPagamento(filtro, args);
			
			
			@SuppressWarnings("unchecked")
			List<RecebimentoLojaVO> montagens = (List<RecebimentoLojaVO>) getJdbcTemplate().query(sql.toString(), args.toArray(), new ResultSetExtractor() {
				
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


			return montagens;

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
