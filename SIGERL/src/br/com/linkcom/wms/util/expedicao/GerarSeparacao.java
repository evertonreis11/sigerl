package br.com.linkcom.wms.util.expedicao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentohistorico;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicaogrupo;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Vmapaseparacao;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaocm;
import br.com.linkcom.wms.geral.bean.auxiliar.IMapaseparacao;
import br.com.linkcom.wms.geral.service.CarregamentoService;
import br.com.linkcom.wms.geral.service.CarregamentoitemService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaogrupoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.TipooperacaoService;
import br.com.linkcom.wms.geral.service.VmapaseparacaoService;
import br.com.linkcom.wms.geral.service.VmapaseparacaocmService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

/**
 * Algoritmo para a geração dos mapas de separação.
 * 
 * @author Pedro Gonçalves
 */
public class GerarSeparacao {
	private List<Ordemservico> listaOS;
	private Carregamento carregamento;
	private List<? extends IMapaseparacao> listaMapa;
	private List<List<Etiquetaexpedicao>> listaEtiquetaExpedicao = new ArrayList<List<Etiquetaexpedicao>>();
	private List<List<Etiquetaexpedicaogrupo>> listaEtiquetaExpedicaoGrupo = new ArrayList<List<Etiquetaexpedicaogrupo>>();
	private Map<Ordemservico, List<Etiquetaexpedicao>> mapaEtiquetasExpedicao = new HashMap<Ordemservico, List<Etiquetaexpedicao>>();
	private IMapaseparacao lastMapaseparacao;
	
	/**
	 * Gera um novo mapa de separação.
	 * @param carregamento
	 */
	public GerarSeparacao(Carregamento carregamento) {
		this.carregamento = carregamento;
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
			this.listaMapa = VmapaseparacaocmService.getInstance().findAllBy(carregamento);
		else
			this.listaMapa = VmapaseparacaoService.getInstance().findAllBy(carregamento);
	}
	
	
	/**
	 * Monta os mapas de separação de acordo com o esquema.
	 * 
	 * Visualizar caso de uso GerarMapaSeparacao.
	 * 
	 * O mapa gerado é salvo no banco.
	 * 
	 * @see #salvarOS()
	 */
	private void makeMapas(){

		for (int i = 0; i < listaMapa.size(); i++) {
			IMapaseparacao mapaseparacao = listaMapa.get(i);
			if(mapaseparacao.getLinhaseparacao() == null)
				throw new WmsException("Não foi possível gerar a separação, pois existem produtos sem linha de separação cadastrada nesta carga.");
			//caso seja o primeiro, inicializar os mapas.
			if(i==0){
				createNewMap(mapaseparacao);
			} else {
				//não é mais o primeiro item do mapa.
				
				//verifica se este produto está na mesma linha do anterior.
				if(mapaseparacao.getLinhaseparacao().equals(lastMapaseparacao.getLinhaseparacao())){
					//se estiver na mesma, verificar o tipo.
					if(mapaseparacao.getTipooperacao().equals(lastMapaseparacao.getTipooperacao())){
						//se estiver com o mesmo tipo de separação, verificar se é o mesmo cliente.
						
						
						if(isSeparaPorCliente(mapaseparacao)) { //se for para separar por cliente.
							if(mapaseparacao.getCliente().equals(lastMapaseparacao.getCliente())){
								//é o mesmo cliente. Colocar o item na lista.
								addProduto(mapaseparacao);
							} else {
								//O cliente não é o mesmo do anterior.
								createNewMap(mapaseparacao);
							}
						} else {
							//Adicionar diretamente este produto.
							addProduto(mapaseparacao);
						}
						
					} else {
						//Possui tipos de operação diferente, criar um novo mapa.
						createNewMap(mapaseparacao);
					}
				} else {
					//possui linha de separação diferente, criar um novo mapa.
					createNewMap(mapaseparacao);
				}
			}
			
		}
		salvarOS();
	}
	
