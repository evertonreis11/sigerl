
package br.com.linkcom.wms.geral.service;

import java.awt.Image;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Embalagemexpedicao;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Reabastecimentolote;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.ConferenciaVolumeVO;
import br.com.linkcom.wms.geral.bean.vo.ConfirmacaoItemVO;
import br.com.linkcom.wms.geral.bean.vo.LeituraDivergenteInventarioVO;
import br.com.linkcom.wms.geral.bean.vo.MovimentacaoAberta;
import br.com.linkcom.wms.geral.dao.OrdemservicoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.LancarcorteFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirmapaseparacaoPapelFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.LancarDadosLotesFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.EtiquetaumaFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ImpressaoOrdemReabastecimentoFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ImpressaoOrdemRecebimentoFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ImpressaoOrdemTransferenciaFiltro;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.expedicao.ItemCorteVO;
import br.com.linkcom.wms.util.expedicao.ItemSeparacaoVO;
import br.com.linkcom.wms.util.expedicao.MapaDados;
import br.com.linkcom.wms.util.expedicao.RelatorioMapasVO;
import br.com.linkcom.wms.util.logistica.EnderecoAux;
import br.com.linkcom.wms.util.logistica.EtiquetasumaVO;
import br.com.linkcom.wms.util.logistica.OrdemVO;
import br.com.linkcom.wms.util.logistica.OrdemcontageminventarioVO;
import br.com.linkcom.wms.util.recebimento.RecebimentoPapelReportVO;

public class OrdemservicoService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Ordemservico> {
 
	public enum TipoEndereco{Origem, Destino};
	
	private OrdemservicoDAO ordemservicoDAO;
	private RecebimentoService recebimentoService;
	private NeoImageResolver neoImageResolver;
	private OrdemprodutohistoricoService ordemprodutohistoricoService;
	private OrdemprodutoligacaoService ordemprodutoligacaoService;
	private InventarioService inventarioService;
	private OrdemservicousuarioService ordemservicousuarioService;
	private TipooperacaoService tipooperacaoService;
	private ClienteService clienteService;
	private EnderecoService enderecoService;
	private ProdutoembalagemService produtoembalagemService;
	private EnderecoprodutoService enderecoprodutoService;	
	private OrdemservicoprodutoService ordemservicoprodutoService;
	private OrdemservicoprodutoenderecoService ordemservicoprodutoenderecoService;
	private DadologisticoService dadologisticoService;
	private TipoenderecamentoService tipoenderecamentoService;
	private EmbalagemexpedicaoService embalagemexpedicaoService;
	private InventarioloteService inventarioloteService;
	private CarregamentohistoricoService carregamentohistoricoService;
	private CarregamentoService carregamentoService;

