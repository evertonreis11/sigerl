package br.com.linkcom.wms.geral.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.service.NotafiscaltipoService;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.RelatorioposicionamentoFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.NotafiscalentradaFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.RecebimentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class NotafiscalentradaDAO extends GenericDAO<Notafiscalentrada> {

	@Override
	public void updateListagemQuery(QueryBuilder<Notafiscalentrada> query, FiltroListagem _filtro) {
		NotafiscalentradaFiltro filtro = (NotafiscalentradaFiltro) _filtro;
		
		query.select("notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.numero,notafiscalentrada.dtcancelamento," +
				"notafiscaltipo.cdnotafiscaltipo,notafiscaltipo.nome,fornecedor.cdpessoa," +
				"fornecedor.nome,recebimentonotafiscal.cdrecebimentonotafiscal");
		
		query.leftOuterJoin("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscal");
		query.leftOuterJoin("notafiscalentrada.notafiscaltipo notafiscaltipo");
		query.leftOuterJoin("notafiscalentrada.fornecedor fornecedor");
		query.where("notafiscalentrada.deposito=?",WmsUtil.getDeposito());		
		query.where("notafiscaltipo=?", filtro.getNotafiscaltipo());				
		query.where("fornecedor=?", filtro.getFornecedor());			
		query.where("notafiscalentrada.dtemissao >= ?",filtro.getDtinicio());
		query.where("notafiscalentrada.dtemissao <= ?",filtro.getDtfim());
		query.whereLikeIgnoreAll("notafiscalentrada.numero", filtro.getNumero());
				
		if (filtro.getStatusnotafiscal() != null && filtro.getStatusnotafiscal().getId() != null){
			
			//atualmente o neo está retornando filtro.getStatusnotafiscal().getId() como String
			int idStatus = Integer.parseInt(filtro.getStatusnotafiscal().getId().toString());
			
			if (NotafiscalentradaFiltro.CANCELADO.getId().equals(idStatus)){
				query.where("notafiscalentrada.dtcancelamento is not null");
			}else if (NotafiscalentradaFiltro.RECEBIDO.getId().equals(idStatus)){
				query.where("recebimentonotafiscal.cdrecebimentonotafiscal is not null");
			}else if (NotafiscalentradaFiltro.EMITIDO.getId().equals(idStatus)){
				query.where("recebimentonotafiscal.cdrecebimentonotafiscal is null");
				query.where("notafiscalentrada.dtcancelamento is null");
			}
		}
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Notafiscalentrada> query) {
		query.select("notafiscalentrada.cdnotafiscalentrada, notafiscalentrada.codigoerp," +
				"notafiscalentrada.dtemissao,notafiscalentrada.dtcancelamento,notafiscalentrada.dtlancamento," +
				"notafiscalentrada.dtchegada,notafiscalentrada.transportador,notafiscalentrada.veiculo," +
				"notafiscalentrada.numero,notafiscalentrada.devolvida," +
				"produto.cdproduto,produto.codigo,produto.descricao,notafiscalentradaproduto.cdnotafiscalentradaproduto," +
				"notafiscalentradaproduto.qtde,notafiscalentradaproduto.valor,notafiscaltipo.cdnotafiscaltipo," +
				"notafiscaltipo.nome,fornecedor.cdpessoa,fornecedor.nome")
			.join("notafiscalentrada.fornecedor fornecedor")
			.leftOuterJoin("notafiscalentrada.notafiscaltipo notafiscaltipo")
			.leftOuterJoin("notafiscalentrada.listaNotafiscalentradaproduto notafiscalentradaproduto")
			.leftOuterJoin("notafiscalentradaproduto.produto produto");
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManagedNormal("listaNotafiscalentradaproduto", "notafiscalentrada");
	}
	
	/**
	 * Carrega as informações para o popup. (Dados nota fiscal.)
	 * 
	 * @param notafiscalentrada
	 * @return
	 */
	public Notafiscalentrada loadInfoForPopUp(Notafiscalentrada notafiscalentrada) {
		if(notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null)
			throw new WmsException("Parâmetros incorretos.");

		return query()
				.select("notafiscalentrada.codigoerp,notafiscalentrada.numero,notafiscaltipo.nome,fornecedor.nome, fornecedor.cdpessoa, notafiscalentrada.dtemissao, " +
						"notafiscalentrada.transportador,notafiscalentrada.veiculo, produto.cdproduto, produto.descricao,produto.peso,produto.codigo, " +
						"listaNotafiscalentradaproduto.qtde,produto.pesounitario")
				.leftOuterJoin("notafiscalentrada.listaNotafiscalentradaproduto listaNotafiscalentradaproduto")
				.leftOuterJoin("notafiscalentrada.notafiscaltipo notafiscaltipo")
				.leftOuterJoin("notafiscalentrada.fornecedor fornecedor")
				.leftOuterJoin("notafiscalentrada.deposito deposito")
				.leftOuterJoin("listaNotafiscalentradaproduto.produto produto")
				.where("deposito =?",WmsUtil.getDeposito())
				.entity(notafiscalentrada)
				.unique();
	}
	
	/**
	 * Carrega todos os produtos da nota fiscal.
	 *  
	 * @param notafiscalentrada
	 * @return
	 */
	public Notafiscalentrada getProdutos(Notafiscalentrada notafiscalentrada) {
		if(notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select("produto.cdproduto,notafiscalentradaproduto.qtde,produto.peso,produto.qtdevolumes")
				.leftOuterJoin("notafiscalentrada.listaNotafiscalentradaproduto notafiscalentradaproduto")
				.leftOuterJoin("notafiscalentradaproduto.produto produto")
				.entity(notafiscalentrada)
				.unique();
	}
	
	/**
	 * Busca os fornecedores das notas fiscais
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @return
	 */
	public List<String> findFornecedores() {
		return newQueryBuilderWithFrom(String.class)
				.select("notafiscalentrada.fornecedor")
				.orderBy("notafiscalentrada.fornecedor")
				.setUseTranslator(false)
				.list();
	}
	
	/**
	 * Busca as placas dos veículos das notas fiscais
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * 
	 * @return
	 */
	public List<String> findPlacas() {
		return queryNotaFiscalEntrada(String.class)
		.select("notafiscalentrada.veiculo")
		.leftOuterJoin("notafiscalentrada.deposito deposito")
		.where("deposito =?",WmsUtil.getDeposito())
		.where("recebimentostatus is null")
		.where("notafiscalentrada.devolvida=?",Boolean.FALSE)
		.orderBy("notafiscalentrada.veiculo")
		
		.setUseTranslator(false)
		.list();
	}
		
	/**
	 * Busca as placas dos veículos das notas fiscais
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * 
	 * @return
	 */
	public List<String> findPlacas(String whereIn) {
		return queryNotaFiscalEntrada(String.class)
		.select("notafiscalentrada.veiculo")
		.join("notafiscalentrada.notafiscaltipo notafiscaltipo")
		.leftOuterJoin("notafiscalentrada.deposito deposito")
		.where("deposito =?",WmsUtil.getDeposito())
		.where("recebimentostatus is null")
		.where("notafiscalentrada.devolvida=?",Boolean.FALSE)
		.where("notafiscaltipo.cdnotafiscaltipo not in ("+whereIn+")")
		.orderBy("notafiscalentrada.veiculo")		
		.setUseTranslator(false)
		.list();
	}
	
	/**
	 * Busca todos as notas fiscais de um determinado veículo
	 * ou todos
	 * @see #queryNotaFiscalEntrada(Class)
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Notafiscalentrada> findBy(RecebimentoFiltro filtro){
		if(filtro == null)
			throw new WmsException("Parâmetros incorretos");
		
		QueryBuilder<Notafiscalentrada> query= queryNotaFiscalEntrada(Notafiscalentrada.class)
					.select("distinct notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.numero, " +
							"notafiscalentrada.codigoerp,notafiscalentrada.veiculo,notafiscalentrada.dtcancelamento, " +
							"fornecedor.nome, fornecedor.cdpessoa, notafiscaltipo.exigeagenda, " +
							"pedidocompra.cdpedidocompra, pedidocompra.numero")
					.leftOuterJoin("notafiscalentrada.notafiscaltipo notafiscaltipo")
					.leftOuterJoin("notafiscalentrada.pedidocompra pedidocompra")
					.where("notafiscalentrada.dtemissao >= ?",filtro.getEmissaode())
					.where("notafiscalentrada.dtemissao <= ?",filtro.getEmissaoate())
					.where("notafiscalentrada.deposito = ?",WmsUtil.getDeposito())
					.where("recebimentostatus is null")
					.where("notafiscalentrada.devolvida=?",Boolean.FALSE)
					.where("notafiscalentrada.dtcancelamento is null ")
					.where("exists (from Notafiscalentrada nfe where  nfe.notafiscaltipo = ? " +
							"and  notafiscalentrada.veiculo = nfe.veiculo)", filtro.getNotafiscaltipo());
		
		//A placa ZZZ0000 é usada como placa default, por isso ele ocorre muitas vezes
		//Quando o Oracle usa o índice para tratar esta placa o desempenho fica horrível
		//por isso vou fazer um to_char para forçar o oracle a não usar o índice.
		if (filtro.getVeiculo() != null && !filtro.getVeiculo().isEmpty())
			query.where("to_char(notafiscalentrada.veiculo) = to_char('" + filtro.getVeiculo() + "') ");
		
		if(filtro.getNotafiscal() != null && !"".equals(filtro.getNotafiscal()))
					query.whereWhen("exists( from Notafiscalentrada nfe where nfe.numero = '"+filtro.getNotafiscal()+"' " +
							"and notafiscalentrada.veiculo = nfe.veiculo and nfe.devolvida=false)",filtro.getNotafiscal() != null);
		if(filtro.getFornecedor() != null )
			query.whereWhen("exists( from Notafiscalentrada nfe where  nfe.fornecedor.id = "+filtro.getFornecedor().getCdpessoa()+" " +
					" and notafiscalentrada.veiculo = nfe.veiculo)",filtro.getFornecedor() != null);
		
		return query.list();
	}
	
	/**
	 * Carrega as informações da nota fiscal para apresentação na tela de devolução.
	 * 
	 * @author Pedro Gonçalves
	 * @param notafiscalentrada
	 * @return
	 */
	public List<Notafiscalentrada> loadInfoForDevolucao(String numero){
		if(numero == null)
			throw new WmsException("Parâmetros incorretos");
		
		return queryNotaFiscalEntrada(Notafiscalentrada.class)
				.select("notafiscalentrada.codigoerp,notafiscalentrada.veiculo,notafiscalentrada.transportador," +
						"notafiscalentrada.devolvida, notafiscaltipo.nome, notafiscalentrada.numero," +
						"notafiscalentrada.cdnotafiscalentrada, listaRecebimentonotafiscalentrada.cdrecebimentonotafiscal")
				.join("notafiscalentrada.notafiscaltipo notafiscaltipo")
				.join("notafiscalentrada.deposito deposito")
				.where("notafiscalentrada.numero=?",numero)
				.where("deposito = ?",WmsUtil.getDeposito())
				.list();
	}
	
	/**
	 * Método que retorna a quantidade de notas fiscais da tabela notafiscalentrada
	 * 
	 * @param nfe
	 * @return
	 * 
	 * @author Pedro Gonçalves / Arantes
	 * 
	 */
	public Long getTotalNfe(Notafiscalentrada nfe){
			return queryNotaFiscalEntrada(Long.class)
		    .select("count(listaRecebimentonotafiscalentrada.cdrecebimentonotafiscal)")
		    .join("notafiscalentrada.notafiscaltipo notafiscaltipo")
		    .where("notafiscalentrada=?", nfe)
		    .setUseTranslator(false)
		    .unique();
	}
	
	/**
	 * Monta a query Básica para a regra da nota fiscal.
	 * @param <E>
	 * @param e
	 * @return
	 * @author Pedro Gonçalves
	 */
	private <E> QueryBuilder<E> queryNotaFiscalEntrada(Class<E> e) {
		return newQueryBuilderWithFrom(e)
				.join("notafiscalentrada.fornecedor fornecedor")
				.leftOuterJoin("notafiscalentrada.listaRecebimentonotafiscalentrada listaRecebimentonotafiscalentrada")
				.leftOuterJoin("listaRecebimentonotafiscalentrada.recebimento recebimento")
				.leftOuterJoin("recebimento.recebimentostatus recebimentostatus")
				//.where("recebimentostatus is null")
				.orderBy("veiculo");
	}
	
	/**
	 * Executa um update no campo devolvida.
	 * 
	 * @author Pedro Gonçalves
	 * @param notafiscalentrada
	 */
	public void devolverNotaFiscalEntrada(Notafiscalentrada notafiscalentrada){
		if(notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		getHibernateTemplate().bulkUpdate("update Notafiscalentrada nfe set nfe.devolvida=? where nfe.id=?",new Object[]{Boolean.TRUE,notafiscalentrada.getCdnotafiscalentrada()});
	}
	
	/* singleton */
	private static NotafiscalentradaDAO instance;
	public static NotafiscalentradaDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscalentradaDAO.class);
		}
		return instance;
	}
	
	/**
	 * Busca as notas ficas de um recebimento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @return
	 */
	public List<Notafiscalentrada> findByRecebimento(Recebimento recebimento) {
		return query()
					.select("notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.numero,fornecedor.nome,notafiscalentrada.codigoerp")
					.leftOuterJoin("notafiscalentrada.listaRecebimentonotafiscalentrada listaRecebimentonotafiscalentrada")
					.join("listaRecebimentonotafiscalentrada.recebimento recebimento")
					.join("notafiscalentrada.fornecedor fornecedor")
					.where("recebimento = ?",recebimento)
					.orderBy("notafiscalentrada.numero")
					.list();
	}
	
	/**
	 * Seta as notas fiscais como devolvidas
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 */
	public void devolverNotaFiscal(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Recebimento inválido.");
		
		getHibernateTemplate().bulkUpdate("update Notafiscalentrada nf set nf.devolvida = 1 " +
										  "where nf.cdnotafiscalentrada in " +
										  "(select rnf.notafiscalentrada.cdnotafiscalentrada from Recebimentonotafiscal rnf where rnf.recebimento=?)",new Object[]{recebimento});
	}

	/**
	 * Atualiza a data de Chegada de todas as notas fiscais de entrada associadas a um recebimento.
	 * 
	 * @author Giovane Freitas
	 * @param recebimento
	 * @param dataChegada
	 */
	public void atualizarDataChegada(Recebimento recebimento, Timestamp dataChegada) {
		if(recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Recebimento inválido.");
		
		if(dataChegada == null){
			getHibernateTemplate().bulkUpdate("update Notafiscalentrada nf set nf.dtchegada = null " +
											  "where nf.cdnotafiscalentrada in " +
											  "(select rnf.notafiscalentrada.cdnotafiscalentrada from Recebimentonotafiscal rnf where rnf.recebimento=?)",new Object[]{recebimento});
		}else{
			getHibernateTemplate().bulkUpdate("update Notafiscalentrada nf set nf.dtchegada = ? " +
					  "where nf.cdnotafiscalentrada in " +
					  "(select rnf.notafiscalentrada.cdnotafiscalentrada from Recebimentonotafiscal rnf where rnf.recebimento=?)",new Object[]{dataChegada, recebimento});
		}
		
	}

	/**
	 * Verifica se uma nota de entrada está relacionada a algum recebimento.
	 * 
	 * @author Giovane Freitas
	 * @param notaentrada
	 * @return
	 */
	public Boolean isRecebido(Notafiscalentrada notaentrada) {
		if (notaentrada == null || notaentrada.getCdnotafiscalentrada() == null)
			return false;
		
		List<Notafiscalentrada> list = query().select("notafiscalentrada.cdnotafiscalentrada")
			.join("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscalentrada")
			.where("notafiscalentrada = ?", notaentrada)
			.setUseTranslator(true)
			.list();
		
		if(list == null || list.size() <= 0)
			return false;
		else
			return true;
	}

	/**
	 * Verifica se uma nota de entrada está cancelada.
	 * 
	 * @author Giovane Freitas
	 * @param notaentrada
	 * @return
	 */
	public Boolean isCancelado(Notafiscalentrada notaentrada) {
		if (notaentrada == null || notaentrada.getCdnotafiscalentrada() == null)
			return false;
		
		List<Notafiscalentrada> list = query().select("notafiscalentrada.cdnotafiscalentrada")
			.where("notafiscalentrada = ?", notaentrada)
			.where("notafiscalentrada.dtcancelamento is not null")
			.setUseTranslator(true)
			.list();
		
		if(list == null || list.size() <= 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Cancela o {@link Notafiscalentrada}, definindo o atributo dtcancelamento com a data atual.
	 * 
	 * @author Giovane Freitas
	 * @param notafiscalentrada
	 */
	public void cancelar(Notafiscalentrada notafiscalentrada) {
		if (notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null)
			throw new WmsException("Parâmetro inválido. O documento de entrada é obrigatório.");
		
		getHibernateTemplate().bulkUpdate("update Notafiscalentrada nf set nf.dtcancelamento = ? where nf.id=? ", 
				new Object[]{new Date(System.currentTimeMillis()), notafiscalentrada.getCdnotafiscalentrada()});
	}

	/**
	 * Método que carrega a lista de nota fiscal para gerar relatório de posicionamento
	 * 
	 * @param filtro
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Notafiscalentrada> findForPosicionamentoReport(RelatorioposicionamentoFiltro filtro) {
		return query()
		.select("notafiscalentrada.cdnotafiscalentrada, notafiscalentrada.numero, listaNotafiscalentradaproduto.lote, " +
				"listaNotafiscalentradaproduto.dtvalidade, listanotafiscalenderecohistorico.qtde, listanotafiscalenderecohistorico.dtentrada, " +
				"endereco.endereco, produto.cdproduto, produto.descricao, produto.codigo, listanotafiscalenderecohistorico.cdnotafiscalenderecohistorico, area.codigo, " +
		"notafiscaltipo.nome")
		.join("notafiscalentrada.listaNotafiscalentradaproduto listaNotafiscalentradaproduto")
		.join("notafiscalentrada.notafiscaltipo notafiscaltipo")
		.join("listaNotafiscalentradaproduto.listanotafiscalenderecohistorico listanotafiscalenderecohistorico")
		.join("listanotafiscalenderecohistorico.endereco endereco")
		.join("endereco.area area")
		.join("listaNotafiscalentradaproduto.produto produto")
		.where("produto = ?", filtro.getProduto())
		.where("notafiscalentrada.numero = ?", filtro.getDocumentoentrada())
		.where("listaNotafiscalentradaproduto.dtvalidade >= ?", WmsUtil.dateToBeginOfDay(filtro.getDtvalidadede()))
		.where("listaNotafiscalentradaproduto.dtvalidade <= ?", WmsUtil.dataToEndOfDay(filtro.getDtvalidadeate()))
		.whereLikeIgnoreAll("listaNotafiscalentradaproduto.lote", filtro.getLote())
		.orderBy("notafiscalentrada.numero, produto.descricao, listanotafiscalenderecohistorico.cdnotafiscalenderecohistorico")
		.list();
	}

	public void updateVinculoAgendamento(Notafiscalentrada notafiscalentrada, Agenda agenda) {
		getHibernateTemplate().bulkUpdate("update Notafiscalentrada nf set nf.agenda = ? where nf.id=? ", 
				new Object[]{agenda, notafiscalentrada.getCdnotafiscalentrada()});
	}
	
	public Notafiscalentrada findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		.select("notafiscalentrada.cdnotafiscalentrada, notafiscalentrada.codigoerp, notafiscalentrada.numero " +
				"listaNotafiscalentradaproduto.cdnotafiscalentradaproduto")
		.leftOuterJoin("notafiscalentrada.listaNotafiscalentradaproduto listaNotafiscalentradaproduto")
		.where("notafiscalentrada.codigoerp = ? ", codigoERP)
		.unique();
	}

	
	public void updateNotafiscalentrada(Notafiscalentrada notafiscalentrada) {
		if(notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Notafiscalentrada set dtsincronizacao=SYSDATE where cdnotafiscalentrada=?", 
				new Object[]{notafiscalentrada.getCdnotafiscalentrada()});
	}
	
	public Notafiscalentrada loadNotaPedido(Notafiscalentrada notafiscalentrada) {
		if(notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null)
			throw new WmsException("Parâmetros incorretos.");

		return query()
				.select("notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.numero," +
						"pedidocompra.cdpedidocompra, pedidocompra.numero")
				.leftOuterJoin("notafiscalentrada.pedidocompra pedidocompra")
				.where("notafiscalentrada =?",notafiscalentrada)
				.unique();
	}

	/**
	 * Busca as placas dos veículos das notas fiscais
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * 
	 * @return
	 */
	public List<String> findPlacasAcompanhamento(String whereIn) {
		return queryNotaFiscalEntrada(String.class)
		.select("acompanhamentoveiculo.placaveiculo")
		.join("notafiscalentrada.acompanhamentoveiculo acompanhamentoveiculo")
		.leftOuterJoin("notafiscalentrada.deposito deposito")
		.join("notafiscalentrada.notafiscaltipo notafiscaltipo")
		.where("deposito =?",WmsUtil.getDeposito())
		.where("cdstatusrav in(0,1)")
		.where("recebimentostatus is null")
		.where("notafiscalentrada.devolvida=?",Boolean.FALSE)
		.whereIn("notafiscaltipo.cdnotafiscaltipo",whereIn)
		.orderBy("notafiscalentrada.veiculo")
		
		.setUseTranslator(false)
		.list();
	}

	/**
	 * Busca todos as notas fiscais de um determinado veículo
	 * ou todos
	 * @see #queryNotaFiscalEntrada(Class)
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Notafiscalentrada> findByGerarRecebimento(RecebimentoFiltro filtro, Boolean isAgenda){
		if(filtro == null)
			throw new WmsException("Parâmetros incorretos");
		
		StringBuilder snotafiscaltipo = new StringBuilder();
		String whereIn = new String();
		List<Notafiscaltipo> listaNotafiscaltipo = NotafiscaltipoService.getInstance().findExigeAgendamento();
		for (Notafiscaltipo notafiscaltipo : listaNotafiscaltipo) {
			snotafiscaltipo.append(notafiscaltipo.getCdnotafiscaltipo());
			snotafiscaltipo.append(",");
		}
		whereIn = snotafiscaltipo.substring(0,snotafiscaltipo.length()-1).toString();
		
		QueryBuilder<Notafiscalentrada> query= queryNotaFiscalEntrada(Notafiscalentrada.class);
					
					if(isAgenda){
						query.select("distinct notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.numero, " +
								"notafiscalentrada.codigoerp,notafiscalentrada.veiculo,notafiscalentrada.dtcancelamento, " +
								"fornecedor.nome, fornecedor.cdpessoa, notafiscaltipo.exigeagenda, " +
								"pedidocompra.cdpedidocompra, pedidocompra.numero, acompanhamentoveiculo.placaveiculo, " +
								"acompanhamentoveiculo.numerorav,acompanhamentoveiculo.cdacompanhamentoveiculo");
						query.join("notafiscalentrada.acompanhamentoveiculo acompanhamentoveiculo");
						query.join("acompanhamentoveiculo.acompanhamentoveiculostatus acompanhamentoveiculostatus");
						/*queiroz incluindo o join para buscar a agenda*/
						//query.leftOuterJoin("acompanhamentoveiculo.listaAgendaacompanhamentoveiculo agendaacompanhamentoveiculo");
						//query.leftOuterJoin("notafiscalentrada.Agenda Agenda");
					}else{					
						query.select("distinct notafiscalentrada.cdnotafiscalentrada,notafiscalentrada.numero, " +
								"notafiscalentrada.codigoerp,notafiscalentrada.veiculo,notafiscalentrada.dtcancelamento, " +
								"fornecedor.nome, fornecedor.cdpessoa, notafiscaltipo.exigeagenda, " +
								"pedidocompra.cdpedidocompra, pedidocompra.numero");
					}
					
					query.leftOuterJoin("notafiscalentrada.notafiscaltipo notafiscaltipo")
						 .leftOuterJoin("notafiscalentrada.pedidocompra pedidocompra")
						 .where("notafiscalentrada.dtemissao >= ?",filtro.getEmissaode())
						 .where("notafiscalentrada.dtemissao <= ?",filtro.getEmissaoate())
						 .where("notafiscalentrada.deposito = ?",WmsUtil.getDeposito())
						 .where("recebimentostatus is null")
						
						 .where("notafiscalentrada.devolvida=?",Boolean.FALSE)
						 .where("notafiscalentrada.dtcancelamento is null ")
						 .where("exists (from Notafiscalentrada nfe where  nfe.notafiscaltipo = ? " +
								 "and  notafiscalentrada.veiculo = nfe.veiculo)", filtro.getNotafiscaltipo());
					if(isAgenda)
					{
						query.whereIn("notafiscaltipo.cdnotafiscaltipo", whereIn);
						query.where("acompanhamentoveiculostatus.cdstatusrav in(0)");
					}
					if(!isAgenda)
					{
						query.where("notafiscaltipo.cdnotafiscaltipo not in ("+whereIn+")");
						
					}

					
		//A placa ZZZ0000 é usada como placa default, por isso ele ocorre muitas vezes
		//Quando o Oracle usa o índice para tratar esta placa o desempenho fica horrível
		//por isso vou fazer um to_char para forçar o oracle a não usar o índice.
		if (filtro.getVeiculo() != null && !filtro.getVeiculo().isEmpty() && filtro.getVeiculo()!="")
			query.where("to_char(notafiscalentrada.veiculo) = to_char('" + filtro.getVeiculo() + "') ");
		
		if(filtro.getNumerorav()!= null && !filtro.getNumerorav().isEmpty() && filtro.getNumerorav()!="" && isAgenda){
			query.where("to_char(acompanhamentoveiculo.numerorav) = to_char('" + filtro.getNumerorav() + "')");
		}
		
		if(filtro.getNotafiscal() != null && !"".equals(filtro.getNotafiscal()))
					query.whereWhen("exists( from Notafiscalentrada nfe where nfe.numero = '"+filtro.getNotafiscal()+"' " +
							"and notafiscalentrada.veiculo = nfe.veiculo and nfe.devolvida=false)",filtro.getNotafiscal() != null);
		if(filtro.getFornecedor() != null )
			query.whereWhen("exists( from Notafiscalentrada nfe where  nfe.fornecedor.id = "+filtro.getFornecedor().getCdpessoa()+" " +
					" and notafiscalentrada.veiculo = nfe.veiculo)",filtro.getFornecedor() != null);
		
		return query.list();
	}

	/**
	 * 
	 * @param jdbcTemplate
	 * @param whereIn
	 * @return
	 */
	public SqlRowSet gerarRelatorioNF(JdbcTemplate jdbcTemplate, String whereIn) {
		StringBuilder sql = new StringBuilder();
		
			sql.append(" SELECT PD.NRO_PEDIDO as \"Nº do Pedido\", L.COD_NRO_LOJA as \"Pedido Loja\", PR.NRO_PRODUTO as \"Cód. Produto ERP\", ");
			sql.append("       PR.COD_NRO_PRODUTO||'.'||PR.COD_COR_PRODUTO||'.'||PR.COD_TIPO_VOLTAGEM as \"Produto\", ");
			sql.append("       PR.DESC_PRODUTO as \"Descrição do Produto\", C.NOME_CLIENTE as \"Cliente\", CF.NRO_LOJA as \"Número Loja\", ");
			sql.append("	   VNF.FISCAL_DOC_ID \"Nota ID\", NVL((SELECT GP.DESC_GRUPO_FAIXA_CEP FROM MV.MV_GRUPO_FAIXA_CEP GP, MV.MV_DATA_ENTREGA DE ");
			sql.append("                WHERE GP.NRO_GRUPO_FAIXA_CEP = DE.NRO_GRUPO_FAIXA_CEP ");
            sql.append("				AND DE.NRO_DATA_ENTREGA = IPP.NRO_DATA_ENTREGA),'SEM ROTA DEFINIDA') as \"Rota\", ");
			sql.append("       NVL((SELECT DE.DATA FROM MV.MV_DATA_ENTREGA DE ");
			sql.append("                WHERE DE.NRO_DATA_ENTREGA = IPP.NRO_DATA_ENTREGA),IPP.DT_PREVISAO_ENTREGA) as \"Previsão de Entrega\" ");
			sql.append(" FROM MV.TB_RE_LOG_PRE_DISP LP, MV_PEDIDO PD, MV.RE_FISCAL_DOC_HEADER VNF, MV.MV_ITEM_PEDIDO_PRODUTO IPP, ");
			sql.append("	  MV.MV_CONTROLE_FATURAMENTO CF, MV.MV_PRODUTO PR, MV.MV_NF NF, MV.MV_LOJA L , MV.MV_CLIENTE C ");
			sql.append(" WHERE PD.NRO_PEDIDO = IPP.NRO_PEDIDO ");
			sql.append(" AND   PD.NRO_LOJA   = IPP.NRO_LOJA ");
			sql.append(" AND   CF.SEQ_CONTROLE_FATURAMENTO = LP.SEQ_CONTROLE_FATURAMENTO ");
			sql.append(" AND   PD.NRO_CLIENTE = C.NRO_CLIENTE ");
			sql.append(" AND   CF.NRO_PEDIDO  = IPP.NRO_PEDIDO ");
			sql.append(" AND   CF.NRO_LOJA_PEDIDO = IPP.NRO_LOJA ");
			sql.append(" AND   L.NRO_LOJA = PD.NRO_LOJA ");
			sql.append(" AND   CF.NRO_ITEM_PEDIDO_PRODUTO = IPP.NRO_ITEM_PEDIDO_PRODUTO ");
			sql.append(" AND   CF.NRO_ITEM_PEDIDO_PRODUTO =  LP.NRO_ITEM_PEDIDO_PRODUTO ");
			sql.append(" AND   PR.NRO_PRODUTO  =CF.NRO_PRODUTO ");
			sql.append(" AND   LP.NRO_SEQ_NF = NF.NRO_SEQ_NF ");
			sql.append(" AND   LP.NRO_LOJA = NF.NRO_LOJA ");
			sql.append(" AND   VNF.NRO_SEQ_NF = LP.NRO_SEQ_NF ");
			sql.append(" AND   PD.NRO_LOJA_CLIENTE = C.NRO_LOJA ");
			sql.append(" AND   VNF.FISCAL_DOC_ID IN (").append(whereIn).append(")");
			// 
		return jdbcTemplate.queryForRowSet(sql.toString());
	}
}