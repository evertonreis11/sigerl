package br.com.linkcom.wms.geral.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.OcupacaoEnderecoHistorico;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.ExtratoOcupacaoVO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ExtratoOcupacaoFiltro;
import br.com.linkcom.wms.util.WmsUtil;

public class OcupacaoEnderecoHistoricoDAO extends GenericDAO<OcupacaoEnderecoHistorico> {


	/**
	 * Lista os históricos de ocupação para o relatório de extrato de ocupação.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ExtratoOcupacaoVO> findForReportExtratoOcupacao(ExtratoOcupacaoFiltro filtro) {
		final QueryBuilder<OcupacaoEnderecoHistorico> queryBuilder = query()
				.select("ordemservico.cdordemservico as cdordemservico," +
						"area.codigo || '.' || endereco.endereco as endereco," +
						"endereco.cdendereco as cdendereco,produto.descricao as descricaoProduto," +
						"produto.codigo as codigoProduto,produto.cdproduto as cdproduto," +
						"ocupacaoEnderecoHistorico.dtentrada as dtentrada,ocupacaoEnderecoHistorico.qtde as qtde," +
						"ocupacaoEnderecoHistorico.acumula as acumula,ordemtipo.nome as tipoOperacao," +
						"carregamento.cdcarregamento as cdcarregamento,transferencia.cdtransferencia as cdtransferencia," +
						"inventario.cdinventario as cdinventario,recebimento.cdrecebimento as cdrecebimento," +
						"ocupacaoEnderecoHistorico.cdocupacaoenderecohistorico as cdocupacaoenderecohistorico," +
						"usuario.nome as operador")
				.join("ocupacaoEnderecoHistorico.produto produto")
				.join("ocupacaoEnderecoHistorico.ordemservico ordemservico")
				.join("ocupacaoEnderecoHistorico.endereco endereco")
				.join("endereco.area area")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.listaOrdemServicoUsuario osu")
				.join("osu.usuario usuario")
				.leftOuterJoin("ordemservico.carregamento carregamento")
				.leftOuterJoin("ordemservico.transferencia transferencia")
				.leftOuterJoin("ordemservico.recebimento recebimento")
				.leftOuterJoin("ordemservico.inventariolote inventariolote")
				.leftOuterJoin("inventariolote.inventario inventario")
				.orderBy("ocupacaoEnderecoHistorico.dtentrada")
				.setUseTranslator(false);
		
		queryBuilder.where("area.deposito = ?", WmsUtil.getDeposito());
		queryBuilder.where("area=?", filtro.getEndereco().getArea());
		queryBuilder.where("endereco.endereco.rua=?", filtro.getEndereco().getRuaI());
		queryBuilder.where("endereco.endereco.predio=?", filtro.getEndereco().getPredioI());
		queryBuilder.where("endereco.endereco.nivel=?", filtro.getEndereco().getNivelI());
		queryBuilder.where("endereco.endereco.apto=?", filtro.getEndereco().getAptoI());
		queryBuilder.where("trunc(ocupacaoEnderecoHistorico.dtentrada)>=?", filtro.getInicio());
		queryBuilder.where("trunc(ocupacaoEnderecoHistorico.dtentrada)<=?", filtro.getTermino());
		if(filtro.getProduto() != null && filtro.getProduto().getCdproduto() != null){
			queryBuilder
				.openParentheses()
					.where("produto=?", filtro.getProduto()).or()
					.where("produto.produtoprincipal=?", filtro.getProduto())
			.closeParentheses();
		}
		queryBuilder.orderBy("produto.descricao, area.codigo, endereco.endereco, ordemservico.cdordemservico");
		
		return getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryBuilder.getQuery());
				for (int i = 0; i < queryBuilder.getWhere().getParameters().size(); i++) {
					query.setParameter(i, queryBuilder.getWhere().getParameters().get(i));
				}
				query.setResultTransformer(new AliasToBeanResultTransformer(ExtratoOcupacaoVO.class));
				return query.list();
			}
		});
	}

	/**
	 * Obtém o saldo anterior a uma determinada data.
	 * 
	 * @author Giovane Freitas
	 * @param inicio
	 * @param endereco
	 * @param produto
	 * @return
	 */
	public Long getSaldoAnterior(Date inicio, Endereco endereco, Produto produto) {
		return getJdbcTemplate().queryForLong(
				"select SALDOATUAL(" + endereco.getCdendereco() + "," + produto.getCdproduto() + ",to_date('"
						+ new SimpleDateFormat("dd/MM/yyyy").format(inicio) + "', 'DD/MM/YYYY')) from dual");
	}
	

}
