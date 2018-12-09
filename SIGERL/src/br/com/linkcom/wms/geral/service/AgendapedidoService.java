package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.validation.BindException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Agendapedido;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.geral.dao.AgendapedidoDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendapedidoService extends GenericService<Agendapedido> {

	private AgendapedidoDAO agendapedidoDAO;
	private AgendaparcialService agendaparcialService;
	private PedidocompraprodutoService pedidocompraprodutoService;
	
	public void setAgendapedidoDAO(AgendapedidoDAO agendapedidoDAO) {
		this.agendapedidoDAO = agendapedidoDAO;
	}
	
	public void setAgendaparcialService(AgendaparcialService agendaparcialService) {
		this.agendaparcialService = agendaparcialService;
	}
	
	public void setPedidocompraprodutoService(PedidocompraprodutoService pedidocompraprodutoService) {
		this.pedidocompraprodutoService = pedidocompraprodutoService;
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.AgendapedidoDAO#findByAgenda(Agenda)
	 * @param form
	 * @return
	 */
	public List<Agendapedido> findByAgenda(Agenda form,String ... campos) {
		return agendapedidoDAO.findByAgenda(form,campos);
	}
	
	/**
	 * Seta os pedidoscompras com pedidos compra produto
	 * de maneira diferentes caso o agendapedido seja parcial ou não
	 * @author Leonardo Guimarães
	 * @param form
	 * @return
	 */
	public void findListaAgendaPedido(Agenda form) {
		if(form  == null){
			throw new WmsException("A agenda não deve ser nulo");
		}
		List<Agendapedido> list = findByAgenda(form);		
		List<Agendapedido> listaaagendapedidoaux = new ArrayList<Agendapedido>();
		List<Agendaparcial> listaAgendaParcial = new ArrayList<Agendaparcial>();
		if(form.getListaagendaparcial() != null){
			for (Agendapedido agendapedido : list) {
				Pedidocompra pedidocompra= new Pedidocompra();
				pedidocompra = agendapedido.getPedidocompra();
				
				listaAgendaParcial = null;
				if(agendapedido.getParcial()){
					listaAgendaParcial = agendaparcialService.findByAgendaPedido(agendapedido);
				}
				
				pedidocompra.setListapedidocompraproduto(new ListSet<Pedidocompraproduto>(Pedidocompraproduto.class,pedidocompraprodutoService.makeListaPedidoCompraProduto(pedidocompra, listaAgendaParcial)));
				
				List<Pedidocompraproduto> lista = pedidocompraprodutoService.findNotIncluded(pedidocompra,form);
				
				agendapedido.setPermitirIncluirProduto(lista != null && lista.size() > 0);
				agendapedido.setPedidocompra(pedidocompra);
				listaaagendapedidoaux.add(agendapedido);
			}
			form.setListaagendapedido(new ListSet<Agendapedido>(Agendapedido.class,listaaagendapedidoaux));
		}
	}
	
	/**
	 * Método que faz validação da quantidade de produtos e 
	 * adciona uma mensagem de erro
	 * @author Leonardo Guimarães
	 * @param pedidocompraproduto
	 * @param errors
	 * @return
	 */
	public boolean doValidation(Pedidocompraproduto pedidocompraproduto, BindException errors) {
		if(pedidocompraproduto == null || errors == null){
			throw new WmsException("Pedidocompraproduto e errors não devem ser nulos");
		} 
		Boolean aux = Boolean.FALSE;
		if(pedidocompraproduto.getQtde() > pedidocompraproduto.getQtdedisponivel()){
			if(pedidocompraproduto.getQtdedisponivel().equals(pedidocompraproduto.getQtdetotal())){
				errors.reject("001","O valor informado para a quantidade agendada para o produto \"" +
						pedidocompraproduto.getProduto().getDescricao()+
				"\" é maior que a quantidade cadastrada no pedido.");
			}
			else{errors.reject("001","O produto \""+pedidocompraproduto.getProduto().getDescricao()+"\" do pedido \""
							  +pedidocompraproduto.getPedidocompra().getNumero()+"\" já participou de um agendamento anterior, " +
							  "portanto a quantidade agendada deve ser no máximo \""+pedidocompraproduto.getQtdedisponivel()	+"\".");
			}
			aux = Boolean.TRUE;
		}
		else{
			if(pedidocompraproduto.getQtde() < 0){
				errors.reject("001","A quantidade fornecida no produto \""+pedidocompraproduto.getProduto().getDescricao()+"\" não pode ser menor que zero.");
				aux = Boolean.TRUE;
			}else if(pedidocompraproduto.getQtde() == 0){
				errors.reject("001","O valor do campo quantidade deve ser maior que 0.");
				aux = Boolean.TRUE;
			}
				
		}
		return aux;
	}
	
	/**
	 * Prepara uma lista de agendapedido ultilizando os 
	 * cds pedido compra contidos na string parameter
	 * @author Leonardo Guimarães
	 * @param parameter
	 * @return
	 */
	public List<Agendapedido> getListaAgendaPedido(String parameter,String[] parciais) {
		List<Agendapedido> listaAgendaPedido = new ArrayList<Agendapedido>();
		if(parameter != null && !parameter.equals("")){
			String[] pedidos = parameter.split(",");
			for (String string : pedidos) {
				Pedidocompra pedidocompra = new Pedidocompra();
				pedidocompra.setCdpedidocompra(Integer.valueOf(string));
				Agendapedido agendapedido = new Agendapedido();
				agendapedido.setPedidocompra(pedidocompra);
				agendapedido.setParcial(Boolean.FALSE);
				if(parciais != null)
					for (String cdparcial : parciais) {
						if(pedidocompra.getCdpedidocompra() == Integer.parseInt(cdparcial)){
							agendapedido.setParcial(Boolean.TRUE);
							break;
						}
					}
				
				listaAgendaPedido.add(agendapedido);
			}
		}
		return listaAgendaPedido;
	}
	
	/**
	 * Cria uma lista de agenda pedido através de uma lista de pedidocompra e 
	 * seta a listapedidocompraproduto dos pedidos de compra
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.service.PedidocompraprodutoService#makeListaPedidoCompraProduto(Pedidocompra pedidocompra)
	 * @param listaPedidocompra
	 * @return
	 */
	public List<Agendapedido> makeListaAgendaPedido(List<Pedidocompra> listaPedidocompra) {
		if(listaPedidocompra == null){
			throw new WmsException("A listaPedidocompra não deve ser nulo");
		}
		List<Agendapedido> listaAgendaPedido = new ArrayList<Agendapedido>();
		for (Pedidocompra pedidocompra : listaPedidocompra) {
			Agendapedido agendapedido = new Agendapedido();
			pedidocompra.setListapedidocompraproduto(new ListSet<Pedidocompraproduto>(Pedidocompraproduto.class,pedidocompraprodutoService.makeListaPedidoCompraProduto(pedidocompra, null)));
			
			Iterator<Pedidocompraproduto> iterator = pedidocompra.getListapedidocompraproduto().iterator();
			while (iterator.hasNext()){
				Pedidocompraproduto pc = iterator.next();
				if (pc.getQtdedisponivel().intValue() <= 0)
					iterator.remove();//este item não foi agendado
			}
			
			agendapedido.setPedidocompra(pedidocompra);
			listaAgendaPedido.add(agendapedido);
		}
		return listaAgendaPedido;
	}

	public boolean existeAgendamentoParcial(Pedidocompra pedidocompra) {
		return agendapedidoDAO.existeAgendamentoParcial(pedidocompra);
	}
	
	/* singleton */
	private static AgendapedidoService instance;
	public static AgendapedidoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(AgendapedidoService.class);
		}
		
		return instance;
	}
	
	public Agendapedido findByPedidocompra(Pedidocompra pedidocompra){
		return agendapedidoDAO.findByPedidocompra(pedidocompra);
	}
	
	public void updateTrocarParaParcial(Pedidocompra pedidocompra, Agenda agenda) {
		agendapedidoDAO.updateTrocarParaParcial(pedidocompra, agenda);
	}
}