	public void setDadologisticoService(DadologisticoService dadologisticoService) {
		this.dadologisticoService = dadologisticoService;
	}
	public void setOrdemservicoprodutoenderecoService(OrdemservicoprodutoenderecoService ordemservicoprodutoenderecoService) {
		this.ordemservicoprodutoenderecoService = ordemservicoprodutoenderecoService;
	}
	public void setOrdemservicoprodutoService(OrdemservicoprodutoService ordemservicoprodutoService) {
		this.ordemservicoprodutoService = ordemservicoprodutoService;
	}
	public void setEnderecoprodutoService(EnderecoprodutoService enderecoprodutoService) {
		this.enderecoprodutoService = enderecoprodutoService;
	}
	public void setRecebimentoService(RecebimentoService recebimentoService) {
		this.recebimentoService = recebimentoService;
	}
	public void setOrdemservicoDAO(OrdemservicoDAO ordemservicoDAO) {
		this.ordemservicoDAO = ordemservicoDAO;
	}
	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {
		this.neoImageResolver = neoImageResolver;
	}
	public void setOrdemprodutohistoricoService(OrdemprodutohistoricoService ordemprodutohistoricoService) {
		this.ordemprodutohistoricoService = ordemprodutohistoricoService;
	}
	public void setOrdemprodutoligacaoService(OrdemprodutoligacaoService ordemprodutoligacaoService) {
		this.ordemprodutoligacaoService = ordemprodutoligacaoService;
	}
	public void setInventarioService(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	public void setOrdemservicousuarioService(OrdemservicousuarioService ordemservicousuarioService) {
		this.ordemservicousuarioService = ordemservicousuarioService;
	}
	public void setTipooperacaoService(TipooperacaoService tipooperacaoService) {
		this.tipooperacaoService = tipooperacaoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setEnderecoService(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}
	public void setProdutoembalagemService(ProdutoembalagemService produtoembalagemService) {
		this.produtoembalagemService = produtoembalagemService;
	}
	public void setTipoenderecamentoService(TipoenderecamentoService tipoenderecamentoService) {
		this.tipoenderecamentoService = tipoenderecamentoService;
	}
	public void setEmbalagemexpedicaoService(EmbalagemexpedicaoService embalagemexpedicaoService) {
		this.embalagemexpedicaoService = embalagemexpedicaoService;
	}
	public void setInventarioloteService(InventarioloteService inventarioloteService) {
		this.inventarioloteService = inventarioloteService;
	}
	public void setCarregamentohistoricoService(CarregamentohistoricoService carregamentohistoricoService) {
		this.carregamentohistoricoService = carregamentohistoricoService;
	}
	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}
	
	
	/**
	 * Salva uma nova ordem de serviço a partir de um recebimento.
	 * Adiciona a ordem 1, e seta o tipo como recebimento.
	 * 
	 * @param recebimento
	 * @return ordemservico
	 * @author Pedro Gonçalves
	 */
	public Ordemservico salvarOrdemServico(Recebimento recebimento,Deposito deposito,Ordemstatus ordemstatus, Integer ordem){
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		Ordemservico ordemservico = new Ordemservico();
		ordemservico.setOrdem(ordem);
		ordemservico.setOrdemtipo((ordem != null && ordem > 1 )? Ordemtipo.RECONFERENCIA_RECEBIMENTO : Ordemtipo.CONFERENCIA_RECEBIMENTO);
		ordemservico.setRecebimento(recebimento);
		ordemservico.setDeposito(deposito);
		ordemservico.setOrdemservicoprincipal(findPrincipal(recebimento));
		ordemservico.setOrdemstatus(ordemstatus);
		saveOrUpdateNoUseTransaction(ordemservico);
		return ordemservico;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see Ordemservico br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findPrincipal(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @return
	 */
	private Ordemservico findPrincipal(Recebimento recebimento) {
		return ordemservicoDAO.findPrincipal(recebimento);
	}

	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findAllByStatusRecebimento
	 * @param recebimentostatus
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Ordemservico> findAllByStatusRecebimento(Recebimentostatus ... recebimentostatus) {
		List<Ordemservico> lista = ordemservicoDAO.findAllByStatusRecebimento(recebimentostatus);
		if(lista != null && !lista.isEmpty())
			Collections.sort(lista,new Comparator<Ordemservico>(){
				public int compare(Ordemservico o1, Ordemservico o2) {
					return o2.getRecebimento().getCdrecebimento().compareTo(o1.getRecebimento().getCdrecebimento());
				}
			});
		return lista;
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadBy
	 * @param recebimentostatus, ordemservico
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Ordemservico loadBy(Recebimentostatus recebimentostatus, Ordemservico ordemservico) {
		return ordemservicoDAO.loadBy(recebimentostatus, ordemservico);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadBy
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Ordemservico loadBy(Recebimento recebimento){
		return ordemservicoDAO.loadBy(recebimento);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadLast
	 * 
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Ordemservico loadLastConferencia(Recebimento recebimento, boolean incluirInfoProduto){
		return ordemservicoDAO.loadLastConferencia(recebimento, incluirInfoProduto);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadLast
	 * 
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Ordemservico loadLastConferencia(Recebimento recebimento){
		return ordemservicoDAO.loadLastConferencia(recebimento, false);
	}
	
	/**
	 * Faz o agrupamento de dados para o relatorio
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #findForMapas(String cds)
	 * @see #repareLista(List<Ordemservico> listraOrdemservico)
	 * 
	 * @param listaOrdemservico
	 * @return
	 */
	public IReport makeMapasReport(EmitirmapaseparacaoPapelFiltro filtro) {
		Report report = new Report("RelatorioMapaSeparacao");
//		report.addParameter(ConfiguracaoVO.MAPA_IGNORA_LINHA,ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.MAPA_IGNORA_LINHA, null));
		 report.addParameter(ConfiguracaoVO.MAPA_IGNORA_LINHA,false);
		if (filtro.getCdsOS() != null && !filtro.getCdsOS().isEmpty()){
			List<Carregamento> listaCarregamento = CarregamentoService.getInstance().findByOnda(filtro.getCdsOS());
			String listaCarregamentos = CollectionsUtil.listAndConcatenate(listaCarregamento, "cdcarregamento", ", ");
			report.addParameter("LISTA_CARREGAMENTO", listaCarregamentos);
		} else if(filtro.getExpedicao() != null && filtro.getExpedicao().getCdexpedicao() != null){
			Expedicao expedicao = ExpedicaoService.getInstance().getExpedicao(filtro.getExpedicao());
			String listaCarregamentos = CollectionsUtil.listAndConcatenate(expedicao.getListaCarregamento(), "cdcarregamento", ", ");
			report.addParameter("LISTA_CARREGAMENTO", listaCarregamentos);
		}

		List<ItemSeparacaoVO> itensMapa = ordemservicoDAO.findForMapas(filtro);
		if(itensMapa.isEmpty()){
			throw new WmsException("O relátorio não conseguiu carregar os produtos com suas respectivas embalagens para separação.<br>" +
					"  Verifique se a configuração 'Utilizar Caixas Mestres' está desativo. ");
		}
			
		for (ItemSeparacaoVO item : itensMapa) {

			Ordemservico ordemservico = new Ordemservico(item.getCdordemservico());
			Tipooperacao tipooperacao = tipooperacaoService.findByOrdemServico(ordemservico);
			if(tipooperacao != null){
				item.setTipoPedido(tipooperacao.getNome());
				List<String> nomesFiliais = null;
				if(tipooperacao.equals(Tipooperacao.MONSTRUARIO_LOJA)){
					nomesFiliais = clienteService.findNomeCliente(ordemservico);
				} else if(tipooperacao.equals(Tipooperacao.TRANSFERENCIA_FILIAL_ENTREGA)){
					nomesFiliais = clienteService.findNomeFilialEntrega(ordemservico);
				}
				if (nomesFiliais != null && !nomesFiliais.isEmpty())
					item.setFilialEntrega(StringUtils.join(nomesFiliais.iterator(), "\n"));
			}
			
		}
		
		report.setDataSource(itensMapa);
		
		return report;
	}
	
//	/**
//	 * Retira as partes das listas que vem nulas.
//	 * 
//	 * @author Leonardo Guimarães
//	 * 
//	 * @param listraOrdemservico
//	 */
//	public void repareLista(List<Ordemservico> listraOrdemservico){
//		for (Iterator<Ordemservico> iterator = listraOrdemservico.iterator(); iterator.hasNext();) {
//			Ordemservico next = iterator.next();
//			if(next == null || next.getCarregamento() == null)
//				iterator.remove();
//			else{
//				Set<Ordemprodutoligacao> listaOrdemProdutoLigacao = next.getListaOrdemProdutoLigacao();
//				for (Iterator<Ordemprodutoligacao> iterator2 = listaOrdemProdutoLigacao.iterator(); iterator2.hasNext();) {
//					Ordemprodutoligacao next2 = iterator2.next();
//					if(next2 == null)
//						iterator2.remove();
//					else{
//						List<Dadologistico> listaDadoLogistico = next2.getOrdemservicoproduto().getProduto().getListaDadoLogistico();
//						for(Iterator<Dadologistico> iterator3 = listaDadoLogistico.iterator();iterator3.hasNext();){
//							Dadologistico next3 = iterator3.next();
//							if(next3 == null)
//								iterator3.remove();
//						}
//					}
//				}
//			}
//				
//		}
//	}
	
	/**
	 * Agrupa os dados da listaCarregamentoitem do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @param mapasVO
	 * @param lista
	 */
	/*public void groupListaCarregamentoItem(Ordemservico ordemservico, RelatorioMapasVO mapasVO, List<MapaDados> lista){
		for(Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()){
			Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
			
			MapaDados mapaDados = new MapaDados();
			Produto produto = ordemservicoproduto.getProduto();
			
			if(produto != null && produto.getListaDadoLogistico() != null && !produto.getListaDadoLogistico().isEmpty()
			&& produto.getListaDadoLogistico().get(0).getLinhaseparacao() != null && mapasVO.getPaleteLinhaSeparacao() == null)
					mapasVO.setPaleteLinhaSeparacao(produto.getListaDadoLogistico().get(0).getLinhaseparacao().getNome());
			
			mapaDados.setProduto(produto);
			
			
			
			
			
			mapaDados.setQtde(ordemservicoproduto.getQtdeesperada().intValue());
			
			
			
			
			
			List<Dadologistico> listaDadoLogistico = produto.getListaDadoLogistico();
			if(listaDadoLogistico != null && !listaDadoLogistico.isEmpty()) {
				Endereco endereco = listaDadoLogistico.get(0).getEndereco();
				
				if(endereco != null)
					mapaDados.setEndereco(endereco.getEndereco());
			}
			
			long largura = produto.getLargura() == null ? 0L : produto.getLargura();
			long altura = produto.getAltura() == null ? 0L : produto.getAltura();
			long profundidade = produto.getProfundidade() == null ? 0L : produto.getAltura();
			
			mapasVO.setTotalItens(lista.size());
			mapasVO.setVolumeTotal(mapasVO.getVolumeTotal() + (largura * altura * profundidade));
			mapasVO.setPesoTotal(mapasVO.getPesoTotal() + (produto.getPeso() == null ? 0.0 : produto.getPeso()));
			
			lista.add(mapaDados);
		}
	}*/

	/**
	 * Agrupa os dados da listaCarregamentoitem do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @param mapasVO
	 * @param lista
	 */
	public void groupListaCarregamentoItem(Ordemservico ordemservico, RelatorioMapasVO mapasVO, List<MapaDados> lista){
		for(Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()){			
			Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
			
			Produto produto = ordemservicoproduto.getProduto();

			if(produto != null && produto.getListaDadoLogistico() != null && !produto.getListaDadoLogistico().isEmpty() && 
					produto.getListaDadoLogistico().get(0).getLinhaseparacao() != null && mapasVO.getPaleteLinhaSeparacao() == null)
				mapasVO.setPaleteLinhaSeparacao(produto.getListaDadoLogistico().get(0).getLinhaseparacao().getNome());
			
			int qtde = 0;
			
			if (ordemservicoproduto.getListaOrdemservicoprodutoendereco() != null
					&& ordemservicoproduto.getListaOrdemservicoprodutoendereco().size() > 0){
				
				for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : ordemservicoproduto.getListaOrdemservicoprodutoendereco()) {				
					MapaDados mapaDados = new MapaDados();			
					
					Produto produtoprincipal = produto.getProdutoprincipal();
					if(produtoprincipal != null && produtoprincipal.getCdproduto() != null){
						produto.setCodigo(produtoprincipal.getCodigo());
						produto.setDescricao(produtoprincipal.getDescricao());
					}
					
					mapaDados.setProduto(produto);						
					mapaDados.setQtde(ordemservicoprodutoendereco.getQtde());
					qtde += ordemservicoprodutoendereco.getQtde();
					
					if(ordemservicoprodutoendereco.getEnderecoorigem() != null) {
						Endereco endereco = ordemservicoprodutoendereco.getEnderecoorigem();
						Area area = endereco.getArea();
						String areaString = area.getCodigo() < 9 ? "0" + area.getCodigo() : area.getCodigo().toString();
						mapaDados.setEndereco(new EnderecoAux(areaString,endereco.getEndereco()));
					}
					
					lista.add(mapaDados);
				}
			}else{
				MapaDados mapaDados = new MapaDados();			
				
				Produto produtoprincipal = produto.getProdutoprincipal();
				if(produtoprincipal != null && produtoprincipal.getCdproduto() != null){
					produto.setCodigo(produtoprincipal.getCodigo());
					produto.setDescricao(produtoprincipal.getDescricao());
				}
				
				mapaDados.setProduto(produto);						
				mapaDados.setQtde(ordemservicoproduto.getQtdeesperada());
				qtde += ordemservicoproduto.getQtdeesperada();
				
				lista.add(mapaDados);
				
			}
			
			Double volume = 0.0;
			if (produto.getCubagemunitaria() != null)
				volume = produto.getCubagemunitaria() * qtde;
			
			if (mapasVO.getVolumeTotal() != null)
				mapasVO.setVolumeTotal(mapasVO.getVolumeTotal() + volume);
			else
				mapasVO.setVolumeTotal(volume);
			
			mapasVO.setTotalItens(ordemservico.getListaOrdemProdutoLigacao().size());
			double peso = produto.getPesounitario() == null ? 0.0 : produto.getPesounitario();
			mapasVO.setPesoTotal(mapasVO.getPesoTotal() + (peso * qtde));
			
		}
	}
	
	/**
	 * Lista todas as ordem de serviço do carregamento especificado.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO.findByCarregamento(Carregamento carregamento)
	 * 
	 * @author Pedro Gonçalves
	 * @param carregamento
	 * @return List<Ordemservico>
	 */
	public List<Ordemservico> findByCarregamento(Carregamento carregamento){
		return ordemservicoDAO.findByCarregamento(carregamento);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Recupera o total de ordens de serviço a partir do carregamento.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#getTotalOs(Carregamento carregamento)
	 * @author Pedro Gonçalves
	 * @param carregamento
	 * @return
	 */
	public Long getTotalOs(Carregamento carregamento){
		return ordemservicoDAO.getTotalOs(carregamento);
	}
	
	/**
	 * Lê os dados e cria o relatório de divergências no recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #preparaLista(List)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#loadBy(Recebimentostatus recebimentostatus, Ordemservico ordemservico)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#findForPapelReport(Recebimento recebimento)
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 * @throws Exception
	 */
	public IReport generateReport(WebRequestContext request, Ordemservico filtro) throws Exception {
		Report report = new Report("RelatorioOrdemRecebimentoPapel");
		Ordemservico os = loadBy(Recebimentostatus.DISPONIVEL, filtro);
		if(os == null) os = loadBy(Recebimentostatus.EM_ANDAMENTO, filtro);
		if(os == null) throw new WmsException("Ordem de serviço inválida.");
		
		Recebimento ultimaVersaoBD = recebimentoService.load(os.getRecebimento());
		if (ultimaVersaoBD.getRecebimentostatus().equals(Recebimentostatus.DISPONIVEL)){
			ultimaVersaoBD.setRecebimentostatus(Recebimentostatus.EM_ANDAMENTO);
			recebimentoService.gravaStatusRecebimento(ultimaVersaoBD);
		}
		
		report.addParameter("TITULO","Ordem de recebimento: "+os.getRecebimento().getCdrecebimento()+" / "+os.getOrdem()+" - "+os.getOrdemtipo().getNome());
		report.addParameter("DTRECEBIMENTO",NeoFormater.getInstance().format(os.getRecebimento().getDtrecebimento()));
		report.addParameter("DTATUAL",NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())));
		report.addParameter("OBSERVACAO", os.getRecebimento().getObservacao());
		report.addParameter("BOX", os.getRecebimento().getBox().getNome());
		report.addParameter("USUARIORESPONSAVEL", os.getRecebimento().getUsuario() != null ? os.getRecebimento().getUsuario().getNome() : "");
		report.addParameter("TIPOVEICULO",os.getRecebimento().getTipoveiculo() != null ? os.getRecebimento().getTipoveiculo().getNome() : "");

		report.addParameter("DATA",new Date(System.currentTimeMillis()));
		report.addParameter("HORA", System.currentTimeMillis());
		report.addParameter("RODAPE", "Impresso em " + NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())) + " por " + WmsUtil.getUsuarioLogado().getNome());

		//carrega a placa
		List<Recebimentonotafiscal> listaRecebimentoNF = os.getRecebimento().getListaRecebimentoNF();
		if(listaRecebimentoNF != null && listaRecebimentoNF.size() > 0){
			Recebimentonotafiscal recebimentonotafiscal = listaRecebimentoNF.get(0);
			Notafiscalentrada notafiscalentrada = recebimentonotafiscal.getNotafiscalentrada();
			
			report.addParameter("VEICULO",notafiscalentrada.getVeiculo());
			report.addParameter("TRANSPORTADOR",notafiscalentrada.getTransportador());
			report.addParameter("DTCHEGADA",NeoFormater.getInstance().format(notafiscalentrada.getDtchegada()));
			
		}

		List<Notafiscalentrada> listaNF = NotafiscalentradaService.getInstance().findByRecebimento(os.getRecebimento());
		Map<String, StringBuilder> builders = new HashMap<String, StringBuilder>();
		for (int i = 0; i < listaNF.size(); i++){
			StringBuilder notasFiscais;

			if (builders.containsKey(listaNF.get(i).getFornecedor().getNome()))
				notasFiscais = builders.get(listaNF.get(i).getFornecedor().getNome());
			else{
				notasFiscais = new StringBuilder();
				builders.put(listaNF.get(i).getFornecedor().getNome(), notasFiscais);
			}
			
			if (notasFiscais.length() > 0)
				notasFiscais.append(", ");
			
			notasFiscais.append(listaNF.get(i).getNumero());
		}
		
		StringBuilder listaCompleta = new StringBuilder();
		for (Entry<String, StringBuilder> entry : builders.entrySet()){
			listaCompleta.append(entry.getKey());
			listaCompleta.append(": ");
			listaCompleta.append(entry.getValue().toString());
			listaCompleta.append("\n");
		}
		
		report.addParameter("NOTAS",listaCompleta.toString());

		List<RecebimentoPapelReportVO> listaItens = ordemprodutohistoricoService.findForPapelReport(os.getRecebimento(), os);

		report.addParameter("TOTAL", listaItens.size());
		report.setDataSource(listaItens);
		Image image = null;
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
		} catch (IOException e) {
			e.printStackTrace();
		}
		report.addParameter("LOGO", image);
		return report;
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Insere ou atualiza o bean de ordem serviço.
	 * 
	 * @author Arantes
	 * 
	 * @param bean
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#saveOrUpdateBean(Ordemservico)
	 * 
	 */	
	public void saveOrUpdateBean(Ordemservico bean) {
		ordemservicoDAO.saveOrUpdateBean(bean);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#loadPenultimaConferencia(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @return
	 */
	public Ordemservico loadPenultimaConferencia(Recebimento recebimento) {
		return ordemservicoDAO.loadPenultimaConferencia(recebimento);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @param listaOs
	 * @return Ordemservico
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByFiltroListaOrdemservicoproduto(LancarcorteFiltro, String) 
	 * 
	 */
	/*public Ordemservico findByFiltroListaOrdemservicoproduto(LancarcorteFiltro filtro, String listaOsProduto) {
		return ordemservicoDAO.findByFiltroListaOrdemservicoproduto(filtro, listaOsProduto);
	}*/
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Lista todas as ordem de serviço do carregamento especificado.
	 * Os dados são carregados para a tela de gerenciamento do carregamento.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByCarregamentoToGerenciamento(Carregamento carregamento)
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByCarregamentoToGerenciamento(Carregamento carregamento){
		return ordemservicoDAO.findByCarregamentoToGerenciamento(carregamento);
	}
	
	/**
	 * Método de referência ao DAO.
	 * Método que recupera uma lista de ordens de serviço à partir de uma lista de id´s de ordens de serviço produto.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByListaOrdemservico(String)
	 * 
	 * @author Arantes
	 * 
	 * @param listaOs
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByListaOrdemservico(String listaOs) {
		return ordemservicoDAO.findByListaOrdemservico(listaOs);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Atualiza o status da ordem de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#updateStatusordemservico(Ordemservico)|
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * 
	 */
	public void updateStatusordemservico(Ordemservico ordemservico) {
		ordemservicoDAO.updateStatusordemservico(ordemservico);
		
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Carrega todas as ordens de serviço que são do tipo Reconferência para carregar o menu
	 * de escolha da ordem de serviço do coletor. 
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByCarregamentoToConferencia(Carregamento carregamento)
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @param usuario 
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByCarregamentoToReconferencia(Carregamento carregamento, Usuario usuario){
		List<Ordemservico> listaOS = findByCarregamentoToRF(carregamento,Ordemtipo.RECONFERENCIA_EXPEDICAO_1, usuario);
		List<Ordemservico> listaOS2 = findByCarregamentoToRF(carregamento,Ordemtipo.RECONFERENCIA_EXPEDICAO_2, usuario);

		listaOS.addAll(listaOS2);
		
		return listaOS;
	}
	
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Carrega todas as ordens de serviço que são do tipo Conferência para carregar o menu
	 * de escolha da ordem de serviço do coletor. 
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByCarregamentoToConferencia(Carregamento carregamento)
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @param usuario 
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByCarregamentoToConferencia(Carregamento carregamento, Usuario usuario){
		List<Ordemservico> listaOS = findByCarregamentoToRF(carregamento,Ordemtipo.CONFERENCIA_EXPEDICAO_1, usuario);
		
		if (listaOS == null || listaOS.isEmpty())
			listaOS = findByCarregamentoToRF(carregamento,Ordemtipo.CONFERENCIA_EXPEDICAO_2, usuario);
		
		return listaOS;
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Carrega todas as ordens de serviço que são do tipo especificado para carregar o menu
	 * de escolha da ordem de serviço do coletor. 
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByCarregamentoToConferencia(Carregamento carregamento)
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @param usuario 
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByCarregamentoToRF(Carregamento carregamento,Ordemtipo ordemtipo, Usuario usuario){
		return ordemservicoDAO.findByCarregamentoToRF(carregamento, ordemtipo, usuario);
	}

	/**
	 * Método de referência ao DAO.
	 * 
	 * Carrega todas as ordens de serviço que são do tipo especificado para carregar o menu
	 * de escolha da ordem de serviço do coletor. 
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByCarregamentoToConferencia(Carregamento carregamento)
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @param usuario 
	 * @return List<Ordemservico>
	 * 
	 */
	public Ordemservico findByCarregamentoToRF(Ordemservico ordemservico, Usuario usuario){
		return ordemservicoDAO.findByCarregamentoToRF(ordemservico, usuario);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * 
	 * Método que recupera o status e o tipo de uma ordem de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadStatusTipoByOrdemservico(Ordemservico)
	 * 
	 * @author Tomás
	 * 
	 * @param whereIn
	 * @return Ordemservico
	 * 
	 */
	public List<Ordemservico> loadStatusTipoByOrdemservicoWhereIn(String whereIn) {
		return ordemservicoDAO.loadStatusTipoByOrdemservicoWhereIn(whereIn);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que recupera o tipo e o status da ordem de serviço. 
	 * Também recupera o carregamento que ela pertence
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadTipostatuscarregamento(Ordemservico)
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return Ordemservico
	 * 
	 */
	public Ordemservico loadTipostatuscarregamento(Ordemservico ordemservico) {
		return ordemservicoDAO.loadTipostatuscarregamento(ordemservico);
	}
	
	/**
	 * 
	 * Método que valida se o filtro está preenchido
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return Boolean
	 *  
	 */
	public Boolean validarFiltroObrigatorio(LancarcorteFiltro filtro) {		
		if(filtro == null)
			return Boolean.FALSE;
		
		if(filtro.getCdordemservico() == null)
			return Boolean.FALSE;
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Método que valida campos obrigatórios
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return Boolean
	 *  
	 */
	public String validarCamposObrigatorios(LancarcorteFiltro filtro) {
		String msg = "";
		
		if(filtro.getCdordemservico() == null)
			msg += "O campo Ordem serviço é obrigatório.\n";
			
		if((filtro.getUsuario() == null) || (filtro.getUsuario().getCdpessoa() == null))
			msg += "O campo Responsável é obrigatório.\n";
			
		if((filtro.getHrinicio() != null) && (filtro.getData() == null)) 
			msg += "O campo Data é obrigatório.\n";
			
		if(filtro.getHrfim() != null) {
			if((filtro.getData() == null) && (filtro.getHrinicio() == null))
				msg += "O campo Data é obrigatório.\n";
				
			if(filtro.getHrinicio() == null)
				msg += "O campo Hora início é obrigatório.\n";				
		}
		
		return msg;
	}
	
	/**
	 * 
	 * Método que valida o tipo da ordem de serviço.
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return Boolean
	 * 
	 */
	public Boolean validarOrdemservico(Ordemservico ordemservico) {
		if((ordemservico == null) || (ordemservico.getCdordemservico() == null))
			return Boolean.FALSE;
			
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Método que valida o tipo da ordem de serviço.
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return Boolean
	 * 
	 */
	public Boolean validarTipoordemservico(Ordemservico ordemservico) {
		if((ordemservico.getOrdemtipo() == null) || (!ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO)) && (!ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1))) 
			return Boolean.FALSE;
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Método que atualiza o status da ordem de serviço de acordo com os status das ordens de serviço do produto
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#updateStatusordemservico(Ordemservico)
	 * 
	 * @author Arantes 
	 * 
	 * @param listaOrdemservico
	 * 
	 */
	public void definirStatusordemservico(List<Ordemservico> listaOrdemservico) {
		for (Ordemservico ordemservico : listaOrdemservico) {	
			Boolean isFinalizado_ok = Boolean.TRUE;
			
			for (Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()) {
				Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
				
				if(ordemservicoproduto.getOrdemprodutostatus().equals(Ordemprodutostatus.CONCLUIDO_DIVERGENTE)) {
					isFinalizado_ok = Boolean.FALSE;
					break;
				}
			}
			
			ordemservico.setOrdemstatus(isFinalizado_ok ? Ordemstatus.FINALIZADO_SUCESSO 
														: Ordemstatus.FINALIZADO_DIVERGENCIA);			
			ordemservicoDAO.updateStatusordemservico(ordemservico);
		}
	}
	
	/* singleton */
	private static OrdemservicoService instance;
	public static OrdemservicoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemservicoService.class);
		}
		return instance;
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#updateStatusordemservicoByCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 */
	public void updateStatusordemservicoByCarregamento(Carregamento carregamento) {
		ordemservicoDAO.updateStatusordemservicoByCarregamento(carregamento);
	}
	
	/**
	 * Gera o relatorio de impressão das ordens de tranferência
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #generateReportByOrdemtipo(Report, Ordemtipo, Object)
	 * 
	 * @param filtro
	 * @return
	 */
	public IReport generateOrdemTransferencia(ImpressaoOrdemTransferenciaFiltro filtro) {
		Report report = new Report("RelatorioOrdemTransferencia");
		generateReportByOrdemtipo(report, Ordemtipo.TRANSFERENCIA, filtro.getTransferencia());
		return report;
	}
	
	/**
	 * Gera o relatorio de impressão das ordens de rabastecimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #generateReportByOrdemtipo(Report, Ordemtipo, Object)
	 * 
	 * @param filtro
	 * @return
	 */
	public IReport generateOrdemReabastecimento(ImpressaoOrdemReabastecimentoFiltro filtro) {
		Report report = new Report("RelatorioOrdemReabastecimento");
		
		Object obj;
		if (filtro.getCarregamento() != null)
			obj = filtro.getCarregamento();
		else if (filtro.getExpedicao() != null)
			obj = filtro.getExpedicao();
		else
			obj = filtro.getOrdens();
		
		generateReportByOrdemtipo(report, Ordemtipo.REABASTECIMENTO_PICKING, obj);
		return report;
	}
	
	/**
	 * Busca os dados e cria seta no report
	 * 
	 * @author Leonardo Guimarães
	 *
	 * @see #findForOrdemReabastecimentoReport(Carregamento, Ordemtipo)) 
	 * 
	 * @param report
	 * @param ordemtipo
	 * @param obj
	 */
	public void generateReportByOrdemtipo(Report report,Ordemtipo ordemtipo,Object obj){
		List<Ordemservico> listaOS = findForOrdemReabastecimentoReport(obj,ordemtipo);
		List<OrdemVO> listaOrdemReabastecimento = new ArrayList<OrdemVO>();

		if(obj instanceof Expedicao){
			Expedicao expedicao = ExpedicaoService.getInstance().getExpedicao((Expedicao) obj);
			String listaCarregamentos = CollectionsUtil.listAndConcatenate(expedicao.getListaCarregamento(), "cdcarregamento", ", ");
			report.addParameter("LISTA_CARREGAMENTO", listaCarregamentos);
		}
		
		for (Ordemservico ordemservico : listaOS) {

			for (Ordemprodutohistorico ordemprodutohistorico : ordemservico.getListaOrdemProdutoHistorico()) {
				Ordemservicoproduto ordemservicoproduto = ordemprodutohistorico.getOrdemservicoproduto();
				Produto produto = ordemprodutohistorico.getOrdemservicoproduto().getProduto();
				Dadologistico dadologistico = dadologisticoService.findByProduto(produto, WmsUtil.getDeposito());
				
				ordemservicoproduto.setListaOrdemservicoprodutoendereco(ordemservicoprodutoenderecoService.findByOrdemServicoProduto(ordemservicoproduto));
				for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : ordemservicoproduto.getListaOrdemservicoprodutoendereco()) {
					OrdemVO ordemVO = new OrdemVO();
					ordemVO.setOs(ordemservico.getCdordemservico());
					ordemVO.setCarregamento(ordemservico.getCarregamento() != null ? ordemservico.getCarregamento().getCdcarregamento() : null);
					ordemVO.setTransferencia(ordemservico.getTransferencia() != null ? ordemservico.getTransferencia().getCdtransferencia() : null);
					ordemVO.setProduto(produto.getCodigo()+" - "+produto.getDescricao());
					
					if(dadologistico.getLinhaseparacao() != null && dadologistico.getLinhaseparacao().getCdlinhaseparacao() != null)
						ordemVO.setLinhaSeparacao(dadologistico.getLinhaseparacao().getCdlinhaseparacao()+" - "+dadologistico.getLinhaseparacao().getNome());
					else
						ordemVO.setLinhaSeparacao("Não definido");
					
					if (ordemservicoprodutoendereco.getEnderecoorigem() != null)
						ordemVO.setEnderecoOrigem(new EnderecoAux(ordemservicoprodutoendereco.getEnderecoorigem().getArea().getCodigo().toString(),ordemservicoprodutoendereco.getEnderecoorigem().getEndereco()));
					else
						ordemVO.setEnderecoOrigem(new EnderecoAux());
					
					if (ordemservicoprodutoendereco.getEnderecodestino() != null)
						ordemVO.setEnderecoDestino(new EnderecoAux(ordemservicoprodutoendereco.getEnderecodestino().getArea().getCodigo().toString(),ordemservicoprodutoendereco.getEnderecodestino().getEndereco()));
					else
						ordemVO.setEnderecoDestino(new EnderecoAux());
					
					ordemVO.setQtde(ordemservicoprodutoendereco.getQtde());

					Produtoembalagem produtoembalagem = produtoembalagemService.findCompraByProduto(produto);
					//Se a quantidade não é múltiplo da embalagem de recebimento então usa a menor embalagem
					if (produtoembalagem == null || (ordemVO.getQtde() % produtoembalagem.getQtde() != 0))
						produtoembalagem = produtoembalagemService.findMenorEmbalagem(produto);;
					
					if(produtoembalagem != null){
						ordemVO.setQtde(ordemVO.getQtde() / produtoembalagem.getQtde());
						ordemVO.setEmbalagem(produtoembalagem.getDescricao() + " - " + produtoembalagem.getQtde());
					}
					listaOrdemReabastecimento.add(ordemVO);
				}
			}
		}
		report.addParameter("data", NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())));
		report.setDataSource(listaOrdemReabastecimento);
	}
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findForOrdemReabastecimentoReport(Carregamento carregamento)
	 * 
	 * @param carregamento
	 * @return
	 */
	private List<Ordemservico> findForOrdemReabastecimentoReport(Object obj,Ordemtipo ordemtipo) {
		return ordemservicoDAO.findForOrdemReabastecimentoReport(obj,ordemtipo);
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Procura todos os mapas de separação da ordem de serviço do tipo conferência e dá um update no campo status.
	 * 
	 * @param ordemservico 
	 * @param onlyDivergentes -  true - Status é setado como finalizado com divergência, e pega apenas as OSP que estão como concluido divergente.
	 *                           false - Seta como finalizado com sucesso.
	 *                           
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#alterarMapasByOSConferencia(Ordemservico, Boolean)
	 * @author Pedro Gonçalves
	 */
	public void alterarMapasByOSConferencia(Ordemservico ordemservico, Boolean onlyDivergentes){
		ordemservicoDAO.alterarMapasByOSConferencia(ordemservico, onlyDivergentes);
	}
	
	/**
	 * Método de referência ao DAO.
	 * A lista será ordenada de acordo com o caso de uso, sendo a ordem:
	 * <ol>
	 * 	<li>Ordem de endereçamento avariado</li>
	 *	<li>Ordem de endereçamento fracionado</li>
	 *	<li>Ordem de endereçamento UMA</li>
	 *	<li>Ordem de conferência</li>
	 *	<li>Ordem de reconferencia;</li>
	 * </ol>
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByRecebimento(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Ordemservico> findByRecebimento(Recebimento recebimento) {
		List<Ordemservico> listaOrdemservico = ordemservicoDAO.findByRecebimento(recebimento);
		
		final List<Integer> ordem = new ArrayList<Integer>();
		ordem.add(Ordemtipo.ENDERECAMENTO_AVARIADO.getCdordemtipo());
		ordem.add(Ordemtipo.ENDERECAMENTO_FRACIONADO.getCdordemtipo());
		ordem.add(Ordemtipo.ENDERECAMENTO_PADRAO.getCdordemtipo());
		ordem.add(Ordemtipo.CONFERENCIA_RECEBIMENTO.getCdordemtipo());
		ordem.add(Ordemtipo.RECONFERENCIA_RECEBIMENTO.getCdordemtipo());
		
		Comparator<Ordemservico> comparator = new Comparator<Ordemservico>(){
			
			public int compare(Ordemservico o1, Ordemservico o2) {
				Integer indexO1 = ordem.indexOf(o1.getOrdemtipo().getCdordemtipo());
				Integer indexO2 = ordem.indexOf(o2.getOrdemtipo().getCdordemtipo());
				
				return indexO1.compareTo(indexO2);
			}
			
		};
		
		Collections.sort(listaOrdemservico, comparator);
		
		return listaOrdemservico;
	}
	
	/**
	 * Cria o relatório das ordens de armazenagem
	 * 
	 * @author Leonardo Guimarães
	 * @author Giovane Freitas
	 * 
	 * @see #findForReportRecebimento(Recebimento, String)
	 * 
	 * @param filtro
	 * @return
	 * @throws Exception 
	 */
	public IReport generateReportRecebimento(ImpressaoOrdemRecebimentoFiltro filtro) throws Exception {
		Report report = new Report("RelatorioOrdemArmazenagem");
		filtro.setCdOs(filtro.getCdOs().substring(0,filtro.getCdOs().lastIndexOf(",") ));

		List<OrdemVO> ordemVoList;
		
		ordemVoList = this.findForReportArmazenagem(filtro.getCdOs());

		report.setDataSource(ordemVoList);
		
		report.addParameter("data", NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())));
		return report;
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Recupera as ordens de serviço de um determinado recebimento.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByRecebimentoUma(EtiquetaumaFiltro)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByRecebimentoUma(EtiquetaumaFiltro filtro) {
		return ordemservicoDAO.findByRecebimentoUma(filtro);
	}

	/**
	 * Método de referência ao DAO 
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO.findByLote(Inventariolote inventariolote)
	 * 
	 * @param inventariolote
	 * @return
	 */
	public List<Ordemservico> findByLote(Inventariolote inventariolote) {
		return ordemservicoDAO.findByLote(inventariolote);
	}	
	
	/**
	 * Método de referência ao DAO 
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param reabastecimentolote
	 * @return
	 */
	public List<Ordemservico> findByLote(Reabastecimentolote reabastecimentolote) {
		return ordemservicoDAO.findByLote(reabastecimentolote);
	}	
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que recupera uma lista de ordens de serviço para gerar o relatório de ordem de serviço de contagem no inventário.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByOrdemservico(OrdemcontagemFiltro)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Ordemservico>
	 *  
	 */
	private List<Ordemservico> findByOrdemservico(Inventario filtro) {
		return ordemservicoDAO.findByOrdemservico(filtro);
	}
	
	
	//*************Inventário**************//
	/**
	 * 
	 * Método que trata e recupera um endereço no formato rua.prédio (000.000)
	 * 
	 * @author Arantes
	 * 
	 * @param enderecoAlvo
	 * @return EnderecoAux
	 * 
	 */
	private EnderecoAux recuperaEndereco(Endereco enderecoAlvo) {
		String[] enderecoSplit = enderecoAlvo.getEndereco().split("\\.");
		String endereco = enderecoSplit[0] + "." + enderecoSplit[1];
		
		return new EnderecoAux(enderecoAlvo.getArea().getCodigo().toString(), endereco);
	}
	
	/**
	 * 
	 * Método que recupera um produto
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservicoproduto
	 * @param contagemInventarioVO
	 * @return Produto
	 * 
	 */
	private Produto configuraProdutoProdutoEmbalagem(Ordemservicoproduto ordemservicoproduto, OrdemcontageminventarioVO contagemInventarioVO) {
		Produto produto = ordemservicoproduto.getProduto();
		
		if(produto == null) {
			contagemInventarioVO.setCodigodescricaoproduto("");
			contagemInventarioVO.setDescricaoprodutoembalagem("");
		
		} else {
			if (produto.getProdutoprincipal() != null)
				contagemInventarioVO.setCodigodescricaoproduto(produto.getCodigo() + " " + produto.getProdutoprincipal().getDescricao() + " - " + produto.getDescricao());
			else
				contagemInventarioVO.setCodigodescricaoproduto(produto.getCodigo() + " " + produto.getDescricao());
			
			if((produto != null) && (produto.getListaProdutoEmbalagem() != null) && (!produto.getListaProdutoEmbalagem().isEmpty())) {
				contagemInventarioVO.setDescricaoprodutoembalagem(produto.getListaProdutoEmbalagem().get(0).getDescricao());
			
			} else {
				contagemInventarioVO.setDescricaoprodutoembalagem("");
				
			}
		}
		
		return produto;
	}
	
	/**
	 * 
	 * Método que configura o vo de contagem inventário com o endereço blocado.
	 * 
	 * @author Arantes
	 * 
	 * @param listaContagemInventario
	 * @param listaProdutoblocado
	 * @param contagemInventarioVO
	 * @param produto
	 * @param enderecoAlvo
	 * 
	 */
	private void configuraEnderecoBlocado(List<OrdemcontageminventarioVO> listaContagemInventario, List<String> listaProdutoblocado, OrdemcontageminventarioVO contagemInventarioVO, Produto produto, Endereco enderecoAlvo) {
		String key = enderecoAlvo.getArea().getCodigo() + "." + enderecoAlvo.getEndereco().substring(0, 7);
		
		if(!listaProdutoblocado.contains(key) || produto != null) {
			EnderecoAux enderecoaux = recuperaEndereco(enderecoAlvo);
			contagemInventarioVO.setEnderecoaux(enderecoaux);
			
			for (OrdemcontageminventarioVO ordemcontageminventarioVO : listaContagemInventario) {
				if(!ordemcontageminventarioVO.getCdordemservico().equals(contagemInventarioVO.getCdordemservico())) {
					continue;
				
				} else {
					if(ordemcontageminventarioVO.getEnderecoaux().getEnderecoComPonto().equals(contagemInventarioVO.getEnderecoaux().getEnderecoComPonto())
							&& ordemcontageminventarioVO.getCodigodescricaoproduto().equals(contagemInventarioVO.getCodigodescricaoproduto())) {
						if(ordemcontageminventarioVO.getDescricaoprodutoembalagem().isEmpty()) {
							listaContagemInventario.remove(ordemcontageminventarioVO);
							break;
						}
					}
				}
			}
			
			listaContagemInventario.add(contagemInventarioVO);
		}

		listaProdutoblocado.add(key);
	}
	
	/**
	 * 
	 * Método para a criação do relatório de ordem de serviço de contagem no inventário.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return IReport
	 * 
	 */
	public IReport generateReportOrdemcontagem(Inventario filtro) {
		Report report = new Report("RelatorioOrdemContagemInventario");
		filtro.setOrdens(filtro.getOrdens().substring(0, filtro.getOrdens().length()-1));
		List<Ordemservico> listaOrdemservico = this.findByOrdemservico(filtro);
		List<OrdemcontageminventarioVO> listaContagemInventario = new ArrayList<OrdemcontageminventarioVO>();
		List<String> listaProdutoblocado = null;
		
		
		if((listaOrdemservico != null) && (!listaOrdemservico.isEmpty())) {
			for (Ordemservico ordemservico : listaOrdemservico) {
				listaProdutoblocado = new ArrayList<String>();
				
				
				for (Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()) {
					Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
					
					for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : ordemservicoproduto.getListaOrdemservicoprodutoendereco()) {
						OrdemcontageminventarioVO contagemInventarioVO = new OrdemcontageminventarioVO();
						
						contagemInventarioVO.setCdordemservico(ordemservico.getCdordemservico());
						contagemInventarioVO.setOrdem(ordemservico.getOrdem());
						contagemInventarioVO.setCdinventario(ordemservico.getInventariolote().getInventario().getCdinventario());
						
						Produto produto = configuraProdutoProdutoEmbalagem(ordemservicoproduto, contagemInventarioVO);
						
						Endereco enderecoAlvo = ordemservicoprodutoendereco.getEnderecodestino();						
						
						if(enderecoAlvo.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)) {
							configuraEnderecoBlocado(listaContagemInventario, listaProdutoblocado, contagemInventarioVO, produto, enderecoAlvo);
						
						} else {
							EnderecoAux enderecoaux = new EnderecoAux(enderecoAlvo.getArea().getCodigo().toString(), enderecoAlvo.getEndereco());
							contagemInventarioVO.setEnderecoaux(enderecoaux);

							listaContagemInventario.add(contagemInventarioVO);
						}
					}
				}
			}
		}
		
		Inventario inventarioAux = inventarioService.load(filtro);
		Boolean fracionada = inventarioAux.getListaInventariolote().iterator().next().getFracionada();
		if (fracionada)
			report.addParameter("TIPO_EMBALAGEM", "Fracionada");
		else
			report.addParameter("TIPO_EMBALAGEM", "Embalagem de recebimento");
		
		report.setDataSource(listaContagemInventario);		
		return report;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findContagemByInventarioLote(Inventariolote inventariolote)
	 * 
	 * @param inventariolote
	 * @return
	 */
	public List<Ordemservico> findContagemByInventarioLote(Inventariolote inventariolote) {
		return ordemservicoDAO.findContagemByInventarioLote(inventariolote);
	}
	
	/**
	 * Faz a verificação se o produto e endereço tem largura excedente, o endereço é impar,
	 * e inicializa o campo endereco vizinho de endereço ( implementação da regra de negócio c, lançar dados lote)
	 * @param listaHistorico
	 * @author Cíntia Nogueira
	 */
	private void  verificaEnderecoEmprestadoComProduto(List<Ordemprodutohistorico> listaHistorico){
		for( Ordemprodutohistorico oph : listaHistorico){
			Produto produto = oph.getOrdemservicoproduto().getProduto();
			
			if (produto == null)
				continue;
			
			if(dadologisticoService.verificaLarguraExcedente(produto.getListaDadoLogistico())){
				for(Ordemservicoprodutoendereco ospe: oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco()){
					Integer apart = Integer.parseInt(ospe.getEnderecodestino().getEndereco().substring(ospe.getEnderecodestino().getEndereco().length()-3));
					if(ospe.getEnderecodestino().getLarguraexcedente()!=null && ospe.getEnderecodestino().getLarguraexcedente() && (apart%2!=0)){						
						Endereco endereco = ospe.getEnderecodestino();
						Endereco enderecoVizinho = new Endereco();
						enderecoVizinho.setArea(endereco.getArea());
						EnderecoAux enderecoAux = new EnderecoAux(endereco.getEndereco());
						enderecoVizinho.setEndereco(enderecoAux.getEnderecoVizinho());
						endereco.setEnderecovizinho(enderecoVizinho);
						endereco.setHasVizinhoExcedente(true);
					}
				}
			}
		}
		
	}
	
	/**
	 * Cria um novo Ordemprodutohistorico, se o endereço de destino tem um vizinho,
	 * enderecovizinho ( implementação da regra de negócio c, lançar dados lote)
	 * @param listaHistorico
	 * @return
	 * @author Cíntia Nogueira
	 */
	private ListSet<Ordemprodutohistorico>criaEnderecoProdutoVizinho(List<Ordemprodutohistorico> listaHistorico){
		ListSet<Ordemprodutohistorico> listaOPH= new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class);
		for( Ordemprodutohistorico oph: listaHistorico){
			listaOPH.add(oph);
			ListSet<Ordemservicoprodutoendereco> listaOSPE= new ListSet<Ordemservicoprodutoendereco>(Ordemservicoprodutoendereco.class, 
					oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco());
			if(listaOSPE!=null && !listaOSPE.isEmpty()){
				Ordemservicoprodutoendereco ospe = listaOSPE.get(0);
				if(ospe.getEnderecodestino().getEnderecovizinho()!=null){
					Ordemprodutohistorico ophVizinho = new Ordemprodutohistorico();
					ophVizinho.setOrdemservico(oph.getOrdemservico());
					
					ophVizinho.setOrdemservicoproduto(new Ordemservicoproduto());
					ophVizinho.getOrdemservicoproduto().setProduto(oph.getOrdemservicoproduto().getProduto());
					ListSet<Ordemservicoprodutoendereco> listaOSPEVizinho= new ListSet<Ordemservicoprodutoendereco>(Ordemservicoprodutoendereco.class);
					Ordemservicoprodutoendereco ospeVizinho = new Ordemservicoprodutoendereco();	
					ospeVizinho.setEnderecodestino(ospe.getEnderecodestino().getEnderecovizinho());
					ospeVizinho.getEnderecodestino().setListaEnderecoproduto(new ListSet<Enderecoproduto>(Enderecoproduto.class));
					ospeVizinho.getEnderecodestino().setEnderecofuncao(Enderecofuncao.PICKING);//Para não aparecer o botão de inserir
					Enderecoproduto enderecoproduto = new Enderecoproduto();
					enderecoproduto.setQtde(0L);
					ospeVizinho.getEnderecodestino().getListaEnderecoproduto().add(enderecoproduto);
					listaOSPEVizinho.add(ospeVizinho);
					ophVizinho.getOrdemservicoproduto().setListaOrdemservicoprodutoendereco(listaOSPEVizinho);
				
					listaOPH.add(ophVizinho);
				}
			}
		}
		return listaOPH;
	}
	
	/**
	 * Prepara os dados para a entrada da tela Lancar dados do lote
	 * 
	 * @author Leonardo Guimarães
	 * @author Cíntia Nogueira
	 * 
	 * @see br.com.linkcom.wms.geral.service.InventarioService#findByOrdemservico(Ordemservico ordemservico)
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#findForLancarDados(Ordemservico ordemservico)
	 * @see #criaEnderecoProdutoVizinho(List)
	 * @see #verificaEnderecoEmprestadoComProduto(List)
	 * @param filtro
	 */
	public void prepareEntrada(LancarDadosLotesFiltro filtro) {
		filtro.setInventario(inventarioService.findByOrdemservico(filtro.getOrdemservico()));
		
		Boolean fracionada = filtro.getInventario().getListaInventariolote().iterator().next().getFracionada();
		
		ListSet<Ordemprodutohistorico> listaHistorico= new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class,
				ordemprodutohistoricoService.findForLancarDados(filtro.getOrdemservico()));
		verificaEnderecoEmprestadoComProduto(listaHistorico);
		listaHistorico=criaEnderecoProdutoVizinho(listaHistorico);
		List<Ordemprodutohistorico> lista = ordemprodutohistoricoService.agrupaOrdemProdutoHistoricoBlocado(listaHistorico);
		
		List<Ordemprodutohistorico> listaOPHPenultimaOS = null;
		if (filtro.getOrdemservico().getOrdemservicoprincipal() != null)
			listaOPHPenultimaOS = ordemprodutohistoricoService.findForLancarDados(filtro.getOrdemservico().getOrdemservicoprincipal());
		
		for (Ordemprodutohistorico oph : lista){
			Produto produto = oph.getOrdemservicoproduto().getProduto();
			
			if (produto == null)
				continue;
			
			oph.getOrdemservicoproduto().setQtdeFracionada(oph.getOrdemservicoproduto().getQtdeesperada());
			
			Endereco endereco = oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino();
			
			if (produto != null && produto.getCdproduto() != null && endereco != null && endereco.getCdendereco() != null
					&& !oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().isEmpty()
					&& oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino() != null){
				
				Set<Enderecoproduto> listaEnderecoproduto = new ListSet<Enderecoproduto>(Enderecoproduto.class);
				listaEnderecoproduto.add(enderecoprodutoService.findByEnderecoAndProduto(endereco, produto));
				oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino().setListaEnderecoproduto(listaEnderecoproduto );
			}
			
			if (listaOPHPenultimaOS != null && listaOPHPenultimaOS.size() > 0){
				List<Ordemprodutohistorico> listaItensIguais = findOrdemProdutoHistorico(listaOPHPenultimaOS, produto, endereco, TipoEndereco.Destino);
				long qtdeAnterior = 0L;
				for (Ordemprodutohistorico ophAux : listaItensIguais)
					if (ophAux.getQtde() != null)
						qtdeAnterior += ophAux.getQtde();

				oph.setQtdeColetada(qtdeAnterior);
			}

			if (!fracionada && !Enderecofuncao.PICKING.equals(endereco.getEnderecofuncao())){
				long qtdeEsperada = 0L;
				if (oph.getOrdemservicoproduto().getQtdeesperada() != null)
					qtdeEsperada = oph.getOrdemservicoproduto().getQtdeesperada();
					
				Produtoembalagem embalagem = produtoembalagemService.findCompraByProduto(produto);
				oph.setProdutoembalagem(embalagem);
				if (embalagem != null && (qtdeEsperada % embalagem.getQtde()) == 0L){
					oph.getOrdemservicoproduto().setQtdeesperada(qtdeEsperada / embalagem.getQtde());
					if(oph.getQtde() != null)
						oph.setQtde(oph.getQtde() / embalagem.getQtde());
					if(oph.getQtdeColetada() != null)
						oph.setQtdeColetada(oph.getQtdeColetada() / embalagem.getQtde());
				}else{
					embalagem = produtoembalagemService.findMenorEmbalagem(produto);
					oph.setProdutoembalagem(embalagem);
				}
			}else{
				Produtoembalagem embalagem = produtoembalagemService.findMenorEmbalagem(produto);
				oph.setProdutoembalagem(embalagem);
			}
		}
		
		filtro.setListaOrdemProdutoHistorico(lista);
	}
	
	@Override
	public void delete(Ordemservico bean) {
		ordemprodutoligacaoService.deleteOrdemProdutoLigacao(bean);
		ordemprodutohistoricoService.deleteByOrdemservico(bean);
		ordemservicoDAO.delete(bean);
	}

	//*********************************//
	
	//*******Endereçamento Manual RF ************//
	
	/**
	 * Associa o usuário à ordem de serviço e muda seu status para em execução
	 * 
	 * @author Leonardo Guimarães
	 * @param horaFim 
	 * @param horaInicio 
	 * 
	 */
	public void associarUsuarioOs(Usuario usuario, Ordemservico ordemservico, Timestamp horaInicio, Timestamp horaFim) {
		ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
		OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
		ordemservicoUsuario.setDtinicio(horaInicio);
		ordemservicoUsuario.setDtfim(horaFim);
		ordemservicoUsuario.setUsuario(usuario);
		ordemservicoUsuario.setOrdemservico(ordemservico);
		ordemservicousuarioService.saveOrUpdateNoUseTransaction(ordemservicoUsuario);
	}

	//*****************************************//
	//************Transferência RF*************//
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findForTransferencia(Linhaseparacao linhaseparacao)
	 * 
	 * @param linhaseparacao 
	 * 
	 */
	public List<Ordemservico> findForTransferencia(Linhaseparacao linhaseparacao,Deposito deposito, Usuario usuario) {
		return ordemservicoDAO.findForTransferencia(linhaseparacao,deposito, usuario);
	}

	//********************************************//
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByTransferencia(Transferencia transferencia)
	 * 
	 */
	public List<Ordemservico> findByTransferencia(Transferencia transferencia) {
		return ordemservicoDAO.findByTransferencia(transferencia,null);
	}
	
	/**
	 * Busca as ordens de serviço da transferência que estejam com o status 
	 * especificado.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByTransferencia(Transferencia transferencia)
	 * 
	 */
	public List<Ordemservico> findByTransferencia(Transferencia transferencia,Ordemstatus ordemstatus) {
		return ordemservicoDAO.findByTransferencia(transferencia,ordemstatus);
	}
	
	/**
	 * Método de referência ao DAO
	 *
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findByCarregamentoAndOrdemTipo(Carregamento carregamento, Ordemtipo ordemtipo)
	 * 
	 * @param carregamento
	 * @param ordemReabastecimento
	 */
	public List<Ordemservico> findByCarregamentoAndOrdemTipo(Carregamento carregamento,Ordemtipo ordemtipo) {
		return ordemservicoDAO.findByCarregamentoAndOrdemTipo(carregamento,ordemtipo);
	}
	
	/**
	 * Busca a ordem de serviço e carrega os dados de inventário
	 * 
	 * @author Pedro Gonçalves
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadAllOSInfo(Ordemservico ordemservico, Usuario usuario){
		return ordemservicoDAO.loadAllOSInfo(ordemservico, usuario);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param substring
	 * @param ordemservico
	 */
	public void updateOrdemServicoPrincipal(String ordens,Ordemservico ordemservico) {
		ordemservicoDAO.updateOrdemServicoPrincipal(ordens,ordemservico);	
	}
	
	/**
	 * Verifica se o Mapa possui alguma ordem de reconferencia
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Boolean hasReconferencia(Ordemservico ordemservico) {
		List<Ordemservico> lista = findPrincipalAndTipo(ordemservico.getOrdemservicoprincipal(),Ordemtipo.RECONFERENCIA_EXPEDICAO_1);
		if(lista == null || lista.isEmpty())
			return false;
		return true;
	}	
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#findPrincipalAndTipo(Ordemservico ordemservico, Ordemtipo ordemtipo)
	 * 
	 * @param ordemservico
	 * @param ordemtipo
	 * @return
	 */
	public List<Ordemservico> findPrincipalAndTipo(Ordemservico ordemservico,Ordemtipo ordemtipo) {
		return ordemservicoDAO.findPrincipalAndTipo(ordemservico,ordemtipo);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#updateStatusOrdemservicoByLote(Inventariolote lote, Ordemstatus ordemstatus)
	 * 
	 * @param inventario
	 * @param cancelado
	 */
	public void updateStatusOrdemservicoByLote(Inventariolote lote,Ordemstatus ordemstatus) {
		ordemservicoDAO.updateStatusOrdemservicoByLote(lote,ordemstatus);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#loadByInventarioLote(Inventariolote)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * 
	 */
	public List<Ordemservico> loadByInventarioLote(Inventariolote filtro) {
		return ordemservicoDAO.loadByInventarioLote(filtro);
	}

	/**
	 * Método de transferência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param linhaseparacao
	 * @param deposito
	 * @param usuario 
	 * @return
	 */
	public List<Ordemservico> findForReabastecimento(Linhaseparacao linhaseparacao, Deposito deposito, Usuario usuario) {
		return ordemservicoDAO.findForReabastecimento(linhaseparacao,deposito, usuario);
	}

	/**
	 * Método de transferência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#gerarOrdemTransferencia(Transferencia bean, Deposito deposito)
	 * 
	 * @param bean
	 * @param deposito
	 * @throws SQLException 
	 */
	public void gerarOrdemTransferencia(Transferencia bean, Deposito deposito) throws SQLException {
		ordemservicoDAO.gerarOrdemTransferencia(bean,deposito);
	}

	/**
	 * Obtém uma lista de itens que deverão ser exibidos na tela de corte.
	 * 
	 * @see OrdemservicoDAO#getListaItemCorteVO(Ordemservico)
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param faturamentoOutraFilial 
	 * @return
	 */
	public List<ItemCorteVO> getListaItemCorteVO(Ordemservico ordemservico) {
		return ordemservicoDAO.getListaItemCorteVO(ordemservico);
	}

	/**
	 * Atualiza o status da ordem de serviço no banco de dados. Irá gravar apenas o valor
	 * da foreign key, nada mais.
	 * 
	 * @see OrdemservicoDAO#updateStatusordemservico(Ordemservico)
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public void atualizarStatusordemservico(Ordemservico ordemservico) {
		ordemservicoDAO.updateStatusordemservico(ordemservico);
	}

	/**
	 * Carrega a ordem de serviço para a baixa de mapa. Irá carregar a OS e os itens dela.
	 * 
	 * @see OrdemservicoDAO#loadForBaixaMapa(Ordemservico)
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadForBaixaMapa(Ordemservico ordemservico) {
		return ordemservicoDAO.loadForBaixaMapa(ordemservico);
	}
	
	
	/**
	 * Grava em OrdemProdutoHistorico a contagem do inventário.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param responsavel
	 * @param horaInicio
	 * @param horaFim
	 * @param feitoPorColetor
	 */
	public void gravarContagemInventario(Ordemservico ordemservico, Usuario responsavel, Timestamp horaInicio, Timestamp horaFim, boolean feitoPorColetor) {
		
		Boolean fracionada = ordemservico.getInventariolote().getFracionada();
		boolean exigiDuasContagens = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_DUAS_CONTAGENS_INVENTARIO, null);
		boolean isPrimeiraContagem = isPrimeiraOrdemServicoContagemInventario(ordemservico);
		
		boolean finalizadoSucesso = true;
		boolean igualLeituraAnterior = true;
		
		//se é a primeira contagem então não existe leitura anterior para comparar
		if (isPrimeiraContagem)
			igualLeituraAnterior = false;
		
		if(ordemservico.getListaOrdemProdutoHistorico() != null && !ordemservico.getListaOrdemProdutoHistorico().isEmpty()){
			
			Ordemservico ordemservicoAux = load(ordemservico);

			for(Ordemprodutohistorico historico : ordemservico.getListaOrdemProdutoHistorico()){
				
				if (historico.getQtde() == null){
					// 28/08/2009 : Foi pedido para gravar ZERO se o usuário não informar quantidade, 
					//pois se o usuário não bipou o item é porque ele não existe
					historico.setQtde(0L);
				}
				
				//Tratando itens inseridos
				if (historico.getOrdemservicoproduto().getQtdeFracionada() == null)
					historico.getOrdemservicoproduto().setQtdeFracionada(0L);
				
				Long quantidadeEsperada = historico.getOrdemservicoproduto().getQtdeesperada();
				
				Endereco endereco = historico.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino();
				endereco = enderecoService.load(endereco);
				
				Produto produto = historico.getOrdemservicoproduto().getProduto();
				
				//O coletor já está fazendo o cálculo, então quando for feito via coletor vou assumir que nunca será em embalagem de recebimento
				if (produto != null && !feitoPorColetor && (!fracionada && !Enderecofuncao.PICKING.equals(endereco.getEnderecofuncao()))){
					Produtoembalagem embalagem = produtoembalagemService.findCompraByProduto(produto);
					
					if (embalagem != null && (historico.getOrdemservicoproduto().getQtdeFracionada() % embalagem.getQtde()) == 0L){						
						//Se não é coleta fracionada, então calculo a quantidade real
						historico.setQtde(historico.getQtde() * embalagem.getQtde());
					}
				}
				
				if(exigiDuasContagens && !isPrimeiraContagem){
					List<Ordemprodutohistorico> listaContagens = ordemprodutohistoricoService.getHistoricoContagensRecontagens(ordemservicoAux.getInventariolote(), historico.getOrdemservicoproduto(), ordemservico);
					boolean achouLeituraIgual = false;

					//Verificando se a contagem atual é igual a uma leitura anterior
					for (Ordemprodutohistorico ordemprodutohistorico : listaContagens) {
						if(ordemprodutohistorico.getQtde() != null && ordemprodutohistorico.getQtde().equals(historico.getQtde())){
							historico.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
							achouLeituraIgual = true;
							break;
						}
					}

					//Se não achou nenhuma leitura igual então deverá ser gerado uma nova recontagem
					if (!achouLeituraIgual){
						historico.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
						igualLeituraAnterior = false;
					}
					
				}else{
					if (quantidadeEsperada.equals(historico.getQtde())){
						historico.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
					}else{
						historico.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
					}
				}

				if (!quantidadeEsperada.equals(historico.getQtde())){
					finalizadoSucesso = false;
				}

				//se foi um produto inserido (quando é a primeira inserção no endereço o cd da OSP já existe, mas não há enderecoproduto
				Set<Enderecoproduto> listaEnderecoproduto = historico.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino().getListaEnderecoproduto();
				if (historico.getOrdemservicoproduto().getCdordemservicoproduto() == null ||
						listaEnderecoproduto.isEmpty() || listaEnderecoproduto.iterator().next().getCdenderecoproduto() == null){
					
					boolean isNovoOSP = historico.getOrdemservicoproduto().getCdordemservicoproduto() == null;
					
					historico.getOrdemservicoproduto().setQtdeesperada(quantidadeEsperada);
					historico.setOrdemservico(ordemservico);
					
					Ordemservicoprodutoendereco ospe = historico.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().iterator().next();

					ordemservicoprodutoService.saveOrUpdateNoUseTransaction(historico.getOrdemservicoproduto());					
					
					//Só pode salvar se já tem ID, senão tem que carregar o OPH gerado pela trigger antes de salvar.
					if (historico.getCdordemprodutohistorico() != null)
						ordemprodutohistoricoService.saveOrUpdateNoUseTransaction(historico);

					if (historico.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().isEmpty()){
						Ordemservicoprodutoendereco ordemservicoprodutoendereco = new Ordemservicoprodutoendereco(historico.getOrdemservicoproduto(), null, endereco, historico.getQtde());
						ordemservicoprodutoenderecoService.saveOrUpdateNoUseTransaction(ordemservicoprodutoendereco);
					} else {
						//Item inserido
						if (historico.getCdordemprodutohistorico() == null){
							ospe.setOrdemservicoproduto(historico.getOrdemservicoproduto());
							ospe.setQtde(0L);
							ordemservicoprodutoenderecoService.saveOrUpdateNoUseTransaction(ospe);
						}
					}
					
					//Quando é feito por coletor já está criando o Ordemprodutoligacao
					if (!feitoPorColetor && isNovoOSP){
						Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
						ordemprodutoligacao.setOrdemservico(ordemservico);
						ordemprodutoligacao.setOrdemservicoproduto(historico.getOrdemservicoproduto());

						ordemprodutoligacaoService.saveOrUpdateNoUseTransaction(ordemprodutoligacao);
					}
					
					if (historico.getCdordemprodutohistorico() == null){
						Ordemprodutohistorico ophGerado = ordemprodutohistoricoService.findByOspOs(historico.getOrdemservicoproduto(), ordemservico);
						historico.setCdordemprodutohistorico(ophGerado.getCdordemprodutohistorico());
						ordemprodutohistoricoService.saveOrUpdateNoUseTransaction(historico);
					}
					
				}
				
				ordemprodutohistoricoService.updateQtde(historico);
				ordemservicoprodutoService.atualizarStatus(historico.getOrdemservicoproduto());
			}			
		}
		
		//Gravando a relação com usuário
		if (!feitoPorColetor)
			this.associarUsuarioOs(responsavel, ordemservico,horaInicio,horaFim);

		//se foi finalizado com sucesso, não precisa recontar então acabou
		if( finalizadoSucesso && (isPrimeiraContagem || igualLeituraAnterior || !exigiDuasContagens) ){
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
			ordemservicoDAO.alteraStatus(ordemservico, Ordemstatus.FINALIZADO_SUCESSO);
		//se não foi finalizado com sucesso mas foi igual a uma leitura anterior, também acabou
		} else if (igualLeituraAnterior){
			ordemservico.setOrdemstatus(Ordemstatus.AGUARDANDO_CONFIRMACAO);
			ordemservicoDAO.alteraStatus(ordemservico, Ordemstatus.AGUARDANDO_CONFIRMACAO);
		} else {//não foi finalizado com sucesso e também não foi igual a nenhuma leitura anterior
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
			ordemservicoDAO.alteraStatus(ordemservico, Ordemstatus.FINALIZADO_DIVERGENCIA);
		}

	}

	/**
	 * Localiza um {@link Ordemprodutohistorico} dentro de uma lista a partir de um {@link Produto}  e de um {@link Endereco}.
	 * 
	 * @author Giovane Freitas
	 * @param listaOPH
	 * @param produto
	 * @param endereco
	 * @param tipoEndereco
	 * @return
	 */
	public List<Ordemprodutohistorico> findOrdemProdutoHistorico(List<Ordemprodutohistorico> listaOPH, Produto produto,
			Endereco endereco, TipoEndereco tipoEndereco) {
		
		if (listaOPH == null || tipoEndereco == null)
			throw new WmsException("Parâmetros inválidos.");

		List<Ordemprodutohistorico> resultado = new ArrayList<Ordemprodutohistorico>();
		
		for_oph:
		for (Ordemprodutohistorico oph : listaOPH){
			Produto produtoAux = oph.getOrdemservicoproduto().getProduto();
			
			//se ambos produtos são nulos ou se são iguais
			if ((produto == null && produtoAux == null) || (produto != null && produto.equals(produtoAux))){
				for (Ordemservicoprodutoendereco ospe : oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco()){

					//Se é para comparar o endereço  de origem e se ambos são nulos ou se são iguais então achou o item
					if (TipoEndereco.Origem.equals(tipoEndereco) && ((endereco == null && ospe.getEnderecoorigem() == null) ||
							(endereco != null && (endereco.equals(ospe.getEnderecoorigem()))))){
						
						resultado.add(oph);
						continue for_oph;
					}
					//Se é para comparar o endereço  de destino e se ambos são nulos ou se são iguais então achou o item
					else if (TipoEndereco.Destino.equals(tipoEndereco) && ((endereco == null && ospe.getEnderecodestino() == null) ||
							(endereco != null && (endereco.equals(ospe.getEnderecodestino()))))){
						
						resultado.add(oph);
						continue for_oph;
					}
				}				
			}
		}
		
		return resultado;
	}

	/**
	 * Método que cria ordem serviço a partir do lançamento de dados do inventário se houve divergências
	 * 
	 * @see OrdemservicoprodutoService#saveOrUpdate(Ordemservicoproduto)
	 * @see OrdemservicoprodutoenderecoService#saveOrUpdate(Ordemservicoprodutoendereco)
	 * @see OrdemservicoService#saveOrUpdate(Ordemservico)
	 * @see OrdemservicoService#callProcedureAtualizarRastreabilidade(Ordemservico)
	 * 
	 * @author Giovane Freitas
	 * @param listaHistorico 
	 * @param ordemservicoprincipal 
	 * @param ordemservicoprodutoendereco 
	 * @throws InsercaoInvalidaException
	 * return <code>true</code> quando houve ajuste de estoque e <code>false</code> caso contrário. 
	 */
	public boolean ajustarEstoque(Inventariolote inventariolote,Deposito deposito, Usuario usuario) throws InsercaoInvalidaException {
		List<LeituraDivergenteInventarioVO> divergencias = inventarioloteService.findLeiturasDivergentes(inventariolote);
		if (divergencias == null || divergencias.isEmpty())
			return false;
		
		Ordemservico ajusteEstoqueOS = new Ordemservico(Ordemtipo.AJUSTE_ESTOQUE, Ordemstatus.FINALIZADO_SUCESSO, deposito);
		ajusteEstoqueOS.setInventariolote(inventariolote);
		
		OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario(usuario);
		ordemservicoUsuario.setDtinicio(new Timestamp(System.currentTimeMillis()));
		ordemservicoUsuario.setDtfim(new Timestamp(System.currentTimeMillis()));
		ajusteEstoqueOS.getListaOrdemServicoUsuario().add(ordemservicoUsuario);

		saveOrUpdateNoUseTransaction(ajusteEstoqueOS);
		ordemservicousuarioService.saveOrUpdateNoUseTransaction(ordemservicoUsuario);
		
		for (LeituraDivergenteInventarioVO divergencia : divergencias){
			
			Produto produto = new Produto(divergencia.getCdproduto());

			Endereco endereco = new Endereco(divergencia.getCdenderecodestino());
			endereco = enderecoService.loadForEntrada(endereco);
			
			Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto(produto, divergencia.getQtdelida(), Ordemprodutostatus.CONCLUIDO_OK);
			
			Ordemservicoprodutoendereco ordemservicoprodutoendereco = new Ordemservicoprodutoendereco(ordemservicoproduto, 
					endereco, endereco, divergencia.getQtdelida());

			ordemservicoprodutoService.saveOrUpdateNoUseTransaction(ordemservicoproduto);
			ordemservicoprodutoenderecoService.saveOrUpdateNoUseTransaction(ordemservicoprodutoendereco);

			Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
			ordemprodutoligacao.setOrdemservico(ajusteEstoqueOS);
			ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
			ordemprodutoligacaoService.saveOrUpdateNoUseTransaction(ordemprodutoligacao);
			
			if (endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
				Long qtdeEsperada = divergencia.getQtdeesperada();
				
				if (divergencia.getQtdelida() > qtdeEsperada){
					enderecoprodutoService.preenchePredioBlocado(produto, endereco, divergencia.getQtdelida() - qtdeEsperada);
				}else{
					enderecoprodutoService.retiraProdutoPredioBlocado(produto, endereco, qtdeEsperada - divergencia.getQtdelida());
				}
			}else
				enderecoprodutoService.ajustarEstoque(endereco, produto, divergencia.getQtdelida());
		}
		
		for (Ordemservico ordemservico : inventariolote.getListaOrdemservico()){
			if (Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(ordemservico.getOrdemstatus())){
				ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
				updateStatusordemservico(ordemservico);
			}
		}
		
		callProcedureAtualizarRastreabilidade(ajusteEstoqueOS);
		
		return true;
	}
	
	/**
	 * Retorna as ordens de servico
	 * @param recebimento
	 * @param ordemstatus
	 * @return
	 * @author Cíntia Nogueira
	 * {@link OrdemservicoDAO#loadBy(Recebimento, Ordemstatus)}
	 */
	public List<Ordemservico> loadBy(Recebimento recebimento, Ordemstatus ordemstatus){
		return ordemservicoDAO.loadBy(recebimento, ordemstatus);
	}
	
	/**
	 * Altera o status da ordem
	 * @param bean
	 * @param status
	 * @author Cíntia Nogueira
	 * {@link OrdemservicoDAO#alteraStatus(Ordemservico, Ordemstatus)}
	 */
	public void alteraStatus(Ordemservico bean, Ordemstatus status){
		ordemservicoDAO.alteraStatus(bean, status);
	}
	
	/**
	 * Carrega a ordem de serviço com as {@link OrdemservicoUsuario} associadas.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadWithUsuario(Ordemservico ordemservico) {
		return ordemservicoDAO.loadWithUsuario(ordemservico);
	}
	
	/**
	 * Retorna os dados para o relatório de etiquetas UMA (endereçamento em embalagem padrão)
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<EtiquetasumaVO> findForReportEtiquetaUma(EtiquetaumaFiltro filtro) {
		return ordemservicoDAO.findForReportEtiquetaUma(filtro);
	}

	/**
	 * Retorna os dados para o relatório de etiquetas UMA (endereçamento em embalagem padrão)
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<EtiquetasumaVO> findForReportEtiquetaUmaManual(EtiquetaumaFiltro filtro) {
		return ordemservicoDAO.findForReportEtiquetaUmaManual(filtro);
	}
	
	/**
	 * Método com referência no DAO
	 * 
	 * @param ordem
	 * @author Tomás Rabelo
	 */
	public void callProcedureAtualizarRastreabilidade(final Ordemservico ordem) {
		ordemservicoDAO.callProcedureAtualizarRastreabilidade(ordem);
	}
	
	/**
	 * Preenche a ordem de conferência de Box com as quantidades conferidas na primeira conferência.
	 * Se a configuração estiver definida para isso no cadastro de configurações do sistema.
	 * 
	 * @param ordem A primeira ordem de conferência.
	 * @author Giovane Freitas
	 */
	public void gerarConferenciaBox(Ordemservico ordem) {
		if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA, ordem.getDeposito() != null ? ordem.getDeposito() : WmsUtil.getDeposito())){
			ordemservicoDAO.gerarConferenciaBox(ordem);
			List<Embalagemexpedicao> listaEmbalagens;
			if(ordem.getOrdemservicoprincipal()!=null)
				 listaEmbalagens = embalagemexpedicaoService.findByPrimeiraConferencia(ordem.getOrdemservicoprincipal());
			else
				 listaEmbalagens = embalagemexpedicaoService.findByPrimeiraConferencia(ordem);
			for (Embalagemexpedicao ee : listaEmbalagens){
				ee.setConferida(false);
				embalagemexpedicaoService.saveOrUpdate(ee);
			}
		}
	}
	
	/**
	 * Cancela o endereçamento de um recebimento. O cancelamento somente será
	 * permitido se o recebimento ainda está em "Em endereçamento".
	 * 
	 * @author Giovane Freitas
	 * @param recebimento
	 */
	public void excluirEnderecamento(Recebimento recebimento) {
		Recebimento ultimaVersaoBD = recebimentoService.load(recebimento);
		if (!ultimaVersaoBD.getRecebimentostatus().equals(Recebimentostatus.EM_ENDERECAMENTO))
			throw new WmsException("Não é possível cancelar o endereçamento do recebimento " + recebimento.getCdrecebimento());
		
		List<Ordemservico> listaOS = this.findByRecebimento(recebimento);
		for (Ordemservico os : listaOS){
			if (Ordemtipo.ENDERECAMENTO_AVARIADO.equals(os.getOrdemtipo()) 
					|| Ordemtipo.ENDERECAMENTO_FRACIONADO.equals(os.getOrdemtipo()) 
					|| Ordemtipo.ENDERECAMENTO_PADRAO.equals(os.getOrdemtipo())){
				
				List<Ordemservicoproduto> listaOSP = ordemservicoprodutoService.findForDelete(os);
				
				for (Ordemservicoproduto osp : listaOSP){
					
					for (Ordemservicoprodutoendereco ospe : osp.getListaOrdemservicoprodutoendereco()){
						enderecoprodutoService.removerQtdeReservada(ospe);
						ordemservicoprodutoenderecoService.delete(ospe);
						if (ospe.getEnderecodestino() != null){
							enderecoService.pAtualizarEndereco(ospe.getEnderecodestino());
						}
					}
					
					ordemservicoprodutoService.delete(osp);
				}
				
				ordemprodutohistoricoService.deleteByOrdemservico(os);
				ordemprodutoligacaoService.deleteOrdemProdutoLigacao(os);
				this.delete(os);
				
			}
		}
		
		Recebimentostatus status = Recebimentostatus.CONCLUIDO;
		Ordemservico lastConferencia = this.loadLastConferencia(recebimento);
		if (Ordemstatus.FINALIZADO_DIVERGENCIA.equals(lastConferencia.getOrdemstatus())){
			status = Recebimentostatus.CONCLUIDO_COM_DIVERGENCIAS;			
		}else if (Ordemstatus.FINALIZADO_SUCESSO.equals(lastConferencia.getOrdemstatus())){
			status = Recebimentostatus.CONCLUIDO;			
		}else if (Ordemstatus.EM_ABERTO.equals(lastConferencia.getOrdemstatus()) 
			|| Ordemstatus.EM_EXECUCAO.equals(lastConferencia.getOrdemstatus()) 
			|| Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(lastConferencia.getOrdemstatus())){
			
			status = Recebimentostatus.EM_ANDAMENTO;
		}
	
		
		recebimento.setRecebimentostatus(status);
		recebimentoService.gravaStatusRecebimento(recebimento);
		tipoenderecamentoService.atualizarTipoEnderecamentoRecebimento(recebimento,Tipoenderecamento.NAO_DEFINIDO);	
	}

	/**
	 * Cria uma ordem de contagem ou recontagem a partir de uma ordem de contagem.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param deposito 
	 */
	public void criarContagemRecontagem(Ordemservico ordemservico, Deposito deposito) {
		
		boolean contagem = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_DUAS_CONTAGENS_INVENTARIO, null) && isPrimeiraOrdemServicoContagemInventario(ordemservico);

		ordemservico = ordemservicoDAO.findForCriarContagemRecontagem(ordemservico,contagem);

		if (ordemservico == null)
			throw new WmsException("Não foi possível carregar a ordem de serviço para gerar a " + (contagem ? "contagem" : "recontagem") + ".");
		
		Ordemservico osRecontagem = new Ordemservico();
		osRecontagem.setOrdemtipo(contagem ? Ordemtipo.CONTAGEM_INVENTARIO : Ordemtipo.RECONTAGEM_INVENTARIO);
		osRecontagem.setOrdemstatus(Ordemstatus.EM_ABERTO);
		osRecontagem.setOrdem(ordemservico.getOrdem() + 1);
		osRecontagem.setInventariolote(ordemservico.getInventariolote());
		osRecontagem.setOrdemservicoprincipal(ordemservico);
		osRecontagem.setDeposito(deposito == null || deposito.getCddeposito() == null ? WmsUtil.getDeposito() : deposito);
		this.saveOrUpdateNoUseTransaction(osRecontagem);
		
		for (Ordemprodutoligacao opl : ordemservico.getListaOrdemProdutoLigacao()){
			if(contagem){
				geraNovosValoresContagemRecontagem(opl, osRecontagem);
			}else{
				if (!opl.getOrdemservicoproduto().getOrdemprodutostatus().equals(Ordemprodutostatus.CONCLUIDO_OK)){
					geraNovosValoresContagemRecontagem(opl, osRecontagem);
				}
			}
		}
	}
	
