package br.com.linkcom.wms.util.expedicao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentohistorico;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicaogrupo;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Vmapaseparacao;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaoboxcm;
import br.com.linkcom.wms.geral.bean.auxiliar.IMapaseparacao;
import br.com.linkcom.wms.geral.bean.vo.ExpedicaoVO;
import br.com.linkcom.wms.geral.service.CarregamentoService;
import br.com.linkcom.wms.geral.service.CarregamentohistoricoService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaogrupoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.VmapaseparacaoboxService;
import br.com.linkcom.wms.geral.service.VmapaseparacaoboxcmService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

/**
 * Algoritmo para a gera��o dos mapas de separa��o.
 * 
 * @author Pedro Gon�alves
 */
public class GeradorSeparacaoBox {

	private List<Ordemservico> listaOS;
	private final List<Box> listaBox;
	private List<? extends IMapaseparacao> listaMapa;
	private List<List<Etiquetaexpedicao>> listaEtiquetaExpedicao = new ArrayList<List<Etiquetaexpedicao>>();
	private List<List<Etiquetaexpedicaogrupo>> listaEtiquetaExpedicaoGrupo = new ArrayList<List<Etiquetaexpedicaogrupo>>();
	private Map<Ordemservico, List<Etiquetaexpedicao>> mapaEtiquetasExpedicao = new HashMap<Ordemservico, List<Etiquetaexpedicao>>();
	private IMapaseparacao lastVmapaseparacao;
	private List<Carregamento> carregamentos = new ArrayList<Carregamento>();
	private final Expedicao expedicaoPrincipal;//Quando for em onda todos os mapas ficar�o centralizados
	private Boolean quebrarpormapa = false;
	private Carregamento lastCarregamento = null;
	private final ExpedicaoVO expedicaoVO;
	
	/**
	 * Gera um novo mapa de separa��o.
	 * @param expedicaoVO
	 */
	public GeradorSeparacaoBox(ExpedicaoVO expedicaoVO) {
		this.expedicaoVO = expedicaoVO;
		this.listaBox = expedicaoVO.getListaBoxes();
		this.expedicaoPrincipal = this.listaBox.iterator().next().getListaExpedicoes().iterator().next();
	}
	
	/**
	 * Inicializa o algoritmo.
	 */
	public void start() {
		findAllMapaSeparacao();
		makeMapas();
	}
	
