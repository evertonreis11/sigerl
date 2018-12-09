package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Pedidocompra;
import br.com.linkcom.wms.geral.bean.Pedidocomprastatus;
import br.com.linkcom.wms.geral.bean.vo.PedidoCompraResumoVO;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.LiberarPedidoCompraFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.PedidocompraFiltro;
import br.com.linkcom.wms.modulo.sistema.controller.process.filtro.AgendamentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("pedidocompra.codigoerp")
public class PedidocompraDAO extends GenericDAO<Pedidocompra> {
	
	
	/**
	 * Método usuado para fazer a filtragem em agendamentoprocess
	 * @author Leonardo Guimarães
	 * @param agendamentoFiltro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Pedidocompra> filtrar(AgendamentoFiltro filtro){
		if(filtro == null || filtro.getDeposito() == null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		StringBuilder select = new StringBuilder();
		StringBuilder where = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		//where básico que sempre terá
		where.append("	where pc.cddeposito = ? \n"); 
		args.add(filtro.getDeposito().getCddeposito());
		
		select.append("select * from ( ");
		select.append("	select pc.cdpedidocompra, pc.codigoerp, pc.numero, f.cdpessoa, f1.nome, pc.dtemissao, \n");
		select.append("	pc.dtcancelamento, (select sum(pcpl.qtdeliberada) from pedidocompraproduto pcp" +
				" left join pedidocompraprodutolibera pcpl on pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto" +
				" where pcp.cdpedidocompra = pc.cdpedidocompra) as qtdeTotal, \n");
		
		select.append("		BUSCAR_QTDEAGENDA(pc.cdpedidocompra) as qtdeAgenda \n");
		select.append("	from Pedidocompra pc \n"); 
		select.append("		join Fornecedor f on pc.cdpessoa=f.cdpessoa \n"); 
		select.append("		join Pessoa f1 on f.cdpessoa=f1.cdpessoa \n"); 
		select.append("		join Pedidocompraproduto pcp on pc.cdpedidocompra=pcp.cdpedidocompra \n"); 
		
		where.append(" and pc.cdpedidocomprastatus in (2,3,5) ");

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()){
			select.append("		join Produto p on pcp.cdproduto=p.cdproduto \n"); 
			where.append("  and p.codigo = ? ");
			args.add(filtro.getCodigoProduto());
		}
		
		
		if (filtro.getParcial() != null || filtro.getCdagendamento() != null || filtro.getCdagenda() != null){
			select.append("		left outer join Agendapedido ap on pc.cdpedidocompra=ap.cdpedidocompra \n");
		}

		if (filtro.getParcial() != null){
			where.append("   and ap.parcial = ? ");
			args.add(filtro.getParcial() ? 1 : 0);
		}

		if (filtro.getCdagendamento() != null){
			where.append("   and ap.cdagenda = ? ");
			args.add(filtro.getCdagendamento());
		}
		
		if (filtro.getApenasNaoAgendados() && filtro.getCdagenda() != null){
			where.append("   and ap.cdagenda is null ");
		}

		if (filtro.getNumeroPedido() != null){
			where.append("   and pc.numero = ? ");
			args.add(filtro.getNumeroPedido());
		}
		
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getCdpessoa() != null){
			where.append("   and pc.cdpessoa = ? ");
			args.add(filtro.getFornecedor().getCdpessoa());
		}
		
		if (filtro.getDatainicial() != null){
			where.append("	and pc.dtemissao >= ? \n");
			args.add(filtro.getDatainicial());
		}
		if (filtro.getDatafinal() != null){
			where.append("	and pc.dtemissao <= ? \n");
			args.add(filtro.getDatafinal());
		}

		select.append(where);
		
		select.append("	group by pc.cdpedidocompra, pc.codigoerp, pc.numero, f.cdpessoa, f1.nome, pc.dtemissao, \n");
		select.append("		pc.dtcancelamento \n");

		select.append(") \n");
		select.append("where nvl(qtdeAgenda, 0) < qtdeTotal \n");
		select.append("order by codigoerp \n");
		
		return getJdbcTemplate().query(select.toString(), args.toArray(), new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int row) throws SQLException {
				Pedidocompra pc = new Pedidocompra();
				pc.setCdpedidocompra(rs.getInt("cdpedidocompra"));
				pc.setCodigoerp(rs.getLong("codigoerp"));
				pc.setNumero(rs.getString("numero"));
				pc.setDtemissao(rs.getDate("dtemissao"));
				pc.setDtcancelamento(rs.getDate("dtcancelamento"));
				pc.setQtdetotal(rs.getInt("qtdeTotal"));
				pc.setQtdeagenda(rs.getInt("qtdeAgenda"));
				
				pc.setFornecedor(new Fornecedor());
				pc.getFornecedor().setCdpessoa(rs.getInt("cdpessoa"));
				pc.getFornecedor().setNome(rs.getString("nome"));
				
				return pc;
			}
		});
	}
	
	/**
	 * Método usuado para encontrar um pedidocompra
	 * @author Leonardo Guimarães 
	 * @param pedidocompra
	 * @return
	 */
	public Pedidocompra findByPedidocompra(Pedidocompra pedidocompra) {
		if(pedidocompra == null || pedidocompra.getCdpedidocompra() == null){
			throw new WmsException("O pedidocompra não deve ser nulo");
		}
		return query()	
					.select("pedidocompra.cdpedidocompra,pedidocompra.codigoerp,pedidocompra.numero,fornecedor.cdpessoa," +
							"fornecedor.nome, fornecedor.documento, pessoanatureza.cdpessoanatureza," +
							"pessoanatureza.nome, fornecedor.ativo," +
							"pedidocompra.dtemissao,pedidocompra.dtcancelamento")
					.join("pedidocompra.fornecedor fornecedor")
					.join("fornecedor.pessoanatureza pessoanatureza")
					.entity(pedidocompra)
					.unique();
	}
	
