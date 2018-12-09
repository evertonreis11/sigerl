package br.com.linkcom.wms.geral.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariostatus;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.vo.VolumeDivergenteVO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ContagemInventarioFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.InventarioFiltro;
import br.com.linkcom.wms.util.CollectionsUtils;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.logistica.InventarioVO;

public class InventarioDAO extends GenericDAO<Inventario>{
	
	@Override
	public void updateListagemQuery(QueryBuilder<Inventario> query,FiltroListagem _filtro) {
		
		InventarioFiltro filtro = (InventarioFiltro)_filtro;
		query
			.select("inventario.cdinventario,inventario.dtinventario,inventario.acuracia, inventariotipo.cdinventariotipo," +
					"inventariotipo.nome,inventariostatus.cdinventariostatus,inventariostatus.nome")
			.join("inventario.inventariotipo inventariotipo")
			.join("inventario.inventariostatus inventariostatus")
			.join("inventario.deposito deposito")
			.where("inventariotipo = ?",filtro.getInventariotipo())
			.where("inventario.dtinventario >= ?",filtro.getDatainicial())
			.where("inventario.dtinventario <= ?",filtro.getDatafinal())
			.where("inventariostatus = ?",filtro.getSituacao())
			.where("inventario.cdinventario = ?",filtro.getCdinventario())
			.where("deposito = ?",WmsUtil.getDeposito())
			.orderBy("inventario.cdinventario DESC")
			;
			
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Inventario> query) {
		query
			.select("inventario.cdinventario,inventario.dtinventario,inventario.acuracia, deposito.cddeposito," +
					"inventariotipo.cdinventariotipo,inventariotipo.nome,inventariostatus.cdinventariostatus," +
					"inventariostatus.nome,inventariolote.cdinventariolote,inventariolote.ruainicial," +
					"inventariolote.predioinicial,inventariolote.nivelinicial,inventariolote.aptoinicial," +
					"inventariolote.ruafinal,inventariolote.prediofinal,inventariolote.nivelfinal," +
					"inventariolote.aptofinal,area.cdarea,area.nome,area.codigo,produto.cdproduto,produto.descricao,produto.codigo," +
					"enderecofuncao.cdenderecofuncao,enderecofuncao.nome,enderecolado.cdenderecolado,enderecolado.nome," +
					"inventariolote.fracionada")
			.join("inventario.inventariotipo inventariotipo")
			.join("inventario.inventariostatus inventariostatus")
			.join("inventario.listaInventariolote inventariolote")
			.leftOuterJoin("inventariolote.produto produto")
			.join("inventariolote.area area")
			.leftOuterJoin("inventariolote.enderecolado enderecolado")
			.leftOuterJoin("inventariolote.enderecofuncao enderecofuncao")
			.join("inventario.deposito deposito")
			.orderBy("inventario.cdinventario, inventariolote.cdinventariolote");
		
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaInventariolote");
	}
	
	/**
	 * Atualiza o status do inventário
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventario
	 */
	public void updateInventarioStatus(Inventario inventario){
		if( inventario == null || inventario.getCdinventario() == null ||
			inventario.getInventariostatus() == null || inventario.getInventariostatus().getCdinventariostatus() == null){
			throw new WmsException("Dados insuficientes para alterar o status do inventário.");
		}
		
		if (inventario.getInventariostatus().equals(Inventariostatus.FINALIZADO_DIVERGENTE) || inventario.getInventariostatus().equals(Inventariostatus.FINALIZADO_SUCESSO)){
			getHibernateTemplate().bulkUpdate("update Inventario inventario set inventario.inventariostatus = ?, inventario.acuracia = ACURACIA_INVENTARIO(inventario.id) where inventario = ?",
					new Object[]{inventario.getInventariostatus(),inventario});			
		}else{
			getHibernateTemplate().bulkUpdate("update Inventario inventario set inventario.inventariostatus = ?, inventario.acuracia = null where inventario = ?",
					new Object[]{inventario.getInventariostatus(),inventario});						
		}
		
			
	}
	
	@Override
	public Inventario load(Inventario bean) {
		if(bean == null || bean.getCdinventario() == null)
			throw new WmsException("O inventário não deve ser nulo.");
		return query()
					.leftOuterJoinFetch("inventario.inventariostatus inventariostatus")
					.leftOuterJoinFetch("inventario.listaInventariolote listaInventariolote")
					.leftOuterJoinFetch("inventario.inventariotipo inventariotipo")
					.where("inventario = ?",bean)
					.unique();
	}
	
