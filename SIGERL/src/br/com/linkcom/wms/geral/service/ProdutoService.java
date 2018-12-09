package br.com.linkcom.wms.geral.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.BindException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.GenericBean;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Municipio;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Pessoaendereco;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.ProdutoReport;
import br.com.linkcom.wms.geral.bean.Produtocodigobarras;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Produtotipoestrutura;
import br.com.linkcom.wms.geral.bean.Produtotipopalete;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Tipoenderecamento;
import br.com.linkcom.wms.geral.bean.Tipoestrutura;
import br.com.linkcom.wms.geral.bean.vo.ClassificacaoABC;
import br.com.linkcom.wms.geral.bean.vo.ProdutosaldoVO;
import br.com.linkcom.wms.geral.bean.vo.SaldoProdutoVO;
import br.com.linkcom.wms.geral.dao.ProdutoDAO;
import br.com.linkcom.wms.geral.filter.IntervaloEndereco;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EtiquetaprodutoseparacaoFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.AjustarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ClassificacaoABCFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.RelatorioestoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.RelatoriomovimentacaoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.DadosFaltantesFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EtiquetaprodutoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.expedicao.EtiquetasProdutoSeparacaoVO;
import br.com.linkcom.wms.util.logistica.EnderecoAux;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.linkcom.wms.util.recebimento.EtiquetasVO;

public class ProdutoService extends GenericService<Produto> {
	
	private ProdutoDAO produtoDAO;
	private EnderecoService enderecoService;
	private ProdutotipopaleteService produtotipopaleteService;
	private EtiquetaexpedicaoService etiquetaexpedicaoService;
	private DadologisticoService dadologisticoService;
	private ProdutotipoestruturaService produtotipoestruturaService;
	private ProdutoembalagemService produtoembalagemService;
	private ClienteService clienteService;
	private ProdutocodigobarrasService produtocodigobarrasService;
	public static Map<Integer, GenericBean> mapa = new ConcurrentHashMap<Integer, GenericBean>();
	public static List<Recebimento> listaConferenciaRecebimento = new ArrayList<Recebimento>();
	
	public void setProdutoDAO(ProdutoDAO produtoDAO) {
		this.produtoDAO = produtoDAO;
	}	
	
	public void setProdutocodigobarrasService(ProdutocodigobarrasService produtocodigobarrasService) {
		this.produtocodigobarrasService = produtocodigobarrasService;
	}

	public void setEnderecoService(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}
	
	public void setProdutotipopaleteService(ProdutotipopaleteService produtotipopaleteService) {
		this.produtotipopaleteService = produtotipopaleteService;
	}
	
	public void setEtiquetaexpedicaoService(EtiquetaexpedicaoService etiquetaexpedicaoService) {
		this.etiquetaexpedicaoService = etiquetaexpedicaoService;
	}
	
	public void setDadologisticoService(DadologisticoService dadologisticoService) {
		this.dadologisticoService = dadologisticoService;
	}
	
	public void setProdutotipoestruturaService(ProdutotipoestruturaService produtotipoestruturaService) {
		this.produtotipoestruturaService = produtotipoestruturaService;
	}
	
