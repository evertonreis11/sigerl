package br.com.linkcom.wms.geral.dao;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Pedidovendaproduto;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PedidovendaFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PedidovendaDAO extends GenericDAO<Pedidovenda> {

	@Override
	public void updateListagemQuery(QueryBuilder<Pedidovenda> query, FiltroListagem _filtro) {
		PedidovendaFiltro filtro = (PedidovendaFiltro) _filtro;		
		query
		.select("distinct pedidovenda.cdpedidovenda, pedidovenda.numero, pedidovendatipo.cdpedidovendatipo," +
				"pedidovendatipo.nome,cliente.cdpessoa, cliente.nome")
				.join("pedidovenda.cliente cliente")
				.join("pedidovenda.pedidovendatipo pedidovendatipo")
				.join("pedidovenda.listaPedidoVendaProduto listaPedidoVendaProduto")
				.where("cliente=?", filtro.getCliente())
				.whereLikeIgnoreAll("pedidovenda.numero", filtro.getNumero())
				.where("pedidovendatipo=?", filtro.getPedidovendatipo())
				.where("listaPedidoVendaProduto.deposito=?", WmsUtil.getDeposito())
				.ignoreJoin("listaPedidoVendaProduto");
		
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Pedidovenda> query) {
		query.select("pedidovenda.cdpedidovenda, pedidovenda.codigoerp," +
				"pedidovenda.dtemissao,pedidovenda.dtcancelamento,pedidovenda.dtlancamento," +
				"pedidovenda.numero,produto.cdproduto,produto.codigo,produto.descricao," +
				"pedidovendaproduto.cdpedidovendaproduto,pedidovendaproduto.qtde," +
				"pedidovendaproduto.valor,pedidovendaproduto.dtprevisaoentrega," +
				"pedidovendaproduto.carregado,pedidovendaproduto.numeronota," +
				"pedidovendatipo.cdpedidovendatipo,pedidovendatipo.nome," +
				"cliente.cdpessoa,cliente.nome,pessoaendereco.cdpessoaendereco," +
				"pessoaendereco.logradouro,pessoaendereco.numero,pessoaendereco.complemento," +
				"pessoaendereco.bairro,pessoaendereco.municipio,pessoaendereco.uf,pessoaendereco.cep")
		
			.join("pedidovenda.cliente cliente")
			.leftOuterJoin("pedidovenda.pedidovendatipo pedidovendatipo")
			.leftOuterJoin("cliente.pessoaendereco pessoaendereco")
			.leftOuterJoin("pedidovenda.listaPedidoVendaProduto pedidovendaproduto")
			.leftOuterJoin("pedidovendaproduto.produto produto");
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaPedidoVendaProduto");
	}
	
	/* singleton */
	private static PedidovendaDAO instance;
	public static PedidovendaDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(PedidovendaDAO.class);
		}
		return instance;
	}
	
	/**
	 * Busca os pedidos do tipo CD-CD-Entrega ou CD-CD-Transferência associados às notas do recebimento;
	 * 
	 * @author Giovane Freitas
	 * @param recebimento
	 */
	public List<Pedidovenda> findPedidosByRecebimento(Recebimento recebimento) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("É necessário informar um recebimento.");
		
		QueryBuilder<Pedidovenda> queryBuilder = query()
			.joinFetch("pedidovenda.listaPedidoVendaProduto pedidovendaproduto")
			.where("pedidovendaproduto.tipooperacao in (?, ?)", new Tipooperacao[]{Tipooperacao.ENTREGA_CD_CD_CLIENTE, Tipooperacao.TRANSFERENCIA_CD_CD_CLIENTE})
			.where("pedidovendaproduto.idnota in (select nfe.codigoerp from Notafiscalentrada nfe join nfe.listaRecebimentonotafiscalentrada rnf where rnf.recebimento = ? )", recebimento);

		return queryBuilder.list();
	}
	
	/**
	 * @author Leonardo Guimarães
	 * 
	 * Busca todos os pedidos de venda com
	 * o tipo de operação fornecido
	 * 
	 * @param praca
	 * @return
	 */
	public List<Pedidovenda> findByTipoOperacaoPraca(Tipooperacao tipooperacao, Praca praca, CarregamentoFiltro filtro){
		if(filtro == null)
			throw new WmsException("Os parâmetros não devem ser nulos.");
		return query()
						.select("pedidovenda.cdpedidovenda, pedidovenda.codigoerp, cliente.cdpessoa, cliente.nome")
						.leftOuterJoin("pedidovenda.cliente cliente")
						.leftOuterJoin("cliente.listaPessoaEndereco listaPessoaEndereco")
						.leftOuterJoin("pedidovenda.listaPedidoVendaProduto listaPedidoVendaProduto")
						.leftOuterJoin("listaPedidoVendaProduto.tipooperacao tipooperacao")
						.leftOuterJoin("listaPedidoVendaProduto.produto produto")
						.where("tipooperacao =?",tipooperacao)
						.where("pedidovenda.numero =?",filtro.getPedido())
						.whereLikeIgnoreAll("cliente.nome", filtro.getNomecliente())
						.where("listaPedidoVendaProduto.dtprevisaoentrega <= ?",filtro.getDtentregafim())
						.where("listaPedidoVendaProduto.dtprevisaoentrega >= ?",filtro.getDtentregainicio())
						.orderBy("cliente.nome,listaPessoaEndereco.cep")
						.list();
					
	}
	
	/**
	 * Verifica se o pedido de venda está relacionado a algum carregamento.
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param pedidovenda
	 * @return
	 */
	public Boolean isCarregado(Pedidovenda pedidovenda) {
		if (pedidovenda == null || pedidovenda.getCdpedidovenda() == null)
			return false;
		
		List<Pedidovendaproduto> list = new QueryBuilder<Pedidovendaproduto>(getHibernateTemplate())
			.from(Pedidovendaproduto.class)
			.select("pedidovendaproduto.cdpedidovendaproduto")
			.join("pedidovendaproduto.pedidovenda pedidovenda")
			.where("pedidovenda=?", pedidovenda)
			.where("pedidovendaproduto.carregado=true")
			.setUseTranslator(true)
			.list();

		if (list == null || list.size() <= 0)
			return false;
		else
			return true;
	}

	/**
	 * Cancela o {@link Pedidovenda}, definindo o atributo dtcancelamento com a data atual.
	 * 
	 * @author Giovane Freitas
	 * @param pedidovenda
	 */
	public void cancelar(Pedidovenda pedidovenda) {
		if (pedidovenda == null || pedidovenda.getCdpedidovenda() == null)
			throw new WmsException("Parâmetro inválido. O documento de saída é obrigatório.");
		
		getHibernateTemplate().bulkUpdate("update Pedidovenda pedidovenda set pedidovenda.dtcancelamento = ? where pedidovenda.id=? ", 
				new Object[]{new Date(System.currentTimeMillis()), pedidovenda.getCdpedidovenda()});
	}
	
	public Pedidovenda findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP não deve ser nulo");
		}
		return query()
		.select("pedidovenda.cdpedidovenda, pedidovenda.codigoerp")
		.where("pedidovenda.codigoerp = ? ", codigoERP)
		.unique();
	}

}