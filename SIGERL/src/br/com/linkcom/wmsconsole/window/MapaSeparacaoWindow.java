package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputValidator;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.EtiquetaexpedicaoService;
import br.com.linkcom.wms.geral.service.ExpedicaoService;
import br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.ConferenciaCegaPapelFiltro;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

public class MapaSeparacaoWindow extends ExecucaoOSWindow {

	private static final String TITULO = "Mapa de Separação";

	private static final String OPERACAO_CANCELADA = "Operação cancelada";
	private static final String FINALIZADO_COM_DIVERGENCIA = "Mapa de separação finalizado com divergência.";
	private static final String FINALIZADO_COM_SUCESSO = "Mapa de separação finalizado com sucesso.";

	private Set<Endereco> enderecosNaoLidos =  new HashSet<Endereco>();
	
	@Override
	public String getTitulo() {
		return TITULO;
	}
	
	/**
	 * Solicita o número do carregamento e lista as ordens de serviço para o
	 * usuário escolher a desejada.
	 * 
	 * @see br.com.linkcom.wmsconsole.system.TelnetWindow#draw()
	 */
	@Override
	public void draw() throws IOException {

		do {
			drawEsqueleto(DIGITE_0_PARA_SAIR);
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			
			String value = readBarcode("O.S.: ");
			
			if (!NumberUtils.isDigits(value)){
				alertError("Número de Ordem de Serviço inválido.");
			}
			
			if (value.equals("0"))
				break;
			
			Ordemservico ordemservico = OrdemservicoService.getInstance().findByCarregamentoToRF(new Ordemservico(Integer.valueOf(value)), usuario);

			if (ordemservico != null && ordemservico.getDeposito().equals(deposito) && ordemservico.getOrdemtipo().equals(Ordemtipo.MAPA_SEPARACAO)){
				executarOrdem(ordemservico);
			} else {
				ordemservico = null;
				alertError("Ordem de serviço indisponível.");
			}

		} while (true);

	}

	/**
	 * Inicia a coleta de dados de uma determinada ordem de serviço.
	 * 
	 * @param ordemservico
	 * @throws IOException
	 */
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		if (ordemservico.getExpedicao() != null && ordemservico.getExpedicao().getCdexpedicao() != null)
			ordemservico.setExpedicao(ExpedicaoService.getInstance().loadExpedicaoForGerenciamento(ordemservico.getExpedicao()));
			
		prepararTela(false);

		// ///////////////////////////////////////////////////////////////
		// Exibindo detalhes do carregamento escolhido
		if (ordemservico.getExpedicao() != null){
			writeLine("");
			writeLine("Expedição: " + ordemservico.getExpedicao().getCdexpedicao());
			writeLine("Box: " + ordemservico.getExpedicao().getBox().getNome() + "");
			writeLine("");
			
			List<Integer> idsCarregamentos = new ArrayList<Integer>();
			List<String> carregamentos = new ArrayList<String>();
			for (Ordemservico osAux : ordemservico.getExpedicao().getListaOrdensservico()){
				if (osAux.getCarregamento() != null && !idsCarregamentos.contains(osAux.getCarregamento().getCdcarregamento())){
					idsCarregamentos.add(osAux.getCarregamento().getCdcarregamento());
					String veiculo = "";
					if (osAux.getCarregamento().getVeiculo() != null && osAux.getCarregamento().getVeiculo().getPlaca() != null)
						veiculo = " - " + osAux.getCarregamento().getVeiculo().getPlaca();
					carregamentos.add(osAux.getCarregamento().getCdcarregamento() + veiculo);
				}
			}
			
			writeLine("Carregamento/Veículo :");
			for (String item : carregamentos)
				writeLine(item);
			writeLine("");
			writeLine("O.S.: " + ordemservico.getCdordemservico());
		} else if (ordemservico.getCarregamento() != null){
			writeLine("");
			writeLine("Carregamento: " + ordemservico.getCarregamento().getCdcarregamento());
			writeLine("");
			if (ordemservico.getCarregamento().getVeiculo() != null && ordemservico.getCarregamento().getVeiculo().getPlaca() != null) {
				writeLine("Veículo: " + ordemservico.getCarregamento().getVeiculo().getPlaca() + "");
				writeLine("");
			}
			writeLine("Box: " + ordemservico.getCarregamento().getBox().getNome() + "");
			writeLine("");
			writeLine("O.S.: " + ordemservico.getCdordemservico());
		}
		
