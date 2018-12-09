package br.com.linkcom.wms.geral.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicaogrupo;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.dao.CarregamentoitemDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class CarregamentoitemService extends GenericService<Carregamentoitem> {
	
		
	private CarregamentoitemDAO carregamentoitemDAO;
	private TransactionTemplate transactionTemplate;
	private PedidovendaprodutoService pedidovendaprodutoService;
	private CarregamentoService carregamentoService;
	private EtiquetaexpedicaogrupoService etiquetaexpedicaogrupoService;
	
	public void setCarregamentoitemDAO(CarregamentoitemDAO carregamentoitemDAO) {
		this.carregamentoitemDAO = carregamentoitemDAO;
	}
	
	public Carregamentoitem findByPedidoVendaProduto(Pedidovendaproduto pedidovendaproduto,Carregamento carregamento){
		return carregamentoitemDAO.findByPedidoVendaProduto(pedidovendaproduto, carregamento);
	}
	
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public void setPedidovendaprodutoService(
			PedidovendaprodutoService pedidovendaprodutoService) {
		this.pedidovendaprodutoService = pedidovendaprodutoService;
	}
	
	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}

	public void setEtiquetaexpedicaogrupoService(EtiquetaexpedicaogrupoService etiquetaexpedicaogrupoService) {
		this.etiquetaexpedicaogrupoService = etiquetaexpedicaogrupoService;
	}
	
	/**
	 * Salva os itens do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param listaCarregamentoItem
	 * @param carregamento
	 */
	public void saveCarregamentoItem(final List<Carregamentoitem> listaCarregamentoItem,final Carregamento carregamento) {
		final List<Carregamentoitem> listaCarregamentoItemBanco = findByCarregamento(carregamento);
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				for(Carregamentoitem carregamentoitem : listaCarregamentoItem){

					carregamentoitem.setCarregamento(carregamento);
					//se o item já existe vou fazer um update
					if(listaCarregamentoItemBanco != null && !listaCarregamentoItemBanco.isEmpty() && listaCarregamentoItemBanco.contains(carregamentoitem)){
						if(updateCarregamentoItem(carregamentoitem) == 0){
							carregamento.setHouveConflitos(true);
						}
						else{
							listaCarregamentoItemBanco.remove(carregamentoitem);
						}
					}//o item ainda não existe, deve ser inserido
					else{
						if(insertCarregamentoItem(carregamentoitem,carregamento) == 0){
							carregamento.setHouveConflitos(true);
						}
					}
				}
				if(listaCarregamentoItemBanco != null && !listaCarregamentoItemBanco.isEmpty())
					for (Carregamentoitem aux : listaCarregamentoItemBanco) {
						aux.setCarregamento(null);
						delete(aux);
					}
				
				CarregamentoService.getInstance().atualizarListaRotas(carregamento);
				return null;
			}

		});
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoitemDAO#insertCarregamentoItem(Carregamentoitem carregamentoitem, Carregamento carregamento)
	 * 
	 * @param carregamentoitem
	 * @param carregamento
	 * @return
	 */
	public int insertCarregamentoItem(Carregamentoitem carregamentoitem, Carregamento carregamento) {
		return carregamentoitemDAO.insertCarregamentoItem(carregamentoitem, carregamento);
	}	
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoitemDAO#updateCarregamentoItem(Carregamentoitem carregamentoitem)
	 * 
	 * @param carregamento
	 */
	public int updateCarregamentoItem(Carregamentoitem carregamentoitem) {
		return carregamentoitemDAO.updateCarregamentoItem(carregamentoitem);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoitemDAO#findByCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Carregamentoitem> findByCarregamento(Carregamento carregamento) {
		return carregamentoitemDAO.findByCarregamento(carregamento);
	}

	/* singleton */
	private static CarregamentoitemService instance;
	public static CarregamentoitemService getInstance() {
		if(instance == null){
			instance = Neo.getObject(CarregamentoitemService.class);
		}
		return instance;
	}

	/**
	 * 
	 * Método que atualiza a quantidade confirmada do carregamentoitem.
	 * Acrescentado funcionalidade ao método método: caso o parametro faturamento em outra filial venha como true. o Método atualiza a 
	 * coluna carregado do pedido venda produto e atualiza o status do carregamento. 
	 * 
	 * @author Giovane Freitas
	 *  
	 * @param map
	 * @param listaCarregamentoitem
	 * 
	 */
	public void atualizarQtdeconfirmadaCorte(List<Carregamentoitem> listaCarregamentoitem, Map<String, Long> map) {
		for (Carregamentoitem ci : listaCarregamentoitem) {
			String key = ci.getPedidovendaproduto().getProduto().getCdproduto() + "-" + ci.getPedidovendaproduto().getPedidovenda().getCdpedidovenda();
			Long qtdeConfirmada = map.get(key);
			Long qtdeEsperada = ci.getPedidovendaproduto().getQtde();
				
			if (!map.containsKey(key))
				ci.setQtdeconfirmada(qtdeEsperada);
			else if (qtdeConfirmada <= 0)
				ci.setQtdeconfirmada(0L);
			else if (qtdeConfirmada > qtdeEsperada){
				ci.setQtdeconfirmada(qtdeEsperada);
				//a quantidade foi maior porque haviam mais de um item, 
				//vou abater a quantidade confirmada para a próxima iteração
				map.put(key, qtdeConfirmada - qtdeEsperada);
			}else{
				ci.setQtdeconfirmada(qtdeConfirmada);
				map.put(key, 0L);
			}

			carregamentoitemDAO.updateQtdeconfirmada(ci);
			
			//Corta o item que foi faturado em outra filial e atualiza o carregamento
			if(ci.getQtdeconfirmada() == 0 && isCarregamentoItemFaturadoOutraFilial(ci)){
				pedidovendaprodutoService.retiraPedidoVendaProdutoDoCarregamento(ci.getPedidovendaproduto());
				carregamentoService.atualizarListaRotas(ci.getCarregamento());
			}
			
			//quando for um pedido de mostruário com produtos repetidos haverá regitros em Etiquetaexpedicaogrupo
			for (Etiquetaexpedicaogrupo eeg : ci.getListaEtiquetaexpedicao().iterator().next().getListaEtiquetaexpedicaogrupo()){
				key = eeg.getCarregamentoitem().getPedidovendaproduto().getProduto().getCdproduto() + "-" + eeg.getCarregamentoitem().getPedidovendaproduto().getPedidovenda().getCdpedidovenda();
				Carregamentoitem ci2 = eeg.getCarregamentoitem();
				Long qtdeConfirmadaRestante = map.get(key);
				Long qtdeEsperada2 = eeg.getCarregamentoitem().getPedidovendaproduto().getQtde();
				
				if (!map.containsKey(key))
					ci2.setQtdeconfirmada(qtdeEsperada2);
				else if (qtdeConfirmadaRestante <= 0)
					ci2.setQtdeconfirmada(0L);
				else if (qtdeConfirmadaRestante > qtdeEsperada2){
					ci2.setQtdeconfirmada(qtdeEsperada2);
					//a quantidade foi maior porque haviam mais de um item, 
					//vou abater a quantidade confirmada para a próxima iteração
					map.put(key, qtdeConfirmadaRestante - qtdeEsperada2);
				}else{
					ci2.setQtdeconfirmada(qtdeConfirmadaRestante);
					map.put(key, 0L);
				}
				
				carregamentoitemDAO.updateQtdeconfirmada(ci2);
				
			}
			
		}
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param ci
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean isCarregamentoItemFaturadoOutraFilial(Carregamentoitem ci) {
		return carregamentoitemDAO.isCarregamentoItemFaturadoOutraFilial(ci);
	}

	/**
	 * 
	 * Método que atualiza a quantidade confirmada de cada item do carregamento. 
	 * Se a quantidade for nula, irá preencher com o valor da qtde do pedido de venda do produto. 
	 *  
	 * @author Giovane Freitas 
	 * 
	 * @param listaCarregamentoitem
	 * 
	 */
	public void atualizarQtdeconfirmada(List<Carregamentoitem> listaCarregamentoitem) {
		for (Carregamentoitem ci : listaCarregamentoitem) {	
			if (ci.getQtdeconfirmada() == null){
				ci.setQtdeconfirmada(ci.getPedidovendaproduto().getQtde());
				carregamentoitemDAO.updateQtdeconfirmada(ci);
			}
				
			for (Etiquetaexpedicao ee : ci.getListaEtiquetaexpedicao()){
				for (Etiquetaexpedicaogrupo eeg : ee.getListaEtiquetaexpedicaogrupo()){
					Carregamentoitem ci2 = eeg.getCarregamentoitem();
					if (ci2.getQtdeconfirmada() == null){
						ci2.setQtdeconfirmada(ci2.getPedidovendaproduto().getQtde());
						carregamentoitemDAO.updateQtdeconfirmada(ci2);
					}
				}
			}
			
		}
	}
	
	/**
	 * 
	 * Atualiza o campo quantidade confirmada do carregamentoitem.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param carregamentoitem
	 * 
	 */
	public void updateQtdeconfirmada(Carregamentoitem carregamentoitem) {
		carregamentoitemDAO.updateQtdeconfirmada(carregamentoitem);
	}

	/**
	 * Obtém a lista de carregamentoitem para realizar o corte.
	 * 
	 * @see CarregamentoitemDAO#findForConfirmacaoCorte(Ordemservico)
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Carregamentoitem> findForConfirmacaoCorte(Ordemservico ordemservico) {
		List<Carregamentoitem> listaCarregamentoitem = carregamentoitemDAO.findForConfirmacaoCorte(ordemservico);
		
		/*Foi adicionado essa busca de lista etiqueta expedição grupo, pois a query a cima faz join em listas aninhadas.
		 * o que não é possível devido a limitações do NEO.*/
		for (Carregamentoitem carregamentoitem : listaCarregamentoitem) 
			if(carregamentoitem.getListaEtiquetaexpedicao() != null && !carregamentoitem.getListaEtiquetaexpedicao().isEmpty())
				for (Etiquetaexpedicao etiquetaexpedicao : carregamentoitem.getListaEtiquetaexpedicao()) 
					etiquetaexpedicao.setListaEtiquetaexpedicaogrupo(new ListSet<Etiquetaexpedicaogrupo>(Etiquetaexpedicaogrupo.class, etiquetaexpedicaogrupoService.carregaListaEtiquetaExpedicaoGrupo(etiquetaexpedicao)));

		return listaCarregamentoitem;
	}

	/**
	 * Carrega o CarregamentoItem e o PedidoVendaProduto associado.
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public Carregamentoitem loadForSeparacao(Carregamentoitem carregamentoitem) {
		return carregamentoitemDAO.loadForSeparacao(carregamentoitem);
	}
	
	/**
	 * Retorna true se há produto sem linha de separação
	 * @param carregamento
	 * @return
	 * @author Cíntia Nogueira
	 */
	public boolean  hasProdutoSemLinha(Carregamento carregamento) {
		List<Carregamentoitem> lista = carregamentoitemDAO.getLinhaSeparacao(carregamento);
		if(lista==null || lista.isEmpty()){
			return true;
		}
		for(Carregamentoitem carregamentoitem : lista){
			Produto produto= carregamentoitem.getPedidovendaproduto().getProduto();
			if(produto.getListaDadoLogistico()==null || produto.getListaDadoLogistico().isEmpty()){
				return true;
			}else{
				for(Dadologistico dado : produto.getListaDadoLogistico()){
					if(dado.getLinhaseparacao()==null){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Localiza o {@link Carregamentoitem} associado a uma {@link Ordemservicoproduto}
	 * 
	 * @author Giovane Freitas
	 * @param ordemservicoproduto
	 * @return
	 */
	public Carregamentoitem findByOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		return carregamentoitemDAO.findByOrdemservicoproduto(ordemservicoproduto);
	}

	/**
	 * Atualiza a quantidade confirmada para 0 para todos o itens que estão null
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	public void atualizaCorte(Carregamento carregamento) {
		carregamentoitemDAO.atualizaCorte(carregamento);
	}

	public Carregamentoitem findByPedidoVendaProduto(Long codigoERP){
		return carregamentoitemDAO.findByPedidoVendaProduto(codigoERP);
	}
	
	public List<Carregamentoitem> loadCarregamentoItens(Carregamento carregamento) {
		return carregamentoitemDAO.loadCarregamentoItens(carregamento);
	}
	
	public List<Carregamentoitem> loadCarregamentoItens() {
		return carregamentoitemDAO.loadCarregamentoItens();
	}
	
	public void updateDtSincronizacao(Carregamentoitem carregamentoitem) {
		carregamentoitemDAO.updateDtSincronizacao(carregamentoitem);
	}

	/**
	 * @param carregamento
	 * @param whereIn
	 */
	public void deleteByCarregamento(Carregamento carregamento, String whereIn) {
		if(whereIn!=null && !whereIn.isEmpty()){
			carregamentoitemDAO.deleteByWhereIn(whereIn);	
		}else{
			carregamentoitemDAO.deleteByCarregamento(carregamento);
		}
	}
	
}
