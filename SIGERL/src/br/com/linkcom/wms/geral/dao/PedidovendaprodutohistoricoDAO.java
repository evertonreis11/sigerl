package br.com.linkcom.wms.geral.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Pedidovendaprodutohistorico;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PedidovendaprodutoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidovendaprodutohistoricoDAO extends GenericDAO<Pedidovendaprodutohistorico>{

	@Override
	public void updateListagemQuery(QueryBuilder<Pedidovendaprodutohistorico> query, FiltroListagem _filtro) {
		
		PedidovendaprodutoFiltro filtro = (PedidovendaprodutoFiltro) _filtro;
		StringBuilder numeroPedido = new StringBuilder();
		
		if(filtro.getNumeroPedido()!=null && !filtro.getNumeroPedido().isEmpty()){
			numeroPedido.append(filtro.getNumeroPedido());
		}else{
			numeroPedido.append("5"); 
			numeroPedido.append(filtro.getEmpresa().getCdempresa());
			numeroPedido.append(StringUtils.leftPad(filtro.getNumeroPedidoMV(), 7, "0"));
			numeroPedido.append(StringUtils.leftPad(filtro.getNumeroLoja(), 4, "0"));
		}
		
		query.select("distinct pedidovendaproduto.cdpedidovendaproduto, pedidovendaprodutostatus.cdpedidovendaprodutostatus, " +
				"pedidovendaprodutostatus.nome, pedidovenda.cdpedidovenda, pedidovenda.numero, produto.cdproduto, produto.descricao, " +
				"produto.codigo, produto.codigoerp, pedidovendaproduto.dtprevisaoentrega ");
		query.join("pedidovendaprodutohistorico.pedidovendaproduto pedidovendaproduto");
		query.join("pedidovendaproduto.pedidovendaprodutostatus pedidovendaprodutostatus");
		query.join("pedidovendaproduto.produto produto");
		query.join("pedidovendaproduto.pedidovenda pedidovenda");
		query.where("pedidovenda.numero = ? ",numeroPedido.toString());
		
		query.list();
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Pedidovendaprodutohistorico> loadHistoricoForDialog(PedidovendaprodutoFiltro filtro) {
		
		QueryBuilder<Pedidovendaprodutohistorico> query = query();
		
		query.select("pedidovendaprodutohistorico.data, pedidovendaprodutohistorico.historico, usuario.nome, " +
					 "pedidovendaprodutohistorico.pedidovendaprodutostatus, pedidovendaprodutostatus.nome");
		query.join("pedidovendaprodutohistorico.usuario usuario");
		query.join("pedidovendaprodutohistorico.pedidovendaprodutostatus pedidovendaprodutostatus");
		query.join("pedidovendaprodutohistorico.pedidovendaproduto pedidovendaproduto");
		query.where("pedidovendaproduto.cdpedidovendaproduto = ?",filtro.getCdpedidovendaproduto());
		query.orderBy("pedidovendaprodutohistorico.data");
		
		return query.list();
	}

}