	/**
	 * Método que copia os valores da ordem de serviço para a outra
	 * 
	 * @param opl
	 * @param osRecontagem
	 */
	private void geraNovosValoresContagemRecontagem(Ordemprodutoligacao opl, Ordemservico osRecontagem) {
		Ordemservicoproduto ospRecontagem = new Ordemservicoproduto();
		ospRecontagem.setProduto(opl.getOrdemservicoproduto().getProduto());
		ospRecontagem.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
		ospRecontagem.setQtdeesperada(opl.getOrdemservicoproduto().getQtdeesperada());
		ordemservicoprodutoService.saveOrUpdateNoUseTransaction(ospRecontagem);
		
		Ordemprodutoligacao oplRecontagem = new Ordemprodutoligacao();
		oplRecontagem.setOrdemservico(osRecontagem);
		oplRecontagem.setOrdemservicoproduto(ospRecontagem);
		ordemprodutoligacaoService.saveOrUpdateNoUseTransaction(oplRecontagem);
		
		for (Ordemservicoprodutoendereco ospe : opl.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco()){
			Ordemservicoprodutoendereco ospeRecontagem = new Ordemservicoprodutoendereco();
			ospeRecontagem.setQtde(ospe.getQtde());
			ospeRecontagem.setEnderecodestino(ospe.getEnderecodestino());
			ospeRecontagem.setEnderecoorigem(ospe.getEnderecoorigem());
			ospeRecontagem.setOrdemservicoproduto(ospRecontagem);
			ordemservicoprodutoenderecoService.saveOrUpdateNoUseTransaction(ospeRecontagem);
		}
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param ordemservico
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean isPrimeiraOrdemServicoContagemInventario(Ordemservico ordemservico) {
		return ordemservicoDAO.isPrimeiraOrdemServicoContagemInventario(ordemservico);
	}

	/**
	 * Lista os dados para impressão do relatório de ordem de armazenagem.
	 * 
	 * @author Giovane Freitas
	 * @param cdOs
	 * @return
	 */
	public List<OrdemVO> findForReportArmazenagem(String cdOs) {
		return ordemservicoDAO.findForReportArmazenagem(cdOs);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param ordemservico
	 * @return
	 * @author Tomás Rabelo
	 */
	public Boolean hasItensCarregamentoFaturadosOutraFilial(Ordemservico ordemservico) {
		return ordemservicoDAO.hasItensCarregamentoFaturadosOutraFilial(ordemservico);
	}

	/**
	 * Localiza todas as ordens de serviço associadas a um determinado usuário.
	 * 
	 * @author Giovane Freitas
	 * @param usuario O usuário ao qual a ordem está associada.
	 */
	public List<Ordemservico> findOSByUsuario(Usuario usuario, Recebimento recebimento) {
		return ordemservicoDAO.findOSByUsuario(usuario, recebimento);
	}

	/**
	 * Carrega a ordem de contagem/recontagem mais recente.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public Ordemservico loadLastContagem(Inventariolote inventariolote){
		return ordemservicoDAO.loadLastContagem(inventariolote);
	}
	
	/**
	 * Carrega a ordem de contagem/recontagem mais recente.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public Ordemservico loadLastContagem(Reabastecimentolote lote){
		return ordemservicoDAO.loadLastContagem(lote);
	}
	
	/**
	 * Localiza todas as movimentações abertas de entrada ou de saída para um
	 * determinado produto em um determinado endereço.
	 * 
	 * @param produto
	 * @param endereco
	 * @param exibirEntradas
	 *            <code>true</code> caso deseje exibir as reservas de entrada
	 *            ou <code>false</code> caso deseje exibir as reservas de
	 *            saída.
	 * @author Giovane Freitas
	 * @return 
	 */
	public List<MovimentacaoAberta> findMovimentacoesAbertas(Produto produto, Endereco endereco, boolean exibirEntradas) {
		List<MovimentacaoAberta> movimentacoesAbertas = ordemservicoDAO.findMovimentacoesAbertas(produto, endereco, exibirEntradas);
		
		Collections.sort(movimentacoesAbertas, new Comparator<MovimentacaoAberta>(){
 
			@Override
			public int compare(MovimentacaoAberta m1, MovimentacaoAberta m2) {
				Integer pesoM1;
				Integer pesoM2;
				
				if (m1.getCarregamento() != null)
					pesoM1 = 1;
				else if (m1.getRecebimento() != null)
					pesoM1 = 2;
				else if (m1.getTransferencia() != null)
					pesoM1 = 3;
				else 
					pesoM1 = 0;
				
				if (m2.getCarregamento() != null)
					pesoM2 = 1;
				else if (m2.getRecebimento() != null)
					pesoM2 = 2;
				else if (m2.getTransferencia() != null)
					pesoM2 = 3;
				else 
					pesoM2 = 0;
				
				return pesoM1.compareTo(pesoM2);
			}
			
		});
		
		return movimentacoesAbertas;
	}
	
	/**
	 * <p>
	 * Finaliza uma ordem de serviço qualquer e remove todas as quantidades de 
	 * reservadas de entrada e saída que tenham sido realizadas pela dada O.S. 
	 * <br/>Também será realizado o ajuste de estoque, acrecentando e 
	 * subtraindo as devidas quantidades.
	 * </p>
	 * <p>
	 * Para ordens de endereçamento as reservas de entrada e o estoque somente serão 
	 * ajustados quando finalizar a última O.S. do recebimento.
	 * </p>
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param usuario 
	 * @param dtinicio 
	 * @param dtfim 
	 */
	public void finalizar(Ordemservico ordem, Usuario usuario, Timestamp dtinicio, Timestamp dtfim, 
			boolean gravarOrdemServicoUsuario){
		
		Ordemservico ordemservicoAux = this.load(ordem);
		if (ordemservicoAux == null)
			throw new WmsException("A ordem de serviço foi excluída.");
			
		if (ordemservicoAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA)
				|| ordemservicoAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO)){
			
			throw new WmsException("A ordem de serviço já foi finalizada.");
		}
		
		boolean concluidoSucesso = true;
		
		boolean isEnderecamento = ordemservicoAux.getOrdemtipo().equals(Ordemtipo.ENDERECAMENTO_AVARIADO)
					|| ordemservicoAux.getOrdemtipo().equals(Ordemtipo.ENDERECAMENTO_FRACIONADO)
					|| ordemservicoAux.getOrdemtipo().equals(Ordemtipo.ENDERECAMENTO_PADRAO);
		
		List<Ordemservicoprodutoendereco> listaOSPE = OrdemservicoprodutoenderecoService.getInstance().findByOrdemServico(ordemservicoAux);
		for (Ordemservicoprodutoendereco ospe : listaOSPE){
			if (Ordemprodutostatus.CONCLUIDO_DIVERGENTE.equals(ospe.getOrdemservicoproduto().getOrdemprodutostatus()))
				concluidoSucesso = false;
			
			// Para endereçamentos o ajuste de estoque somente pode ser feito
			// quando todas as ordens de endereçamento já foram finalizadas.
			// No fim deste método é chamado o método atualizarEstoqueRecebimento 
			// para validar e atualizar o estoque quando for o momento correto.
			if (!isEnderecamento){
				//Atualizar os estoques dos produtos endereçados
				try {
					if (ospe.getEnderecodestino() != null && ospe.getQtde() != null && ospe.getQtde() > 0L)
						EnderecoprodutoService.getInstance().atualizaEstoqueEntrada(ospe, false, true);
					if (ospe.getEnderecoorigem() != null && ospe.getQtde() != null && ospe.getQtde() > 0L)
						EnderecoprodutoService.getInstance().atualizaEstoqueSaida(ospe, false, true);
				} catch (InsercaoInvalidaException e) {
					throw new WmsException(e.getMessage(), e);
				}
			}
			
		}

		//Associando o usuário
		if (gravarOrdemServicoUsuario){
			if (usuario == null)
				usuario = WmsUtil.getUsuarioLogado();
			if (dtinicio == null)
				dtinicio = new Timestamp(Calendar.getInstance().getTimeInMillis());
			if (dtfim == null)
				dtfim = new Timestamp(Calendar.getInstance().getTimeInMillis());

			OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
			ordemservicoUsuario.setOrdemservico(ordemservicoAux);
			ordemservicoUsuario.setUsuario(usuario);
			ordemservicoUsuario.setDtinicio(dtinicio);
			ordemservicoUsuario.setDtfim(dtfim);
			ordemservicousuarioService.saveOrUpdateNoUseTransaction(ordemservicoUsuario);
		}
		
		//Atualizando os históricos
		//FIXME: Quando for ordem de endereçamento somar a quantidade de OSPE para determinar a qtde para o histórico
		List<Ordemprodutohistorico> listaOPH = ordemprodutohistoricoService.findByOS(ordemservicoAux);
		for (Ordemprodutohistorico oph : listaOPH){
			oph.setQtde(oph.getOrdemservicoproduto().getQtdeesperada());
			oph.setQtdeavaria(0L);
			oph.setQtdefracionada(0L);
			
			ordemprodutohistoricoService.saveOrUpdateNoUseTransaction(oph);
		}
			
		//Atualizando o status dos itens da O.S.
		ordemservicoprodutoService.atualizarStatus(ordemservicoAux, Ordemprodutostatus.CONCLUIDO_OK);

		//Atualizando o status da O.S.
		if (concluidoSucesso)
			ordemservicoAux.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
		else
			ordemservicoAux.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
		atualizarStatusordemservico(ordemservicoAux);

		//Se é endereçamento vou chamar a função que valida se deve ajustar o estoque.
		if (isEnderecamento){
			Recebimento recebimento = recebimentoService.findByOrdemservico(ordemservicoAux);
			recebimentoService.atualizarEstoqueRecebimento(recebimento);
		}else{
			//Atualizando a tabela de histórico de rastreabilidade
			callProcedureAtualizarRastreabilidade(ordemservicoAux);
		}
		
		//Copiando o status para que a tela possa exibir corretamente
		ordem.setOrdemstatus(ordemservicoAux.getOrdemstatus());
	}
	
