package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.ibm.icu.util.Currency;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecolado;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.service.EnderecoService;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.ConsultarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.TransferenciaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EnderecoprodutoDAO extends GenericDAO<Enderecoproduto> {
		
	@Override
	public void updateEntradaQuery(QueryBuilder<Enderecoproduto> query) {
		query.joinFetch("enderecoproduto.produto produto")
			.joinFetch("enderecoproduto.endereco endereco")
			.joinFetch("endereco.enderecostatus status")
			.joinFetch("endereco.enderecofuncao funcao")
			.joinFetch("endereco.area area");
	}
	
	/**
	 * 
	 * Recupera a listagem de endereços produtos.
	 * 
	 * @author Arantes
	 * @author Giovane Freitas
	 * 
	 * @param transferenciaFiltro
	 * @return List<Enderecoproduto>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Enderecoproduto> findByEnderecoorigemproduto(TransferenciaFiltro filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos");

		QueryBuilder<Enderecoproduto> queryBuilder = query()
				.select("enderecoproduto.cdenderecoproduto, enderecoproduto.qtde,enderecoproduto.qtdereservadasaida," +
						"enderecoproduto.qtdereservadaentrada,endereco.cdendereco,endereco.endereco,endereco.rua," +
						"endereco.predio,endereco.nivel,endereco.apto,enderecofuncao.cdenderecofuncao," +
						"enderecofuncao.nome,area.cdarea,area.codigo,area.nome,produto.cdproduto,produto.descricao," +
						"produto.codigo,produtoprincipal.cdproduto,produtoprincipal.descricao,produtoprincipal.codigo," +
						"dadologistico.cddadologistico,dadologistico.larguraexcedente")
				.join("enderecoproduto.endereco endereco")
				.join("endereco.enderecofuncao enderecofuncao")
				.join("endereco.area area")
				.join("enderecoproduto.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.join("produto.listaDadoLogistico dadologistico with dadologistico.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
				.where("enderecoproduto.qtde >= 1")
				.where("area.deposito = ? ", WmsUtil.getDeposito())
				.where("area.codigo = ? ", filtro.getCodigoareaOrigem())
				.where("endereco.rua = ? ", filtro.getRuaOrigem())
				.where("endereco.predio = ? ", filtro.getPredioOrigem())
				.where("endereco.nivel = ? ", filtro.getNivelOrigem())
				.where("endereco.apto = ? ", filtro.getAptoOrigem())
				.where("produto = ? ", filtro.getProduto())
				.where("area.box = false");
					
		return queryBuilder.list();	
	}
	
	/**
	 * Busca o endereço produto através dos parâmetros passados
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @param produto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Enderecoproduto findByEnderecoAndProduto(Endereco endereco,Produto produto) {
	
		if(endereco == null || endereco.getCdendereco() == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("O endereço e/ou produto não devem ser nulos.");
		
		List<Object> args = new ArrayList<Object>();
		StringBuilder select = new StringBuilder();
		
		select.append(" SELECT EP.CDENDERECOPRODUTO,EP.CDENDERECO,EP.CDPRODUTO,EP.QTDE,EP.DTENTRADA,EP.QTDERESERVADAENTRADA,EP.QTDERESERVADASAIDA,EP.UMA ");
		select.append(" FROM ENDERECOPRODUTO EP ");
		select.append(" WHERE EP.CDENDERECO = ? ");
		args.add(endereco.getCdendereco());
		select.append(" AND EP.CDPRODUTO = ? ");		
		args.add(produto.getCdproduto());
				
		List<Enderecoproduto> list = (List<Enderecoproduto>) getJdbcTemplate().query(select.toString(), args.toArray(),new ResultSetExtractor(){
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Enderecoproduto> list = new ArrayList<Enderecoproduto>();
				
				while (rs.next()){
					Enderecoproduto enderecoproduto = new Enderecoproduto();
					enderecoproduto.setCdenderecoproduto(rs.getInt("CDENDERECOPRODUTO"));
						Endereco endereco = new Endereco();
						endereco.setCdendereco(rs.getInt("CDENDERECO"));
					enderecoproduto.setEndereco(endereco);
						Produto produto = new Produto();
						produto.setCdproduto(rs.getInt("CDPRODUTO"));
					enderecoproduto.setProduto(produto);
					enderecoproduto.setQtde(rs.getLong("QTDE"));
					enderecoproduto.setDtentrada(rs.getDate("DTENTRADA"));
					enderecoproduto.setQtdereservadaentrada(rs.getLong("QTDERESERVADAENTRADA"));
					enderecoproduto.setQtdereservadasaida(rs.getLong("QTDERESERVADASAIDA"));
					enderecoproduto.setUma(rs.getBoolean("UMA"));
					list.add(enderecoproduto);
				}
				return list;
			}
		});
		
		Enderecoproduto enderecoproduto = null;
		if(list!=null && !list.isEmpty()){
			enderecoproduto = list.get(0);
			Enderecoproduto epaux = null;
			int index = 0;		
			for(int i=1;i<list.size();i++){
				epaux = list.get(i);
				if(enderecoproduto.getDtentrada().after(epaux.getDtentrada())){
					enderecoproduto = list.get(i);
					index = i;
				}
			}
			
			list.remove(index);
			for (Enderecoproduto enderecoproduto2 : list) {
					enderecoproduto.setQtde(enderecoproduto.getQtde() + enderecoproduto2.getQtde());
					enderecoproduto.setQtdereservadaentrada(enderecoproduto.getQtdereservadaentrada() + enderecoproduto2.getQtdereservadaentrada());
					enderecoproduto.setQtdereservadasaida(enderecoproduto.getQtdereservadasaida() + enderecoproduto2.getQtdereservadasaida());
				delete(enderecoproduto2);
			}
		}
		return enderecoproduto;
	}
	
	/**
	 * Retorna o produtoEndereço 
	 * @param endereco
	 * @param produto
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Enderecoproduto loadByEnderecoEProduto(Endereco endereco,Produto produto) {
		
		if(endereco == null || endereco.getCdendereco() == null || produto == null || produto.getCdproduto() == null)	
			throw new WmsException("O endereço e/ou produto não devem ser nulos.");

		return query().
		select("enderecoproduto.cdenderecoproduto, endereco.cdendereco, produto.cdproduto")	
		.join("enderecoproduto.produto produto")
		.join("enderecoproduto.endereco endereco")
		.where("endereco = ?",endereco)					
		.where("produto = ?",produto)
		.unique();
	}
	
	/**
	 * Retorna o endereço produto uma
	 * @param endereco
	 * @param produto
	 * @return
	 * @author Cintia Nogueira
	 */	
	public Enderecoproduto loadByEnderecoEProdutoUma(Endereco endereco,Produto produto) {
			
		if(endereco == null || endereco.getCdendereco() == null || produto == null || produto.getCdproduto() == null)	
			throw new WmsException("O endereço e/ou produto não devem ser nulos.");

		return query()
				.join("enderecoproduto.produto produto")
				.join("enderecoproduto.endereco endereco")
				.where("endereco = ?",endereco)					
				.where("produto = ?",produto)
				.orderBy("enderecoproduto.uma desc")
				.setMaxResults(1)
				.unique();
	}
	
	/**
	 * Atualiza a quantidade no enderecoProduto
	 * @param enderecoproduto
	 * @author Cíntia Nogueira
	 */
	public void updateQtde(Enderecoproduto enderecoproduto) {
		if(enderecoproduto == null || enderecoproduto.getCdenderecoproduto() == null || 
				   enderecoproduto.getQtde() == null)		{
			throw new WmsException("Dados inválidos para execução da função updateQtde.");
		}
		String query = "update Enderecoproduto set qtde = ? where cdenderecoproduto = ?";
		Object[] objects= {enderecoproduto.getQtde(),enderecoproduto.getCdenderecoproduto()};
		getHibernateTemplate().bulkUpdate(query,objects);
	}
		
	public List<Enderecoproduto> findByEnderecos(List<Endereco> listaendereco, Produto produto) {
		if(listaendereco == null || listaendereco.isEmpty())
			throw new WmsException("O endereco não deve ser nulo.");
		
		return query()
					.select("enderecoproduto.cdenderecoproduto," +
							"enderecoproduto.qtde,endereco.cdendereco,produto.cdproduto," +
							"enderecoproduto.qtde,enderecofuncao.cdenderecofuncao,endereco.nivel,endereco.apto")
					.join("enderecoproduto.endereco endereco")
					.join("enderecoproduto.produto produto")
					.join("endereco.area area")
					.join("endereco.enderecofuncao enderecofuncao")
					.whereIn("endereco.cdendereco",CollectionsUtil.listAndConcatenate(listaendereco, "cdendereco", ","))
					.where("enderecoproduto.qtde > 0")
					.where("enderecoproduto.produto = ?", produto)
					.orderBy("endereco.nivel, endereco.apto desc")
					.list()
					;
	}
	
	
	
	/**
	 * Retorna com cdendereco carregado
	 * @param bean
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Enderecoproduto loadWithEndereco(Enderecoproduto bean){
		if(bean==null || bean.getCdenderecoproduto()==null){
			throw new WmsException("O enderecoproduto não deve ser nulo.");
		}
		
		return query()
		.select("enderecoproduto.cdenderecoproduto, enderecoproduto.qtdereservadaentrada, enderecoproduto.qtdereservadasaida, endereco.cdendereco")
		.join("enderecoproduto.endereco endereco")
		.where("enderecoproduto.cdenderecoproduto=?", bean.getCdenderecoproduto())
		.unique();
	}

	/**
	 * Subtrái a quantidade reservada de saída para um determinado endereço.
	 * Este procedimento é utilizado ao cancelar um carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @param qtde
	 * @param produto
	 */
	public void removerQtdeReservadaSaida(Endereco endereco, Produto produto, Long qtde) {
		if (endereco == null || endereco.getCdendereco() == null || qtde == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Enderecoproduto set qtdereservadasaida = qtdereservadasaida - ? where endereco.id = ? and produto.id = ? ", 
				new Object[]{qtde, endereco.getCdendereco(), produto.getCdproduto()});
	}
	
	/**
	 * Subtrái a quantidade reservada de entrada para um determinado endereço.
	 * Este procedimento é utilizado ao cancelar um carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @param qtde
	 * @param produto
	 */
	public void removerQtdeReservadaEntrada(Endereco endereco, Produto produto, Long qtde) {
		if (endereco == null || endereco.getCdendereco() == null || qtde == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Enderecoproduto set qtdereservadaentrada = qtdereservadaentrada - ? where endereco.id = ? and produto.id = ? ", 
				new Object[]{qtde, endereco.getCdendereco(), produto.getCdproduto()});
	}


	/**
	 * Verifica se o endereço já possui um outro produto diferente do informado.
	 * 
	 * @author Giovane Freitas
	 * @param endereco O endereço que deve ser consultado.
	 * @param produto Produto usado para comparação.
	 * @return
	 */
	public boolean possuiOutroProduto(Endereco endereco, Produto produto) {
		if (endereco == null || endereco.getCdendereco() == null || produto == null || produto.getCdproduto() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		QueryBuilder<Long> query = new QueryBuilder<Long>(getHibernateTemplate()).from(Enderecoproduto.class);
		query.select("count(*)").
			where("enderecoproduto.endereco = ?", endereco).
			where("enderecoproduto.produto <> ?", produto);
		Long count = query.unique();
		
		if (count == null || count <= 0L)
			return false;
		else
			return true;
	}
	

	/**
	 * Verifica se em uma faixa de endereços existe algum com quantidade reservada de entrada ou de saída.
	 * 
	 * @author Giovane Freitas
	 * @param inicio 
	 * @param fim 
	 * @return
	 */
	public boolean hasEnderecoReservado(Endereco inicio, Endereco fim, Enderecolado lado) {
		if (inicio == null || fim == null)
			throw new WmsException("Parâmetros inválidos.");
		
		QueryBuilder<Enderecoproduto> queryBuilder = query()
				.where("endereco.area = ?", inicio.getArea())
				.where("endereco.enderecofuncao = ?", inicio.getEnderecofuncao())
				.openParentheses()
					.where("qtdereservadaentrada <> 0 ")
					.or()
					.where("qtdereservadasaida <> 0 ")
				.closeParentheses()
				.whereIntervalMatches("endereco.rua", "endereco.rua", inicio.getRua(), fim.getRua())
				.whereIntervalMatches("endereco.predio", "endereco.predio", inicio.getPredio(), fim.getPredio())
				.whereIntervalMatches("endereco.nivel", "endereco.nivel", inicio.getNivel(), fim.getNivel())
				.whereIntervalMatches("endereco.apto", "endereco.apto", inicio.getApto(), fim.getApto())
				.setUseTranslator(false);
		
		if (lado != null){
			if(Enderecolado.PAR.equals(lado))
				queryBuilder.where("mod(endereco.predio, 2) = 0");
			
			else 
				queryBuilder.where("mod(endereco.predio, 2) <> 0");
		}
		
		Enderecoproduto enderecoproduto = queryBuilder.setMaxResults(1).unique();
		
		if (enderecoproduto == null)
			return false;
		else
			return true;
	}

	/**
	 * Busca o saldo atual de um determinado produto para o depósito atual.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long getSaldo(ConsultarEstoqueFiltro filtro) {
		if (filtro == null || filtro.getProduto() == null) {
			throw new WmsException("O produto não pode ser nulo");
		}

		List<Object> args = new ArrayList<Object>();
		StringBuilder select = new StringBuilder();
		
		select.append("SELECT min(BUSCAR_SALDOPRODUTO(?, V.CDPRODUTO)) as saldo ");
		args.add(WmsUtil.getDeposito().getCddeposito());
		select.append("FROM PRODUTO P ");
		select.append("  inner join produto v on p.cdproduto = v.cdprodutoprincipal ");
		select.append("WHERE P.cdproduto = ? ");
		args.add(filtro.getProduto().getCdproduto());

		select.append(" union all ");

		select.append("SELECT BUSCAR_SALDOPRODUTO(?, P.CDPRODUTO) as saldo ");
		args.add(WmsUtil.getDeposito().getCddeposito());
		select.append("FROM PRODUTO P ");
		select.append("WHERE P.cdproduto = ? ");
		args.add(filtro.getProduto().getCdproduto());

		List<Long> list = getJdbcTemplate().queryForList(select.toString(), args.toArray(), Long.class);

		long total = 0L;
		for (Long parcial : list)
			total += parcial;
		
		return total;
	}
	
}
