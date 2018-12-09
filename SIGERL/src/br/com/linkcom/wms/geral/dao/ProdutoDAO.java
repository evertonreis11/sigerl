package br.com.linkcom.wms.geral.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.geral.bean.vo.ClassificacaoABC;
import br.com.linkcom.wms.geral.bean.vo.ProdutosaldoVO;
import br.com.linkcom.wms.geral.bean.vo.SaldoProdutoVO;
import br.com.linkcom.wms.geral.service.DepositoService;
import br.com.linkcom.wms.geral.service.UsuariodepositoService;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ClassificacaoABCFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.Ordenacao;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.RelatorioestoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.RelatoriomovimentacaoFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.TipoAnalise;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ProdutoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EtiquetaprodutoFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

import com.ibm.icu.util.Calendar;

@DefaultOrderBy("produto.descricao")
public class ProdutoDAO extends GenericDAO<Produto> {
	
	@Override
	public void updateSaveOrUpdate(final SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaProdutoCodigoDeBarras")
			.saveOrUpdateManaged("listaProdutoEmbalagem");
	}

	@Override
	public void updateListagemQuery(QueryBuilder<Produto> query,FiltroListagem _filtro) {
		ProdutoFiltro filtro = (ProdutoFiltro) _filtro;
		
		query
			.select("distinct produto.cdproduto, produto.descricao, produto.referencia, produto.codigo,produtoclasse.cdprodutoclasse," +
					"produtoclasse.nome,produtoprincipal.descricao")
			.openParentheses()		
			.leftOuterJoin("produto.produtoprincipal produtoprincipal")
			.leftOuterJoin("produto.produtoclasse produtoclasse")
			.leftOuterJoin("produto.listaDadoLogistico dadologistico")
			.leftOuterJoin("produtoprincipal.listaDadoLogistico dadologisticoPrincipal")
			.whereLikeIgnoreAll("produto.descricao",filtro.getDescricao())
			.whereLikeIgnoreAll("produto.referencia",filtro.getReferencia())
			.whereLikeIgnoreAll("produto.codigo", filtro.getCodigo())
			.where("produto.produtoclasse=?",filtro.getProdutoclasse())
			.where("exists (select cddadologistico from Dadologistico where cdproduto = produto.cdproduto " +
				   "and cddeposito = " + WmsUtil.getDeposito().getCddeposito() + " and linhaseparacao = ?)",
				   filtro.getLinhaseparacao())
			.where("produtoprincipal is null");
			
		//Busca produtos filhos apenas para o módulo de logística(os popups de produto)
		if(WmsUtil.getNomeModulo().equals("popup")){
			query
			.where("dadologistico.normavolume=0")
			.closeParentheses()
			.or()
			.openParentheses()
			.where("dadologisticoPrincipal.normavolume=1")
			.whereLikeIgnoreAll("produtoprincipal.descricao",filtro.getDescricao())
			.whereLikeIgnoreAll("produtoprincipal.referencia",filtro.getReferencia())
			.whereLikeIgnoreAll("produtoprincipal.codigo", filtro.getCodigo())
			.where("produtoprincipal.produtoclasse=?",filtro.getProdutoclasse())
			.where("exists (select cddadologistico from Dadologistico where cdproduto = produtoprincipal.cdproduto " +
				   "and cddeposito = " + WmsUtil.getDeposito().getCddeposito() + " and linhaseparacao = ?)",
				   filtro.getLinhaseparacao())
			.closeParentheses();
		}else{
			query
			.closeParentheses()
			.orderBy("produto.descricao");
//			query.ignoreJoin("produtoprincipal");
		}
		
		query.ignoreJoin("dadologistico");
		query.ignoreJoin("dadologisticoPrincipal");
		
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Produto> query) {
		
		makeSelect(query);

	}
	
	/**
	 * @author Leonardo Guimarães
	 * 
	 * Busca todos os produtos a partir do
	 * pedidovenda informado
	 * 
	 * @param pedidovenda
	 * @return
	 */
	public List<Produto> findByPedidovenda(Pedidovenda pedidovenda) {
		if(pedidovenda == null || pedidovenda.getCdpedidovenda() == null)
			throw new WmsException("O documento de saída não deve ser nulo");
		return query()
					.select("produto.cdproduto, produto.codigoerp, produto.descricao")
					.leftOuterJoin("produto.listaPedidoVendaProduto listaPedidoVendaProduto")
					.leftOuterJoin("listaPedidoVendaProduto.pedidovenda pedidovenda")
					.where("pedidovenda =?",pedidovenda)
					.list();
	}
		
