package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Agendapedido;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.geral.dao.AgendaparcialDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendaparcialService extends GenericService<Agendaparcial> {
	
	private AgendaparcialDAO agendaparcialDAO;
	private AgendapedidoService agendapedidoService;
	private PedidocompraprodutoService pedidocompraprodutoService;
	
	public void setAgendaparcialDAO(AgendaparcialDAO agendaparcialDAO) {
		this.agendaparcialDAO = agendaparcialDAO;
	}
	
	public void setAgendapedidoService(AgendapedidoService agendapedidoService) {
		this.agendapedidoService = agendapedidoService;
	}
	
	public void setPedidocompraprodutoService(PedidocompraprodutoService pedidocompraprodutoService) {
		this.pedidocompraprodutoService = pedidocompraprodutoService;
	}

	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.AgendaparcialDAO#findByPedidoCompraProduto(Pedidocompraproduto)
	 * @author Leonardo Guimarães
	 * @param pedidocompraproduto
	 * @return
	 */
	public List<Agendaparcial> findByPedidoCompraProduto(Pedidocompraproduto pedidocompraproduto) {
		return agendaparcialDAO.findByPedidoCompraProduto(pedidocompraproduto);
	}
	
	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.AgendaparcialDAO#findByAgendaPedido(Agendapedido)
	 * @author Leonardo Guimarães
	 * @param agendapedido
	 * @return
	 */
	public List<Agendaparcial> findByAgendaPedido(Agendapedido agendapedido) {
		return agendaparcialDAO.findByAgendaPedido(agendapedido);
	}
	
	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.AgendaparcialDAO#findByAgendaPedido(Agendapedido)
	 * @see br.com.linkcom.neo.service.GenericService#findBy(Object o, String... extraFields)
	 * @author Leonardo Guimarães 
	 * @param pedidocompra
	 * @return
	 */
	public List<Agendaparcial> findByPedidocompra(Pedidocompra pedidocompra) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null){
			throw new WmsException("pedidocompra e cdpedidocompra não devem ser nulos");
		}
		List<Agendapedido> listaagendapedido = agendapedidoService.findBy(pedidocompra,"agenda.cdagenda","pedidocompra.cdpedidocompra");
		List<Agendaparcial> listaagendaparcial=new ArrayList<Agendaparcial>();
		if(listaagendapedido != null && !listaagendapedido.isEmpty()){
			for (Agendapedido agendapedido : listaagendapedido) {
				List<Agendaparcial> listaagendaparcialaux = agendaparcialDAO.findByAgendaPedido(agendapedido);
				for (Agendaparcial agendaparcialaux : listaagendaparcialaux) {
					listaagendaparcial.add(agendaparcialaux);
				}
			}
		}				
		return listaagendaparcial;
	}
	
	public List<Agendaparcial> preparaListaAgendaParcial(Agenda bean) {
		List<Agendaparcial> listaAgendaParcial = new ArrayList<Agendaparcial>();
		if(bean != null && bean.getListaagendapedido() != null){
			int length = 0;
			for (Agendapedido agendapedido : bean.getListaagendapedido()) {
				agendapedido.setParcial(Boolean.FALSE);
				for (Pedidocompraproduto pedidocompraproduto : agendapedido.getPedidocompra().getListapedidocompraproduto()) {
					Agendaparcial agendaparcial = new Agendaparcial();
					agendaparcial.setAgenda(bean);
					agendaparcial.setQtde(pedidocompraproduto.getQtde());
					agendaparcial.setPedidocompraproduto(pedidocompraproduto);
					if(!agendapedido.getParcial() && !pedidocompraproduto.getQtde().equals(pedidocompraproduto.getQtdetotal())){
						agendapedido.setParcial(Boolean.TRUE);
					}
					listaAgendaParcial.add(agendaparcial);
				}

				if(listaAgendaParcial.size() - length < pedidocompraprodutoService.getCountItens(agendapedido.getPedidocompra())){
					agendapedido.setParcial(Boolean.TRUE);
				}
				
				length = listaAgendaParcial.size();
			}			
		}else {
			throw new WmsException("A agenda não deve ser nula");
		}
		return listaAgendaParcial;
	}
	
	/**
	 * Prepara uma lista de agendas parciais com os produtos incluídos e suas respectivas quantidades
	 * @see br.com.linkcom.wms.geral.service.PedidocompraprodutoService#findByCd(Pedidocompraproduto pedidocompraproduto)
	 * @author Leonardo Guimarães
	 * @param produtosIncluidos
	 * @param quantidades
	 * @return
	 */
	public List<Agendaparcial> getListaAgendaParcial(String[] produtosIncluidos, String[] quantidades) {
		if(produtosIncluidos == null || quantidades == null){
			throw new WmsException("ProdutosIncluidos e quantidades não devem ser nulos");
		}
		List<Agendaparcial> listaAgendaParcial = new ArrayList<Agendaparcial>();
		for(int i = 0; i < produtosIncluidos.length; i++) {
			Agendaparcial agendaparcial = new Agendaparcial();
			Pedidocompraproduto pedidocompraproduto = new Pedidocompraproduto();
			pedidocompraproduto.setCdpedidocompraproduto(Integer.parseInt(produtosIncluidos[i]));
			pedidocompraproduto = pedidocompraprodutoService.findByCd(pedidocompraproduto);
			agendaparcial.setPedidocompraproduto(pedidocompraproduto);
			agendaparcial.setQtde(Integer.parseInt(quantidades[i]));
			if(agendaparcial.getQtde() > pedidocompraprodutoService.getQuantidadedisponivel(pedidocompraproduto)){
				throw new WmsException("O valor do campo quantidade é maior que o permitido");
			}
			listaAgendaParcial.add(agendaparcial);
		}
		return listaAgendaParcial;
	}
	
	/**
	 * Método de refenrência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 *
	 * @see br.com.linkcom.wms.geral.dao.AgendaparcialDAO.findByAgendaPedido(Agenda agenda)
	 * 
	 * @param agenda
	 * @return
	 */
	public List<Agendaparcial> findByAgenda(Agenda agenda) {
		return agendaparcialDAO.findByAgendaPedido(agenda);
	}
	
	/**
	 * Salva os novos registros de agenda parcial
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param agenda
	 * @param pedidocompra
	 */
	public void saveAgendasParciais(Agenda agenda, Pedidocompra pedidocompra) {
		
		if(pedidocompra != null && pedidocompra.getListapedidocompraproduto() != null)
			for(Pedidocompraproduto pcp : pedidocompra.getListapedidocompraproduto()){
				if(pcp.getIncluded() != null && pcp.getIncluded()){
					Agendaparcial agendaparcial = new Agendaparcial();
					agendaparcial.setAgenda(agenda);
					agendaparcial.setPedidocompraproduto(pcp);
					agendaparcial.setQtde(pcp.getQtde());
					saveOrUpdate(agendaparcial);
				}
			}
		
	}

	/**
	 * Faz referência ao DAO. 
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AgendaparcialDAO#getQtdeAgendada
	 *
	 * @param pcp
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Integer getQtdeAgendada(Pedidocompraproduto pcp) {
		return agendaparcialDAO.getQtdeAgendada(pcp);
	}

	/**
	 * Metódo com referência no DAO
	 * 
	 * @param agenda
	 * @param pedidocompraproduto
	 * @author Tomás Rabelo
	 */
	public void deleteAgendaParcial(Agenda agenda, Pedidocompraproduto pedidocompraproduto) {
		agendaparcialDAO.deleteAgendaParcial(agenda, pedidocompraproduto);
	}	

	/* singleton */
	private static AgendaparcialService instance;
	public static AgendaparcialService getInstance() {
		if(instance == null){
			instance = Neo.getObject(AgendaparcialService.class);
		}
		return instance;
	}

	public void insertTrocarParaParcial(Pedidocompra pedidocompra, Agenda agenda, Integer qtde) {
		agendaparcialDAO.insertTrocarParaParcial(pedidocompra, agenda, qtde);
	}
	
	public List<Agendaparcial> findItensReduzirQtdeAgendada(Pedidocompraproduto pedidocompraproduto) {
		return agendaparcialDAO.findItensReduzirQtdeAgendada(pedidocompraproduto);
	}
	
	public void updateAgendaParcial(Agendaparcial agendaparcial) {
		agendaparcialDAO.updateAgendaParcial(agendaparcial);
	}

	/**
	 * Verifica se um List&lt;AgendaParcial&gt; contem uma agendaparcial com o pedidocompraproduto pcp.
	 * @param listaagendaparcial
	 * @param pcp
	 * @return
	 */
	public static boolean listaContemPCP(Set<Agendaparcial> listaagendaparcial, Pedidocompraproduto pcp) {
		if(pcp==null || pcp.getCdpedidocompraproduto()==null)
			throw new WmsException("Paramêtro PCP em AgendaService#listaContemPCP é nulo.");
		if(listaagendaparcial==null) return false;
		for (Agendaparcial agendaparcial : listaagendaparcial) {
			if(pcp.equals(agendaparcial.getPedidocompraproduto()))
				return true;
		}
		return false;
	}
}
