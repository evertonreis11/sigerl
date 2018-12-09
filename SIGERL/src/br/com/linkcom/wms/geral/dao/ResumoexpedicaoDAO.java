package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Resumoexpedicao;
import br.com.linkcom.wms.geral.bean.vo.ResumoexpedicaoVO;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ResumoexpedicaoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ResumoexpedicaoDAO extends GenericDAO<Resumoexpedicao> {

	@Override
	public void updateListagemQuery(QueryBuilder<Resumoexpedicao> query, FiltroListagem _filtro) {
		ResumoexpedicaoFiltro filtro = (ResumoexpedicaoFiltro) _filtro;
		
		if (filtro.getDeposito() == null || filtro.getDeposito().getCddeposito() == null)
			throw new WmsException("O campo depósito é obrigatório.");
		else
			query.where("cddeposito = ? ", filtro.getDeposito().getCddeposito());
		
		if (filtro.getDatainicio() == null )
			throw new WmsException("O campo data inicial é obrigatório.");
		else
			query.where("fechamento >= ?", filtro.getDatainicio());
		
		query.where("fechamento <= ?", filtro.getDatafim());
		
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosExportacao(ResumoexpedicaoFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		Date datafim = filtro.getDatafim();

		if (filtro.getDeposito() == null || filtro.getDeposito().getCddeposito() == null)
			throw new WmsException("O campo depósito é obrigatório.");
		
		if (filtro.getDatainicio() == null )
			throw new WmsException("O campo data inicial é obrigatório.");
		
		sql.append("select deposito as \"Depósito\", fechamento as \"Fechamento\", linhaseparacao as \"Linha de separação\", ");
		sql.append("tipooperacao as \"Tipo de operação\", qtdeesperada as \"Qtde. esperada\", pesoesperado as \"Peso esperado\", ");
		sql.append("valoresperado as \"Valor esperado\", volumesesperados as \"Volumes esperados\", cubagemesperada as \"Cubagem esperada\", ");
		sql.append("qtdeconfirmada as \"Qtde. confirmada\", pesoconfirmado as \"Peso confirmado\", valorconfirmado as \"Valor confirmado\", ");
		sql.append("volumesconfirmados as \"Volumes confirmados\", cubagemconfirmada as \"Cubagem confirmada\" from V_RESUMOEXPEDICAO ");
		sql.append("where cddeposito = ? and fechamento >= ? ");
		if (filtro.getDatafim() != null){
			sql.append(" and fechamento <= ?");
		}
		
		Object[] args;
		
		if (datafim != null){
			args = new Object[]{filtro.getDeposito().getCddeposito(), filtro.getDatainicio(), datafim};
		}else
			args = new Object[]{filtro.getDeposito().getCddeposito(), filtro.getDatainicio()};
		
		return getJdbcTemplate().queryForRowSet(sql.toString(), args);
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResumoexpedicaoVO> getDadosPlanilhaDetalhada(ResumoexpedicaoFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		String dataInicio = null;
		String dataFim = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		if(filtro.getDatainicio()!=null){
			dataInicio = dateFormat.format(filtro.getDatainicio().getTime());
		}
		
		if(filtro.getDatafim()!=null){
			dataFim = dateFormat.format(filtro.getDatafim().getTime());
		}
		
		sql.append("select ");
		sql.append(" d.nome as deposito, tp.nome as tipoOperacao, t.nome as transportador, v.placa as veiculoPlaca, ls.nome as linhaSeparacao, "); 
		//subselect para buscar os números dos pedidos do carregamento.
			sql.append(" DBMS_LOB.SUBSTR( (select DISTINCT(rtrim(xmlagg(xmlelement(e, pv2.numero || ',')).extract('//text()').extract('//text()').getclobval() ,', ')) ");
			sql.append(" from carregamento c2 ");
			sql.append(" join carregamentoitem ci2 on (ci2.cdcarregamento = c2.cdcarregamento) ");
			sql.append(" join pedidovendaproduto pp2 on (ci2.cdpedidovendaproduto = pp2.cdpedidovendaproduto) ");
			sql.append(" join pedidovenda pv2 on (pv2.cdpedidovenda = pp2.cdpedidovenda) ");
			sql.append(" join produto p2 on (pp2.cdproduto = p2.cdproduto) ");
			sql.append(" where ci2.cdcarregamento=ci.cdcarregamento");
			sql.append(" and p.codigo=p2.codigo),5000,1) ");
			sql.append("as numeroPedido, ");
		sql.append("  trunc(c.dtcarregamento) as montagem,  ");
		sql.append("  trunc(c.dtfimcarregamento) as fechamento, "); 
		sql.append("  c.cdcarregamento as cdcarregamento, ");
		sql.append("  p.codigo as codigo, p.descricao as descricao, "); 
		sql.append("  sum((pvp.qtde - ci.qtdeconfirmada) * pvp.valor)/100 as valorCortado, ");   
		sql.append("  sum((pvp.qtde) * pvp.valor)/100 as valorEsperado,    ");
		sql.append("  sum(pvp.qtde) as qtdeEsperada,  ");
		sql.append("  sum(pvp.qtde * p.peso) as pesoEsperado, "); 
		sql.append("  sum(ci.qtdeconfirmada) as qtdeConfirmada,  ");
		sql.append("  sum(pvp.qtde - ci.qtdeconfirmada) as qtdeCortada, ");
		sql.append("  sum(ci.qtdeconfirmada * p.peso) as pesoConfirmado,  ");
		sql.append("  sum(ci.qtdeconfirmada * pvp.valor)/100 as valorConfirmado, "); 
		sql.append("  sum(ci.qtdeconfirmada * case when (p.qtdevolumes = 0) or (p.qtdevolumes is null) then 1 else p.qtdevolumes end) as numeroVolumeConfirmado, ");
		sql.append("  Sum(ci.qtdeconfirmada * p.cubagem) as cubagemConfirmada, ");
		sql.append("  tv.nome as tipoVeiculo ");
		sql.append("from carregamentoitem ci ");
		sql.append("  join pedidovendaproduto pvp on pvp.cdpedidovendaproduto = ci.cdpedidovendaproduto ");
		sql.append("  join carregamento c on ci.cdcarregamento = c.cdcarregamento ");
		sql.append("  join pedidovenda pv on pv.cdpedidovenda = pvp.cdpedidovenda ");
		sql.append("  join tipooperacao tp on tp.cdtipooperacao = pvp.cdtipooperacao ");
		sql.append("  join deposito d on d.cddeposito = c.cddeposito ");
		sql.append("  join produto p ON p.cdproduto = pvp.cdproduto ");
		sql.append("  left join veiculo v ON v.cdveiculo = c.cdveiculo ");
		sql.append("  left join tipoveiculo tv ON tv.cdtipoveiculo = v.cdtipoveiculo ");
		sql.append("  left join pessoa t on t.cdpessoa = v.cdpessoa ");
		sql.append("  join dadologistico dl ON dl.cdproduto = p.cdproduto AND dl.cddeposito = c.cddeposito ");
		sql.append("  join linhaseparacao ls ON ls.cdlinhaseparacao = dl.cdlinhaseparacao ");
		sql.append("where ");
		sql.append("   c.cddeposito = ").append(filtro.getDeposito().getCddeposito());
		sql.append("   and Trunc(c.dtcarregamento) >= to_date('").append(dataInicio).append("','dd/MM/yyyy')");
		sql.append("   and (c.cdcarregamentostatus = 4 or c.cdcarregamentostatus = 6) ");
		
		if(filtro.getDatafim()!=null){
			sql.append("   and Trunc(c.dtfimcarregamento) <= to_date('").append(dataFim).append("','dd/MM/yyyy')");
		}
		
		sql.append("group by pv.numero, d.nome, tp.nome, t.nome, v.placa, ls.nome, trunc(c.dtcarregamento), trunc(c.dtfimcarregamento), ");
		sql.append("	c.cdcarregamento, p.codigo, p.descricao, tv.nome, ci.cdcarregamento ");
		sql.append("order by 1,2,3 ");
		
		return getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ResumoexpedicaoVO resumoexpedicaoVO = new ResumoexpedicaoVO();
				
					resumoexpedicaoVO.setCdcarregamento(rs.getString("cdcarregamento"));
					resumoexpedicaoVO.setCodigo(rs.getString("codigo"));
					resumoexpedicaoVO.setCubagemConfirmada(rs.getString("cubagemConfirmada"));
					resumoexpedicaoVO.setDeposito(rs.getString("deposito"));
					resumoexpedicaoVO.setDescricao(rs.getString("descricao"));
					resumoexpedicaoVO.setFechamento(rs.getString("fechamento"));
					resumoexpedicaoVO.setLinhaSeparacao(rs.getString("linhaSeparacao"));
					resumoexpedicaoVO.setMontagem(rs.getString("montagem"));
					resumoexpedicaoVO.setNumeroPedido(rs.getString("numeroPedido"));
					resumoexpedicaoVO.setNumeroVolumeConfirmado(rs.getString("numeroVolumeConfirmado"));
					resumoexpedicaoVO.setPesoConfirmado(rs.getString("pesoConfirmado"));
					resumoexpedicaoVO.setPesoEsperado(rs.getString("pesoEsperado"));
					resumoexpedicaoVO.setQtdeConfirmada(rs.getString("qtdeConfirmada"));
					resumoexpedicaoVO.setQtdeCortada(rs.getString("qtdeCortada"));
					resumoexpedicaoVO.setQtdeEsperada(rs.getString("qtdeEsperada"));
					resumoexpedicaoVO.setTipoOperacao(rs.getString("tipoOperacao"));
					resumoexpedicaoVO.setTransportador(rs.getString("transportador"));
					resumoexpedicaoVO.setValorCortado(rs.getString("valorCortado"));
					resumoexpedicaoVO.setValorEsperado(rs.getString("valorEsperado"));
					resumoexpedicaoVO.setVeiculoPlaca(rs.getString("veiculoPlaca"));
				
				return resumoexpedicaoVO;
			}
		});
		
	}
	
}