	/**
	 * 
	 * Recupera uma lista de produtos pelo recebimento
	 * 
	 * @param recebimento
	 * @return
	 * @author Arantes
	 * 
	 */
	public List<Produto> findByRecebimento(Recebimento recebimento,String ... fields) {
		if((recebimento == null) || (recebimento.getCdrecebimento() == null))
			throw new WmsException("O recebimento não deve ser nulo");
		
		return query()
			.select("produto.cdproduto, produto.descricao")
			.join("produto.listaNotafiscalentradaproduto notafiscalentradaproduto")
			.join("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
			.join("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscal")
			.join("recebimentonotafiscal.recebimento recebimento")
			.where("recebimento = ?", recebimento)
			.orderBy("produto.descricao")
			.list();
	}

	/**
	 * Busca todos os produtos que possuem dados logísticos em falta.
	 * 
	 * @param filtro - é opcional pois o sistema pode pegar todos os produtos com dados faltantes.
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Produto> findProdutosDadosFaltantes(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		return query()
				.select("distinct produto.cdproduto, produto.codigo, produto.descricao," +
						"produto.altura, produto.largura,produto.qtdevolumes, produto.profundidade, produto.peso," +
						"produtotipopalete.cdprodutotipopalete,listaProdutoCodigoDeBarras.cdprodutocodigobarras," +
						"listaProdutoCodigoDeBarras.valido")
				.leftOuterJoin("produto.listaProdutoTipoPalete produtotipopalete")
				.leftOuterJoin("produto.listaNotafiscalentradaproduto notafiscalentradaproduto")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
				.leftOuterJoin("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
				.leftOuterJoin("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscal")
				.leftOuterJoin("recebimentonotafiscal.recebimento recebimento")
				.openParentheses()
					.where("produto.altura is null").or().where("trim(produto.altura) = ''")
					.or()
					.where("produto.largura is null").or().where("trim(produto.largura) = ''")
					.or()
					.where("produto.profundidade is null").or().where("trim(produto.profundidade) = ''")
					.or()
					.where("produto.peso is null").or().where("trim(produto.peso) = ''")
				.closeParentheses()
				.where("recebimento=?",recebimento)
				.orderBy("produto.descricao")
				.list();
	}

	/**
	 * Busca todos os produtos que possuem dados logísticos em falta. Método novo, busca mais campos que o anterior. 
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Produto> findProdutosDadosFaltantesNovo(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		return query()
				.select("distinct produto.cdproduto, produto.codigo, produto.descricao, " +
						"produto.altura, produto.largura,produto.qtdevolumes, produto.profundidade, produto.peso, produto.cubagem, " +
						"produtotipopalete.cdprodutotipopalete,produtotipopalete.padrao, produtotipopalete.lastro, produtotipopalete.camada, " +
						"produtotipoestrutura.cdprodutotipoestrutura, linhaseparacao.cdlinhaseparacao, tipoendereco.cdtipoendereco, endereco.cdendereco, " +
						"tipoestrutura.cdtipoestrutura, tiporeposicao.cdtiporeposicao, area.cdarea, " +
						"listaProdutoEmbalagem.cdprodutoembalagem, listaDadoLogistico.normavolume, " +
						"listaDadoLogistico.cddadologistico, listaDadoLogistico.pontoreposicao, listaDadoLogistico.capacidadepicking, " +
						"listaProdutoCodigoDeBarras.cdprodutocodigobarras, listaProdutoCodigoDeBarras.valido")
				.leftOuterJoin("produto.listaProdutoTipoPalete produtotipopalete with produtotipopalete.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
				.leftOuterJoin("produto.listaProdutoTipoEstrutura produtotipoestrutura with produtotipoestrutura.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
				.leftOuterJoin("produtotipoestrutura.tipoestrutura tipoestrutura")
				.leftOuterJoin("produto.listaDadoLogistico listaDadoLogistico with listaDadoLogistico.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
				.leftOuterJoin("produto.listaProdutoEmbalagem listaProdutoEmbalagem")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
				.leftOuterJoin("listaDadoLogistico.linhaseparacao linhaseparacao")
				.leftOuterJoin("listaDadoLogistico.tipoendereco tipoendereco")
				.leftOuterJoin("listaDadoLogistico.endereco endereco")
				.leftOuterJoin("listaDadoLogistico.tiporeposicao tiporeposicao")
				.leftOuterJoin("listaDadoLogistico.area area")
				.leftOuterJoin("produto.listaNotafiscalentradaproduto notafiscalentradaproduto")
				.leftOuterJoin("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
				.leftOuterJoin("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscal")
				.leftOuterJoin("recebimentonotafiscal.recebimento recebimento")
				.where("recebimento=?",recebimento)
				.orderBy("produtotipoestrutura.cdprodutotipoestrutura")
				.list();
	}

	/**
	 * Busca todos os volumes dos produtos que possuem dados logísticos em falta.
	 * @param produtoPrincipal
	 * @return
	 */
	public List<Produto> findProdutosDadosFaltantesVolumeNovo(Produto produtoPrincipal) {
		if(produtoPrincipal == null || produtoPrincipal.getCdproduto() == null)
			throw new WmsException("O produto principal não deve ser nulo.");
		return query()
		.select("distinct produto.cdproduto, produto.codigo, produto.descricao,produto.cubagem, " +
				"produto.altura, produto.largura,produto.qtdevolumes, produto.profundidade, produto.peso, " +
				"produtotipopalete.cdprodutotipopalete, produtotipopalete.padrao, produtotipoestrutura.cdprodutotipoestrutura, " +
				"linhaseparacao.cdlinhaseparacao, tipoendereco.cdtipoendereco, endereco.cdendereco, tipoestrutura.cdtipoestrutura, " +
				"tiporeposicao.cdtiporeposicao, area.cdarea, listaProdutoEmbalagem.cdprodutoembalagem, listaDadoLogistico.normavolume, " +
				"listaDadoLogistico.cddadologistico, listaDadoLogistico.pontoreposicao, listaDadoLogistico.capacidadepicking, " +
				"listaProdutoCodigoDeBarras.cdprodutocodigobarras, listaProdutoCodigoDeBarras.valido")
		.join("produto.produtoprincipal produtoprincipal")
		.leftOuterJoin("produto.listaProdutoTipoPalete produtotipopalete with produtotipopalete.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
		.leftOuterJoin("produto.listaProdutoTipoEstrutura produtotipoestrutura")
		.leftOuterJoin("produtotipoestrutura.tipoestrutura tipoestrutura")
		.leftOuterJoin("produto.listaDadoLogistico listaDadoLogistico with listaDadoLogistico.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
		.leftOuterJoin("produto.listaProdutoEmbalagem listaProdutoEmbalagem")
		.leftOuterJoin("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
		.leftOuterJoin("listaDadoLogistico.linhaseparacao linhaseparacao")
		.leftOuterJoin("listaDadoLogistico.tipoendereco tipoendereco")
		.leftOuterJoin("listaDadoLogistico.endereco endereco")
		.leftOuterJoin("listaDadoLogistico.tiporeposicao tiporeposicao")
		.leftOuterJoin("listaDadoLogistico.area area")
		.where("produtoprincipal=?",produtoPrincipal)
		.orderBy("produtotipoestrutura.cdprodutotipoestrutura")
		.list();
	}
	
	/**
	 * Busca todos os volumes do produto indicado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #makeSelect(QueryBuilder)
	 * 
	 * @param form
	 * @return
	 */
	public List<Produto> findVolumes(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto não deve ser nulo.");
		return query() 
				.select("produto.cdproduto, produto.codigoerp, " +
					"produto.codigo,produto.descricao,produto.referencia, produtoclasse.cdprodutoclasse," +
					"produtoclasse.nome,produtoclasse.numero, produto.altura, produto.largura, " +
					"produto.profundidade, produto.peso,produto.complementocodigobarras,produtoprincipal.cdproduto," +
					"listaProdutoCodigoDeBarras.cdprodutocodigobarras,listaProdutoCodigoDeBarras.codigo," +
					"listaProdutoEmbalagem.cdprodutoembalagem,listaProdutoEmbalagem.qtde")
				.leftOuterJoin("produto.produtoclasse produtoclasse")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
				.leftOuterJoin("produto.listaProdutoEmbalagem listaProdutoEmbalagem")
				//.leftOuterJoin("produto.listaDadoLogistico listaDadoLogistico")
				//.leftOuterJoin("listaDadoLogistico.deposito deposito")
				.where("produtoprincipal =?",produto)
				//os volumes devem aparecer em todos os depósitos.
				//.openParentheses() 
				//	.where("deposito = ?",WmsUtil.getDeposito())
				//	.or()
				//	.where("listaDadoLogistico is null")
				//.closeParentheses()
				.orderBy("produto.cdproduto")
				.list();
	}
	
	/**
	 * Cria um select de todos os itens da tabela produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param query
	 */
	public void makeSelect(QueryBuilder<Produto> query) {
			query
				.select("produto.cdproduto, produto.codigoerp,produto.qtdevolumes," +
				"produto.codigo,produto.descricao,produto.referencia, produtoclasse.cdprodutoclasse," +
				"produtoclasse.nome,produtoclasse.numero, produto.altura, produto.largura,produto.cubagem, " +
				"produto.profundidade, produto.peso,produto.complementocodigobarras,produtoprincipal.cdproduto," +
				"produtoprincipal.descricao,produtoprincipal.codigo,produto.exigelote,produto.exigevalidade")
				.leftOuterJoin("produto.produtoclasse produtoclasse")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal");
	}
	
	/**
	 * 
	 * Recupera os produtos pela ordem de serviço e pela ordem tipo
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @param ordemtipo
	 * @return List<Produto>
	 * 
	 */
	public List<Produto> findByOrdemservico(Ordemservico ordemservico, Ordemtipo ordemtipo) {
		if((ordemservico == null) || (ordemservico.getCdordemservico() == null) || (ordemtipo == null) || (ordemtipo.getCdordemtipo() == null))
			throw new WmsException("Parâmetros inválidos");
		
		QueryBuilder<Produto> query = query();
		
		query
			.select("produto.cdproduto, produto.codigo, produto.descricao")
			.join("produto.listaOrdemServicoProduto listaOrdemServicoProduto")
			.join("listaOrdemServicoProduto.listaOrdemprodutohistorico listaOrdemprodutohistorico")
			.join("listaOrdemprodutohistorico.ordemservico ordemservico")		
			.where("ordemservico = ?", ordemservico);
		
		if(ordemtipo.equals(Ordemtipo.MAPA_SEPARACAO)) {
			query
				.where("ordemservico.ordemtipo = ?", Ordemtipo.MAPA_SEPARACAO);
		
		} else if(ordemtipo.equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1)) {
			query
				.where("ordemservico.ordemtipo = ?", Ordemtipo.RECONFERENCIA_EXPEDICAO_1)
				.where("listaOrdemprodutohistorico.qtde is not null")
				.where("listaOrdemServicoProduto.qtdeesperada <> listaOrdemprodutohistorico.qtde");
		}
		
		return query
			.orderBy("produto.codigo, produto.descricao")
			.list();
	}
	
	/**
	 * Busca o produto pai do produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @return
	 */
	public Produto findPrincipal(Produto produto){
		if (produto == null || (produto.getCdproduto() == null && produto.getCodigo() == null))
			throw new WmsException("Parâmetro inválido.");
		
		QueryBuilder<Produto> queryBuilder = query()
				.select("produto.cdproduto,produto.descricao,produto.codigo,pp.cdproduto,pp.codigo,pp.descricao")
				.leftOuterJoin("produto.produtoprincipal pp");
		
		if (produto.getCdproduto() != null)
			queryBuilder.where("produto =?",produto);
		else if (produto.getCodigo() != null && !produto.getCodigo().isEmpty())
			queryBuilder.where("upper(produto.codigo) = ? ",produto.getCodigo().toUpperCase());
		else
			throw new WmsException("Produto inválido.");

		
		return queryBuilder.setMaxResults(1).unique();
	}
	
	/**
	 * Carrega os dados logísticos do produto. 
	 * 
	 * Encontra a embalagem do produto a partir do valor bipado no coletor.
	 * 
	 * Pega o tipo palete padrão de acordo com o depósito logado no coletor.
	 * 
	 * Caso o código de barras seja nulo, deverá pegar todos os códigos do produto.
	 * 
	 * @param produto
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Produto findDadosLogisticosProdutoRF(Produto produto,Deposito deposito){
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("produto.cdproduto, produto.codigoerp, produto.descricao,produto.complementocodigobarras, produto.exigevalidade, " +
						"produto.exigelote, produtoprincipal.cdproduto, produtoprincipal.codigoerp, produtoprincipal.descricao," +
						"tipopaletepp.nome,pptipopalete.lastro,pptipopalete.camada,pptipopalete.padrao," +
						"ppprodutoembalagem.descricao,ppprodutoembalagem.compra,ppprodutoembalagem.qtde," +
						"produtoembalagem.descricao,produtoembalagem.compra,produtoembalagem.qtde,produtoembalagem.cdprodutoembalagem," +
						"codigobarras.codigo," +
						"tipopalete.nome,ptipopalete.lastro,ptipopalete.camada,ptipopalete.padrao, produto.codigo, "+ 
						"embalagemcodigo.cdprodutoembalagem, embalagemcodigo.qtde, embalagemcodigo.compra ")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras codigobarras")
				.leftOuterJoin("codigobarras.produtoembalagem embalagemcodigo")
				
				//a restrição de depósito deve ser feita no momento do join
				//se ficar no where irá invalidar o leftjoin quanto existir
				//produtotipopalete para um depósito e para o outro não
				.leftOuterJoin("produto.listaProdutoTipoPalete ptipopalete with ptipopalete.deposito.cddeposito=" + deposito.getCddeposito())
				
				.leftOuterJoin("produto.listaProdutoEmbalagem produtoembalagem")
				.leftOuterJoin("ptipopalete.deposito deposito")
				.leftOuterJoin("ptipopalete.tipopalete tipopalete")
				.leftOuterJoin("produtoprincipal.listaProdutoTipoPalete pptipopalete")
				.leftOuterJoin("produtoprincipal.listaProdutoEmbalagem ppprodutoembalagem")
				.leftOuterJoin("pptipopalete.deposito depositopp")
				.leftOuterJoin("pptipopalete.tipopalete tipopaletepp")
				//.where("codigobarras.codigo=?",codigobarra)
				
				//põe todos os tipos de palete
//				.openParentheses()
//					.where("produtoembalagem.compra is null")
//					.or()
//					.where("produtoembalagem.compra=?",Boolean.TRUE)
//				.closeParentheses()
//				.where("ptipopalete.padrao=?",Boolean.TRUE)
				.entity(produto)
				.unique();
	}
	
	/**
	 * Carrega os dados logísticos do produto. 
	 * 
	 * Encontra a embalagem do produto a partir do valor bipado no coletor.
	 * 
	 * Encontra o produto a partir do código de barras.
	 * 
	 * @param produto
	 * @return
	 * @author Pedro Gonçalves
	 * @param aceitarCodProduto Indica se deve aceitar código do produto ou se será apenas códigos de barras.
	 */
	public Produto findProdutoByBarcode(String codigobarra,Deposito deposito, boolean aceitarCodProduto){
		if(codigobarra == null || "".equals(codigobarra))
			throw new WmsException("Parâmetros incorretos.");
		/*Queiroz - 05-05-2014 
		 * Acrescentando o campo Principal que indica qual o codigo de barras 
		 * é o principal do produto*/
		
		QueryBuilder<Produto> queryBuilder = query()
				.select("produto.cdproduto, produto.codigoerp, produto.descricao, produtoembalagem.descricao," +
						"produtoembalagem.compra,produtoembalagem.qtde,codigobarras.codigo,codigobarras.principal," +
						"tipopalete.nome,ptipopalete.lastro,ptipopalete.camada,ptipopalete.padrao, produto.codigo," +
						"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao," +
						"produto.peso,produto.altura,produto.profundidade,produto.largura")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras codigobarras")
				.leftOuterJoin("produto.listaProdutoTipoPalete ptipopalete with ptipopalete.deposito.id = " + deposito.getCddeposito())
				.leftOuterJoin("produto.listaProdutoEmbalagem produtoembalagem")
				.leftOuterJoin("ptipopalete.tipopalete tipopalete")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal");
		
		if (aceitarCodProduto){
			queryBuilder.openParentheses()
				.where("codigobarras.codigo=?",codigobarra)
				.or()
				.where("UPPER(produto.codigo) = UPPER(?)", codigobarra)
			.closeParentheses();
		}else{
			queryBuilder.where("codigobarras.codigo=?",codigobarra);
		}
		
		return queryBuilder.unique();
	}
	
	/**
	 * Altera a descrição e referência do produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param volume
	 */
	public void updateDadosVolume(Produto volume) {
		if(volume == null || volume.getCdproduto() == null)
			throw new WmsException("Os dados do volume estão incorretos");
		getHibernateTemplate().bulkUpdate("update Produto p set p.descricao=?, p.referencia=? where p.id=?", new Object[]{volume.getDescricao(),volume.getReferencia(),volume.getCdproduto()});
	}

	/**
	 * Busca os dados necessário para criação do relatório de Etiquetas
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Produto> findForReport(EtiquetaprodutoFiltro filtro) {
		if(filtro == null || (filtro.getRecebimento() == null && filtro.getProduto() == null))
			throw new WmsException("Dados inválidos para geração do relatório.");
		
		String selectAdicional = "";
		if (filtro.getRecebimento() != null)
			selectAdicional = "listaOrdemServicoProduto.cdordemservicoproduto, listaOrdemServicoProduto.qtdeesperada,";
		
		QueryBuilder<Produto> queryBuilder = query().select("produto.cdproduto,produto.descricao,produto.codigo,produto.complementocodigobarras,"+
					 		"listaProdutoCodigoDeBarras.cdprodutocodigobarras,listaProdutoCodigoDeBarras.codigo," +
					 		"listaDadoLogistico.normavolume," +
					 		selectAdicional  +
					 		"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao")
					.join("produto.listaDadoLogistico listaDadoLogistico")
					.join("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
					.leftOuterJoin("produto.produtoprincipal produtoprincipal")
					.leftOuterJoin("produtoprincipal.listaDadoLogistico  dadologisticoPP")
					.leftOuterJoin("produto.listaOrdemServicoProduto listaOrdemServicoProduto")							
					.where("listaProdutoCodigoDeBarras.interno is true")
					.where("listaDadoLogistico.deposito =?",WmsUtil.getDeposito())
					.where("produto = ? ", filtro.getProduto());		
		if (filtro.getRecebimento() != null){
			queryBuilder.join("listaOrdemServicoProduto.listaOrdemprodutoLigacao listaOrdemprodutoLigacao")					
					.join("listaOrdemprodutoLigacao.ordemservico ordemservico")
					.join("ordemservico.recebimento recebimento")
					.where("ordemservico.ordem = 1")
 					.where("recebimento = ?",filtro.getRecebimento())
					.openParentheses()
						.where("listaDadoLogistico.geracodigo is true")
						.or()
						.where("dadologisticoPP.geracodigo is true")
					.closeParentheses();
		}
//		else{
//			queryBuilder.
//				openParentheses()
//					.where("produto = ? ", filtro.getProduto())
//					.where("not exists (from Produto p2 where p2.produtoprincipal = ?)", filtro.getProduto())
//					.or()
//					.where("produtoprincipal = ? ", filtro.getProduto())
//				.closeParentheses();
//		}
		
		
		queryBuilder.orderBy("produtoprincipal.codigo,produto.codigo");
		
		return queryBuilder.list();
	}
	
	/**
	 * Executa a procedure ATUALIZAR_PRODUTO que irá atualizar 
	 * os dados do produto principal e dos volumes.
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 */
	public void atualizarProduto(Produto produto) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("Dados insuficientes para invocar a procedure 'ATUALIZAR_PRODUTO'");
		
		getJdbcTemplate().update("BEGIN ATUALIZAR_PRODUTO(" + WmsUtil.getDeposito().getCddeposito() + ", " +
				produto.getCdproduto() +"); END;");
	}
	
	/**
	 * Retorna o produto com RestricaoNivel e a norma
	 * @param produto
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Produto loadWithNormaAndRestricaoNivel(Produto produto){		
	if(produto == null || produto.getCdproduto() == null){
				throw new WmsException("O produto não pode ser nulo");
		}
		
	return query()
			.select("produto.cdproduto, listaProdutoTipoPalete.cdprodutotipopalete,listaProdutoTipoPalete.padrao, listaProdutoTipoPalete.lastro," +
					"listaProdutoTipoPalete.camada, listaProdutoTipoEstrutura.cdprodutotipoestrutura, " +
					"listaProdutoTipoEstrutura.restricaonivel, tipopalete.cdtipopalete, tipopalete.nome, " +
					"tipoestrutura.cdtipoestrutura, tipoestrutura.nome")
					.leftOuterJoin("produto.listaProdutoTipoPalete listaProdutoTipoPalete")
					.leftOuterJoin("listaProdutoTipoPalete.tipopalete tipopalete")
					.leftOuterJoin("produto.listaProdutoTipoEstrutura listaProdutoTipoEstrutura")
					.leftOuterJoin("listaProdutoTipoEstrutura.tipoestrutura tipoestrutura")
					.where("produto.cdproduto=?", produto.getCdproduto())
					.unique();
	}
	
	/**
	 * Retorna produto com parâmetros de embalagem e dadologistico preenchidos
	 * @param produto
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Produto loadWithEmbalagem(Produto produto){
		if(produto == null || produto.getCdproduto() == null){
			throw new WmsException("O produto não pode ser nulo");
	}
	
		return query()
		.select("produto.cdproduto, produto.codigo, produto.descricao,produtoembalagem.cdprodutoembalagem," +
				"produtoembalagem.descricao, dadologistico.cddadologistico,dadologistico.larguraexcedente, " +
				"linhaseparacao.cdlinhaseparacao, linhaseparacao.nome, endereco.cdendereco, endereco.endereco, " +
				"enderecofuncao.cdenderecofuncao,enderecofuncao.nome, area.cdarea, area.codigo," +
				"produtotipoestrutura.cdprodutotipoestrutura,produtotipoestrutura.restricaonivel," +
				"tipoestrutura.cdtipoestrutura")
		.leftOuterJoin("produto.listaProdutoEmbalagem produtoembalagem")
		.leftOuterJoin("produto.listaProdutoTipoEstrutura produtotipoestrutura with produtotipoestrutura.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
		.leftOuterJoin("produtotipoestrutura.tipoestrutura tipoestrutura")
		.leftOuterJoin("produto.listaDadoLogistico dadologistico")
		.leftOuterJoin("dadologistico.linhaseparacao linhaseparacao")
		.leftOuterJoin("dadologistico.endereco endereco")
		.leftOuterJoin("endereco.enderecofuncao enderecofuncao")
		.leftOuterJoin("endereco.area area")
		.where("produto.cdproduto=?", produto.getCdproduto())
		.where("dadologistico.deposito =?",WmsUtil.getDeposito())
		.unique();
		
	}

	/**
	 * Retorna o saldo de estoque de todos os produto de um determinado depósito.
	 * 
	 * @param deposito
	 * @return
	 * @author Giovane Freitas
	 */
	@SuppressWarnings("unchecked")
	public List<SaldoProdutoVO> findSaldoProduto(Deposito deposito) {
		if (deposito == null || deposito.getCddeposito() == null) {
			throw new WmsException("O depósito não pode ser nulo");
		}

		StringBuilder select = new StringBuilder();
		select.append("select cdprodutoprincipal, codigo, codigoerp, descricao, volumes, \n"); 
		select.append("  count(distinct cdproduto) as volumesarmazenados, min(saldo) as saldo, max(avaria) as avaria, \n");
		select.append("  max(saldo) - min(saldo) as volumesdivergentes \n");
  
		select.append("from ( \n");
    
		select.append("    select v.cdproduto, v.cdprodutoprincipal, p.codigo,p.codigoerp,p.descricao, \n");
		select.append("      (select count(*) from produto where cdprodutoprincipal = p.cdproduto) as volumes, \n");
		select.append("      sum(case when a.avaria != 1 then ep.qtde else 0 end) as saldo , \n");
		select.append("      sum(case when a.avaria = 1 then ep.qtde else 0 end) as avaria \n");
		select.append("    from produto v \n"); 
		select.append("      inner join produto p on p.cdproduto = v.cdprodutoprincipal \n"); 
		select.append("      inner join enderecoproduto ep on ep.cdproduto =  p.cdproduto or ep.cdproduto = v.cdproduto \n");
		select.append("      inner join endereco e on ep.cdendereco = e.cdendereco \n");
		select.append("      inner join area a on e.cdarea = a.cdarea \n");
		select.append("    where a.cddeposito = ?  \n");
		select.append("    group by v.cdproduto, v.cdprodutoprincipal, p.cdproduto,p.codigo,p.codigoerp,p.descricao \n"); 

		select.append("    union all \n");
    
		select.append("    select p.cdproduto, p.cdproduto,p.codigo,p.codigoerp,p.descricao, \n");
		select.append("      1 as volumes, \n");
		select.append("      sum(case when a.avaria != 1 then ep.qtde else 0 end) as saldo , \n");
		select.append("      sum(case when a.avaria = 1 then ep.qtde else 0 end) as avaria \n");
		select.append("    from produto p \n"); 
		select.append("      inner join enderecoproduto ep on ep.cdproduto =  p.cdproduto \n");
		select.append("      inner join endereco e on ep.cdendereco = e.cdendereco \n");
		select.append("      inner join area a on e.cdarea = a.cdarea \n");
		select.append("    where a.cddeposito = ? \n");
		select.append("      and p.cdprodutoprincipal is null \n");
		select.append("      and p.cdproduto not in (select distinct cdprodutoprincipal from produto where cdprodutoprincipal is not null) \n");
		select.append("    group by p.cdproduto,p.codigo,p.codigoerp,p.descricao \n"); 

		select.append(") \n");
		select.append("group by cdprodutoprincipal, codigo, codigoerp, descricao, volumes \n");


		return (List<SaldoProdutoVO>) getJdbcTemplate().query(select.toString(),
				new Object[] { deposito.getCddeposito(), deposito.getCddeposito() }, new ResultSetExtractor() {

					public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

						List<SaldoProdutoVO> list = new ArrayList<SaldoProdutoVO>();

						while (rs.next()) {
							SaldoProdutoVO saldo = new SaldoProdutoVO();

							saldo.setCdproduto(rs.getInt("cdprodutoprincipal"));
							saldo.setCodigoproduto(rs.getString("codigo"));
							saldo.setCodigoerp(rs.getLong("codigoerp"));
							saldo.setDescricaoproduto(rs.getString("descricao"));
							saldo.setVolumes(rs.getLong("volumes"));
							saldo.setVolumesarmazenados(rs.getLong("volumesarmazenados"));
							saldo.setQtde(rs.getLong("saldo"));
							saldo.setAvaria(rs.getLong("avaria"));
							saldo.setVolumesdivergentes(rs.getLong("volumesdivergentes"));
							list.add(saldo);
						}

						return list;
					}

				});
		
	}
	
	/**
	 * Gera os dados logísticos para o volume para os outros depósitos diferentes do atual.
	 * 
	 * @author Giovane Freitas
	 * @param volume O volume para o qual deve ser gerado os dados logísticos.
	 * @param deposito O depósito atual onde está sendo feita a edição do produto.
	 */
	public void gerarDadosLogisticosOutrosDepositos(Produto volume, Deposito deposito) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("insert into Dadologistico (produto, deposito, normavolume, linhaseparacao, larguraexcedente, geracodigo) ");
		hql.append("select p, dlp.deposito, dlp.normavolume, dlp.linhaseparacao, dlp.larguraexcedente, dlp.geracodigo "); 
		hql.append("from Produto p ");
		hql.append("  join p.produtoprincipal pp ");
		hql.append("  join pp.listaDadoLogistico dlp with dlp.deposito.id <> ? ");
		hql.append("  left outer join p.listaDadoLogistico dl with dl.deposito.id <> ? ");
		hql.append("where p.cdproduto = ? ");
		hql.append("  and dl.cddadologistico is null ");
		
		getHibernateTemplate().bulkUpdate(hql.toString(), new Object[]{deposito.getCddeposito(), deposito.getCddeposito(), volume.getCdproduto()});
	}

