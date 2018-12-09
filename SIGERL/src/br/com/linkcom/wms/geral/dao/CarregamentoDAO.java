package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.geral.bean.vo.ProdutoSemEstoqueVO;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.ListagemcarregamentoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.PlanilhaConferenciaFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirliberacaoveiculoFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.ReabastecimentoException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class CarregamentoDAO extends GenericDAO<Carregamento> {
	
	private ConfiguracaoService configuracaoService;
	
	public void setConfiguracaoService(ConfiguracaoService configuracaoService) {
		this.configuracaoService = configuracaoService;
	}

	@Override
	public void updateListagemQuery(QueryBuilder<Carregamento> query, FiltroListagem _filtro) {
		if(_filtro == null)
			throw new WmsException("Parâmetros incorretos.");
		
		CarregamentoFiltro filtro = (CarregamentoFiltro) _filtro;
		
		query
			.select("carregamento.cdcarregamento, carregamentostatus.cdcarregamentostatus, carregamentostatus.nome, box.cdbox, box.nome, " +
					"carregamento.paletesdisponiveis,carregamento.dtcarregamento,carregamento.dtfimcarregamento,carregamento.listaRota," +
					"veiculo.cdveiculo, veiculo.placa, transportador.cdpessoa, transportador.nome, expedicao.cdexpedicao, motorista.cdmotorista," +
					"motorista.nome")
			.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
			.leftOuterJoin("carregamento.expedicao expedicao")
			.leftOuterJoin("carregamento.veiculo veiculo")
			.leftOuterJoin("carregamento.motorista motorista")
			.leftOuterJoin("veiculo.transportador transportador")
			.leftOuterJoin("carregamento.box box")
			.where("carregamento.deposito = ?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("carregamento.cdcarregamento = ?", filtro.getCdcarregamento())			
			.where("expedicao.cdexpedicao = ?", filtro.getCdexpedicao())		
			.where("carregamento.dtcarregamento >= ?", DateUtil.dataToBeginOfDay(filtro.getDtmontageminicial()))
			.where("carregamento.dtcarregamento <= ?", DateUtil.dataToEndOfDay(filtro.getDtmontagemfinal()))			
			.where("carregamento.dtfimcarregamento >= ?",  DateUtil.dataToBeginOfDay(filtro.getDtfinalizadainicial()))
			.where("carregamento.dtfimcarregamento <= ?", DateUtil.dataToEndOfDay(filtro.getDtfinalizadafinal()))
			.where("veiculo = ?", filtro.getVeiculo())
			.where("box = ?", filtro.getBox())
			.where("transportador = ?", filtro.getTransportador())
			.orderBy("carregamento.cdcarregamento desc");
	
		if (filtro.getCarregamentostatus() != null){
			if (new Integer(-1).equals(filtro.getCarregamentostatus().getCdcarregamentostatus())){
				
				List<Carregamentostatus> lista = new ArrayList<Carregamentostatus>();
				lista.add(Carregamentostatus.EM_MONTAGEM);
				lista.add(Carregamentostatus.MONTADO);
				lista.add(Carregamentostatus.EM_SEPARACAO);
				lista.add(Carregamentostatus.CONFERIDO);
				query.whereIn("carregamentostatus.cdcarregamentostatus", CollectionsUtil.listAndConcatenate(lista,"cdcarregamentostatus", ","));
			}else
				query.where("carregamentostatus = ?", filtro.getCarregamentostatus());
		}
	}

	@Override
	public Carregamento load(Carregamento bean) {
		if(bean == null){
			return null;
		}
		return query()
			.joinFetch("carregamento.carregamentostatus status")
			.joinFetch("carregamento.deposito deposito")
			.entity(bean)
			.unique();
	}
	
	/**
	 * 
	 * Método que salva ou atualiza um carregamento.
	 * 
	 * @author Arantes
	 * 
	 * @param bean
	 * 
	 */
	public void saveOrUpdateEntity(Carregamento bean) {
		SaveOrUpdateStrategy save = save(bean);
		save.execute();
		getHibernateTemplate().flush();
	}
	
	/**
	 * Carrega o carregamento com os dados de box e cliente.
	 * 
	 * @param carregamento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public Carregamento findForSeparacao(Carregamento carregamento,String ... extraargs) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
				.select(WmsUtil.makeSelectClause("carregamento.cdcarregamento,tipooperacao.cdtipooperacao,cliente.cdpessoa,carregamento.paletesdisponiveis",extraargs))
				.leftOuterJoin("carregamento.listaCarregamentoitem carregamentoitem")
				.leftOuterJoin("carregamento.box box")
				.leftOuterJoin("carregamento.veiculo veiculo")
				.leftOuterJoin("veiculo.tipoveiculo tipoveiculo")
				.leftOuterJoin("carregamentoitem.pedidovendaproduto pedidovendaproduto")
				.leftOuterJoin("pedidovendaproduto.pedidovenda pedidovenda")
				.leftOuterJoin("pedidovendaproduto.tipooperacao tipooperacao")
				.leftOuterJoin("pedidovenda.cliente cliente")
				.entity(carregamento)
				.orderBy("tipooperacao.cdtipooperacao,cliente.cdpessoa")
				.unique();
	}
	
	/**
	 * Gera a listagem de carregamento a partir do filtro.
	 * 
	 * @author Pedro Gonçalves
	 * @param filtro
	 * @return
	 */	
	public List<Carregamento> findForListagemCarregamento(ListagemcarregamentoFiltro filtro) {
		if(filtro == null){
			throw new WmsException("Parâmetros incorretos");
		}
		
		return query()
				.select("carregamento.cdcarregamento, carregamentostatus.nome, box.nome, " +
						"carregamento.paletesdisponiveis, veiculo.placa, tipoveiculo.nome")
				.join("carregamento.carregamentostatus carregamentostatus")
				.join("carregamento.veiculo veiculo")
				.join("veiculo.tipoveiculo tipoveiculo")
				.join("carregamento.box box")
				.where("carregamento.deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
				.where("veiculo = ?",filtro.getVeiculo())
				.where("box = ?",filtro.getBox())
				.where("tipoveiculo = ?",filtro.getTipoveiculo())
				.where("carregamentostatus = ?",filtro.getCarregamentostatus())				
				.list();
				
	}
	
	/**
	 * Busca todos os dados do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @return
	 */
	public Carregamento findByCarregamento(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
				throw new WmsException("O carregamento não deve ser nulo");
		return query()
					.select("carregamento.cdcarregamento, listaCarregamentoitem.cdcarregamentoitem, listaCarregamentoitem.carregamento," +
							"listaCarregamentoitem.ordem,listaCarregamentoitem.pedidovendaproduto,pedidovendaproduto.cdpedidovendaproduto," +
							"carregamentostatus.cdcarregamentostatus,carregamentostatus.nome")
					.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
					.leftOuterJoin("carregamento.listaCarregamentoitem listaCarregamentoitem")
					.leftOuterJoin("listaCarregamentoitem.pedidovendaproduto pedidovendaproduto")
					.entity(carregamento)
					.unique();
	}
	
	
	/**
	 * Atualiza o status do carregamento
	 * 
	 * @author Pedro Gonçalves
	 * @param recebimento
	 */
	public void atualizaStatusCarregamento(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null || 
				carregamento.getCarregamentostatus() == null || carregamento.getCarregamentostatus().getCdcarregamentostatus() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Carregamento carregamento set carregamento.carregamentostatus.id=? where carregamento.id=? ", new Object[]{carregamento.getCarregamentostatus().getCdcarregamentostatus(),carregamento.getCdcarregamento()});
	}
	
	/**
	 * Atualiza os dados do carregamento cancelado
	 * 
	 * @author Thiers Euller
	 * @param recebimento
	 */
	public void atualizaCarregamentoCancelado(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null || 
				carregamento.getCarregamentostatus() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Carregamento carregamento set carregamento.cdusuariocancela=? , carregamento.dtcancela=? , carregamento.carregamentostatus.id = 7  where carregamento.id=? ", new Object[]{carregamento.getCdusuariocancela(), carregamento.getDtcancela(),carregamento.getCdcarregamento()});
	}
	
	/**
	 * Busca o carregamento através do cdcarregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param cdcarregamento
	 * @return
	 */
	public Carregamento findByCdCarregamento(Integer cdcarregamento) {
		return query()
					.select("carregamento.cdcarregamento,carregamento.paletesdisponiveis,box.cdbox,box.nome,veiculo.cdveiculo,veiculo.placa," +
							"veiculo.capacidadepalete,transportador.cdpessoa,transportador.nome,listaCarregamentoitem.cdcarregamentoitem," +
							"listaCarregamentoitem.ordem,pedidovendaproduto.cdpedidovendaproduto,praca.cdpraca," +
							"praca.nome,rota.cdrota,rota.nome,tipooperacao.cdtipooperacao,tipooperacao.nome,tipooperacao.sigla,pessoaendereco.cep," +
							"cliente.cdpessoa,cliente.nome,pedidovendaproduto.valor,pedidovendaproduto.qtde,produto.cdproduto," +
							"produto.peso,produto.altura,produto.largura,produto.profundidade,produto.cubagem,produto.pesounitario," +
							"produto.cubagemunitaria,pessoaenderecocliente.cdpessoaendereco,pessoaenderecocliente.cep," +
							"veiculo.cdveiculo,veiculo.capacidadepalete,veiculo.capacidadepeso,veiculo.altura,veiculo.largura,veiculo.profundidade," +
							"pedidovenda.cdpedidovenda,pedidovenda.numero,pedidovenda.dtemissao, filialEntrega.cdpessoa,filialEntrega.nome,filialentregatransbordo.cdpessoa,filialentregatransbordo.nome," +
							"enderecofilial.cdpessoaendereco,enderecofilial.cep,filialretirada.cdpessoa,tipooperacaoretirada.cdtipooperacao," +
							"depositotransbordo.cddeposito,depositotransbordo.nome,enderecofilialtransbordo.cdpessoaendereco,enderecofilialtransbordo.cep,filialemissao.cdpessoa, filialemissao.nome," +
							"pedidovendaproduto.dtprevisaoentrega,pedidovendaproduto.prioridade,motorista.cdmotorista,motorista.nome,carregamentostatus.cdcarregamentostatus")

					.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
					.leftOuterJoin("carregamento.listaCarregamentoitem listaCarregamentoitem")
					.leftOuterJoin("carregamento.box box")
					.leftOuterJoin("carregamento.filialretirada filialretirada")
					.leftOuterJoin("carregamento.tipooperacaoretirada tipooperacaoretirada")
					.leftOuterJoin("carregamento.motorista motorista")
					.leftOuterJoin("carregamento.veiculo veiculo")
					.leftOuterJoin("veiculo.transportador transportador")
					.leftOuterJoin("listaCarregamentoitem.pedidovendaproduto pedidovendaproduto")
					.leftOuterJoin("pedidovendaproduto.pessoaendereco pessoaendereco")
					.leftOuterJoin("pedidovendaproduto.tipooperacao tipooperacao")
					.leftOuterJoin("pedidovendaproduto.produto produto")
					.leftOuterJoin("pedidovendaproduto.pedidovenda pedidovenda")
					.leftOuterJoin("pedidovenda.filialemissao filialemissao")
					.leftOuterJoin("pedidovendaproduto.filialEntrega filialEntrega")
					.leftOuterJoin("pedidovendaproduto.filialentregatransbordo filialentregatransbordo")
					.leftOuterJoin("pedidovendaproduto.depositotransbordo depositotransbordo")
					.leftOuterJoin("filialEntrega.pessoaendereco enderecofilial")
					.leftOuterJoin("filialentregatransbordo.pessoaendereco enderecofilialtransbordo")
					.leftOuterJoin("pedidovenda.cliente cliente")
					.leftOuterJoin("cliente.pessoaendereco pessoaenderecocliente")
					.leftOuterJoin("pedidovendaproduto.praca praca")
					.leftOuterJoin("praca.listaRotapraca listaRotapraca")
					.leftOuterJoin("listaRotapraca.rota rota")
					.where("carregamento.cdcarregamento =?",cdcarregamento)
					.orderBy("listaCarregamentoitem.ordem")
					.unique();
	}
	
	/**
	 * Carrega o carregamento para a tela de carregamento.
	 * 
	 * @param carregamento
	 * @author Pedro Gonçalves
	 * @return
	 */
	public Carregamento loadCarregamentoForGerenciamento(Carregamento carregamento){
		return query()
			.joinFetch("carregamento.carregamentostatus carregamentostatus")
			.leftOuterJoinFetch("carregamento.deposito deposito")
			.leftOuterJoinFetch("carregamento.veiculo veiculo")				
			.leftOuterJoinFetch("veiculo.transportador transportador")				
			.leftOuterJoinFetch("carregamento.box box")
			.leftOuterJoinFetch("carregamento.expedicao expedicao")
			.leftOuterJoinFetch("carregamento.listaOrdemservico listaOrdemservico")
			.leftOuterJoinFetch("listaOrdemservico.ordemtipo ordemtipo")
			.leftOuterJoinFetch("listaOrdemservico.ordemstatus ordemstatus")
			.entity(carregamento)
			.unique();
	}
	
	/* singleton */
	private static CarregamentoDAO instance;
	public static CarregamentoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(CarregamentoDAO.class);
		}
		return instance;
	}

	/**
	 * Chama a procedure 'enderecar_mapasseparacao' no banco de dados do sistema
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param deposito
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void enderecarCarregamento(final Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Dados insuficientes para invocar a função 'enderecar_mapasseparacao'");

		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("enderecar_mapaseparacao")
						.setInteger(0, carregamento.getCdcarregamento())
						.executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			throw new WmsException("Erro ao executar a procedure ENDERECAR_MAPASSEPARACAO.", e);
		}
	}
	
	/**
	 * Chama a procedure 'GERAR_REABASTECIMENTO' no banco de dados do sistema
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void gerarReabastecimento(final Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new ReabastecimentoException("Dados insuficientes para invocar a função 'GERAR_REABASTECIMENTO'");
		
		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("gerar_reabastecimento").setInteger(0, carregamento.getCdcarregamento()).executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			e.printStackTrace();

			Pattern padrao = Pattern.compile(".*###(.*)###.*",Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
			Matcher matcher = padrao.matcher(e.getMessage());
			String msgRetorno;
			if (matcher.matches()) {
				msgRetorno = matcher.group(1);
			}
			else {
				msgRetorno = e.getMessage();
			}

			
			throw new ReabastecimentoException("Erro ao gerar reabastecimento de picking.\n" + msgRetorno);
		}
	}
	
	/**
	 * Chama a procedure 'GERAR_REABASTECIMENTO_BOX' no banco de dados do sistema
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param expedicao
	 * @throws SQLException 
	 */
	public void gerarReabastecimentoBox(final Expedicao expedicao) {
		if(expedicao == null || expedicao.getCdexpedicao() == null)
			throw new ReabastecimentoException("Dados insuficientes para invocar a função 'GERAR_REABASTECIMENTO_BOX'");
		
		try{
			getHibernateTemplate().execute(new HibernateCallback(){
				
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("gerar_reabastecimento_box").setInteger(0, expedicao.getCdexpedicao()).executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			e.printStackTrace();
			
			Pattern padrao = Pattern.compile(".*###(.*)###.*",Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
			Matcher matcher = padrao.matcher(e.getMessage());
			String msgRetorno;
			if (matcher.matches()) {
				msgRetorno = matcher.group(1);
			}
			else {
				msgRetorno = e.getMessage();
			}
			
			
			throw new ReabastecimentoException("Erro ao gerar reabastecimento de picking.\n" + msgRetorno);
		}
	}

	/**
	 * Atualiza a lista de rotas no carregamento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param carregamento
	 */
	public void atualizarListaRotas(Carregamento carregamento) {
		
		if(carregamento==null || carregamento.getCdcarregamento() == null){
			throw new WmsException("Dados insuficientes para invocar a procedure 'ATUALIZAR_CARREGAMENTO'");
		}
		
		getJdbcTemplate().execute("BEGIN ATUALIZAR_CARREGAMENTO(" + carregamento.getCdcarregamento() +"); END;");	
	}

	/**
	 * Faz a baixa de estoque a partir de um carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	@SuppressWarnings("unchecked")
	public void baixarEstoque(final Carregamento carregamento) throws WmsException{
		if (carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo");
		
		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("baixar_estoque_carregamento")
						.setInteger(0, carregamento.getCdcarregamento())
						.executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			throw new WmsException("Erro ao realizar a baixa de estoque.", e);
		}
	}
	
	/**
	 * Carrega um Carregamento e sua propriedade Carregamentostatus.
	 * 
	 * @param carregamento
	 * @return
	 */
	public Carregamento loadWithStatus(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null){
			return null;
		}
		return query()
			.joinFetch("carregamento.carregamentostatus")
			.entity(carregamento)
			.unique();
	}

	/**
	 * Método que verifica se há algum item que foi faturado em outra filial.
	 * 
	 * @param carregamento
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean existeItemFaturadoEmOutraFilial(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parametros inválidos.");
		return newQueryBuilderWithFrom(Long.class)
			.select("count(*)")
			.join("carregamento.listaCarregamentoitem listaCarregamentoitem")
			.join("listaCarregamentoitem.pedidovendaproduto pedidovendaproduto")
			.join("pedidovendaproduto.deposito deposito")
			.join("pedidovendaproduto.listaCarregamentoitem listaCarregamentoitem")
			.join("pedidovendaproduto.filialnota filialnota")
			.where("carregamento = ?", carregamento)
			.where("pedidovendaproduto.carregado = ?", Boolean.TRUE)
			.where("filialnota.documento <> deposito.cnpj")
			.setUseTranslator(false)
			.unique()
			.longValue()>0;
	}
	
	/**
	 * Método que atualiza referência do carregamento para expedição e atualiza o veículo
	 * 
	 * @param expedicao
	 * @param carregamento
	 * @author Tomás Rabelo
	 */
	public void atualizarExpedicao(Expedicao expedicao,	Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
			sql.append(" update Carregamento carregamento set ");
			sql.append(" 	carregamento.expedicao.id=?, ");
			sql.append(" 	carregamento.veiculo.id=?, ");
			sql.append(" 	carregamento.paletesdisponiveis =?, ");
			sql.append(" 	carregamento.tipocarregamento.id=?, ");
			sql.append(" 	carregamento.prioridade=?, ");
			sql.append(" 	carregamento.observacao=? ");
			sql.append(" where carregamento.id=? ");
		
		getHibernateTemplate().bulkUpdate(sql.toString(), new Object[]{expedicao.getCdexpedicao(), 
																		carregamento.getVeiculo().getCdveiculo(), 
																		carregamento.getPaletesdisponiveis(), 
																		carregamento.getTipocarregamento().getCdtipocarregamento(), 
																		carregamento.getPrioridade(), 
																		carregamento.getObservacao(), 
																		carregamento.getCdcarregamento()});		
	}
	
	/**
	 * Busca os carregamentos associados a uma expedição.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @param carregamentostatus
	 * @return
	 */
	public List<Carregamento> findForLiberacao(EmitirliberacaoveiculoFiltro filtro, Carregamentostatus carregamentostatus) {
		return query()
			.joinFetch("carregamento.box box")
			.leftOuterJoinFetch("carregamento.veiculo veiculo")
			.leftOuterJoinFetch("veiculo.transportador transportador")
			.where("carregamento.expedicao.id = ?", filtro.getCdexpedicao())
			.whereIn("carregamento.id", filtro.getListaCarregamento())
			.where("carregamento.carregamentostatus = ?", carregamentostatus)
			.list();
	}

	/**
	 * Localiza todos os carregamentos de uma determinada expedição.
	 * 
	 * @author Giovane Freitas
	 * @param expedicao
	 * @return
	 */
	public List<Carregamento> findByExpedicao(Expedicao expedicao) {
		if(expedicao == null || expedicao.getCdexpedicao() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.joinFetch("carregamento.deposito deposito")
			.joinFetch("carregamento.carregamentostatus status")
			.where("carregamento.expedicao = ? ", expedicao)
			.list();
	}
	
	/**
	 * Busca todos os carregamentos que pertencem a mesma onda de separação.
	 * 
	 * @author Giovane Freitas
	 * @param cdsOS
	 * @return
	 */
	public List<Carregamento> findByOnda(String cdsOS) {
		if(cdsOS == null || cdsOS.isEmpty())
			throw new WmsException("Parâmetros inválidos.");
		
		cdsOS = cdsOS.trim();
		if (cdsOS.endsWith(","))
			cdsOS = cdsOS.substring(0, cdsOS.length() - 1);
		
		return query()
			.join("carregamento.listaCarregamentoitem ci")
			.join("ci.listaEtiquetaexpedicao ee")
			.join("ee.ordemservicoproduto osp")
			.join("osp.listaOrdemprodutoLigacao opl")
			.whereIn("opl.ordemservico.id", cdsOS)
			.orderBy("carregamento.cdcarregamento")
			.list();
	}

	public SqlRowSet getDadosPlanilhaConferencia(PlanilhaConferenciaFiltro filtro) {
		if(filtro == null || filtro.getDataEmissaoPedido() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ");
		sql.append("  d.nome AS \"Depósito\", ");
		sql.append("  tp.nome AS \"Tipo de operação\", ");
		sql.append("  pv.dtemissao AS \"Data de emissão\", ");
		sql.append("  pv.numero AS \"Nº Pedido\", ");
		sql.append("  pvp.dtchegadaerp AS \"Chegou no WMS em\", ");
		sql.append("  pvp.dtprevisaoentrega AS \"Entrega prevista para\", ");
		sql.append("  c.dtcarregamento AS \"Carga montada em\", ");
		sql.append("  c.dtfimcarregamento AS \"Hora de finalização\", ");
		sql.append("  Sum(pvp.qtde * pvp.valor)/100 AS \"Valor\", ");
		sql.append("  pvp.dtfaturamento AS \"Faturado em\", ");
		sql.append("  Min(osu1.dtinicio) AS \"Início 1º conferência\", ");
		sql.append("  Max(osu1.dtfim) AS \"Término 1º conferência\", ");
		sql.append("  Min(osu2.dtinicio) AS \"Início 2º conferência\", ");
		sql.append("  Max(osu2.dtfim) AS \"Término 2º conferência\", ");
		sql.append("  Sum (cubagemunitaria_produto(pvp.cdproduto) * pvp.qtde) AS \"Cubagem M³\", ");
		sql.append("  Sum(pvp.qtde) AS \"Itens\" ");

		sql.append("FROM pedidovendaproduto pvp ");
		sql.append("  join pedidovenda pv ON pv.cdpedidovenda = pvp.cdpedidovenda ");
		sql.append("  join tipooperacao tp ON tp.cdtipooperacao = pvp.cdtipooperacao ");
		sql.append("  join carregamentoitem ci ON ci.cdpedidovendaproduto = pvp.cdpedidovendaproduto AND ci.qtdeconfirmada > 0 ");
		sql.append("  join carregamento c ON c.cdcarregamento = ci.cdcarregamento AND c.cdcarregamentostatus <> " + Carregamentostatus.CANCELADO.getCdcarregamentostatus() + " ");
		sql.append("  join deposito d ON d.cddeposito = pvp.cddeposito ");
		sql.append("  join etiquetaexpedicao ee ON ee.cdcarregamentoitem = ci.cdcarregamentoitem ");
		sql.append("  join ordemprodutoligacao opl ON opl.cdordemservicoproduto = ee.cdordemservicoproduto ");
		sql.append("  join ordemservico os ON os.cdordemservico = opl.cdordemservico ");
		sql.append("  left join ordemservicousuario osu1 ON osu1.cdordemservico = opl.cdordemservico AND os.cdordemtipo IN (" + Ordemtipo.CONFERENCIA_EXPEDICAO_1.getCdordemtipo() + ", " + Ordemtipo.RECONFERENCIA_EXPEDICAO_1.getCdordemtipo() + ") ");
		sql.append("  left join ordemservicousuario osu2 ON osu2.cdordemservico = opl.cdordemservico AND os.cdordemtipo IN (" + Ordemtipo.CONFERENCIA_EXPEDICAO_2.getCdordemtipo() + ", " + Ordemtipo.RECONFERENCIA_EXPEDICAO_2.getCdordemtipo() + ") ");
		sql.append("WHERE Trunc(pv.dtemissao, 'DD') = ? ");

		sql.append("GROUP BY d.nome, tp.nome, pv.dtemissao, pv.numero, pvp.dtchegadaerp, pvp.dtprevisaoentrega, ");
		sql.append("  c.dtcarregamento, c.dtfimcarregamento, pvp.dtfaturamento ");

		sql.append("UNION ALL ");

		sql.append("SELECT ");
		sql.append("  d.nome AS \"Depósito\", ");
		sql.append("  tp.nome AS \"Tipo de operação\", ");
		sql.append("  pv.dtemissao AS \"Data de emissão\", ");
		sql.append("  pv.numero AS \"Nº Pedido\", ");
		sql.append("  pvp.dtchegadaerp AS \"Chegou no WMS em\", ");
		sql.append("  pvp.dtprevisaoentrega AS \"Entrega prevista para\", ");
		sql.append("  NULL AS \"Carga montada em\", ");
		sql.append("  NULL AS \"Hora de finalização\", ");
		sql.append("  Sum(pvp.qtde * pvp.valor)/100 AS \"Valor\", ");
		sql.append("  pvp.dtfaturamento AS \"Faturado em\", ");
		sql.append("  NULL AS \"Início 1º conferência\", ");
		sql.append("  NULL AS \"Término 1º conferência\", ");
		sql.append("  NULL AS \"Início 2º conferência\", ");
		sql.append("  NULL AS \"Término 2º conferência\", ");
		sql.append("  Sum (cubagemunitaria_produto(pvp.cdproduto) * pvp.qtde) AS \"Cubagem  M³\", ");
		sql.append("  Sum(pvp.qtde) AS \"Itens\" ");

		sql.append("FROM pedidovendaproduto pvp ");
		sql.append("  join pedidovenda pv ON pv.cdpedidovenda = pvp.cdpedidovenda ");
		sql.append("  join tipooperacao tp ON tp.cdtipooperacao = pvp.cdtipooperacao ");
		sql.append("  join deposito d ON d.cddeposito = pvp.cddeposito ");
		sql.append("WHERE Trunc(pv.dtemissao, 'DD') = ? ");
		sql.append("  AND pvp.carregado = 0 ");

		sql.append("GROUP BY d.nome, tp.nome, pv.dtemissao, pv.numero, pvp.dtchegadaerp, pvp.dtprevisaoentrega, pvp.dtfaturamento ");
		                                                
		
		Object[] args = new Object[]{filtro.getDataEmissaoPedido(), filtro.getDataEmissaoPedido()};
		
		return getJdbcTemplate().queryForRowSet(sql.toString(), args);
	}
	
	@SuppressWarnings("unchecked")
	public List<Carregamento> loadCarregamentos() {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select distinct c.cdcarregamento, v.placa, p.nome as transportador, count(ci.cdcarregamentoitem) as itens ");
		sql.append(" from carregamentoitem ci  ");
		sql.append(" join carregamento c on c.cdcarregamento = ci.cdcarregamento ");
		sql.append(" left join veiculo v on v.cdveiculo = c.cdveiculo ");
		sql.append(" left join pessoa p on p.cdpessoa = v.cdpessoa ");
		sql.append(" left join expedicao e on e.cdexpedicao = c.cdexpedicao");
		sql.append(" where c.dtfimcarregamento is not null ");
		sql.append(" and c.cdcarregamentostatus = 4 ");
		sql.append(" and ci.dtsincronizacao is null  ");
		sql.append(" and ci.qtdeconfirmada > 0");
		sql.append(" and ci.qtdeconfirmada > 0 ");
		sql.append(" and (e.cdexpedicao is null or e.cdexpedicaostatus = 2)  ");
		sql.append(" group by c.cdcarregamento, v.placa, p.nome ");

		return (List<Carregamento>) getJdbcTemplate().query(sql.toString(), new Object[]{},
			new ResultSetExtractor(){
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Carregamento> list = new ArrayList<Carregamento>();
					
					while (rs.next()){
						Carregamento carregamento = new Carregamento();
						Veiculo veiculo = new Veiculo();
						Transportador transportador = new Transportador();
						carregamento.setCdcarregamento(rs.getInt("c.cdcarregamento"));
						veiculo.setPlaca(rs.getString("v.placa"));
						transportador.setNome(rs.getString("transportador"));
						veiculo.setTransportador(transportador);
						carregamento.setVeiculo(veiculo);
						carregamento.setQtdeItens(rs.getLong("itens"));
						list.add(carregamento);
					}
					return list;
				}
			}
		);
	}
	
	public List<Carregamento> findByDeposito(Integer cddeposito){
		if(cddeposito == null){
			throw new WmsException("Parâmetros incorretos");
		}
		
		return query()
				.select("carregamento.cdcarregamento, carregamento.dtcarregamento, box.nome, " +
						"carregamento.listaRota, veiculo.placa, transportador.cdpessoa, transportador.nome," +
						"deposito.cddeposito, carregamentostatus.cdcarregamentostatus")
				.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
				.leftOuterJoin("carregamento.veiculo veiculo")
				.leftOuterJoin("veiculo.transportador transportador")
				.join("carregamento.deposito deposito")
				.join("carregamento.box box")
				.where("deposito.cddeposito = ?", cddeposito)
				.where("carregamentostatus = ?", Carregamentostatus.EM_SEPARACAO)
				.list();
	}
	

	/**
	 * Busca os produtos que estão em carregamentos montados e que não possui
	 * estoque disponível para gerar a separação.
	 * 
	 * @author Giovane Freitas
	 * @param whereIn
	 *            Lista de IDs de Carregamento.
	 */
	@SuppressWarnings("unchecked")
	public List<ProdutoSemEstoqueVO> findProdutosSemEstoque(final String whereIn) {
		if (whereIn == null || whereIn.isEmpty())
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("select cdproduto, codigo, descricao, qtdepedido,  nvl(estoque, 0) as estoque \n");
		sql.append("from ( \n");
		sql.append("  select pvp.cdproduto, nvl(v.codigo, p.codigo) as codigo, p.descricao, sum(pvp.qtde) as qtdepedido, \n"); 
		sql.append("    ( select sum(qtde) - sum(qtdereservadasaida) \n");
		sql.append("      from enderecoproduto ep    \n");
		sql.append("        join endereco e on e.cdendereco = ep.cdendereco \n");
		sql.append("        join area a on a.cdarea = e.cdarea \n");
		sql.append("      where (ep.cdproduto = pvp.cdproduto or ep.cdproduto = v.cdproduto) \n");
		sql.append("        and a.cddeposito = c.cddeposito \n");
		sql.append("        and a.avaria = 0 \n");
		
		if (configuracaoService.isTrue(ConfiguracaoVO.BLOQUEAR_MOVIMENTACAO_AREA_BOX, WmsUtil.getDeposito()))
			sql.append("        and a.box = 0 \n");
		
		if (configuracaoService.isTrue(ConfiguracaoVO.BLOQUEAR_SAIDA_AREA_VIRTUAL, WmsUtil.getDeposito()))
			sql.append("        and a.virtual = 0 \n");
		
		sql.append("        and e.cdenderecostatus in (?,?) \n");
		args.add(Enderecostatus.OCUPADO.getCdenderecostatus());
		args.add(Enderecostatus.RESERVADO.getCdenderecostatus());
		
		sql.append("    ) as estoque \n");
		sql.append("  from carregamento c  \n");
		sql.append("    join carregamentoitem ci on ci.cdcarregamento = c.cdcarregamento \n");
		sql.append("    join pedidovendaproduto pvp on pvp.cdpedidovendaproduto = ci.cdpedidovendaproduto \n");
		sql.append("    join pedidovenda pv ON pv.cdpedidovenda = pvp.cdpedidovenda \n");
		   
		sql.append("    join produto p on p.cdproduto = pvp.cdproduto \n");
		
		sql.append("    left join produto v on v.cdprodutoprincipal = pvp.cdproduto \n");
		sql.append("  where c.cdcarregamento in (").append(whereIn).append(") \n");
		sql.append("  group by pvp.cdproduto, v.codigo, p.codigo, p.descricao, pvp.cdproduto, v.cdproduto, c.cddeposito \n");
		sql.append(") \n");
		sql.append("where qtdepedido > nvl(estoque, 0) \n");
		sql.append("order by codigo \n");

		List<ProdutoSemEstoqueVO> listaBD = getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProdutoSemEstoqueVO item = new ProdutoSemEstoqueVO();
				item.setCdproduto(rs.getInt("cdproduto"));
				item.setCodigo(rs.getString("codigo"));
				item.setDescricao(rs.getString("descricao"));
				item.setQtdepedido(rs.getInt("qtdepedido"));
				item.setEstoque(rs.getInt("estoque"));
//				item.setCdcarregamento(rs.getInt("cdcarregamento"));
				return item;
			}
			
		});
		List<ProdutoSemEstoqueVO> listaProcessada = new ArrayList<ProdutoSemEstoqueVO>();
		for (ProdutoSemEstoqueVO item : listaBD) {
			Map<Integer, List<String>> mapa = getCarregamentoAndPedidosForFindProdutosSemEstoque(item, whereIn);
			List<Integer> listaCdCarregamento = new ArrayList<Integer>(mapa.keySet());
			for(int i=0; i< listaCdCarregamento.size(); i++) {
				Integer cdcarregamento = listaCdCarregamento.get(i);
				if(i==0){
					item.setCdcarregamento(cdcarregamento);
					item.setNumeropedido(CollectionsUtil.concatenate(mapa.get(cdcarregamento), "<br/>" ));
					listaProcessada.add(item);
				}else{ //se tem em mais de um carregamento, preciso inserir outro registro na lista 
					ProdutoSemEstoqueVO novoItem = new ProdutoSemEstoqueVO();
//					novoItem.set
					novoItem.setCdcarregamento(cdcarregamento);
					novoItem.setCdproduto(item.getCdproduto());
					novoItem.setCodigo(item.getCodigo());
					novoItem.setDescricao(item.getDescricao());
					novoItem.setEstoque(item.getEstoque());
					novoItem.setNumeropedido(CollectionsUtil.concatenate(mapa.get(cdcarregamento), "<br/>" ));
					novoItem.setQtdepedido(item.getQtdepedido());
					listaProcessada.add(novoItem);
				}
			}
		}
		return listaProcessada;
	}
	
	private Map<Integer, List<String>> getCarregamentoAndPedidosForFindProdutosSemEstoque(ProdutoSemEstoqueVO item, String whereIn){
		StringBuilder sb = new StringBuilder(" SELECT CI.CDCARREGAMENTO, PV.NUMERO")
		.append(" FROM PEDIDOVENDAPRODUTO PP JOIN PEDIDOVENDA PV ON (PV.CDPEDIDOVENDA = PP.CDPEDIDOVENDA) ")
		.append(" JOIN CARREGAMENTOITEM CI ON (PP.CDPEDIDOVENDAPRODUTO = CI.CDPEDIDOVENDAPRODUTO) ")
		.append(" WHERE CI.CDCARREGAMENTO IN (").append(whereIn).append(" ) ")
		.append(" AND PP.CDPRODUTO = ? "); 
		SqlRowSet rowSet = getJdbcTemplate().queryForRowSet(sb.toString(), new Object[]{item.getCdproduto()});
		Map<Integer, List<String>> mapa = new HashMap<Integer, List<String>>();
		while(rowSet.next()){
			Integer cdcarregamento = rowSet.getInt("cdcarregamento");
			String numeroPedido = rowSet.getString("numero");
			
			List<String> lista = new ArrayList<String>();
			if(mapa.containsKey(cdcarregamento))
				lista = mapa.get(cdcarregamento);
			lista.add(numeroPedido);
			mapa.put(cdcarregamento, lista);
		}
		return mapa;
	}

	/**
	 * 
	 * @param cdcarregamento
	 */
	public void callAtualizarCarregamento(Integer cdcarregamento) {
		if(cdcarregamento == null || cdcarregamento<=0)
			throw new WmsException("Dados insuficientes para invocar a procedure 'ATUALIZAR_CARREGAMENTO'");
		
		getJdbcTemplate().update("BEGIN ATUALIZAR_CARREGAMENTO(" + cdcarregamento +"); END;");
	}

	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Carregamento> validarCarregamentoCancelado(String whereIn) {
		return query()
				.select("carregamento.cdcarregamento")
				.join("carregamento.carregamentostatus carregamentostatus")
				.where("carregamentostatus = ?",Carregamentostatus.MONTADO)
				.list();
	}

	/**
	 * 
	 * @param expedicao
	 * @return
	 */
	public List<Carregamento> findCarregamentoStatusByExepdicao(Expedicao expedicao){
		
		return query()
				.select("carregamento.cdcarregamento")
				.join("carregamento.carregamentostatus carregamentostatus")
				.join("carregamento.expedicao expedicao")
				.where("carregamentostatus <> ?",Carregamentostatus.CANCELADO)
				.where("expedicao = ?",expedicao)
				.list();
	}

	/**
	 * 
	 * @param expedicao
	 * @return
	 */
	public List<Carregamento> findCarregamentoOSByExpedicao(Expedicao expedicao){
		
		return query()
				.select("carregamento.cdcarregamento")
				.join("carregamento.listaOrdemservico listaOrdemservico")
				.join("carregamento.expedicao expedicao")
				.where("expedicao = ?",expedicao)
				.list();
	}

	/**
	 * 
	 * @param whereIn 
	 * @param carregamento 
	 * @return
	 */
	public List<Carregamento> findStatusForExclusao(Carregamento carregamento, String whereIn){
		
		return query()
				.select("carregamento.cdcarregamento")
				.join("carregamento.carregamentostatus carregamentostatus")
				.where("carregamentostatus <> ?",Carregamentostatus.MONTADO)
				.where("carregamentostatus <> ?",Carregamentostatus.EM_MONTAGEM)
				.whereIn("carregamento.cdcarregamento",whereIn)
				.where("carregamento = ?",carregamento)
				.list();
	}

}