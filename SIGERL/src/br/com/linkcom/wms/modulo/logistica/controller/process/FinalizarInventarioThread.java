package br.com.linkcom.wms.modulo.logistica.controller.process;

import java.util.concurrent.ConcurrentHashMap;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.GenericBean;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Inventariostatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.service.InventarioService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.util.InsercaoInvalidaException;

public class FinalizarInventarioThread extends Thread{
//	Neo.getObject(OrdemservicoService.class).
	private static ConcurrentHashMap<Integer, GenericBean> mapa = new ConcurrentHashMap<Integer, GenericBean>();
	private GenericBean gb = new GenericBean();
	private Inventario inventario;
	private Inventariostatus inventariostatus;	
	private int contador;
	private InventarioService inventarioService =  Neo.getObject(InventarioService.class);
	private OrdemservicoService ordemservicoService =  Neo.getObject(OrdemservicoService.class);
	private String datasourceJNDI;
	private Deposito deposito;
	private Usuario usuario;
 
	public FinalizarInventarioThread(Inventario inventario,Inventariostatus inventariostatus, String datasourceJNDI, Deposito deposito, Usuario usuario){
		super();		
		this.inventario = inventario;
		this.inventariostatus = inventariostatus;
		this.datasourceJNDI = datasourceJNDI;
		this.deposito = deposito;
		this.usuario = usuario;
	}
	
	@Override
	public void	run(){
		try{ 
			setName("FinalizarInventario_"+datasourceJNDI+"_"+Math.round(Math.random()*8));
			contador=0;					
			inventario.setInventariostatus(Inventariostatus.AJUSTANDO_ESTOQUE);
			inventarioService.updateInventarioStatus(inventario);			
			for (Inventariolote inventariolote : inventario.getListaInventariolote()) {
				boolean houveAjusteEstoque = false;
				try {
					houveAjusteEstoque = ordemservicoService.ajustarEstoque(inventariolote,deposito,usuario);
					contador++;
					gb.setId((contador / inventario.getListaInventariolote().size())*100);					
					gb.setValue(null);
					mapa.put(inventario.getCdinventario(),gb);
				} catch (InsercaoInvalidaException e) {
					e.printStackTrace();				
					gb.setValue("Ocorreu um erro durante o Ajuste de Estoque");
					mapa.put(inventario.getCdinventario(),gb);
				}				
				if (houveAjusteEstoque)
					inventario.setInventariostatus(Inventariostatus.FINALIZADO_DIVERGENTE);
				else
					inventario.setInventariostatus(Inventariostatus.FINALIZADO_SUCESSO);								
			}
			
			// desbloqueia os endereços
			inventarioService.desbloquearEnderecos(inventario, deposito);
			
			// atualiza o status do inventario
			inventarioService.updateInventarioStatus(inventario);			
			mapa.remove(inventario.getCdinventario());
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Erro durante a execução da Thread FinalizarInventarioThread");
		}
	}	
	
	public static GenericBean getStatus(Integer cdinventario){
		return mapa.get(cdinventario);
	}
}