	/**
	 * Consulta o Banco de Dados e cria uma lista de {@link ProdutosaldoVO} contendo o 
	 * estoque de cada produto de cada depósito ou apenas dos depósitos que forem 
	 * especificados no filtro.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProdutosaldoVO> findProdutosaldo(RelatorioestoqueFiltro filtro) {
		String sql;
		if (filtro.getTiporelatorio() != null && 
				RelatorioestoqueFiltro.TIPO_SINTETICO.getId().toString().equals(filtro.getTiporelatorio().getId())){
			sql = " select d.cddeposito, d.nome, sum(v.qtde) as qtde, '' as codigo, '' as descricao " + 
				" from vprodutosaldo v " +
			  	"	inner join deposito d on d.cddeposito = v.cddeposito ";
			
			if (filtro.getDeposito() != null && filtro.getDeposito().getCddeposito() != null)
				sql += " where v.cddeposito = " + filtro.getDeposito().getCddeposito() + " ";
			else{
				sql += " inner join usuariodeposito ud on ud.cddeposito = v.cddeposito" +
						"	where ud.cdpessoa = " + WmsUtil.getUsuarioLogado().getCdpessoa();
			}
			
			sql += " group by d.cddeposito, d.nome " +
			  	" order by d.nome ";
		}else{
			sql = " select p.codigo, p.descricao, d.cddeposito, d.nome, v.qtde " + 
				" from vprodutosaldo v " +
			  	"	inner join deposito d on d.cddeposito = v.cddeposito " +
			  	"	inner join produto p on p.cdproduto = v.cdproduto ";

			if (filtro.getDeposito() != null && filtro.getDeposito().getCddeposito() != null)
					sql += " where v.cddeposito = " + filtro.getDeposito().getCddeposito() + " and ";
			else{
				sql += " inner join usuariodeposito ud on ud.cddeposito = v.cddeposito" +
						"	where ud.cdpessoa = " + WmsUtil.getUsuarioLogado().getCdpessoa() + " and ";
			}
			  	
			sql += "	v.qtde <> 0 ";
			if (filtro.getOrdenacao() != null && filtro.getOrdenacao().equals(Ordenacao.CODIGO))
				sql += " order by d.nome, p.codigo";
			else
				sql += " order by d.nome, p.descricao";
		}
		
		Object queryResult = getJdbcTemplate().query(sql, new ResultSetExtractor(){

			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {

				List<ProdutosaldoVO> resultado = new ArrayList<ProdutosaldoVO>();
		
				while (rs.next()){
					ProdutosaldoVO vo = new ProdutosaldoVO();
					vo.setCddeposito(rs.getLong("cddeposito"));
					vo.setNomedeposito(rs.getString("nome"));
					vo.setCodigoproduto(rs.getString("codigo"));
					vo.setDescricaoproduto(rs.getString("descricao"));
					vo.setQtde(rs.getLong("qtde"));
					vo.setValorunitario(new Money(0));
					vo.setValortotal(new Money(0));
					
					resultado.add(vo);
				}
				
				return resultado;
			}
			
		});
		
		return (List<ProdutosaldoVO>) queryResult;
	}

	/**
	 * Grava a linha de separação padrão para os produtos que ainda não possuem uma linha definida.
	 * 
	 * @author Giovane Freitas
	 * @param deposito 
	 * @param linhaseparacaoPadrao
	 */
	public void definirLinhaPadrao(final Deposito deposito, final Linhaseparacao linhaseparacao) {
		getJdbcTemplate().execute(new ConnectionCallback(){

			public Object doInConnection(Connection con) throws SQLException, DataAccessException {
				//Insere dados logísticos faltantes
				StringBuilder insertDadosFaltantes = new StringBuilder();
				insertDadosFaltantes.append("insert into dadologistico (cddadologistico, cdproduto, cddeposito, normavolume, "); 
				insertDadosFaltantes.append("cdlinhaseparacao, larguraexcedente, geracodigo) ");
				insertDadosFaltantes.append("select sq_dadologistico.nextval, p.cdproduto, ?, 0, ?, 0, 0 ");
				insertDadosFaltantes.append("from produto p ");
				insertDadosFaltantes.append("left join dadologistico dl on dl.cdproduto = p.cdproduto and dl.cddeposito = ? ");
				insertDadosFaltantes.append("where cdprodutoprincipal is null ");
				insertDadosFaltantes.append("and dl.cddadologistico is null ");
				 				
				//Insere os dados logísticos faltantes para os volumes
				StringBuilder insertDadosVolumes = new StringBuilder();
				insertDadosVolumes.append("insert into dadologistico (cddadologistico, cdproduto, cddeposito, normavolume, "); 
				insertDadosVolumes.append("cdlinhaseparacao, larguraexcedente, geracodigo) ");
				insertDadosVolumes.append("select sq_dadologistico.nextval, v.cdproduto, dlpp.cddeposito, 0, dlpp.cdlinhaseparacao, 0, 0 ");
				insertDadosVolumes.append("from produto pp ");
				insertDadosVolumes.append("inner join produto v on v.cdprodutoprincipal = pp.cdproduto ");
				insertDadosVolumes.append("inner join dadologistico dlpp on dlpp.cdproduto = pp.cdproduto and dlpp.cddeposito = ? ");
				insertDadosVolumes.append("left join dadologistico dlv on dlv.cdproduto = v.cdproduto and dlv.cddeposito = ? ");
				insertDadosVolumes.append("where dlv.cddadologistico is null ");

				//Atualiza os dados logísticos que não possuem linha de separação
				StringBuilder updateDadosLogisticos = new StringBuilder();
				updateDadosLogisticos.append("update dadologistico set cdlinhaseparacao = ? ");
				updateDadosLogisticos.append("where cddeposito = ? and cdlinhaseparacao is null "); 

				
				PreparedStatement stmtPP = null;
				PreparedStatement stmtV = null;
				PreparedStatement stmtUpdate = null;
				try{
					stmtPP = con.prepareStatement(insertDadosFaltantes.toString());
					stmtPP.setInt(1, deposito.getCddeposito());
					stmtPP.setInt(2, linhaseparacao.getCdlinhaseparacao());
					stmtPP.setInt(3, deposito.getCddeposito());
					int count = stmtPP.executeUpdate();
					System.out.println("Foram inseridos " + count + " dados logísticos faltantes para o depósito " + deposito.getCddeposito());

					stmtV = con.prepareStatement(insertDadosVolumes.toString());
					stmtV.setInt(1, deposito.getCddeposito());
					stmtV.setInt(2, deposito.getCddeposito());
					count = stmtV.executeUpdate();
					System.out.println("Foram inseridos " + count + " dados logísticos faltantes para os volumes do depósito " + deposito.getCddeposito());
					
					stmtUpdate = con.prepareStatement(updateDadosLogisticos.toString());
					stmtUpdate.setInt(1, linhaseparacao.getCdlinhaseparacao());
					stmtUpdate.setInt(2, deposito.getCddeposito());
					count = stmtUpdate.executeUpdate();
					System.out.println("Foram atualizados " + count + " dados logísticos que não possuíam linha de separação do depósito " + deposito.getCddeposito());
					
				}catch (SQLException e) {
					try {
						if (stmtPP != null)
							stmtPP.close();
						if (stmtV != null)
							stmtV.close();
						if (stmtUpdate != null)
							stmtUpdate.close();
					}catch (Exception e2) {
						//oculta erro ao fechar o statement
					}
						
					throw e;
				}
				return null;
			}
			
		});
		
	}

	
	/**
	 * Retorna o produto de acordo com o código
	 * @param codigo
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Produto getProdutoByCodigo(String codigo){
		return query()
			.select("produto.cdproduto, produto.codigo")
			.where("produto.codigo=?", codigo)
			.unique();
	}

	/**
	 * Método que busca produto pelo código
	 * 
	 * @param codigo
	 * @return
	 * @author Tomás Rabelo
	 */
	public Produto findProdutoByCodigo(String codigo){
		if(codigo == null || codigo.equals(""))
			throw new WmsException("Parametros inválidos");
		
		return query()
			.select("produto.cdproduto, produto.codigo")
			.where("upper(produto.codigo) = ?", codigo.toUpperCase())
			.unique();
	}

