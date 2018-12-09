package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.CodigobarrasVO;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;

public class RetornoBoxWindow extends ConferenciaExpedicaoWindow {

	private static final String TITULO = "Retorno de Box";

	/**
	 * Monta o menu de seleção das ações disponíveis para a ordem de serviço.
	 * <ul>
	 * 		<li>Voltar - Retorna para a coleta</li>
	 * 		<li>Cancelar - Cancela a coleta e desassocia o usuário e reseta os valores coletados.</li>
	 * 		<li>Encerrar coleta - Finaliza a coleta, e gera a ordem de reconferencia caso seja necessário.</li>
	 * </ul>
	 * 
	 * @see #processarOSP()
	 * @see #desassociarOSU()
	 * 
	 * @throws IOException
	 */
	@Override
	protected void makeMenu() throws IOException {
		drawEsqueleto("");
		writeLine("Selecione uma ação: ");
		writeLine("");
		
		final HashMap<Integer, String> mapaMenu = new HashMap<Integer, String>();
		mapaMenu.put(0, "Voltar");
		mapaMenu.put(1, "Cancelar");
		
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
		
		if("1".equals(value)){
			if(confirmAction("Cancelar operação?")){
				desassociarOSU();
				stoploop = true;
				if (isConvocacaoAtiva())
					logout();
			} else {
				stoploop = false;
				next = true;
			}
		} else if("0".equals(value)){
			stoploop = false;
			next = false;
		}
	}
	

	/**
	 * Carrega a lista das ordens de serviço que são do tipo conferência.
	 * @return 
	 */
	@Override
	protected List<Ordemservico> loadOsList(Carregamento carregamento) {
		return OrdemservicoService.getInstance().findByCarregamentoToRF(carregamento, Ordemtipo.RETORNO_BOX, usuario);
	}
	
	/**
	 * Verifica se a ordem de serviço é uma ordem de reconferência de expedição válida.
	 * 
	 * @param ordemservico2
	 * @return
	 */
	@Override
	protected boolean isOrdemValida(Ordemservico ordemservico2) {
		if (ordemservico2 == null)
			return false;
		
		return ordemservico.getOrdemtipo().equals(Ordemtipo.RETORNO_BOX);
	}
	
	/**
	 * Coleta dos produtos.
	 * 
	 * Caso o usuário digite 0 na etiqueta, é apresentado na tela o menu de seleção.
	 * @see #makeMenu()
	 * 
	 * É solicitado a etiqueta e o produto, e é verificado se os mesmos percencem às OSP.
	 * @see #etiquetaPertence(String)
	 * @see #produtoPertence(String, Etiquetaexpedicao)
	 * 
	 * Caso esteja ok, é apresentado informações do produto. e início da coleta das quantidades.
	 * @see #showInfoProduto(CodigobarrasVO, Etiquetaexpedicao)
	 * 
	 * @throws IOException
	 */
	@Override
	protected void startCollectProduct() throws IOException {
		validarBloqueioExistente();
		
		for (Ordemprodutohistorico oph : listaOPH) {
			fixReadedEtiqueta(oph);
		}
		
		int i;
		for (i=0;i<listaOPH.size();) {
			next = false;
			stoploop = false;
			
			drawEsqueleto("Digite 0 para ações.");
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
			writeSeparator();
			
			showInfoProdutoResumido(listaOPH.get(i));
			
			Etiquetaexpedicao etiquetaPertence = null;
			String valueEtiqueta = null;
			if(leituraPorEtiqueta) {
				if(canGoNextProduct(listaOPH.get(i))){
					i++;
					next = false;
					continue;
				}
				
				valueEtiqueta = readBarcode("Etiqueta: ");
				if(valueEtiqueta == null || "0".equals(valueEtiqueta)) {
					makeMenu();
					if(trocarCarregamento != null)
						if(trocarCarregamento)
							break;
						else
							continue;
					if(next){
						i++;
						next = false;
						continue;
					}
					if(stoploop)
						break;
					
					//se o usuário pressionou 0 para voltar devo continuar.
					continue;
				}
					
				
				valueEtiqueta = getCodigoProduto(valueEtiqueta);
				writeLine("");
				writeLine("");
				
				etiquetaPertence = etiquetaPertence(valueEtiqueta,listaOPH.get(i));
				
				if (etiquetaPertence == null && lerEmbalagemExpedicao(valueEtiqueta)){
					continue;
				} else if(etiquetaPertence == null){
					alertError( "Etiqueta não encontrada.");
					continue;
				}else
					if(etiquetaPertence.getReaded()){
						alertError( "Etiqueta já coletada.");
						continue;
					}
			}
			
			String valueProduto = readBarcode("Produto: ");
			if(!leituraPorEtiqueta){
				if("0".equals(valueProduto)) {
					makeMenu();
					if(next){
						i++;
						next = false;
						continue;
					}
					if(stoploop)
						break;
				}
			}
			
			writeLine("");
			writeLine("");
			
			CodigobarrasVO produtoPertence = produtoPertence(valueProduto,etiquetaPertence,listaOPH.get(i));
			if(produtoPertence == null){
				alertError( "Produto não encontrado.");
				continue;
			}
			
			//carrega a etiqueta caso seja um carregamento por carga.
			if(!leituraPorEtiqueta){
				etiquetaPertence = produtoPertence.getEtiquetaexpedicao();

				//mostra as informações do produto
				produtoInformations(produtoPertence.getOrdemprodutohistorico());
			}
			
			//coleta das quantidades
			collectQte(produtoPertence.getOrdemprodutohistorico(), produtoPertence, etiquetaPertence);
			
			if(leituraPorEtiqueta)
				etiquetaPertence.setReaded(true);
			
			
			if(next)
				i++;
		}
		
		if(!stoploop){
			writeOnCenter("Não existem mais itens para serem coletados." +
					"\nO retorno de box será finalizado.", null, true, true);
			processarOSP();
		}
	}
	
