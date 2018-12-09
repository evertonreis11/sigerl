package br.com.linkcom.wms.util.expedicao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicaogrupo;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaocm;
import br.com.linkcom.wms.geral.bean.auxiliar.IOrdemexpedicao;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaogrupoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.TipooperacaoService;
import br.com.linkcom.wms.geral.service.VordemexpedicaoService;
import br.com.linkcom.wms.geral.service.VordemexpedicaocmService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

/**
 * Algoritmo para gerar a ordem de conferência da expedição
 * 
 * @author Leonardo Guimarães
 * 
 */
public class GerarOrdemConferenciaExpedicao {

	/**
	 * Cria a ordem de conferência
	 * 
	 * @see #doValidation(List, int)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#findByOrdemservico(Ordemservico ordemservico)
	 * @see br.com.linkcom.wms.geral.service.VordemexpedicaoService#findByCarregamento(Carregamento carregamento)
	 * @see #generateOrdens(List, Set, Ordemservicoproduto)
	 * @see #saveEtiquetas(Etiquetaexpedicao)
	 * 
	 */
	public void execute(final List<Ordemservico> listaOS, final Carregamento carregamento) {
		
		// Não é preciso usar transaction template porque este código já está em uma
		// TransactionTemplate transactionTemplate = Neo.getObject(TransactionTemplate.class);
		// transactionTemplate.execute(new TransactionCallback(){
		
		if (carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo.");
		
		Map<Produto, List<Produto>> mapaVolumes = new HashMap<Produto, List<Produto>>();
		
		List<? extends IOrdemexpedicao> listaVOrdemExpedicao = null;
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito()))
			listaVOrdemExpedicao = VordemexpedicaocmService.getInstance().findByCarregamento(carregamento);
		else
			listaVOrdemExpedicao = VordemexpedicaoService.getInstance().findByCarregamento(carregamento);
		
		int i = 0;
		if (listaVOrdemExpedicao == null)
			return;

		while (i < listaVOrdemExpedicao.size()) {

			// Criando a ordem de conferência
			IOrdemexpedicao ordemexpedicao = listaVOrdemExpedicao.get(i);
			String ordens = "";
			Ordemservico ordemservico = new Ordemservico();

			Cliente cliente = ordemexpedicao.getCdcliente() == null ? null : new Cliente(ordemexpedicao.getCdcliente());
			ordemservico.setClienteExpedicao(cliente);

			Tipooperacao tipooperacao = ordemexpedicao.getCdtipooperacao() == null ? null : TipooperacaoService.getInstance().load(new Tipooperacao(ordemexpedicao.getCdtipooperacao()));
			ordemservico.setTipooperacao(tipooperacao);

			ordemservico.setDeposito(WmsUtil.getDeposito());
			ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
			ordemservico.setOrdemtipo(Ordemtipo.CONFERENCIA_EXPEDICAO_1);
			ordemservico.setCarregamento(new Carregamento(ordemexpedicao.getCdcarregamento()));
			ordemservico.setOrdem(1);
			Set<Ordemprodutoligacao> listaOrdemProdutoLigacao = new ListSet<Ordemprodutoligacao>(Ordemprodutoligacao.class);
			// FIM - Criando a ordem de conferência

			// Criando as OrdemServidoProduto e etiquetas
			// Este do-while é o responável pela quebra das ordens
			do {
				ordemexpedicao = listaVOrdemExpedicao.get(i);
				Vordemexpedicaocm vordemexpedicaocm = null;
				int indexOS = listaOS.indexOf(new Ordemservico(ordemexpedicao.getCdordemservico()));
				Ordemservico os = listaOS.get(indexOS);
				
				// criando os objetos OrdemServicoProduto a partir dos objetos
				// que já existem nos mapas
				if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito())){
					setListaOrdemprodutoligacaoCM(mapaVolumes, ordemexpedicao,ordemservico, listaOrdemProdutoLigacao, vordemexpedicaocm);
				}else{
					for(Ordemprodutoligacao opl : os.getListaOrdemProdutoLigacao())
						setListaOrdemprodutoligacao(mapaVolumes, ordemexpedicao,ordemservico, listaOrdemProdutoLigacao,	vordemexpedicaocm, opl);			
				}
				