	/**
	 * Cancela uma ordem de serviço qualquer e remove todas as quantidades de 
	 * reservadas de entrada e saída que tenham sido realizadas pela dada O.S. 
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public void cancelar(Ordemservico ordemservico){
		List<Ordemservicoprodutoendereco> listaOSPE = OrdemservicoprodutoenderecoService.getInstance().findByOrdemServico(ordemservico);
		for (Ordemservicoprodutoendereco ospe : listaOSPE){			
			EnderecoprodutoService.getInstance().removerQtdeReservada(ospe);
		}
		
		ordemservico.setOrdemstatus(Ordemstatus.CANCELADO);
		atualizarStatusordemservico(ordemservico);
	}

	/**
	 * Busca a próxima ordem de serviço a ser executada para o processo de convocação ativa.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param usuario
	 * @return
	 */
	public Ordemservico associarProximaOrdem(final Deposito deposito, final Usuario usuario){
		return ordemservicoDAO.associarProximaOrdem(deposito, usuario);
	}
	
	/**
	 * Carrega as informações da ordem de serviço e sua origem, por exemplo, carregamento,
	 * recebimento, transferência, etc.
	 * 
	 * @author Giovane Freitas
	 * @param ordem
	 * @return
	 */
	public Ordemservico loadOrdemAndOrigem(Ordemservico ordem) {
		return ordemservicoDAO.loadOrdemAndOrigem(ordem);
	}