		read();
		
	
		
		// ///////////////////////////////////////////////////////////////
		
		ordemservico.getListaOrdemServicoUsuario().clear();
		OrdemservicoUsuario ordemservicoUsuario = associarUsuarioOS(ordemservico);
		
		if (ordemservicoUsuario == null)
			return;
		
		ordemservico.getListaOrdemServicoUsuario().add(ordemservicoUsuario);

		List<Ordemprodutohistorico> listaOPH = OrdemprodutohistoricoService.getInstance().findForConferenciaColetor(
				ordemservico, true);

		ordemservico.setListaOrdemProdutoHistorico(listaOPH);
		List<Ordemprodutohistorico> listaExecucao = new ArrayList<Ordemprodutohistorico>(listaOPH);
		
		enderecosNaoLidos = new HashSet<Endereco>();
		for (Ordemprodutohistorico oph : listaExecucao){
			for (Ordemservicoprodutoendereco ospe : oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco()){
				if (oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco() != null){
					enderecosNaoLidos.add(ospe.getEnderecoorigem());
				}
			}
		}
		
		loop_endereco:
		while (listaExecucao.size() > 0) {
			Ordemprodutohistorico oph = listaExecucao.get(0);
			
			oph.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().setOrdemservico(
					oph.getOrdemservico());
			
			//Se o item já foi executado então ignora ele e passa para o próximo
			if (!oph.getOrdemservicoproduto().getOrdemprodutostatus().equals(Ordemprodutostatus.NAO_CONCLUIDO)){
				listaExecucao.remove(0);
				continue;
			}
			
			exibirCabecalho(oph.getOrdemservicoproduto(), true);

			//se existem endereços definidos
			if (oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco() != null
					&& !oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().isEmpty()){
				
				for (Ordemservicoprodutoendereco ospe : oph.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco()) {
					ospe.setOrdemservicoproduto(oph.getOrdemservicoproduto());
					RetornoLeitura leituraEndereco;
	
					if (ospe.getEnderecoorigem() != null)
						leituraEndereco = lerEndereco(ospe);
					else
						leituraEndereco = RetornoLeitura.LEITURA_OK;
	
					if (RetornoLeitura.LEITURA_OK.equals(leituraEndereco)){
						RetornoLeitura leituraProduto = lerProduto(oph.getOrdemservicoproduto());
						if (RetornoLeitura.LEITURA_OK.equals(leituraProduto))
							coletarQtde(oph, ospe);
						else if (RetornoLeitura.ABORTAR.equals(leituraProduto))
							return;
						else if (RetornoLeitura.PULAR_ITEM.equals(leituraProduto)){
							if (listaExecucao.size() > 1){
								//Remove adiciona no final da lista
								listaExecucao.remove(0);
								listaExecucao.add(oph);
								continue loop_endereco;
							}
						}
					}else if (RetornoLeitura.PULAR_ITEM.equals(leituraEndereco)){
						if (listaExecucao.size() > 1){
							//Remove adiciona no final da lista
							listaExecucao.remove(0);
							listaExecucao.add(oph);
							continue loop_endereco;
						}
					}else
						return;
				}
			}else{//Não há endereços definidos
				RetornoLeitura leitura = lerProduto(oph.getOrdemservicoproduto());
				if (RetornoLeitura.LEITURA_OK.equals(leitura))
					coletarQtde(oph, null);
				else if (RetornoLeitura.ABORTAR.equals(leitura))
					return;
				else if (RetornoLeitura.PULAR_ITEM.equals(leitura)){
					if (listaExecucao.size() > 1){
						//Remove adiciona no final da lista
						listaExecucao.remove(0);
						listaExecucao.add(oph);
						
						continue loop_endereco;
					}
				}
			}
		}

