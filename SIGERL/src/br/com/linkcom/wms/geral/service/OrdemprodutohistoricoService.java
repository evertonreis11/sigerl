package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.CodigobarrasVO;
import br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.ConferenciaCegaPapelFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.linkcom.wms.util.recebimento.RecebimentoPapelReportVO;

public class OrdemprodutohistoricoService extends GenericService<Ordemprodutohistorico>{
	
	private OrdemservicoService ordemservicoService;
	private OrdemprodutohistoricoDAO ordemprodutohistoricoDAO;
	private OrdemprodutoligacaoService ordemprodutoligacaoService;
	private EtiquetaexpedicaoService etiquetaexpedicaoService;
	private OrdemservicoprodutoService ordemservicoprodutoService;

	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setOrdemprodutohistoricoDAO(OrdemprodutohistoricoDAO ordemprodutohistoricoDAO) {
		this.ordemprodutohistoricoDAO = ordemprodutohistoricoDAO;
	}
	
	public void setOrdemprodutoligacaoService(OrdemprodutoligacaoService ordemprodutoligacaoService) {
		this.ordemprodutoligacaoService = ordemprodutoligacaoService;
	}
	
	public void setEtiquetaexpedicaoService(EtiquetaexpedicaoService etiquetaexpedicaoService) {
		this.etiquetaexpedicaoService = etiquetaexpedicaoService;
	}
	