	/**
	 * Localiza a ordem de serviço de uma determinada transferência que contém um produto específico.
	 * 
	 * @author Giovane Freitas
	 * @param transferencia
	 * @param produto
	 * @return
	 */
	public Ordemservico findByTransferenciaProduto(Transferencia transferencia, Produto produto){
		return ordemservicoDAO.findByTransferenciaProduto(transferencia, produto);
	}

	/**
	 * Carrega uma ordem de endereçamento para edição.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadForEdicaoEnderecamento(Ordemservico ordemservico) {
		return ordemservicoDAO.loadForEdicaoEnderecamento(ordemservico);
	}

	
	/**
	 * Busca a quantidade conferida de um determinado recebimento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param recebimento
	 * @return
	 */
	public Map<Produto, Ordemprodutohistorico> findQtdeConferida(Recebimento recebimento) {
		Map<Produto, Ordemprodutohistorico> result = new HashMap<Produto, Ordemprodutohistorico>();
		
		List<Ordemprodutohistorico> listaOPH = ordemprodutohistoricoService.findConferencia(recebimento);
		for (Ordemprodutohistorico oph : listaOPH){
			Ordemprodutohistorico maxOPH = ordemprodutohistoricoService.findMaxByOrdemservicoproduto(oph.getOrdemservicoproduto());
			
			boolean isNormaVolume = true;
			
			if (oph.getOrdemservicoproduto().getProduto().getProdutoprincipal() != null){
				Produto pp = oph.getOrdemservicoproduto().getProduto().getProdutoprincipal();
				Dadologistico dadologistico = dadologisticoService.findByProduto(pp, recebimento.getDeposito());
				if (dadologistico != null)
					isNormaVolume = dadologistico.getNormavolume();
			}
			
			if (isNormaVolume)
				result.put(new Produto(oph.getOrdemservicoproduto().getProduto().getCdproduto()), maxOPH);
			else
				result.put(new Produto(oph.getOrdemservicoproduto().getProduto().getProdutoprincipal().getCdproduto()), maxOPH);
		}
		
		return result;
	}

