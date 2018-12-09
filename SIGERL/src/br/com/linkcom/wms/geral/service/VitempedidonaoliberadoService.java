package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.view.Vitempedidonaoliberado;
import br.com.linkcom.wms.geral.dao.VitempedidonaoliberadoDAO;
import br.com.linkcom.wms.util.WmsUtil;

public class VitempedidonaoliberadoService extends GenericService<Vitempedidonaoliberado> {

	private VitempedidonaoliberadoDAO vitempedidonaoliberadoDAO;
	private CarregamentoService carregamentoService;
	private PedidovendaprodutoService pedidovendaprodutoService;
	private TransactionTemplate transactionTemplate;
	private CarregamentohistoricoService carregamentohistoricoService;
	
	public void setVitempedidonaoliberadoDAO(VitempedidonaoliberadoDAO vitempedidonaoliberadoDAO) {
		this.vitempedidonaoliberadoDAO = vitempedidonaoliberadoDAO;
	}
	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}
	public void setPedidovendaprodutoService(PedidovendaprodutoService pedidovendaprodutoService) {
		this.pedidovendaprodutoService = pedidovendaprodutoService;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	public void setCarregamentohistoricoService(CarregamentohistoricoService carregamentohistoricoService) {
		this.carregamentohistoricoService = carregamentohistoricoService;
	}
	
	/**
	 * Método que libera pedidos e muda o status do carregamento
	 * 
	 * @param whereInPedidoVendaProduto
	 * @author Tomás Rabelo
	 * @param whereInCarregamentos 
	 */
	public void liberarPedidos(final String whereInPedidoVendaProduto, final String whereInCarregamentos, final String motivoLiberacao) {
		
		List<Pedidovendaproduto> listaPvp = pedidovendaprodutoService.findByIds(whereInPedidoVendaProduto);
		final List<Pedidovendaproduto> listaPvpNovo = new ArrayList<Pedidovendaproduto>();
		final List<String> numeroPv = new ArrayList<String>();
		final StringBuilder numeroPedidovenda = new StringBuilder();

		for (Pedidovendaproduto pedidovendaproduto : listaPvp){
			if(pedidovendaproduto.getPedidovenda()!=null && pedidovendaproduto.getPedidovenda().getNumero()!=null){
				if(!numeroPv.contains(pedidovendaproduto.getPedidovenda().getNumero())){
					numeroPv.add(pedidovendaproduto.getPedidovenda().getNumero());
					numeroPedidovenda.append(pedidovendaproduto.getPedidovenda().getNumero()).append(", ");
				}
			}
			Pedidovendaproduto pvp = new Pedidovendaproduto(pedidovendaproduto);
			listaPvpNovo.add(pvp);
		}
		
		final String msg = "Itens Removidos da Carga. Motivo: "+motivoLiberacao+". Pedidos: "+numeroPedidovenda.substring(0,numeroPedidovenda.length()-2);
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				vitempedidonaoliberadoDAO.marcarPedidoVendaProduto(whereInPedidoVendaProduto);
				
				for (Pedidovendaproduto pedidovendaproduto : listaPvpNovo) {
					pedidovendaprodutoService.saveOrUpdate(pedidovendaproduto);
				}
				
				return null;
			}
		});
		
		/*
		 *	Filipe Santos, 23/06/2015
		 *	Trecho de código removido da Transação. Motivo: A PROC, por alguma razão, não é executada! 
		 */
		for (String cdCarregamento : whereInCarregamentos.split(",")){ 
			Carregamento carregamento = new Carregamento(Integer.valueOf(cdCarregamento));
			carregamentoService.atualizarListaRotas(carregamento);
			carregamento.setCarregamentostatus(Carregamentostatus.FINALIZADO);
			carregamentohistoricoService.criaHistorico(carregamento,msg,WmsUtil.getUsuarioLogado());
		}
		
	}
}
