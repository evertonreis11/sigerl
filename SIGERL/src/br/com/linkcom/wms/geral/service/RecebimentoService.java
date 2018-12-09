package br.com.linkcom.wms.geral.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Descargaprecoveiculo;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Notafiscalentradaproduto;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.geral.dao.RecebimentoDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.EtiquetaumaFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.AgendaverbaFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.CalculocobrancaFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.RecebimentoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitirDivergenciaRecebimentoFiltro;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.logistica.EtiquetasumaVO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class RecebimentoService extends GenericService<Recebimento> {

	@SuppressWarnings("unused")
	private RecebimentoDAO recebimentoDAO;
	private RecebimentonotafiscalService recebimentonotafiscalService;
	private OrdemservicoService ordemservicoService;
	private OrdemservicoprodutoService ordemservicoprodutoService;
	private DescargaprecoveiculoService descargaprecoveiculoService;
	private NotafiscalentradaService notafiscalentradaService;
	private OrdemprodutoligacaoService ordemprodutoligacaoService;
	private ProdutoService produtoService;
	private NotafiscalentradaprodutoService notafiscalentradaprodutoService;
	private PedidocompraprodutoService pedidocompraprodutoService;
	private AcompanhamentoveiculoService acompanhamentoveiculoService;

	public void setRecebimentoDAO(RecebimentoDAO recebimentoDAO) {
		this.recebimentoDAO = recebimentoDAO;
	}
	
	public void setNotafiscalentradaService(NotafiscalentradaService notafiscalentradaService) {
		this.notafiscalentradaService = notafiscalentradaService;
	}
	
	public void setRecebimentonotafiscalService(RecebimentonotafiscalService recebimentonotafiscalService) {
		this.recebimentonotafiscalService = recebimentonotafiscalService;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setOrdemservicoprodutoService(OrdemservicoprodutoService ordemservicoprodutoService) {
		this.ordemservicoprodutoService = ordemservicoprodutoService;
	}
	
	public void setDescargaprecoveiculoService(DescargaprecoveiculoService descargaprecoveiculoService) {
		this.descargaprecoveiculoService = descargaprecoveiculoService;
	}
	
	public void setOrdemprodutoligacaoService(OrdemprodutoligacaoService ordemprodutoligacaoService) {
		this.ordemprodutoligacaoService = ordemprodutoligacaoService;
	}
	
	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}
	
	public void setNotafiscalentradaprodutoService(NotafiscalentradaprodutoService notafiscalentradaprodutoService) {
		this.notafiscalentradaprodutoService = notafiscalentradaprodutoService;
	}
	
	public void setPedidocompraprodutoService(PedidocompraprodutoService pedidocompraprodutoService) {
		this.pedidocompraprodutoService = pedidocompraprodutoService;
	}
	
	public void setAcompanhamentoveiculoService(AcompanhamentoveiculoService acompanhamentoveiculoService) {
		this.acompanhamentoveiculoService = acompanhamentoveiculoService;
	}

	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#findByStatusAbertoPendente
	 * 
	 * @return
	 * @author Pedro Gon�alves
	 */
	public List<Recebimento> findByStatusAbertoPendente(){
		return recebimentoDAO.findByStatusAbertoPendente();
	}

	/**
	 * Salva o recebimento, e gera a ordem de servi�o.
	 * 
	 * @see br.com.linkcom.wms.geral.service.RecebimentonotafiscalService#salvarRecebimentoNotaFiscal
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#salvarOrdemServico
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#salvarOrdemServicoProduto
	 * 
	 * @author Pedro Gon�alves
	 * @param recebimento
	 * @param veiculo
	 */
	public void salvarRecebimento(final RecebimentoFiltro filtro){
		Recebimento recebimento = filtro.getRecebimento();
		recebimento.setTipoenderecamento(Tipoenderecamento.NAO_DEFINIDO);
		recebimento.setRecebimentostatus(Recebimentostatus.DISPONIVEL);				
		recebimento.setDtrecebimento(new java.sql.Timestamp(System.currentTimeMillis()));				
		recebimento.setDeposito(WmsUtil.getDeposito());
						
		//Criando um usu�rio tempor�rio porque sen�o ir� dar erro de LazyInitializationException
		Usuario usuario = new Usuario();
		usuario.setCdpessoa(WmsUtil.getUsuarioLogado().getCdpessoa());
		usuario.setNome(WmsUtil.getUsuarioLogado().getNome());
		recebimento.setUsuario(usuario);
		
		saveOrUpdateNoUseTransaction(recebimento);
		
		Ordemservico salvarOrdemServico = ordemservicoService.salvarOrdemServico(recebimento,recebimento.getDeposito(),Ordemstatus.EM_ABERTO,1);
		List<Ordemservicoproduto> listaOrdemServicoProduto = ordemservicoprodutoService.makeListaOrdemServicoProduto(salvarOrdemServico,recebimentonotafiscalService.salvarRecebimentoNotaFiscal(filtro));
		makeListaProdutoLigacao(listaOrdemServicoProduto);
		ordemprodutoligacaoService.salvarOrdemProdutoLigacao(salvarOrdemServico,listaOrdemServicoProduto);
		filtro.setOrdemservico(salvarOrdemServico);
		//Solicitacao de nao bloqueio de box a partir da implantacao do dia 26/05/2008
		//recebimento.getBox().setBoxstatus(Boxstatus.BLOQUEADO);
		//boxService.updateStatusBox(recebimento.getBox());
		
		Timestamp dataChegada = WmsUtil.convertDateTimeInTimestamp(filtro.getDataChegada(), filtro.getHoraChegada());
		notafiscalentradaService.atualizarDataChegada(recebimento, dataChegada);
	}
	
	/**
	 * Cria uma lista de ordemprodutoligacao
	 * 
	 * @param listaOrdemServicoProduto
	 */
	public void makeListaProdutoLigacao(List<Ordemservicoproduto> listaOrdemServicoProduto) {
		for (Ordemservicoproduto ordemservicoproduto : listaOrdemServicoProduto) {
			Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
			ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
			ordemservicoproduto.getListaOrdemprodutoLigacao().add(ordemprodutoligacao);
		}
	}			
	
	/**
	 * Se o recebimento est� "Em endere�amento" e se todas as O.S. de endere�amento e confer�ncia j� foram 
	 * executadas, muda o Status do recebimento e atualiza o estoque.
	 * 
	 * @param recebimento
	 * @author Giovane Freitas
	 * @throws InsercaoInvalidaException 
	 */
	public void atualizarEstoqueRecebimento(final Recebimento recebimento) {
		Recebimento ultimaVersaoBD = load(recebimento);
		if (!ultimaVersaoBD.getRecebimentostatus().equals(Recebimentostatus.EM_ENDERECAMENTO)){
			return;
		}
		
		if (isOrdensServicoDoRecebimentoFinalizadas(recebimento)){
			recebimento.setRecebimentostatus(Recebimentostatus.ENDERECADO);
			gravaStatusRecebimento(recebimento);
			
			List<Ordemservico> listaOS = ordemservicoService.findByRecebimento(recebimento);
			
			for (Ordemservico os : listaOS){
				//Se � uma ordem de endere�amento e se j� foi finalizada, atualiza o status dos endere�os
				if ((Ordemtipo.ENDERECAMENTO_AVARIADO.equals(os.getOrdemtipo())
						|| Ordemtipo.ENDERECAMENTO_FRACIONADO.equals(os.getOrdemtipo())
						|| Ordemtipo.ENDERECAMENTO_PADRAO.equals(os.getOrdemtipo()))){
				
					List<Ordemservicoprodutoendereco> listaOSPE = OrdemservicoprodutoenderecoService.getInstance().findByOrdemServico(os);
					for (Ordemservicoprodutoendereco ospe : listaOSPE){
						//Se o item ainda n�o foi conclu�do ent�o ignora
						if (Ordemprodutostatus.NAO_CONCLUIDO.equals(ospe.getOrdemservicoproduto().getOrdemprodutostatus()))
							continue;
						
						//Atualizar os estoques dos produtos endere�ados
						boolean isUMA = Ordemtipo.ENDERECAMENTO_PADRAO.equals(os.getOrdemtipo()) 
										&& Tipoenderecamento.AUTOMATICO.equals(recebimento.getTipoenderecamento());
						
						try {
							EnderecoprodutoService.getInstance().atualizaEstoqueEntrada(ospe, isUMA, true);

							if (ospe.getEnderecoorigem() != null && ospe.getQtde() != null && ospe.getQtde() > 0L)
								EnderecoprodutoService.getInstance().atualizaEstoqueSaida(ospe, false, true);
						} catch (InsercaoInvalidaException e) {
							throw new WmsException(e.getMessage(), e);
						}
					}
			
					//Atualizando a rastreabilidade para as O.S. de endere�amento
					ordemservicoService.callProcedureAtualizarRastreabilidade(os);
				}
			}				
		}
		
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * @see RecebimentoDAO#atualizaStatusRecebimento(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @author Pedro Gon�alves
	 */
	public void gravaStatusRecebimento(Recebimento recebimento) {
		recebimentoDAO.gravaStatusRecebimento(recebimento);
	}
	
	public void gravaStatusRecebimentoDireto(Recebimento recebimento) {
		recebimentoDAO.gravaStatusRecebimentoDireto(recebimento);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.service.RecebimentoService#findRecebimentosNaoCalculados()
	 * @return
	 * 
	 * @author Arantes
	 * 
	 */
	public List<Recebimento> findRecebimentosNaoCalculados() {
		return recebimentoDAO.findRecebimentosNaoCalculados();
	}
	
	/**
	 * M�todo de refer�ncia ao DAO. Este m�todo carrega o recebimento que est� sendo atualizado e 
	 * configura alguns de seus atributos com os atributos do filtro passado.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#load(Recebimento)
	 * @see br.com.linkcom.wms.geral.service.DescargaprecoveiculoService#load(Descargaprecoveiculo)
	 * @param recebimento
	 * @author Arantes
	 */
	public void salvarCalculoCobranca(CalculocobrancaFiltro filtro) {
		Recebimento recebimento = recebimentoDAO.load(new Recebimento(filtro.getRecebimento()));
		
		recebimento.setDescargapreco(filtro.getDescargapreco());
		recebimento.setValordescarga(filtro.getValor());
		recebimento.setValorreceber(filtro.getValorReceber());
		recebimento.setObservacao(filtro.getObs());
		
		if(filtro.getDescargaprecoveiculo() != null) {
			Descargaprecoveiculo descargaprecoveiculo = descargaprecoveiculoService.load(filtro.getDescargaprecoveiculo());
			recebimento.setTipoveiculo(descargaprecoveiculo.getTipoveiculo());
		}
		
		recebimentoDAO.saveOrUpdate(recebimento);
	}
	
	/**
	 *
	 * M�todo de refer�ncia ao DAO. 
	 * Verifica se o recebimento est� conclu�do com diverg�ncia
	 * 
	 * @param filtro
	 * @return
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#isconcluidodivergencia(Recebimento)
	 * 
	 * @author Arantes
	 */
	public Boolean isconcluidodivergencia(Recebimento filtro) {
		return recebimentoDAO.isconcluidodivergencia(filtro);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO. 
	 * Encontra o recebimento em aberto a partir do box. O recebimento status deve ser em andamento.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#findRecebimentoByBoxRF(String box)
	 * @param box
	 * @return
	 * @author Pedro Gon�alves
	 */
	public Recebimento findRecebimentoByBoxRF(Recebimento recebimento,Deposito deposito){
		return recebimentoDAO.findRecebimentoByBoxRF(recebimento,deposito);
	}
	
	/**
	 * Prepara a lista de notas fiscais exibida
	 * na listagem
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param listagem
	 */
	public void prepareNotasFiscais(ListagemResult<Recebimento> listagem) {
		List<Recebimento> listaRecebimento = listagem.list();
		for (Recebimento recebimento : listaRecebimento) {
			List<Notafiscalentrada> listaNotafiscal = notafiscalentradaService.findByRecebimento(recebimento);
			recebimento.setNotasFiscais(WmsUtil.concatenateWithLimit(listaNotafiscal,"numero",30));
		}
	}
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO.
	 * Recupera uma lista de recebimentos
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#findByRecebimento(EmitirDivergenciaRecebimentoFiltro)
	 * 
	 * @author Arantes
	 * 
	 * @param recebimento
	 * @return List<Recebimento>
	 * 
	 */
	public List<Recebimento> findByRecebimento(EmitirDivergenciaRecebimentoFiltro filtro) {
		return recebimentoDAO.findByRecebimento(filtro);
	}
	
	/* singleton */
	private static RecebimentoService instance;
	public static RecebimentoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(RecebimentoService.class);
		}
		return instance;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#findByRecebimento(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @return
	 */
	public Recebimento findByRecebimento(Recebimento recebimento) {
		return recebimentoDAO.findByRecebimento(recebimento);
	}
	
	/**
	 * M�todo de referencia DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param recebimento
	 * @throws SQLException 
	 */
	public void enderecarRecebimento(Recebimento recebimento,Integer automatico, boolean forcarUmaManual) throws SQLException {
		recebimentoDAO.enderecarRecebimento(recebimento,automatico, forcarUmaManual);
	}
	
	/**
	 * 
	 * M�todo que cria uma lista de bean de EtiquetaUma para ser passado para o relat�rio.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#findByRecebimentoUma(EtiquetaumaFiltro)
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#findByListaOrdemservico(List) 
	 * 
	 * @author Arantes
	 * @author Giovane Freitas
	 * 
	 * @param filtro
	 * @return IReport
	 * 
	 */
	public IReport createReportEtiquetaUma(EtiquetaumaFiltro filtro) {
		Report report = new Report("RelatorioEtiquetaProdutoUma");

		List<EtiquetasumaVO> listaEtUma = ordemservicoService.findForReportEtiquetaUma(filtro);
		report.setDataSource(listaEtUma);			
		
		return report;
	}
	
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.RecebimentoDAO#findForEnderecamento(Recebimento, Deposito, Tipoenderecamento)
	 * 
	 * @param cdrecebimento
	 * @return
	 */
	public Recebimento findForEnderecamento(Recebimento recebimento,Deposito deposito,Tipoenderecamento tipoenderecamento) {
		return recebimentoDAO.findForEnderecamento(recebimento,deposito,tipoenderecamento);
	}

	/**
	 * M�todo que verifica se o recebimento possui algum produto com dados faltantes.
	 * Os dados log�sticos que ser�o considerados s�o: todos os campos dos grupos Embalagem de Recebimento, 
	 * Normas de paletiza��o e Armazenagem, incluindo Estrutura (Endere�o de  picking s� ser� exigido se a 
	 * primeira estrutura cadastrada for porta-palete);
	 * 
	 * @param recebimento
	 * @return
	 * @author Tom�s Rabelo
	 * @param tipoenderecamento 
	 */
	public boolean possuiProdutosDadosFaltantes(Recebimento recebimento, Tipoenderecamento tipoenderecamento) {
		List<Produto> listaProdutos = produtoService.findProdutosDadosFaltantesNovo(recebimento);
		return produtoService.possuiProdutosDadosFaltantes(listaProdutos, tipoenderecamento);
	}

	/**
	 * Verifica se a confer�ncia do recebimento j� foi conclu�da.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public boolean isConferenciaConcluida(Recebimento recebimento) {
		List<Ordemservico> listaOS = ordemservicoService.findByRecebimento(recebimento);
		for (Ordemservico os : listaOS){
			//Verifica se � uma OS de confer�ncia
			if (Ordemtipo.CONFERENCIA_RECEBIMENTO.equals(os.getOrdemtipo()) 
					|| Ordemtipo.RECONFERENCIA_RECEBIMENTO.equals(os.getOrdemtipo())){
				
				//Se tiver uma OS de recebimento n�o finalizada, ent�o retorna false.
				if (!Ordemstatus.FINALIZADO_DIVERGENCIA.equals(os.getOrdemstatus()) 
						&& !Ordemstatus.FINALIZADO_SUCESSO.equals(os.getOrdemstatus())){
					
					return false;
				}
			}
		}
		
		//Todas as OS de confer�ncia foram conclu�das
		return true;
	}

	/**
	 * Localiza um recebimento a partir da ordem de servi�o.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Recebimento findByOrdemservico(Ordemservico ordemservico) {
		return recebimentoDAO.findByOrdemservico(ordemservico);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param recebimento
	 * @return
	 * @author Tom�s Rabelo
	 */
	public boolean isOrdensServicoDoRecebimentoFinalizadas(Recebimento recebimento) {
		return recebimentoDAO.isOrdensServicoDoRecebimentoFinalizadas(recebimento);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param recebimento
	 * @return
	 * @auhtor Tom�s Rabelo
	 */
	public boolean isRecebimentoAvailableForCobrancaDescarga(Recebimento recebimento) {
		return recebimentoDAO.isRecebimentoAvailableForCobrancaDescarga(recebimento);
	}
	
	/**
	 * Chama a procedure 'atualizar_valormedio' no banco de dados do sistema
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param deposito
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void atualizarValorMedio(Recebimento recebimento) {
		recebimentoDAO.atualizarValorMedio(recebimento);
	}
	
	/**
	 * Busca os totais recebidos por m�s dentro de um semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoRecebimento(AgendaverbaFiltro filtro){
		return recebimentoDAO.findResumoRecebimento(filtro.getDeposito(), filtro.getExercicio());
	}
	
	public List<ResumoAgendaverba> findResumoRecebido(Deposito deposito, Date data){
		return recebimentoDAO.findResumoRecebimentoVerbaNovo(deposito, data);
	}
	
	public void updateRecebimento(Recebimento recebimento) {
		recebimentoDAO.updateRecebimento(recebimento);
	} 
	
	public Boolean validaAgendamento(WebRequestContext request, List<Notafiscalentrada> notas){

		String idAgenda = null;
		Agenda agenda;
		for (Notafiscalentrada ne : notas) {
			idAgenda = request.getParameter("vvv" + ne.getCdnotafiscalentrada());
			if(idAgenda != null && !idAgenda.equals("")){
				agenda = new Agenda(Integer.parseInt(idAgenda));
				
				List<Notafiscalentradaproduto> listaNFEP = notafiscalentradaprodutoService.findNotaProdutos(ne);
				
				if(listaNFEP != null && listaNFEP.size() > 0){
					for (Notafiscalentradaproduto item : listaNFEP) {
						if(!pedidocompraprodutoService.verificaProdutoAgenda(item.getProduto(), agenda, ne.getPedidocompra()))
							return false;
					}
				}
			}
		}
		return true;
	}
	
	public List<ResumoAgendaverba> findResumoRecebimentoVerbaNovo(AgendaverbaFiltro filtro){
		return recebimentoDAO.findResumoRecebimentoVerbaNovo(filtro.getDeposito(), filtro.getExercicio());
	}
	
	public List<ResumoAgendaverba> findResumoRecebimentoVerbaNovo(Deposito deposito, Date data){
		return recebimentoDAO.findResumoRecebimentoVerbaNovo(deposito, data);
	}
	
	public List<ResumoAgendaverba> findResumoRecebimentoFinanceiroNovo(Deposito deposito, Date dtagenda, Date dtprevisao){
		return recebimentoDAO.findResumoRecebimentoFinanceiroNovo(deposito, dtagenda, dtprevisao);
	}

	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * @see RecebimentoDAO#LiberaVeiculo(Recebimento recebimento)
	 * 
	 * @param recebimento
	 * @author Jos� de Queiroz
	 */
	public String callLiberaVeiculo(Integer tipo,Recebimento recebimento) throws SQLException {
		return recebimentoDAO.callLiberaVeiculo(tipo, recebimento);
	}
}
