package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;

import org.apache.commons.lang.math.NumberUtils;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamentohistorico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Embalagemexpedicao;
import br.com.linkcom.wms.geral.bean.Embalagemexpedicaoproduto;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoerrado;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.bean.vo.CodigobarrasVO;
import br.com.linkcom.wms.geral.service.CarregamentohistoricoService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EmbalagemexpedicaoService;
import br.com.linkcom.wms.geral.service.EmbalagemexpedicaoprodutoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService;
import br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoerradoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.UsuarioService;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.ConferenciaCegaPapelFiltro;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

/**
 * Tela para confer�ncia de checkout
 * 
 * @author Pedro Gon�alves
 *
 */
public class ConferenciaCheckoutWindow extends ExecucaoOSWindow {
	
	protected Ordemservico ordemservico;
	protected OrdemservicoUsuario ordemservicoUsuario;
	protected List<Ordemprodutohistorico> listaOPH;
	protected List<CodigobarrasVO> codeBars;
	protected boolean stoploop;
	protected boolean leituraPorEtiqueta = false;
	protected boolean next = true;
	protected Boolean trocarCarregamento;
	protected CarregamentohistoricoService carregamentohistoricoService = CarregamentohistoricoService.getInstance();
	
	private Deque<Embalagemexpedicao> embalagensStack = new ArrayDeque<Embalagemexpedicao>();
	private List<Etiquetaexpedicao> listaEtiquetaexpedicao = new ArrayList<Etiquetaexpedicao>();
	
