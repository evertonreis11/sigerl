package br.com.linkcom.wms.geral.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Cortediario;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.CortediarioFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class CortediarioDAO extends GenericDAO<Cortediario>{

	@Override
	public void updateListagemQuery(QueryBuilder<Cortediario> query, FiltroListagem _filtro) {
		CortediarioFiltro filtro = (CortediarioFiltro) _filtro;
		
		if (filtro.getDeposito() == null || filtro.getDeposito().getCddeposito() == null)
			throw new WmsException("O campo dep�sito � obrigat�rio.");
		else
			query.where("cddeposito = ? ", filtro.getDeposito().getCddeposito());
		
		if (filtro.getDatainicio() == null )
			throw new WmsException("O campo data de fechamento � obrigat�rio.");
		else
			query.where("fechamento >= ?", filtro.getDatainicio());
		
		query.where("fechamento <= ?", filtro.getDatafim());
	}

	public SqlRowSet getDadosExportacao(CortediarioFiltro filtro) {
		if (filtro.getDeposito() == null || filtro.getDeposito().getCddeposito() == null)
			throw new WmsException("O campo dep�sito � obrigat�rio.");
		
		if (filtro.getDatainicio() == null )
			throw new WmsException("O campo data de fechamento � obrigat�rio.");
		
		String sql = "select deposito as \"Dep�sito\", tipooperacao as \"Tipo de opera��o\", " +
				"fechamento as \"Fechamento\", placa as \"Ve�culo\", qtdeesperada as \"Qtde. esperada\", " +
				"qtdeconfirmada as \"Qtde. confirmada\", qtdecortada as \"Qtde. cortada\" " +
				"from V_CORTEDIARIO where cddeposito = ? and fechamento >= ? ";
		
		Object[] args;

		if (filtro.getDatafim() != null){
			sql += " and fechamento <= ?";
			args = new Object[]{filtro.getDeposito().getCddeposito(), filtro.getDatainicio(), filtro.getDatafim()};
		}else
			args = new Object[]{filtro.getDeposito().getCddeposito(), filtro.getDatainicio()};
		
		return getJdbcTemplate().queryForRowSet(sql, args);
	}
}