	/**
	 * Verifica se será efeturada a separação por cliente.
	 * 
	 * @see br.com.linkcom.wms.util.WmsUtil#getConfig(String key)
	 * @param vmapaseparacao 
	 * @return
	 */
	private boolean isSeparaPorCliente(IMapaseparacao mapaseparacao){
		Tipooperacao tipooperacao = TipooperacaoService.getInstance().findByCd(mapaseparacao.getTipooperacao().getCdtipooperacao());
		return tipooperacao.getSeparacliente();
	}
	
	/**
	 * Gera uma nova ordem de serviço. 
	 * Ao criar, é adicionado o produto atual a ordem de serviço criada.
	 * 
	 * @see #addProduto(Vmapaseparacao) 
	 * 
	 * @param vmapaseparacao
	 */
	private void createNewMap(IMapaseparacao mapaseparacao){
		if(listaOS == null)
			listaOS = new ArrayList<Ordemservico>();
		
		listaEtiquetaExpedicao.add(new ArrayList<Etiquetaexpedicao>());
		listaEtiquetaExpedicaoGrupo.add(new ArrayList<Etiquetaexpedicaogrupo>());

		Ordemservico ordemservico = new Ordemservico();
		ordemservico.setCarregamento(carregamento);
		ordemservico.setDeposito(WmsUtil.getDeposito());
		ordemservico.setTipooperacao(new Tipooperacao(mapaseparacao.getTipooperacao().getCdtipooperacao()));
		ordemservico.setClienteExpedicao(new Cliente(mapaseparacao.getCliente().getCdpessoa()));
		
		int size = listaOS.size();
		
		size ++;
		
		ordemservico.setOrdem(size);
		ordemservico.setOrdemtipo(Ordemtipo.MAPA_SEPARACAO);
		
		ordemservico.setListaOrdemProdutoHistorico(new ArrayList<Ordemprodutohistorico>());
		listaOS.add(ordemservico);
		addProduto(mapaseparacao);
	}
	
