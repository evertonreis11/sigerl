package br.com.linkcom.wms.sincronizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.java.dev.jaxb.array.StringArray;

public class SqlUtil {

	public static Integer getSequence(Connection connection, String nomeSequence) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select sq_"+nomeSequence+".nextval from dual";
			statementSelect = connection.prepareStatement(sqlSelect);
			resultSet = statementSelect.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				throw new RuntimeException("Nao foi possivel obter sequence de: " + nomeSequence);
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static StringArray getListaDeposito(Connection connection) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cnpj from deposito where cnpj is not null";
			statementSelect = connection.prepareStatement(sqlSelect);
			resultSet = statementSelect.executeQuery();

			StringArray listaDeposito = new StringArray();
			while (resultSet.next()) {
				listaDeposito.getItem().add(resultSet.getString(1));
			}
			return listaDeposito;
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}
	
	public static Integer getCdDeposito(Connection connection, String cnpj) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cddeposito from deposito where cnpj=?";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setString(1, cnpj);
			resultSet = statementSelect.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				throw new RuntimeException("Deposito nao encontrado: " + cnpj);
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static Integer getCdCliente(Connection connection, Long codigoerp) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdpessoa from cliente where codigoerp=?";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setLong(1, codigoerp);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				return null;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}
	
	public static Integer getCdFornecedor(Connection connection, Long codigoerp) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdpessoa from fornecedor where codigoerp=?";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setLong(1, codigoerp);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				return null;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}
	
	public static Integer getCdProduto(Connection connection, Long codigoerp) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdproduto from produto where codigoerp=?";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setLong(1, codigoerp);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				return null;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static Integer getCdPedidoCompraProduto(Connection connection, Integer cdPedidoCompra, Integer cdProduto) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdpedidocompraproduto from pedidocompraproduto where cdpedidocompra = ? and cdproduto = ? ";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setInt(1, cdPedidoCompra);
			statementSelect.setInt(2, cdProduto);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt("cdpedidocompraproduto");
			}
			else {
				return null;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static List<Integer> getIDsPedidoCompraProduto(Connection connection, Integer cdPedidoCompra) throws SQLException {

		List<Integer> resultado = new ArrayList<Integer>();

		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdpedidocompraproduto from pedidocompraproduto where cdpedidocompra = ? ";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setInt(1, cdPedidoCompra);
			resultSet = statementSelect.executeQuery();
			
			while (resultSet.next()) {
				resultado.add(resultSet.getInt(1));
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
		
		return resultado;
	}

	public static TipoAgendamento getTipoAgendamento(Connection connection, Integer cdPedidoCompra) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdagenda, ap.parcial from agendapedido ap " +
					"where cdpedidocompra = ?";
			
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setInt(1, cdPedidoCompra);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				TipoAgendamento tipoAgendamento;
				if (resultSet.getInt("parcial") == 1)
					tipoAgendamento = TipoAgendamento.PARCIAL;
				else
					tipoAgendamento = TipoAgendamento.COMPLETO;
				
				tipoAgendamento.setCdAgendamento(resultSet.getInt("cdagenda"));
				return tipoAgendamento;
			}
			else {
				return TipoAgendamento.INEXISTENTE;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static int getQtdeAgendadaAtual(Connection connection, Integer cdPedidoCompraProduto) throws SQLException {

		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select sum(aparc.qtde), ap.cdpedidocompra, cdpedidocompraproduto " +
					"from agendaparcial aparc " +
					"inner join agendapedido ap on aparc.cdagenda = ap.cdagenda " +
					"where aparc.cdpedidocompraproduto = ? " +
					"group by ap.cdpedidocompra, cdpedidocompraproduto ";
		
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setInt(1, cdPedidoCompraProduto);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				return 0;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static Integer getQtdePedCompProdAtual(Connection connection, Integer cdPedidoCompraProduto) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select qtde from pedidocompraproduto where cdpedidocompraproduto = ?";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setInt(1, cdPedidoCompraProduto);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			else {
				return null;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}

	public static Integer getCdPedidoCompra(Connection connection, Long pedidoCompra) throws SQLException {
		PreparedStatement statementSelect = null;
		ResultSet resultSet = null;
		try {
			String sqlSelect = "select cdpedidocompra from pedidocompra where codigoerp = ? ";
			statementSelect = connection.prepareStatement(sqlSelect);
			statementSelect.setLong(1, pedidoCompra);
			resultSet = statementSelect.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				return null;
			}
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
			try {
				statementSelect.close();
			}
			catch (Exception e) {
			}
		}
	}
}
