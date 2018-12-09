package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoendereco;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class OrdemservicoprodutoenderecoDAO extends GenericDAO<Ordemservicoprodutoendereco> {
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Ordemservicoprodutoendereco> query) {
		query.leftOuterJoinFetch("ordemservicoprodutoendereco.ordemservicoproduto osp")
			.leftOuterJoinFetch("osp.tipopalete tipopalete")
			.leftOuterJoinFetch("ordemservicoprodutoendereco.enderecodestino enderecodestino")
			.leftOuterJoinFetch("enderecodestino.area areadestino")
			.leftOuterJoinFetch("ordemservicoprodutoendereco.enderecoorigem enderecoorigem")
			.leftOuterJoinFetch("enderecoorigem.area areaorigem");
	}

	/**
	 * Faz a soma da quantidade do produto que ja foi endereçada para UMA
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param recebimento
	 * @param produto
	 * @return
	 */
	public Long getQtdeUMAEnderecado(Recebimento recebimento, Produto produto) {
		return newQueryBuilderWithFrom(Long.class)
				.select("sum(ordemservicoprodutoendereco.qtde)")
				.join("ordemservicoprodutoendereco.ordemservicoproduto osp")
				.join("osp.produto produto")
				.join("osp.listaOrdemprodutoLigacao opl")
				.join("opl.ordemservico os")
				.join("os.recebimento recebimento")
				.join("os.ordemtipo ordemtipo")
				.where("recebimento = ?",recebimento)
				.where("produto = ?",produto)
				.where("ordemtipo = ?",Ordemtipo.ENDERECAMENTO_PADRAO)
				.setUseTranslator(false)
				.unique();
	}
	
	/**
	 * Excluir todos os Ordemservicoprodutoendereco através da ordemservico produto.
	 * @author Pedro gonçalves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		if(listaOSP == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		getHibernateTemplate().bulkUpdate("delete Ordemservicoprodutoendereco ospe where ospe.ordemservicoproduto.id in ("+listaOSP+")");
	}
	
	/**
	 * Retorna a lista de produtos de endereços da ordem de servico
	 * @param ordemservico
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Ordemservicoprodutoendereco> findByOrdemServicoProduto(Ordemservicoproduto ordemservicoproduto){
		if(ordemservicoproduto==null || ordemservicoproduto.getCdordemservicoproduto()==null){
			throw new WmsException("Parametros não podem ser nulos em OrdemservicoprodutoenderecoDAO");
		}
		
		return query()
		.select("ordemservicoprodutoendereco.cdordemservicoprodutoendereco,ordemservicoprodutoendereco.qtde," +
						"enderecodestino.cdendereco, enderecodestino.endereco, enderecoorigem.cdendereco, enderecoorigem.endereco, " +
						"areaDes.cdarea,areaDes.codigo,areaOri.cdarea,areaOri.codigo,listaEnderecoprodutoOrigem.cdenderecoproduto," +
						"listaEnderecoprodutoOrigem.qtde,listaEnderecoprodutoOrigem.qtdereservadaentrada,listaEnderecoprodutoOrigem.qtdereservadasaida," +
						"listaEnderecoprodutoOrigem.dtentrada,listaEnderecoprodutoDestino.cdenderecoproduto,listaEnderecoprodutoDestino.qtde," +
						"listaEnderecoprodutoDestino.qtdereservadaentrada,listaEnderecoprodutoDestino.qtdereservadasaida,listaEnderecoprodutoDestino.dtentrada")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecoorigem enderecoorigem")
				.leftOuterJoin("enderecoorigem.area areaOri")	
				.leftOuterJoin("enderecodestino.area areaDes")	
				.leftOuterJoin("enderecoorigem.listaEnderecoproduto listaEnderecoprodutoOrigem")
				.leftOuterJoin("enderecodestino.listaEnderecoproduto listaEnderecoprodutoDestino")
				.where("ordemservicoprodutoendereco.ordemservicoproduto =?", ordemservicoproduto)
				.list();
		        
		       
		    
	}

	
	/**
	 * Lista para o relatorio de ordem de
	 * armazenagem
	 * @param recebimento
	 * @param cdOs
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Object[]> findForReportRecebimento(Recebimento recebimento,String cdOs) {
		if(recebimento == null || recebimento.getCdrecebimento() == null || cdOs == null || cdOs.equals(""))
			throw new WmsException("O recebimento e as ordens de serviço não devem ser nulas.");
		
		QueryBuilder<Object[]> query = newQueryBuilderWithFrom(Object[].class)
				.select("ordemservico.cdordemservico,box.cdbox," +
			 		 "enderecodestino.cdendereco,enderecodestino.endereco," +
			 		 "produto.cdproduto,produto.codigo,produto.descricao," +
			 		 "listaProdutoEmbalagem.cdprodutoembalagem,listaProdutoEmbalagem.descricao," +
			 		 "ordemservicoprodutoendereco.qtde,area.codigo,ordemservicoprodutoendereco.cdordemservicoprodutoendereco, " +
			 		 "ordemtipo.cdordemtipo")
			 		 .from(Ordemservicoprodutoendereco.class)	
			 		 .join("ordemservicoprodutoendereco.ordemservicoproduto ordemservicoproduto")			 		
			 		 .join("ordemservicoprodutoendereco.enderecodestino enderecodestino")
			 		 .join("enderecodestino.area area")
			 		 .join("area.listaEnderecosentido listaEnderecosentido")
			 		 .join("area.deposito deposito")
			 		  .join("deposito.listaRecebimento recebimento")
			 		 .join("recebimento.box box")
			 		.join("ordemservicoproduto.produto produto")
			 		.leftOuterJoin("produto.listaProdutoEmbalagem listaProdutoEmbalagem")
			 		.join("ordemservicoproduto.listaOrdemprodutoLigacao listaOrdemprodutoLigacao")			 		
			 		.join("listaOrdemprodutoLigacao.ordemservico ordemservico")	
			 		.join("ordemservico.ordemtipo ordemtipo")
				 .where("recebimento = ?",recebimento)
				 .where("ordemservico.cdordemservico in ("+cdOs+")")
				 //.where("listaProdutoEmbalagem.compra is true")
				 .where("ordemtipo.cdordemtipo in (8,9,10)")
				 .setUseTranslator(false)
				 .orderBy("area.codigo,enderecodestino.rua, (enderecodestino.predio*listaEnderecosentido.sentido)," +
				 		"enderecodestino.nivel,enderecodestino.apto");
		
		
		return query.list();
	}
	
	/**
	 * Retorna as Ordemservicoprodutoendereco da ordem de serviço
	 * @param ordem
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Ordemservicoprodutoendereco> findByOrdemServico(Ordemservico ordem){
		if(ordem==null || ordem.getCdordemservico()==null){
			throw new WmsException("Ordem não pode ser nula em OrdemservicoprodutoenderecoDAO");
		}
		
		return query()
				.select("ordemservicoprodutoendereco.cdordemservicoprodutoendereco,ordemservicoprodutoendereco.qtde," +
						"produto.cdproduto,produto.codigo, produto.descricao," +						
						"destino.cdendereco, destino.endereco,destino.apto, " +
						"destino.rua,destino.nivel, destino.predio,areaD.cdarea, areaD.codigo, " +
						"origem.cdendereco, origem.endereco,origem.apto, " +
						"origem.rua,origem.nivel, origem.predio,areaO.cdarea, areaO.codigo, " +
						"ordemservicoprodutoendereco.qtde, ordemservicoproduto.cdordemservicoproduto," +
						"ordemprodutostatus.cdordemprodutostatus,produtoprincipal.cdproduto," +
						"produtoprincipal.codigo,produtoprincipal.descricao," +
						"ordemservicoproduto.qtdeesperada")
				.join("ordemservicoprodutoendereco.ordemservicoproduto ordemservicoproduto")
				.join("ordemservicoproduto.listaOrdemprodutoLigacao listaOrdemprodutoLigacao")
				.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
				.join("listaOrdemprodutoLigacao.ordemservico ordemservico")
				.join("ordemservicoproduto.produto produto")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino destino")
				.leftOuterJoin("destino.area areaD")				
				.leftOuterJoin("ordemservicoprodutoendereco.enderecoorigem origem")
				.leftOuterJoin("origem.area areaO")			
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.where("ordemservico=?", ordem)
				.orderBy("produto.codigo")
				.list();
	}


	/**
	 * Busca os objetos {@link Ordemservicoprodutoendereco} de um determinado carregamento e 
	 * para um tipo específico de O.S.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 * @param mapaSeparacao
	 */
	public List<Ordemservicoprodutoendereco> findByCarregamento(Carregamento carregamento, Ordemtipo ordemtipo) {
		if(carregamento == null || carregamento.getCdcarregamento() == null){
			throw new WmsException("O Carregamento não deve ser nulo.");
		}
		
		return query().select("ordemservicoprodutoendereco.cdordemservicoprodutoendereco,ordemservicoprodutoendereco.qtde," +
				"enderecodestino.cdendereco,enderecoorigem.cdendereco,produto.cdproduto,produtoprincipal.cdproduto," +
				"ordemservicoproduto.cdordemservicoproduto")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecoorigem enderecoorigem")
				.join("ordemservicoprodutoendereco.ordemservicoproduto ordemservicoproduto")
				.join("ordemservicoproduto.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.join("ordemservicoproduto.listaOrdemprodutoLigacao ordemprodutoligacao")
				.where("ordemprodutoligacao.ordemservico.ordemtipo =?", ordemtipo)
				.where("ordemprodutoligacao.ordemservico.carregamento =?", carregamento)
			.list();
	}	
		
}
