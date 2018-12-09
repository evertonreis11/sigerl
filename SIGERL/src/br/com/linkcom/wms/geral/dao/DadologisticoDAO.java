package br.com.linkcom.wms.geral.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipoendereco;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ProdutoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class DadologisticoDAO extends GenericDAO<Dadologistico>{
	
	/**
	 * Busca todos os dados logísticos deste produto no depósito logado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param produto
	 * @return
	 */
	public Dadologistico findByProduto(Produto produto,Deposito deposito) {
		if(produto == null || produto.getCdproduto() == null)
			throw new WmsException("O produto não deve ser nulo.");
		return query()
					.select("dadologistico.cddadologistico, dadologistico.normavolume, linhaseparacao.cdlinhaseparacao," +
							"linhaseparacao.nome, dadologistico.larguraexcedente, tipoendereco.cdtipoendereco, tipoendereco.nome," +
							"tipo.cdtipoendereco, tipo.nome,area.cdarea, area.nome,area.codigo, endereco.cdendereco,endereco.endereco," +
							"endereco.rua,endereco.predio,endereco.nivel,endereco.apto,areaE.cdarea, areaE.nome,areaE.codigo," +
							"dadologistico.capacidadepicking,dadologistico.pontoreposicao, tiporeposicao.cdtiporeposicao, " +
							"tiporeposicao.nome, descargarpreco.cddescargapreco,descargarpreco.nome,dadologistico.geracodigo," +
							"produto.cdproduto,deposito.cddeposito")
					.leftOuterJoin("dadologistico.linhaseparacao linhaseparacao")
					.leftOuterJoin("dadologistico.endereco endereco")
					.leftOuterJoin("endereco.area areaE")
					.leftOuterJoin("dadologistico.tipoendereco tipo")
					.leftOuterJoin("endereco.tipoendereco tipoendereco")
					.leftOuterJoin("dadologistico.area area")
					.leftOuterJoin("dadologistico.tiporeposicao tiporeposicao")
					.leftOuterJoin("dadologistico.descargapreco descargarpreco")
					.join("dadologistico.produto produto")
					.join("dadologistico.deposito deposito")
					.where("produto = ?",produto)
					.where("deposito = ?",deposito)
					.setMaxResults(1)
					.unique();
	}

	/**
	 * Atualiza o campo 'NormaVolume' de um determinado produto
	 * em todos os depósitos cadastrados.
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param normavolume
	 */
	public void updateNormavolume(Produto produto, boolean normavolume) {
		getHibernateTemplate().bulkUpdate("update " + Dadologistico.class.getName() + 
				" d set d.normavolume = ? where d.produto.id = ? ", 
				new Object[]{normavolume, produto.getCdproduto()});
	}

	public SqlRowSet getDadosExportacao(ProdutoFiltro filtro, Deposito deposito, Integer primeiroProduto, Integer ultimoProduto) {
		StringBuilder sql = new StringBuilder();

		List<Object> args = new ArrayList<Object>();
		
		sql.append("select * from ( select topn.*, ROWNUM rnum from ( ");
		sql.append("select p.codigo as \"Código\", p.descricao as \"Descrição\", p.altura as \"Altura\", p.largura as \"Largura\",  ");
		sql.append("  p.profundidade as \"Profundidade\", p.peso as \"Peso\", e.endereco as \"Picking\", ");
		sql.append("  ls.nome as \"Linha de separação\", ");
		sql.append("  tp.nome AS \"Tipo de palete\", ptp.lastro AS \"Lastro\", ptp.camada AS \"Camada\", ");
		sql.append("  te.nome AS \"Tipo de estrutura\", pte.restricaonivel AS \"Restrição de nível\", ");
		sql.append("  case when dl.larguraexcedente = 1 then 'Sim' else 'Não' end AS \"Largura excedente\", 'Não' AS \"Usa norma do volume\", ");
		sql.append("  tePulmao.nome AS \"Tipo de endereço do pulmão\", tePick.nome AS \"Tipo de endereço do picking\", ");
		sql.append("  a.codigo as \"Área\", dl.capacidadepicking AS \"Capacidade do picking\", dl.pontoreposicao AS \"Ponto de reposição\", ");
		sql.append("  tr.nome AS \"Tipo de reposição\", pcb.codigo AS \"Código de barras\", case when pcb.interno = 1 then 'Sim' else 'Não' end AS \"Interno\", ");
		sql.append("  pe.descricao as \"Embalagem\", pe.qtde as \"Qtde. por embalagem\", case when pe.compra = 1 then 'Sim' else 'Não' end as \"Emb. de recebimento\" ");
		sql.append("from produto p ");
		sql.append("  join dadologistico dl on dl.cdproduto = p.cdproduto ");
		sql.append("  left join endereco e on dl.cdendereco = e.cdendereco ");
		sql.append("  left join produto v on v.cdprodutoprincipal = p.cdproduto and v.cdproduto is null ");
		sql.append("  left join linhaseparacao ls on ls.cdlinhaseparacao = dl.cdlinhaseparacao ");
		sql.append("  left join produtotipopalete  ptp ON ptp.cdproduto = p.cdproduto and ptp.cddeposito = dl.cddeposito ");
		sql.append("  left join produtotipoestrutura pte ON pte.cdproduto = p.cdproduto and pte.cddeposito = dl.cddeposito ");
		sql.append("  left join tipopalete tp ON tp.cdtipopalete = ptp.cdtipopalete and ptp.cddeposito = dl.cddeposito ");
		sql.append("  left join tipoestrutura te ON te.cdtipoestrutura = pte.cdtipoestrutura ");
		sql.append("  left join tipoendereco tePulmao ON tePulmao.cdtipoendereco = dl.cdtipoendereco ");
		sql.append("  left join tipoendereco tePick ON tePick.cdtipoendereco = e.cdtipoendereco ");
		sql.append("  left join area a ON a.cdarea = e.cdarea ");
		sql.append("  left join tiporeposicao tr ON tr.cdtiporeposicao = dl.cdtiporeposicao ");
		sql.append("  left join produtocodigobarras pcb ON pcb.cdproduto = p.cdproduto ");
		sql.append("  left join produtoembalagem pe on pe.cdproduto = p.cdproduto ");
		sql.append("where p.cdprodutoprincipal is null and dl.normavolume = 0 and dl.cddeposito = ? ");
		args.add(deposito.getCddeposito());
		if (filtro.getLinhaseparacao() != null && filtro.getLinhaseparacao().getCdlinhaseparacao() != null){
			sql.append("  and ls.cdlinhaseparacao = ? ");
			args.add(filtro.getLinhaseparacao().getCdlinhaseparacao());
		}
		if (filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){
			sql.append("  and upper(p.codigo) like '%'||?||'%' ");
			args.add(filtro.getCodigo().toUpperCase());
		}
		if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()){
			sql.append("  and upper(tiraacento(p.descricao)) like '%'||?||'%' ");
			args.add(Util.strings.tiraAcento(filtro.getDescricao().toUpperCase()));
		}
		if (filtro.getReferencia() != null && !filtro.getReferencia().isEmpty()){
			sql.append("  and upper(tiraacento(p.referencia)) like '%'||?||'%' ");
			args.add(Util.strings.tiraAcento(filtro.getReferencia().toUpperCase()));
		}
		if (filtro.getProdutoclasse() != null && filtro.getProdutoclasse().getCdprodutoclasse() != null){
			sql.append("  and p.cdprodutoclasse = ? ");
			args.add(filtro.getProdutoclasse().getCdprodutoclasse());
		}
		sql.append("group by p.codigo, p.descricao, p.altura, p.largura, p.profundidade, p.peso, e.endereco, ls.nome, ");
		sql.append("  tp.nome, ptp.lastro, ptp.camada, te.nome, pte.restricaonivel,dl.larguraexcedente, dl.normavolume, ");
		sql.append("  tePulmao.nome, tePick.nome, a.codigo, dl.capacidadepicking, dl.pontoreposicao, tr.nome, ");
		sql.append("  pcb.codigo, pcb.interno,pe.descricao, pe.qtde, pe.compra ");

		sql.append("union all ");

		sql.append("select p.codigo, pp.descricao || ' - ' || p.descricao, p.altura, p.largura, p.profundidade, p.peso, eV.endereco, ");
		sql.append("  ls.nome, tp.nome, ptp.lastro, ptp.camada, te.nome, pte.restricaonivel, ");
		sql.append("  case when dlV.larguraexcedente = 1 then 'Sim' else 'Não' end,  ");
		sql.append("  'Sim', ");
		sql.append("  tePulmao.nome, tePick.nome, a.codigo, dlV.capacidadepicking, dlV.pontoreposicao, ");
		sql.append("  tr.nome, pcb.codigo, case when pcb.interno = 1 then 'Sim' else 'Não' end, ");
		sql.append("  pe.descricao, pe.qtde, case when pe.compra = 1 then 'Sim' else 'Não' end ");
		sql.append("from produto p ");
		sql.append("  join produto pp on p.cdprodutoprincipal = pp.cdproduto ");
		sql.append("  join dadologistico dlPP on dlPP.cdproduto = pp.cdproduto  ");
		sql.append("  join dadologistico dlV on dlV.cdproduto = p.cdproduto ");
		sql.append("  left join endereco eV on dlV.cdendereco = eV.cdendereco ");
		sql.append("  left join linhaseparacao ls on ls.cdlinhaseparacao = dlPP.cdlinhaseparacao ");
		sql.append("  left join produtotipopalete  ptp ON ptp.cdproduto = p.cdproduto and ptp.cddeposito = dlPP.cddeposito ");
		sql.append("  left join produtotipoestrutura pte ON pte.cdproduto = p.cdproduto and pte.cddeposito = dlPP.cddeposito ");
		sql.append("  left join tipopalete tp ON tp.cdtipopalete = ptp.cdtipopalete ");
		sql.append("  left join tipoestrutura te ON te.cdtipoestrutura = pte.cdtipoestrutura ");
		sql.append("  left join tipoendereco tePulmao ON tePulmao.cdtipoendereco = dlV.cdtipoendereco ");
		sql.append("  left join tipoendereco tePick ON tePick.cdtipoendereco = eV.cdtipoendereco ");
		sql.append("  left join area a ON a.cdarea = eV.cdarea ");
		sql.append("  left join tiporeposicao tr ON tr.cdtiporeposicao = dlV.cdtiporeposicao ");
		sql.append("  left join produtocodigobarras pcb ON pcb.cdproduto = p.cdproduto ");
		sql.append("  left join produtoembalagem pe on pe.cdproduto = p.cdproduto ");
		sql.append("where p.cdprodutoprincipal is not null and dlPP.Normavolume = 1  and dlPP.cddeposito = ? and dlV.cddeposito = ?  ");
		args.add(deposito.getCddeposito());
		args.add(deposito.getCddeposito());
		if (filtro.getLinhaseparacao() != null && filtro.getLinhaseparacao().getCdlinhaseparacao() != null){
			sql.append("  and ls.cdlinhaseparacao = ? ");
			args.add(filtro.getLinhaseparacao().getCdlinhaseparacao());
		}
		if (filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){
			sql.append("  and upper(pp.codigo) like '%'||?||'%' ");
			args.add(filtro.getCodigo().toUpperCase());
		}
		if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()){
			sql.append("  and upper(tiraacento(pp.descricao)) like '%'||?||'%' ");
			args.add(Util.strings.tiraAcento(filtro.getDescricao().toUpperCase()));
		}
		if (filtro.getReferencia() != null && !filtro.getReferencia().isEmpty()){
			sql.append("  and upper(tiraacento(pp.referencia)) like '%'||?||'%' ");
			args.add(Util.strings.tiraAcento(filtro.getReferencia().toUpperCase()));
		}
		if (filtro.getProdutoclasse() != null && filtro.getProdutoclasse().getCdprodutoclasse() != null){
			sql.append("  and pp.cdprodutoclasse = ? ");
			args.add(filtro.getProdutoclasse().getCdprodutoclasse());
		}
		sql.append("group by p.codigo, pp.descricao, p.descricao, p.altura, p.largura, p.profundidade, p.peso, eV.endereco, ");
		sql.append("  ls.nome, tp.nome, ptp.lastro, ptp.camada, te.nome, pte.restricaonivel, ");
		sql.append("  dlV.larguraexcedente, dlV.normavolume, tePulmao.nome, tePick.nome, a.codigo, ");
		sql.append("  dlV.capacidadepicking, dlV.pontoreposicao, tr.nome, pcb.codigo, pcb.interno, ");
		sql.append("  pe.descricao, pe.qtde, pe.compra ");
		sql.append("ORDER BY 8 desc  ");
		sql.append(") topn where ROWNUM <= ? ) where rnum  > ? ");
		args.add(ultimoProduto);
		args.add(primeiroProduto);
		
		return getJdbcTemplate().queryForRowSet(sql.toString(), args.toArray());		
	}

	/**
	 * Busca os dados logísticos com o endereço do tipo informado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param filtro
	 * @return
	 */
	public Dadologistico loadByTipoendereco(Tipoendereco filtro) {
		if((filtro == null) || (filtro.getCdtipoendereco() == null))
			throw new WmsException("O tipo de endereço não deve ser nulo.");
		
		return query()
			.where("dadologistico.tipoendereco = ?", filtro)
			.setMaxResults(1)
			.unique();
		
	}
	
	/**
	 * Encontra os dados logísticos dos produtos desse endereco
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @return
	 */
	public List<Dadologistico> findByEndereco(Endereco endereco) {
		return query()
					.select("dadologistico.cddadologistico,dadologistico.larguraexcedente")
					.join("dadologistico.produto produto")
					.join("dadologistico.deposito deposito")
					.join("produto.listaEnderecoproduto listaEnderecoproduto")
					.join("listaEnderecoproduto.endereco endereco")
					.where("deposito = ?",WmsUtil.getDeposito())
					.where("endereco = ?",endereco)
					.list()
					;
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosExportacaoPrincipal(ProdutoFiltro filtro){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select pr.codigo, pr.descricao, pr.altura, pr.largura, ");
		sql.append("        pr.profundidade, pr.cubagem, pr.peso, ep.descricao as TipoEmbalagem, ");
		sql.append("        pr.qtdevolumes as QtdVolumes, ls.nome as LinhaSeparacao, ");
		
		sql.append("       (select t3.nome ");
		sql.append("        from produtotipopalete l3  ");
		sql.append("        join tipopalete t3 on l3.cdtipopalete=t3.cdtipopalete ");
		sql.append("        where l3.cdproduto=dl.cdproduto ");
		sql.append("        and l3.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("		and l3.padrao=1 ) as TipoPalete, "); //TipoPalete
		
		sql.append("       (select l.lastro ");
		sql.append("        from produtotipopalete l ");
		sql.append("        where l.cdproduto=dl.cdproduto ");
		sql.append("        and l.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("        and l.padrao=1) as lastro, ");		//Lastro
		
		sql.append("       (select l1.camada ");
		sql.append("        from produtotipopalete l1 ");
		sql.append("        where l1.cdproduto=dl.cdproduto  ");
		sql.append("        and l1.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("        and l1.padrao=1) as camada, "); 	//Camada
		
		sql.append("       (select l2.lastro*l2.camada ");
		sql.append("        from produtotipopalete l2  ");
		sql.append("        where l2.cdproduto=dl.cdproduto  ");
		sql.append("        and l2.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("        and l2.padrao=1) as TotalPalete "); //TotalPalete
		
		sql.append(" from produtoembalagem ep, produto pr, dadologistico dl, linhaseparacao ls ");
		sql.append(" where pr.cdproduto = ep.cdproduto ");
		sql.append(" and   dl.cdproduto = pr.cdproduto ");
		sql.append(" and   ls.cdlinhaseparacao = dl.cdlinhaseparacao ");
		sql.append(" and   dl.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append(" and   pr.ativo=1 ");
		sql.append(" and   pr.cdprodutoprincipal is null ");
		sql.append(" and   ep.compra=1 ");
		sql.append(" order by descricao, codigo ");
		
		return getJdbcTemplate().queryForRowSet(sql.toString());		
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosExportacaoVolume(ProdutoFiltro filtro){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select a.codigo, b.descricao||' - '||a.descricao as descricao, ");
		sql.append("  		a.altura, a.largura, a.profundidade, a.cubagem, ");
		sql.append("  		a.peso, ep.descricao as TipoEmbalagem, ");
		sql.append("  		SubStr(a.complementocodigobarras,1,2)||'/'||SubStr(a.complementocodigobarras,3,4) as NumeroDoVolume, ");
		sql.append("  		ls.nome as LinhaSeparacao, ");
		
		sql.append("  	   (select t3.nome ");
		sql.append("  		from produtotipopalete l3, tipopalete t3 ");
		sql.append("  		where l3.cdproduto=a.cdproduto  ");
		sql.append(" 		and l3.cdtipopalete=t3.cdtipopalete ");
		sql.append("  		and l3.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("		and l3.padrao=1) as TipoPalete, "); //TipoPaletes
		
		sql.append("	   (select l.lastro ");
		sql.append("	    from produtotipopalete l  ");
		sql.append("	    where l.cdproduto=a.cdproduto  ");
		sql.append("	    and l.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("	    and l.padrao=1) as lastro, ");		//Lastro
		
		sql.append("	   (select l1.camada ");
		sql.append("	    from produtotipopalete l1  ");
		sql.append("	    where l1.cdproduto=a.cdproduto ");
		sql.append("	    and l1.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("		and l1.padrao=1) as camada, ");		//Camada

		sql.append("	   (select l2.lastro*l2.camada ");
		sql.append("	    from produtotipopalete l2 ");		
		sql.append("	    where l2.cdproduto=a.cdproduto ");
		sql.append("	    and l2.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append("	    and l2.padrao=1) as TotalPalete ");	//TotalPalete
		
		sql.append(" from produto a, produto b, produtoembalagem ep, linhaseparacao ls, dadologistico dl ");
		sql.append(" where b.cdproduto = a.cdprodutoprincipal ");
		sql.append(" and   ep.cdproduto = a.cdproduto ");
		sql.append(" and   dl.cdproduto = a.cdproduto ");
		sql.append(" and   ls.cdlinhaseparacao = dl.cdlinhaseparacao ");
		sql.append(" and   dl.cddeposito=").append(WmsUtil.getDeposito().getCddeposito());
		sql.append(" and   ep.compra=1 ");
		sql.append(" and   a.cdprodutoprincipal is not null ");
		sql.append(" order by descricao, codigo ");
		
		return getJdbcTemplate().queryForRowSet(sql.toString());		
	}	
	
}