	/**
	 * Localiza o mapa de separação que contém um determinado produto, para associar como ordem principal na conferência.
	 * 
	 * @author Giovane Freitas
	 * @param expedicao
	 * @param produto
	 * @return
	 */
	public Ordemservico findMapaByProduto(Expedicao expedicao, Produto produto) {
		return ordemservicoDAO.findMapaByProduto(expedicao, produto);
	}

	/**
	 * 
	 * Método que recupera o status e o tipo de uma ordem de serviço
	 * 
	 * @author Giovane Freitas
	 * 
	 * @return Ordemservico
	 * 
	 */
	public List<Ordemservico> findByExpedicao(Expedicao expedicao, Ordemtipo ordemtipo) {
		return ordemservicoDAO.findByExpedicao(expedicao, ordemtipo);
	}

	/**
	 * Busca as quantidades confirmadas na conferência de uma ordem de serviço, considerando a existência de uma reconferência.
	 * 
	 * @author Giovane Freitas
	 * @param conferencia
	 * @return
	 */
	public List<ConfirmacaoItemVO> findQtdeConfirmada(Ordemservico conferencia) {
		return ordemservicoDAO.findQtdeConfirmada(conferencia);
	}

	/**
	 * 
	 * @param ordemservico
	 * @param ordemservicoUsuario
	 * @param user
	 */
	public void finalizaCheckout(final Ordemservico ordemservico, final OrdemservicoUsuario ordemservicoUsuario, final Usuario user) {
		//sempre atualiza.
		ordemservicoprodutoService.atualizarStatus(ordemservico, Ordemprodutostatus.CONCLUIDO_OK);
		atualizarStatusordemservico(ordemservico);
	
		ordemservicoUsuario.setDtfim(new Timestamp(System.currentTimeMillis()));
		ordemservicousuarioService.saveOrUpdateNoUseTransaction(ordemservicoUsuario);
		
		ordemservico.getCarregamento().setDeposito(ordemservico.getDeposito());
		
		if(ordemservico.getExpedicao() != null && ordemservico.getExpedicao().getCdexpedicao() != null){
			ExpedicaoService.getInstance().processaSituacaoExpedicao(ordemservico.getExpedicao(), user);
//			this.gerarHistoricoExpedicao(ordemservico, user);
		}
	}		
		
