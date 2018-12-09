package br.com.linkcom.wms.geral.service;

import java.awt.Image;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.ItemOrdemServico;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Notafiscalentradaproduto;
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
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.OrdemservicoprodutoVO;
import br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.ConferenciaCegaPapelFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitirDivergenciaRecebimentoFiltro;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.linkcom.wms.util.recebimento.DivergenciaRecebimentoVO;

public class OrdemservicoprodutoService extends GenericService<Ordemservicoproduto> {

	private NeoImageResolver neoImageResolver;

	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {
		this.neoImageResolver = neoImageResolver;
	}
	
	private OrdemservicoprodutoDAO ordemservicoprodutoDAO;
	private NotafiscalentradaService notafiscalentradaService;
	private RecebimentoService recebimentoService;
	private OrdemservicoService ordemservicoService;
	private ProdutoembalagemService produtoembalagemService;
	private OrdemservicousuarioService ordemservicousuarioService;
	private OrdemprodutohistoricoService ordemprodutohistoricoService;
	private OrdemprodutoligacaoService ordemprodutoligacaoService;
	private NotafiscalentradaprodutoService notafiscalentradaprodutoService;
	private ProdutotipopaleteService produtotipopaleteService;
	
	public void setOrdemservicoprodutoDAO(OrdemservicoprodutoDAO ordemservicoprodutoDAO) {
		this.ordemservicoprodutoDAO = ordemservicoprodutoDAO;
	}
	
	public void setNotafiscalentradaService(NotafiscalentradaService notafiscalentradaService) {
		this.notafiscalentradaService = notafiscalentradaService;
	}
	