	/**
	 * Responsável pela coleta das quantidades.
	 * 
	 * Quando a coleta for por loja, poderá digitar a quantidade, caso contrário é sempre setado a quantidade 1.
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
	@Override
	protected boolean collectQte(Ordemprodutohistorico produtoPertence, CodigobarrasVO vo, Etiquetaexpedicao etiquetaexpedicao) throws IOException{
		//entrada de dados para a quantidade
				
		long coletado = 0l;
		
		//Quando a coleta for por loja, poderá digitar a quantidade.
		if(!leituraPorEtiqueta) {
			coletado = readInteger("Quantidade: ");
		} else {
			//Sempre que a coleta for por carga adicionar 1.
			coletado = 1L;
		}
		
		if (!isOrdemAindaAberta(ordemservico)){
			stoploop = true;
			return false;
		}
		
		long total = 0L;
		
		//verifica se é volumes ou se é o produto diretamente.
		if(vo.getProduto().getCdproduto().equals(produtoPertence.getOrdemservicoproduto().getProduto().getCdproduto()) 
				|| vo.getProdutoprincipal().getCdproduto() == null || vo.getProdutoprincipal().getCdproduto().equals(0)){
			//caso seja o unitário
			Long qtderecebida = produtoPertence.getQtde();
			
			if(qtderecebida == null)
				qtderecebida = 0L;
						
			Long qtdecoletor = etiquetaexpedicao.getQtdecoletor();
			if(qtdecoletor == null)
				qtdecoletor = 0L;
			
			total = qtdecoletor + coletado;
			
			if(total > produtoPertence.getOrdemservicoproduto().getQtdeesperada()){
				alertError("A quantidade coletada excede o valor máximo, portanto este valor será ignorado.");
				return false;
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
				alertError("A quantidade coletada excede o valor máximo, portanto este valor será ignorado.");
				return false;
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
		
		if (Ordemstatus.EM_ABERTO.equals(ordemservico.getOrdemstatus())){
			ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
			OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
		}
		
		writeLine("");
		writeLine("");
		
		//quando for por carga, validar se já bipou todas as etiquetas.
		
		if(!leituraPorEtiqueta || (leituraPorEtiqueta && canGoNextProduct(produtoPertence)))
			next = true;
		
		return false;
	}
	
	/**
	 * Exibe as informações resumidas do produto.
	 * 
	 * @param produtoPertence
	 */
	private void showInfoProdutoResumido(Ordemprodutohistorico ordemprodutohistorico) throws IOException {

		Produto produto = ordemprodutohistorico.getOrdemservicoproduto().getProduto();
		
		drawEsqueleto("Digite 0 para ações.");
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
		writeLine("O.S.: " + ordemservico.getCdordemservico().toString());
		writeSeparator();
		writeLine("Código: " + produto.getCodigo());
		
		if (produto.getProdutoprincipal() == null){
			writeLine("Descrição: " + produto.getDescricao());
		} else {
			writeLine("Descrição: " + produto.getProdutoprincipal().getDescricao() + " - " + produto.getDescricao());
			writeLine("Volume: " + produto.getComplementocodigobarras());
		}
		
		writeSeparator();
		
	}
	
	@Override
	public String getTitulo() {
		if (ordemservico != null)
			return ordemservico.getOrdemtipo().getNome();
		else
			return TITULO;
	}
	
	/**
	 * Processa a ordem de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService#processarConferencia(Ordemservico, List<Ordemprodutohistorico>)
	 */
	@Override
	protected void processarOSP() throws IOException {
		ordemservico.setDeposito(deposito);
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				OrdemservicoService.getInstance().executarRetornoBox(ordemservico, listaOPH, usuario);
				ordemservicoUsuario.setDtfim(new Timestamp(System.currentTimeMillis()));
				OrdemservicousuarioService.getInstance().saveOrUpdate(ordemservicoUsuario);

				return null;
			}
		});
		
		writeOnCenter("Finalizado com sucesso!", null, true, true);
		
	}

}