		finalizarSeparacao(ordemservico);

	}

	private RetornoLeitura lerProduto(Ordemservicoproduto ordemservicoproduto) throws IOException {
		boolean produtoValido = false;
		
		do {
			exibirCabecalho(ordemservicoproduto, true);

			String codigo = readLine("Produto:");

			if ("0".equals(codigo)) {
				Acao acaoExecutada = executarAcao(ordemservicoproduto.getListaOrdemprodutoLigacao()
						.iterator().next().getOrdemservico(), true, enderecosNaoLidos.isEmpty());

				if (Acao.PROXIMO_ENDERECO.equals(acaoExecutada)){
					return RetornoLeitura.PULAR_ITEM;
					
				} else if (Acao.FINALIZAR_OS.equals(acaoExecutada) || Acao.CANCELAR.equals(acaoExecutada) 
						|| Acao.SAIR.equals(acaoExecutada)){
					
					if (isConvocacaoAtiva())
						logout();
					
					return RetornoLeitura.ABORTAR;
				}else if (Acao.RETORNAR.equals(acaoExecutada))
					continue;
			}

			Produto aux;
			try {
				aux = ProdutoService.getInstance().findProdutoByBarcode(codigo.toUpperCase(), deposito, !ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, deposito));

				if (aux == null) {
					alertError("Produto inválido.");
					continue;
				}
			} catch (Exception e) {
				alertError("Produto inválido.");
				continue;
			}

			produtoValido = aux.equals(ordemservicoproduto.getProduto());

			if (!produtoValido) {
				alertError("Produto inválido.");
				continue;
			}

		} while (!produtoValido);

		return RetornoLeitura.LEITURA_OK;
	}

	/**
	 * Faz a coleta da quantidade para o item atual.
	 * 
	 * @param oph
	 * 
	 * @param ospe
	 * @throws IOException
	 */
	private void coletarQtde(final Ordemprodutohistorico oph, final Ordemservicoprodutoendereco ospe) throws IOException {
		exibirCabecalho(oph.getOrdemservicoproduto(), false);
		
		long qtdeEsperada;
		
		if (ospe != null){
			qtdeEsperada = ospe.getQtde() != null ? ospe.getQtde() : 0L;

			if (ospe.getEnderecoorigem() != null){
				writeLine("Origem: " + ospe.getEnderecoorigem().getEnderecoArea());
			}			
		}else{
			qtdeEsperada = oph.getOrdemservicoproduto().getQtdeesperada() != null ? 
					oph.getOrdemservicoproduto().getQtdeesperada() : 0L;
		}
		
		writeLine("Qtde. Esperada: " + qtdeEsperada);
		writeSeparator();
			
		Integer qtde = readInteger("Qtde.: ");

		if (qtde != null) {
			if (qtde.intValue() > qtdeEsperada) {
				alertError("A quantidade digitada está maior que a quantidade esperada.");
				coletarQtde(oph, ospe);
				return;
			} else if (qtde.intValue() < qtdeEsperada && !confirmAction("A quantidade digitada está menor que a quantidade esperada. "
					+ "Prosseguir assim mesmo?")) {
				
				coletarQtde(oph, ospe);
				return;
			}

			oph.setQtde((oph.getQtde() != null ? oph.getQtde() : 0L) + qtde);
			
			Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
				public Object doInTransaction(TransactionStatus status) {
					OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph);

					//Atualizando a hora da última movimentação feita
					OrdemservicoUsuario osu = oph.getOrdemservico().getListaOrdemServicoUsuario().iterator().next();
					osu.setDtfim(new Timestamp(System.currentTimeMillis()));
					OrdemservicousuarioService.getInstance().saveOrUpdate(osu);

					long qtdeLida = oph.getQtde() != null ? oph.getQtde() : 0L;
					long qtdeEsperada = oph.getOrdemservicoproduto().getQtdeesperada() != null ? 
							oph.getOrdemservicoproduto().getQtdeesperada() : 0L;
							
					if (qtdeLida == qtdeEsperada){
						oph.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
					}else{
						oph.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_DIVERGENTE);						
					}
					
					OrdemservicoprodutoService.getInstance().atualizarStatus(oph.getOrdemservicoproduto());
					
					List<Etiquetaexpedicao> listaEtiquetaExpedicao = EtiquetaexpedicaoService.getInstance().findBy(oph.getOrdemservicoproduto(), new String[]{});
					
					if (listaEtiquetaExpedicao != null && listaEtiquetaExpedicao.size() > 0){
						Etiquetaexpedicao etiquetaexpedicao = listaEtiquetaExpedicao.get(0);
						etiquetaexpedicao.setQtdecoletor(oph.getQtde());
						EtiquetaexpedicaoService.getInstance().updateQtdecoletor(etiquetaexpedicao);
					}
					
					return null;
				}
			});
		}
	}

	/**
	 * Aguarda a leitura de um endereço.
	 * 
	 * @param enderecoorigem
	 * @return <code>false</code> se o usuário cancelou a leitura do endereço.
	 * @throws IOException
	 */
	private RetornoLeitura lerEndereco(Ordemservicoprodutoendereco ospe) throws IOException {
		boolean enderecoValido = false;

		do {
			exibirCabecalho(ospe.getOrdemservicoproduto(), true);
			writeLine("Origem: " + ospe.getEnderecoorigem().getEnderecoArea());
			writeSeparator();

			String codigo = readLine("Endereço:");

			if ("0".equals(codigo)) {
				Acao acaoExecutada = executarAcao(ospe.getOrdemservicoproduto().getListaOrdemprodutoLigacao()
						.iterator().next().getOrdemservico(), false, enderecosNaoLidos.isEmpty());

				if (Acao.PROXIMO_ENDERECO.equals(acaoExecutada)){
					return RetornoLeitura.PULAR_ITEM;
				} else if (Acao.FINALIZAR_OS.equals(acaoExecutada) || Acao.CANCELAR.equals(acaoExecutada) 
						|| Acao.SAIR.equals(acaoExecutada)){
					
					if (isConvocacaoAtiva())
						logout();
					
					return RetornoLeitura.ABORTAR;
				}else if (Acao.RETORNAR.equals(acaoExecutada))
					continue;
			}

			if (ospe.getEnderecoorigem().getEnderecofuncao().equals(Enderecofuncao.BLOCADO)) {
				Endereco aux;
				try {
					aux = EnderecoService.getInstance().prepareLoadEnderecoByCodigoEtiqueta(codigo, deposito);
				} catch (Exception e) {
					alertError("Endereço inválido.");
					continue;
				}
				enderecoValido = EnderecoService.getInstance().predioEquals(ospe.getEnderecoorigem(), aux);
			} else {
				Endereco aux;
				try {
					aux = EnderecoService.getInstance().loadEnderecoByCodigoEtiqueta(codigo, this.deposito);

					if (aux == null) {
						alertError("Endereço inválido.");
						continue;
					}
				} catch (Exception e) {
					alertError("Endereço inválido.");
					continue;
				}

				enderecoValido = aux.equals(ospe.getEnderecoorigem());
			}

			if (!enderecoValido) {
				alertError("Endereço inválido.");
				continue;
			}

		} while (!enderecoValido);

		enderecosNaoLidos.remove(ospe.getEnderecoorigem());
		return RetornoLeitura.LEITURA_OK;
	}

	/**
	 * Mostra as opções para o usuário
	 * 
	 * @param ordemservico
	 * 
	 * @return
	 * @throws IOException
	 */
	protected Acao executarAcao(Ordemservico ordemservico, boolean permitirPularItem, boolean permitirFinalizar) throws IOException {
		drawEsqueleto("");

		writeLine("Selecione a ação desejada.");
		writeLine("");
		writeLine("");
		writeLine("0 - Retornar");
		writeLine("1 - Cancelar separação");
		if (isConvocacaoAtiva())
			writeLine("2 - Sair");
		else
			writeLine("2 - Trocar de carregamento");
		
		if (permitirPularItem)
			writeLine("3 - Próximo item");

		if (permitirFinalizar)
			writeLine("4 - Finalizar separação");

		writeLine("");

		final Editfield confirmEdf = new Editfield(getTermIO(), "edf", 1);
		confirmEdf.registerInputValidator(new InputValidator() {

			public boolean validate(String str) {
				return "0".equals(str) || "1".equals(str) || "2".equals(str) || "3".equals(str) || "4".equals(str);
			}

		});
		confirmEdf.run();
		String opcao = confirmEdf.getValue();

		if ("0".equals(opcao)) {
			return Acao.RETORNAR;
		} else if ("3".equals(opcao)) {
			return Acao.PROXIMO_ENDERECO;
		} else if ("1".equals(opcao)) {
			if (confirmAction(CANCELAR_OPERACAO)) {
				cancelarExecucao(ordemservico);
				return Acao.CANCELAR;
			} else
				return Acao.RETORNAR;
		} else if ("2".equals(opcao)) {
			if (!isConvocacaoAtiva())
				return Acao.SAIR;
			else if (confirmAction(CONFIRMAR_SAIR)) {
				return Acao.SAIR;
			} else
				return Acao.RETORNAR;
		} else if ("4".equals(opcao) && permitirFinalizar){
			finalizarSeparacao(ordemservico);
			return Acao.FINALIZAR_OS;
		}

		return Acao.RETORNAR;
	}

	/**
	 * Descarta as últimas coletas feitas, retorna o status da ordem de serviço
	 * para {@link Ordemstatus#EM_ABERTO} e exclui o {@link OrdemservicoUsuario} 
	 * que foi criado nesta execução.
	 * 
	 * @param ordemservico
	 * @throws IOException
	 */
	private void cancelarExecucao(final Ordemservico ordemservico) throws IOException {
		if (!isOrdemAindaAberta(ordemservico)){
			return;
		}
		
		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				for (Ordemprodutohistorico oph : ordemservico.getListaOrdemProdutoHistorico()){
					oph.setQtde(null);
					OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph);
					
					oph.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.NAO_CONCLUIDO);
					OrdemservicoprodutoService.getInstance().atualizarStatus(oph.getOrdemservicoproduto());
					
					
					EtiquetaexpedicaoService.getInstance().resetQtdecoletor(ordemservico);
				}
				ordemservico.setOrdemstatus(Ordemstatus.EM_ABERTO);
				OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
				
				OrdemservicousuarioService.getInstance().delete(ordemservico.getListaOrdemServicoUsuario().iterator().next());
				ordemservico.getListaOrdemServicoUsuario().clear();

				return null;
			}
		});
		
		writeOnCenter(OPERACAO_CANCELADA, null, true, true);
	}

	/**
	 * Atualiza o status da ordem de serviço para
	 * {@link Ordemstatus#FINALIZADO_SUCESSO} ou
	 * {@link Ordemstatus#FINALIZADO_DIVERGENCIA} e atualiza a hora final em
	 * {@link OrdemservicoUsuario}.
	 * 
	 * @param ordemservico
	 * @throws IOException
	 */
	private void finalizarSeparacao(final Ordemservico ordemservico) throws IOException {
		boolean finalizadoComSucesso = true;

		for (Ordemprodutohistorico oph : ordemservico.getListaOrdemProdutoHistorico()) {
			long qtdeLida = oph.getQtde() != null ? oph.getQtde() : 0L;
			long qtdeEsperada = oph.getOrdemservicoproduto().getQtdeesperada() != null ? 
					oph.getOrdemservicoproduto().getQtdeesperada() : 0L;
					
			if (qtdeLida != qtdeEsperada){
				finalizadoComSucesso = false;
				break;
			}
		}
		
		if (finalizadoComSucesso)
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_SUCESSO);
		else
			ordemservico.setOrdemstatus(Ordemstatus.FINALIZADO_DIVERGENCIA);

		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
				
				OrdemservicoUsuario osu = ordemservico.getListaOrdemServicoUsuario().iterator().next();
				osu.setDtfim(new Timestamp(System.currentTimeMillis()));
				OrdemservicousuarioService.getInstance().saveOrUpdate(osu);
				
				return null;
			}
		});
		
		prepararTela(false);
		
		writeLine("");
		writeLine("O.S.: " + ordemservico.getCdordemservico());
		if (ordemservico.getCarregamento() != null){
			writeLine("Carregamento: " + ordemservico.getCarregamento().getCdcarregamento());
			if (ordemservico.getCarregamento().getVeiculo() != null && ordemservico.getCarregamento().getVeiculo().getPlaca() != null)
				writeLine("Veículo: " + ordemservico.getCarregamento().getVeiculo().getPlaca());
			writeLine("Box: " + ordemservico.getCarregamento().getBox().getNome());
		} else if (ordemservico.getExpedicao() != null){
			writeLine("Expedição: " + ordemservico.getExpedicao().getCdexpedicao());
			writeLine("Box: " + ordemservico.getExpedicao().getBox().getNome());
		}
		writeLine("");
		
		if (finalizadoComSucesso)
			writeOnCenter(FINALIZADO_COM_SUCESSO, null, false, true);
		else
			writeOnCenter(FINALIZADO_COM_DIVERGENCIA, null, false, true);
	}

	/**
	 * Exibe os dados do produto que deve ser coletado.
	 * @param permiteSair 
	 * 
	 * @param ordemservico
	 * 
	 * @param oph
	 * @throws IOException
	 */
	private void exibirCabecalho(Ordemservicoproduto osp, boolean permiteSair) throws IOException {
		Ordemservico ordemservico = osp.getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();

		prepararTela(permiteSair);
		if (ordemservico.getCarregamento() != null){
			writeLine("Carregamento: " + ordemservico.getCarregamento().getCdcarregamento());
			writeLine("Box: " + ordemservico.getCarregamento().getBox().getNome());
		} else if (ordemservico.getExpedicao() != null){
			writeLine("Expedição: " + ordemservico.getExpedicao().getCdexpedicao());
			writeLine("Box: " + ordemservico.getExpedicao().getBox().getNome());
		}
		writeLine("O.S.: " + ordemservico.getCdordemservico());
		writeSeparator();
		writeLine("Código: " + osp.getProduto().getCodigo());
		writeLine("Descrição: " + osp.getProduto().getDescricao());
		writeSeparator();
	}

	/**
	 * Associa o usuário que está logado no box do recebimento, e amarra ele a
	 * última ordem de serviço.
	 * 
	 * @return
	 * @throws IOException 
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicousuarioService#salvarOrdemServicoUsuarioForConferencia(ConferenciaCegaPapelFiltro
	 *      filtro)
	 */
	private OrdemservicoUsuario associarUsuarioOS(final Ordemservico ordemservico) throws IOException {
		if (!isOrdemAindaAberta(ordemservico)){
			return null;
		}
		
		final OrdemservicoUsuario ordemservicoUsuario = new OrdemservicoUsuario();
		ordemservico.setOrdemstatus(Ordemstatus.EM_EXECUCAO);
		
		ordemservicoUsuario.setDtinicio(new Timestamp(System.currentTimeMillis()));
		ordemservicoUsuario.setOrdemservico(ordemservico);
		ordemservicoUsuario.setUsuario(usuario);

		Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				OrdemservicoService.getInstance().atualizarStatusordemservico(ordemservico);
				OrdemservicousuarioService.getInstance().saveOrUpdate(ordemservicoUsuario);

				return null;
			}
		});

		return ordemservicoUsuario;
	}

	/**
	 * Prepara o cabeçalho e rodapé da tela.
	 * 
	 * @throws IOException
	 */
	private void prepararTela(boolean permitirSair) throws IOException {
		if (permitirSair)
			drawEsqueleto(DIGITE_0_PARA_SAIR);
		else
			drawEsqueleto("");
		
		writeOnCenter(TITULO, null, false, false);
		writeSeparator();
	}

}
