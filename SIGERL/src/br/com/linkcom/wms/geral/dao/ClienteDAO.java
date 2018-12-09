package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.ClienteFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("cliente.nome")
public class ClienteDAO extends GenericDAO<Cliente> {

	@Override
	public void updateEntradaQuery(QueryBuilder<Cliente> query) {
		query.joinFetch("cliente.pessoanatureza");
		query.leftOuterJoinFetch("cliente.pessoaendereco");
	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Cliente> query,
			FiltroListagem _filtro) {
		ClienteFiltro filtro = (ClienteFiltro) _filtro;
		query.joinFetch("cliente.pessoanatureza pessoanatureza");
		query.whereLikeIgnoreAll("cliente.nome", filtro.getNome());
		query.where("cliente.ativo = ?", filtro.getAtivo());
		query.where("pessoanatureza = ?", filtro.getPessoanatureza());
	}
	
	/**
	 * M�todo que encontra um cliente pela pk
	 * @author Leonardo Guimar�es
	 * @param cd
	 * @return
	 */
	public Cliente findByCd(Integer cd){
		if(cd == null){
			throw new WmsException("O cd n�o deve ser nulo");
		}
		return query()
					.select("cliente.cdpessoa,cliente.nome,pessoanatureza.cdpessoanatureza,cliente.documento," +
							"cliente.ativo")
					.join("cliente.pessoanatureza pessoanatureza")
					.where("cliente.cdpessoa=?",cd)
					.unique();
	}

	/**
	 * Busca os clientes que se encontram na view de forma��o carga
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @return
	 */
	public List<Cliente> findByVformacaoCarga() {
		return
			query()
			.select("cliente.cdpessoa,cliente.nome")
			.join("cliente.listaFormacaoCarga vfc")
			.join("vfc.deposito deposito")
			.where("deposito = ?",WmsUtil.getDeposito())
			.orderBy("cliente.nome")
			.list()
			;
	}
	
	/**
	 * Encontra o cliente filial de entrega da ordem de servico
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param os
	 * @return
	 */
	public Cliente findFilialEntrega(Ordemservico os) {
		if(os == null || os.getCdordemservico() == null){
			throw new WmsException("A ordem de servi�o n�o deve ser nula");
		}
		
		QueryBuilder<Ordemservico> query = new QueryBuilder<Ordemservico>(getHibernateTemplate())
				.from(Ordemservico.class)
				.select("cliente.cdpessoa,cliente.nome")
				.join("ordemservico.clienteExpedicao cliente")
				.where("ordemservico = ?",os)
				.setMaxResults(1);
		
		Ordemservico ordemservico = query.unique();
		if (ordemservico != null)
			return ordemservico.getClienteExpedicao();
		else 
			return null;
	}

	/**
	 * Retorna os clientes com o documento
	 * @param documento
	 * @return
	 * @author C�ntia Nogueira
	 */
	public List<Cliente> findByDocumento(String documento){
		if(documento==null || documento.equals("")){
			throw new WmsException("O documento n�o pode ser nulo.");
		}
		
		return query()
				.select("cliente.cdpessoa, cliente.nome, cliente.documento")
				.where("cliente.documento=?", documento)
				.list();
	}

	/**
	 * Retorna cliente com seu endere�o
	 * @param cliente
	 * @return
	 * @author C�ntia Nogueira
	 */
	public Cliente loadWithEndereco(Cliente cliente){
		if(cliente==null || cliente.getCdpessoa()==null){
			throw new WmsException("O cliente n�o pode se nulo.");
		}
		return query()
				.select("cliente.cdpessoa, pessoaendereco.cdpessoaendereco, " +
						"pessoaendereco.logradouro, pessoaendereco.municipio," +
						"pessoaendereco.numero, pessoaendereco.uf, pessoaendereco.complemento")
						.join("cliente.pessoaendereco pessoaendereco")
						.where("cliente.cdpessoa=?", cliente.getCdpessoa())
				.unique();
	}
	
