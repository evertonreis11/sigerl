package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.dao.PedidovendaDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;
import br.com.linkcom.wms.util.WmsException;


public class PedidovendaService extends GenericService<Pedidovenda> {

	@SuppressWarnings("unused")
	private PedidovendaDAO pedidovendaDAO;
	
	public void setPedidovendaDAO(PedidovendaDAO pedidovendaDAO) {
		this.pedidovendaDAO = pedidovendaDAO;
	}
	
		
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 *  
	 * @see br.com.linkcom.neo.wms.geral.service.PedidovendaService.pedidovendaDAO
	 * 
	 * @param tipooperacao - Tipo de operação do pedido
	 * @param praca - Praça do pedido
	 * @param filtro - Filtro contendo os exigências para que os dados sejam buscados do banco
	 * @param verificaCodigoErp - Caso true verifica se o produto contém o código erp do filtro e
	 * 							  se a venda é para o cliente do filtro
	 * @return
	 */
	public List<Pedidovenda> findByTipoOperacaoPraca(Tipooperacao tipooperacao, Praca praca, CarregamentoFiltro filtro){
		return pedidovendaDAO.findByTipoOperacaoPraca(tipooperacao,praca, filtro);
	}

	/* singleton */
	private static PedidovendaService instance;
	public static PedidovendaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidovendaService.class);
		}
		return instance;
	}
	
	/**
	 * Libera os pedidos de venda de transbordo associados às notas que foram
	 * conferidas no recebimento.
	 * 
	 * @author Giovane Freitas
	 */
	public void liberarPedidosTransbordo(Recebimento recebimento) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException(
					"É necessário informar um recebimento para liberar os pedidos de venda de transbordo.");

		// Identificando quais pedidos serão duplicados
		List<Pedidovenda> listaPedidos = pedidovendaDAO.findPedidosByRecebimento(recebimento);

		// Criando os pedidos de transbordo
		for (Pedidovenda pv : listaPedidos) {
			for (Pedidovendaproduto pvp : pv.getListaPedidoVendaProduto()){
				Pedidovendaproduto novoPVP = new Pedidovendaproduto();
				novoPVP.setCarregado(false);
				novoPVP.setDeposito(pvp.getDepositotransbordo());
				novoPVP.setDtprevisaoentrega(pvp.getDtprevisaoentrega());
				novoPVP.setPedidovenda(pv);
				novoPVP.setPessoaendereco(pvp.getPessoaendereco());
				novoPVP.setProduto(pvp.getProduto());
				novoPVP.setQtde(pvp.getQtde());
				novoPVP.setValor(pvp.getValor());
				novoPVP.setCodigoerp(pvp.getCodigoerp());
				
				// 1.Pedidos do tipo CD-CD-Entrega darão origem a novos pedidos do
				// tipo Entrega CD-Cliente, associados ao depósito de transbordo
				// determinado no pedido original;
				if (Tipooperacao.ENTREGA_CD_CD_CLIENTE.equals(pvp.getTipooperacao()))
					novoPVP.setTipooperacao(Tipooperacao.ENTREGA_CD_CLIENTE);
	
				// 2.Pedidos do tipo CD-CD-Transferência darão origem a novos
				// pedidos do tipo Transferência Filial-Entrega, associados ao
				// depósito de transbordo e à filial de entrega determinados no
				// pedido original.
				if (Tipooperacao.TRANSFERENCIA_CD_CD_CLIENTE.equals(pvp.getTipooperacao())){
					novoPVP.setTipooperacao(Tipooperacao.TRANSFERENCIA_FILIAL_ENTREGA);
					novoPVP.setFilialEntrega(pvp.getFilialentregatransbordo());
				}
				
				PedidovendaprodutoService.getInstance().saveOrUpdateNoUseTransaction(novoPVP);
			}

		}
	}
	
	/**
	 * Verifica se o pedido de venda está relacionado a algum carregamento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param pedidovenda
	 * @return
	 */
	public Boolean isCarregado(Pedidovenda pedidovenda) {
		return pedidovendaDAO.isCarregado(pedidovenda);
	}

	/**
	 * Cancela o pedido de venda;
	 * 
	 * @see PedidovendaDAO#cancelar(Pedidovenda)
	 * @author Giovane Freitas
	 * @param pedidovenda
	 */
	public void cancelar(Pedidovenda pedidovenda) {
		pedidovendaDAO.cancelar(pedidovenda);
	}
	
	/**
	 * 
	 * @param codigoERP
	 * @return
	 */
	public Pedidovenda findByCodigoERP(Long codigoERP){
		return pedidovendaDAO.findByCodigoERP(codigoERP);
	}
	
}
