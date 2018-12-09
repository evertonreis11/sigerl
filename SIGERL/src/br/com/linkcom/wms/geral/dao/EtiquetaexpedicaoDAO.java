/**
 * @author Giovane
 *
 */
package br.com.linkcom.wms.geral.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.vo.ConferenciaVolumeVO;
import br.com.linkcom.wms.geral.service.CarregamentoService;
import br.com.linkcom.wms.geral.service.ConfiguracaoService;
import br.com.linkcom.wms.geral.service.OrdemservicoService;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.LancarcorteFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EtiquetaprodutoseparacaoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.expedicao.EtiquetasProdutoSeparacaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EtiquetaexpedicaoDAO extends GenericDAO<Etiquetaexpedicao> {
	
	/**
	 * 
	 * Atualiza o campo quantidade coletor da etiqueta.
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param etiqueta
	 * 
	 */
	public void updateQtdecoletor(Etiquetaexpedicao etiqueta) {
		if((etiqueta == null) || (etiqueta.getCdetiquetaexpedicao() == null) || (etiqueta.getQtdecoletor() == null)) 
			throw new WmsException("Parâmetros inválidos.");
		
		getHibernateTemplate().bulkUpdate("update Etiquetaexpedicao etiqueta set etiqueta.qtdecoletor=? where etiqueta.id=? ", new Object[]{etiqueta.getQtdecoletor(), etiqueta.getCdetiquetaexpedicao()});
	}
	
	/**
	 * 
	 * Reseta a quantidade coletada.
	 * 
	 * @author Pedro Gonçalves
	 * 
	 * @param etiqueta
	 * 
	 */
	public void resetQtdecoletor(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		String hql = "update Etiquetaexpedicao etiqueta " +
						"set etiqueta.qtdecoletor = null " +
					"where etiqueta.ordemservicoproduto.id in (" +
							"select osp.id " +
							"from Ordemservicoproduto osp " +
								"left join osp.listaOrdemprodutoLigacao opl  " +
							"where opl.ordemservico.id = ?)";
		
		getHibernateTemplate().bulkUpdate(hql, ordemservico.getCdordemservico());
	}
	
	/**
	 * Busca os dados necessários para a emissão do relatório
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public List<EtiquetasProdutoSeparacaoVO> findForReport(EtiquetaprodutoseparacaoFiltro filtro) {
		if((filtro.getCarregamento() == null || filtro.getCarregamento().getCdcarregamento() == null) && (filtro.getExpedicao() == null || filtro.getExpedicao().getCdexpedicao() == null))
			throw new WmsException("Parâmetros inválidos.");
		
		Boolean caixaMestre = ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito());
		List<Integer> args = new ArrayList<Integer>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select ee.cdetiquetaexpedicao, ls.nome AS linhaseparacao, pv.numero as pedido, cli.nome AS cliente, "); 
		sql.append("	c.cdcarregamento, b.nome AS box, LPad(a.codigo, 2, '0') || '.' || e.endereco as endereco, v.descricao AS descricaovolume, ");
		sql.append("	p.descricao AS descricaoprincipal, v.codigo AS codigovolume, p.codigo AS codigoprincipal, ");
		sql.append("	V.complementocodigobarras, pe.bairro, pe.numero, pe.logradouro, pe.municipio, pe.uf, ");
		sql.append("	fe.nome AS filial, os.cdordemservico, pe.cdpessoaendereco ");
		
		if(caixaMestre){
			sql.append(" ,pem.cdprodutoembalagem ,pem.qtde as qtdeembalagem ");
		}
		
		sql.append("from ");
		sql.append("  ordemservico os ");
		sql.append("  inner join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico ");
		sql.append("  inner join ordemservicoproduto osp ON osp.cdordemservicoproduto = opl.cdordemservicoproduto ");
		
		if(caixaMestre){
			sql.append("  inner join produtoembalagem pem ON osp.cdprodutoembalagem = pem.cdprodutoembalagem ");	
		}
		
		sql.append("  inner join etiquetaexpedicao ee on ee.cdordemservicoproduto = osp.cdordemservicoproduto ");
		sql.append("  inner join Carregamentoitem ci on ee.cdcarregamentoitem = ci.cdcarregamentoitem ");
		sql.append("  inner join Carregamento c on ci.cdcarregamento = c.cdcarregamento ");
		sql.append("  inner join Box b on c.cdbox = b.cdbox ");
		sql.append("  inner join Pedidovendaproduto pvp on ci.cdpedidovendaproduto = pvp.cdpedidovendaproduto ");
		sql.append("  inner join Pedidovenda pv on pvp.cdpedidovenda = pv.cdpedidovenda ");
		sql.append("  inner join Pessoa cli on cli.cdpessoa = pv.cdcliente ");
		sql.append("  inner join Pessoa fe on fe.cdpessoa = pv.cdfilialemissao ");
		sql.append("  inner join Pessoaendereco pe on pvp.cdenderecoentrega = pe.cdpessoaendereco ");
		sql.append("  inner join Produto p on pvp.cdproduto = p.cdproduto ");
		sql.append("  inner join Dadologistico dl on p.cdproduto = dl.cdproduto and dl.cddeposito = os.cddeposito ");
		sql.append("  inner join Linhaseparacao ls on dl.cdlinhaseparacao = ls.cdlinhaseparacao ");
		sql.append("  inner join produto v ON v.cdproduto = osp.cdproduto ");
		sql.append("  inner join tipooperacao tpo on tpo.cdtipooperacao = pvp.cdtipooperacao ");
		sql.append("  left join Endereco e ON e.cdendereco = BUSCAR_ENDERECOETIQUETA_ID(ee.cdetiquetaexpedicao) ");
		sql.append("  left join area a ON a.cdarea = e.cdarea ");
		sql.append("  left join enderecosentido es on es.cdarea = e.cdarea and es.rua = e.rua ");
		sql.append("WHERE ");
		
		sql.append("  (os.cdordemtipo = ? or os.cdordemtipo = ?) ");
		args.add(Ordemtipo.CONFERENCIA_EXPEDICAO_1.getCdordemtipo());
		args.add(Ordemtipo.CONFERENCIA_CHECKOUT.getCdordemtipo());
		
		if (filtro.getExpedicao() != null && filtro.getExpedicao().getCdexpedicao() != null){
			if (filtro.getImpressaoOnda()){
				//Primeiro tenho que achar os mapas de separação, apenas eles tem ligação com todos os carregamentos
				List<Ordemservico> listaMapa = OrdemservicoService.getInstance().findMapaByOnda(filtro.getExpedicao());
				//Com a lista de carregamentos conseguirei buscar todas as etiquetas da geração em onda
				List<Carregamento> listaCarregamento = CarregamentoService.getInstance().findByOnda(Util.collections.listAndConcatenate(listaMapa, "cdordemservico", ","));
				sql.append("  and (c.cdcarregamento in (" + Util.collections.listAndConcatenate(listaCarregamento, "cdcarregamento", ",") + ")) ");
			} else {
				sql.append("  and (c.cdexpedicao = ? )");
				args.add(filtro.getExpedicao().getCdexpedicao());
			}
		} else {
			sql.append("  and (c.cdcarregamento = ? )");
			args.add(filtro.getCarregamento().getCdcarregamento());
			
			//Só validar o imprimeetiqueta quando for carregamento antigo, ou seja, que não possui expedição
			sql.append(" and (tpo.imprimeetiqueta = 1)");
		}
		
		if (ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.OPERACAO_EXPEDICAO_POR_BOX, WmsUtil.getDeposito())
				&& ConfiguracaoService.getInstance().isTrue(ConfiguracaoVO.QUEBRAR_POR_CARREGAMENTO, WmsUtil.getDeposito())){

			sql.append("ORDER BY c.cdcarregamento, ls.nome, a.codigo,(e.rua*es.sentido), e.nivel, e.apto, ee.cdetiquetaexpedicao "); 
		} else {
			sql.append("ORDER BY ls.nome, a.codigo,(e.rua*es.sentido), e.nivel, e.apto, ee.cdetiquetaexpedicao ");
		}

		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				EtiquetasProdutoSeparacaoVO etiqueta = new EtiquetasProdutoSeparacaoVO();
				etiqueta.setData(NeoFormater.getInstance().format(new Date(System.currentTimeMillis())));
				etiqueta.setBox(rs.getString("box"));
				etiqueta.setCdetiquetaexpedicao(rs.getInt("cdetiquetaexpedicao"));
				etiqueta.setCdpessoaendereco(rs.getInt("cdpessoaendereco"));
				etiqueta.setLinhaSeparacao(rs.getString("linhaseparacao"));
				
				if(ConfiguracaoService.getInstance().isTrue("UTILIZAR_CAIXA_MESTRE", WmsUtil.getDeposito()))
					etiqueta.setQtdeembalagem(rs.getLong("qtdeembalagem"));
				
				if(rs.getObject("filial") != null)
					etiqueta.setPedido(rs.getString("pedido") + " / " + rs.getString("filial"));
				else
					etiqueta.setPedido(rs.getString("pedido"));
				
				etiqueta.setCliente(rs.getString("cliente"));
				etiqueta.setCarga(rs.getInt("cdcarregamento"));
				etiqueta.setEnderecoPiking(rs.getString("endereco"));
				etiqueta.setCodigoDescricao(rs.getString("codigoprincipal") + " - " + rs.getString("descricaoprincipal"));
				
				if(rs.getObject("complementocodigobarras") != null){
					etiqueta.setVolume(rs.getString("complementocodigobarras").substring(0,2) + " / " + 
							rs.getString("complementocodigobarras").substring(2,4));
				}
				
				etiqueta.setEndereco(rs.getString("logradouro")+ ", " + rs.getString("numero") + ", " + 
						rs.getString("bairro")+ ", " + rs.getString("municipio") + "/" + rs.getString("uf"));
				
				etiqueta.setNomefilial(rs.getString("filial"));
				etiqueta.setOrdemservico(rs.getInt("cdordemservico"));
				
				return etiqueta;
			}
		});
	}
	
	/**
	 * Remove a associação de todas as etiquetas do carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamento
	 */
	public void removeEtiquetasCarregamento(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("O carregamento não deve ser nulo.");
		
		getHibernateTemplate().bulkUpdate("delete Etiquetaexpedicaogrupo ee where exists(" +
				"select ci.cdcarregamentoitem from Carregamentoitem ci " +
				"where ci.cdcarregamentoitem = ee.carregamentoitem.cdcarregamentoitem and " +
				"ci.carregamento = ?)",new Object[]{carregamento});

		getHibernateTemplate().bulkUpdate("delete Embalagemexpedicaoproduto eep where exists(" +
				"select ee.cdetiquetaexpedicao from Carregamentoitem ci, Etiquetaexpedicao ee " +
				"where ci.cdcarregamentoitem = ee.carregamentoitem.cdcarregamentoitem  " +
				"and eep.etiquetaexpedicao.cdetiquetaexpedicao = ee.cdetiquetaexpedicao " +
				"and ci.carregamento = ?)",new Object[]{carregamento});
		
		getHibernateTemplate().bulkUpdate("delete Etiquetaexpedicao ee where exists(" +
										"select ci.cdcarregamentoitem from Carregamentoitem ci " +
										"where ci.cdcarregamentoitem = ee.carregamentoitem.cdcarregamentoitem and " +
										"ci.carregamento = ?)",new Object[]{carregamento});
	}

	public String getEnderecoOrigem(Etiquetaexpedicao etiqueta) {
		return (String) getJdbcTemplate().queryForObject("select BUSCAR_ENDERECOETIQUETA(?) from dual", 
				new Object[]{etiqueta.getCdetiquetaexpedicao()}, String.class);
	}

	public List<Etiquetaexpedicao> findByOrdemservico(Ordemservico ordemservico, boolean conferenciaBox, LancarcorteFiltro filtro) {
		QueryBuilder<Etiquetaexpedicao> query = query()
			.joinFetch("etiquetaexpedicao.ordemservicoproduto osp")
			.joinFetch("osp.produto produto")
			.joinFetch("etiquetaexpedicao.carregamentoitem ci")
			.joinFetch("ci.pedidovendaproduto pvp")
			.joinFetch("pvp.pedidovenda pv")
			.joinFetch("pv.cliente cliente")
			.joinFetch("osp.listaOrdemprodutoLigacao opl")
			.leftOuterJoinFetch("osp.produtoembalagem produtoembalagem")
			.leftOuterJoinFetch("produto.produtoprincipal produtoprincipal")
			.leftOuterJoinFetch("etiquetaexpedicao.embalagemexpedicaoproduto eep")
			.leftOuterJoinFetch("eep.embalagemexpedicao eex")
			.where("opl.ordemservico = ?", ordemservico)
			.orderBy("etiquetaexpedicao.cdetiquetaexpedicao");
		
		if (conferenciaBox){
			query.where("etiquetaexpedicao.qtdecoletororiginal is not null")
				.where("etiquetaexpedicao.qtdecoletororiginal > 0");
		}
		
		if (filtro!=null && filtro.getVisualizacao()!=null){
			if(filtro.getVisualizacao()){
				query.openParentheses();
					query.where("etiquetaexpedicao.qtdecoletor is null");
					query.or();
					query.where("etiquetaexpedicao.qtdecoletor = 0");
				query.closeParentheses();
			}else{
				query.where("etiquetaexpedicao.qtdecoletor > 0");
			}
		}

		return query.list();
	}

	/**
	 * Localiza as quantidades que foram conferidas para cada volume de um dado produto.
	 * Quando o usuário realiza corte de um único volume de um produto o sistema localiza
	 * os outros volumes e define as etiquetas que serão cortadas de forma automática.
	 * 
	 * @author Giovane Freitas
	 * @param carregamentoitem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConferenciaVolumeVO> findConferenciaVolume(final Carregamentoitem carregamentoitem){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sql = new StringBuilder();
				
				sql.append("SELECT ci.cdcarregamentoitem AS cdcarregamentoitem, pvp.cdpedidovendaproduto AS cdpedidovendaproduto, pvp.produto.id AS cdproduto , ");
				sql.append("  osp.produto.id AS cdvolume, osp.qtdeesperada AS qtdeesperada, Sum(ee.qtdecoletor) AS qtdeconferida ");
				sql.append("FROM Etiquetaexpedicao ee ");
				sql.append("  join ee.carregamentoitem ci ");
				sql.append("  join ee.ordemservicoproduto osp ");
				sql.append("  join osp.listaOrdemprodutoLigacao opl ");
				sql.append("  join ci.pedidovendaproduto pvp ");
				sql.append("  join opl.ordemservico os ");
				sql.append("WHERE ");
				sql.append("  ee.carregamentoitem.id = ? ");
				sql.append("  AND os.ordemtipo.id IN (4,14) ");
				sql.append("GROUP BY ci.cdcarregamentoitem, pvp.cdpedidovendaproduto, pvp.produto.id, osp.produto.id, osp.qtdeesperada ");

				Query query = session.createQuery(sql.toString());
				query.setInteger(0, carregamentoitem.getCdcarregamentoitem());
				query.setResultTransformer(Transformers.aliasToBean(ConferenciaVolumeVO.class));
				
				return query.list();
			}
			
		});
	}

	/**
	 * Seleciona as etiquetas dos volumes para realizar o corte automático, identificando as etiquetas para o volume desejado e que pertencem
	 * ao mesmo carregamentoitem.
	 * 
	 * @param carregamentoitem
	 * @param volume
	 * @param numeroEtiquetas
	 * @return
	 */
	public List<Etiquetaexpedicao> findEtiquetasParaCorte(int cdcarregamentoitem, int cdvolume, int numeroEtiquetas) {
	
		QueryBuilder<Etiquetaexpedicao> query = query()
			.select("etiquetaexpedicao.cdetiquetaexpedicao,osp.cdordemservicoproduto," +
					"ci.cdcarregamentoitem,etiquetaexpedicao.qtdecoletor,etiquetaexpedicao.qtdecoletororiginal")
			.join("etiquetaexpedicao.ordemservicoproduto osp")
			.join("osp.produto produto")
			.join("etiquetaexpedicao.carregamentoitem ci")
			.join("ci.pedidovendaproduto pvp")
			.join("pvp.pedidovenda pv")
			.join("pv.cliente cliente")
			.join("osp.listaOrdemprodutoLigacao opl")
			.leftOuterJoin("produto.produtoprincipal produtoprincipal")
			.where("ci.id = ?", cdcarregamentoitem)
			.where("produto.id = ? ", cdvolume)
			.where("etiquetaexpedicao.qtdecoletor = 1")
			.orderBy("etiquetaexpedicao.cdetiquetaexpedicao")
			.setMaxResults(numeroEtiquetas);
		
		return query.list();
	}
	
	/**
	 * 
	 * @param whereIn
	 */
	public void deleteByCarregamentoWhereIn(String whereIn){
		
		if(whereIn == null || whereIn.isEmpty()) 
			throw new WmsException("Parâmetros inválidos.");

		StringBuilder sql = new StringBuilder();
		
			sql.append(" delete from etiquetaexpedicao ee ");
			sql.append(" where ee.cdcarregamentoitem in ");
			sql.append("	(select ci.cdcarregamentoitem ");
			sql.append("	 from carregamentoitem ci ");
			sql.append("	 where ci.cdcarregamentoitem = ee.cdcarregamentoitem ");
			sql.append("	 and ci.cdcarregamento in (?)) "); 
		
		getJdbcTemplate().update(sql.toString(), new Object[]{whereIn});		
	}

	/**
	 * 
	 * @param carregamento
	 */
	public void deleteByCarregamento(Carregamento carregamento){
		
		if(carregamento == null || carregamento.getCdcarregamento() == null) 
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();

			sql.append(" delete from etiquetaexpedicao ee ");
			sql.append(" where ee.cdcarregamentoitem in ");
			sql.append("	(select ci.cdcarregamentoitem ");
			sql.append("	 from carregamentoitem ci ");
			sql.append("	 where ci.cdcarregamentoitem = ee.cdcarregamentoitem ");
			sql.append("	 and ci.cdcarregamento = ?) ");
		
		getHibernateTemplate().bulkUpdate(sql.toString(), new Object[]{carregamento.getCdcarregamento()});
		
	}
	
}