	/**
	 * Método que valida se existe alguma OS aberta vinculada a Expedição
	 * 
	 * @author Filipe Santos
	 * @see ExpedicaoService#processaSituacaoExpedicao(Expedicao)
	 * @see OrdemservicoService#finalizaCheckout(Ordemservico, OrdemservicoUsuario, Usuario)
	 * @see OrdemprodutohistoricoService#processarConferencia(Ordemservico, List, Usuario)
	 * @param expedicao
	 * @return Booelan existeOSAberta
	 */
	public boolean existeOSAberta(final Expedicao expedicao) {
		List<Ordemservico> listaOrdemservico = this.findByExpedicao(expedicao, null);		
		
		boolean existeOSAberta = false;

		for (Ordemservico ordemAux : listaOrdemservico) {
			if(!ordemAux.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO)) {
				if(!ordemAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO) && !ordemAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA)
						 && !ordemAux.getOrdemstatus().equals(Ordemstatus.CANCELADO)) {
					existeOSAberta = true;
				}
			}
		}
		return existeOSAberta;
	}

	/**
	 * Localiza os mapas de separação gerados em onda que engloba o carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservico> findMapaByOnda(Carregamento carregamento) {
		return ordemservicoDAO.findMapaByOnda(carregamento);
	}
	
	/**
	 * Localiza os mapas de separação gerados em onda que engloba a expedicao.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservico> findMapaByOnda(Expedicao expedicao) {
		return ordemservicoDAO.findMapaByOnda(expedicao);
	}

	/**
	 * Busca a segunda conferência de expedição associada a uma primeira conferência.
	 * 
	 * @author Giovane Freitas
	 * @param primeiraConferencia
	 * @return 
	 */
	public Ordemservico loadSegundaConferencia(Ordemservico primeiraConferencia) {
		return ordemservicoDAO.loadSegundaConferencia(primeiraConferencia);
	}

	public Ordemservico loadWithPrincipal(Ordemservico ordemservico) {
		return ordemservicoDAO.loadWithPrincipal(ordemservico);
	}
		
	public List<Ordemservico> selectStatusUltimaOS(Recebimento recebimento) {
		return ordemservicoDAO.selectStatusUltimaOS(recebimento);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param endereco
	 * @return
	 * @author Tomás Rabelo
	 * @param contagem 
	 */
	public Ordemservico findOrdemServicoAbertaByEndereco(Endereco endereco, boolean contagem, Deposito deposito) {
		return ordemservicoDAO.findOrdemServicoAbertaByEndereco(endereco, contagem, deposito);
	}

	/**
	 * Finaliza uma conferência de retorno de box.
	 * 
	 * @param ordemservico
	 * @param listaOPH
	 */
	public void executarRetornoBox(Ordemservico ordemservicoRetorno, List<Ordemprodutohistorico> listaOPH, Usuario user) {
		Ordemservico ordemservicoAux = this.loadWithPrincipal(ordemservicoRetorno);
		if (!ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_ABERTO) 
				&& !ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_EXECUCAO) 
				&& !ordemservicoAux.getOrdemstatus().equals(Ordemstatus.AGUARDANDO_CONFIRMACAO)){
			
			throw new WmsException("Esta ordem de serviço já foi finalizada por outro operador.");
		}
		
		if(!ordemservicoAux.getOrdemtipo().equals(Ordemtipo.RETORNO_BOX))
			throw new WmsException("Ordem de serviço inválida. Não é um retorno de box.");
		
		ordemservicoAux.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
		this.atualizarStatusordemservico(ordemservicoAux);
		
		////////////////////////////////////////////////////////////////
		//Gravando o corte dos itens
		List<Etiquetaexpedicao> listaEtiquetas = EtiquetaexpedicaoService.getInstance().findByOrdemservico(ordemservicoAux.getOrdemservicoprincipal(), true, null);

		List<Ordemservicoproduto> listaOsp = new ArrayList<Ordemservicoproduto>();
		List<Ordemservicoproduto> listaOspDivergente = new ArrayList<Ordemservicoproduto>();
		Map<Ordemservicoproduto, Ordemprodutohistorico> mapOPH = new HashMap<Ordemservicoproduto, Ordemprodutohistorico>();
		List<Carregamentoitem> itensCortados = new ArrayList<Carregamentoitem>();
		
		for (Etiquetaexpedicao etiqueta : listaEtiquetas){
			if (etiqueta.getQtdecoletor() == null){
				etiqueta.setQtdecoletor(0L);
			
				EtiquetaexpedicaoService.getInstance().updateQtdecoletor(etiqueta);
			
				if (!listaOsp.contains(etiqueta.getOrdemservicoproduto())){
					listaOsp.add(etiqueta.getOrdemservicoproduto());
					Ordemprodutohistorico oph = OrdemprodutohistoricoService.getInstance().findByOspOs(etiqueta.getOrdemservicoproduto(), ordemservicoAux.getOrdemservicoprincipal());
					oph.setQtde(etiqueta.getQtdecoletor());
					mapOPH.put(etiqueta.getOrdemservicoproduto(), oph);
				}else{
					Ordemprodutohistorico oph = mapOPH.get(etiqueta.getOrdemservicoproduto());
					oph.setQtde(oph.getQtde() + etiqueta.getQtdecoletor());
				}
			
				if (!listaOspDivergente.contains(etiqueta.getOrdemservicoproduto()))
					listaOspDivergente.add(etiqueta.getOrdemservicoproduto());
				
				//Se é um volume devo guardar o carregamentoitem para depois fazer corte automático dos volumes correspondentes.
				if (etiqueta.getOrdemservicoproduto().getProduto().getProdutoprincipal() != null && !itensCortados.contains(etiqueta.getCarregamentoitem())){
					itensCortados.add(etiqueta.getCarregamentoitem());
				}
			}
		}

		//----------------------------------------------
		//atualizando o statos dos Ordemservicoproduto
		for (Ordemservicoproduto osp : listaOspDivergente){
			osp.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
			OrdemservicoprodutoService.getInstance().updateStatusordemservicoproduto(osp);
		}
		
		for (Ordemprodutohistorico oph : mapOPH.values()) {
			OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph);
		}
		//----------------------------------------------

		
		//Realizando o corte automático dos volumes, isso é necessário quando um dos volumes é 
		//confirmado com divergência mas os demais foram coletados com sucesso.
		for (Carregamentoitem item : itensCortados){
			List<ConferenciaVolumeVO> conferencias = EtiquetaexpedicaoService.getInstance().findConferenciaVolume(item);
			Long min = null;
			for (ConferenciaVolumeVO conferenciaVO : conferencias){
				if (min != null)
					min = Math.min(min, conferenciaVO.getQtdeconferida());
				else
					min = conferenciaVO.getQtdeconferida();
			}
			
			if (min == null)
				min = 0L;
			
			for (ConferenciaVolumeVO conferenciaVO : conferencias){
				if (conferenciaVO.getQtdeconferida() > min.longValue()){
					Long divergencia = conferenciaVO.getQtdeconferida() - min;
					List<Etiquetaexpedicao> etiquetas = EtiquetaexpedicaoService.getInstance().findEtiquetasParaCorte(conferenciaVO.getCdcarregamentoitem(), conferenciaVO.getCdvolume(), divergencia.intValue());
					for (Etiquetaexpedicao etiqueta : etiquetas){
						etiqueta.setQtdecoletor(0L);
						EtiquetaexpedicaoService.getInstance().updateQtdecoletor(etiqueta);
					}
				}
			}
		}
		
		ordemservicoAux.getOrdemservicoprincipal().setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
		OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservicoAux.getOrdemservicoprincipal());		
		//Fim da gravação do corte
		////////////////////////////////////////////////////////////////
				
		
		//Finalizando o carregamento se esta era última O.S. aberta
		ordemservicoRetorno.getCarregamento().setDeposito(ordemservicoRetorno.getDeposito());
		if(ordemservicoRetorno.getExpedicao() != null && ordemservicoRetorno.getExpedicao().getCdexpedicao() != null)
			ExpedicaoService.getInstance().processaSituacaoExpedicao(ordemservicoRetorno.getExpedicao(), user);

	}
	
	/**
	 * Verifica se o recebimento possui etiqueta de UMA quando é um endereçamento manual.
	 * 
	 * @param recebimento
	 * @return
	 */
	public boolean possuiUmaManual(Recebimento recebimento){
		return ordemservicoDAO.possuiUmaManual(recebimento);
	}

	public Ordemservico find1ConferenciaByRetornoBox(Ordemservico ordemservicoRB) {
		return ordemservicoDAO.find1ConferenciaByRetornoBox(ordemservicoRB);
	}
	public Ordemservico find2ConferenciaByRetornoBox(Ordemservico ordemservicoRB) {
		return ordemservicoDAO.find2ConferenciaByRetornoBox(ordemservicoRB);
	}

	public Ordemservico find2ReconferenciaByRetornoBox(Ordemservico ordemservicoRB) {
		return ordemservicoDAO.find2ReconferenciaByRetornoBox(ordemservicoRB);
	}
	
	/**
	 * Método que retorna a lista de ordem serviço que esteja com Status EM ABERTO e seja um REABASTECIMENTO
	 * 
	 * @param Deposito
	 * @return List<Ordemservico>
	 * @author Thiago Augusto
	 */
	public List<Ordemservico> findOSPickingAberto(Deposito deposito, Usuario usuario){
		return ordemservicoDAO.findOSPickingAberto(deposito, usuario);
	}

	/**
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Ordemstatus getStatus(Ordemservico ordemservico) {
		return ordemservicoDAO.getStatus(ordemservico);
	}
	
	/**
	 * 
	 * @param carregamento
	 * @return
	 */
	public Boolean confirmaCheckoutFinalizados(Carregamento carregamento){
		if (ordemservicoDAO.findCheckoutAberto(carregamento)==null || ordemservicoDAO.findCheckoutAberto(carregamento).isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public Boolean isDuasConferenciasRecebimentoIguais(Recebimento recebimento) {
		
		List<Ordemprodutohistorico> listaOrdemprodutohistorico = ordemprodutohistoricoService.findAllConferenciaByRecebimento(recebimento);
	
		if(listaOrdemprodutohistorico!=null && !listaOrdemprodutohistorico.isEmpty()){
			
			for (Ordemprodutohistorico ordemprodutohistorico : listaOrdemprodutohistorico) {
				
				for (Ordemprodutohistorico ordemprodutohistoricoAux : listaOrdemprodutohistorico) {
					
					if(!ordemprodutohistorico.getCdordemprodutohistorico().equals(ordemprodutohistoricoAux.getCdordemprodutohistorico())){
						
						if( ordemprodutohistorico.getQtde().equals(ordemprodutohistoricoAux.getQtde()) 						&&
							ordemprodutohistorico.getQtdeavaria().equals(ordemprodutohistoricoAux.getQtdeavaria()) 			&&
							ordemprodutohistorico.getQtdefracionada().equals(ordemprodutohistoricoAux.getQtdefracionada()) 	&&
							ordemprodutohistorico.getQtdefalta().equals(ordemprodutohistoricoAux.getQtdefalta()) ){
							
							return true;
							
						}
						
					}
					
				}	
			
			}
			
		}
				
		return false;
		
	}
	
}
