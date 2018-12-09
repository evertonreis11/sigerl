package br.com.linkcom.wms.geral.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Inventariostatus;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Reabastecimentolote;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.geral.bean.Transferenciastatus;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.ConfirmacaoItemVO;
import br.com.linkcom.wms.geral.bean.vo.MovimentacaoAberta;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.EnderecoprodutoService;
import br.com.linkcom.wms.geral.service.ProdutoembalagemService;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirmapaseparacaoPapelFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.EtiquetaumaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.expedicao.ItemCorteVO;
import br.com.linkcom.wms.util.expedicao.ItemSeparacaoVO;
import br.com.linkcom.wms.util.logistica.EtiquetasumaVO;
import br.com.linkcom.wms.util.logistica.OrdemVO;

public class OrdemservicoDAO extends br.com.linkcom.wms.util.neo.persistence.GenericDAO<Ordemservico> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Ordemservico> query, FiltroListagem _filtro) {
		query.leftOuterJoinFetch("ordemservico.ordemtipo ordemtipo");
		query.leftOuterJoinFetch("ordemservico.recebimento recebimento");
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaOrdemServicoUsuario");
		save.saveOrUpdateManaged("listaOrdemProdutoHistorico");
		save.saveOrUpdateManaged("listaOrdemProdutoLigacao");
	}
	
	/**
	 * 
	 * Insere ou atualiza o bean de ordem serviço
	 * 
	 * @author Arantes
	 * 
	 * @param bean
	 * 
	 */
	public void saveOrUpdateBean(Ordemservico bean) {
		SaveOrUpdateStrategy save = save(bean);
		save.execute();
		getHibernateTemplate().flush();
	}
	
	@Override
	public void delete(Ordemservico bean) {
		getHibernateTemplate().delete(bean);
		getHibernateTemplate().flush();
	}
	
	@Override
	public Ordemservico load(Ordemservico bean) {
		if (bean == null || bean.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.joinFetch("ordemservico.ordemstatus status")
			.joinFetch("ordemservico.ordemtipo ordemtipo")
			.entity(bean)
			.unique();
	}
	
	public Ordemservico loadWithPrincipal(Ordemservico bean) {
		if (bean == null || bean.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.joinFetch("ordemservico.ordemstatus status")
			.joinFetch("ordemservico.ordemtipo ordemtipo")
			.leftOuterJoinFetch("ordemservico.ordemservicoprincipal principal")
			.entity(bean)
			.unique();
	}
	
	/**
	 * Lista as ordens de serviços cujo status do recebimento é o mesmo do informado
	 * no parâmetro.
	 * 
	 * @param recebimentostatus - status do recebimento
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#selectOrdemServicoStatusRecebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Ordemservico> findAllByStatusRecebimento(Recebimentostatus ... recebimentostatus) {
		if(recebimentostatus == null || recebimentostatus.length == 0)
			throw new WmsException("Parâmetros inválidos");
		
		QueryBuilder<Ordemservico> query = selectOrdemServicoStatusRecebimento("ordemservico.cdordemservico","recebimento.cdrecebimento","recebimento.dtrecebimento","box.nome",
				"recebimento.observacao","status.nome","ordemservico.ordem");
		
		if(recebimentostatus.length == 1){
			query.where("status=?",recebimentostatus[0]);
		} else {
			query.openParentheses();
			query.where("status=?",recebimentostatus[0]);
			
			for (int i = 1; i < recebimentostatus.length; i++) {
				query.or();
				query.where("status=?",recebimentostatus[i]);
			}
			
			query.closeParentheses();
		}
		
		return query
				.join("ordemservico.deposito deposito")
				.where("deposito=?",WmsUtil.getDeposito())
				.where("ordemservico.cdordemservico = (select max(ord2.cdordemservico) from Ordemservico ord2 where ord2.recebimento = recebimento))")
				.list();
	}
	
	/**
	 * Carrega a ordem de serviço cujo status do recebimento é o mesmo do informado
	 * no parâmetro.
	 * 
	 * @param recebimentostatus - status do recebimento
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicoDAO#selectOrdemServicoStatusRecebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Ordemservico loadBy(Recebimentostatus recebimentostatus, Ordemservico ordemservico) {
		if(recebimentostatus == null || recebimentostatus.getCdrecebimentostatus() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return  selectOrdemServicoStatusRecebimento("recebimento.cdrecebimento","notafiscalentrada.veiculo", "box.nome", 
													"notafiscalentrada.transportador", "fornecedor.nome", "recebimento.dtrecebimento",
													"ordemservico.cdordemservico", "ordemservico.ordem","ordemtipo.cdordemtipo",
													"ordemtipo.nome", "recebimento.observacao",
													"tipoveiculo.nome", "usuario.nome", "notafiscalentrada.numero", 
													"notafiscalentrada.dtchegada", "recebimentonotafiscal.cdrecebimentonotafiscal",
													"notafiscalentrada.cdnotafiscalentrada")
				.leftOuterJoin("recebimento.tipoveiculo tipoveiculo")
				.leftOuterJoin("recebimento.usuario usuario")
				.join("recebimento.listaRecebimentoNF recebimentonotafiscal")
				.join("recebimentonotafiscal.notafiscalentrada notafiscalentrada")
				.join("notafiscalentrada.fornecedor fornecedor")
				.join("ordemservico.ordemtipo ordemtipo")
				.where("recebimento.cdrecebimento = (select rec.cdrecebimento from Ordemservico ord join ord.recebimento rec where ord = ?)",ordemservico)
				.where("status=?",recebimentostatus)
				.where("ordemservico.id = (select max(ord2.cdordemservico) from Ordemservico ord2 where ord2.recebimento = recebimento)")
			    .unique();
	}
	
	/**
	 * Carrega uma ordem de serviço a partir de um recebimento
	 * 
	 * @author Pedro Gonçalves
	 * @param recebimento
	 * @return
	 */
	public Ordemservico loadBy(Recebimento recebimento){
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("ordemservico.cdordemservico")
				.join("ordemservico.recebimento recebimento")
				.where("recebimento=?",recebimento)
				.unique();
	}
	
	/**
	 * Carrega a ordem de conferência/reconferência mais recente.
	 * 
	 * @author Pedro Gonçalves
	 * @param recebimento
	 * @param incluirInfoProduto 
	 * @return
	 */
	public Ordemservico loadLastConferencia(Recebimento recebimento, boolean incluirInfoProduto){
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos");
		
		String camposExtras = "";
		if (incluirInfoProduto)
			camposExtras = ",ordemservicoproduto.cdordemservicoproduto,ordemservicoproduto.qtdeesperada,produto.cdproduto," +
					"tipopalete.cdtipopalete";
			
		QueryBuilder<Ordemservico> queryBuilder = query().select("ordemservico.cdordemservico,ordemservico.ordem,listaOrdemProdutoHistorico.cdordemprodutohistorico," +
						"listaOrdemProdutoHistorico.qtde,listaOrdemProdutoHistorico.qtdeavaria,listaOrdemProdutoHistorico.qtdefracionada," +
						"ordemstatus.cdordemstatus" + camposExtras)
				.join("ordemservico.recebimento recebimento")
				.join("ordemservico.ordemstatus ordemstatus")
				.leftOuterJoin("ordemservico.listaOrdemProdutoHistorico listaOrdemProdutoHistorico")
				.where("recebimento=?",recebimento)
				.where("ordemservico.ordemtipo in (?, ?) ", new Ordemtipo[]{Ordemtipo.CONFERENCIA_RECEBIMENTO, Ordemtipo.RECONFERENCIA_RECEBIMENTO})
				.orderBy("ordemservico.cdordemservico desc");
		
		if (incluirInfoProduto){
			queryBuilder.leftOuterJoin("listaOrdemProdutoHistorico.ordemservicoproduto ordemservicoproduto");
			queryBuilder.leftOuterJoin("ordemservicoproduto.produto produto");
			queryBuilder.leftOuterJoin("ordemservicoproduto.tipopalete tipopalete");
		}
		
		return queryBuilder.setMaxResults(1).unique();
	}
	
	/**
	 * Carrega a ordem de contagem/recontagem mais recente.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public Ordemservico loadLastContagem(Inventariolote inventariolote){
		if(inventariolote == null || inventariolote.getCdinventariolote() == null)
			throw new WmsException("Parâmetros inválidos");
		
		QueryBuilder<Ordemservico> queryBuilder = query().select("ordemservico.cdordemservico,ordemservico.ordem," +
						"ordemstatus.cdordemstatus")
				.join("ordemservico.ordemstatus ordemstatus")
				.where("ordemservico.inventariolote = ?", inventariolote)
				.where("ordemservico.ordemtipo in (?, ?) ", new Ordemtipo[]{Ordemtipo.CONTAGEM_INVENTARIO, Ordemtipo.RECONTAGEM_INVENTARIO})
				.orderBy("ordemservico.cdordemservico desc");
		
		return queryBuilder.setMaxResults(1).unique();
	}
	
	/**
	 * Carrega a ordem de contagem/recontagem mais recente.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public Ordemservico loadLastContagem(Reabastecimentolote lote){
		if(lote == null || lote.getCdreabastecimentolote() == null)
			throw new WmsException("Parâmetros inválidos");
		
		QueryBuilder<Ordemservico> queryBuilder = query().select("ordemservico.cdordemservico,ordemservico.ordem," +
		"ordemstatus.cdordemstatus")
		.join("ordemservico.ordemstatus ordemstatus")
		.where("ordemservico.reabastecimentolote = ?", lote)
		.where("ordemservico.ordemtipo = ? ", Ordemtipo.REABASTECIMENTO_PICKING)
		.orderBy("ordemservico.cdordemservico desc");
		
		return queryBuilder.setMaxResults(1).unique();
	}
	
	/**
	 * Monta a query de exibiçao da ordem de serviço a partir do status informado.
	 * @param recebimentostatus
	 * @param select
	 * @return
	 * @author Pedro Gonçalves
	 */
	private QueryBuilder<Ordemservico> selectOrdemServicoStatusRecebimento(String ... select){
		return query()			
			.select(WmsUtil.makeSelectClause("ordemservico.cdordemservico,ordemservico.ordem, recebimento.cdrecebimento,recebimento.dtrecebimento, box.nome, recebimento.observacao,status.nome", select))
			.join("ordemservico.recebimento recebimento")
			.join("recebimento.recebimentostatus status")
			.join("recebimento.box box");
	}
	
	/**
	 * Lista todas as ordem de serviço do carregamento especificado.
	 * 
	 * @author Pedro Gonçalves
	 * @param carregamento
	 * @return List<Ordemservico>
	 */
	public List<Ordemservico> findByCarregamento(Carregamento carregamento){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("ordemservico.cdordemservico, ordemservico.ordem, linhaseparacao.nome, " +
						"ordemstatus.cdordemstatus, ordemtipo.cdordemtipo, carregamento.cdcarregamento, " +
						"usuario.cdpessoa, usuario.login")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.ordemtipo ordemtipo")
				.leftOuterJoin("ordemservico.carregamento carregamento")
				.leftOuterJoin("ordemservico.listaOrdemProdutoLigacao opl")
				.leftOuterJoin("opl.ordemservicoproduto osp")
				.leftOuterJoin("osp.produto produto")
				.leftOuterJoin("produto.listaDadoLogistico dl")
				.leftOuterJoin("dl.linhaseparacao linhaseparacao")
				.leftOuterJoin("linhaseparacao.deposito deposito")
				.leftOuterJoin("ordemservico.listaOrdemServicoUsuario listaOrdemServicoUsuario")
				.leftOuterJoin("listaOrdemServicoUsuario.usuario usuario")
				.openParentheses()
					.where("deposito=?",carregamento.getDeposito() == null ? WmsUtil.getDeposito() : carregamento.getDeposito())
					.or()
					.where("deposito is null")
				.closeParentheses()
				.where("carregamento=?",carregamento)
				.orderBy("ordemservico.cdordemservico")
				.list();
	}
	
	/**
	 * Lista todas as ordem de serviço do carregamento especificado.
	 * Os dados são carregados para a tela de gerenciamento do carregamento.
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByCarregamentoToGerenciamento(Carregamento carregamento){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("distinct linhaseparacao.nome, ordemservico.cdordemservico, ordemservico.ordem, linhaseparacao.cdlinhaseparacao, " +
						"linhaseparacao.nome, ordemtipo.cdordemtipo, ordemtipo.nome, ordemstatus.cdordemstatus, ordemstatus.nome, " +
						"carregamentostatus.cdcarregamentostatus,clienteexpedicao.cdpessoa, clienteexpedicao.nome," +
						"ordemservicoprincipal.cdordemservico, tipooperacao.cdtipooperacao, tipooperacao.nome")
				.join("ordemservico.carregamento carregamento")
				.join("ordemservico.ordemtipo ordemtipo")
				.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
				.join("ordemservico.listaOrdemProdutoLigacao opl")
				.join("ordemservico.ordemstatus ordemstatus")
				.leftOuterJoin("ordemservico.ordemservicoprincipal ordemservicoprincipal")
				.leftOuterJoin("ordemservico.clienteExpedicao clienteexpedicao")
				.join("opl.ordemservicoproduto osp")
				.join("osp.produto produto")
				.join("produto.listaDadoLogistico dl with dl.deposito.cddeposito=" + WmsUtil.getDeposito().getCddeposito())
				.join("dl.linhaseparacao linhaseparacao")
				.join("linhaseparacao.deposito deposito")
				.join("carregamento.carregamentostatus carregamentostatus")				
				.openParentheses()
					.where("ordemtipo=?",Ordemtipo.CONFERENCIA_EXPEDICAO_1)
					.or()
					.where("ordemtipo=?",Ordemtipo.MAPA_SEPARACAO)
					.or()
					.where("ordemtipo=?",Ordemtipo.RECONFERENCIA_EXPEDICAO_1)
					.or()
					.where("ordemtipo=?",Ordemtipo.REABASTECIMENTO_PICKING)
				.closeParentheses()
				.where("carregamento=?",carregamento)
				.where("linhaseparacao.cdlinhaseparacao is not null")
				.orderBy("ordemtipo.cdordemtipo, ordemservico.cdordemservico")
				.list();
	}
	
	/**
	 * Carrega todas as ordens de serviço que são do tipo Conferência para carregar o menu
	 * de escolha da ordem de serviço do coletor. 
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @param usuario 
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByCarregamentoToRF(Carregamento carregamento,Ordemtipo ordemtipo, Usuario usuario){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos");

		return query()
				.select("ordemservico.cdordemservico,ordemtipo.cdordemtipo,deposito.cddeposito, ordemservico.ordem, " +
						"carregamento.cdcarregamento,ordemstatus.cdordemstatus, clienteExpedicao.cdpessoa,clienteExpedicao.nome," +
						"tipooperacao.cdtipooperacao,tipooperacao.nome,tipooperacao.imprimeetiqueta," +
						"ordemservicoprincipal.cdordemservico,box.cdbox,box.nome")
				.join("ordemservico.carregamento carregamento")
				.join("carregamento.box box")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.deposito deposito")
				.join("ordemservico.listaOrdemProdutoLigacao oph")//só deve carregar OS de conferência de box que possui itens.
				.join("carregamento.carregamentostatus carregamentostatus")
				.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
				.leftOuterJoin("ordemservico.clienteExpedicao clienteExpedicao")				
				.leftOuterJoin("ordemservico.listaOrdemServicoUsuario ordemServicoUsuario")
				.leftOuterJoin("ordemservico.ordemservicoprincipal ordemservicoprincipal")
				.where("ordemtipo=?",ordemtipo)
				.openParentheses()
					.where("ordemstatus=?",Ordemstatus.EM_ABERTO)
					.or()
					.openParentheses()
						.where("ordemstatus=?",Ordemstatus.EM_EXECUCAO)
						.where("ordemServicoUsuario.usuario=?", usuario)
					.closeParentheses()
				.closeParentheses()
				.where("carregamento=?",carregamento)
				.list();
	}
	

	/**
	 * Carrega todas as ordens de serviço que são do tipo Conferência para carregar o menu
	 * de escolha da ordem de serviço do coletor. 
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param carregamento
	 * @param usuario 
	 * @return List<Ordemservico>
	 * 
	 */
	public Ordemservico findByCarregamentoToRF(Ordemservico ordemservico, Usuario usuario){
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos");

		return query()
				.select("ordemservico.cdordemservico,ordemtipo.cdordemtipo, ordemtipo.nome,deposito.cddeposito, ordemservico.ordem, " +
						"ordemstatus.cdordemstatus, clienteExpedicao.cdpessoa,clienteExpedicao.nome," +
						"tipooperacao.cdtipooperacao,tipooperacao.nome,tipooperacao.imprimeetiqueta," +
						"ordemservicoprincipal.cdordemservico,expedicao.cdexpedicao,carregamento.cdcarregamento, " +
						"box.nome")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.deposito deposito")
				.join("ordemservico.listaOrdemProdutoLigacao oph")//só deve carregar OS de conferência de box que possui itens.
				.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
				.leftOuterJoin("ordemservico.expedicao expedicao")
				.leftOuterJoin("ordemservico.carregamento carregamento")
				.leftOuterJoin("carregamento.box box")
				.leftOuterJoin("ordemservico.clienteExpedicao clienteExpedicao")
				.leftOuterJoin("ordemservico.listaOrdemServicoUsuario ordemServicoUsuario")
				.leftOuterJoin("ordemservico.ordemservicoprincipal ordemservicoprincipal")
				.openParentheses()
					.where("ordemstatus=?",Ordemstatus.EM_ABERTO)
					.or()
					.openParentheses()
						.where("ordemstatus=?",Ordemstatus.EM_EXECUCAO)
						.where("ordemServicoUsuario.usuario=?", usuario)
					.closeParentheses()
				.closeParentheses()
				.openParentheses()
					.where("ordemservico=?", ordemservico)
					.or()
					.where("ordemservico.ordemservicoprincipal=?", ordemservico)
				.closeParentheses()
				.unique();
	}
	
	/**
	 * Recupera o total de ordens de serviço a partir do carregamento.
	 * 
	 * @author Pedro Gonçalves
	 * @param carregamento
	 * @return
	 */
	public Long getTotalOs(Carregamento carregamento){
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return newQueryBuilderWithFrom(Long.class)
				.select("count(*)")
				.join("ordemservico.carregamento carregamento")
				.where("carregamento=?",carregamento)
				.unique();
	}
	
	/**
	 * Busca a penultima ordem de servico do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @return
	 */
	public Ordemservico loadPenultimaConferencia(Recebimento recebimento) {
		return query()
				.select("ordemservico.cdordemservico,ordemservico.ordem," +
						"listaOrdemProdutoHistorico.qtde,listaOrdemProdutoHistorico.qtdeavaria")
				.join("ordemservico.recebimento recebimento")
				.leftOuterJoin("ordemservico.listaOrdemProdutoHistorico listaOrdemProdutoHistorico")
				.where("recebimento=?",recebimento)
				.where("ordemservico.cdordemservico <> (select MAX(os.cdordemservico) from Ordemservico os where (os.ordemtipo.id = " + 
						Ordemtipo.CONFERENCIA_RECEBIMENTO.getCdordemtipo() + " or os.ordemtipo.id = " +
						Ordemtipo.RECONFERENCIA_RECEBIMENTO.getCdordemtipo() + " ) and os.recebimento.id="+recebimento.getCdrecebimento()+")")
				.openParentheses()
					.where("ordemservico.ordemtipo = ?" , Ordemtipo.CONFERENCIA_RECEBIMENTO)
					.or()
					.where("ordemservico.ordemtipo = ?" , Ordemtipo.RECONFERENCIA_RECEBIMENTO)
				.closeParentheses()
				.orderBy("ordemservico.cdordemservico desc")
				.setMaxResults(1)
				.unique();
	}
	
	/**
	 * 
	 * Método que recupera o tipo e o status da ordem de serviço. Também recupera o carregamento que ela pertence
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * @return Ordemservico
	 * 
	 */
	public Ordemservico loadTipostatuscarregamento(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("ordemservico.cdordemservico, ordemtipo.cdordemtipo, ordemstatus.cdordemstatus, carregamento.cdcarregamento, " +
							"carregamentostatus.cdcarregamentostatus,ordemprincipal.cdordemservico, tipoprincipal.cdordemtipo," +
							"expedicao.cdexpedicao, box.cdbox,clienteExpedicao.cdpessoa,tipooperacao.cdtipooperacao,tipooperacao.separacliente," +
							"tipooperacao.nome")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.ordemstatus ordemstatus")
					.leftOuterJoin("ordemservico.carregamento carregamento")
					.leftOuterJoin("ordemservico.expedicao expedicao")
					.leftOuterJoin("expedicao.box box")
					.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
					.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
					.leftOuterJoin("ordemservico.clienteExpedicao clienteExpedicao")
					.leftOuterJoin("ordemservico.ordemservicoprincipal ordemprincipal")
					.leftOuterJoin("ordemprincipal.ordemtipo tipoprincipal")
					.where("ordemservico = ?", ordemservico)
					.unique();					
	}
	
	public Ordemservico findPrincipal(Recebimento recebimento) {
		return query()
				.select("ordemservico.cdordemservico,ordemservico.ordem")
				.where("ordemservico.recebimento = ?",recebimento)
				.where("ordem = 1")
				.setMaxResults(1)
				.unique();
	}
	
	/**
	 * Método que recupera uma lista de ordens de serviço à partir de uma lista de id´s de ordens de serviço produto.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByListaOrdemservico(String filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetro inválido");
		
		return query()
					.select("ordemservico.cdordemservico, listaOrdemProdutoLigacao.ordemservico, ordemservicoproduto.cdordemservicoproduto, " +
							"ordemprodutostatus.cdordemprodutostatus")
					.join("ordemservico.listaOrdemProdutoLigacao listaOrdemProdutoLigacao")
					.join("listaOrdemProdutoLigacao.ordemservicoproduto ordemservicoproduto")
					.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
					.whereIn("ordemservico.cdordemservico", filtro)
					.list();
	}
	
	/**
	 * 
	 * Atualiza o status da ordem de servico.
	 * 
	 * @author Arantes
	 * 
	 * @param ordemservico
	 * 
	 */
	public void updateStatusordemservico(Ordemservico ordemservico) {
		if((ordemservico == null) || (ordemservico.getCdordemservico() == null) || 
		   (ordemservico.getOrdemstatus() == null) || (ordemservico.getOrdemstatus().getCdordemstatus() == null))
			throw new WmsException("Parâmetros inválidos");
		
		getHibernateTemplate().bulkUpdate("update Ordemservico ordemservico set ordemservico.ordemstatus.id=? where ordemservico.id=? ",
				                           new Object[]{ordemservico.getOrdemstatus().getCdordemstatus(), ordemservico.getCdordemservico()});
	}
	
	/**
	 * 
	 * Método que recupera o status e o tipo de uma ordem de serviço
	 * 
	 * @author Arantes
	 * 
	 * @para filtro
	 * @return Ordemservico
	 * 
	 */
	public Ordemservico loadStatusTipoByOrdemservico(Ordemservico filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("ordemservico.cdordemservico, ordemtipo.cdordemtipo, ordemtipo.nome, ordemstatus.cdordemstatus, ordemstatus.nome")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.ordemstatus ordemstatus")
					.where("ordemservico = ?", filtro)
					.unique();
	}
	
	/**
	 * 
	 * Método que recupera o status e o tipo de uma ordem de serviço
	 * 
	 * @author Tomás
	 * 
	 * @para filtro
	 * @return Ordemservico
	 * 
	 */
	public List<Ordemservico> loadStatusTipoByOrdemservicoWhereIn(String whereIn) {
		if(whereIn == null || whereIn.equals(""))
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("ordemservico.cdordemservico, ordemtipo.cdordemtipo, ordemtipo.nome, " +
							"ordemstatus.cdordemstatus, ordemstatus.nome, box.cdbox, box.nome, carregamento.cdcarregamento, clienteExpedicao.cdpessoa, " +
							"clienteExpedicao.nome, tipooperacao.cdtipooperacao, tipooperacao.nome")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.ordemstatus ordemstatus")
					.leftOuterJoin("ordemservico.carregamento carregamento")
					.leftOuterJoin("ordemservico.clienteExpedicao clienteExpedicao")
					.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
					.leftOuterJoin("ordemservico.expedicao expedicao")
					.leftOuterJoin("expedicao.box box")
					.whereIn("ordemservico.cdordemservico", whereIn)
					.list();
	}
	
	/* singleton */
	private static OrdemservicoDAO instance;
	public static OrdemservicoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(OrdemservicoDAO.class);
		}
		return instance;
	}
	
	/**
	 * Cancela as ordens de servico do carregamento.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 */
	public void updateStatusordemservicoByCarregamento(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("update Ordemservico os set os.ordemstatus.cdordemstatus = ? where os.carregamento.cdcarregamento = ? " +
											"and os.ordemtipo.cdordemtipo in (3,4,5)",
										   new Object[]{Ordemstatus.CANCELADO.getCdordemstatus(),carregamento.getCdcarregamento()});
		
	}
	
	/**
	 * Busca os dados necessários para a emissão do relatorio 
	 * de impressão das ordens de reabastecimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservico> findForOrdemReabastecimentoReport(Object obj,Ordemtipo ordemtipo) {
		if(obj == null || ordemtipo == null || ordemtipo.getCdordemtipo() == null)
			throw new WmsException("Erro ao executar a função findForOrdemReabastecimentoReport.");
			
		QueryBuilder<Ordemservico> query = newQueryBuilderWithFrom(Ordemservico.class) 		
		.select("ordemservico.cdordemservico,carregamento.cdcarregamento," +
				"produto.cdproduto,produto.codigo,produto.descricao," +
				"transferencia.cdtransferencia, ordemservicoproduto.cdordemservicoproduto," +
				"produtoprincipal.codigo,produtoprincipal.descricao")
					 .join("ordemservico.listaOrdemProdutoHistorico listaOrdemProdutoHistorico")
					 .leftOuterJoin("ordemservico.transferencia transferencia")
					 .leftOuterJoin("ordemservico.carregamento carregamento")
					 .join("ordemservico.ordemtipo ordemtipo")
					 .join("listaOrdemProdutoHistorico.ordemservicoproduto ordemservicoproduto")
					 .join("ordemservicoproduto.produto produto")
					 .leftOuterJoin("produto.produtoprincipal produtoprincipal")
					 .join("produto.listaDadoLogistico listaDadoLogistico with listaDadoLogistico.deposito.id = "+WmsUtil.getDeposito().getCddeposito())
					 .leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco listaOrdemservicoprodutoendereco")
					 .leftOuterJoin("listaOrdemservicoprodutoendereco.enderecoorigem enderecoorigem")
					 .leftOuterJoin("enderecoorigem.area area")
					 .leftOuterJoin("area.listaEnderecosentido listaEnderecosentido")
					 .openParentheses()
					 	.where("enderecoorigem.rua = listaEnderecosentido.rua")
					 	.or()
					 	.where("listaEnderecosentido.id is null")
					 .closeParentheses()
					 .where("ordemtipo = ?",ordemtipo);
					 
					 
		String ordemLinha = "";
		if(ordemtipo != null){
			if(ordemtipo.equals(Ordemtipo.REABASTECIMENTO_PICKING)){
				if (obj instanceof Carregamento)
					query.where("carregamento = ?",((Carregamento)obj));
				else if (obj instanceof String){
					query.whereIn("ordemservico.id", (String)obj);
					ordemLinha = "listaDadoLogistico.linhaseparacao.nome,";
				} else if (obj instanceof Expedicao){
						query.where("ordemservico.expedicao = ? ", (Expedicao)obj);
						ordemLinha = "listaDadoLogistico.linhaseparacao.nome,";
				}else
					throw new WmsException("O parâmetro OBJ é inválido.");
			}else if(ordemtipo.equals(Ordemtipo.TRANSFERENCIA)){
				query.where("transferencia = ?",((Transferencia)obj));
				ordemLinha = "listaDadoLogistico.linhaseparacao.nome,";
			}
		}

		query.ignoreJoin("listaOrdemservicoprodutoendereco", "enderecoorigem", "area", "listaEnderecosentido", "listaDadoLogistico");
		
		query.orderBy("ordemservico.cdordemservico," + ordemLinha + "area.cdarea,(enderecoorigem.rua*listaEnderecosentido.sentido)," +
				"enderecoorigem.nivel,enderecoorigem.apto,produtoprincipal.descricao,produto.descricao");

		return query.list();
	}
	
	/**
	 * Procura todos os mapas de separação da ordem de serviço do tipo conferência e dá um update no campo status.
	 * 
	 * @param ordemservico 
	 * @param onlyDivergentes -  true - Status é setado como finalizado com divergência, e pega apenas as OSP que estão como concluido divergente.
	 *                           false - Seta como finalizado com sucesso.
	 *                           
	 * @author Pedro Gonçalves
	 */
	public void alterarMapasByOSConferencia(Ordemservico ordemservico, Boolean onlyDivergentes){
		Ordemstatus ordemstatus = Ordemstatus.FINALIZADO_SUCESSO;
		
		StringBuilder qrySub = new StringBuilder();
		qrySub.append("select distinct ospc.cdordemservicoproduto from ordemservicoproduto ospc ");
		qrySub.append("join ordemprodutoligacao oplc on oplc.cdordemservicoproduto = ospc.cdordemservicoproduto ");
		qrySub.append("where oplc.cdordemservico = "+ordemservico.getCdordemservico()+" and ");
		qrySub.append("ospc.cdordemservicoproduto = ospd.cdordemservicoproduto ");
		if(onlyDivergentes) {
			qrySub.append("and ospc.cdordemprodutostatus = "+Ordemprodutostatus.CONCLUIDO_DIVERGENTE.getCdordemprodutostatus() + " ");
			ordemstatus = Ordemstatus.FINALIZADO_DIVERGENCIA;
		}
		
		StringBuilder qryPrinc = new StringBuilder();
		qryPrinc.append("select distinct osd.cdordemservico from ordemservico osd ");
		qryPrinc.append("join ordemprodutoligacao opld on opld.cdordemservico= osd.cdordemservico ");
		qryPrinc.append("join ordemservicoproduto ospd on ospd.cdordemservicoproduto = opld.cdordemservicoproduto ");
		qryPrinc.append("where osd.cdordemtipo = 3 and "); 
		qryPrinc.append("exists ("+qrySub.toString()+") ");
		
		getJdbcTemplate().update("update Ordemservico set cdordemstatus=? where cdordemservico in ("+qryPrinc.toString()+")", new Object[]{ordemstatus.getCdordemstatus()});
	}
	
	/**
	 * Busca as ordens de servico do recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Ordemservico> findByRecebimento(Recebimento recebimento) {
		
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("O recebimento não deve ser nulo.");
		
		return query()
					 .select("ordemservico.cdordemservico, ordemtipo.cdordemtipo,ordemtipo.nome," +
					 		"ordemstatus.cdordemstatus,ordemstatus.nome,recebimento.cdrecebimento," +
					 		"ordemservico.ordem,tipoenderecamento.cdtipoenderecamento")
					 .join("ordemservico.ordemtipo ordemtipo")
					 .join("ordemservico.ordemstatus ordemstatus")
					 .join("ordemservico.recebimento recebimento")
					 .join("recebimento.tipoenderecamento tipoenderecamento")
					 .where("recebimento = ?",recebimento)
					 .orderBy("ordemtipo.nome asc, ordemservico.cdordemservico desc")
					 .list();
	}
	
	/**
	 * 
	 * Método que localiza as ordens de servico de um recebimento
	 * 
	 * @author Arantes 
	 * 
	 * @param filtro
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> findByRecebimentoUma(EtiquetaumaFiltro filtro) {
		if((filtro == null) || (filtro.getRecebimento() == null) || (filtro.getRecebimento().getCdrecebimento() == null))
			throw new WmsException("Parâmetros inválidos.");
			
		return query()
					.select("ordemservico.cdordemservico")
					.join("ordemservico.recebimento recebimento")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.deposito deposito")
					.where("recebimento = ?", filtro.getRecebimento())
					.where("ordemtipo = ?", Ordemtipo.ENDERECAMENTO_PADRAO)
					.where("deposito = ?", WmsUtil.getDeposito())
					.list();
	}
	
	/**
	 * Encontra a ultima ordem de servico do lote
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventariolote
	 * @return
	 */
	public List<Ordemservico> findByLote(Inventariolote inventariolote) {
		if(inventariolote == null || inventariolote.getCdinventariolote() == null)
			throw new WmsException("O lote não deve ser nulo.");
		
		List<Ordemtipo> lista = new ArrayList<Ordemtipo>();
		lista.add(Ordemtipo.CONTAGEM_INVENTARIO);
		lista.add(Ordemtipo.RECONTAGEM_INVENTARIO);
		
		return query()
					.select("ordemservico.cdordemservico,ordemstatus.nome, ordemstatus.cdordemstatus")
					.join("ordemservico.inventariolote inventariolote")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.ordemstatus ordemstatus")
					.where("inventariolote = ?",inventariolote)
					.whereIn("ordemtipo", CollectionsUtil.listAndConcatenate(lista, "cdordemtipo", ","))
					.orderBy("ordemservico.cdordemservico desc")
					.list();
	}
	
	/**
	 * Encontra a ultima ordem de servico do lote
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param reabastecimentolote
	 * @return
	 */
	public List<Ordemservico> findByLote(Reabastecimentolote reabastecimentolote) {
		if(reabastecimentolote == null || reabastecimentolote.getCdreabastecimentolote() == null)
			throw new WmsException("O lote não deve ser nulo.");
		
		return query()
				.select("ordemservico.cdordemservico,ordemstatus.nome, ordemstatus.cdordemstatus")
				.join("ordemservico.ordemstatus ordemstatus")
				.where("ordemservico.reabastecimentolote = ?", reabastecimentolote)
				.orderBy("ordemservico.cdordemservico desc")
				.list();
	}
	
	/**
	 * 
	 * Método que recupera uma lista de ordens de serviço para gerar o relatório de ordem de serviço de contagem no inventário
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Ordemservico>
	 *  
	 */
	public List<Ordemservico> findByOrdemservico(Inventario filtro) {
		if(filtro == null || filtro.getCdinventario() == null) 
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("ordemservico.cdordemservico, ordemservico.ordem," +
						"inventario.cdinventario, " +
						"inventariolote.cdinventariolote, " +
						"ordemprodutoligacao.cdordemprodutoligacao, " +
						"ordemservicoproduto.cdordemservicoproduto, " +
						"ordemservicoprodutoendereco.cdordemservicoprodutoendereco, " +
						"enderecodestino.cdendereco, enderecodestino.endereco, " +
						"enderecofuncao.cdenderecofuncao, " +
						"areadestino.cdarea, areadestino.codigo, " +
						"produto.cdproduto, produto.codigo,produto.qtdevolumes, produto.descricao, " +
						"produtoembalagem.cdprodutoembalagem, produtoembalagem.descricao," +
						"produtoprincipal.cdproduto, produtoprincipal.codigo, produtoprincipal.descricao")
				.leftOuterJoin("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
				.leftOuterJoin("ordemprodutoligacao.ordemservicoproduto ordemservicoproduto")
				.leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.leftOuterJoin("enderecodestino.enderecofuncao enderecofuncao")
				.leftOuterJoin("enderecodestino.area areadestino")
				.leftOuterJoin("ordemservicoproduto.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.leftOuterJoin("produto.listaProdutoEmbalagem produtoembalagem")
				.leftOuterJoin("ordemservico.inventariolote inventariolote")
				.leftOuterJoin("inventariolote.inventario inventario")
				.leftOuterJoin("areadestino.listaEnderecosentido listaEnderecosentido")
				.where("inventario.cdinventario = ?", filtro.getCdinventario())
				.where("listaEnderecosentido.rua = enderecodestino.rua")
				.whereIn("ordemservico.cdordemservico", filtro.getOrdens())
				.openParentheses()
					.openParentheses()
						.openParentheses()
							.where("inventariolote.fracionada = ?", Inventariolote.FRACIONADA_TRUE)
							.or()
							.where("enderecofuncao = ? ", Enderecofuncao.PICKING)
						.closeParentheses()
						.openParentheses()
							.where("produtoembalagem.cdprodutoembalagem is null")
							.or()
							.where("produtoembalagem.qtde = (select min(qtde)   " +
								   "                         from produtoembalagem   " +
								   "                         where produtoembalagem.produto = produto)")
						.closeParentheses()
					.closeParentheses()
					
					.or()
					
					.openParentheses()
						.where("inventariolote.fracionada = ?", Inventariolote.FRACIONADA_FALSE)
						.openParentheses()
							.where("produtoembalagem.cdprodutoembalagem is null")
							.or()
							.where("produtoembalagem.cdprodutoembalagem = (select cdprodutoembalagem " +
								   "                                       from produtoembalagem " +
								   "                                       where produtoembalagem.produto = produto " +
								   "                                       and produtoembalagem.compra = " + Produtoembalagem.COMPRA + ")")
						.closeParentheses()
					.closeParentheses()
				.closeParentheses()
				.orderBy("ordemservico.cdordemservico asc, (enderecodestino.rua*listaEnderecosentido.sentido), enderecodestino.predio," +
				 		"enderecodestino.nivel,enderecodestino.apto")
				.list();
	}

	/**
	 * Encotra as ordens de contagem do lote que estão em execução
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventariolote
	 * @return
	 */
	public List<Ordemservico> findContagemByInventarioLote(Inventariolote inventariolote) {
		if(inventariolote == null || inventariolote.getCdinventariolote() == null)
			throw new WmsException("O lote não deve ser nulo.");
		
		List<Ordemstatus> listaStatus = new ArrayList<Ordemstatus>();
		listaStatus.add(Ordemstatus.EM_EXECUCAO);
		listaStatus.add(Ordemstatus.AGUARDANDO_CONFIRMACAO);
		listaStatus.add(Ordemstatus.FINALIZADO_DIVERGENCIA);
		listaStatus.add(Ordemstatus.FINALIZADO_SUCESSO);
		
		return query()
					.select("ordemservico.cdordemservico")
					.join("ordemservico.inventariolote inventariolote")
					.where("inventariolote = ?",inventariolote)
					.whereIn("ordemservico.ordemstatus.id", CollectionsUtil.listAndConcatenate(listaStatus, "cdordemstatus", ","))
					.list();
	}
	
	/**
	 * Busca uma ordem de servico do banco de acordo com as regras do caso de uso.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param linhaseparacao
	 * @param deposito
	 * @return
	 */
	public List<Ordemservico> findForTransferencia(Linhaseparacao linhaseparacao,Deposito deposito, Usuario usuario) {
		if(linhaseparacao == null || linhaseparacao.getCdlinhaseparacao() == null || deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("A linha de separação não deve ser nula.");
		return query()
				.select("ordemservico.cdordemservico,transferencia.cdtransferencia,transferenciastatus.cdtransferenciastatus")
				.join("ordemservico.transferencia transferencia")
				.join("transferencia.transferenciastatus transferenciastatus")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.listaOrdemProdutoLigacao opl")
				.join("opl.ordemservicoproduto osp")
				.join("osp.produto produto")
				.join("produto.listaDadoLogistico dl")
				.join("dl.deposito deposito")
				.join("dl.linhaseparacao linhaseparacao")
				.leftOuterJoin("ordemservico.listaOrdemServicoUsuario osu")
				.where("dl.deposito = ?",deposito)
				.where("transferencia.deposito = ?",deposito)
				.where("linhaseparacao = ?",linhaseparacao)
				.openParentheses()
					.where("transferenciastatus = ?",Transferenciastatus.EM_ABERTO)
					.or()
					.where("transferenciastatus = ?",Transferenciastatus.EM_EXECUCAO)
				.closeParentheses()
				.where("ordemtipo = ?",Ordemtipo.TRANSFERENCIA)
				.openParentheses()
					.where("ordemstatus=?",Ordemstatus.EM_ABERTO)
					.or()
					.openParentheses()
						.where("ordemstatus=?",Ordemstatus.EM_EXECUCAO)
						.where("osu.usuario=?", usuario)
					.closeParentheses()
				.closeParentheses()
				.orderBy("transferencia.cdtransferencia desc,ordemservico.cdordemservico desc")
				.list();
	}
	
	/**
	 * Encontra as ordens do carregamento dependendo do tipo.
	 * Caso o tipo não seja informado retorna todas as ordens de serviço do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @param ordemtipo
	 * @return
	 */
	public List<Ordemservico> findByCarregamentoAndOrdemTipo(Carregamento carregamento, Ordemtipo ordemtipo) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo.");
		return query()
					.select("ordemservico.cdordemservico, ordemstatus.cdordemstatus")
					.join("ordemservico.ordemstatus ordemstatus")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.carregamento carregamento")
					.where("carregamento = ?",carregamento)
					.where("ordemtipo = ?",ordemtipo)
					.list();
	}
	
	/**
	 * Encontra todas as ordens da transferência
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param transferencia
	 * @return
	 */
	public List<Ordemservico> findByTransferencia(Transferencia transferencia,Ordemstatus ordemstatus) {
		if(transferencia == null || transferencia.getCdtransferencia() == null)
			throw new WmsException("A transferência não deve ser nula.");
		return query()
					.select("ordemservico.cdordemservico,ordemstatus.cdordemstatus")
					.join("ordemservico.transferencia transferencia")
					.join("ordemservico.ordemstatus ordemstatus")
					.where("transferencia = ?",transferencia)
					.where("ordemstatus = ?",ordemstatus)
					.list();
	}
	
	
	/**
	 * Busca a ordem de serviço e carrega os dados de inventário
	 * 
	 * @author Pedro Gonçalves
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadAllOSInfo(Ordemservico ordemservico, Usuario usuario){
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("ordemservico.cdordemservico, ordemtipo.cdordemtipo, deposito.cddeposito, ordemstatus.cdordemstatus, " +
						"inventariolote.cdinventariolote, inventariolote.fracionada,ordemservicoprincipal.cdordemservico")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.deposito deposito")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.inventariolote inventariolote")
				.leftOuterJoin("ordemservico.ordemservicoprincipal ordemservicoprincipal")
				.leftOuterJoin("ordemservico.listaOrdemServicoUsuario ordemservicousuario")
				.leftOuterJoin("ordemservicousuario.usuario usuario")
				.where("1=1")
				.openParentheses()
				.openParentheses()
					.where("ordemstatus = ? ", Ordemstatus.EM_ABERTO)
					.closeParentheses()
					.or()
					.openParentheses()
						.where("ordemstatus = ? ", Ordemstatus.EM_EXECUCAO)
						.where("usuario=?", usuario)
					.closeParentheses()
				.closeParentheses()
				.entity(ordemservico)
				.unique();
		
	
	}
	

		
	
	
	/**
	 * Altera a ordemServicoPrincipal das ordens fornecidas
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordens
	 * @param ordemservico
	 * @return
	 */
	public void updateOrdemServicoPrincipal(String ordens,Ordemservico ordemservico) {
		
		if(ordemservico == null || ordemservico.getCdordemservico() == null || ordens == null || ordens.equals("") || ordens.endsWith(",")){
			throw new WmsException("Não foi possível executar a função updateOrdemServicoPrincipal.");
		}
		getHibernateTemplate().bulkUpdate("update Ordemservico set cdordemservicoprincipal = ? " +
								"where cdordemservico in ("+ordens+")",new Object[]{ordemservico.getCdordemservico()});
	}
	
	/**
	 * Encontra a ordem pelo pai e seu tipo
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @param ordemtipo
	 * @return
	 */
	public List<Ordemservico> findPrincipalAndTipo(Ordemservico ordemservico,Ordemtipo ordemtipo) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null || ordemtipo == null || ordemtipo.getCdordemtipo() == null)
			throw new WmsException("Não foi possível excutar a função findPrincipalAndTipo.");

		return query()
				.select("ordemservico.cdordemservico")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.ordemservicoprincipal ordemservicoprincipal")
				.where("ordemservicoprincipal = ?",ordemservico)
				.where("ordemtipo = ?",ordemtipo)
				.list()
				;
	}
	
	/**
	 * Atualiza o status de todas as ordens de serviço do lote
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventario
	 * @param ordemstatus
	 */
	public void updateStatusOrdemservicoByLote(Inventariolote lote,Ordemstatus ordemstatus) {
		if(lote == null || lote.getCdinventariolote() == null || ordemstatus == null || ordemstatus.getCdordemstatus() == null)
			throw new WmsException("Erro ao excecutar a função updateStatusOrdemservicoByLote");
		
		getHibernateTemplate().bulkUpdate("update Ordemservico set ordemstatus = ? where inventariolote = ?",
										new Object[]{ordemstatus,lote});
	}
	
	/**
	 * 
	 * Método que as ordens de serviço de um determinado inventário lote
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Ordemservico>
	 * 
	 */
	public List<Ordemservico> loadByInventarioLote(Inventariolote filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("ordemservico.cdordemservico, ordemservico.ordem, ordemstatus.cdordemstatus, ordemstatus.nome," +
							"ordemtipo.cdordemtipo, ordemtipo.nome, osu.cdordemservicousuario, osu.dtinicio, osu.dtfim," +
							"usuario.cdpessoa, usuario.nome")
					.join("ordemservico.inventariolote inventariolote")
					.join("ordemservico.ordemstatus ordemstatus")
					.join("ordemservico.ordemtipo ordemtipo")
					.leftOuterJoin("ordemservico.listaOrdemServicoUsuario osu")
					.leftOuterJoin("osu.usuario usuario")
					.where("inventariolote = ?", filtro)
					.list();
	}

	/**
	 * Encontras as ordens utilizadas para fazer o reabastecimento corretivo de picking
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param linhaseparacao
	 * @param deposito
	 * @return
	 */
	public List<Ordemservico> findForReabastecimento(Linhaseparacao linhaseparacao, Deposito deposito, Usuario usuario) {
		if(linhaseparacao == null || linhaseparacao.getCdlinhaseparacao() == null || deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("A linha de separação não deve ser nula.");
		return query()
				.select("ordemservico.cdordemservico, carregamento.cdcarregamento,lote.cdreabastecimentolote," +
						"reabastecimento.cdreabastecimento,expedicao.cdexpedicao")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.listaOrdemProdutoLigacao opl")
				.join("opl.ordemservicoproduto osp")
				.join("osp.produto produto")
				.join("produto.listaDadoLogistico dl")
				.join("dl.linhaseparacao linhaseparacao")
				.leftOuterJoin("ordemservico.carregamento carregamento")
				.leftOuterJoin("ordemservico.expedicao expedicao")
				.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
				.leftOuterJoin("ordemservico.reabastecimentolote lote")
				.leftOuterJoin("lote.reabastecimento reabastecimento")
				.leftOuterJoin("ordemservico.listaOrdemServicoUsuario osu")

				.where("dl.deposito = ?",deposito)
				.where("linhaseparacao = ?",linhaseparacao)
				.where("ordemtipo = ?",Ordemtipo.REABASTECIMENTO_PICKING)
				.where("ordemservico.deposito = ? ", deposito)
				.openParentheses()
					.where("ordemstatus = ?",Ordemstatus.EM_ABERTO)
					.or()
					.openParentheses()
						.where("ordemstatus = ?",Ordemstatus.EM_EXECUCAO)
						.where("osu.usuario = ?", usuario)
					.closeParentheses()
				.closeParentheses()
				.orderBy("carregamento.cdcarregamento desc,ordemservico.cdordemservico desc")
				.list();
	}
	
	/**
	 * Chama a procedure GERAR_ORDEMTRANSFERENCIA
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param bean
	 * @param deposito
	 * @throws SQLException 
	 */
	public void gerarOrdemTransferencia(Transferencia transferencia, Deposito deposito) throws SQLException {
		if(deposito == null || deposito.getCddeposito() == null || transferencia == null || transferencia.getCdtransferencia() == null)
			throw new WmsException("Dados insuficientes para invocar a função 'GERAR_ORDEMTRANSFERENCIA'");
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		try{
			cstmt = (CallableStatement) conn.prepareCall("BEGIN GERAR_ORDEMTRANSFERENCIA(:1, :2); END;");
			cstmt.setInt(1, deposito.getCddeposito());
			cstmt.setInt(2, transferencia.getCdtransferencia());
			
			cstmt.execute();
		}catch(Exception e){
			throw new WmsException(e.getMessage());
		}
		finally{
			cstmt.close();
			conn.close();
		}
	}

	/**
	 * Obtém uma lista de itens que deverão ser exibidos na tela de corte.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param faturamentoOutraFilial 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemCorteVO> getListaItemCorteVO(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("O parâmetro \"Ordem de Serviço\" é obrigatório.");
		
		StringBuilder sql = new StringBuilder();
		String qtdeesperada, qtdeconfirmada;
		Boolean caixaMestre = ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito());
		if(caixaMestre){
			qtdeesperada = " osp.qtdeesperada * pe.qtde ";
			qtdeconfirmada = " ci.qtdeconfirmada ";
		}else{
			qtdeesperada = " pvp.qtde ";
			qtdeconfirmada = " sum(ci.qtdeconfirmada) ";
		}
		
		sql.append("SELECT cdproduto, codigo, descricao, cdpessoa, nome, numero, cdpedidovenda, faturadooutrafilial, Sum(qtdeesperada) AS qtdeesperada, Sum(qtdecoletor) AS qtdecoletor, Sum(qtdeconfirmada) AS qtdeconfirmada ");
		sql.append("from ( ");
		sql.append("select p.cdproduto, p.codigo, p.descricao, c.cdpessoa, c.nome, pv.numero, pv.cdpedidovenda, case when count(distinct v.cdproduto) > 1 then sum(pvp.qtde) / count(v.cdproduto) else  sum(").append(qtdeesperada).append(") end  as qtdeesperada, ");  
		sql.append("  min(Nvl(v.qtdecoletor, 0)) as qtdecoletor,  case when count(distinct v.cdproduto) > 1 then sum(ci.qtdeconfirmada) / count(v.cdproduto) else  ").append(qtdeconfirmada).append("end as qtdeconfirmada  ");
		sql.append(", case when (pes.documento <> d.cnpj) then 'true' else 'false' end as faturadoOutraFilial ");
		sql.append("from vcarregamentocorte v  ");
		sql.append("  join carregamentoitem ci on ci.cdcarregamentoitem = v.cdcarregamentoitem "); 
		sql.append("  join produto p on v.cdprodutoprincipal = p.cdproduto  ");
		sql.append("  join pedidovendaproduto pvp on pvp.cdpedidovendaproduto = v.cdpedidovendaproduto "); 
		sql.append("  join pedidovenda pv on pv.cdpedidovenda = pvp.cdpedidovenda  ");
		sql.append("  join pessoa c on pv.cdcliente = c.cdpessoa ");
		sql.append("  left join deposito d on d.cddeposito = pvp.cddeposito ");
		sql.append("  left join cliente cli on cli.cdpessoa = pvp.cdfilialnota ");
		sql.append("  left join pessoa pes on pes.cdpessoa = cli.cdpessoa ");
		
		if(caixaMestre){
			sql.append("  join ordemservicoproduto osp on osp.cdordemservicoproduto = v.cdordemservicoproduto ");
			sql.append("  join produtoembalagem pe on pe.cdprodutoembalagem = osp.cdprodutoembalagem ");			
		}
			
		sql.append("where v.cdordemservico =  ?  ");
		
		if(ordemservico.getCarregamento() != null && ordemservico.getCarregamento().getCarregamentostatus().equals(Carregamentostatus.FINALIZADO))
			sql.append(" and pes.documento <> d.cnpj ");
		
		sql.append("group by p.cdproduto, p.codigo, p.descricao, c.cdpessoa, c.nome, pv.numero, pv.cdpedidovenda, pvp.cdpedidovendaproduto, pvp.carregado, pes.documento, d.cnpj ");
		if(caixaMestre)
			sql.append(" ,ci.qtdeconfirmada ");
		sql.append(") GROUP BY cdproduto, codigo, descricao, cdpessoa, nome, numero, cdpedidovenda, faturadooutrafilial ");
		sql.append("order by descricao, nome ");
		
		List<ItemCorteVO> itens = (List<ItemCorteVO>) getJdbcTemplate().query(sql.toString(), new Integer[]{ordemservico.getCdordemservico()} , new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				List<ItemCorteVO> resultado = new ArrayList<ItemCorteVO>();
				
				while (rs.next()){
					ItemCorteVO itemCorteVO = new ItemCorteVO();
					itemCorteVO.setCliente(new Cliente(rs.getInt("cdpessoa"), rs.getString("nome")));
					itemCorteVO.setProduto(new Produto(rs.getInt("cdproduto"), rs.getString("codigo"), rs.getString("descricao")));
					itemCorteVO.setNumeroPedido(rs.getString("numero"));
					
					if (rs.getObject("qtdeconfirmada") != null)
						itemCorteVO.setQtdeConfirmada(rs.getInt("qtdeconfirmada"));
					
					if (rs.getObject("qtdecoletor") != null){
						itemCorteVO.setQtdeColetada(rs.getInt("qtdecoletor"));
					}
					
					if (rs.getObject("qtdeesperada") != null){
						itemCorteVO.setQtdeEsperada(rs.getInt("qtdeesperada"));
					}

					itemCorteVO.setFaturadoOutraFilial(new Boolean(rs.getString("faturadoOutraFilial")));
					itemCorteVO.setCdpedidovenda(rs.getInt("cdpedidovenda"));
					
					resultado.add(itemCorteVO);
				}
				
				return resultado;
			}
			
		});
		
		return itens;
	}

	/**
	 * Carrega a ordem de serviço para a baixa de mapa. Irá carregar a OS e os itens dela.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadForBaixaMapa(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("ordemservico.cdordemservico, ordemtipo.cdordemtipo, ordemstatus.cdordemstatus," +
						"ordemprodutoligacao.cdordemprodutoligacao,ordemservicoproduto.cdordemservicoproduto," +
						"ordemprodutostatus.cdordemprodutostatus,carregamento.cdcarregamento")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.carregamento carregamento")
				.join("ordemservico.ordemstatus ordemstatus")
				.join("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
				.join("ordemprodutoligacao.ordemservicoproduto ordemservicoproduto")
				.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
				.entity(ordemservico)
				.unique();
	}
	
	/**
	 * Altera o status da ordem
	 * @param bean
	 * @param status
	 * @author Cíntia Nogueira
	 */
	public void alteraStatus(Ordemservico bean, Ordemstatus status){
		if(bean == null || bean.getCdordemservico()==null || status==null || status.getCdordemstatus()==null){
			throw new WmsException("Parâmetros inválidos");
		}
		getHibernateTemplate().bulkUpdate("update Ordemservico set ordemstatus=?" 
				 +" where cdordemservico=?", 
				 new Object[]{ status,bean.getCdordemservico()});
	}


	/**
	 * Retorna as ordens de servico
	 * @param recebimento
	 * @param ordemstatus
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Ordemservico> loadBy(Recebimento recebimento, Ordemstatus ordemstatus){
		if(recebimento == null || recebimento.getCdrecebimento() == null || ordemstatus==null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
				.select("ordemservico.cdordemservico")
				.join("ordemservico.recebimento recebimento")
				.where("recebimento=?",recebimento)
				.where("ordemservico.ordemstatus=?", ordemstatus)
				.list();
	}

	/**
	 * Carrega a ordem de serviço com as {@link OrdemservicoUsuario} associadas.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadWithUsuario(Ordemservico ordemservico) {
		if(ordemservico == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("ordemservico.cdordemservico, ordemstatus.cdordemstatus, ordemstatus.nome," +
							"OrdemservicoUsuario.cdordemservicousuario,OrdemservicoUsuario.dtinicio," +
							"OrdemservicoUsuario.dtfim,usuario.cdpessoa,usuario.nome,inventariolote.cdinventariolote," +
							"inventariolote.fracionada, ordemservicoprincipal.cdordemservico")
					.join("ordemservico.inventariolote inventariolote")
					.join("ordemservico.ordemstatus ordemstatus")
					.leftOuterJoin("ordemservico.ordemservicoprincipal ordemservicoprincipal")
					.leftOuterJoin("ordemservico.listaOrdemServicoUsuario OrdemservicoUsuario")
					.leftOuterJoin("OrdemservicoUsuario.usuario usuario")
					.where("ordemservico = ?", ordemservico)
					.orderBy("OrdemservicoUsuario.cdordemservicousuario desc")
					.unique();
	}

	/**
	 * Retorna os dados para o relatório de etiquetas UMA (endereçamento em embalagem padrão)
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<EtiquetasumaVO> findForReportEtiquetaUma(EtiquetaumaFiltro filtro) {
		if(filtro == null || filtro.getRecebimento() == null || filtro.getRecebimento().getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object[]> list = newQueryBuilder(Object[].class)
				.from(Ordemservico.class)
				.select("produto.codigo, produto.descricao, areadestino.codigo, areapicking.codigo, enderecodestino.endereco," +
						"picking.endereco,ordemservicoprodutoendereco.qtde,embalagem.qtde,embalagem.descricao," +
						"produtotipopalete.lastro,produtotipopalete.camada,produto.cdproduto,enderecodestino.cdendereco," +
						"recebimento.cdrecebimento")
				.join("ordemservico.recebimento recebimento")
				.join("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
				.join("ordemprodutoligacao.ordemservicoproduto ordemservicoproduto")
				.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
				.join("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.join("enderecodestino.area areadestino")
				.join("ordemservicoproduto.produto produto")
				.join("produto.listaDadoLogistico dadologistico with dadologistico.deposito.cddeposito = " + WmsUtil.getDeposito().getCddeposito())
				.leftOuterJoin("dadologistico.endereco picking")
				.leftOuterJoin("picking.area areapicking")
				.leftOuterJoin("produto.listaProdutoEmbalagem embalagem with embalagem.compra = true")
				.leftOuterJoin("produto.listaProdutoTipoPalete produtotipopalete with produtotipopalete.padrao = true and produtotipopalete.deposito.cddeposito = " + WmsUtil.getDeposito().getCddeposito())
				.where("recebimento = ?", filtro.getRecebimento())
				.where("ordemservico.ordemtipo = ?", Ordemtipo.ENDERECAMENTO_PADRAO)
				.where("ordemservico.deposito = ?", WmsUtil.getDeposito())
			.setUseTranslator(false)
			.list();
		
		List<EtiquetasumaVO> listaEtUma = new ArrayList<EtiquetasumaVO>();

		for (Object[] linha : list){
			EtiquetasumaVO etUma = new EtiquetasumaVO();					
			etUma.setData(NeoFormater.getInstance().format(new Date(System.currentTimeMillis())));
			
			String areaDestino = linha[2].toString();
			if (areaDestino.length() < 2)
				areaDestino = "0" + areaDestino;

			String areaDadoLogistico = linha[3] != null ? linha[3].toString() : null;
			if (areaDadoLogistico != null && areaDadoLogistico.length() < 2)
				areaDadoLogistico = "0" + areaDadoLogistico;
			if (areaDadoLogistico != null && linha[3] != null)
				etUma.setEnderecoDadoLogistico(areaDadoLogistico + "." + (String) linha[5]);
			
			if (areaDestino != null && linha[4] != null)
				etUma.setEnderecoDestino(areaDestino + "." + (String) linha[4]);

			etUma.setProdutoCodigoDescricao(linha[0] + " - " + linha[1]);

			if(linha[6] != null){
				etUma.setCalculoQtde(((Long)linha[6]) / ((linha[7] == null) ? 1 : ((Long)linha[7])));
			}else{
				etUma.setCalculoQtde(1L);
			}
			
			etUma.setProdutoEmbalagemDescricao((String) linha[8] + " - " + ((linha[7] == null) ? 1 : ((Long)linha[7])));						
			etUma.setLastroTipoPaleteCamada((Long) linha[9]);
			etUma.setCamadaTipoPaleteCamada((Long) linha[10]);

			Produto produto = new Produto((Integer) linha[11]);
			Endereco endereco = new Endereco((Integer) linha[12]);
			Enderecoproduto uma = EnderecoprodutoService.getInstance().loadByEnderecoEProdutoUma(endereco, produto);

			etUma.setRecebimento((Integer) linha[13]);
			
			if (uma != null)
				etUma.setEnderecoProdutoId(String.format("%010d", uma.getCdenderecoproduto()));		
			
			listaEtUma.add(etUma);

		}
		
		return listaEtUma;
	}
	
	/**
	 * Verifica se o recebimento possui etiqueta de UMA quando é um endereçamento manual.
	 * 
	 * @param recebimento
	 * @return
	 */
	public boolean possuiUmaManual(Recebimento recebimento){
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object[]> list = newQueryBuilder(Object[].class)
			.from(Ordemservico.class)
			.select("umareserva.cdumareserva,enderecoproduto.cdenderecoproduto")
			.join("ordemservico.recebimento recebimento")
			.join("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
			.join("ordemprodutoligacao.ordemservicoproduto ordemservicoproduto")
			.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
			.join("ordemservicoprodutoendereco.listaUmareserva umareserva")
			.join("umareserva.enderecoproduto enderecoproduto")
			.where("recebimento = ?", recebimento)
		.setUseTranslator(false)
		.setMaxResults(1)
		.list();
		
		return list != null && list.size() > 0;
	}
	
	/**
	 * Retorna os dados para o relatório de etiquetas UMA (endereçamento em embalagem padrão)
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<EtiquetasumaVO> findForReportEtiquetaUmaManual(EtiquetaumaFiltro filtro) {
		if(filtro == null || filtro.getRecebimento() == null || filtro.getRecebimento().getCdrecebimento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object[]> list = newQueryBuilder(Object[].class)
				.from(Ordemservico.class)
				.select("produto.codigo, produto.descricao, areadestino.codigo, areapicking.codigo, enderecodestino.endereco," +
						"picking.endereco,ordemservicoprodutoendereco.qtde,embalagem.qtde,embalagem.descricao," +
						"produtotipopalete.lastro,produtotipopalete.camada,produto.cdproduto,enderecodestino.cdendereco," +
						"recebimento.cdrecebimento,umareserva.cdumareserva,enderecoproduto.cdenderecoproduto")
				.join("ordemservico.recebimento recebimento")
				.join("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
				.join("ordemprodutoligacao.ordemservicoproduto ordemservicoproduto")
				.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.leftOuterJoin("ordemservicoprodutoendereco.listaUmareserva umareserva")
				.leftOuterJoin("umareserva.enderecoproduto enderecoproduto")
				.leftOuterJoin("enderecodestino.area areadestino")
				.join("ordemservicoproduto.produto produto")
				.join("produto.listaDadoLogistico dadologistico with dadologistico.deposito.cddeposito = " + WmsUtil.getDeposito().getCddeposito())
				.leftOuterJoin("dadologistico.endereco picking")
				.leftOuterJoin("picking.area areapicking")
				.leftOuterJoin("produto.listaProdutoEmbalagem embalagem with embalagem.compra = true")
				.leftOuterJoin("produto.listaProdutoTipoPalete produtotipopalete with produtotipopalete.padrao = true and produtotipopalete.deposito.cddeposito = " + WmsUtil.getDeposito().getCddeposito())
				.where("recebimento = ?", filtro.getRecebimento())
				.where("ordemservico.ordemtipo = ?", Ordemtipo.ENDERECAMENTO_PADRAO)
				.where("ordemservico.deposito = ?", WmsUtil.getDeposito())
			.setUseTranslator(false)
			.list();
		
		List<EtiquetasumaVO> listaEtUma = new ArrayList<EtiquetasumaVO>();

		for (Object[] linha : list){
			EtiquetasumaVO etUma = new EtiquetasumaVO();					
			etUma.setData(NeoFormater.getInstance().format(new Date(System.currentTimeMillis())));
			
			String areaDadoLogistico = linha[3] != null ? linha[3].toString() : null;
			if (areaDadoLogistico != null && areaDadoLogistico.length() < 2)
				areaDadoLogistico = "0" + areaDadoLogistico;
			if (areaDadoLogistico != null && linha[3] != null)
				etUma.setEnderecoDadoLogistico(areaDadoLogistico + "." + (String) linha[5]);
			
			etUma.setProdutoCodigoDescricao(linha[0] + " - " + linha[1]);

			if(linha[6] != null){
				etUma.setCalculoQtde(((Long)linha[6]) / ((linha[7] == null) ? 1 : ((Long)linha[7])));
			}else{
				etUma.setCalculoQtde(1L);
			}
			
			etUma.setProdutoEmbalagemDescricao((String) linha[8] + " - " + ((linha[7] == null) ? 1 : ((Long)linha[7])));						
			etUma.setLastroTipoPaleteCamada((Long) linha[9]);
			etUma.setCamadaTipoPaleteCamada((Long) linha[10]);

			if (linha[12] != null){
				String areaDestino = linha[2].toString();
				if (areaDestino.length() < 2)
					areaDestino = "0" + areaDestino;
				
				if (areaDestino != null && linha[4] != null)
					etUma.setEnderecoDestino(areaDestino + "." + (String) linha[4]);				
			}
			
			etUma.setRecebimento((Integer) linha[13]);
			
			if ( linha[15] != null)
				etUma.setEnderecoProdutoId(String.format("%010d",  (Integer) linha[15]));		
			
			listaEtUma.add(etUma);

		}
		
		return listaEtUma;
	}
	
	/**
	 * Método que chama procedure ATUALIZAR_RASTREABILIDADEDOC no banco
	 * 
	 * @param ordem
	 * @author Tomás Rabelo
	 */
	public void callProcedureAtualizarRastreabilidade(final Ordemservico ordem) {
		if (ordem == null || ordem.getCdordemservico() == null) {
			throw new WmsException("Erro na passagem de parâmetro.");
		}
		
		getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				session.getNamedQuery("atualizar_rastreabilidadedoc").setInteger(0, ordem.getCdordemservico()).executeUpdate();
				
				return null;
			}
			
		});
		
	}
	
	/**
	 * Preenche a ordem de conferência de Box com as quantidades conferidas na primeira conferência.
	 * Chama o procedure GERAR_CONFERENCIA_BOX na base de dados.
	 * 
	 * @param ordem A primeira ordem de conferência.
	 * @author Giovane Freitas
	 */
	public void gerarConferenciaBox(final Ordemservico ordem) {
		if (ordem == null || ordem.getCdordemservico() == null) {
			throw new WmsException("Erro na passagem de parâmetro.");
		}
		try{
			getHibernateTemplate().execute(new HibernateCallback(){
				
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("gerar_conferencia_box").setInteger(0, ordem.getCdordemservico()).executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			e.printStackTrace();
			throw new WmsException("Erro ao realizar a geração de conferencia por box.", e);
		}
	}
	
	/**
	 * Busca a ordem de serviço e seus itens divergentes para poder criar uma ordem de recontagem ou uma segunda ordem de contagem.
	 * 
	 * @author Giovane Freitas 
	 * @param ordemservico
	 * @return 
	 */
	public Ordemservico findForCriarContagemRecontagem(Ordemservico ordemservico, boolean contagem) {
		QueryBuilder<Ordemservico> queryBuilder = 
			query()
				.select("inventariolote.cdinventariolote, ordemprodutoligacao.cdordemprodutoligacao,ordemservicoproduto.cdordemservicoproduto," +
						"ordemservicoproduto.qtdeesperada,ordemprodutostatus.cdordemprodutostatus,produto.cdproduto," +
						"ordemservicoprodutoendereco.cdordemservicoprodutoendereco,ordemservicoprodutoendereco.qtde," +
						"enderecodestino.cdendereco,ordemservico.cdordemservico,ordemservico.ordem")
				.join("ordemservico.inventariolote inventariolote")
				.join("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
				.join("ordemprodutoligacao.ordemservicoproduto ordemservicoproduto")
				.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
				.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
				.join("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.where("ordemservico = ?", ordemservico);
		
		if (contagem) { // Lista todos os endereços, independente de terem produtos
			queryBuilder.leftOuterJoin("ordemservicoproduto.produto produto");
		}
		else { // Lista somente os endereços com produtos
			queryBuilder.join("ordemservicoproduto.produto produto");
		}
		
		return queryBuilder.unique();
	}



	/**
	 * Lista os dados para impressão do relatório de ordem de armazenagem.
	 * 
	 * @author Giovane Freitas
	 * @param cdOs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OrdemVO> findForReportArmazenagem(final String cdOs) {
		if(cdOs == null || cdOs.equals(""))
			throw new WmsException("É necessário informar uma lista de cd de Ordemservico.");
		
		 List<OrdemVO> itens = getHibernateTemplate().executeFind(new HibernateCallback(){

			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder hql = new StringBuilder();
				hql.append("select ordem.cdordemservico as os,b.cdbox as box,lpad(to_char(a.codigo), 2, '0') || '.' || ed.endereco as enderecoDestinoStr, ");
				hql.append("		p.codigo || ' - ' || p.descricao as produto,pe.descricao as embalagem,ospe.qtde as qtde, ");
				hql.append("		ot.nome as tipoArmazenagem, r.cdrecebimento as recebimento, pe.qtde as qtdeEmbalagem, ");
				hql.append("		ot.cdordemtipo as cdordemtipo, p.cdproduto as cdproduto ");
				hql.append("from Ordemservico as  ordem ");
				hql.append("	join ordem.ordemtipo as ot ");
				hql.append("	join ordem.listaOrdemProdutoLigacao as opl ");
				hql.append("	join opl.ordemservicoproduto as osp ");
				hql.append("	join osp.produto as p ");
				hql.append("	join p.listaProdutoEmbalagem as pe with pe.compra is true ");
				hql.append("	left join osp.listaOrdemservicoprodutoendereco as ospe ");
				hql.append("	left join ospe.enderecodestino as ed ");
				hql.append("	left join ed.area a ");
				hql.append("	left join a.listaEnderecosentido as es ");
				hql.append("	join ordem.recebimento as r ");
				hql.append("	join r.box as b ");
				hql.append("where (es.rua = ed.rua or ed.cdendereco is null)  ");
				hql.append("	and ordem.cdordemservico in (").append(cdOs).append(") ");
				
				//O order by listaProdutoEmbalagem.compra desc é crucial, pois quando trata-se de coleta padrão
				//o relatório exibe a primeira embalem retornada, que deve ser obrigatoriamente a embalagem de compra (compra=1)
				//Apesar de o caso de uso não mandar ordenar por O.S. devo fazê-lo senão uma mesma O.S. será quebrada em várias páginas
				hql.append("order by ordem.cdordemservico,ed.rua, (ed.predio*es.sentido), ");
				hql.append("	ed.nivel,ed.apto,p.codigo,pe.compra desc,pe.qtde ");
				
				Query query = session.createQuery(hql.toString());
				query.setResultTransformer(new AliasToBeanResultTransformer(OrdemVO.class));
				
				return query.list();
			}
			
		});
		 
		for (OrdemVO item : itens){
			//se for fracionado/avariado a embalagem deve ser a de menor unidade
			if (Ordemtipo.ENDERECAMENTO_AVARIADO.getCdordemtipo().equals(item.getCdordemtipo()) ||
					Ordemtipo.ENDERECAMENTO_FRACIONADO.getCdordemtipo().equals(item.getCdordemtipo())){
				
				Produtoembalagem embalagem = ProdutoembalagemService.getInstance().findMenorEmbalagem(new Produto(item.getCdproduto()));
				item.setEmbalagem(embalagem.getDescricao());
				
			//se for coleta padrão deve dividir a quantidade pela qtde da embalagem
			}else if (item.getQtde() != null && item.getQtdeEmbalagem() != null && item.getQtdeEmbalagem() > 0L)
				item.setQtde(item.getQtde()/item.getQtdeEmbalagem());
		}
		
		return itens;
	}

	/**
	 * Método que verifica se o Mapa de separação possui algum item que foi faturado em outra filial
	 * 
	 * @param ordemservico
	 * @return
	 * @author Tomás Rabelo
	 */
	public Boolean hasItensCarregamentoFaturadosOutraFilial(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parametros inválidos.");
		/*
		return newQueryBuilderWithFrom(Long.class)
			.select("count(*)")
			.join("ordemservico.carregamento carregamento")
			.join("carregamento.listaCarregamentoitem listaCarregamentoitem")
			.join("listaCarregamentoitem.pedidovendaproduto pedidovendaproduto")
			.join("pedidovendaproduto.deposito deposito")
			.join("pedidovendaproduto.listaCarregamentoitem listaCarregamentoitem")
			.join("pedidovendaproduto.filialnota filialnota")
			.where("ordemservico = ?", ordemservico)
			.where("pedidovendaproduto.carregado = ?", Boolean.TRUE)
			.where("filialnota.documento <> deposito.cnpj")
			.setUseTranslator(false)
			.unique()
			.longValue()>0;
		*/
		StringBuilder sql = new StringBuilder();
		sql
			.append("select count(*) ")
			.append("from Vcarregamentocorte vcc ")
			.append("join Pedidovendaproduto pvp on vcc.cdpedidovendaproduto = pvp.cdpedidovendaproduto ")
			.append("join Deposito d on d.cddeposito = pvp.cddeposito ")
			.append("join Cliente c on c.cdpessoa = pvp.cdfilialnota ")
			.append("join Pessoa p on p.cdpessoa = c.cdpessoa ")
			.append("where vcc.cdordemservico = "+ ordemservico.getCdordemservico()+" and ")
			.append("pvp.carregado = 1 and p.documento <> d.cnpj");
			
		Long qtde = getJdbcTemplate().queryForLong(sql.toString());
		return qtde > 0;
	}


	/**
	 * Localiza todas as ordens de serviço associadas a um determinado usuário.
	 * 
	 * @author Giovane Freitas
	 * @param usuario O usuário ao qual a ordem está associada.
	 */
	public List<Ordemservico> findOSByUsuario(Usuario usuario, Recebimento recebimento) {
		if (usuario == null || usuario.getCdpessoa() == null) {
			throw new WmsException("Erro na passagem de parâmetro.");
		}
		
		QueryBuilder<Ordemservico> queryBuilder = query()
			.joinFetch("ordemservico.listaOrdemServicoUsuario osu")
			.joinFetch("osu.usuario u")
			.where("osu.usuario = ?", usuario)
			.where("ordemservico.recebimento = ?", recebimento)
			.where("ordemservico.ordemstatus = ?", Ordemstatus.EM_EXECUCAO)
			.openParentheses()
				.where("ordemservico.ordemtipo = ?", Ordemtipo.ENDERECAMENTO_AVARIADO)
				.or()
				.where("ordemservico.ordemtipo = ?", Ordemtipo.ENDERECAMENTO_FRACIONADO)
				.or()
				.where("ordemservico.ordemtipo = ?", Ordemtipo.ENDERECAMENTO_PADRAO)
			.closeParentheses();
		
		return queryBuilder.list();
	}

	/**
	 * Localiza todas as movimentações abertas de entrada ou de saída para um
	 * determinado produto em um determinado endereço.
	 * 
	 * @param produto
	 * @param endereco
	 * @param exibirEntradas
	 *            <code>true</code> caso deseje exibir as reservas de entrada
	 *            ou <code>false</code> caso deseje exibir as reservas de
	 *            saída.
	 * @author Giovane Freitas
	 */
	@SuppressWarnings("unchecked")
	public List<MovimentacaoAberta> findMovimentacoesAbertas(final Produto produto, final Endereco endereco, final boolean exibirEntradas) {
		return (List<MovimentacaoAberta>) getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				List<Ordemtipo> tiposInvalidos = new ArrayList<Ordemtipo>();
				tiposInvalidos.add(Ordemtipo.CONFERENCIA_EXPEDICAO_1);
				tiposInvalidos.add(Ordemtipo.RECONFERENCIA_EXPEDICAO_1);
				tiposInvalidos.add(Ordemtipo.CONTAGEM_INVENTARIO);
				tiposInvalidos.add(Ordemtipo.RECONTAGEM_INVENTARIO);
				
				List<Integer> statusCarregamento = new ArrayList<Integer>();
				statusCarregamento.add(Carregamentostatus.EM_SEPARACAO.getCdcarregamentostatus());
				statusCarregamento.add(Carregamentostatus.CONFERIDO.getCdcarregamentostatus());
				if (!WmsUtil.isFinalizadoBaixaEstoque())
					statusCarregamento.add(Carregamentostatus.FINALIZADO.getCdcarregamentostatus());
				statusCarregamento.add(Carregamentostatus.EM_MONTAGEM.getCdcarregamentostatus());

				StringBuilder builder = new StringBuilder();
				builder.append("select sum(ospe.qtde) as qtde, ot.nome as tipoOperacao, os.cdordemservico as ordemServico, ");
				builder.append("	os.carregamento.id as carregamento, os.recebimento.id as recebimento, ex.cdexpedicao as expedicao, ");
				builder.append("	os.transferencia.id as transferencia, rp.cdreabastecimento as reabastecimento, ");
				builder.append("	coalesce(ca.dtcarregamento, r.dtrecebimento, rp.dtreabastecimento) as dataOrdemServico ");
				builder.append("from Ordemservico os ");
				builder.append("	join os.ordemtipo ot ");
				builder.append("	join os.listaOrdemProdutoLigacao ol ");
				builder.append("	join ol.ordemservicoproduto osp ");
				builder.append("	join osp.listaOrdemservicoprodutoendereco ospe ");
				builder.append("	left join os.carregamento ca with ca.carregamentostatus.id in (:statusCarregamento) ");
				builder.append("	left join os.expedicao ex with ex.expedicaostatus.id = 1 ");
				builder.append("	left join os.recebimento r ");
				builder.append("	left join os.reabastecimentolote rl ");
				builder.append("	left join rl.reabastecimento rp ");
				builder.append("where ((os.ordemstatus = :aberta) or (os.ordemstatus = :execucao) or ");
				builder.append("	(ca.id is not null and os.ordemtipo = :mapaSeparacao) or ");//mapas de separação de carregamentos não faturados
				builder.append("	(r.recebimentostatus = :statusRecebimento)) ");//Equanto o recebimento não muda de status para ENDEREÇADO o estoque não é ajustado
				builder.append("	and osp.produto = :produto ");
				builder.append("	and os.ordemtipo not in (:tiposInvalidos)");//desconsidera conferências de expedição e contagens de inventário
				builder.append("	and os.deposito = :deposito ");
				
				if (exibirEntradas)
					builder.append("and ospe.enderecodestino = :endereco ");
				else
					builder.append("and ospe.enderecoorigem = :endereco ");
				
				builder.append("group by ot.nome, os.cdordemservico,os.carregamento.id, os.recebimento.id, os.transferencia.id, rp.id, ");
				builder.append("	coalesce(ca.dtcarregamento, r.dtrecebimento, rp.dtreabastecimento), ex.cdexpedicao ");

				Query query = session.createQuery(builder.toString());
				query.setEntity("produto", produto);				
				query.setEntity("endereco", endereco);
				query.setEntity("aberta", Ordemstatus.EM_ABERTO);
				query.setEntity("execucao", Ordemstatus.EM_EXECUCAO);
				query.setEntity("deposito", WmsUtil.getDeposito());
				query.setEntity("mapaSeparacao", Ordemtipo.MAPA_SEPARACAO);
				query.setEntity("statusRecebimento", Recebimentostatus.EM_ENDERECAMENTO);
				query.setParameterList("statusCarregamento", statusCarregamento);
				query.setParameterList("tiposInvalidos", tiposInvalidos);
				
				query.setResultTransformer(new AliasToBeanResultTransformer(MovimentacaoAberta.class));
				
				return query.list();
			}
			
		});
	}

	/**
	 * Busca a próxima ordem de serviço a ser executada para o processo de convocação ativa.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param usuario
	 * @return
	 */
	public Ordemservico associarProximaOrdem(final Deposito deposito, final Usuario usuario){
		if (usuario == null || usuario.getCdpessoa() == null) {
			throw new WmsException("O usuário não pode ser nulo.");
		}
		if (deposito == null || deposito.getCddeposito() == null) {
			throw new WmsException("O depósito não pode ser nulo.");
		}
		
		try{
			Connection connection = Neo.getObject(JdbcTemplate.class).getDataSource().getConnection();
			try{
				CallableStatement cs = null;
		        cs = connection.prepareCall("{? = call PROXIMA_ORDEM (?, ?)}");
		        
		        cs.registerOutParameter(1,Types.NUMERIC); 
		        cs.setInt(2,deposito.getCddeposito()); 
		        cs.setInt(3,usuario.getCdpessoa()); 
		        cs.execute();
		        Long resposta = cs.getLong(1);
			
		        return new Ordemservico(resposta.intValue());
			}catch (Exception e) {
				throw new WmsException("Erro ao obter a próxima ordem de serviço.", e);
			}finally{
				connection.close();
			}
		}catch (SQLException e) {
			throw new WmsException("Erro ao obter a próxima ordem de serviço.", e);
		}
		
	}

	/**
	 * Carrega as informações da ordem de serviço e sua origem, por exemplo, carregamento,
	 * recebimento, transferência, etc.
	 * 
	 * @author Giovane Freitas
	 * @param ordem
	 * @return
	 */
	public Ordemservico loadOrdemAndOrigem(Ordemservico ordem) {
		if(ordem == null || ordem.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return  query().select("ordemservico.cdordemservico,ordemservico.ordem,ordemtipo.cdordemtipo,ordemtipo.nome," +
				"recebimento.cdrecebimento,carregamento.cdcarregamento,ordemstatus.cdordemstatus," +
				"transferencia.cdtransferencia,inventariolote.cdinventariolote,inventario.cdinventario," +
				"tipoenderecamento.cdtipoenderecamento,box.cdbox,box.nome,veiculo.cdveiculo,veiculo.placa," +
				"veiculo.modelo,reabastecimentolote.cdreabastecimentolote,reabastecimento.cdreabastecimento," +
				"tipooperacao.cdtipooperacao,tipooperacao.nome,tipooperacao.separacliente," +
				"tipooperacao.imprimeetiqueta,tipooperacao.sigla,deposito.cddeposito," +
				"clienteExpedicao.cdpessoa,clienteExpedicao.nome," +
				"ordemprincipal.cdordemservico,tipoprincipal.cdordemtipo," +
				"expedicao.cdexpedicao, boxE.cdbox, boxE.nome")
			.leftOuterJoin("ordemservico.ordemtipo ordemtipo")
			.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
			.leftOuterJoin("ordemservico.deposito deposito")
			.leftOuterJoin("ordemservico.clienteExpedicao clienteExpedicao")
			.leftOuterJoin("ordemservico.recebimento recebimento")
			.leftOuterJoin("recebimento.tipoenderecamento tipoenderecamento")
			.leftOuterJoin("ordemservico.carregamento carregamento")
			.leftOuterJoin("ordemservico.ordemstatus ordemstatus")
			.leftOuterJoin("ordemservico.transferencia transferencia")
			.leftOuterJoin("ordemservico.inventariolote inventariolote")
			.leftOuterJoin("ordemservico.reabastecimentolote reabastecimentolote")
			.leftOuterJoin("ordemservico.expedicao expedicao")
			.leftOuterJoin("inventariolote.inventario inventario")
			.leftOuterJoin("reabastecimentolote.reabastecimento reabastecimento")
			.leftOuterJoin("carregamento.box box")
			.leftOuterJoin("carregamento.veiculo veiculo")
			.leftOuterJoin("ordemservico.ordemservicoprincipal ordemprincipal")
			.leftOuterJoin("ordemprincipal.ordemtipo tipoprincipal")
			.leftOuterJoin("expedicao.box boxE")
			.where("ordemservico.id = ?", ordem.getCdordemservico())
			.unique();
		}
	
	/**
	 * Localiza a ordem de serviço de uma determinada transferência que contém um produto específico.
	 * 
	 * @author Giovane Freitas
	 * @param transferencia
	 * @param produto
	 * @return
	 */
	public Ordemservico findByTransferenciaProduto(Transferencia transferencia, Produto produto){
		if(transferencia == null || transferencia.getCdtransferencia() == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
			.joinFetch("ordemservico.ordemstatus ordemstatus")
			.join("ordemservico.listaOrdemProdutoLigacao opl")
			.join("opl.ordemservicoproduto osp")
			.where("osp.produto = ? ", produto)
			.where("ordemservico.transferencia = ?", transferencia)
			.unique();
	}

	/**
	 * Carrega uma ordem de endereçamento para edição.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Ordemservico loadForEdicaoEnderecamento(Ordemservico ordemservico) {
		return query()
			.joinFetch("ordemservico.ordemstatus status")
			.joinFetch("ordemservico.ordemtipo tipo")
			.joinFetch("ordemservico.recebimento recebimento")
			.joinFetch("recebimento.tipoenderecamento tipoenderecamento")
			.entity(ordemservico)
			.unique();
	}

	/**
	 * Localiza o mapa de separação que contém um determinado produto, para associar como ordem principal na conferência.
	 * 
	 * @author Giovane Freitas
	 * @param expedicao
	 * @param produto
	 * @return
	 */
	public Ordemservico findMapaByProduto(Expedicao expedicao, Produto produto) {
		if(expedicao == null || expedicao.getCdexpedicao() == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetros inválidos");
		
		return query()
			.joinFetch("ordemservico.ordemstatus ordemstatus")
			.join("ordemservico.listaOrdemProdutoLigacao opl")
			.join("opl.ordemservicoproduto osp")
			.where("osp.produto = ? ", produto)
			.where("ordemservico.expedicao = ?", expedicao)
			.where("ordemservico.ordemtipo = ?", Ordemtipo.MAPA_SEPARACAO)
			.setMaxResults(1)
			.unique();
	}

	/**
	 * 
	 * Método que recupera o status e o tipo de uma ordem de serviço
	 * 
	 * @author Giovane Freitas
	 * 
	 * @para filtro
	 * @return Ordemservico
	 * 
	 */
	public List<Ordemservico> findByExpedicao(Expedicao expedicao, Ordemtipo ordemtipo) {
		if(expedicao == null || expedicao.getCdexpedicao() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
					.select("ordemservico.cdordemservico, ordemservico.ordem, ordemtipo.cdordemtipo, ordemtipo.nome, " +
							"ordemstatus.cdordemstatus, ordemstatus.nome, box.cdbox, box.nome, carregamento.cdcarregamento, clienteExpedicao.cdpessoa, " +
							"clienteExpedicao.nome, tipooperacao.cdtipooperacao, tipooperacao.nome, tipooperacao.separacliente, expedicao.cdexpedicao")
					.join("ordemservico.ordemtipo ordemtipo")
					.join("ordemservico.ordemstatus ordemstatus")
					.leftOuterJoin("ordemservico.carregamento carregamento")
					.leftOuterJoin("ordemservico.clienteExpedicao clienteExpedicao")
					.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
					.leftOuterJoin("ordemservico.expedicao expedicao")
					.leftOuterJoin("expedicao.box box")
					.where("ordemservico.expedicao = ?", expedicao)
					.where("ordemservico.ordemtipo = ?", ordemtipo)
					.orderBy("ordemservico.cdordemservico")
					.list();
	}

	/**
	 * Busca as quantidades confirmadas na conferência de uma ordem de serviço, considerando a existência de uma reconferência.
	 * 
	 * @author Giovane Freitas
	 * @param conferencia
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConfirmacaoItemVO> findQtdeConfirmada(final Ordemservico conferencia) {
		final StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT CDCARREGAMENTOITEM, Min(qtdecoletor) as QTDECONFIRMADA ");
		sql.append("FROM ( ");
		sql.append("     SELECT cdcarregamentoitem, osp.cdproduto, SUM(qtdecoletor*NVL(pe.qtde,1)) AS qtdecoletor "); 
		sql.append("    FROM etiquetaexpedicao ee ");
		sql.append("      join ordemservicoproduto osp ON osp.cdordemservicoproduto = ee.cdordemservicoproduto ");
		sql.append("      join ordemprodutoligacao opl ON opl.cdordemservicoproduto = osp.cdordemservicoproduto ");
		sql.append("      join produtoembalagem pe ON osp.cdprodutoembalagem = pe.cdprodutoembalagem ");
		sql.append("    WHERE  ");
		sql.append("      opl.cdordemservico = ? ");
		sql.append("    GROUP BY cdcarregamentoitem, osp.cdproduto ");
		sql.append(") ");
		sql.append("GROUP BY cdcarregamentoitem ");

		//É necessário utilizar HibernateTemplate para garantir que esta consulta será executada na mesma transação que os
		//updates e inserts
		return getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setInteger(0, conferencia.getCdordemservico());
				query.addScalar("CDCARREGAMENTOITEM", Hibernate.INTEGER);
				query.addScalar("QTDECONFIRMADA", Hibernate.LONG);
				List<Object[]> list = query.list();
				
				List<ConfirmacaoItemVO> result = new ArrayList<ConfirmacaoItemVO>();
				for (Object[] array : list){
					ConfirmacaoItemVO itemVO = new ConfirmacaoItemVO();
					itemVO.setCdcarregamentoitem((Integer) array[0]);
					itemVO.setQtdeconfirmada((Long) array[1]);
					result.add(itemVO);
				}
				
				return result;
			}
		});
	}
	
	/**
	 * Busca os dados para criação do relatorio de mapa de separação
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param cds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemSeparacaoVO> findForMapas(EmitirmapaseparacaoPapelFiltro filtro) {
		
		Object[] args = new Object[0];
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT os.cdordemservico, os.cdcarregamento, os.cdexpedicao, ls.nome AS linhaseparacao, Nvl(e.dtexpedicao, c.dtcarregamento) AS data, ");
		sql.append("  Nvl(be.nome, bc.nome) AS box, p.codigo, pp.descricao AS descricaoPrincipal, p.descricao, p.complementocodigobarras,  ");
		sql.append("  sum(osp.qtdeesperada) as qtdeesperada, lpad(a.codigo, 2, '0') as area, e.endereco, sum(ospe.qtde) as qtdeendereco, ");
		sql.append("  cubagemunitaria_produto(osp.cdproduto) as cubagem, pesounitario_produto(osp.cdproduto) as peso ");
		
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito())){
			sql.append(" ,pe.qtde as qtdeembalagem, pe.cdprodutoembalagem ");
		}else{
			sql.append(" ,1 as qtdeembalagem ");
		}
		
		sql.append("FROM ordemservico os ");
		sql.append("  join ordemprodutoligacao opl ON opl.cdordemservico = os.cdordemservico ");
		sql.append("  join ordemservicoproduto osp ON osp.cdordemservicoproduto = opl.cdordemservicoproduto ");
		sql.append("  join dadologistico dl ON dl.cdproduto = osp.cdproduto AND dl.cddeposito = os.cddeposito ");
		sql.append("  join linhaseparacao ls ON ls.cdlinhaseparacao = dl.cdlinhaseparacao ");
		sql.append("  join produto p ON p.cdproduto = osp.cdproduto ");
		sql.append("  left join produto pp ON p.cdprodutoprincipal = pp.cdproduto ");
		sql.append("  left join carregamento c ON c.cdcarregamento = os.cdcarregamento ");
		sql.append("  left join expedicao e ON e.cdexpedicao = os.cdexpedicao ");
		sql.append("  left join box bc ON bc.cdbox = c.cdbox ");
		sql.append("  left join box be ON be.cdbox = e.cdbox ");
		sql.append("  left join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto ");
		sql.append("  left join endereco e on e.cdendereco = ospe.cdenderecoorigem ");
		sql.append("  left join area a on a.cdarea = e.cdarea ");
		sql.append("  left join enderecosentido es on es.cdarea = e.cdarea and es.rua = e.rua ");
		
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito())){
			sql.append("  join produtoembalagem pe on pe.cdprodutoembalagem = osp.cdprodutoembalagem ");			
		}
		
		sql.append("WHERE os.cdordemtipo = 3 ");//Apenas mapas de separação

		if(filtro.getCdsOS() != null && filtro.getCdsOS().length()>0){
			sql.append("  AND os.cdordemservico in (" + filtro.getCdsOS().substring(0, filtro.getCdsOS().length() - 1) + ") ");
		} else if(filtro.getExpedicao() == null || filtro.getExpedicao().getCdexpedicao() != null){
			sql.append("  AND os.cdexpedicao = ? ");
			args = new Integer[]{filtro.getExpedicao().getCdexpedicao()};
		} else {
			sql.append("  AND os.cdcarregamento = ? ");
			args = new Integer[]{filtro.getCarregamento().getCdcarregamento()};
		}
		
		sql.append("group by os.cdordemservico, os.cdcarregamento, os.cdexpedicao, ls.nome, Nvl(e.dtexpedicao, c.dtcarregamento), ");
		sql.append("  Nvl(be.nome, bc.nome), p.codigo, pp.descricao, p.descricao, p.complementocodigobarras, osp.cdproduto, ");
		sql.append("  lpad(a.codigo, 2, '0'), e.endereco, e.rua, es.sentido, e.nivel, e.apto, pp.descricao, p.descricao, a.codigo ");
		if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito())){
			sql.append("  ,pe.qtde ,pe.cdprodutoembalagem ");			
		}
		sql.append("order by os.cdordemservico, a.codigo,(e.rua*es.sentido), e.nivel, e.apto, pp.descricao, p.descricao ");

		System.out.println(sql);
		
		return getJdbcTemplate().query(sql.toString(), args, new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ItemSeparacaoVO item = new ItemSeparacaoVO();
				item.setCdordemservico(rs.getInt("cdordemservico"));
				item.setCdcarregamento(rs.getInt("cdcarregamento"));
				item.setCdexpedicao(rs.getInt("cdexpedicao"));
				item.setLinhaseparacao(rs.getString("linhaseparacao"));
				item.setData(rs.getDate("data"));
				item.setBox(rs.getString("box"));
				item.setCodigo(rs.getString("codigo"));
				item.setDescricaoPrincipal(rs.getString("descricaoPrincipal"));
				item.setDescricao(rs.getString("descricao"));
				item.setComplementocodigobarras(rs.getString("complementocodigobarras"));
				item.setQtdeesperada(rs.getLong("qtdeesperada"));
				item.setArea(rs.getString("area"));
				item.setEndereco(rs.getString("endereco"));
				item.setQtdeendereco(rs.getLong("qtdeendereco"));
				item.setPeso(rs.getDouble("peso"));
				item.setCubagem(rs.getDouble("cubagem"));
				item.setQtdeembalagem(rs.getInt("qtdeembalagem"));
				if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito()))
					item.setCdprodutoembalagem(rs.getInt("cdprodutoembalagem"));				
				
				return item;
			}
		});
	}

	/**
	 * Localiza os mapas de separação gerados em onda que engloba o carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservico> findMapaByOnda(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		Expedicao expedicao = new QueryBuilder<Expedicao>(getHibernateTemplate())
			.from(Ordemservico.class)
			.select("ordemservico.expedicao")
			.join("ordemservico.listaOrdemProdutoLigacao oph")
			.join("oph.ordemservicoproduto osp")
			.join("osp.listaEtiquetaexpedicao ee")
			.join("ee.carregamentoitem ci")
			.where("ci.carregamento = ?", carregamento)
			.where("ordemservico.ordemtipo = ?", Ordemtipo.MAPA_SEPARACAO)
			.setMaxResults(1)
			.setUseTranslator(false)
			.unique();
		
		if (expedicao != null){
			return findByExpedicao(expedicao, Ordemtipo.MAPA_SEPARACAO);
		} else 
			return new ArrayList<Ordemservico>();
	}
	
	/**
	 * Localiza os mapas de separação gerados em onda que engloba a expedicao.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservico> findMapaByOnda(Expedicao expedicao) {
		if(expedicao == null || expedicao.getCdexpedicao() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		Expedicao expedicaoAux = new QueryBuilder<Expedicao>(getHibernateTemplate())
			.from(Ordemservico.class)
			.select("ordemservico.expedicao")
			.join("ordemservico.listaOrdemProdutoLigacao oph")
			.join("oph.ordemservicoproduto osp")
			.join("osp.listaEtiquetaexpedicao ee")
			.join("ee.carregamentoitem ci")
			.where("ci.carregamento.expedicao = ?", expedicao)
			.where("ordemservico.ordemtipo = ?", Ordemtipo.MAPA_SEPARACAO)
			.setMaxResults(1)
			.setUseTranslator(false)
			.unique();
		
		if (expedicaoAux != null){
			return findByExpedicao(expedicaoAux, Ordemtipo.MAPA_SEPARACAO);
		} else 
			return new ArrayList<Ordemservico>();
	}

	/**
	 * Busca a segunda conferência de expedição associada a uma primeira conferência.
	 * 
	 * @author Giovane Freitas
	 * @param primeiraConferencia
	 * @return 
	 */
	public Ordemservico loadSegundaConferencia(Ordemservico primeiraConferencia) {
		if(primeiraConferencia == null || primeiraConferencia.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.leftOuterJoinFetch("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
			.where("ordemservico.ordemtipo = ? ", Ordemtipo.CONFERENCIA_EXPEDICAO_2)
			.where("ordemservico.ordemservicoprincipal = ?", primeiraConferencia)
			.unique();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ordemservico> selectStatusUltimaOS(Recebimento recebimento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select cdordemstatus from ordemservico  where cdrecebimento = ? and cdordemtipo in (1,2) order by cdordemservico desc ");
		
		return (List<Ordemservico>) getJdbcTemplate().query(sql.toString(), new Object[]{recebimento.getCdrecebimento()},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Ordemservico> list = new ArrayList<Ordemservico>();
				
				while (rs.next()){
					Ordemservico ordemservico = new Ordemservico();
					ordemservico.getOrdemstatus().setCdordemstatus(rs.getInt("cdordemstatus"));
					list.add(ordemservico);
				}
				return list;
			}
		}
		);
	}

	/**
	 * Método que encontra a ordem de serviço aberta pelo endereço informado no coletor
	 * 
	 * @param endereco
	 * @param contagem
	 * @return
	 * @author Tomás Rabelo
	 */
	public Ordemservico findOrdemServicoAbertaByEndereco(Endereco endereco, final boolean contagem, Deposito deposito) {
		if(endereco == null || endereco.getArea() == null || endereco.getArea().getCodigo() == null || endereco.getRua() == null || 
		   endereco.getPredio() == null || endereco.getNivel() == null || endereco.getApto() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder("")
		.append("SELECT OS.CDORDEMSERVICO AS CONTAGEM, OSR.CDORDEMSERVICO AS RECONTAGEM ")
		.append("FROM ENDERECO E ")
		.append("JOIN AREA A ON (A.CDAREA = E.CDAREA) ")
		.append("JOIN ORDEMSERVICOPRODUTOENDERECO OSPE ON (OSPE.CDENDERECODESTINO = E.CDENDERECO) ")
		.append("JOIN ORDEMPRODUTOLIGACAO OL ON (OSPE.CDORDEMSERVICOPRODUTO = OL.CDORDEMSERVICOPRODUTO) ") 
		.append("JOIN ORDEMSERVICO OS ON (OL.CDORDEMSERVICO = OS.CDORDEMSERVICO) ")
		.append("LEFT JOIN ORDEMSERVICO OSR ON (OS.CDINVENTARIOLOTE = OSR.CDINVENTARIOLOTE AND OSR.CDORDEMSTATUS IN (1,2)) ")
		.append("JOIN INVENTARIOLOTE IL ON (IL.CDINVENTARIOLOTE = OS.CDINVENTARIOLOTE) ")
		.append("JOIN INVENTARIO IV ON (IV.CDINVENTARIO = IL.CDINVENTARIO) ")
		.append("WHERE OS.CDORDEMTIPO = ? ")
		.append("AND OS.CDDEPOSITO = ? ")
		.append("AND IV.CDINVENTARIOSTATUS = ? ")
		.append("AND OS.CDORDEMSTATUS IN (?,?,?,?) ")
		.append("AND A.CODIGO = ? AND E.RUA = ? AND E.PREDIO = ? AND ( (E.NIVEL = ? AND E.APTO = ?) OR E.CDENDERECOFUNCAO = ? ) ")
		.append("ORDER BY OS.ORDEM DESC, OS.CDORDEMSERVICO DESC");
		
		return (Ordemservico) getJdbcTemplate().query(sql.toString(), new Object[]{Ordemtipo.CONTAGEM_INVENTARIO.getCdordemtipo(), deposito.getCddeposito(), Inventariostatus.EM_EXECUCAO.getCdinventariostatus(),Ordemstatus.EM_ABERTO.getCdordemstatus(),  Ordemstatus.EM_EXECUCAO.getCdordemstatus(),Ordemstatus.FINALIZADO_DIVERGENCIA.getCdordemstatus(), Ordemstatus.FINALIZADO_SUCESSO.getCdordemstatus(), 
																				   endereco.getArea().getCodigo(), endereco.getRua(), endereco.getPredio(), endereco.getNivel(), endereco.getApto(), Enderecofuncao.BLOCADO.getCdenderecofuncao()},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				Ordemservico ordemservico = null;
				while (rs.next()){
					ordemservico = new Ordemservico();
					if(contagem)
						ordemservico.setCdordemservico(rs.getInt("CONTAGEM"));
					else
						ordemservico.setCdordemservico(rs.getInt("RECONTAGEM"));
					break;
				}
				return ordemservico;
			}
		});
	}

	/**
	 * Método que retorna se a OS é a 2º do inventário
	 * 
	 * @param ordemservico
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean isPrimeiraOrdemServicoContagemInventario(Ordemservico ordemservico) {
		return new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(Ordemservico.class)
			.setUseTranslator(false)
			.join("ordemservico.ordemstatus ordemstatus")
			.join("ordemservico.ordemtipo ordemtipo")
			.join("ordemservico.inventariolote inventariolote")
			.where("inventariolote.id = (select il.id from Inventariolote il join il.listaOrdemservico os where os = ?)", ordemservico)
			.where("ordemtipo = ? ", Ordemtipo.CONTAGEM_INVENTARIO)
			.where("ordemstatus <> ?", Ordemstatus.CANCELADO)
			.unique() == 1;
	}

	public Ordemservico find1ConferenciaByRetornoBox(Ordemservico osRetornoBox) {
		String sql = "select os1.cdordemservico " +
		" from ordemservico os1" +
		" join ordemservico os2 on os2.cdordemservicoprincipal=os1.cdordemservico" + 
		" join ordemservico osR2 on osR2.cdordemservicoprincipal=os2.cdordemservico" + 
		" join ordemservico osRB on osRB.cdordemservicoprincipal=osr2.cdordemservico" + 
		" where osRB.cdordemservico = ?";

		Integer cdordemservico = getJdbcTemplate().queryForInt(sql, new Object[]{osRetornoBox.getCdordemservico()});
		return new Ordemservico(cdordemservico);
	}
	
	public Ordemservico find2ConferenciaByRetornoBox(Ordemservico osRetornoBox) {
		String sql = "select os2.cdordemservico " +
		" from ordemservico os2 " +
		" join ordemservico osR2 on osR2.cdordemservicoprincipal=os2.cdordemservico" + 
		" join ordemservico osRB on osRB.cdordemservicoprincipal=osr2.cdordemservico" + 
		" where osRB.cdordemservico = ?";

		Integer cdordemservico = getJdbcTemplate().queryForInt(sql, new Object[]{osRetornoBox.getCdordemservico()});
		return new Ordemservico(cdordemservico);
	}

	public Ordemservico find2ReconferenciaByRetornoBox(Ordemservico ordemservicoRB) {
		String sql = "select osR2.cdordemservico " +
		" from ordemservico osR2" + 
		" join ordemservico osRB on osRB.cdordemservicoprincipal=osR2.cdordemservico" + 
		" where osRB.cdordemservico = ?";

		Integer cdordemservico = getJdbcTemplate().queryForInt(sql, new Object[]{ordemservicoRB.getCdordemservico()});
		return new Ordemservico(cdordemservico);
	}
	
	/**
	 * Método que retorna a lista de ordem serviço que esteja com Status EM ABERTO e seja um REABASTECIMENTO
	 * 
	 * @param Deposito
	 * @return List<Ordemservico>
	 * @author Thiago Augusto
	 */
	public List<Ordemservico> findOSPickingAberto(Deposito deposito, Usuario usuario){
		
		return query()
			.leftOuterJoin("ordemservico.listaOrdemServicoUsuario ordemServicoUsuario")
			.openParentheses()
				.where("ordemservico.ordemstatus=?",Ordemstatus.EM_ABERTO)
				.or()
				.openParentheses()
					.where("ordemservico.ordemstatus=?",Ordemstatus.EM_EXECUCAO)
					.where("ordemServicoUsuario.usuario = ?", usuario)
				.closeParentheses()
			.closeParentheses()
			.where("ordemservico.ordemtipo = ?", Ordemtipo.REABASTECIMENTO_PICKING)
			.where("ordemservico.deposito = ?", deposito)
			.list();
	}

	/**
	 * 
	 * @param ordemservico
	 * @return
	 */	
	public Ordemstatus getStatus(Ordemservico ordemservico) {
		Integer cdordemstatus = getJdbcTemplate().queryForInt("select cdordemstatus from ordemservico where cdordemservico = ?", new Object[]{ordemservico.getCdordemservico()});
		Ordemstatus ordemstatus = new Ordemstatus(cdordemstatus);
		return ordemstatus;
	}

	/**
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Ordemservico> findCheckoutAberto(Carregamento carregamento){
		String ordemstatus = "1,2,6";
		return query()
			.joinFetch("ordemservico.carregamento carregamento")
			.joinFetch("ordemservico.ordemstatus ordemstatus")
			.joinFetch("ordemservico.ordemtipo ordemtipo")
			.where("ordemservico.ordemtipo = ?",Ordemtipo.CONFERENCIA_CHECKOUT)
			.where("ordemservico.carregamento = ?",carregamento)
			.whereIn("ordemservico.ordemstatus",ordemstatus)
			.list();
	}
	

	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Ordemservico> findReconferenciaByRecebimento(Recebimento recebimento){
		
		return query()
				.select("ordemservico.cdordemservico")
				.join("ordemservico.recebimento recebimento")
				.join("ordemservico.ordemtipo ordemtipo")
				.where("ordemtipo.cdordemtipo = 2")
				.where("recebimento = ?",recebimento)
				.list();
	}
	
	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Ordemservico> findAllOrdemServicoByRecebimento(Recebimento recebimento) {

		return query()
				.select("ordemservico.cdordemservico, ordemprodutohistorico.cdordemprodutohistorico, ordemprodutohistorico.qtde," +
						"ordemprodutohistorico.qtdeavaria, ordemprodutohistorico.qtdefracionada, ordemprodutohistorico.qtdefalta")
				.join("ordemservico.recebimento recebimento")
				.join("ordemservico.ordemtipo ordemtipo")
				.join("ordemservico.listaOrdemProdutoHistorico ordemprodutohistorico")
				.where("recebimento = ?",recebimento)
				.list();
	}
	
}