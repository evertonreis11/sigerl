package br.com.linkcom.wms.geral.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Inventariostatus;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.vo.ContagemInventarioVO;
import br.com.linkcom.wms.geral.dao.InventarioDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ContagemInventarioFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.logistica.InventarioVO;

public class InventarioService extends GenericService<Inventario>{
	
	protected InventarioDAO inventarioDAO;
	protected OrdemservicoService ordemservicoService;
	protected EnderecoService enderecoService;
	
	public void setInventarioDAO(InventarioDAO inventarioDAO) {
		this.inventarioDAO = inventarioDAO;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setEnderecoService(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}
		
	@Override
	public void saveOrUpdate(Inventario bean) {
		bean.setDeposito(WmsUtil.getDeposito());
		if(bean.getCdinventario() == null){
			bean.setDtinventario(new Date(System.currentTimeMillis()));
			bean.setInventariostatus(Inventariostatus.NAO_INICIADO);
		}

		super.saveOrUpdate(bean);
		
		for(Inventariolote inventariolote : bean.getListaInventariolote()){
			List<Ordemservico> listaOrdemServico = new ArrayList<Ordemservico>();
			if(inventariolote.getCdinventariolote() != null){
				listaOrdemServico.addAll(ordemservicoService.findByLote(inventariolote));
			}
			if(listaOrdemServico == null || listaOrdemServico.isEmpty())
				inventariolote.setListaOrdemservico(null);
			else
				inventariolote.setListaOrdemservico(listaOrdemServico);
			
			inventariolote.setInventario(bean);
			InventarioloteService.getInstance().saveOrUpdate(inventariolote);
		}

	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventario
	 */
	public void updateInventarioStatus(Inventario inventario){
		inventarioDAO.updateInventarioStatus(inventario);
	}
	
	/**
	 * Verifica se o os lotes possuem alguma ordem em execução
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#findContagemByInventarioLote(Inventariolote inventariolote)
	 * 
	 * @param listaInventariolote
	 * @return
	 */
	public boolean containsOrdemEmExecucao(Collection<Inventariolote> listaInventariolote) {
		List<Ordemservico> listaOS = null;
		for(Inventariolote inventariolote : listaInventariolote){
			listaOS = ordemservicoService.findContagemByInventarioLote(inventariolote);
			if(listaOS != null && !listaOS.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.InventarioDAO#findByOrdemservico(Ordemservico ordemservico)
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Inventario findByOrdemservico(Ordemservico ordemservico) {
		return inventarioDAO.findByOrdemservico(ordemservico);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Recupera uma lista de inventários com os dados já calculados para preencher o relatório de inventário.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.InventarioDAO#findByInventario(Inventario)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<InventarioVO>
	 * 
	 */
	private List<InventarioVO> findByInventario(Inventario filtro) {
		return inventarioDAO.findByInventario(filtro);		
	}
	
	/**
	 * 
	 * Recupera uma lista de inventários com os dados já calculados para preencher o relatório de inventário.
	 * 
	 * @see br.com.linkcom.wms.geral.service.InventarioService#findByInventario(Inventario)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<InventarioVO>
	 * 
	 */
	public IReport generateReportInventario(Inventario filtro) {
		Report report = new Report("RelatorioInventario");	
		List<InventarioVO> listaInventario = this.findByInventario(filtro);
		for(InventarioVO inventarioVo: listaInventario){
			if(inventarioVo.getQtdeanterior()==null){
				inventarioVo.setQtdeanterior(0l);
			}
			if(inventarioVo.getQtdeinventario()==null){
				inventarioVo.setQtdeinventario(0l);				
			}
			if(inventarioVo.getDivergencia()==null){
				inventarioVo.setDivergencia(inventarioVo.getQtdeinventario()-inventarioVo.getQtdeanterior());
			}
		}
		
		report.setDataSource(listaInventario);
		return report;
	}

	/**
	 * 
	 * Método que verifica se um inventário é para ser finalizado ou não.
	 * 
	 * @author Arantes
	 * 
	 * @param inventarioLote
	 * @param hasOs
	 * 
	 * @return Boolean
	 */
	
	public Boolean isFinalizarInventario(Inventariolote inventarioLote, Boolean hasOs) {
		if(hasOs){
			for (Ordemservico ordemservico : inventarioLote.getListaOrdemservico()){
				if(!ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO) 
						&& !ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) 
						&& !ordemservico.getOrdemstatus().equals(Ordemstatus.AGUARDANDO_CONFIRMACAO)){
					
					return Boolean.FALSE;
				}
			}
		}
				
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Método que verifica se um inventário lote possui ou não ordem de serviço
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#loadByInventarioLote(Inventariolote)
	 * 
	 * @author Arantes
	 * 
	 * @param inventarioLote
	 * 
	 * @return Boolean
	 * 
	 */
	public Boolean hasOs(Inventariolote inventarioLote) {
		List<Ordemservico> listaOs = ordemservicoService.loadByInventarioLote(inventarioLote);
		
		if((listaOs != null) && (!listaOs.isEmpty())) {
			inventarioLote.setListaOrdemservico(listaOs);
			return Boolean.TRUE;
		}
	
		return Boolean.FALSE;
	}
 	
	/**
	 * Método de referencia ao DAO
	 * 
	 * @author Rodrigo Alvarenga
	 * 
	 * @see br.com.linkcom.wms.geral.dao.InventarioDAO#iniciarInventario(Inventario inventario)
	 * 
	 * @param inventario
	 * @return mensagem caso haja algum erro na execução da procedure 
	 * @throws SQLException 
	 */
	public String iniciarInventario(Inventario inventario) throws SQLException {
		return inventarioDAO.iniciarInventario(inventario);
	} 	
	
	/**
	 * 
	 * Método que valida se o inventário pode ser finalizado
	 * 
	 * @see br.com.linkcom.wms.geral.service.InventarioService#hasOs(Inventariolote)
	 * @see br.com.linkcom.wms.geral.service.InventarioService#isFinalizarInventario(Inventariolote, Boolean)
	 * 
	 * @author Arantes
	 * 
	 * @param inventario
	 * @return Boolean
	 * 
	 */
	public Boolean podeFinalizar(Inventario inventario) {
		Boolean hasOs;
		
		for (Inventariolote inventarioLote : inventario.getListaInventariolote()) {
			hasOs = this.hasOs(inventarioLote);
			
			if(!this.isFinalizarInventario(inventarioLote, hasOs)) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Método que desbloqueia os endereços.
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoService#desbloquearEnderecos(Inventario, Inventariostatus)
	 * 
	 * @author Arantes
	 * 
	 * @param inventario
	 * @param inventarioAux
	 * 
	 */
	public void desbloquearEnderecos(Inventario inventario, Deposito deposito) {
		Inventario inventarioAux = new Inventario();
		
		for (Inventariolote inventarioLote : inventario.getListaInventariolote()) {			
			List<Inventariolote> listaInventarioloteAux = new ArrayList<Inventariolote>();
			listaInventarioloteAux.add(inventarioLote);
			inventarioAux.setCdinventario(inventario.getCdinventario());
			inventarioAux.setListaInventariolote(listaInventarioloteAux);
			enderecoService.desbloquearEnderecos(inventarioAux, inventario.getInventariostatus(), deposito);
		}
	}

	/**
	 * Cancela ordem de serviços do inventário
	 * @param inventario
	 * @author Cíntia Nogueira
	 */
	public void cancelaOrdensServico(Inventario inventario){
		for(Inventariolote lote: inventario.getListaInventariolote()){
			ordemservicoService.updateStatusOrdemservicoByLote(lote, Ordemstatus.CANCELADO);
		}
	}

	/**
	 * Verifica se um dado inventário terá de fazer ajuste de estoque ao ser finalizado.
	 * 
	 * @autor Giovane Freitas
	 * @param inventario
	 * @return
	 */
	public boolean isAjusteEstoqueNecessario(Inventario inventario) {
		return inventarioDAO.isAjusteEstoqueNecessario(inventario);
	}

	/**
	 * Busca o número de contagens que foram realizadas em um inventário.
	 * 
	 * @param filtro
	 * @return
	 */
	public int getNumeroContagens(ContagemInventarioFiltro filtro) {
		return inventarioDAO.getNumeroContagens(filtro);
	}
	
	public List<ContagemInventarioVO> findContagemInventario(ContagemInventarioFiltro filtro) {
		List<ContagemInventarioVO> result = new ArrayList<ContagemInventarioVO>();
		
		int numContagens = inventarioDAO.getNumeroContagens(filtro);
		SqlRowSet dadosExportacao = inventarioDAO.findContagemInventario(filtro);
		
		ContagemInventarioVO itemAtual = null;
		String ultimoCodigo = null;
		String ultimoEndereco = null;
		while (dadosExportacao.next()){
			
			boolean naoMudouProduto = (ultimoCodigo == dadosExportacao.getObject("codigo") )
						|| (ultimoCodigo != null && ultimoCodigo.equals(dadosExportacao.getString("codigo")) );
			
			boolean naoMudouEndereco = (ultimoEndereco == dadosExportacao.getObject("endereco") )
						|| (ultimoEndereco != null && ultimoEndereco.equals(dadosExportacao.getString("endereco")) );
			
			//se é apenas uma contagem diferente
			if (itemAtual != null && dadosExportacao.getInt("ordem") > 1 && naoMudouProduto && naoMudouEndereco ){

				//Verificar se já existe leitura igual antes de ir gravar a atual
				boolean achouLeituraIgual = false;
				if (dadosExportacao.getObject("qtde") != null){
					for (Integer leitura : itemAtual.getLeituras()){
						if (leitura != null && leitura.equals(dadosExportacao.getInt("qtde")))
							achouLeituraIgual = true;
					}

					itemAtual.setLeitura(dadosExportacao.getInt("ordem"), dadosExportacao.getInt("qtde"));
				}
				
				if (achouLeituraIgual){
					itemAtual.setValorFinal(dadosExportacao.getInt("qtde"));
				}
			} else {
				ultimoCodigo = dadosExportacao.getString("codigo");
				ultimoEndereco = dadosExportacao.getString("endereco");

				itemAtual = new ContagemInventarioVO(numContagens);
				result.add(itemAtual);
				itemAtual.setCodigoProduto(dadosExportacao.getString("codigo"));
				itemAtual.setDescricaoProduto(dadosExportacao.getString("descricao"));
				itemAtual.setEndereco(dadosExportacao.getString("endereco"));
				itemAtual.setQtdeEsperada(dadosExportacao.getInt("qtdeesperada"));
				
				if (dadosExportacao.getObject("qtde") != null){
					itemAtual.setLeitura(dadosExportacao.getInt("ordem"), dadosExportacao.getInt("qtde"));
				}
			}
			
		}

		
		return result;
	}
		
}