	/**
	 * M�todo principal da classe. Inicia aqui todo o processamento.
	 * 
	 * Verifica se o carregamento digitado � v�lido, e carrega as ordens de servi�os que est�o anexadas a 
	 * este carregamento.
	 * 
	 * @see #findColetor(String)
	 * 
	 * Caso seja um carregamento v�lido, � apresentado o menu para escolha da ordem de servi�o.
	 * @see #choiceOs()
	 * 
	 * Carrega as informa��es do carregamento na tela.
	 * 
	 * Carrega as OrdemProdutoHist�rico e os c�digos de barras que podem ser bipados.
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#findByForRF(Ordemservico ordemservico)
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#findAllBarCodeByOS(Ordemservico ordemservico, Deposito deposito)
	 * 
	 * Inicia a coleta dos produtos.
	 * @see #startCollectProduct()
	 */
	@Override
	public void draw() throws IOException {
		do {
			drawEsqueleto(DIGITE_0_PARA_SAIR);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			String value = readBarcode("O.S.: ");

			if (!NumberUtils.isDigits(value)){
				alertError("N�mero de Ordem de Servi�o inv�lido.");
				continue;
			}
			
			if (value.equals("0"))
				break;

			ordemservico = OrdemservicoService.getInstance().findByCarregamentoToRF(new Ordemservico(Integer.valueOf(value)), usuario);
			
		
			if (ordemservico != null && !ordemservico.getDeposito().equals(deposito)){
				ordemservico = null;
			}

			if (ordemservico != null && ordemservico.getOrdemtipo().equals(Ordemtipo.CONFERENCIA_CHECKOUT)) {
				Boolean verificaOsAberta = verificaOsAberta();
				if(!verificaOsAberta){
					writeLine("");
					writeLine("");
					writeLine("O.S. alocada com outro conferente.");
					read();
				} else if(verificaOsAberta){
					//associar o usu�rio a esta os.
					leituraPorEtiqueta = Boolean.TRUE.equals(ordemservico.getTipooperacao().getImprimeetiqueta()) || ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.OPERACAO_EXPEDICAO_POR_BOX, deposito); 
					associarUsuarioOS();
					executarOrdem(ordemservico);
				}
				
			} else {
				ordemservico = null;
				alertError("Ordem de servi�o indispon�vel");
			}

		} while (true);
	}
	
	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		stoploop = false;
		this.ordemservico = OrdemservicoService.getInstance().loadOrdemAndOrigem(ordemservico);
		this.ordemservicoUsuario = OrdemservicousuarioService.getInstance().findByOrdemservico(ordemservico);
		
		showInformationsCarregamento();
		read();
		listaOPH = OrdemprodutohistoricoService.getInstance().findByForRF(ordemservico, false);
		this.codeBars = OrdemprodutohistoricoService.getInstance().findAllBarCodeByOS(ordemservico, deposito);
		
		embalagensStack = new ArrayDeque<Embalagemexpedicao>();
		listaEtiquetaexpedicao = new ArrayList<Etiquetaexpedicao>();
		
		for (Ordemprodutohistorico oph : listaOPH) {
			Ordemservicoproduto osp = oph.getOrdemservicoproduto();
			for (Etiquetaexpedicao etiquetaexpedicao : osp.getListaEtiquetaexpedicao()) {				
				if(etiquetaexpedicao.getQtdecoletor() == null && !listaEtiquetaexpedicao.contains(etiquetaexpedicao)){
					listaEtiquetaexpedicao.add(etiquetaexpedicao);
				}
			}
		}
		
		Embalagemexpedicao embalagemAberta = EmbalagemexpedicaoService.getInstance().findEmbalagemAberta(ordemservico);
		if (embalagemAberta != null){
			embalagensStack.addFirst(embalagemAberta);
		}
		
		startCollectProduct();
	}
	
	/**
	 * Coleta dos produtos.
	 * 
	 * Caso o usu�rio digite 0 na etiqueta, � apresentado na tela o menu de sele��o.
	 * @see #makeMenu()
	 * 
	 * � solicitado a etiqueta e o produto, e � verificado se os mesmos percencem �s OSP.
	 * @see #etiquetaPertence(String)
	 * @see #produtoPertence(String, Etiquetaexpedicao)
	 * 
	 * Caso esteja ok, � apresentado informa��es do produto. e in�cio da coleta das quantidades.
	 * @see #showInfoProduto(CodigobarrasVO, Etiquetaexpedicao)
	 * 
	 * @throws IOException
	 */
	protected void startCollectProduct() throws IOException {
		validarBloqueioExistente();
		
		do {
			next = false;
			drawEsqueleto("Digite 0 para a��es.");
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
			writeSeparator();
			
			Etiquetaexpedicao etiquetaPertence = null;
			
			String valueEtiqueta = null;
			if(leituraPorEtiqueta) {
				valueEtiqueta = readBarcode("Etiqueta: ");
				if(valueEtiqueta == null || "0".equals(valueEtiqueta)) {
					makeMenu();
					if(trocarCarregamento != null)
						if(trocarCarregamento)
							break;
						else
							continue;
					if(stoploop)
						break;
					if(next)
						continue;
				}
				
				etiquetaPertence = etiquetaPertence(valueEtiqueta);
				if(etiquetaPertence == null){
					alertError("Etiqueta n�o encontrada.");
					Ordemservicoprodutoerrado leituraErrada = gravarLeituraErrada(valueEtiqueta, null);
					bloquearColetor(leituraErrada);
					continue;
				}else if ((leituraPorEtiqueta && Boolean.TRUE.equals(etiquetaPertence.getReaded())) || (etiquetaPertence.getQtdecoletor() != null && etiquetaPertence.getQtdecoletor() > 0)){
					alertError("A etiqueta j� foi bipada.");
					continue;
				}
				
			}
			String valueProduto = readBarcode("Prod.: ");
			
			if(!leituraPorEtiqueta){
				if(valueProduto == null || "0".equals(valueProduto)) {
					makeMenu();
					if(stoploop)
						break;
					if(next)
						continue;
				}
			}
				
			writeLine("");
			writeLine("");
			
			CodigobarrasVO produtoPertence = produtoPertence(valueProduto,etiquetaPertence);
			if(produtoPertence == null){
				alertError("Produto n�o encontrado.");
				Ordemservicoprodutoerrado leituraErrada = gravarLeituraErrada(valueEtiqueta, valueProduto);
				bloquearColetor(leituraErrada);
				continue;
			}else if (leituraPorEtiqueta && Boolean.TRUE.equals(produtoPertence.getEtiquetaexpedicao().getReaded())){
				alertError("A etiqueta j� foi bipada.");
				continue;
			}else {
				produtoPertence.getEtiquetaexpedicao().setReaded(true);
			}
			
			if(!leituraPorEtiqueta){
				etiquetaPertence = produtoPertence.getEtiquetaexpedicao();
			}
			
			//mostra as informa��es do produto
			if(!leituraPorEtiqueta)
				produtoInformations(produtoPertence.getOrdemprodutohistorico());
				
			//coleta das quantidades
			collectQte(produtoPertence.getOrdemprodutohistorico(), produtoPertence, etiquetaPertence);
			
			if(stoploop)
				break;
			
		} while (true);
	}
	
	
	/**
	 * Mostra as informa��es do produto.
	 * 
	 * @param produtoPertence
	 * @throws IOException
	 */
	protected void produtoInformations(Ordemprodutohistorico produtoPertence) throws IOException{
		boolean showInfoProduto = showInfoProduto(produtoPertence);
		if(showInfoProduto){
			writeLine("Dados log�sticos incompletos", ColorHelper.RED, false);
			writeSeparator();
		} 
	}
	
	protected Ordemservicoprodutoerrado gravarLeituraErrada(String etiquetaLida, String produtoLido){
		Produto produto = null;
		if (produtoLido != null && !produtoLido.isEmpty())
			produto = ProdutoService.getInstance().findProdutoByBarcode(produtoLido, deposito, true);
		
		Etiquetaexpedicao etiqueta = null;
		try{
			if (etiquetaLida != null && !etiquetaLida.isEmpty())
				etiqueta = EtiquetaexpedicaoService.getInstance().get(Integer.valueOf(etiquetaLida));
		}catch (Exception e) {
			//ignorando erro de carateres inv�lidos no codigo lido
		}
		
		Ordemservicoprodutoerrado leituraErrada = new Ordemservicoprodutoerrado();
		leituraErrada.setDtbloqueio(new Timestamp(System.currentTimeMillis()));
		leituraErrada.setOrdemservico(ordemservico);
		leituraErrada.setProduto(produto);
		leituraErrada.setUsuariobloqueio(usuario);
		leituraErrada.setEtiquetaexpedicao(etiqueta);
		leituraErrada.setCodigoProduto(produtoLido);
		leituraErrada.setCodigoEtiqueta(etiquetaLida);
		
		OrdemservicoprodutoerradoService.getInstance().saveOrUpdate(leituraErrada);
		
		return leituraErrada;
	}
	
	/**
	 * Verifica se existem leituras erradas registradas para a ordem de servi�o que ainda n�o foram liberadas. 
	 * Se existir, trava o coletor at� que todos os bloqueios sejam resolvidos.
	 * @throws IOException 
	 */
	protected void validarBloqueioExistente() throws IOException{
		List<Ordemservicoprodutoerrado> leiturasErradas = OrdemservicoprodutoerradoService.getInstance().findBloqueiosAtivos(ordemservico);
		for (Ordemservicoprodutoerrado leituraErrada : leiturasErradas)
			bloquearColetor(leituraErrada);
	}
	
	/**
	 * Bloqueia o coletor at� que um "Coordenador de expedi��o" informe o seu login e senha.
	 * 
	 * @param leituraErrada
	 * @throws IOException
	 */
	protected void bloquearColetor(Ordemservicoprodutoerrado leituraErrada) throws IOException{
		Usuario usuarioDesbloqueio = null;
		do{
			drawEsqueleto("");
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
			writeSeparator();
			writeOnCenter("Bloqueio de confer�ncia", ColorHelper.RED, false, false);
			writeSeparator();
			if (leituraErrada.getProduto() != null)
				writeLine("Produto: " + leituraErrada.getProduto().getDescriptionProperty());
			writeLine("Usu�rio: " + leituraErrada.getUsuariobloqueio().getNome());
			writeLine("Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(leituraErrada.getDtbloqueio()));
			writeSeparator();
			writeLine("Autoriza��o do coordenador de expedi��o:");
			writeLine("");
			
			getTermIO().write("Login:");
			Editfield loginEdf = new Editfield(getTermIO(),"login_coordenador",8);
			loginEdf.run();
			String login = loginEdf.getValue();
			getTermIO().write(BasicTerminalIO.CRLF);
			
			getTermIO().write("Senha:");
			Editfield senhaEdf = new Editfield(getTermIO(),"senha_coordenador",8);
			senhaEdf.setPasswordField(true);
			senhaEdf.run();
			String senha = senhaEdf.getValue();

			if(login == null || login.equals("") || senha == null || senha.equals("")){
				alertError("Login inv�lido.");
				continue;
			}
			
			usuarioDesbloqueio = UsuarioService.getInstance().findByLogin(login);
			if (usuarioDesbloqueio == null || usuarioDesbloqueio.getCdpessoa() == null){
				usuarioDesbloqueio = null;
				alertError("Login inv�lido.");
				continue;
			}
			
			//////////////////////////////////////////
			//Validando se � um coordenador
			boolean isCoordenadorExpedicao = false;
			for (Usuariopapel usuariopapel : usuarioDesbloqueio.getListaUsuariopapel()){
				if (usuariopapel.getPapel().equals(Papel.COORDENADOR_EXPEDICAO) || usuariopapel.getPapel().isAdmin()){
					isCoordenadorExpedicao = true;
					break;
				}
			}
			
			if (!isCoordenadorExpedicao) {
				usuarioDesbloqueio = null;
				alertError("� necess�rio a autoriza��o de um coordenador de expedi��o.");
				continue;
			}
			//////////////////////////////////////////
			
			StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
			if (!encryptor.checkPassword(senha, usuarioDesbloqueio.getSenha())) {
				usuarioDesbloqueio = null;
				alertError("Senha incorreta.");
				continue;
			}

		} while (usuarioDesbloqueio == null);
		
		
		leituraErrada.setDtlibera(new Timestamp(System.currentTimeMillis()));
		leituraErrada.setUsuariolibera(usuarioDesbloqueio);
		OrdemservicoprodutoerradoService.getInstance().saveOrUpdate(leituraErrada);
	}
	
	/**
	 * Exibe as informa��es do produto da osp bipada.
	 * 
	 * @see #getEmbalagemCompra(Produto)
	 * 
	 * @param produtoPertence
	 * @return false - Caso os dados log�sticos estejam ok.
	 *         true - Caso falte algum dado log�stico.
	 */
	private boolean showInfoProduto(Ordemprodutohistorico produtoPertence) throws IOException {
		boolean error = false;
		
		Produto produto = produtoPertence.getOrdemservicoproduto().getProduto();

		drawEsqueleto(" ");
		
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
		writeSeparator();

		String codigoP = "";
		try {
			codigoP = produto.getCodigo();
		} catch (Exception e) {error=true;}
		
		writeLine("C�digo: " + codigoP);
		writeLine("Descri��o: " + produto.getDescricao());
		
		if(produtoPertence.getCodigoBarrasIndex() != null){
			String codigobarras = "";
			try {
				codigobarras = produto.getListaProdutoCodigoDeBarras().get(produtoPertence.getCodigoBarrasIndex()).getCodigo();
			} catch (Exception e) {error=true;}
			
			writeLine("C�digo barras: " + codigobarras);
		}
		
		writeSeparator();
		
		return error;
	}
	
	/**
	 * Verifica se o c�digo de barras bipado(Etiqueta) pertence a alguma osp.
	 * 
	 * @param codigo
	 * @return null caso n�o tenha sido encontrado nenhuma ocorr�ncia.
	 */
	protected Etiquetaexpedicao etiquetaPertence(String codigo, Ordemprodutohistorico ordemprodutohistorico){
		int parseInt = -1;
		try{
			parseInt = Integer.parseInt(codigo.trim());
		} catch (Exception e) {	}
		
		Ordemservicoproduto osp = null;
		for (Ordemprodutohistorico oph : listaOPH) {
			osp = oph.getOrdemservicoproduto();
			if(ordemprodutohistorico == null || (ordemprodutohistorico != null && ordemprodutohistorico.getCdordemprodutohistorico().equals(oph.getCdordemprodutohistorico()))) {
				for (Etiquetaexpedicao etiquetaexpedicao : osp.getListaEtiquetaexpedicao()) {
					
					if(etiquetaexpedicao.getCdetiquetaexpedicao().equals(parseInt)){
						etiquetaexpedicao.setOrdemservicoproduto(osp);
						return etiquetaexpedicao;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Verifica se o c�digo de barras bipado(Etiqueta) pertence a alguma osp.
	 * 
	 * @param codigo
	 * @return null caso n�o tenha sido encontrado nenhuma ocorr�ncia.
	 */
	protected Etiquetaexpedicao etiquetaPertence(String codigo){
		return etiquetaPertence(codigo, null);
	}
	
	/**
	 * Verifica se o c�digo de barras bipado(produto) pertence a etiqueta bipada anteriormente.
	 * 
	 * @see #existeProdutoEtiqueta(Ordemprodutohistorico, List)
	 * 
	 * @param codigo
	 * @param etiquetaexpedicaoBipe
	 * 
	 * @return null caso n�o tenha sido encontrado nenhuma ocorr�ncia.
	 */
	protected CodigobarrasVO produtoPertence(String codigo, Etiquetaexpedicao etiquetaexpedicaoBipe,Ordemprodutohistorico ordemprodutohistorico){
		boolean aceitaCodigoInterno = !ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito);

		List<CodigobarrasVO> listaVO = new ArrayList<CodigobarrasVO>();
		for (CodigobarrasVO vo : codeBars) {
			String codigoProduto = vo.getCodigoProduto();
			
			if(codigo.trim().equalsIgnoreCase(vo.getCodigo()) || (aceitaCodigoInterno && codigo.trim().equalsIgnoreCase(codigoProduto)))
				listaVO.add(vo);
		}
		
		for (Ordemprodutohistorico oph : listaOPH) {
			
			if(ordemprodutohistorico == null || (ordemprodutohistorico != null && ordemprodutohistorico.getCdordemprodutohistorico().equals(oph.getCdordemprodutohistorico()))) {
				for (Etiquetaexpedicao etiquetaexpedicao : oph.getOrdemservicoproduto().getListaEtiquetaexpedicao()) {
					
					//caso a etiqueta expedi��o seja nula, ignorar ela e pegar a primeira que aparecer.
					//Altera��o solicitada pelo Christian.
					if(etiquetaexpedicaoBipe == null || etiquetaexpedicao.getCdetiquetaexpedicao().equals(etiquetaexpedicaoBipe.getCdetiquetaexpedicao())){
						CodigobarrasVO existeProdutoEtiqueta = existeProdutoEtiqueta(oph,listaVO);
						if(existeProdutoEtiqueta != null){
							
							//if(etiquetaexpedicaoBipe == null)
							existeProdutoEtiqueta.setEtiquetaexpedicao(etiquetaexpedicao);
							existeProdutoEtiqueta.setOrdemprodutohistorico(oph);
							
							//existeProdutoEtiqueta.setProduto(oph.getOrdemservicoproduto().getProduto());
							
							return existeProdutoEtiqueta;
						}
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * 	Verifica se o c�dgio de barra da embalagem do produto foi bipado corretamente. 
	 * 	Caso n�o exista uma embalagem vincualda essa ordem o sistema vai ignorar essa valida��o e continuar o fluxo antigo.
	 * @param oph
	 * @param listaVO
	 * @return
	 */
	private CodigobarrasVO etiquetaEmbalagemValida(Ordemprodutohistorico oph, List<CodigobarrasVO> listaVO){
		Integer cdproduto = 0;
		Integer cdprodutoembalagem = null;

		if(oph.getOrdemservicoproduto()!=null && oph.getOrdemservicoproduto().getProdutoembalagem()!=null && oph.getOrdemservicoproduto().getProdutoembalagem().getCdprodutoembalagem()!=null)
			cdprodutoembalagem = oph.getOrdemservicoproduto().getProdutoembalagem().getCdprodutoembalagem();
		else
			return null;	
		
		for (CodigobarrasVO codigobarrasVO : listaVO) {
			if(codigobarrasVO.getProdutoprincipal() == null || codigobarrasVO.getProduto().getCdproduto() == null || !codigobarrasVO.getProduto().getCdproduto().equals(0))
				cdproduto = codigobarrasVO.getProduto().getCdproduto(); 
			else 
				cdproduto = codigobarrasVO.getProdutoprincipal().getCdproduto();
			
			if(codigobarrasVO.getCdprodutoembalagem()!=null && codigobarrasVO.getCdprodutoembalagem().equals(cdprodutoembalagem) && oph.getOrdemservicoproduto().getProduto().getCdproduto().equals(cdproduto))
				return codigobarrasVO;
		}
		
		if(cdprodutoembalagem!=null)
			return new CodigobarrasVO();
		else
			return null;
	}
	
	/**
	 * Verifica se o c�digo de barras bipado(produto) pertence a etiqueta bipada anteriormente.
	 * 
	 * @see #existeProdutoEtiqueta(Ordemprodutohistorico, List)
	 * 
	 * @param codigo
	 * @param etiquetaexpedicaoBipe
	 * 
	 * @return null caso n�o tenha sido encontrado nenhuma ocorr�ncia.
	 */
	protected CodigobarrasVO produtoPertence(String codigo, Etiquetaexpedicao etiquetaexpedicaoBipe){
		return produtoPertence(codigo, etiquetaexpedicaoBipe,null);
	}
	
	/**
	 * Verifica se o produto pertence � etiqueta.
	 * @param oph
	 * @param listaVO
	 * @return
	 */
	private CodigobarrasVO existeProdutoEtiqueta(Ordemprodutohistorico oph, List<CodigobarrasVO> listaVO) {
		
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", deposito)){
			CodigobarrasVO cbVO = etiquetaEmbalagemValida(oph,listaVO);
			if(cbVO!=null && cbVO.getCdprodutoembalagem()!=null)
				return cbVO;
			else if(cbVO!=null && cbVO.getCdprodutoembalagem()==null)
				return null;
		}
		
		Integer cdproduto = 0;
		for (CodigobarrasVO codigobarrasVO : listaVO) {
			if(codigobarrasVO.getProdutoprincipal() == null || codigobarrasVO.getProduto().getCdproduto() == null || !codigobarrasVO.getProduto().getCdproduto().equals(0))
				cdproduto = codigobarrasVO.getProduto().getCdproduto(); 
			else
				cdproduto = codigobarrasVO.getProdutoprincipal().getCdproduto();
			if(oph.getOrdemservicoproduto().getProduto().getCdproduto().equals(cdproduto)) {
				return codigobarrasVO;
			}
		}
		return null;
	}

	/**
	 * Verifica se o usu�rio pode coletar esta OS. Caso o usu�rio tenha perdido a conex�o, ele pode
	 * reconectar-se a mesma os.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#loadByOS(Ordemservico)
	 * 
	 * @return true - Caso este usu�rio possa coletar esta OS.
	 *         false - O usu�rio n�o pode coletar essa OS.
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
	 * Associa o usu�rio que est� logado no box do recebimento, e amarra ele a �ltima ordem de servi�o.
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
	
	/**
	 * Exibe as informa��es do carregamento que foi encontrado na tela.
	 * 
	 * @throws IOException
	 * @author Pedro Gon�alves
	 */
	private void showInformationsCarregamento() throws IOException {
		drawEsqueleto("");
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("O.S.: " + this.ordemservico.getCdordemservico());
		
		if (ordemservico.getCarregamento() != null && ordemservico.getCarregamento().getVeiculo() != null){
			writeLine("");
			writeLine("Ve�culo: " + ordemservico.getCarregamento().getVeiculo().getPlaca() + "");
		}
		
		if (ordemservico.getExpedicao() !=  null && ordemservico.getExpedicao().getBox() != null){
			writeLine("");
			writeLine("Box: " + ordemservico.getExpedicao().getBox().getNome() + "");
		} else if (ordemservico.getCarregamento() !=  null && ordemservico.getCarregamento().getBox() != null){
			writeLine("");
			writeLine("Box: " + ordemservico.getExpedicao().getBox().getNome() + "");
		}
		
	}
	
	/**
	 * Obt�m o t�tulo da janela.
	 * 
	 * @return
	 */
	@Override
	public String getTitulo() {
		if (ordemservico != null)
			return ordemservico.getOrdemtipo().getNome();
		else
			return "Confer�ncia de checkout";
	}

	/**
	 * Respons�vel pela coleta das quantidades.
	 * 
	 * Quando a coleta for por loja, poder� digitar a quantidade, caso contr�rio � sempre setado a quantidade 1.
	 * 
	 * @see #processarVolume(CodigobarrasVO, Ordemprodutohistorico)
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#atualizarQuantidades(Ordemprodutohistorico)
	 * @see br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService#updateQtdecoletor(Etiquetaexpedicao)
	 * 
	 * @param produtoPertence
	 * @param vo
	 * @param etiquetaexpedicao
	 * @return
	 * @throws IOException
	 */
	protected void collectQte(Ordemprodutohistorico produtoPertence, CodigobarrasVO vo, Etiquetaexpedicao etiquetaexpedicao) throws IOException{
		//entrada de dados para a quantidade
				
		long coletado = 0l;
		
		//Quando a coleta for por loja, poder� digitar a quantidade.
		if(!leituraPorEtiqueta) {
			coletado = readInteger("Quantidade: ");
		} else {
			//Sempre que a coleta for por carga adicionar 1.
			coletado = 1L;
		}
		
		if (!isOrdemAindaAberta(ordemservico)){
			stoploop = true;
			return;
		}
		
		long total = 0L;
		
		//verifica se � volumes ou se � o produto diretamente.
		if(vo.getProduto().getCdproduto().equals(produtoPertence.getOrdemservicoproduto().getProduto().getCdproduto()) 
				|| vo.getProdutoprincipal().getCdproduto() == null || vo.getProdutoprincipal().getCdproduto().equals(0)){
			//caso seja o unit�rio
			Long qtderecebida = produtoPertence.getQtde();
			
			if(qtderecebida == null)
				qtderecebida = 0L;
						
			Long qtdecoletor = etiquetaexpedicao.getQtdecoletor();
			if(qtdecoletor == null)
				qtdecoletor = 0L;
			
			total = qtdecoletor + coletado;
			
			if(total > produtoPertence.getOrdemservicoproduto().getQtdeesperada()){
				alertError("A quantidade coletada excede o valor m�ximo, portanto este valor ser� ignorado.");
				return;
			} else{
				etiquetaexpedicao.setQtdecoletor(total);
				produtoPertence.setQtde(coletado + qtderecebida);
			}
			
		} else {
			//caso seja volume, adicionar 1 no volume e verificar se formou um produto completo.
			Integer qtderecebida = vo.getQtdeBipe();
			
			if(qtderecebida == null)
				qtderecebida = 0;
			
			if(vo.getEmbalgem() != null && vo.getQtde() != null && vo.getQtde() > 0){
				coletado = vo.getQtde() + coletado;
			}
			int i = new Long(coletado).intValue() + qtderecebida;
			
			if(i > produtoPertence.getOrdemservicoproduto().getQtdeesperada()){
				alertError("A quantidade coletada excede o valor m�ximo, portanto este valor ser� ignorado.");
				return;
			}
			
			vo.setQtdeBipe(i);
			
			int processarVolume = processarVolume(vo,produtoPertence);
			
			Long qtdecoletor = etiquetaexpedicao.getQtdecoletor();
			if(qtdecoletor == null)
				qtdecoletor = 0L;
			
			total = qtdecoletor + processarVolume;
			etiquetaexpedicao.setQtdecoletor(qtdecoletor);
			produtoPertence.setQtde(total);
		}
		
		if(produtoPertence.getQtde() == null)
			produtoPertence.setQtde(0L);
		
		if(produtoPertence.getQtdeavaria() == null)
			produtoPertence.setQtdeavaria(0L);
		
		if(produtoPertence.getQtdefracionada() == null)
			produtoPertence.setQtdefracionada(0L);
		
		OrdemprodutohistoricoService.getInstance().atualizarQuantidades(produtoPertence);
		EtiquetaexpedicaoService.getInstance().updateQtdecoletor(etiquetaexpedicao);
		
		if (Ordemstatus.EM_ABERTO.equals(ordemservico.getOrdemstatus())){
			ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
			OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
		}
		
		//---------------------------------------
		//Adicionando o produto � embalagem atual
		Embalagemexpedicao embalagem;
		if (embalagensStack.isEmpty()){
			embalagem = new Embalagemexpedicao();			
			embalagem.setOrdemservico(ordemservico);
			embalagem = setLacreEmbalagem(embalagem);
			embalagensStack.addFirst(embalagem);
		}else{
			embalagem = embalagensStack.peekFirst();
			if(embalagem.getLacre() == null || embalagem.getLacre().isEmpty() || embalagem.getCdembalagemexpedicao() == null){
				embalagem = setLacreEmbalagem(embalagem);
			}				
		}		
		if (embalagem.getCdembalagemexpedicao() == null)
			EmbalagemexpedicaoService.getInstance().saveOrUpdate(embalagem);		
		
		Embalagemexpedicaoproduto embalagemproduto = new Embalagemexpedicaoproduto();
		embalagemproduto.setEmbalagemexpedicao(embalagem);
		embalagemproduto.setEtiquetaexpedicao(etiquetaexpedicao);
		embalagem.getListaEmbalagemexpedicaoproduto().add(embalagemproduto);
		
		EmbalagemexpedicaoprodutoService.getInstance().saveOrUpdate(embalagemproduto);
		//---------------------------------------
		
		writeLine("");
		writeLine("");
		
		listaEtiquetaexpedicao.remove(etiquetaexpedicao);
		if (listaEtiquetaexpedicao.isEmpty()){
			finalizarEmbalagem();
			processarOSP();
			gerarConferenciaEmbalagem();
			stoploop = true;
			next = true;
		}
		
		//quando for por carga, validar se j� bipou todas as etiquetas.
		if(!leituraPorEtiqueta || (leituraPorEtiqueta && canGoNextProduct(produtoPertence)))
			next = true;
		
	}
	
	private void gerarConferenciaEmbalagem() {
		
	}

	/**
	 * Seta como true o campo readed das etiquetas caso haja perda na conexao.
	 * @param ordemprodutohistorico
	 */
	protected void fixReadedEtiqueta(Ordemprodutohistorico ordemprodutohistorico){
		Set<Etiquetaexpedicao> listaEtiquetaexpedicao = ordemprodutohistorico.getOrdemservicoproduto().getListaEtiquetaexpedicao();
		for (Etiquetaexpedicao etiquetaexpedicao : listaEtiquetaexpedicao) {
			if(etiquetaexpedicao.getQtdecoletor() != null && etiquetaexpedicao.getQtdecoletor() > 0)
				etiquetaexpedicao.setReaded(true);
		}
		
	}
	
	/**
	 * Quando a conferencia for por carga, somente liberar o pr�ximo produto quando ele j� tiver bipado todas as etiquetas do produto.
	 * @param ordemprodutohistorico
	 * @return
	 */
	protected Boolean canGoNextProduct(Ordemprodutohistorico ordemprodutohistorico){
		Set<Etiquetaexpedicao> listaEtiquetaexpedicao = ordemprodutohistorico.getOrdemservicoproduto().getListaEtiquetaexpedicao();
		for (Etiquetaexpedicao etiquetaexpedicao : listaEtiquetaexpedicao) {
			if(etiquetaexpedicao.getQtdecoletor() != null && etiquetaexpedicao.getQtdecoletor() > 0)
				etiquetaexpedicao.setReaded(true);
			
			if(!etiquetaexpedicao.getReaded())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Monta o menu de sele��o das a��es dispon�veis para a ordem de servi�o.
	 * <ul>
	 * 		<li>Voltar - Retorna para a coleta</li>
	 * 		<li>Cancelar - Cancela a coleta e desassocia o usu�rio e reseta os valores coletados.</li>
	 * 		<li>Encerrar coleta - Finaliza a coleta, e gera a ordem de reconferencia caso seja necess�rio.</li>
	 * </ul>
	 * 
	 * @throws IOException
	 */
	protected void makeMenu() throws IOException {
		drawEsqueleto("");
		writeLine("Selecione uma a��o: ");
		writeLine("");
		
		final HashMap<Integer, String> mapaMenu = new HashMap<Integer, String>();
		mapaMenu.put(0, "Voltar");
		if (!isConvocacaoAtiva())
			mapaMenu.put(1, "Trocar de carregamento");
		else
			mapaMenu.put(1, "Sair");
		mapaMenu.put(2, "Cancelar");
		mapaMenu.put(3, "Finalizar embalagem");		
		mapaMenu.put(4, "Finalizar confer�ncia");		
		
		Set<Entry<Integer, String>> entrySet = mapaMenu.entrySet();
		
		for (Entry<Integer, String> entry : entrySet) {
			writeLine(entry.getKey()+" - "+entry.getValue());
		}
		
		getTermIO().write(BasicTerminalIO.CRLF);
		Editfield optDep=new Editfield(getTermIO(),"editfield 3",2);
		optDep.registerInputValidator(new InputValidator(){
			public boolean validate(String str) {
				if(str == null || str.equals(""))
					return true;
				
				try{
					int option = Integer.parseInt(str);
					if(option <= (mapaMenu.size() - 1) && option >= 0){
						return true;
					}
				} catch (Exception e){
					return false;
				}
				return false;
			}
		});
		
		optDep.run();
		
		String value = optDep.getValue();
		trocarCarregamento = null;
		
		if (value == null || value.trim().isEmpty()){
			makeMenu();
			return;
		}
		
		if ("4".equals(value)){			
			if(confirmAction("Finalizar a confer�ncia?")){
				//Se h� embalagens abertas
				if (!embalagensStack.isEmpty()) {
					Embalagemexpedicao ultimaEmbalagem = embalagensStack.peekFirst();
					if (!ultimaEmbalagem.getListaEmbalagemexpedicaoproduto().isEmpty()){
						finalizarEmbalagem();
					}
				}

				alertError("Aguardando finaliza��o manual pela mesa de opera��es.");
				stoploop = true;
				trocarCarregamento = true;
			}else {
				stoploop = false;
				trocarCarregamento = false;
			}
		} else if ("3".equals(value)){
			if(confirmAction("Finalizar a embalagem?")){
				finalizarEmbalagem();
				if (listaEtiquetaexpedicao.isEmpty()){
					processarOSP();
					stoploop = true;
				}else{
					stoploop = false;
					next = true;					
				}
			} else {
				stoploop = false;
				next = true;
			}
		} else if("2".equals(value)){
			if(confirmAction("Cancelar opera��o?")){
				desassociarOSU();
				stoploop = true;
				if (isConvocacaoAtiva())
					logout();
			} else {
				stoploop = false;
				next = true;
			}
		} else if("1".equals(value) && !isConvocacaoAtiva()){
			if(confirmAction("Trocar de carregamento?")){
				stoploop = true;
				trocarCarregamento = true;
			}else {
				stoploop = false;
				trocarCarregamento = false;
			}
		} else if("1".equals(value) && isConvocacaoAtiva()){
			if(confirmAction("Tem certeza que deseja sair?")){
				stoploop = true;
				trocarCarregamento = true;
				if (isConvocacaoAtiva())
					logout();
			}else {
				stoploop = false;
				trocarCarregamento = false;
			}
		} else if("0".equals(value)){
			stoploop = false;
			next = true;
		}
	}
	
	private void finalizarEmbalagem() throws IOException {
		
		//Se n�o h� embalagens abertas
		if (embalagensStack.isEmpty())
			return;
		
		Embalagemexpedicao ultimaEmbalagem = embalagensStack.peekFirst();
		ultimaEmbalagem = verificaLacreExiste(ultimaEmbalagem);
		if (ultimaEmbalagem.getListaEmbalagemexpedicaoproduto().isEmpty()){
			alertError("N�o h� produtos na embalagem atual.");
			return;
		}

		String lacre = null;
		String opt = new String();
		do {
			drawEsqueleto("");
			writeLine("");
			if(ultimaEmbalagem.getLacre()!=null){
				lacre = ultimaEmbalagem.getLacre();				
				while(!opt.equals("1") && !opt.equals("2")){
					drawEsqueleto("");
					writeLine("");
					writeLine("C�digo do lacre: "+lacre);
					writeLine("1 - Confirmar");
					opt = readLine("2 - Modificar");			
					if (opt.equals("2")) 
						lacre = readLine("Informe o c�digo do lacre:");	
				}
			}else{
				lacre = readLine("Informe o c�digo do lacre:");	
			}
			
			if (lacre != null && lacre.length() > 20){
				lacre = null;
				alertError("O c�digo do lacre n�o pode conter mais de 20 d�gitos.");
			}
			
			if (lacre.equals("0")){
				lacre = null;
				alertError("O lacre n�o pode ser igual a 0");
			}
			
		} while (lacre == null || lacre.trim().isEmpty());
		
		ultimaEmbalagem.setLacre(lacre);
		EmbalagemexpedicaoService.getInstance().saveOrUpdate(ultimaEmbalagem);
		
		if (!listaEtiquetaexpedicao.isEmpty()){
			Embalagemexpedicao embalagem = new Embalagemexpedicao();
			embalagem.setOrdemservico(ordemservico);
			embalagensStack.addFirst(embalagem);
		}
	}

	/**
	 * Processa a ordem de servi�o.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#processarConferencia(Ordemservico, List<Ordemprodutohistorico>)
	 */
	protected void processarOSP() throws IOException {
		ordemservico.setDeposito(deposito);
		
		if(listaEtiquetaexpedicao.isEmpty()){
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
		} else {
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);
		}
		
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				
				CarregamentohistoricoService.getInstance().gerarHistoricoFinalizaOS(ordemservico, Carregamentohistorico.CONFERENCIA_FINALIZADA_CHECKOUT, usuario);
				
				OrdemservicoService.getInstance().finalizaCheckout(ordemservico, ordemservicoUsuario, usuario);
				return null;
			}
		});
		
		if(ordemservico.getOrdemstatus().equals(Ordemstatus.FINALIZADO_DIVERGENCIA)){
			writeOnCenter("Finalizado com divergencia", null, true, true);
		}else{
			writeOnCenter("Finalizado com sucesso!", null, true, true);
		}
		
	}

	/**
	 * Desassocia a ordem de servi�o do usu�rio logado no coletor.
	 * 
	 * @see br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService#resetQtdecoletor(Ordemservico)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoprodutoService#resetarQuantidades(Ordemservico)
	 */
	private void desassociarOSU() throws IOException{
		if (!isOrdemAindaAberta(ordemservico)){
			stoploop = true;
			return;
		}
		
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){

			public Object doInTransaction(TransactionStatus status) {
				EtiquetaexpedicaoService.getInstance().resetQtdecoletor(ordemservico);
				OrdemservicoprodutoService.getInstance().resetarQuantidades(ordemservico);
				ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
				OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
				OrdemservicousuarioService.getInstance().delete(ordemservicoUsuario);
				EmbalagemexpedicaoprodutoService.getInstance().deleteByOrdem(ordemservico);
				EmbalagemexpedicaoService.getInstance().deleteByOrdem(ordemservico);
				
				return null;
			}
		});
		
		writeOnCenter("Coleta cancelada", null, true, true);
	}
	
	/**
	 * Processa o volume, quando o usu�rio bipar o volume ao inv�s da unidades, somente acrescentar a unidade, quando completar
	 * todos os volumes do produto, adicionar os valores completos da unidade.
	 * 
	 * @param vo
	 * @param produtoPertence
	 * @return
	 */
	private int processarVolume(CodigobarrasVO vo,Ordemprodutohistorico produtoPertence) {
		List<CodigobarrasVO> listaP = new ArrayList<CodigobarrasVO>();
		for (CodigobarrasVO code : codeBars) {
			if(code.getProdutoprincipal().getCdproduto().equals(produtoPertence.getOrdemservicoproduto().getProduto().getCdproduto())){
				System.out.println("Adicionado "+code.getCodigo());
				listaP.add(code);
			}
		}
		
		int menor = 99999;
		for (CodigobarrasVO codigobarrasVO : listaP) {
			if(codigobarrasVO.getQtdeBipe() < menor){
				menor = codigobarrasVO.getQtdeBipe();
			}
			System.out.println("* "+codigobarrasVO.getCodigo() + " - " +codigobarrasVO.getQtdeBipe());
		}
		
		//caso tenha ao menos um completado, acrescentar no historico.
		if(menor > 0 && menor != 99999){
			for (CodigobarrasVO codigobarrasVO2 : listaP) {
				codigobarrasVO2.setQtdeBipe(codigobarrasVO2.getQtdeBipe() - menor);
				System.out.println("Reduzido valor "+codigobarrasVO2.getCodigo() + " - " +codigobarrasVO2.getQtdeBipe());
			}
			produtoPertence.setQtde(produtoPertence.getQtde() + menor);
			System.out.println("Novo valor "+produtoPertence.getQtde());
		}
		
		if(menor == 99999)
			menor = 0;
		
		return menor;
	}
	
	/**
	 * M�todo responsavel por salvar o lacre na etiqueta recem criada.
	 * 
	 * @author Filipe Santos
	 * @param emaEmbalagemexpedicao
	 * @throws IOException
	 */
	public Embalagemexpedicao setLacreEmbalagem(Embalagemexpedicao emaEmbalagemexpedicao) throws IOException{		
		String lacre = null;
		do {
			drawEsqueleto("");
			writeLine("");
			
			lacre = readLine("Informe o c�digo do lacre:");	
			
			if (lacre != null && lacre.length() > 20){
				lacre = null;
				alertError("O c�digo do lacre n�o pode conter mais de 20 d�gitos.");
			}
			
			if (lacre.equals("0")){
				lacre = null;
				alertError("O lacre n�o pode ser igual a 0");
			}
							
		} while (lacre == null || lacre.trim().isEmpty());
		
		emaEmbalagemexpedicao.setLacre(lacre);
		emaEmbalagemexpedicao = verificaLacreExiste(emaEmbalagemexpedicao);
		return emaEmbalagemexpedicao;
	}
	
	/**
	 * M�todo que verifica se o lacre que o usu�rio digitou j� est� sendo utilizado em outra embalagem.
	 * Caso esteja, o m�todo devolve a embalagem com o lacre cadastrado da OS.
	 * 
	 * @author Filipe Santos
	 * @param embalagem
	 * @return {@link Embalagemexpedicao}
	 */
	public Embalagemexpedicao verificaLacreExiste(Embalagemexpedicao embalagem){
		Embalagemexpedicao embalagemexpedicao = EmbalagemexpedicaoService.getInstance().findByLacre(embalagem);
		if((embalagemexpedicao == null) || (embalagemexpedicao.getCdembalagemexpedicao()==null))
			return embalagem;
		else{
			return embalagemexpedicao;
		}
		
	}
}