	/**
	 * Busca o inventário da ordem de serviço informada
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Inventario findByOrdemservico(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de serviço não deve ser nula.");
		return query()
					.select("inventario.cdinventario,listaInventariolote.cdinventariolote,listaInventariolote.fracionada")
					.join("inventario.listaInventariolote listaInventariolote")
					.join("listaInventariolote.listaOrdemservico ordemservico")
					.where("ordemservico = ?",ordemservico)
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * 
	 * Recupera uma lista de inventários com os dados já calculados para preencher o relatório de inventário
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<InventarioVO>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<InventarioVO> findByInventario(Inventario filtro) {
		if(filtro == null || filtro.getCdinventario() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		sql.append("select i.cdinventario, i.dtinventario, it.nome as inventariotipo, p.codigo as codigo_produto, \n");
		sql.append("  nvl(pp.descricao, p.descricao) as descricao_produto, pe.descricao as embalagem,   \n");
		sql.append("   sum(osp.qtdeesperada) as qtdeesperada, sum(oph.qtde) as qtde  \n");
		sql.append("from ordemservicoproduto osp    \n");
		sql.append("  join ordemprodutoligacao opl on opl.cdordemservicoproduto = osp.cdordemservicoproduto    \n");
		sql.append("  join ordemservico os on os.cdordemservico = opl.cdordemservico   \n");
		sql.append("  join inventariolote il on il.cdinventariolote = os.cdinventariolote   \n");
		sql.append("  join inventario i on i.cdinventario = il.cdinventario  \n");
		sql.append("  join ordemprodutohistorico oph on oph.cdordemservico = os.cdordemservico and oph.cdordemservicoproduto = osp.cdordemservicoproduto \n");  
		sql.append("  join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");  
		sql.append("  join produtoembalagem pe on pe.cdproduto = osp.cdproduto \n"); 
		sql.append("  join produto p on p.cdproduto = osp.cdproduto \n"); 
		sql.append("  join inventariotipo it on it.cdinventariotipo = i.cdinventariotipo \n"); 
		sql.append("  left join produto pp on pp.cdproduto = p.cdprodutoprincipal \n"); 
		sql.append("where il.cdinventario = ? \n");  
		sql.append("  and cdordemprodutostatus = 2 \n"); 
		sql.append("  and os.cdordemtipo <> 13 \n");  
		sql.append("  and oph.cdordemprodutohistorico = ( \n"); 
		sql.append("    select max(cdordemprodutohistorico) \n");  
		sql.append("    from ordemprodutohistorico oph2 \n"); 
		sql.append("      join ordemservico os2 on os2.cdordemservico = oph2.cdordemservico \n"); 
		sql.append("      join ordemservicoproduto osp2 on osp2.cdordemservicoproduto = oph2.cdordemservicoproduto \n"); 
		sql.append("      join ordemservicoprodutoendereco ospe2 on ospe2.cdordemservicoproduto = osp2.cdordemservicoproduto \n");
		sql.append("    where osp2.cdproduto = osp.cdproduto \n"); 
		sql.append("      and os2.cdinventariolote = os.cdinventariolote \n"); 
		sql.append("      and ospe2.cdenderecodestino = ospe.cdenderecodestino  \n");
		sql.append("      and osp2.cdordemprodutostatus <> 1 \n"); 
		sql.append("      and os2.cdordemtipo <> 13 \n"); 
		sql.append("  ) \n"); 
		sql.append("  and ( il.fracionada = 1 and pe.qtde = ( \n"); 
		sql.append("            select min(pe.qtde)   \n");
		sql.append("            from Produtoembalagem pe \n");  
		sql.append("            where pe.cdproduto=p.cdproduto  \n");
		sql.append("        ) \n"); 
		sql.append("        or il.fracionada = 0 \n");  
		sql.append("        and pe.cdprodutoembalagem = ( \n"); 
		sql.append("            select pe.cdprodutoembalagem \n");  
		sql.append("            from Produtoembalagem pe \n");  
		sql.append("            where pe.cdproduto=p.cdproduto and pe.compra=1 \n"); 
		sql.append("        ) \n"); 
		sql.append("  ) \n");  
		sql.append("group by i.cdinventario, i.dtinventario, it.nome, p.codigo, nvl(pp.descricao, p.descricao), pe.descricao \n");
		sql.append("order by p.codigo \n");
		
		return getJdbcTemplate().query(sql.toString(), new Object[]{filtro.getCdinventario()}, new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				InventarioVO inventarioVO = new InventarioVO();
				inventarioVO.setCdinventario(rs.getInt("cdinventario"));
				inventarioVO.setData(NeoFormater.getInstance().format(rs.getDate("dtinventario")));
				inventarioVO.setInventariotipo(rs.getString("inventariotipo"));
				inventarioVO.setCodigodescricaoproduto(rs.getString("codigo_produto") + " - " + rs.getString("descricao_produto"));
				inventarioVO.setDescricaoembalagem(rs.getString("embalagem"));
				inventarioVO.setQtdeanterior(rs.getLong("qtdeesperada"));
				inventarioVO.setQtdeinventario(rs.getLong("qtde"));
				inventarioVO.setDivergencia(rs.getLong("qtde") - rs.getLong("qtdeesperada"));
				return inventarioVO;
			}
			
			
		});
	}
	
	/**
	 * Chama a procedure 'INICIAR_INVENTARIO' no banco de dados do sistema
	 * 
	 * @author Rodrigo Alvarenga
	 * 
	 * @param inventario
	 * @return mensagem caso haja algum erro na execução da procedure
	 * @throws WmsException - caso o parâmetro seja nulo 
	 */
	public String iniciarInventario(Inventario inventario) throws SQLException {
		String msgRetorno = "";
		
		if(inventario == null || inventario.getCdinventario() == null) {
			throw new WmsException("Dados insuficientes para invocar a função 'INICIAR_INVENTARIO'");
		}
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		try {
			cstmt = (CallableStatement) conn.prepareCall("BEGIN INICIAR_INVENTARIO(:1); END;");
			cstmt.setInt(1, inventario.getCdinventario());
			cstmt.execute();
		}
		catch(SQLException e){
			e.printStackTrace();
			
			Pattern padrao = Pattern.compile(".*###(.*)###.*",Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
			Matcher matcher = padrao.matcher(e.getMessage());
			if (matcher.matches()) {
				msgRetorno = matcher.group(1) + " <a href='"+WmsUtil.getContex()+"/popup/Enderecosindisponiveis?cdinventario=" + inventario.getCdinventario()+ "'> Verificar Endereços </a>" ;
			}
			else {
				msgRetorno = e.getMessage();
			}
		}
		finally {
			cstmt.close();
			conn.close();
		}
		return msgRetorno;
	}
	
	/**
	 * Busca os resumos do inventário para a tela de acompanhamento de contagem do inventário.
	 *
	 * @param inventario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findResumoAcompanhamento(Inventario inventario){
		if (inventario == null || inventario.getCdinventario() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sqlTotalEndereco = new StringBuilder();
		sqlTotalEndereco.append("select count(*) \n");
		sqlTotalEndereco.append("from ( \n");
		sqlTotalEndereco.append("    select distinct os.cdordemstatus, \n"); 
		sqlTotalEndereco.append("      case when e.cdenderecofuncao = 3 then a.codigo || '.' || e.rua || '.' || e.predio \n");
		sqlTotalEndereco.append("        else a.codigo || '.' || e.endereco end as enderecodestino \n");
		sqlTotalEndereco.append("    from inventariolote il \n");
		sqlTotalEndereco.append("       join ordemservico os on os.cdinventariolote = il.cdinventariolote \n");
		sqlTotalEndereco.append("       join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico \n");
		sqlTotalEndereco.append("       join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto \n");
		sqlTotalEndereco.append("       join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sqlTotalEndereco.append("       join endereco e on e.cdendereco = ospe.cdenderecodestino \n");
		sqlTotalEndereco.append("       join area a on a.cdarea = e.cdarea \n");
		sqlTotalEndereco.append("    where cdinventario = ? and os.ordem = 1 \n");
		sqlTotalEndereco.append(") \n");

		
		StringBuilder sqlContagem = new StringBuilder();
		sqlContagem.append("select nvl(sum(case when cdordemstatus = 1 then 1 else 0 end), 0) as em_aberto, \n");
		sqlContagem.append("  nvl(sum(case when cdordemstatus = 2 then 1 else 0 end), 0) as em_execucao, \n");
		sqlContagem.append("  nvl(sum(case when cdordemstatus in (3,4,6,7) then 1 else 0 end), 0) as finalizado, \n");
		sqlContagem.append("  count(enderecodestino) as total \n");
		sqlContagem.append("from ( \n");
		sqlContagem.append("    select distinct os.cdordemstatus, \n"); 
		sqlContagem.append("      case when e.cdenderecofuncao = 3 then a.codigo || '.' || e.rua || '.' || e.predio \n");
		sqlContagem.append("        else a.codigo || '.' || e.endereco end as enderecodestino \n");
		sqlContagem.append("    from inventariolote il \n");
		sqlContagem.append("       join ordemservico os on os.cdinventariolote = il.cdinventariolote \n");
		sqlContagem.append("       join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico \n");
		sqlContagem.append("       join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto \n");
		sqlContagem.append("       join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sqlContagem.append("       join endereco e on e.cdendereco = ospe.cdenderecodestino \n");
		sqlContagem.append("       join area a on a.cdarea = e.cdarea \n");
		sqlContagem.append("    where cdinventario = ? and os.ordem = ? \n");
		sqlContagem.append(") \n");

		StringBuilder sqlDivergencia = new StringBuilder();
		sqlDivergencia.append("select nvl(sum(case when cdordemstatus = 1 then 1 else 0 end), 0) as em_aberto, \n");
		sqlDivergencia.append("  nvl(sum(case when cdordemstatus = 2 then 1 else 0 end), 0) as em_execucao, \n");
		sqlDivergencia.append("  nvl(sum(case when cdordemstatus in (3,4,6,7) then 1 else 0 end), 0) as finalizado, \n");
		sqlDivergencia.append("  count(enderecodestino) as total \n");
		sqlDivergencia.append("from ( \n");
		sqlDivergencia.append("    select min(os.cdordemstatus) as cdordemstatus,   \n");
		sqlDivergencia.append("      case when e.cdenderecofuncao = 3 then a.codigo || '.' || e.rua || '.' || e.predio \n");
		sqlDivergencia.append("        else a.codigo || '.' || e.endereco end as enderecodestino \n");
		sqlDivergencia.append("    from inventariolote il \n");
		sqlDivergencia.append("       join ordemservico os on os.cdinventariolote = il.cdinventariolote \n");
		sqlDivergencia.append("       join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico \n");
		sqlDivergencia.append("       join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto \n");
		sqlDivergencia.append("       join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sqlDivergencia.append("       join endereco e on e.cdendereco = ospe.cdenderecodestino \n");
		sqlDivergencia.append("       join area a on a.cdarea = e.cdarea  \n");
		sqlDivergencia.append("    where cdinventario = ? and os.cdordemtipo = ? \n");
		sqlDivergencia.append("    group by case when e.cdenderecofuncao = 3 then a.codigo || '.' || e.rua || '.' || e.predio  \n");
		sqlDivergencia.append("        else a.codigo || '.' || e.endereco end \n");
		
		sqlDivergencia.append(") \n");

		StringBuilder sqlConcluidos = new StringBuilder();
		sqlConcluidos.append("select nvl(sum(case when cdordemtipo = 12 then 1 else 0 end), 0) as com_recontagem,  \n");
		sqlConcluidos.append("  nvl(sum(case when cdordemtipo = 11 then 1 else 0 end), 0) as sem_recontagem,  \n");
		sqlConcluidos.append("  count(enderecodestino) as total  \n");
		sqlConcluidos.append("from ( \n");
		sqlConcluidos.append("    select max(os.cdordemtipo) as cdordemtipo,   \n");
		sqlConcluidos.append("      case when e.cdenderecofuncao = 3 then a.codigo || '.' || e.rua || '.' || e.predio  \n");
		sqlConcluidos.append("        else a.codigo || '.' || e.endereco end as enderecodestino  \n");
		sqlConcluidos.append("    from inventariolote il  \n");
		sqlConcluidos.append("       join ordemservico os on os.cdinventariolote = il.cdinventariolote  \n");
		sqlConcluidos.append("       join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico  \n");
		sqlConcluidos.append("       join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto  \n");
		sqlConcluidos.append("       join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto  \n");
		sqlConcluidos.append("       join endereco e on e.cdendereco = ospe.cdenderecodestino  \n");
		sqlConcluidos.append("       join area a on a.cdarea = e.cdarea  \n");
		sqlConcluidos.append("    where cdinventario = ?  \n");
		sqlConcluidos.append("    and not exists ( \n");
		sqlConcluidos.append("    	select * from ordemservico os2   \n");
		sqlConcluidos.append("			join ordemprodutoligacao opl2 on opl2.cdordemservico = os2.cdordemservico   \n");
		sqlConcluidos.append("			join ordemservicoproduto osp2 on osp2.cdordemservicoproduto = opl2.cdordemservicoproduto   \n");
		sqlConcluidos.append("			join ordemservicoprodutoendereco ospe2 on ospe2.cdordemservicoproduto = osp2.cdordemservicoproduto   \n");
		sqlConcluidos.append("		where os2.cdordemservicoprincipal = os.cdordemservico and ospe2.cdenderecodestino = ospe.cdenderecodestino   \n");
		sqlConcluidos.append("			and os2.cdordemtipo in (11, 12) ) \n");
		sqlConcluidos.append("    and ( (os.cdordemtipo = ? and os.ordem = ?) or os.cdordemtipo = ?)  and osp.cdordemprodutostatus = ?  \n");
		sqlConcluidos.append("    group by case when e.cdenderecofuncao = 3 then a.codigo || '.' || e.rua || '.' || e.predio  \n");
		sqlConcluidos.append("        else a.codigo || '.' || e.endereco end \n");
		sqlConcluidos.append(") \n");

		
		Integer totalEnderecos = getJdbcTemplate().queryForInt(sqlTotalEndereco.toString(), new Object[]{inventario.getCdinventario()});
		
		Map<String, Number> primeiraContagem = getJdbcTemplate().queryForMap(sqlContagem.toString(), new Object[]{inventario.getCdinventario(), 1});
		for (Entry<String, Number> entry : new ArrayList<Entry<String, Number>>(primeiraContagem.entrySet()))
			if (entry.getValue() != null)
				primeiraContagem.put(entry.getKey() + "_percent", entry.getValue().doubleValue() / totalEnderecos.doubleValue());
		
		Map<String, Number> segundaContagem = getJdbcTemplate().queryForMap(sqlContagem.toString(), new Object[]{inventario.getCdinventario(), 2});
		segundaContagem.put("em_aberto", segundaContagem.get("em_aberto").longValue() + totalEnderecos - segundaContagem.get("total").longValue());
		segundaContagem.put("total", totalEnderecos);
		for (Entry<String, Number> entry : new ArrayList<Entry<String, Number>>(segundaContagem.entrySet()))
			if (entry.getValue() != null)
				segundaContagem.put(entry.getKey() + "_percent", entry.getValue().doubleValue() / totalEnderecos.doubleValue());
		
		Map divergencias = getJdbcTemplate().queryForMap(sqlDivergencia.toString(), new Object[]{inventario.getCdinventario(), Ordemtipo.RECONTAGEM_INVENTARIO.getCdordemtipo()});
		for (Entry<String, Number> entry : new ArrayList<Entry<String, Number>>(divergencias.entrySet()))
			if (entry.getValue() != null)
				divergencias.put(entry.getKey() + "_percent", entry.getValue().doubleValue() / totalEnderecos.doubleValue());
		
		Map fechamento = getJdbcTemplate().queryForMap(sqlConcluidos.toString(), new Object[]{inventario.getCdinventario(), Ordemtipo.CONTAGEM_INVENTARIO.getCdordemtipo(), 2, Ordemtipo.RECONTAGEM_INVENTARIO.getCdordemtipo(), Ordemprodutostatus.CONCLUIDO_OK.getCdordemprodutostatus()});
		for (Entry<String, Number> entry : new ArrayList<Entry<String, Number>>(fechamento.entrySet()))
			if (entry.getValue() != null)
				fechamento.put(entry.getKey() + "_percent", entry.getValue().doubleValue() / totalEnderecos.doubleValue());
		
		Map<String, Object> resumo = new HashMap<String, Object>();
		resumo.put("cdinventario", inventario.getCdinventario());
		resumo.put("totalEndereco", totalEnderecos);
		resumo.put("contagem1", primeiraContagem);
		resumo.put("contagem2", segundaContagem);
		resumo.put("divergencias", divergencias);
		resumo.put("fechamento", fechamento);
		
		return resumo;
	}

	/**
	 * Obtém a lista de produtos que tiverem divergência durantea contagem do inventário.
	 * 
	 * @author Giovane Freitas
	 * @author Igor Silvério Costa (revisão 22/07/2011)
	 * @param inventario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VolumeDivergenteVO> getVolumesDivergentes(Inventario inventario) {
		if (inventario == null || inventario.getCdinventario() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		sql.append("select p.cdprodutoprincipal, p.codigo, pp.descricao, lpad(a.codigo, 2, '0') || '.' || e.endereco as endereco, oph.qtde,   \n");
		sql.append("          (select count(*) from produto where cdprodutoprincipal = p.cdprodutoprincipal) as volumes, os.cdordemtipo \n");
		sql.append("from ordemprodutohistorico oph  \n");
		sql.append("  join ordemservico os on os.cdordemservico = oph.cdordemservico \n"); 
		sql.append("  join inventariolote il on il.cdinventariolote = os.cdinventariolote \n"); 
		sql.append("  join ordemservicoproduto osp on osp.cdordemservicoproduto = oph.cdordemservicoproduto \n"); 
		sql.append("  join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n"); 
		sql.append("  join produto p on p.cdproduto = osp.cdproduto \n"); 
		sql.append("  join endereco e on e.cdendereco = ospe.cdenderecodestino \n"); 
		sql.append("  join produto pp on pp.cdproduto = p.cdprodutoprincipal \n"); 
		sql.append("  join area a on a.cdarea = e.cdarea \n"); 
		sql.append(" where il.cdinventario = ? \n"); 
//		sql.append(" and p.cdprodutoprincipal = (select cdproduto from produto where codigo = '135616.120.0')");
		sql.append("  and p.cdprodutoprincipal is not null \n"); 
		sql.append("  and os.cdordemtipo in (11, 12) \n"); 
		sql.append("  and oph.cdordemprodutohistorico = ( \n");
		sql.append("      select max(oph2.cdordemprodutohistorico) \n");
		sql.append("      from ordemprodutohistorico oph2 \n");
		sql.append("        join ordemservico os2 on os2.cdordemservico = oph2.cdordemservico \n");
		sql.append("        join ordemservicoproduto osp2 on osp2.cdordemservicoproduto = oph2.cdordemservicoproduto \n");
		sql.append("        join ordemservicoprodutoendereco ospe2 on ospe2.cdordemservicoproduto = osp2.cdordemservicoproduto \n");
		sql.append("      where osp2.cdproduto = osp.cdproduto \n");
		sql.append("        and os2.cdinventariolote = os.cdinventariolote \n");
		sql.append("        and ospe2.cdenderecodestino = ospe.cdenderecodestino \n");
		sql.append("        and os2.cdordemtipo = os.cdordemtipo \n"); 
		sql.append("        and osp2.cdordemprodutostatus <> 1  \n");//ignora os itens da última O.S. que ainda não foram lidos
		sql.append("  ) \n");
		sql.append(" order by 4, 2 \n");

		final Map<Integer, Integer> menorQuantidadeProdutoMap = new HashMap<Integer, Integer>();
		final Map<Integer, Integer> totalVolumesMap = new HashMap<Integer, Integer>();
		final Map<Integer, Integer> volumesContadosMap = new HashMap<Integer, Integer>();//<cdprodutoprincipal, qtdeTotalVolumes> 
		final List<String> listaVolumesJaContados = new ArrayList<String>();//lista que armazena se um produto já foi contado como um volume
		
		final Map<Integer, Map<String, Integer>> qtdeVolumesMap = new HashMap<Integer, Map<String, Integer>>();//<cdprodutprincipal, <item.codigo, qtde>>
		final Map<Integer, Boolean> qtdeDivergenteMap = new HashMap<Integer, Boolean>();//armazena se as qtde dos volumes que compõe um cdprodutoprincial divergem entre si
		List<VolumeDivergenteVO> volumes = getJdbcTemplate().query(sql.toString(), new Object[]{inventario.getCdinventario()}, new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				VolumeDivergenteVO item = new VolumeDivergenteVO();
				item.setCdprodutoprincipal(rs.getInt("cdprodutoprincipal"));
				item.setCodigo(rs.getString("codigo"));
				item.setDescricao(rs.getString("descricao")); 
				item.setEndereco(rs.getString("endereco"));
				item.setQtde(rs.getInt("qtde"));
				item.setCdordemtipo(rs.getInt("cdordemtipo"));
				item.setSq(rowNum);
				item.setVolumes(rs.getInt("volumes"));
				return item;
			}
		});	
		
		//retira os volumes duplicados
		List<VolumeDivergenteVO> volumesFiltrados = new ArrayList<VolumeDivergenteVO>();
			for (VolumeDivergenteVO vd : volumes) {
			int index = VolumeDivergenteVO.indexOfComMesmoEndereco(volumesFiltrados, vd);//localiza um volume na lista com o mesmo codigo e o mesmo endereco
			if(index!=-1){
				VolumeDivergenteVO vd2 = volumesFiltrados.get(index);
				if(vd2.getEndereco().equals(vd.getEndereco())){
					if(vd.getCdordemtipo() > vd2.getCdordemtipo())//se for recontagem
						volumesFiltrados.set(index, vd);//substitui o item atual da lista pelo da recontagem
				} else volumesFiltrados.add(vd);
			}else volumesFiltrados.add(vd);
		}
		
		//inicia os maps
		for (VolumeDivergenteVO item : volumesFiltrados) {
			if (menorQuantidadeProdutoMap.containsKey(item.getCdprodutoprincipal())){
				Integer qtdeAnterior = menorQuantidadeProdutoMap.get(item.getCdprodutoprincipal());
				menorQuantidadeProdutoMap.put(item.getCdprodutoprincipal(), Math.min(item.getQtde(), qtdeAnterior));
			}else{
				menorQuantidadeProdutoMap.put(item.getCdprodutoprincipal(), item.getQtde());
			}

			//pega a quantidade de volumes cadastrada no bd
			if (!totalVolumesMap.containsKey(item.getCdprodutoprincipal())){
				totalVolumesMap.put(item.getCdprodutoprincipal(), item.getVolumes());
			}
			
			//Conta a quantidade de volumes de um produto, no processo de inventário para comparar com a quantidade cadastrada no banco
			if (volumesContadosMap.containsKey(item.getCdprodutoprincipal())){
				if(!listaVolumesJaContados.contains(item.getCodigo())){//se o produto ainda nao contado como um volume
					Integer qtdeAnterior = volumesContadosMap.get(item.getCdprodutoprincipal());
					volumesContadosMap.put(item.getCdprodutoprincipal(), qtdeAnterior + 1);
				}
			}else{
				volumesContadosMap.put(item.getCdprodutoprincipal(), 1);
			}
			listaVolumesJaContados.add(item.getCodigo());
			
			//inicializa o mapa com a quantidade somada dos volumnes
			Map<String, Integer> mapaQtdeSomadaVolume = new HashMap<String, Integer>(); 
			Integer soma = 0;
			if (qtdeVolumesMap.containsKey(item.getCdprodutoprincipal())){
				mapaQtdeSomadaVolume = qtdeVolumesMap.get(item.getCdprodutoprincipal());
				if(mapaQtdeSomadaVolume.containsKey(item.getCodigo())){
					soma = mapaQtdeSomadaVolume.get(item.getCodigo()) + item.getQtde();
				}else{
					soma = item.getQtde();
				}
			}else{
				soma = item.getQtde();
			}
			mapaQtdeSomadaVolume.put(item.getCodigo(), soma);
			qtdeVolumesMap.put(item.getCdprodutoprincipal(), mapaQtdeSomadaVolume);
		}
		
		//inicia o map que armazena se o os volumes possuem quantidades divergentes entre si
		for(Integer cdprodutoprincipal : qtdeVolumesMap.keySet()){
			Map<String, Integer> mapaQtdeProduto = qtdeVolumesMap.get(cdprodutoprincipal);
//			List<Integer> listaQtde = mapaQtdeProduto.get(cdprodutoprincipal);
			Boolean divergente = Boolean.FALSE;
			Integer ultimaQtde = null;
			for (String codigoP : mapaQtdeProduto.keySet()) {
				Integer qtde = mapaQtdeProduto.get(codigoP);
				if(ultimaQtde!=null && !ultimaQtde.equals(qtde)) //se algum dos produtos tem a quantidade difente
					divergente = Boolean.TRUE;
				ultimaQtde = qtde;
			}
			qtdeDivergenteMap.put(cdprodutoprincipal, divergente);
		}
		
		//filtra a lista deixando somente os produtos que possuem a contagem de produtos divergente ou que a quantidade dos volumes seja divergente entre si
		List<VolumeDivergenteVO> volumesFiltrados2 = new ArrayList<VolumeDivergenteVO>();
		for (VolumeDivergenteVO volume : volumesFiltrados){
			if (totalVolumesMap.get(volume.getCdprodutoprincipal()).equals(volumesContadosMap.get(volume.getCdprodutoprincipal()))){
				//se a quantidade de oph.qtde é divergente entre os volumes que componhem um produo, tb vai pro relatório
				if(qtdeDivergenteMap.get( volume.getCdprodutoprincipal())){//se possui oph.qtde divergente
					volume.setSobra(volume.getQtde() - menorQuantidadeProdutoMap.get(volume.getCdprodutoprincipal()));
					volumesFiltrados2.add(volume);
				}
			}else{
				volume.setSobra(volume.getQtde());
				volumesFiltrados2.add(volume);//se a qtde de volumes é divergente, vai pro relatório
			}
		}
		return volumesFiltrados2;
	}
	
	/**
	 * Busca o número de contagens que foram realizadas em um inventário.
	 * 
	 * @param filtro
	 * @return
	 */
	public int getNumeroContagens(ContagemInventarioFiltro filtro) {
		if (filtro == null || filtro.getCdinventario() == null)
			throw new WmsException("Parâmetros inválidos");
		
		int numContagens = getJdbcTemplate().queryForInt("select max(os.ordem) from ordemservico os join inventariolote il on il.cdinventariolote = os.cdinventariolote where il.cdinventario = ? and os.cdordemtipo in (11, 12)", new Object[]{filtro.getCdinventario()});
		return numContagens;
	}

	/**
	 * Busca os valores lidos em cada contagem de um inventário.
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet findContagemInventario(ContagemInventarioFiltro filtro) {
		if (filtro == null || filtro.getCdinventario() == null)
			throw new WmsException("Parâmetros inválidos");
		
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("select p.codigo, nvl(pp.descricao, p.descricao) as descricao,  \n");
		sql.append("  lpad(a.codigo, 2, '0') || '.' || case when e.cdenderecofuncao <> 3 then e.endereco else substr(e.endereco, 1, 7) end as endereco, \n"); 
		sql.append("  osp.qtdeesperada, os.ordem, oph.qtde, osp.cdordemprodutostatus \n");
		sql.append("from inventariolote il \n");
		sql.append("  join ordemservico os on os.cdinventariolote = il.cdinventariolote \n");
		sql.append("  join ordemprodutoligacao opl on opl.cdordemservico = os.cdordemservico \n");
		sql.append("  join ordemservicoproduto osp on osp.cdordemservicoproduto = opl.cdordemservicoproduto \n");
		sql.append("  join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sql.append("  left join produto p on osp.cdproduto = p.cdproduto \n");
		sql.append("  left join produto pp on pp.cdproduto = p.cdprodutoprincipal \n");
		sql.append("  join endereco e on e.cdendereco = ospe.cdenderecodestino \n");
		sql.append("  join area a on a.cdarea = e.cdarea \n");
		sql.append("  join ordemprodutohistorico oph on oph.cdordemservicoproduto = osp.cdordemservicoproduto and oph.cdordemservico = os.cdordemservico \n");
		sql.append("where cdinventario = ? \n");
		
		args.add(filtro.getCdinventario());
		sql.append("  and os.cdordemtipo in (11,12) \n");
		
		if (filtro.getInventariolote() != null && filtro.getInventariolote().getCdinventariolote() != null){
			sql.append("   and il.cdinventariolote = ? \n");
			args.add(filtro.getInventariolote().getCdinventariolote());
		}
		
		if (filtro.getProduto() != null && filtro.getProduto().getCdproduto() != null){
			sql.append("  and (p.cdproduto = ? or p.cdprodutoprincipal = ? or p.cdproduto is null) ");
			args.add(filtro.getProduto().getCdproduto());
			args.add(filtro.getProduto().getCdproduto());
		}
		
		if (filtro.getArea() != null && filtro.getArea().getCdarea() != null){
			sql.append("  and a.cdarea = ? ");
			args.add(filtro.getArea().getCdarea());
		}
		
		if (filtro.getRuaInicial() != null){
			sql.append("  and e.rua >= ? ");
			args.add(filtro.getRuaInicial());			
		}
		if (filtro.getRuaFinal() != null){
			sql.append("  and e.rua <= ? ");
			args.add(filtro.getRuaFinal());			
		}
		
		if (filtro.getPredioInicial() != null){
			sql.append("  and e.predio >= ? ");
			args.add(filtro.getPredioInicial());			
		}
		if (filtro.getPredioFinal() != null){
			sql.append("  and e.predio <= ? ");
			args.add(filtro.getPredioFinal());			
		}
		
		if (filtro.getNivelInicial() != null){
			sql.append("  and e.nivel >= ? ");
			args.add(filtro.getNivelInicial());			
		}
		if (filtro.getNivelFinal() != null){
			sql.append("  and e.nivel <= ? ");
			args.add(filtro.getNivelFinal());			
		}
		
		if (filtro.getAptoInicial() != null){
			sql.append("  and e.apto >= ? ");
			args.add(filtro.getAptoInicial());			
		}
		if (filtro.getAptoFinal() != null){
			sql.append("  and e.apto <= ? ");
			args.add(filtro.getAptoFinal());			
		}
		
		if (filtro.getExisteDivergenciaContagem() != null){
			if (filtro.getExisteDivergenciaContagem())
				sql.append("   and exists ( \n");
			else
				sql.append("   and not exists ( \n");
			
			sql.append("      select osp2.cdordemservicoproduto \n");
			sql.append("      from ordemservicoproduto osp2 \n");
			sql.append("         join ordemservicoprodutoendereco ospe2 on ospe2.cdordemservicoproduto = osp2.cdordemservicoproduto \n");
			sql.append("         join ordemprodutohistorico oph2 on oph2.cdordemservicoproduto = osp2.cdordemservicoproduto \n");
			sql.append("         join ordemservico os2 on os2.cdordemservico = oph2.cdordemservico \n");
			sql.append("       where os2.cdinventariolote = il.cdinventariolote \n");
			sql.append("         and ospe2.cdenderecodestino = ospe.cdenderecodestino \n");
			sql.append("         and osp2.cdproduto = osp.cdproduto \n");
			sql.append("         and osp2.cdordemprodutostatus = 3 \n");
			sql.append("       ) \n");
		}
		
		if (filtro.getDivergenciaEsperadoFinal() != null){
			if (filtro.getDivergenciaEsperadoFinal())
				sql.append("   and not exists ( \n");//Não existe item finalizado com quantidade igual a esperada
			else
				sql.append("   and exists ( \n");//Existe item finalizado com quantidade igual a esperada
			
			sql.append("      select osp2.cdordemservicoproduto \n");
			sql.append("      from ordemservicoproduto osp2 \n");
			sql.append("        join ordemservicoprodutoendereco ospe2 on ospe2.cdordemservicoproduto = osp2.cdordemservicoproduto \n");
			sql.append("        join ordemprodutohistorico oph2 on oph2.cdordemservicoproduto = osp2.cdordemservicoproduto \n");
			sql.append("        join ordemservico os2 on os2.cdordemservico = oph2.cdordemservico \n");
			sql.append("      where os2.cdinventariolote = il.cdinventariolote \n");
			sql.append("        and ospe2.cdenderecodestino = ospe.cdenderecodestino \n");
			sql.append("        and osp2.cdproduto = osp.cdproduto \n");
			sql.append("        and osp2.cdordemprodutostatus = 2 \n");
			sql.append("        and os2.ordem > 1 \n");
			sql.append("        and osp2.qtdeesperada = oph2.qtde \n");
			sql.append("      ) \n");
		}
		
		if ("PRODUTO".equals(filtro.getOrdenacao().getId()))
			sql.append("order by p.codigo, a.codigo, e.endereco, os.ordem \n");
		else
			sql.append("order by lpad(a.codigo, 2, '0'), e.endereco, p.codigo, os.ordem \n");
		
		return getJdbcTemplate().queryForRowSet(sql.toString(), args.toArray());
	}

	/**
	 * Verifica se um dado inventário terá de fazer ajuste de estoque ao ser finalizado.
	 * 
	 * @autor Giovane Freitas
	 * @param inventario
	 * @return
	 */	
	public boolean isAjusteEstoqueNecessario(Inventario inventario) {
		if (inventario == null || inventario.getCdinventario() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select distinct ospe.cdenderecodestino, osp.cdproduto, osp.qtdeesperada, oph.qtde as qtdelida \n"); 
		sql.append("from ordemservicoproduto osp  \n");
		sql.append("  join ordemprodutoligacao opl on opl.cdordemservicoproduto = osp.cdordemservicoproduto \n"); 
		sql.append("  join ordemservico os on os.cdordemservico = opl.cdordemservico \n");
		sql.append("  join inventariolote il on il.cdinventariolote = os.cdinventariolote \n");
		sql.append("  join ordemprodutohistorico oph on oph.cdordemservico = os.cdordemservico and oph.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sql.append("  join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sql.append("where il.cdinventario = ? \n");
		sql.append("  and cdordemprodutostatus = ? \n");
		sql.append("  and oph.qtde <> osp.qtdeesperada \n");
		sql.append("  and rownum = 1 \n");
		
		Object args[] = new Object[]{inventario.getCdinventario(), Ordemprodutostatus.CONCLUIDO_OK.getCdordemprodutostatus()};
		
		List<?> ajusteEstoquePendente = getJdbcTemplate().queryForList(sql.toString(), args);
		
		return ajusteEstoquePendente != null && ajusteEstoquePendente.size() > 0;
	}
	

}
