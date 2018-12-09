package br.com.linkcom.wms.geral.service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecolado;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.AjustarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.ConsultarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.TransferenciaFiltro;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.logistica.EnderecoAux;

public class EnderecoprodutoService extends br.com.linkcom.wms.util.neo.persistence.GenericService<Enderecoproduto> {
	private EnderecoprodutoDAO enderecoprodutoDAO;
	private EnderecoService enderecoService;
	private ProdutoService produtoService;
	
	private OrdemservicoprodutoService ordemservicoprodutoService;
	private OrdemservicoprodutoenderecoService ordemservicoprodutoenderecoService;
	private OrdemservicoService ordemservicoService;
	
	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}
	
	
	public void setEnderecoprodutoDAO(EnderecoprodutoDAO enderecoprodutoDAO) {
		this.enderecoprodutoDAO = enderecoprodutoDAO;
	}
	
	public void setEnderecoService(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}
	
	public void setOrdemservicoprodutoService(
			OrdemservicoprodutoService ordemservicoprodutoService) {
		this.ordemservicoprodutoService = ordemservicoprodutoService;
	}
	
	public void setOrdemservicoprodutoenderecoService(
			OrdemservicoprodutoenderecoService ordemservicoprodutoenderecoService) {
		this.ordemservicoprodutoenderecoService = ordemservicoprodutoenderecoService;
	}
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Recupera a listagem de endereços produtos.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#findByEnderecoorigemproduto(TransferenciaFiltro) 
	 * 
	 * @author Arantes
	 * 
	 * @param transferenciaFiltro
	 * @return List<Enderecoproduto>
	 * 
	 */
	public List<Enderecoproduto> findByEnderecoorigemproduto(TransferenciaFiltro transferenciaFiltro) {
		return enderecoprodutoDAO.findByEnderecoorigemproduto(transferenciaFiltro);
	}
	
	/**
	 * 
	 * Preenche a quantidade do endereço produto com a quantidade atual do formulário caso o campo quantidade esteja vazio.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * 
	 */
	public void preencherQuantidade(AjustarEstoqueFiltro filtro) {
		if((filtro == null) || (filtro.getListaAjustarEstoque() == null))
			throw new WmsException("Parâmetros inválidos.");
		
		for (AjustarEstoqueFiltro ajustarEstoque : filtro.getListaAjustarEstoque()) {
			Enderecoproduto enderecoproduto = ajustarEstoque.getEnderecoproduto();

			//So valida se tudo estiver preenchido
			if(enderecoproduto != null && enderecoproduto.getProduto() != null && 
			   enderecoproduto.getProduto().getCdproduto() != null && enderecoproduto.getQtde() != null){
				enderecoproduto.setAtualizar(Boolean.TRUE);
			}else{
				enderecoproduto.setAtualizar(Boolean.FALSE);
			}
		}
	}
	
	/**
	 * 
	 * Método que insere, atualiza e remove dados da tabela Enderecoproduto
	 * 
	 * @author Arantes
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#excluirEnderecosProdutos(Enderecoproduto)
	 * @see #loadWithEndereco(Enderecoproduto)
	 * @see #criaOrdemServicoAjusteEstoque(AjustarEstoqueFiltro, Enderecoproduto)
	 * 
	 * @param filtro
	 */
	public boolean realizarAcoes(AjustarEstoqueFiltro filtro) {
		if((filtro == null) || (filtro.getListaAjustarEstoque() == null))
			throw new WmsException("Parâmetros inválidos.");

		boolean houveAlgumSave = false;
		for (AjustarEstoqueFiltro ajustarEstoque : filtro.getListaAjustarEstoque()) {
			Enderecoproduto enderecoproduto = ajustarEstoque.getEnderecoproduto();
			
			 if (enderecoproduto.getAtualizar()){
				houveAlgumSave = true;
				if(enderecoproduto.getCdenderecoproduto() != null) {
					this.atualizarEnderecoProduto(ajustarEstoque, enderecoproduto);
					
				} else if(enderecoproduto.getCdenderecoproduto() == null) {
					this.inserirEnderecoProduto(ajustarEstoque, enderecoproduto);
					
				}

				criaOrdemServicoAjusteEstoque(ajustarEstoque, enderecoproduto);
			}
		}
		return houveAlgumSave;
	}
	
	/**
	 * Método que cria ordem serviço a partir do ajuste de estoque
	 * 
	 * @see OrdemservicoprodutoService#saveOrUpdate(Ordemservicoproduto)
	 * @see OrdemservicoprodutoenderecoService#saveOrUpdate(Ordemservicoprodutoendereco)
	 * @see OrdemservicoService#saveOrUpdate(Ordemservico)
	 * @see OrdemservicoService#callProcedureAtualizarRastreabilidade(Ordemservico)
	 * 
	 * @param ajustarEstoque
	 * @param enderecoproduto
	 * @author Tomás Rabelo
	 */
	private void criaOrdemServicoAjusteEstoque(AjustarEstoqueFiltro ajustarEstoque, final Enderecoproduto enderecoproduto) {		
		getGenericDAO().getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {

				Ordemservico ordemservico = new Ordemservico(Ordemtipo.AJUSTE_ESTOQUE, Ordemstatus.FINALIZADO_SUCESSO, WmsUtil.getDeposito());
				OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario((Usuario)Neo.getUser());
				ordemservicoUsuario.setDtinicio(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ordemservicoUsuario.setDtfim(new Timestamp(Calendar.getInstance().getTimeInMillis()));
				Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto(enderecoproduto.getProduto(), enderecoproduto.getQtde(), Ordemprodutostatus.CONCLUIDO_OK);
				Ordemservicoprodutoendereco ordemservicoprodutoendereco = new Ordemservicoprodutoendereco(ordemservicoproduto, enderecoproduto.getEndereco(), enderecoproduto.getEndereco(), enderecoproduto.getQtde());

				ordemservicoprodutoService.saveOrUpdate(ordemservicoproduto);
				ordemservicoprodutoenderecoService.saveOrUpdate(ordemservicoprodutoendereco);
				
				ordemservico.getListaOrdemServicoUsuario().add(ordemservicoUsuario);
				ordemservico.getListaOrdemProdutoLigacao().add(new Ordemprodutoligacao(ordemservicoproduto));
				
				ordemservicoService.saveOrUpdate(ordemservico);
				ordemservicoService.callProcedureAtualizarRastreabilidade(ordemservico);
				
				return status;
			}
		});
	}

	/**
	 * 
	 * Inserir novos dados na tabela Enderecoproduto
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoService#findEndereco(Endereco)
	 * 
	 * @author Arantes
	 * 
	 * @param ajustarEstoque
	 * @param enderecoproduto
	 * 
	 */
	private void inserirEnderecoProduto(AjustarEstoqueFiltro ajustarEstoque, Enderecoproduto enderecoproduto) {
		Endereco enderecoAux = enderecoService.findEndereco(ajustarEstoque.getEndereco());
		
		enderecoproduto.setEndereco(enderecoAux);
		if(enderecoproduto.getDtentrada()==null){
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
		}
		enderecoproduto.setQtdereservadaentrada(new Long(0));
		enderecoproduto.setQtdereservadasaida(new Long(0));
		enderecoproduto.setUma(Boolean.FALSE);
		
		this.saveOrUpdate(enderecoproduto);
		enderecoService.pAtualizarEndereco(enderecoAux);
	}

	/**
	 * 
	 * Atualiza os dados do produto, endereço e qtde da tabela Enderecoproduto
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#load(Enderecoproduto)
	 * 
	 * @author Arantes
	 * 
	 * @param ajustarEstoque
	 * @param enderecoproduto
	 * 
	 */
	private void atualizarEnderecoProduto(AjustarEstoqueFiltro ajustarEstoque, Enderecoproduto enderecoproduto) {
		Enderecoproduto enderecoprodutoAux = enderecoprodutoDAO.load(enderecoproduto);
		Endereco endereco = ajustarEstoque.getEndereco();
		
		enderecoproduto.setEndereco(ajustarEstoque.getEndereco());
		//enderecoproduto.setDtentrada(enderecoprodutoAux.getDtentrada());
		
		if (enderecoprodutoAux != null){
			enderecoproduto.setQtdereservadaentrada(enderecoprodutoAux.getQtdereservadaentrada());
			enderecoproduto.setQtdereservadasaida(enderecoprodutoAux.getQtdereservadasaida());			
		}else{
			enderecoproduto.setQtdereservadaentrada(0L);
			enderecoproduto.setQtdereservadasaida(0L);						
		}
	
		enderecoproduto.setUma(Boolean.FALSE);
		
		this.saveOrUpdate(enderecoproduto);
		enderecoService.pAtualizarEndereco(endereco);
		
	}
	
	/**
	 * 
	 * Método que valida se as qtdes inseridas não estão nulas.
	 * 
	 * @author Arantes
	 * 
	 * @param listaAjustarEstoque
	 * @return Boleaan
	 * 
	*/
	public String validarQtdesAjax(Set<AjustarEstoqueFiltro> listaAjustarEstoque) {
		StringBuilder erros = new StringBuilder();
		
		for (AjustarEstoqueFiltro ajustarEstoque : listaAjustarEstoque) {
			
			//So valida se tudo estiver preenchido
			if(ajustarEstoque.getEnderecoproduto() != null && ajustarEstoque.getEnderecoproduto().getProduto() != null && 
			   ajustarEstoque.getEnderecoproduto().getProduto().getCdproduto() != null && ajustarEstoque.getEnderecoproduto().getQtde() != null){
			
				//Endereço inválido, já foi pego pelo método de validar endereços
				if (!EnderecoAux.isEnderecoCompleto(ajustarEstoque.getEndereco()))
					continue;
				
				if (ajustarEstoque.getEnderecoproduto().getQtdereservadasaida() != null &&
						(ajustarEstoque.getEnderecoproduto().getQtde().compareTo(ajustarEstoque.getEnderecoproduto().getQtdereservadasaida()) < 0) ){
					
					erros.append("A quantidade para o endereço \"");
					erros.append(EnderecoAux.formatarEndereco(ajustarEstoque.getEndereco()));
					erros.append("\" deve ser maior ou igual a ");
					erros.append(ajustarEstoque.getEnderecoproduto().getQtdereservadasaida());
					erros.append(".\n");
				}
			}
		}
		
		return erros.toString();
	}
	
	/*Singleton*/
	private static EnderecoprodutoService instance;
	public static EnderecoprodutoService getInstance(){
		if(instance == null)
			instance = Neo.getObject(EnderecoprodutoService.class);
		
		return instance;
	}
	
	/**
	 * Atualiza a quantidade de estoque acrescentando o valor que está entrando.<br/>Este
	 * método irá acrescentar a quantidade em estoque (ENDERECOPRODUTO.QTDE) e
	 * reduzir a quantidade reservada de entrada
	 * (ENDERECOPRODUTO.QTDERESERVADAENTRADA) a partir da quantidade esperada
	 * (ORDEMSERVICOPRODUTOENDERECO.QTDE).<br/> Estas movimentações serão
	 * feitas no endereço de destino especificado no parâmetro <code>ospe</code>.
	 * 
	 * @author Leonardo Guimarães
	 * @author Giovane Freitas
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoprodutoService#findByEnderecoAndProduto(Endereco
	 *      endereco, Produto produto)
	 * 
	 * @param ospe Um ojbeto do tipo {@link Ordemservicoprodutoendereco} representando o endereço a ser atualizado.
	 * @param uma
	 * @throws InsercaoInvalidaException 
	 */
	public void atualizaEstoqueEntrada(Ordemservicoprodutoendereco ospe, boolean uma, boolean houveReserva) throws InsercaoInvalidaException {
		Produto produto = ospe.getOrdemservicoproduto().getProduto();

		Enderecoproduto enderecoproduto = findByEnderecoAndProduto(ospe.getEnderecodestino(), produto);
		if (enderecoproduto != null) {
			if (ospe.getQtde() != null) {
				enderecoproduto.setQtde(enderecoproduto.getQtde() + ospe.getQtde());
				
				if (houveReserva && enderecoproduto.getQtdereservadaentrada() != null)
					enderecoproduto.setQtdereservadaentrada(enderecoproduto.getQtdereservadaentrada() - ospe.getQtde());
			}
			enderecoproduto.setUma(uma);
		} else {
			EnderecoService.getInstance().validarInsercao(produto, ospe.getEnderecodestino());
			
			enderecoproduto = new Enderecoproduto();
			enderecoproduto.setQtde(ospe.getQtde());
			enderecoproduto.setUma(uma);
			enderecoproduto.setEndereco(ospe.getEnderecodestino());
			enderecoproduto.setProduto(produto);
			enderecoproduto.setQtdereservadaentrada(0L);
			enderecoproduto.setQtdereservadasaida(0L);
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
		}

		saveOrUpdateNoUseTransaction(enderecoproduto);
		enderecoService.pAtualizarEndereco(ospe.getEnderecodestino());
	}

	/**
	 * Atualiza a quantidade de estoque subtraindo o valor que está saindo.<br/>Este
	 * método irá subrair a quantidade em estoque (ENDERECOPRODUTO.QTDE) e
	 * reduzir a quantidade reservada de saida
	 * (ENDERECOPRODUTO.QTDERESERVADASAIDA) a partir da quantidade esperada
	 * (ORDEMSERVICOPRODUTOENDERECO.QTDE).<br/> Estas movimentações serão
	 * feitas no endereço de origem especificado no parâmetro <code>ospe</code>.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoprodutoService#findByEnderecoAndProduto(Endereco
	 *      endereco, Produto produto)
	 * 
	 * @param ospe Um ojbeto do tipo {@link Ordemservicoprodutoendereco} representando o endereço a ser atualizado.
	 * @param uma
	 * @throws InsercaoInvalidaException 
	 */
	public void atualizaEstoqueSaida(Ordemservicoprodutoendereco ospe, boolean uma, boolean houveReserva) throws InsercaoInvalidaException {
		Produto produto = ospe.getOrdemservicoproduto().getProduto();

		Enderecoproduto enderecoproduto = findByEnderecoAndProduto(ospe.getEnderecoorigem(), produto);
		if (enderecoproduto != null) {
			if (ospe.getQtde() != null) {
				enderecoproduto.setQtde(enderecoproduto.getQtde() - ospe.getQtde());
				
				if (houveReserva && enderecoproduto.getQtdereservadasaida() != null)
					enderecoproduto.setQtdereservadasaida(enderecoproduto.getQtdereservadasaida() - ospe.getQtde());
			}
			saveOrUpdateNoUseTransaction(enderecoproduto);
		}

		enderecoService.pAtualizarEndereco(ospe.getEnderecoorigem());
	}

	/**
	 * Atualiza a quantidade reservada de entrada acrescentando o valor que está entrando.<br/>Este
	 * método irá acrescentar a quantidade reservada de entrada (ENDERECOPRODUTO.QTDERESERVADAENTRADA)
	 * a partir da quantidade endereçada (ORDEMSERVICOPRODUTOENDERECO.QTDE).<br/> Estas movimentações serão
	 * feitas no endereço de destino especificado no parâmetro <code>ospe</code>.
	 * 
	 * @author Leonardo Guimarães
	 * @author Giovane Freitas
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoprodutoService#findByEnderecoAndProduto(Endereco
	 *      endereco, Produto produto)
	 * 
	 * @param ospe Um ojbeto do tipo {@link Ordemservicoprodutoendereco} representando o endereço a ser atualizado.
	 * @param uma
	 * @throws InsercaoInvalidaException 
	 */
	public void reservarEntrada(Ordemservicoprodutoendereco ospe, boolean uma) throws InsercaoInvalidaException {
		if (ospe == null || ospe.getQtde() == null)
			throw new WmsException("A quantidade não deve ser nula.");
		
		Produto produto = ospe.getOrdemservicoproduto().getProduto();
		Enderecoproduto enderecoproduto = findByEnderecoAndProduto(ospe.getEnderecodestino(), produto);
		if (enderecoproduto != null) {
			enderecoproduto.setQtdereservadaentrada(enderecoproduto.getQtdereservadaentrada() + ospe.getQtde());
			enderecoproduto.setUma(uma);
		} else {
			EnderecoService.getInstance().validarInsercao(produto, ospe.getEnderecodestino());
			
			enderecoproduto = new Enderecoproduto();
			enderecoproduto.setQtde(0L);
			enderecoproduto.setUma(uma);
			enderecoproduto.setEndereco(ospe.getEnderecodestino());
			enderecoproduto.setProduto(produto);
			enderecoproduto.setQtdereservadaentrada(ospe.getQtde());
			enderecoproduto.setQtdereservadasaida(0L);
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
		}

		Area area = AreaService.getInstance().findByEndereco(ospe.getEnderecodestino());
		Dadologistico dadologistico = DadologisticoService.getInstance().findByProduto(produto, area.getDeposito());
		
		saveOrUpdateNoUseTransaction(enderecoproduto);
		enderecoService.reservarEndereco(ospe.getEnderecodestino(), dadologistico.getLarguraexcedente());
	}
	
	/**
	 * Atualiza a quantidade reservada de saída.<br/>Este
	 * método irá acrescentar a quantidade reservada de saída (ENDERECOPRODUTO.QTDERESERVADASAIDA)
	 * a partir da quantidade endereçada (ORDEMSERVICOPRODUTOENDERECO.QTDE).<br/> Estas movimentações serão
	 * feitas no endereço de origem especificado no parâmetro <code>ospe</code>.
	 * 
	 * @author Leonardo Guimarães
	 * @author Giovane Freitas
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoprodutoService#findByEnderecoAndProduto(Endereco
	 *      endereco, Produto produto)
	 * 
	 * @param ospe Um ojbeto do tipo {@link Ordemservicoprodutoendereco} representando o endereço a ser atualizado.
	 * @param uma
	 * @throws InsercaoInvalidaException 
	 */
	public void reservarSaida(Ordemservicoprodutoendereco ospe, boolean uma) {
		if (ospe == null || ospe.getQtde() == null)
			throw new WmsException("A quantidade não deve ser nula.");
		
		Produto produto = ospe.getOrdemservicoproduto().getProduto();
		Enderecoproduto enderecoproduto = findByEnderecoAndProduto(ospe.getEnderecoorigem(), produto);
		if (enderecoproduto != null) {
			enderecoproduto.setQtdereservadasaida(enderecoproduto.getQtdereservadasaida() + ospe.getQtde());
			enderecoproduto.setUma(uma);
		} else {
			throw new WmsException("O endereço de origem não possui estoque para o produto especificado.");
		}
		
		saveOrUpdateNoUseTransaction(enderecoproduto);
		enderecoService.reservarEndereco(ospe.getEnderecoorigem(), false);
	}

	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @param produto
	 * @return
	 */
	public Enderecoproduto findByEnderecoAndProduto(Endereco endereco,Produto produto) {		
		return enderecoprodutoDAO.findByEnderecoAndProduto(endereco, produto);
	}
	
	/**
	 * Retorna com cdendereco carregado
	 * 
	 * @param bean
	 * @return
	 * @author Cíntia Nogueira
	 * @see  br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#loadWithEndereco(Enderecoproduto)
	 */
	public Enderecoproduto loadWithEndereco(Enderecoproduto bean){
		return enderecoprodutoDAO.loadWithEndereco(bean);
	}
	
	/**
	 * Verifica se há outro produto na lista de enderecoprodutos
	 * @param produto
	 * @param lista
	 * @return
	 * @author Cíntia Nogueira
	 */
	private boolean hasOutroProduto(Produto produto, List<Enderecoproduto> lista){
		for(Enderecoproduto enderecoproduto:lista){
			if(!enderecoproduto.getProduto().getCdproduto().equals(produto.getCdproduto())){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Problema encontrado na query, necessário inicializar a lista de endereços produtos
	 * manualmente
	 * @param listaEndereco
	 * @author Cíntia Nogueira
	 *  @see  br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#findByEnderecos(List)
	 */
	
	private void ajeitaListaEndereco(List<Endereco> listaEndereco, Produto produto){
		List<Enderecoproduto> listaEnderecoproduto = enderecoprodutoDAO.findByEnderecos(listaEndereco, produto);
		for(Endereco endereco:listaEndereco){
			ListSet<Enderecoproduto> listaEnderecoEndereco = new ListSet<Enderecoproduto>(Enderecoproduto.class);
			for(Enderecoproduto enderecoProduto:listaEnderecoproduto){
				if(endereco.getCdendereco().equals(enderecoProduto.getEndereco().getCdendereco())){
					listaEnderecoEndereco.add(enderecoProduto);
				}
			}
			if(!listaEnderecoEndereco.isEmpty()){
				endereco.setListaEnderecoproduto(listaEnderecoEndereco);
				listaEnderecoproduto.removeAll(listaEnderecoEndereco);
			}
		}
	}
	
	/**
	 * Retorna a quantidade comportada em cada apartamento
	 * @param produtoAux
	 * @return
	 * @author Cíntia Nogueira
	 */
	private Long getQuantPorApartamento(Produto produtoAux){
		Long quantidadePorApto=1L;
		if(produtoAux.getListaProdutoTipoPalete()!=null){
			for( Produtotipopalete produtotipopalete: produtoAux.getListaProdutoTipoPalete()){
				if(produtotipopalete.getPadrao()!=null && produtotipopalete.getPadrao()){
					quantidadePorApto = produtotipopalete.getCamada()*produtotipopalete.getLastro();
					break;
				}
			}
		}	
		return quantidadePorApto;
	}
	
	/**
	 * Faz operações necessárias para preencher e criar um novo produto endereço
	 * 
	 * @param enderecoaux
	 * @param produto
	 * @param quantidadeColocada
	 * @param quantidadePorApto
	 * @param quantidadeAdicionada
	 * @throws SQLException
	 * @author Cínta Nogueira
	 * @see br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#saveOrUpdate(Enderecoproduto)
	 * @see EnderecoService#pAtualizarEndereco(Endereco)
	 */
	private Long preencheNovoProdutoEndereco(Endereco enderecoaux, Produto produto, Long quantidadeColocada,
			Long quantidadePorApto, Long quantidadeAdicionada, boolean forcarAdicionar) {
		
		Enderecoproduto enderecoProduto = new Enderecoproduto();
		enderecoProduto.setEndereco(enderecoaux);
		enderecoProduto.setProduto(produto);
		enderecoProduto.setDtentrada(new Date(System.currentTimeMillis()));
		enderecoProduto.setQtdedestino(0L);
		enderecoProduto.setQtdereservadaentrada(0L);
		enderecoProduto.setQtdereservadasaida(0L);
		enderecoProduto.setUma(false);
		
		if (forcarAdicionar){
			enderecoProduto.setQtde(quantidadeAdicionada - quantidadeColocada);
			quantidadeColocada += quantidadeAdicionada - quantidadeColocada;			
		}else if (quantidadeColocada + quantidadePorApto > quantidadeAdicionada) {
			enderecoProduto.setQtde(quantidadeAdicionada - quantidadeColocada);
			quantidadeColocada += quantidadePorApto;
		} else {
			enderecoProduto.setQtde(quantidadePorApto);
			quantidadeColocada += quantidadePorApto;
		}
		enderecoprodutoDAO.saveOrUpdate(enderecoProduto);
		return quantidadeColocada;

	}
	
	/**
	 * Pega a quantidade total de produtos na lista
	 * @param enderecoaux
	 * @return
	 * @author Cíntia Nogueira
	 */
	private Long getTotalProduto(Endereco enderecoaux, Produto produto){
		Long totalProdutos=0L;
		for(Enderecoproduto enderecoprod : enderecoaux.getListaEnderecoproduto()){
			if(enderecoprod.getQtde()!=null && produto.equals(enderecoprod.getProduto())){
				totalProdutos+= enderecoprod.getQtde();
			}
			
		}
		return totalProdutos;
	}
	
	/**
	 * Faz operações necessárias para preencher um produtoendereço
	 * 
	 * @param enderecoproduto
	 * @param quantidadePorApto
	 * @param quantidadeAdicionada
	 * @param quantidadeColocada
	 * @throws SQLException
	 * @see br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#saveOrUpdate(Enderecoproduto)
	 * @see EnderecoService#pAtualizarEndereco(Endereco)
	 * @see #getTotalProduto(Endereco)
	 * @author Cíntia Nogueira
	 */
	private Long preencheProdutoEndereco(Enderecoproduto enderecoproduto, Long quantidadePorApto, Long quantidadeAdicionada,
			Long quantidadeColocada, boolean forcarAdicionar) {

		if (forcarAdicionar || (quantidadePorApto - enderecoproduto.getQtde() > 0)) {
			Long valorPodecolocar = quantidadePorApto - enderecoproduto.getQtde();
			
			if (forcarAdicionar){
				quantidadeColocada += quantidadeAdicionada;
				enderecoproduto.setQtde(quantidadeAdicionada + enderecoproduto.getQtde());				
			} if (valorPodecolocar < (quantidadeAdicionada - quantidadeColocada)) {
				quantidadeColocada += valorPodecolocar;
				enderecoproduto.setQtde(valorPodecolocar + enderecoproduto.getQtde());
			} else {
				enderecoproduto.setQtde(quantidadeAdicionada - quantidadeColocada + enderecoproduto.getQtde());
				quantidadeColocada += quantidadeAdicionada - quantidadeColocada;
			}
			
//			enderecoprodutoDAO.updateQtde(enderecoproduto);
		}
		return quantidadeColocada;
	}
	
	/**
	 * Preenche quantidades de produto em um prédio blocado
	 * 
	 * @param produto
	 * @param endereco
	 * @param quantidadeAdicionada
	 * @throws SQLException
	 * @author Cíntia Nogueira
	 * @see ProdutoService#loadWithNormaAndRestricaoNivel(Produto)
	 * @see EnderecoService#findEnderecoByPredio(br.com.linkcom.wms.geral.bean.Area,
	 *      Integer, Integer, String)
	 * @see #ajeitaListaEndereco(List)
	 * @see #hasOutroProduto(Produto, List)
	 * @see #preencheNovoProdutoEndereco(Endereco, Produto, Long, Long, Long,
	 *      Long)
	 * @see #preencheProdutoEndereco(Endereco, Long, Long, Long, Long)
	 */
	public void preenchePredioBlocado(Produto produto, Endereco endereco, Long quantidadeAdicionada) {
		Produto produtoAux = produtoService.loadWithNormaAndRestricaoNivel(produto);
		List<Endereco> todoPredio = enderecoService.findEnderecoByPredio(endereco.getArea(), endereco.getRua(),
				endereco.getPredio(), "endereco.apto desc, endereco.nivel");
		ajeitaListaEndereco(todoPredio, produto);
		Long quantidadePorApto = getQuantPorApartamento(produtoAux);
		Long quantidadeColocada = 0L;
		for (int i = 0; i < todoPredio.size(); i++) {
			Endereco enderecoaux = todoPredio.get(i);
			
			if ((quantidadeAdicionada - quantidadeColocada) <= 0) {
				break;
			}
			
			Enderecoproduto enderecoprodutoExistente = null;
			for(Enderecoproduto enderecoproduto : enderecoaux.getListaEnderecoproduto()){
				if(enderecoproduto.getProduto().getCdproduto().equals(produto.getCdproduto())){
					enderecoprodutoExistente = enderecoproduto;
					break;
				}
			}
			
			if (enderecoprodutoExistente == null) {
				quantidadeColocada = preencheNovoProdutoEndereco(enderecoaux, produto, quantidadeColocada,
						quantidadePorApto, quantidadeAdicionada, i == (todoPredio.size() - 1));

			} else {
				quantidadeColocada = preencheProdutoEndereco(enderecoprodutoExistente, quantidadePorApto, quantidadeAdicionada,
						quantidadeColocada, i == (todoPredio.size() - 1));
			}
			enderecoService.pAtualizarEndereco(enderecoaux);
		}

	}
	
	/**
	 * Retira produtos de um prédio blocado
	 * 
	 * @param produto
	 * @param endereco
	 * @param quantidadeRetirada
	 * @throws SQLException
	 * @author Cíntia Nogueira
	 * @see #getTotalProduto(Endereco)
	 * @see EnderecoService#findEnderecoByPredio(br.com.linkcom.wms.geral.bean.Area,
	 *      Integer, Integer, String)
	 * @see #ajeitaListaEndereco(List)
	 * @see #hasOutroProduto(Produto, List)
	 */
	public void retiraProdutoPredioBlocado(Produto produto, Endereco endereco, Long quantidadeRetirada)	{		
		List<Endereco> todoPredio = enderecoService.findEnderecoByPredio(endereco.getArea(),endereco.getRua(),
				endereco.getPredio(), "endereco.apto, endereco.nivel desc");
		ajeitaListaEndereco(todoPredio, produto);		
		Long quantidadeColocada=0L;		
		for(Endereco enderecoaux : todoPredio){						
			if((quantidadeRetirada-quantidadeColocada)==0){
				break;
			}	
			ArrayList<Enderecoproduto> listaEnderecoProduto = new ArrayList<Enderecoproduto>(enderecoaux.getListaEnderecoproduto());
			Long totalProdutoEndereco = getTotalProduto(enderecoaux, produto);
			if(totalProdutoEndereco<(quantidadeRetirada-quantidadeColocada)){			
				if(listaEnderecoProduto!=null){
					for(Enderecoproduto enderecoproduto: listaEnderecoProduto){
							enderecoproduto.setQtde(0L);
							enderecoprodutoDAO.delete(enderecoproduto);						
					}
				}
				
				quantidadeColocada+= totalProdutoEndereco;
			}else{
				Long quantASerRetirada= quantidadeRetirada-quantidadeColocada;
				quantidadeColocada+= quantASerRetirada;
				for(Enderecoproduto enderecoproduto: listaEnderecoProduto){
					if(quantASerRetirada.equals(0L)){
						break;
					}
					if((enderecoproduto.getQtde())>quantASerRetirada){
						enderecoproduto.setQtde(enderecoproduto.getQtde() -quantASerRetirada);
						enderecoprodutoDAO.updateQtde(enderecoproduto);
						quantASerRetirada=0L;
					}
					else{
						quantASerRetirada-=enderecoproduto.getQtde();
						enderecoproduto.setQtde(0L);
						enderecoprodutoDAO.delete(enderecoproduto);
					}
				}
			}					
		 //enderecoService.pAtualizarEndereco(enderecoaux);
		}
	}
	
	/**
	 * Verifica se o endereço está emprestado ou ocupado e tem o produto no endereço
	 * @param endereco
	 * @param produto
	 * @return
	 * @author Cíntia Nogueira
	 * @see EnderecoService#load(Endereco)
	 * @see br.com.linkcom.wms.geral.dao.EnderecoprodutoDAO#loadByEnderecoEProduto(Endereco, Produto)
	 */
	public boolean isEnderecoEmpResMesmoProduto(Endereco endereco, Produto produto){
		endereco = enderecoService.load(endereco);
		if(endereco!=null && (endereco.getEnderecostatus().getCdenderecostatus().equals(Enderecostatus.OCUPADO.getCdenderecostatus())
				|| endereco.getEnderecostatus().getCdenderecostatus().equals(Enderecostatus.RESERVADO.getCdenderecostatus()) )){
					Enderecoproduto enderecoProduto = enderecoprodutoDAO.loadByEnderecoEProduto(endereco, produto);
					if(enderecoProduto==null){
						return false;
					}
				}
		
		return true;
	}
	
	/**
	 * Método com referência no DAO
	 * 
	 * @param enderecoproduto
	 * @return
	 * @author Cintia Nogueira
	 */	
	public Enderecoproduto loadByEnderecoEProdutoUma(Endereco endereco,Produto produto) {
		return enderecoprodutoDAO.loadByEnderecoEProdutoUma(endereco, produto);
	}

	/**
	 * Subtrái a quantidade reservada de saída para um determinado carregamento.
	 * Este procedimento é utilizado ao cancelar um carregamento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param carregamento
	 */
	public void removerQtdeReservadaSaida(Carregamento carregamento) {
		List<Ordemservico> listaMapas = ordemservicoService.findByCarregamentoAndOrdemTipo(carregamento, Ordemtipo.MAPA_SEPARACAO);
		
		for (Ordemservico os : listaMapas){
			List<Ordemservicoproduto> listaOSP = ordemservicoprodutoService.findForDelete(os);
			
			for (Ordemservicoproduto osp : listaOSP){
				for (Ordemservicoprodutoendereco ospe : osp.getListaOrdemservicoprodutoendereco()){
					if (ospe.getEnderecoorigem() != null && ospe.getQtde() != null){
						this.removerQtdeReservada(ospe);
						enderecoService.pAtualizarEndereco(ospe.getEnderecoorigem());
					}
				}
			}
		}
	}


	/**
	 * Cancela as O.S. de reabastecimento de picking que ainda não foram executadas. Este método irá calcular as quantidades reservadas
	 * de saída e entrada dos endereços envolvidos no processo.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	public void cancelarReabastecimentoPicking(Carregamento carregamento) {
		List<Ordemservico> ordemList = ordemservicoService.findByCarregamentoAndOrdemTipo(carregamento, Ordemtipo.REABASTECIMENTO_PICKING);
		for (Ordemservico ordemservico : ordemList){
			//Só será feito o cancelamendo das ordens de reabastecimento que ainda não foram executadas.
			if (Ordemstatus.CANCELADO.equals(ordemservico.getOrdemstatus()) 
					|| Ordemstatus.FINALIZADO_DIVERGENCIA.equals(ordemservico.getOrdemstatus())
					|| Ordemstatus.FINALIZADO_SUCESSO.equals(ordemservico.getOrdemstatus()))
				continue;
			
			List<Ordemservicoprodutoendereco> movimentacoes = ordemservicoprodutoenderecoService.findByOrdemServico(ordemservico);
			for (Ordemservicoprodutoendereco ospe : movimentacoes){
				if (ospe.getOrdemservicoproduto().getProduto() == null || ospe.getQtde() == null || ospe.getQtde().equals(0L))
					continue;
				
				if (ospe.getEnderecoorigem() != null)
					enderecoprodutoDAO.removerQtdeReservadaSaida(ospe.getEnderecoorigem(), ospe.getOrdemservicoproduto().getProduto(), ospe.getQtde());
				if (ospe.getEnderecodestino() != null)
					enderecoprodutoDAO.removerQtdeReservadaEntrada(ospe.getEnderecodestino(), ospe.getOrdemservicoproduto().getProduto(), ospe.getQtde());
			}
			
			ordemservicoService.alteraStatus(ordemservico, Ordemstatus.CANCELADO);
		}
	}


	/**
	 * Remove as quantidades reservadas de entrada e de saída.
	 * 
	 * @param ospe
	 */
	public void removerQtdeReservada(Ordemservicoprodutoendereco ospe) {
		if (ospe.getEnderecodestino() != null){
			enderecoprodutoDAO.removerQtdeReservadaEntrada(ospe.getEnderecodestino(), ospe.getOrdemservicoproduto().getProduto(), ospe.getQtde());
			enderecoService.pAtualizarEndereco(ospe.getEnderecodestino());
		}
		
		if (ospe.getEnderecoorigem() != null){
			enderecoprodutoDAO.removerQtdeReservadaSaida(ospe.getEnderecoorigem(), ospe.getOrdemservicoproduto().getProduto(), ospe.getQtde());
			enderecoService.pAtualizarEndereco(ospe.getEnderecoorigem());
		}
	}


	/**
	 * Ajusta o estoque de um determinado produto em um dado endereço.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @param produto
	 * @param qtde
	 * @throws InsercaoInvalidaException 
	 */
	public void ajustarEstoque(Endereco endereco, Produto produto, Long qtde) throws InsercaoInvalidaException {
		Enderecoproduto enderecoproduto = findByEnderecoAndProduto(endereco, produto);
		
		if (enderecoproduto == null){
			EnderecoService.getInstance().validarInsercao(produto, endereco);
			
			enderecoproduto = new Enderecoproduto();
			enderecoproduto.setProduto(produto);
			enderecoproduto.setEndereco(endereco);
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
			enderecoproduto.setUma(false);
			enderecoproduto.setQtdereservadaentrada(0L);
			enderecoproduto.setQtdereservadasaida(0L);
			enderecoproduto.setQtde(qtde);
			this.saveOrUpdate(enderecoproduto);
			
			enderecoService.pAtualizarEndereco(enderecoproduto.getEndereco());
		}else{
			enderecoproduto.setQtde(qtde);
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
			this.saveOrUpdate(enderecoproduto);
		}
		
		enderecoService.pAtualizarEndereco(endereco);
	}

	/**
	 * Verifica se o endereço já possui um outro produto diferente do informado.
	 * 
	 * @author Giovane Freitas
	 * @param endereco O endereço que deve ser consultado.
	 * @param produto Produto usado para comparação.
	 * @return
	 */
	public boolean possuiOutroProduto(Endereco endereco, Produto produto) {
		return enderecoprodutoDAO.possuiOutroProduto(endereco, produto);
	}
	
	/**
	 * Verifica se em uma faixa de endereços existe algum com quantidade reservada de entrada ou de saída.
	 * 
	 * @author Giovane Freitas
	 * @param inicio
	 * @param fim
	 * @param lado 
	 * @return
	 */
	public boolean hasEnderecoReservado(Endereco inicio, Endereco fim, Enderecolado lado) {
		return enderecoprodutoDAO.hasEnderecoReservado(inicio, fim, lado);
	}
	
	/**
	 * Alimenta o estoque no endereço de Box
	 * 
	 * @author Giovane Freitas

	 * @param produto
	 * @param qtde
	 */
	public void alimentarEstoqueBox(Deposito deposito,Produto produto, Long qtde) {
		Endereco enderecoBox = enderecoService.findEnderecoBox(deposito);
		Enderecoproduto enderecoproduto = findByEnderecoAndProduto(enderecoBox, produto);
		
		if (enderecoproduto == null){
			enderecoproduto = new Enderecoproduto();
			enderecoproduto.setProduto(produto);
			enderecoproduto.setEndereco(enderecoBox);
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
			enderecoproduto.setUma(false);
			enderecoproduto.setQtdereservadaentrada(0L);
			enderecoproduto.setQtdereservadasaida(0L);
			enderecoproduto.setQtde(qtde);
			this.saveOrUpdate(enderecoproduto);
		}else{
			enderecoproduto.setQtde(enderecoproduto.getQtde() + qtde);
			enderecoproduto.setDtentrada(new Date(System.currentTimeMillis()));
			this.saveOrUpdate(enderecoproduto);
		}
		
		enderecoService.pAtualizarEndereco(enderecoBox);
	}

	/**
	 * Busca o saldo atual de um determinado produto para o depósito atual.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public long getSaldo(ConsultarEstoqueFiltro filtro) {
		return enderecoprodutoDAO.getSaldo(filtro);
	}


	/**
	 * Verifica se a saída pode ser reservada para o endereço de box.
	 * 
	 * @author Giovane Freitas
	 * @param ospe
	 * @param recebimento 
	 * @throws InsercaoInvalidaException 
	 */
	public void validarSaidaBox(Ordemservicoprodutoendereco ospe, Recebimento recebimento) throws InsercaoInvalidaException {
		if (RecebimentoService.getInstance().isConferenciaConcluida(recebimento)){
			Enderecoproduto enderecoproduto = enderecoprodutoDAO.findByEnderecoAndProduto(ospe.getEnderecoorigem(), ospe.getOrdemservicoproduto().getProduto());
			if (enderecoproduto == null || ( enderecoproduto.getQtde().longValue() - enderecoproduto.getQtdereservadasaida().longValue() < ospe.getQtde().longValue() ) ){
				throw new InsercaoInvalidaException("Não há saldo disponível na área 97 para esta movimentação.");
			}
		}
	}

}
