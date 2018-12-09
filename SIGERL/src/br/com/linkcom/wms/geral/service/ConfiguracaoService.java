package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Configuracao;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.dao.ConfiguracaoDAO;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ConfiguracaoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ConfiguracaoService extends GenericService<Configuracao>{
	
	private ConfiguracaoDAO configuracaoDAO;
	private TipooperacaoService tipooperacaoService;
	private HashMap<Deposito, HashMap<String,String>> confDeposito = new HashMap<Deposito, HashMap<String,String>>();
	private HashMap<String, String> configCache = new HashMap<String, String>();
	private NotafiscaltipoService notafiscaltipoService;
	
	public void setNotafiscaltipoService(
			NotafiscaltipoService notafiscaltipoService) {
		this.notafiscaltipoService = notafiscaltipoService;
	}
	public void setConfiguracaoDAO(ConfiguracaoDAO configuracaoDAO) {
		this.configuracaoDAO = configuracaoDAO;
	}
	public void setTipooperacaoService(TipooperacaoService tipooperacaoService) {
		this.tipooperacaoService = tipooperacaoService;
	}
	
	/**
	 * Método que retorna uma lista com as siglas que serão colocadas no banco
	 * @author Leonardo Guimarães
	 * @return
	 */
	public List<String> getNomesForBanco() {
		List<String> lista = new ArrayList<String>();
		lista.add(ConfiguracaoVO.PERCENTUAL_RETENCAO_DESCAGA);
		lista.add(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_LOJA);
		lista.add(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_MOSTRUARIO);
		lista.add(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_CLIENTE);
		lista.add(ConfiguracaoVO.TIPO_SEPARACAO_LOJA);
		lista.add(ConfiguracaoVO.TIPO_SEPARACAO_MOSTRUARIO);
		lista.add(ConfiguracaoVO.TIPO_SEPARACAO_CLIENTE);
		lista.add(ConfiguracaoVO.LINHA_SEPARACAO_PADRAO);
		lista.add(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS);
		lista.add(ConfiguracaoVO.PORTAPALETE_EXIGE_PICKING);
		lista.add(ConfiguracaoVO.OPERACAO_CD_CD_DUPLICA_PEDIDO);
		lista.add(ConfiguracaoVO.ENVIAR_RECEBIMENTO_ENDERECADO);
		lista.add(ConfiguracaoVO.MAX_ENDERECOS_CRIADOS_POR_VEZ);
		lista.add(ConfiguracaoVO.ENDERECAMENTO_AUTOMATICO);
		lista.add(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA);
		lista.add(ConfiguracaoVO.QUEBRAR_POR_CARREGAMENTO);
		lista.add(ConfiguracaoVO.OPERACAO_EXPEDICAO_POR_BOX);
		lista.add(ConfiguracaoVO.UTILIZAR_CAIXA_MESTRE);
		lista.add(ConfiguracaoVO.SEGUNDANOTA_CDCLIENTE_ENTREGA);
		lista.add(ConfiguracaoVO.BORDERO_RATEIRO_EMPRESA);		

		return lista;
	}

	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.ConfiguracaoDAO#findByDeposito(Deposito deposito)
	 * @param deposito
	 * @return
	 */
	public List<Configuracao> findByDeposito(Deposito deposito){
		return configuracaoDAO.findByDeposito(deposito);
	}
	
	/**
	 * Método que seta um ConfiguracaoVO a partir de 
	 * uma lista de Configuracao
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ConfiguracaoDAO#findByDeposito(Deposito deposito)
	 * 
	 * @param deposito
	 * @return
	 */
	public ConfiguracaoVO fillConfiguracaoVO(Deposito deposito) {
		if(deposito == null || deposito.getCddeposito() == null){
			throw new WmsException("O depósito não deve ser nulo");
		}
		List<Configuracao> listaconfiguracao = findByDeposito(deposito);
		
		ConfiguracaoVO e = new ConfiguracaoVO();
		if(listaconfiguracao != null && !listaconfiguracao.isEmpty()){
			
			int size = listaconfiguracao.size();
			for(int i = 0;i < size; i++){
				
				Configuracao configuracao = listaconfiguracao.get(i);
				
				if(configuracao.getNome().equals(ConfiguracaoVO.PERCENTUAL_RETENCAO_DESCAGA))						
					e.setPercentualDescarga(configuracao.getValor() != null ? Double.valueOf(configuracao.getValor()) : 15.0);
				
				if(configuracao.getNome().equals(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_LOJA))
					e.setSepararPorClienteLoja(WmsUtil.getBoolean(configuracao.getValor()));
				
				else if(configuracao.getNome().equals((ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_MOSTRUARIO)))
					e.setSepararPorClienteMostruario(WmsUtil.getBoolean(configuracao.getValor()));
				
				else if(configuracao.getNome().equals(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_CLIENTE))
					e.setSepararPorClienteCliente(WmsUtil.getBoolean(configuracao.getValor()));
				
				else if(configuracao.getNome().equals(ConfiguracaoVO.TIPO_SEPARACAO_LOJA)){
						if(configuracao.getValor() != null)
							e.setTipoSeparacaoLoja(WmsUtil.getBoolean(configuracao.getValor()));
					}
				
				else if(configuracao.getNome().equals(ConfiguracaoVO.TIPO_SEPARACAO_MOSTRUARIO)){
						if(configuracao.getValor() != null)
							e.setTipoSeparacaoMostruario(WmsUtil.getBoolean(configuracao.getValor()));
					}
				
				else if(configuracao.getNome().equals(ConfiguracaoVO.TIPO_SEPARACAO_CLIENTE)){
					if(configuracao.getValor() != null)
						e.setTipoSeparacaoCliente(WmsUtil.getBoolean(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.ETIQUETA_SEPARACAO))
					setEtiquetaSeparacao(e, configuracao);
				else if(configuracao.getNome().equals(ConfiguracaoVO.MAX_ENDERECOS_CRIADOS_POR_VEZ)){
					if(configuracao.getValor() != null)
						e.setMaxEnderecos(Long.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.LINHA_SEPARACAO_PADRAO)){
					if(configuracao.getValor() != null)
						e.setLinhaseparacaoPadrao(LinhaseparacaoService.getInstance().load(new Linhaseparacao(Integer.valueOf(configuracao.getValor()))));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.ENDERECAMENTO_AUTOMATICO)){
					if(configuracao.getValor() != null)
						e.setEnderecamentoAutomatico(WmsUtil.getBoolean(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS)){
					if(configuracao.getValor() != null)
						e.setColetorExigeCodigoBarras(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.PORTAPALETE_EXIGE_PICKING)){
					if(configuracao.getValor() != null)
						e.setPortapaleteExigePicking(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.OPERACAO_CD_CD_DUPLICA_PEDIDO)){
					if(configuracao.getValor() != null)
						e.setOperacaoCdCdDuplicaPedido(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.ENVIAR_RECEBIMENTO_ENDERECADO)){
					if(configuracao.getValor() != null)
						e.setEnviarRecebimentosEnderecados(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA)){
					if(configuracao.getValor() != null)
						e.setExigirSegundaConferencia(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.QUEBRAR_POR_CARREGAMENTO)){
					if(configuracao.getValor() != null)
						e.setQuebrarCarregamento(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.OPERACAO_EXPEDICAO_POR_BOX)){
					if(configuracao.getValor() != null)
						e.setOperacaoExpedicaoBox(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.UTILIZAR_CAIXA_MESTRE)){
					if(configuracao.getValor() != null)
						e.setUtilizarCaixaMestre(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.SEGUNDANOTA_CDCLIENTE_ENTREGA)){
					if(configuracao.getValor() != null)
						e.setSegundanotaCdclienteEntrega(Boolean.valueOf(configuracao.getValor()));
				}
				else if(configuracao.getNome().equals(ConfiguracaoVO.BORDERO_RATEIRO_EMPRESA)){
					if(configuracao.getValor() != null)
						e.setBorderoRateioEmpresa(Boolean.valueOf(configuracao.getValor()));
				}					
			}
		}
		
		if(e.getListaEtiquetaEspedicao() == null){
			e.setListaEtiquetaEspedicao(new ArrayList<Tipooperacao>());
			e.getListaEtiquetaEspedicao().add(Tipooperacao.ENTREGA_CD_CLIENTE);
		}
			
		return e;
	}
	
	/**
	 * Seta a etiqueta de separação na configuração
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param e
	 * @param configuracao
	 */
	private void setEtiquetaSeparacao(ConfiguracaoVO e,Configuracao configuracao) {
		if(configuracao.getValor() != null)
			e.setListaEtiquetaEspedicao(new ArrayList<Tipooperacao>());
			if(!configuracao.getValor().equals("nulo")){
				try{
					String[] array = configuracao.getValor().split(",");
					for (String string : array) {
						e.getListaEtiquetaEspedicao().add(tipooperacaoService.findByCd(Integer.valueOf(string)));
					}
				}catch (Exception ex) {
				}
			}
	}

	/**
	 * Método que seta 18 beans de Configuração
	 * com os valores contidos em configuraçãoVO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #clearCache()
	 * 
	 * @param filtro
	 */
	public void salvar(ConfiguracaoFiltro filtro){
		if(filtro == null)
			return;
		
		List<Notafiscaltipo> listaNotafiscaltipo = filtro.getListaExigeAgendamento();
		
		String whereInNF = null;
		if(listaNotafiscaltipo != null && listaNotafiscaltipo.size() > 0){
			whereInNF = CollectionsUtil.listAndConcatenate(listaNotafiscaltipo, "cdnotafiscaltipo", ",");
			notafiscaltipoService.atualizaExigeAgendamento(whereInNF);
		}
		
		notafiscaltipoService.atualizaNotExigeAgendamento(whereInNF);
		
		if(filtro.getNumeroDiasCancelamentoAgenda() != null){
			Configuracao numDiasConfig = this.findByName(null, ConfiguracaoVO.NUM_DIAS_AGENDA_CANCELAMENTO);
			numDiasConfig.setValor(filtro.getNumeroDiasCancelamentoAgenda().toString());
			saveOrUpdate(numDiasConfig);
		}
		
		if(filtro.getValidarPedidosAgendamento() != null){
			Configuracao validarPedidosAgendamento = this.findByName(null, ConfiguracaoVO.VALIDAR_PEDIDOS_AGENDAMENTO);
			validarPedidosAgendamento.setValor(filtro.getValidarPedidosAgendamento().toString());
			saveOrUpdate(validarPedidosAgendamento);
		}

		if(filtro.getValidarProdutosAgendamento() != null){
			Configuracao validarProdutosAgendamento = this.findByName(null, ConfiguracaoVO.VALIDAR_PRODUTOS_AGENDAMENTO);
			validarProdutosAgendamento.setValor(filtro.getValidarProdutosAgendamento().toString());
			saveOrUpdate(validarProdutosAgendamento);
		}

		List<Tipooperacao> listaTO = tipooperacaoService.findAll();		

		for (Tipooperacao to : listaTO){
			if (filtro.getListaOperacaoImprimeetiqueta() != null){
				if (filtro.getListaOperacaoImprimeetiqueta().contains(to))
					to.setImprimeetiqueta(true);
				else
					to.setImprimeetiqueta(false);
			}

			if (filtro.getListaOperacaoSeparacliente() != null){
				if (filtro.getListaOperacaoSeparacliente().contains(to))
					to.setSeparacliente(true);
				else
					to.setSeparacliente(false);
			}

			tipooperacaoService.saveOrUpdate(to);
		}
		
		//-------------------------------------------------------------
		//Configuração independente do depósito
		Configuracao configSeparaViaColetor = configuracaoDAO.findByName(null, ConfiguracaoVO.SEPARACAO_VIA_COLETOR);
		if (configSeparaViaColetor == null){
			configSeparaViaColetor = new Configuracao();
			configSeparaViaColetor.setNome(ConfiguracaoVO.SEPARACAO_VIA_COLETOR);
		}
		
		if (filtro.getSeparacaoViaColetor() != null)
			configSeparaViaColetor.setValor(getString(filtro.getSeparacaoViaColetor()));
		else
			configSeparaViaColetor.setValor(getString(false));
			
		saveOrUpdate(configSeparaViaColetor);

		Configuracao configQuebraMapa = configuracaoDAO.findByName(null, ConfiguracaoVO.MAPA_IGNORA_LINHA);
		if (configQuebraMapa == null){
			configQuebraMapa = new Configuracao();
			configQuebraMapa.setNome(ConfiguracaoVO.MAPA_IGNORA_LINHA);
		}
		
		if (filtro.getMapaIgnoraLinha() != null)
			configQuebraMapa.setValor(getString(filtro.getMapaIgnoraLinha()));
		else
			configQuebraMapa.setValor(getString(false));
			
		saveOrUpdate(configQuebraMapa);

		Configuracao configConvocacaoAtiva = configuracaoDAO.findByName(null, ConfiguracaoVO.CONVOCACAO_ATIVA_COLETOR);
		if (configConvocacaoAtiva == null){
			configConvocacaoAtiva = new Configuracao();
			configConvocacaoAtiva.setNome(ConfiguracaoVO.CONVOCACAO_ATIVA_COLETOR);
		}
		
		if (filtro.getConvocacaoAtivaColetor() != null)
			configConvocacaoAtiva.setValor(getString(filtro.getConvocacaoAtivaColetor()));
		else
			configConvocacaoAtiva.setValor(getString(false));
			
		saveOrUpdate(configConvocacaoAtiva);
		
		Configuracao configValidarVerba = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDAR_VERBA_AGENDAMENTO);
		if (configValidarVerba == null){
			configValidarVerba = new Configuracao();
			configValidarVerba.setNome(ConfiguracaoVO.VALIDAR_VERBA_AGENDAMENTO);
		}
		saveOrUpdate(configValidarVerba);

		Configuracao configValidarPedidosAgendamento = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDAR_PEDIDOS_AGENDAMENTO);
		if (configValidarPedidosAgendamento == null){
			configValidarPedidosAgendamento = new Configuracao();
			configValidarPedidosAgendamento.setNome(ConfiguracaoVO.VALIDAR_PEDIDOS_AGENDAMENTO);
		}
		saveOrUpdate(configValidarPedidosAgendamento);
		
		Configuracao configValidarProdutosAgendamento = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDAR_PRODUTOS_AGENDAMENTO);
		if (configValidarProdutosAgendamento == null){
			configValidarProdutosAgendamento = new Configuracao();
			configValidarProdutosAgendamento.setNome(ConfiguracaoVO.VALIDAR_PRODUTOS_AGENDAMENTO);
		}
		saveOrUpdate(configValidarProdutosAgendamento);
		
		/*if (filtro.getValidarVerbaAgendamento() != null)
			configValidarVerba.setValor(getString(filtro.getValidarVerbaAgendamento()));
		else
			configValidarVerba.setValor(getString(false));*/
		
		Configuracao validacaoVerbaDeposito = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDACAO_VERBA_DEPOSITO);
		if (validacaoVerbaDeposito == null){
			validacaoVerbaDeposito = new Configuracao();
			validacaoVerbaDeposito.setNome(ConfiguracaoVO.VALIDACAO_VERBA_DEPOSITO);
		}
		
		if (filtro.getValidarVerbaDeposito() != null)
			validacaoVerbaDeposito.setValor(getString(filtro.getValidarVerbaDeposito()));
		else
			validacaoVerbaDeposito.setValor(getString(false));
		
		saveOrUpdate(validacaoVerbaDeposito);
		
		Configuracao validacaoVerbaLiberacaoRecebimento = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDACAO_VERBA_LIBERACAO_RECEBIMENTO);
		if (validacaoVerbaLiberacaoRecebimento == null){
			validacaoVerbaLiberacaoRecebimento = new Configuracao();
			validacaoVerbaLiberacaoRecebimento.setNome(ConfiguracaoVO.VALIDACAO_VERBA_LIBERACAO_RECEBIMENTO);
		}
		
		if (filtro.getValidarVerbaLiberacaoRecebimento() != null)
			validacaoVerbaLiberacaoRecebimento.setValor(getString(filtro.getValidarVerbaLiberacaoRecebimento()));
		else
			validacaoVerbaLiberacaoRecebimento.setValor(getString(false));
		
		saveOrUpdate(validacaoVerbaLiberacaoRecebimento);
		
		Configuracao validacaoVerbaLiberacaoFinanceira = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDACAO_VERBA_LIBERACAO_FINANCEIRA);
		if (validacaoVerbaLiberacaoFinanceira == null){
			validacaoVerbaLiberacaoFinanceira = new Configuracao();
			validacaoVerbaLiberacaoFinanceira.setNome(ConfiguracaoVO.VALIDACAO_VERBA_LIBERACAO_FINANCEIRA);
		}
		
		if (filtro.getValidarVerbaLiberacaoFinanceira() != null)
			validacaoVerbaLiberacaoFinanceira.setValor(getString(filtro.getValidarVerbaLiberacaoFinanceira()));
		else
			validacaoVerbaLiberacaoFinanceira.setValor(getString(false));
		
		saveOrUpdate(validacaoVerbaLiberacaoFinanceira);
		
		Configuracao validacaoVerbaAgendamentoRecebimento = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDACAO_VERBA_AGENDAMENTO_RECEBIMENTO);
		if (validacaoVerbaAgendamentoRecebimento == null){
			validacaoVerbaAgendamentoRecebimento = new Configuracao();
			validacaoVerbaAgendamentoRecebimento.setNome(ConfiguracaoVO.VALIDACAO_VERBA_AGENDAMENTO_RECEBIMENTO);
		}
		
		if (filtro.getValidarVerbaAgendamentoRecebimento() != null)
			validacaoVerbaAgendamentoRecebimento.setValor(getString(filtro.getValidarVerbaAgendamentoRecebimento()));
		else
			validacaoVerbaAgendamentoRecebimento.setValor(getString(false));
		
		saveOrUpdate(validacaoVerbaAgendamentoRecebimento);
		
		Configuracao validacaoVerbaAgendamentoFinanceira = configuracaoDAO.findByName(null, ConfiguracaoVO.VALIDACAO_VERBA_AGENDAMENTO_FINANCEIRA);
		if (validacaoVerbaAgendamentoFinanceira == null){
			validacaoVerbaAgendamentoFinanceira = new Configuracao();
			validacaoVerbaAgendamentoFinanceira.setNome(ConfiguracaoVO.VALIDACAO_VERBA_AGENDAMENTO_FINANCEIRA);
		}
		
		if (filtro.getValidarVerbaAgendamentoFinanceira() != null)
			validacaoVerbaAgendamentoFinanceira.setValor(getString(filtro.getValidarVerbaAgendamentoFinanceira()));
		else
			validacaoVerbaAgendamentoFinanceira.setValor(getString(false));
		
		saveOrUpdate(validacaoVerbaAgendamentoFinanceira);

		Configuracao exigirDuasContagensInventario = configuracaoDAO.findByName(null, ConfiguracaoVO.EXIGIR_DUAS_CONTAGENS_INVENTARIO);
		if (exigirDuasContagensInventario == null){
			exigirDuasContagensInventario = new Configuracao();
			exigirDuasContagensInventario.setNome(ConfiguracaoVO.EXIGIR_DUAS_CONTAGENS_INVENTARIO);
		}
		
		if (filtro.getExigirDuasContagensInventario() != null)
			exigirDuasContagensInventario.setValor(getString(filtro.getExigirDuasContagensInventario()));
		else
			exigirDuasContagensInventario.setValor(getString(false));
		
		saveOrUpdate(exigirDuasContagensInventario);
		//-------------------------------------------------------------
		
		if(filtro.getDeposito() == null){
			clearCache();// Apaga o cache da configuracao
			return;
		}
		
		List<String> nomes = getNomesForBanco();
		List<Configuracao> listaConfiguracao = findByDeposito(filtro.getDeposito());
		
		ConfiguracaoVO configuracaoVO = filtro.getConfiguracaoVO();
		
		if(configuracaoVO == null)
			configuracaoVO = new ConfiguracaoVO();
		
		for(String config : nomes) {
			Configuracao configuracao = getConfiguracao(listaConfiguracao, config);
			
			if (configuracao == null){
				configuracao = new Configuracao();
				configuracao.setNome(config);
			}
			
			if (configuracao.getNome().equals(ConfiguracaoVO.PERCENTUAL_RETENCAO_DESCAGA)){
				if(configuracaoVO.getPercentualDescarga() != null)
					configuracao.setValor(configuracaoVO.getPercentualDescarga().toString());	
				else
					configuracao.setValor("0");
			}else if (configuracao.getNome().equals(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_LOJA)){
				configuracao.setValor(getString(configuracaoVO.getSepararPorClienteLoja()));
			}else if (configuracao.getNome().equals(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_MOSTRUARIO)){
				configuracao.setValor(getString(configuracaoVO.getSepararPorClienteMostruario()));
			}else if (configuracao.getNome().equals(ConfiguracaoVO.SEPARAR_CLIENTEFILIAL_CLIENTE)){
				configuracao.setValor(getString(configuracaoVO.getSepararPorClienteCliente()));
			}else if (configuracao.getNome().equals(ConfiguracaoVO.TIPO_SEPARACAO_LOJA)){
				if(configuracaoVO.getSepararPorClienteLoja() != null && configuracaoVO.getSepararPorClienteLoja())
					configuracao.setValor(getString(configuracaoVO.getTipoSeparacaoLoja()));
				else
					configuracao.setValor(null);
			}else if (configuracao.getNome().equals(ConfiguracaoVO.TIPO_SEPARACAO_MOSTRUARIO)){
				if(configuracaoVO.getSepararPorClienteMostruario() != null && configuracaoVO.getSepararPorClienteMostruario())
					configuracao.setValor(getString(configuracaoVO.getTipoSeparacaoMostruario()));
				else
					configuracao.setValor(null);
			}else if (configuracao.getNome().equals(ConfiguracaoVO.TIPO_SEPARACAO_CLIENTE)){
				if(configuracaoVO.getSepararPorClienteCliente() != null && configuracaoVO.getSepararPorClienteCliente())
					configuracao.setValor(getString(configuracaoVO.getTipoSeparacaoCliente()));
				else
					configuracao.setValor(null);
			}else if (configuracao.getNome().equals(ConfiguracaoVO.ETIQUETA_SEPARACAO)){
				if(configuracaoVO.getListaEtiquetaEspedicao() != null && !configuracaoVO.getListaEtiquetaEspedicao().isEmpty())
					prepareEtiquetasToSave(configuracaoVO,configuracao);
				else
					configuracao.setValor("nulo");
			}else if (configuracao.getNome().equals(ConfiguracaoVO.MAX_ENDERECOS_CRIADOS_POR_VEZ)){
				if(configuracaoVO.getMaxEnderecos() != null)
					configuracao.setValor(configuracaoVO.getMaxEnderecos().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.LINHA_SEPARACAO_PADRAO)){
				if(configuracaoVO.getLinhaseparacaoPadrao() != null && configuracaoVO.getLinhaseparacaoPadrao().getCdlinhaseparacao() != null){
					configuracao.setValor(configuracaoVO.getLinhaseparacaoPadrao().getCdlinhaseparacao().toString());
					ProdutoService.getInstance().definirLinhaPadrao(filtro.getDeposito(), configuracaoVO.getLinhaseparacaoPadrao());
				}
			}else if (configuracao.getNome().equals(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS)){
				if(configuracaoVO.getColetorExigeCodigoBarras() != null){
					configuracao.setValor(getString(configuracaoVO.getColetorExigeCodigoBarras()));
				}
			}else if (configuracao.getNome().equals(ConfiguracaoVO.PORTAPALETE_EXIGE_PICKING)){
				if(configuracaoVO.getPortapaleteExigePicking() != null){
					configuracao.setValor(getString(configuracaoVO.getPortapaleteExigePicking()));
				}
			}else if (configuracao.getNome().equals(ConfiguracaoVO.OPERACAO_CD_CD_DUPLICA_PEDIDO)){
				if(configuracaoVO.getOperacaoCdCdDuplicaPedido() != null){
					configuracao.setValor(getString(configuracaoVO.getOperacaoCdCdDuplicaPedido()));
				}
			}else if (configuracao.getNome().equals(ConfiguracaoVO.ENVIAR_RECEBIMENTO_ENDERECADO)){
				if(configuracaoVO.getEnviarRecebimentosEnderecados() != null){
					configuracao.setValor(getString(configuracaoVO.getEnviarRecebimentosEnderecados()));
				}
			}else if (configuracao.getNome().equals(ConfiguracaoVO.ENDERECAMENTO_AUTOMATICO)){
				if(configuracaoVO.getEnderecamentoAutomatico() != null)
					configuracao.setValor(configuracaoVO.getEnderecamentoAutomatico().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.EXIGIR_SEGUNDA_CONFERENCIA)){
				if(configuracaoVO.getExigirSegundaConferencia() != null)
					configuracao.setValor(configuracaoVO.getExigirSegundaConferencia().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.QUEBRAR_POR_CARREGAMENTO)){
				if(configuracaoVO.getQuebrarCarregamento() != null)
					configuracao.setValor(configuracaoVO.getQuebrarCarregamento().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.OPERACAO_EXPEDICAO_POR_BOX)){
				if(configuracaoVO.getOperacaoExpedicaoBox() != null)
					configuracao.setValor(configuracaoVO.getOperacaoExpedicaoBox().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.UTILIZAR_CAIXA_MESTRE)){
				if(configuracaoVO.getUtilizarCaixaMestre() != null)
					configuracao.setValor(configuracaoVO.getUtilizarCaixaMestre().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.SEGUNDANOTA_CDCLIENTE_ENTREGA)){
				if(configuracaoVO.getSegundanotaCdclienteEntrega() != null)
					configuracao.setValor(configuracaoVO.getSegundanotaCdclienteEntrega().toString());
			}else if (configuracao.getNome().equals(ConfiguracaoVO.BORDERO_RATEIRO_EMPRESA)){
				if(configuracaoVO.getBorderoRateioEmpresa() != null)
					configuracao.setValor(configuracaoVO.getBorderoRateioEmpresa().toString());
			}
	
			configuracao.setDeposito(filtro.getDeposito());
			
			Configuracao c = getConfiguracao(listaConfiguracao,configuracao);
			if(c != null)
				configuracao.setCdconfiguracao(c.getCdconfiguracao());
			
			saveOrUpdate(configuracao);
		}
		
		clearCache();// Apaga o cache da configuracao
	}
	
	private Configuracao getConfiguracao(List<Configuracao> listaConfiguracao, String nome) {
		for (Configuracao config : listaConfiguracao)
			if (config.getNome().equals(nome))
				return config;
		
		return null;
	}

	/**
	 * Busca a configuração na lista
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param listaConfiguracao
	 * @param configuracao
	 * @return
	 */
	private Configuracao getConfiguracao(List<Configuracao> listaConfiguracao,Configuracao configuracao) {
		for (Configuracao c : listaConfiguracao) {
			if(c.getNome().equals(configuracao.getNome()))
				return c;
		}
		return null;
	}

	/**
	 * Prepara um representação String para a lista de etiquetas da configuração
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param configuracaoVO
	 * @param configuracao
	 */
	private void prepareEtiquetasToSave(ConfiguracaoVO configuracaoVO, Configuracao configuracao) {
		String etiquetas = "";
		
		int i = 0;
		int size = configuracaoVO.getListaEtiquetaEspedicao().size();
		
		do{
			etiquetas += configuracaoVO.getListaEtiquetaEspedicao().get(i).getCdtipooperacao();
			
			i++;
			if(i < size)
				etiquetas += ",";
			else 
				break;
			
		}while(true);
		
		configuracao.setValor(etiquetas);		
	}

	/**
	 * Retorna uma string com o valor de
	 * aux
	 * @author Leonardo Guimarães
	 * @param aux
	 * @return
	 */
	public String getString(Boolean aux){
		if(aux != null){
			return aux.toString();
		}
		return "nulo";
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ConfiguracaoDAO#loadPercentualConfiguracao
	 * @param recebimento
	 * @return
	 * @author Arantes
	 */
	public Double loadPercentualConfiguracao() {
		return configuracaoDAO.loadPercentualConfiguracao();
	}
	
	/**
	 * Encontra o config a partir de uma key.
	 * 
	 * @see br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO
	 * @see #findByDeposito(Deposito)
	 * 
	 * @param key
	 * @return a configuração do depósito
	 * @author Pedro Gonçalves
	 */
	public String getConfigValue(String key, Deposito deposito) {
		if(key == null)
			throw new WmsException("O nome da configuração não pode ser null.");
		
		if (deposito == null){
			if (configCache.containsKey(key))
				return configCache.get(key);
			
			Configuracao configuracao = configuracaoDAO.findByName(null, key);
			if (configuracao != null ){
				configCache.put(key, configuracao.getValor());
				return configuracao.getValor();
			} else {
				configCache.put(key, null);
				return null;
			}
		}
		
		if(confDeposito.get(deposito) == null){
			List<Configuracao> findByDeposito = this.findByDeposito(deposito);
			HashMap<String,String> mapaN = new HashMap<String, String>();
			for (Configuracao configuracao : findByDeposito) {
				mapaN.put(configuracao.getNome(), configuracao.getValor());
			}
			
			confDeposito.put(deposito,mapaN);
		}
		HashMap<String, String> a = confDeposito.get(deposito);
		if(a == null)
			throw new WmsException("Config "+key+" não foi encontrado.");
		
		String string = a.get(key);
		return string;
	}
	
	/**
	 * Limpa o cache de config.
	 * 
	 * @author Pedro Gonçalves
	 */
	public void clearCache(){
		this.confDeposito = new HashMap<Deposito, HashMap<String,String>>();
		this.configCache = new HashMap<String, String>();
	}
	
	/* singleton */
	private static ConfiguracaoService instance;

	public static ConfiguracaoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ConfiguracaoService.class);
		}
		return instance;
	}

	/**
	 * Obtém uma configuração por nome. <br/>
	 * Utilize este método para obter as configurações que são independentes de depósito. 
	 * Se você usar este método para consultar uma configuração que depende de depósito, 
	 * uma exceção poderá ser lançada caso exista dois registros com o mesmo nome de configuração.
	 * 
	 * @author Giovane Freitas
	 * @param nomeConfig
	 * @return
	 */
	public String getConfigByName(String nomeConfig, Deposito deposito) {
		return configuracaoDAO.getConfigByName(nomeConfig, deposito);
	}
	
	/**
	 * Verifica se existe uma configuração com valor booleano <code>true</code>
	 * ou <code>false</code> ou valores inteiros 1 (true) ou 0 (false) e se
	 * está gravada com valor verdadeiro.
	 * 
	 * @param key
	 * @param deposito
	 * @return <code>true</code> se o partâmetro existe e se o valor é
	 *         <code>true</code> ou 1, retorna <code>false</code> para
	 *         qualquer outra hipótese.
	 */
	public boolean isTrue(String key, Deposito deposito) {
		String configValue = getConfigValue(key, deposito);
		if (configValue != null && (configValue.equalsIgnoreCase("true") || configValue.equals("1")))
			return true;
		else 
			return false;
	}
	
	/**
	 * Grava um determinado valor na tabela de configurações.
	 * 
	 * @param key
	 * @param deposito
	 * @param valor
	 */
	public void set(String key, Deposito deposito, String valor){
		Configuracao config = configuracaoDAO.findByName(deposito, key);
		if (config == null){
			config = new Configuracao();
			config.setDeposito(deposito);
			config.setNome(key);
		}
		
		config.setValor(valor);
		saveOrUpdate(config);
		
		//atualizando o cache
		if (confDeposito.containsKey(deposito)){
			HashMap<String, String> map = confDeposito.get(deposito);
			map.put(key, valor);
		}
	}
	
	public Configuracao findByName(Deposito deposito, String key){
		return configuracaoDAO.findByName(deposito, key);
	}

}
