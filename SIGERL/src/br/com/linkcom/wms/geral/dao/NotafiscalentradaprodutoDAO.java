package br.com.linkcom.wms.geral.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Notafiscalentradaproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class NotafiscalentradaprodutoDAO extends GenericDAO<Notafiscalentradaproduto> {

	@Override
	public void updateListagemQuery(QueryBuilder<Notafiscalentradaproduto> query, FiltroListagem _filtro) {
//		NotafiscalentradaprodutoFiltro filtro = (NotafiscalentradaprodutoFiltro) _filtro;
		query.leftOuterJoinFetch("notafiscalentradaproduto.notafiscalentrada notafiscalentrada");
		query.leftOuterJoinFetch("notafiscalentradaproduto.produto produto");
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Notafiscalentradaproduto> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	
	/**
	 * Método que recupera uma lista de notafiscalentradaproduto pelo recebimento
	 * 
	 * @param recebimento
	 * @return
	 * @author Arantes
	 */
	public List<Notafiscalentradaproduto> recuperarNotafiscalentradaproduto(Recebimento recebimento) {
		if((recebimento == null) || (recebimento.getCdrecebimento() == null)) {
			throw new WmsException("O recebimento não deve ser nulo.");
		}
		
		return query()
		            .select("notafiscalentradaproduto.cdnotafiscalentradaproduto, produto.pesounitario," +
		            		"notafiscalentradaproduto.qtde, produto.cdproduto, produto.peso")
					.join("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
					.join("notafiscalentradaproduto.produto produto")
					.join("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscal")
					.join("recebimentonotafiscal.recebimento recebimento")
					.where("recebimento = ?", recebimento)					
					.list();
	}
	
	/**
	 * Reseta a validade e o lote do produto
	 * @param lista
	 * @author Cíntia Nogueira
	 */
	public void resetarLoteDataValidade(List<Notafiscalentradaproduto> lista){
		if(lista==null || lista.isEmpty()){
			throw new WmsException("A lista não deve ser nula.");		}
			
		getJdbcTemplate().update("update Notafiscalentradaproduto  set lote=null, dtvalidade=null "+
				"where cdnotafiscalentradaproduto in (" +
				  CollectionsUtil.listAndConcatenate(lista, "cdnotafiscalentradaproduto", ",")
				  +") ");
		
		
	}
	
	/**
	 * Reseta a validade e o lote do produto
	 * @param notafiscalentradaproduto
	 * @author Cíntia Nogueira
	 */
	public void resetarLoteDataValidade(Notafiscalentradaproduto notafiscal){
		if(notafiscal==null || notafiscal.getCdnotafiscalentradaproduto()==null ){
			throw new WmsException("A lista não deve ser nula.");		}
			
		getJdbcTemplate().update("update Notafiscalentradaproduto  set lote=null, dtvalidade=null "+
				"where cdnotafiscalentradaproduto = " + notafiscal.getCdnotafiscalentradaproduto());
		
		
	}
	
	/**
	 * Método que a notafiscalentradaproduto
	 * 
	 * @param recebimento
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Notafiscalentradaproduto loadByRecebimentoProduto(Recebimento recebimento, Produto produto) {
		if((recebimento == null) || (recebimento.getCdrecebimento() == null) || produto==null || produto.getCdproduto()==null) {
			throw new WmsException("Os parâmetros não podem ser nulos.");
		}
		
		return query()
		            .select("notafiscalentradaproduto.cdnotafiscalentradaproduto,notafiscalentradaproduto.lote, notafiscalentradaproduto.dtvalidade,  " +
		            		"notafiscalentradaproduto.qtde, produto.cdproduto")
					.join("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
					.join("notafiscalentradaproduto.produto produto")
					.join("notafiscalentrada.listaRecebimentonotafiscalentrada recebimentonotafiscal")
					.join("recebimentonotafiscal.recebimento recebimento")
					.where("recebimento = ?", recebimento)		
					.where("produto=?", produto)
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * 
	 * Método que recupera uma lista de notas fiscais de entrada de produto pelas notas fiscais de entrada
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Notafiscalentradaproduto>
	 * 
	 */
	public List<Notafiscalentradaproduto> findByListaNotafiscalentrada(String listaNotafiscalentrada) {
		if(listaNotafiscalentrada == null)
			throw new WmsException("Parâmetros incorretos.");
		
		listaNotafiscalentrada = listaNotafiscalentrada.substring(0, listaNotafiscalentrada.length() - 1);
		
		return query()
					.joinFetch("notafiscalentradaproduto.produto produto")
					.joinFetch("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
					.joinFetch("notafiscalentrada.fornecedor fornecedor")
					.whereIn("notafiscalentradaproduto.notafiscalentrada", listaNotafiscalentrada)
					.list();
	}
	
	/**
	 * Altera a data de validade e o lote
	 * @param bean
	 * @author Cíntia Nogueira
	 */
	public void updateLoteValidade(Notafiscalentradaproduto bean){
		if(bean==null || bean.getCdnotafiscalentradaproduto()==null){
			throw new WmsException("O parâmetro não pode ser nulo.");
		}
		
		if(bean.getDtvalidade()!=null){
		
			getHibernateTemplate().bulkUpdate("update Notafiscalentradaproduto " +
					"set lote=? , dtvalidade=?" +			
					  "where cdnotafiscalentradaproduto =? " , 
					  new Object[]{bean.getLote(),bean.getDtvalidade(),
							  bean.getCdnotafiscalentradaproduto()});
		}else{
			getHibernateTemplate().bulkUpdate("update Notafiscalentradaproduto " +
					"set lote=? " +			
					  "where cdnotafiscalentradaproduto =? " , 
					  new Object[]{bean.getLote(),
							  bean.getCdnotafiscalentradaproduto()});
		}
	}
	
	
	/* singleton */
	private static NotafiscalentradaprodutoDAO instance;
	public static NotafiscalentradaprodutoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscalentradaprodutoDAO.class);
		}
		return instance;
	}
	
	public List<Notafiscalentradaproduto> findNotaProdutos(Notafiscalentrada notafiscalentrada){
		if (notafiscalentrada == null || notafiscalentrada.getCdnotafiscalentrada() == null)
			throw new WmsException("Parâmetro inválido. O documento de entrada é obrigatório.");
		
		return query()
		.select("notafiscalentradaproduto.cdnotafiscalentradaproduto, " +
				"produto.cdproduto, produto.codigo")
		.join("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
		.join("notafiscalentradaproduto.produto produto")
		.where("notafiscalentradaproduto.notafiscalentrada = ?", notafiscalentrada)
		.list();
	}
	
	/**
	 * 
	 * Método que recupera uma lista de notas fiscais de entrada de produto pelas notas fiscais de entrada
	 * 
	 * @author Filipe Santos
	 * 
	 * @param filtro
	 * @return List<Notafiscalentradaproduto>
	 * 
	 */
	public List<Notafiscalentradaproduto> findByListaNotafiscalAgendadas(String listaNotafiscalentrada) {
		if(listaNotafiscalentrada == null)
			throw new WmsException("Parâmetros incorretos.");
		
		listaNotafiscalentrada = listaNotafiscalentrada.substring(0, listaNotafiscalentrada.length() - 1);
		
		return query()
					.select("notafiscalentradaproduto.cdnotafiscalentradaproduto,notafiscalentradaproduto.qtde,notafiscalentradaproduto.valor," +
							"notafiscalentradaproduto.dtvalidade,notafiscalentradaproduto.lote,notafiscalentrada.cdnotafiscalentrada," +
							"produto.cdproduto,notafiscaltipo.exigeagenda,notafiscaltipo.cdnotafiscaltipo")
					.join("notafiscalentradaproduto.produto produto")
					.join("notafiscalentradaproduto.notafiscalentrada notafiscalentrada")
					.join("notafiscalentrada.notafiscaltipo notafiscaltipo")
					.join("notafiscalentrada.deposito deposito")
					.whereIn("notafiscalentradaproduto.notafiscalentrada.cdnotafiscalentrada", listaNotafiscalentrada)
					.where("notafiscaltipo.exigeagenda=1")
					.where("deposito = ?",WmsUtil.getDeposito())
					.list();				
	}

	/**
	 * 
	 * @param listaNotafiscalentrada
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Notafiscalentradaproduto> findProdutosByNotafiscal(String listaNotafiscalentrada) {
		String sentenca = " select sum(nfep.qtde) as qtde, p.cdproduto as cdproduto" +
						  " from notafiscalentradaproduto nfep " +
						  " join notafiscalentrada nfe on nfep.cdnotafiscalentrada = nfe.cdnotafiscalentrada " +
						  "	join produto p on p.cdproduto = nfep.cdproduto " +
						  " where nfe.cdnotafiscalentrada in ("+listaNotafiscalentrada+") "+
						  " and nfe.cddeposito = "+WmsUtil.getDeposito().getCddeposito()+" "+
						  " group by p.cdproduto ";
		
		List<Notafiscalentradaproduto> lista = getJdbcTemplate().query(sentenca,new RowMapper(){		
			public Object mapRow(java.sql.ResultSet rs, int currentItem) throws SQLException {
				Notafiscalentradaproduto nfep = new Notafiscalentradaproduto();
				nfep.setQtde(rs.getLong("qtde"));
				nfep.setProduto(new Produto(rs.getInt("cdproduto")));				
				return nfep;
			}
		});	
		
		return lista;
	}
}