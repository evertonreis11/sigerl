package br.com.linkcom.wms.geral.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoexpedicaoFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

public class CarregamentoexpedicaoDAO extends GenericDAO<Carregamento> {

	@Override
	public void updateListagemQuery(QueryBuilder<Carregamento> query,FiltroListagem _filtro) {
		if(_filtro == null)
			throw new WmsException("Parâmetros incorretos.");
		
		CarregamentoexpedicaoFiltro filtro = (CarregamentoexpedicaoFiltro) _filtro;
		
		query
			.select("carregamento.cdcarregamento, carregamentostatus.cdcarregamentostatus, carregamentostatus.nome, box.cdbox, box.nome, " +
					"carregamento.paletesdisponiveis,carregamento.dtcarregamento,carregamento.dtfimcarregamento,carregamento.listaRota," +
					"veiculo.cdveiculo, veiculo.placa, transportador.cdpessoa, transportador.nome")
			.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
			.leftOuterJoin("carregamento.veiculo veiculo")
			.leftOuterJoin("veiculo.transportador transportador")
			.leftOuterJoin("carregamento.box box")
			.where("carregamento.deposito = ?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("carregamento.cdcarregamento = ?", filtro.getCdcarregamento())			
			.where("carregamento.dtcarregamento >= ?", DateUtil.dataToBeginOfDay(filtro.getDtmontageminicial()))
			.where("carregamento.dtcarregamento <= ?", DateUtil.dataToEndOfDay(filtro.getDtmontagemfinal()))			
			.where("carregamento.dtfimcarregamento >= ?",  DateUtil.dataToBeginOfDay(filtro.getDtfinalizadainicial()))
			.where("carregamento.dtfimcarregamento <= ?", DateUtil.dataToEndOfDay(filtro.getDtfinalizadafinal()))
			.where("veiculo = ?", filtro.getVeiculo())
			.where("box = ?", filtro.getBox())
			.where("transportador = ?", filtro.getTransportador())
			.orderBy("carregamento.cdcarregamento desc");
	
		if (filtro.getCarregamentostatus() != null){
			if (new Integer(-1).equals(filtro.getCarregamentostatus().getCdcarregamentostatus())){
				
				List<Carregamentostatus> lista = new ArrayList<Carregamentostatus>();
				lista.add(Carregamentostatus.EM_MONTAGEM);
				lista.add(Carregamentostatus.MONTADO);
				lista.add(Carregamentostatus.EM_SEPARACAO);
				lista.add(Carregamentostatus.CONFERIDO);
				query.whereIn("carregamentostatus.cdcarregamentostatus", CollectionsUtil.listAndConcatenate(lista,"cdcarregamentostatus", ","));
			}else if (new Integer(-2).equals(filtro.getCarregamentostatus().getCdcarregamentostatus())){
				List<Carregamentostatus> lista = new ArrayList<Carregamentostatus>();
				lista.add(Carregamentostatus.EM_MONTAGEM);
				lista.add(Carregamentostatus.MONTADO);
				query.whereIn("carregamentostatus.cdcarregamentostatus", CollectionsUtil.listAndConcatenate(lista,"cdcarregamentostatus", ","));
			}else
				query.where("carregamentostatus = ?", filtro.getCarregamentostatus());
		}
	}

	/**
	 * Busca o número de carregamentos abertos para um determinado box.
	 * 
	 * @author Giovane Freitas
	 * @param box
	 * @return
	 */
	public Integer getCountCarregamentos(Box box) {
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		List<Carregamentostatus> lista = new ArrayList<Carregamentostatus>();
		lista.add(Carregamentostatus.EM_MONTAGEM);
		lista.add(Carregamentostatus.MONTADO);

		return new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(Carregamento.class, "carregamento")
			.where("carregamento.deposito = ?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("box = ?", box)
			.whereIn("carregamentostatus.cdcarregamentostatus", CollectionsUtil.listAndConcatenate(lista,"cdcarregamentostatus", ","))
			.unique().intValue();	
	}
}
