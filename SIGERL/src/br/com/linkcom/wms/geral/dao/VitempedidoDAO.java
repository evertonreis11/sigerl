package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.view.Vitempedido;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.VitempedidoFiltro;
import br.com.linkcom.wms.util.WmsUtil;

public class VitempedidoDAO extends GenericDAO<Vitempedido> {

	@Override
	public void updateListagemQuery(QueryBuilder<Vitempedido> query, FiltroListagem _filtro) {
		VitempedidoFiltro filtro = (VitempedidoFiltro) _filtro;
		
		query
			.select("vitempedido.cdcarregamentoitem, vitempedido.qtdeconfirmada, vitempedido.numero, vitempedido.numeronota, " +
					"vitempedido.dtmarcacao, vitempedido.dtexclusaoerp, vitempedido.qtde, vitempedido.codigo, " +
					"vitempedido.descricao, vitempedido.cdcarregamento, vitempedido.status, vitempedido.operacao," +
					"vitempedido.dtfaturamento, vitempedido.placa, vitempedido.transportador, vitempedido.cdtipooperacao, " +
					"vitempedido.usaminicd, vitempedido.serienota, vitempedido.numerosegundanota ");
			if(filtro.getCdCarregamento()!=null)
				query.where("vitempedido.cdcarregamento = ?", filtro.getCdCarregamento());
			if(filtro.getNumeroPedido()!=null)
				query.where("vitempedido.numero = ?", filtro.getNumeroPedido());
			if(filtro.getCdExpedicao()!=null)
				query.where("vitempedido.cdexpedicao = ?", filtro.getCdExpedicao());
			query.where("vitempedido.deposito = ?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito());
		
			//Null - Todos, True - Faturado, False - Pendente de faturado
			if(filtro.getSituacaoItem() != null){
				if(filtro.getSituacaoItem())
					query.where("((vitempedido.numeronota is not null and vitempedido.usaminicd = 0) " +
							"or (vitempedido.numeronota is not null and vitempedido.numerosegundanota is not null and vitempedido.usaminicd = 1))");
				else
					query.where("((vitempedido.numeronota is null and vitempedido.usaminicd = 0) " +
							"or (vitempedido.numeronota is null and vitempedido.numerosegundanota is null and vitempedido.usaminicd = 1))");
			}
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Vitempedido> findByRelatorioConsultarPedido(VitempedidoFiltro filtro){
		
		QueryBuilder<Vitempedido> query = query();
		
			query.select("vitempedido.chave, vitempedido.codigo, vitempedido.descricao, vitempedido.qtde, vitempedido.qtdeconfirmada, " +
						 "vitempedido.status, vitempedido.numeronota, vitempedido.numerosegundanota, vitempedido.dtfaturamento, " + 
						 "vitempedido.transportador, vitempedido.placa, vitempedido.cdcarregamento, vitempedido.numero, vitempedido.operacao ");
			
			if(filtro.getCdCarregamento()!=null)
				query.where("vitempedido.cdcarregamento = ?", filtro.getCdCarregamento());
			if(filtro.getNumeroPedido()!=null)
				query.where("vitempedido.numero = ?", filtro.getNumeroPedido());
			if(filtro.getCdExpedicao()!=null)
				query.where("vitempedido.cdexpedicao = ?", filtro.getCdExpedicao());
			
			query.where("vitempedido.deposito = ?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito());
		
			if(filtro.getOrderBy()!=null && !filtro.getOrderBy().isEmpty()){
				query.orderBy("vitempedido."+filtro.getOrderBy());
			}
			
			//Null - Todos, True - Faturado, False - Pendente de faturado
			if(filtro.getSituacaoItem() != null){
				if(filtro.getSituacaoItem())
					query.where("((vitempedido.numeronota is not null and vitempedido.usaminicd = 0) " +
							"or (vitempedido.numeronota is not null and vitempedido.numerosegundanota is not null and vitempedido.usaminicd = 1))");
				else
					query.where("((vitempedido.numeronota is null and vitempedido.usaminicd = 0) " +
							"or (vitempedido.numeronota is null and vitempedido.numerosegundanota is null and vitempedido.usaminicd = 1))");
			}
			
		return query.list();
	}
	
}
