package br.com.linkcom.wms.geral.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Controleestoque;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ControleestoqueFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

public class ControleestoqueDAO extends GenericDAO<Controleestoque> {

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		super.updateSaveOrUpdate(save);
		save.saveOrUpdateManaged("listaControleestoqueproduto");
	}

	/**
	 * Busca os {@link Controleestoque} baseado no filtro informado.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<Controleestoque> find(ControleestoqueFiltro filtro) {

		QueryBuilder<Controleestoque> query = query()
			.select("controleestoque.cdcontroleestoque,controleestoque.dtcontroleestoque," +
					"controleestoquestatus.cdcontroleestoquestatus,controleestoquestatus.nome")
			.join("controleestoque.controleestoquestatus controleestoquestatus")
			.where("controleestoque.deposito=?", WmsUtil.getDeposito())
			.orderBy("controleestoque.dtcontroleestoque desc");

		if (filtro.getNumeroControle() != null)
			query.entity(new Controleestoque(filtro.getNumeroControle()));
		else {
			query.where("controleestoque.dtcontroleestoque>=?",filtro.getDataInicial())
				.where("controleestoque.dtcontroleestoque<=?", WmsUtil.dataToEndOfDay(filtro.getDataFinal()))
				.where("controleestoque.controleestoquestatus=?",filtro.getControleestoquestatus());
		}
		
		return query.list();
	}

	/**
	 * Busca o {@link Controleestoque} e todos os seus itens para o relatório.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public Controleestoque findForReport(ControleestoqueFiltro filtro) {
		QueryBuilder<Controleestoque> query = query()
			.select("controleestoque.cdcontroleestoque,controleestoque.dtcontroleestoque," +
					"controleestoquestatus.cdcontroleestoquestatus,controleestoquestatus.nome," +
					"controleestoqueproduto.cdcontroleestoqueproduto,controleestoqueproduto.qtdeesperada," +
					"controleestoqueproduto.qtde,controleestoqueproduto.qtdeavaria,produto.codigo,produto.descricao," +
					"deposito.cddeposito,deposito.nome,produtoclasse.nome")
			.join("controleestoque.deposito deposito")
			.join("controleestoque.controleestoquestatus controleestoquestatus")
			.leftOuterJoin("controleestoque.listaControleestoqueproduto controleestoqueproduto")
			.leftOuterJoin("controleestoqueproduto.produto produto")
			.leftOuterJoin("produto.produtoclasse produtoclasse")
			.entity(new Controleestoque(filtro.getNumeroControle()));
		
		if (filtro.getSomenteItensDivergentes() != null && filtro.getSomenteItensDivergentes())
			query.where("controleestoqueproduto.qtdeesperada <> controleestoqueproduto.qtde");
			
		if (filtro.getLinhaseparacao() != null){
			query.leftOuterJoin("produto.listaDadoLogistico dadologistico with dadologistico.deposito.id = " + WmsUtil.getDeposito().getCddeposito());
			query.where("dadologistico.linhaseparacao = ?", filtro.getLinhaseparacao());
		}
		
		if (filtro.getOrdenacao() != null)
			query.orderBy(filtro.getOrdenacao().getId().toString());
		else
			query.orderBy("produto.codigo,produto.descricao");
		
		return query.unique();
	}

	/**
	 * Retorna o saldo total da última importação realizada.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public long getTotalUltimaImportacao(Produto produto) {
		if (produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetro inválido.");
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(qtde) ");
		sql.append("from controleestoqueproduto ");
		sql.append("where cdcontroleestoque = (select max(cdcontroleestoque) from controleestoque where cddeposito = ?) ");
		sql.append("	and cdproduto = ? ");
		
		Object[] args = new Object[]{
					WmsUtil.getDeposito().getCddeposito(),
					produto.getCdproduto()};
	
		return getJdbcTemplate().queryForLong(sql.toString(), args);
	}

	/**
	 * Retorna a data da última importação realizada.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public Date getDataUltimaImportacao() {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select dtcontroleestoque ");
		sql.append("from controleestoque ");
		sql.append("where cdcontroleestoque = (select max(cdcontroleestoque) from controleestoque where cddeposito = ?)");
		
		Object[] args = new Object[]{
				WmsUtil.getDeposito().getCddeposito()};
		
		return (Date) getJdbcTemplate().query(sql.toString(), args, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				
				if (rs.next())
					return rs.getDate("dtcontroleestoque");
				else
					return null;
			}
			
		});
	}
}