				ordens += ordemexpedicao.getCdordemservico() + ",";
				i++;
			} while (doValidation(listaVOrdemExpedicao, i));
			
			ordemservico.setListaOrdemProdutoLigacao(listaOrdemProdutoLigacao);
			OrdemservicoService.getInstance().saveOrUpdateNoUseTransaction(ordemservico);
			OrdemservicoService.getInstance().updateOrdemServicoPrincipal(ordens.substring(0, ordens.length() - 1), ordemservico);
			
			if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA, WmsUtil.getDeposito())){
				//Criando a ordem de conferência de box.
				Ordemservico conferenciaBox = new Ordemservico();
				conferenciaBox.setClienteExpedicao(ordemservico.getClienteExpedicao());
				conferenciaBox.setTipooperacao(ordemservico.getTipooperacao());
				conferenciaBox.setDeposito(WmsUtil.getDeposito());
				conferenciaBox.setOrdemstatus(Ordemstatus.EM_ABERTO);
				conferenciaBox.setOrdemtipo(Ordemtipo.CONFERENCIA_EXPEDICAO_2);
				conferenciaBox.setCarregamento(ordemservico.getCarregamento());
				conferenciaBox.setOrdem(1);
				conferenciaBox.setOrdemservicoprincipal(ordemservico);
				OrdemservicoService.getInstance().saveOrUpdateNoUseTransaction(conferenciaBox);
			}
		}
	}

	/**
	 * 
	 * @param mapaVolumes
	 * @param ordemexpedicao
	 * @param ordemservico
	 * @param listaOrdemProdutoLigacao
	 * @param vordemexpedicaocm
	 * @param opl
	 */
	private void setListaOrdemprodutoligacao(Map<Produto, List<Produto>> mapaVolumes,IOrdemexpedicao ordemexpedicao, Ordemservico ordemservico,Set<Ordemprodutoligacao> listaOrdemProdutoLigacao,Vordemexpedicaocm vordemexpedicaocm, Ordemprodutoligacao opl) {
		Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto();
		ordemservicoproduto.setProduto(opl.getOrdemservicoproduto().getProduto());
		ordemservicoproduto.setQtdeesperada(opl.getOrdemservicoproduto().getQtdeesperada());
		ordemservicoproduto.setOrdemprodutostatus(opl.getOrdemservicoproduto().getOrdemprodutostatus());
		ordemservicoproduto.setTipopalete(opl.getOrdemservicoproduto().getTipopalete());

		ordemservicoproduto.setCdordemservicoproduto(null);					
		List<Produto> listaVolumes = null;

		// os volumes ficam no Map para evitar consultas
		// desnecessárias
		if (!mapaVolumes.containsKey(ordemservicoproduto.getProduto())) {

			listaVolumes = ProdutoService.getInstance().findVolumes(ordemservicoproduto.getProduto());
			if (listaVolumes != null)
				mapaVolumes.put(ordemservicoproduto.getProduto(), listaVolumes);
			else {
				List<Produto> listaVazia = Collections.emptyList();
				mapaVolumes.put(ordemservicoproduto.getProduto(), listaVazia);
			}

		} else
			listaVolumes = mapaVolumes.get(ordemservicoproduto.getProduto());

		if (listaVolumes != null && !listaVolumes.isEmpty() && opl!=null)
			generateOrdens(listaVolumes, listaOrdemProdutoLigacao, opl.getOrdemservicoproduto(), ordemservico, opl.getOrdemservicoproduto().getListaEtiquetaexpedicaogrupo());
		else {
			Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
			ordemprodutoligacao.setOrdemservico(ordemservico);
			ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
			listaOrdemProdutoLigacao.add(ordemprodutoligacao);
			
			OrdemservicoprodutoService.getInstance().saveOrUpdateNoUseTransaction(ordemservicoproduto);
			
			// criando as etiquetas de conferência a partir das etiqueta que foram criadas para os mapas
			for (Etiquetaexpedicao etiquetaexpedicao : opl.getOrdemservicoproduto().getListaEtiquetaexpedicao())
				saveEtiquetas(etiquetaexpedicao.getCarregamentoitem(), ordemservicoproduto, ordemservico, opl.getOrdemservicoproduto().getListaEtiquetaexpedicaogrupo());		
		}
	}

	/**
	 * 
	 * @param mapaVolumes
	 * @param ordemexpedicao
	 * @param ordemservico
	 * @param listaOrdemProdutoLigacao
	 * @param vordemexpedicaocm
	 */
	private void setListaOrdemprodutoligacaoCM(Map<Produto, List<Produto>> mapaVolumes,IOrdemexpedicao ordemexpedicao, Ordemservico ordemservico,Set<Ordemprodutoligacao> listaOrdemProdutoLigacao,Vordemexpedicaocm vordemexpedicaocm) {
		Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto();
		if(ordemexpedicao instanceof Vordemexpedicaocm){
			vordemexpedicaocm = (Vordemexpedicaocm) ordemexpedicao;
			ordemservicoproduto.setQtdeesperada(vordemexpedicaocm.getQtde());
			ordemservicoproduto.setProduto(new Produto(vordemexpedicaocm.getCdproduto()));
			ordemservicoproduto.setProdutoembalagem(vordemexpedicaocm.getProdutoembalagem());
			ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
			ordemservicoproduto.setCdordemservicoproduto(null);
		}		

		Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
		ordemprodutoligacao.setOrdemservico(ordemservico);
		ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
		listaOrdemProdutoLigacao.add(ordemprodutoligacao);
		
		OrdemservicoprodutoService.getInstance().saveOrUpdateNoUseTransaction(ordemservicoproduto);

		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito()) && vordemexpedicaocm!=null){
			for (int j = 0; j < ordemservicoproduto.getQtdeesperada(); j++) {						
				Carregamentoitem carregamentoitem = new Carregamentoitem(vordemexpedicaocm.getCdcarregamentoitem());
				saveEtiquetas(carregamentoitem, ordemservicoproduto, ordemservico, null);
			}
		}
	}
	
	/**
	 * Salva o número de etiquetas de acordo com a quantidade encontrado no pedido de venda
	 * @param etiquetasgrupo 
	 * 
	 * @param etiqueta
	 */
	private void saveEtiquetas(Carregamentoitem carregamentoitem, Ordemservicoproduto osp, Ordemservico ordemservico, Set<Etiquetaexpedicaogrupo> etiquetasgrupo) {
		Boolean imprimeEtiqueta = ordemservico.getTipooperacao().getImprimeetiqueta();

		// se o tipo de operação imprime etiqueta, irei criar uma etiqueta para cada unidade do produto
		if(imprimeEtiqueta && ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito())){
			Etiquetaexpedicao novaEtiqueta = new Etiquetaexpedicao();
			novaEtiqueta.setOrdemservicoproduto(osp);
			novaEtiqueta.setCarregamentoitem(carregamentoitem);
			EtiquetaexpedicaoService.getInstance().saveOrUpdate(novaEtiqueta);			
		}else if (imprimeEtiqueta) {
			for (int i = 0; i < osp.getQtdeesperada(); i++) {
				Etiquetaexpedicao novaEtiqueta = new Etiquetaexpedicao();
				novaEtiqueta.setOrdemservicoproduto(osp);
				novaEtiqueta.setCarregamentoitem(carregamentoitem);
				EtiquetaexpedicaoService.getInstance().saveOrUpdateNoUseTransaction(novaEtiqueta);
			}
		} else {// a operação não imprime etiqueta, então será uma única etiqueta.
			Etiquetaexpedicao novaEtiqueta = new Etiquetaexpedicao();
			novaEtiqueta.setOrdemservicoproduto(osp);
			novaEtiqueta.setCarregamentoitem(carregamentoitem);

			EtiquetaexpedicaoService.getInstance().saveOrUpdateNoUseTransaction(novaEtiqueta);
			
			//verificando se existem itens sem etiquetas
			for (Etiquetaexpedicaogrupo etiquetaexpedicaogrupo : etiquetasgrupo){
				Etiquetaexpedicaogrupo novaEtiquetaGrupo = new Etiquetaexpedicaogrupo();
				novaEtiquetaGrupo.setOrdemservicoproduto(osp);
				novaEtiquetaGrupo.setCarregamentoitem(etiquetaexpedicaogrupo.getCarregamentoitem());
				novaEtiquetaGrupo.setEtiquetaexpedicao(novaEtiqueta);

				EtiquetaexpedicaogrupoService.getInstance().saveOrUpdateNoUseTransaction(novaEtiquetaGrupo);				
			}
		}
	}

	/**
	 * Gera as ordens de servico das etiquetas e dos volumes
	 * 
	 * @see br.com.linkcom.wms.util.expedicao.GerarOrdemConferenciaExpedicao#generateNewOSP(Ordemservicoproduto
	 *      ordemservicoproduto, Produto produto)
	 * 
	 * @param listaVolumes
	 * @param listaOrdemProdutoLigacao
	 * @param ordemservicoproduto
	 * @param z
	 * @param etiquetaexpedicao
	 * @param ordemservico
	 * @param etiquetasgrupo 
	 */
	private void generateOrdens(List<Produto> listaVolumes,Set<Ordemprodutoligacao> listaOrdemProdutoLigacao, Ordemservicoproduto ospMapa, Ordemservico ordemservico, Set<Etiquetaexpedicaogrupo> etiquetasgrupo) {

		for (Produto produto : listaVolumes) {
			Ordemservicoproduto osp = new Ordemservicoproduto();
			osp.setProduto(produto);
			osp.setQtdeesperada(ospMapa.getQtdeesperada());
			osp.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
								
			OrdemservicoprodutoService.getInstance().saveOrUpdateNoUseTransaction(osp);

			Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
			ordemprodutoligacao.setOrdemservico(ordemservico);
			ordemprodutoligacao.setOrdemservicoproduto(osp);
			listaOrdemProdutoLigacao.add(ordemprodutoligacao);

			OrdemservicoprodutoService.getInstance().saveOrUpdateNoUseTransaction(osp);

			// criando as etiquetas de conferência a partir das etiqueta que foram criadas para os mapas
			for (Etiquetaexpedicao etiquetaexpedicao : ospMapa.getListaEtiquetaexpedicao()) {
				saveEtiquetas(etiquetaexpedicao.getCarregamentoitem(), osp, ordemservico, etiquetasgrupo);
			}				
		}

	}

	/**
	 * Faz a validação do while
	 * 
	 * @param listaVOrdemExpedicao
	 * @param index
	 * @param vordemexpedicao
	 * 
	 * @throws NullPointerException -
	 *             Caso o index não seja maior que zero.
	 * 
	 * @return
	 */
	private boolean doValidation(List<? extends IOrdemexpedicao> listaVOrdemExpedicao, int index) {
		if (index < listaVOrdemExpedicao.size()) {
			if (listaVOrdemExpedicao.get(index - 1).getCdcliente() == null) {

				Integer cdtipooperacaoAtual = listaVOrdemExpedicao.get(index).getCdtipooperacao();
				Integer cdtipooperacaoAnterior = listaVOrdemExpedicao.get(index - 1).getCdtipooperacao();

				if (cdtipooperacaoAnterior == null)
					return cdtipooperacaoAtual == null;
				else if (cdtipooperacaoAtual == null)
					return false;

				return cdtipooperacaoAtual.equals(cdtipooperacaoAnterior);

			} else {
				boolean mesmoCliente = false;

				if (listaVOrdemExpedicao.get(index).getCdcliente() == null)
					mesmoCliente = false;
				else
					mesmoCliente = listaVOrdemExpedicao.get(index).getCdcliente().equals(listaVOrdemExpedicao.get(index - 1).getCdcliente());

				// se for o mesmo cliente vou comparar o tipo de operação
				if (mesmoCliente) {
					Integer cdtipooperacaoAtual = listaVOrdemExpedicao.get(index).getCdtipooperacao();
					Integer cdtipooperacaoAnterior = listaVOrdemExpedicao.get(index - 1).getCdtipooperacao();

					if (cdtipooperacaoAnterior == null)
						return cdtipooperacaoAtual == null;
					else if (cdtipooperacaoAtual == null)
						return false;

					return cdtipooperacaoAtual.equals(cdtipooperacaoAnterior);
				} else
					return false;
			}
		}
		return false;
	}

}