	/**
	 * Cria uma nova ordemservicoproduto, e seta este à última ordem de serviço.
	 * 
	 * é setado o parâmetro lastVmapaseparacao, que armazena o valor anterior.
	 * 
	 * Quando o produto já encontra-se na osp, é adicionado +1 na ordem.
	 * @see #addProduto(Vmapaseparacao)
	 * 
	 * @param vmapaseparacao
	 */
	private void addProduto(IMapaseparacao mapaseparacao){
		//pega sempre o último mapa, é incremental
		Ordemservico ordemservico = listaOS.get(listaOS.size() - 1);
		Produto produto = new Produto();
		produto.setCdproduto(mapaseparacao.getProduto().getCdproduto());
		
		Ordemservicoproduto ordemservicoproduto= new Ordemservicoproduto();
		
		Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
		ordemprodutoligacao.setOrdemservico(ordemservico);
		ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
		
		ordemservicoproduto.setProduto(produto);
		
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito()) && mapaseparacao instanceof Vmapaseparacaocm){
			Vmapaseparacaocm vmapaseparacaocm = (Vmapaseparacaocm) mapaseparacao;
			ordemservicoproduto.setQtdeesperada(mapaseparacao.getQtde());
			ordemservicoproduto.setProdutoembalagem(vmapaseparacaocm.getProdutoembalagem());
			ordemservico.getListaOrdemProdutoLigacao().add(ordemprodutoligacao);
			createEtiquetaExpedicao(mapaseparacao, ordemservicoproduto);
		}else if(!addQuantidadeEsperada(ordemservico,produto,mapaseparacao)){
			ordemservicoproduto.setQtdeesperada(mapaseparacao.getQtde());
			ordemservico.getListaOrdemProdutoLigacao().add(ordemprodutoligacao);
			createEtiquetaExpedicao(mapaseparacao, ordemservicoproduto);
		}
		
		//seta o mapa atual..
		this.lastMapaseparacao = mapaseparacao;
	}

	/**
	 * Cria a etiqueta de expedição
	 * 
	 * @param vmapaseparacao
	 * @param ordemservicoproduto
	 */
	private void createEtiquetaExpedicao(IMapaseparacao mapaseparacao,Ordemservicoproduto ordemservicoproduto) {
		Carregamentoitem ci = CarregamentoitemService.getInstance().loadForSeparacao(new Carregamentoitem(mapaseparacao.getCarregamentoitem().getCdcarregamentoitem()));
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito())){
			for (int i=0;i<ordemservicoproduto.getQtdeesperada();i++){
				Etiquetaexpedicao etiquetaexpedicao = new Etiquetaexpedicao();
				etiquetaexpedicao.setCarregamentoitem(ci);
				etiquetaexpedicao.setOrdemservicoproduto(ordemservicoproduto);
				ordemservicoproduto.getListaEtiquetaexpedicao().add(etiquetaexpedicao);
				listaEtiquetaExpedicao.get(listaEtiquetaExpedicao.size() - 1).add(etiquetaexpedicao);
			}
		}else{
			Etiquetaexpedicao etiquetaexpedicao = new Etiquetaexpedicao();
			etiquetaexpedicao.setCarregamentoitem(ci);
			etiquetaexpedicao.setOrdemservicoproduto(ordemservicoproduto);
			ordemservicoproduto.getListaEtiquetaexpedicao().add(etiquetaexpedicao);
			listaEtiquetaExpedicao.get(listaEtiquetaExpedicao.size() - 1).add(etiquetaexpedicao);
		}
	}
	
	/**
	 * Adiciona 1 na ordem de serviço produto quando o mesmo produto já foi registrado.
	 * 
	 * @param ordemservico
	 * @param produto
	 * @param vmapaseparacao 
	 * @return true - caso a quantidade recebida foi alterada.
	 * 		   false - caso a quantidade recebida não foi alterada.
	 */
	private boolean addQuantidadeEsperada(Ordemservico ordemservico,Produto produto, IMapaseparacao mapaseparacao){
		for (Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()) {
			Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
			if(ordemservicoproduto.getProduto().equals(produto)){
				ordemservicoproduto.setQtdeesperada(ordemservicoproduto.getQtdeesperada() + mapaseparacao.getQtde());
				if(TipooperacaoService.getInstance().imprimeEtiqueta(new Tipooperacao(mapaseparacao.getTipooperacao().getCdtipooperacao()))){
					createEtiquetaExpedicao(mapaseparacao, ordemservicoproduto);
				}else{
					Etiquetaexpedicaogrupo etiquetaexpedicaogrupo = new Etiquetaexpedicaogrupo();
					etiquetaexpedicaogrupo.setCarregamentoitem(CarregamentoitemService.getInstance().loadForSeparacao(new Carregamentoitem(mapaseparacao.getCarregamentoitem().getCdcarregamentoitem())));
					etiquetaexpedicaogrupo.setOrdemservicoproduto(ordemservicoproduto);
				
					//Como esta etiquetaexpedicaogrupo foi criada para os casos onde dois carregamentoitem geram uma única etiqueta
					//e este caso sempre ocorre com TPO que não gera etiqueta, então vou pegar sempre a primeria, pois só haverá ela
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
	 * Salva as ordens de serviço geradas no banco de dados.
	 * 
	 * Os dados são salvos dentro de uma transaction.
	 */
	private void salvarOS() {
		int i = 0;
		if (listaOS != null) {
			for (Ordemservico ordemservico : listaOS) {
				// salva a ordem de serviço.
				for (Ordemprodutoligacao ordemprodutoligacao : ordemservico.getListaOrdemProdutoLigacao()) {
					Ordemservicoproduto ordemservicoproduto = ordemprodutoligacao.getOrdemservicoproduto();
					ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
					OrdemservicoprodutoService.getInstance().saveOrUpdateNoUseTransaction(ordemservicoproduto);

					// OrdemservicoprodutoenderecoService.getInstance().saveForMapas(ordemservicoproduto);//Faz
					// o endereçamento da ordem de servico do produto

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

			carregamento.setCarregamentostatus(Carregamentostatus.EM_SEPARACAO);
			CarregamentoService.getInstance().atualizaStatusCarregamento(carregamento, Carregamentohistorico.CRIA_EXPEDICAO, WmsUtil.getUsuarioLogado());

			new GerarOrdemConferenciaExpedicao().execute(listaOS, carregamento);
		}
	}
}
