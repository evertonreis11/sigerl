package br.com.linkcom.wms.geral.service;

import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Pedidovendaprodutohistorico;
import br.com.linkcom.wms.geral.bean.Pedidovendaprodutostatus;
import br.com.linkcom.wms.geral.dao.PedidovendaprodutohistoricoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PedidovendaprodutoFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidovendaprodutohistoricoService extends GenericService<Pedidovendaprodutohistorico>{
	
	private PedidovendaprodutohistoricoDAO pedidovendaprodutohistoricoDAO;
	private PedidovendaprodutoService pedidovendaprodutoService;
	private TransactionTemplate transactionTemplate;

	public void setPedidovendaprodutohistoricoDAO(PedidovendaprodutohistoricoDAO pedidovendaprodutohistoricoDAO) {
		this.pedidovendaprodutohistoricoDAO = pedidovendaprodutohistoricoDAO;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	public void setPedidovendaprodutoService(PedidovendaprodutoService pedidovendaprodutoService) {
		this.pedidovendaprodutoService = pedidovendaprodutoService;
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Pedidovendaprodutohistorico> loadHistoricoForDialog(PedidovendaprodutoFiltro filtro) {
		return pedidovendaprodutohistoricoDAO.loadHistoricoForDialog(filtro);
	}
	
	/**
	 * 
	 */
	@Override
	public ListagemResult<Pedidovendaprodutohistorico> findForListagem(FiltroListagem filtro) {
		
		ListagemResult<Pedidovendaprodutohistorico> lista = super.findForListagem(filtro);
				
		if(lista.list()!=null && !lista.list().isEmpty()){
			for (Pedidovendaprodutohistorico pvph : lista.list()) {
				try{
					String numeroPedido = pvph.getPedidovendaproduto().getPedidovenda().getNumero();
					String empresa = numeroPedido.substring(1,1);
					String pedidoMV = numeroPedido.substring(3,9);
					String lojaMV = numeroPedido.substring(11);
					pvph.getPedidovendaproduto().getPedidovenda().setNumeroPedidoMV(pedidoMV);
					pvph.getPedidovendaproduto().getPedidovenda().setNumeroLojaMV(lojaMV);
					pvph.getPedidovendaproduto().getPedidovenda().setEmpresaMV(empresa);
				}catch (Exception e) {
					continue;
				}
			}
		}
				
		return lista;
	}

	/**
	 * 
	 * @param msg
	 * @param status
	 * @param whereIn
	 * @param tipomovito 
	 */
	public void criaHistoricoStatus(String msg,	Pedidovendaprodutostatus status, String whereIn) {
		if(whereIn.split(",").length >= 1){
			for (String cdpedidovendaproduto : whereIn.split(",")){
				Pedidovendaprodutohistorico pedidovendaprodutohistorico = new Pedidovendaprodutohistorico();
					pedidovendaprodutohistorico.setCdpedidovendaprodutohistorico(null);
					pedidovendaprodutohistorico.setData(WmsUtil.currentDate());
					pedidovendaprodutohistorico.setHistorico(msg);
					pedidovendaprodutohistorico.setPedidovendaproduto(new Pedidovendaproduto(Integer.parseInt(cdpedidovendaproduto)));
					pedidovendaprodutohistorico.setPedidovendaprodutostatus(status);
					pedidovendaprodutohistorico.setUsuario(WmsUtil.getUsuarioLogado());
				this.saveOrUpdate(pedidovendaprodutohistorico);
			}
		}else{
			Pedidovendaprodutohistorico pedidovendaprodutohistorico = new Pedidovendaprodutohistorico();
				pedidovendaprodutohistorico.setCdpedidovendaprodutohistorico(null);
				pedidovendaprodutohistorico.setData(WmsUtil.currentDate());
				pedidovendaprodutohistorico.setHistorico(msg);
				pedidovendaprodutohistorico.setPedidovendaproduto(new Pedidovendaproduto(Integer.parseInt(whereIn)));
				pedidovendaprodutohistorico.setPedidovendaprodutostatus(status);
				pedidovendaprodutohistorico.setUsuario(WmsUtil.getUsuarioLogado());
			this.saveOrUpdate(pedidovendaprodutohistorico);			
		}
	}

	/**
	 * 
	 * @param msg
	 * @param status
	 * @param cdpedidovendaproduto
	 */
	public void criaHistoricoStatus(String msg, Pedidovendaprodutostatus status,Integer cdpedidovendaproduto) {
		
		Pedidovendaprodutohistorico pedidovendaprodutohistorico = new Pedidovendaprodutohistorico();
			
			pedidovendaprodutohistorico.setCdpedidovendaprodutohistorico(null);
			pedidovendaprodutohistorico.setData(WmsUtil.currentDate());
			pedidovendaprodutohistorico.setHistorico(msg);
			pedidovendaprodutohistorico.setPedidovendaproduto(new Pedidovendaproduto(cdpedidovendaproduto));
			pedidovendaprodutohistorico.setPedidovendaprodutostatus(status);
			pedidovendaprodutohistorico.setUsuario(WmsUtil.getUsuarioLogado());
			
		this.saveOrUpdate(pedidovendaprodutohistorico);
	}

	/**
	 * 
	 * @param listaPedidoVendaProduto
	 * @param carregamento 
	 */
	public void criarHistoricoPedidosForCarga(final List<Pedidovendaproduto> listaPvp, final Carregamento carregamento) {
		
		if(listaPvp!=null && !listaPvp.isEmpty()){
			
			transactionTemplate.execute(new TransactionCallback(){
				public Object doInTransaction(TransactionStatus status) {
					
					for (Pedidovendaproduto pvp : listaPvp) {
						
						Pedidovendaprodutohistorico pedidovendaprodutohistorico = new Pedidovendaprodutohistorico();
						
							pedidovendaprodutohistorico.setCdpedidovendaprodutohistorico(null);
							pedidovendaprodutohistorico.setData(WmsUtil.currentDate());
							pedidovendaprodutohistorico.setHistorico("O pedido foi vinculado a uma carga: "+carregamento.getCdcarregamento()+".");
							pedidovendaprodutohistorico.setPedidovendaproduto(pvp);
							pedidovendaprodutohistorico.setPedidovendaprodutostatus(Pedidovendaprodutostatus.EM_CARREGAMENTO);
							pedidovendaprodutohistorico.setUsuario(WmsUtil.getUsuarioLogado());
						
						saveOrUpdate(pedidovendaprodutohistorico);
						
					}
					
					return null;
				}
			});
		}
	}

	/**
	 * 
	 * @param listaConfirmacao
	 * @param carregamento 
	 */
	public void criarHistoricoPedidoForCorte(final List<Pedidovendaproduto> listaPvp, final Carregamento carregamento) {
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				
				for (Pedidovendaproduto pvp : listaPvp) {
					
					Pedidovendaprodutohistorico pedidovendaprodutohistorico = new Pedidovendaprodutohistorico();
					
						pedidovendaprodutohistorico.setCdpedidovendaprodutohistorico(null);
						pedidovendaprodutohistorico.setData(WmsUtil.currentDate());
						pedidovendaprodutohistorico.setHistorico("O pedido foi cortado da carga: "+carregamento.getCdcarregamento()+".");
						pedidovendaprodutohistorico.setPedidovendaproduto(pvp);
						pedidovendaprodutohistorico.setPedidovendaprodutostatus(Pedidovendaprodutostatus.DISPONIVEL);
						pedidovendaprodutohistorico.setUsuario(WmsUtil.getUsuarioLogado());
					
					saveOrUpdate(pedidovendaprodutohistorico);
					
				}
				
				return null;
				
			}
		});
	}
	
	/**
	 * 
	 * @param carregamento
	 */
	public void criarHistoricoForCancelamentoCarga(Carregamento carregamento){
		
		List<Pedidovendaproduto> listaPvp = pedidovendaprodutoService.findByCarregamento(carregamento);
		
		if(listaPvp!=null && !listaPvp.isEmpty()){
			for (Pedidovendaproduto pvp : listaPvp) {
				
				Pedidovendaprodutohistorico pedidovendaprodutohistorico = new Pedidovendaprodutohistorico();
				
					pedidovendaprodutohistorico.setCdpedidovendaprodutohistorico(null);
					pedidovendaprodutohistorico.setData(WmsUtil.currentDate());
					pedidovendaprodutohistorico.setHistorico("O pedido foi cancelado da carga: "+carregamento.getCdcarregamento()+".");
					pedidovendaprodutohistorico.setPedidovendaproduto(pvp);
					pedidovendaprodutohistorico.setPedidovendaprodutostatus(Pedidovendaprodutostatus.DISPONIVEL);
					pedidovendaprodutohistorico.setUsuario(WmsUtil.getUsuarioLogado());
			
				saveOrUpdate(pedidovendaprodutohistorico);
				
			}
		}
		
	}
	
}
