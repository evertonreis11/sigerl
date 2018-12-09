package br.com.linkcom.wms.geral.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.geral.bean.Pedidocompraprodutolibera;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.dao.PedidocompraprodutoDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class PedidocompraprodutoService extends GenericService<Pedidocompraproduto>{

	private PedidocompraprodutoDAO pedidocompraprodutoDAO;
	private AgendaparcialService agendaparcialService;
	private PedidocompraService pedidocompraService;
	private PedidocompraprodutoliberaService pedidocompraprodutoliberaService;
	
	public void setPedidocompraprodutoDAO(PedidocompraprodutoDAO pedidocompraprodutoDAO) {
		this.pedidocompraprodutoDAO = pedidocompraprodutoDAO;
	}
	
	public void setAgendaparcialService(AgendaparcialService agendaparcialService) {
		this.agendaparcialService = agendaparcialService;
	}
	
	public void setPedidocompraService(PedidocompraService pedidocompraService) {
		this.pedidocompraService = pedidocompraService;
	}
	
	public void setPedidocompraprodutoliberaService(
			PedidocompraprodutoliberaService pedidocompraprodutoliberaService) {
		this.pedidocompraprodutoliberaService = pedidocompraprodutoliberaService;
	}
	
	/**
	 * Cria um lista de pedido compra produto
	 * @see br.com.linkcom.wms.geral.service.PedidocompraprodutoService#findByPedidoCompra(Pedidocompra pedidocompra)
	 * @see br.com.linkcom.wms.geral.service.AgendaparcialService#findByPedidoCompraProduto(Pedidocompraproduto pedidocompraproduto)
	 * @author Leonardo Guimarães
	 * @param pedidocompra
	 * @param excluirItensCompletos 
	 * @return
	 */
	public List<Pedidocompraproduto> makeListaPedidoCompraProduto(Pedidocompra pedidocompra, List<Agendaparcial> listaParcial){
		
		List<Pedidocompraproduto> listapedidocompraproduto = this.findByPedidoCompra(pedidocompra);
		
		if(listaParcial != null && !listaParcial.isEmpty()){
			Iterator<Pedidocompraproduto> iterator = listapedidocompraproduto.iterator();
			while (iterator.hasNext()){
				Pedidocompraproduto pcp = iterator.next();
				boolean achou = false;
				for (Agendaparcial agendaparcial : listaParcial){ 
					if(agendaparcial.getPedidocompraproduto().equals(pcp)){
						achou = true;
						break;
					}
				}
				if(!achou)
					iterator.remove();
			}
		}
		
		for (Pedidocompraproduto pcp : listapedidocompraproduto) {
			pcp.setQtdetotal(pcp.getQtde());
			
			Integer qtdeAgendada = agendaparcialService.getQtdeAgendada(pcp);
			pcp.setQtdedisponivel(pcp.getQtdeliberada() - qtdeAgendada);
			pcp.setQtde(qtdeAgendada);
			
			if(listaParcial != null){
				for (Agendaparcial ap : listaParcial) {
					if(ap.getPedidocompraproduto().getCdpedidocompraproduto().equals(pcp.getCdpedidocompraproduto())){
						pcp.setQtde(ap.getQtde());
						pcp.setQtdedisponivel(pcp.getQtdedisponivel() + ap.getQtde());
					}
				}
			}
		}

		return listapedidocompraproduto;
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraprodutoDAO#findByPedidoCompra(Pedidocompra pedidocompra)
	 * @param pedidocompra
	 * @return
	 */
	public List<Pedidocompraproduto> findByPedidoCompra(Pedidocompra pedidocompra){
		return pedidocompraprodutoDAO.findByPedidoCompra(pedidocompra);
	}
	
	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraprodutoDAO#findbyagendaparcial
	 * @author Leonardo Guimarães
	 * @param listaagendaparcial
	 * @return
	 */
	public Pedidocompraproduto findByAgendaParcial(Agendaparcial agendaparcial){
		return pedidocompraprodutoDAO.findByAgendaParcial(agendaparcial);
	}
	
	/**
	 * Cria uma lista de pedidoCompraProduto e seta as quantidades
	 * @see br.com.linkcom.wms.geral.service.PedidocompraprodutoService#findByAgendaParcial(Agendaparcial agendaparcial)
	 * @author Leonardo Guimarães
	 * @param listAgendaParcial
	 * @return
	 */
	public List<Pedidocompraproduto> makeListaPedidoCompraProduto(List<Agendaparcial> listAgendaParcial){
		if(listAgendaParcial == null){
			throw new WmsException("A listAgendaParcial não deve ser nulo");
		}
		List<Pedidocompraproduto> listaPedidoCompraProduto = new ArrayList<Pedidocompraproduto>();
		for (Agendaparcial agendaparcial : listAgendaParcial) {
			Pedidocompraproduto pedidocompraproduto = this.findByAgendaParcial(agendaparcial);			
			pedidocompraproduto.setQtdetotal(pedidocompraproduto.getQtdeliberada());
			pedidocompraproduto.setQtde(this.getQuantidadedisponivel(pedidocompraproduto));
			pedidocompraproduto.setQtdedisponivel(pedidocompraproduto.getQtde()+agendaparcial.getQtde());
			pedidocompraproduto.setQtde(agendaparcial.getQtde());
			listaPedidoCompraProduto.add(pedidocompraproduto);
		}
		return listaPedidoCompraProduto;
	}
	
	/**
	 * Seta a quantidade do pedidoCompraProduto
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.service.PedidocompraService.isParcial(Pedidocompra pedidocompra)
	 * @param pedidocompraproduto
	 */
	public Integer getQuantidadedisponivel(Pedidocompraproduto pedidocompraproduto){
		Integer soma = pedidocompraproduto.getQtdeliberada() != null ? pedidocompraproduto.getQtdeliberada() : 0;
		if(pedidocompraproduto == null){
			throw new WmsException("O pedidocompraproduto não deve ser nulo");
		}
		List<Agendaparcial> listaagendaparcial = agendaparcialService.findByPedidoCompraProduto(pedidocompraproduto);
		if(pedidocompraService.isParcial(pedidocompraproduto.getPedidocompra())){
			if(!listaagendaparcial.isEmpty()){
				for (Agendaparcial agendaparcial : listaagendaparcial) {
					if(agendaparcial.getQtde() != null){
						soma = soma - agendaparcial.getQtde();
					}
				}
			}			
		}

		return soma;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraprodutoDAO#findByCd(Pedidocompraproduto pedidocompraproduto)
	 * @param pedidocompraproduto
	 * @return
	 */
	public Pedidocompraproduto findByCd(Pedidocompraproduto pedidocompraproduto) {
		return pedidocompraprodutoDAO.findByCd(pedidocompraproduto);
	}	
	
	/* singleton */
	private static PedidocompraprodutoService instance;
	public static PedidocompraprodutoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidocompraprodutoService.class);
		}
		return instance;
	}

	/**
	 * Retorna o número de itens que um pedido possui.
	 * 
	 * @author Giovane Freitas
	 * @param pedidocompra
	 * @return
	 */
	public long getCountItens(Pedidocompra pedidocompra) {
		return pedidocompraprodutoDAO.getCountItens(pedidocompra);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param pedidocompra
	 * @param agenda
	 * @return
	 */
	public List<Pedidocompraproduto> findNotIncluded(Pedidocompra pedidocompra,Agenda agenda) {
		return pedidocompraprodutoDAO.findNotIncluded(pedidocompra,agenda);
	}

	/**
	 * Busca os produtos dos predidos de compra e seta suas quantidades 
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #findByPedidoCompra(Pedidocompra)
	 * 
	 * @param pedidocompra
	 * @return
	 */
	public List<Pedidocompraproduto> getListaPedidoCompraWithQtdes(Pedidocompra pedidocompra) {
		List<Pedidocompraproduto> listaPedidoCompraProduto = this.findByPedidoCompra(pedidocompra);
		for (Pedidocompraproduto pedidocompraproduto : listaPedidoCompraProduto) {
			pedidocompraproduto.setQtdedisponivel(pedidocompraproduto.getQtdeliberada());
			pedidocompraproduto.setQtdetotal(pedidocompraproduto.getQtdeliberada());
		}
		return listaPedidoCompraProduto;
	}

	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidocompraprodutoDAO#updateLiberacaoTotal
	 * 
	 * @param pcp
	 * @author Rodrigo Freitas
	 * @param dtprevisaorecebimento 
	 */
	public void insertLiberacaoTotal(Pedidocompraproduto pcp, Date dtprevisaorecebimento, Date dtprevisaofinanceiro) {
		Pedidocompraprodutolibera pedidocompraprodutolibera = new Pedidocompraprodutolibera();
		
		pedidocompraprodutolibera.setDtprevisaorecebimento(dtprevisaorecebimento);
		pedidocompraprodutolibera.setDtprevisaofinanceiro(dtprevisaofinanceiro);
		pedidocompraprodutolibera.setPedidocompraproduto(pcp);
		pedidocompraprodutolibera.setQtdeliberada(pcp.getQtde());
		
		pedidocompraprodutoliberaService.saveOrUpdate(pedidocompraprodutolibera);
	}

	/**
	 * Faz referência ao DAO. 
	 * 
	 *
	 * @param pcp
	 * @param qtdeliberada
	 * @author Rodrigo Freitas
	 * @param previsaoRecebimento 
	 */
	public void insertLiberacaoParcial(Pedidocompraproduto pcp, Integer qtdeliberada, Date previsaoRecebimento, Date dtprevisaofinanceiro) {
		Pedidocompraprodutolibera pedidocompraprodutolibera = new Pedidocompraprodutolibera();
		
		pedidocompraprodutolibera.setDtprevisaofinanceiro(dtprevisaofinanceiro);
		pedidocompraprodutolibera.setDtprevisaorecebimento(previsaoRecebimento);
		pedidocompraprodutolibera.setPedidocompraproduto(pcp);
		pedidocompraprodutolibera.setQtdeliberada(qtdeliberada);
		
		pedidocompraprodutoliberaService.saveOrUpdate(pedidocompraprodutolibera);
	}

	
	
	/**
	 * Método que busca uma lista de pedidocompraproduto através de uma lista de CDPEDIDOCOMPRAPRODUTO
	 * @author Giovane Freitas
	 * @return
	 */
	public Pedidocompraproduto findForEdicaoAgenda(Integer cdpedidocompraproduto){
		return pedidocompraprodutoDAO.findForEdicaoAgenda(cdpedidocompraproduto);
	}

	/**
	 * Verifica se a quantidade total de um pedido de compra já foi liberada para agendamento.
	 * 
	 * @author Giovane Freitas
	 * @param pedidocompra
	 * @return
	 */
	public boolean isLiberadoIntegral(Pedidocompra pedidocompra){
		return pedidocompraprodutoDAO.isLiberadoIntegral(pedidocompra);
	}

	/**
	 * Busca o valor montante já liberado para uma determinada classe de produto em um determinado mês.
	 * 
	 * @author Giovane Freitas
	 * @param numeroClassePrincipal
	 * @param dtprevisaorecebimento
	 * @return
	 */
	public Money getValorLiberado(Deposito deposito, String numeroClassePrincipal, Date mesReferencia) {
		return pedidocompraprodutoDAO.getValorLiberado(deposito, numeroClassePrincipal, mesReferencia);
	}
	public Money getValorLiberadoFinanceiro(Deposito deposito, String numeroClassePrincipal, Date mesReferencia, Date dtprevisaoFinaceiro) {
		return pedidocompraprodutoDAO.getValorLiberadoFinanceiro(deposito, numeroClassePrincipal, mesReferencia, dtprevisaoFinaceiro);
	}
	
	/**
	 * Faz referência ao DAO.
	 * @param pedidocompraproduto
	 * @author Taidson
	 */
	public Pedidocompraproduto loadPedidoCompraproduto(Pedidocompraproduto pedidocompraproduto){
		return pedidocompraprodutoDAO.loadPedidoCompraproduto(pedidocompraproduto);
	}
	
	/**
	 * Faz referência ao DAO.
	 * @param pedidocompraproduto
	 * @author Taidson
	 */
	public void saveOrUpdateEditaPedidocompraLiberado(Pedidocompraproduto pedidocompraproduto) {
		pedidocompraprodutoDAO.saveOrUpdateEditaPedidocompraLiberado(pedidocompraproduto);
	}
	
	public Boolean verificaProdutoAgenda(Produto produto, Agenda agenda, Pedidocompra pedidocompra) {
		return pedidocompraprodutoDAO.verificaProdutoAgenda(produto, agenda, pedidocompra);
	}
	
	public Boolean verificaPedidoNotaAgendamento(Agenda agenda, Pedidocompra pedidocompra) {
		return pedidocompraprodutoDAO.verificaPedidoNotaAgendamento(agenda, pedidocompra);
	}
	
	public List<Pedidocompraproduto> findPedidoProdutos(Agenda agenda){
		return pedidocompraprodutoDAO.findPedidoProdutos(agenda);
	}
	
	public Pedidocompraproduto findByPedidocompraProduto(Pedidocompra pedidocompra, Produto produto) {
		return pedidocompraprodutoDAO.findByPedidocompraProduto(pedidocompra, produto);
	}
	
	/*public void setQtdeTotal(List<Pedidocompra> listaPedidocompra){
		Map<Pedidocompra, List<Produto>> mapa = new HashMap<Pedidocompra, List<Produto>>();
		if(listaPedidocompra != null && listaPedidocompra.size() > 0){
			for (Pedidocompra pedidocompra : listaPedidocompra) {
				List<Pedidocompraproduto> listaPedidocompraproduto = this.findByPedidoCompra(pedidocompra);
				for (Pedidocompraproduto pedidocompraproduto : listaPedidocompraproduto) {
					List<Produto> listaProduto = new ArrayList<Produto>();
					if(mapa.containsKey(pedidocompra)){
						listaProduto = mapa.get(pedidocompra);
						listaProduto.add(pedidocompraproduto.getProduto());
					}else{
						listaProduto.add(pedidocompraproduto.getProduto());
					}
					mapa.put(pedidocompraproduto.getPedidocompra(), listaProduto);
				
				}
		//		pedidocompra.setQtdetotal(pedidocompraprodutoDAO.getQtdeTotal(pedidocompra));
			}
			
			for (Pedidocompra item : listaPedidocompra) {
				item.setQtdetotal((int) this.getQtdeTotal(item, WmsUtil.concatenateWithLimit(mapa.get(item), "cdproduto", mapa.get(item).size())));
				item.setQtdeliberada((int) this.getQtdeLiberada(item, WmsUtil.concatenateWithLimit(mapa.get(item), "cdproduto", mapa.get(item).size())));
			}
			
		}
	}*/
	
/*	public void setValoresPedicompra(List<Pedidocompra> listaPedidocompra){
		Map<Pedidocompra, List<Produto>> mapa = new HashMap<Pedidocompra, List<Produto>>();
		if(listaPedidocompra != null && listaPedidocompra.size() > 0){
			for (Pedidocompra pedidocompra : listaPedidocompra) {
				List<Pedidocompraproduto> listaPedidocompraproduto = this.findByPedidoCompra(pedidocompra);
				for (Pedidocompraproduto pedidocompraproduto : listaPedidocompraproduto) {
					List<Produto> listaProduto = new ArrayList<Produto>();
					if(mapa.containsKey(pedidocompra)){
						listaProduto = mapa.get(pedidocompra);
						listaProduto.add(pedidocompraproduto.getProduto());
					}else{
						listaProduto.add(pedidocompraproduto.getProduto());
					}
					mapa.put(pedidocompraproduto.getPedidocompra(), listaProduto);
				}
				
				
				for (Pedidocompra item : listaPedidocompra) {
					item.setQtdetotal((int) this.getQtdeTotal(item, WmsUtil.concatenateWithLimit(mapa.get(item), "cdproduto", mapa.get(item).size())));
					item.setQtdeliberada((int) this.getQtdeLiberada(item, WmsUtil.concatenateWithLimit(mapa.get(item), "cdproduto", mapa.get(item).size())));
					item.setQtdeagenda(qtdedisponivel);
					item.setQtdedisponivel(qtdedisponivel);
				}
				
			}
		}
	}*/
	
	
	public long getQtdeTotal(Pedidocompra pedidocompra, String whereIn) {
		return pedidocompraprodutoDAO.getQtdeTotal(pedidocompra, whereIn);
	}
	
	public long getQtdeLiberada(Pedidocompra pedidocompra, String whereIn) {
		return pedidocompraprodutoDAO.getQtdeLiberada(pedidocompra, whereIn);
	}
	
//	/**
//	 * Método que verifica se o pedidoCompraProduto foi agendadado parcialmente
//	 * 
//	 * @param List<Pedidocompraproduto>
//	 * @param Agenda
//	 * @return List<Pedidocompraproduto>
//	 * @author Thiago Augusto
//	 */
//	public List<Pedidocompraproduto> verificaListaPedidoCompraProduto(List<Pedidocompraproduto> lista, Agenda agenda){
//		List<Pedidocompraproduto> retorno = new ArrayList<Pedidocompraproduto>();
//		for (Pedidocompraproduto pedidocompraproduto : lista) {
//			retorno.add(pedidocompraprodutoDAO.findByPedidocompraproduto(pedidocompraproduto, agenda));
//		}
//		return retorno;
//	}
}
