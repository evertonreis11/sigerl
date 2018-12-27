package br.com.linkcom.wms.geral.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.PontoControle;
import br.com.linkcom.wms.geral.bean.ProblemaPedidoLoja;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ProblemaPedidoLojaDAO extends GenericDAO<ProblemaPedidoLoja> {
	@Override
	public void updateEntradaQuery(QueryBuilder<ProblemaPedidoLoja> query) {
		query.joinFetch("problemaPedidoLoja.pontoControle pontoControle")
			.joinFetch("problemaPedidoLoja.tipoEstoque tipoEstoque ");
	}

	public void criarTrakkingNota(Notafiscalsaida nota, PontoControle pontoControle) {

		Connection connection = null;
		CallableStatement cs = null;

		try {
			connection = IntegradorSqlUtil.getNewConnection();

			cs = connection.prepareCall("{ call PRC_GRAVAPONTODECONTROLE(?, ?, ?, ?, ?) }");
			
			cs.setInt(1,nota.getCdnotafiscalsaida());
			cs.setString(2, "N");
			cs.setString(3, pontoControle.getSigla());
			cs.setNull(4, Types.VARCHAR);
			cs.setNull(5, Types.DATE);

			cs.execute();

			connection.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		finally {
			try {				
				cs.close();
				connection.close();
			}
			catch (Exception e) {
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}
	}
}
