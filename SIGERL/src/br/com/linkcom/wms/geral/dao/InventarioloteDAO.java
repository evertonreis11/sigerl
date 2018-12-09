package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecolado;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Ordemprodutostatus;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.vo.EnderecoLoteVO;
import br.com.linkcom.wms.geral.bean.vo.LeituraDivergenteInventarioVO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;

public class InventarioloteDAO extends GenericDAO<Inventariolote> {
	
	/**
	 * Busca o lote associado à ordem de serviço
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Inventariolote findByOS(Ordemservico ordemservico) {
		if(ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de serviço não deve ser nula.");
		return query()
					.select("inventariolote.cdinventariolote")
					.join("inventariolote.listaOrdemservico os")
					.where("os = ?",ordemservico)
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * Monta uma lista com todas as leitura divergentes de um lote de inventário, para realizar
	 * o ajuste de estoque dos endereços.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LeituraDivergenteInventarioVO> findLeiturasDivergentes(Inventariolote inventariolote){
		if (inventariolote == null || inventariolote.getCdinventariolote() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select distinct ospe.cdenderecodestino, osp.cdproduto, osp.qtdeesperada, oph.qtde as qtdelida \n"); 
		sql.append("from ordemservicoproduto osp  \n");
		sql.append("  join ordemprodutoligacao opl on opl.cdordemservicoproduto = osp.cdordemservicoproduto \n"); 
		sql.append("  join ordemservico os on os.cdordemservico = opl.cdordemservico \n");
		sql.append("  join inventariolote il on il.cdinventariolote = os.cdinventariolote \n");
		sql.append("  join ordemprodutohistorico oph on oph.cdordemservico = os.cdordemservico and oph.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sql.append("  join ordemservicoprodutoendereco ospe on ospe.cdordemservicoproduto = osp.cdordemservicoproduto \n");
		sql.append("where il.cdinventariolote = ? \n");
		sql.append("  and cdordemprodutostatus = ? \n");
		sql.append("  and oph.qtde <> osp.qtdeesperada \n");
		
		Object args[] = new Object[]{inventariolote.getCdinventariolote(), Ordemprodutostatus.CONCLUIDO_OK.getCdordemprodutostatus()};
		
		return getJdbcTemplate().query(sql.toString(), args, new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				LeituraDivergenteInventarioVO leitura = new LeituraDivergenteInventarioVO();
				leitura.setCdenderecodestino(rs.getInt("cdenderecodestino"));
				leitura.setCdproduto(rs.getInt("cdproduto"));
				leitura.setQtdeesperada(rs.getLong("qtdeesperada"));
				leitura.setQtdelida(rs.getLong("qtdelida"));
				return leitura;
			}
			
		});
	}

	/**
	 * Conta quantos lotes gerão gerados pela criação automática de lotes em massa.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public int contarLotesEmMassa(Inventariolote inventariolote) {
		if (inventariolote == null || inventariolote.getArea() == null || inventariolote.getArea().getCdarea() == null 
				|| inventariolote.getRuainicial() == null || inventariolote.getRuafinal() == null 
				|| inventariolote.getPredioinicial() == null || inventariolote.getPrediofinal() == null 
				|| inventariolote.getNivelinicial() == null || inventariolote.getNivelfinal() == null 
				|| inventariolote.getAptoinicial() == null || inventariolote.getAptofinal() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sql = new StringBuilder();		
		
		sql.append("select count(*) from ( \n");
		sql.append("  	select e.cdarea, e.rua, case when e.cdenderecofuncao = ").append(Enderecofuncao.BLOCADO.getCdenderecofuncao());
		sql.append("	then null else e.nivel end as nivel, mod(e.predio, 2) \n");
		sql.append("  from endereco e  \n");
		sql.append("    join area a on a.cdarea = e.cdarea \n");
		
		if (inventariolote.getProduto() != null && inventariolote.getProduto().getCdproduto() != null){
			sql.append("    left join enderecoproduto ep on ep.cdendereco = e.cdendereco \n");			
		}
		
		montaWhereLoteEmMassa(inventariolote, sql);
		
		sql.append("	group by e.rua, e.cdarea, case when e.cdenderecofuncao = ").append(Enderecofuncao.BLOCADO.getCdenderecofuncao());
		sql.append("	then null else e.nivel end, mod(e.predio, 2) \n");
		sql.append(") \n");
		
		return getJdbcTemplate().queryForInt(sql.toString());
	}

	/**
	 * Busca os endereços para montagem automática dos lotes de contagem de inventário.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EnderecoLoteVO> findEnderecoLote(Inventariolote inventariolote){
		if (inventariolote == null || inventariolote.getArea() == null || inventariolote.getArea().getCdarea() == null 
				|| inventariolote.getRuainicial() == null || inventariolote.getRuafinal() == null 
				|| inventariolote.getPredioinicial() == null || inventariolote.getPrediofinal() == null 
				|| inventariolote.getNivelinicial() == null || inventariolote.getNivelfinal() == null 
				|| inventariolote.getAptoinicial() == null || inventariolote.getAptofinal() == null)
			throw new WmsException("Parâmetros inválidos.");

		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("select e.cdarea, e.rua, e.nivel, mod(e.predio, 2) as lado , case when e.cdenderecofuncao = 3 then 1 else 0 end as blocado, \n"); 
		sql.append("   min(e.predio) as predio_inicial, max(e.predio) as predio_final, \n");
		sql.append("   min(e.apto) as apto_inicial, max(e.apto) as apto_final \n");
		sql.append("from endereco e \n");
		sql.append("  join area a on a.cdarea = e.cdarea \n");

		
		if (inventariolote.getProduto() != null && inventariolote.getProduto().getCdproduto() != null){
			sql.append("    left join enderecoproduto ep on ep.cdendereco = e.cdendereco \n");			
		}

		montaWhereLoteEmMassa(inventariolote, sql);

		sql.append("group by e.cdarea, e.rua, e.nivel, mod(e.predio, 2), case when e.cdenderecofuncao = ")
			.append(Enderecofuncao.BLOCADO.getCdenderecofuncao())
			.append("then 1 else 0 end \n"); 
		
		//A ordem do resultado é fundamental para o processo de geração de lotes
		sql.append("order by e.cdarea, e.rua, mod(e.predio, 2), e.nivel, case when e.cdenderecofuncao = ")
			.append(Enderecofuncao.BLOCADO.getCdenderecofuncao())
			.append("then 1 else 0 end \n"); 

		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				EnderecoLoteVO item = new EnderecoLoteVO();
				item.setCdarea(rs.getInt("cdarea"));
				item.setRua(rs.getInt("rua"));
				item.setNivel(rs.getInt("nivel"));
				item.setLadoImpar(rs.getBoolean("lado"));
				item.setBlocado(rs.getBoolean("blocado"));
				item.setPredioInicial(rs.getInt("predio_inicial"));
				item.setPredioFinal(rs.getInt("predio_final"));
				item.setAptoInicial(rs.getInt("apto_inicial"));
				item.setAptoFinal(rs.getInt("apto_final"));
				return item;
			}
			
		});
	}
	
	
	/**
	 * Monta a cláusa de where para as consultas de contar lotes a criar em
	 * masse e para a consulta de obter os endereços que formarão os lotes.
	 * 
	 * @autor Giovane Freitas
	 * @param inventariolote
	 * @param sql
	 * @param args
	 */
	private void montaWhereLoteEmMassa(Inventariolote inventariolote, StringBuilder sql) {
		sql.append("  where a.cddeposito = ").append(WmsUtil.getDeposito().getCddeposito()).append("\n");
		sql.append("    and a.cdarea = ").append(inventariolote.getArea().getCdarea()).append("\n");
		
		sql.append("    and e.rua >= ").append(inventariolote.getRuainicial()).append(" \n");
		sql.append("    and e.rua <= ").append(inventariolote.getRuafinal()).append(" \n");
		
		sql.append("    and e.predio >= ").append(inventariolote.getPredioinicial()).append("\n");
		sql.append("    and e.predio <= ").append(inventariolote.getPrediofinal()).append("\n");
		
		sql.append("    and e.nivel >= ").append(inventariolote.getNivelinicial()).append(" \n");
		sql.append("    and e.nivel <= ").append(inventariolote.getNivelfinal()).append("\n");
		
		sql.append("    and e.apto >= ").append(inventariolote.getAptoinicial()).append(" \n");
		sql.append("    and e.apto <= ").append(inventariolote.getAptofinal()).append(" \n");
		
		if (inventariolote.getProduto() != null && inventariolote.getProduto().getCdproduto() != null)
			sql.append("    and ep.cdproduto = ").append(inventariolote.getProduto().getCdproduto()).append(" \n");

		if (inventariolote.getEnderecofuncao() != null && inventariolote.getEnderecofuncao().getCdenderecofuncao() != null)
			sql.append("    and e.cdenderecofuncao = ?").append(inventariolote.getEnderecofuncao().getCdenderecofuncao()).append(" \n");
		
		if (inventariolote.getEnderecolado() != null && inventariolote.getEnderecolado().getCdenderecolado() != null){
			if (Enderecolado.PAR.equals(inventariolote.getEnderecolado()))
				sql.append("    and mod(e.predio, 2) = 0 \n");
			else
				sql.append("    and mod(e.predio, 2) <> 0 \n");
		}
		
	}

}
