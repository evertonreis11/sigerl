package br.com.linkcom.wms.geral.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Expedicao;
import br.com.linkcom.wms.geral.bean.Expedicaostatus;
import br.com.linkcom.wms.util.WmsException;

public class ExpedicaoDAO extends br.com.linkcom.wms.util.neo.persistence.GenericDAO<Expedicao> {

	@Override
	public void updateEntradaQuery(QueryBuilder<Expedicao> query) {
		query
			.joinFetch("expedicao.box box")
			.joinFetch("box.boxstatus boxstatus")
			.joinFetch("expedicao.expedicaostatus expedicaostatus")
			.leftOuterJoinFetch("expedicao.listaOrdensservico listaOrdensservico")
			.leftOuterJoinFetch("listaOrdensservico.ordemtipo ordemtipo")
			.leftOuterJoinFetch("listaOrdensservico.ordemstatus ordemstatus")
			.orderBy("ordemtipo.cdordemtipo, listaOrdensservico.cdordemservico");
	}
	
	@Override
	public Expedicao load(Expedicao bean) {
		if(bean == null){
			return null;
		}
		
		return query()
			.joinFetch("expedicao.expedicaostatus expedicaostatus")
			.joinFetch("expedicao.box box")
			.entity(bean)
			.unique();
	}

	/**
	 * Método que carrega dados da expedição para gerenciamento
	 * 
	 * @param box
	 * @return
	 * @author Tomás Rabelo
	 */
	public Expedicao loadExpedicaoForGerenciamento(Box box) {
		return query()
			.select("expedicao.cdexpedicao, expedicao.dtexpedicao, expedicao.dtfimexpedicao, box.cdbox, box.nome, boxstatus.cdboxstatus, boxstatus.nome, expedicaostatus.cdexpedicaostatus, " +
					"expedicaostatus.nome, listaOrdensservico.cdordemservico, ordemtipo.cdordemtipo, ordemtipo.nome, ordemstatus.cdordemstatus, ordemstatus.nome, carregamento.cdcarregamento, " +
					"tipooperacao.cdtipooperacao, tipooperacao.nome, clienteExpedicao.cdpessoa, clienteExpedicao.nome")
			.join("expedicao.box box")
			.join("box.boxstatus boxstatus")
			.join("expedicao.expedicaostatus expedicaostatus")
			.leftOuterJoin("expedicao.listaOrdensservico listaOrdensservico")
			.leftOuterJoin("listaOrdensservico.ordemtipo ordemtipo")
			.leftOuterJoin("listaOrdensservico.clienteExpedicao clienteExpedicao")
			.leftOuterJoin("listaOrdensservico.tipooperacao tipooperacao")
			.leftOuterJoin("listaOrdensservico.carregamento carregamento")
			.leftOuterJoin("carregamento.veiculo veiculo")
			.leftOuterJoin("listaOrdensservico.ordemstatus ordemstatus")
			.where("box = ?", box)
			.where("expedicaostatus = ?", Expedicaostatus.EM_ANDAMENTO)
			.orderBy("ordemtipo.cdordemtipo, listaOrdensservico.cdordemservico")
			.unique();
	}

	/**
	 * Método que verifica se o box possui alguma expedição em andamento
	 * 
	 * @param box
	 * @return
	 * @author Tomás Rabelo
	 */
	public Boolean possuiExpedicaoEmAndamento(Box box) {
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(Expedicao.class)
			.join("expedicao.expedicaostatus expedicaostatus")
			.join("expedicao.box box")
			.where("box = ?", box)
			.where("expedicaostatus = ?", Expedicaostatus.EM_ANDAMENTO)
			.unique() > 0;
	}

	/**
	 * Método que carrega expedição para tela de gerenciamento. Este método é chamado a partir da tela de carregamento listagem
	 * 
	 * @param expedicao
	 * @return
	 * @author Tomás Rabelo
	 */
	public Expedicao loadExpedicaoForGerenciamento(Expedicao expedicao) {
		return query()
			.select("expedicao.cdexpedicao, expedicao.dtexpedicao, expedicao.dtfimexpedicao, box.cdbox, " +
					"box.nome, boxstatus.cdboxstatus, boxstatus.nome, expedicaostatus.cdexpedicaostatus, " +
					"expedicaostatus.nome, ordemservico.cdordemservico, ordemtipo.cdordemtipo, " +
					"ordemtipo.nome, ordemstatus.cdordemstatus, ordemstatus.nome, carregamento.cdcarregamento, " +
					"cliente.cdpessoa, cliente.nome, tipooperacao.cdtipooperacao, tipooperacao.nome, tipooperacao.separacliente," +
					"tipooperacao.sigla, veiculo.cdveiculo, veiculo.placa")
			.join("expedicao.box box")
			.join("box.boxstatus boxstatus")
			.join("expedicao.expedicaostatus expedicaostatus")
			.join("expedicao.listaOrdensservico ordemservico")
			.join("ordemservico.ordemtipo ordemtipo")
			.join("ordemservico.ordemstatus ordemstatus")
			.join("ordemservico.listaOrdemProdutoLigacao ordemprodutoligacao")
			.leftOuterJoin("ordemservico.carregamento carregamento")
			.leftOuterJoin("carregamento.veiculo veiculo")
			.leftOuterJoin("ordemservico.clienteExpedicao cliente")
			.leftOuterJoin("ordemservico.tipooperacao tipooperacao")
			.where("expedicao = ?", expedicao)
			.orderBy("ordemtipo.cdordemtipo, ordemservico.cdordemservico")
			.unique();
	}

