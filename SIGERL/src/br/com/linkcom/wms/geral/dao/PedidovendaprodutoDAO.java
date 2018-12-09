package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Pedidovendaprodutostatus;
import br.com.linkcom.wms.geral.bean.Pessoaendereco;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.service.MunicipioService;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PedidovendaprodutoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.RotaManualPedidoVendaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidovendaprodutoDAO extends GenericDAO<Pedidovendaproduto> {
	
//	@Override
//	public void updateListagemQuery(QueryBuilder<Pedidovendaproduto> query, FiltroListagem _filtro) {
//		query.leftOuterJoinFetch("pedidovendaproduto.produtovendaproduto produtovendaproduto");
//		query.leftOuterJoinFetch("pedidovendaproduto.pedidovenda pedidovenda");
//		query.leftOuterJoinFetch("pedidovendaproduto.deposito deposito");
//		query.leftOuterJoinFetch("pedidovendaproduto.produto produto");
//		query.leftOuterJoinFetch("pedidovendaproduto.enderecoentrega enderecoentrega");
//	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Pedidovendaproduto> query, FiltroListagem _filtro) {
		
		PedidovendaprodutoFiltro filtro = (PedidovendaprodutoFiltro) _filtro;
		StringBuilder numeroPedido = new StringBuilder();
		
		if(filtro.getNumeroPedido()!=null && !filtro.getNumeroPedido().isEmpty()){
			numeroPedido.append(filtro.getNumeroPedido());
		}else if(filtro.getEmpresa()!=null && filtro.getEmpresa().getCdempresa()!=null && filtro.getNumeroPedidoMV()!=null && filtro.getNumeroPedidoMV().isEmpty() && filtro.getNumeroLoja().isEmpty()){
			numeroPedido.append("5"); 
			numeroPedido.append(filtro.getEmpresa().getCdempresa());
			numeroPedido.append(StringUtils.leftPad(filtro.getNumeroPedidoMV(), 7, "0"));
			numeroPedido.append(StringUtils.leftPad(filtro.getNumeroLoja(), 4, "0"));
		}
		
		query.select("distinct pedidovendaproduto.cdpedidovendaproduto, pedidovendaprodutostatus.cdpedidovendaprodutostatus, " +
				"pedidovendaprodutostatus.nome, pedidovenda.cdpedidovenda, pedidovenda.numero, produto.cdproduto, produto.descricao, " +
				"produto.codigo, produto.codigoerp, pedidovendaproduto.dtprevisaoentrega, pedidovendaproduto.carregado, " +
				"pedidovendaproduto.dtmarcacao, pedidovendaproduto.dtexclusaoerp, tipooperacao.cdtipooperacao, tipooperacao.nome ");
		query.join("pedidovendaproduto.pedidovendaprodutostatus pedidovendaprodutostatus");
		query.join("pedidovendaproduto.produto produto");
		query.join("pedidovendaproduto.pedidovenda pedidovenda");
		query.join("pedidovendaproduto.deposito deposito");
		query.join("pedidovendaproduto.tipooperacao tipooperacao");
		query.where("pedidovendaprodutostatus = ?",filtro.getPedidovendaprodutostatus());
		query.where("deposito = ?",filtro.getDeposito());
		query.where("tipooperacao = ?",filtro.getTipooperacao());
		query.where("produto = ?",filtro.getProduto());
		query.where("trunc(pedidovendaproduto.dtprevisaoentrega) >= ?",filtro.getDtemissaoInicio());
		query.where("trunc(pedidovendaproduto.dtprevisaoentrega) <= ?",filtro.getDtemissaoFim());
		
		if (filtro.getCorte() != null && !filtro.getCorte()){
			query.openParentheses();
			query.where("pedidovendaproduto.corte = ?",filtro.getCorte());
			query.or();
			query.where("pedidovendaproduto.corte is null");
			query.closeParentheses();
		}else{
			query.where("pedidovendaproduto.corte = ?",filtro.getCorte());
		}

		if(numeroPedido!=null){
			query.where("pedidovenda.numero = ? ",numeroPedido.toString());
		}
		
		query.list();
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Pedidovendaproduto> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	
	/**
	 * Busca todos os Pedidovendaproduto do pedidovenda
	 * informado
	 *
	 * @author Leonardo Guimarães
	 * 
	 * @param pedidovenda
	 * @return
	 */
	public List<Pedidovendaproduto> findByPedidoVenda(Pedidovenda pedidovenda,String pedidosVendaProduto) {
		
		if(pedidovenda == null || pedidovenda.getCdpedidovenda() == null)
			throw new WmsException("O pedido de veda não deve ser nulo.");
		
		QueryBuilder<Pedidovendaproduto> query = query()
					 .select("distinct pedidovendaproduto.cdpedidovendaproduto, pedidovendaproduto.qtde, pedidovendaproduto.valor,pedidovendaproduto.dtprevisaoentrega," +
					 		"produto.cdproduto, produto.descricao, produto.peso, produto.altura,produto.profundidade, produto.largura")
					 .leftOuterJoin("pedidovendaproduto.produto produto")
					 .leftOuterJoin("pedidovendaproduto.pedidovenda pedidovenda")
					 .where("pedidovenda = ?",pedidovenda)
					 .openParentheses()
					 	.where("pedidovendaproduto.carregado is false")
					 	.or()
					 	.where("pedidovendaproduto.carregado is null")
					 .closeParentheses()
		  			 .orderBy("produto.descricao");
		
		query.ignoreJoin("pedidovenda");
		
		if(pedidosVendaProduto != null && !"".equals(pedidosVendaProduto))
			query.where("pedidovendaproduto.cdpedidovendaproduto not in("+pedidosVendaProduto+")");
		
		return query .list();
	}
	
	/**
	 * Busca os pedidovendaproduto através do cdpraca,cdtipooperação, cep, cdcliente e número (do pedido)
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param cdpedidovendaproduto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Pedidovendaproduto> findByFiveKeys(String[] array,Integer cdcarregamento, Date datainicio, Date datafim) {
		if(array == null || array.length < 3)
			throw new WmsException("Parâmetros incorretos.");
		
		StringBuilder builder = new StringBuilder();
		
		String select = " SELECT PP.cdpedidovendaproduto,PP.qtde,PP.valor,PP.dtprevisaoentrega,PP.carregado,PP.observacao, " + 
				" PE.cdpedidovenda,PE.numero,P.codigo,P.cdproduto,P.descricao,PESOUNITARIO_PRODUTO(P.cdproduto) as pesounitario,P.altura,P.profundidade," +
				"P.largura,CUBAGEMUNITARIA_PRODUTO(P.cdproduto) as cubagemunitaria, E.cdpessoaendereco, PP.prioridade "; 
		
		if (cdcarregamento != null)
			select += ",CI.cdcarregamentoitem ";
		else
			select += ", null as cdcarregamentoitem ";
			

		makeQuery(array, cdcarregamento, builder, select, true, datainicio, datafim);
		
		Object result = getJdbcTemplate().query(builder.toString(), new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {

				Map<Integer, Pedidovendaproduto> itens = new HashMap<Integer, Pedidovendaproduto>();
				
				while (rs.next()){
					Pedidovendaproduto pedidovendaproduto;
					
					if (itens.containsKey(rs.getInt("cdpedidovendaproduto"))){
						pedidovendaproduto = itens.get(rs.getInt("cdpedidovendaproduto"));
					}else{
						pedidovendaproduto = new Pedidovendaproduto();
			
						pedidovendaproduto.setCdpedidovendaproduto(rs.getInt("cdpedidovendaproduto"));
						pedidovendaproduto.setQtde(rs.getLong("qtde"));
						pedidovendaproduto.setValor(new Money(rs.getLong("valor"),true));
						pedidovendaproduto.setDtprevisaoentrega(rs.getDate("dtprevisaoentrega"));
						pedidovendaproduto.setCarregado(rs.getBoolean("carregado"));
						pedidovendaproduto.setPessoaendereco(new Pessoaendereco(rs.getInt("cdpessoaendereco")));
						pedidovendaproduto.setPrioridade(rs.getBoolean("prioridade"));
						pedidovendaproduto.setObservacao(rs.getString("observacao"));
						
						Pedidovenda pedidovenda = new Pedidovenda();
						pedidovenda.setCdpedidovenda(rs.getInt("cdpedidovenda"));
						pedidovenda.setNumero(rs.getString("numero"));
						pedidovendaproduto.setPedidovenda(pedidovenda);
						
						Produto produto = new Produto();
						produto.setCodigo(rs.getString("codigo"));
						produto.setCdproduto(rs.getInt("cdproduto"));
						produto.setDescricao(rs.getString("descricao"));
						produto.setPesounitario(rs.getDouble("pesounitario"));
						produto.setAltura(rs.getLong("altura"));
						produto.setProfundidade(rs.getLong("profundidade"));
						produto.setLargura(rs.getLong("largura"));
						produto.setCubagemunitaria(rs.getDouble("cubagemunitaria"));
						pedidovendaproduto.setProduto(produto);
						
						itens.put(pedidovendaproduto.getCdpedidovendaproduto(), pedidovendaproduto);
					}
					
					//é uma lista
					Carregamentoitem carregamentoitem = new Carregamentoitem();
					carregamentoitem.setCdcarregamentoitem(rs.getInt("cdcarregamentoitem"));
					if (pedidovendaproduto.getListaCarregamentoitem() == null)
						pedidovendaproduto.setListaCarregamentoitem(new ListSet<Carregamentoitem>(Carregamentoitem.class));
					
					pedidovendaproduto.getListaCarregamentoitem().add(carregamentoitem);
				}
				
				return new ArrayList<Pedidovendaproduto>(itens.values());
			}
			
		});
		
		
		return (List<Pedidovendaproduto>) result;
	}
	
	/**
	 * Busca uma lista de cdpedidovendaproduto através do cdpraca,cdtipooperação, cep, cdcliente e numero (do pedido)
	 * 
	 * Obs: O id do carregamento pode ser nulo.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento 
	 * 
	 * @param cdpedidovendaproduto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findCdsByFiveKeys(String[] array, Carregamento carregamento, Date datainicio, Date datafim) {
		if(array == null || array.length < 4 || carregamento == null)
			throw new WmsException("Parâmetros incorretos.");
		
		StringBuilder builder = new StringBuilder();
		
		StringBuilder select = new StringBuilder("SELECT /*+  OPTIMIZER_FEATURES_ENABLE('11.2.0.4')   */ ")
				.append("PP.cdpedidovendaproduto");
		
		makeQuery(array, carregamento.getCdcarregamento(), builder, select.toString(), false, datainicio, datafim);
		
		return getJdbcTemplate().queryForList(builder.toString(), Long.class);
	}
	
	/**
	 * Monta a consulta para os métodos findByFiveKeys e findCdsByFiveKeys
	 * 
	 * @author Giovane Freitas
	 * @param array
	 * @param cdcarregamento
	 * @param builder
	 * @param select
	 */
	private void makeQuery(String[] array, Integer cdcarregamento, StringBuilder builder, String select, boolean incluirJoinProduto, Date datainicio, Date datafim) {

		DateFormat dateFormatSQL = new SimpleDateFormat("dd/MM/yyyy");
		
		if (!select.toLowerCase().contains("ci.cdcarregamentoitem")){
			builder.append(select);
			builder.append(" FROM PEDIDOVENDA PE JOIN PEDIDOVENDAPRODUTO PP ON (PE.CDPEDIDOVENDA = PP.CDPEDIDOVENDA) "); 
	
			if (incluirJoinProduto)
				builder.append(" JOIN PRODUTO P ON (PP.CDPRODUTO = P.CDPRODUTO) ");
			
			builder.append(" JOIN PESSOAENDERECO E ON (PP.CDENDERECOENTREGA = E.CDPESSOAENDERECO) "); 
			builder.append(" WHERE "); 
			builder.append(" PP.DTMARCACAO IS NULL "); 
			builder.append(" AND PP.NUMERONOTA IS NULL "); 
			builder.append(" AND PP.CDFILIALENTREGA IS NULL "); 
			builder.append(" AND PP.DTEXCLUSAOERP IS NULL "); 
		
			if (WmsUtil.isDefined(array[1]))
				builder.append(" AND PP.CDPRACA = " + array[1]); 
			else
				builder.append(" AND PP.CDPRACA is null ");
			
			if (array.length >= 5 && WmsUtil.isDefined(array[4]))
				builder.append(" AND PE.cdpedidovenda = " + array[4]); 
	
			if (array.length >=4 && WmsUtil.isDefined(array[3]))
				builder.append(" AND E.cep = " + array[3]); 
			else
				builder.append(" AND E.cep is null "); 
		
			builder.append(" AND PP.CDTIPOOPERACAO = " + array[2]); 
			builder.append(" AND PE.CDCLIENTE = " + array[0]); 
			builder.append(" AND PP.carregado = 0 ");
			builder.append(" AND PP.cddeposito = " + WmsUtil.getDeposito().getCddeposito());
			
			if (datainicio != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) >= to_date('" + dateFormatSQL.format(datainicio) + "', 'DD/MM/YYYY') ");
			}
			if (datafim != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) <= to_date('" + dateFormatSQL.format(datafim) + "', 'DD/MM/YYYY') ");
			}
		}
		
		if (cdcarregamento != null){
			if (builder.length() > 0)
				builder.append(" UNION ALL "); 
			
			builder.append(select);
			builder.append(" FROM PEDIDOVENDA PE JOIN PEDIDOVENDAPRODUTO PP ON (PE.CDPEDIDOVENDA = PP.CDPEDIDOVENDA) "); 
	
			if (incluirJoinProduto)
				builder.append(" JOIN PRODUTO P ON (PP.CDPRODUTO = P.CDPRODUTO) ");
			
			builder.append(" JOIN PESSOAENDERECO E ON (PP.CDENDERECOENTREGA = E.CDPESSOAENDERECO) "); 
			builder.append(" JOIN CARREGAMENTOITEM CI ON (PP.CDPEDIDOVENDAPRODUTO = CI.CDPEDIDOVENDAPRODUTO) "); 
			builder.append(" JOIN CARREGAMENTO C ON C.CDCARREGAMENTO = CI.CDCARREGAMENTO AND C.CDCARREGAMENTOSTATUS IN (1,5)  "); 
			builder.append(" WHERE "); 
			builder.append(" PP.DTMARCACAO IS NULL "); 
			builder.append(" AND PP.NUMERONOTA IS NULL "); 
			builder.append(" AND PP.CDFILIALENTREGA IS NULL "); 
			builder.append(" AND PP.DTEXCLUSAOERP IS NULL ");
		
			if (WmsUtil.isDefined(array[1]))
				builder.append(" AND PP.CDPRACA = " + array[1]); 
			else
				builder.append(" AND PP.CDPRACA is null ");
			
			if (array.length >= 5 && WmsUtil.isDefined(array[4]))
				builder.append(" AND PE.cdpedidovenda = " + array[4]); 
	
			if (array.length >=4 && WmsUtil.isDefined(array[3]))
				builder.append(" AND E.cep = " + array[3]); 
			else
				builder.append(" AND E.cep is null "); 
		
			builder.append(" AND PP.CDTIPOOPERACAO = " + array[2]); 
			builder.append(" AND PE.CDCLIENTE = " + array[0]); 
			builder.append(" AND C.cdcarregamento = " + cdcarregamento);
			builder.append(" AND PP.cddeposito = " + WmsUtil.getDeposito().getCddeposito()); 
			
			if (datainicio != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) >= to_date('" + dateFormatSQL.format(datainicio) + "', 'DD/MM/YYYY') ");
			}
			if (datafim != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) <= to_date('" + dateFormatSQL.format(datafim) + "', 'DD/MM/YYYY') ");
			}


		}

		if (!select.toLowerCase().contains("ci.cdcarregamentoitem")){
			if (builder.length() > 0)
				builder.append(" UNION ALL "); 

			builder.append(select);
			builder.append(" FROM PEDIDOVENDA PE JOIN PEDIDOVENDAPRODUTO PP ON (PE.CDPEDIDOVENDA = PP.CDPEDIDOVENDA) "); 
	
			if(incluirJoinProduto)
				builder.append(" JOIN PRODUTO P ON (PP.CDPRODUTO = P.CDPRODUTO) "); 
			
			builder.append(" JOIN CLIENTE F ON (F.CDPESSOA = PP.CDFILIALENTREGA) "); 
			builder.append(" LEFT JOIN PESSOAENDERECO E ON (F.CDPESSOAENDERECO = E.CDPESSOAENDERECO) "); 
			builder.append(" WHERE "); 
			builder.append(" PP.DTMARCACAO IS NULL "); 
			builder.append(" AND PP.NUMERONOTA IS NULL ");
			builder.append(" AND PP.DTEXCLUSAOERP IS NULL ");
			
			if (WmsUtil.isDefined(array[1]))
				builder.append(" AND PP.CDPRACA = " + array[1]);
			else
				builder.append(" AND PP.CDPRACA is null ");
			
			if (array.length >= 5 && WmsUtil.isDefined(array[4]))
				builder.append(" AND PE.cdpedidovenda = " + array[4]); 
	
			if (array.length >=4 && WmsUtil.isDefined(array[3]))
				builder.append(" AND E.cep = " + array[3]); 
			else
				builder.append(" AND E.cep is null "); 
		
			builder.append(" AND PP.CDTIPOOPERACAO = " + array[2]); 
			builder.append(" AND PE.CDCLIENTE = " + array[0]); 
			builder.append(" AND PP.carregado = 0 ");
			builder.append(" AND PP.cddeposito = " + WmsUtil.getDeposito().getCddeposito()); 

			if (datainicio != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) >= to_date('" + dateFormatSQL.format(datainicio) + "', 'DD/MM/YYYY') ");
			}
			if (datafim != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) <= to_date('" + dateFormatSQL.format(datafim) + "', 'DD/MM/YYYY') ");
			}

		}
		
		if (cdcarregamento != null){
			if (builder.length() > 0)
				builder.append(" UNION ALL "); 

			builder.append(select);
			builder.append(" FROM PEDIDOVENDA PE JOIN PEDIDOVENDAPRODUTO PP ON (PE.CDPEDIDOVENDA = PP.CDPEDIDOVENDA) "); 

			if(incluirJoinProduto)
				builder.append(" JOIN PRODUTO P ON (PP.CDPRODUTO = P.CDPRODUTO) "); 
			
			builder.append(" JOIN CLIENTE F ON (F.CDPESSOA = PP.CDFILIALENTREGA) "); 
			builder.append(" LEFT JOIN PESSOAENDERECO E ON (F.CDPESSOAENDERECO = E.CDPESSOAENDERECO) "); 
			builder.append(" JOIN CARREGAMENTOITEM CI ON (PP.CDPEDIDOVENDAPRODUTO = CI.CDPEDIDOVENDAPRODUTO) "); 
			builder.append(" JOIN CARREGAMENTO C ON C.CDCARREGAMENTO = CI.CDCARREGAMENTO AND C.CDCARREGAMENTOSTATUS IN (1,5)  "); 
			builder.append(" WHERE "); 
			builder.append(" PP.DTMARCACAO IS NULL "); 
			builder.append(" AND PP.NUMERONOTA IS NULL ");
			builder.append(" AND PP.DTEXCLUSAOERP IS NULL ");
			
			if (WmsUtil.isDefined(array[1]))
				builder.append(" AND PP.CDPRACA = " + array[1]);
			else
				builder.append(" AND PP.CDPRACA is null ");
			
			if (array.length >= 5 && WmsUtil.isDefined(array[4]))
				builder.append(" AND PE.cdpedidovenda = " + array[4]); 

			if (array.length >=4 && WmsUtil.isDefined(array[3]))
				builder.append(" AND E.cep = " + array[3]); 
			else
				builder.append(" AND E.cep is null "); 
		
			builder.append(" AND PP.CDTIPOOPERACAO = " + array[2]); 
			builder.append(" AND PE.CDCLIENTE = " + array[0]); 			
			builder.append(" AND C.cdcarregamento = " + cdcarregamento);
			builder.append(" AND PP.cddeposito = " + WmsUtil.getDeposito().getCddeposito()); 

			if (datainicio != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) >= to_date('" + dateFormatSQL.format(datainicio) + "', 'DD/MM/YYYY') ");
			}
			if (datafim != null){
				builder.append(" AND trunc(PP.dtprevisaoentrega) <= to_date('" + dateFormatSQL.format(datafim) + "', 'DD/MM/YYYY') ");
			}

		}
	}

	/**
	 * Altera o status do pedido de venda do produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param pedidos
	 * @param status
	 */
	public void updateStatusPedidos(String pedido,Boolean status) {
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto set pedidovendaproduto.carregado=? " +
				"where pedidovendaproduto.id in ("+pedido+")",
				new Object[]{status});
	}
	
	/**
	 * Calcula a quantidade de enderecos de entrega
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @return
	 */
	public Integer getTotalEntrega(String cds) {
		QueryBuilder<Object[]> query = newQueryBuilderWithFrom(Object[].class)
									.select("distinct cliente.cdpessoa, pessoaendereco.cep")
									.join("pedidovendaproduto.pessoaendereco pessoaendereco")
									.join("pedidovendaproduto.pedidovenda pedidovenda")
									.join("pedidovenda.cliente cliente");
		if(cds != null && !"".equals(cds))
			   query.where("pedidovendaproduto.cdpedidovendaproduto in ("+cds+")");
		return query.setUseTranslator(false).list().size();
	}
	
	/* singleton */
	private static PedidovendaprodutoDAO instance;
	public static PedidovendaprodutoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidovendaprodutoDAO.class);
		}
		return instance;
	}
	
	/**
	 * Habilita os pedidos de venda do carregamento cancelado
	 * para que eles possam fazer parte de outro carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 */
	public void habilitarByCarregamentoCancelado(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pp set pp.carregado = 0 " +
				"where pp.cdpedidovendaproduto in (select ci.pedidovendaproduto.cdpedidovendaproduto " +
				"from Carregamentoitem ci where ci.carregamento.cdcarregamento = ? )",new Object[]{carregamento.getCdcarregamento()});
	}

	/**
	 * Método que corta o PVP de um carregamento
	 * 
	 * @param pedidovendaproduto
	 * @author Tomás Rabelo
	 */
	public void retiraPedidoVendaProdutoDoCarregamento(Pedidovendaproduto pedidovendaproduto) {
		if(pedidovendaproduto == null || pedidovendaproduto.getCdpedidovendaproduto() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pvp set pvp.carregado = ? where pvp.id=? ", 
				new Object[]{Boolean.FALSE, pedidovendaproduto.getCdpedidovendaproduto()});
	}

	/**
	 * Lista todos itens de pedidos de um determinado  
	 * 
	 * @param pedidovendaproduto
	 * @author Giovane Freitas
	 */
	public List<Pedidovendaproduto> findByCarregamento(Carregamento carregamento) {
		
		if(carregamento == null || carregamento.getCdcarregamento() == null) 
			throw new WmsException("O Carregamento não deve ser nulo.");
		
		QueryBuilder<Pedidovendaproduto> query = new QueryBuilder<Pedidovendaproduto>(getHibernateTemplate())
			.from(Pedidovendaproduto.class, "pvp")
			.join("pvp.listaCarregamentoitem ci")
			.joinFetch("pvp.pedidovenda pv")
			.joinFetch("pvp.pessoaendereco e")
			.joinFetch("pvp.tipooperacao tpo")
			.joinFetch("pvp.produto produto")
			.joinFetch("pv.cliente cliente")
			.leftOuterJoinFetch("produto.produtoprincipal pp")
			.where("ci.carregamento = ?", carregamento);
		
		return query.list();
	}
	
	/**
	 * Busca o pedido de venda do produto do carregamentoItem
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public Pedidovendaproduto findByCarregamentoItem(Carregamentoitem carregamentoitem) {
		if(carregamentoitem == null || carregamentoitem.getCdcarregamentoitem() == null)
			throw new WmsException("O carregamentoItem não deve ser nulo.");
		
		return query()
				.select("pedidovendaproduto.cdpedidovendaproduto,pedidovendaproduto.qtde")
				.join("pedidovendaproduto.listaCarregamentoitem carregamentoItem")
				.where("carregamentoItem = ?",carregamentoitem)
				.unique();
	}

	/**
	 * Busca o pedido de venda do produto do carregamentoItem
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public List<Pedidovendaproduto> findByConfirmacaoItemVO (Ordemservico conferenciaBox) {
		
		if(conferenciaBox == null || conferenciaBox.getCdordemservico() == null)
			throw new WmsException("Parâmetros Inválidos.");
		
		return query()
				.select("pedidovendaproduto.cdpedidovendaproduto,pedidovendaproduto.qtde")
				.join("pedidovendaproduto.listaCarregamentoitem carregamentoItem")
				.join("carregamentoItem.carregamento carregamento")
				.join("carregamento.listaOrdemservico ordemservico")
				.where("ordemservico = ?",conferenciaBox)
				.list();
	}
	
	/**
	 * Busca o pedido de venda do produto do carregamentoItem
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public List<Pedidovendaproduto> findByListCarregamentoItem (List<Carregamentoitem> listaCarregamentoitem) {
		
		if(listaCarregamentoitem == null || listaCarregamentoitem.isEmpty())
			throw new WmsException("O carregamentoItem não deve ser nulo.");
		
		return query()
				.select("pedidovendaproduto.cdpedidovendaproduto")
				.join("pedidovendaproduto.listaCarregamentoitem carregamentoItem")
				.whereIn("carregamentoItem",CollectionsUtil.listAndConcatenate(listaCarregamentoitem, "cdcarregamentoitem", ","))
				.list();
	}
	
	/**
	 * Consulta os PedidoVendaProduto a partir do pedidovenda, retornando também os 
	 * Carregamentoitem quando existirem
	 * 
	 * @author Giovane Freitas
	 * @param pedidovenda
	 */
	public List<Pedidovendaproduto> findByPedidoVenda(Pedidovenda pedidovenda) {
		if(pedidovenda == null || pedidovenda.getCdpedidovenda() == null)
			throw new WmsException("O documento de saída não deve ser nulo.");
		
		return query()
					.select("carregamentoitem.cdcarregamentoitem,carregamentoitem.qtdeconfirmada," +
							"pedidovendaproduto.numeronota," +
							"pedidovendaproduto.cdpedidovendaproduto,pedidovendaproduto.qtde," +
							"produto.cdproduto,produto.codigo,produto.descricao," +
							"carregamento.cdcarregamento,carregamentostatus.cdcarregamentostatus," +
							"carregamentostatus.nome,tipooperacao.cdtipooperacao,tipooperacao.nome")
					.join("pedidovendaproduto.pedidovenda pedidovenda")
					.join("pedidovendaproduto.produto produto")
					.join("pedidovendaproduto.tipooperacao tipooperacao")
					.leftOuterJoin("pedidovendaproduto.listaCarregamentoitem carregamentoitem")
					.leftOuterJoin("carregamentoitem.carregamento carregamento")
					.leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
					.where("pedidovenda = ?",pedidovenda)
					.list();
	}

	/**
	 * Busca os números das notas associadas a um carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @return
	 */
	public String findNumerosNotas(Carregamento carregamento) {
		List<String> list = new QueryBuilder<String>(getHibernateTemplate())
			.select("distinct pvp.numeronota")
			.from(Carregamentoitem.class, "ci")
			.join("ci.pedidovendaproduto pvp")
			.where("ci.carregamento = ?", carregamento)
			.orderBy("ci.pedidovendaproduto.numeronota")
			.setUseTranslator(false)
			.list();
		
		return StringUtils.join(list.iterator(), ", ");
	}
	
	
	public Pedidovendaproduto findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
	//	.select("pedidovendaproduto.cdpedidovendaproduto, pedidovendaproduto.codigoerp ")
		.joinFetch("pedidovendaproduto.pedidovenda pedidovenda")
		.joinFetch("pedidovendaproduto.deposito deposito")
		.joinFetch("pedidovendaproduto.produto produto")
		.joinFetch("pedidovendaproduto.pessoaendereco pessoaendereco")
		.joinFetch("pedidovendaproduto.tipooperacao tipooperacao")
		.joinFetch("pedidovendaproduto.filialEntrega filialEntrega")
		.joinFetch("pedidovendaproduto.depositotransbordo depositotransbordo")
		.joinFetch("pedidovendaproduto.filialentregatransbordo filialentregatransbordo")
		.joinFetch("pedidovendaproduto.filialnota filialnota")
		.leftOuterJoinFetch("pedidovendaproduto.listaCarregamentoitem listaCarregamentoitem")
		.where("pedidovendaproduto.codigoerp = ? ", codigoERP)
		.unique();
	}
	
	public Pedidovendaproduto findByCodigoERPFaturamento(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		//	.select("pedidovendaproduto.cdpedidovendaproduto, pedidovendaproduto.codigoerp ")
		.joinFetch("pedidovendaproduto.pedidovenda pedidovenda")
		.joinFetch("pedidovendaproduto.deposito deposito")
		.joinFetch("pedidovendaproduto.produto produto")
		.joinFetch("pedidovendaproduto.pessoaendereco pessoaendereco")
		.joinFetch("pedidovendaproduto.tipooperacao tipooperacao")
		.joinFetch("pedidovendaproduto.filialEntrega filialEntrega")
		.joinFetch("pedidovendaproduto.depositotransbordo depositotransbordo")
		.joinFetch("pedidovendaproduto.filialentregatransbordo filialentregatransbordo")
		.joinFetch("pedidovendaproduto.filialnota filialnota")
		.leftOuterJoinFetch("pedidovendaproduto.listaCarregamentoitem listaCarregamentoitem")
		.where("pedidovendaproduto.codigoerp = ? ", codigoERP)
		.where("pedidovendaproduto.numeronota is null")
		.where("pedidovendaproduto.dtmarcacao is null")
		.where("pedidovendaproduto.dtexclusaoerp is null")
		.unique();
	}
	

	@SuppressWarnings("unchecked")
	public List<Pedidovendaproduto> pedidoVendaProdutoRotaManual(RotaManualPedidoVendaFiltro filtro){
		
		if(filtro.getDeposito() == null){
			throw new WmsException("Parâmetros inválidos");
		} 
		
		StringBuilder select = new StringBuilder();
		
		select.append("SELECT pvp.cdpedidovendaproduto AS cdpedidovendaproduto, pv.cdpedidovenda AS cdpedidovenda, pv.numero AS numeropedidovenda, pv.codigoerp as pvcodigoerp,p.cdproduto AS cdproduto, ")
		.append("p.codigo AS codigo, p.descricao AS descricao, pe.cdpessoaendereco AS cdpessoaendereco, pe.logradouro AS logradouro, pe.uf AS uf, pe.bairro AS bairro, pe.caixapostal AS caixapostal, ")
		.append("pe.numero AS numero, pe.complemento AS complemento, pe.municipio AS municipio, pe.cep AS cep, tio.cdtipooperacao AS cdtipooperacao, tio.nome AS nome ")
		.append("FROM Pedidovendaproduto pvp ")
		.append("JOIN Pedidovenda pv ON pv.cdpedidovenda = pvp.cdpedidovenda ")
		.append("JOIN Produto p ON p.cdproduto = pvp.cdproduto ")
		.append("JOIN Pessoaendereco pe ON pe.cdpessoaendereco = pvp.cdenderecoentrega ")
		.append("JOIN Tipooperacao tio ON tio.cdtipooperacao = pvp.cdtipooperacao ")
		.append("WHERE ")
		.append("pvp.carregado = 0 AND pvp.dtmarcacao is null AND pvp.numeronota is null AND pvp.dtexclusaoerp is null AND pvp.dtmarcacao is null AND rownum<=50 ")
		.append("AND pvp.cdpraca is null AND pvp.cddeposito = ").append(WmsUtil.getDeposito().getCddeposito());
		
		if(filtro.getPedidoVendaNumero() != null && !filtro.getPedidoVendaNumero().isEmpty())
			select.append(" AND pv.numero = ").append(filtro.getPedidoVendaNumero());
		if(filtro.getDatainicial() != null)
			select.append(" AND pvp.dtprevisaoentrega >= to_date('").append(new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDatainicial())).append("', 'DD/MM/YYYY')");
		if(filtro.getDatafim() != null)
			select.append(" AND pvp.dtprevisaoentrega <= to_date('").append(new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDatafim())).append("', 'DD/MM/YYYY')");
		if(filtro.getMunicipio()!=null && filtro.getMunicipio().getCdmunicipio()!=null)
			select.append(" AND upper(tiraacento(pe.municipio)) like '%'||upper(tiraacento('").append( MunicipioService.getInstance().load(filtro.getMunicipio()).getNome() ).append("'))||'%'");
		if(filtro.getUf()!=null && filtro.getUf().getCduf()!=null) 
			select.append(" AND pe.uf.cduf='").append(filtro.getUf().getCduf()).append("'");
		if(filtro.getBairro()!=null && !filtro.getBairro().isEmpty())
			select.append(" AND pe.bairro like '%'||upper(tiraacento('").append(filtro.getBairro()).append("'))||'%'");
		if(filtro.getFilialEmissao()!=null && filtro.getFilialEmissao().getCdpessoa()!=null)
			select.append(" AND pv.cdfilialemissao=").append(filtro.getFilialEmissao().getCdpessoa());
		if(filtro.getTipooperacao()!=null && filtro.getTipooperacao().getCdtipooperacao()!=null)
			select.append(" AND tio.cdtipooperacao=").append(filtro.getTipooperacao().getCdtipooperacao());
		
		select.append(" ORDER BY pe.municipio, pe.bairro");
