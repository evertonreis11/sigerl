package br.com.ricardoeletro.coletor.geral.as;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.service.DadologisticoService;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.geral.service.ProdutoService;
import br.com.linkcom.wms.geral.service.ProdutotipopaleteService;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.ricardoeletro.coletor.geral.filtro.ColetorFiltro;
import br.com.ricardoeletro.coletor.modulo.produto.process.filtro.ConsultaProdutoFiltro;

public class ConsultarProdutoAS extends ColetorAS{
	
	public Map<String, String> consultaProduto(ConsultaProdutoFiltro filtro){
		
		Map<String, String> retorno = new HashMap<String, String>();
		
		StringBuilder mensagem = null;
		
		Produto produto =  ((ProdutoService)getService(ProdutoService.class)).findProdutoByBarcode(filtro.getValorInicial(),WmsUtil.getDeposito(), true);
		
		if (produto == null){
			
			mensagem = new StringBuilder().append("C�digo inv�lido.");
			retorno.put("mensagem", mensagem.toString());
			return retorno;
		}
		
		String dadosProduto = exibirDados(produto);
		
		retorno.put("dados", dadosProduto);
		
		return retorno;
			
	}

	/**
	 * Exibe os dados do produto localizado.
	 * 
	 * @param produto
	 * @throws IOException
	 */
	private String exibirDados(Produto produto) {
		StringBuilder sb = new StringBuilder();
		Deposito deposito = WmsUtil.getDeposito();
		
		writeLine(sb, "C�digo: " + produto.getCodigo());
		
		if (produto.getProdutoprincipal() != null){
			writeLine(sb, "Descri��o: " + produto.getProdutoprincipal().getDescricao());			
			writeLine(sb, "Volume: " + produto.getDescricao());			
		}else{
			writeLine(sb, "Descri��o: " + produto.getDescricao());			
		}
		writeSeparator(sb);
		
		writeLine(sb, "Altura: " + (produto.getAltura() != null ? produto.getAltura() : "N�o definida."));
		writeLine(sb, "Largura: " + (produto.getLargura() != null ? produto.getLargura() : "N�o definida."));
		writeLine(sb, "Profundidade: " + (produto.getProfundidade() != null ? produto.getProfundidade() : "N�o definida."));
		writeLine(sb, "Peso: " + (produto.getPeso() != null ? NumberFormat.getNumberInstance().format(produto.getPeso()) : "N�o definido."));
		writeSeparator(sb);

		Dadologistico dadologistico = null;
		if (produto.getProdutoprincipal() == null)
			dadologistico = ((DadologisticoService)getService(DadologisticoService.class)).findByProduto(produto, deposito);
		else{
			dadologistico = ((DadologisticoService)getService(DadologisticoService.class)).findByProduto(produto.getProdutoprincipal(), deposito);
			if (dadologistico.getNormavolume())
				dadologistico = ((DadologisticoService)getService(DadologisticoService.class)).findByProduto(produto, deposito);
		}
		
		if (dadologistico != null){
			//passo como par�metro dadologistico.getProduto() para pegar o correto principal/volume
			Produtotipopalete produtotipopalete = ((ProdutotipopaleteService)getService(ProdutotipopaleteService.class)).findPaletePadrao(dadologistico.getProduto(), deposito);
			if(produtotipopalete != null){
				String norma = "";
				norma = produtotipopalete.getLastro() + " X " + produtotipopalete.getCamada();
				writeLine(sb, "Norma: " + norma);
			}else{
				writeLine(sb, "Nenhum palete cadastrado.");
			}
			
			writeLine(sb, "Linha: " + dadologistico.getLinhaseparacao().getNome());
			writeSeparator(sb);
			writeLine(sb, "Picking: " + EnderecoService.formatarEndereco(dadologistico.getEndereco()));
			writeLine(sb, "Capacidade: " + (dadologistico.getCapacidadepicking() != null ? dadologistico.getCapacidadepicking() : ""));
		} else {
			writeLine(sb, "N�o h� dados log�sticos cadastrados");
		}
		
		return sb.toString();
	}
}
