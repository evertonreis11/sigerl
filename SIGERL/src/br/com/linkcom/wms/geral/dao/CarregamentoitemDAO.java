package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.LockMode;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.util.WmsException;

public class CarregamentoitemDAO extends GenericDAO<Carregamentoitem> {

	@Override
	public void updateListagemQuery(QueryBuilder<Carregamentoitem> query, FiltroListagem _filtro) {
		query.leftOuterJoinFetch("carregamentoitem.carregamento carregamento");
		query.leftOuterJoinFetch("carregamentoitem.produtovendaproduto produtovendaproduto");
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Carregamentoitem> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	
	/**
	 * Busca um item do carregamento através do PedidoVenda
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param pedidovendaproduto
	 * @param carregamento
	 * @return
	 */
	public Carregamentoitem findByPedidoVendaProduto(Pedidovendaproduto pedidovendaproduto,Carregamento carregamento){
		if(pedidovendaproduto == null || pedidovendaproduto.getCdpedidovendaproduto() == null){
			throw new WmsException(Util.locale.getBundleKey("pedidovenda.naoNulo"));
		}
		return query()
					.select("carregamentoitem.cdcarregamentoitem")
					.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
				 	.where("carregamentoitem.carregamento.id = ?",carregamento.getCdcarregamento())
					.where("pedidovendaproduto = ?",pedidovendaproduto)
					.unique();
	}
	
	/* singleton */
	private static CarregamentoitemDAO instance;
	public static CarregamentoitemDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(CarregamentoitemDAO.class);
		}
		return instance;
	}
	
	/**
	 * Encontra todos os itens do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @return
	 */
	public List<Carregamentoitem> findByCarregamento(Carregamento carregamento) {
		
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo.");
		
		return query()
					.select("carregamentoitem.cdcarregamentoitem,carregamentoitem.ordem," +
							"pedidovendaproduto.cdpedidovendaproduto")
					.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
					.join("carregamentoitem.carregamento carregamento")
					.where("carregamento = ?",carregamento)
					.list()
					;
	}

	/**
	 * Retorna a lista de carregamento item com a linha de separacão
	 * @param carregamento
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Carregamentoitem>  getLinhaSeparacao(Carregamento carregamento) {
			
			if(carregamento == null || carregamento.getCdcarregamento() == null)
				throw new WmsException("O carregamento não deve ser nulo.");
			
			return query()
						.select("carregamentoitem.cdcarregamentoitem," +
								"pedidovendaproduto.cdpedidovendaproduto, produto.cdproduto, " +
								"dadologistico.cddadologistico, linhaseparacao.cdlinhaseparacao")
						.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
						.join("carregamentoitem.carregamento carregamento")
						.join("pedidovendaproduto.produto produto")
						.leftOuterJoin("produto.listaDadoLogistico dadologistico")
						.leftOuterJoin("dadologistico.linhaseparacao linhaseparacao")
						.where("pedidovendaproduto.deposito= dadologistico.deposito")
						.where("carregamento = ?",carregamento)
						.list()
						;
			
			
		}
	
	/**
	 * Atualiza os dados do carregamentoItem
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoitem
	 */
	public int updateCarregamentoItem(Carregamentoitem carregamentoitem) {
		if(carregamentoitem == null || carregamentoitem.getCdcarregamentoitem() == null)
			throw new WmsException("O carregamentoItem não deve ser nulo.");
		
		//fazendo um "select for update" via hibernate, para ficar dentro da mesma transação.
		getHibernateTemplate().get(Carregamentoitem.class, carregamentoitem.getCdcarregamentoitem(), LockMode.UPGRADE);;
		
		return getHibernateTemplate().bulkUpdate("update Carregamentoitem ci set ci.ordem = ? where ci = ? ",
			    new Object[]{carregamentoitem.getOrdem(),carregamentoitem});

	}
	
	/**
	 * Insere um carregamento item através de um select
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoitem
	 * @param carregamento
	 * @return
	 */
	public int insertCarregamentoItem(Carregamentoitem carregamentoitem,Carregamento carregamento){
		String sql = "insert into Carregamentoitem (cdcarregamentoitem,cdcarregamento,ordem,cdpedidovendaproduto) " +
				"SELECT sq_carregamentoitem.nextval,"+carregamento.getCdcarregamento()+","+
				carregamentoitem.getOrdem()+",pp.cdpedidovendaproduto from Pedidovendaproduto pp " +
				"where pp.dtmarcacao is null and pp.dtexclusaoerp is null and pp.numeronota is null and " +
				"pp.carregado = 0 and pp.cdpedidovendaproduto = "+carregamentoitem.getPedidovendaproduto().getCdpedidovendaproduto();
		return getJdbcTemplate().update(sql);
	}

	/**
	 * Obtém a lista de carregamentoitem para realizar o corte.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Carregamentoitem> findForConfirmacaoCorte(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null || 
				ordemservico.getCarregamento() == null || 
				ordemservico.getCarregamento().getCdcarregamento() == null){
			
			throw new WmsException("A ordem de serviço e o carregamento não devem ser nulos.");
		}
		
		return query().select("carregamentoitem.cdcarregamentoitem,carregamento.cdcarregamento," +
							"carregamentoitem.qtdeconfirmada,ordemservicoproduto.cdordemservicoproduto," +
							"etiquetaexpedicao.cdetiquetaexpedicao,pedidovendaproduto.cdpedidovendaproduto," +
							"pedidovendaproduto.qtde,ordemservicoproduto.qtdeesperada," +
							"pedidovenda.cdpedidovenda,produto.cdproduto")
					.join("carregamentoitem.carregamento carregamento")
					.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
					.join("pedidovendaproduto.pedidovenda pedidovenda")
					.join("pedidovendaproduto.produto produto")
					.join("carregamentoitem.listaEtiquetaexpedicao etiquetaexpedicao")
					.join("etiquetaexpedicao.ordemservicoproduto ordemservicoproduto")
					.join("ordemservicoproduto.listaOrdemprodutoLigacao ordemprodutoLigacao")
					.join("ordemprodutoLigacao.ordemservico ordemservico")
					.where("ordemservico = ? ",ordemservico)
					.orderBy("etiquetaexpedicao.qtdecoletor")
					.list();
 	}
	
	/**
	 * 
	 * Atualiza o campo quantidade confirmada do carregamentoitem.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param carregamentoitem
	 * 
	 */
	public void updateQtdeconfirmada(Carregamentoitem carregamentoitem) {
		if((carregamentoitem == null) || (carregamentoitem.getCdcarregamentoitem() == null) || (carregamentoitem.getQtdeconfirmada() == null)) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Carregamentoitem ci set ci.qtdeconfirmada=? where ci.id=? ", 
				new Object[]{carregamentoitem.getQtdeconfirmada(), carregamentoitem.getCdcarregamentoitem()});
	}

	/**
	 * Carrega o CarregamentoItem e o PedidoVendaProduto associado.
	 * 
	 * @param carregamentoitem
	 * @return
	 */
	public Carregamentoitem loadForSeparacao(Carregamentoitem carregamentoitem) {
		if(carregamentoitem == null || carregamentoitem.getCdcarregamentoitem() == null){
			throw new WmsException("O item de carregamento não deve ser nulo.");
		}
		return query()
					.select("carregamentoitem.cdcarregamentoitem,carregamentoitem.ordem," +
							"carregamentoitem.qtdeconfirmada," +
							"pedidovendaproduto.cdpedidovendaproduto,pedidovendaproduto.qtde")
					.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
					.where("carregamentoitem = ?",carregamentoitem)
					.unique();
	}

	/**
	 * Verifica se o item do carregamento foi faturado em outra filial
	 * 
	 * @param carregamentoitem
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean isCarregamentoItemFaturadoOutraFilial(Carregamentoitem carregamentoitem) {
		if(carregamentoitem == null || carregamentoitem.getCdcarregamentoitem() == null){
			throw new WmsException("O item de carregamento não deve ser nulo.");
		}
		
		return newQueryBuilderWithFrom(Long.class)
			.select("count(*)")
			.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
			.join("pedidovendaproduto.filialnota filialnota")
			.join("pedidovendaproduto.deposito deposito")
			.where("filialnota.documento <> deposito.cnpj")
			.where("carregamentoitem = ?", carregamentoitem)
			.setUseTranslator(false)
			.unique()
			.longValue()>0;
	}

	/**
	 * Localiza o {@link Carregamentoitem} associado a uma {@link Ordemservicoproduto}
	 * 
	 * @author Giovane Freitas
	 * @param ordemservicoproduto
	 * @return
	 */
	public Carregamentoitem findByOrdemservicoproduto(Ordemservicoproduto ordemservicoproduto) {
		return query()
			.join("carregamentoitem.listaEtiquetaexpedicao etiqueta")
			.where("etiqueta.ordemservicoproduto = ?", ordemservicoproduto)
			.setMaxResults(1)
			.unique();
	}
	
	/**
	 * Atualiza a quantidade confirmada para 0 para todos o itens que estão null
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	public void atualizaCorte(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null){
			throw new WmsException("O carregamento não deve ser nulo.");
		}
		
		String hql = "update Carregamentoitem ci set ci.qtdeconfirmada = 0 " +
				"where ci.qtdeconfirmada is null and ci.carregamento.id = ? ";
		getHibernateTemplate().bulkUpdate(hql, new Integer[]{carregamento.getCdcarregamento()});
	}
	
	
	public Carregamentoitem findByPedidoVendaProduto(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
					.select("distinct carregamentoitem.cdcarregamento")
					.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
				 	.where("pedidovendaproduto.codigoerp = ?", codigoERP)
					.unique();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Carregamentoitem> loadCarregamentoItens(Carregamento carregamento) {
		if (carregamento == null || carregamento.getCdcarregamento() ==  null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ci.cdcarregamentoitem, pp.codigoerp, nvl(ci.qtdeconfirmada, pp.qtde) as qtdeconfirmada, ");
		sql.append("pesounitario_produto(pp.cdproduto) as peso, cli.codigoerp as codigofilialretirada, ");
		sql.append("c.cdtipooperacaoretirada, pp.cdtipooperacao, ");
		sql.append("(select count(*) from produto p where p.cdprodutoprincipal = pp.cdproduto) as qtdevolumes ");
		sql.append("from carregamentoitem ci ");
		sql.append("join carregamento c on c.cdcarregamento = ci.cdcarregamento ");
		sql.append("join pedidovendaproduto pp on (ci.cdpedidovendaproduto = pp.cdpedidovendaproduto) ");
		sql.append("left join cliente cli on cli.cdpessoa = c.cdfilialretirada ");
		sql.append("where c.dtfimcarregamento is not null ");
		sql.append("and ci.dtsincronizacao is null ");
		sql.append("and ci.qtdeconfirmada > 0 ");
		sql.append("and ci.cdcarregamento = ? ");
		
		return (List<Carregamentoitem>) getJdbcTemplate().query(sql.toString(), new Object[]{carregamento.getCdcarregamento()},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Carregamentoitem> list = new ArrayList<Carregamentoitem>();
				
				while (rs.next()){
					Carregamentoitem carregamentoitem = new Carregamentoitem();
					carregamentoitem.setCdcarregamentoitem(rs.getInt("ci.cdcarregamentoitem"));
					carregamentoitem.getPedidovendaproduto().setCodigoerp(rs.getLong("pp.codigoerp"));
					carregamentoitem.setQtdeconfirmada(rs.getLong("qtdeconfirmada"));
					carregamentoitem.getPedidovendaproduto().getProduto().setPesounitario(rs.getDouble("peso"));
					carregamentoitem.getCarregamento().getFilialretirada().setCodigoerp(rs.getLong("codigofilialretirada"));
					carregamentoitem.getCarregamento().getTipooperacaoretirada().setCdtipooperacao(rs.getInt("c.cdtipooperacaoretirada"));
					carregamentoitem.getPedidovendaproduto().getTipooperacao().setCdtipooperacao(rs.getInt("pp.cdtipooperacao"));
					carregamentoitem.getPedidovendaproduto().getProduto().setQtdevolumes(rs.getInt("qtdevolumes"));
					list.add(carregamentoitem);
				}
				return list;
			}
		}
		);
	}
	
	@SuppressWarnings("unchecked")
	public List<Carregamentoitem> loadCarregamentoItens() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ci.cdcarregamentoitem, pp.codigoerp, nvl(ci.qtdeconfirmada, pp.qtde) as qtdeconfirmada, ");
		sql.append("pesounitario_produto(pp.cdproduto) as peso, cli.codigoerp as codigofilialretirada, ");
		sql.append("c.cdtipooperacaoretirada, pp.cdtipooperacao, ");
		sql.append("(select count(*) from produto p where p.cdprodutoprincipal = pp.cdproduto) as qtdevolumes ");
		sql.append("from carregamentoitem ci ");
		sql.append("join carregamento c on c.cdcarregamento = ci.cdcarregamento ");
		sql.append("join pedidovendaproduto pp on (ci.cdpedidovendaproduto = pp.cdpedidovendaproduto) ");
		sql.append("left join cliente cli on cli.cdpessoa = c.cdfilialretirada ");
		sql.append("where c.dtfimcarregamento is not null ");
		sql.append("and ci.dtsincronizacao is null ");
		sql.append("and ci.qtdeconfirmada > 0 ");
		
		return (List<Carregamentoitem>) getJdbcTemplate().query(sql.toString(), new Object[]{},
				new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Carregamentoitem> list = new ArrayList<Carregamentoitem>();
				
				while (rs.next()){
					Carregamentoitem carregamentoitem = new Carregamentoitem();
					Pedidovendaproduto pedidovendaproduto = new Pedidovendaproduto();
					Produto produto = new Produto();
					Carregamento carregamento = new Carregamento();
					Cliente filialretirada = new Cliente();
					Tipooperacao tipooperacaoretirada = new Tipooperacao();
					Tipooperacao tipooperacao = new Tipooperacao();
					
					carregamentoitem.setCdcarregamentoitem(rs.getInt("cdcarregamentoitem"));
					pedidovendaproduto.setCodigoerp(rs.getLong("codigoerp"));
					carregamentoitem.setQtdeconfirmada(rs.getLong("qtdeconfirmada"));
					produto.setPesounitario(rs.getDouble("peso"));
					filialretirada.setCodigoerp(rs.getLong("codigofilialretirada"));
					tipooperacaoretirada.setCdtipooperacao(rs.getInt("cdtipooperacaoretirada"));
					tipooperacao.setCdtipooperacao(rs.getInt("cdtipooperacao"));
					produto.setQtdevolumes(rs.getInt("qtdevolumes"));
					
					carregamento.setFilialretirada(filialretirada);
					carregamento.setTipooperacaoretirada(tipooperacaoretirada);
					pedidovendaproduto.setProduto(produto);
					pedidovendaproduto.setTipooperacao(tipooperacao);
					carregamentoitem.setCarregamento(carregamento);
					carregamentoitem.setPedidovendaproduto(pedidovendaproduto);
					
					list.add(carregamentoitem);
				}
				return list;
			}
		}
		);
	}


	public void updateDtSincronizacao(Carregamentoitem carregamentoitem) {
		if(carregamentoitem == null || carregamentoitem.getCdcarregamentoitem() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update carregamentoitem set dtsincronizacao=SYSDATE where cdcarregamentoitem=?", 
				new Object[]{carregamentoitem.getQtdeconfirmada(), carregamentoitem.getCdcarregamentoitem()});
	}


	public void deleteByCarregamento(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Carregamentoitem ci where ci.carregamento.id = ?", 
				new Object[]{carregamento.getCdcarregamento()});
	}
	
	public void deleteByWhereIn(String whereIn) {
		if(whereIn == null || whereIn.isEmpty()) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("delete from Carregamentoitem ci where ci.carregamento.id = ?",new Object[]{whereIn});
	}
}