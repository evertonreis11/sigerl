package br.com.linkcom.wms.geral.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendaparcial;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocompraproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidocompraprodutoDAO extends GenericDAO<Pedidocompraproduto> {
	
	/**
	 * Método que busca uma lista de pedidocompraproduto através de um pedidocompra
	 * @author Leonardo Guimarães
	 * @param pedidocompra
	 * @return
	 */
	public List<Pedidocompraproduto> findByPedidoCompra(Pedidocompra pedidocompra){
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null){
			throw new WmsException("O pedido compra não deve ser nulo");
		}
		return query()
					.select("pedidocompraproduto.cdpedidocompraproduto, pedidocompra.cdpedidocompra," +
		  					"produto.cdproduto, produto.descricao, produto.codigo, pedidocompraproduto.qtde, " +
		  					"produtoclasse.cdprodutoclasse,produtoclasse.numero,produtoclassepai.numero," +
		  					"produtoclasse.nome, listaPedidocompraprodutolibera.cdpedidocompraprodutolibera," +
		  					"listaPedidocompraprodutolibera.dtprevisaorecebimento, listaPedidocompraprodutolibera.qtdeliberada")
		  			.join("pedidocompraproduto.produto produto")
		  			.join("pedidocompraproduto.pedidocompra pedidocompra")
		  			.join("produto.produtoclasse produtoclasse")
		  			.join("produtoclasse.v_produtoclasse v_produtoclasse")
		  			.leftOuterJoin("v_produtoclasse.produtoclasse produtoclassepai")
		  			.leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
		  			.where("pedidocompra =?",pedidocompra)
		  			.orderBy("produto.descricao")
		  			.list();
		 
	}
	
	
	/**
	 * Método que busca uma lista de pedidocompraproduto através de um CDPEDIDOCOMPRAPRODUTO
	 * @author Giovane Freitas
	 * @return
	 */
	public Pedidocompraproduto findForEdicaoAgenda(Integer cdpedidocompraproduto){
		if(cdpedidocompraproduto == null ){
			throw new WmsException("Parâmetros inválidos.");
		}
		return query()
					.select("pedidocompraproduto.cdpedidocompraproduto, pedidocompra.cdpedidocompra," +
		  					"produto.cdproduto, produto.descricao, produto.codigo, pedidocompraproduto.qtde, " +
		  					"produtoclasse.cdprodutoclasse, produtoclasse.numero, produtoclasse.nome," +
		  					"listaPedidocompraprodutolibera.cdpedidocompraprodutolibera," +
		  					"listaPedidocompraprodutolibera.dtprevisaorecebimento, listaPedidocompraprodutolibera.qtdeliberada")
		  			.join("pedidocompraproduto.produto produto")
		  			.join("produto.produtoclasse produtoclasse")
		  			.join("pedidocompraproduto.pedidocompra pedidocompra")
		  			.leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
		  			.where("pedidocompraproduto.id = ? ", cdpedidocompraproduto)
		  			.orderBy("produto.descricao")
		  			.unique();
	}
	
	/**
	 * Método que busca uma lista de pedidocompraproduto através de uma lista de agendaparcial
	 * @author Leonardo Guimarães
	 * @param listaagendaparcial
	 * @return
	 */
	public Pedidocompraproduto findByAgendaParcial(Agendaparcial agendaparcial){
		if(agendaparcial == null || agendaparcial.getCdagendaparcial() == null){
			throw new WmsException("A agendaparcial não dever ser nula.");
		}
		return query()
					.select("pedidocompraproduto.cdpedidocompraproduto,pedidocompra.cdpedidocompra," +
							 "produto.cdproduto,pedidocompraproduto.qtde,produto.codigoerp," +
							 "produto.codigo,produto.descricao, listaPedidocompraprodutolibera.cdpedidocompraprodutolibera," +
		  					"listaPedidocompraprodutolibera.dtprevisaorecebimento, listaPedidocompraprodutolibera.qtdeliberada")
					 .join("pedidocompraproduto.produto produto")
					 .join("pedidocompraproduto.pedidocompra pedidocompra")
					 .leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
					 .where("pedidocompraproduto=?",agendaparcial.getPedidocompraproduto())
					 .unique();
				
	}
	
	/**
	 * Encontra um pedidoCompraProduto no banco
	 * através do pedidoCompraProduto passado como parâmetro
	 * @author Leonardo Guimarães
	 * @param pedidocompraproduto
	 * @return
	 */
	public Pedidocompraproduto findByCd(Pedidocompraproduto pedidocompraproduto) {
		if(pedidocompraproduto == null || pedidocompraproduto.getCdpedidocompraproduto() == null){
			throw new WmsException("O pedidocompraproduto não deve ser nulo");
		}
		return query()
					.select("pedidocompraproduto.cdpedidocompraproduto, pedidocompraproduto.qtde, produto.cdproduto," +
							"pedidocompra.cdpedidocompra,pedidocompra.codigoerp,fornecedor.cdpessoa, fornecedor.nome," +
							"produto.codigoerp,produto.descricao,pedidocompra.dtemissao,pedidocompra.dtcancelamento," +
							"produtoclasse.cdprodutoclasse,produtoclasse.numero, produtoclasse.nome," +
							"listaPedidocompraprodutolibera.cdpedidocompraprodutolibera," +
		  					"listaPedidocompraprodutolibera.dtprevisaorecebimento, listaPedidocompraprodutolibera.qtdeliberada, " +
		  					"produtoclasse2.numero")
					.leftOuterJoin("pedidocompraproduto.pedidocompra pedidocompra")
					.join("pedidocompra.fornecedor fornecedor")
					.leftOuterJoin("pedidocompraproduto.produto produto")
					.leftOuterJoin("produto.produtoclasse produtoclasse")
					.leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
					.leftOuterJoin("produtoclasse.v_produtoclasse v_produtoclasse")
					.leftOuterJoin("v_produtoclasse.produtoclasse produtoclasse2")
					.where("pedidocompraproduto.cdpedidocompraproduto=?",pedidocompraproduto.getCdpedidocompraproduto())
					.unique();
	}

	/**
	 * Retorna o número de itens que um pedido possui.
	 * 
	 * @author Giovane Freitas
	 * @param pedidocompra
	 * @return
	 */
	public long getCountItens(Pedidocompra pedidocompra) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null){
			throw new WmsException("O pedidocompra não deve ser nulo");
		}
		Long count = newQueryBuilderWithFrom(Long.class)
					.select("count(*)")
					.join("pedidocompraproduto.pedidocompra pedidocompra")
					.where("pedidocompra = ?", pedidocompra)
					.setUseTranslator(false)
					.unique();
		
		if (count == null)
			return 0L;
		else 
			return count;
	}
	
	/**
	 * Retorna os itens do pedido de compra que não estão incluídos no agendamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param pedidocompra
	 * @param agenda
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Pedidocompraproduto> findNotIncluded(Pedidocompra pedidocompra,Agenda agenda) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null || agenda == null || agenda.getCdagenda() == null)
			throw new WmsException("O pedidocompra e/ou agenda não devem ser nulas");
		
		String query = 	"select pedidocompraproduto.cdpedidocompraproduto," +
						"pedidocompraproduto.qtde,produto.cdproduto," +
						"produto.codigo,produto.descricao " +
						"from pedidocompraproduto " +
						"join produto on (pedidocompraproduto.cdproduto = produto.cdproduto)" +
						"where pedidocompraproduto.qtde > coalesce(" +
						"(select sum(ag.qtde) from agendaparcial ag" +
						"	join agenda on (ag.cdagenda = agenda.cdagenda)" +
						"	where ag.cdagenda <> " + agenda.getCdagenda() +
						"	and agenda.cdagendastatus = 1" +
						"	and ag.cdpedidocompraproduto = pedidocompraproduto.cdpedidocompraproduto),0" +
						") " +
						"and not exists(" +
						"	select distinct ag.cdpedidocompraproduto from Agendaparcial ag " +
						"	where ag.cdagenda = " + agenda.getCdagenda() + 
						"	and ag.cdpedidocompraproduto = pedidocompraproduto.cdpedidocompraproduto"+
						") " +
						"and pedidocompraproduto.cdpedidocompra = " + pedidocompra.getCdpedidocompra()
						;
		return getJdbcTemplate().query(query, new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Pedidocompraproduto pedidocompraproduto = new Pedidocompraproduto();
				pedidocompraproduto.setCdpedidocompraproduto(rs.getInt("cdpedidocompraproduto"));
				pedidocompraproduto.setQtde(rs.getInt("qtde"));
				
				Produto produto = new Produto();
				produto.setCdproduto(rs.getInt("cdproduto"));
				produto.setCodigo(rs.getString("codigo"));
				produto.setDescricao(rs.getString("descricao"));
				
				pedidocompraproduto.setProduto(produto);
				
				return pedidocompraproduto;
			}
		});
	}

	/**
	 * Verifica se a quantidade total de um pedido de compra já foi liberada para agendamento.
	 * 
	 * @author Giovane Freitas
	 * @param pedidocompra
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isLiberadoIntegral(Pedidocompra pedidocompra){
		StringBuilder sql = new StringBuilder();
		
		sql.append("select (select sum(pcp.qtde) from pedidocompraproduto pcp where pcp.cdpedidocompra = pedidocompraproduto.cdpedidocompraproduto) - " +
				" (select coalesce (sum(pcpl.qtdeliberada),0) from Pedidocompraprodutolibera pcpl " +
				"join pedidocompraproduto pcp on pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto where pcp.cdpedidocompra = pedidocompraproduto.cdpedidocompraproduto) ");
		sql.append("from pedidocompraproduto ");
		sql.append("where cdpedidocompra = "+ pedidocompra.getCdpedidocompra());
		
//		return getJdbcTemplate().queryForLong(sql.toString(), new Integer[]{pedidocompra.getCdpedidocompra()}) <= 0;
		List<Long> lista = getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				return rs.getLong(1);
			}
		});
		
		Long total = 0l;
		for (Long valor : lista) 
			if(valor != null)
				total += valor;
		
		return total <= 0l;
	}
	
	/**
	 * Busca o valor montante já liberado para uma determinada classe de produto em um determinado mês.
	 * 
	 * @author Giovane Freitas
	 * @param numeroClassePrincipal
	 * @param dtprevisaorecebimento
	 * @return
	 */
	public Money getValorLiberado(Deposito deposito, String numeroClassePrincipal, Date mesReferencia) {
		if (mesReferencia == null || numeroClassePrincipal == null || numeroClassePrincipal.isEmpty())
			throw new WmsException("Parâmetros inválidos.");
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Sum(pcp.valor * Nvl(pcpl.qtdeliberada, 0)) AS valor ");
		sql.append("FROM pedidocompra pc ");
		sql.append("  join pedidocompraproduto pcp ON pc.cdpedidocompra = pcp.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join pedidocompraprodutolibera pcpl ON pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto ");
		sql.append("WHERE Trunc(pcpl.dtprevisaorecebimento, 'MM') = Trunc(?, 'MM') ");
		sql.append("  AND cla.numero = ? ");
		sql.append("  AND pc.cdpedidocomprastatus <> 4 ");
	
		
		List<Object> args = new ArrayList<Object>();
		args.add(mesReferencia);
		args.add(numeroClassePrincipal);
		
		if(deposito != null && deposito.getCddeposito() != null){
			sql.append("  AND pc.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}

		
		
		long valor = getJdbcTemplate().queryForLong(sql.toString(), args.toArray());
		
		return new Money(valor, true);
	}
	
	public Money getValorLiberadoFinanceiro(Deposito deposito, String numeroClassePrincipal, Date mesReferencia, Date dtprevisaoFinaceiro) {
		if (mesReferencia == null || numeroClassePrincipal == null || numeroClassePrincipal.isEmpty())
			throw new WmsException("Parâmetros inválidos.");
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Sum(pcp.valor * Nvl(pcpl.qtdeliberada, 0)) AS valor ");
		sql.append("FROM pedidocompra pc ");
		sql.append("  join pedidocompraproduto pcp ON pc.cdpedidocompra = pcp.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join pedidocompraprodutolibera pcpl ON pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto ");
		sql.append("WHERE Trunc(pcpl.dtprevisaorecebimento, 'MM') = Trunc(?, 'MM') ");
		sql.append(" AND Trunc(pcpl.dtprevisaofinanceiro, 'MM') = Trunc(?, 'MM') ");
		sql.append("  AND cla.numero = ? ");
		sql.append("  AND pc.cdpedidocomprastatus <> 4 ");
		
		List<Object> args = new ArrayList<Object>();
		args.add(mesReferencia);
		args.add(dtprevisaoFinaceiro);
		args.add(numeroClassePrincipal);

		if(deposito != null && deposito.getCddeposito() != null){
			sql.append("  AND pc.cddeposito = ? ");
			args.add(deposito.getCddeposito());
			
		}
		
		
		
		long valor = getJdbcTemplate().queryForLong(sql.toString(), args.toArray());
		
		return new Money(valor, true);
	}
	
	public Pedidocompraproduto findByPedidocompraProduto(Pedidocompra pedidocompra, Produto produto) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null || produto == null || produto.getCdproduto() == null){
			throw new WmsException("Pedido de compra ou Produto não pode ser nulo.");
		}
		return query()
					.select("pedidocompraproduto.cdpedidocompraproduto")
					.where("pedidocompraproduto.pedidocompra = ?", pedidocompra)
					.where("pedidocompraproduto.produto = ?", produto)
					.unique();
	}
	
	
	/**
	 * Faz um load em Pedidocompraproduto
	 * @param pedidocompraproduto
	 * @return
	 * @author Taidson
	 */
	public Pedidocompraproduto loadPedidoCompraproduto(Pedidocompraproduto pedidocompraproduto){
		if(pedidocompraproduto == null || pedidocompraproduto.getCdpedidocompraproduto() == null){
			throw new WmsException("O pedidocompraproduto não deve ser nulo");
		}
		return query()
					.select("pedidocompraproduto.cdpedidocompraproduto, pedidocompraproduto.qtde, " +
							"pedidocompraproduto.valor, produto.cdproduto, pedidocompra.cdpedidocompra")
		  			.join("pedidocompraproduto.produto produto")
		  			.join("pedidocompraproduto.pedidocompra pedidocompra")
		  			.where("pedidocompraproduto =?",pedidocompraproduto)
		  			.unique();
		 
	}
	
	/**
	 * Método criado exclusivamente para salvar o objeto em edição de pedido liberado.
	 * @param pedidocompraproduto
	 * @author Taidson
	 */
	public void saveOrUpdateEditaPedidocompraLiberado(Pedidocompraproduto pedidocompraproduto) {
		SaveOrUpdateStrategy save = save(pedidocompraproduto);
		save.saveOrUpdateManaged("listaPedidocompraprodutolibera", "pedidocompraproduto");
		save.execute();
		getHibernateTemplate().flush();
		}
	
	
	public Boolean verificaProdutoAgenda(Produto produto, Agenda agenda, Pedidocompra pedidocompra) {
		if(produto == null || agenda == null || pedidocompra == null || produto.getCdproduto() == null || 
				agenda.getCdagenda() == null || pedidocompra.getCdpedidocompra() == null)
			throw new WmsException("Parametros inválidos.");
		StringBuilder sql = new StringBuilder();
		
		sql
			.append("select count(*) ")
			.append("from pedidocompraproduto pcp ")
			.append("join agendapedido ap on(pc.cdpedidocompra = pcp.cdpedidocompra) ")
			.append("where pcp.cdproduto = "+ produto.getCdproduto() +" and ")
			.append("ap.cdagenda = "+ agenda.getCdagenda() +" and ")
			.append("ap.cdpedidocompra = "+ pedidocompra.getCdpedidocompra());
			
			
		Long qtde = getJdbcTemplate().queryForLong(sql.toString());
		return qtde > 0;
	}
	
	public Boolean verificaPedidoNotaAgendamento(Agenda agenda, Pedidocompra pedidocompra) {
		if(agenda == null || agenda.getCdagenda() == null || 
				 pedidocompra == null || pedidocompra.getCdpedidocompra() == null)
			throw new WmsException("Parametros inválidos.");
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("select count(*) ")
		.append("from agendapedido ap ")
		.append("where ap.cdagenda = "+ agenda.getCdagenda() +" and ")
		.append("ap.cdpedidocompra = "+ pedidocompra.getCdpedidocompra());
		
		Long qtde = getJdbcTemplate().queryForLong(sql.toString());
		return qtde > 0;
	}
	
	public List<Pedidocompraproduto> findPedidoProdutos(Agenda agenda){
		if (agenda == null || agenda.getCdagenda() == null)
			throw new WmsException("Parâmetro inválido. A agenda não pode ser nula.");

		return query()
		.select("pedidocompraproduto.cdpedidocompraproduto, produto.cdproduto, produto.codigo")
		.join("pedidocompraproduto.pedidocompra pedidocompra")
		.join("pedidocompraproduto.produto produto")
		.leftOuterJoin("pedidocompra.listaagendapedido listaagendapedido")
		.where("listaagendapedido.agenda = ?", agenda)
		.list();
	}
	
	public long getQtdeTotal(Pedidocompra pedidocompra, String whereIn) {
		if (pedidocompra == null || pedidocompra.getCdpedidocompra() == null)
			throw new WmsException("Parâmetro inválido.");
		
		if(pedidocompra.getCdpedidocompra().equals(83))
			System.out.println("teste");

		StringBuilder sql = new StringBuilder();
		sql.append("select sum(pcp.qtde) ");
		sql.append("from pedidocompraproduto pcp ");
		sql.append("where pcp.cdpedidocompra = ? ");
		sql.append("and pcp.cdproduto in("+whereIn+") ");
		sql.append("and pcp.cdpedidocompraproduto in(select pcpl.cdpedidocompraproduto from pedidocompraprodutolibera pcpl where pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto) ");
		
		Object[] args = new Object[]{pedidocompra.getCdpedidocompra()};
	
		return getJdbcTemplate().queryForLong(sql.toString(), args);
	}
	
	public long getQtdeLiberada(Pedidocompra pedidocompra, String whereIn) {
		if (pedidocompra == null || pedidocompra.getCdpedidocompra() == null)
			throw new WmsException("Parâmetro inválido.");

		StringBuilder sql = new StringBuilder();
		sql.append("select sum(pcpl.qtdeliberada) ");
		sql.append("from pedidocompraproduto pcp ");
		sql.append("join pedidocompraprodutolibera pcpl ");
		sql.append("on(pcp.cdpedidocompraproduto = pcpl.cdpedidocompraproduto) ");
		sql.append("where pcp.cdpedidocompra = ? ");
		sql.append("and pcp.cdproduto in("+whereIn+") ");
		
		Object[] args = new Object[]{pedidocompra.getCdpedidocompra()};
		 
		return getJdbcTemplate().queryForLong(sql.toString(), args);
	}
}