	/**
	 * Método que carrega expedição pela ordem de serviço
	 * 
	 * @param expedicao
	 * @return
	 * @author Tomás Rabelo
	 */
	public Expedicao getExpedicao(Expedicao expedicao) {
		return query()
			.select("expedicao.cdexpedicao, box.cdbox, listaCarregamento.cdcarregamento, listaOrdensservico.cdordemservico, ordemtipo.cdordemtipo")
			.join("expedicao.box box")
			.leftOuterJoin("expedicao.listaCarregamento listaCarregamento")
			.leftOuterJoin("expedicao.listaOrdensservico listaOrdensservico")
			.leftOuterJoin("listaOrdensservico.ordemtipo ordemtipo")
			.where("expedicao = ?", expedicao)
			.unique();
	}

	/**
	 * Método que atualiza status da expedição
	 * 
	 * @param expedicaoAux
	 * @param status
	 * @author Tomás Rabelo
	 */
	public void updateStatusExpedicao(Expedicao expedicaoAux, Expedicaostatus status) {
		if(expedicaoAux == null || expedicaoAux.getCdexpedicao() == null || status == null || status.getCdexpedicaostatus() == null)
			throw new WmsException("Parâmetros inválidos.");
		

		if (status.equals(Expedicaostatus.FINALIZADO) && expedicaoAux.getDtfimexpedicao() == null){
			expedicaoAux.setDtfimexpedicao(new Timestamp(System.currentTimeMillis()));

			String hql = "update Expedicao expedicao set expedicao.expedicaostatus.id = ?, expedicao.dtfimexpedicao = ? where expedicao.id = ? ";
			getHibernateTemplate().bulkUpdate(hql, new Object[]{status.getCdexpedicaostatus(), expedicaoAux.getDtfimexpedicao(), expedicaoAux.getCdexpedicao()});		
		} else {
			String hql = "update Expedicao expedicao set expedicao.expedicaostatus.id = ? where expedicao.id = ? ";
			getHibernateTemplate().bulkUpdate(hql, new Object[]{status.getCdexpedicaostatus(), expedicaoAux.getCdexpedicao()});		
		}
		
	}

	/**
	 * Método que verifica se há alguma expedição em andamento para o box
	 * 
	 * @param box
	 * @return
	 * @author Tomás Rabelo
	 */
	public boolean existeExpedicaoEmAndamentoParaBox(Box box) {
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(Expedicao.class)
			.join("expedicao.box box")
			.join("expedicao.expedicaostatus expedicaostatus")
			.where("box = ?", box)
			.where("expedicaostatus = ?", Expedicaostatus.EM_ANDAMENTO)
			.unique() > 0;
	}

	/**
	 * Método que carrega as expedições do box
	 * 
	 * @param box
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Expedicao> findExpedicoesDoBoxEmAndamento(Box box) {
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.select("expedicao.cdexpedicao, expedicao.dtexpedicao,carregamento.cdcarregamento,carregamento.listaRota")
			.join("expedicao.box box")
			.join("expedicao.listaCarregamento carregamento")
			.where("expedicao.expedicaostatus = ?", Expedicaostatus.EM_ANDAMENTO)
			.where("box = ?", box)
			.orderBy("expedicao.cdexpedicao desc")
			.list();
	}
	
	/**
	 * Chama a procedure 'baixar_estoque_expedicao' no banco de dados do sistema
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param deposito
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void baixarEstoqueExpedicao(final Expedicao expedicao) {
		if(expedicao == null || expedicao.getCdexpedicao() == null)
			throw new WmsException("Dados insuficientes para invocar a função 'baixar_estoque_expedicao'");

		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("baixar_estoque_expedicao")
						.setInteger(0, expedicao.getCdexpedicao())
						.executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			throw new WmsException("Erro ao executar a procedure BAIXAR_ESTOQUE_EXPEDICAO.", e);
		}
	}
	
	/**
	 * Chama a procedure 'ENDERECAR_MAPAS_EXPEDICAO' no banco de dados do sistema
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param deposito
	 * @param carregamento
	 * @throws SQLException 
	 */
	public void enderecarExpedicao(final Expedicao expedicao) {
		if(expedicao == null || expedicao.getCdexpedicao() == null)
			throw new WmsException("Dados insuficientes para invocar a função 'ENDERECAR_MAPAS_EXPEDICAO'");
		
		try{
			getHibernateTemplate().execute(new HibernateCallback(){
				
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("enderecar_mapas_expedicao")
					.setInteger(0, expedicao.getCdexpedicao())
					.executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			throw new WmsException("Erro ao executar a procedure ENDERECAR_MAPAS_EXPEDICAO.", e);
		}
	}
	
}
