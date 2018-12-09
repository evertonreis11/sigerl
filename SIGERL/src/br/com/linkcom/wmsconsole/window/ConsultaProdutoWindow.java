package br.com.linkcom.wmsconsole.window;

import java.io.IOException;
import java.text.NumberFormat;

import net.wimpi.telnetd.io.terminal.ColorHelper;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.service.DadologisticoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.ProdutotipopaleteService;
import br.com.linkcom.wmsconsole.system.ExecucaoOSWindow;

public class ConsultaProdutoWindow extends ExecucaoOSWindow {
	
	private static final String CODIGO_INVALIDO = "Código inválido.";
	private static final String DIGIGE_0_PARA_SAIR = "Digige 0 para retornar.";
	private static final String TITULO = "Consulta de produto";

	@Override
	public void draw() throws IOException {
		do {
			drawEsqueleto(DIGIGE_0_PARA_SAIR);
			writeOnCenter(TITULO, null, false, false);
			writeSeparator();
			
			String barcode = readCodigoProduto();
			
			if ("0".equals(barcode))
				break;
			
			Produto produto = ProdutoService.getInstance().findProdutoByBarcode(barcode, this.deposito, true);
			if (produto == null){
				writeOnCenter(CODIGO_INVALIDO, ColorHelper.RED, true, true);
				continue;
			}
			
			exibirDados(produto);
			
		} while (true);
	}

	/**
	 * Exibe os dados do produto localizado.
	 * 
	 * @param produto
	 * @throws IOException
	 */
	private void exibirDados(Produto produto) throws IOException {
		drawEsqueleto("");
		writeOnCenter(TITULO, null, false, false);
		writeSeparator();
		
		writeLine("Código: " + produto.getCodigo());
		
		if (produto.getProdutoprincipal() != null){
			writeLine("Descrição: " + produto.getProdutoprincipal().getDescricao());			
			writeLine("Volume: " + produto.getDescricao());			
		}else{
			writeLine("Descrição: " + produto.getDescricao());			
		}
		writeSeparator();
		
		writeLine("Altura: " + (produto.getAltura() != null ? produto.getAltura() : "Não definida."));
		writeLine("Largura: " + (produto.getLargura() != null ? produto.getLargura() : "Não definida."));
		writeLine("Profundidade: " + (produto.getProfundidade() != null ? produto.getProfundidade() : "Não definida."));
		writeLine("Peso: " + (produto.getPeso() != null ? NumberFormat.getNumberInstance().format(produto.getPeso()) : "Não definido."));
		writeSeparator();

		Dadologistico dadologistico = null;
		if (produto.getProdutoprincipal() == null)
			dadologistico = DadologisticoService.getInstance().findByProduto(produto, deposito);
		else{
			dadologistico = DadologisticoService.getInstance().findByProduto(produto.getProdutoprincipal(), deposito);
			if (dadologistico.getNormavolume())
				dadologistico = DadologisticoService.getInstance().findByProduto(produto, deposito);
		}
		
		if (dadologistico != null){
			//passo como parâmetro dadologistico.getProduto() para pegar o correto principal/volume
			Produtotipopalete produtotipopalete = ProdutotipopaleteService.getInstance().findPaletePadrao(dadologistico.getProduto(), deposito);
			if(produtotipopalete != null){
				String norma = "";
				norma = produtotipopalete.getLastro() + " X " + produtotipopalete.getCamada();
				writeLine("Norma: " + norma);
			}else{
				writeLine("Nenhum palete cadastrado.", ColorHelper.RED, false);
			}
			
			writeLine("Linha: " + dadologistico.getLinhaseparacao().getNome());
			writeSeparator();
			writeLine("Picking: " + EnderecoService.formatarEndereco(dadologistico.getEndereco()));
			writeLine("Capacidade: " + (dadologistico.getCapacidadepicking() != null ? dadologistico.getCapacidadepicking() : ""));
		} else {
			writeLine("Não há dados logísticos cadastrados", ColorHelper.RED, false);
		}
		
		readEnter();
	}

	@Override
	public void executarOrdem(Ordemservico ordemservico) throws IOException {
		
	}

	@Override
	public String getTitulo() {
		return TITULO;
	}

}
