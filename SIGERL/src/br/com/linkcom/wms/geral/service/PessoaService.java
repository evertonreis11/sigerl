package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.neo.types.Cpf;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Pessoa;
import br.com.linkcom.wms.geral.bean.Tipopessoa;
import br.com.linkcom.wms.geral.dao.PessoaDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ImpressaoprodutividadeFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.PessoaFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.logistica.ProdutividadeVO;

public class PessoaService extends GenericService<Pessoa> {
	
	private PessoaDAO pessoaDAO;
	private FabricanteService fabricanteService;
	private FornecedorService fornecedorService;
	private TransportadorService transportadorService;
	private ClienteService clienteService;
	
	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}
	
	public void setFabricanteService(FabricanteService fabricanteService) {
		this.fabricanteService = fabricanteService;
	}
	
	public void setFornecedorService(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}
	
	public void setTransportadorService(TransportadorService transportadorService) {
		this.transportadorService = transportadorService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	/**
	 * Método de referência ao DAO
	 * @see br.com.linkcom.wms.geral.dao.PessoaDAO#findPessoaByCpf(Cpf)
	 * @param cpf
	 * @return 
	 * @author João Paulo Zica
	 */
	public Pessoa findPessoaByDocumento(String documento) {
		return pessoaDAO.findPessoaByDocumento(documento);
	}
	
	/**
	 * Método que obtem uma pessoa a partir da pk
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.dao.PessoaDAO#findPessoaByCodigo(cod)
	 * @param cod
	 * @return Uma pessoa com base no cod
	 */
	public Pessoa findPessoaByCodigo(Integer cod) {
		return pessoaDAO.findPessoaByCodigo(cod);
	}
	
	/**
	 * Metodo que salva o cdpessoa nas tabelas selecionadas nos checkbox
	 * @see br.com.linkcom.wms.geral.dao.PessoaDAO#salvaListaTipo(bean)
	 * @author Leonardo  
	 * @param bean
	 */
	public void salvalistatipo(Pessoa bean){
		pessoaDAO.salvaListaTipo(bean);
	}
	
	/**
	 * Metodo para criar uma lista com os tipos de pessoa que serão ultilizados na aplicacao
	 * @author Leonardo Guimarães
	 * @return
	 */
	public List<Tipopessoa> listaTipoPessoa(){
		List<Tipopessoa> listatipopessoa = new ArrayList<Tipopessoa>();
		Tipopessoa tipopessoa = new Tipopessoa();
		tipopessoa.setDescricao("Cliente");
		tipopessoa.setChavenome("CL");
		listatipopessoa.add(tipopessoa);
		tipopessoa = new Tipopessoa();
		tipopessoa.setDescricao("Fornecedor");
		tipopessoa.setChavenome("FO");
		listatipopessoa.add(tipopessoa);
		return listatipopessoa;
	}	
	
	/**
	 * Cria uma string com os tipos da pessoa
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.service.FornecedorService#findByCd(Integer cd)
	 * @see br.com.linkcom.wms.geral.service.ClienteService#findByCd(Integer cd)
	 * @see	br.com.linkcom.wms.geral.service.FabricanteService#findByCd(Integer cd)
	 * @see br.com.linkcom.wms.geral.service.TransportadorService#findByCd(Integer cd)
	 * @param lista
	 * @param filtro
	 * @return
	 */
	public ListagemResult<Pessoa> prepareForListagem(ListagemResult<Pessoa> lista,PessoaFiltro filtro) {
		Comparator<Pessoa> asc = new Comparator<Pessoa>(){
					public int compare(Pessoa o1, Pessoa o2) {
						String tipos1 = o1.getTipos();
						String tipos2 = o2.getTipos();
						return tipos1.compareTo(tipos2);
					}
				};
		Comparator<Pessoa> desc = new Comparator<Pessoa>(){
					public int compare(Pessoa pessoa1, Pessoa pessoa2) {
						String tipos1 = pessoa1.getTipos();
						String tipos2 = pessoa2.getTipos();
						return tipos1.compareTo(tipos2) * -1;
					}
				};		
		if(lista != null){
			List<Pessoa> listaPessoa = lista.list();
			for (Pessoa pessoa : listaPessoa){
				List<Tipopessoa> listatipopessoa = new ArrayList<Tipopessoa>();
				makeListaTipo(listatipopessoa,pessoa);
				pessoa.setListatipo(listatipopessoa);
				pessoa.setTipos(getStringTipos(pessoa));
			}
			if(filtro.getOrderBy() != null && filtro.getOrderBy().equals("1"))
				if(filtro.isAsc()){
					Collections.sort(listaPessoa,asc);
				}else{
					Collections.sort(listaPessoa,desc);
				}
		}
		return lista;
	}
	
	/**
	 * Método que agrupa todos os tipos de uma pessoa
	 * em uma string
	 * @author Leonardo Guimarães
	 * @param pessoa
	 * @return
	 */
	private String getStringTipos(Pessoa pessoa) {
		return WmsUtil.concatenateWithLimit(pessoa.getListatipo(),"descricao", 5);		
	}
	
	/**
	 * Método de referência ao DAO
	 * @author Leonardo Guimarães
	 * @param bean
	 */
	public void desativa(Pessoa bean) {
		if(bean.getCdpessoa().equals(WmsUtil.getUsuarioLogado().getCdpessoa())){
			throw new WmsException("Não foi possível excluir \""+WmsUtil.getUsuarioLogado().getNome()+
											 "\" pois seu perfil possui referências à ele(a)");
		} else pessoaDAO.desativa(bean);
		
	}
	
	@Override
	public Pessoa loadForEntrada(Pessoa bean) {
		bean = super.loadForEntrada(bean);
		List<Tipopessoa> listatipopessoa = new ArrayList<Tipopessoa>();
		makeListaTipo(listatipopessoa,bean);
		Pessoa pessoa=new Pessoa();
		pessoa.setCdpessoa(bean.getCdpessoa());
		pessoa.setAtivo(bean.getAtivo());
		pessoa.setDocumento(bean.getDocumento());
		pessoa.setNome(bean.getNome());
		pessoa.setEmail(bean.getEmail());
		pessoa.setPessoanatureza(bean.getPessoanatureza());
		pessoa.setListatipo(listatipopessoa);
		return pessoa;
	}
	
	/**
	 * Adiciona os tipos correspondentes de cada pessoa
	 * em um lista de tipopessoa
	 * @author Leonardo Guimarães
	 * @see br.com.linkcom.wms.geral.service.ClienteService#findByCd(Integer cd)
	 * @see br.com.linkcom.wms.geral.service.FabricanteService#findByCd(Integer cd)
	 * @see br.com.linkcom.wms.geral.service.FornecedorService.findByCd(Integer cd)
	 * @see br.com.linkcom.wms.geral.service.TransportadorService.findByCd(Integer cd)
	 * @param listaTipoPessoa
	 * @param bean
	 */
	public void makeListaTipo(List<Tipopessoa> listaTipoPessoa,Pessoa bean){
		if(clienteService.findByCd(bean.getCdpessoa()) != null){
			Tipopessoa tipopessoa = new Tipopessoa();
			tipopessoa.setChavenome("CL");
			tipopessoa.setDescricao("Cliente");
			listaTipoPessoa.add(tipopessoa);
		}
		if(fabricanteService.findByCd(bean.getCdpessoa()) != null){
			Tipopessoa tipopessoa = new Tipopessoa();
			tipopessoa.setChavenome("FA");
			tipopessoa.setDescricao("Fabricante");
			listaTipoPessoa.add(tipopessoa);
		}
		if(fornecedorService.findByCd(bean.getCdpessoa()) != null){
			Tipopessoa tipopessoa = new Tipopessoa();
			tipopessoa.setChavenome("FO");
			tipopessoa.setDescricao("Fornecedor");
			listaTipoPessoa.add(tipopessoa);
		}
		if(transportadorService.findByCd(bean.getCdpessoa()) != null){
			Tipopessoa tipopessoa = new Tipopessoa();
			tipopessoa.setChavenome("TR");
			tipopessoa.setDescricao("Transportador");
			listaTipoPessoa.add(tipopessoa);
		}
	}
	
	/* singleton */
	private static PessoaService instance;
	public static PessoaService getInstance() {
		if(instance == null){
			instance = Neo.getObject(PessoaService.class);
		}
		return instance;
	}
	
	@Override
	public void delete(Pessoa bean) {
		desativa(bean);
	}	
	
	/**
	 * 
	 * Método para criação do relatório de produtividade
	 * Método de referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.PessoaDAO#findByProdutividade(ImpressaoprodutividadeFiltro)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return IReport
	 * 
	 */
	private List<ProdutividadeVO> findByProdutividade(ImpressaoprodutividadeFiltro filtro) {
		return pessoaDAO.findByProdutividade(filtro);
	}
	
	/**
	 * 
	 * Método para criação do relatório de produtividade
	 * 
	 * @see br.com.linkcom.wms.geral.service.PessoaService#findByProdutividade(ImpressaoprodutividadeFiltro)
	 * @see #ajustaOperacao(List)
	 * @author Arantes
	 * @author Cíntia Nogueira
	 * 
	 * @param filtro
	 * @return IReport
	 * 
	 * 
	 */
		Report report = new Report("RelatorioProdutividade");
		public IReport createReportProdutividade(ImpressaoprodutividadeFiltro filtro) {
//		if(filtro.getDatainicial()!=null){
//			filtro.setDatainicial(DateUtil.limpaMinSegHora(filtro.getDatainicial()));
//		}
//		if(filtro.getDatafinal()!=null){
//			filtro.setDatafinal(DateUtil.limpaMinSegHora(filtro.getDatafinal()));
//		}
		List<ProdutividadeVO> listaProdutividade = this.findByProdutividade(filtro);
		
	/*	for (ProdutividadeVO produtividade : listaProdutividade) {
			if(filtro.getDatainicial() != null)
				produtividade.setDataInicio(NeoFormater.getInstance().format(filtro.getDatainicial()));
			
			if(filtro.getDatafinal() != null)
				produtividade.setDataFim(NeoFormater.getInstance().format(filtro.getDatafinal()));
		}*/
		listaProdutividade =ajustaOperacao(listaProdutividade);
		
		if(filtro.getOrdemtipo() != null)
			report.addParameter("hasUnicaOperacao", Boolean.TRUE);
		else
			report.addParameter("hasUnicaOperacao", Boolean.FALSE);
		if(filtro.getDatainicial() != null){
			report.addParameter("DATAINICIO",NeoFormater.getInstance().format(filtro.getDatainicial()));
		}else{
			report.addParameter("DATAINICIO", null);
		}
		if(filtro.getDatafinal() != null){
			report.addParameter("DATAFIM", NeoFormater.getInstance().format(filtro.getDatafinal()));
		}else{
			report.addParameter("DATAFIM",null);
		}
			report.setDataSource(listaProdutividade);
		
		return report;
	}

	/**
	 * Cria um novo produtividadeVO
	 * @param produtividade
	 * @param tipoOrdem
	 * @return
	 * @author Cíntia Nogueira
	 */	
	private ProdutividadeVO criaNovoProdutividadeVO(ProdutividadeVO produtividade, String tipoOrdem){
		ProdutividadeVO novo = new ProdutividadeVO();
		novo.setCdpessoa(produtividade.getCdpessoa());
		novo.setDataFim(produtividade.getDataFim());
		novo.setOrdemTipoNome(tipoOrdem);
		novo.setPalete(produtividade.getPalete()==null ? 0 : produtividade.getPalete());
		novo.setPessoaNome(produtividade.getPessoaNome());
		novo.setPeso(produtividade.getPeso()==null ? 0.0 : produtividade.getPeso());
		novo.setVolume(produtividade.getVolume()==null? 0.0 :produtividade.getVolume());
		return novo;
	}
	/**
	 * Ajusta o agrupamento agrupando de acordo com os tipos de operação pedido no
	 * caso de uso UC024
	 * @param lista
	 * @return
	 * @author Cíntia Nogueira
	 * @see #hasTipoOrdemPessoa(List, String, Integer)
	 * @see #criaNovoProdutividadeVO(ProdutividadeVO, String)
	 */
	private List<ProdutividadeVO> ajustaOperacao(List<ProdutividadeVO> lista){
		List<ProdutividadeVO> listaRetorno = new ArrayList<ProdutividadeVO>();
		for(ProdutividadeVO produtividade :lista){
			String nomeTipoOrdem= produtividade.getOrdemTipoNome();
			if(nomeTipoOrdem.equals("Conferência de recebimento") || 
					nomeTipoOrdem.equals("Reconferência de recebimento") ){
				ProdutividadeVO produtividadeNovo = hasTipoOrdemPessoa(listaRetorno, "Conferência de recebimento", 
						produtividade.getCdpessoa());
				
				if(produtividadeNovo==null){
					produtividadeNovo = criaNovoProdutividadeVO(produtividade, "Conferência de recebimento");
					listaRetorno.add(produtividadeNovo);
				}else{
					produtividadeNovo.setPeso(produtividadeNovo.getPeso()+produtividade.getPeso());
					produtividadeNovo.setPalete(produtividadeNovo.getPalete()+produtividade.getPalete());
					produtividadeNovo.setVolume(produtividadeNovo.getVolume() + produtividade.getVolume());
				}
			}else if(nomeTipoOrdem.equals("Reabastecimento de picking")){
				produtividade.setOrdemTipoNome("Reabastecimento");
				listaRetorno.add(produtividade);
			}else if(nomeTipoOrdem.equals("Transferência") || nomeTipoOrdem.equals("Mapa de separação") 
					|| nomeTipoOrdem.equals("Contagem de inventário")){
				 listaRetorno.add(produtividade);
			}else if(nomeTipoOrdem.equals("Conferência de expedição") || 
					nomeTipoOrdem.equals("Reconferência de expedição") ){
				ProdutividadeVO produtividadeNovo = hasTipoOrdemPessoa(listaRetorno, "Conferência de expedição", 
						produtividade.getCdpessoa());
				
				if(produtividadeNovo==null){
					produtividadeNovo = criaNovoProdutividadeVO(produtividade, "Conferência de expedição");
					listaRetorno.add(produtividadeNovo);
				}else{
					produtividadeNovo.setPeso(produtividadeNovo.getPeso()+produtividade.getPeso());
					produtividadeNovo.setPalete(produtividadeNovo.getPalete()+produtividade.getPalete());
					produtividadeNovo.setVolume(produtividadeNovo.getVolume() + produtividade.getVolume());
				}
			}
			else if(nomeTipoOrdem.equals("Endereçamento avariado") || 
					nomeTipoOrdem.equals("Endereçamento UMA")  || nomeTipoOrdem.equals("Endereçamento fracionado")){
				ProdutividadeVO produtividadeNovo = hasTipoOrdemPessoa(listaRetorno, "Endereçamento", 
						produtividade.getCdpessoa());
				
				if(produtividadeNovo==null){
					produtividadeNovo = criaNovoProdutividadeVO(produtividade, "Endereçamento");
					listaRetorno.add(produtividadeNovo);
				}else{
					produtividadeNovo.setPeso(produtividadeNovo.getPeso()+produtividade.getPeso());
					produtividadeNovo.setPalete(produtividadeNovo.getPalete()+produtividade.getPalete());
					produtividadeNovo.setVolume(produtividadeNovo.getVolume() + produtividade.getVolume());
				}
			}
		}
		return listaRetorno;
	}
	
	/**
	 * Retorna o produtividadeVO da lista que tem a pessoa e o tipoordem,
	 * caso não exista retorna null 
	 * @param listaProdutividade
	 * @param tipoordem
	 * @param cdpessoa
	 * @return
	 * @author Cíntia Nogueira
	 */
	private ProdutividadeVO hasTipoOrdemPessoa(List<ProdutividadeVO> listaProdutividade, String tipoordem,Integer cdpessoa ){
		
		for(ProdutividadeVO produtividade: listaProdutividade){
			if(produtividade.getCdpessoa().equals(cdpessoa) &&
					produtividade.getOrdemTipoNome().equals(tipoordem)){
				return produtividade;
			}
		}
		
		return null;
	}
}
