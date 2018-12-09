package br.com.linkcom.wmsconsole.system;

import java.io.IOException;
import java.util.List;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.toolkit.Editfield;
import net.wimpi.telnetd.io.toolkit.InputFilter;
import net.wimpi.telnetd.io.toolkit.InputValidator;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;


public abstract class ExecucaoOSWindow extends TelnetWindow {

	protected static final String DIGITE_0_PARA_SAIR = "Digite 0 para sair.";
	protected static final String DIGITE_0_PARA_ACOES = "Digite 0 para a��es.";
	protected static final String CONFIRMAR_SAIR = "Tem certeza que deseja sair?";
	protected static final String CANCELAR_OPERACAO = "Cancelar opera��o?";

	public enum Acao { RETORNAR, CANCELAR, FINALIZAR_OS, PROXIMO_ENDERECO, SAIR }
	public enum RetornoLeitura {ABORTAR, LEITURA_OK, PULAR_ITEM}
	public enum TipoColeta { PADRAO, FRACIONADA, AVARIA};
	
	/**
	 * <p>Exibe uma linha pedindo o usu�rio para informar o produto e valida se o usu�rio informou
	 * o produto esperado. </p>
	 * <p>Caso tenha sido bipado pelo c�digo de barras e se o produto � o esperado,
	 * grava em {@link Ordemservicoproduto} o c�digo de barras que foi lido.</p>
	 * 
	 * @author Giovane Freitas
	 * @param listaOSP
	 * @return
	 * @throws IOException
	 */
	protected Ordemservicoproduto biparProduto(List<Ordemservicoproduto> listaOSP) throws IOException{
		String codigoLido = readCodigoProduto();
		
		for (Ordemservicoproduto osp : listaOSP){
			Produto produto = osp.getProduto();
			if((!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito))
					&& getCodigoProduto(produto.getCodigo()).toUpperCase().equals(codigoLido.toUpperCase())){ // Se o c�digo � igual ao codigo do produto
				return osp;
			}else{
				for(int i = 0; i < produto.getListaProdutoCodigoDeBarras().size(); i++){ // Se o c�digo � igual a algum c�digo de barras o produto
					Produtocodigobarras pcb = produto.getListaProdutoCodigoDeBarras().get(i);
					if(getCodigoProduto(pcb.getCodigo()).toUpperCase().equals(codigoLido.toUpperCase())){
						osp.setCodigoBarrasLido(pcb);
						return osp;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Solicita que usu�rio informe um c�digo de produto e procura o objeto {@link Ordemprodutohistorico} associado.
	 * 
	 * @param codigo
	 * @return null caso n�o tenha sido encontrado nenhuma ocorr�ncia.
	 * @throws IOException 
	 */
	protected Ordemprodutohistorico biparProduto(List<Ordemprodutohistorico> listaOPH) throws IOException{
		String codigoLido = readCodigoProduto();
		
		Ordemservicoproduto osp = null;
		for (Ordemprodutohistorico oph : listaOPH) {
			osp = oph.getOrdemservicoproduto();
			
			List<Produtocodigobarras> listaProdutoCodigoDeBarras = osp.getProduto().getListaProdutoCodigoDeBarras();
			
			String cod = getCodigoProduto(osp.getProduto().getCodigo().trim());
			
			//O sistema pode ser configurado para n�o aceitar leitura de c�digo interno
			if((!ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.COLETOR_EXIGE_CODIGOBARRAS, this.deposito))
					&& cod.toUpperCase().equals(codigoLido.trim().toUpperCase())){
				oph.setCodigoBarrasIndex(null);//N�o encontrou atrav�s do c�digo de barras
				return oph;
			}
			
			for (int i = 0; i < listaProdutoCodigoDeBarras.size(); i++) {
				Produtocodigobarras produtocodigobarras = listaProdutoCodigoDeBarras.get(i);

				String codigo2 = produtocodigobarras.getCodigo();
				
				if(codigoLido.trim().equals(codigo2.trim())){
					oph.setCodigoBarrasIndex(i);//Indice que encontrou o c�digo de barras
					oph.setCodigoBarrasConferencia(codigo2); // codigo de barras utilizado para identificar o produto.
					return oph;
				}
			}
		}
		return null;
	}
	
	/**
	 * Exibe uma linha para digita��o do c�digo do produto e 
	 * aguarda a entrada do usu�rio.
	 * 
	 * @author Giovane Freitas
	 * @return
	 * @throws IOException
	 */
	protected String readCodigoProduto() throws IOException{
		return getCodigoProduto(readLine("Informe o produto:"));
	}
	

	/**
	 * Respons�vel por interpretar c�digos ean 128.
	 * 
	 * Caso n�o seja um c�digo ean128, a mesma string de entrada ser� retornada.
	 * 
	 * @param codigo
	 * @author Pedro Gon�alves
	 * @return
	 */
	protected String getCodigoProduto(String codigo){
		//se n�o for um c�digo ean passar direto.
		if(codigo == null || codigo.equals("")){
			return codigo;
		}
		
		//Verifica se n�o � um c�digo EAN128
		if(!codigo.startsWith("]C")){
			
			//C�digos EAN128 com defeito
			if(codigo.startsWith("8006") && codigo.length() > 20){
				String codigoTemp = codigo.substring(5,22);
				
				//verifica se possui volume
				if ("0101".equals(codigo.substring(21,25)))
					codigoTemp = codigoTemp.substring(0,13);
				
				return codigoTemp;
			} else
				return codigo;
		} else {
			if(codigo.startsWith("]C1010")) 
				return codigo.substring(6,19);
			else if(codigo.startsWith("]C") && codigo.length() > 20){
			
				String codigoTemp = codigo.substring(8,25);
				
				//verifica se possui volume
				if ("0101".equals(codigo.substring(21,25)))
					codigoTemp = codigoTemp.substring(0,13);
				
				return codigoTemp;
			} else 
				return ".";//Retorna um valor para que seja dado como inv�lido
		}
	}
	

	/**
	 * L� os c�digos de barras. Quando encontrar o caracter # na string, � acionado o enter automaticamente.
	 * 
	 * @param label
	 * @return
	 * @throws IOException
	 */
	protected String readBarcode(String label) throws IOException {
		exibirMenu = false;
		
		if (label != null)
			writeLine(label);
		getTermIO().write("#");
		
		final Editfield produtoEdf = new Editfield(getTermIO(),"edf",50);
		produtoEdf.registerInputFilter(new InputFilter(){

			public int filterInput(int key) throws IOException {
				if(key == 35) {
					return BasicTerminalIO.ENTER;
				}
				return key;
			}
			
		});
		
		produtoEdf.registerInputValidator(new InputValidator(){

			public boolean validate(String str) {
				return !"".equals(str);
			}
			
		});
		produtoEdf.run();
		String valueProduto = produtoEdf.getValue();
		
		//Fazendo uma quebra de linha, para que a pr�xima informa��o n�o fique na mesma linha.
		writeLine("");
		
		if(valueProduto.equals("0")){
			exibirMenu = true;
			return valueProduto;
		}
		
		if(valueProduto != null && !valueProduto.equals(""))
			valueProduto = getCodigoProduto(valueProduto);
		return valueProduto;
	}
	
	/**
	 * M�todo que deve ser implementado por toda tela para executar uma dada Ordem de Servi�o.
	 * Este m�todo ser� chamado pelo processo de convoca��o ativa.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @throws IOException 
	 */
	public abstract void executarOrdem(Ordemservico ordemservico) throws IOException;
	
	/**
	 * Obt�m o t�tulo da janela.
	 * 
	 * @return
	 */
	public abstract String getTitulo();
	
	/**
	 * Desenha o esqueleto e o t�tulo da tela.
	 * 
	 * @author Giovane Freitas
	 * @param rodape
	 * @throws IOException
	 */
	protected void showCabecalho(String rodape) throws IOException {
		drawEsqueleto(rodape);
		writeOnCenter(getTitulo(), null, false, false);
		writeSeparator();
	}
	

	protected boolean isOrdemAindaAberta(Ordemservico ordemservico) throws IOException {
		
		Ordemservico ordemservicoAux = OrdemservicoService.getInstance().load(ordemservico);
		if (!ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_ABERTO) 
				&& !ordemservicoAux.getOrdemstatus().equals(Ordemstatus.EM_EXECUCAO)){
			
			alertError("Esta ordem de servi�o foi finalizada por outro operador.");
			return false;
		} else
			return true;
	}
	
}