	public void setRecebimentoService(RecebimentoService recebimentoService) {
		this.recebimentoService = recebimentoService;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setProdutoembalagemService(ProdutoembalagemService produtoembalagemService) {
		this.produtoembalagemService = produtoembalagemService;
	}
	
	public void setOrdemservicousuarioService(OrdemservicousuarioService ordemservicousuarioService) {
		this.ordemservicousuarioService = ordemservicousuarioService;
	}

	public void setOrdemprodutohistoricoService(OrdemprodutohistoricoService ordemprodutohistoricoService) {
		this.ordemprodutohistoricoService = ordemprodutohistoricoService;
	}
	
	public void setOrdemprodutoligacaoService(OrdemprodutoligacaoService ordemprodutoligacaoService) {
		this.ordemprodutoligacaoService = ordemprodutoligacaoService;
	}

	public void setNotafiscalentradaprodutoService(NotafiscalentradaprodutoService notafiscalentradaprodutoService) {
		this.notafiscalentradaprodutoService = notafiscalentradaprodutoService;
	}
	
	public void setProdutotipopaleteService(ProdutotipopaleteService produtotipopaleteService) {
		this.produtotipopaleteService = produtotipopaleteService;
	}
	
	/**
	 * Agrupa os produtos das nfs, e acumula a quantidade esperada, em seguida salva elas.
	 * 
	 * @see br.com.linkcom.wms.geral.service.NotafiscalentradaService#getProdutos
	 * @author Pedro Gonçalves
	 * @author Leonardo Guimarães
	 * @param ordemservico
	 * @param lista
	 */
	public List<Ordemservicoproduto> makeListaOrdemServicoProduto(Ordemservico ordemservico, List<Notafiscalentrada> lista) {
		if(lista == null || ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		List<Ordemservicoproduto> listaOrdemServicoProduto = new ArrayList<Ordemservicoproduto>();
		if(lista != null){
			Ordemservicoproduto ordemservicoproduto = null;
			int idx = -1;
			for (Notafiscalentrada notafiscalentrada : lista) {

				Notafiscalentrada nfEntradaProdutos = notafiscalentradaService.getProdutos(notafiscalentrada);
				Set<Notafiscalentradaproduto> listaNotafiscalentradaproduto = nfEntradaProdutos.getListaNotafiscalentradaproduto();
				for (Notafiscalentradaproduto notafiscalentradaproduto : listaNotafiscalentradaproduto) {
					
					//se o produto possui volumes vou criar uma lista de volumes do produto, 
					//senão a lista terá apenas o produto original
					List<Produto> listaProdutos = ProdutoService.getInstance().findVolumes(notafiscalentradaproduto.getProduto());
					if (listaProdutos == null || listaProdutos.size() <= 1){
						listaProdutos = new ArrayList<Produto>();
						listaProdutos.add(notafiscalentradaproduto.getProduto());
					}
					
					//Criando uma Ordemservicoproduto para cada volume
					for (Produto produto : listaProdutos){
						idx = Util.collections.indexOf(listaOrdemServicoProduto,"produto", produto);
						if(idx >= 0){
							ordemservicoproduto = listaOrdemServicoProduto.get(idx);
							ordemservicoproduto.setQtdeesperada(ordemservicoproduto.getQtdeesperada() + notafiscalentradaproduto.getQtde());
							
						} else {
							ordemservicoproduto = new Ordemservicoproduto();
							ordemservicoproduto.setProduto(produto);
							ordemservicoproduto.setQtdeesperada(notafiscalentradaproduto.getQtde());
							listaOrdemServicoProduto.add(ordemservicoproduto);
						}
						ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
					}
				}
			}
			for (Ordemservicoproduto aux : listaOrdemServicoProduto) {
				saveOrUpdateNoUseTransaction(aux);
			}
		}
		return listaOrdemServicoProduto;
	}
	
	/**
	 * Seta como zero todos as quantidades da ordem de serviço especificada.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#resetarQuantidades(Ordemservico ordemservico)
	 * 
	 * @param ordemservico
	 * @author Pedro Gonçalves
	 */
	public void resetarQuantidades(Ordemservico ordemservico){
		ordemservicoprodutoDAO.resetarQuantidades(ordemservico);
	}
		
	/**
	 * Salva a lista de ordem de serviço produto para conferência. Caso alguma ordem de serviço
	 * produto cujo somatório da quantidade de avaria e quantidade recebida não seja a soma da
	 * quantidade esperada a ordem de serviço deverá ser marcada como concluído com divergencia.
	 * 
	 * Quando o recebimento estiver concluído, seja com divergências ou sem, o box é liberado.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#atualizaValoresListaOrdemServicoProduto
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#atualizarQuantidades
	 * @see br.com.linkcom.wms.geral.service.RecebimentoService.loadRecebimentoBox(Recebimento recebimento)
	 * @see br.com.linkcom.wms.geral.service.BoxService.updateStatusBox(Box box)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#salvarOrdemServicoUsuarioForConferencia(ConferenciaCegaPapelFiltro)
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#prepareQtdes(ConferenciaCegaPapelFiltro filtro)
	 * 
	 * @author Pedro Gonçalves
	 * @param coletor - Se a conferencia é feita ou não pelo coletor.
	 * @param filtro
	 * @return status
	 * @throws InsercaoInvalidaException 
	 */
	public Recebimentostatus salvarListaOrdemServicoProdutoForConferencia(ConferenciaCegaPapelFiltro filtro,boolean coletor) throws InsercaoInvalidaException {
		
		Recebimento recebimento = filtro.getRecebimento();
		Map<Integer, Boolean> conferenciasIguaisPorOrdem = ordemprodutohistoricoService.isDuasConferenciasRecebimentoIguais(recebimento);
		Boolean isDuasConferenciasRecebimentoIguais = Boolean.FALSE;
		
		Recebimento aux = RecebimentoService.getInstance().load(recebimento);
		if(aux != null && aux.getRecebimentostatus() != null && aux.getRecebimentostatus().getCdrecebimentostatus() != null)
			recebimento.setRecebimentostatus(aux.getRecebimentostatus());
		
		if(!filtro.getSkipSaveOSU())
			recebimento.setDeposito(WmsUtil.getDeposito());
		
		if(filtro == null || recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos");
		
		//valores que vem da tela.
		List<Ordemprodutohistorico> listaOSPHOriginal = ordemprodutohistoricoService.prepareQtdes(filtro);
		//recupera os valores da ordem da última ordem de serviço
		List<Ordemprodutohistorico> listaOSHPPenultimo = ordemprodutohistoricoService.findBy(recebimento,true);
		
		Integer inerror = 0;
		boolean divergencia = false;
		int indexOf = -1;
		Ordemservico ordemservico = ordemservicoService.loadLastConferencia(recebimento);
		
		for(Ordemprodutohistorico ordemprodutohistorico : listaOSPHOriginal){
			
			isDuasConferenciasRecebimentoIguais = conferenciasIguaisPorOrdem.get(ordemprodutohistorico.getCdordemprodutohistorico());
			
			Ordemservicoproduto ordemservicoproduto = ordemprodutohistorico.getOrdemservicoproduto();
			ordemservicoproduto.setListaOrdemprodutoLigacao(new ListSet<Ordemprodutoligacao>(Ordemprodutoligacao.class));
			
			if(ordemprodutohistorico.getQtdeavaria() == null) 
				ordemprodutohistorico.setQtdeavaria(0L);
			if(ordemprodutohistorico.getQtde() == null) 
				ordemprodutohistorico.setQtde(0L);
			if(ordemprodutohistorico.getQtdefracionada() == null) 
				ordemprodutohistorico.setQtdefracionada(0L);
			if(ordemprodutohistorico.getQtdefalta() == null)
				ordemprodutohistorico.setQtdefalta(0L);
			
			//Não utilizar != para comparar, usar o !equals já que declaramos como "Long" e não "long"
			if(!ordemservicoproduto.getQtdeesperada().equals((ordemprodutohistorico.getQtdefracionada() + ordemprodutohistorico.getQtde() + ordemprodutohistorico.getQtdeavaria()))){
				inerror++;
				if(listaOSHPPenultimo != null) {
					
					//Carrega a ordem de serviço
					indexOf = Util.collections.indexOf(listaOSHPPenultimo, "ordemservicoproduto.produto.cdproduto", ordemservicoproduto.getProduto().getCdproduto());
					Ordemprodutohistorico ordemProdutoHistoricoOriginal = listaOSHPPenultimo.get(indexOf);
					if(ordemProdutoHistoricoOriginal.getQtdeavaria() == null) ordemProdutoHistoricoOriginal.setQtdeavaria(-1L);
					if(ordemProdutoHistoricoOriginal.getQtde() == null) ordemProdutoHistoricoOriginal.setQtde(-1L);
					if(ordemProdutoHistoricoOriginal.getQtdefracionada() == null) ordemProdutoHistoricoOriginal.setQtdefracionada(-1L);
					
					
					if(ordemProdutoHistoricoOriginal.getQtdeavaria().equals(ordemprodutohistorico.getQtdeavaria()) && 
						ordemProdutoHistoricoOriginal.getQtde().equals(ordemprodutohistorico.getQtde()) && 
						ordemProdutoHistoricoOriginal.getQtdefracionada().equals(ordemprodutohistorico.getQtdefracionada())) {
						
						ordemservicoproduto.setIgnore(true);
						inerror--;
						divergencia = true;
						ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
						
						if(coletor && !isDuasConferenciasRecebimentoIguais)// Caso seja do coletor
							makeLigacao(ordemservicoproduto);
					}
					else{
						if(filtro.getGeraReconferencia() != null && !filtro.getGeraReconferencia()){
							ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
						}
						
						divergencia = true;
						
						if(!isDuasConferenciasRecebimentoIguais)
							makeLigacao(ordemservicoproduto);
					}
					
				}
				else{
					if(filtro.getGeraReconferencia() != null && !filtro.getGeraReconferencia()){
						ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
					}

					divergencia = true;
					
					if(!isDuasConferenciasRecebimentoIguais)
						makeLigacao(ordemservicoproduto);
					
				}

				//Atribuindo a QtdeFaltante...
				ordemprodutohistorico.setQtdefalta(ordemservicoproduto.getQtdeesperada() - (ordemprodutohistorico.getQtdefracionada() + ordemprodutohistorico.getQtde() + ordemprodutohistorico.getQtdeavaria()));
				
			}else
				ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
			
			if(ordemservicoproduto.getOrdemprodutostatus() == null)
				ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
			
			if (ordemservicoproduto.getTipopalete() == null && ordemservicoproduto.getProduto() != null){
				Deposito deposito = coletor ? recebimento.getDeposito() : WmsUtil.getDeposito();
				
				Produtotipopalete produtotipopalete;
				
				if (ordemservicoproduto.getProduto().getProdutoprincipal() != null && ordemservicoproduto.getProduto().getProdutoprincipal().getCdproduto() != null){
					Dadologistico dadologistico = DadologisticoService.getInstance().findByProduto(ordemservicoproduto.getProduto().getProdutoprincipal(), deposito);
					if (dadologistico != null && dadologistico.getNormavolume())
						produtotipopalete = produtotipopaleteService.findPaletePadrao(ordemservicoproduto.getProduto(), deposito);
					else
						produtotipopalete = produtotipopaleteService.findPaletePadrao(ordemservicoproduto.getProduto().getProdutoprincipal(), deposito);
				}else				
					produtotipopalete = produtotipopaleteService.findPaletePadrao(ordemservicoproduto.getProduto(), deposito);
					
				if (produtotipopalete != null)
					ordemservicoproduto.setTipopalete(produtotipopalete.getTipopalete());
			}
			
			saveOrUpdateNoUseTransaction(ordemservicoproduto);
			ordemprodutohistorico.setOrdemservico(ordemservico);
			ordemprodutohistorico.setOrdemservicoproduto(ordemservicoproduto);
			ordemprodutohistoricoService.saveOrUpdateNoUseTransaction(ordemprodutohistorico);
		}
		Ordemservico newOrdemservico = null;
		if(inerror > 0 && (filtro.getGeraReconferencia() == null || filtro.getGeraReconferencia()) && !isDuasConferenciasRecebimentoIguais){
			newOrdemservico = generateNewOrdem(recebimento, ordemservico);
		} else {
			if(divergencia){// se foi divergente
				if(coletor && !isDuasConferenciasRecebimentoIguais){// pelo coletor
					newOrdemservico = generateNewOrdem(recebimento,ordemservico);
				}else {// pela tela do wms

					//se o recebimento já está em endereçamento não devo mexer no status dele
					if (!Recebimentostatus.EM_ENDERECAMENTO.equals(recebimento.getRecebimentostatus()) && !Recebimentostatus.ENDERECADO.equals(recebimento.getRecebimentostatus())){
						recebimento.setRecebimentostatus(Recebimentostatus.CONCLUIDO_COM_DIVERGENCIAS);
						if (filtro.getUsuariofinalizacao() != null && filtro.getUsuariofinalizacao().getCdpessoa() != null)
							recebimento.setUsuariofinalizacao(filtro.getUsuariofinalizacao());
						else
							recebimento.setUsuariofinalizacao(WmsUtil.getUsuarioLogado());
						recebimento.setDtfinalizacao(new Timestamp(System.currentTimeMillis()));
						recebimentoService.gravaStatusRecebimento(recebimento);
					}
					
					ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
				}
			} else{
				//se o recebimento já está em endereçamento não devo mexer no status dele
				if (!Recebimentostatus.EM_ENDERECAMENTO.equals(recebimento.getRecebimentostatus()) && !Recebimentostatus.ENDERECADO.equals(recebimento.getRecebimentostatus())){
					recebimento.setRecebimentostatus(Recebimentostatus.CONCLUIDO);
					if (filtro.getUsuariofinalizacao() != null && filtro.getUsuariofinalizacao().getCdpessoa() != null)
						recebimento.setUsuariofinalizacao(filtro.getUsuariofinalizacao());
					else
						recebimento.setUsuariofinalizacao(WmsUtil.getUsuarioLogado());
					recebimento.setDtfinalizacao(new Timestamp(System.currentTimeMillis()));
					recebimentoService.gravaStatusRecebimento(recebimento);
				}

				ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
			}
		}
		
		if(newOrdemservico != null)
			ordemprodutoligacaoService.salvarOrdemProdutoLigacaoByHistorico(newOrdemservico, listaOSPHOriginal);
		
		ordemservicoService.updateStatusordemservico(ordemservico);
		
		// Salva a ordem de servico em ordem servico usuario
		if(!coletor){
			OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
			
			ordemservicoUsuario.setOrdemservico(ordemservico);
			ordemservicoUsuario.setUsuario(filtro.getPessoaPapel());
			ordemservicoUsuario.setDtinicio(filtro.getDthrinicio());
			ordemservicoUsuario.setDtfim(filtro.getDthrfim());
			
			ordemservicousuarioService.saveOrUpdateNoUseTransaction(ordemservicoUsuario);
		}
				
		Recebimentostatus status;
		
		//se o recebimento já está em endereçamento devo verificar se a OS foi concluída com sucesso
		if (Recebimentostatus.EM_ENDERECAMENTO.equals(recebimento.getRecebimentostatus()) || Recebimentostatus.ENDERECADO.equals(recebimento.getRecebimentostatus())){
			if (Ordemstatus.FINALIZADO_SUCESSO.equals(ordemservico.getOrdemstatus()))
				status = Recebimentostatus.CONCLUIDO;
			else
				status = Recebimentostatus.CONCLUIDO_COM_DIVERGENCIAS;
		}else
			status = recebimento.getRecebimentostatus();
		
		if (Recebimentostatus.CONCLUIDO.equals(status) || Recebimentostatus.CONCLUIDO_COM_DIVERGENCIAS.equals(status)){
			Deposito deposito = coletor ? recebimento.getDeposito() : WmsUtil.getDeposito();

			//Atualizando o estoque na área de Box
			Map<Produto, Ordemprodutohistorico> qtdeConferida = ordemservicoService.findQtdeConferida(recebimento);
			for (Entry<Produto, Ordemprodutohistorico> entry : qtdeConferida.entrySet()){
				Ordemprodutohistorico oph = entry.getValue();
				long qtde = oph.getQtde() != null ? oph.getQtde() : 0L;
				qtde += oph.getQtdeavaria() != null ? oph.getQtdeavaria() : 0L;
				qtde += oph.getQtdefracionada() != null ? oph.getQtdefracionada() : 0L;
				
				if (qtde > 0L)
					EnderecoprodutoService.getInstance().alimentarEstoqueBox(deposito, entry.getKey(), qtde);
			}
			
			//se o recebimento foi finalizado deve-se executar o processo de liberar pedidos de transbordo (UC122)
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.OPERACAO_CD_CD_DUPLICA_PEDIDO, deposito)){
//				PedidovendaService.getInstance().liberarPedidosTransbordo(recebimento);
			}
		}
		
		//Se já foi endereçado atualiza o estoque
		//Deve ser a última coisa feita por causa do estoque que é alimentado no Box e que será removido
		//na hora de confirmar o estoque do endereçamento.
		recebimentoService.atualizarEstoqueRecebimento(recebimento);

		return status;
	}
	
	/**
	 * Gera a ordem de serviço do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @param ordemservico
	 * @return
	 */
	private Ordemservico generateNewOrdem(Recebimento recebimento,Ordemservico ordemservico) {
		Ordemservico newOrdemservico;
		newOrdemservico = salvarOrdemServico(recebimento,Recebimentostatus.EM_ANDAMENTO,Ordemstatus.EM_ABERTO);
		//Definir o status aqui prejudica o método onde o recebimento é usado, pois o recebimento pode estar EM_ENDERECAMENTO
		//recebimento.setRecebimentostatus(Recebimentostatus.EM_ANDAMENTO);
		ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
		return newOrdemservico;
	}
	
	/**
	 * Faz a ligacao de uma ordem de servico com uma 
	 * ordem de servico do produto
	 * 
	 * @author Leonardo Guimarães
	 *  
	 * @param ordemservicoproduto
	 */
	private void makeLigacao(Ordemservicoproduto ordemservicoproduto) {
		Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
		ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
		ordemservicoproduto.getListaOrdemprodutoLigacao().add(ordemprodutoligacao);		
	}

	/**
	 * Gera uma nova ordem de serviço com os produtos que estao em conflito.
	 * 
	 * @param recebimento
	 * @param listaOrdemServicoProduto
	 * @return true - caso a ordem seja maior que 1
	 *         false - caso a ordem seja 1 ou nula.
	 */
	private Ordemservico salvarOrdemServico(Recebimento recebimento,Recebimentostatus recebimentostatus,Ordemstatus ordemstatus){
			Ordemservico loadLast = ordemservicoService.loadLastConferencia(recebimento);
			return ordemservicoService.salvarOrdemServico(recebimento,recebimento.getDeposito(),ordemstatus, loadLast.getOrdem() + 1);
	}
	
	/**
	 * Prepara todos os dados que serão usados na geração do
	 * relatório
	 * 
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#findForReport(Recebimento recebimento)
	 * @param filtro
	 * @return
	 */
	public IReport createOrdemservico(EmitirDivergenciaRecebimentoFiltro filtro) {
		Report report = new Report("RelatorioDivergenciaRecebimento");
		
		Recebimento recebimento = recebimentoService.findByRecebimento(new Recebimento(filtro.getRecebimento()));
		/*
		 * Busca as ordens de serviço produto de um recebimento com status 'CONCLUÍDO COM DIVERGÊNCIA'. As ordens de serviço produto que serão 
		 * trazidas também possuem o status CONCLUÍDO COM DIVERGÊNCIA
		 * 
		 */
		List<Ordemservicoproduto> listaOrdemservicoproduto = findForReport(filtro);
		List<DivergenciaRecebimentoVO> listaDivergencia = new ArrayList<DivergenciaRecebimentoVO>();
		
		DivergenciaRecebimentoVO divergenciaRecebimentoVO = new DivergenciaRecebimentoVO();

		Notafiscalentrada notafiscalentrada = recebimento.getListaRecebimentoNF().get(0).getNotafiscalentrada();
		divergenciaRecebimentoVO.setData(recebimento.getDtrecebimento());
		divergenciaRecebimentoVO.setPlaca(notafiscalentrada.getVeiculo());
		divergenciaRecebimentoVO.setTransportador(notafiscalentrada.getTransportador());
		
		String conferentes = ordemservicousuarioService.findByRecebimento(recebimento);
		divergenciaRecebimentoVO.setConferente(conferentes);
		
		int soma = 0;
		for (int i=0; i<listaOrdemservicoproduto.size(); i++) {
			Ordemservicoproduto ordemservicoproduto = listaOrdemservicoproduto.get(i);				
			if((ordemservicoproduto.getOrdemprodutostatus() != null) && (ordemservicoproduto.getOrdemprodutostatus().getCdordemprodutostatus() != null)) {
				
				Produto produto = ordemservicoproduto.getProduto();
				
				Ordemprodutohistorico ordemprodutohistorico = ordemprodutohistoricoService.findMaxByOrdemservicoproduto(ordemservicoproduto);
				
				ordemservicoproduto.setListaOrdemprodutohistorico(new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class));
				ordemservicoproduto.getListaOrdemprodutohistorico().add(ordemprodutohistorico);
				
				long qtdefracionada = ordemprodutohistorico.getQtdefracionada() != null ? ordemprodutohistorico.getQtdefracionada() : 0L;
				long qtdeavaria = ordemprodutohistorico.getQtdeavaria() != null ? ordemprodutohistorico.getQtdeavaria() : 0L;
				long qtde = ordemprodutohistorico.getQtde() != null ? ordemprodutohistorico.getQtde() : 0L;
				
				long qtdeConferida = (qtdefracionada + qtdeavaria + qtde);
				int divergencia = (int)(qtdeConferida - ordemservicoproduto.getQtdeesperada());
				
				if (divergencia == 0)
					continue;
				
				produto.setTotalDirvegencias(divergencia);
				
				soma += divergencia;
				
				List<Produtoembalagem> listaProdutoEmbalagem = new ArrayList<Produtoembalagem>();
				Produtoembalagem embalagem = produtoembalagemService.findCompraByProduto(produto);
				if(embalagem != null){
					listaProdutoEmbalagem.add(embalagem);
					produto.setListaProdutoEmbalagem(listaProdutoEmbalagem);
				}
				
				/*
				 * Como o produto pode não ter uma embalagem foi dado este golpe
				 */
				if(produto.getListaProdutoEmbalagem() == null || produto.getListaProdutoEmbalagem().size() == 0){
					List<Produtoembalagem> list = new ArrayList<Produtoembalagem>();
					list.add(new Produtoembalagem());
					produto.setListaProdutoEmbalagem(list);
				}
				
				divergenciaRecebimentoVO.getListaOrdemServicoProduto().add(ordemservicoproduto);
			}
		} // fim for
		
		if(divergenciaRecebimentoVO.getListaOrdemServicoProduto().size() != 0)				
			divergenciaRecebimentoVO.setFornecedorNotafiscal(preparaNotasFiscais(recebimento, divergenciaRecebimentoVO.getListaOrdemServicoProduto()));
		
		divergenciaRecebimentoVO.setTotalDivergente(soma);
		divergenciaRecebimentoVO.setTotalProdutos(divergenciaRecebimentoVO.getListaOrdemServicoProduto().size());
		divergenciaRecebimentoVO.setRecebimento(recebimento);
		listaDivergencia.add(divergenciaRecebimentoVO);
		
		Image image = null;
		
		try {
			image = neoImageResolver.getImage(WmsUtil.getLogoForReport());
	
		} catch (IOException e) {
			throw new WmsException("Erro ao capturar o logotipo do relatório");
		
		}
		
		report.addParameter("LOGO", image);
		report.addSubReport("SUBDIVERGENCIA", new Report("SubRelatorioDivergenciaRecebimento"));		
		report.addSubReport("NOTAFISCALENTRADA", new Report("SubRelatorioNotaFiscalEntrada"));		
		report.setDataSource(listaDivergencia);

		return report;
	}
	
