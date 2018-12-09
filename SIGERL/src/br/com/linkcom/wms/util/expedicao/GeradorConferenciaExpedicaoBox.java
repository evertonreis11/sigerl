package br.com.linkcom.wms.util.expedicao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaoboxcm;
import br.com.linkcom.wms.geral.bean.auxiliar.IOrdemexpedicaobox;
import br.com.linkcom.wms.geral.bean.vo.ExpedicaoVO;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService;
import br.com.linkcom.wms.geral.service.OrdemprodutoligacaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.Vordemexpedicaobox43Service;
import br.com.linkcom.wms.geral.service.VordemexpedicaoboxcmService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

/**
 * Algoritmo para gerar a ordem de conferência da expedição
 * 
 * @author Giovane Freitas
 * 
 */
public class GeradorConferenciaExpedicaoBox {


	/**
	 * Cria a ordem de conferência
	 * 
	 */
	public static void criarConferencias(final Box box, boolean simulacao, ExpedicaoVO expedicaoVO) {
		
		ConfiguracaoService configuracaoService = ConfiguracaoService.getInstance();
		
		//Não é preciso usar transaction template porque este código já está em uma
		// TransactionTemplate transactionTemplate = Neo.getObject(TransactionTemplate.class);
		// transactionTemplate.execute(new TransactionCallback(){

		if (box == null || box.getCdbox() == null )
			throw new WmsException("O box não deve ser nulo.");
		
		//Criando grupos de itens que formarão as Ordens de Serviço, para fazer o agrupamento como o usuário escolheu
		Map<String, Integer> chaveGrupoMap = new HashMap<String, Integer>(); 
		if (expedicaoVO != null){
			for (Box b : expedicaoVO.getListaBoxes()){
				if (!b.getCdbox().equals(box.getCdbox()))
					continue;
				
				for (Carregamento c : b.getListaCarregamentos()){
					for (Ordemservico os : c.getListaConferencias()){
						String[] chaves = os.getItensOrdem().split(",");
						for (String chave : chaves)
							chaveGrupoMap.put(chave, os.getOrdem());
					}
				}
			}
		}
		
		//Map que armazenará a ordem de serviço gerada para cada O.S. definida pelo usuário
		Map<Integer, Ordemservico> grupoOrdemservico = new HashMap<Integer, Ordemservico>();
		
		List<? extends IOrdemexpedicaobox> listaVOrdemExpedicao = null;
		
		if(configuracaoService.isTrue(ConfiguracaoVO.UTILIZAR_CAIXA_MESTRE, WmsUtil.getDeposito())){
			listaVOrdemExpedicao = VordemexpedicaoboxcmService.getInstance().findByBox(box);	
		}else{
			listaVOrdemExpedicao = Vordemexpedicaobox43Service.getInstance().findByBox(box);
		}
				
		if (listaVOrdemExpedicao == null)
			return;

		Ordemservico ordemservico = null;
		Ordemservicoproduto ordemservicoproduto = null;
		
		Map<Integer, Carregamento> carregamentosMap = new HashMap<Integer, Carregamento>();
		for (Carregamento c : box.getListaCarregamentos())
			carregamentosMap.put(c.getCdcarregamento(), c);

		boolean exigeSegundaConferencia = configuracaoService.isTrue(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA, WmsUtil.getDeposito());
		
		for (IOrdemexpedicaobox item : listaVOrdemExpedicao) {
			
			if (chaveGrupoMap.containsKey(item.getChave())){
				Integer numOS = chaveGrupoMap.get(item.getChave());
				if (grupoOrdemservico.containsKey(numOS)){
					ordemservico = grupoOrdemservico.get(numOS);
				} else if (criarNovaOrdem(ordemservico, item)){
					ordemservico = null;
					ordemservicoproduto = null;
				}
			} else {
				if (criarNovaOrdem(ordemservico, item)){
					ordemservico = null;
					ordemservicoproduto = null;
				}
			}
			
			if (ordemservico == null){
				
				//Criando a ordem de conferência
				ordemservico = new Ordemservico();
				ordemservico.setClienteExpedicao(item.getCliente());
				ordemservico.setTipooperacao(item.getTipooperacao());
				ordemservico.setDeposito(WmsUtil.getDeposito());
				ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
				
				if (item.getLinhaseparacao().getUsacheckout()){
					ordemservico.setOrdemtipo(Ordemtipo.CONFERENCIA_CHECKOUT);
				} else {
					ordemservico.setOrdemtipo(Ordemtipo.CONFERENCIA_EXPEDICAO_1);
				}
				
				if (box.getListaExpedicoes() != null && !box.getListaExpedicoes().isEmpty()){
					ordemservico.setExpedicao(box.getListaExpedicoes().iterator().next());
			    }else{
			    	ordemservico.setExpedicao(new Expedicao());
			    }
				
				ordemservico.setCarregamento(item.getCarregamento());
				
				Carregamento carregamento = carregamentosMap.get(item.getCarregamento().getCdcarregamento());
				carregamento.getListaOrdemservico().add(ordemservico);

				if (chaveGrupoMap.containsKey(item.getChave())){
					Integer numOS = chaveGrupoMap.get(item.getChave());
					ordemservico.setOrdem(numOS);
				}else
					ordemservico.setOrdem(carregamento.getListaOrdemservico().size());
				
				if (!simulacao)
					OrdemservicoService.getInstance().saveOrUpdate(ordemservico);
				
				
				if (!item.getLinhaseparacao().getUsacheckout() && exigeSegundaConferencia){
					//Criando a ordem de conferência de box.
					Ordemservico conferenciaBox = new Ordemservico();
					conferenciaBox.setClienteExpedicao(ordemservico.getClienteExpedicao());
					conferenciaBox.setTipooperacao(ordemservico.getTipooperacao());
					conferenciaBox.setDeposito(WmsUtil.getDeposito());
					conferenciaBox.setOrdemstatus(Ordemstatus.EM_ABERTO);
					conferenciaBox.setOrdemtipo(Ordemtipo.CONFERENCIA_EXPEDICAO_2);
					conferenciaBox.setExpedicao(ordemservico.getExpedicao());
					conferenciaBox.setCarregamento(ordemservico.getCarregamento());
					conferenciaBox.setOrdem(ordemservico.getOrdem());
					conferenciaBox.setOrdemservicoprincipal(ordemservico);
					
					if (!simulacao)
						OrdemservicoService.getInstance().saveOrUpdateNoUseTransaction(conferenciaBox);
				}

			}
			
			if (ordemservico.getItensOrdem() == null)
				ordemservico.setItensOrdem(item.getChave());
			else
				ordemservico.setItensOrdem(ordemservico.getItensOrdem() + "," + item.getChave());
			
			if (chaveGrupoMap.containsKey(item.getChave())){
				Integer numOS = chaveGrupoMap.get(item.getChave());
				if (!grupoOrdemservico.containsKey(numOS)){
					grupoOrdemservico.put(numOS, ordemservico);
				}
			}
			
			//Somente gera os itens da ordem de serviço se não é uma simulação
			if (!simulacao){

				Produto produto = item.getVolume() == null ? item.getProdutoprincipal() : item.getVolume();
				Produtoembalagem produtoembalagem = null;
				
				if(item instanceof Vordemexpedicaoboxcm){
					Vordemexpedicaoboxcm voecm = (Vordemexpedicaoboxcm) item;
					produtoembalagem = (voecm.getProdutoembalagem() == null || voecm.getProdutoembalagem().getCdprodutoembalagem() == null) ? null : voecm.getProdutoembalagem();
				}
				
				if (ordemservicoproduto == null || 
						(!ordemservicoproduto.getProduto().getCdproduto().equals(produto.getCdproduto())) || 
						(produtoembalagem!=null && !ordemservicoproduto.getProdutoembalagem().getCdprodutoembalagem().equals(produtoembalagem.getCdprodutoembalagem())) ){
					
					//O OrdemServicoProduto só deve ser criado quando troca de produto.
					ordemservicoproduto = new Ordemservicoproduto();
					ordemservicoproduto.setCdordemservicoproduto(null);
					ordemservicoproduto.setProduto(produto);
					ordemservicoproduto.setQtdeesperada(item.getQtde());
					ordemservicoproduto.setProdutoembalagem(produtoembalagem);
					ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
					OrdemservicoprodutoService.getInstance().saveOrUpdate(ordemservicoproduto);
		
					Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
					ordemprodutoligacao.setOrdemservico(ordemservico);
					ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
					OrdemprodutoligacaoService.getInstance().saveOrUpdate(ordemprodutoligacao);
				} else {
					ordemservicoproduto.setQtdeesperada(ordemservicoproduto.getQtdeesperada() + item.getQtde());
					OrdemservicoprodutoService.getInstance().saveOrUpdate(ordemservicoproduto);
				}
				
				for (int i = 0; i < item.getQtde(); i++) {
					Etiquetaexpedicao etiqueta = new Etiquetaexpedicao();
					etiqueta.setOrdemservicoproduto(ordemservicoproduto);
					etiqueta.setCarregamentoitem(item.getCarregamentoitem());
					EtiquetaexpedicaoService.getInstance().saveOrUpdateNoUseTransaction(etiqueta);
				}
			}
			
		}
	}

