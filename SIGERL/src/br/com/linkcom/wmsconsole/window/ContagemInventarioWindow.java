package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
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
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.bean.Tipoestrutura;
import br.com.linkcom.wms.geral.bean.Tipopalete;
import br.com.linkcom.wms.geral.bean.vo.CodigobarrasVO;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService;
import br.com.linkcom.wms.geral.service.OrdemprodutoligacaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoenderecoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.ProdutotipopaleteService;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.ConferenciaCegaPapelFiltro;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

/**
 * Tela para contagem de inventário
 * 
 * @author Pedro Gonçalves
 *
 */
public class ContagemInventarioWindow extends ExecucaoOSWindow {
	
	private static final String TITULO = "Contagem de inventário";

	public enum Acao { RETORNAR, CANCELAR, FINALIZAR_OS, PROXIMO_ENDERECO }

	private Ordemservico ordemservico;
	private OrdemservicoUsuario ordemservicoUsuario;
	protected List<Ordemprodutohistorico> listaOPH;
	private List<Endereco> enderecos;
	private List<CodigobarrasVO> listaCodeBar;
	private Boolean fracionada;
	protected Ordemtipo tipoHabilitado = Ordemtipo.CONTAGEM_INVENTARIO;
	private Timestamp horaInicial;
	protected String mensagemProximoItem = "Próximo endereço";

	@Override
	public void draw() throws IOException {
		do {
			drawEsqueleto(DIGITE_0_PARA_SAIR);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			String valor = readLine("Endereço: ");
			
			if(valor.equals("0"))
				break;
			
			Endereco aux = null;
			try {
				 aux = EnderecoService.getInstance().prepareLoadEnderecoByCodigoEtiqueta(valor, deposito);
				 Ordemservico ordemservico = OrdemservicoService.getInstance().findOrdemServicoAbertaByEndereco(aux, this.tipoHabilitado.equals(Ordemtipo.CONTAGEM_INVENTARIO) ? true : false, deposito);
				 if(ordemservico == null || ordemservico.getCdordemservico() == null){
					 alertError("O.S. inexistente.");
					 continue;
				 }
				executarOrdem(ordemservico);
			} catch (Exception e) {
				alertError("Endereço inválido.");
			}
			
		} while (true);
	}
	
	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		if(!verificarOS(ordemservico))
			return;
		