//		if(filtro.getTroca() != null){
//			if(filtro.getTroca())
//				select.append(" AND pv.troca =true");
//			else select.append(" AND pv.troca =false");
//		}
		return (List<Pedidovendaproduto>) getJdbcTemplate().query(select.toString(), new org.springframework.jdbc.core.RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new Pedidovendaproduto(rs.getInt("cdpedidovendaproduto"), new Pedidovenda(rs.getInt("cdpedidovenda"), rs.getString("numeropedidovenda"), rs.getLong("pvcodigoerp")), new Produto(rs.getInt("cdproduto"), rs.getString("codigo"), rs.getString("descricao")), 
							new Pessoaendereco(rs.getInt("cdpessoaendereco"), rs.getString("logradouro"), rs.getString("uf"), rs.getString("bairro"), rs.getString("caixapostal"), 
									rs.getString("numero"), rs.getString("complemento"), rs.getString("municipio"), rs.getLong("cep")), new Tipooperacao(rs.getInt("cdtipooperacao"), rs.getString("nome")));
				}
		});
		
		/*
		QueryBuilder<Pedidovendaproduto> query = query()
		.select("pedidovendaproduto.cdpedidovendaproduto, pedidovenda.cdpedidovenda, pedidovenda.numero," +
				"produto.cdproduto, produto.codigo, produto.descricao, pessoaendereco.cdpessoaendereco, " +
				"pessoaendereco.logradouro, pessoaendereco.uf, pessoaendereco.bairro, pessoaendereco.caixapostal, " +
				"pessoaendereco.numero, pessoaendereco.complemento, pessoaendereco.municipio, pessoaendereco.cep," +
				"tipooperacao.cdtipooperacao, tipooperacao.nome")
		.join("pedidovendaproduto.pedidovenda pedidovenda")
		.join("pedidovendaproduto.deposito deposito")
		.join("pedidovendaproduto.produto produto")
		.join("pedidovendaproduto.pessoaendereco pessoaendereco")
		.join("pedidovendaproduto.tipooperacao tipooperacao")
		.leftOuterJoin("pedidovendaproduto.praca praca")
		.where("deposito = ? ", filtro.getDeposito())
		.where("tipooperacao = ? ", filtro.getTipooperacao())
		.where("pedidovendaproduto.carregado = ?", Boolean.FALSE)
		.where("pedidovendaproduto.dtmarcacao is null ")
		.where("pedidovendaproduto.numeronota is null ")
		.where("pedidovendaproduto.dtexclusaoerp is null ")
		.where("pedidovendaproduto.dtmarcacao is null ")
		.where("praca.cdpraca is null ")
		.where("pedidovendaproduto.dtprevisaoentrega >= ?", filtro.getDatainicial())
		.where("pedidovendaproduto.dtprevisaoentrega <= ?", filtro.getDatafim())
		.where("pedidovenda.numero = ?", filtro.getPedidoVendaNumero());
		query.ignoreJoin("praca");
		
		return query.list();
		*/
	}
	
	public void updatePedidoPraca(Pedidovendaproduto pedidovendaproduto, Praca praca) {
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto set pedidovendaproduto.praca.id =? " +
				"where pedidovendaproduto.id = "+pedidovendaproduto.getCdpedidovendaproduto()+"",
				new Object[]{praca.getCdpraca()});
	}

	public void updatePrioridade(String listaPedidoVendaPrioritario) {
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto set pedidovendaproduto.prioridade = 1 " +
				" where pedidovendaproduto.id in ( select pvp.id from Pedidovendaproduto pvp where pvp.pedidovenda.numero in (?)) ",
				new Object[]{listaPedidoVendaPrioritario});
	}

	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Pedidovendaproduto> findByIds(String whereIn) {
		return query()
			.leftOuterJoinFetch("pedidovendaproduto.deposito deposito")
			.leftOuterJoinFetch("pedidovendaproduto.pedidovenda pedidovenda")
			.leftOuterJoinFetch("pedidovendaproduto.produto produto")
			.leftOuterJoinFetch("pedidovendaproduto.pessoaendereco pessoaendereco")
			.leftOuterJoinFetch("pedidovendaproduto.tipooperacao tipooperacao")
			.leftOuterJoinFetch("pedidovendaproduto.filialEntrega filialEntrega")
			.leftOuterJoinFetch("pedidovendaproduto.depositotransbordo depositotransbordo")
			.leftOuterJoinFetch("pedidovendaproduto.filialentregatransbordo filialentregatransbordo")
			.leftOuterJoinFetch("pedidovendaproduto.filialnota filialnota")
			.leftOuterJoinFetch("pedidovendaproduto.tipopedido tipopedido")
			.leftOuterJoinFetch("pedidovendaproduto.empresa empresa")
			.leftOuterJoinFetch("pedidovendaproduto.turnodeentrega turnodeentrega")
			.whereIn("pedidovendaproduto.cdpedidovendaproduto", whereIn)
			.list();
	}

	/**
	 * 
	 * @param whereIn
	 */
	public void updateStatusLiberado(String whereIn) {
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto " +
				"set pedidovendaproduto.pedidovendaprodutostatus.id = 1 " +
				"where pedidovendaproduto.id in ("+whereIn+")");		
	}

	/**
	 * 
	 * @param whereIn
	 */
	public void updateStatusTravado(String whereIn) {
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto " +
				"set pedidovendaproduto.pedidovendaprodutostatus.id = 10 " +
				"where pedidovendaproduto.id in ("+whereIn+")");		
	}
	
	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Pedidovendaproduto> findForExclusao(String whereIn){
		
		QueryBuilder<Pedidovendaproduto> query = query();
		
			query.select("pedidovendaproduto.cdpedidovendaproduto, tipooperacao.cdtipooperacao");
			query.join("pedidovendaproduto.tipooperacao tipooperacao");
			query.whereIn("pedidovendaproduto.cdpedidovendaproduto",whereIn);
			
		return query.list();
		
	}

	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Pedidovendaproduto> findByCancelamentoAbastecimento(String whereIn) {
		
		QueryBuilder<Pedidovendaproduto> query = query();
		
			query.select("pedidovendaproduto.cdpedidovendaproduto, pedidovendaproduto.codigoerp");
			query.whereIn("pedidovendaproduto.cdpedidovendaproduto", whereIn);
		
		return query.list();
	}

	/**
	 * 
	 * @param pvp
	 */
	public void excluirPedido(Pedidovendaproduto pvp) {
		
		if(pvp == null || pvp.getCdpedidovendaproduto() == null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto set pedidovendaproduto.dtexclusaoerp = sysdate where pedidovendaproduto.id = ?", 
				new Object[]{pvp.getCdpedidovendaproduto()});
	}

	
	/***
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Pedidovendaproduto> findPedidoTravadoByIds(String whereIn){
		
		if(whereIn==null || whereIn.isEmpty()){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		return query()
				.select("pedidovendaproduto.cdpedidovendaproduto, pedidovendaprodutostatus.cdpedidovendaprodutostatus")
				.join("pedidovendaproduto.pedidovendaprodutostatus pedidovendaprodutostatus")
				.whereIn("pedidovendaproduto.cdpedidovendaproduto", whereIn)
				.list();
	}

	/**
	 * 
	 * @param whereIn
	 * @param status
	 */
	public void updateStatusForPedidos(String whereIn, Pedidovendaprodutostatus status) {
		
		getHibernateTemplate().bulkUpdate("update Pedidovendaproduto pedidovendaproduto " +
				"set pedidovendaproduto.pedidovendaprodutostatus.id = "+status.getCdpedidovendaprodutostatus()+" " +
				"where pedidovendaproduto.id in ("+whereIn+")");		
	}
	
	/**
	 * Lista todos itens de pedidos de um determinado  
	 * 
	 * @param pedidovendaproduto
	 * @author Giovane Freitas
	 */
	public List<Pedidovendaproduto> findByCarregamentoForCancelamento(Carregamento carregamento) {
		
		if(carregamento == null || carregamento.getCdcarregamento() == null) 
			throw new WmsException("O Carregamento não deve ser nulo.");
		
		QueryBuilder<Pedidovendaproduto> query = new QueryBuilder<Pedidovendaproduto>(getHibernateTemplate())
			.from(Pedidovendaproduto.class, "pvp")
			.join("pvp.listaCarregamentoitem ci")
			.where("ci.carregamento = ?", carregamento);
		
		return query.list();
	}
	
}