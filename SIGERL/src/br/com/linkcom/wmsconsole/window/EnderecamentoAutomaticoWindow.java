package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutoligacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Recebimentonotafiscal;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.EnderecoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemprodutohistoricoService;
import br.com.linkcom.wms.geral.service.OrdemservicoprodutoService;
import br.com.linkcom.wms.geral.service.OrdemservicousuarioService;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;

/**
 * 
 * @author Leonardo Guimarães
 *
 */
public class EnderecamentoAutomaticoWindow extends EnderecamentoManualWindow{
	
	private static final String TITULO = "Endereçamento automático";
	private Ordemservicoprodutoendereco enderecoLido;
	
	@Override
	protected Tipoenderecamento getTipoEnderecamento() {
		return Tipoenderecamento.AUTOMATICO;
	}
	
	private boolean validarProduto(String codigo, List<Ordemservicoproduto> lista) {
		if (TipoColeta.PADRAO.equals(tipoColeta)){
			for (Ordemservicoproduto osp : lista){
				Iterator<Ordemservicoprodutoendereco> iterator = osp.getListaOrdemservicoprodutoendereco().iterator();
				while ( iterator.hasNext() ){
					Ordemservicoprodutoendereco ospe = iterator.next();
					
					if (ospe.getEtiquetaUMA() == null){
						Enderecoproduto enderecoproduto = EnderecoprodutoService.getInstance().loadByEnderecoEProdutoUma(ospe.getEnderecodestino(),osp.getProduto());
						ospe.setEtiquetaUMA(enderecoproduto.getCdenderecoproduto().toString());
					}
					
					try{
						if (Long.valueOf(ospe.getEtiquetaUMA()).equals(Long.valueOf(codigo))){
							if (isOrdemservicoprodutoEmAberto(osp)){
								enderecoLido = ospe;
								osp.setOrdemprodutostatus(Ordemprodutostatus.EM_EXECUCAO);
								OrdemservicoprodutoService.getInstance().atualizarStatus(osp);

								Ordemservico ordemservico = enderecoLido.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
								if(ordemservico.getListaOrdemServicoUsuario().isEmpty()){
									OrdemservicoUsuario osu = OrdemservicousuarioService.getInstance().associarUsuario(usuario, ordemservico);
									ordemservico.getListaOrdemServicoUsuario().add(osu);
								}
								
								return true;
							} else {
								iterator.remove();
								alertError(UMA_EM_EXECUCAO_POR_OUTRO_OPERADOR);
							}
						}
					}catch (Exception e) {
						//Erro ao converter o código para Long.
						return false;
					}
				}
			}
			
			//Se não achou a etiqueta retorna false
			return false;
		}else{
			for (Ordemservicoproduto osp : lista){
				Produto produto = osp.getProduto();
				
				
				if((!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito))
						&& getCodigoProduto(produto.getCodigo()).toUpperCase().equals(getCodigoProduto(codigo).toUpperCase())){ // Se o código é igual ao codigo do produto
					enderecoLido = osp.getListaOrdemservicoprodutoendereco().get(0);
					return true;
				}else{
					for(int i = 0; i < produto.getListaProdutoCodigoDeBarras().size(); i++){ // Se o código é igual a algum código de barras o produto
						Produtocodigobarras pcb = produto.getListaProdutoCodigoDeBarras().get(i);
						if(getCodigoProduto(pcb.getCodigo()).toUpperCase().equals(codigo.toUpperCase())){
							enderecoLido = osp.getListaOrdemservicoprodutoendereco().get(0);
							osp.setCodigoBarrasLido(pcb);
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
	}

	private boolean biparUMA(List<Ordemservicoproduto> lista) throws IOException, TrocarRecebimentoException {
		while(true){
			drawEsqueleto(this.cabecalho,"Digite 0 para ações.");
			
			writeOnCenter(getTitulo(), null, false, false);
			writeSeparator();
			Recebimentonotafiscal next = recebimento.getListaRecebimentoNF().iterator().next();
			writeLine("Recebimento: " + this.recebimento.getCdrecebimento());			
			writeLine("Veiculo: " + next.getNotafiscalentrada().getVeiculo());
			writeSeparator();

			String msg;
			if (TipoColeta.PADRAO.equals(tipoColeta))
				msg = "Informe a UMA:";
			else
				msg = "Informe o produto:";
			
			String codigo = readBarcode(msg);
			if(exibirMenu){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao) || Acao.FinalizarEnderecamento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					return biparUMA(lista);
			}
			
			if(validarProduto(codigo, lista))
				return true;
			 
			alertProdutoInvalido(15);
		}
	}
	
	@Override
	protected void coletarPorProduto() throws IOException, TrocarRecebimentoException {
		final List<Ordemservicoproduto> lista;
		
		if (TipoColeta.PADRAO.equals(tipoColeta)) {
			lista = this.listaUma;
		} else if (TipoColeta.FRACIONADA.equals(tipoColeta)) {
			lista = this.listaFracionada;
		} else if (TipoColeta.AVARIA.equals(tipoColeta))
			lista = this.listaAvariada;
		else
			lista = new ArrayList<Ordemservicoproduto>();

		this.enderecoLido = null;
		
		while (lista.size() > 0) {
			if (biparUMA(lista) && this.enderecoLido != null) {// Se a etiqueta UMA está correta
				Ordemservico ordemservico = enderecoLido.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
				iniciarExecucao(ordemservico);
				
				if (biparEndereco(this.enderecoLido)){
					try{
						Neo.getObject(TransactionTemplate.class).execute(new TransactionCallback(){
							public Object doInTransaction(TransactionStatus status) {
								try {
									enderecarProdutoBipado(lista);
								} catch (TrocarRecebimentoException e) {
									throw new WmsException(e.getMessage(), e);
								}
								return null;
							}
						});
					}catch (Exception e) {
						if (e.getCause() instanceof TrocarRecebimentoException)
							throw new TrocarRecebimentoException();
						else 
							throw new WmsException(e.getMessage(), e);
					}
					
					//Se terminou alerta o usuário
					if(ordemservico.getListaOrdemProdutoLigacao().isEmpty())
						alertConclusaoEnderecamento();
				}
				
				this.enderecoLido = null;
			} else
				break;
		}

	}
	
	/**
	 * Verifica se o endereço bipado é válido
	 * @param osp 
	 * 
	 * @return
	 * @throws IOException 
	 * @throws TrocarRecebimentoException 
	 */
	private boolean biparEndereco(Ordemservicoprodutoendereco ospe) throws IOException, TrocarRecebimentoException {
		while(true){
			
			drawEsqueleto(this.cabecalho,"Digite 0 para ações.");
			makeCabecalhoColeta();
			showInformationProduto(ospe.getOrdemservicoproduto(), false);
			showEndereco(ospe);
			
			String codigoEtiqueta = readBarcode("Informe o end. de destino:");
			if(exibirMenu){
				Acao acao = menuMaisAcoes();
				if (Acao.TrocarRecebimento.equals(acao))
					throw new TrocarRecebimentoException();
				else
					return biparEndereco(ospe);
			}
			
			try{
				
				if(this.enderecoLido.getEnderecodestino().getEnderecofuncao().equals(Enderecofuncao.PICKING)){
					Endereco aux = EnderecoService.getInstance().loadEnderecoByCodigoEtiqueta(codigoEtiqueta, this.deposito);
					if (this.enderecoLido.getEnderecodestino().equals(aux))
						return true;
					else
						alertError(ENDERECO_INVALIDO);
				}else{
					Endereco aux = EnderecoService.getInstance().prepareLoadEnderecoByCodigoEtiqueta(codigoEtiqueta, this.deposito);
					if (EnderecoService.getInstance().predioEquals(this.enderecoLido.getEnderecodestino(), aux))
						return true;
					else
						alertError(ENDERECO_INVALIDO);
				}
			}catch (EnderecoInvalidoException e) {
				alertError(ENDERECO_INVALIDO);
			}
		}
	}
	
	@Override
	protected void alertProdutoInvalido(int row) throws IOException {
		if(isUMA){
			alertError("UMA inválida.");
		}else
			super.alertProdutoInvalido(row);
	}
	
	private void enderecarProdutoBipado(final List<Ordemservicoproduto> lista) throws TrocarRecebimentoException {
		//ATENÇÃO: Este método não deve fazer operações de IO, pois é executado dentro de uma transação.
		
		Ordemservico ordemservico = enderecoLido.getOrdemservicoproduto().getListaOrdemprodutoLigacao().iterator().next().getOrdemservico();
		
		validarOrdemServico(ordemservico);
		
		Endereco end = enderecoLido.getEnderecodestino();

		enderecoLido.getOrdemservicoproduto().setQtdeFaltante(enderecoLido.getOrdemservicoproduto().getQtdeFaltante() - enderecoLido.getQtde());
		
		//Atualizando o histórico de coleta
		Ordemprodutohistorico oph = enderecoLido.getOrdemservicoproduto().getListaOrdemprodutohistorico().iterator().next();
		oph.setQtde(enderecoLido.getOrdemservicoproduto().getQtdeesperada() - enderecoLido.getOrdemservicoproduto().getQtdeFaltante());
		if (oph.getQtdeavaria() == null)
			oph.setQtdeavaria(0L);
		if (oph.getQtdefracionada() == null)
			oph.setQtdefracionada(0L);
		OrdemprodutohistoricoService.getInstance().atualizarQuantidades(oph );
		
		enderecoLido.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().remove(enderecoLido);
		
		atualizarPaletesExecutados(ordemservico, enderecoLido.getQtde(), enderecoLido.getOrdemservicoproduto().getProduto());
		
		if (enderecoLido.getOrdemservicoproduto().getListaOrdemservicoprodutoendereco().isEmpty()){
			enderecoLido.getOrdemservicoproduto().setOrdemprodutostatus(Ordemprodutostatus.CONCLUIDO_OK);
			OrdemservicoprodutoService.getInstance().atualizarStatus(enderecoLido.getOrdemservicoproduto());
			lista.remove(enderecoLido.getOrdemservicoproduto());
			
			//esvaziando a lista para saber quando terminou
			for (Ordemprodutoligacao opl : enderecoLido.getOrdemservicoproduto().getListaOrdemprodutoLigacao())
				ordemservico.getListaOrdemProdutoLigacao().remove(opl);
			
			//Se terminou muda o status para finalizado
			if(ordemservico.getListaOrdemProdutoLigacao().isEmpty() && OrdemservicoprodutoService.getInstance().isTodosItensFinalizados(ordemservico)){
				Neo.getObject(TransactionTemplate.class).execute(new FinalizaExecucaoOS(ordemservico));
			}
		}

		List<Dadologistico> listaDadoLogistico = enderecoLido.getOrdemservicoproduto().getProduto().getListaDadoLogistico();
		Dadologistico dadologistico = listaDadoLogistico!= null && !listaDadoLogistico.isEmpty() ? listaDadoLogistico.get(0) : null;
		if(dadologistico != null && end.getLarguraexcedente() && dadologistico.getLarguraexcedente()){
				EnderecoService.getInstance().emprestarEnderecoVisinho(end,deposito);
		}
		
	}
	
	@Override
	public String getTitulo() {
		return TITULO;
	}
	
}
