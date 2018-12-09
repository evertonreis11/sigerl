package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Transferenciaitem;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;
import br.com.linkcom.wms.geral.dao.TransferenciaDAO;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.TransferenciaFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TransferenciaService extends GenericService<Transferencia> {
	
	private TransferenciaDAO transferenciaDAO;
	private OrdemservicoService ordemservicoService;
	private ProdutoembalagemService produtoembalagemService;
	
	public void setTransferenciaDAO(TransferenciaDAO transferenciaDAO) {
		this.transferenciaDAO = transferenciaDAO;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setProdutoembalagemService(
			ProdutoembalagemService produtoembalagemService) {
		this.produtoembalagemService = produtoembalagemService;
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que recupera uma lista de transferências.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.TransferenciaDAO#findByTransferencia(TransferenciaFiltro)
	 * 
	 * @author Arantes
	 * @author Cíntia
	 * 
	 * @param transferenciaFiltro
	 * @return List<Transferencia>
	 * 
	 */
	public List<Transferencia> findByTransferencia(TransferenciaFiltro transferenciaFiltro, String orderBy) {
		return transferenciaDAO.findByTransferencia(transferenciaFiltro, orderBy);
	}

	/**
	 * Verifica se a transferencia possui ordens de serviço
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param transferencia
	 */
	public boolean hasOrdemServico(Transferencia transferencia) {
		List<Ordemservico> listaTransferencia = ordemservicoService.findByTransferencia(transferencia);
		if(listaTransferencia == null || listaTransferencia.isEmpty())
			return false;
		
		return true;
	}

	/**
	 * Método que carrega informações complementares para a tela de entrada
	 * 
	 * @param transferencia
	 * @author Tomás Rabelo
	 */
	public void carregaInformacoesComplementaresForEntrada(Transferencia transferencia) {
		// Faz este método pois estava sendo carregado lista dentro de lista o
		// que não funciona
    	if(transferencia != null && transferencia.getCdtransferencia() != null)
    		if(transferencia.getListaTransferenciaitem() != null && !transferencia.getListaTransferenciaitem().isEmpty())
	    		for (Transferenciaitem transferenciaitem : transferencia.getListaTransferenciaitem()) {
	    			
	    			if (transferenciaitem.getProduto().getListaDadoLogistico().isEmpty()){
	    				transferenciaitem.getProduto().getListaDadoLogistico().add(DadologisticoService.getInstance().findByProduto(transferenciaitem.getProduto(), WmsUtil.getDeposito()));
	    			}
	    			
	    			if(transferenciaitem.getEnderecodestino() != null && transferenciaitem.getEnderecodestino().getCdendereco() != null && 
	    				transferenciaitem.getEnderecodestino().getEnderecofuncao() != null && transferenciaitem.getEnderecodestino().getEnderecofuncao().getCdenderecofuncao() != null && 
	    				transferenciaitem.getProduto() != null && transferenciaitem.getProduto().getCdproduto() != null){
	    				
	    				Produtoembalagem produtoembalagem;
	    				if (transferenciaitem.getEnderecoorigem().getEnderecofuncao().equals(Enderecofuncao.PICKING))
	    					produtoembalagem = produtoembalagemService.findMenorEmbalagem(transferenciaitem.getProduto());
	    				else
	    					produtoembalagem = produtoembalagemService.findCompraByProduto(transferenciaitem.getProduto());
	    					
	    				transferenciaitem.setProdutoembalagem(produtoembalagem);
	    			}
	    		}
	}

	private static TransferenciaService instance;
	public static TransferenciaService getInstance() {
		if (instance == null)
			instance = Neo.getObject(TransferenciaService.class);
		
		return instance;
	}

	/**
	 * Atualiza a transferência para finalizada se todas as ordens já foram executadas.
	 * 
	 * @param transferencia
	 */
	public void finalizarSeCompleta(Transferencia transferencia) {
		List<Ordemservico> listaOS = ordemservicoService.findBy(transferencia, "ordemstatus.cdordemstatus");
		for (Ordemservico os : listaOS){
			if (!os.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) 
					&& !os.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO)){
				return;
			}
		}
		
		transferencia.setTransferenciastatus(Transferenciastatus.FINALIZADO);
		TransferenciastatusService.getInstance().updateStatusTransferencia(transferencia);
	}
}
