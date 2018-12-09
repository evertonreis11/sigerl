package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Pedidovendahistoricomotivo;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Pedidovendaprodutostatus;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.RotaManualPedidoVendaFiltro;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class PedidovendaprodutoService extends GenericService<Pedidovendaproduto> {

	private PedidovendaprodutoDAO pedidovendaprodutoDAO;
	private PedidovendaprodutohistoricoService pedidovendaprodutohistoricoService;
	private TransactionTemplate transactionTemplate;
	private PedidovendahistoricomotivoService pedidovendahistoricomotivoService;

	public void setPedidovendaprodutoDAO(PedidovendaprodutoDAO pedidovendaprodutoDAO) {
		this.pedidovendaprodutoDAO = pedidovendaprodutoDAO;
	}
	
	public void setPedidovendaprodutohistoricoService(PedidovendaprodutohistoricoService pedidovendaprodutohistoricoService) {
		this.pedidovendaprodutohistoricoService = pedidovendaprodutohistoricoService;
	}
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public void setPedidovendahistoricomotivoService(PedidovendahistoricomotivoService pedidovendahistoricomotivoService) {
		this.pedidovendahistoricomotivoService = pedidovendahistoricomotivoService;
	}

	/**
	 * @author Leonardo Guimarães
	 * 
	 * Método de referência ao DAO
	 * 
	 * @param pedidovenda
	 * @return
	 */
	public List<Pedidovendaproduto> findByPedidoVenda(Pedidovenda pedidovenda,String pedidosVendaProduto) {
		return pedidovendaprodutoDAO.findByPedidoVenda(pedidovenda,pedidosVendaProduto);
	}

	/* singleton */
	private static PedidovendaprodutoService instance;
	public static PedidovendaprodutoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidovendaprodutoService.class);
		}
		return instance;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#findByOneCdpededidovendaproduto(Integer cdpedidovendaproduto)
	 * 
	 * @param cdpedidovendaproduto
	 * @return
	 */
	public List<Pedidovendaproduto> findByFiveKeys(String[] array,Integer cdcarregamento, Date datainicio, Date datafim) {
		return pedidovendaprodutoDAO.findByFiveKeys(array,cdcarregamento, datainicio, datafim);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * @param carregamento 
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#findCdsByFiveKeys(Integer cdpedidovendaproduto)
	 * 
	 * @param cdpedidovendaproduto
	 * @return
	 */
	public List<Long> findCdsByFiveKeys(String[] array, Carregamento carregamento, Date datainicio, Date datafim) {
		return pedidovendaprodutoDAO.findCdsByFiveKeys(array,carregamento, datainicio, datafim);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#updateStatusPedidos(String pedidos)
	 * 
	 * @param pedido
	 */
	public void updateStatusPedidos(String pedido,Boolean status) {
		pedidovendaprodutoDAO.updateStatusPedidos(pedido,status);		
	}
	

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#getTotalEntrega()
	 * 
	 * @param cds - Caso cds = "" busca todos os enderecos de entrega
	 * @return
	 */
	public Integer getTotalEntrega(String cds) {
		return pedidovendaprodutoDAO.getTotalEntrega(cds);
	}
	
	/**
	 * Cria uma string com os cds dos pedidos de venda
	 * @param chave
	 * @return
	 */
	public String getCds(WebRequestContext request) {
		Date datainicio = null;
		Date datafim = null;
		
		String[] array = request.getParameter("chave").split(",");
		List<Pedidovendaproduto> listaPedidovendaproduto = findByFiveKeys(array, null, datainicio, datafim);
		String cds="";
		for (Pedidovendaproduto pedidovendaproduto : listaPedidovendaproduto) {
			cds+=pedidovendaproduto.getCdpedidovendaproduto()+",";
		}
		return cds;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#abilitarByCarregamentoCancelado(Carregamento carregamento)
	 * 
	 * @param carregamento
	 */
	public void habilitarByCarregamentoCancelado(Carregamento carregamento) {
		pedidovendaprodutoDAO.habilitarByCarregamentoCancelado(carregamento);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param pedidovendaproduto
	 * @author Tomás Rabelo
	 */
	public void retiraPedidoVendaProdutoDoCarregamento(Pedidovendaproduto pedidovendaproduto) {
		pedidovendaprodutoDAO.retiraPedidoVendaProdutoDoCarregamento(pedidovendaproduto);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param pedidovendaproduto
	 * @author Giovane Freitas
	 */
	public List<Pedidovendaproduto> findByCarregamento(Carregamento carregamento) {
		return pedidovendaprodutoDAO.findByCarregamento(carregamento);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#findByCarregamentoItem(Carregamentoitem carregamentoitem)
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public Pedidovendaproduto findByCarregamentoItem(Carregamentoitem carregamentoitem) {
		return pedidovendaprodutoDAO.findByCarregamentoItem(carregamentoitem);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#findByCarregamentoItem(Carregamentoitem carregamentoitem)
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public List<Pedidovendaproduto> findByListCarregamentoItem(List<Carregamentoitem> listaCarregamentoItem) {
		return pedidovendaprodutoDAO.findByListCarregamentoItem(listaCarregamentoItem);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PedidovendaprodutoDAO#findByCarregamentoItem(Carregamentoitem carregamentoitem)
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public List<Pedidovendaproduto> findByConfirmacaoItemVO(Ordemservico conferenciaBox) {
		return pedidovendaprodutoDAO.findByConfirmacaoItemVO(conferenciaBox);
	}
	
	/**
	 * Consulta os PedidoVendaProduto a partir do pedidovenda, retornando também os 
	 * Carregamentoitem quando existirem
	 * 
	 * @see PedidovendaprodutoDAO#findByPedidoVenda(Pedidovenda)
	 * @author Giovane Freitas
	 * @param pedidovenda
	 * @return 
	 */
	public List<Pedidovendaproduto> findByPedidoVenda(Pedidovenda pedidovenda) {
		return pedidovendaprodutoDAO.findByPedidoVenda(pedidovenda);
	}

	/**
	 * Busca os números das notas associadas a um carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @return
	 */
	public String findNumerosNotas(Carregamento carregamento) {
		return pedidovendaprodutoDAO.findNumerosNotas(carregamento);
	}

	public Pedidovendaproduto findByCodigoERP(Long codigoERP){
		return pedidovendaprodutoDAO.findByCodigoERP(codigoERP);
	}
	
	public Pedidovendaproduto findByCodigoERPFaturamento(Long codigoERP){
		return pedidovendaprodutoDAO.findByCodigoERPFaturamento(codigoERP);
	}
	
	public List<Pedidovendaproduto> pedidoVendaProdutoRotaManual(RotaManualPedidoVendaFiltro filtro){
		return pedidovendaprodutoDAO.pedidoVendaProdutoRotaManual(filtro);
	}
	
	public void updatePedidoPraca(Pedidovendaproduto pedidovendaproduto, Praca praca) {
		pedidovendaprodutoDAO.updatePedidoPraca(pedidovendaproduto, praca);
	}

	public void updatePrioridade(String listaPedidoVendaPrioritario) {
		pedidovendaprodutoDAO.updatePrioridade(listaPedidoVendaPrioritario);
	}

	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Pedidovendaproduto> findByIds(String whereIn){
		return pedidovendaprodutoDAO.findByIds(whereIn);
	}
	
	/**
	 * 
	 */
	@Override
	public ListagemResult<Pedidovendaproduto> findForListagem(FiltroListagem filtro) {
		
		ListagemResult<Pedidovendaproduto> lista = super.findForListagem(filtro);
				
		if(lista.list()!=null && !lista.list().isEmpty()){
			for (Pedidovendaproduto pvp : lista.list()) {
				try{
					String numeroPedido = pvp.getPedidovenda().getNumero();
					String empresa = numeroPedido.substring(1,1);
					String pedidoMV = numeroPedido.substring(3,9);
					String lojaMV = numeroPedido.substring(11);
					pvp.getPedidovenda().setNumeroPedidoMV(pedidoMV);
					pvp.getPedidovenda().setNumeroLojaMV(lojaMV);
					pvp.getPedidovenda().setEmpresaMV(empresa);
				}catch (Exception e) {
					continue;
				}
			}
		}
				
		return lista;
	}

	/**
	 * 
	 * @param whereIn
	 */
	public void liberarPedidosRoterizacao(final String whereIn){
		
		final String msg = "Os pedidos de id: ("+whereIn+") estão liberados novamenete para geração de separação.";
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				pedidovendaprodutoDAO.updateStatusLiberado(whereIn);
				pedidovendaprodutohistoricoService.criaHistoricoStatus(msg,Pedidovendaprodutostatus.DISPONIVEL,whereIn);
				return null;
			}
		});
	}
	
	/**
	 * 
	 * @param whereIn
	 * @param tipomovito 
	 */
	public void travarPedidosRoterizacao(final String whereIn, Pedidovendahistoricomotivo tipomotivo, String motivo){
		
		if(tipomotivo!=null){
			tipomotivo = pedidovendahistoricomotivoService.load(tipomotivo);
		}
		else{
			throw new WmsException("O Tipo de Motivo não pode ser nulo. Por favor, tente novamente.");
		}
		
		final String msg = "Os pedidos de id: ("+whereIn+") estão travados e não estão disponiveis para geração de separação." +
							"<br>Motivo: "+tipomotivo.getNome()+", "+motivo;
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				pedidovendaprodutoDAO.updateStatusTravado(whereIn);
				pedidovendaprodutohistoricoService.criaHistoricoStatus(msg,Pedidovendaprodutostatus.ENTREGA_TRAVADA,whereIn);
				return null;
			}
		});
		
	}

	/**
	 * 
	 * @param pedidosSelecionados
	 * @return
	 */
	public boolean validarPedidosExclusao(String whereIn){
		
		List<Pedidovendaproduto> lista = pedidovendaprodutoDAO.findForExclusao(whereIn);
		
		for (Pedidovendaproduto pvp : lista){
			if(!pvp.getTipooperacao().equals(Tipooperacao.MONSTRUARIO_LOJA)){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public List<Pedidovendaproduto> findByCancelamentoAbastecimento(String whereIn) {
		return pedidovendaprodutoDAO.findByCancelamentoAbastecimento(whereIn);
	}

	/**
	 * 
	 * @param codigoerp
	 * @param cdempresa
	 */
	public String callCancelarAbastecimento(Long codigoerp, Integer cdempresa) {
		
		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call prc_cancela_abastecimento(?,?,?) }");
	        
        	cs.setLong(1,codigoerp);
        	cs.setInt(2,cdempresa);
	        cs.registerOutParameter(3,Types.VARCHAR);
	        
	        cs.execute();
	        
	        String resposta = cs.getString(3);	      
	        
	        connection.commit();
	        
	        return resposta;
	        
		}catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
			return "Erro durante a execução da chamada ao WebService.";
		}finally {
			try {
				cs.close();
				connection.close();
			}
			catch (Exception e) {
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param pvp
	 */
	public void excluirPedido(final Pedidovendaproduto pvp) {
		
		final String msg = "O pedido foi excluído.";
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				pedidovendaprodutoDAO.excluirPedido(pvp);
				pedidovendaprodutohistoricoService.criaHistoricoStatus(msg,Pedidovendaprodutostatus.EXCLUIDO,pvp.getCdpedidovendaproduto());
				return null;
			}
		});
	}

	/**
	 * 
	 * @param pedidosSelecionados
	 * @return
	 */
	public boolean validarPedidosTravados(String whereIn) {
		
		List<Pedidovendaproduto> listaPvp = pedidovendaprodutoDAO.findPedidoTravadoByIds(whereIn); 
		
		if(listaPvp!=null && !listaPvp.isEmpty()){
			for (Pedidovendaproduto pvp : listaPvp) {
				if(pvp.getPedidovendaprodutostatus().equals(Pedidovendaprodutostatus.ENTREGA_TRAVADA)){
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	 * 
	 * @param listAndConcatenate
	 */
	public void updateStatusForPedidos(String whereIn, Pedidovendaprodutostatus status) {
		pedidovendaprodutoDAO.updateStatusForPedidos(whereIn,status);
	}

	/**
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Pedidovendaproduto> findByCarregamentoForCancelamento (Carregamento carregamento){
		return pedidovendaprodutoDAO.findByCarregamentoForCancelamento(carregamento);
	}
	
}