	public void setOrdemservicoprodutoService(OrdemservicoprodutoService ordemservicoprodutoService) {
		this.ordemservicoprodutoService = ordemservicoprodutoService;
	}


	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Arantes
	 * 
	 * @param ordemprodutohistorico
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#findMaxByOrdemservicoproduto(Ordemservicoproduto)
     * @return Ordemprodutohistorico
	 *
	 */
	public Ordemprodutohistorico findMaxByOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		return ordemprodutohistoricoDAO.findMaxByOrdemservicoproduto(ordemservicoproduto);
	}
	
	/**
	 * Encontra a ordem de servi�o a partir do recebimento e lista todos os seus
	 * produtos para o relat�rio.
	 * 
	 * Obs: Este m�todo foi criado pois o hybernate n�o estava
	 * buscando mais de um item da lista na query do m�todo {@link #findBy(Recebimento recebimento,Ordemservico ordemservico)}
	 * 
	 * Obs: Este m�todo teve o tipo de retorno alterado devido a lentid�o e consumo de mem�ria
	 * 
	 * @author Leonardo Guimar�es
	 * 		   Pedro Gon�alves - Migra��o para a nova vers�o do banco
	 * 		   Giovane Freitas - Resolvendo problema de desempenho
	 * 
	 * @param recebimento
	 * @param ordemservico
	 * @return
	 */
	public List<RecebimentoPapelReportVO> findForPapelReport(Recebimento recebimento, Ordemservico ordemservico){
		return ordemprodutohistoricoDAO.findForPapelReport(recebimento, ordemservico);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#atualizarQuantidades(Ordemprodutohistorico ordemprodutohistorico)
	 * 
	 * @param ordemprodutohistorico
	 */
	public void atualizarQuantidades(Ordemprodutohistorico ordemprodutohistorico) {
		ordemprodutohistoricoDAO.atualizarQuantidades(ordemprodutohistorico);
	}
	
	/**
	 * 
 	 * M�todo de refer�ncia ao DAO.
	 * 
	 * Encontra a ordem de servi�o a partir do recebimento e lista todos os seus
	 * produtos. 
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoDAO#

	 * 
	 * @param recebimento
	 * @param ordemservico
	 * @param ultimaOs
	 * @return
	 */
	public List<Ordemprodutohistorico> findForPopUp(Recebimento recebimento, Ordemservico ordemservico, Long ultimaOs) {
		return ordemprodutohistoricoDAO.findForPopUp(recebimento, ordemservico, ultimaOs);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * Encontra a ordem de servi�o a partir do recebimento e lista todos os seus
	 * produtos. Pega sempre a �ltima ordem de servi�o do recebimento.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#findByForRF(Recebimento recebimento)
	 *  
	 * @param recebimento
	 * @return
	 * @author Pedro Gon�alves
	 */
	public List<Ordemprodutohistorico> findByForRF(Recebimento recebimento) {
		return ordemprodutohistoricoDAO.findByForRF(recebimento);
	}
	
	/**
	 * M�todo desenvolvido para evitar a quebra de compatibilitade,
	 * seta automaticamente a �ltima ordem de servi�o.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ordemservicoprodutoDAO#findBy
	 * @see br.com.linkcom.neo.wms.geral.service.OrdemservicoprodutoService.findBy#loadLast
	 * 
	 * @param recebimento
	 * @return null caso n�o tenha nenhuma ordem de servi�o precedente.
	 * @author Pedro Gon�alves
	 */
	public List<Ordemprodutohistorico> findBy(Recebimento recebimento, Boolean reduce) {
		Ordemservico loadLast = ordemservicoService.loadLastConferencia(recebimento);
		if(reduce && loadLast != null) {
			loadLast = ordemservicoService.loadPenultimaConferencia(recebimento);
			if(loadLast == null)
				return null;
		}
		return findBy(recebimento,loadLast);
	}
	
	/**
	 * M�todo desenvolvido para evitar a quebra de compatibilitade,
	 * seta automaticamente a �ltima ordem de servi�o.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ordemservicoprodutoDAO#findBy
	 * @see br.com.linkcom.neo.wms.geral.service.OrdemservicoprodutoService.findBy#loadLast
	 * 
	 * @param recebimento
	 * @return
	 * @author Pedro Gon�alves
	 */
	public List<Ordemprodutohistorico> findBy(Recebimento recebimento) {
		Ordemservico loadLast = ordemservicoService.loadLastConferencia(recebimento);
		return findBy(recebimento,loadLast);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ordemservicoprodutoDAO#findBy
	 * 
	 * @param recebimento
	 * @return
	 * @author Pedro Gon�alves
	 */
	public List<Ordemprodutohistorico> findBy(Recebimento recebimento, Ordemservico ordemservico) {
		return ordemprodutohistoricoDAO.findBy(recebimento, ordemservico);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * Carrega o hist�rico a partir de uma ordem de confer�ncia.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#findByForRF(Ordemservico ordemservico)
	 * @param recebimento
	 * @return
	 * @author Pedro Gon�alves
	 */
	public List<Ordemprodutohistorico> findByForRF(Ordemservico ordemservico, boolean conferenciaBox) {
		return ordemprodutohistoricoDAO.findByForRF(ordemservico, conferenciaBox);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * Lista todos os c�digos de barras poss�veis de serem bipados pelo coletor de uma ordem de 
	 * servi�o. Somente s�o pegos os c�digos de barras do produto principal, quando a informa��o
	 * normavolume est� nulo ou falso. Caso seja true, ser� pego os c�digos de barras dos volumes.
	 * 
	 * O dado log�stico � espec�fico por dep�sito. Portanto � necess�rio especificar o dep�sito.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#findAllBarCodeByOS(Ordemservico ordemservico, Deposito deposito)
	 * @author Pedro Gon�alves
	 * @param ordemservico
	 * @param deposito
	 * @return
	 */
	public List<CodigobarrasVO> findAllBarCodeByOS(Ordemservico ordemservico,Deposito deposito){
		return ordemprodutohistoricoDAO.findAllBarCodeByOS(ordemservico, deposito);
	}
	
	public List<CodigobarrasVO> findAllBarCodeByOSInventario(Ordemservico ordemservico,Deposito deposito){
		return ordemprodutohistoricoDAO.findAllBarCodeByOSInventario(ordemservico, deposito);
	}
	
	/**
	 * Processa as ordens de confer�ncia, Caso seja necess�rio, � gerada as ordens de reconfer�ncia.
	 * 
	 * Inicialmente � separado somente os produtos divergentes, e tamb�m � setado inicialmente todas as 
	 * ordemservicoproduto com o status conclu�do. Os mapas tamb�m s�o alterados, e todos s�o setados como OK.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#atualizarStatus(Ordemservico, Ordemprodutostatus)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#alterarMapasByOSConferencia(Ordemservico, Boolean)
	 * 
	 * Caso seja encontrado produtos divergentes, a OS � setada como FINALIZADO_DIVERGENCIA, e as OSP's divergentes como
	 * CONCLUIDO_DIVERGENTE.
	 * 
	 * Uma nova OS de reconfer�ncia � gerada.
	 * 
	 * � resetado os valores coletados das OSP divergentes.
	 * @see br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService#resetQtdecoletor(Ordemservico)
	 * 
	 * @author Pedro Gon�alves
	 * @param ordemservico
	 * @param listaOPH
	 */
	public void processarConferencia(Ordemservico ordemservico, List<Ordemprodutohistorico> listaOPH, Usuario user){
		Ordemservico ordemservicoAux = ordemservicoService.load(ordemservico);
		if (!ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_ABERTO) 
				&& !ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_EXECUCAO) 
				&& !ordemservicoAux.getOrdemstatus().equals(Ordemstatus.AGUARDANDO_CONFIRMACAO)){
			
			throw new WmsException("Esta ordem de servi�o j� foi finalizada por outro operador.");
		}
		
		boolean generateNew = true;
		
		if(ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1) || ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_2))
			generateNew = false;
		
		Ordemservico osNova = new Ordemservico();
		
		List<Ordemprodutohistorico> listaDivergentes = new ArrayList<Ordemprodutohistorico>();
		for (Ordemprodutohistorico ordemprodutohistorico : listaOPH) {
			if(ordemprodutohistorico.getQtde() == null || !ordemprodutohistorico.getQtde().equals(ordemprodutohistorico.getOrdemservicoproduto().getQtdeesperada())){
				listaDivergentes.add(ordemprodutohistorico);
			}
		}
		
		//sempre atualiza.
		ordemservicoprodutoService.atualizarStatus(ordemservico, Ordemprodutostatus.CONCLUIDO_OK);
		ordemservicoService.alterarMapasByOSConferencia(ordemservico, false);
		
		if(listaDivergentes.isEmpty()){
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
			ordemservicoService.atualizarStatusordemservico(ordemservico);
			gravarQuantidadesColetadas(listaOPH);
		} else {
			//concluir a atual como divergente e gerar uma nova.
			if(ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1) || ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_2))
				ordemservico.setOrdemstatus(Ordemstatus.AGUARDANDO_CONFIRMACAO);
			else
				ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
			ordemservicoService.atualizarStatusordemservico(ordemservico);
			gravarQuantidadesColetadas(listaOPH);
			
			if(generateNew) {
				//criado uma nova ordem para reconferencia.
				osNova.setCdordemservico(null);
				osNova.setCarregamento(ordemservico.getCarregamento());
				osNova.setExpedicao(ordemservico.getExpedicao());
				osNova.setDeposito(ordemservico.getDeposito());
				osNova.setClienteExpedicao(ordemservico.getClienteExpedicao());
				osNova.setTipooperacao(ordemservico.getTipooperacao());
				osNova.setOrdemstatus(Ordemstatus.EM_ABERTO);
				
				if (ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_EXPEDICAO_1))
					osNova.setOrdemtipo(Ordemtipo.RECONFERENCIA_EXPEDICAO_1);
				else
					osNova.setOrdemtipo(Ordemtipo.RECONFERENCIA_EXPEDICAO_2);
					
				osNova.setOrdem(ordemservico.getOrdem() + 1);
				osNova.setOrdemservicoprincipal(ordemservico);
				ordemservicoService.saveOrUpdateBean(osNova);
			}
			
			for (Ordemprodutohistorico ordemprodutohistoricoDivergente : listaDivergentes) {
				ordemprodutohistoricoDivergente.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
				ordemservicoprodutoService.saveOrUpdate(ordemprodutohistoricoDivergente.getOrdemservicoproduto());
				
				if(generateNew) {
					Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
					ordemprodutoligacao.setOrdemservico(osNova);
					ordemprodutoligacao.setOrdemservicoproduto(ordemprodutohistoricoDivergente.getOrdemservicoproduto());
					ordemprodutoligacaoService.saveOrUpdate(ordemprodutoligacao);
				}
			}
			
			ordemservicoService.alterarMapasByOSConferencia(ordemservico, true);
			
			if(generateNew)
				etiquetaexpedicaoService.resetQtdecoletor(osNova);
		}
		
		//Se o usu�rio finalizou uma ordem de confer�ncia vou gerar a ordem de confer�ncia de box.
		if (ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO) ) {
			
			if (ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_EXPEDICAO_1))
				ordemservicoService.gerarConferenciaBox(ordemservico);
			else if (ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1)){
				ordemservico.getOrdemservicoprincipal().setDeposito(ordemservico.getDeposito());
				ordemservicoService.gerarConferenciaBox(ordemservico.getOrdemservicoprincipal());
			}
		}
		
		boolean finalizarCarregamento = false;
		//Se o usu�rio finalizou a confer�ncia de box, vou finalizar o carregamento
		if (ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_EXPEDICAO_2) && listaDivergentes.isEmpty())
			finalizarCarregamento  = true;
		else if ((ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1) || ordemservico.getOrdemtipo().equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_2)) 
				&& ordemservico.getOrdemservicoprincipal().getOrdemtipo().equals(Ordemtipo.CONFERENCIA_EXPEDICAO_2) 
				&& (ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO) || ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA))){
			
			finalizarCarregamento = true;
		}

		if (finalizarCarregamento){
			
			ordemservico.getCarregamento().setDeposito(ordemservico.getDeposito());
			ordemservico.getCarregamento().setCarregamentostatus(Carregamentostatus.FINALIZADO);
			
			if(ordemservico.getExpedicao() != null && ordemservico.getExpedicao().getCdexpedicao() != null){
				ExpedicaoService.getInstance().processaSituacaoExpedicao(ordemservico.getExpedicao(),user);
			}
		}
	}

	/**
	 * 
	 * @param listaOPH
	 */
	private void gravarQuantidadesColetadas(List<Ordemprodutohistorico> listaOPH) {
		for (Ordemprodutohistorico oph : listaOPH){
			
			if (oph.getQtde() != null)
				ordemprodutohistoricoDAO.atualizarQuantidades(oph);
			
			for (Etiquetaexpedicao etiqueta : oph.getOrdemservicoproduto().getListaEtiquetaexpedicao()) {
				if(etiqueta.getQtdecoletor() != null) 
					etiquetaexpedicaoService.updateQtdecoletor(etiqueta);
			}
		}
	}

	private static OrdemprodutohistoricoService instance;
	
	public static OrdemprodutohistoricoService getInstance() {
		if(instance == null)
			instance = Neo.getObject(OrdemprodutohistoricoService.class);
		
		return instance;
	}
	
	/**
	 * Busca as quantidades esperadas dos produtos no banco de dados e recupera os itens referentes aos volumes
	 * 
	 * @author Leonardo Guimar�es
	 * 		   Giovane Freitas
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#findBy(Recebimento recebimento, Ordemservico ordemservico)
	 * 
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Ordemprodutohistorico> prepareQtdes(ConferenciaCegaPapelFiltro filtro) {
		List<Ordemprodutohistorico> listaOrdemProdutoHistorico = (ArrayList<Ordemprodutohistorico>)filtro.getListaOrdemProdutoHistorico();
		Ordemservico loadLast = ordemservicoService.loadLastConferencia(filtro.getRecebimento());
		List<Ordemprodutohistorico> listaOrdemProdutoOriginal = findBy(filtro.getRecebimento(),loadLast);
		

		for(int i = 0; i < listaOrdemProdutoOriginal.size(); i++){
			Produto produtoprincipal = listaOrdemProdutoOriginal.get(i).getOrdemservicoproduto().getProduto().getProdutoprincipal();
			
			//verifico se � um volume e se n�o � o primeiro volume, que j� veio pelo filtro, 
			//se for devo recriar os volumes faltantes e copiar as quantidades.
			if (produtoprincipal != null && !listaOrdemProdutoHistorico.contains(listaOrdemProdutoOriginal.get(i))){				
				//procuro pelo volume do mesmo produto que j� existe para copiar as quantidades
				int indexProdutoMesmoPai = Util.collections.indexOf(listaOrdemProdutoHistorico, 
								"ordemservicoproduto.produto.produtoprincipal.cdproduto", 
								produtoprincipal.getCdproduto());
				
				if (indexProdutoMesmoPai >= 0){
					Ordemprodutohistorico ophMesmoPai = listaOrdemProdutoHistorico.get(indexProdutoMesmoPai);
					Ordemprodutohistorico oph = listaOrdemProdutoOriginal.get(i);
					oph.setQtde(ophMesmoPai.getQtde());
					oph.setQtdeavaria(ophMesmoPai.getQtdeavaria());
					oph.setQtdeColetada(ophMesmoPai.getQtdeColetada());
					oph.setQtdefracionada(ophMesmoPai.getQtdefracionada());
					oph.getOrdemservicoproduto().setQtdeesperada(ophMesmoPai.getOrdemservicoproduto().getQtdeesperada());
					listaOrdemProdutoHistorico.add(oph);
				}
			}else{
				//� um produto simples ou � o primeiro volume (que j� est� no filtro), 
				//n�o precisa de tratamento especial
				Ordemprodutohistorico ordemprodutoOriginal = listaOrdemProdutoOriginal.get(i);
				int index = listaOrdemProdutoHistorico.indexOf(ordemprodutoOriginal);
				Ordemprodutohistorico ordemprodutohistorico = listaOrdemProdutoHistorico.get(index);
				ordemprodutohistorico.getOrdemservicoproduto().setQtdeesperada(ordemprodutoOriginal.getOrdemservicoproduto().getQtdeesperada());
			}
		}
		
		return listaOrdemProdutoHistorico;
	}

	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#findForLancarDados(Ordemservico ordemservico)
	 * 
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemprodutohistorico> findForLancarDados(Ordemservico ordemservico) {
		return ordemprodutohistoricoDAO.findForLancarDados(ordemservico);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param ordemservico
	 */
	public void deleteByOrdemservico(Ordemservico ordemservico) {
		ordemprodutohistoricoDAO.deleteByOrdemservico(ordemservico);
	}
	
	/**
	 * Encontra o ordemprodutohistorico a partir da ordem servi�o produto.
	 * 
	 * @author Pedro Gon�alves
	 * @param ordemservicoproduto
	 * @return
	 */
	public Ordemprodutohistorico findByOSP(Ordemservicoproduto ordemservicoproduto){
		return ordemprodutohistoricoDAO.findByOSP(ordemservicoproduto);
	}
	
	
	/**
	 * Encontra o ordemprodutohistorico a partir da ordem servi�o produto.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservicoproduto
	 * @return
	 */
	public Ordemprodutohistorico findByOspOs(Ordemservicoproduto ordemservicoproduto, Ordemservico ordemservico){
		return ordemprodutohistoricoDAO.findByOspOs(ordemservicoproduto, ordemservico);
	}
	
	/**
	 * excluir todos os Ordemprodutohistorico atrav�s da ordemservico produto.
	 * @author Pedro gon�alves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		ordemprodutohistoricoDAO.deleteAllBy(listaOSP);
	}
	
	/**
	 * Altera a quantidade de produto no hist�rico
	 * @param bean
	 * @author C�ntia Nogueira
	 * @see  br.com.linkcom.wms.geral.dao.OrdemprodutohistoricoDAO#updateQtde(Ordemprodutohistorico)
	 */
	public void updateQtde(Ordemprodutohistorico bean){
	  ordemprodutohistoricoDAO.updateQtde(bean);
	
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param recebimento
	 * @param ordemservicoproduto
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Ordemprodutohistorico getEmbalagemEscolhidaLancamentoConferencia(Recebimento recebimento, Ordemservicoproduto ordemservicoproduto) {
		return ordemprodutohistoricoDAO.getEmbalagemEscolhidaLancamentoConferencia(recebimento, ordemservicoproduto);
	}
	
	/**
	 * Se os endere�os forem BLOCADOS as ordens s�o agrupadas
	 * 
	 * @param listaHistorico
	 * @return
	 */
	public List<Ordemprodutohistorico> agrupaOrdemProdutoHistoricoBlocado(ListSet<Ordemprodutohistorico> listaHistorico) {
		
		//Usar apenas lista indexadas, para garantir que os itens n�o v�o perder a ordem
		
		List<String> chaves = new ArrayList<String>();
		List<Ordemprodutohistorico> itensSelecionados = new ArrayList<Ordemprodutohistorico>();
		List<String> enderecosAdicionados = new ArrayList<String>();
		
		for (Ordemprodutohistorico ordemprodutohistorico : listaHistorico) {
			Endereco endereco = ordemprodutohistorico.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino();
			Produto produto = ordemprodutohistorico.getOrdemservicoproduto().getProduto();
			
			for (Ordemprodutohistorico ophAux : listaHistorico) {
				if(!ordemprodutohistorico.equals(ophAux)){
					Endereco enderecoAux = ophAux.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino();
					Produto produtoAux = ophAux.getOrdemservicoproduto().getProduto();
					
					boolean duplaIdentica = (produto !=  null && produto.equals(produtoAux) && endereco.equals(enderecoAux))
							|| (produto == null && produtoAux == null && endereco.equals(enderecoAux));
					boolean isBlocado = endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO);
					boolean isPredioIgual = endereco.getEnderecoArea().equals(enderecoAux.getEnderecoArea());
					boolean isProdutoIgual = (produto == produtoAux) || (produto != null && produto.equals(produtoAux));
						
					if(duplaIdentica || (isBlocado && isPredioIgual && isProdutoIgual)){
						ordemprodutohistorico.getOrdemservicoproduto().setQtdeesperada(ordemprodutohistorico.getOrdemservicoproduto().getQtdeesperada() + 
								ophAux.getOrdemservicoproduto().getQtdeesperada());
						
						if (ordemprodutohistorico.getQtde() != null || ophAux.getQtde() != null){
							long qtdeInicial = ordemprodutohistorico.getQtde() != null ? ordemprodutohistorico.getQtde() : 0L;
							long qtdeAux = ophAux.getQtde() != null ? ophAux.getQtde() : 0L;
							ordemprodutohistorico.setQtde(qtdeInicial + qtdeAux);
						}
					}
				}
			}
				
			String key = (produto != null ? produto.getCdproduto() : "null") + "-" + endereco.getEnderecoArea();
			//TRATAMENTO PARA N�O EXIBIR LINHAS DUPLICADAS OU COM INFORMA��O REDUNDANTE
			if((!chaves.contains(key) && produto != null) || (produto == null && !enderecosAdicionados.contains(endereco.getEnderecoArea()))){
				chaves.add(key);
				itensSelecionados.add(ordemprodutohistorico);
				enderecosAdicionados.add(endereco.getEnderecoArea());
				
				//Removendo algum endere�o sem produto que tenha sido adicionado para este mesmo endere�o
				int indexOf = chaves.indexOf("null-" + endereco.getEnderecoArea());
				if (produto != null && indexOf >= 0){
					chaves.remove(indexOf);
					itensSelecionados.remove(indexOf);
				}
			}
		}
		
		return itensSelecionados;
	}

	/**
	 * Localiza todos os {@link Ordemprodutohistorico} e seus respectivos {@link Ordemservicoproduto} 
	 * relacionados a uma {@link Ordemservico}.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public List<Ordemprodutohistorico> findByOS(Ordemservico ordemservico) {
		return ordemprodutohistoricoDAO.findByOS(ordemservico);
	}
	
	/**
	 * Busca os dados para executar a confer�ncia via coletor
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemprodutohistorico> findForConferenciaColetor(Ordemservico ordemservico, boolean orderByOrigem) {
		return ordemprodutohistoricoDAO.findForConferenciaColetor(ordemservico, orderByOrigem);
	}
	
	/**
	 * Localiza todos os {@link Ordemprodutohistorico} e seus respectivos {@link Ordemservicoproduto} 
	 * relacionados � primeira ordem de contagem de um recebimento.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public List<Ordemprodutohistorico> findConferencia(Recebimento recebimento) {
		return ordemprodutohistoricoDAO.findConferencia(recebimento);
	}

	/**
	 * Grava null nas refer�ncias de ProdutoEmbalagem.
	 * 
	 * @author Giovane Freitas
	 * @param bean
	 */
	public void removerEmbalagem(Produto bean) {
		ordemprodutohistoricoDAO.removerEmbalagem(bean);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param inventariolote
	 * @return
	 * @author Tom�s Rabelo
	 * @param ordemservicoproduto 
	 * @param ordemservicoAtual 
	 */
	public List<Ordemprodutohistorico> getHistoricoContagensRecontagens(Inventariolote inventariolote, Ordemservicoproduto ordemservicoproduto, Ordemservico ordemservicoAtual) {
		return ordemprodutohistoricoDAO.getHistoricoContagensRecontagens(inventariolote, ordemservicoproduto, ordemservicoAtual);
	}
	
	/**
	 * M�todo responsavel por recuperar a Ordemprodutohistorico para evitar que 
	 * as contagens de inventario sejam duplicadas.
	 * 
	 * @author Filipe Santos
	 * @param endereco
	 * @param produto
	 * @param ordemservico
	 * @return Ordemprodutohistorico
	 */
	public Ordemprodutohistorico findByEnderecoProdutoOrdemServico(Endereco endereco, Produto produto, Ordemservico ordemservico){
		return ordemprodutohistoricoDAO.findByEnderecoProdutoOrdemServico(endereco,produto,ordemservico);
	}
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Ordemprodutohistorico> findAllConferenciaByRecebimento(Recebimento recebimento) {
		return ordemprodutohistoricoDAO.findAllConferenciaByRecebimento(recebimento);
	}
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public Map<Integer, Boolean> isDuasConferenciasRecebimentoIguais(Recebimento recebimento) {
		
		List<Ordemprodutohistorico> listaOrdemprodutohistorico = findAllConferenciaByRecebimento(recebimento);
		
		Map<Integer, Boolean> mapConferenciasIguais = new HashMap<Integer, Boolean>();
	
		if(listaOrdemprodutohistorico!=null && !listaOrdemprodutohistorico.isEmpty()){
			
			for (Ordemprodutohistorico ordemprodutohistorico : listaOrdemprodutohistorico) {
				
				Boolean conferenciasIguais = Boolean.FALSE;
				
				for (Ordemprodutohistorico ordemprodutohistoricoAux : listaOrdemprodutohistorico) {
					
					if(!ordemprodutohistorico.getCdordemprodutohistorico().equals(ordemprodutohistoricoAux.getCdordemprodutohistorico())
							&& ordemprodutohistorico.getOrdemservicoproduto().equals(ordemprodutohistoricoAux.getOrdemservicoproduto())){
						
						if( ordemprodutohistorico.getQtde().equals(ordemprodutohistoricoAux.getQtde()) 						&&
							ordemprodutohistorico.getQtdeavaria().equals(ordemprodutohistoricoAux.getQtdeavaria()) 			&&
							ordemprodutohistorico.getQtdefracionada().equals(ordemprodutohistoricoAux.getQtdefracionada()) ){
							
							conferenciasIguais = Boolean.TRUE;
						}
						
					}
					
				}
				
				mapConferenciasIguais.put(ordemprodutohistorico.getCdordemprodutohistorico(), conferenciasIguais);
			
			}
			
		}
				
		return mapConferenciasIguais;
		
	}
	
}
