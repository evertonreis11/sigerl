package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Transferencia;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.TransferenciaFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class TransferenciaDAO extends GenericDAO<Transferencia> {	
	/**
	 * 
	 * Método que recupera as transferências de acordo com um filtro.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Transferencia>
	 * 
	 */
	public List<Transferencia> findByTransferencia(TransferenciaFiltro filtro) {
		if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.select("transferencia.cdtransferencia, transferencia.dttransferencia, transferenciastatus.cdtransferenciastatus, transferenciastatus.nome")
			.join("transferencia.transferenciastatus transferenciastatus")
			.join("transferencia.listaTransferenciaitem transferenciaitem")
			.join("transferenciaitem.produto produto")
			.join("transferenciaitem.enderecoorigem enderecoorigem")
			.join("enderecoorigem.area areaorigem")
			.join("transferenciaitem.enderecodestino enderecodestino")
			.join("enderecodestino.area areadestino")			
			.where("transferencia.dttransferencia >= ?", filtro.getDtinicial())
			.where("transferencia.dttransferencia <= ?", filtro.getDtfinal())			
			.where("transferenciastatus = ?", filtro.getTransferenciastatus())			
			.where("produto = ?", filtro.getProduto())
			.where("areaorigem.codigo = ?", filtro.getCodigoareaOrigem())
			.where("enderecoorigem.rua = ?", filtro.getRuaOrigem())
			.where("enderecoorigem.predio = ?", filtro.getPredioOrigem())
			.where("enderecoorigem.nivel = ?", filtro.getNivelOrigem())
			.where("enderecoorigem.apto = ?", filtro.getAptoOrigem())			
			.where("areadestino.codigo = ?", filtro.getCodigoareaDestino())
			.where("enderecodestino.rua = ?", filtro.getRuaDestino())
			.where("enderecodestino.predio = ?", filtro.getPredioDestino())
			.where("enderecodestino.nivel = ?", filtro.getNivelDestino())
			.where("enderecodestino.apto = ?", filtro.getAptoDestino())
			.orderBy("transferencia.cdtransferencia desc")
			.list();
	}
	
	/**
	 * 
	 * Método que recupera as transferências de acordo com um filtro.
	 * 
	 * @author Arantes
	 * @author Cíntia Nogueira
	 * 
	 * @param filtro
	 * @return List<Transferencia>
	 * 
	 */
	public List<Transferencia> findByTransferencia(TransferenciaFiltro filtro, String orderBy) {
		if(filtro == null || orderBy == null || orderBy.equals(""))
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.select("transferencia.cdtransferencia, transferencia.dttransferencia, transferenciastatus.cdtransferenciastatus, transferenciastatus.nome")
			.join("transferencia.transferenciastatus transferenciastatus")
			.join("transferencia.listaTransferenciaitem transferenciaitem")
			.join("transferenciaitem.produto produto")
			.join("transferenciaitem.enderecoorigem enderecoorigem")
			.join("enderecoorigem.area areaorigem")
			.join("transferenciaitem.enderecodestino enderecodestino")
			.join("enderecodestino.area areadestino")	
			.join("transferencia.deposito deposito with deposito.id = " + WmsUtil.getDeposito().getCddeposito())
			.where("transferencia.cdtransferencia=?", filtro.getCdtransferencia())
			.where("transferencia.dttransferencia >= ?", DateUtil.dataToBeginOfDay(filtro.getDtinicial()))
			.where("transferencia.dttransferencia <= ?", DateUtil.dataToEndOfDay(filtro.getDtfinal()))			
			.where("transferenciastatus = ?", filtro.getTransferenciastatus())			
			.where("produto = ?", filtro.getProduto())
			.where("areaorigem.codigo = ?", filtro.getCodigoareaOrigem())
			.where("enderecoorigem.rua = ?", filtro.getRuaOrigem())
			.where("enderecoorigem.predio = ?", filtro.getPredioOrigem())
			.where("enderecoorigem.nivel = ?", filtro.getNivelOrigem())
			.where("enderecoorigem.apto = ?", filtro.getAptoOrigem())			
			.where("areadestino.codigo = ?", filtro.getCodigoareaDestino())
			.where("enderecodestino.rua = ?", filtro.getRuaDestino())
			.where("enderecodestino.predio = ?", filtro.getPredioDestino())
			.where("enderecodestino.nivel = ?", filtro.getNivelDestino())
			.where("enderecodestino.apto = ?", filtro.getAptoDestino())
			.orderBy(orderBy)
			.list();
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaTransferenciaitem");
	}
	
	/**
	 * Atualiza o status da transferência
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param transferencia
	 */
	public void updateStatusTransferencia(Transferencia transferencia) {
		if(transferencia == null || transferencia.getCdtransferencia() == null || transferencia.getTransferenciastatus() == null || transferencia.getTransferenciastatus().getCdtransferenciastatus() == null)
			throw new WmsException("Não foi possível executar a função updateStatusTransferencia. Verifique se a transferência e se status não são nulos.");
		
		getHibernateTemplate().bulkUpdate("update Transferencia t set t.transferenciastatus = ? " +
										  "where t.cdtransferencia = ?",new Object[]{transferencia.getTransferenciastatus(),transferencia.getCdtransferencia()});
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Transferencia> query) {
		query
		.select("transferencia.cdtransferencia,transferencia.dttransferencia," +
						"transferenciastatus.cdtransferenciastatus,transferenciastatus.nome," +
						"listaTransferenciaitem.cdtransferenciaitem, listaTransferenciaitem.qtde,produto.cdproduto, " +
						"produto.descricao, produto.codigo, listaDadoLogistico.cddadologistico," +
						" listaDadoLogistico.larguraexcedente, listaProdutoEmbalagem.cdprodutoembalagem," +
						"listaProdutoEmbalagem.descricao,listaProdutoEmbalagem.qtde, enderecoorigem.cdendereco,enderecoorigem.endereco," +
						"enderecoorigem.larguraexcedente, areaorigem.cdarea,areaorigem.codigo, " +
						"transferencia2.cdtransferencia, enderecodestino.cdendereco,enderecodestino.endereco," +
						"areadestino.cdarea,areadestino.codigo,enderecofuncao.cdenderecofuncao,enderecofuncao.nome," +
						"efO.cdenderecofuncao,produtoprincipal.codigo,produtoprincipal.descricao")
				.join("transferencia.transferenciastatus transferenciastatus")
				.join("transferencia.listaTransferenciaitem listaTransferenciaitem")
				.join("listaTransferenciaitem.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.join("produto.listaDadoLogistico listaDadoLogistico with listaDadoLogistico.deposito.id=" + WmsUtil.getDeposito().getCddeposito())
				.join("produto.listaProdutoEmbalagem listaProdutoEmbalagem")
				.join("listaTransferenciaitem.enderecoorigem enderecoorigem")
				.join("enderecoorigem.enderecofuncao efO")
				.join("enderecoorigem.area areaorigem")
				.join("listaTransferenciaitem.enderecodestino enderecodestino")
				.join("enderecodestino.enderecofuncao enderecofuncao")
				.join("enderecodestino.area areadestino")
				.join("listaTransferenciaitem.transferencia transferencia2")
				.join("areaorigem.listaEnderecosentido listaEnderecosentido")
				.where("enderecoorigem.rua = listaEnderecosentido.rua")
				.orderBy("areaorigem.cdarea,(enderecoorigem.rua*listaEnderecosentido.sentido)," +
		 					"enderecoorigem.nivel,enderecoorigem.apto,produtoprincipal.descricao, produto.descricao");
	}
}