		//Tudo ok. Associar a OS Ao usuário.
		associarUsuarioOS();
		alterarStatusOS();
		//coleta os dados da Os.
		collect();
	}

	/**
	 * 
	 */
	private void alterarStatusOS() {
		ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
		OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
	}

	private void collect() throws IOException {
		carregarListaOrdemProdutoHistorico(); //carrega as oph's
		carregarEnderecos();
		carregarCodigosDeBarra();
		horaInicial = new Timestamp(System.currentTimeMillis());
		
		for (int i = 0; i < enderecos.size();) {
			Endereco enderecoAtual =  enderecos.get(i);
			
			//verifica se a ordem foi finalizada ou cancelada(EM_ABERTO) na última iteração
			if (Ordemstatus.FINALIZADO_DIVERGENCIA.equals(ordemservico.getOrdemstatus()) || 
					Ordemstatus.FINALIZADO_SUCESSO.equals(ordemservico.getOrdemstatus()) ||
					Ordemstatus.AGUARDANDO_CONFIRMACAO.equals(ordemservico.getOrdemstatus()) ||
					Ordemstatus.EM_ABERTO.equals(ordemservico.getOrdemstatus())){
				break;
			}
			
			drawEsqueleto(null);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			makeOsHeader(); //adiciona o header
			
			//Mostrar endereço destino.
			makeEnderecoHeader(enderecoAtual);
			String codigo = readLine("Endereço:");
						
			boolean enderecoValido = false;
			
			// Rodrigo Alvarenga - 07/06/2011. 
			// A pedido do Christian, o "if" abaixo passou a considerar somente endereços BLOCADOS. 
			
			//if(enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.BLOCADO) || enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.PULMAO)){
			if(enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
				Endereco aux;
				try{
					aux = EnderecoService.getInstance().prepareLoadEnderecoByCodigoEtiqueta(codigo, deposito);
				} catch (Exception e) {
					alertError("Endereço inválido.");
					continue;
				}
				enderecoValido = EnderecoService.getInstance().predioEquals(enderecoAtual, aux);
			}else{
				Endereco aux;
				try{
					aux = EnderecoService.getInstance().loadEnderecoByCodigoEtiquetaExato(codigo, this.deposito);

					if (aux == null){
						alertError("Endereço inválido.");
						continue;
					}
				} catch (Exception e) {
					alertError("Endereço inválido.");
					continue;
				}

				enderecoValido = aux.equals(enderecoAtual);
			}
			
			if(!enderecoValido){
				alertError("Endereço inválido.");
				continue;
			}
			
			//inicia a coleta dos produtos
			collectProdutos(enderecoAtual);
			i++;
		}
		
		//verifica se a ordem ainda está em execução, se estiver devo finalizar, pois já percorreu todos os produtos.
		if (Ordemstatus.EM_EXECUCAO.equals(ordemservico.getOrdemstatus()) ){
			finalizarColeta();
		}
	}

	/**
	 * Mostra as opções para o usuário
	 * 
	 * @return
	 * @throws IOException
	 */
	protected Acao executarAcao() throws IOException {
		drawEsqueleto("");
		
		writeLine("Selecione a ação desejada.");
		writeLine("");
		writeLine("");
		writeLine("0 - Retornar");
		writeLine("1 - " + mensagemProximoItem );
		writeLine("");

		final Editfield confirmEdf = new Editfield(getTermIO(),"edf",1);		
		confirmEdf.registerInputValidator(new InputValidator(){

			public boolean validate(String str) {
				return "0".equals(str) || "1".equals(str);
			}
			
		});
		confirmEdf.run();
		String opcao = confirmEdf.getValue();
		
		if("0".equals(opcao)){
			return Acao.RETORNAR;
		}else if("1".equals(opcao)){
			return Acao.PROXIMO_ENDERECO;
		}
		
		return Acao.RETORNAR;
	}

	/**
	 * @throws IOException
	 */
	private void finalizarColeta() throws IOException {
		validarOrdemServico(ordemservico);
				
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				ordemservico.setListaOrdemProdutoHistorico(new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class, listaOPH));
				OrdemservicoService.getInstance().gravarContagemInventario(ordemservico, usuario, horaInicial, new Timestamp(System.currentTimeMillis()), true);
				
				if(ordemservicoUsuario != null) {
					ordemservicoUsuario.setDtfim(new Timestamp(System.currentTimeMillis()));
					OrdemservicousuarioService.getInstance().saveOrUpdateNoUseTransaction(ordemservicoUsuario);
				}
				
				boolean exigeDuasContagens = ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.EXIGIR_DUAS_CONTAGENS_INVENTARIO, null); 
				boolean isPrimeiraContagem = OrdemservicoService.getInstance().isPrimeiraOrdemServicoContagemInventario(ordemservico);
				
				if(Ordemstatus.FINALIZADO_DIVERGENCIA.equals(ordemservico.getOrdemstatus()) || (exigeDuasContagens && isPrimeiraContagem) ){
					OrdemservicoService.getInstance().criarContagemRecontagem(ordemservico, deposito);
				}

				return null;
			}
		});
		
		if(ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO)){
			writeOnCenter("A contagem foi finalizada com sucesso.", null, true, true);
		}else{
			writeOnCenter("A contagem foi finalizada com divergencias.", null, true, true);
		}
	}

	/**
	 * 
	 */
	private void carregarCodigosDeBarra() {
		listaCodeBar = OrdemprodutohistoricoService.getInstance().findAllBarCodeByOSInventario(ordemservico,deposito);
	}
	
	/**
	 * Desassocia a ordem de serviço do usuário logado no coletor.
	 * 
	 * @see br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService#resetQtdecoletor(Ordemservico)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#resetarQuantidades(Ordemservico)
	 */
	@SuppressWarnings("unused")
	private void cancelarExecucao() throws IOException{
		OrdemservicoprodutoService.getInstance().atualizarStatus(ordemservico, Ordemprodutostatus.NAO_CONCLUIDO);
		OrdemservicoprodutoService.getInstance().resetarQuantidades(ordemservico);
		OrdemservicousuarioService.getInstance().delete(ordemservicoUsuario);			
		
		//Resetando o status da O.S.
		ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
		OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
		
		if(tipoHabilitado.equals(Ordemtipo.CONTAGEM_INVENTARIO))
			excluirProdutosIncluidos();
		
		writeOnCenter("Operação cancelada.", null, true, true);
	}

	private void excluirProdutosIncluidos() {
		StringBuilder strBuilder = new StringBuilder();
		for (Ordemprodutohistorico oph : listaOPH){
			if (!oph.getOrdemservicoproduto().getCreatedByColetor())
				continue;
			
			if (strBuilder.length() > 0)
				strBuilder.append(",");
			
			strBuilder.append(oph.getOrdemservicoproduto().getCdordemservicoproduto());
		}
		
		if (strBuilder.length() > 0){
			String listaOSP = strBuilder.toString();
			OrdemservicoprodutoenderecoService.getInstance().deleteAllBy(listaOSP);
			OrdemprodutoligacaoService.getInstance().deleteAllBy(listaOSP);
			OrdemprodutohistoricoService.getInstance().deleteAllBy(listaOSP);
			OrdemservicoprodutoService.getInstance().deleteAllBy(listaOSP);
		}
	}
	
	protected void collectProdutos(Endereco enderecoAtual) throws IOException {
		do {
			drawEsqueleto(DIGITE_0_PARA_ACOES);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			makeOsHeader(); //adiciona o header
			
			//Mostrar endereço destino.
			makeEnderecoHeader(enderecoAtual);
			
			String codigo = readCodigoProduto();
			
			if(codigo == null || codigo.trim().isEmpty()) {
				//o usuário não digitou nada e pressionou Enter
				continue;
			}
			
			if(exibirMenu){
				Acao acaoExecutada = executarAcao();
				
				if (Acao.PROXIMO_ENDERECO.equals(acaoExecutada)){
					finalizarOSP(enderecoAtual);
					break;
				}else if (Acao.FINALIZAR_OS.equals(acaoExecutada) || Acao.CANCELAR.equals(acaoExecutada))
					break;
				else if (Acao.RETORNAR.equals(acaoExecutada))
					continue;
			}

			
			Ordemprodutohistorico produtoPertence = produtoPertence(codigo, enderecoAtual);
			
			if(produtoPertence == null){
				Produto produtoEncontrado = ProdutoService.getInstance().findProdutoByBarcode(codigo.toUpperCase(), deposito, !ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, deposito));
				
				if (produtoEncontrado != null){
					//Verificando se não existe volume, se o usuário digitou um código de produto que possui volumes
					//devo considerar inválido, pois neste caso ele é obrigado a digitar o código do volume e não
					//do produto principal.
					List<Produto> volumes = ProdutoService.getInstance().findVolumes(produtoEncontrado);
					if (volumes != null && volumes.size() > 0)
						produtoEncontrado = null;
				}
				
				if(produtoEncontrado == null) {
					alertError("Produto inválido.");
					continue;
				} else {
					Ordemprodutohistorico enderecoSemProduto = findEnderecoSemProduto(enderecoAtual);
					try {
						produtoPertence = addProduto(enderecoAtual, enderecoSemProduto != null ? enderecoSemProduto : produtoPertence,produtoEncontrado);
					} catch (InsercaoInvalidaException e) {
						alertError(e.getMessage());
						continue;
					}
				}
			}
			
			if(produtoPertence.getOrdemservicoproduto().getProduto().getModificado() == null || !produtoPertence.getOrdemservicoproduto().getProduto().getModificado()){
				//carrega o produto caso não tenha sido carregado ainda.
				produtoPertence.getOrdemservicoproduto().setProduto(ProdutoService.getInstance().findDadosLogisticosProdutoRF(produtoPertence.getOrdemservicoproduto().getProduto(), deposito));
				produtoPertence.getOrdemservicoproduto().getProduto().setModificado(true);
			}
			
			coletarInformacoesProduto(enderecoAtual, produtoPertence);
			
			//se o endereço é de picking deve coletar apenas um produto
			if (enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.PICKING))
				return;
			
		} while (true);
	}

	/**
	 * Finaliza o OrdemServicoProduto quando o usuário troca de endereço, para que ao reconectar o 
	 * mesmo item não seja pedido outra vez.
	 * 
	 * @author Giovane Freitas
	 * @param enderecoAtual
	 */
	protected void finalizarOSP(Endereco enderecoAtual) {
		for (Ordemprodutohistorico oph : listaOPH) {
			
			List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco = oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco();
			for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : listaOrdemservicoprodutoendereco) {
				if(isEnderecoIgual(ordemservicoprodutoendereco.getEnderecodestino(), enderecoAtual)){
					atualizaOSP(oph);
				}
			}
		}
	}
	
	/**
	 * Atualiza o status do OrdemServicoProduto
	 * 
	 * @author Giovane Freitas
	 * @param enderecoAtual
	 */
	protected void atualizaOSP(Ordemprodutohistorico oph) {
		validarOrdemServico(ordemservico);
		
		Long qtdeesperada = oph.getOrdemservicoproduto().getQtdeesperada();
		
		if ((oph.getQtde() == null && qtdeesperada != null && qtdeesperada > 0L) || (oph.getQtde() != null  && !oph.getQtde().equals(qtdeesperada)))
			oph.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);
		else
			oph.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
			
		OrdemservicoprodutoService.getInstance().atualizarStatus(oph.getOrdemservicoproduto());
	}

	/**
	 * Procura pelo {@link Ordemprodutohistorico} associado ao endereço e que não possui um 
	 * produto associado.
	 * 
	 * @param endereco
	 * @return
	 */
	private Ordemprodutohistorico findEnderecoSemProduto(Endereco endereco) {
		for (Ordemprodutohistorico oph : listaOPH) {
			
			List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco = oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco();
			for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : listaOrdemservicoprodutoendereco) {
				if(oph.getOrdemservicoproduto().getProduto() == null && endereco.equals(ordemservicoprodutoendereco.getEnderecodestino())){
					return oph;
				}
			}
		}

		return null;
	}

	/**
	 * @param enderecoAtual
	 * @param produtoPertence
	 * @return
	 * @throws IOException
	 */
	protected void coletarInformacoesProduto(Endereco enderecoAtual,Ordemprodutohistorico produtoPertence) throws IOException {
		Tipoestrutura tipoestrutura = enderecoAtual.getTipoestrutura();
		
		drawEsqueleto(null);
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		
		makeOsHeader();
		makeEnderecoHeader(enderecoAtual);
		showInfoProduto(produtoPertence,tipoestrutura, enderecoAtual);
		if(collectQte(produtoPertence,tipoestrutura, enderecoAtual)){
			drawEsqueleto(DIGITE_0_PARA_ACOES);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			makeOsHeader();
			makeEnderecoHeader(enderecoAtual);
			showInfoProduto(produtoPertence,tipoestrutura, enderecoAtual);
			if(fracionada || enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.PICKING))
				writeLine("Qtde. Frac.: "+produtoPertence.getQtde());
			 else 
				writeLine("Qtde. Emb.: "+ produtoPertence.getQtde());
		}
	}

	/**
	 * @param enderecoAtual
	 * @param oph
	 * @param produtoEncontrado
	 * @throws InsercaoInvalidaException 
	 */
	protected Ordemprodutohistorico addProduto(Endereco enderecoAtual,Ordemprodutohistorico oph, Produto produtoEncontrado) throws InsercaoInvalidaException {
		validarOrdemServico(ordemservico);
		
		EnderecoService.getInstance().validarInsercao(produtoEncontrado, enderecoAtual);
		
		produtoEncontrado.setModificado(true);
		
		//Tenta recuperar uma Ordemprodutohistorico com a Ordemservicoproduto para não criar uma nova OPH e OSP e impactar no Planilha de Contagem.
		if(oph == null){
			oph = OrdemprodutohistoricoService.getInstance().findByEnderecoProdutoOrdemServico(enderecoAtual,produtoEncontrado,ordemservico);
		}
		
		//verifica se não era esperado nenhum produto, porque se não era esperado 
		//não irei criar uma nova OSP e sim atualizar a existente
		if (oph != null && oph.getOrdemservicoproduto().getProduto() == null){
			oph.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
			oph.getOrdemservicoproduto().setQtdeesperada(0L);
			oph.getOrdemservicoproduto().setProduto(produtoEncontrado);
			oph.getOrdemservicoproduto().setCreatedByColetor(false);
			OrdemservicoprodutoService.getInstance().saveOrUpdate(oph.getOrdemservicoproduto());
			
			CodigobarrasVO vo = new CodigobarrasVO();
			String codigobarras = "";
			try {
				codigobarras = extractCodeBar(oph, produtoEncontrado);
			} catch (Exception e) {e.printStackTrace();System.out.println("erro!");}
			vo.setCodigo(codigobarras);
			vo.setProduto(produtoEncontrado);
			vo.setCodigoProduto(produtoEncontrado.getCodigo());
			listaCodeBar.add(vo);
			
			return oph;
		}else{
			//salva a osp
			Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto();
			ordemservicoproduto.setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
			ordemservicoproduto.setQtdeesperada(0l);
			ordemservicoproduto.setProduto(produtoEncontrado);
			ordemservicoproduto.setCreatedByColetor(true);
			OrdemservicoprodutoService.getInstance().saveOrUpdate(ordemservicoproduto);
			
			//salva a opl
			Ordemprodutoligacao ordemprodutoligacao = new Ordemprodutoligacao();
			ordemprodutoligacao.setOrdemservico(ordemservico);
			ordemprodutoligacao.setOrdemservicoproduto(ordemservicoproduto);
			OrdemprodutoligacaoService.getInstance().saveOrUpdate(ordemprodutoligacao);
			
			//salva a ospe
			Ordemservicoprodutoendereco ordemservicoprodutoendereco = new Ordemservicoprodutoendereco();
			ordemservicoprodutoendereco.setEnderecodestino(enderecoAtual);
			ordemservicoprodutoendereco.setOrdemservicoproduto(ordemservicoproduto);
			ordemservicoprodutoendereco.setQtde(0l);
			OrdemservicoprodutoenderecoService.getInstance().saveOrUpdate(ordemservicoprodutoendereco);
			
			//Carrega a ordem produto histórico.
			Ordemprodutohistorico ordemprodutohistorico = OrdemprodutohistoricoService.getInstance().findByOSP(ordemservicoproduto);
			ordemprodutohistorico.setOrdemservicoproduto(ordemservicoproduto);
			
			ordemservicoproduto.getListaOrdemservicoprodutoendereco().add(ordemservicoprodutoendereco);
			
			listaOPH.add(ordemprodutohistorico);
			CodigobarrasVO vo = new CodigobarrasVO();
			String codigobarras = "";
			try {
				codigobarras = extractCodeBar(oph, produtoEncontrado);
			} catch (Exception e) {e.printStackTrace();System.out.println("erro!");}
			vo.setCodigo(codigobarras);
			vo.setProduto(produtoEncontrado);
			vo.setCodigoProduto(produtoEncontrado.getCodigo());
			listaCodeBar.add(vo);
			
			return ordemprodutohistorico;
		}
	}
	

	/**
	 * Faz a coleta da quantidade de acordo com o padrão.
	 * 
	 * Modo: 
	 * & Quantidade
	 * 	 Coleta padrão: Pega a quantidade de produtos por embalagem e multiplica.
	 *   Coleta fracionada: Quantidade é incrementada diretamente.
	 *  
	 * & Avaria
	 *   Coleta fracionada: (*Somente possui este modo para o modo de coleta de avarias.)
	 *
	 * Se o produto possui mais de um tipo de palete cadastrado, é dado a opção de alterar o tipo de palete.
	 * @see #makeMenuTipoPalete(Produto)
	 * 
	 * @param produtoPertence
	 * @throws IOException
	 */
	protected boolean collectQte(Ordemprodutohistorico produtoPertence, Tipoestrutura tipoestrutura, Endereco enderecoAtual) throws IOException{
		//entrada de dados para a quantidade
		
		String label = "";

		Produtoembalagem embalagem = getEmbalagemCompra(produtoPertence.getOrdemservicoproduto().getProduto());
		
		long qtdeEsperada = produtoPertence.getOrdemservicoproduto().getQtdeesperada() != null ? produtoPertence.getOrdemservicoproduto().getQtdeesperada() : 0L;
		
		if (fracionada || enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.PICKING) || (qtdeEsperada % embalagem.getQtde()) != 0L){
			embalagem = getMenorEmbalagem(produtoPertence.getOrdemservicoproduto().getProduto());
			label = "Qtde. Frac.: ";			
		}else{
			if(embalagem == null){
				//produto não possui embalagem de recebimento. e será coletado neste modo de coleta. Pressione enter para continuar
				writeLine("Este produto não possui embalagem de recebimento. ");
				writeLine("");
				writeLine("Pressione ENTER para continuar");
				read();
				
				return false;
			}
			
			label = "Qtde. Emb.: ";
		}
		
		long coletado = readInteger(label);
		
		if (produtoPertence.getQtde() == null)
			produtoPertence.setQtde(0L);
			
		//coleta padrão. Pega a quantidade de produtos por embalagem e multiplica. Assim gera a unidade.
		if(fracionada || enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.PICKING)){
			Long qtderecebida = produtoPertence.getQtde();
			if(qtderecebida == null)
				qtderecebida = 0l;
			produtoPertence.setQtde(coletado + qtderecebida);
		} else {
			Long qtdeEmbalagem = embalagem != null ? embalagem.getQtde() : 1L;			
			produtoPertence.setQtde((qtdeEmbalagem * coletado) + produtoPertence.getQtde());
		}
		
		if(produtoPertence.getQtde() == null)
			produtoPertence.setQtde(0l);
		if(produtoPertence.getQtdefracionada() == null)
			produtoPertence.setQtdefracionada(0l);
		if(produtoPertence.getQtdeavaria() == null)
			produtoPertence.setQtdeavaria(0l);
		
		OrdemprodutohistoricoService.getInstance().atualizarQuantidades(produtoPertence);
				
		writeLine("");
		writeLine("");
		
		if(produtoPertence.getOrdemservicoproduto().getProduto().getListaProdutoTipoPalete() != null && 
			produtoPertence.getOrdemservicoproduto().getProduto().getListaProdutoTipoPalete().size() > 1 &&
			tipoestrutura.equals(Tipoestrutura.BLOCADO)){
			if (confirmAction("Trocar o palete? ")){
				makeMenuTipoPalete(produtoPertence.getOrdemservicoproduto().getProduto());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Dá a opção ao usuário de alterar o tipo de palete.
	 * 
	 * @throws IOException
	 */
	private void makeMenuTipoPalete(Produto produto) throws IOException{
		drawEsqueleto(DIGITE_0_PARA_SAIR);
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		
		writeLine("Selecione um tipo de palete: ");
		writeLine("");
		
		List<Produtotipopalete> listaProdutoTipoPalete = produto.getListaProdutoTipoPalete();
		final HashMap<Integer, Produtotipopalete> mapa = new HashMap<Integer, Produtotipopalete>();
		
		writeLine("0 - Voltar");
				
		int i = 1;
		for (Produtotipopalete produtotipopalete : listaProdutoTipoPalete) {
			mapa.put(i, produtotipopalete);
			writeLine((i++)+" - "+produtotipopalete.getTipopalete().getNome());
		}
		
		writeLine("");
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
		optDep.registerInputValidator(new InputValidator(){
			public boolean validate(String str) {
				try{
					int option = Integer.parseInt(str);
					if(option <= mapa.size() && option >= 0){
						return true;
					}
				} catch (Exception e){
					return false;
				}
				return false;
			}
		});
		optDep.run();

		writeLine("");
		
		String valorSelecionado = optDep.getValue();
		
		if("0".equals(valorSelecionado))
			return;
		
		for (Produtotipopalete produtotipopalete : listaProdutoTipoPalete) {
			produtotipopalete.setAlterado(false);
		}
		
		Produtotipopalete produtotipopalete = mapa.get(Integer.parseInt(valorSelecionado));
		produtotipopalete.setAlterado(true);
		
		writeLine("");
		writeLine("Palete selecionado:"+BasicTerminalIO.CRLF);
		writeLine(produtotipopalete.getTipopalete().getNome());
		read();
	}
	
	/**
	 * Pega a embalagem de compra do produto. Caso o produto não possua uma embalagem para recebimento 
	 * é retornado null.
	 * 
	 * @param produto
	 * @return
	 */
	private Produtoembalagem getEmbalagemCompra(Produto produto){
		List<Produtoembalagem> listaProdutoEmbalagem = produto.getListaProdutoEmbalagem();
		for (Produtoembalagem produtoembalagem : listaProdutoEmbalagem) {
			if(produtoembalagem.getCompra() != null && produtoembalagem.getCompra())
				return produtoembalagem;
		}
		return null;
	}

	/**
	 * Pega a embalagem de compra do produto. Caso o produto não possua uma embalagem para recebimento 
	 * é retornado null.
	 * 
	 * @param produto
	 * @return
	 */
	private Produtoembalagem getMenorEmbalagem(Produto produto){
		Produtoembalagem menorEmbalagem = null;
		
		List<Produtoembalagem> listaProdutoEmbalagem = produto.getListaProdutoEmbalagem();
		for (Produtoembalagem produtoembalagem : listaProdutoEmbalagem) {
			if(menorEmbalagem == null || produtoembalagem.getQtde().compareTo(menorEmbalagem.getQtde()) < 0)
				menorEmbalagem = produtoembalagem;
		}
		
		return menorEmbalagem;
	}

	/**
	 * Exibe as informações do produto da osp bipada.
	 * 
	 * @param produtoPertence
	 * @param enderecoAtual 
	 * @return false - Caso os dados logísticos estejam ok.
	 *         true - Caso falte algum dado logístico.
	 */
	private boolean showInfoProduto(Ordemprodutohistorico produtoPertence, Tipoestrutura tipoestrutura, Endereco enderecoAtual) throws IOException {
		boolean error = false;
		
		Produto produto = produtoPertence.getOrdemservicoproduto().getProduto();
		
		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {error=true;}
		
		writeLine("Código: " + codigoP);
		writeLine("Descrição: " + produto.getDescriptionProperty());
		
		if(produtoPertence.getCodigoBarrasIndex() != null){
			String codigobarras = "";
			try {
				codigobarras = extractCodeBar(produtoPertence, produto);
			} catch (Exception e) {error=true;}
			
			writeLine("Código barras: " + codigobarras);
		}
		String norma = "";
		String tipopalete = "";
		Tipopalete tipopaleteSelecionado = null;
		try {
			List<Produtotipopalete> lista = ProdutotipopaleteService.getInstance().findByProduto(produto,deposito);
			norma = produto.getListaProdutoTipoPalete().get(0).getLastro() + " x " + produto.getListaProdutoTipoPalete().get(0).getCamada();
			//verifica se possui algum alterado
			for (Produtotipopalete produtotipopalete : lista) {
				if(produtotipopalete.getAlterado() != null && produtotipopalete.getAlterado()){
					tipopalete = produtotipopalete.getTipopalete().getNome() + " *";
					tipopaleteSelecionado = produtotipopalete.getTipopalete();
					norma = produtotipopalete.getLastro() + " x "+produtotipopalete.getCamada() + " *";
					//ao processar a OSP, salvar tbm o tipo de palete.
					
					produtoPertence.getOrdemservicoproduto().setTipopalete(produtotipopalete.getTipopalete());
					break;
				}
			}
			//se não foi alterado ainda, carregar o default
			if("".equals(tipopalete)){
				for (Produtotipopalete produtotipopalete : lista) {
					if(produtotipopalete.getPadrao() != null && produtotipopalete.getPadrao()){
						tipopalete = produtotipopalete.getTipopalete().getNome();
						norma = produtotipopalete.getLastro() + " x "+produtotipopalete.getCamada();
						produtoPertence.getOrdemservicoproduto().setTipopalete(produtotipopalete.getTipopalete());
						tipopaleteSelecionado = produtotipopalete.getTipopalete();
						break;
					}
				}
			}
		} catch (Exception e) {error=true;}
		
		if(tipoestrutura != null && tipoestrutura.equals(Tipoestrutura.BLOCADO)) {
			writeLine("Tipo palete: "+ tipopalete);
		}
		
		writeLine("Norma: "+norma);
		
		long qtdeEsperada = produtoPertence.getOrdemservicoproduto().getQtdeesperada() != null ? produtoPertence.getOrdemservicoproduto().getQtdeesperada() : 0L;
		
		Produtoembalagem embalagem = getEmbalagemCompra(produto);			
		if (fracionada || enderecoAtual.getEnderecofuncao().equals(Enderecofuncao.PICKING) || (qtdeEsperada % embalagem.getQtde()) != 0L){
			embalagem = getMenorEmbalagem(produto);
		}
		
		if (embalagem != null)
			writeLine("Embalagem: " + embalagem.getDescricao() + " - " + embalagem.getQtde());
		
		writeSeparator();
		
		if(tipopaleteSelecionado != null) {
			produtoPertence.getOrdemservicoproduto().setTipopalete(tipopaleteSelecionado);
			OrdemservicoprodutoService.getInstance().updateTipopaleteordemservicoproduto(produtoPertence.getOrdemservicoproduto());
		}
		
		return error;
	}

	/**
	 * @param produtoPertence
	 * @param produto
	 * @return
	 */
	private String extractCodeBar(Ordemprodutohistorico produtoPertence, Produto produto) {
		List<Produtocodigobarras> listaProdutoCodigoDeBarras = produto.getListaProdutoCodigoDeBarras();
		Integer idx = 0;
		if(listaProdutoCodigoDeBarras.size() > 1){
			produtoPertence.setCodigoBarrasIndex(0);
			//encontra o código de barras na lista de VO
			int i = 0;
			for (Produtocodigobarras produtocodigobarras : listaProdutoCodigoDeBarras) {
				if(produtocodigobarras.getInterno()) {
					idx = i;
					break;
				}
				i++;
			}
		}
		
		String codigobarras = null;
		if (idx >= 0 && idx < listaProdutoCodigoDeBarras.size())
			codigobarras = listaProdutoCodigoDeBarras.get(idx).getCodigo();
		return codigobarras;
	}
	
	/**
	 * Verifica se o código de barras bipado(produto) pertence a etiqueta bipada anteriormente.
	 * 
	 * @see #existeProdutoEtiqueta(Ordemprodutohistorico, List)
	 * 
	 * @param codigo
	 * @param etiquetaexpedicaoBipe
	 * 
	 * @return null caso não tenha sido encontrado nenhuma ocorrência.
	 */
	protected Ordemprodutohistorico produtoPertence(String codigo, Endereco endereco){
		boolean aceitaCodigoInterno = !ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito);

		List<CodigobarrasVO> listaVO = new ArrayList<CodigobarrasVO>();
		for (CodigobarrasVO vo : listaCodeBar) {
			String codigoProduto = vo.getCodigoProduto();
			
			if(codigo.trim().equalsIgnoreCase(vo.getCodigo()) || (aceitaCodigoInterno && codigo.trim().equalsIgnoreCase(codigoProduto)))
				listaVO.add(vo);
		}
		
		for (Ordemprodutohistorico oph : listaOPH) {
			
			List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco = oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco();
			for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : listaOrdemservicoprodutoendereco) {
				//caso a etiqueta expedição seja nula, ignorar ela e pegar a primeira que aparecer.
				if(isEnderecoIgual(ordemservicoprodutoendereco.getEnderecodestino(), endereco)){
					CodigobarrasVO existeProdutoEndereco = existeProdutoEtiqueta(oph,listaVO);
					if(existeProdutoEndereco != null){
						return oph;
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * Verifica se dois endereços são iguais, quando tratar de endereço blocado a validação será feita somente até o Prédio.
	 * @param enderecodestino
	 * @param endereco
	 * @return
	 */
	private boolean isEnderecoIgual(Endereco enderecodestino, Endereco endereco) {
		if (enderecodestino == null || endereco == null)
			return false;
		
		if (Enderecofuncao.BLOCADO.equals(enderecodestino.getEnderecofuncao()) && Enderecofuncao.BLOCADO.equals(endereco.getEnderecofuncao()))
			return enderecodestino.getEndereco().substring(0, 7).equals(endereco.getEndereco().substring(0, 7));
		else
			return enderecodestino.equals(endereco);
	}

	/**
	 * Verifica se o produto pertence à etiqueta.
	 * @param oph
	 * @param listaVO
	 * @return
	 */
	private CodigobarrasVO existeProdutoEtiqueta(Ordemprodutohistorico oph, List<CodigobarrasVO> listaVO) {
		Integer cdproduto = 0;
		for (CodigobarrasVO codigobarrasVO : listaVO) {
			cdproduto = codigobarrasVO.getProduto().getCdproduto();
			
			if(oph.getOrdemservicoproduto().getProduto() != null && oph.getOrdemservicoproduto().getProduto().getCdproduto().equals(cdproduto)) {
				return codigobarrasVO;
			}
		}
		return null;
	}

	/**
	 * Carrega os endereços destino destintos dos itens que ainda não foram executados.
	 * 
	 */
	private void carregarEnderecos() {
		enderecos = new ArrayList<Endereco>();
		
		//vou criar uma lista de strings para que cada prédio blocado só seja adicionado uma vez.
		List<String> enderecosAdicionados = new ArrayList<String>();
		
		for (Ordemprodutohistorico oph : listaOPH) {
			//Vou ignorar os OrdemServicoProduto que já foram concluídos
			if (!oph.getOrdemservicoproduto().getOrdemprodutostatus().equals(Ordemprodutostatus.NAO_CONCLUIDO))
				continue;
			

			//Ignorando os itens que já foram contados
//			Iterator<Ordemprodutohistorico> iterator = listaOPH.iterator();
//			while (iterator.hasNext()){
//				Ordemprodutohistorico oph = iterator.next();
//				if ((oph.getQtde() != null && oph.getQtde() > 0) || (oph.getQtdefracionada() != null && oph.getQtdefracionada() > 0))
//					continue;
//			}
			
			List<Ordemservicoprodutoendereco> listaOrdemservicoprodutoendereco = oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco();
			for (Ordemservicoprodutoendereco ordemservicoprodutoendereco : listaOrdemservicoprodutoendereco) {
				Endereco enderecodestino = ordemservicoprodutoendereco.getEnderecodestino();
				if (!enderecosAdicionados.contains(enderecodestino.getEnderecoArea())){
					enderecos.add(enderecodestino);
					enderecosAdicionados.add(enderecodestino.getEnderecoArea());
				}
			}
		}
	}

	/**
	 * 
	 */
	private void carregarListaOrdemProdutoHistorico() {
		listaOPH = OrdemprodutohistoricoService.getInstance().findForLancarDados(ordemservico);
		listaOPH = new ArrayList<Ordemprodutohistorico>(OrdemprodutohistoricoService.getInstance().agrupaOrdemProdutoHistoricoBlocado(new ListSet<Ordemprodutohistorico>(Ordemprodutohistorico.class, listaOPH)));
	}

	/**
	 * Monta o Header da OS.
	 * @throws IOException
	 */
	protected void makeOsHeader() throws IOException {
		writeLine("O.S.: "+ordemservico.getCdordemservico());
		writeSeparator();
	}
	
	@Override
	public String getTitulo() {
		return TITULO;
	}

	/**
	 * Monta o Header do endereço.
	 * @throws IOException
	 */
	protected void makeEnderecoHeader(Endereco endereco) throws IOException {
		writeLine("End.: "+endereco.getEnderecoArea());
		writeSeparator();
	}

	/**
	 * Verifica se a ordem de serviço é válida.
	 * 
	 * @param osStr
	 * @throws IOException
	 * @return true - caso ela possa ser bipada.
	 * 		   false - caso não possa.
	 */
	protected Boolean verificarOS(Ordemservico ordem) throws IOException {
		ordemservico = OrdemservicoService.getInstance().loadAllOSInfo(ordem, usuario);

		if(ordemservico == null || !ordemservico.getOrdemtipo().equals(tipoHabilitado)){
			alertError("O.S. Inválida.");
			ordemservico = null;
			return false;
		}
		
		fracionada = ordemservico.getInventariolote().getFracionada();
		Boolean verificaOsAberta = verificaOsAberta();
		if(!verificaOsAberta){
			alertError("O.S. associada a outro conferente.");
			ordemservico = null;
			return false;
		}
			
		
		return true;
	}
	
	/**
	 * Verifica se o usuário pode coletar esta OS. Caso o usuário tenha perdido a conexão, ele pode
	 * reconectar-se a mesma os.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#loadByOS(Ordemservico)
	 * 
	 * @return true - Caso este usuário possa coletar esta OS.
	 *         false - O usuário não pode coletar essa OS.
	 */
	private Boolean verificaOsAberta(){
		this.ordemservicoUsuario = OrdemservicousuarioService.getInstance().findByOrdemservico(ordemservico);
		if(ordemservicoUsuario == null)
			return true;
		else{
			if(ordemservicoUsuario.getUsuario().getCdpessoa().equals(usuario.getCdpessoa())){
				return true;
			} else { 
				this.ordemservicoUsuario = null;
				return false;
			}
		}
	}
	
	/**
	 * Associa o usuário que está logado no box do recebimento, e amarra ele a última ordem de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#salvarOrdemServicoUsuarioForConferencia(ConferenciaCegaPapelFiltro filtro)
	 */
	private void associarUsuarioOS(){
		if(ordemservicoUsuario == null) {
			OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
			ordemservicoUsuario.setDtinicio(new Timestamp(System.currentTimeMillis()));
			ordemservicoUsuario.setOrdemservico(ordemservico);
			ordemservicoUsuario.setUsuario(usuario);
			OrdemservicousuarioService.getInstance().saveOrUpdate(ordemservicoUsuario);
			this.ordemservicoUsuario = ordemservicoUsuario;
		}
	}
	
	protected void validarOrdemServico(Ordemservico ordemservico) {
		Ordemservico ordemAux = OrdemservicoService.getInstance().load(ordemservico);
		if (ordemAux == null)
			throw new WmsException("O inventário foi cancelado.");
		if (ordemAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA) || ordemAux.getOrdemstatus().equals(Ordemstatus.FINALIZADO_SUCESSO))
			throw new WmsException("Esta ordem de contagem já foi finalizada.");
		if (ordemAux.getOrdemstatus().equals(Ordemstatus.CANCELADO) )
			throw new WmsException("Esta ordem de contagem foi cancelada.");
			
	}
	
}
