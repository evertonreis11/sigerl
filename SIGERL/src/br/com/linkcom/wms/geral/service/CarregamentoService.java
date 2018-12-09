package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Pessoaendereco;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotapraca;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.geral.bean.vo.ConfirmacaoItemVO;
import br.com.linkcom.wms.geral.bean.vo.ProdutoSemEstoqueVO;
import br.com.linkcom.wms.geral.dao.CarregamentoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.ListagemcarregamentoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirliberacaoveiculoFiltro;
import br.com.linkcom.wms.sincronizador.IntegradorSqlUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.expedicao.AgrupaRotas;
import br.com.linkcom.wms.util.expedicao.CarregamentoApoio;
import br.com.linkcom.wms.util.expedicao.CarregamentoDadosProduto;
import br.com.linkcom.wms.util.expedicao.CarregamentoVO;
import br.com.linkcom.wms.util.expedicao.DadosPopUp;
import br.com.linkcom.wms.util.expedicao.DadosProduto;
import br.com.linkcom.wms.util.expedicao.GerarSeparacao;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class CarregamentoService extends GenericService<Carregamento> {
	
	private CarregamentoDAO carregamentoDAO;
	private PedidovendaprodutoService pedidovendaprodutoService;
	private PracaService pracaService;
	private ClienteService clienteService;
	private TipooperacaoService tipooperacaoService;
	private VeiculoService veiculoService;
	private OrdemservicoService ordemservicoService;
	private CarregamentoitemService carregamentoitemService;
	private EtiquetaexpedicaoService etiquetaexpedicaoService;
	private PessoaenderecoService pessoaenderecoService;
	private CarregamentohistoricoService carregamentohistoricoService;
	private PedidovendaprodutohistoricoService pedidovendaprodutohistoricoService;
	private TransactionTemplate transactionTemplate;

	
	public void setCarregamentoDAO(CarregamentoDAO carregamentoDAO) {
		this.carregamentoDAO = carregamentoDAO;
	}
	
	public void setPedidovendaprodutoService(PedidovendaprodutoService pedidovendaprodutoService) {
		this.pedidovendaprodutoService = pedidovendaprodutoService;
	}
	
	public void setTipooperacaoService(TipooperacaoService tipooperacaoService) {
		this.tipooperacaoService = tipooperacaoService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setPracaService(PracaService pracaService) {
		this.pracaService = pracaService;
	}
	
	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setCarregamentoitemService(CarregamentoitemService carregamentoitemService) {
		this.carregamentoitemService = carregamentoitemService;
	}
	
	public void setEtiquetaexpedicaoService(EtiquetaexpedicaoService etiquetaexpedicaoService) {
		this.etiquetaexpedicaoService = etiquetaexpedicaoService;
	}
	
	public void setPessoaenderecoService(PessoaenderecoService pessoaenderecoService) {
		this.pessoaenderecoService = pessoaenderecoService;
	}
	
	public void setCarregamentohistoricoService(CarregamentohistoricoService carregamentohistoricoService) {
		this.carregamentohistoricoService = carregamentohistoricoService;
	}
	
	public void setPedidovendaprodutohistoricoService(PedidovendaprodutohistoricoService pedidovendaprodutohistoricoService) {
		this.pedidovendaprodutohistoricoService = pedidovendaprodutohistoricoService;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}


	/* singleton */
	private static CarregamentoService instance;
	public static CarregamentoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(CarregamentoService.class);
		}
		return instance;
	}
	
	/**
	 * Prepara o carregamento para ser salvo
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #prepareListaCarregamentoItem(Carregamento, List, List)
	 * @see #translatePopUpParameters(String)
	 * 
	 * @param listaPedidoVendaProduto
	 * @param filtro
	 * @param definitivo
	 */
	public Carregamento save(String listaPedidoVendaProduto,CarregamentoFiltro filtro,boolean definitivo) {

		Carregamento carregamento = new Carregamento();
		if(filtro.getCdcarregamento() != null && filtro.getCdcarregamento() != -1)
			carregamento.setCdcarregamento(filtro.getCdcarregamento());

		//verificando se este carregamento já foi gerado
		//em alguns casos o carregamento está voltando para status MONTADO, este pode ser um  ponto
		Carregamento carregamentoTemp = null;
		if (carregamento.getCdcarregamento() != null){
			carregamentoTemp = findByCarregamento(carregamento);
			if (carregamentoTemp != null && Carregamentostatus.EM_SEPARACAO.equals(carregamentoTemp.getCarregamentostatus()))
				throw new WmsException("Este carregamento já está em separação.");
		}
		carregamento.setBox(filtro.getBox());
		carregamento.setCdusuariogera(WmsUtil.getUsuarioLogado().getCdpessoa());
		carregamento.setFilialretirada(filtro.getFilialretirada());
		carregamento.setTipooperacaoretirada(filtro.getTipooperacaoretirada());
		carregamento.setTransportador(filtro.getTransportador());
		carregamento.setMotorista(filtro.getMotorista());
		carregamento.setDeposito(WmsUtil.getDeposito());
		
		Veiculo veiculo = filtro.getVeiculo();
		
		if(veiculo != null)
			carregamento.setPaletesdisponiveis(filtro.getPaletesdisponiveis());
		
		carregamento.setVeiculo(veiculo);
		
		if(definitivo){
			carregamento.setCarregamentostatus(Carregamentostatus.MONTADO);
			carregamento.setDtcarregamento(new Timestamp(System.currentTimeMillis()));
		}else{
			carregamento.setCarregamentostatus(Carregamentostatus.EM_MONTAGEM);
		}
		
		List<Carregamentoitem> listaCarregamentoItem = new ArrayList<Carregamentoitem>();
		List<String[]> arrayPedidoVendaProduto;

		if(listaPedidoVendaProduto == null || listaPedidoVendaProduto.equals(""))
			carregamento.setListaCarregamentoitem(null);
		else{
			arrayPedidoVendaProduto = translatePopUpParameters(listaPedidoVendaProduto,carregamento, filtro.getDtentregainicio(), filtro.getDtentregafim());
			prepareListaCarregamentoItem(carregamento,arrayPedidoVendaProduto,listaCarregamentoItem);
		}
		
		if (filtro.isValidarConcorrencia() && conflitoEdicao(carregamentoTemp, listaCarregamentoItem, filtro.getItensRemovidos(), filtro.getDtentregainicio(), filtro.getDtentregafim())){
			throw new WmsException("Este carregamento foi alterado em outra estação de trabalho.");
		}else{
			try{
				saveOrUpdate(carregamento);
				carregamentoitemService.saveCarregamentoItem(listaCarregamentoItem,carregamento);
//				if(definitivo){
//					final List<Pedidovendaproduto> listaPvp = pedidovendaprodutoService.findByListCarregamentoItem(listaCarregamentoItem);			
//					pedidovendaprodutohistoricoService.criarHistoricoPedidosForCarga(listaPvp,carregamento);
//					pedidovendaprodutoService.updateStatusForPedidos(CollectionsUtil.listAndConcatenate(listaPvp,"cdpedidovendaproduto", ","),
//																	 Pedidovendaprodutostatus.EM_CARREGAMENTO);
//				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new WmsException("Não foi possível gerar o carregamento.", e);
				
			}
			if(carregamento.getHouveConflitos() != null && carregamento.getHouveConflitos()){
				throw new WmsException("Alguns produtos não puderam ser carregados pois ja pertencem a outros carregamentos.");
			}			
		}
		
		return carregamento;
	}
	
	/**
	 * Verifica se ocorreu conflito de edição no carregamento, ou seja, se dois usuários estão editando o mesmo
	 * carregamento simultâneamente.
	 * 
	 * @param carregamentoTemp
	 * @param listaCarregamentoItem
	 * @param itensRemovidos 
	 * @return
	 */
	private boolean conflitoEdicao(Carregamento carregamentoTemp, List<Carregamentoitem> listaCarregamentoItem, String itensRemovidos, Date datainicio, Date datafim) {
		
		if (carregamentoTemp == null)
			return false;
		
		List<Integer> removidos = new ArrayList<Integer>();
		if (WmsUtil.isDefined(itensRemovidos)){
			String[] itens = itensRemovidos.split(",");
			for (String item : itens){
				//se não é um FiveKeys
				if (!item.contains("["))
					removidos.add(Integer.valueOf(item));
				else{
					item = item.replace("[", "");
					item = item.replace("]", "");
					List<Long> longList = pedidovendaprodutoService.findCdsByFiveKeys(item.split(";"),carregamentoTemp, datainicio, datafim);
					for (Long cd : longList)
						removidos.add(cd.intValue());
				}
			}
		}
		
		List<Integer> pvpBaseDados = new ArrayList<Integer>();
		for (Carregamentoitem ci : carregamentoTemp.getListaCarregamentoitem())
			if (!removidos.contains(ci.getPedidovendaproduto().getCdpedidovendaproduto()))//ignorar os que foram removidos
				pvpBaseDados.add(ci.getPedidovendaproduto().getCdpedidovendaproduto());
		
		List<Integer> pvpEdicao = new ArrayList<Integer>();
		for (Carregamentoitem ci : listaCarregamentoItem)
			if (ci.getCdcarregamentoitem() != null)//validar apenas os itens que já existiam
				pvpEdicao.add(ci.getPedidovendaproduto().getCdpedidovendaproduto());
		
		return !pvpBaseDados.containsAll(pvpEdicao) || !pvpEdicao.containsAll(pvpBaseDados);
	}

	/**
	 * Carrega um Carregamento e sua propriedade Carregamentostatus.
	 * 
	 * @see CarregamentoDAO#loadWithStatus(Carregamento)
	 * @param carregamento
	 * @return
	 */
	public Carregamento loadWithStatus(Carregamento carregamento) {
		return carregamentoDAO.loadWithStatus(carregamento);
	}

	/**
	 * Prepara uma lista de carregamentoItem com a ordem e pedidovendaproduto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.PedidovendaprodutoService#findByCd(Pedidovendaproduto pedidovendaproduto, Carregamento carregamento)
	 * 
	 * @param carregamento
	 * @param arrayPedidoVendaProduto
	 * @param listaCarregamentoItem
	 */
	public void prepareListaCarregamentoItem(Carregamento carregamento,List<String[]> arrayPedidoVendaProduto,List<Carregamentoitem> listaCarregamentoItem) {

		for (int z = 0; z < arrayPedidoVendaProduto.size(); z++){
			String[] array = arrayPedidoVendaProduto.get(z);
			for (int i = 0 ; i< array.length; i++) {
				String str = array[i];
				Carregamentoitem carregamentoitem = new Carregamentoitem();
				try{
					if(str.trim().equals(""))
						carregamento.setHouveConflitos(Boolean.TRUE);
					else{
						Pedidovendaproduto pedidovendaproduto = new Pedidovendaproduto(Integer.parseInt(str.trim()));
						Carregamentoitem item = carregamentoitemService.findByPedidoVendaProduto(pedidovendaproduto, carregamento);
						if(item != null && carregamento.getCdcarregamento() != null)
							carregamentoitem.setCdcarregamentoitem(item.getCdcarregamentoitem());
						carregamentoitem.setPedidovendaproduto(pedidovendaproduto);
						
						carregamentoitem.setOrdem(new Long(z + 1));
						
						listaCarregamentoItem.add(carregamentoitem);	
					}
				}catch (Exception e) {
					throw new WmsException("Não foi possível salvar o carregamento.");
				}
			}
		}
	}
	
	/**
	 * Transfroma uma string com os cdpedidovendaproduto agrupados
	 * em uma List<String[]>
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.PedidovendaprodutoService#findCdsByFiveKeys(String[] array)
	 * 
	 * @param listaPedidoVendaProduto
	 * @param carregamento 
	 * @return
	 */
	private List<String[]> translatePopUpParameters(String listaPedidoVendaProduto, Carregamento carregamento, Date datainicio, Date datafim) {
		List<String[]> list = new ArrayList<String[]>();

		int beginIndex = listaPedidoVendaProduto.indexOf("[");
		int endIndex = 0;
		String str = "";
		
		while(beginIndex != -1){
			endIndex = listaPedidoVendaProduto.indexOf("]",beginIndex);
			str = listaPedidoVendaProduto.substring(beginIndex+1,endIndex);
			
			if(str.endsWith(","))
				list.add(str.split(","));
			else{
				List<Long> longList = pedidovendaprodutoService.findCdsByFiveKeys(str.split(","),carregamento, datainicio, datafim);
				String stringLong = longList.toString();
				list.add(stringLong.substring(1,stringLong.length()-1).split(","));
			}			
			beginIndex = listaPedidoVendaProduto.indexOf("[",endIndex);
		}
		
		return list;
	}   
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO.findForListagemCarregamento(ListagemcarregamentoFiltro filtro)
	 * Gera a listagem de carregamento a partir do filtro.
	 * 
	 * @author Pedro Gonçalves
	 * @param filtro
	 * @return
	 */
	public List<Carregamento> findForListagemCarregamento(ListagemcarregamentoFiltro filtro) {
		return carregamentoDAO.findForListagemCarregamento(filtro);
	}
	
	/**
	 * Método de referência ao DAO.
	 * Carrega o carregamento com os dados de box e cliente.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#findForSeparacao(Carregamento carregamento)
	 * @param carregamento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Carregamento findForSeparacao(Carregamento carregamento,String ... extraargs) {
		return carregamentoDAO.findForSeparacao(carregamento,extraargs);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @return
	 */
	public Carregamento findByCarregamento(Carregamento carregamento) {
		return carregamentoDAO.findByCarregamento(carregamento);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#atualizaStatusCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 */
	public void atualizaStatusCarregamento(Carregamento carregamento, String msg, Usuario usuario){
		carregamentoDAO.atualizaStatusCarregamento(carregamento);
		carregamentohistoricoService.criaHistorico(carregamento, msg, usuario);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Thiers Euller
	 * 
	 * @param carregamento
	 */
	public void atualizaCarregamentoCancelado(Carregamento carregamento, Usuario usuario){
		carregamentoDAO.atualizaCarregamentoCancelado(carregamento);
		carregamento.setCarregamentostatus(Carregamentostatus.CANCELADO);
		carregamentohistoricoService.criaHistorico(carregamento, null, usuario);
	}
	
	/**
	 * Prepara os dados para serem carregado no javascript da tela
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #findByCdCarregamento(Integer)
	 * @see #generateAssociatedArray(List, List)
	 * @see br.com.linkcom.wms.geral.service.VeiculoService#findForCarregamentoCombo(Tipoveiculo tipoveiculo, Boolean onlyDisponiveis)
	 * @see br.com.linkcom.wms.geral.service.CarregamentoService#gerItensAndKeys(List<StringBuilder> listaItensCarregamento, List<Long> keys, Carregamento carregamento, StringBuilder tabelaRoteirizacao)
	 * 
	 * @param request
	 * @param cdcarregamento
	 */
	public void prepareForEdition(WebRequestContext request, Integer cdcarregamento, CarregamentoFiltro filtro) {
		List<StringBuilder> listaItensCarregamento = new ArrayList<StringBuilder>();
		List<Long> keys = new ArrayList<Long>();
		StringBuilder tabelaRoteirizacao = new StringBuilder();
		Carregamento carregamento = findByCdCarregamento(cdcarregamento);
		
		if(carregamento!=null && (!carregamento.getCarregamentostatus().equals(Carregamentostatus.EM_MONTAGEM) && !carregamento.getCarregamentostatus().equals(Carregamentostatus.MONTADO))){
			throw new WmsException("Não é possível editar carregamentos com stauts diferente de 'Em Montagem' ou 'Montado'");
		}
		
		if(carregamento.getListaCarregamentoitem() != null || !carregamento.getListaCarregamentoitem().isEmpty()){
			gerItensAndKeys(listaItensCarregamento, keys, carregamento,tabelaRoteirizacao);
			request.setAttribute("PEDIDOSCARREGAMENTO", generateAssociatedArray(keys,listaItensCarregamento));
			request.setAttribute("TABELAROTEIRIZACAO", tabelaRoteirizacao.length() == 0 ? "new Object()" : tabelaRoteirizacao);
		}
		else{
			request.setAttribute("PEDIDOSCARREGAMENTO", "new Object()");
			request.setAttribute("TABELAROTEIRIZACAO", "new Object()");
		}
		
		filtro.setBox(carregamento.getBox());
		Veiculo veiculo = carregamento.getVeiculo() == null ? new Veiculo() : carregamento.getVeiculo();
		request.setAttribute("VEICULOS", veiculoService.findForCarregamentoCombo(veiculo.getTransportador()));
		prepareRequest(request, veiculo);
		filtro.setPaletesdisponiveis(carregamento.getPaletesdisponiveis());
		filtro.setVeiculo(veiculo);
		filtro.setTransportador(veiculo.getTransportador());
		filtro.setMotorista(carregamento.getMotorista());
		filtro.setFilialretirada(carregamento.getFilialretirada());
		filtro.setTipooperacaoretirada(carregamento.getTipooperacaoretirada());
		if(filtro.getFilialretirada() != null || filtro.getTipooperacaoretirada() != null)
			filtro.setTrocarlocal(Boolean.TRUE);
	}
	
	/**
	 * Adiciona os atributos de veiculo no request
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.util.WmsUtil#calcularCubagemMetro(double altura, double largura, double profundidade)
	 * 
	 * @param request
	 * @param veiculo
	 */
	private void prepareRequest(WebRequestContext request, Veiculo veiculo) {
		request.setAttribute("PALETESVEICULO", veiculo.getCapacidadepalete() == null ? "-1" : veiculo.getCapacidadepalete());
		request.setAttribute("PESOVEICULO", veiculo.getCapacidadepeso() == null ? "-1.0" : veiculo.getCapacidadepeso());
		Double cubagem = -1.0;
		if(veiculo.getAltura() != null && veiculo.getLargura() != null && veiculo.getProfundidade() != null){
			cubagem = WmsUtil.calcularCubagemMetro(veiculo.getAltura(), veiculo.getLargura(), veiculo.getProfundidade());
		}
		request.setAttribute("CUBAGEMVEICULO", cubagem);
	}

	/**
	 * Separa os conteudos e chaves para serem carregados no javascript da tela
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #makeCarregamentoObjects(Carregamentoitem, Double, Double, Long, Money, StringBuilder, List, Integer)
	 * 
	 * @param listaItensCarregamento
	 * @param keys
	 * @param carregamento
	 */
	private void gerItensAndKeys(List<StringBuilder> listaItensCarregamento,List<Long> keys, Carregamento carregamento,StringBuilder tabelaRoteirizacao) {
		if(carregamento.getListaCarregamentoitem() != null && !carregamento.getListaCarregamentoitem().isEmpty()){
			int i = 0;
			int keyIndex = 0;
			tabelaRoteirizacao.append("[");
			do{
				Long keyAux = 0L;
				Double peso = 0.0;
				Double cubagem = 0.0;
				Long qtde = 0l;
				Money valor = new Money();
				Carregamentoitem carregamentoitem = null;
				listaItensCarregamento.add(new StringBuilder());
				StringBuilder str = listaItensCarregamento.get(keyIndex);
				do{
					carregamentoitem = carregamento.getListaCarregamentoitem().get(i);
					Pedidovendaproduto pedidovendaproduto = carregamentoitem.getPedidovendaproduto();
					if(pedidovendaproduto != null){
						keyAux = pedidovendaproduto.getCdpedidovendaproduto() > keyAux ? pedidovendaproduto.getCdpedidovendaproduto() : keyAux;
						
						str.append(pedidovendaproduto.getCdpedidovendaproduto() + ",");				
						
						Produto produto = pedidovendaproduto.getProduto();
						
						Double pesoAux = produto.getPesounitario() != null ? produto.getPesounitario() : 0;
						Long qtdeAux = pedidovendaproduto.getQtde() != null ? pedidovendaproduto.getQtde() : 0;
						Double valorAux = pedidovendaproduto.getValor() != null ? Double.parseDouble(pedidovendaproduto.getValor().getValue().toString()) : 0.0;
						
						peso += pesoAux * qtdeAux;
						if (produto.getCubagemunitaria() != null)
							cubagem += produto.getCubagemunitaria() * qtdeAux;
						qtde += qtdeAux;
						valor = valor.add(new Money(valorAux * qtdeAux));
					}
					i++;
				}while(i < carregamento.getListaCarregamentoitem().size() && carregamentoitem.getOrdem().equals(carregamento.getListaCarregamentoitem().get(i).getOrdem()));
				makeCarregamentoObjects(carregamentoitem,peso,cubagem,qtde,valor,tabelaRoteirizacao,keys,keyAux);
				keyIndex++;
			}while(i < carregamento.getListaCarregamentoitem().size());
			tabelaRoteirizacao.deleteCharAt(tabelaRoteirizacao.length() - 1);
			tabelaRoteirizacao.append("]");
		}
	}
	
	/**
	 * Cria uma string que representará os objetos, relacionados com 
	 * a tela de carregamento, em javascript
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.VformacaocargaService.findKey(String cdRota, String cdPraca, String cdtipooperacao, String cep)
	 * 
	 * @param carregamentoitem
	 * @param peso
	 * @param cubagem
	 * @param qtde
	 * @param valor
	 * @param tabelaRoteirizacao
	 * @param keys
	 * @param keyAux
	 */
	@SuppressWarnings("unchecked")
	private void makeCarregamentoObjects(Carregamentoitem carregamentoitem,Double peso, Double cubagem, Long qtde, 
									     Money valor,StringBuilder tabelaRoteirizacao,List<Long> keys,Long keyAux) {
		
		Cliente cliente = carregamentoitem.getPedidovendaproduto().getPedidovenda().getCliente();
		
		Cliente filialentrega = null;
		
		if (Tipooperacao.TRANSFERENCIA_CD_CD_CLIENTE.equals(carregamentoitem.getPedidovendaproduto().getTipooperacao())){
			filialentrega = carregamentoitem.getPedidovendaproduto().getFilialentregatransbordo();
		}else {
			filialentrega = carregamentoitem.getPedidovendaproduto().getFilialEntrega();			
		}
		
		if (filialentrega == null)
			filialentrega = new Cliente();
		
		Praca praca = carregamentoitem.getPedidovendaproduto().getPraca();

		String cep;
		if(filialentrega.getPessoaendereco() == null) {
			cep = carregamentoitem.getPedidovendaproduto().getPessoaendereco().getCep().getValue();
		} else{
			cep = filialentrega.getPessoaendereco().getCep().getValue(); 
		}
		
		String cdPraca = praca != null ? praca.getCdpraca().toString() : null;
		String pracaNome = praca != null ? praca.getNome() : "Praca não encontrada";
		
		String cdRota = null;
		String rotaNome = null;
		if(praca != null && praca.getListaRotapraca() != null && !praca.getListaRotapraca().isEmpty()){
			Rota rota = ((ArrayList<Rotapraca>)(praca.getListaRotapraca())).get(0).getRota();
			
			cdRota = rota.getCdrota().toString();
			rotaNome = rota.getNome();
		}
		else{
			rotaNome = "Rota não encontrada";
		}
		
		Tipooperacao tipooperacao = carregamentoitem.getPedidovendaproduto().getTipooperacao();
		String cdtipooperacao = null;
		String tipooperacaonome = null;
		if(tipooperacao != null){
			cdtipooperacao = tipooperacao.getCdtipooperacao().toString();
			tipooperacaonome = tipooperacao.getNome();
		}
		
		keys.add(keyAux);
			
		String nomeFilial = null;
		if (filialentrega.getNome() != null)
			nomeFilial = filialentrega.getNome().replace("\\", "").replace("'", "\\'");
		
		String filialEmisao = null;
		if (carregamentoitem.getPedidovendaproduto().getPedidovenda().getFilialemissao() != null && carregamentoitem.getPedidovendaproduto().getPedidovenda().getFilialemissao().getNome() != null)
			filialEmisao = carregamentoitem.getPedidovendaproduto().getPedidovenda().getFilialemissao().getNome().replace("\\", "").replace("'", "\\'");
		
		String nomeCliente = null;
		if (cliente.getNome() != null)
			nomeCliente = cliente.getNome().replace("\\", "").replace("'", "\\'");
		
		String nomeDepositoTransbordo = "";
		if (carregamentoitem.getPedidovendaproduto().getDepositotransbordo() != null)
			nomeDepositoTransbordo = carregamentoitem.getPedidovendaproduto().getDepositotransbordo().getNome();
		
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

		String prioridade;
		if (carregamentoitem.getPedidovendaproduto().getPrioridade() != null && carregamentoitem.getPedidovendaproduto().getPrioridade())
			prioridade = "true";
		else
			prioridade = "false";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		tabelaRoteirizacao.append("{'rota':{'cdrota':'"+cdRota+"','nome':'"+rotaNome.replace("\\", "").replace("'", "\\'")+"'}," +
					"'praca':{'cdpraca':'"+cdPraca+"','nome':'"+pracaNome.replace("\\", "").replace("'", "\\'")+"'}," +
					"'tipooperacao':{'cdtipooperacao':'"+cdtipooperacao+"','sigla':'"+tipooperacao.getSigla()+"','nome':'"+tipooperacaonome.replace("\\", "").replace("'", "\\'")+"'}," +
					"'cliente':{'nome':'"+nomeCliente+"','cdpessoa':'"+cliente.getCdpessoa()+"'}," +
					"'filialentrega':{'nome':'"+nomeFilial+"','cdpessoa':'"+filialentrega.getCdpessoa()+"'}," +
					"'totalItens':'"+qtde+"','cep':'"+cep+"','peso':'"+peso+"','cubagem':'"+cubagem+"'," +
					"'prioridade': '"+prioridade+"'," +
					"'cdpedidovenda':'"+carregamentoitem.getPedidovendaproduto().getPedidovenda().getCdpedidovenda()+"'," +
					"'dtprevisaoentrega':'"+dateFormat.format(carregamentoitem.getPedidovendaproduto().getDtprevisaoentrega())+"'," +
					"'numeropedido':'"+carregamentoitem.getPedidovendaproduto().getPedidovenda().getNumero()+"'," +
					"'filialemissao':'"+filialEmisao+"'," +
					"'nomedepositotransbordo':'"+nomeDepositoTransbordo+"'," +
					"'datavenda':'"+ sdf.format(carregamentoitem.getPedidovendaproduto().getPedidovenda().getDtemissao())+"'," +
					"'valor':'"+valor+"','cdformacaocarga':'"+keys.get(keys.size() - 1)+"'},");
		
	}
	
	/**
	 * Cria a representação javascript de um hashmap
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * 
	 * @param keys
	 * @param listaItensCarregamento
	 * @return
	 */
	private String generateAssociatedArray(List<Long> keys, List<StringBuilder> listaItensCarregamento) {
		StringBuilder stringBuilder = new StringBuilder("{");
		for (int i = 0; i < keys.size(); i++) {
			stringBuilder.append(keys.get(i) + ":[\"" + listaItensCarregamento.get(i) + "\"]");
			if(i + 1 < keys.size())
				stringBuilder.append(",");
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#findByCdCarregamento(Integer cdcarregamento)
	 * 
	 * @param cdcarregamento
	 * @return
	 */
	public Carregamento findByCdCarregamento(Integer cdcarregamento){
		return carregamentoDAO.findByCdCarregamento(cdcarregamento);
	}
	
	/**
	 * Prepara os dados para o popUp para tela Flex
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.PedidovendaprodutoService#findByFiveKeys(String[] array, Integer cdcarregamento)
	 * @see #makeTituloPopUp(WebRequestContext, String[])
	 * 
	 * @param request
	 * @return
	 */
	public List<Pedidovendaproduto> preparePopUp(WebRequestContext request) {
		
		Date dtentregainicio = null;
		Date dtentregafim = null;
		
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		if (request.getParameter("dtentregainicio") != null && !request.getParameter("dtentregainicio").isEmpty()){
			try {
				dtentregainicio = dateformat.parse(request.getParameter("dtentregainicio"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (request.getParameter("dtentregafim") != null && !request.getParameter("dtentregafim").isEmpty()){
			try {
				dtentregafim = dateformat.parse(request.getParameter("dtentregafim"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		String[] array = request.getParameter("chave").toString().split(",");
		request.setAttribute("clienteID", request.getParameter("clienteID"));
		request.setAttribute("isRoteirizacao", request.getParameter("isRoteirizacao"));
		Integer cdcarregamento = request.getParameter("cdcarregamento") == null || request.getParameter("cdcarregamento").equals("<null>")
								 ? null : Integer.parseInt(request.getParameter("cdcarregamento"));
		
		List<Pedidovendaproduto> listaPedidoVendaProduto = pedidovendaprodutoService.findByFiveKeys(array,cdcarregamento, dtentregainicio, dtentregafim);
		
		if (listaPedidoVendaProduto != null && listaPedidoVendaProduto.size() > 0){
			StringBuilder builder = concatenaEnderecoEntrega(listaPedidoVendaProduto.get(0));
			
			request.setAttribute("enderecoentrega", builder.toString());
		}else
			request.setAttribute("enderecoentrega", "");
		
		String titulo = makeTituloPopUp(array,cdcarregamento);
		request.setAttribute("TITULO", titulo);
		
		Double cubagemTotal = 0.0;
		Double pesoTotal = 0.0;
		Long qtdeTotal = 0L;
		Money valorTotal = new Money();
		Double peso = 0.0;
		Long qtde = 0L;
		
		for (Pedidovendaproduto pedidovendaproduto : listaPedidoVendaProduto) {
			pedidovendaproduto.setCarregado(false);
			Produto produto = pedidovendaproduto.getProduto();
			peso = produto.getPesounitario() == null ? 0.0 : produto.getPesounitario();
			qtde = pedidovendaproduto.getQtde() == null ? 0 : pedidovendaproduto.getQtde();
			produto.setCubagem(produto.getCubagemunitaria() * qtde);
			produto.setPeso(peso * qtde);
			pedidovendaproduto.setValor(new Money(Double.parseDouble(pedidovendaproduto.getValor().getValue().toString()) * qtde));
			if(!pedidovendaproduto.getCarregado()){
				cubagemTotal += produto.getCubagem();
				pesoTotal += produto.getPeso();
				qtdeTotal += qtde;
				valorTotal = valorTotal.add(pedidovendaproduto.getValor());
			}
		}
		
		request.setAttribute("cubagemTotal", cubagemTotal);
		request.setAttribute("pesoTotal", pesoTotal);
		request.setAttribute("qtdeTotal", qtdeTotal);
		request.setAttribute("valorTotal", valorTotal);
		return listaPedidoVendaProduto;
	}

	/**
	 * Concatena o endereço de entrega para exibir na tela de detalhes do pedido.
	 * 
	 * @author Giovane Freitas
	 * @param pedidovendaproduto
	 * @return
	 */
	public StringBuilder concatenaEnderecoEntrega(Pedidovendaproduto pedidovendaproduto) {
		Pessoaendereco endereco = pessoaenderecoService.load(pedidovendaproduto.getPessoaendereco());
		
		StringBuilder builder = new StringBuilder();
		if (endereco != null){
			builder.append(endereco.getLogradouro() !=  null ? endereco.getLogradouro() : "");
		
			if (endereco.getNumero() != null && !endereco.getNumero().trim().isEmpty()){
				builder.append(", ");
				builder.append(endereco.getNumero());
			}
			
			if (endereco.getComplemento() !=  null && !endereco.getComplemento().trim().isEmpty()){
				builder.append(", ");
				builder.append(endereco.getComplemento());
			}
			builder.append(". ");
			builder.append(endereco.getBairro() != null ? endereco.getBairro() : "");
			builder.append(". ");
			builder.append(endereco.getMunicipio() != null ? endereco.getMunicipio() : "");
			builder.append(" - ");
			builder.append(endereco.getUf() != null ? endereco.getUf() : "");
			builder.append(". CEP: ");
			builder.append(endereco.getCep() != null ? endereco.getCep() : "");
		}
		return builder;
	}
	
	/**
	 * Prepara os dados para o popUp para tela Flex
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.PedidovendaprodutoService#findByFiveKeys(String[] array, Integer cdcarregamento)
	 * @see #makeTituloPopUp(WebRequestContext, String[])
	 * 
	 * @param request
	 * @return
	 */
	public DadosPopUp preparePopUp(DadosPopUp dadosPopUp) {		
		String[] array = dadosPopUp.getChave().toString().split(",");
		
		Integer cdcarregamento = dadosPopUp.getCdcarregamento();
		boolean preRotericacao = dadosPopUp.getRoteirizacao() == null || !dadosPopUp.getRoteirizacao();
		if (preRotericacao || (cdcarregamento != null && cdcarregamento.compareTo(0) <= 0))
			cdcarregamento = null;
		
		List<Pedidovendaproduto> listaPedidoVendaProduto = pedidovendaprodutoService.findByFiveKeys(array,cdcarregamento, null, null);
		
		if (listaPedidoVendaProduto != null && listaPedidoVendaProduto.size() > 0){
			StringBuilder builder = concatenaEnderecoEntrega(listaPedidoVendaProduto.get(0));
			dadosPopUp.setEnderecoentrega(builder.toString());
		}else
			dadosPopUp.setEnderecoentrega("");
		
		String titulo = makeTituloPopUp(array,cdcarregamento);
		dadosPopUp.setTitulo(titulo);
		
		Double cubagemTotal = 0.0;
		Double pesoTotal = 0.0;
		Long qtdeTotal = 0L;
		Money valorTotal = new Money();
		Double peso = 0.0;
		Long qtde = 0L;
		
		for (Pedidovendaproduto pedidovendaproduto : listaPedidoVendaProduto) {
			pedidovendaproduto.setCarregado(false);
			Produto produto = pedidovendaproduto.getProduto();
			peso = produto.getPesounitario() == null ? 0.0 : produto.getPesounitario();
			qtde = pedidovendaproduto.getQtde() == null ? 0 : pedidovendaproduto.getQtde();
			produto.setCubagem(produto.getCubagemunitaria() * qtde);
			produto.setPesounitario(peso * qtde);
			pedidovendaproduto.setValor(new Money(Double.parseDouble(pedidovendaproduto.getValor().getValue().toString()) * qtde));
			if(!pedidovendaproduto.getCarregado()){
				cubagemTotal += produto.getCubagemunitaria();
				pesoTotal += produto.getPesounitario();
				qtdeTotal += qtde;
				valorTotal = valorTotal.add(pedidovendaproduto.getValor());
			}
		}
		
		dadosPopUp.setCubagemTotal(cubagemTotal);
		dadosPopUp.setPesoTotal(pesoTotal);
		dadosPopUp.setQtdeTotal(qtdeTotal);
		dadosPopUp.setValorTotal(valorTotal);
		dadosPopUp.setListaPedidoVendaProduto(listaPedidoVendaProduto);

		return dadosPopUp;
	}
	
	/**
	 * Cria o título do popup
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.PracaService#findByPraca(Praca praca)
	 * @see br.com.linkcom.wms.geral.service.TipooperacaoService#findByCd(Integer cdtipooperacao)
	 * @see br.com.linkcom.wms.geral.service.ClienteService#findByCd(Integer cd)
	 * 
	 * @param request
	 * @param array
	 * @param cdcarregamento 
	 */
	@SuppressWarnings("unchecked")
	private String makeTituloPopUp(String[] array, Integer cdcarregamento) {
		Praca praca = new Praca();
		Rota rota = new Rota();
		Tipooperacao tipooperacao = new Tipooperacao();
		Cliente cliente = new Cliente();
		if(array[1] != null && !array[1].equals("null") && !array[1].equals("")){
			praca = pracaService.findByPraca(new Praca(Integer.valueOf(array[1])));
			if(praca != null)
				if(praca.getListaRotapraca() != null && praca.getListaRotapraca().size() > 0)
					rota = ((ArrayList<Rotapraca>)praca.getListaRotapraca()).get(0).getRota();
		}
		if(array[2] != null && !array[2].equals("null")){
			tipooperacao = tipooperacaoService.findByCd(Integer.valueOf(array[2]));
		}
		if(array[0] != null && !array[2].equals("null")){
			cliente = clienteService.findByCd(Integer.valueOf(array[0]));
		}
		StringBuilder builder = new StringBuilder();
		builder.append(rota == null || rota.getNome() == null ? "Rota não encontrada" : rota.getNome());
		builder.append("/");
		builder.append(praca == null || praca.getNome() == null ? "Praca não encontrada" : praca.getNome());
		builder.append("/");
		builder.append(tipooperacao.getNome());
		builder.append("/");
		builder.append(cliente.getNome());
		if(cdcarregamento != null){
			builder.append("/");
			builder.append("Carregamento "+cdcarregamento);
		}
		return builder.toString();
	}
	
	/**
	 * Método de referência do DAO.
	 * 
	 * Carrega o carregamento para a tela de carregamento.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#loadCarregamentoForGerenciamento(Carregamento carregamento)
	 * @param carregamento
	 * @author Pedro Gonçalves
	 * @return
	 */
	public Carregamento loadCarregamentoForGerenciamento(Carregamento carregamento){
		return carregamentoDAO.loadCarregamentoForGerenciamento(carregamento);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que salva ou atualiza um carregamento.
	 * 
	 * @author Arantes
	 * 
	 * @param carregamento
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#saveOrUpdateEntity(Carregamento)
	 * 
	 */
	public void saveOrUpdateEntity(Carregamento carregamento) {
		carregamentoDAO.saveOrUpdateEntity(carregamento);
	}
	
	
	/**
	 * 
	 * Método que constrói um map onde key é o id do PedidoVendaProduto e o value é o valor da qtde coletada
	 * 
	 * @author Arantes
	 *  
	 * @param listaQtdeColetada
	 * @param listaEtiquetaExped
	 * @return Map<Integer, Long>
	 * 
	 */
	public Map<String, Long> construirMap(String listaQtdeColetada, String listaOsproduto) {
		String[] osProduto = listaOsproduto.split(",");
		String[] qtdeColetada = listaQtdeColetada.split(",");
		
		Map<String, Long> map = new HashMap<String, Long>();
		
		for (int i=0; i < osProduto.length; i++) {
			map.put(osProduto[i], new Long(qtdeColetada[i]));
			
		}
		return map;
	}
	
	
	
	/**
	 * Cancela o carregamento e suas referências
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #atualizaStatusCarregamento(Carregamento)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#updateStatusordemservicoByCarregamento(Carregamento carregamento)
	 * @see br.com.linkcom.wms.geral.service.PedidovendaprodutoService#habilitarByCarregamentoCancelado(Carregamento)
	 * @see br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService#removeEtiquetasCarregamento(Carregamento carregamento)
	 * 
	 * @param carregamento
	 */
	public void cancelarCarregamento(final Carregamento carregamento) {
		if (carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		TransactionTemplate transactionTemplate = Neo.getObject(TransactionTemplate.class);
		transactionTemplate.execute(new TransactionCallback(){

			public Object doInTransaction(TransactionStatus status) {
				carregamento.setCarregamentostatus(Carregamentostatus.CANCELADO);
				
				carregamento.setDtcancela(new Timestamp(System.currentTimeMillis()));
				carregamento.setCdusuariocancela(WmsUtil.getUsuarioLogado().getCdpessoa());
				atualizaCarregamentoCancelado(carregamento, WmsUtil.getUsuarioLogado());
				
				EnderecoprodutoService.getInstance().removerQtdeReservadaSaida(carregamento);
				EnderecoprodutoService.getInstance().cancelarReabastecimentoPicking(carregamento);
				ordemservicoService.updateStatusordemservicoByCarregamento(carregamento);
				pedidovendaprodutoService.habilitarByCarregamentoCancelado(carregamento);
//				pedidovendaprodutohistoricoService.criarHistoricoForCancelamentoCarga(carregamento);
				etiquetaexpedicaoService.removeEtiquetasCarregamento(carregamento);

				return null;
			}
			
		});
		
	}
	
	/**
	 * 
	 * Método que valida o status do carregamento de uma determinada ordem de serviço.
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return Boolean
	 */
	public Boolean validarCarregamentoordemservico(Ordemservico ordemservico) {
		if((ordemservico.getCarregamento() == null) || 
		   (!ordemservico.getCarregamento().getCarregamentostatus().equals(Carregamentostatus.CONFERIDO) && !ordemservico.getCarregamento().getCarregamentostatus().equals(Carregamentostatus.EM_SEPARACAO)) && !ordemservicoService.hasItensCarregamentoFaturadosOutraFilial(ordemservico)) {
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;		
	}
	
	/**
	 * Método de referencia ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#enderecarCarregamento(Deposito deposito, Carregamento carregamento)
	 * 
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void enderecarCarregamento(Carregamento carregamento){
		carregamentoDAO.enderecarCarregamento(carregamento);
	}
	
	/**
	 * Chama a procedure 'GERAR_REABASTECIMENTO' no banco de dados do sistema
	 * 
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#gerarReabastecimento(Carregamento) 
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void gerarReabastecimento(final Carregamento carregamento){
		TransactionTemplate transactionTemplate = Neo.getObject(TransactionTemplate.class);
		
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				carregamentoDAO.gerarReabastecimento(carregamento);
				return null;
			}
		});
	}
	
	/**
	 * Chama a procedure 'GERAR_REABASTECIMENTO_BOX' no banco de dados do sistema
	 * 
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.CarregamentoDAO#gerarReabastecimentoBox(Expedicao) 
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param expedicao
	 * @throws SQLException 
	 */
	public void gerarReabastecimentoBox(final Expedicao expedicao){
		carregamentoDAO.gerarReabastecimentoBox(expedicao);
	}
	
	/**
	 * Atualiza a lista de rotas no carregamento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param carregamento
	 */
	public void atualizarListaRotas(Carregamento carregamento) {
		carregamentoDAO.atualizarListaRotas(carregamento);
	}

	
	/**
	 * Finaliza um carregamento e realiza a baixa no estoque.
	 * 
	 * @see CarregamentoDAO#baixarEstoque(Carregamento)
	 * @author Giovane Freitas
	 * 
	 * @param carregamento
	 * @param listaOrdemservico 
	 */
	public void finalizar(final Carregamento carregamento, final List<Ordemservico> listaOrdemservico) {
		Carregamento carregamentoAux = load(carregamento);

		boolean isOperacaoPorBox = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.OPERACAO_EXPEDICAO_POR_BOX, carregamentoAux.getDeposito());
		boolean isSegundaConferencia = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA, carregamentoAux.getDeposito()); 
		
		if (isOperacaoPorBox){
			List<Ordemservico> listaConferenciaBox = new ArrayList<Ordemservico>();
			if(isSegundaConferencia)
				 listaConferenciaBox = ordemservicoService.findByCarregamentoAndOrdemTipo(carregamentoAux, Ordemtipo.CONFERENCIA_EXPEDICAO_2);
			else
				listaConferenciaBox = ordemservicoService.findByCarregamentoAndOrdemTipo(carregamentoAux, Ordemtipo.CONFERENCIA_EXPEDICAO_1);
			
			//TODO: Quando criar as ordens de conferência de embalagem isso deverá ser alterado.
			List<Ordemservico> listaConferenciaCheckout = ordemservicoService.findByCarregamentoAndOrdemTipo(carregamentoAux, Ordemtipo.CONFERENCIA_CHECKOUT);
			listaConferenciaBox.addAll(listaConferenciaCheckout);
			
			for (Ordemservico conferenciaBox : listaConferenciaBox){
				// atualiza a quantidade confirmada do carregamentoitem
				List<ConfirmacaoItemVO> listaConfirmacao = ordemservicoService.findQtdeConfirmada(conferenciaBox);
				for (ConfirmacaoItemVO item : listaConfirmacao){
					Carregamentoitem carregamentoitem = new Carregamentoitem(item.getCdcarregamentoitem());
					carregamentoitem.setQtdeconfirmada(item.getQtdeconfirmada() != null ? item.getQtdeconfirmada() : 0);
					carregamentoitemService.updateQtdeconfirmada(carregamentoitem);
				}
//				List<Pedidovendaproduto> listaPvp = pedidovendaprodutoService.findByConfirmacaoItemVO(conferenciaBox);
//				pedidovendaprodutohistoricoService.criarHistoricoPedidoForCorte(listaPvp, carregamentoAux);
//				pedidovendaprodutoService.updateStatusForPedidos(CollectionsUtil.listAndConcatenate(listaPvp,"cdpedidovendaproduto", ","),Pedidovendaprodutostatus.DISPONIVEL);
			}
			
			//Atualiza os cortes que foram feitos na primeira conferência.
			carregamentoitemService.atualizaCorte(carregamento);
		}
		
		//Giovane: 03/09/2011
		//Se a operação não é por box e a separação foi feita via coletor, então a quantidade confirmada é gravada somente agora ao finalizar
		if (!isOperacaoPorBox && ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.SEPARACAO_VIA_COLETOR, null)){
			for (Ordemservico ordemservico : listaOrdemservico) {
				if(ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO)) {
					// atualiza a quantidade confirmada do carregamentoitem
					List<Carregamentoitem> listaCarregamentoitem = carregamentoitemService.findForConfirmacaoCorte(ordemservico);
					carregamentoitemService.atualizarQtdeconfirmada(listaCarregamentoitem);
				}
			}
		}

		if((carregamentoAux.getVeiculo() == null || carregamentoAux.getVeiculo().getCdveiculo() == null) && carregamento.getVeiculo() != null && carregamento.getVeiculo().getCdveiculo() != null)
			carregamentoAux.setVeiculo(carregamento.getVeiculo());
		
		//O status tem que ser atualizado por último por causa do trigger que libera os pedidos cortados
		carregamentoAux.setCarregamentostatus(Carregamentostatus.FINALIZADO);
		carregamentoAux.setDtfimcarregamento(new Timestamp(System.currentTimeMillis()));
		saveOrUpdateNoUseTransaction(carregamentoAux);
		
		if (WmsUtil.isFinalizadoBaixaEstoque() && !isOperacaoPorBox){
			carregamentoDAO.baixarEstoque(carregamentoAux);
			for (Ordemservico ordemservico : listaOrdemservico){ 
				if(ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO)){
					ordemservicoService.callProcedureAtualizarRastreabilidade(ordemservico);
				}
			}
		}
	}
	
	
	/**
	 * Método que busca dados do produto gravados na session
	 * 
	 * @return
	 */
	public DadosProduto getDadosCarregamento(){
		return (DadosProduto) NeoWeb.getRequestContext().getSession().getAttribute("dadosProduto");
	}

	/**
	 * Método chamado qnd a tela do flex esta sendo carregada. Este método caso o usuário esteje editando carrega os dados do carregamento.
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public CarregamentoDadosProduto getDadosCarregamentoFlex(){
		String cdcarregamento = (String) NeoWeb.getRequestContext().getSession().getAttribute("cdcarregamento");
		if(cdcarregamento != null && !cdcarregamento.equals("")){
			 NeoWeb.getRequestContext().getSession().removeAttribute("cdcarregamento");
			 CarregamentoDadosProduto carregamentoDadosProduto = preparaCarregamentoEdicao(Integer.parseInt(cdcarregamento));
			 return carregamentoDadosProduto;
		}
		return null;
	}
	
	/**
	 * Método que começa a buscar o carregamento e montar.
	 * 
	 * @see #findByCarregamento(Carregamento)
	 * @see #montaCarregamentoDadosProduto(List, CarregamentoDadosProduto)
	 * @param cdcarregamento
	 * @return
	 * 
	 * @author Tomás Rabelo
	 */
	private CarregamentoDadosProduto preparaCarregamentoEdicao(Integer cdcarregamento) {
		Carregamento carregamento = findByCdCarregamento(cdcarregamento);
			
		CarregamentoDadosProduto carregamentoDadosProduto = new CarregamentoDadosProduto(cdcarregamento, carregamento.getVeiculo(),
																						 carregamento.getVeiculo() != null ? carregamento.getVeiculo().getTransportador() : null, carregamento.getBox(), 
																						 carregamento.getFilialretirada(), carregamento.getTipooperacaoretirada(), 
																						 (carregamento.getTipooperacaoretirada() != null || carregamento.getFilialretirada() != null ? Boolean.TRUE : Boolean.FALSE));
		
		if(carregamento.getListaCarregamentoitem() != null && carregamento.getListaCarregamentoitem().size() != 0){
			montaCarregamentoDadosProduto(carregamento.getListaCarregamentoitem(), carregamentoDadosProduto);
		}

		return carregamentoDadosProduto;
	}

	/**
	 * 
	 * Método que monta os dados apresentados na roterização e dos respectivos pop up's.
	 * 
	 * @see #makeCarregamentoObjects(Carregamentoitem, Double, Double, Long, Money, CarregamentoApoio, CarregamentoDadosProduto)
	 * @param listaCarregamentoitem
	 * @param carregamentoDadosProduto
	 * @author Tomás Rabelo
	 */
	private void montaCarregamentoDadosProduto(List<Carregamentoitem> listaCarregamentoitem, CarregamentoDadosProduto carregamentoDadosProduto) {
		int i = 0;
		int keyIndex = 0;
		
		CarregamentoApoio carregamentoApoio = null;
		do{
			carregamentoApoio = new CarregamentoApoio();
			
			Integer keyAux = 0;
			Double peso = 0.0;
			Double cubagem = 0.0;
			Long qtde = 0l;
			Money valor = new Money();
			Carregamentoitem carregamentoitem = null;
			
			do{
				carregamentoitem = listaCarregamentoitem.get(i);
				Pedidovendaproduto pedidovendaproduto = carregamentoitem.getPedidovendaproduto();
				if(pedidovendaproduto != null){
					keyAux = pedidovendaproduto.getCdpedidovendaproduto() > keyAux ? pedidovendaproduto.getCdpedidovendaproduto() : keyAux;
					
					Produto produto = pedidovendaproduto.getProduto();
					
					Double pesoAux = produto.getPesounitario() != null ? produto.getPesounitario() : 0;
					Long qtdeAux = pedidovendaproduto.getQtde() != null ? pedidovendaproduto.getQtde() : 0;
					Double valorAux = pedidovendaproduto.getValor() != null ? Double.parseDouble(pedidovendaproduto.getValor().getValue().toString()) : 0.0;
					
					peso += pesoAux * qtdeAux;
					if (produto.getCubagemunitaria() != null)
						cubagem += produto.getCubagemunitaria() * qtdeAux;
					qtde += qtdeAux;
					valor = valor.add(new Money(valorAux * qtdeAux));
					
					carregamentoApoio.getListaPedidoVendaProduto().add(pedidovendaproduto);
					}
				i++;
			}while(i < listaCarregamentoitem.size() && carregamentoitem.getOrdem().equals(listaCarregamentoitem.get(i).getOrdem()));
			carregamentoApoio.setCdformacaocarga(new Long(keyAux.intValue()));
			makeCarregamentoObjects(carregamentoitem,peso,cubagem,qtde,valor,carregamentoApoio,carregamentoDadosProduto);
			keyIndex++;
		}while(i < listaCarregamentoitem.size());
	}
		
	/**
	 * 
	 * Método que continua montando os dados apresentados na roterização e dos respectivos pop up's.
	 * 
	 * @param carregamentoitem
	 * @param peso
	 * @param cubagem
	 * @param qtde
	 * @param valor
	 * @param carregamentoApoio
	 * @param carregamentoDadosProduto
	 * @author Tomás Rabelo
	 */
	@SuppressWarnings("unchecked")
	private void makeCarregamentoObjects(Carregamentoitem carregamentoitem, Double peso, Double cubagem, Long qtde, Money valor, CarregamentoApoio carregamentoApoio, CarregamentoDadosProduto carregamentoDadosProduto) {
		Praca praca = null;
		
		carregamentoApoio.setCdpessoa(carregamentoitem.getPedidovendaproduto().getPedidovenda().getCliente().getCdpessoa());
		carregamentoApoio.setPedidovenda(carregamentoitem.getPedidovendaproduto().getPedidovenda().getNumero());
		
		Cliente filialentrega = null;
		if (Tipooperacao.TRANSFERENCIA_CD_CD_CLIENTE.equals(carregamentoitem.getPedidovendaproduto().getTipooperacao()) || 
			Tipooperacao.ENTREGA_CD_CD_CLIENTE.equals(carregamentoitem.getPedidovendaproduto().getTipooperacao())){
			filialentrega = carregamentoitem.getPedidovendaproduto().getFilialentregatransbordo();
		}else {
			filialentrega = carregamentoitem.getPedidovendaproduto().getFilialEntrega();
		}

		//Quando tem filial de entrega deve exibir o nome da filial e não do cliente que fez o pedido
		if (filialentrega != null){
			carregamentoApoio.setCliente(filialentrega.getNome());
			carregamentoApoio.setPedidovenda(carregamentoApoio.getPedidovenda()+" / "+filialentrega.getNome());
		}else
			carregamentoApoio.setCliente(carregamentoitem.getPedidovendaproduto().getPedidovenda().getCliente().getNome());
		if (filialentrega == null)
			filialentrega = new Cliente();
		
		if(carregamentoitem.getPedidovendaproduto().getPraca() != null){
			carregamentoApoio.setPraca(carregamentoitem.getPedidovendaproduto().getPraca().getNome());
			carregamentoApoio.setCdpraca(carregamentoitem.getPedidovendaproduto().getPraca().getCdpraca());
			praca = carregamentoitem.getPedidovendaproduto().getPraca();
		}
		if(filialentrega.getPessoaendereco() == null) {
			carregamentoApoio.setCep(carregamentoitem.getPedidovendaproduto().getPessoaendereco().getCep().getValue());
		} else{
			carregamentoApoio.setCep( filialentrega.getPessoaendereco().getCep().getValue());
		}
		if(praca != null && praca.getListaRotapraca() != null && !praca.getListaRotapraca().isEmpty()){
			Rota rota = ((ArrayList<Rotapraca>)(praca.getListaRotapraca())).get(0).getRota();
			
			carregamentoApoio.setRota(rota.getNome());
			carregamentoApoio.setCdpraca(praca.getCdpraca());
			carregamentoApoio.setPraca(praca.getNome());
		}
		else{
			carregamentoApoio.setRota("Rota não encontrada");
			carregamentoApoio.setPraca("Praca não encontrada");
		}
		
		Tipooperacao tipooperacao = carregamentoitem.getPedidovendaproduto().getTipooperacao();
		if(tipooperacao != null){
			carregamentoApoio.setCdtipooperacao(tipooperacao.getCdtipooperacao());
			carregamentoApoio.setTipooperacao(tipooperacao.getNome());
		}
		
		carregamentoApoio.setCdpedidovenda(carregamentoitem.getPedidovendaproduto().getPedidovenda().getCdpedidovenda());
		carregamentoApoio.setPeso(peso);
		carregamentoApoio.setCubagem(cubagem);
		carregamentoApoio.setValor(valor);
		carregamentoApoio.setTotalItens(qtde.intValue());
		carregamentoApoio.setFilialEntrega(carregamentoitem.getPedidovendaproduto().getPedidovenda().getCliente());
		
		if (carregamentoitem.getPedidovendaproduto().getDepositotransbordo() != null)
			carregamentoApoio.setNomedepositotransbordo(carregamentoitem.getPedidovendaproduto().getDepositotransbordo().getNome());

		carregamentoDadosProduto.getListaCarregamentoApoio().add(carregamentoApoio);
	}
	
	/**
	 * Método que retorna a tabela de pre roterização atualizada
	 * 
	 * @param filtro
	 * @return
	 */
	public List<CarregamentoApoio> getAllCarregamentoForFlex(CarregamentoFiltro filtro){
		
		AgrupaRotas agrupaRotas = new AgrupaRotas(filtro);
		List<CarregamentoVO> prepararListaCarregamentoVO = agrupaRotas.prepararListaCarregamentoVO();
		
		DadosProduto dadosProduto = new DadosProduto();
		dadosProduto.setPesoTotal(agrupaRotas.getPesoTotal());
		dadosProduto.setCubagemTotal(agrupaRotas.getCubagemTotal());
		dadosProduto.setValorTotal(agrupaRotas.getValorTotal());
		dadosProduto.setTotalProdutos(agrupaRotas.getTotalProdutos());
		dadosProduto.setTotalEntrega(agrupaRotas.getTotalEntrega());
		
		NeoWeb.getRequestContext().getSession().setAttribute("dadosProduto", dadosProduto);
		
		
		List<CarregamentoApoio> novaLista = new ArrayList<CarregamentoApoio>();
		
		for (CarregamentoVO carregamentoVO : prepararListaCarregamentoVO) {
			if(carregamentoVO != null && carregamentoVO.getRota() != null && carregamentoVO.getNivel().equals(5) &&
					carregamentoVO.getCliente() != null && carregamentoVO.getCliente().getNome() != null &&
					carregamentoVO.getPraca() != null && carregamentoVO.getPraca().getNome() != null ) {
				
				CarregamentoApoio a = new CarregamentoApoio();
				a.setRota("Rota: "+carregamentoVO.getRota().getNome());
				a.setPraca("Praça: "+carregamentoVO.getPraca().getNome());
				a.setTipooperacao("TP: "+carregamentoVO.getTipooperacao().getNome());
				
				String depositoTransbordo;
				if (carregamentoVO.getNomedepositotransbordo() != null)
					depositoTransbordo = carregamentoVO.getNomedepositotransbordo() + " / ";
				else
					depositoTransbordo = "";
				
				if (!Tipooperacao.ENTREGA_CD_CLIENTE.equals(carregamentoVO.getTipooperacao()) && !Tipooperacao.ENTREGA_CD_CD_CLIENTE.equals(carregamentoVO.getTipooperacao()) && 
						carregamentoVO.getFilialEntrega() != null){
					
					a.setCliente("FL: " + depositoTransbordo + carregamentoVO.getFilialEntrega().getNome());
				}else if (Tipooperacao.MONSTRUARIO_LOJA.equals(carregamentoVO.getTipooperacao()))
					a.setCliente("FL: " + depositoTransbordo + carregamentoVO.getCliente().getNome());
				else
					a.setCliente("CL: " + depositoTransbordo + carregamentoVO.getCliente().getNome());
				
				a.setDescricao(carregamentoVO.getDescricao());
				a.setTotalItens(carregamentoVO.getTotalItens());
				a.setPedidovenda(carregamentoVO.getNumeropedido());
				a.setCdpedidovenda(carregamentoVO.getCdpedidovenda());
				a.setPeso(carregamentoVO.getPeso() == null ? 0.0 : carregamentoVO.getPeso());
				a.setCubagem(carregamentoVO.getCubagem());
				a.setValor(carregamentoVO.getValor());
				
				a.setCdpessoa(carregamentoVO.getCliente().getCdpessoa());
				a.setCdpraca(carregamentoVO.getPraca().getCdpraca());
				a.setCdtipooperacao(carregamentoVO.getTipooperacao().getCdtipooperacao());
				
				a.setCep(carregamentoVO.getCep());
				a.setNumeropedido(carregamentoVO.getNumeropedido());
				
				a.setCdformacaocarga(carregamentoVO.getCdformacaocarga());
				a.setNomedepositotransbordo(carregamentoVO.getNomedepositotransbordo());
				a.setFilialEntrega(carregamentoVO.getFilialEntrega());
				
				novaLista.add(a);
			}
		}
		
		return novaLista;
	}
	
	/**
	 * Retorna string com o contexto da aplicação
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public String getAppContext(){
		StringBuffer url = NeoWeb.getRequestContext().getServletRequest().getRequestURL();
		url = url.replace(url.indexOf("/", url.indexOf(NeoWeb.getRequestContext().getServletRequest().getContextPath())+1),url.length(),"");
		return url.toString();
	}
	
	/**
	 * Método que verifica se o usuario tem permissão de trocar local na tela carregamento flex
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public Boolean usuarioHasAuthorizationCarregamentoFlex(){
		return WmsUtil.isUserHasAction("LOCAL_RETIRADA_CARREGAMENTO");
	}

	/**
	 * Gera os mapas de separação, endereçamentos e ordens de reabastecimento de picking, para um determinado carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	public void gerarSeparacao(final Carregamento carregamento) {
		TransactionTemplate transactionTemplate = Neo.getObject(TransactionTemplate.class);
		
		transactionTemplate.execute(new TransactionCallback(){

			public Object doInTransaction(TransactionStatus status) {
				GerarSeparacao gerarSeparacao = new GerarSeparacao(carregamento);
				gerarSeparacao.start();
			
				enderecarCarregamento(carregamento);
				//28/08/2009 Foi discutido e definido que não é seguro nem necessário fazer esta duplicação
				//ordemservicoprodutoService.duplicaEnderecamentoOspConferencia(carregamento);
				return null;
			}
			
		});
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param carregamento
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean existeItemFaturadoEmOutraFilial(Carregamento carregamento) {
		return carregamentoDAO.existeItemFaturadoEmOutraFilial(carregamento);
	}

	/**
	 * Método com referência no DAO
	 * 
	 * @param expedicao
	 * @param carregamento
	 * @author Tomás Rabelo
	 */
	public void atualizarExpedicao(Expedicao expedicao,	Carregamento carregamento) {
		if (carregamento.getPaletesdisponiveis() == null)
			carregamento.setPaletesdisponiveis(0);
		
		carregamentoDAO.atualizarExpedicao(expedicao, carregamento);
	}

	/**
	 * Busca os carregamentos associados a uma expedição.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @param carregamentostatus
	 * @return
	 */
	public List<Carregamento> findForLiberacao(EmitirliberacaoveiculoFiltro filtro, Carregamentostatus carregamentostatus) {
		return carregamentoDAO.findForLiberacao(filtro, carregamentostatus);
	}

	/**
	 * Localiza todos os carregamentos de uma determinada expedição.
	 * 
	 * @author Giovane Freitas
	 * @param expedicao
	 * @return
	 */
	public List<Carregamento> findByExpedicao(Expedicao expedicao) {
		return carregamentoDAO.findByExpedicao(expedicao);
	}

	/**
	 * Busca todos os carregamentos que pertencem a mesma onda de separação.
	 * 
	 * @author Giovane Freitas
	 * @param cdsOS
	 * @return
	 */
	public List<Carregamento> findByOnda(String cdsOS) {
		return carregamentoDAO.findByOnda(cdsOS);
	}
	
	public List<Carregamento> loadCarregamentos() {
		return carregamentoDAO.loadCarregamentos();
	}
	
	public List<Carregamento> findByDeposito(Integer cddeposito){
		return carregamentoDAO.findByDeposito(cddeposito);
	}
	

	/**
	 * Busca os produtos que estão em carregamentos montados e que não possui
	 * estoque disponível para gerar a separação.
	 * 
	 * @author Giovane Freitas
	 * @param whereIn
	 *            Lista de IDs de Box, para os quais deverão ser listados os
	 *            carregamentos montados.
	 * @return 
	 */
	public List<ProdutoSemEstoqueVO> findProdutosSemEstoque(String whereIn) {
		return carregamentoDAO.findProdutosSemEstoque(whereIn);
	}
	
	/**
	 * Verifica se na lista existe algum produto com estoque negativo e substitui esse valor por 0
	 * @author Filipe Santos
	 * @return Lista de Produtos sem Estoque 
	 * @since 22/09/2011 
	 */
	public List<ProdutoSemEstoqueVO> zeraEstoqueNegativo(List<ProdutoSemEstoqueVO> produtosSemEstoque){
		for (ProdutoSemEstoqueVO produtoSemEstoqueVO : produtosSemEstoque) {
			if (produtoSemEstoqueVO.getEstoque() < 0)
				produtoSemEstoqueVO.setEstoque(0);
		}
		return produtosSemEstoque;
	}
	
	/**
	 * 
	 * @param cdCarregamento
	 */
	public void callAtualizarCarregamento(String cdCarregamento) {
		Integer cdcarregamento = null;
		
		try{
			cdcarregamento = Integer.parseInt(cdCarregamento);
		}catch (Exception e) {
			throw new WmsException("Parâmetros Inválidos");
		}
		
		carregamentoDAO.callAtualizarCarregamento(cdcarregamento);
	}

	/**
	 * Método que chama o webservice para validar os pedidos vinculados a uma determinada carga.
	 * 
	 * @param carregamento
	 * @param empresa
	 */
	public String callPreValidacaoCarga(Carregamento carregamento, Empresa empresa) {

		Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call prc_pre_validacao_carga(?,?,?) }");
	        
	        if(carregamento!=null && carregamento.getCdcarregamento()!=null){
	        	cs.setFloat(1,carregamento.getCdcarregamento());
	        }else{
	        	cs.setNull(1, Types.FLOAT);
	        }	      
	        
	        if(empresa!=null && empresa.getCdempresa()!=null){
	        	cs.setFloat(2, empresa.getCdempresa());
	        }else{
	        	cs.setNull(2, Types.FLOAT);
	        }
	        
	        cs.registerOutParameter(3,Types.VARCHAR);
	        
	        cs.execute();
	        
	        String resposta = cs.getString(3);	      
	        
	        connection.commit();
	        
	        return resposta==null?"O carregamento "+carregamento.getCdcarregamento()+" não pode ser validado como o esperado.":resposta;
	        
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
	 * @param whereIn
	 * @return
	 */
	public boolean validarCarregamentoCancelado(String whereIn){
		
		List<Carregamento> listaCarregamento = carregamentoDAO.validarCarregamentoCancelado(whereIn);
		
		if(listaCarregamento!=null && !listaCarregamento.isEmpty()){
			return false;
		}else{
			return true;
		}
		
	}

	/**
	 * 
	 * @param expedicao
	 * @return
	 */
	public boolean validaExpedicaoByStatusCarga(Expedicao expedicao){
		
		List<Carregamento> listaCarregamentoByStatus = carregamentoDAO.findCarregamentoStatusByExepdicao(expedicao);
		List<Carregamento> listaCarregamentoByOrdemServico = carregamentoDAO.findCarregamentoOSByExpedicao(expedicao);
		
		if(listaCarregamentoByStatus!=null && !listaCarregamentoByStatus.isEmpty()){
			return true;
		}else if(listaCarregamentoByOrdemServico!=null && !listaCarregamentoByOrdemServico.isEmpty()){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * 
	 * @param bean
	 * @param whereIn
	 */
	public void excluirDadosReferenciados(final Carregamento bean, final String whereIn){
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status){
				carregamentoitemService.deleteByCarregamento(bean,whereIn);
				carregamentohistoricoService.deleteByCarregamento(bean,whereIn);
				return null;
			}
		});
	}

	/**
	 * 
	 * @param whereIn 
	 * @param carregamento 
	 * @return
	 */
	public boolean validarStatusCargaExclusao(Carregamento carregamento, String whereIn) {
		
		List<Carregamento> listaCarga = carregamentoDAO.findStatusForExclusao(carregamento,whereIn);
		
		if(listaCarga!=null && !listaCarga.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
}
