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
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 *  
	 * @see br.com.linkcom.neo.wms.geral.service.PedidovendaService.pedidovendaDAO
	 * 
	 * @param tipooperacao - Tipo de opera��o do pedido
	 * @param praca - Pra�a do pedido
	 * @param filtro - Filtro contendo os exig�ncias para que os dados sejam buscados do banco
	 * @param verificaCodigoErp - Caso true verifica se o produto cont�m o c�digo erp do filtro e
	 * 							  se a venda � para o cliente do filtro
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
	 * Libera os pedidos de venda de transbordo associados �s notas que foram
	 * conferidas no recebimento.
	 * 
	 * @author Giovane Freitas
	 */
	public void liberarPedidosTransbordo(Recebimento recebimento) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException(
					"� necess�rio informar um recebimento para liberar os pedidos de venda de transbordo.");

		// Identificando quais pedidos ser�o duplicados
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
				
				// 1.Pedidos do tipo CD-CD-Entrega dar�o origem a novos pedidos do
				// tipo Entrega CD-Cliente, associados ao dep�sito de transbordo
				// determinado no pedido original;
				if (Tipooperacao.ENTREGA_CD_CD_CLIENTE.equals(pvp.getTipooperacao()))
					novoPVP.setTipooperacao(Tipooperacao.ENTREGA_CD_CLIENTE);
	
				// 2.Pedidos do tipo CD-CD-Transfer�ncia dar�o origem a novos
				// pedidos do tipo Transfer�ncia Filial-Entrega, associados ao
				// dep�sito de transbordo e � filial de entrega determinados no
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
	 * Verifica se o pedido de venda est� relacionado a algum carregamento.
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