	public void setProdutoembalagemService(ProdutoembalagemService produtoembalagemService) {
		this.produtoembalagemService = produtoembalagemService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @param recebimento
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findByRecebimento(Recebimento)
	 * @return
	 * @author Arantes
	 */
	public List<Produto> findByRecebimento(Recebimento recebimento) {
		return produtoDAO.findByRecebimento(recebimento);
	}	

	/**
	 * Busca todos os produtos que possuem dados log�sticos em falta.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findProdutosDadosFaltantes(DadosFaltantesFiltro filtro)
	 * @param filtro - � opcional pois o sistema pode pegar todos os produtos com dados faltantes.
	 * @return
	 * @author Pedro Gon�alves
	 */
	public List<Produto> findProdutosDadosFaltantes(Recebimento recebimento) {
		return produtoDAO.findProdutosDadosFaltantes(recebimento);
	}

	/**
	 * Busca todos os produtos que possuem dados log�sticos em falta.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findProdutosDadosFaltantesNovo(DadosFaltantesFiltro filtro)
	 * @param filtro - � opcional pois o sistema pode pegar todos os produtos com dados faltantes.
	 * @return
	 */
	public List<Produto> findProdutosDadosFaltantesNovo(Recebimento recebimento) {
		return produtoDAO.findProdutosDadosFaltantesNovo(recebimento);
	}

	/**
	 * Busca todos os volumes do produto que possuem dados log�sticos em falta.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findProdutosDadosFaltantesVolumeNovo(DadosFaltantesFiltro filtro)
	 * @param filtro - 
	 * @return
	 */
	public List<Produto> findProdutosDadosFaltantesVolumeNovo(Produto produto) {
		return produtoDAO.findProdutosDadosFaltantesVolumeNovo(produto);
	}
	
	/**
	 * Filtra os produtos que est�o com dados log�sticos em falta.
	 * 
	 * @param recebimento - pode ser nulo
	 * @return
	 * @author Pedro Gon�alves
	 */
	public boolean filterListaProdutosFaltantes(Recebimento recebimento){
		List<Produto> findProdutosDadosFaltantes = findProdutosDadosFaltantesNovo(recebimento);
		return possuiProdutosDadosFaltantes(findProdutosDadosFaltantes, recebimento.getTipoenderecamento());
	}
	
	/**
	 * Gera um relat�rio de dados faltantes. Lista produtos que possuem os campos log�sticos
	 * n�o preenchidos.
	 * 
	 * @see #findProdutosDadosFaltantes(DadosFaltantesFiltro)
	 * 
	 * @author Pedro Gon�alves
	 * @param filtro
	 * @return
	 */
	public IReport createDadosFaltantes(DadosFaltantesFiltro filtro) {
		Report report = new Report("RelatorioDadosFaltantes");
		
		List<Produto> listaProdutos = findProdutosDadosFaltantesNovo(filtro.getRecebimento());

		if((listaProdutos == null) || (listaProdutos.isEmpty())) {
			throw new WmsException("N�o foi encontrado nenhum produto com dados log�sticos faltantes.");
		}

		List<ProdutoReport> listaProdutoReport = new ArrayList<ProdutoReport>();

		for (Produto produto : listaProdutos) {

			if(produto.getListaDadoLogistico() != null && !produto.getListaDadoLogistico().isEmpty()){
				Dadologistico dadologistico = produto.getListaDadoLogistico().get(0);
				if(dadologistico.getNormavolume()){
					List<Produto> listaVolumes = findProdutosDadosFaltantesVolumeNovo(produto);
					for (Produto volume : listaVolumes) 
						validaProdutoDadosFaltantesReport(volume, listaProdutoReport, produto);
				}else
					validaProdutoDadosFaltantesReport(produto, listaProdutoReport, null);
			}else{
				validaProdutoDadosFaltantesReport(produto, listaProdutoReport, produto);
			}
		}
		
		Recebimento recebimento = filtro.getRecebimento();
		
		report.addParameter("RECEBIMENTO", recebimento == null ? null : recebimento.getCdrecebimento().toString() );
		report.addSubReport("SUBRELATORIO", new Report("SubRelatorioDadosFaltantes"));
		report.setDataSource(listaProdutoReport);
		report.addParameter("RODAPE", "Impresso em " + NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())) + " por " + WmsUtil.getUsuarioLogado().getNome());
		return report;
	}

	/**
	 * 
	 * Valida dados faltantes do produto
	 * 
	 * @param produtoReport
	 * @param produto
	 * @param listaProdutoReport
	 * @produtoPrincipal Produto Principal passado somente quando o <code>produto</code> 'e um volume
	 * @author Tom�s Rabelo
	 */
	private void validaProdutoDadosFaltantesReport(Produto produto, List<ProdutoReport> listaProdutoReport, Produto produtoPrincipal) {
		ProdutoReport produtoReport = new ProdutoReport();
		
		List<Produto> listaVolumes = findProdutosDadosFaltantesVolumeNovo(produto);

		if (listaVolumes != null && !listaVolumes.isEmpty()){
			for (Produto volume : listaVolumes){
				if(volume.getAltura() == null) {
					produtoReport.addField(volume.getDescricao() + " Altura");
				}
				
				if(volume.getLargura() == null) {
					produtoReport.addField(volume.getDescricao() + " Largura");
				}
				
				if(volume.getProfundidade() == null) {
					produtoReport.addField(volume.getDescricao() + " Profundidade");
				}
				
				if(volume.getPeso() == null) {
					produtoReport.addField(volume.getDescricao() + " Peso");
				}
				
				if(volume.getListaProdutoEmbalagem() == null || volume.getListaProdutoEmbalagem().isEmpty())
					produtoReport.addField(volume.getDescricao() + " Embalagens do produto");
				
				if(volume.getListaProdutoCodigoDeBarras() == null || volume.getListaProdutoCodigoDeBarras().isEmpty())
					produtoReport.addField(volume.getDescricao() + " C�digo de barras");
			}
		}else{
			if(produto.getAltura() == null) {
				produtoReport.addField("Altura");
			}
			
			if(produto.getLargura() == null) {
				produtoReport.addField("Largura");
			}
			
			if(produto.getProfundidade() == null) {
				produtoReport.addField("Profundidade");
			}
			
			if(produto.getPeso() == null) {
				produtoReport.addField("Peso");
			}
			
			if(produto.getListaProdutoEmbalagem() == null || produto.getListaProdutoEmbalagem().isEmpty())
				produtoReport.addField("Embalagens do produto");
			
			if(produto.getListaProdutoCodigoDeBarras() == null || produto.getListaProdutoCodigoDeBarras().isEmpty())
				produtoReport.addField("C�digo de barras");
		}
		
		if(produto.getListaDadoLogistico() != null && produto.getListaDadoLogistico().size() > 0){
			Dadologistico dadologistico = produto.getListaDadoLogistico().get(0);
			if(dadologistico == null){
				produtoReport.addField("Armazenagem");
			}else{
				if(dadologistico.getLinhaseparacao() == null || dadologistico.getLinhaseparacao().getCdlinhaseparacao() == null){
					produtoReport.addField("Linha separa��o");
				}
				if(dadologistico.getTipoendereco() == null || dadologistico.getTipoendereco().getCdtipoendereco() == null){
					produtoReport.addField("Tipo de endere�o de pulm�o");
				}
			}
		}
		
		if(produto.getListaProdutoTipoPalete() == null || produto.getListaProdutoTipoPalete().isEmpty()){
			produtoReport.addField("Normas de paletiza��o");
		}else{
			boolean achouPadrao = false;
			for (Produtotipopalete produtotipopalete : produto.getListaProdutoTipoPalete()) 
				if(produtotipopalete.getPadrao() != null && produtotipopalete.getPadrao())
					achouPadrao = true;
			
			if(!achouPadrao)
				produtoReport.addField("Normas de paletiza��o");
		}
			
		if(produto.getListaProdutoTipoEstrutura() == null || produto.getListaProdutoTipoEstrutura().isEmpty())
			produtoReport.addField("Estrutura");
		else{
			Produtotipoestrutura produtotipoestrutura = produto.getListaProdutoTipoEstrutura().get(0);
			if(produtotipoestrutura.getTipoestrutura().equals(Tipoestrutura.PORTA_PALETE) &&
					ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.PORTAPALETE_EXIGE_PICKING, WmsUtil.getDeposito())){
				if(produto.getListaDadoLogistico() != null && produto.getListaDadoLogistico().size() > 0){
					Dadologistico dadologistico = produto.getListaDadoLogistico().get(0);
					
					if(dadologistico.getArea() == null || dadologistico.getArea().getCdarea() == null)
						produtoReport.addField("�rea de armazenagem");
					if(dadologistico.getEndereco() == null || dadologistico.getEndereco().getCdendereco() == null)
						produtoReport.addField("Endere�o de picking");
					if(dadologistico.getCapacidadepicking() == null)
						produtoReport.addField("Capacidade de picking");
					if(dadologistico.getPontoreposicao() == null)
						produtoReport.addField("Ponto de reposi��o");
					if(dadologistico.getTiporeposicao() == null || dadologistico.getTiporeposicao().getCdtiporeposicao() == null)
						produtoReport.addField("Tipo de reposi��o");
				}
			}
		}
		
		if(produtoReport.getCampos().size() > 0) {
			produtoReport.setCodigo(produto.getCodigo());
			if(produtoPrincipal!=null)
				produtoReport.setDescricao(produtoPrincipal.getDescricao() + " - " + produto.getDescricao());
			else produtoReport.setDescricao(produto.getDescricao());
			listaProdutoReport.add(produtoReport);
		}			
		
	}

	/**
	 * Valida se o produto ou seu volume possui dados faltantes
	 * 
	 * @param produto
	 * @return
	 * @author Tom�s Rabelo
	 * @param tipoenderecamento 
	 */
	private boolean validaProdutoDadosFaltantes(Produto produto, Tipoenderecamento tipoenderecamento) {
		//Se n�o possuir embalagem, dado logistico ja � desconsiderado
		if(produto.getListaProdutoEmbalagem() == null || produto.getListaProdutoEmbalagem().size() == 0 || 
				produto.getListaDadoLogistico() == null || produto.getListaDadoLogistico().size() == 0 )
			return true;
		
		Dadologistico dadologistico = produto.getListaDadoLogistico().get(0);

		if (Tipoenderecamento.MANUAL.equals(tipoenderecamento)){
			//01/10/2009: se for endere�amento manual exige apenas tipo de palete e linha de separa��o
			return produto.getListaProdutoTipoPalete() == null || produto.getListaProdutoTipoPalete().size() == 0 || 
					dadologistico.getLinhaseparacao() == null || dadologistico.getLinhaseparacao().getCdlinhaseparacao() == null;
		}else{
			//Se n�o possui tipo de estrutura n�o � v�lido.
			if (produto.getListaProdutoTipoEstrutura() == null || produto.getListaProdutoTipoEstrutura().size() == 0 )
				return true;

			//Se a primeira estrutura for palete, verificar se possui endere�o de picking
			Produtotipoestrutura produtotipoestrutura = produto.getListaProdutoTipoEstrutura().get(0);
			if(produtotipoestrutura.getTipoestrutura().equals(Tipoestrutura.PORTA_PALETE) &&
					ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.PORTAPALETE_EXIGE_PICKING, WmsUtil.getDeposito())){
				if(dadologistico.getEndereco() == null || dadologistico.getEndereco().getCdendereco() == null || 
				   dadologistico.getTiporeposicao() == null || dadologistico.getTiporeposicao().getCdtiporeposicao() == null ||
				   dadologistico.getCapacidadepicking() == null || dadologistico.getPontoreposicao() == null || 
				   dadologistico.getArea() == null || dadologistico.getArea().getCdarea() == null)
					return true;
			}
			//Verifica se possui todos os campos de dado logistico
			if(dadologistico.getLinhaseparacao() == null || dadologistico.getLinhaseparacao().getCdlinhaseparacao() == null || 
			   dadologistico.getTipoendereco() == null || dadologistico.getTipoendereco().getCdtipoendereco() == null)
				return true;
			
			boolean achouPadrao = false;
			for (Produtotipopalete produtotipopalete : produto.getListaProdutoTipoPalete()) {
				if(produtotipopalete.getPadrao() != null && produtotipopalete.getPadrao())
					achouPadrao = true;
			}
				
			if(!achouPadrao)
				return true;
		}
		
		return false;
	}

	@Override
	public void saveOrUpdate(final Produto bean) {
		TransactionTemplate transactionTemplate = Neo.getObject(TransactionTemplate.class);
		transactionTemplate.execute(new TransactionCallback(){

			public Object doInTransaction(TransactionStatus status) {
				if (bean.getCdproduto() != null){
					produtoembalagemService.retirarEmbalagemCompra(bean);
					produtoembalagemService.removeDescricoes(bean);
				}

				Deposito deposito = WmsUtil.getDeposito();
				prepareToSave(bean, deposito);
				if(bean.getListaVolumes() != null)
					bean.setQtdevolumes(bean.getListaVolumes().size());
						
				if (bean.getListaProdutoCodigoDeBarras() != null){
					for (Produtocodigobarras codigobarras : bean.getListaProdutoCodigoDeBarras()){
						Pattern pattern = Pattern.compile("\\D");
						Matcher matcher = pattern.matcher(codigobarras.getCodigo().trim());
						if(!matcher.find())
							codigobarras.setValido(WmsUtil.validaCodigoDeBarras(codigobarras.getCodigo()));
						else
							codigobarras.setValido(Boolean.FALSE);
						
						if (codigobarras.getInterno() == null)
							codigobarras.setInterno(false);
					}
				}
				
				saveOrUpdateNoUseTransaction(bean);

				saveListas(bean);
				List<Produto> primeirosVolumes = findVolumes(bean);
				if(primeirosVolumes == null)
					primeirosVolumes = new ArrayList<Produto>();
				
				prepareVolumes(bean, deposito, primeirosVolumes);

				//se o produto n�o tem volumes ele n�o pode ter o flag ligado em nenhum dep�sito
				if(bean.getProdutoprincipal() == null && bean.getListaVolumes() != null && bean.getListaVolumes().isEmpty())
					dadologisticoService.updateNormavolume(bean, false);

				return null;
			}
		});
		
		produtoDAO.atualizarProduto(bean);
	}
	
	/**
	 * Salva as lista do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param bean
	 */
	private void saveListas(Produto produto) {
		
		Dadologistico dado = dadologisticoService.findByProduto(produto,WmsUtil.getDeposito());
		List<Produtotipopalete> listaProdutoTipoPalete = produtotipopaleteService.findByProduto(produto,WmsUtil.getDeposito());
		List<Produtotipoestrutura> listaProdutoTipoEstrutura = produtotipoestruturaService.findByProduto(produto);
		produtotipoestruturaService.updateOrdemByProduto(produto);
		
		for(int i = 0; i < produto.getListaDadoLogistico().size(); i++){
			Dadologistico dadologistico = produto.getListaDadoLogistico().get(i);
			if(!dado.equals(dadologistico))
				dadologisticoService.delete(dado);
			dadologisticoService.saveOrUpdateNoUseTransaction(dadologistico);
		}
		
		for(int i = 0; i < produto.getListaProdutoTipoEstrutura().size(); i++){
			Produtotipoestrutura produtotipoestrutura = produto.getListaProdutoTipoEstrutura().get(i);
			if(listaProdutoTipoEstrutura.contains(produtotipoestrutura))
				listaProdutoTipoEstrutura.remove(produtotipoestrutura);
			produtotipoestruturaService.saveOrUpdateNoUseTransaction(produtotipoestrutura);
		}
		
		for(int i = 0; i < produto.getListaProdutoTipoPalete().size(); i++){
			Produtotipopalete produtotipopalete = produto.getListaProdutoTipoPalete().get(i);
			if(listaProdutoTipoPalete.contains(produtotipopalete))
				listaProdutoTipoPalete.remove(produtotipopalete);
			produtotipopaleteService.saveOrUpdateNoUseTransaction(produtotipopalete);
		}
		
		for (Produtotipopalete produtotipopalete : listaProdutoTipoPalete) {
			produtotipopaleteService.delete(produtotipopalete);
		}
		
		for (Produtotipoestrutura produtotipoestrutura : listaProdutoTipoEstrutura) {
			produtotipoestruturaService.delete(produtotipoestrutura);
		}
		
	}
	
	/**
	 * Prepara os volumes do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see #prepareCodigoBarras(Produto, Produto, List)
	 * @see #containsVolume(List, Produto)
	 * 
	 * @param bean
	 * @param deposito
	 * @param primeirosVolumes
	 */
	private void prepareVolumes(final Produto bean, final Deposito deposito, final List<Produto> primeirosVolumes) {
		for (Produto volume : bean.getListaVolumes()) {
			List<Produtocodigobarras> listaProdutoCodigoBarras = new ArrayList<Produtocodigobarras>();

			prepareCodigoBarras(bean, volume, listaProdutoCodigoBarras);

			volume.setProdutoclasse(bean.getProdutoclasse());
			volume.setListaProdutoCodigoDeBarras(listaProdutoCodigoBarras);
			volume.setProdutoprincipal(bean);

			if (primeirosVolumes.contains(volume)) {
				updateDadosVolume(volume);
				primeirosVolumes.remove(volume);
			} else {
				/*
				 * Isso � feito para que n�o haja erro de index(campo c�digo)
				 * quando um volume for salvo.
				 */
				validateVolume(primeirosVolumes, volume);

				prepareToSave(volume, deposito);
				saveOrUpdateNoUseTransaction(volume);

				gerarDadosLogisticosOutrosDepositos(volume, deposito);
			}
		}

		for (Produto prod : primeirosVolumes) {
			OrdemprodutohistoricoService.getInstance().removerEmbalagem(prod);
			ProdutoService.super.delete(prod);
		}

	}

	/**
	 * Gera os dados log�sticos para o volume para os outros dep�sitos diferentes do atual.
	 * 
	 * @author Giovane Freitas
	 * @param volume O volume para o qual deve ser gerado os dados log�sticos.
	 * @param deposito O dep�sito atual onde est� sendo feita a edi��o do produto.
	 */
	protected void gerarDadosLogisticosOutrosDepositos(Produto volume, Deposito deposito) {

		produtoDAO.gerarDadosLogisticosOutrosDepositos(volume, deposito);
	}

	/**
	 * Prepara os c�digos de barras do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param bean
	 * @param volume
	 * @param listaProdutoCodigoBarras
	 */
	private void prepareCodigoBarras(Produto bean, Produto volume, List<Produtocodigobarras> listaProdutoCodigoBarras) {
		for(Produtocodigobarras produtocodigobarras : bean.getListaProdutoCodigoDeBarras()){
			Produtocodigobarras prodBarras = new Produtocodigobarras();
			prodBarras.setCodigo(produtocodigobarras.getCodigo() + volume.getComplementocodigobarras());
			prodBarras.setProduto(volume);
			prodBarras.setValido(produtocodigobarras.getValido());
			prodBarras.setInterno(produtocodigobarras.getInterno());
			listaProdutoCodigoBarras.add(prodBarras);
		}
	}
	
	/**
	 * Verifica se a lista possui o volume atrav�s do c�digo
	 * e o remove da lista e do banco de dados
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param primeirosVolumes
	 * @param volume
	 * @return
	 */
	private void validateVolume(List<Produto> primeirosVolumes,Produto volume) {
		if(primeirosVolumes == null || volume == null || volume.getCodigo() == null)
				throw new WmsException("Dados inv�lidos para executar a fun��o containsVolume");
		
		int i = 0;
		while (i < primeirosVolumes.size()) {
			Produto produto = primeirosVolumes.get(i);
			if(produto.getCodigo() != null && produto.getCodigo().equals(volume.getCodigo())){
				super.delete(produto);
				primeirosVolumes.remove(i);
			}else
				i++;
		}
		
	}

	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 *
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#updateDadosVolume(Produto volume)
	 * 
	 * @param volume
	 */
	private void updateDadosVolume(Produto volume) {
		produtoDAO.updateDadosVolume(volume);
		
	}

	/**
	 * Prepara o bean para ser salvo
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoService.findByEnderecoPicking(String enderecoPicking, Integer cdarea)
	 * 
	 * @param bean
	 * @param deposito
	 */
	private void prepareToSave(Produto bean, Deposito deposito) {
		for(Dadologistico dadologistico : bean.getListaDadoLogistico()){
			dadologistico.setProduto(bean);
			if(dadologistico.getEnderecoDePicking() != null && !"".equals(dadologistico.getEnderecoDePicking())){
				Endereco findByEnderecoPicking = enderecoService.findByEnderecoPicking(dadologistico.getEnderecoDePicking(),dadologistico.getArea().getCdarea());
				if(findByEnderecoPicking == null || findByEnderecoPicking.getCdendereco() == null)
					throw new WmsException("A �rea de armazenagem selecionada n�o possui o endere�o informado");
				dadologistico.setEndereco(findByEnderecoPicking);
			}
			dadologistico.setDeposito(deposito);
			if(dadologistico.getNormavolume() == null)
					dadologistico.setNormavolume(false);
			
			if(bean.getProdutoprincipal() != null){
				if (bean.getProdutoprincipal().getListaDadoLogistico() != null && 
						!bean.getProdutoprincipal().getListaDadoLogistico().isEmpty() && 
						Boolean.TRUE.equals(bean.getProdutoprincipal().getListaDadoLogistico().iterator().next().getGeracodigo())){
					
					dadologistico.setGeracodigo(true);
				}else	
					dadologistico.setGeracodigo(false);
			}else if (dadologistico.getGeracodigo() == null)
				dadologistico.setGeracodigo(false);				
		}
		
		/*
		 * Como o exclus�o da embalagem esta como set null, ao excluir uma emabalagem associada a um c�digo de barras
		 * a referencia deve ser retirada do bean de c�digo de barras para que n�o aconte�a um erro.
		 */		
		for(Produtocodigobarras produtocodigobarras : bean.getListaProdutoCodigoDeBarras()){
			if(!bean.getListaProdutoEmbalagem().contains(produtocodigobarras.getProdutoembalagem()))
				produtocodigobarras.setProdutoembalagem(null);
		}
	
		//Garantindo que o produto/volume sempre ter� uma embalagem de compra
		if (bean.getListaProdutoEmbalagem().size() == 0){
			String descricao = null;
			Long qtde = null;
			if (bean.getProdutoprincipal() != null && bean.getProdutoprincipal().getListaProdutoEmbalagem().size() > 0){
				for (Produtoembalagem embalagemCompraPrincipal : bean.getProdutoprincipal().getListaProdutoEmbalagem()){
					if (embalagemCompraPrincipal.getQtde() == 1)
						descricao = embalagemCompraPrincipal.getDescricao();
						qtde = embalagemCompraPrincipal.getQtde();
					break;
				}
			}
			
			if (descricao == null || descricao.trim().isEmpty())
				descricao = "UN";
			
			Produtoembalagem produtoembalagem = new Produtoembalagem();
			produtoembalagem.setCompra(true);
			produtoembalagem.setDescricao(descricao);
			produtoembalagem.setFator(1.0);
			produtoembalagem.setOrigemerp(false);
			produtoembalagem.setProduto(bean);
			produtoembalagem.setQtde(qtde!=null?qtde:1L);	

			bean.getListaProdutoEmbalagem().add(produtoembalagem);
		}
		
		for(Produtoembalagem produtoembalagem : bean.getListaProdutoEmbalagem()){
			produtoembalagem.setProduto(bean);
			if(produtoembalagem.getOrigemerp() == null)
				produtoembalagem.setOrigemerp(Boolean.FALSE);
		}
			
		for(int i = 0; i < bean.getListaProdutoTipoEstrutura().size(); i++){
			Produtotipoestrutura produtotipoestrutura = bean.getListaProdutoTipoEstrutura().get(i);
			produtotipoestrutura.setOrdem(i);
			produtotipoestrutura.setProduto(bean);
			produtotipoestrutura.setDeposito(deposito);
		}
		
		for(Produtotipopalete produtotipopalete : bean.getListaProdutoTipoPalete()){
			produtotipopalete.setDeposito(deposito);
			produtotipopalete.setProduto(bean);
		}
	}
	
	/**
	 * Seta a norma de paletiza��o e o peso de uma lista de Produtotipopalete
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 */
	public void setNormaAndPeso(Produto produto){
		if(produto == null)
			throw new WmsException("O produto n�o deve ser nulo.");
		
		Double peso = produto.getPesounitario() == null ? 0.0 : produto.getPesounitario();
		
		if(produto != null && produto.getListaProdutoTipoPalete() != null)
			for(Produtotipopalete produtotipopalete : produto.getListaProdutoTipoPalete()){
				Long lastro = produtotipopalete.getLastro() == null ? 0l : produtotipopalete.getLastro();
				Long camada = produtotipopalete.getCamada() == null ? 0l : produtotipopalete.getCamada();
				produtotipopalete.setNormaPaletizacao(lastro * camada);
				produtotipopalete.setPesoDoPalete(produtotipopalete.getNormaPaletizacao() * peso);
			}
	}
	

	
	/**
	 * Seta o endereco e o tipo de endereco do dado log�stico
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param dadologistico
	 */
	public void setEnderecoAndTipo(Dadologistico dadologistico) {
		if(dadologistico == null)
			throw new WmsException("O dado logistico n�o deve ser nulo.");
		
		Endereco endereco = dadologistico.getEndereco();
		if(endereco != null){
			dadologistico.setEnderecoDePicking(endereco.getEndereco());
			if(endereco.getTipoendereco() != null)
				dadologistico.setTipoEnderecoPicking(endereco.getTipoendereco().getNome());
		}		
	}
	
	/**
	 * Seta os tipos dos c�digos de barras do produto
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param listaProdutocodigobarras
	 */
	public void setTipoCB(List<Produtocodigobarras> listaProdutocodigobarras) {
		if(listaProdutocodigobarras != null)
			for (Produtocodigobarras produtocodigobarras : listaProdutocodigobarras) {
				String codigo = produtocodigobarras.getCodigo();
				if(codigo.length() == 14 || codigo.length() == 18)
					produtocodigobarras.setTipo("DUN");
				else if(codigo.length() == 13 || codigo.length() == 17)
					produtocodigobarras.setTipo("EAN");
			}
		
	}
	
	/**
	 * Verifica se existem tipos de paletes repetidos na lista
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param lista
	 * @param produtotipopalete
	 * @return
	 */
	public boolean existsRepetido(List<Produtotipopalete> lista, Produtotipopalete produtotipopalete) {
		if(lista == null || produtotipopalete == null)
			throw new WmsException("Os par�mentros n�o devem ser nulos");
		int count = 0;
		for (Produtotipopalete produtotipopaleteIt : lista) {
			if(produtotipopaleteIt.getTipopalete() != null && produtotipopalete.getTipopalete() != null && produtotipopaleteIt.getTipopalete().getCdtipopalete().equals(produtotipopalete.getTipopalete().getCdtipopalete())){
				count++;
				if(count == 2){
					return true;
				}
			}
			
		}
		return false;
	}
	/**
	 * Verifica se existem estruturas repetidas na lista
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param lista
	 * @param produtotipoestrutura
	 * @return
	 */
	public boolean existsRepetidoEstrutura(List<Produtotipoestrutura> lista, Produtotipoestrutura produtotipoestrutura) {
		if(lista == null || produtotipoestrutura == null)
			throw new WmsException("Os par�mentros n�o devem ser nulos");
		
		int count = 0;
		for (Produtotipoestrutura produtotipoestruturaIt : lista) {
			if(produtotipoestruturaIt.getTipoestrutura().getCdtipoestrutura().equals(produtotipoestrutura.getTipoestrutura().getCdtipoestrutura())){
				count++;
				if(count == 2){
					return true;
				}
			}
			
		}
		return false;
	}
	
	/**
	 * Verifica se existem quantidades repetidas na lista
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param lista
	 * @param produtotipoestrutura
	 * @return
	 */
	public boolean existsRepetidoUnidades(List<Produtoembalagem> lista,Produtoembalagem produtoembalagem) {
		if(lista == null || produtoembalagem == null)
			throw new WmsException("Os par�mentros n�o devem ser nulos");
		
		int count = 0;
		for (Produtoembalagem produtoembalagemIt : lista) {
			if(produtoembalagemIt.getQtde().equals(produtoembalagem.getQtde())){
				count++;
				if(count == 2){
					return true;
				}
			}
			
		}
		return false;
	}
	
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO.findVolumes(Produto form)
	 * 
	 * @param form
	 * @return
	 */
	public List<Produto> findVolumes(Produto form) {
		return produtoDAO.findVolumes(form);
	}
	
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Arantes
	 *  
	 * @param ordemservico
	 * @param ordemtipo
	 * @return List<Produto>
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findByOrdemservico(Ordemservico) 
	 * 
	 */
	public List<Produto> findByOrdemservico(Ordemservico ordemservico, Ordemtipo ordemtipo) {
		return produtoDAO.findByOrdemservico(ordemservico, ordemtipo);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO.
	 * 
	 * Carrega os dados log�sticos do produto. 
	 * 
	 * Encontra a embalagem do produto a partir do valor bipado no coletor.
	 * 
	 * Pega o tipo palete padr�o de acordo com o dep�sito logado no coletor.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findDadosLogisticosProdutoRF(Produto produto, String codigobarra, Deposito deposito)
	 * 
	 * @param produto
	 * @return
	 * @author Pedro Gon�alves
	 */
	public Produto findDadosLogisticosProdutoRF(Produto produto,Deposito deposito){
		return produtoDAO.findDadosLogisticosProdutoRF(produto, deposito);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findPrincipal(Produto produto)
	 * 
	 * @param produto
	 * @return
	 */
	public Produto findPrincipal(Produto produto){
		return produtoDAO.findPrincipal(produto);
	}
	
	/**
	 * Verifica se algum produto selecionado possui volumes.
	 * @author Filipe Santos
	 * @since 15/09/2011
	 * @return Lista de Produtos
	 */
	public List<Produto> verificaVolumeProdutos(List<Produto> listaProduto){
		System.out.println("verificaVolumeProdutos");
		List<Produto> listaProdutosVolume = produtoDAO.findVolumesByCdProduto(listaProduto);
		System.out.println("VerificaVolumeProdutos after findVolumesByCdProduto");
		System.out.println("VerificaVolumeProdutos after findVolumesByCdProduto size " + listaProdutosVolume.size());
		//Captura os indexs da lista principal onde os produtos possuem volumes.		
		Iterator<Produto> iterator = listaProduto.iterator();
		for(Produto produto : listaProdutosVolume){
			while (iterator.hasNext()){
				Produto produto2 = (Produto) iterator.next();
				//Remove todos os produtos com volumes da lista principal				
				if(produto.getProdutoprincipal().getCdproduto().equals(produto2.getCdproduto())){
					iterator.remove();
				}		
			}
		}				

		//Adicionando os volumes na lista Principal
		for(Produto produto : listaProdutosVolume){			
			produto.setListaProdutoCodigoDeBarras(produtocodigobarrasService.findByProdutoInterno(produto));
			listaProduto.add(produto);
		}
		
		return listaProduto;
	}
	
	/**
	 * Verifica se o produto possui codigo de barras interno, se n�o tiver o m�todo gera um codigo para o produto.
	 * @author Filipe Santos
	 * @since 15/09/2011
	 * @return Lista de Produtocodigobarras
	 */
	public List<Produtocodigobarras> verificaCodigoBarrasProduto(Produto produto, List<Produtocodigobarras> listaCodigoBarras){		
		Produtocodigobarras produtocodigobarras = new Produtocodigobarras();		
		String codigoBarraInterno = produto.getCdproduto().toString();			
		for (int j = 0; j < 12-produto.getCdproduto().toString().length(); j++) {
			codigoBarraInterno = "0" + codigoBarraInterno;
		}
		codigoBarraInterno += WmsUtil.calculaDigitoVerificadorCodigoDeBarras(codigoBarraInterno);		
		produtocodigobarras.setCodigo(codigoBarraInterno);
		produtocodigobarras.setInterno(true);
		produtocodigobarras.setProduto(produto);		
		produtocodigobarras.setValido(true);
		produtocodigobarrasService.saveOrUpdate(produtocodigobarras);
		listaCodigoBarras.add(produtocodigobarras);
		
		return listaCodigoBarras;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Cria o relat�rio de emitir etiqueta de produto no recebimento
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see #findForReport(EtiquetaprodutoFiltro)
	 *  
	 * @return
	 */
	public IReport createReportEtiqueta(EtiquetaprodutoFiltro filtro,WebRequestContext request) {
		Integer cdproduto = filtro.getProduto() !=null ? filtro.getProduto().getCdproduto(): null;
		Integer cdrecebimento = filtro.getRecebimento() !=null ? filtro.getRecebimento().getCdrecebimento(): null;
		System.out.println("CDPRODUTO: " + cdproduto); 
		System.out.println("CDRECEBIMENTO: " + cdrecebimento); 
		
		
		Integer contador = new Integer(0);
		GenericBean gb = new GenericBean();
		Report report = new Report("RelatorioEtiquetaProdutoRecebimentoVertical");
		Map<String, Image> mapaCodigoBarras = new HashMap<String, Image>();
		
		if (listaConferenciaRecebimento.contains(filtro.getRecebimento())){
			throw new WmsException("O relat�rio j� est� sendo gerado por outro usu�rio, por favor, tente mais tarde. <a href='javascript:history.back()'>Voltar para tela anterior</a>");
		}
		
		//adicionando o recebimento a lista est�tica que controla os recebimentos que tem etiquetas sendo gerados no momento.
		if(filtro.getRecebimento()!=null)
			listaConferenciaRecebimento.add(filtro.getRecebimento());
		
		List<Produtocodigobarras> listaCodigoBarras = new ArrayList<Produtocodigobarras>();
		List<EtiquetasVO> listaDadosProdutos = new ArrayList<EtiquetasVO>();
		long l = System.currentTimeMillis();
		List<Produto> listaProduto = findForReport(filtro);
		if(filtro.getRecebimento()!=null){
			List<Produto> lp = produtoDAO.findByRecebimento(filtro.getRecebimento());
			for(Produto produto : lp){
				listaProduto.add(produto);
			}
		}else if(listaProduto.size()==0 && filtro.getProduto()!=null){
			listaProduto.add(produtoDAO.findByCdproduto(filtro.getProduto().getCdproduto()));
		}
		System.out.println("ProdutoService.createReportEtiqueta depois if recebimento " + (System.currentTimeMillis()-l) + "ms");
		System.out.println("Lista produto size "+ listaProduto.size());
		if(listaProduto.size()>0)
			verificaVolumeProdutos(listaProduto);
		
		System.out.println("ProdutoService.createReportEtiqueta depois do verificaVolumeProdutos " + (System.currentTimeMillis()-l) + "ms");
		for (Produto produto : listaProduto) {
			int totalEtiquetas = getEtiquetasPorEmbalagem(produto,filtro);
			
			//Verifica se o produto possui codigo de barras valido.
			if(produto.getListaProdutoCodigoDeBarras().size()==0){
				System.out.println("ProdutoService.createReportEtiqueta 997");
				listaCodigoBarras.clear();
				verificaCodigoBarrasProduto(produto, listaCodigoBarras);
				produto.setListaProdutoCodigoDeBarras(listaCodigoBarras);				
			}									
			
			//Se n�o tem OSP associada � porque estou imprimindo apenas a partir do produto, ent�o pegarei
			//a quantidade de etiquetas a partir do filtro
			if (produto.getListaOrdemServicoProduto() == null || produto.getListaOrdemServicoProduto().isEmpty())
				totalEtiquetas = filtro.getNumerocopias();
				
			for(int z = 0; z < totalEtiquetas; z++){
				generateLista(listaDadosProdutos, produto, mapaCodigoBarras);
			}
			if(filtro.getRecebimento()!=null){
				contador++;
				gb.setId((contador / listaProduto.size())*100);	
				gb.setValue(null);
				mapa.put(filtro.getRecebimento().getCdrecebimento(),gb);
			}
		}
		System.out.println("TERMINO PORCENTAGEM!");
		if(filtro.getRecebimento()!=null){
			mapa.remove(filtro.getRecebimento().getCdrecebimento());
			listaConferenciaRecebimento.remove(filtro.getRecebimento());
		}
		System.out.println("ProdutoService.createReportEtiqueta depois d for produtos " + (System.currentTimeMillis()-l) + "ms");
		report.addParameter("mapaCodigoBarras", mapaCodigoBarras);
		report.setDataSource(listaDadosProdutos);
		return report;
	}
	
	/**
	 * Faz a divisao da quantidade do produto pela quantidad da embalagem de recebimen
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param produto
	 * @return
	 */
	private int getEtiquetasPorEmbalagem(Produto produto,EtiquetaprodutoFiltro filtro) {
		int result = 1;
		 List<Ordemservicoproduto> listaOrdemServicoProduto = produto.getListaOrdemServicoProduto();
		
		if(listaOrdemServicoProduto != null && filtro.getRecebimento()!=null){
			if(!listaOrdemServicoProduto.isEmpty()){
				Produtoembalagem produtoembalagem = produtoembalagemService.findCompraByProduto(produto);
			
				if(produtoembalagem != null){
					Ordemservicoproduto ordemservicoproduto = listaOrdemServicoProduto.get(0);
					result = Math.round(ordemservicoproduto.getQtdeesperada() / produtoembalagem.getQtde());
				}
				else{
					Ordemservicoproduto ordemservicoproduto = listaOrdemServicoProduto.get(0);
					result = ordemservicoproduto.getQtdeesperada().intValue();
				}
		    }
		}else{
			listaOrdemServicoProduto = new ArrayList<Ordemservicoproduto>();
			produto.setListaOrdemServicoProduto(listaOrdemServicoProduto);
		}		
		return result;
	}

	/**
	 * Gera os dados da lista de etiquetas
	 * 
	 * @author Leonardo Guimar�es
	 * 		   Giovane Freitas - Criando uma imagem no c�digo de barras na vertical.
	 * 
	 * @param listaDadosProdutos
	 * @param produto
	 */
private void generateLista(List<EtiquetasVO> listaDadosProdutos,Produto produto, Map<String, Image> mapaCodigoBarras) {
		
		for(int i = 0; i < produto.getListaProdutoCodigoDeBarras().size(); i++){
			Produtocodigobarras produtocodigobarras = produto.getListaProdutoCodigoDeBarras().get(i);
			
			EtiquetasVO etiquetasVO = new EtiquetasVO();
			etiquetasVO.setCodProduto(produto.getCodigo());
			if (produto.getProdutoprincipal() != null){
				etiquetasVO.setDescricao(produto.getProdutoprincipal().getDescricao());
				etiquetasVO.setVolume(produto.getDescricao());
			}else{
				etiquetasVO.setDescricao(produto.getDescricao());
			}				
			etiquetasVO.setCodigobarras(produtocodigobarras.getCodigo());
			
			if(!mapaCodigoBarras.containsKey(produto.getCodigo())){
				System.out.println("=======================");
				System.out.println("GERANDO IMAGEM!!!!!!!");
				System.out.println("======================");
				Barcode barcode;
				try {
					barcode = BarcodeFactory.createCode128(etiquetasVO.getCodigobarras());
					barcode.setDrawingText(false);
					barcode.setBarHeight(40);
					BufferedImage img = BarcodeImageHandler.getImage(barcode);
					
					//C�digo para colocar a imagem na vertical
					int width = img.getWidth();
					int height = img.getHeight();
					
					BufferedImage imgVertical = new BufferedImage(height, width, img.getType());
					
					for(int k=0; k<width; k++)
						for(int j=0; j<
						height; j++)
							imgVertical.setRGB(j, k, img.getRGB(k, height-1-j));
					/**
					 * Processo modificado
					 * Como a imagem do c�digo de barras � a mesma para todos os c�digos de produto,
					 * foi gerado um mapa que � passado para o relat�rio ao inv�s de passar a mesma imagem v�rias vezes para
					 * cada etiqueta. Isso reduz sensivelmente o tempo de gera��o e o tamanho do relat�rio para grandes recebimentos.
					 * @author igorsilveriocosta
					 * 
					 */
					
					mapaCodigoBarras.put(produto.getCodigo(), imgVertical);
					//FIM
					//escrevendo a imagem em disco
	//				String pathImagem = "/Users/igorsilveriocosta/Desenvolvimento/WMS/tempcodigobarras/"+z+"_"+i+".png";
	//				File file = new File(pathImagem);
	//				file.createNewFile();
	//				ImageIO.write(imgVertical, "png", file);
	//				etiquetasVO.setPathImagemCodigoBarra(pathImagem);
	//				etiquetasVO.setImagemCodigoBarra(imgVertical);
				} catch (Exception e) {
					System.err.println("Erro ao gerar c�digo de barras");
					e.printStackTrace();
				}
			}	
			listaDadosProdutos.add(etiquetasVO); //02536.0102 02536.0102
		}
		
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findForReport(EtiquetaprodutoFiltro filtro)
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Produto> findForReport(EtiquetaprodutoFiltro filtro){
		return produtoDAO.findForReport(filtro);
	}
	
	/**
	 * Gera as etiquetas de produto separa��o
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see #initializeEtiqueta(Carregamento carregamento, Box box, Pessoaendereco pessoaendereco, Municipio municipio, EtiquetasProdutoSeparacaoVO etiqueta, Pedidovenda pedidovenda, Dadologistico dadologistico)
	 * @see #prepareEtiquetas(List<EtiquetasProdutoSeparacaoVO> listaEtiquetas, long i, Etiquetaexpedicao etiquetaexpedicao, Pedidovendaproduto pedidovendaproduto, Produto produto, Dadologistico dadologistico, EtiquetasProdutoSeparacaoVO etiqueta, List<Produto> listaVolumes) throws CloneNotSupportedException
	 * @see #doTest(List<Etiquetaexpedicao> listaEtiquetaExpedicao, int index, Pedidovenda pedidovenda)
	 * 
	 * @param filtro
	 * @return report
	 * @throws CloneNotSupportedException 
	 */
	public IReport createReportProdutoSeparacao(EtiquetaprodutoseparacaoFiltro filtro) throws CloneNotSupportedException {
		Report report = new Report("RelatorioEtiquetaProdutoSeparacao");

		Map<Integer, Integer> mapCountEtiquetasPorEndereco = new HashMap<Integer, Integer>();
		
		List<EtiquetasProdutoSeparacaoVO> listaEtiquetaExpedicao = etiquetaexpedicaoService.findForReport(filtro);
		for (EtiquetasProdutoSeparacaoVO etiqueta : listaEtiquetaExpedicao) {
			etiqueta.setCodigo(geraCodigoByCdEtiqueta(etiqueta.getCdetiquetaexpedicao()));
		
			if (!mapCountEtiquetasPorEndereco.containsKey(etiqueta.getCdpessoaendereco()))
				mapCountEtiquetasPorEndereco.put(etiqueta.getCdpessoaendereco(), 1);
			else
				mapCountEtiquetasPorEndereco.put(etiqueta.getCdpessoaendereco(), mapCountEtiquetasPorEndereco.get(etiqueta.getCdpessoaendereco()) + 1);
			
			int volume = mapCountEtiquetasPorEndereco.get(etiqueta.getCdpessoaendereco());
			etiqueta.setVolumeEntrega(volume + "/");
			if(volume < 10)
				etiqueta.setVolumeEntrega("0" + etiqueta.getVolumeEntrega());
		}

		for (EtiquetasProdutoSeparacaoVO item : listaEtiquetaExpedicao){
			//atualizando o n�mero total de volumes para cada etiqueta
			StringBuilder totalEntrega = new StringBuilder();
			Integer total = mapCountEtiquetasPorEndereco.get(item.getCdpessoaendereco());
			if (total < 10)
				totalEntrega.append("0");
			totalEntrega.append(total);
			item.setTotalEntrega(totalEntrega);
		}
		
		report.setDataSource(listaEtiquetaExpedicao);
		return report;
	}
	
	/**
	 * Gera o c�digo de barras da etiqueta
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param cdetiquetaexpedicao
	 * @return
	 */
	private String geraCodigoByCdEtiqueta(Integer cdetiquetaexpedicao) {
		if(cdetiquetaexpedicao == null)
			throw new WmsException("O cdetiquetaexpedicao n�o deve ser nulo");
		String builder = cdetiquetaexpedicao.toString();
		for(int i = 0; builder.length() < 13; i++){
			builder = "0" + builder;
		}
		
		return builder;
	}
	
	/**
	 * 
	 * M�todo que valida se o campo endere�o do ajuste de estoque est� preenchido.
	 * 
	 * @author Arantes
	 * 
	 * @param listaAjustarEstoque
	 * @return Boolean
	 * 
	*/
	public String validarProdutosAjax(Set<AjustarEstoqueFiltro> listaAjustarEstoque) {
		StringBuilder erros = new StringBuilder();
		
		for (AjustarEstoqueFiltro ajustarEstoque : listaAjustarEstoque) {		
			
			//So valida se tudo estiver preenchido
			if(ajustarEstoque.getEnderecoproduto() != null && ajustarEstoque.getEnderecoproduto().getProduto() != null && 
			   ajustarEstoque.getEnderecoproduto().getProduto().getCdproduto() != null && ajustarEstoque.getEnderecoproduto().getQtde() != null){
			
				//Endere�o inv�lido, j� foi pego pelo m�todo de validar endere�os
				if (!EnderecoAux.isEnderecoCompleto(ajustarEstoque.getEndereco()) )
					continue;
				
				if((ajustarEstoque.getEnderecoproduto() == null) || (ajustarEstoque.getEnderecoproduto().getProduto() == null) || (ajustarEstoque.getEnderecoproduto().getProduto().getCdproduto() == null)){
					erros.append("O endere�o \"");
					erros.append(EnderecoAux.formatarEndereco(ajustarEstoque.getEndereco()));
					erros.append("\" n�o possui um produto definido.\n");
					continue;
				}
				
				Endereco endereco = enderecoService.findEndereco(ajustarEstoque.getEndereco());
				//Endere�o inv�lido, j� foi pego pelo m�todo de validar endere�os
				if (endereco == null)
					continue;
	
				boolean isPicking = Enderecofuncao.PICKING.equals(endereco.getEnderecofuncao());
	
				//guardar NULL ou o produto associado ao endere�o de picking
				Produto produto = endereco.getListaDadologistico().isEmpty() ? null  : endereco.getListaDadologistico().iterator().next().getProduto();
			
				//Se for um endere�o de picking e o usu�rio est� tentando gravar um produto 
				//diferente do associado, ent�o n�o deixarei
				if (isPicking && !ajustarEstoque.getEnderecoproduto().getProduto().equals(produto)){
					Produto produtoAux = ProdutoService.getInstance().load(ajustarEstoque.getEnderecoproduto().getProduto());
					erros.append("O produto \"");
					erros.append(produtoAux.getDescricao());
					erros.append("\" n�o pertence ao endere�o de picking \"");
					erros.append(endereco.getEndereco());
					erros.append("\".\n");
					continue;
				}
				
				Dadologistico dadologistico = DadologisticoService.getInstance().findByProduto(ajustarEstoque.getEnderecoproduto().getProduto(), WmsUtil.getDeposito());

				if (ajustarEstoque.getEnderecoproduto().getCdenderecoproduto() == null){
					Produto produtoAux = produtoDAO.findPrincipal(ajustarEstoque.getEnderecoproduto().getProduto());
					if (produtoAux.getProdutoprincipal() != null){
						Dadologistico dadologisticoPP = DadologisticoService.getInstance().findByProduto(produtoAux.getProdutoprincipal(), WmsUtil.getDeposito());
						if (!dadologisticoPP.getNormavolume()){
							erros.append("O produto \"" + produtoAux.getProdutoprincipal().getCodigo() + "\" n�o utiliza norma de volume, portanto o estoque deve ser ajustado para o produto principal.\n");
						}					
					} else {
						List<Produto> volumes = produtoDAO.findVolumes(ajustarEstoque.getEnderecoproduto().getProduto());
						if (volumes != null && !volumes.isEmpty() && dadologistico.getNormavolume()){
							erros.append("O produto \"" + ajustarEstoque.getEnderecoproduto().getProduto().getCodigo() + "\" utiliza norma de volume, portanto o estoque deve ser ajustado para cada volume do produto.\n");							
						}
					}
				}
				
				if (!endereco.getArea().getAvaria() && !endereco.getArea().getVirtual() && dadologistico.getLarguraexcedente()){
					if (!endereco.getLarguraexcedente()){
						erros.append("O endere�o \"" + endereco.getEndereco() + "\" n�o possui largura excedente compat�vel com o produto.\n");
					}
					
					String vizinhoStr = EnderecoService.calcularEnderecoVizinho(endereco);
					Endereco vizinho = enderecoService.findEndereco(vizinhoStr, endereco.getArea());
					if (ajustarEstoque.getEnderecoproduto().getQtde() > 0L && vizinho != null && !vizinho.getEnderecostatus().equals(Enderecostatus.DISPONIVEL) && !vizinho.getEnderecostatus().equals(Enderecostatus.EMPRESTIMO)){
						erros.append("O endere�o vizinho ao \"" + endereco.getEndereco() + "\" n�o est� dispon�vel.\n");
					}
				}
			}
		}
		
		return erros.toString();
	} 

	/* singleton */
	private static ProdutoService instance;
	public static ProdutoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutoService.class);
		}
		
		return instance;
	}

	public boolean existsRepetidoDescricoes(List<Produtoembalagem> listaProdutoEmbalagem,Produtoembalagem produtoembalagem) {
		if(listaProdutoEmbalagem == null || produtoembalagem == null)
			throw new WmsException("Os par�mentros n�o devem ser nulos");
		
		int count = 0;
		for (Produtoembalagem produtoembalagemIt : listaProdutoEmbalagem) {
			if(produtoembalagemIt.getDescricao().equals(produtoembalagem.getDescricao())){
				count++;
				if(count == 2){
					return true;
				}
			}
			
		}
		return false;
	}
	
	/**
	 * Carrega os dados log�sticos do produto. 
	 * 
	 * Encontra a embalagem do produto a partir do valor bipado no coletor.
	 * 
	 * Encontra o produto a partir do c�digo de barras.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findProdutoByBarcode(String, Deposito)
	 * 
	 * @param produto
	 * @return
	 * @author Pedro Gon�alves
	 * @param aceitarCodProduto Indica se deve aceitar c�digo do produto ou se ser� apenas c�digos de barras.
	 */
	public Produto findProdutoByBarcode(String codigobarra,Deposito deposito, boolean aceitarCodProduto){
		return produtoDAO.findProdutoByBarcode(codigobarra, deposito, aceitarCodProduto);
	}
	
	/**
	 * Retorna o produto com RestricaoNivel e a norma
	 * @param produto
	 * @return
	 * @author C�ntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#loadWithNormaAndRestricaoNivel(Produto)
	 */
	public Produto loadWithNormaAndRestricaoNivel(Produto produto){	
		return produtoDAO.loadWithNormaAndRestricaoNivel(produto);
	}
	
	/**
	 * Retorna produto com par�metros de embalagem e dadologistico preenchidos
	 * @param produto
	 * @return
	 * @author C�ntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#loadWithEmbalagem(Produto)
	 */
	public Produto loadWithEmbalagem(Produto produto){
		return produtoDAO.loadWithEmbalagem(produto);
	}

	/**
	 * Cria o relat�rio de estoque.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public IReport createReportEstoque(RelatorioestoqueFiltro filtro) {
		Report report;
		if (filtro.getTiporelatorio() != null && 
				RelatorioestoqueFiltro.TIPO_SINTETICO.getId().toString().equals(filtro.getTiporelatorio().getId()))
			report = new Report("RelatorioestoqueprodutoSintetico");
		else
			report = new Report("RelatorioestoqueprodutoAnalitico");
		
		
		List<ProdutosaldoVO> listaSaldo = produtoDAO.findProdutosaldo(filtro);
		report.setDataSource(listaSaldo);
		
		return report;
	}	
	
	/**
	 * Retorna o produto de acordo com o c�digo
	 * @param codigo
	 * @return
	 * @author C�ntia Nogueira
	 */
	public Produto getProdutoByCodigo(String codigo){
		return produtoDAO.getProdutoByCodigo(codigo);
	}

	/**
	 * Retorna o saldo de estoque de todos os produto de um determinado dep�sito.
	 * 
	 * @param deposito
	 * @return
	 * @author Giovane Freitas
	 * @see br.com.linkcom.wms.geral.dao.ProdutoDAO#findSaldoProduto(Deposito)
	 */
	public List<SaldoProdutoVO> findSaldoProduto(Deposito deposito) {
		return produtoDAO.findSaldoProduto(deposito);
	}

	/**
	 * M�todo que verifica se o recebimento possui algum produto com dados faltantes.
	 * Os dados log�sticos que ser�o considerados s�o: todos os campos dos grupos Embalagem de Recebimento, 
	 * Normas de paletiza��o e Armazenagem, incluindo Estrutura (Endere�o de  picking s� ser� exigido se a 
	 * primeira estrutura cadastrada for porta-palete);
	 * 
	 * @param listaProdutos
	 * @param tipoenderecamento 
	 * @return
	 * @author Tom�s Rabelo
	 */
	public boolean possuiProdutosDadosFaltantes(List<Produto> listaProdutos, Tipoenderecamento tipoenderecamento) {
		if(listaProdutos != null && listaProdutos.size() > 0){
			for (Produto produto : listaProdutos) {
				if(produto.getListaDadoLogistico() != null && !produto.getListaDadoLogistico().isEmpty()){
					Dadologistico dadologistico = produto.getListaDadoLogistico().get(0);
					
					List<Produto> listaVolumes = findProdutosDadosFaltantesVolumeNovo(produto);

					//Se possuir o checkbox marcado verifica os dados de endere�amento do volume sen�o verifica do produto principal
					if(dadologistico.getNormavolume()){
						for (Produto volume : listaVolumes)
							if(validaProdutoDadosFaltantes(volume, tipoenderecamento))
								return true;
					}else{
						if(validaProdutoDadosFaltantes(produto, tipoenderecamento))
								return true;
					}
					
					//se tem volumes verifico o peso, altura e largura deles
					if (listaVolumes != null && listaVolumes.size() > 0){
						for (Produto volume : listaVolumes)
							if (Tipoenderecamento.AUTOMATICO.equals(tipoenderecamento) && !isDimensoesValidas(volume))
								return true;
					}else if (Tipoenderecamento.AUTOMATICO.equals(tipoenderecamento) && !isDimensoesValidas(produto))
							return true;
										
				}else{
					return true;
				}
			}
		}else
			return true;
		
		return false;
	}

	/**
	 * Verifica se as dimens�es foram preenchidas corretamente.
	 * 
	 * @param produto
	 * @return
	 */
	private boolean isDimensoesValidas(Produto produto) {
		if ((produto.getAltura() == null || produto.getAltura().equals("") || 
				   produto.getLargura() == null || produto.getLargura().equals("") || 
				   produto.getProfundidade() == null || produto.getProfundidade().equals("") || 
				   produto.getPeso() == null || produto.getPeso().equals("")))
			return false;
		else
			return true;
	}

	/**
	 * Grava a linha de separa��o padr�o para os produtos que ainda n�o possuem uma linha definida.
	 * 
	 * @author Giovane Freitas
	 * @param deposito 
	 * @param linhaseparacaoPadrao
	 */
	public void definirLinhaPadrao(Deposito deposito, Linhaseparacao linhaseparacao) {
		produtoDAO.definirLinhaPadrao(deposito, linhaseparacao);
	}


	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param produto
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Produto getProdutoDescriptionProperty(Produto produto) {
		return produtoDAO.getProdutoDescriptionProperty(produto);
	}

	/**
	 * M�todo que prepara o relat�rio de movimenta��o
	 * 
	 * @see #retiraMovimentacoesZeradas(List)
	 * @param filtro
	 * @return
	 * @author Tom�s Rabelo
	 */
	public IReport createReportMovimentacao(RelatoriomovimentacaoFiltro filtro) {
		Report report = new Report("RelatorioMovimentacao");

		List<Produto> list = findProdutoForMovimentacaoReport(filtro);
		retiraMovimentacoesZeradas(list);
		
		adicionaInformacoesComplementares(list, filtro);
		
		report.setDataSource(list);
		
		if(filtro.getProduto() != null && filtro.getProduto().getCdproduto() != null)
			report.addParameter("PRODUTO", load(filtro.getProduto()).getDescricao());
		if(filtro.getCliente() != null && filtro.getCliente().getCdpessoa() != null)
			report.addParameter("CLIENTE", clienteService.load(filtro.getCliente()).getNome());

		report.addParameter("nomeusuario", WmsUtil.getUsuarioLogado().getNome());
		report.addParameter("data", NeoFormater.getInstance().format(new Timestamp(System.currentTimeMillis())));
		report.addParameter("PERIODO", WmsUtil.getDescricaoPeriodo(filtro.getDatade(), filtro.getDataate()));
		report.addParameter("DEPOSITO", WmsUtil.getDeposito().getNome());
		return report;
	}

	/**
	 * M�todo que adiciona informa��es complementares nos produtos para relatorio de movimenta��o.
	 * Qtde de pallete e pico de pallete
	 * 
	 * @param list
	 * @param filtro
	 * @auhtor Tom�s Rabelo
	 */
	private void adicionaInformacoesComplementares(List<Produto> list, RelatoriomovimentacaoFiltro filtro) {
		if(list != null && list.size() > 0){
			Deposito depositoAtual = WmsUtil.getDeposito();
			for (Produto produto : list) {
				produto.setQtdePallete(produtotipopaleteService.getPalletsOcupados(produto, depositoAtual));
				produto.setPicoPallete(buscaPicoPallete(depositoAtual, produto, filtro.getDatade(), filtro.getDataate(), produto.getQtdeanterior()));
			}
		}
	}


	/**
	 * M�todo que chama a function BUSCAR_PICOPALLETE
	 * 
	 * @param depositoAtual
	 * @param produto
	 * @param datade
	 * @param dateate
	 * @param qtdeanterior
	 * @return
	 * @author Tom�s rabelo
	 */
	public Long buscaPicoPallete(Deposito depositoAtual, Produto produto, Date datade, Date dateate, Integer qtdeanterior) {
		return produtoDAO.buscaPicoPallete(depositoAtual, produto, datade, dateate, qtdeanterior);
	}

	/**
	 * M�todo que retira todos os produtos do relat�rio de movimenta��o que possuem todas as qtdes (anterior, entrada, saida, final)
	 * igual a 0
	 * 
	 * @param list
	 * @author Tom�s Rabelo
	 */
	private void retiraMovimentacoesZeradas(List<Produto> list) {
		List<Produto> itensExcluir = new ArrayList<Produto>();
		if(list != null && list.size() > 0)
			for (Produto produto : list) 
				if(produto.getQtdeanterior() == 0 && produto.getQtdeentrada() == 0 && produto.getQtdesaida() == 0 && produto.getQtdefinal() == 0)
					itensExcluir.add(produto);
		
		if(itensExcluir != null && itensExcluir.size() > 0)
			for (Produto produto : itensExcluir) 
				list.remove(produto);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param filtro
	 * @return
	 * @author Tom�s Rabelo
	 */
	private List<Produto> findProdutoForMovimentacaoReport(RelatoriomovimentacaoFiltro filtro) {
		return produtoDAO.findProdutoForMovimentacaoReport(filtro);
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param codigo
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Produto findProdutoByCodigo(String codigo) {
		return produtoDAO.findProdutoByCodigo(codigo);
	}

	/**
	 * Busca as movimenta��es dos produtos em um determinado per�odo para a montagem da classifica��o ABC.
	 * O per�odo � filtrado baseado na data de finaliza��o dos carregamentos.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return 
	 * @return
	 */
	public List<ClassificacaoABC> findForClassificacaoABC(ClassificacaoABCFiltro filtro) {
		return produtoDAO.findForClassificacaoABC(filtro);
	}

	/**
	 * M�todo que valida se os c�digos dos produtos existem.
	 * 
	 * Este m�todo � utilizado para validar o invent�rio e o reabastecimento preventivo.
	 * 
	 * @param listaInventariolote
	 * @param errors
	 * @author Tom�s Rabelo
	 */
	public void validaProdutos(Collection<IntervaloEndereco> lotes,	BindException errors) {
		StringBuilder sb = new StringBuilder("");
		for (IntervaloEndereco lote : lotes) 
			if(lote.getProduto() == null && lote.getCodigo() != null && !lote.getCodigo().equals("")){
				lote.setProduto(this.findProdutoByCodigo(lote.getCodigo()));
				if(lote.getProduto() == null || lote.getProduto().getCdproduto() == null)
					sb.append(lote.getCodigo()).append(", ");
			}
		
		if(!sb.toString().equals(""))
			errors.reject("002", "O(s) c�digo(s) do(s) produto(s) "+sb.delete(sb.length()-2, sb.length())+" n�o existe(m)."); 
	}
	
	/**
	 * Lista os produtos que est�o inclu�dos em uma determinada ordem de servi�o.
	 * 
	 * @param recebimento
	 * @return
	 * @author Giovane Freitas
	 * 
	 */
	public List<Produto> findByOrdemservico(Ordemservico ordemservico) {
		return produtoDAO.findByOrdemservico(ordemservico);
	}
	
	/**
	 * Busca o valor m�dio de um determinado produto em uma determinada data. Se
	 * a data for <code>null</code> ser� considerada a data atual.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param produto
	 * @param data
	 * @return
	 */
	public double getValorMedioEntrada(Deposito deposito, Produto produto, Date data) {
		return produtoDAO.getValorMedioEntrada(deposito, produto, data);
	}
	
	public Produto findByCodigoERP(Long codigoERP){
		return produtoDAO.findByCodigoERP(codigoERP);
	}

	/**
	 * Carrega um produto pelo seu c�digo de barras
	 * @param codigo
	 * @throws WmsException Se o param�tro c�digo for nulo.
	 * @return
	 */
	public Produto loadByCodigoBarras(String codigo) {
		if(codigo==null) throw new WmsException("Param�tros inv�lidos em ProdutoService#loadByCodigoBarras: codigo n�o pode ser nulo.");
		return produtoDAO.loadByCodigoBarras(codigo);
	}

	/**
	 * @author Filipe Santos
	 * @since 12/01/2012
	 * 
	 * @return count dos produtos do deposito
	 */
	public Integer countProdutosByDeposito(){
		return produtoDAO.countProdutosByDeposito(WmsUtil.getDeposito());
	}
	
}