	/**
	 * Verifica se está na hora de criar uma nova ordem de serviço, de acordo com as regras definidas no caso de uso.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param item
	 * @return
	 */
	private static boolean criarNovaOrdem(Ordemservico ordemservico, IOrdemexpedicaobox item) {
		//Se a ordem é nula então deve criar uma nova
		if (ordemservico == null)
			return true;
		
		//Se trocou o carregamento então deve cria uma nova ordem
		if(ordemservico.getCarregamento() != null && ordemservico.getCarregamento().getCdcarregamento() != null && !ordemservico.getCarregamento().equals(item.getCarregamento()))
			return true;
		
		//Se a ordem não é de checkout e o item usa checkout ou vice-versa então deve criar uma nova
		boolean isConferenciaCheckout = ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_CHECKOUT);
		if (isConferenciaCheckout != item.getLinhaseparacao().getUsacheckout())
			return true;
		
		boolean mudouCliente = false;
		boolean mudouTipo = false; 
		
		if(item.getTipooperacao().getSeparacliente()){
			if (item.getCliente() != null && ordemservico.getClienteExpedicao() == null)
				mudouCliente = true;
			else if (item.getCliente() != null && ordemservico.getClienteExpedicao() != null)
				mudouCliente = !item.getCliente().getCdpessoa().equals(ordemservico.getClienteExpedicao().getCdpessoa());
			else if (item.getCliente() == null && ordemservico.getClienteExpedicao() != null)
				mudouCliente = true;			
		}
		if(item.getTipooperacao()!=null && item.getTipooperacao().getCdtipooperacao()!=null)
			mudouTipo =  !item.getTipooperacao().getCdtipooperacao().equals(ordemservico.getTipooperacao().getCdtipooperacao());

		return mudouCliente || mudouTipo;
	}

}