	/**
	 * Retorna o produto
	 * @param codigo
	 * @return
	 * @author Tomás Rabelo
	 */
	public Produto getProdutoDescriptionProperty(Produto produto){
		return query()
		.select("produto.cdproduto, produto.codigo, produto.descricao")
		.where("produto = ?", produto)
		.unique();
	}

	/**
	 * Método que busca as movimentações de entrada e saida do produto
	 * 
	 * @param filtro
	 * @return
	 * @author Tomás Rabelo
	 */
	@SuppressWarnings("unchecked")
	public List<Produto> findProdutoForMovimentacaoReport(RelatoriomovimentacaoFiltro filtro) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Deposito deposito = WmsUtil.getDeposito();
		
		String produto1 = filtro.getProduto() != null ? filtro.getProduto().getCdproduto().toString() : "p1.cdproduto";
		String dataDe = filtro.getDatade() != null ? "to_date('"+format.format(filtro.getDatade())+"')" : "null";
		String dataAte = filtro.getDataate() != null ? "to_date('"+format.format(filtro.getDataate())+"')" : "null";
		
		String sql = "SELECT DISTINCT(p1.cdproduto), p1.codigo, p1.descricao, PESOUNITARIO_PRODUTO(p1.cdproduto) as PESO, p1.cubagem, DL.LARGURAEXCEDENTE, "+
					 "trunc(BUSCAR_VALORMEDIOPRODUTO("+deposito.getCddeposito()+","+produto1+",null)*100) as VALORMEDIOPRODUTO, "+ 
					 "BUSCAR_SALDOPRODUTODATA("+deposito.getCddeposito()+","+produto1+","+dataDe+") as QTDEANTERIOR, "+ 
					 "BUSCAR_SALDOPRODUTODATA("+deposito.getCddeposito()+","+produto1+","+dataAte+") as QTDEFINAL, "+ 
					 "BUSCAR_SALDOPRODUTOENTRADADATA("+deposito.getCddeposito()+","+produto1+","+dataDe+","+dataAte+") as QTDEENTRADA, "+
					 "BUSCAR_SALDOPRODUTOSAIDADATA("+deposito.getCddeposito()+","+produto1+","+dataDe+","+dataAte+") as QTDESAIDA "+
					 
