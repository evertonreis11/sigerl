package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Fornecedor;
import br.com.linkcom.wms.geral.bean.Ordemstatus;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Pessoa;
import br.com.linkcom.wms.geral.bean.Tipopessoa;
import br.com.linkcom.wms.geral.service.ClienteService;
import br.com.linkcom.wms.geral.service.FabricanteService;
import br.com.linkcom.wms.geral.service.FornecedorService;
import br.com.linkcom.wms.geral.service.TransportadorService;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ImpressaoprodutividadeFiltro;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.PessoaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.logistica.ProdutividadeVO;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class PessoaDAO extends GenericDAO<Pessoa> {

	private ClienteService clienteService;
	private FornecedorService fornecedorService;
	private FabricanteService fabricanteService;
	private TransportadorService transportadorService;
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setTransportadorService(TransportadorService transportadorService) {
		this.transportadorService = transportadorService;
	}
	
	public void setFornecedorService(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}
	
	public void setFabricanteService(FabricanteService fabricanteService) {
		this.fabricanteService = fabricanteService;
	}
	
	
	

	@Override
	public void updateListagemQuery(QueryBuilder<Pessoa> query,FiltroListagem _filtro) {
		PessoaFiltro filtro=(PessoaFiltro)_filtro;
		String select = "";
		String tipo = "pessoa";
		Tipopessoa listatipo = filtro.getListatipo();
		if(listatipo != null && listatipo.getChavenome() != null){
			if(listatipo.getChavenome().equals("CL")){
					query.from(Cliente.class);
					tipo = "cliente";
			}else if(listatipo.getChavenome().equals("FO")){
					query.from(Fornecedor.class);
					tipo = "fornecedor";
			}
				
		}
		select = makeSelect(tipo);
		query
			.ignoreAllJoinsPath(true)
			.select(select)
			.where(tipo+".ativo=?",filtro.getAtivo() != null ? new Boolean(filtro.getAtivo()) : null)
			.where("not exists (select usuario.cdpessoa from Usuario usuario where usuario.cdpessoa = "+tipo+".cdpessoa)")
			.where("not exists (select transportador.cdpessoa from Transportador transportador where transportador.cdpessoa = "+tipo+".cdpessoa)")
			.whereLikeIgnoreAll(tipo+".nome",filtro.getNome())
			.where(tipo+".pessoanatureza=?",filtro.getPessoanatureza())
			.orderBy(tipo+".cdpessoa");
			
	}
	
	/**
	 * Cria um select de pessoa com o tipo desejado
	 * caso o tipo seja nulo retorna ""
	 * @author Leonardo Guimarães
	 * @param tipo
	 * @return
	 */
	private String makeSelect(String tipo) {
		if(tipo != null && !tipo.equals("")){
			return "distinct "+tipo+".cdpessoa, "+tipo+".pessoanatureza, "+tipo+".nome, "+tipo+".ativo";
		}
		return "";
	}
	
	/**
	 * Método para localização de cadastro prévio de pessoa.
	 * @param cpf
	 * @return null se não encontrar
	 * @author João Paulo Zica
	 */
	public Pessoa findPessoaByDocumento(String documento) {
		if(documento == null){
			throw new WmsException("O documento não pode ser nulo");
		}
		return query()
				.select("pessoa.cdpessoa,pessoa.nome,pessoanatureza.cdpessoanatureza,pessoanatureza.nome," +
						"pessoa.documento, pessoa.ativo,pessoa.telefone")
				.join("pessoa.pessoanatureza pessoanatureza")
				.where("pessoa.documento = ?", documento)
				.setMaxResults(1).unique();
	}

	/**
	 * Salva o cdpessoa nas tabelas selecionadas nos checkbox
	 * @author Leonardo  
	 * @param bean
	 */
	public void salvaListaTipo(Pessoa bean){
		if(bean == null || bean.getCdpessoa() == null || bean.getListatipo() == null){
			throw new WmsException("A pessoa não pode ser nula");
		}
		
		List<String> listaTipo = new ArrayList<String>();
		for (Tipopessoa tipopessoa : bean.getListatipo()) {
			listaTipo.add(tipopessoa.getChavenome());			
		}
		if(bean.getCdpessoa() != null){
			saveListaTipo("Cliente",bean.getCdpessoa(),listaTipo.contains("CL"));
			saveListaTipo("Fornecedor",bean.getCdpessoa(),listaTipo.contains("FO"));
			saveListaTipo("Transportador",bean.getCdpessoa(),listaTipo.contains("TR"));
			saveListaTipo("Fabricante",bean.getCdpessoa(),listaTipo.contains("FA"));
		}
	}
	
	/**
	 * <p>Caso isInsert seja true:<br>
	 * Verifica se existe algum registro com o mesmo cd no banco, 
	 * se não existir não salva o cd no banco
	 * e se existir não faz nada.</p>
	 * <p>Caso  isInsert seja false:<br>
	 * Deleta o registro que tenha o cd correspondente.</p>
	 * @author Leonardo Guimarães
	 * @param tipo
	 * @param cd
	 * @param isInsert
	 */
	public void saveListaTipo(String tipo,Integer cd,Boolean isInsert){
		if(cd == null){
			throw new WmsException("O cd não deve ser nulo");
		}
		Object[] obj = new Object[] { cd };
		if(isInsert && tipo!= null){
			if(tipo.equals("Cliente") && clienteService.findByCd(cd) != null){
				return;
			} else if(tipo.equals("Fornecedor") && fornecedorService.findByCd(cd) != null){
				return;
			} else if(tipo.equals("Fabricante") && fabricanteService.findByCd(cd) != null){
				return;
			} else if(tipo.equals("Transportador") && transportadorService.findByCd(cd) != null){
				return;
			}
					
			getJdbcTemplate().update("insert into " + tipo + " (cdpessoa) values(?)",obj);
		} else{
			getJdbcTemplate().update("delete " + tipo + " where " + tipo + ".cdpessoa=?",obj);
		}
	}
	
	/**
	 * Encontra uma pessoa que possua o respectivo cod
	 * @author Leonardo Guimarães
	 * @param cod
	 * @return Uma pessoa com base no cod
	 */
	public Pessoa findPessoaByCodigo(Integer cod) {
		if (cod == null) {
			throw new WmsException("Parâmetro Código não pode ser null.");
		}
		return query()
			.select("pessoa.cdpessoa, pessoa.nome")
			.where("pessoa.cdpessoa = ?", cod)
			.setMaxResults(1)
			.unique();
	}
	
	/**
	 * Desativa a pessoa ao invés de exclui-la
	 * @author Leonardo Guimarães
	 * @param bean
	 */
	public void desativa(Pessoa bean) {
		if(bean == null || bean.getCdpessoa() == null){
			throw new WmsException("O bean ou cdpessoa não deve ser nulo");
		}
		Object[] obj = new Object[] { false,bean.getCdpessoa() };
		getJdbcTemplate().update("update pessoa set ativo=? where pessoa.cdpessoa=?",obj);
		
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Pessoa> query) {
		query.joinFetch("pessoa.pessoanatureza pessoanatureza");
	}
	
	/**
	 * 
	 * Método que recupera a produtividade de uma ou mais pessoas respeitando os parâmetros do filtro.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<ProdutividadeVO>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<ProdutividadeVO> findByProdutividade(ImpressaoprodutividadeFiltro filtro) {
		 if(filtro == null)
			throw new WmsException("Parâmetros inválidos.");
		 
		String virgula = ", ";
		
		String sentenca = 
			"SELECT PESSOA.CDPESSOA, PESSOA.NOME AS PESSOA_NOME, " +
			"       OT.NOME AS OT_NOME, " +
			"       SUM(P.PESO * OSPE.QTDE)AS PESO, " +
			"       SUM(P.ALTURA * P.PROFUNDIDADE * P.LARGURA * OSPE.QTDE * 0.000001) AS VOLUME, COUNT(OS.CDORDEMSERVICO) AS PALETE " +
			"FROM PESSOA " +
			"JOIN ORDEMSERVICOUSUARIO OSU " +
			"  ON OSU.CDPESSOA = PESSOA.CDPESSOA " +
			"JOIN ORDEMSERVICO OS " +
			"  ON OS.CDORDEMSERVICO = OSU.CDORDEMSERVICO " +
			"JOIN ORDEMPRODUTOLIGACAO OPL " +
			"  ON OPL.CDORDEMSERVICO = OS.CDORDEMSERVICO " +
			"JOIN ORDEMSERVICOPRODUTO OSP " +
			"  ON OSP.CDORDEMSERVICOPRODUTO = OPL.CDORDEMSERVICOPRODUTO " +
			"JOIN ORDEMSERVICOPRODUTOENDERECO OSPE " +
			"  ON OSPE.CDORDEMSERVICOPRODUTO = OSP.CDORDEMSERVICOPRODUTO " +
			"JOIN PRODUTO P " +
			"  ON P.CDPRODUTO = OSP.CDPRODUTO " +
			"JOIN ORDEMTIPO OT " +
			"  ON OT.CDORDEMTIPO = OS.CDORDEMTIPO " +
			 "WHERE OS.CDORDEMTIPO IN (" + 
			 					Ordemtipo.MAPA_SEPARACAO.getCdordemtipo() + virgula + 
								Ordemtipo.REABASTECIMENTO_PICKING.getCdordemtipo() + virgula + 
								Ordemtipo.TRANSFERENCIA.getCdordemtipo() + virgula + 
								Ordemtipo.ENDERECAMENTO_AVARIADO.getCdordemtipo() + virgula + 
								Ordemtipo.ENDERECAMENTO_FRACIONADO.getCdordemtipo() + virgula + 
								Ordemtipo.ENDERECAMENTO_PADRAO.getCdordemtipo() + ") " +
			"  AND OS.CDORDEMSTATUS IN (" + Ordemstatus.FINALIZADO_DIVERGENCIA.getCdordemstatus() + virgula +
											Ordemstatus.FINALIZADO_SUCESSO.getCdordemstatus() + ") ";
		
			if((filtro.getUsuario() != null) && (filtro.getUsuario().getCdpessoa() != null)) 
				sentenca += "  AND OSU.CDPESSOA = " + filtro.getUsuario().getCdpessoa();
							  
			if(filtro.getDatainicial() != null)
				sentenca += "  AND TO_DATE(OSU.DTINICIO) >= TO_DATE('" + filtro.getDatainicial() + "', 'yyyy/mm/ddhh24miss')";
			
			if(filtro.getDatafinal() != null)
				sentenca += "  AND TO_DATE(OSU.DTINICIO) <= TO_DATE('" + filtro.getDatafinal() + "', 'yyyy/mm/ddhh24miss')";
		   
			if((filtro.getOrdemtipo() != null) )
				sentenca += "  AND OS.CDORDEMTIPO  in ( " + 
				CollectionsUtil.listAndConcatenate(filtro.getOrdemtipo().getListaOrdemTipo(), "cdordemtipo", ",")
				 + ") ";
			
			sentenca += "GROUP BY PESSOA.CDPESSOA, PESSOA.NOME, OT.NOME ";
			
			sentenca += " " +
		    			"UNION ALL " +
		       			" ";
			
			sentenca += 
				"SELECT PESSOA.CDPESSOA, PESSOA.NOME AS PESSOA_NOME, " +
				"       OT.NOME AS OT_NOME, " +
				"       SUM(P.PESO * OPH.QTDE + P.PESO * OPH.QTDEAVARIA) AS PESO, " +
				"  	    SUM(P.ALTURA * P.PROFUNDIDADE * P.LARGURA * ((OPH.QTDE * 0.000001) + (OPH.QTDEAVARIA * 0.000001))) AS VOLUME, " +
				"   	0 AS PALETE " +
				"FROM PESSOA JOIN ORDEMSERVICOUSUARIO OSU " +
				"  ON OSU.CDPESSOA =PESSOA.CDPESSOA " +
				"JOIN ORDEMSERVICO OS " +
				"  ON OS.CDORDEMSERVICO = OSU.CDORDEMSERVICO " +
				"JOIN ORDEMPRODUTOLIGACAO OPL " +
				"  ON OPL.CDORDEMSERVICO = OS.CDORDEMSERVICO " +
				"JOIN ORDEMSERVICOPRODUTO OSP " +
				"  ON OSP.CDORDEMSERVICOPRODUTO = OPL.CDORDEMSERVICOPRODUTO " +
				"JOIN ORDEMPRODUTOHISTORICO OPH " +
				"  ON OPH.CDORDEMSERVICOPRODUTO = OSP.CDORDEMSERVICOPRODUTO " +
				"JOIN PRODUTO P " +
				"  ON P.CDPRODUTO = OSP.CDPRODUTO " +
				"JOIN ORDEMTIPO OT " +
				"  ON OT.CDORDEMTIPO = OS.CDORDEMTIPO " +
				"WHERE OS.CDORDEMTIPO IN (" + 
									Ordemtipo.CONFERENCIA_RECEBIMENTO.getCdordemtipo() + virgula + 
									Ordemtipo.RECONFERENCIA_RECEBIMENTO.getCdordemtipo() + virgula + 
									Ordemtipo.CONFERENCIA_EXPEDICAO_1.getCdordemtipo() + virgula + 
									Ordemtipo.RECONFERENCIA_EXPEDICAO_1.getCdordemtipo() + virgula + 
									Ordemtipo.CONTAGEM_INVENTARIO.getCdordemtipo() + ") " +
				"  AND OS.CDORDEMSTATUS IN (" + Ordemstatus.FINALIZADO_DIVERGENCIA.getCdordemstatus() + virgula + 
												Ordemstatus.FINALIZADO_SUCESSO.getCdordemstatus() + ") ";
			
			if((filtro.getUsuario() != null) && (filtro.getUsuario().getCdpessoa() != null)) 
				sentenca += "  AND OSU.CDPESSOA = " + filtro.getUsuario().getCdpessoa();
						  
			if(filtro.getDatainicial() != null)
				sentenca += "  AND TO_DATE(OSU.DTINICIO) >= TO_DATE('" + filtro.getDatainicial() + "', 'yyyy/mm/ddhh24miss')";
				
			if(filtro.getDatafinal() != null)
				sentenca += "  AND  TO_DATE(OSU.DTINICIO) <= TO_DATE('" + filtro.getDatafinal() + "', 'yyyy/mm/ddhh24miss')";
			   
			if((filtro.getOrdemtipo() != null) )
				sentenca += "  AND OS.CDORDEMTIPO  in ( " + 
				CollectionsUtil.listAndConcatenate(filtro.getOrdemtipo().getListaOrdemTipo(), "cdordemtipo", ",")
				 + ") ";
				
				sentenca += "GROUP BY PESSOA.CDPESSOA, PESSOA.NOME, OT.NOME " +
							"ORDER BY PESSOA_NOME";
	
		System.out.println(sentenca);
		
		List<ProdutividadeVO> lista = getJdbcTemplate().query(
			sentenca,		
			new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
					ProdutividadeVO produtividade = new ProdutividadeVO();
					
					produtividade.setCdpessoa(rs.getInt("CDPESSOA"));
					produtividade.setPessoaNome(rs.getString("PESSOA_NOME"));
					produtividade.setOrdemTipoNome(rs.getString("OT_NOME"));
					produtividade.setPeso(rs.getDouble("PESO"));
					produtividade.setVolume(rs.getDouble("VOLUME"));
					produtividade.setPalete(rs.getLong("PALETE"));
				
					return produtividade;
				}
			}
		);
		
		return lista;
	}
	
	/* singleton */
	private static PessoaDAO instance;

	public static PessoaDAO getInstance() {
		if (instance == null) {
			instance = Neo.getObject(PessoaDAO.class);
		}
		return instance;
	}
}
