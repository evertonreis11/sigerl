package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentohistorico;
import br.com.linkcom.wms.geral.bean.Carregamentohistoricotipo;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.dao.CarregamentohistoricoDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class CarregamentohistoricoService extends GenericService<Carregamentohistorico>{
	
	private CarregamentohistoricoDAO carregamentohistoricoDAO;
	
	public void setCarregamentohistoricoDAO(CarregamentohistoricoDAO carregamentohistoricoDAO) {
		this.carregamentohistoricoDAO = carregamentohistoricoDAO;
	}

	public void criaHistoricoCargasLiberadas(String whereInCarregamentos, String pedidovenda) {
		for (String cdCarregamento : whereInCarregamentos.split(",")){
			Carregamentohistorico carregamentohistorico = new Carregamentohistorico();
			Carregamento carregamento = new Carregamento(Integer.valueOf(cdCarregamento));
				carregamentohistorico.setCarregamentostatus(Carregamentostatus.FINALIZADO);
				carregamentohistorico.setCarregamento(carregamento);
				carregamentohistorico.setDescricao("Foram removidos os itens dos Pedidos (Nº): "+pedidovenda);
				carregamentohistorico.setDtaltera(new Timestamp(System.currentTimeMillis()));
				carregamentohistorico.setUsuarioaltera(WmsUtil.getUsuarioLogado());
				carregamentohistorico.setCarregamentohistoricotipo(Carregamentohistoricotipo.STATUS);
			this.saveOrUpdateNoUseTransaction(carregamentohistorico);
		}
	}

	/**
	 * 
	 * @param emSeparacao
	 * @param carregamento
	 */
	public void criaHistorico(Carregamento carregamento, String msg, Usuario usuario) {
		
		if(carregamento==null || carregamento.getCdcarregamento()==null || carregamento.getCarregamentostatus()==null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		Carregamentohistorico carregamentohistorico = new Carregamentohistorico();
			carregamentohistorico.setCarregamentostatus(carregamento.getCarregamentostatus());
			carregamentohistorico.setCarregamento(carregamento);
			carregamentohistorico.setDescricao(msg!=null?msg:carregamento.getCarregamentostatus().getDescricao());
			carregamentohistorico.setDtaltera(new Timestamp(System.currentTimeMillis()));
			carregamentohistorico.setUsuarioaltera(WmsUtil.getUsuarioLogado(usuario));
			carregamentohistorico.setCarregamentohistoricotipo(Carregamentohistoricotipo.STATUS);
		this.saveOrUpdateNoUseTransaction(carregamentohistorico);
	}

	/**
	 * 
	 * @param expedicao
	 * @return
	 */
	public List<Carregamentohistorico> findByExpedicao(Expedicao expedicao) {
		return carregamentohistoricoDAO.findByExpedicao(expedicao);
	}
	
	/* singleton */
	private static CarregamentohistoricoService instance;
	public static CarregamentohistoricoService getInstance(){
		if(instance == null){
			instance = Neo.getObject(CarregamentohistoricoService.class);
		}
		return instance;
	}

	/**
	 * 
	 * @param whereIn 
	 * @param bean
	 */
	public void deleteByCarregamento(Carregamento carregamento, String whereIn) {
		if(whereIn!=null && !whereIn.isEmpty()){
			carregamentohistoricoDAO.deleteByCarregamentoWhereIn(whereIn);	
		}else{
			carregamentohistoricoDAO.deleteByCarregamento(carregamento);
		}
	}

	/**
	 * Método que salva os históricos de Finalização de Ordens de Serviço de Carregamentos.
	 * 
	 * @author Filipe Santos
	 * @since 23/09/2014
	 */
	public void gerarHistoricoFinalizaOS(Ordemservico ordemservico, String msg, Usuario usuario) {
		if(ordemservico!=null && ordemservico.getCarregamento()!=null && ordemservico.getCarregamento().getCdcarregamento()!=null){
			ordemservico.getCarregamento().setCarregamentostatus(Carregamentostatus.EM_SEPARACAO);
			this.criaHistorico(ordemservico.getCarregamento(), Carregamentohistorico.CONFERENCIA_FINALIZADA_CHECKOUT+ordemservico.getCdordemservico(), usuario);
		}
	}

	/**
	 * 
	 * @param emSeparacao
	 * @param carregamento
	 */
	public void criaHistoricoAlteraStatus(Carregamentostatus status, Carregamento carregamento) {
		Carregamentohistorico carregamentohistorico = new Carregamentohistorico();
			carregamentohistorico.setCarregamentostatus(status);
			carregamentohistorico.setCarregamento(carregamento);
			carregamentohistorico.setDescricao("Status do Carregamento alterado: "+status.getNome());
			carregamentohistorico.setDtaltera(new Timestamp(System.currentTimeMillis()));
			carregamentohistorico.setUsuarioaltera(WmsUtil.getUsuarioLogado());
			carregamentohistorico.setCarregamentohistoricotipo(Carregamentohistoricotipo.STATUS);
		this.saveOrUpdate(carregamentohistorico);
	}

	/**
	 * 
	 * @param cdcarregamentos
	 * @return
	 */
	public List<Carregamentohistorico> findByCarregamentoPreValidados(String cdcarregamentos) {
		return carregamentohistoricoDAO.findByCarregamentoPreValidados(cdcarregamentos);
	}

	/**
	 * 
	 * @param emSeparacao
	 * @param carregamento
	 */
	public void criaHistoricoPreValidacao(Carregamento carregamento, String msg, Usuario usuario) {
		
		if(carregamento==null || carregamento.getCdcarregamento()==null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		Carregamentohistorico carregamentohistorico = new Carregamentohistorico();
			carregamentohistorico.setCarregamentostatus(Carregamentostatus.MONTADO);
			carregamentohistorico.setCarregamento(carregamento);
			carregamentohistorico.setDescricao(msg);
			carregamentohistorico.setDtaltera(new Timestamp(System.currentTimeMillis()));
			carregamentohistorico.setUsuarioaltera(WmsUtil.getUsuarioLogado(usuario));
			carregamentohistorico.setCarregamentohistoricotipo(Carregamentohistoricotipo.PRE_VALIDACAO);
		this.saveOrUpdateNoUseTransaction(carregamentohistorico);
	}
}