					 "FROM Produto p1 "+
					 "LEFT JOIN DADOLOGISTICO DL ON DL.CDPRODUTO = P1.CDPRODUTO "+
					 "LEFT JOIN PEDIDOVENDAPRODUTO PVP ON PVP.CDPRODUTO = P1.CDPRODUTO "+
					 "LEFT JOIN PEDIDOVENDA PV ON PV.CDPEDIDOVENDA = PVP.CDPEDIDOVENDA "+
					 "WHERE p1.cdproduto = "+produto1;
		if(filtro.getCliente() != null && filtro.getCliente().getCdpessoa() != null)
			sql += " AND PV.CDCLIENTE = "+filtro.getCliente().getCdpessoa();
		
		sql += " ORDER BY p1.cdproduto";
		List<Produto> list = getJdbcTemplate().query(
					sql,
					new RowMapper() {
						public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
							Produto produto = new Produto();
							produto.setCdproduto(rs.getInt("CDPRODUTO"));
							produto.setCodigo(rs.getString("CODIGO"));
							produto.setDescricao(rs.getString("DESCRICAO"));
							produto.setPeso(rs.getDouble("PESO"));
							produto.setLarguraexcedente(rs.getBoolean("LARGURAEXCEDENTE"));
							produto.setCubagem(rs.getDouble("CUBAGEM"));
							
							produto.setValormedio(new Money(rs.getLong("VALORMEDIOPRODUTO"), true));
							produto.setQtdeanterior(rs.getInt("QTDEANTERIOR"));
							produto.setQtdeentrada(rs.getInt("QTDEENTRADA"));
							produto.setQtdesaida(rs.getInt("QTDESAIDA"));
							produto.setQtdefinal(rs.getInt("QTDEFINAL"));
							return produto;
						}
					});
		
		return list;
	}

	/**
	 * Método que chama a function no banco que verifica qual foi o dia dentro de um periodo que houve o maior pico de pallete no deposito
	 * 
	 * @param deposito
	 * @param produto
	 * @param datade
	 * @param dateate
	 * @param qtdeanterior
	 * @return
	 * @auhtor Tomás Rabelo
	 */
	public Long buscaPicoPallete(Deposito deposito, Produto produto,Date datade, Date dateate, Integer qtdeanterior) {
		if(deposito == null || deposito.getCddeposito() == null || produto == null || produto.getCdproduto() == null || datade == null)
			throw new WmsException("Parametros inválidos para a function BUCAR_PICOPALLETE");
		
		return getJdbcTemplate().queryForLong("SELECT BUSCAR_PICOPALLETE("+deposito.getCddeposito()+","+produto.getCdproduto()+"," +
											  "TO_DATE('"+new SimpleDateFormat("dd/MM/yyyy").format(datade)+"'),"+
											  "TO_DATE('"+new SimpleDateFormat("dd/MM/yyyy").format(dateate)+"'),"+
											  qtdeanterior+") FROM DUAL");
	}

	@Override
	public List<Produto> findForAutocomplete(String q, String propertyMatch, String propertyLabel, Boolean matchOption) {
		return query()
			.select("produto.cdproduto, produto.codigo, produto.descricao, pp.cdproduto, pp.codigo, pp.descricao")
			.leftOuterJoin("produto.produtoprincipal pp")
			.openParentheses()
				.where("UPPER(produto.codigo) LIKE ? || '%'", q.toUpperCase())
				.or()
				.where("UPPER(produto.descricao) LIKE ? || '%'", q.toUpperCase())
			.closeParentheses()
			.orderBy("produto.descricao")
			.setMaxResults(300)//Valor atual no arquivo neo.autocomplete.js
			.list();
	}

	/**
	 * Busca as movimentações dos produtos em um determinado período para a montagem da classificação ABC.
	 * O período é filtrado baseado na data de finalização dos carregamentos.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ClassificacaoABC> findForClassificacaoABC(final ClassificacaoABCFiltro filtro) {
		if (filtro.getDatafinal() == null || filtro.getDatainicial() == null)
			throw new WmsException("Parametros inválidos.");
			
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.codigo as codigo, p.descricao as descricao, ");
		sql.append("	pp.codigo as codigoPrincipal, pp.descricao as descricaoPrincipal, ");
		
		if (filtro.getTipoAnalise().equals(TipoAnalise.GIRO)){
			sql.append("	NVL(GIRO_PRODUTO(?, p.cdproduto, ?, ?), 0) as valor ");
			args.add(WmsUtil.getDeposito().getCddeposito());
			args.add(filtro.getDatainicial());
			args.add(filtro.getDatafinal());
		} else if (filtro.getTipoAnalise().equals(TipoAnalise.VENDA_QTDE)){
			sql.append("	NVL(BUSCAR_SALDOPRODUTOSAIDADATA(?, p.cdproduto, ?, ?), 0) as valor ");
			args.add(WmsUtil.getDeposito().getCddeposito());
			args.add(filtro.getDatainicial());
			args.add(filtro.getDatafinal());
		} else if (filtro.getTipoAnalise().equals(TipoAnalise.VENDA_VALOR)){
			sql.append("	NVL(BUSCAR_SALDOPRODUTOSAIDADATA(?, p.cdproduto, ?, ?), 0) * ");
			sql.append("    NVL(BUSCAR_VALORMEDIOPRODUTOSAIDA(?, p.cdproduto, ?, ?), 0) as valor ");
			
			//para o saldo
			args.add(WmsUtil.getDeposito().getCddeposito());
			args.add(filtro.getDatainicial());
			args.add(filtro.getDatafinal());

			//para o valor médio
			args.add(WmsUtil.getDeposito().getCddeposito());
			args.add(filtro.getDatainicial());
			args.add(filtro.getDatafinal());
		} else {
			java.util.Date date = new java.util.Date((filtro.getDatainicial().getTime() + filtro.getDatafinal().getTime()) / 2);
			date = WmsUtil.dateToBeginOfDay(date);
			
			if (filtro.getTipoAnalise().equals(TipoAnalise.ESTOQUE_QTDE)){
				sql.append("	NVL(BUSCAR_SALDOPRODUTODATA(?, p.cdproduto, ? ), 0) as valor ");
				args.add(WmsUtil.getDeposito().getCddeposito());
				args.add(date);
			} else if (filtro.getTipoAnalise().equals(TipoAnalise.ESTOQUE_VALOR)){
				sql.append("	NVL(BUSCAR_SALDOPRODUTODATA(?, p.cdproduto, ?), 0) * ");
				sql.append("    NVL(BUSCAR_VALORMEDIOPRODUTOSAIDA(?, p.cdproduto, ?, ?), 0) as valor ");
				
				//para o saldo
				args.add(WmsUtil.getDeposito().getCddeposito());
				args.add(date);
				
				//para o valor médio
				args.add(WmsUtil.getDeposito().getCddeposito());
				args.add(filtro.getDatainicial());
				args.add(filtro.getDatafinal());
			}
		}
		
		sql.append("FROM Produto p ");
		sql.append("	LEFT JOIN Produto pp on pp.cdproduto = p.cdprodutoprincipal ");
		
		if (filtro.getLinhaseparacao() != null && filtro.getLinhaseparacao().getCdlinhaseparacao() != null){
			sql.append("	LEFT JOIN Dadologistico dl on p.cdproduto = dl.cdproduto and dl.cddeposito = ? ");
			sql.append("WHERE dl.cdlinhaseparacao = ? ");
			args.add(WmsUtil.getDeposito().getCddeposito());
			args.add(filtro.getLinhaseparacao().getCdlinhaseparacao());
		}
		
		//sql.append("GROUP BY pp.codigo, pp.descricao, p.codigo, p.descricao ");
		sql.append("ORDER BY 5 DESC");

		return getJdbcTemplate().query(sql.toString(), args.toArray() , new RowMapper(){

			@Override
			public Object mapRow(ResultSet rs, int row) throws SQLException {
				ClassificacaoABC item = new ClassificacaoABC();
				item.setCodigo(rs.getString("codigo"));
				item.setDescricao(rs.getString("descricao"));
				item.setCodigoPrincipal(rs.getString("codigoPrincipal"));
				item.setDescricaoPrincipal(rs.getString("descricaoPrincipal"));
				item.setValor(rs.getDouble("valor"));
				return item;
			}
			
		});
		
	}
	
	/**
	 * Lista os produtos que estão incluídos em uma determinada ordem de serviço.
	 * 
	 * @param recebimento
	 * @return
	 * @author Giovane Freitas
	 * 
	 */
	public List<Produto> findByOrdemservico(Ordemservico ordemservico) {
		if((ordemservico == null) || (ordemservico.getCdordemservico() == null))
			throw new WmsException("A ordem de serviço não deve ser nula.");
		
		return query()
			.select("produto.cdproduto, produto.descricao,produto.codigo , pp.cdproduto, pp.codigo, pp.descricao")
			.join("produto.listaOrdemServicoProduto osp")
			.join("osp.listaOrdemprodutoLigacao opl")
			.leftOuterJoin("produto.produtoprincipal pp")
			.where("opl.ordemservico = ?", ordemservico)
			.orderBy("pp.descricao, produto.descricao")
			.list();
	}
	
	/**
	 * Busca o valor médio de um determinado produto em uma determinada data. Se
	 * a data for <code>null</code> será considerada a data atual.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param produto
	 * @param data
	 * @return
	 */
	public double getValorMedioEntrada(Deposito deposito, Produto produto, Date data) {
		if (deposito == null || produto == null)
			throw new WmsException("Parâmetro inválido.");

		String sql = "select BUSCAR_VALORMEDIOPRODUTO (?, ?, ?) from dual ";
		Object[] args = { deposito.getCddeposito(), produto.getCdproduto(), WmsUtil.dataToEndOfDay(data) };

		return (Double) getJdbcTemplate().queryForObject(sql, args, Double.class);
	}

	/**
	 * Busca o saldo de estoque para o relatório de estoque sintético.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SaldoProdutoVO> findSaldoProdutoSintetico(RelatorioestoqueFiltro filtro) {
		if (filtro == null) {
			throw new WmsException("Parâmetro inválido.");
		}
		
		//se a data for maior ou igual a hoje vou setar pra null para otimizar o desempenho
		java.util.Date hoje = new Date(Calendar.getInstance().getTimeInMillis());
		hoje = DateUtil.limpaMinSegHora(hoje);
		if (hoje.compareTo(filtro.getDatafinal()) <= 0)
			filtro.setDatafinal(null);

		List<Object> args = new ArrayList<Object>();
		
		StringBuilder select = new StringBuilder();
		select.append("");

		if (filtro.getDatafinal() != null){
			java.util.Date beginOfDay = WmsUtil.dateToBeginOfDay(filtro.getDatafinal());

			select.append("select d.cddeposito, d.nome, ");
			select.append("  SUM(BUSCAR_SALDOPRODUTODATA(d.cddeposito, p.cdproduto, ?)) as saldo, ");
			args.add(beginOfDay);
			select.append("  SUM(BUSCAR_SALDOPRODUTODATA(d.cddeposito, p.cdproduto, ?) * PESOUNITARIO_PRODUTO(p.CDPRODUTO)) as peso, ");
			args.add(beginOfDay);
			select.append("  SUM(BUSCAR_SALDOPRODUTODATA(d.cddeposito, p.cdproduto, ?) * trunc(BUSCAR_VALORMEDIOPRODUTO(d.CDDEPOSITO, p.CDPRODUTO, NULL)*100)) as valor ");
			args.add(beginOfDay);
			select.append("from produto p ");
			select.append("  join dadologistico dl on dl.cdproduto = p.cdproduto ");
			select.append("  join deposito d on dl.cddeposito = d.cddeposito ");

			if (filtro.getDeposito() != null){
				select.append("where d.cddeposito = ? ");
				args.add(filtro.getDeposito().getCddeposito());
			} else if (!WmsUtil.isUsuarioLogadoAdministrador()){
				select.append("where d.cddeposito in (");
				
				Iterator<Usuariodeposito> iterator = UsuariodepositoService.getInstance().findByUser(WmsUtil.getUsuarioLogado()).iterator();
				while(iterator.hasNext()){
					select.append(iterator.next().getDeposito().getCddeposito());
					if (iterator.hasNext())
						select.append(", ");
				}
				
				select.append(") "); 
			}

			select.append("group by d.cddeposito, d.nome ");
			select.append("order by d.nome ");
		}else{
			select.append("select d.cddeposito, d.nome, sum(ep.qtde) as saldo, ");
			select.append("  SUM(ep.qtde * PESOUNITARIO_PRODUTO(EP.CDPRODUTO)) as peso, ");
			select.append("  SUM(ep.qtde * trunc(BUSCAR_VALORMEDIOPRODUTO(A.CDDEPOSITO, EP.CDPRODUTO, NULL)*100)) as valor ");
			select.append("from enderecoproduto ep ");
			select.append("  join endereco e on e.cdendereco = ep.cdendereco "); 
			select.append("  join area a on a.cdarea = e.cdarea ");
			select.append("  join deposito d on d.cddeposito = a.cddeposito ");
			select.append("  join produto p on p.cdproduto = ep.cdproduto ");

			if (filtro.getDeposito() != null){
				select.append("where d.cddeposito = ? ");
				args.add(filtro.getDeposito().getCddeposito());
			} else if (!WmsUtil.isUsuarioLogadoAdministrador()){
				select.append("where d.cddeposito in (");
				
				Iterator<Usuariodeposito> iterator = UsuariodepositoService.getInstance().findByUser(WmsUtil.getUsuarioLogado()).iterator();
				while(iterator.hasNext()){
					select.append(iterator.next().getDeposito().getCddeposito());
					if (iterator.hasNext())
						select.append(", ");
				}
				
				select.append(") "); 
			}

			select.append("group by d.cddeposito, d.nome ");
			select.append("order by d.nome ");
		}


		return (List<SaldoProdutoVO>) getJdbcTemplate().query(select.toString(), args.toArray(), new ResultSetExtractor() {

					public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

						List<SaldoProdutoVO> list = new ArrayList<SaldoProdutoVO>();

						while (rs.next()) {
							SaldoProdutoVO saldo = new SaldoProdutoVO();
							saldo.setCddeposito(rs.getInt("cddeposito"));
							saldo.setNomedeposito(rs.getString("nome"));
							saldo.setQtde(rs.getLong("saldo"));
							saldo.setValortotal(new Money(rs.getDouble("valor"), true));							
							saldo.setPesototal(rs.getDouble("peso"));
							list.add(saldo);
						}

						return list;
					}

				});
		

	}

	/**
	 * Busca o saldo de estoque para o relatório de estoque analítico.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SaldoProdutoVO> findSaldoProdutoAnalitico(final RelatorioestoqueFiltro filtro) {
		if (filtro == null) {
			throw new WmsException("Parâmetro inválido.");
		}
		
		//se a data for maior ou igual a hoje vou setar pra null para otimizar o desempenho
		java.util.Date hoje = new Date(Calendar.getInstance().getTimeInMillis());
		hoje = DateUtil.limpaMinSegHora(hoje);
		if (hoje.compareTo(filtro.getDatafinal()) <= 0)
			filtro.setDatafinal(null);

		List<Object> args = new ArrayList<Object>();
		
		StringBuilder select = new StringBuilder();
		select.append("");
		if (filtro.getDatafinal() != null){
			
			if (filtro.getDeposito() != null){
				filtro.setDeposito(DepositoService.getInstance().load(filtro.getDeposito()));
				
				select.append("select p.codigo, p.descricao, ");
				select.append("  SUM(BUSCAR_SALDOPRODUTODATA(?, p.cdproduto, ?)) as saldo, ");
				args.add(filtro.getDeposito().getCddeposito());
				args.add(WmsUtil.dateToBeginOfDay(filtro.getDatafinal()));
				select.append("  PESOUNITARIO_PRODUTO(p.cdproduto) as peso, ");
				select.append("  BUSCAR_VALORMEDIOPRODUTO(?, p.cdproduto, NULL) as valor ");
				args.add(filtro.getDeposito().getCddeposito());
				select.append("from produto p ");
			}else{
				select.append("select d.cddeposito, d.nome as deposito, p.codigo, p.descricao, ");
				select.append("  SUM(BUSCAR_SALDOPRODUTODATA(d.cddeposito, p.cdproduto, ?)) as saldo, ");
				args.add(WmsUtil.dateToBeginOfDay(filtro.getDatafinal()));
				select.append("  PESOUNITARIO_PRODUTO(p.cdproduto) as peso, ");
				select.append("  BUSCAR_VALORMEDIOPRODUTO(d.cddeposito, p.cdproduto, NULL) as valor ");
				select.append("from produto p ");
				select.append("  join dadologistico dl on dl.cdproduto = p.cdproduto ");
				select.append("  join deposito d on dl.cddeposito = d.cddeposito ");
				
			}
			
			if (filtro.getDeposito() == null && !WmsUtil.isUsuarioLogadoAdministrador()){
				select.append(" where d.cddeposito in ( ");
				
				Iterator<Usuariodeposito> iterator = UsuariodepositoService.getInstance().findByUser(WmsUtil.getUsuarioLogado()).iterator();
				while(iterator.hasNext()){
					select.append(iterator.next().getDeposito().getCddeposito());
					if (iterator.hasNext())
						select.append(", ");
				}
				
				select.append(") "); 
			}
			
			if (filtro.getDeposito() != null){
				select.append(" group by p.cdproduto, p.codigo, p.descricao ");
				select.append(" order by P.").append(filtro.getOrdenacao().name());				
			}else{
				select.append(" group by d.cddeposito, d.nome, p.cdproduto, p.codigo, p.descricao ");
				select.append(" order by d.nome, d.cddeposito, ");
				select.append(" P.").append(filtro.getOrdenacao().name());
			}
		}else{
			select.append(" select d.cddeposito, d.nome as deposito, p.codigo, p.descricao, sum(ep.qtde) as saldo , ");
			select.append("  PESOUNITARIO_PRODUTO(P.CDPRODUTO) as peso, ");
			select.append("  BUSCAR_VALORMEDIOPRODUTO(D.CDDEPOSITO, P.CDPRODUTO, NULL) as valor ");
			select.append(" from enderecoproduto ep  ");
			select.append("  join endereco e on e.cdendereco = ep.cdendereco "); 
			select.append("  join area a on a.cdarea = e.cdarea ");
			select.append("  join deposito d on d.cddeposito = a.cddeposito ");
			select.append("  join produto p on p.cdproduto = ep.cdproduto ");
			
			if (filtro.getDeposito() != null){
				select.append(" where d.cddeposito = ? ");
				args.add(filtro.getDeposito().getCddeposito());
			} else if (!WmsUtil.isUsuarioLogadoAdministrador()){
				select.append(" where d.cddeposito in ( ");
				
				Iterator<Usuariodeposito> iterator = UsuariodepositoService.getInstance().findByUser(WmsUtil.getUsuarioLogado()).iterator();
				while(iterator.hasNext()){
					select.append(iterator.next().getDeposito().getCddeposito());
					if (iterator.hasNext())
						select.append(", ");
				}
				
				select.append(") "); 
			}
			
			select.append(" group by d.cddeposito, d.nome, p.cdproduto, p.codigo, p.descricao ");
			select.append(" order by d.nome, d.cddeposito, ");
			select.append(" P.").append(filtro.getOrdenacao().name());
		}

		return (List<SaldoProdutoVO>) getJdbcTemplate().query(select.toString(), args.toArray(), new ResultSetExtractor() {

					public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

						List<SaldoProdutoVO> list = new ArrayList<SaldoProdutoVO>();

						while (rs.next()) {
							SaldoProdutoVO saldo = new SaldoProdutoVO();
							saldo.setCodigoproduto(rs.getString("codigo"));
							saldo.setDescricaoproduto(rs.getString("descricao"));
							
							if (filtro.getDatafinal() != null && filtro.getDeposito() != null){
								saldo.setCddeposito(filtro.getDeposito().getCddeposito());
								saldo.setNomedeposito(filtro.getDeposito().getNome());																
							}else{
								saldo.setCddeposito(rs.getInt("cddeposito"));
								saldo.setNomedeposito(rs.getString("deposito"));								
							}
							
							saldo.setQtde(rs.getLong("saldo"));
							
							long valor = (long)(rs.getDouble("valor") * 100);
							saldo.setValorunitario(new Money(valor, true));
							saldo.setValortotal(new Money(rs.getLong("saldo") * valor, true ));
							
							saldo.setPeso(rs.getDouble("peso"));
							double qtde = (double)rs.getLong("saldo");
							double peso = (double)rs.getDouble("peso");
							saldo.setPesototal(qtde * peso);
							
							list.add(saldo);
						}

						return list;
					}

				});
	}
	
	public Produto findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		.select("produto.cdproduto, produto.codigoerp, produto.codigo ")
		.where("produto.codigoerp = ? ", codigoERP)
		.unique();
	}

	/**
	 * Carrega um produto pelo seu código de barras
	 * @param codigo
	 * @return
	 */
	public Produto loadByCodigoBarras(String codigo) {
		return query()
		.where("produto.codigo = ?", codigo)
		.unique();
	}

	/**
	 * Método que busca os volumes dos produtos por cdproduto, não alterar o select e os joins para não impactar na geração
	 * de etiquetas de código de barra automatica.
	 * @since 15/09/2011
	 * @param cdproduto
	 * @return lista de Produtos
	 * @author Filipe Santos
	 */
	public List<Produto> findVolumesByCdProduto(List<Produto> listaProduto){
		
		if(listaProduto == null || listaProduto.isEmpty())
			throw new WmsException("Parametros inválidos");
		
		return query()
		.select("produto.cdproduto,produto.descricao,produto.codigo,produto.complementocodigobarras,"+
		 		"listaProdutoCodigoDeBarras.cdprodutocodigobarras,listaProdutoCodigoDeBarras.codigo," +
		 		"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao")
		.leftOuterJoin("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
		.leftOuterJoin("produto.produtoprincipal produtoprincipal")
		.whereIn("produtoprincipal.cdproduto", CollectionsUtil.listAndConcatenate(listaProduto,"cdproduto", ","))
		.list();
		
		/** Consulta antiga **/
//		return query()
//					.select("produto.cdproduto,produto.descricao,produto.codigo,produto.complementocodigobarras,"+
//					 		"listaProdutoCodigoDeBarras.cdprodutocodigobarras,listaProdutoCodigoDeBarras.codigo," +
//					 		"listaDadoLogistico.normavolume," +
//					 		"listaOrdemServicoProduto.cdordemservicoproduto, listaOrdemServicoProduto.qtdeesperada,"  +
//					 		"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao")
//					.leftOuterJoin("produto.listaDadoLogistico listaDadoLogistico")
//					.leftOuterJoin("produto.listaProdutoCodigoDeBarras listaProdutoCodigoDeBarras")
//					.leftOuterJoin("produto.produtoprincipal produtoprincipal")
//					.leftOuterJoin("produtoprincipal.listaDadoLogistico  dadologisticoPP")
//					.leftOuterJoin("produto.listaOrdemServicoProduto listaOrdemServicoProduto")							
//					.whereIn("produtoprincipal.cdproduto", CollectionsUtil.listAndConcatenate(listaProduto,"cdproduto", ","))
//					.list();
	}
	
	/**
	 * Retorna o produto com as listas de Dados Logistico e Lista de Ordem de Serviço, não alterar o select e os joins para não impactar na geração
	 * de etiquetas de código de barra automatica.
	 * @param cdproduto
	 * @return Produto
	 * @author Filipe Santos
	 * @since 20/09/2011
	 */
	public Produto findByCdproduto(Integer cdproduto){
		if(cdproduto == null )
			throw new WmsException("Parametros inválidos");
		
		return query()
					.select("produto.cdproduto,produto.descricao,produto.codigo,produto.complementocodigobarras,"+
					 		"listaDadoLogistico.normavolume," +
					 		"listaOrdemServicoProduto.cdordemservicoproduto, listaOrdemServicoProduto.qtdeesperada")
					.leftOuterJoin("produto.listaDadoLogistico listaDadoLogistico")
					.leftOuterJoin("produto.produtoprincipal produtoprincipal")
					.leftOuterJoin("produtoprincipal.listaDadoLogistico  dadologisticoPP")
					.leftOuterJoin("produto.listaOrdemServicoProduto listaOrdemServicoProduto")		
			.where("produto.cdproduto = ?",cdproduto)
			.unique();
	}
	
	/**
	 * Retorna a lista de produtos vinculados ao recebimento, não alterar o select e os joins para não impactar na geração
	 * de etiquetas de código de barra automatica. 
	 * @return Lista de Produtos
	 * @author Desenvolvimento
	 * @since 21/09/2011
	 */
	public List<Produto> findByRecebimento(Recebimento recebimento){
		if(recebimento == null || recebimento.getCdrecebimento()==null)
			throw new WmsException("Parametros inválidos");
		
		return query()
			 .select("produto.cdproduto,produto.descricao,produto.codigo,produto.complementocodigobarras,"+
			 		 "listaDadoLogistico.normavolume," +
			 		 "listaOrdemServicoProduto.cdordemservicoproduto, listaOrdemServicoProduto.qtdeesperada")
			 .join("produto.listaDadoLogistico listaDadoLogistico")					 		
			 .join("produto.listaOrdemServicoProduto listaOrdemServicoProduto")			 
			 .join("listaOrdemServicoProduto.listaOrdemprodutoLigacao listaOrdemprodutoLigacao")					
			 .join("listaOrdemprodutoLigacao.ordemservico ordemservico")
			 .where("listaDadoLogistico.geracodigo = 1")
			 .where("ordemservico.recebimento = ?",recebimento)
			 .where("ordemservico.ordem = 1")
			 .where("not exists (select produtocodigobarras.codigo from Produtocodigobarras produtocodigobarras " +					 
			 		"where produtocodigobarras.produto = produto " +
			 		"and produtocodigobarras.interno = 1)")
			 .list();					
	}	
	
	/**
	 * @author Filipe Santos
	 * @since 12/01/2012
	 * @param deposito
	 * @return quantidade de produtos no deposito
	 */
	public Integer countProdutosByDeposito (Deposito deposito){
		if(deposito == null || deposito.getCddeposito()==null)
			throw new WmsException("Parametros inválidos");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select count(p.cdproduto) as total from produto p ");
		sql.append(" join dadologistico dl on dl.cdproduto = p.cdproduto ");
		sql.append(" join deposito d on d.cddeposito = dl.cddeposito ");
		sql.append(" where d.cddeposito = ? ");		
		sql.append(" and p.cdprodutoprincipal is null ");
		
		Object[] args = {deposito.getCddeposito()};

		return (Integer) getJdbcTemplate().queryForObject(sql.toString(), args, Integer.class);
	}
	
}