	/**
	 * 
	 * Método que recupera, ordena e valida as notas fiscais de um recebimento
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservicoproduto
	 * @return List<Notafiscalentrada>
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#ordenaNotasfiscaisByFornecedor(List) 
	 * @see br.com.linkcom.wms.geral.service.NotafiscalentradaprodutoService#findByListaNotafiscalentrada(List)
	 * 
	 */
	private TreeMap<String, String> preparaNotasFiscais(Recebimento recebimento, List<Ordemservicoproduto> listaOrdemservicoproduto) {
		List<Notafiscalentrada> listaNotafiscalentradaOrdenada = new ArrayList<Notafiscalentrada>();
		List<Notafiscalentrada> listaNotafiscalProdutoDivergente = new ArrayList<Notafiscalentrada>();

		List<Recebimentonotafiscal> listaRecebimentonotafiscal = recebimento.getListaRecebimentoNF();
		
		carregaNotasfiscais(listaNotafiscalentradaOrdenada, listaRecebimentonotafiscal);
		
		ordenaNotasfiscaisByFornecedor(listaNotafiscalentradaOrdenada);
		TreeMap<String, String> fornecedorFornotafiscal = new TreeMap<String, String>();
		
		List<Notafiscalentradaproduto> listaNotafiscalentradaproduto = notafiscalentradaprodutoService.findByListaNotafiscalentrada(listaNotafiscalentradaOrdenada);

		if((listaNotafiscalentradaproduto != null) && (listaNotafiscalentradaproduto.size() != 0)) {
			for(int i=0; i<listaNotafiscalentradaproduto.size(); i++) {
				Notafiscalentradaproduto notafiscalentradaproduto = (Notafiscalentradaproduto)listaNotafiscalentradaproduto.get(i);
				
				for(int j=0; j<listaOrdemservicoproduto.size(); j++) {
					Ordemservicoproduto ordemservicoproduto = (Ordemservicoproduto) listaOrdemservicoproduto.get(j);
					
					if(ordemservicoproduto.getProduto().equals(notafiscalentradaproduto.getProduto()))
						listaNotafiscalProdutoDivergente.add(notafiscalentradaproduto.getNotafiscalentrada());
				}
			}
		}
		
		String fornecedor = "";
		String numero = "";
		
		for(int k=0; k <listaNotafiscalProdutoDivergente.size(); k++) {
			Notafiscalentrada notafiscalentrada = (Notafiscalentrada) listaNotafiscalProdutoDivergente.get(k);
			
			if(k == 0) {
				fornecedor += notafiscalentrada.getFornecedor().getNome();
				numero += notafiscalentrada.getNumero() + ", ";
			
			} else {
				Notafiscalentrada notafiscalentradaAnterior = (Notafiscalentrada) listaNotafiscalProdutoDivergente.get(k-1);
				
				if(notafiscalentrada.getFornecedor().getNome().equals(notafiscalentradaAnterior.getFornecedor().getNome())) {
					if(!numero.contains(notafiscalentrada.getNumero())) {
						numero += notafiscalentrada.getNumero() + ", ";
					}
				
				} else {
					numero = ordenaNotaFiscal(numero).substring(0, numero.length() - 2);
					fornecedorFornotafiscal.put(fornecedor, numero);
					
					fornecedor = "";
					numero = "";
					
					fornecedor += notafiscalentrada.getFornecedor().getNome();
					numero += notafiscalentrada.getNumero() + ", ";
				}
			}			
		}
		
		if (!numero.isEmpty())
			numero = ordenaNotaFiscal(numero).substring(0, numero.length() - 2);
		
		fornecedorFornotafiscal.put(fornecedor, numero);
		
		return fornecedorFornotafiscal;
	}