	/**
	 * Busca os pedidos de compra para a tela de liberação dos pedidos para o agendamento.
	 *
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Pedidocompra> findForLiberarPedido(LiberarPedidoCompraFiltro filtro) {
		if(filtro == null){
			throw new WmsException("Filtro não pode ser nulo.");
		}
		
		QueryBuilder<Pedidocompra> query = new QueryBuilder<Pedidocompra>(getHibernateTemplate())
		.select("pedidocompra.cdpedidocompra, pedidocompra.dtemissao, pedidocompra.numero, " +
				"fornecedor.cdpessoa, fornecedor.nome, pedidocomprastatus.cdpedidocomprastatus, pedidocomprastatus.nome")
		.from(Pedidocompra.class)
		.leftOuterJoin("pedidocompra.pedidocomprastatus pedidocomprastatus")
		.leftOuterJoin("pedidocompra.fornecedor fornecedor")
		.leftOuterJoin("pedidocompra.deposito deposito")
		.leftOuterJoin("pedidocompra.listapedidocompraproduto pedidocompraproduto")
		.leftOuterJoin("pedidocompraproduto.produto produto")
		.where("deposito = ?", filtro.getDeposito())
		.where("fornecedor = ?", filtro.getFornecedor())
		.where("pedidocompra.dtemissao >= ?", filtro.getDtemissao1())
		.where("pedidocompra.dtemissao <= ?", filtro.getDtemissao2())
		.where("pedidocompra.numero like ?", filtro.getNumeroPedido())
		.where("produto.codigo like ?", filtro.getCodigoProduto());
	//	.where("pedidocompraproduto.qtdeliberada < pedidocompraproduto.qtde")
		if(filtro.getPedidocomprastatus() != null && filtro.getPedidocomprastatus().equals(Pedidocomprastatus.LIBERADO_TOTAL)){			
				query.where("pedidocomprastatus = ?", Pedidocomprastatus.LIBERADO)
				.where(	"(select sum(pcp.qtde) from Pedidocompra pc join pc.listapedidocompraproduto pcp where pc.id = pedidocompra.id) <= " +
						" (select coalesce (sum(pcpl.qtdeliberada),0) from Pedidocompraprodutolibera pcpl " +
						"join pcpl.pedidocompraproduto pcp " +
						"join pcp.pedidocompra pc where pc.id = pedidocompra.id ) ");
		}else{
		query.where("pedidocomprastatus = ?", filtro.getPedidocomprastatus())
		// Colocado esta condição para não aparecer os pedidos que foram totalmente liberados.
		.where(	"(select sum(pcp.qtde) from Pedidocompra pc join pc.listapedidocompraproduto pcp where pc.id = pedidocompra.id) > " +
				" (select coalesce (sum(pcpl.qtdeliberada),0) from Pedidocompraprodutolibera pcpl " +
				"join pcpl.pedidocompraproduto pcp " +
				"join pcp.pedidocompra pc where pc.id = pedidocompra.id ) ");
		}
		return query.orderBy("pedidocompra.numero desc").list();
	}

	/**
	 * Busca os pedidos de compra com os detalhes dos produtos para a liberação.
	 *
	 * @param pedidos
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Pedidocompra> findForLiberarPedido(String pedidos) {
		if(pedidos == null || pedidos.equals("")){
			throw new WmsException("Nenhum pedido selecionado.");
		}
		return query()
					.select("pedidocompra.cdpedidocompra, pedidocompra.dtemissao, pedidocompra.numero, " +
							"pedidocomprastatus.cdpedidocomprastatus, pedidocomprastatus.nome, " +
							"produto.codigo, produto.descricao, pedidocompraproduto.cdpedidocompraproduto, " +
							"pedidocompraproduto.valor," +
							"pedidocompraproduto.qtde," +
							"produtoclasse.cdprodutoclasse,produtoclasse.numero," +
							"deposito.cddeposito, listaPedidocompraprodutolibera.cdpedidocompraprodutolibera," +
		  					"listaPedidocompraprodutolibera.dtprevisaorecebimento, listaPedidocompraprodutolibera.qtdeliberada")
					.leftOuterJoin("pedidocompra.deposito deposito")
					.leftOuterJoin("pedidocompra.pedidocomprastatus pedidocomprastatus")
					.leftOuterJoin("pedidocompra.listapedidocompraproduto pedidocompraproduto")
					.leftOuterJoin("pedidocompraproduto.produto produto")
					.leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
					.leftOuterJoin("produto.produtoclasse produtoclasse")
					.whereIn("pedidocompra.cdpedidocompra", pedidos)
					.list();
	}

	/**
	 * Atualiza o status do pedido de compra com o status passado por parâmetro.
	 *
	 * @param pc
	 * @param status
	 * @author Rodrigo Freitas
	 */
	public void updateStatusPedidoCompra(Pedidocompra pc, Pedidocomprastatus status) {
		if(pc == null || pc.getCdpedidocompra() == null){
			throw new WmsException("O pedido de compra não deve ser nulo");
		}
		getHibernateTemplate().bulkUpdate("update Pedidocompra pc set pc.pedidocomprastatus = ? where pc.id = ?", 
				new Object[]{status, pc.getCdpedidocompra()});
	}

