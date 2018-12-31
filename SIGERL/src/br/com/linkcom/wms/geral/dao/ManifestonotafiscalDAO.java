package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestonotafiscal;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Statusconfirmacaoentrega;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ManifestonotafiscalDAO extends GenericDAO<Manifestonotafiscal>{
	
	public List<Manifestonotafiscal> findByManifesto(Manifesto manifesto) {
		
		QueryBuilder<Manifestonotafiscal> query = query();
		
		query.select("manifestonotafiscal.cdmanifestonotafiscal, manifestonotafiscal.dataentrega, notafiscalsaida.cdnotafiscalsaida, " +
					"notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.chavenfe, notafiscalsaida.dtemissao ,notafiscalsaida.vlrtotalnf, " +
					"notafiscalsaida.qtdeitens, deposito.cddeposito, deposito.nome, cliente.cdpessoa, cliente.nome, rota.cdrota, rota.nome, " +
					"notafiscalsaida.numeropedido, notafiscalsaida.lojapedido, manifestonotafiscal.dt_inclusao, manifestonotafiscal.dt_alteracao," +
					"manifestonotafiscal.observacao, manifestonotafiscal.ind_avulso, manifestonotafiscal.valorentrega, manifesto.cdmanifesto," +
					"statusconfirmacaoentrega.cdstatusconfirmacaoentrega, statusconfirmacaoentrega.nome, statusconfirmacaoentrega.finanlizador, " +
					"usuario.cdpessoa, usuario.nome, manifestonotafiscal.dataentrega, rota.temDepositoTransbordo, depositotransbordo.cddeposito," +
					"depositotransbordo.nome, depositotransbordoRota.cddeposito, depositotransbordoRota.nome, manifestonotafiscal.temDepositoTransbordo, " +
					"notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.remanifestavel, motivoretornoentrega.cdmotivoretornoentrega")
		     .join("manifestonotafiscal.manifesto manifesto")
		     .join("manifesto.deposito deposito")
		     .join("manifestonotafiscal.usuario usuario")
		     .join("manifestonotafiscal.notafiscalsaida notafiscalsaida")
			 .join("notafiscalsaida.cliente cliente")
			 .join("notafiscalsaida.notafiscaltipo notafiscaltipo")
			 .leftOuterJoin("notafiscalsaida.praca praca")
			 .leftOuterJoin("praca.listaRotapraca rotapraca")
			 .leftOuterJoin("rotapraca.rota rota")
			 .leftOuterJoin("manifestonotafiscal.depositotransbordo depositotransbordo")
			 .leftOuterJoin("rota.depositotransbordo depositotransbordoRota")
			 .leftOuterJoin("manifestonotafiscal.statusconfirmacaoentrega statusconfirmacaoentrega")
			 .leftOuterJoin("manifestonotafiscal.motivoretornoentrega motivoretornoentrega")
			 .where("manifesto = ?",manifesto)
			 .orderBy("cliente.nome");
		
		return query.list();
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public void updateStatusConfirmacaoEntrega(Manifesto manifesto) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update manifestonotafiscal mnf set mnf.cdstatusconfirmacaoentrega = ");
			sql.append(Statusconfirmacaoentrega.ENTREGA_EM_ANDAMENTO.getCdstatusconfirmacaoentrega());
			sql.append(" where mnf.cdmanifesto in ( ").append(manifesto.getCdmanifesto()).append(" ) "); 
	
		getJdbcTemplate().execute(sql.toString());
		
	}
	
	/**
	 * 
	 * @param manifesto
	 */
	public void deleteByManifesto(String whereIn) {
		if(whereIn == null || whereIn.isEmpty()){
			throw new WmsException("Parametros inválidos, erro ao excluir as notas do manifesto.");
		}
		
		getHibernateTemplate().bulkUpdate("delete from Manifestonotafiscal mnf where mnf.manifesto.id in ("+whereIn+")");		
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public void updateStatusConfirmacaoEntrega(Manifestonotafiscal manifestonotafiscal, Statusconfirmacaoentrega statusconfirmacaoentrega) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update manifestonotafiscal mnf set mnf.cdstatusconfirmacaoentrega = ");
			sql.append(statusconfirmacaoentrega.getCdstatusconfirmacaoentrega());
			sql.append(" , mnf.observacao = ");
			
			if (manifestonotafiscal.getObservacao() != null){
				sql.append("'").append(manifestonotafiscal.getObservacao()).append("'");
			}else{
				sql.append(manifestonotafiscal.getObservacao());
			}
			
			sql.append(", mnf.cdmotivoretorno = ");
			sql.append(manifestonotafiscal.getMotivoretornoentrega() != null ? manifestonotafiscal.getMotivoretornoentrega().getCdmotivoretornoentrega() : null); 
			sql.append(" where mnf.cdmanifestonotafiscal = ").append(manifestonotafiscal.getCdmanifestonotafiscal()); 
	
		getJdbcTemplate().execute(sql.toString());
		
	}

	/**
	 * 
	 * @param manifestonotafiscal
	 */
	public void updateDepositoTransbordo(Manifestonotafiscal manifestonotafiscal) {

		StringBuilder sql = new StringBuilder();
		
			sql.append(" update manifestonotafiscal mnf set mnf.temDepositoTransbordo = ");
			sql.append(manifestonotafiscal.getTemDepositoTransbordo() == true ? 1 : 0);
			if(manifestonotafiscal.getDepositotransbordo()!=null && manifestonotafiscal.getDepositotransbordo().getCddeposito()!=null)
				sql.append(" ,mnf.cddepositotransbordo = ").append(manifestonotafiscal.getDepositotransbordo().getCddeposito());
			else
				sql.append(" ,mnf.cddepositotransbordo = null ");
			sql.append(" where mnf.cdmanifestonotafiscal = ").append(manifestonotafiscal.getCdmanifestonotafiscal()); 

		getJdbcTemplate().execute(sql.toString());
		
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public List<Manifestonotafiscal> findByManifestoCodigoBarras(String codigo) {
		return query()
			.select("notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.numero, notafiscalsaida.numeropedido, notafiscalsaida.dtemissao, " +
					"notafiscalsaida.serie, notafiscalsaida.qtdeitens, notafiscalsaida.lojapedido, manifesto.cdmanifesto, notafiscalsaida.dtemissao," +
					"notafiscalsaida.dtemissao, filialfaturamento.nome ")
			.join("manifestonotafiscal.manifesto manifesto")
			.join("manifestonotafiscal.notafiscalsaida notafiscalsaida")
			.join("manifesto.listaManifestocodigobarra manifestocodigobarra")
			.join("manifestonotafiscal.depositotransbordo depositotransbordo")
			.join("manifesto.manifestostatus manifestostatus")
			.join("notafiscalsaida.filialfaturamento filialfaturamento")
			.where("manifestocodigobarra.ativo = 1")
			.where("manifestocodigobarra.codigo = ?",codigo)
			.where("manifestostatus = ?",Manifestostatus.ENTREGA_EM_ANDAMENTO)
			.where("depositotransbordo = ?",WmsUtil.getDeposito())
			.list();
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Manifestonotafiscal> findAllNotasSemPracas(Manifesto manifesto) {
		
		QueryBuilder<Manifestonotafiscal> query = query();
		
		query.select("manifestonotafiscal.cdmanifestonotafiscal, notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.numero, " +
					 "notafiscaltipo.cdnotafiscaltipo, notafiscaltipo.nome, cliente.cdpessoa, pessoaendereco.cdpessoaendereco, " +
					 "pessoaendereco.municipio, pessoaendereco.uf, pessoaendereco.uf, pessoaendereco.logradouro, pessoaendereco.bairro, " +
					 "pessoaendereco.cep, pessoaendereco.numero, pessoaendereco.complemento, praca.cdpraca, praca.nome, manifesto.cdmanifesto, " +
					 "tipoentrega.cdtipoentrega, rota.cdrota, notafiscalsaida.cep")
			 .join("manifestonotafiscal.manifesto manifesto")
			 .join("manifesto.tipoentrega tipoentrega")
			 .join("manifestonotafiscal.notafiscalsaida notafiscalsaida")
			 .join("notafiscalsaida.notafiscaltipo notafiscaltipo")
			 .join("notafiscalsaida.cliente cliente")
			 .join("notafiscalsaida.pessoaendereco pessoaendereco")
			 .leftOuterJoin("notafiscalsaida.praca praca")
			 .leftOuterJoin("praca.listaRotapraca rotapraca")
			 .leftOuterJoin("rotapraca.rota rota")
			 .where("manifestonotafiscal.manifesto = ?",manifesto);
		
		return query.list();
	}

	/**
	 * 
	 * @param manifesto
	 * @param whereIn
	 * @return
	 */
	public List<Manifestonotafiscal> findAllbyManifesto(Manifesto manifesto, String whereIn) {
		
		QueryBuilder<Manifestonotafiscal> query = query();
		
		query.select("notafiscalsaida.cdnotafiscalsaida, notafiscalsaida.serie, notafiscalsaida.numero, notafiscalsaida.chavenfe, " +
				"notafiscalsaida.dtemissao ,notafiscalsaida.vlrtotalnf ,notafiscalsaida.qtdeitens, deposito.cddeposito, deposito.nome, " +
				"cliente.cdpessoa, cliente.nome, pedidovenda.cdpedidovenda, notafiscalsaida.numeropedido, notafiscalsaida.lojapedido, " +
				"pedidovendaproduto.cdpedidovendaproduto, carregamentoitem.cdcarregamentoitem, carregamento.cdcarregamento," +
				"carregamentostatus.cdcarregamentostatus, rota.cdrota, rota.nome, rota.temDepositoTransbordo, depositotransbordo.cddeposito," +
				"depositotransbordo.nome, manifestonotafiscal.temDepositoTransbordo, depositotransbordo2.cddeposito, depositotransbordo2.nome")
			 .join("manifestonotafiscal.manifesto manifesto")
			 .join("manifestonotafiscal.notafiscalsaida notafiscalsaida")
			 .join("notafiscalsaida.deposito deposito")
			 .join("notafiscalsaida.cliente cliente")
			 .join("manifesto.manifestostatus manifestostatus")
			 .leftOuterJoin("notafiscalsaida.listNotafiscalsaidaproduto notafiscalsaidaproduto")
			 .leftOuterJoin("notafiscalsaidaproduto.pedidovenda pedidovenda")
			 .leftOuterJoin("notafiscalsaidaproduto.pedidovendaproduto pedidovendaproduto")
			 .leftOuterJoin("pedidovendaproduto.listaCarregamentoitem carregamentoitem")
			 .leftOuterJoin("carregamentoitem.carregamento carregamento")
			 .leftOuterJoin("carregamento.carregamentostatus carregamentostatus")
			 .leftOuterJoin("notafiscalsaida.praca praca")
			 .leftOuterJoin("praca.listaRotapraca rotapraca")
			 .leftOuterJoin("rotapraca.rota rota")
			 .leftOuterJoin("rota.depositotransbordo depositotransbordo")
			 .leftOuterJoin("manifestonotafiscal.depositotransbordo depositotransbordo2")
			 .where("deposito = ?",WmsUtil.getDeposito())
			 .where("manifestostatus = ?",Manifestostatus.IMPRESSO)
			 .whereIn("manifesto.cdmanifesto", whereIn!=null ? whereIn.trim() : null);
		
		return query.list();
	}

	/**
	 * 
	 * @param manifesto
	 */
	public void updateStatusConfirmacaoEntregaFilhos(Manifesto manifesto) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update manifestonotafiscal mnf set mnf.cdstatusconfirmacaoentrega = ");
			sql.append(Statusconfirmacaoentrega.ENTREGA_EM_ANDAMENTO.getCdstatusconfirmacaoentrega());
			sql.append(" where mnf.cdmanifesto in ");
			sql.append(" (select m.cdmanifesto from manifesto m where m.cdtipoentrega = 2 and m.cdmanifestopai = ");
			sql.append(manifesto.getCdmanifesto()).append(") ");
		
		getJdbcTemplate().execute(sql.toString());
	}
	
	/**
	 * Find token by manifesto.
	 *
	 * @param manifesto the manifesto
	 * @return the list
	 */
	public List<Manifestonotafiscal> findTokenByManifesto(Manifesto manifesto) {
		QueryBuilder<Manifestonotafiscal> query = query();

		query.select("manifestonotafiscal.cdmanifestonotafiscal, manifestonotafiscal.token")
			 .where("manifesto = ?",manifesto)
			 .orderBy("manifestonotafiscal.cdmanifestonotafiscal");
		
		return query.list();
	}
	
}