	/**
	 * M�todo que retorna todos os clientes do tipo filial que est�o ativos, ou 
	 * um cliente caso esteje editando e ele foi desativado.
	 * 
	 * @param cliente
	 * @return
	 * @author Tom�s Rabelo
	 */
	public List<Cliente> findAllFiliais(Cliente cliente) {
		return query()
			.select("cliente.cdpessoa, cliente.nome")
			.where("cliente.filial = ?", Boolean.TRUE)
			.openParentheses()
				.where("cliente.ativo = ?", Boolean.TRUE).or()
				.where("cliente = ?", cliente)
			.closeParentheses()
			.orderBy("cliente.nome")
			.list();
	}

	/**
	 * Localiza os nomes das filiais para o mapa de separa��o.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<String> findNomeFilialEntrega(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null){
			throw new WmsException("A ordem de servi�o n�o deve ser nula");
		}
		
		return new QueryBuilder<String>(getHibernateTemplate())
				.from(Ordemservico.class)
				.select("distinct filial.nome")
				.join("ordemservico.listaOrdemProdutoLigacao opl")
				.join("opl.ordemservicoproduto osp")
				.join("osp.listaEtiquetaexpedicao ee")
				.join("ee.carregamentoitem ci")
				.join("ci.pedidovendaproduto pvp")
				.join("pvp.filialEntrega filial")
				.where("ordemservico = ?", ordemservico)
				.orderBy("filial.nome")
				.setUseTranslator(false)
				.list();
		
	}

	/**
	 * Localiza os nomes dos clientes para o mapa de separa��o.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<String> findNomeCliente(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null){
			throw new WmsException("A ordem de servi�o n�o deve ser nula");
		}
		
		return new QueryBuilder<String>(getHibernateTemplate())
				.from(Ordemservico.class)
				.select("distinct cliente.nome")
				.join("ordemservico.listaOrdemProdutoLigacao opl")
				.join("opl.ordemservicoproduto osp")
				.join("osp.listaEtiquetaexpedicao ee")
				.join("ee.carregamentoitem ci")
				.join("ci.pedidovendaproduto pvp")
				.join("pvp.pedidovenda pv")
				.join("pv.cliente cliente")
				.where("ordemservico = ?", ordemservico)
				.orderBy("cliente.nome")
				.setUseTranslator(false)
				.list();
		
	}
	
	public Cliente findByCodigoERP(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP n�o deve ser nulo");
		}
		return query()
		.select("cliente.cdpessoa, cliente.nome, cliente.documento, " +
				"cliente.ativo, cliente.codigoerp ")
		.where("cliente.codigoerp = ? ", codigoERP)
		.unique();
	}
	
	public Cliente findByCodigoERPFilial1(Long codigoERP){
		if(codigoERP == null){
			throw new WmsException("O codigoERP n�o deve ser nulo");
		}
		return query()
		.select("cliente.cdpessoa, cliente.nome, cliente.documento, " +
		"cliente.ativo, cliente.codigoerp ")
		.where("cliente.codigoerp = ? ", codigoERP)
		.where("cliente.filial = 1 ")
		.unique();
	}

	/**
	 * 
	 * @return
	 */
	public List<Cliente> findFilialByDepositoLogado() {
		
		QueryBuilder<Cliente> query = query();
			
			query.select("cliente.cdpessoa, cliente.nome, cliente.codigoerp")
				.join("cliente.listaDepositofilial depositofilial")
				.where("cliente.filial = 1")
				.where("depositofilial.deposito = ?",WmsUtil.getDeposito());
		
		return query.list();
	}
	
	/**
	 * 
	 * @param filial
	 * @return
	 */
	public Cliente getCodigoerp(Cliente filial) {
		return query()
			.where("cliente = ?",filial)
			.setMaxResults(1)
			.unique();
	}
}