	/**
	 * 
	 * Carrega  as notas fiscais do recebimento
	 *  
	 * @param listaNotafiscalentradaOrdenada
	 * @param listaRecebimentonotafiscal
	 * 
	 * @author Arantes
	 * 
	 */
	private void carregaNotasfiscais(List<Notafiscalentrada> listaNotafiscalentradaOrdenada, List<Recebimentonotafiscal> listaRecebimentonotafiscal) {
		if(listaRecebimentonotafiscal.size() > 0) {
			for (int j = 0; j<listaRecebimentonotafiscal.size(); j++) {
				Recebimentonotafiscal recebimentonotafiscal = (Recebimentonotafiscal) listaRecebimentonotafiscal.get(j);
				listaNotafiscalentradaOrdenada.add(recebimentonotafiscal.getNotafiscalentrada());						
			}					
		}
	}
	
	/**
	 * 
	 * Ordena os números das notas fiscais. O algoritmo usado é o bubbleSort.
	 * 
	 * @param notasfiscais
	 * @return String
	 * 
	 * @author Arantes
	 * 
	 */
	private String ordenaNotaFiscal(String notasfiscais) {
		String[] notafiscalArray = StringUtils.split(notasfiscais, ", ");
		
		for(int i=notafiscalArray.length - 1; i>0; i--) {
			for(int j=0; j<i; j++) {
				if(notafiscalArray[j].compareTo(notafiscalArray[j+1]) > 0) {
					String temp = notafiscalArray[j];
					notafiscalArray[j] = notafiscalArray[j+1];
					notafiscalArray[j+1] = temp;
				}
			}
		}
		
		String notafiscal = "";
		for(int i=0; i<notafiscalArray.length; i++) {
			notafiscal += notafiscalArray[i] + ", ";
		}
		
		return notafiscal;
	}