	/**
	 * Acessa a view, e carrega todos os mapas dele a partir do carregamento.
	 * 
	 * @see br.com.linkcom.wms.geral.service.VmapaseparacaoService#findAllBy(Carregamento carregamento)
	 */
	private void findAllMapaSeparacao(){
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito()))
			this.listaMapa = VmapaseparacaoboxcmService.getInstance().findAllByBox(listaBox);
		else
			this.listaMapa = VmapaseparacaoboxService.getInstance().findAllByBox(listaBox);
	}
	
	
	/**
	 * Monta os mapas de separa��o de acordo com o esquema.
	 * 
	 * Visualizar caso de uso GerarMapaSeparacao.
	 * 
	 * O mapa gerado � salvo no banco.
	 * 
	 * @see #salvarOS()
	 */
	private void makeMapas(){
		if (listaMapa == null || listaMapa.isEmpty())
			throw new WmsException("N�o foi encontrado nenhum item para gerar separa��o. Verifique os carregamentos.");
		
		this.quebrarpormapa = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.QUEBRAR_POR_CARREGAMENTO, WmsUtil.getDeposito());
		
		for (int i = 0; i < listaMapa.size(); i++) {
			IMapaseparacao item = listaMapa.get(i);
			if(item.getLinhaseparacao() == null)
				throw new WmsException("N�o foi poss�vel gerar a separa��o, pois existem produtos sem linha de separa��o cadastrada nesta carga.");
			
			//caso seja o primeiro, inicializar os mapas.
			if(i==0){
				createNewMap(item);
			} else {
				//n�o � mais o primeiro item do mapa.
				
				if (!carregamentos.contains(item.getCarregamento()))
					carregamentos.add(item.getCarregamento());
				
				if(quebrarpormapa && lastCarregamento != null && !lastCarregamento.equals(item.getCarregamento())){
					createNewMap(item);
				}else if(item.getLinhaseparacao().equals(lastVmapaseparacao.getLinhaseparacao())){//verifica se este produto est� na mesma linha do anterior.
					addProduto(item);
				}else {
					//possui linha de separa��o diferente, criar um novo mapa.
					createNewMap(item);
				}
			}
			lastCarregamento = item.getCarregamento();
		}
		salvarOS();
	}
	
	/**
	 * Gera uma nova ordem de servi�o. 
	 * Ao criar, � adicionado o produto atual a ordem de servi�o criada.
	 * 
	 * @see #addProduto(Vmapaseparacao) 
	 * 
	 * @param vmapaseparacaobox
	 */
	private void createNewMap(IMapaseparacao vmapaseparacaobox){
		if(listaOS == null)
			listaOS = new ArrayList<Ordemservico>();
		
		listaEtiquetaExpedicao.add(new ArrayList<Etiquetaexpedicao>());
		listaEtiquetaExpedicaoGrupo.add(new ArrayList<Etiquetaexpedicaogrupo>());
		
		if (!carregamentos.contains(vmapaseparacaobox.getCarregamento()))
			carregamentos.add(vmapaseparacaobox.getCarregamento());

		Ordemservico ordemservico = new Ordemservico();
		ordemservico.setDeposito(WmsUtil.getDeposito());
		ordemservico.setExpedicao(this.expedicaoPrincipal);
		if(this.quebrarpormapa)
			ordemservico.setCarregamento(vmapaseparacaobox.getCarregamento());
		int size = listaOS.size();
		
		size ++;
		
		ordemservico.setOrdem(size);
		ordemservico.setOrdemtipo(Ordemtipo.MAPA_SEPARACAO);
		
		ordemservico.setListaOrdemProdutoHistorico(new ArrayList<Ordemprodutohistorico>());
		listaOS.add(ordemservico);
		addProduto(vmapaseparacaobox);
	}
	
	/**
	 * Cria uma nova ordemservicoproduto, e seta este � �ltima ordem de servi�o.
	 * 
	 * � setado o par�metro lastVmapaseparacao, que armazena o valor anterior.
	 * 
	 * Quando o produto j� encontra-se na osp, � adicionado +1 na ordem.
	 * @see #addProduto(Vmapaseparacao)
	 * 
	 * @param mapaseparacaobox
	 */
	private void addProduto(IMapaseparacao mapaseparacaobox){
		//pega sempre o �ltimo mapa, � incremental
		Ordemservico ordemservico = listaOS.get(listaOS.size() - 1);
		Ordemservicoproduto ordemservicoproduto= new Ordemservicoproduto();
		
		Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
		ordemprodutoligacao.setOrdemservico(ordemservico);
		if(mapaseparacaobox instanceof Vmapaseparacaoboxcm){
			Vmapaseparacaoboxcm vmapaseparacaoboxcm = (Vmapaseparacaoboxcm) mapaseparacaobox;
			ordemservicoproduto.setProdutoembalagem(vmapaseparacaoboxcm.getProdutoembalagem());	
		}
		
		ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
		
		ordemservicoproduto.setProduto(mapaseparacaobox.getProduto());
		
		if(!addQuantidadeEsperada(ordemservico, mapaseparacaobox.getProduto(), mapaseparacaobox)){
			ordemservicoproduto.setQtdeesperada(mapaseparacaobox.getQtde());
			ordemservico.getListaOrdemProdutoLigacao().add(ordemprodutoligacao);
			createEtiquetaExpedicao(mapaseparacaobox, ordemservicoproduto);
		}
		
		//seta o mapa atual..
		this.lastVmapaseparacao = mapaseparacaobox;
	}

	/**
	 * Cria a etiqueta de expedi��o
	 * 
	 * @param vmapaseparacao
	 * @param ordemservicoproduto
	 */
	private void createEtiquetaExpedicao(IMapaseparacao mapaseparacaobox,Ordemservicoproduto ordemservicoproduto) {
		Etiquetaexpedicao etiquetaexpedicao = new Etiquetaexpedicao();
		etiquetaexpedicao.setCarregamentoitem(mapaseparacaobox.getCarregamentoitem());
		etiquetaexpedicao.setOrdemservicoproduto(ordemservicoproduto);
		ordemservicoproduto.getListaEtiquetaexpedicao().add(etiquetaexpedicao);
		listaEtiquetaExpedicao.get(listaEtiquetaExpedicao.size() - 1).add(etiquetaexpedicao);
	}
	
	/**
	 * Adiciona 1 na ordem de servi�o produto quando o mesmo produto j� foi registrado.
	 * 
	 * @param ordemservico
	 * @param produto
	 * @param mapaseparacaobox 
	 * @return true - caso a quantidade recebida foi alterada.
	 * 		   false - caso a quantidade recebida n�o foi alterada.
	 */
	private boolean addQuantidadeEsperada(Ordemservico ordemservico,Produto produto, IMapaseparacao mapaseparacaobox){
		for (Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()) {
			Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
			if(ordemservicoproduto.getProduto().equals(produto)){
				ordemservicoproduto.setQtdeesperada(ordemservicoproduto.getQtdeesperada() + mapaseparacaobox.getQtde());
				if(mapaseparacaobox.getTipooperacao().getImprimeetiqueta()){
					createEtiquetaExpedicao(mapaseparacaobox, ordemservicoproduto);
				}else{
					Etiquetaexpedicaogrupo etiquetaexpedicaogrupo = new Etiquetaexpedicaogrupo();
					etiquetaexpedicaogrupo.setCarregamentoitem(mapaseparacaobox.getCarregamentoitem());
					etiquetaexpedicaogrupo.setOrdemservicoproduto(ordemservicoproduto);
				
					//Como esta etiquetaexpedicaogrupo foi criada para os casos onde dois carregamentoitem geram uma �nica etiqueta
					//e este caso sempre ocorre com TPO que n�o gera etiqueta, ent�o vou pegar sempre a primeria, pois s� haver� ela
					Etiquetaexpedicao etiquetaexpedicao = ordemservicoproduto.getListaEtiquetaexpedicao().iterator().next();
					etiquetaexpedicaogrupo.setEtiquetaexpedicao(etiquetaexpedicao);
					etiquetaexpedicao.getListaEtiquetaexpedicaogrupo().add(etiquetaexpedicaogrupo);
					
					ordemservicoproduto.getListaEtiquetaexpedicaogrupo().add(etiquetaexpedicaogrupo);
					listaEtiquetaExpedicaoGrupo.get(listaEtiquetaExpedicaoGrupo.size() - 1).add(etiquetaexpedicaogrupo);
				}
				
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Salva as ordens de servi�o geradas no banco de dados.
	 * 
	 * Os dados s�o salvos dentro de uma transaction.
	 */
	private void salvarOS() {
		int i = 0;
		if (listaOS != null) {
			for (Ordemservico ordemservico : listaOS) {
				// salva a ordem de servi�o.
				for (Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()) {
					Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
					ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
					OrdemservicoprodutoService.getInstance().saveOrUpdateNoUseTransaction(ordemservicoproduto);

					// OrdemservicoprodutoenderecoService.getInstance().saveForMapas(ordemservicoproduto);//Faz
					// o endere�amento da ordem de servico do produto

					for (Etiquetaexpedicao etiquetaexpedicao : ordemservicoproduto.getListaEtiquetaexpedicao()) {
						EtiquetaexpedicaoService.getInstance().saveOrUpdateNoUseTransaction(etiquetaexpedicao);
					}

					for (Etiquetaexpedicaogrupo etiquetaexpedicaogrupo : ordemservicoproduto.getListaEtiquetaexpedicaogrupo()) {
						EtiquetaexpedicaogrupoService.getInstance().saveOrUpdateNoUseTransaction(etiquetaexpedicaogrupo);
					}
				}
				ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
				OrdemservicoService.getInstance().saveOrUpdateNoUseTransaction(ordemservico);
				mapaEtiquetasExpedicao.put(ordemservico,listaEtiquetaExpedicao.get(i));
				i++;
			}

			for (Carregamento carregamento : carregamentos){
				carregamento.setCarregamentostatus(Carregamentostatus.EM_SEPARACAO);
				CarregamentoService.getInstance().atualizaStatusCarregamento(carregamento, Carregamentohistorico.CRIA_EXPEDICAO, WmsUtil.getUsuarioLogado());
				CarregamentohistoricoService.getInstance().criaHistorico(carregamento, null, WmsUtil.getUsuarioLogado());
			}

			for (Box box : listaBox)
				GeradorConferenciaExpedicaoBox.criarConferencias(box, false, this.expedicaoVO);
		}
	}
}
