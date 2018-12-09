package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("tipooperacao.nome")
public class TipooperacaoDAO extends GenericDAO<Tipooperacao> {

	@Override
	public void updateListagemQuery(QueryBuilder<Tipooperacao> query, FiltroListagem _filtro) {
//		TipooperacaoFiltro filtro = (TipooperacaoFiltro) _filtro;
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Tipooperacao> query) {
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	

	/* singleton */
	private static TipooperacaoDAO instance;
	public static TipooperacaoDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(TipooperacaoDAO.class);
		}
		return instance;
	}
	
	/**
	 * Encontra todos os tipos de operação
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param tipooperacao
	 * @return
	 */
	public List<Tipooperacao> findAllForFlex() {
		return query()
				  .select("tipooperacao.cdtipooperacao, tipooperacao.nome")
				  .list();
	}
	
	/**
	 * Busca o tipo de operacao pelo cd
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param cdtipooperacao
	 * @return
	 */
	public Tipooperacao findByCd(Integer cdtipooperacao) {
		return query()
					.select("tipooperacao.cdtipooperacao,tipooperacao.separacliente,tipooperacao.nome")
					.where("tipooperacao.cdtipooperacao = ?",cdtipooperacao)
					.unique();
	}
	
	/**
	 * Busca o tipo de operação da ordem
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Tipooperacao findByOrdemServico(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de serviço não deve ser nula.");
		return query()
					.select("tipooperacao.cdtipooperacao, tipooperacao.nome")
					.join("tipooperacao.listaPedidoVendaProduto listaPedidoVendaProduto")
					.join("listaPedidoVendaProduto.listaCarregamentoitem listaCarregamentoitem")
					.join("listaCarregamentoitem.listaEtiquetaexpedicao listaEtiquetaexpedicao")
					.join("listaEtiquetaexpedicao.ordemservicoproduto osp")
					.join("osp.listaOrdemprodutoLigacao opl")
					.join("opl.ordemservico ordemservico")
					.where("ordemservico = ?",ordemservico)
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * Verifica se o tipo de operação imprime etiqueta de expedição
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param tipooperacao
	 * @return
	 */
	public Boolean imprimeEtiqueta(Tipooperacao tipooperacao) {
		if(tipooperacao == null || tipooperacao.getCdtipooperacao() == null)
			throw new WmsException("O tipo de operação não deve ser nulo.");
		return newQueryBuilderWithFrom(Boolean.class)
			   .select("tipooperacao.imprimeetiqueta")
			   .where("tipooperacao = ?",tipooperacao)
			   .setUseTranslator(false)
			   .unique();
	}

	/**
	 * Método que retorna o tipo operação Entrega CD Cliente e Transferencia Filial Entrega
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Tipooperacao> getTipoOperacaoEntregaClienteTransferenciaFilial() {
		return query()
			.select("tipooperacao.cdtipooperacao, tipooperacao.nome")
			.where("tipooperacao = ?", Tipooperacao.ENTREGA_CD_CLIENTE).or()
			.where("tipooperacao = ?", Tipooperacao.TRANSFERENCIA_FILIAL_ENTREGA)
			.list();
	}

	/**
	 * Retorna todos os tipos de operações dos pvp's. 
	 * 
	 * @param String listaPedidoVendaProduto (cdpedidovendaproduto)
	 * @author Filipe Santos
	 * @return
	 */
	public List<Tipooperacao> findByPedidovendaproduto(String listaPedidoVendaProduto) {
		return query()
			.select("tipooperacao.cdtipooperacao, tipooperacao.nome")
			.join("tipooperacao.listaPedidoVendaProduto listaPedidoVendaProduto")
			.whereIn("listaPedidoVendaProduto.cdpedidovendaproduto",listaPedidoVendaProduto)
			.list();
	}
	
	/* (non-Javadoc)
	 * @see br.com.linkcom.neo.persistence.GenericDAO#findForCombo(java.lang.String, java.lang.String[])
	 * 
	 * Sobrescrito devido a adoção do campo ativo para recuperação de objetos para os combos que utilizam o Tipo Operação.
	 * Everton Reis das Dores - 15/04/2016 
	 */
	@Override
	public List<Tipooperacao> findForCombo(String orderBy, String... extraFields) {
		
		return (List<Tipooperacao>) query()
				.select("tipooperacao.cdtipooperacao, tipooperacao.nome")
				.where("tipooperacao.ativo = ?",Boolean.TRUE)
				.orderBy(getOrderBy())
				.list();
	}
}