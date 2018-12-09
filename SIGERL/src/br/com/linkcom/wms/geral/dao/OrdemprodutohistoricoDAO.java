package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoproduto;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Recebimentostatus;
import br.com.linkcom.wms.geral.bean.Tipopalete;
import br.com.linkcom.wms.geral.bean.vo.CodigobarrasVO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
import br.com.linkcom.wms.util.recebimento.RecebimentoPapelReportVO;

public class OrdemprodutohistoricoDAO extends GenericDAO<Ordemprodutohistorico>{
	/**
	 * Encontra a ordem de serviço a partir do recebimento e lista todos os seus
	 * produtos. 
	 *  
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Ordemprodutohistorico> findBy(Recebimento recebimento,Ordemservico ordemservico) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		return query()
			.select("produto.cdproduto, produto.codigoerp, produto.descricao,produto.codigo,produto.complementocodigobarras,produto.exigevalidade," +
					"produto.exigelote,	produtoprincipal.cdproduto, produtoprincipal.codigoerp, produtoprincipal.descricao,produtoprincipal.codigo," +
						"ordemservicoproduto.cdordemservicoproduto, otipopalete.cdtipopalete,ordemservico.cdordemservico, ordemservicoproduto.qtdeesperada, " +
						"ordemprodutohistorico.cdordemprodutohistorico,ordemprodutohistorico.qtde, ordemprodutohistorico.qtdeavaria," +
						"ordemprodutohistorico.qtdefracionada,listaProdutoTipoPalete.cdprodutotipopalete," +
						"listaProdutoTipoPalete.lastro, listaProdutoTipoPalete.camada, listaProdutoTipoPalete.padrao, tipopalete.cdtipopalete, " +
						"tipopalete.nome, codigobarras.codigo, produtoembalagem.cdprodutoembalagem, produtoembalagem.descricao")				
				.leftOuterJoin("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
				.leftOuterJoin("ordemservicoproduto.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.leftOuterJoin("ordemservicoproduto.tipopalete otipopalete")
				.leftOuterJoin("ordemprodutohistorico.ordemservico ordemservico")
				.leftOuterJoin("ordemprodutohistorico.produtoembalagem produtoembalagem")
				.leftOuterJoin("ordemservico.recebimento recebimento")
				.leftOuterJoin("recebimento.recebimentostatus recebimentostatus")
				.leftOuterJoin("produto.listaProdutoTipoPalete listaProdutoTipoPalete")
				.leftOuterJoin("produto.listaProdutoCodigoDeBarras codigobarras")
				.leftOuterJoin("listaProdutoTipoPalete.tipopalete tipopalete")
				.where("recebimento=?",recebimento)
				.openParentheses()
					.where("recebimentostatus=?",Recebimentostatus.EM_ANDAMENTO)
					.or()
					.where("recebimentostatus=?",Recebimentostatus.EM_ENDERECAMENTO)
					.or()
					.where("recebimentostatus=?",Recebimentostatus.ENDERECADO)
				.closeParentheses()
				.where("ordemservico=?",ordemservico)
				.openParentheses()
					.where("ordemservico is null")
					.or()
					.where("ordemservico=?",ordemservico)
				.closeParentheses()
				.orderBy("coalesce(produtoprincipal.descricao,produto.descricao),produto.codigo")
				.list();
	}
	
	/**
	 * Encontra a ordem de serviço a partir do recebimento e lista todos os seus
	 * produtos para o relatório.
	 * 
	 * Obs: Este método foi criado pois o hybernate não estava
	 * buscando mais de um item da lista na query do método {@link #findBy(Recebimento recebimento,Ordemservico ordemservico)} 
	 * 
	 * @author Leonardo Guimarães
	 * 		   Pedro Gonçalves - Migração para a nova versão do banco
	 * 		   Giovane Freitas - Alteração da consulta e do tipo de retorno, o modelo anterior estava lento e consumindo muita memória.
	 * 
	 * @param recebimento
	 * @param ordemservico
	 * @return
	 */
	public List<RecebimentoPapelReportVO> findForPapelReport(Recebimento recebimento, Ordemservico ordemservico){
		StringBuilder sql = new StringBuilder();

		sql.append(" select os.cdordemservico, nvl(pp.codigo,p.codigo) as codigo, nvl(pp.descricao,p.descricao) as descricao,p.exigevalidade as exigevalidade, p.exigelote as exigelote, ");
		sql.append("   case when ptpv.cdprodutotipopalete is not null then ");
		sql.append("     1 ");
		sql.append("   else ");
		sql.append("     0 end as usanormavolume, ");
		sql.append("   case when ptpv.cdprodutotipopalete is null then ");
		sql.append("     tp.nome || '  ' || ptp.lastro || case when ptp.cdprodutotipopalete is not null then ' X ' end || ptp.camada ");
		sql.append("   else ");
		sql.append("     tpv.nome || '  ' || ptpv.lastro || case when ptpv.cdprodutotipopalete is not null then ' X ' end || ptpv.camada ");
		sql.append("   end as norma, case when p.cdprodutoprincipal is not null then p.codigo end as codigovolume, ");
		sql.append("   dl.normavolume,case when p.cdprodutoprincipal is not null then p.descricao end as descricaoVolume, ");
		sql.append("   ptpv.cdprodutotipopalete ");
		sql.append(" from recebimento r ");
		sql.append("   join ordemservico os on os.cdrecebimento =r.cdrecebimento ");
		sql.append("   join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico ");
		sql.append("   join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto ");
		sql.append("   join produto p on osp.cdproduto = p.cdproduto ");
		sql.append("   left join dadologistico dl on dl.cdproduto = p.cdprodutoprincipal and dl.cddeposito = " + 
				WmsUtil.getDeposito().getCddeposito());
		sql.append("   left join produto pp on p.cdprodutoprincipal = pp.cdproduto ");
		sql.append("   left join produtotipopalete ptp on ptp.cdproduto = pp.cdproduto and ptp.cddeposito = " + 
				WmsUtil.getDeposito().getCddeposito() +
				" and (dl.normavolume is null or dl.normavolume = 0) ");
		sql.append("   left join tipopalete tp on ptp.cdtipopalete = tp.cdtipopalete ");
		sql.append("   left join produtotipopalete ptpv on ptpv.cdproduto = p.cdproduto and ptpv.cddeposito = " +
				 + WmsUtil.getDeposito().getCddeposito() +
				" and (dl.normavolume is null or dl.normavolume = 1) ");
		sql.append("   left join tipopalete tpv on ptpv.cdtipopalete = tpv.cdtipopalete ");
		sql.append(" where r.cdrecebimento = " + recebimento.getCdrecebimento());
		sql.append("   and r.cdrecebimentostatus = " + Recebimentostatus.EM_ANDAMENTO.getCdrecebimentostatus());
		sql.append("   and os.cdordemservico = " + ordemservico.getCdordemservico());
		sql.append(" order by nvl(pp.descricao,p.descricao),p.codigo ");

		@SuppressWarnings("unchecked")
		List<RecebimentoPapelReportVO> resultado = (List<RecebimentoPapelReportVO>) getJdbcTemplate().query(sql.toString(), new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				
				ArrayList<RecebimentoPapelReportVO> listaVO = new ArrayList<RecebimentoPapelReportVO>();
				
				String produtoAnterior = null;
				String volumeAnterior = null;
				RecebimentoPapelReportVO itemAnterior = null;
				boolean primeiroVolume = true;
				
				while (rs.next()){
					String norma;
					if (rs.getObject("cdprodutotipopalete") != null && rs.getObject("descricaoVolume") != null)
						norma = rs.getString("descricaoVolume").concat(": ").concat(rs.getString("norma"));
					else
						norma = rs.getString("norma");
					
					if (!rs.getString("codigo").equals(produtoAnterior)){
						RecebimentoPapelReportVO item = new RecebimentoPapelReportVO();
						item.setCodigo(rs.getString("codigo"));
						item.setDescricao(rs.getString("descricao"));
						item.setNorma(norma);
						item.setExigevalidade(rs.getBoolean("exigevalidade"));
						item.setExigelote(rs.getBoolean("exigelote"));
						listaVO.add(item);

						itemAnterior = item;
						primeiroVolume = true;
						volumeAnterior = rs.getString("codigovolume");
					}else{						
						if (volumeAnterior != null && !volumeAnterior.equals(rs.getString("codigovolume")))
							primeiroVolume = false;
						
						//quando não usa norma de volume somente concateno as normas do primeiro volume, para não duplicar
						if (primeiroVolume || rs.getInt("usanormavolume") == 1)
							itemAnterior.setNorma(itemAnterior.getNorma().concat("\n").concat(norma));

						volumeAnterior = rs.getString("codigovolume");
					}
					itemAnterior.setExigevalidade(rs.getBoolean("exigevalidade"));
					itemAnterior.setExigelote(rs.getBoolean("exigelote"));
					produtoAnterior = rs.getString("codigo");
				}
				
				return listaVO;
			}
			
		});
		
		return resultado;
	}
	
	/**
	 * Atualiza a quantidade do histórico da ordem de produto
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemprodutohistorico
	 */
	public void atualizarQuantidades(Ordemprodutohistorico ordemprodutohistorico) {
		if(ordemprodutohistorico == null || ordemprodutohistorico.getCdordemprodutohistorico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		Object[] parametros = new Object[4];
		parametros[0] = ordemprodutohistorico.getQtde() == null ? 0 : ordemprodutohistorico.getQtde();
		parametros[1] = ordemprodutohistorico.getQtdeavaria() == null ? 0 : ordemprodutohistorico.getQtdeavaria();
		parametros[2] = ordemprodutohistorico.getQtdefracionada() == null ? 0 : ordemprodutohistorico.getQtdefracionada();
		parametros[3] = ordemprodutohistorico.getCdordemprodutohistorico() == null ? 0 : ordemprodutohistorico.getCdordemprodutohistorico();
		
		String produtoembalagem = ordemprodutohistorico.getProdutoembalagem() == null || ordemprodutohistorico.getProdutoembalagem().getCdprodutoembalagem() == null ? null : ordemprodutohistorico.getProdutoembalagem().getCdprodutoembalagem().toString();
		
		getHibernateTemplate().bulkUpdate("update Ordemprodutohistorico ordemprodutohistorico set ordemprodutohistorico.qtde=?, " +
										  "ordemprodutohistorico.qtdeavaria=?, ordemprodutohistorico.qtdefracionada = ?, ordemprodutohistorico.produtoembalagem = " +produtoembalagem +
										  " where ordemprodutohistorico.id=? ", parametros);
	}
	
	/**
	 * Recupera o registro de ordem produto histórico de maior id
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @param Ordemprodutohistorico
     *
	 */
	public Ordemprodutohistorico findMaxByOrdemservicoproduto(Ordemservicoproduto filtro) {
		if((filtro == null) || (filtro.getCdordemservicoproduto() == null))
			throw new WmsException("O filtro não deve ser nulo");
	
		return query()
					.where("ordemprodutohistorico.cdordemprodutohistorico = (" +
							"select max(oph.cdordemprodutohistorico) " +
							"from Ordemprodutohistorico oph " +
							"where oph.ordemservicoproduto = ?)", filtro)
					.unique();
	}
	
	/**
	 * Encontra a ordem de serviço a partir do recebimento e lista todos os seus
	 * produtos. 
	 *  
	 * @param recebimento
	 * @return
	 * @author Leonardo Guimarães
	 * 		   Pedro Gonçalves - Modificação feita devido alteração da base de dados.
	 */
	public List<Ordemprodutohistorico> findForPopUp(Recebimento recebimento, Ordemservico ordemservico, Long ultimaOs) {
		if ((recebimento == null) || (recebimento.getCdrecebimento() == null) ||
			(ordemservico == null) || (ordemservico.getCdordemservico() == null) || 
			(ultimaOs== null))
			throw new WmsException("Parâmetros incorretos.");
		
		QueryBuilder<Ordemprodutohistorico> sql = 
			query()
				.select("produto.cdproduto, produto.codigoerp, produto.descricao, produto.codigo,produto.complementocodigobarras, " +
						"produtoprincipal.cdproduto, produtoprincipal.codigoerp, produtoprincipal.descricao, produtoprincipal.codigo, " +
						"os.cdordemservico, os.ordem," +
						"osp.cdordemservicoproduto, osp.qtdeesperada, " +
						"oph.qtde, oph.qtdeavaria, oph.qtdefracionada")
				.from(Ordemprodutohistorico.class, "oph")
				.leftOuterJoin("oph.ordemservico os")
				.leftOuterJoin("os.recebimento recebimento")
				.leftOuterJoin("oph.ordemservicoproduto osp")
				.leftOuterJoin("osp.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.where("recebimento=?", recebimento);
		
		if(!ultimaOs.equals(new Long(ordemservico.getCdordemservico().toString()))) {
			sql.where("os = ?", ordemservico);
			
		} else {
			sql.where("os.ordem = (select max(os2.ordem) " +
					   "            from Ordemprodutohistorico oph2 " +
					   "            join oph2.ordemservico os2 " +
					   "            join oph2.ordemservicoproduto osp2 " +
					   "            join osp2.produto produto2 " +
					   "            where produto2.cdproduto = produto.cdproduto" +
					   "              and os2.cdordemservico = os.cdordemservico)");
		}
		
		sql.orderBy("os.ordem, produto.codigo");
		return sql.list();
		
	}
	
	/**
	 * Encontra a ordem de serviço a partir do recebimento e lista todos os seus
	 * produtos. Pega sempre a última ordem de serviço de conferência do recebimento.
	 *  
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 */
	public List<Ordemprodutohistorico> findByForRF(Recebimento recebimento) {
		if (recebimento == null || recebimento.getCdrecebimento() == null)
			throw new WmsException("Parâmetros incorretos.");
		
		List<Recebimentostatus> lista = new ArrayList<Recebimentostatus>();
		lista.add( Recebimentostatus.DISPONIVEL);
		lista.add( Recebimentostatus.EM_ANDAMENTO);
		lista.add( Recebimentostatus.EM_ENDERECAMENTO);
		lista.add( Recebimentostatus.ENDERECADO);

		QueryBuilder<Integer> subQuery = newQueryBuilderWithFrom(Integer.class);
		subQuery.select("max(os.cdordemservico)");
		subQuery.from(Ordemservico.class,"os");
		subQuery.join("os.recebimento r");
		subQuery.where("r=recebimento");
		subQuery.openParentheses()
				.where("os.ordemtipo.id=" + Ordemtipo.CONFERENCIA_RECEBIMENTO.getCdordemtipo())
				.or()
				.where("os.ordemtipo.id=" + Ordemtipo.RECONFERENCIA_RECEBIMENTO.getCdordemtipo())
			.closeParentheses();

		QueryBuilder<Ordemprodutohistorico> queryBuilder = query()
				.joinFetch("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
				.joinFetch("ordemservicoproduto.produto produto")
				.joinFetch("ordemprodutohistorico.ordemservico ordemservico")
				.joinFetch("ordemservico.recebimento recebimento")
				.joinFetch("recebimento.recebimentostatus recebimentostatus")
				
				//Tirei estes joins porque estava estourando memória e como o produto é recarregado ao ser bipado, não fará diferença
				//.leftOuterJoinFetch("produto.produtoprincipal produtoprincipal")
				//.leftOuterJoinFetch("produto.listaProdutoTipoPalete listaProdutoTipoPalete")
				//.leftOuterJoinFetch("produto.listaProdutoEmbalagem listaProdutoEmbalagem")

				.leftOuterJoinFetch("produto.listaProdutoCodigoDeBarras codigobarras")
				.leftOuterJoinFetch("codigobarras.produtoembalagem produtoembalagem")
				.where("recebimento=?",recebimento)
				.whereIn("recebimentostatus",CollectionsUtil.listAndConcatenate(lista, "cdrecebimentostatus", ","))
				.where("ordemservico=?",subQuery)
				.orderBy("ordemprodutohistorico.cdordemprodutohistorico");

		return queryBuilder.setUseTranslator(true).list();
	}
	
	/**
	 * Carrega o histórico a partir de uma ordem de conferência.
	 *  
	 * @param recebimento
	 * @return
	 * @author Pedro Gonçalves
	 * 		   Giovane Freitas
	 * @param conferenciaBox 
	 */
	public List<Ordemprodutohistorico> findByForRF(Ordemservico ordemservico, boolean conferenciaBox) {
		if (ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros incorretos.");

		QueryBuilder<Object[]> queryBuilder = new QueryBuilder<Object[]>(getHibernateTemplate())
				.from(Ordemprodutohistorico.class)
				.select("ordemprodutohistorico.cdordemprodutohistorico,ordemprodutohistorico.qtde," +
						"ordemservicoproduto.cdordemservicoproduto,ordemprodutostatus.cdordemprodutostatus," +
						"tipopalete.cdtipopalete," +
						"ordemservicoproduto.qtdeesperada,etiquetaexpedicao.cdetiquetaexpedicao," +
						"etiquetaexpedicao.qtdecoletor,produto.cdproduto,produto.descricao,produto.codigo," +
						"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao," +
						"produto.complementocodigobarras,produtoembalagem.cdprodutoembalagem,produtoembalagem.qtde")
				.leftOuterJoin("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
				.leftOuterJoin("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
				.leftOuterJoin("ordemservicoproduto.tipopalete tipopalete")
				.leftOuterJoin("ordemservicoproduto.produto produto")
				.leftOuterJoin("produto.produtoprincipal produtoprincipal")
				.leftOuterJoin("ordemservicoproduto.listaEtiquetaexpedicao etiquetaexpedicao")
				.leftOuterJoin("ordemservicoproduto.produtoembalagem produtoembalagem")
				.where("ordemprodutohistorico.ordemservico = ?",ordemservico)
				.orderBy("produto.descricao");
		
		if (conferenciaBox){
			queryBuilder.where("etiquetaexpedicao.qtdecoletororiginal is not null")
				.where("etiquetaexpedicao.qtdecoletororiginal > 0");
		}
		
		//Por causa de um bug no Neo vou fazer a conversão em objetos manualmente.
		List<Object[]> list = queryBuilder.setUseTranslator(false).list();
				
		Map<Integer, Ordemprodutohistorico> mapOPH = new HashMap<Integer, Ordemprodutohistorico>();
		Map<Integer, Ordemprodutostatus> mapOPS = new HashMap<Integer, Ordemprodutostatus>();
		Map<Integer, Tipopalete> mapTP = new HashMap<Integer, Tipopalete>();
		Map<Integer, Produto> mapP = new HashMap<Integer, Produto>();
		
		List<Ordemprodutohistorico> resultado = new ArrayList<Ordemprodutohistorico>();
		for (Object[] tupla : list){
			Ordemprodutohistorico oph;
			if (mapOPH.containsKey(tupla[0]))
				oph = mapOPH.get(tupla[0]);
			else{
				oph = new Ordemprodutohistorico((Integer)tupla[0]);
				oph.setQtde((Long) tupla[1]);
				mapOPH.put((Integer)tupla[0], oph);
			}
			
			Ordemservicoproduto osp;
			Produtoembalagem pe;
			if (oph.getOrdemservicoproduto() == null){
				osp = new Ordemservicoproduto((Integer) tupla[2]);
				//vincula o produto emabalgem a ordemservicoprduto
				if(tupla[15]!=null){
					pe = new Produtoembalagem((Integer) tupla[15]);
					pe.setQtde((Long) tupla[16]);
					osp.setProdutoembalagem(pe);
				}
				osp.setQtdeesperada((Long) tupla[5]);
				oph.setOrdemservicoproduto(osp);
			}else
				osp = oph.getOrdemservicoproduto();
			
			Ordemprodutostatus ops;
			if (mapOPS.containsKey(tupla[3]))
				ops = mapOPS.get(tupla[3]);
			else{
				ops = new Ordemprodutostatus((Integer)tupla[3]);
				mapOPS.put((Integer)tupla[3], ops);
			}

			Tipopalete tp = null;
			if (tupla[4] != null){
				if (mapTP.containsKey(tupla[4]))
					tp = mapTP.get(tupla[4]);
				else{
					tp = new Tipopalete((Integer)tupla[4]);
					mapTP.put((Integer)tupla[4], tp);
				}
			}

			Produto p;
			if (mapP.containsKey(tupla[8]))
				p = mapP.get(tupla[8]);
			else{
				p = new Produto((Integer)tupla[8]);
				p.setDescricao((String) tupla[9]);
				p.setCodigo((String) tupla[10]);
				p.setComplementocodigobarras((String) tupla[14]);
				
				if (tupla[11] != null){
					Produto pp = new Produto();
					pp.setCdproduto((Integer) tupla[11]);
					pp.setCodigo((String) tupla[12]);
					pp.setDescricao((String) tupla[13]);
					
					p.setProdutoprincipal(pp);
				}
				
				mapP.put((Integer)tupla[8], p);
			}

			osp.setOrdemprodutostatus(ops);
			osp.setTipopalete(tp);
			osp.setProduto(p);

			Etiquetaexpedicao ee = new Etiquetaexpedicao((Integer) tupla[6]);
			ee.setQtdecoletor((Long) tupla[7]);
			osp.getListaEtiquetaexpedicao().add(ee);
		}
		
		resultado.addAll(mapOPH.values());
		return resultado;
	}
	
	/**
	 * Lista todos os códigos de barras possíveis de serem bipados pelo coletor de uma ordem de 
	 * serviço. Somente são pegos os códigos de barras do produto principal, quando a informação
	 * normavolume está nulo ou falso. Caso seja true, será pego os códigos de barras dos volumes.
	 * 
	 * O dado logístico é específico por depósito. Portanto é necessário especificar o depósito.
	 * 
	 * @author Pedro Gonçalves
	 * @param ordemservico
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CodigobarrasVO> findAllBarCodeByOS(Ordemservico ordemservico,Deposito deposito){
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		if(deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		String query = "select osp.cdordemservicoproduto,p.cdproduto,p.descricao as nomep,p.cdprodutoprincipal,pp.descricao as nomepr,pcb.codigo,p.codigo as codproduto,pe.descricao,pe.qtde,pe.cdprodutoembalagem as cdprodutoembalagem "+  
						"from ordemprodutohistorico oph "+
						"join ordemservicoproduto osp on osp.cdordemservicoproduto = oph.cdordemservicoproduto "+
						"join produto p on p.cdprodutoprincipal = osp.cdproduto "+
						"join produto pp on pp.cdproduto = p.cdprodutoprincipal "+
						"join dadologistico d on pp.cdproduto = d.cdproduto and d.normavolume=1 "+
						"left join produtocodigobarras pcb on pcb.cdproduto = p.cdproduto "+
						"left join produtoembalagem pe on pcb.cdprodutoembalagem = pe.cdprodutoembalagem and osp.cdprodutoembalagem = pe.cdprodutoembalagem "+
						"where oph.cdordemservico = "+ordemservico.getCdordemservico()+" and " +
						"d.cddeposito="+deposito.getCddeposito()+" " +  
						" "+
						"union "+
						" "+
						"select osp.cdordemservicoproduto,p.cdproduto,p.descricao as nomep,p.cdprodutoprincipal,null,pcb.codigo,p.codigo as codproduto,pe.descricao,pe.qtde,pe.cdprodutoembalagem as cdprodutoembalagem "+  
						"from ordemprodutohistorico oph "+
						"join ordemservicoproduto osp on osp.cdordemservicoproduto = oph.cdordemservicoproduto "+
						"join produto p on p.cdproduto = osp.cdproduto "+
						"join dadologistico d on p.cdproduto = d.cdproduto and (d.normavolume=0 or d.normavolume is null) "+
						"left join produtocodigobarras pcb on pcb.cdproduto = p.cdproduto "+
						"left join produtoembalagem pe on pcb.cdprodutoembalagem = pe.cdprodutoembalagem and osp.cdprodutoembalagem = pe.cdprodutoembalagem "+
						"where oph.cdordemservico = "+ordemservico.getCdordemservico()+" and " +
						"d.cddeposito="+deposito.getCddeposito()+" ";
		
		// System.out.println(query);
		
		List<CodigobarrasVO> lista = getJdbcTemplate().query(
				query,
				new RowMapper(){

					public Object mapRow(java.sql.ResultSet rs, int currentItem) throws SQLException {
						CodigobarrasVO codigobarrasVO = new CodigobarrasVO();
						codigobarrasVO.setCodigo(rs.getString("codigo"));
						codigobarrasVO.setEmbalgem(rs.getString("descricao"));
						codigobarrasVO.setCodigoProduto(rs.getString("codproduto"));
						codigobarrasVO.setCdprodutoembalagem(rs.getInt("cdprodutoembalagem"));
						//System.out.println(rs.getString("codigo") + "  " + rs.getInt("cdproduto") + "  " + rs.getString("codproduto"));
						Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto();
						ordemservicoproduto.setCdordemservicoproduto(rs.getInt("cdordemservicoproduto"));
						codigobarrasVO.setOrdemservicoproduto(ordemservicoproduto);
						
						Produto produto = new Produto();
						produto.setDescricao(rs.getString("nomep"));
						produto.setCdproduto(rs.getInt("cdproduto"));
						codigobarrasVO.setProduto(produto);
						
						Produto produtoPrincipal = new Produto();
						produtoPrincipal.setCdproduto(rs.getInt("cdprodutoprincipal"));
						produtoPrincipal.setDescricao(rs.getString("nomepr"));
						codigobarrasVO.setProdutoprincipal(produtoPrincipal);
						
						codigobarrasVO.setQtde(rs.getInt("qtde"));
						return codigobarrasVO;
					}
					
				});
		
		return lista;
	}
	
	/**
	 * Lista todos os códigos de barras possíveis de serem bipados pelo coletor de uma ordem de 
	 * serviço. Somente são pegos os códigos de barras do produto principal, quando a informação
	 * normavolume está nulo ou falso. Caso seja true, será pego os códigos de barras dos volumes.
	 * 
	 * O dado logístico é específico por depósito. Portanto é necessário especificar o depósito.
	 * 
	 * @author Pedro Gonçalves
	 * @param ordemservico
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CodigobarrasVO> findAllBarCodeByOSInventario(Ordemservico ordemservico,Deposito deposito){
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		if(deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		String query = "select osp.cdordemservicoproduto,p.cdproduto,p.descricao as nomep,pcb.codigo,p.codigo as codproduto "+  
						"from ordemprodutohistorico oph "+
						"join ordemservicoproduto osp on osp.cdordemservicoproduto = oph.cdordemservicoproduto "+
						"join produto p on p.cdproduto = osp.cdproduto "+
						"left join produtocodigobarras pcb on pcb.cdproduto = p.cdproduto "+
						"where oph.cdordemservico = "+ordemservico.getCdordemservico()+"  "
//						" and pcb.interno = 1 "
						;
		
		List<CodigobarrasVO> lista = getJdbcTemplate().query(
				query,
				new RowMapper(){

					public Object mapRow(java.sql.ResultSet rs, int currentItem) throws SQLException {
						CodigobarrasVO codigobarrasVO = new CodigobarrasVO();
						codigobarrasVO.setCodigo(rs.getString("codigo")); //código de barras
						codigobarrasVO.setCodigoProduto(rs.getString("codproduto")); //codigo do produto
						Ordemservicoproduto ordemservicoproduto = new Ordemservicoproduto();
						ordemservicoproduto.setCdordemservicoproduto(rs.getInt("cdordemservicoproduto"));
						codigobarrasVO.setOrdemservicoproduto(ordemservicoproduto);
						
						Produto produto = new Produto();
						produto.setDescricao(rs.getString("nomep"));
						produto.setCdproduto(rs.getInt("cdproduto"));
						codigobarrasVO.setProduto(produto);
						
						return codigobarrasVO;
					}
					
				});
		
		return lista;
	}
	
	/**
	 * Busca os dados para a tela da lançar dados dos lotes
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemprodutohistorico> findForLancarDados(Ordemservico ordemservico) {
		
		QueryBuilder<Ordemservico> queryBuilder = new QueryBuilder<Ordemservico>(getHibernateTemplate())
			.from(Ordemservico.class)
			.select("inventariolote.cdinventariolote, ordemservicoproduto.cdordemservicoproduto," +
					"ordemprodutohistorico.cdordemprodutohistorico,ordemprodutohistorico.qtde," +
					"ordemservicoproduto.qtdeesperada,ordemprodutostatus.cdordemprodutostatus,produto.cdproduto," +
					"produto.descricao,produto.codigo,ordemservicoprodutoendereco.cdordemservicoprodutoendereco," +
					"ordemservicoprodutoendereco.qtde,enderecodestino.cdendereco,enderecodestino.endereco," +
					"area.cdarea,area.codigo,enderecofuncao.cdenderecofuncao,enderecofuncao.nome," +
					"ordemservico.cdordemservico,tipoestrutura.cdtipoestrutura,tipoestrutura.nome," +
					"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao," +
					"enderecodestino.rua,enderecodestino.predio,enderecodestino.nivel,enderecodestino.apto")
			.leftOuterJoin("ordemservico.inventariolote inventariolote")
			.leftOuterJoin("ordemservico.listaOrdemProdutoHistorico ordemprodutohistorico")
			.leftOuterJoin("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
			.leftOuterJoin("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
			.leftOuterJoin("ordemservicoproduto.produto produto")
			.leftOuterJoin("produto.produtoprincipal produtoprincipal")
			.leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
			.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
			.leftOuterJoin("enderecodestino.area area")
			.leftOuterJoin("enderecodestino.enderecofuncao enderecofuncao")			
			.leftOuterJoin("enderecodestino.tipoestrutura tipoestrutura")
			.leftOuterJoin("area.listaEnderecosentido listaEnderecosentido")
			.where("ordemservico = ?", ordemservico)
			.where("listaEnderecosentido.rua = enderecodestino.rua")
			.orderBy("(enderecodestino.rua*listaEnderecosentido.sentido), enderecodestino.predio," +
				 		"enderecodestino.nivel,enderecodestino.apto,produtoprincipal.descricao,produto.descricao");

		ordemservico = queryBuilder.unique();
		if (ordemservico == null)
			return new ArrayList<Ordemprodutohistorico>();
		
		for (Ordemprodutohistorico oph : ordemservico.getListaOrdemProdutoHistorico()){			
			oph.setOrdemservico(ordemservico);
		}
		
		return ordemservico.getListaOrdemProdutoHistorico();
		
		/*
		 * Se der erro por causa do bug do Neo quando tem duas listas na query, tente usar o código a seguir.
		 * Ele está comentado porque as listas sempre tem apenas um item e não está acontecendo erro atualmente.
		 * 


		QueryBuilder<Ordemservico> queryBuilder = new QueryBuilder<Ordemservico>(getHibernateTemplate())
			.from(Ordemservico.class)
			.select("inventariolote.cdinventariolote, ordemservicoproduto.cdordemservicoproduto," +
					"ordemprodutohistorico.cdordemprodutohistorico,ordemprodutohistorico.qtde," +
					"ordemservicoproduto.qtdeesperada,ordemprodutostatus.cdordemprodutostatus,produto.cdproduto," +
					"produto.descricao,produto.codigo,ordemservico.cdordemservico," +
					"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao")
			.leftOuterJoin("ordemservico.inventariolote inventariolote")
			.leftOuterJoin("ordemservico.listaOrdemProdutoHistorico ordemprodutohistorico")
			.leftOuterJoin("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
			.leftOuterJoin("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
			.leftOuterJoin("ordemservicoproduto.produto produto")
			.leftOuterJoin("produto.produtoprincipal produtoprincipal")
			.leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
			.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
			.leftOuterJoin("enderecodestino.area area")
			.leftOuterJoin("area.listaEnderecosentido listaEnderecosentido")
			.where("ordemservico = ?", ordemservico)
			.where("listaEnderecosentido.rua = enderecodestino.rua")
			.orderBy("(enderecodestino.rua*listaEnderecosentido.sentido), enderecodestino.predio," +
				 		"enderecodestino.nivel,enderecodestino.apto");

		queryBuilder.ignoreJoin("ordemservicoprodutoendereco", "enderecodestino", "area", "listaEnderecosentido");
		
		ordemservico = queryBuilder.unique();
		for (Ordemprodutohistorico oph : ordemservico.getListaOrdemProdutoHistorico()){			
			oph.setOrdemservico(ordemservico);
			
			//Se for carregado na mesma query acima alguns itens podem não aparecer devido a um bug no Neo.
			QueryBuilder<Ordemservicoprodutoendereco> queryBuilderEnderecos = new QueryBuilder<Ordemservicoprodutoendereco>(getHibernateTemplate())
				.from(Ordemservicoprodutoendereco.class)
				.select("ordemservicoprodutoendereco.cdordemservicoprodutoendereco," +
						"ordemservicoprodutoendereco.qtde,enderecodestino.cdendereco,enderecodestino.endereco," +
						"area.cdarea,area.codigo,enderecofuncao.cdenderecofuncao,enderecofuncao.nome," +
						"tipoestrutura.cdtipoestrutura,tipoestrutura.nome," +
						"enderecodestino.rua,enderecodestino.predio,enderecodestino.nivel,enderecodestino.apto")
				.leftOuterJoin("ordemservicoprodutoendereco.ordemservicoproduto ordemservicoproduto")
				.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
				.leftOuterJoin("enderecodestino.area area")
				.leftOuterJoin("enderecodestino.enderecofuncao enderecofuncao")			
				.leftOuterJoin("enderecodestino.tipoestrutura tipoestrutura")
				.leftOuterJoin("area.listaEnderecosentido listaEnderecosentido")
				.where("ordemservicoproduto = ?", oph.getOrdemservicoproduto())
				.where("listaEnderecosentido.rua = enderecodestino.rua")
				.orderBy("(enderecodestino.rua*listaEnderecosentido.sentido), enderecodestino.predio," +
					 		"enderecodestino.nivel,enderecodestino.apto");

			
			oph.getOrdemservicoproduto().setListaOrdemservicoprodutoendereco(queryBuilderEnderecos.list());
		}
		
		return ordemservico.getListaOrdemProdutoHistorico();

		 */
	}
	
	

	/**
	 * Deleta os históricos da ordem de servico
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 */
	public void deleteByOrdemservico(Ordemservico ordemservico) {
		
		if(ordemservico == null || ordemservico.getCdordemservico() == null){
			throw new WmsException("A ordem de serviço não deve ser nula.");
		}
		
		getHibernateTemplate().bulkUpdate("delete Ordemprodutohistorico oph where oph.ordemservico = ?",new Object[]{ordemservico});
	}
	
	/**
	 * Encontra o ordemprodutohistorico a partir da ordem serviço produto.
	 * 
	 * @author Pedro Gonçalves
	 * @param ordemservicoproduto
	 * @return
	 */
	public Ordemprodutohistorico findByOSP(Ordemservicoproduto ordemservicoproduto){
		if(ordemservicoproduto == null || ordemservicoproduto.getCdordemservicoproduto() == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		return query()
				.join("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
				.where("ordemservicoproduto=?",ordemservicoproduto)
				.unique();
				
	}
	
	/**
	 * Encontra o ordemprodutohistorico a partir da ordem serviço produto.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservicoproduto
	 * @return
	 */
	public Ordemprodutohistorico findByOspOs(Ordemservicoproduto ordemservicoproduto, Ordemservico ordemservico){
		if(ordemservicoproduto == null || ordemservicoproduto.getCdordemservicoproduto() == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		return query()
				.where("ordemprodutohistorico.ordemservicoproduto = ?", ordemservicoproduto)
				.where("ordemprodutohistorico.ordemservico = ?", ordemservico)
				.unique();
	}
	
	/**
	 * excluir todos os Ordemprodutohistorico através da ordemservico produto.
	 * @author Pedro gonçalves
	 * @param listaOSP
	 */
	public void deleteAllBy(String listaOSP) {
		if(listaOSP == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		getHibernateTemplate().bulkUpdate("delete Ordemprodutohistorico oph where oph.ordemservicoproduto.id in ("+listaOSP+")");
	}
	
	/**
	 * Altera a quantidade de produto no histórico
	 * @param bean
	 * @author Cíntia Nogueira
	 */
	public void updateQtde(Ordemprodutohistorico bean){
		if(bean==null || bean.getCdordemprodutohistorico()==null || bean.getQtde()==null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		getHibernateTemplate().bulkUpdate("update Ordemprodutohistorico set  qtde = ? where cdordemprodutohistorico =?", 
				new Object[]{bean.getQtde(), bean.getCdordemprodutohistorico()});
	}

	/**
	 * Localiza todos os {@link Ordemprodutohistorico} e seus respectivos {@link Ordemservicoproduto} 
	 * relacionados a uma {@link Ordemservico}.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public List<Ordemprodutohistorico> findByOS(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		return query()
			.joinFetch("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
			.where("ordemprodutohistorico.ordemservico = ?",ordemservico)
			.list();
	}

	/**
	 * Método que acha o registro de histórico que possui a embalagem escolhida na tela de conferencia. Busca através do recebimento, ordemservicoproduto e produto. 
	 * Como pode achar mais de um registro. Se houver reconferencia existe um max q busca último registro.
	 * 
	 * @param recebimento
	 * @param ordemservicoproduto
	 * @return
	 * @auhtor Tomás Rabelo
	 */
	@SuppressWarnings("unchecked")
	public Ordemprodutohistorico getEmbalagemEscolhidaLancamentoConferencia(Recebimento recebimento, Ordemservicoproduto ordemservicoproduto) {
		List<Ordemprodutohistorico> list = getJdbcTemplate().query(
			"select max(t.cdordemprodutohistorico) as cdordemprodutohistorico, pe.cdprodutoembalagem, pe.descricao " +
			"from ordemprodutohistorico t "+ 
			"join Ordemservicoproduto osp on osp.cdordemservicoproduto = t.cdordemservicoproduto "+
			"join Produto p on p.cdproduto = osp.cdproduto "+ 
			"join Ordemservico os on t.cdordemservico = os.cdordemservico "+ 
			"join Recebimento r on r.cdrecebimento = os.cdrecebimento "+
			"left join Produtoembalagem pe on t.cdprodutoembalagem = pe.cdprodutoembalagem "+ 
			"where r.cdrecebimento = "+recebimento.getCdrecebimento()+" and osp.cdproduto = "+ordemservicoproduto.getProduto().getCdproduto()+" and "+
			"t.qtde = (select o.qtdeesperada from ordemservicoproduto o "+
			"where o.cdordemservicoproduto = "+ordemservicoproduto.getCdordemservicoproduto()+") "+
			"group by pe.cdprodutoembalagem, pe.descricao",
			new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Ordemprodutohistorico ordemprodutohistorico = new Ordemprodutohistorico();
					ordemprodutohistorico.setCdordemprodutohistorico(rs.getInt("cdordemprodutohistorico"));
					ordemprodutohistorico.setProdutoembalagem(new Produtoembalagem(rs.getString("cdprodutoembalagem") != null ? rs.getInt("cdprodutoembalagem") : null, rs.getString("descricao")));
					return ordemprodutohistorico;
				}
			});
		
		if(list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * Busca os dados para executar a conferência via coletor
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemprodutohistorico> findForConferenciaColetor(Ordemservico ordemservico, boolean orderByOrigem) {
		
		QueryBuilder<Ordemservico> queryBuilder = new QueryBuilder<Ordemservico>(getHibernateTemplate())
			.from(Ordemservico.class)
			.select("ordemservicoproduto.cdordemservicoproduto,ordemprodutohistorico.cdordemprodutohistorico," +
					"ordemprodutohistorico.qtde,ordemprodutohistorico.qtdeavaria,ordemprodutohistorico.qtdefracionada," +
					"ordemservicoproduto.qtdeesperada,ordemprodutostatus.cdordemprodutostatus," +
					"produto.cdproduto,produto.descricao,produto.codigo,ordemservicoprodutoendereco.cdordemservicoprodutoendereco," +
					"produtoprincipal.cdproduto,produtoprincipal.codigo,produtoprincipal.descricao," +
					"ordemservicoprodutoendereco.qtde,ordemservico.cdordemservico," +
					"enderecodestino.cdendereco,enderecodestino.endereco," +
					"enderecoorigem.cdendereco,enderecoorigem.endereco," +
					"areaO.cdarea,areaO.codigo,enderecofuncaoO.cdenderecofuncao,enderecofuncaoO.nome," +
					"areaD.cdarea,areaD.codigo,enderecofuncaoD.cdenderecofuncao,enderecofuncaoD.nome," +
					"tipoestruturaO.cdtipoestrutura,tipoestruturaO.nome," +
					"tipoestruturaD.cdtipoestrutura,tipoestruturaD.nome," +
					"enderecodestino.rua,enderecodestino.predio,enderecodestino.nivel,enderecodestino.apto," +
					"enderecoorigem.rua,enderecoorigem.predio,enderecoorigem.nivel,enderecoorigem.apto," +
					"ordemprodutoligacao.cdordemprodutoligacao")
			.join("ordemservico.listaOrdemProdutoHistorico ordemprodutohistorico")
			.join("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
			.join("ordemservicoproduto.ordemprodutostatus ordemprodutostatus")
			.join("ordemservicoproduto.listaOrdemprodutoLigacao ordemprodutoligacao")
			.join("ordemservicoproduto.produto produto")
			.leftOuterJoin("produto.produtoprincipal produtoprincipal")
			.leftOuterJoin("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
			.leftOuterJoin("ordemservicoprodutoendereco.enderecodestino enderecodestino")
			.leftOuterJoin("ordemservicoprodutoendereco.enderecoorigem enderecoorigem")
			.leftOuterJoin("enderecodestino.area areaD")
			.leftOuterJoin("enderecoorigem.area areaO")
			.leftOuterJoin("enderecodestino.enderecofuncao enderecofuncaoO")			
			.leftOuterJoin("enderecoorigem.enderecofuncao enderecofuncaoD")			
			.leftOuterJoin("enderecoorigem.tipoestrutura tipoestruturaO")
			.leftOuterJoin("enderecodestino.tipoestrutura tipoestruturaD")
			.where("ordemservico = ?", ordemservico);
		
		if (orderByOrigem){
			queryBuilder.leftOuterJoin("areaO.listaEnderecosentido listaEnderecosentido")
				.openParentheses()
					.where("listaEnderecosentido.rua = enderecoorigem.rua")
					.or()
					.where("enderecoorigem.id is null")
				.closeParentheses()
				.orderBy("(enderecoorigem.rua*listaEnderecosentido.sentido), enderecoorigem.predio," +
				 		"enderecoorigem.nivel,enderecoorigem.apto,produtoprincipal.descricao,produto.descricao");
		}else{
			queryBuilder.leftOuterJoin("areaD.listaEnderecosentido listaEnderecosentido")
				.openParentheses()
					.where("listaEnderecosentido.rua = enderecodestino.rua")
					.or()
					.where("enderecodestino.id is null")
				.closeParentheses()
				.orderBy("(enderecodestino.rua*listaEnderecosentido.sentido), enderecodestino.predio," +
						"enderecodestino.nivel,enderecodestino.apto,produtoprincipal.descricao,produto.descricao");
		}
		
		Ordemservico aux  = queryBuilder.unique();
		if (aux == null)
			return new ArrayList<Ordemprodutohistorico>();
		
		for (Ordemprodutohistorico oph : aux.getListaOrdemProdutoHistorico()){			
			oph.setOrdemservico(ordemservico);
		}
		
		return aux.getListaOrdemProdutoHistorico();
	}
	
	/**
	 * Localiza todos os {@link Ordemprodutohistorico} e seus respectivos {@link Ordemservicoproduto} 
	 * relacionados à primeira ordem de contagem de um recebimento.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public List<Ordemprodutohistorico> findConferencia(Recebimento recebimento) {
		if(recebimento == null || recebimento.getCdrecebimento() == null){
			throw new WmsException("Parâmetros inválidos");
		}
		
		return query()
			.joinFetch("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
			.joinFetch("ordemservicoproduto.produto produto")
			.leftOuterJoinFetch("produto.produtoprincipal produtoprincipal")
			.where("ordemprodutohistorico.ordemservico.recebimento = ?", recebimento)
			.where("ordemprodutohistorico.ordemservico.ordem = 1")
			.list();
	}

	/**
	 * Grava null nas referências de ProdutoEmbalagem.
	 * 
	 * @author Giovane Freitas
	 * @param bean
	 */
	public void removerEmbalagem(Produto bean) {
		getHibernateTemplate().bulkUpdate("UPDATE Ordemprodutohistorico oph " +
				"SET oph.produtoembalagem.id = NULL " +
				"WHERE oph.produtoembalagem.id IN (SELECT cdprodutoembalagem FROM Produtoembalagem WHERE produto.id = ? )", bean.getCdproduto());
	}

	/**
	 * Método que retorna os valores informados na 1º e 2º contagem do invetário
	 * 
	 * @param inventariolote
	 * @return
	 * @author Tomás Rabelo
	 * @param ordemservicoproduto 
	 * @param ordemservicoAtual 
	 */
	public List<Ordemprodutohistorico> getHistoricoContagensRecontagens(Inventariolote inventariolote, Ordemservicoproduto ordemservicoproduto, Ordemservico ordemservicoAtual) {
		return query()
			.select("ordemprodutohistorico.cdordemprodutohistorico, ordemprodutohistorico.qtde, ordemprodutohistorico.qtdeavaria, ordemprodutohistorico.qtdefracionada")
			.join("ordemprodutohistorico.ordemservico ordemservico")
			.join("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
			.leftOuterJoin("ordemservicoproduto.produto produto")
			.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
			.join("ordemservicoprodutoendereco.enderecodestino enderecodestino")
			.where("produto = ?", ordemservicoproduto.getProduto())
			.where("enderecodestino = ?", ordemservicoproduto.getListaOrdemservicoprodutoendereco().get(0).getEnderecodestino())
			.where("ordemservico.id in (select os.id from Ordemservico os join os.inventariolote il join os.ordemstatus oss where il.id = "+inventariolote.getCdinventariolote()+" and oss.id <> "+Ordemstatus.CANCELADO.getCdordemstatus()+")")
			.where("ordemservico <> ?", ordemservicoAtual)
			.list();
	}
	
	/**
	 * Método responsavel por recuperar a Ordemprodutohistorico para evitar que 
	 * as contagens de inventario sejam duplicadas.
	 * 
	 * @author Filipe Santos
	 * @param endereco
	 * @param produto
	 * @param ordemservico
	 * @return Ordemprodutohistorico
	 */
	public Ordemprodutohistorico findByEnderecoProdutoOrdemServico(Endereco endereco, Produto produto, Ordemservico ordemservico){
		return query()
				.joinFetch("ordemprodutohistorico.ordemservicoproduto ordemservicoproduto")
				.join("ordemprodutohistorico.ordemservico ordemservico")				
				.join("ordemservicoproduto.produto produto")
				.join("ordemservicoproduto.listaOrdemservicoprodutoendereco ordemservicoprodutoendereco")
				.join("ordemservicoprodutoendereco.enderecodestino endereco")
				.where("ordemservico = ?",ordemservico)
				.where("endereco = ?",endereco)
				.where("produto = ?", produto)
				.where("ordemprodutohistorico.qtde > 0")
			.unique();
	}

	/**
	 * 
	 * @param recebimento
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Ordemprodutohistorico> findAllConferenciaByRecebimento(Recebimento recebimento) {
		
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append(" select oph.cdordemprodutohistorico, oph.cdordemservico, oph.cdordemservicoproduto, oph.cdprodutoembalagem, ");
		sql.append(" 		nvl(oph.qtde,0) as qtde, nvl(oph.qtdeavaria,0) as qtdeavaria, nvl(oph.qtdefracionada,0) as qtdefracionada, ");
		sql.append(" 		nvl(oph.qtdefalta,0) as qtdefalta ");
		sql.append(" from ordemprodutohistorico oph ");
		sql.append(" join ordemservico os on os.cdordemservico = oph.cdordemservico ");
		sql.append(" join recebimento r on r.cdrecebimento = os.cdrecebimento ");
		sql.append(" where r.cdrecebimento = ").append(recebimento.getCdrecebimento());
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Ordemprodutohistorico oph = new Ordemprodutohistorico();
				Ordemservico os = new Ordemservico();
				Ordemservicoproduto osp = new Ordemservicoproduto();
				Produtoembalagem pe = new Produtoembalagem();
				
				oph.setCdordemprodutohistorico(rs.getInt("cdordemprodutohistorico"));
				
				os.setCdordemservico(rs.getInt("cdordemservico"));
				osp.setCdordemservicoproduto(rs.getInt("cdordemservicoproduto"));
				pe.setCdprodutoembalagem(rs.getInt("cdprodutoembalagem"));
				
				oph.setOrdemservico(os);
				oph.setOrdemservicoproduto(osp);
				
				oph.setQtde(rs.getLong("qtde"));
				oph.setQtdeavaria(rs.getLong("qtdeavaria"));
				oph.setQtdefracionada(rs.getLong("qtdefracionada"));
				oph.setQtdefalta(rs.getLong("qtdefalta"));
				
				return oph;
			}
		});
		
	}
	
}