	/**
	 * Busca o pedido de compra para a validação do agendamento.
	 * Busca as classes dos produtos preenchidos.
	 *
	 * @param pc
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Pedidocompra loadForValidacaoAgendamento(Pedidocompra pc) {
		if(pc == null || pc.getCdpedidocompra() == null){
			throw new WmsException("Pedido de compra não pode ser nulo.");
		}
		return query()
					.select("pedidocompra.cdpedidocompra, produtoclasse.cdprodutoclasse, produtoclasse.nome, produtoclasse.numero, " +
							"pedidocompraproduto.valor, pedidocompraproduto.cdpedidocompraproduto, listaPedidocompraprodutolibera.cdpedidocompraprodutolibera," +
		  					"listaPedidocompraprodutolibera.dtprevisaorecebimento, listaPedidocompraprodutolibera.qtdeliberada")
					.leftOuterJoin("pedidocompra.listapedidocompraproduto pedidocompraproduto")
					.leftOuterJoin("pedidocompraproduto.produto produto")
					.leftOuterJoin("produto.produtoclasse produtoclasse")
					.leftOuterJoin("pedidocompraproduto.listaPedidocompraprodutolibera listaPedidocompraprodutolibera")
					.where("pedidocompra = ?", pc)
					.unique();
	}
	
	/**
	 * Busca os totais liberados por mês dentro de um semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ResumoAgendaverba> findResumoLiberado(Deposito deposito, Date exercicio){
		if (deposito == null || exercicio == null)
			throw new WmsException("Parâmetros inválidos.");
		
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct cla.numero AS classeproduto, Trunc(pcpl.dtprevisaorecebimento, 'MM') AS mes, Sum(pcp.valor * Nvl(pcpl.qtdeliberada, 0)) AS valor ");
		sql.append("FROM pedidocompra pc ");
		sql.append("  join pedidocompraproduto pcp ON pc.cdpedidocompra = pcp.cdpedidocompra ");
		sql.append("  join produto p ON p.cdproduto = pcp.cdproduto ");
		sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
		sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
		sql.append("  left join pedidocompraprodutolibera pcpl ON pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto  ");
		sql.append("  join deposito d ON d.cddeposito = pc.cddeposito ");
		sql.append("WHERE Trunc(pcpl.dtprevisaorecebimento, 'MM') >= ? AND Trunc(pcpl.dtprevisaorecebimento, 'MM') <= ? ");
//		sql.append(" AND d.ativo = 1");
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(exercicio);
		calendar.add(Calendar.MONTH, 5);
				
		args.add(exercicio);
		args.add(calendar.getTime());

		if (deposito.getCddeposito() != null && deposito.getCddeposito() > 0){
			sql.append(" AND pc.cddeposito = ? ");
			args.add(deposito.getCddeposito());
		}
		
		sql.append("GROUP BY cla.numero, Trunc(pcpl.dtprevisaorecebimento, 'MM') ");

		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResumoAgendaverba resumoAgendaverba = new ResumoAgendaverba();
				resumoAgendaverba.setClasseproduto(rs.getString("classeproduto"));
				resumoAgendaverba.setMes(rs.getDate("mes"));
				resumoAgendaverba.setValor(new Money(rs.getLong("valor"), true));
				return resumoAgendaverba;
			}
		});
	}

	/**
	 * Lista os pedidos de compra para um determinado mês de acordo com o seu status.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PedidoCompraResumoVO> filtrarMesStatus(PedidocompraFiltro filtro) {
		if (filtro == null || filtro.getDtagendaverba() == null || filtro.getDeposito() == null || filtro.getPedidocomprastatus() == null)
			throw new WmsException("Parâmetros inválidos.");
		
			
			//.orderBy("fornecedor.nome, pedidocompra.numero")

		StringBuilder sql = new StringBuilder();

		if (filtro.getPedidocomprastatus().equals(Pedidocomprastatus.LIBERADO)){
			sql.append("SELECT f.nome as fornecedor, pcpl.dtprevisaorecebimento as dtrecebimento, pc.numero, pc.dtemissao, ");
			sql.append("  Sum(pcp.valor * pcpl.qtdeliberada) AS valor, 1 as validado ");
			sql.append("FROM pedidocompra pc ");
			sql.append("  join pessoa f ON pc.cdpessoa = f.cdpessoa ");
			sql.append("  join pedidocompraproduto pcp ON pc.cdpedidocompra = pcp.cdpedidocompra ");
			sql.append("  join produto p ON p.cdproduto = pcp.cdproduto ");
			sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
			sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
			sql.append("  left join pedidocompraprodutolibera pcpl ON pcpl.cdpedidocompraproduto = pcp.cdpedidocompraproduto ");
			sql.append("WHERE trunc(pcpl.dtprevisaorecebimento, 'MM') = trunc(?, 'MM') ");
			if (filtro.getDeposito().getCddeposito() > 0)
				sql.append(" AND pc.cddeposito = ? ");
			if (filtro.getClasseproduto() != null && !filtro.getClasseproduto().trim().isEmpty())
				sql.append("  AND cla.numero = ? ");
			sql.append("  AND Nvl(pcpl.qtdeliberada, 0) > 0 ");
			sql.append("GROUP BY f.nome, pcpl.dtprevisaorecebimento, pc.numero, pc.dtemissao ");
		}else if (filtro.getPedidocomprastatus().equals(Pedidocomprastatus.AGENDADO)){
			sql.append("SELECT f.nome as fornecedor, a.dtagenda AS dtrecebimento, pedc.numero, pedc.dtemissao, a.validado, "); 
			sql.append("  Sum(CASE WHEN (ap.parcial = 1) THEN aparc.qtde * pcp.valor ELSE pcp.qtde * pcp.valor END) AS valor ");
			sql.append("FROM agenda a ");
			sql.append("  join agendapedido ap ON ap.cdagenda = a.cdagenda ");
			sql.append("  join pedidocompraproduto pcp ON pcp.cdpedidocompra = ap.cdpedidocompra ");
			sql.append("  join pedidocompra pedc ON pedc.cdpedidocompra = pcp.cdpedidocompra ");
			sql.append("  join pessoa f ON pedc.cdpessoa = f.cdpessoa ");
			sql.append("  join produto p ON p.cdproduto = pcp.cdproduto  ");
			sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
			sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
			sql.append("  left join agendaparcial aparc ON aparc.cdagenda = a.cdagenda AND aparc.cdpedidocompraproduto = pcp.cdpedidocompraproduto AND ap.parcial = 1 ");
			sql.append("WHERE trunc(a.dtagenda, 'MM') = trunc(?, 'MM') AND a.cdagendastatus = 1 ");
			if (filtro.getDeposito().getCddeposito() > 0)
				sql.append(" AND a.cddeposito = ? ");
			if (filtro.getClasseproduto() != null && !filtro.getClasseproduto().trim().isEmpty())
				sql.append("  AND cla.numero = ? ");
			sql.append("GROUP BY f.nome, a.dtagenda, pedc.numero, pedc.dtemissao, a.validado ");
		} else if (filtro.getPedidocomprastatus().equals(Pedidocomprastatus.RECEBIDO)){
			sql.append("SELECT r.dtfinalizacao as dtrecebimento, f.nome as fornecedor, pedc.numero, pedc.dtemissao, Sum(nfep.valor * nfep.qtde) AS valor, NVL(a.validado, 0) as validado ");
			sql.append("FROM recebimento r  ");
			sql.append("  join recebimentonotafiscal rnf ON rnf.cdrecebimento = r.cdrecebimento ");
			sql.append("  join notafiscalentrada nfe ON rnf.cdnotafiscalentrada = nfe.cdnotafiscalentrada ");
			sql.append("  join pedidocompra pedc ON nfe.cdpedidocompra = pedc.cdpedidocompra ");
			sql.append("  join pessoa f ON f.cdpessoa = pedc.cdpessoa ");
			sql.append("  join notafiscalentradaproduto nfep ON nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
			sql.append("  join produto p ON p.cdproduto = nfep.cdproduto  ");
			sql.append("  join v_produtoclasse v ON p.cdprodutoclasse = v.cdprodutoclasse ");
			sql.append("  join produtoclasse cla on cla.cdprodutoclasse = v.cdclasse ");
//			sql.append("  join agendapedido ap on ap.cdpedidocompra = nfe.cdpedidocompra ");
//			sql.append("  join agenda a on (a.cdagenda = ap.cdagenda and a.cdagenda = nfe.cdagenda) ");
			sql.append("  join agenda a on (a.cdagenda = nfe.cdagenda) ");			
			sql.append("WHERE trunc(r.dtfinalizacao, 'MM') = trunc(?, 'MM') AND r.cdrecebimentostatus in (3,4,7,8) ");
			if (filtro.getDeposito().getCddeposito() > 0)
				sql.append(" AND r.cddeposito = ? ");
			if (filtro.getClasseproduto() != null && !filtro.getClasseproduto().trim().isEmpty())
				sql.append("  AND cla.numero = ? ");
			sql.append("GROUP BY r.dtfinalizacao, f.nome, pedc.numero, pedc.dtemissao, a.validado ");
		}
		
		List<Object> args = new ArrayList<Object>();
		args.add(filtro.getDtagendaverba());
		
		if (filtro.getDeposito().getCddeposito() > 0)
			args.add(filtro.getDeposito().getCddeposito());

		if (filtro.getClasseproduto() != null && !filtro.getClasseproduto().trim().isEmpty())
			args.add(filtro.getClasseproduto());
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				PedidoCompraResumoVO resumo = new PedidoCompraResumoVO();
				resumo.setFornecedor(rs.getString("fornecedor"));
				resumo.setDtrecebimento(rs.getDate("dtrecebimento"));
				resumo.setValor(new Money(rs.getLong("valor"), true));
				resumo.setNumero(rs.getString("numero"));
				resumo.setDataemissao(rs.getDate("dtemissao"));
				resumo.setHouveValidacao(rs.getBoolean("validado"));
				return resumo;
			}
		});
	}
	
	
	public Pedidocompra findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		.select("pedidocompra.cdpedidocompra, pedidocompra.codigoerp," +
				"listapedidocompraproduto.cdpedidocompraproduto ")
		.leftOuterJoin("pedidocompra.listapedidocompraproduto listapedidocompraproduto")
		.where("pedidocompra.codigoerp = ? ", codigoERP)
		.unique();
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listapedidocompraproduto");
	}
	
	public Integer getQtdeAgendada(Pedidocompra pedidocompra) {
		return getJdbcTemplate().queryForInt("SELECT BUSCAR_QTDEAGENDA(?) FROM DUAL", 
				new Integer[]{pedidocompra.getCdpedidocompra()});
	}
}
