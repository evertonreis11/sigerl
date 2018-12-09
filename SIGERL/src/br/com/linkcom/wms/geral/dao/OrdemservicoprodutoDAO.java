package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.ItemOrdemServico;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.OrdemservicoprodutoVO;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.modulo.recebimento.controller.report.filtro.EmitirDivergenciaRecebimentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class OrdemservicoprodutoDAO extends GenericDAO<Ordemservicoproduto> {

	@Override
	public void updateListagemQuery(QueryBuilder<Ordemservicoproduto> query, FiltroListagem _filtro) {
		query.leftOuterJoinFetch("ordemservicoproduto.ordemservico ordemservico");
		query.leftOuterJoinFetch("ordemservicoproduto.produto produto");
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Ordemservicoproduto> query) {
		query.leftOuterJoinFetch("ordemservicoproduto.listaOrdemservicoprodutoendereco ospe")
			.leftOuterJoinFetch("ospe.enderecodestino enderecodestino")
			.leftOuterJoinFetch("enderecodestino.area area");
	}
	
	@Override
	public Ordemservicoproduto load(Ordemservicoproduto bean) {
		if(bean == null){
			return null;
		}
		
		return query()
			.joinFetch("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
			.entity(bean)
			.unique();
	}

	/**
	 * Encontra a ordem de serviço a partir do recebimento e lista todos os seus
	 * produtos. 
	 *  
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Ordemservicoproduto> findBy(Recebimento recebimento,Ordemservico ordemservico) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
			.select("produto.cdproduto, produto.codigoerp, produto.descricao, " +
						"produto.codigo," +
						"ordemservicoproduto.cdordemservicoproduto, ordemservico.cdordemservico, ordemservicoproduto.qtdeesperada, " +
						"listaOrdemprodutohistorico.cdordemprodutohistorico,listaOrdemprodutohistorico.qtde, listaOrdemprodutohistorico.qtdeavaria," +
						"listaProdutoTipoPalete.cdprodutotipopalete," +
						"listaProdutoTipoPalete.lastro, listaProdutoTipoPalete.camada, tipopalete.cdtipopalete, tipopalete.nome, codigobarras.codigo")
				.leftOuterJoin("ordemservicoproduto.produto produto")
				.leftOuterJoin("ordemservicoproduto.listaOrdemprodutoLigacao listaOrdemprodutoLigacao")
				.leftOuterJoin("listaOrdemprodutoLigacao.ordemservico ordemservico")
				.leftOuterJoin("ordemservicoproduto.listaOrdemprodutohistorico listaOrdemprodutohistorico")
				.leftOuterJoin("ordemservico.recebimento recebimento")
				.leftOuterJoin("recebimento.recebimentostatus recebimentostatus")
				.leftOuterJoin("produto.listaProdutoTipoPalete listaProdutoTipoPalete")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras codigobarras")
				.leftOuterJoin("listaProdutoTipoPalete.tipopalete tipopalete")
				.where("recebimento=?",recebimento)
				.where("recebimentostatus=?",Recebimentostatus.EM_ANDAMENTO)
				.where("ordemservico=?",ordemservico)
				.openParentheses()
					.where("listaOrdemprodutohistorico.ordemservico is null")
					.or()
					.where("listaOrdemprodutohistorico.ordemservico=?",ordemservico)
				.closeParentheses()
				.orderBy("produto.descricao")
				.list();
	}
	
	/**
	 * Seta como zero todos as quantidades da ordem de serviço especificada.
	 * 
	 * @param ordemservicoproduto
	 * @author Pedro Gonçalves
	 */
	public void resetarQuantidades(Ordemservico ordemservico){
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Ordemprodutohistorico ordemprodutohistorico set ordemprodutohistorico.qtde=?, " +
										  "ordemprodutohistorico.qtdeavaria=?, ordemprodutohistorico.qtdefracionada=? where ordemprodutohistorico.ordemservico.id = ?",
										  new Object[]{0l,0l,0l,ordemservico.getCdordemservico()});
	}
	
	/**
	 * Atualiza o status da ordem de servico do produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservicoproduto
	 */
	public void atualizarStatus(Ordemservicoproduto ordemservicoproduto){
		if(ordemservicoproduto == null || ordemservicoproduto.getCdordemservicoproduto() == null || 
				ordemservicoproduto.getOrdemprodutostatus() == null || ordemservicoproduto.getOrdemprodutostatus().getCdordemprodutostatus() == null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		getHibernateTemplate().bulkUpdate("update Ordemservicoproduto ordemservicoproduto set ordemservicoproduto.ordemprodutostatus = ? " +
				"where ordemservicoproduto.id = ?",new Object[]{ordemservicoproduto.getOrdemprodutostatus(),ordemservicoproduto.getCdordemservicoproduto()});
	}
	
	/**
	 * Atualiza o status de todas as ordemservicoproduto da ordem de serviço em questão.
	 * 
	 * @param ordemservico
	 * @param ordemprodutostatus
	 * @author Pedro Gonçalves
	 */
	public void atualizarStatus(Ordemservico ordemservico, Ordemprodutostatus ordemprodutostatus){
		if(ordemservico == null || ordemservico.getCdordemservico() == null || 
				ordemprodutostatus == null || ordemprodutostatus.getCdordemprodutostatus() == null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		getHibernateTemplate().bulkUpdate("update Ordemservicoproduto ordemservicoproduto set ordemservicoproduto.ordemprodutostatus.id = ? " +
				"where ordemservicoproduto.id in (select opl.ordemservicoproduto.id from Ordemprodutoligacao opl where opl.ordemservico.id = ? )",
				new Object[]{ordemprodutostatus.getCdordemprodutostatus(),ordemservico.getCdordemservico()});
	}

	/* singleton */
	private static OrdemservicoprodutoDAO instance;
	public static OrdemservicoprodutoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemservicoprodutoDAO.class);
		}
		return instance;
	}
	
	/**
	 * 
	 * Busca os dados necessários para a criação do relatório
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @return
	 * 
	 */
	public List<Ordemservicoproduto> findForReport(EmitirDivergenciaRecebimentoFiltro filtro){
		if(filtro == null){
			throw new WmsException("O recebimento não deve ser nulo");
		}
		return query().select("ordemservicoproduto.cdordemservicoproduto,ordemservicoproduto.qtdeesperada," +
				"ordemprodutostatus.cdordemprodutostatus,produto.cdproduto,produto.codigo,produto.descricao," +
				"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao")
					.join("ordemservicoproduto.listaOrdemprodutoLigacao listaOrdemprodutoLigacao")
					.join("listaOrdemprodutoLigacao.ordemservico ordemservico")
					.join("ordemservicoproduto.listaOrdemprodutohistorico listaOrdemprodutohistorico")
					.join("ordemservicoproduto.produto produto")
					.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
					.leftOuterJoin("produto.produtoprincipal produtoprincipal")
					.leftOuterJoin("ordemservico.recebimento recebimento")
					.leftOuterJoin("produto.listaProdutoEmbalagem produtoembalagem")
					.join("recebimento.recebimentostatus recebimentostatus")
					.join("recebimento.listaRecebimentoNF listaRecebimentoNF")
					.join("listaRecebimentoNF.notafiscalentrada notafiscalentrada")
					.join("notafiscalentrada.fornecedor fornecedor")
					.where("recebimento.id = ?", filtro.getRecebimento())
					.orderBy("ordemservico.recebimento, produto.codigo")
					.list();
	}
	
	/**
	 * 
	 * Método que recupera uma lista de ordens de serviço produto pela ordem de serviço fornecida
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return List<Ordemservicoproduto>
	 * 
	 */
	public List<Ordemservicoproduto> findForVisualizarDetalhes(Ordemservico ordemservico, boolean conferenciaBox) {
		if((ordemservico == null) || (ordemservico.getCdordemservico() == null) || 
		   (ordemservico.getOrdemtipo() == null) || (ordemservico.getOrdemtipo().getCdordemtipo() == null))
				throw new WmsException("Parâmetros inválidos");
		
		QueryBuilder<Ordemservicoproduto> query =		
				query()
					.select("produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao," +
							"produtoprincipal.qtdevolumes," +
							"produto.cdproduto, produto.codigo, produto.descricao, produto.complementocodigobarras," +
							"ordemservicoproduto.cdordemservicoproduto,cliente.nome, pedidovenda.numero, " +
							"pedidovendaproduto.cdpedidovendaproduto, pedidovendaproduto.qtde, " +
							"etiquetaexpedicao.cdetiquetaexpedicao,  carregamentoitem.qtdeconfirmada, etiquetaexpedicao.qtdecoletor, etiquetaexpedicao.qtdecoletororiginal," +
							"carregamentoitem.cdcarregamentoitem, ordemservicoproduto.qtdeesperada, " +
							"tipooperacao.cdtipooperacao,tipooperacao.nome," +
							"tipooperacao.imprimeetiqueta,tipooperacao.separacliente," +
							"carregamentoitemgrupo.cdcarregamentoitem,carregamentoitemgrupo.qtdeconfirmada, embalagemexpedicao.lacre," +
							"produtoembalagem.cdprodutoembalagem, produtoembalagem.qtde")		
					.join("ordemservicoproduto.listaEtiquetaexpedicao etiquetaexpedicao")
					.leftOuterJoin("etiquetaexpedicao.embalagemexpedicaoproduto embalagemexpedicaoproduto")
					.leftOuterJoin("embalagemexpedicaoproduto.embalagemexpedicao embalagemexpedicao")
					.leftOuterJoin("ordemservicoproduto.produtoembalagem produtoembalagem")
					.join("etiquetaexpedicao.carregamentoitem carregamentoitem")
					.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
					.join("pedidovendaproduto.pedidovenda pedidovenda")			
					.join("pedidovendaproduto.tipooperacao tipooperacao")			
					.join("pedidovenda.cliente cliente")
					.join("pedidovendaproduto.pessoaendereco pessoaendereco")
					.join("ordemservicoproduto.listaOrdemprodutoLigacao ordemprodutoLigacao")
					.join("ordemservicoproduto.produto produto")
					.leftOuterJoin("etiquetaexpedicao.listaEtiquetaexpedicaogrupo etiquetaexpedicaogrupo")
					.leftOuterJoin("etiquetaexpedicaogrupo.carregamentoitem carregamentoitemgrupo")
					.leftOuterJoin("produto.produtoprincipal produtoprincipal")
					
					.where("ordemprodutoLigacao.ordemservico = ?", ordemservico);
		
		if (conferenciaBox){
			query.where("etiquetaexpedicao.qtdecoletororiginal is not null")
				.where("etiquetaexpedicao.qtdecoletororiginal > 0");
		}

		return query
				.orderBy("etiquetaexpedicao.cdetiquetaexpedicao")					
		 		.list();
		
	}
	
	/**
	 * 
	 * Método que atualiza o status da ordem de serviço produto
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservicoproduto
	 * 
	 */
	public void updateStatusordemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		if((ordemservicoproduto == null) || (ordemservicoproduto.getCdordemservicoproduto() == null) || (ordemservicoproduto.getOrdemprodutostatus() == null) || (ordemservicoproduto.getOrdemprodutostatus().getCdordemprodutostatus() == null))
			throw new WmsException("Parâmetros inválidos");
		
		
		getHibernateTemplate().bulkUpdate("update Ordemservicoproduto ordemservicoproduto set ordemservicoproduto.ordemprodutostatus.id=? where ordemservicoproduto.id=? ", new Object[]{ordemservicoproduto.getOrdemprodutostatus().getCdordemprodutostatus(), ordemservicoproduto.getCdordemservicoproduto()});
	}
	
	/**
	 * 
	 * Método que atualiza o tipo palete da ordem servico produto
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param ordemservicoproduto
	 * 
	 */
	public void updateTipopaleteordemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		if((ordemservicoproduto == null) || (ordemservicoproduto.getCdordemservicoproduto() == null) || (ordemservicoproduto.getTipopalete() == null) || (ordemservicoproduto.getTipopalete().getCdtipopalete() == null))
			throw new WmsException("Parâmetros inválidos");
		
		
		getHibernateTemplate().bulkUpdate("update Ordemservicoproduto ordemservicoproduto set ordemservicoproduto.tipopalete.id=? where ordemservicoproduto.id=? ", new Object[]{ordemservicoproduto.getTipopalete().getCdtipopalete(), ordemservicoproduto.getCdordemservicoproduto()});
	}
	
	/**
	 * Busca as ordens de servico dos produtos 
	 * através da ordem de servico
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findByOrdemservico(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de servico não deve ser nula");
		return query()
					 .select("ordemservicoproduto.cdordemservicoproduto,ordemservicoproduto.qtdeesperada,produto.cdproduto,produto.descricao,produto.codigo," +
					 		"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao,ospe.cdordemservicoprodutoendereco," +
					 		"ospe.qtde,enderecodestino.cdendereco,enderecodestino.endereco,enderecodestino.rua,enderecodestino.predio," +
					 		"enderecodestino.nivel,enderecodestino.apto,area.cdarea,area.codigo,tipopalete.cdtipopalete,tipopalete.nome," +
					 		"enderecofuncao.cdenderecofuncao")
					 .join("ordemservicoproduto.produto produto")
					 .leftOuterJoin("ordemservicoproduto.tipopalete tipopalete")
					 .leftOuterJoin("produto.produtoprincipal produtoprincipal")
					 .leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ospe")
					 .leftOuterJoin("ospe.enderecodestino enderecodestino")
					 .leftOuterJoin("enderecodestino.area area")
					 .leftOuterJoin("enderecodestino.enderecofuncao enderecofuncao")
					 .join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
					 .join("opl.ordemservico os")
					 .where("os = ?",ordemservico)
					 .orderBy("produto.codigo, area.codigo, enderecodestino.endereco")
					 .list();
	}
	

	/**
	 * Busca os produtos das ordens do tipo e recebimento especificados
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @param ordemtipo
	 * @return
	 */
	public List<Ordemservicoproduto> loadByOrdemTipo(Recebimento recebimento,Ordemtipo ordemtipo, Usuario usuario) {
		if(recebimento == null || recebimento.getCdrecebimento() == null || ordemtipo == null || ordemtipo.getCdordemtipo() == null){
			throw new WmsException("O recebimento e a ordem não deve ser nulos.");
		}
		List<Ordemprodutostatus> listaStatus = new ArrayList<Ordemprodutostatus>();
		listaStatus.add(Ordemprodutostatus.NAO_CONCLUIDO);
		listaStatus.add(Ordemprodutostatus.EM_EXECUCAO);
		
		QueryBuilder<Ordemservicoproduto> query = query()
					.select("ordemservicoproduto.cdordemservicoproduto,ordemservicoproduto.qtdeesperada," +
								"produto.cdproduto,produto.descricao,os.cdordemservico,opl.cdordemprodutoligacao," +
								"produto.codigo,ospe.cdordemservicoprodutoendereco,ospe.qtde,ordemtipo.cdordemtipo," +
								"enderecodestino.cdendereco,enderecodestino.endereco,area.cdarea,area.codigo," +
								"enderecodestino.larguraexcedente,enderecofuncao.cdenderecofuncao," +
								"enderecodestino.rua,enderecodestino.predio,enderecodestino.nivel,enderecodestino.apto," +
								"osu.cdordemservicousuario,osu.dtinicio,osu.dtfim,osu.paletes,usuario.cdpessoa," +
								"pp.cdproduto,pp.descricao,pp.codigo," +
								"enderecoorigem.cdendereco,enderecoorigem.endereco,areaorigem.cdarea,areaorigem.codigo," +
								"enderecoorigem.larguraexcedente,funcaoorigem.cdenderecofuncao," +
								"umareserva.cdumareserva,enderecoproduto.cdenderecoproduto")
					.join("ordemservicoproduto.produto produto")
					.leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ospe")
					.leftOuterJoin("ospe.listaUmareserva umareserva")
					.leftOuterJoin("umareserva.enderecoproduto enderecoproduto")
					.join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
					.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
					.join("opl.ordemservico os")
					.leftOuterJoin("os.listaOrdemServicoUsuario osu")
					.leftOuterJoin("osu.usuario usuario")
					.join("os.recebimento recebimento")
					.join("os.ordemstatus ordemstatus")
					.leftOuterJoin("ospe.enderecodestino enderecodestino")
					.leftOuterJoin("ospe.enderecoorigem enderecoorigem")
					.leftOuterJoin("enderecodestino.enderecofuncao enderecofuncao")
					.leftOuterJoin("enderecoorigem.enderecofuncao funcaoorigem")
					.leftOuterJoin("enderecodestino.area area")
					.leftOuterJoin("enderecoorigem.area areaorigem")
					.leftOuterJoin("produto.produtoprincipal pp")
					.join("os.ordemtipo ordemtipo")
					.where("recebimento = ?",recebimento)
					.openParentheses()
						.where("ordemstatus = ?",Ordemstatus.EM_ABERTO)
						.or()
						.openParentheses()
							.where("ordemstatus = ?",Ordemstatus.EM_EXECUCAO)
							.where("usuario=?", usuario)
						.closeParentheses()
					.closeParentheses()
					.whereIn("ordemprodutostatus", CollectionsUtil.listAndConcatenate(listaStatus, "cdordemprodutostatus", ","))
					.where("ordemtipo = ?",ordemtipo)
					.orderBy("produto.descricao");
		
		return query.list();
	}
	
	/**
	 * Busca os dados da osp para que seja possível a execução da transferencia
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param linhaseparacao
	 * @param deposito
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findDadosOSPForTransferencia(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de serviço não deve ser nula.");
		
		List<Ordemservicoproduto> listaOSP = null;
		List<Object[]> lista =  newQueryBuilderWithFrom(Object[].class)
					.select("distinct ordemservicoproduto.cdordemservicoproduto,ordemservicoproduto.qtdeesperada,produto.cdproduto,produto.codigo,produto.descricao," +
							"ospe.cdordemservicoprodutoendereco,enderecodestino.cdendereco,enderecodestino.endereco," +
							"areadestino.cdarea,areadestino.codigo,enderecoorigem.cdendereco,enderecoorigem.endereco,areaorigem.cdarea," +
							"areaorigem.codigo,enderecoproduto.cdenderecoproduto,enderecoproduto.uma," +
							"embalagem.cdprodutoembalagem,embalagem.descricao,embalagem.qtde," +
							"endFuncOrigem.cdenderecofuncao,endFuncDestino.cdenderecofuncao,enderecoproduto.dtentrada,ospe.qtde," +
							"enderecodestino.rua,enderecodestino.predio,enderecodestino.nivel,enderecodestino.apto," +
							"enderecoorigem.rua,enderecoorigem.predio,enderecoorigem.nivel,enderecoorigem.apto, enderecoproduto.qtdereservadaentrada, " +
							"enderecoproduto.qtdereservadasaida")
					.join("ordemservicoproduto.produto produto")
					.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
					.join("produto.listaProdutoEmbalagem embalagem")
					.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ospe")
					.join("ospe.enderecodestino enderecodestino")
					.join("enderecodestino.area areadestino")
					.join("enderecodestino.enderecofuncao endFuncDestino")
					.join("ospe.enderecoorigem enderecoorigem")
					.join("enderecoorigem.listaEnderecoproduto enderecoproduto")
					.join("enderecoproduto.produto prodEnd")
					.join("enderecoorigem.area areaorigem")
					.join("enderecoorigem.enderecofuncao endFuncOrigem")
					.join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
					.join("opl.ordemservico os")
					.join("os.ordemstatus ordemstatus")
					.where("produto = prodEnd")
					.where("ordemprodutostatus = ?",Ordemprodutostatus.NAO_CONCLUIDO)
					.where("os = ?",ordemservico)
					.openParentheses()
						.where("embalagem.compra is true")
						.or()
						.where("embalagem is null")
					.closeParentheses()
					.orderBy("produto.descricao")
					.setUseTranslator(false)
					.list();
		
		//Isto foi feito pois sem o comando setUseTranslator(false) o Neo não consegue executar a query corretamente
		if(lista != null && !lista.isEmpty()){
			listaOSP = new ArrayList<Ordemservicoproduto>();
			for (int i = 0;i < lista.size();) {
				Object[] obj = lista.get(i);
				
				Ordemservicoproduto osp = new Ordemservicoproduto();
				osp.setCdordemservicoproduto((Integer)obj[0]);
				osp.setQtdeesperada((Long)obj[1]);
				
				Produto produto = new Produto();
				produto.setCdproduto((Integer)obj[2]);
				produto.setCodigo((String)obj[3]);
				produto.setDescricao((String)obj[4]);
				
				osp.setProduto(produto);
				
				do{
					obj = lista.get(i);
					
					Ordemservicoprodutoendereco ospe = new Ordemservicoprodutoendereco();
					ospe.setCdordemservicoprodutoendereco((Integer)obj[5]);
					
					Endereco endDest = new Endereco();
					Endereco endOrig = new Endereco();
					ospe.setEnderecodestino(endDest);
					ospe.setEnderecoorigem(endOrig);
					
					endDest.setCdendereco((Integer) obj[6]);
					endDest.setEndereco((String)obj[7]);
					endDest.setRua((Integer)obj[23]);
					endDest.setPredio((Integer)obj[24]);
					endDest.setNivel((Integer)obj[25]);
					endDest.setApto((Integer)obj[26]);
					
					
					Area areaDest = new Area();
					areaDest.setCdarea((Integer)obj[8]);
					areaDest.setCodigo((Long)obj[9]);
					endDest.setArea(areaDest);
					
					endOrig.setCdendereco((Integer) obj[10]);
					endOrig.setEndereco((String)obj[11]);
					endOrig.setRua((Integer)obj[27]);
					endOrig.setPredio((Integer)obj[28]);
					endOrig.setNivel((Integer)obj[29]);
					endOrig.setApto((Integer)obj[30]);
					
					Area areaOrig = new Area();
					areaOrig.setCdarea((Integer)obj[12]);
					areaOrig.setCodigo((Long)obj[13]);
					endOrig.setArea(areaOrig);
					
					Enderecoproduto enderecoproduto = new Enderecoproduto();
					enderecoproduto.setCdenderecoproduto((Integer)obj[14]);
					enderecoproduto.setUma((Boolean)obj[15]);
					enderecoproduto.setDtentrada((java.sql.Date)obj[21]);
					enderecoproduto.setQtdereservadaentrada((Long) obj[31]);
					enderecoproduto.setQtdereservadasaida((Long) obj[32]);
					endOrig.getListaEnderecoproduto().add(enderecoproduto);
					
					Produtoembalagem produtoembalagem = new Produtoembalagem();
					produtoembalagem.setCdprodutoembalagem((Integer)obj[16]);
					produtoembalagem.setDescricao((String)obj[17]);
					produtoembalagem.setQtde((Long)obj[18]);
					produto.getListaProdutoEmbalagem().add(produtoembalagem);
					
					Enderecofuncao enderecofuncaoOrigem = new Enderecofuncao((Integer)obj[19]);
					Enderecofuncao enderecofuncaoDestino = new Enderecofuncao((Integer)obj[20]);
					
					endOrig.setEnderecofuncao(enderecofuncaoOrigem);
					endDest.setEnderecofuncao(enderecofuncaoDestino);
					
					ospe.setQtde((Long)obj[22]);
					
					osp.getListaOrdemservicoprodutoendereco().add(ospe);
					
					i++;
				}while(i < lista.size() && ((Integer)obj[0]).equals(((Integer)lista.get(i)[0])));
				listaOSP.add(osp);
			}
		}
		return listaOSP;
	}

	public Ordemservicoproduto findByOrdemservicoAndProduto(Ordemservico ordemservico, Produto produto) {

		if(ordemservico == null || ordemservico.getCdordemservico() == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("Erro ao executar a função findByOrdemservicoAndProduto.");
		return 	query()
				.select("ordemservicoproduto.cdordemservicoproduto, ordemservicoproduto.qtdeesperada")
				.join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
				.join("opl.ordemservico ordemservico")
				.join("ordemservicoproduto.produto produto")
				.where("produto = ?",produto)
				.where("ordemservico = ?",ordemservico)
				.unique()
				;
	}
	
	/**
	 * excluir todos os Ordemservicoproduto através da ordemservico produto.
	 * @author Pedro gonçalves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		if(listaOSP == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		getHibernateTemplate().bulkUpdate("delete Ordemservicoproduto osp where osp.id in ("+listaOSP+")");
	}

	/**
	 * Busca os itens para o processo de corte.
	 * 
	 * @param ordemservico
	 */
	@SuppressWarnings("unchecked")
	public List<ItemOrdemServico> findForCorte(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Erro ao executar a função findForCorte.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select cdordemservicoproduto, qtdeesperada, sum(qtdeconfirmada) as qtdeconfirmada ");
		sql.append("from ( ");
		sql.append("    select osp.cdordemservicoproduto, osp.qtdeesperada, ci.qtdeconfirmada ");
		sql.append("    from ordemprodutoligacao opl  ");
		sql.append("      join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto ");
		sql.append("      join etiquetaexpedicao ee on ee.cdordemservicoproduto = osp.cdordemservicoproduto ");
		sql.append("      join carregamentoitem ci on ci.cdcarregamentoitem = ee.cdcarregamentoitem ");
		sql.append("    where opl.cdordemservico = ? ");
		sql.append("    UNION ALL ");
		sql.append("    select osp.cdordemservicoproduto, osp.qtdeesperada, ci.qtdeconfirmada ");
		sql.append("    from ordemprodutoligacao opl  ");
		sql.append("      join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto ");
		sql.append("      join etiquetaexpedicaogrupo eeg on eeg.cdordemservicoproduto = osp.cdordemservicoproduto ");
		sql.append("      join carregamentoitem ci on ci.cdcarregamentoitem = eeg.cdcarregamentoitem ");
		sql.append("    where opl.cdordemservico = ? ");
		sql.append(") ");
		sql.append("group by cdordemservicoproduto, qtdeesperada ");
		
		Object itens = getJdbcTemplate().query(sql.toString(),new Integer[]{ordemservico.getCdordemservico(), ordemservico.getCdordemservico()}, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,DataAccessException {
				List<ItemOrdemServico> resultado = new ArrayList<ItemOrdemServico>();
				
				while (rs.next()){
					ItemOrdemServico item = new ItemOrdemServico();
					
					item.setCdordemservicoproduto(rs.getInt("cdordemservicoproduto"));
					item.setQtdeconfirmada(rs.getLong("qtdeconfirmada"));
					item.setQtdeesperada(rs.getLong("qtdeesperada"));
					
					resultado.add(item);
				}
				
				return resultado;
			}
		});
		
		return (List<ItemOrdemServico>) itens;
	}
	
	/**
	 * Método que carrega ordem de seriço produto para validação no lançamento manual
	 * 
	 * @param ordemservicoproduto
	 * @return
	 * @author Tomás Rabelo
	 */
	public Ordemservicoproduto carregaOrdemservicoprodutoParaLancamentoManual(Ordemservicoproduto ordemservicoproduto) {
		return query()
			.select("ordemservicoproduto.cdordemservicoproduto, ordemservicoproduto.qtdeesperada, produto.cdproduto, produto.codigo, " +
					"produto.descricao, listaDadoLogistico.cddadologistico, listaDadoLogistico.larguraexcedente, endereco.cdendereco")
			.join("ordemservicoproduto.produto produto")
			.leftOuterJoin("produto.listaDadoLogistico listaDadoLogistico")
			.leftOuterJoin("listaDadoLogistico.endereco endereco")
			.where("ordemservicoproduto = ?", ordemservicoproduto)
			.unique();
	}

	/**
	 * Busca as {@link Ordemservicoproduto} juntamente com o {@link Produto} associado.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findByOrdemservicoWithProduto(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de servico não deve ser nula");
		
		return query()
			 .select("ordemservicoproduto.cdordemservicoproduto,ordemservicoproduto.qtdeesperada," +
			 		"produto.cdproduto, ordemprodutostatus.cdordemprodutostatus")
			 .join("ordemservicoproduto.produto produto")
			 .join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
			 .join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
			 .join("opl.ordemservico os")
			 .where("os = ?",ordemservico)
			 .list();
	}

	/**
	 * Método que atualiza o produto embalagem da ordem de serviço
	 * 
	 * @param ordemservicoproduto
	 * @param produtoEmbalagem
	 * @author Tomás Rabelo
	 */
	public void atualizaProdutoEmbalagem(Ordemservicoproduto ordemservicoproduto, Produtoembalagem produtoEmbalagem) {
		if(ordemservicoproduto == null || ordemservicoproduto.getCdordemservicoproduto() == null || produtoEmbalagem == null || produtoEmbalagem.getCdprodutoembalagem() == null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		getHibernateTemplate().bulkUpdate("update Ordemservicoproduto ordemservicoproduto set ordemservicoproduto.produtoembalagem.id = ? " +
				"where ordemservicoproduto.id = ?",new Object[]{produtoEmbalagem.getCdprodutoembalagem(),ordemservicoproduto.getCdordemservicoproduto()});
	}
	
	/**
	 * Busca os objetos {@link Ordemservicoproduto} e seus {@link Ordemservicoprodutoendereco} associados para
	 * serem excluídos.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoproduto> findForDelete(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de servico não deve ser nula");
		
		return query().select("ordemservicoproduto.cdordemservicoproduto, produto.cdproduto," +
				"ordemservicoproduto2.cdordemservicoproduto, produto2.cdproduto," +
				"ordemservicoprodutoendereco.cdordemservicoprodutoendereco,ordemservicoprodutoendereco.qtde," +
				"enderecodestino.cdendereco,enderecoorigem.cdendereco")
				.join("ordemservicoproduto.produto produto")
				.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
				.join("ordemservicoprodutoendereco.ordemservicoproduto ordemservicoproduto2")
				.join("ordemservicoproduto2.produto produto2")
				.join("ordemservicoproduto.listaOrdemprodutoLigacao ordemprodutoligacao")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecoorigem enderecoorigem")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.where("ordemprodutoligacao.ordemservico=?", ordemservico)
				.list();
	}
	
	/**
	 * Método que busca qtde esperada da ordem de serviço produto
	 * 
	 * @param ordemservicoproduto
	 * @return
	 * @author Tomás Rabelo
	 */
	public Ordemservicoproduto getQtdeEsperadaOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		return query()
			.select("ordemservicoproduto.cdordemservicoproduto, ordemservicoproduto.qtdeesperada")
			.where("ordemservicoproduto = ?", ordemservicoproduto)
			.unique();
	}

	/**
	 * Busca os objetos {@link Ordemservicoproduto} de um determinado carregamento e 
	 * para um tipo específico de O.S.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @param mapaSeparacao
	 */
	public List<Ordemservicoproduto> findByCarregamento(Carregamento carregamento, Ordemtipo ordemtipo) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O Carregamento não deve ser nulo.");
		
		return query().select("ordemservicoproduto.cdordemservicoproduto, produto.cdproduto," +
				"produtoprincipal.cdproduto")
				.join("ordemservicoproduto.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.join("ordemservicoproduto.listaOrdemprodutoLigacao ordemprodutoligacao")
				.where("ordemprodutoligacao.ordemservico.carregamento=?", carregamento)
				.where("ordemprodutoligacao.ordemservico.ordemtipo=?", ordemtipo)
				.list();
	}
	
	/**
	 * Método que busca as osp do tipo MAPA_SEPARACAO e CONFERENCIA_EXPEDICAO 
	 * e duplica os registros de OSPE de MAPA_SEPARACAO para CONFERENCIA_EXPEDICAO
	 *  
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservicoproduto> findOrdensServicoProdutoTipoMapaConferencia(Carregamento carregamento) {
		return query()
			.select("ordemservicoproduto.cdordemservicoproduto, produto.cdproduto, " +
					"ospe.qtde, enderecoorigem.cdendereco, enderecodestino.cdendereco, ordemtipo.cdordemtipo")
			.join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
			.join("opl.ordemservico ordemservico")
			.join("ordemservico.carregamento carregamento")
			.join("ordemservico.ordemtipo ordemtipo")
			.join("ordemservicoproduto.produto produto")
			.leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ospe")
			.leftOuterJoin("ospe.enderecoorigem enderecoorigem")
			.leftOuterJoin("ospe.enderecodestino enderecodestino")
			.where("carregamento = ?", carregamento)
			.openParentheses()
				.where("ordemtipo = ?", Ordemtipo.MAPA_SEPARACAO).or()
				.where("ordemtipo = ?", Ordemtipo.CONFERENCIA_EXPEDICAO_1)
			.closeParentheses()
			.list();
	}

	/**
	 * Verifica se todos os itens da ordem de serviço já foram concluídos.
	 * 
	 * @param ordemservico
	 * @return
	 */
	public boolean isTodosItensFinalizados(Ordemservico ordemservico) {
		if (ordemservico == null || ordemservico.getCdordemservico() == null){
			throw new WmsException("Parâmetros inválidos.");
		}

		List<Ordemservicoproduto> list = query()
			.join("ordemservicoproduto.listaOrdemprodutoLigacao opl")
			.where("opl.ordemservico = ? ", ordemservico)
			.openParentheses()
				.where("ordemservicoproduto.ordemprodutostatus = ? ", Ordemprodutostatus.EM_EXECUCAO)
				.or()
				.where("ordemservicoproduto.ordemprodutostatus = ? ", Ordemprodutostatus.NAO_CONCLUIDO)
			.closeParentheses()
			.setMaxResults(2)
			.list();
		
		return list == null || list.isEmpty();
	}

	/**
	 * 
	 * @param expedicao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OrdemservicoprodutoVO> findQtdesForVisualizarDetalhes(Expedicao expedicao, Carregamento carregamento){		
		if((expedicao==null || expedicao.getCdexpedicao()==null) && (carregamento==null || carregamento.getCdcarregamento()==null)){
			throw new WmsException("Parametros Invalidos");
		}
		
		StringBuilder sql = new StringBuilder();
			sql.append(" SELECT PE.CDPRODUTOEMBALAGEM, SUM(EE.QTDECOLETOR*NVL(PE.QTDE,0)) AS QTDE FROM ETIQUETAEXPEDICAO EE ");
			sql.append(" JOIN ORDEMSERVICOPRODUTO OSP ON OSP.CDORDEMSERVICOPRODUTO = EE.CDORDEMSERVICOPRODUTO ");
			sql.append(" JOIN ORDEMPRODUTOLIGACAO OPL ON OPL.CDORDEMSERVICOPRODUTO = OSP.CDORDEMSERVICOPRODUTO ");
			sql.append(" JOIN ORDEMSERVICO OS ON OPL.CDORDEMSERVICO = OS.CDORDEMSERVICO ");
			sql.append(" LEFT OUTER JOIN PRODUTOEMBALAGEM PE ON PE.CDPRODUTOEMBALAGEM = OSP.CDPRODUTOEMBALAGEM ");						
			if(ConfiguracaoService.getInstance().isTrue("OPERACAO_EXPEDICAO_POR_BOX", WmsUtil.getDeposito()))
				sql.append(" WHERE OS.CDEXPEDICAO = ").append(expedicao.getCdexpedicao());
			else
				sql.append(" WHERE OS.CDCARREGAMENTO = ").append(carregamento.getCdcarregamento());			
			sql.append(" AND OS.CDORDEMTIPO IN (4,15) ");
			sql.append(" GROUP BY PE.CDPRODUTOEMBALAGEM ");
			
		List<OrdemservicoprodutoVO> lista = getJdbcTemplate().query(sql.toString(),new RowMapper(){
			public Object mapRow(java.sql.ResultSet rs, int currentItem) throws SQLException {
					OrdemservicoprodutoVO ordemservicoprodutoVO = new OrdemservicoprodutoVO();
					ordemservicoprodutoVO.setCdprodutoembalagem(rs.getInt("CDPRODUTOEMBALAGEM"));
					ordemservicoprodutoVO.setQtde(rs.getInt("QTDE"));
					return ordemservicoprodutoVO;
			}
		});
		return lista;
	}
	
}