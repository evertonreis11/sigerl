package br.com.linkcom.wms.geral.dao;

import java.sql.Timestamp;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.view.Vitempedidonaoliberado;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.VitempedidonaoliberadoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

import com.ibm.icu.text.SimpleDateFormat;

public class VitempedidonaoliberadoDAO extends GenericDAO<Vitempedidonaoliberado> {

	@Override
	public void updateListagemQuery(QueryBuilder<Vitempedidonaoliberado> query, FiltroListagem _filtro) {
		VitempedidonaoliberadoFiltro filtro = (VitempedidonaoliberadoFiltro) _filtro;
		
		query
			.select("vitempedidonaoliberado.cdpedidovendaproduto, vitempedidonaoliberado.qtde, vitempedidonaoliberado.numero, vitempedidonaoliberado.operacao, " +
					"vitempedidonaoliberado.codigo, vitempedidonaoliberado.descricao, vitempedidonaoliberado.qtdeconfirmada, vitempedidonaoliberado.cdcarregamento")
			.where("vitempedidonaoliberado.cdcarregamento = ?", filtro.getCdCarregamento())
			.where("vitempedidonaoliberado.numero = ?", filtro.getNumeroPedido())
			.where("vitempedidonaoliberado.codigo = ?", filtro.getCodigoProduto())
			.where("vitempedidonaoliberado.deposito = ?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito());
	}

	/**
	 * Método que altera o status do pedido venda produto
	 * 
	 * @param whereInPedidoVendaProduto
	 * @author Tomás Rabelo
	 */
	public void marcarPedidoVendaProduto(String whereInPedidoVendaProduto) {
		if(whereInPedidoVendaProduto == null || whereInPedidoVendaProduto.equals(""))
			throw new WmsException("Parâmetros inválidos.");
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Timestamp dtAtual = new Timestamp(System.currentTimeMillis());
		
		StringBuilder sql = new StringBuilder();
			sql.append(" update pedidovendaproduto p");
			sql.append(" set p.dtexclusaoerp = TO_DATE('").append(dtFormat.format(dtAtual)).append("','DD/MM/YYYY hh:mi:ss')");
			sql.append(" where p.cdpedidovendaproduto in (").append(whereInPedidoVendaProduto).append(")");
		
		getJdbcTemplate().update(sql.toString());
	}
	
}