	/**
	 * 
	 * Método que ordena a lista de notas fiscais pelo nome do fornecedor
	 * 
	 * @param listaNotafiscalentradaOrdenada
	 * 
	 * @author Arantes
	 * 
	 */
	private void ordenaNotasfiscaisByFornecedor(List<Notafiscalentrada> listaNotafiscalentradaOrdenada) {
		Collections.sort(listaNotafiscalentradaOrdenada, new Comparator<Notafiscalentrada>() {
			public int compare(Notafiscalentrada o1, Notafiscalentrada o2) {
				if (o1 == o2)
					return 0;
				
				if (o1 == null || o1.getFornecedor() == null || o1.getFornecedor().getNome() == null)
					return -1;
				
				if (o2 == null || o2.getFornecedor() == null || o2.getFornecedor().getNome() == null)
					return 1;
				
				
				
				return o1.getFornecedor().getNome().compareTo(o2.getFornecedor().getNome());
			}
		});
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#findForReport(Recebimento recebimento)
	 * @param recebimento 
	 *  
	 * @return
	 */
	public List<Ordemservicoproduto> findForReport(EmitirDivergenciaRecebimentoFiltro filtro){
		return ordemservicoprodutoDAO.findForReport(filtro);
		
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#atualizarStatus(Ordemservicoproduto ordemservicoproduto)
	 * 
	 * @param ordemservicoproduto
	 */
	public void atualizarStatus(Ordemservicoproduto ordemservicoproduto){
		ordemservicoprodutoDAO.atualizarStatus(ordemservicoproduto);
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#findForVisualizarDetalhes(Ordemservico)
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return List<Ordemservicoproduto>
	 * 
	 */
	public List<Ordemservicoproduto> findForVisualizarDetalhes(Ordemservico ordemservico, boolean conferenciaBox) {
		return ordemservicoprodutoDAO.findForVisualizarDetalhes(ordemservico, conferenciaBox);		
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#findForVisualizarDetalhes(Ordemservico)
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return List<Ordemservicoproduto>
	 * 
	 */
	public List<Ordemservicoproduto> findForVisualizarDetalhesWithCaixaMestre(Ordemservico ordemservico, boolean conferenciaBox) {
		List<Ordemservicoproduto> listaOsp = ordemservicoprodutoDAO.findForVisualizarDetalhes(ordemservico, conferenciaBox);
		List<OrdemservicoprodutoVO> litaOspVO = ordemservicoprodutoDAO.findQtdesForVisualizarDetalhes(ordemservico.getExpedicao(),ordemservico.getCarregamento());
		for (OrdemservicoprodutoVO ordemservicoprodutoVO : litaOspVO) {
			for (Ordemservicoproduto ordemservicoproduto : listaOsp) {
				if(ordemservicoproduto.getProdutoembalagem()!=null && ordemservicoproduto.getProdutoembalagem().getCdprodutoembalagem()!=null){
					if(ordemservicoprodutoVO.getCdprodutoembalagem().equals(ordemservicoproduto.getProdutoembalagem().getCdprodutoembalagem())){
						ordemservicoproduto.setQntdeTotal(ordemservicoprodutoVO.getQtde());
					}	
				}
			}
		}
		return listaOsp;		
	}

	/**
	 * 
	 * Método de referência ao DAO.
	 * Método que atualiza o status da ordem de serviço produto
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#updateStatusordemservicoproduto(Ordemservicoproduto)
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservicoproduto
	 * 
	 */
	public void updateStatusordemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		ordemservicoprodutoDAO.updateStatusordemservicoproduto(ordemservicoproduto);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#findByOrdemservico(Ordemservico ordemservico)
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findByOrdemservico(Ordemservico ordemservico) {
		return ordemservicoprodutoDAO.findByOrdemservico(ordemservico);
	}
	
	/**
	 * 
	 * Método que atualiza o status da ordem de serviço produto de acordo com a qtde esperada e a qtde confirmada.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#updateStatusordemservicoproduto(Ordemservicoproduto)
	 * 
	 * @author Arantes
	 * 
	 * @param listaOrdemservicoproduto
	 * 
	 */
	public void atualizarStatusordemservicoproduto(Ordemservico ordemservico) {
		List<ItemOrdemServico> itensServico = ordemservicoprodutoDAO.findForCorte(ordemservico);
		
		for (ItemOrdemServico item : itensServico) {
			Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto(item.getCdordemservicoproduto());
			ordemservicoproduto.setOrdemprodutostatus(item.getQtdeesperada().equals(item.getQtdeconfirmada()) ? Ordemprodutostatus.CONCLUIDO_OK 
					                                                                           : Ordemprodutostatus.CONCLUIDO_DIVERGENTE);				

			ordemservicoprodutoDAO.updateStatusordemservicoproduto(ordemservicoproduto);
		}
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Atualiza o status de todas as ordemservicoproduto da ordem de serviço em questão.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#atualizarStatus(Ordemservico, Ordemprodutostatus)
	 * @param ordemservico
	 * @param ordemprodutostatus
	 * @author Pedro Gonçalves
	 */
	public void atualizarStatus(Ordemservico ordemservico, Ordemprodutostatus ordemprodutostatus){
		ordemservicoprodutoDAO.atualizarStatus(ordemservico, ordemprodutostatus);
	}
	
	/* singleton */
	private static OrdemservicoprodutoService instance;
	public static OrdemservicoprodutoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemservicoprodutoService.class);
		}
		return instance;
	}
	
	/**
	 * Busca os produtos das ordens do tipo e recebimento especificados
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#loadByOrdemAvaria(Recebimento recebimento,Ordemtipo ordemtipo)
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Ordemservicoproduto> loadByOrdemTipo(Recebimento recebimento,Ordemtipo ordemtipo, Usuario usuario) {
		return ordemservicoprodutoDAO.loadByOrdemTipo(recebimento,ordemtipo, usuario);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#findDadosOSPForTransferencia(Linhaseparacao linhaseparacao, Ordemservico ordemservico)
	 * 
	 * @param ordemservico
	 */
	public List<Ordemservicoproduto> findDadosOSPForTransferencia(Ordemservico ordemservico){
		return ordemservicoprodutoDAO.findDadosOSPForTransferencia(ordemservico);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * Método que atualiza o tipo palete da ordem servico produto
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoprodutoDAO#updateTipopaleteordemservicoproduto(Ordemservicoproduto)
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param ordemservicoproduto
	 * 
	 */
	public void updateTipopaleteordemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		ordemservicoprodutoDAO.updateTipopaleteordemservicoproduto(ordemservicoproduto);
	}
	
	/**
	 * excluir todos os Ordemservicoproduto através da ordemservico produto.
	 * @author Pedro gonçalves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		ordemservicoprodutoDAO.deleteAllBy(listaOSP);
	}
	
	/**
	 * Método com referência no DAO
	 * 
	 * @param ordemservicoproduto
	 * @param produtoEmbalagem
	 * @author Tomás Rabelo
	 */
	public void atualizaProdutoEmbalagem(Ordemservicoproduto ordemservicoproduto, Produtoembalagem produtoEmbalagem) {
		ordemservicoprodutoDAO.atualizaProdutoEmbalagem(ordemservicoproduto, produtoEmbalagem);
	}

	/**
	 * Busca as {@link Ordemservicoproduto} juntamente com o {@link Produto} associado.
	 * 
	 * @see OrdemservicoprodutoDAO#findByOrdemservicoWithProduto(Ordemservico)
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findByOrdemservicoWithProduto(
			Ordemservico ordemservico) {
		return ordemservicoprodutoDAO.findByOrdemservicoWithProduto(ordemservico);
	}
	
	/**
	 * Cria uma ordem de serviço de endereçamento para o coletor caso seja necessário.
	 * 
	 * @deprecated Nunca foi usado, mas há uma grande chance de precisar. Se for utilizar deverá testar se está funcionando.
	 * @author Giovane Freitas
	 * @param recebimento
	 * @param enderecoAvariado
	 * @return
	 */
	@Deprecated
	public List<Ordemservicoproduto> criarOrdemParaColetor(Recebimento recebimento, Ordemtipo ordemtipo) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		
		if (ordemtipo == null || (!Ordemtipo.ENDERECAMENTO_AVARIADO.equals(ordemtipo) && !Ordemtipo.ENDERECAMENTO_FRACIONADO.equals(ordemtipo)))
			throw new WmsException("Este método cria O.S. apenas para endereçamentos fracionados e avariados.");
		
		Ordemservico ultimaConferencia = ordemservicoService.loadLastConferencia(recebimento, true);
		
		Ordemservico ordemservico = new Ordemservico();
		ordemservico.setRecebimento(recebimento);
		ordemservico.setOrdemtipo(ordemtipo);
		ordemservico.setDeposito(recebimento.getDeposito());
		ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
		ordemservico.setOrdem(1);
		
		for (Ordemprodutohistorico oph : ultimaConferencia.getListaOrdemProdutoHistorico()){
			Ordemservicoproduto osp = new Ordemservicoproduto();
			osp.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
			
			Ordemprodutoligacao opl = new Ordemprodutoligacao();
			opl.setOrdemservico(ordemservico);
			opl.setOrdemservicoproduto(osp);

			osp.setProduto(oph.getOrdemservicoproduto().getProduto());
			osp.setTipopalete(oph.getOrdemservicoproduto().getTipopalete());

			Ordemservicoprodutoendereco ospe = new Ordemservicoprodutoendereco();
			ospe.setOrdemservicoproduto(osp);

			Long qtdeAvaria = oph.getQtdeavaria();
			Long qtdeFracionada = oph.getQtdefracionada();

			//A conferência não foi finalizada, as ordens de enderamento serão criadas com a quantidade esperada
			if (Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(ultimaConferencia.getOrdemstatus()) 
					|| Ordemstatus.EM_ABERTO.equals(ultimaConferencia.getOrdemstatus())
					|| Ordemstatus.EM_EXECUCAO.equals(ultimaConferencia.getOrdemstatus())){
				
				qtdeAvaria = osp.getQtdeesperada();
				qtdeFracionada = osp.getQtdeesperada();
			}
			
			if (Ordemtipo.ENDERECAMENTO_AVARIADO.equals(ordemtipo) && qtdeAvaria != null && qtdeAvaria > 0L){
				//Deve criar endereçamento de avaria e existe qtde de avaria coletada
				osp.setQtdeesperada(qtdeAvaria);
				ospe.setQtde(qtdeAvaria);
				ospe.setEnderecodestino(EnderecoService.getInstance().findEnderecoAvaria(recebimento.getDeposito()));
				
			}else if (Ordemtipo.ENDERECAMENTO_FRACIONADO.equals(ordemtipo) && qtdeFracionada != null && qtdeFracionada > 0L){
				//Deve criar endereçamento fracionado e existe qtde fracionada coletada
				
				osp.setQtdeesperada(qtdeFracionada);
				
				//verificar se o produto possui endereço de picking
				Endereco picking = EnderecoService.getInstance().findPicking(osp.getProduto(), recebimento.getDeposito());
				if (picking != null && picking.getCdendereco() != null)
					ospe.setEnderecodestino(picking);
				else//se não tiver endereço de picking, então alocar no endereço virtual.
					ospe.setEnderecodestino(EnderecoService.getInstance().findEnderecoVirtual(recebimento.getDeposito()));
			}
		}
		
		return null;
	}

	/**
	 * Busca os objetos {@link Ordemservicoproduto} e seus {@link Ordemservicoprodutoendereco} associados para
	 * serem excluídos.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findForDelete(Ordemservico ordemservico) {
		return ordemservicoprodutoDAO.findForDelete(ordemservico);
	}
	

	/**
	 * Método com referência no DAO
	 * 
	 * @param ordemservicoproduto
	 * @return
	 * @author Tomás Rabelo
	 */
	public Ordemservicoproduto getQtdeEsperadaOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		return ordemservicoprodutoDAO.getQtdeEsperadaOrdemservicoproduto(ordemservicoproduto);
	}

	/**
	 * Verifica se todos os itens da ordem de serviço já foram concluídos.
	 * 
	 * @param ordemservico
	 * @return
	 */
	public boolean isTodosItensFinalizados(Ordemservico ordemservico) {
		return ordemservicoprodutoDAO.isTodosItensFinalizados(ordemservico);
	}
	
}