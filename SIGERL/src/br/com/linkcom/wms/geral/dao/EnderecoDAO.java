package br.com.linkcom.wms.geral.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.jdbc.driver.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.OracleSQLErrorCodeSQLExceptionTranslator.ForeignKeyException;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.EnderecoLinhaseparacao;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecolado;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Motivobloqueio;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.TotalVO;
import br.com.linkcom.wms.geral.filter.IntervaloEndereco;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.BuscarEnderecoPopupFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.ConsultarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.EnderecoDestinoFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.EnderecoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.logistica.EnderecoAux;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EnderecoDAO extends GenericDAO<Endereco> {	
	
	/**
	 * 
	 * Gera a listagem para o endereço
	 * 
	 * @author Arantes
	 * 
	 * @param _filtro (FiltroListagem)
	 * 
	 */
	@Override
	public void updateListagemQuery(QueryBuilder<Endereco> query, FiltroListagem _filtro) {
		if(_filtro == null) 
			throw new WmsException("Os parâmetros não deve ser nulos");
		
		EnderecoFiltro enderecoFiltro = (EnderecoFiltro) _filtro;
		
		query
			.joinFetch("endereco.area area")
			.join("area.deposito deposito")
			.joinFetch("endereco.tipoendereco tipoendereco")
			.joinFetch("endereco.enderecostatus enderecostatus")
			.joinFetch("endereco.tipoestrutura tipoestrutura")
			.joinFetch("endereco.enderecofuncao enderecofuncao")
			.joinFetch("endereco.tipopalete tipopalete")
//			.leftOuterJoin("endereco.listaEnderecoLinhaseparacao els")
			.fetchCollection("listaEnderecoLinhaseparacao")
//			.leftOuterJoin("els.linhaseparacao linhaseparacao")
			.where("deposito = ?",WmsUtil.getDeposito())
			.where("area = ?", enderecoFiltro.getArea())
			.where("endereco.tipopalete=?", enderecoFiltro.getTipopalete())
			.where("exists (from EnderecoLinhaseparacao els2 where els2.linhaseparacao = ? and els2.endereco = endereco) ", enderecoFiltro.getLinhaseparacao())
			.whereIntervalMatches("endereco.rua", "endereco.rua", enderecoFiltro.getRuaI(), enderecoFiltro.getRuaF());
		
		if(enderecoFiltro.getLado() != null) {
			if(enderecoFiltro.getLado())
				query
					.where("mod(endereco.predio, 2) = 0");
			
			else if(!enderecoFiltro.getLado())
				query
					.where("mod(endereco.predio, 2) <> 0");
			
		}
		query.whereIntervalMatches("endereco.predio", "endereco.predio", enderecoFiltro.getPredioI(), enderecoFiltro.getPredioF());
	
		
		query
			.whereIntervalMatches("endereco.nivel", "endereco.nivel", enderecoFiltro.getNivelI(), enderecoFiltro.getNivelF())
			.whereIntervalMatches("endereco.apto", "endereco.apto", enderecoFiltro.getAptoI(), enderecoFiltro.getAptoF())
			.where("enderecofuncao = ?", enderecoFiltro.getEnderecofuncao())
			.where("tipoestrutura = ?", enderecoFiltro.getTipoestrutura())
			.where("tipoendereco = ?", enderecoFiltro.getTipoendereco())
			.where("endereco.larguraexcedente = ?", enderecoFiltro.getLarguraexcedente())
			.where("enderecostatus = ?", enderecoFiltro.getEnderecostatus())
			.orderBy("endereco.area, endereco.endereco");
	}
	
	/**
	 * Retorna todos os endereços do prédio
	 * @param area
	 * @param ruaI
	 * @param ruaF
	 * @param predio
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Endereco> findEnderecoByPredio(Area area,Integer ruaI, Integer ruaF, Integer predioI, Integer predioF, Boolean lado ){
		if(area==null || area.getCdarea()==null || ruaI==null || ruaF==null || predioI==null || predioF==null){
			throw new WmsException("Todos os campos tem que ser preenchidos");
		}
		QueryBuilder<Endereco> query = query()
		      .select("endereco.cdendereco,endereco.endereco, enderecofuncao.cdenderecofuncao, " +
		      		"enderecofuncao.nome, area.cdarea," +
		      		" area.codigo, enderecostatus.cdenderecostatus, enderecostatus.nome," +
		      		" motivobloqueio.cdmotivobloqueio, motivobloqueio.nome")
		      .join("endereco.enderecofuncao enderecofuncao")
		      .join("endereco.enderecostatus enderecostatus")
		      .leftOuterJoin("endereco.motivobloqueio motivobloqueio")
		      .join("endereco.area area")
		      .where("area = ?", area)
		     .whereIntervalMatches("endereco.rua", "endereco.rua", ruaI, ruaF)
		     .whereIntervalMatches("endereco.predio", "endereco.predio", predioI,predioF);
		
		if (lado != null){
			if(lado)
				query.where("mod(endereco.predio, 2) = 0");
			
			else 
				query.where("mod(endereco.predio, 2) <> 0");
		}
		
		return query.list();
	}
	
	/**
	 * Retorna todos os endereços de um prédio
	 * @param area
	 * @param rua
	 * @param predio
	 * @param orderby
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Endereco> findEnderecoByPredio(Area area,Integer rua, Integer predio, String orderby){
		if(area==null || area.getCdarea()==null || rua==null  || predio==null){
			throw new WmsException("Todos os campos tem que ser preenchidos");
		}
		return query()
		      .select("endereco.cdendereco,endereco.endereco, enderecofuncao.cdenderecofuncao, enderecofuncao.nome," +
		      		"area.cdarea,area.codigo, endereco.rua, endereco.predio, endereco.nivel, endereco.apto " 
		      		/*"listaEnderecoproduto.produto,listaEnderecoproduto.cdenderecoproduto," +
		      		"listaEnderecoproduto.qtde,enderecostatus.cdenderecostatus "*/)
		      .leftOuterJoin("endereco.enderecofuncao enderecofuncao")
		   //   .leftOuterJoin("endereco.listaEnderecoproduto listaEnderecoproduto")
		      .leftOuterJoin("endereco.enderecostatus enderecostatus")		      
		      .join("endereco.area area")
		      .where("area = ?", area)
		     .where("endereco.rua=?",rua)
		     .where("endereco.predio=?", predio)	
		     .orderBy((orderby !=null && !orderby.isEmpty())? orderby : "endereco.cdendereco")
		     .list();
	}
	
	
	
	/**
	 * Busca o endereco através do endereço de picking
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param enderecoPicking
	 * @param cdarea
	 * @return
	 */
	public Endereco findByEnderecoPicking(String enderecoPicking, Integer cdarea) {
		if(enderecoPicking == null || "".equals(enderecoPicking) || cdarea == null)
			throw new WmsException("Os parâmetros não deve ser nulos");
		return query()
				.select("endereco.cdendereco, endereco.endereco ,tipoendereco.cdtipoendereco, tipoendereco.nome," +
						"listaDadologistico.cddadologistico,produto.cdproduto, endereco.larguraexcedente")
				.join("endereco.area area")
				.join("endereco.tipoendereco tipoendereco")
				.join("area.deposito deposito")
				.join("endereco.enderecofuncao enderecofuncao")
				.join("endereco.enderecostatus enderecostatus")
				.leftOuterJoin("endereco.listaDadologistico listaDadologistico")
				.leftOuterJoin("listaDadologistico.produto produto")
				.where("endereco.endereco =?",enderecoPicking)
				.where("area.id = ?",cdarea)
				.where("deposito = ?",WmsUtil.getDeposito())
				.where("enderecofuncao = ?",Enderecofuncao.PICKING)
				.openParentheses()
					.where("enderecostatus <> ?",Enderecostatus.BLOQUEADO)
					.or()
					.openParentheses()
						.where("enderecostatus = ?",Enderecostatus.BLOQUEADO)
						.where("endereco.motivobloqueio = ?",Motivobloqueio.INVENTARIO)
					.closeParentheses()
				.closeParentheses()
				.unique();
	}
	
	/**
	 * 
	 * Verifica se há intersecção dos endereços já cadastrados com os endereços presentes no filtro
	 * 
	 * @author Arantes
	 * 
	 * @param _filtro (Endereco)
	 * @return Boolean
	 * 
	 */
	public Boolean hasInterseccaoEndereco(Endereco _filtro) {
		if(_filtro == null)
			throw new WmsException("Os parâmetros não deve ser nulos");
		
		Endereco endereco = (Endereco) _filtro;
		
		QueryBuilder<Long> query = newQueryBuilderWithFrom(Long.class)
		.select("count (endereco.cdendereco)")
		.join("endereco.area area")
		.join("endereco.tipoendereco tipoendereco")
		.join("endereco.enderecostatus enderecostatus")
		.join("endereco.tipoestrutura tipoestrutura")
		.join("endereco.enderecofuncao enderecofuncao")
		.where("area = ?", endereco.getArea())
		.whereIntervalMatches("endereco.rua", "endereco.rua", endereco.getRuaI(), endereco.getRuaF())
		.whereIntervalMatches("endereco.predio", "endereco.predio", endereco.getPredioI(), endereco.getPredioF())
		.whereIntervalMatches("endereco.nivel", "endereco.nivel", endereco.getNivelI(), endereco.getNivelF())
		.whereIntervalMatches("endereco.apto", "endereco.apto", endereco.getAptoI(), endereco.getAptoF())
		.setUseTranslator(false);
		
		if (_filtro.getLado() != null){
			if(_filtro.getLado())
				query.where("mod(endereco.predio, 2) = 0");
			
			else 
				query.where("mod(endereco.predio, 2) <> 0");
		}
		
		Long result = query.unique();
		
		if(result.longValue() == 0)
			return Boolean.FALSE;
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Verifica se há intersecção dos endereços cadastrados com os endereços presentes no filtro
	 * 
	 * @author Arantes
	 * 
	 * @param _filtro (EnderecoFiltro)
	 * @return Boolean
	 * 
	 */
	public Boolean hasInterseccaoEnderecofiltro(EnderecoFiltro _filtro) {
		if(_filtro == null)
			throw new WmsException("Os parâmetros não deve ser nulos");
		
		EnderecoFiltro enderecofiltro = (EnderecoFiltro) _filtro;
		
		QueryBuilder<Long> query = newQueryBuilderWithFrom(Long.class)
		.select("count (endereco.cdendereco)")
		.join("endereco.area area")
		.join("endereco.tipoendereco tipoendereco")
		.join("endereco.enderecostatus enderecostatus")
		.join("endereco.tipoestrutura tipoestrutura")
		.join("endereco.enderecofuncao enderecofuncao")
		.where("area = ?", enderecofiltro.getArea())
		.whereIntervalMatches("endereco.rua", "endereco.rua", enderecofiltro.getRuaI(), enderecofiltro.getRuaF())
		.whereIntervalMatches("endereco.predio", "endereco.predio", enderecofiltro.getPredioI(), enderecofiltro.getPredioF())
		.whereIntervalMatches("endereco.nivel", "endereco.nivel", enderecofiltro.getNivelI(), enderecofiltro.getNivelF())
		.whereIntervalMatches("endereco.apto", "endereco.apto", enderecofiltro.getAptoI(), enderecofiltro.getAptoF())
		.setUseTranslator(false);
		
		if(enderecofiltro.getLado() != null) {
			if(enderecofiltro.getLado())
				query
					.where("mod(endereco.predio, 2) = 0");
			
			else if(!enderecofiltro.getLado())
				query
					.where("mod(endereco.predio, 2) <> 0");
			
		}
		
		Long result = query.unique();
		
		if(result.longValue() == 0)
			return Boolean.FALSE;
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Método que atualiza um endereço no banco de dados
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @param enderecosModificados 
	 * 
	 */
	public void atualizarEndereco(Endereco endereco, List<Endereco> enderecosModificados) {
		if((endereco == null) || (endereco.getArea() == null) || (endereco.getArea().getCdarea() == null) || 
		   (endereco.getRuaI() == null) || 
		   (endereco.getPredioI() == null) || 
		   (endereco.getNivelI() == null) || 
		   (endereco.getAptoI() == null) || 
		   (endereco.getRuaF() == null) || 
		   (endereco.getPredioF() == null) || 
		   (endereco.getNivelF() == null) || 
		   (endereco.getAptoF() == null) || 
		   (endereco.getTipoestrutura() == null) || (endereco.getTipoestrutura().getCdtipoestrutura() == null) ||
		   (endereco.getEnderecofuncao() == null) || (endereco.getEnderecofuncao().getCdenderecofuncao() == null) || 
		   (endereco.getTipoendereco() == null) || (endereco.getTipoendereco().getCdtipoendereco() == null) || 
		   (endereco.getTipopalete() == null) || (endereco.getTipopalete().getCdtipopalete() == null) ||
		   (endereco.getLarguraexcedente() == null))		   
				throw new WmsException("Parâmetros inválidos.");
	
		String sentenca = "";
		if(endereco.getLado() != null) {
			if(endereco.getLado())
				sentenca += "and mod(endereco.predio, 2) = 0";
			
			else if(!endereco.getLado())
				sentenca += "and mod(endereco.predio, 2) <> 0";
		}
		
		getHibernateTemplate().bulkUpdate("update Endereco endereco set " +
											"endereco.tipoestrutura.id = ?, " +
											"endereco.enderecofuncao.id = ?, " +
											"endereco.tipoendereco.id = ?, " +
											"endereco.tipopalete.id = ?, " +
											"endereco.larguraexcedente = ? " +
										  	"where endereco.id in (" +CollectionsUtil.listAndConcatenate(enderecosModificados, "cdendereco",",")+") " +
								  	  sentenca,
						 new Object[]{endereco.getTipoestrutura().getCdtipoestrutura(), 
										endereco.getEnderecofuncao().getCdenderecofuncao(), 
										endereco.getTipoendereco().getCdtipoendereco(), 
										endereco.getTipopalete().getCdtipopalete(), 
										endereco.getLarguraexcedente()}); 
		
		if (endereco.isBloqueado()){
			getHibernateTemplate().bulkUpdate("update Endereco endereco set " +
											"endereco.enderecostatus.id = ?, " +
											"endereco.motivobloqueio.id = ? " +
										  	"where endereco.id in (" +CollectionsUtil.listAndConcatenate(enderecosModificados, "cdendereco",",")+") " +
								  	  sentenca,
						 new Object[]{Enderecostatus.BLOQUEADO.getCdenderecostatus(), 
										Motivobloqueio.ROTINA.getCdmotivobloqueio()}); 
		}else{
			for (Endereco aux : enderecosModificados)
				pAtualizarEndereco(aux);
		}
		
		//excluindo a ligação com linha de separação
		getHibernateTemplate().bulkUpdate("delete from " + EnderecoLinhaseparacao.class.getName() + " enderecolinha where enderecolinha.endereco.id in (select id from Endereco endereco " +
										  "where endereco.area.id = ? " +
									  	  	  "and endereco.rua between ? and ? " +
									  	  	  "and endereco.predio between ? and ? " +
									  	  	  "and endereco.nivel between ? and ? " +
									  	  	  "and endereco.apto between ? and ? " +
									  	  	  sentenca +
									  	  	  " )",
							 new Object[]{endereco.getArea().getCdarea(), 
											endereco.getRuaI(), 
											endereco.getRuaF(), 
											endereco.getPredioI(), 
											endereco.getPredioF(), 
											endereco.getNivelI(), 
											endereco.getNivelF(), 
											endereco.getAptoI(),
											endereco.getAptoF()});
		
		
		if (endereco.getListaLinhaseparacao() != null && !endereco.getListaLinhaseparacao().isEmpty()){
			sentenca += " and linha.cdlinhaseparacao in (";
			for (int i = 0; i < endereco.getListaLinhaseparacao().size(); i++){
				Linhaseparacao linha = endereco.getListaLinhaseparacao().get(i);
				if (i > 0){
					sentenca += ", ";
				}
				
				sentenca += linha.getCdlinhaseparacao();
			}
			
			sentenca += ")";
			
			//Incluindo a ligação com linha de separação
			getHibernateTemplate().bulkUpdate("insert into " + EnderecoLinhaseparacao.class.getName() + "(endereco, linhaseparacao) " +
									" select endereco, linha from Endereco endereco, Linhaseparacao linha " +
									"where endereco.area.id = ? " +
									 	  "and endereco.rua between ? and ? " +
									  	  "and endereco.predio between ? and ? " +
									  	  "and endereco.nivel between ? and ? " +
									  	  "and endereco.apto between ? and ? " +
									 	  sentenca,
								 new Object[]{endereco.getArea().getCdarea(), 
												endereco.getRuaI(), 
												endereco.getRuaF(), 
												endereco.getPredioI(), 
												endereco.getPredioF(), 
												endereco.getNivelI(), 
												endereco.getNivelF(), 
												endereco.getAptoI(),
												endereco.getAptoF()});			
		}
	}
	
	/**
	 * 
	 * Método que exclui um endereço do banco de dados
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @param enderecoFiltro 
	 * 
	 */
	public void excluirEndereco(Endereco endereco, EnderecoFiltro enderecoFiltro) {
		if((endereco == null) || (endereco.getArea() == null) || (endereco.getArea().getCdarea() == null) || 
				   (endereco.getRuaI() == null) || 
				   (endereco.getPredioI() == null) || 
				   (endereco.getNivelI() == null) || 
				   (endereco.getAptoI() == null) ||
				   (endereco.getRuaF() == null) || 
				   (endereco.getPredioF() == null) || 
				   (endereco.getNivelF() == null) || 
				   (endereco.getAptoF() == null))
			throw new WmsException("Parâmetros inválidos.");
		
		StringBuilder sb = new StringBuilder()
		  .append("delete from Endereco endereco ")
		  .append("where endereco.area.id = ? ")
		  .append("and endereco.rua between ? and ? ")
		  .append("and endereco.predio between ? and ? ")
		  .append("and endereco.nivel between ? and ? ")
		  .append("and endereco.apto between ? and ? ");
		
		if(endereco.getLado() != null) {
			if(endereco.getLado())
				sb.append("and mod(endereco.predio, 2) = 0 ");
			
			else if(!endereco.getLado())
				sb.append("and mod(endereco.predio, 2) <> 0 ");
		}
		if(enderecoFiltro.getLarguraexcedente() != null)
			sb.append("and endereco.larguraexcedente = "+endereco.getLarguraexcedente()+" ");
		if(enderecoFiltro.getEnderecofuncao() != null && enderecoFiltro.getEnderecofuncao().getCdenderecofuncao() != null){
			sb.append("and endereco.enderecofuncao.id in (")
			  .append("select ef.id from Enderecofuncao ef ")
			  .append("where ef.id = "+enderecoFiltro.getEnderecofuncao().getCdenderecofuncao()+") ");
		}
		if(enderecoFiltro.getTipoestrutura() != null && enderecoFiltro.getTipoestrutura().getCdtipoestrutura() != null){
			sb.append("and endereco.tipoestrutura.id in (")
			.append("select te.id from Tipoestrutura te ")
			.append("where te.id = "+enderecoFiltro.getTipoestrutura().getCdtipoestrutura()+") ");
		}
		if(enderecoFiltro.getTipoendereco() != null && enderecoFiltro.getTipoendereco().getCdtipoendereco() != null){
			sb.append("and endereco.tipoendereco.id in (")
			.append("select te1.id from Tipoendereco te1 ")
			.append("where te1.id = "+enderecoFiltro.getTipoendereco().getCdtipoendereco()+") ");
		}
		if(enderecoFiltro.getTipopalete() != null && enderecoFiltro.getTipopalete().getCdtipopalete() != null){
			sb.append("and endereco.tipopalete.id in (")
			.append("select tp.id from Tipopalete tp ")
			.append("where tp.id = "+enderecoFiltro.getTipopalete().getCdtipopalete()+") ");
		}
		
		try{
			getHibernateTemplate().bulkUpdate(sb.toString(),
								new Object[]{endereco.getArea().getCdarea(), 
											 endereco.getRuaI(), 
											 endereco.getRuaF(), 
											 endereco.getPredioI(), 
											 endereco.getPredioF(), 
											 endereco.getNivelI(), 
											 endereco.getNivelF(), 
											 endereco.getAptoI(),
											 endereco.getAptoF()});
		}catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ForeignKeyException)
				throw new WmsException("Não foi possível remover endereço. Existe(m) registro(s) vinculado(s) a produtos.");
			else
				throw new WmsException(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * Método que altera a situação entre bloqueado e não bloqueado.
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @param enderecosModificados 
	 * 
	 */
	public void alterarBloqueioEndereco(Endereco endereco, List<Endereco> enderecosModificados) {
		if((endereco == null) || (endereco.getArea() == null) || (endereco.getArea().getCdarea() == null))		   
						throw new WmsException("Parâmetros inválidos.");
			
		String sentenca = "";
		if(endereco.getLado() != null) {
			if(endereco.getLado())
				sentenca += "and mod(endereco.predio, 2) = 0";
			
			else if(!endereco.getLado())
				sentenca += "and mod(endereco.predio, 2) <> 0";
		}
						
		if (endereco.isBloqueado()){
			getHibernateTemplate().bulkUpdate("update Endereco endereco set " +
											"endereco.enderecostatus.id = ?, " +
											"endereco.motivobloqueio.id = ? " +
										  	"where endereco.id in (" +CollectionsUtil.listAndConcatenate(enderecosModificados, "cdendereco",",")+") " +
								  	  sentenca,
						 new Object[]{Enderecostatus.BLOQUEADO.getCdenderecostatus(), 
										Motivobloqueio.ROTINA.getCdmotivobloqueio()}); 
		}else{
			for (Endereco aux : enderecosModificados)
				pAtualizarEndereco(aux);
		}
				
	}
	
	/**
	 * 
	 * Recupera uma lista de endereços específicos já cadastrados de acordo com a área, rua, prédio, nível, apto e lado presentes no filtro
	 * 
	 * @author Arantes
	 * @param enderecoFiltro 
	 * 
	 * @param enderecoFiltro
	 * @return List<Endereco>
	 * 
	 */
	public List<Endereco> recuperarEnderecosCadastrados(Endereco endereco, EnderecoFiltro enderecoFiltro) {		
		if(endereco == null)
			throw new WmsException("O parâmetro não deve ser nulo");
		
		QueryBuilder<Endereco> sentenca = query()
			.join("endereco.area area")
			.leftOuterJoin("endereco.enderecofuncao enderecofuncao")
			.leftOuterJoin("endereco.tipoestrutura tipoestrutura")
			.leftOuterJoin("endereco.tipoendereco tipoendereco")
			.leftOuterJoin("endereco.enderecostatus enderecostatus")
			.leftOuterJoin("endereco.tipopalete tipopalete")
			.where("area = ?", endereco.getArea())
			.whereIntervalMatches("endereco.rua", "endereco.rua", endereco.getRuaI(), endereco.getRuaF())
			.whereIntervalMatches("endereco.predio", "endereco.predio", endereco.getPredioI(), endereco.getPredioF())
			.whereIntervalMatches("endereco.nivel", "endereco.nivel", endereco.getNivelI(), endereco.getNivelF())
			.whereIntervalMatches("endereco.apto", "endereco.apto", endereco.getAptoI(), endereco.getAptoF());
		
			if(endereco.getLado() != null) {
				if(endereco.getLado())
					sentenca.where("mod(endereco.predio, 2) = 0");
				
				else if(!endereco.getLado())
					sentenca.where("mod(endereco.predio, 2) <> 0");
			}
		
		if(enderecoFiltro != null){
			sentenca
			.where("enderecofuncao = ?", enderecoFiltro.getEnderecofuncao())
			.where("tipoestrutura = ?", enderecoFiltro.getTipoestrutura())
			.where("tipoendereco = ?", enderecoFiltro.getTipoendereco())
			.where("tipopalete = ?", enderecoFiltro.getTipopalete())
			.where("endereco.larguraexcedente = ?", enderecoFiltro.getLarguraexcedente())
			.where("enderecostatus = ?", enderecoFiltro.getEnderecostatus());
			
		}
			
		return sentenca.list();
	}
	
	/**
	 * 
	 * Método que recupera uma lista de endereços de acordo com o filtro. 
	 * Faz chamada à procedure ENDERECODISPONIVELPRODUTO no banco de dados.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Endereco>
	 * @throws SQLException 
	 * 
	 */
	public List<Endereco> recuperarEnderecosDestino(EnderecoDestinoFiltro filtro) throws SQLException {
		if((filtro == null) || 
		   (filtro.getArea() == null) || (filtro.getArea().getCodigo() == null) ||
		   (filtro.getRuaI() == null) || (filtro.getPredioI() == null) || (filtro.getNivelI() == null) || (filtro.getAptoI() == null) || 
		   (filtro.getRuaF() == null) || (filtro.getPredioF() == null) || (filtro.getNivelF() == null) || (filtro.getAptoF() == null) ||
		   (filtro.getProduto() == null) || (filtro.getProduto().getCdproduto() == null) ||
		   (filtro.getDeposito() == null) || (filtro.getDeposito().getCddeposito() == null))
				throw new WmsException("Parâmetros inválidos.");
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		ResultSet cursor = null;
		
		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		
		try {
			String sql = "BEGIN PKG_WMS.ENDERECODISPONIVELPRODUTO(:1, :2, :3, :4, :5, :6, :7, :8, :9, :10, :11, :12); END;";
			cstmt = (CallableStatement) conn.prepareCall(sql);
			
			
			cstmt.setInt(2, filtro.getProduto().getCdproduto());
			cstmt.setInt(3, filtro.getDeposito().getCddeposito());
			cstmt.setLong(4, filtro.getArea().getCodigo());
			cstmt.setInt(5, filtro.getRuaI());
			cstmt.setInt(6, filtro.getRuaF());
			cstmt.setInt(7, filtro.getPredioI());
			cstmt.setInt(8, filtro.getPredioF());
			cstmt.setInt(9, filtro.getNivelI());
			cstmt.setInt(10, filtro.getNivelF());
			cstmt.setInt(11, filtro.getAptoI());
			cstmt.setInt(12, filtro.getAptoF());
			
			
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			
			cstmt.execute();
			
			// return the result set
			cursor = (ResultSet) cstmt.getObject(1);
			
			while(cursor.next()) {
				Endereco enderecodestino = new Endereco();
		    	enderecodestino.setCdendereco(cursor.getInt(1));
		    	 
		    	Area area = new Area();
		    	area.setCdarea(cursor.getInt(2));
		    	area.setCodigo(cursor.getLong(5));
		    	 
		    	enderecodestino.setArea(area);
		    	
		    	enderecodestino.setEndereco(cursor.getString(3));
		    	enderecodestino.setLarguraexcedente(cursor.getBoolean(4));
		    	 
		    	Enderecostatus enderecostatus = new Enderecostatus(cursor.getInt(6));
		    	enderecostatus.setNome(cursor.getString(7));
		    	enderecodestino.setEnderecostatus(enderecostatus);
		    	 
		    	Enderecofuncao enderecofuncao = new Enderecofuncao(cursor.getInt(8));
		    	enderecofuncao.setNome(cursor.getString(9));
		    	enderecodestino.setEnderecofuncao(enderecofuncao);
		    	 
		    	listaEndereco.add(enderecodestino);
			}
			
		} catch (Exception e) {
			throw new WmsException("Erro ao executar a procedure ENDERECODISPONIVELPRODUTO." + e.getMessage());
			
		} finally {
			cursor.close();
			cstmt.close();
			conn.close();
		}
		
		return listaEndereco;
	}
	
	/**
	 * Método que recupera a lista de endereços indisponíveis de acordo com o inventário
	 * 
	 * @param inventario
	 * @return List<Endereco>
	 * @throws SQLException
	 */
	public List<Endereco> recuperarEnderecosIndisponiveis(Inventario inventario) throws SQLException {
		if((inventario == null) || (inventario.getCdinventario() == null))
				throw new WmsException("Parâmetros inválidos.");
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		ResultSet cursor = null;
		
		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		
		try {
			String sql = "BEGIN PKG_WMS.INICIAR_INVENTARIO(:1, :2); END;";
			cstmt = (CallableStatement) conn.prepareCall(sql);
			
			
			cstmt.setInt(2, inventario.getCdinventario());
			
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			
			cstmt.execute();
			
			// return the result set
			cursor = (ResultSet) cstmt.getObject(1);
			
			while(cursor.next()) {
				Endereco enderecodestino = new Endereco();
		    	enderecodestino.setCdendereco(cursor.getInt("CDENDERECO"));
		    	 
		    	Area area = new Area();
		    	area.setCdarea(cursor.getInt("CDAREA"));
		    	area.setCodigo(cursor.getLong("CODIGOAREA"));
		    	
		    	 
		    	enderecodestino.setArea(area);
		    	
		    	enderecodestino.setEndereco(cursor.getString("ENDERECO"));
		    	 
		    	Enderecostatus enderecostatus = new Enderecostatus(cursor.getInt("CDENDERECOSTATUS"));
		    	enderecostatus.setNome(cursor.getString("ENDERECOSTATUS"));
		    	enderecodestino.setEnderecostatus(enderecostatus);
		    	
		    	enderecodestino.setQtde(cursor.getInt("QTDE"));
		    	enderecodestino.setReservaentrada(cursor.getInt("RESERVAENTRADA"));
		    	enderecodestino.setReservasaida(cursor.getInt("RESERVASAIDA"));
		    	
		    	listaEndereco.add(enderecodestino);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WmsException("Erro ao executar a procedure INICIAR_INVENTARIO." + e.getMessage());
			
		} finally {
			
			if(cursor != null)
				cursor.close();
			if(cstmt != null)
				cstmt.close();
			if(conn != null)
				conn.close();
		}
		
		return listaEndereco;
	}
	
	
	/**
	 * Query para verificar quantas etiquetas serão
	 * geradas
	 * @param endereco
	 * @return
	 * @author Cíntia Nogueira
	 * @param enderecoFiltro 
	 */
	public Long findfindForReportEtiquetaEnderecosCount(Endereco endereco, EnderecoFiltro enderecoFiltro){
		if(endereco == null)
			throw new WmsException("O endereço não deve ser nulo.");
		
		QueryBuilder<Long> query = newQueryBuilderWithFrom(Long.class);
			query
				.from(Endereco.class)
					.select(" count (endereco.cdendereco)")
					.join("endereco.area area")
					.join("endereco.enderecofuncao enderecofuncao")
					.leftOuterJoin("endereco.tipoestrutura tipoestrutura")
					.leftOuterJoin("endereco.tipoendereco tipoendereco")
					.leftOuterJoin("endereco.tipopalete tipopalete")
					.leftOuterJoin("endereco.enderecostatus enderecostatus")
					.leftOuterJoin("endereco.listaDadologistico listaDadologistico")
					.leftOuterJoin("listaDadologistico.produto produto")
					.leftOuterJoin("produto.produtoprincipal pp")
					.where("endereco.rua >= ?",endereco.getRuaI())
					.where("endereco.rua <= ?",endereco.getRuaF())
					.where("endereco.predio >= ?",endereco.getPredioI())
					.where("endereco.predio <= ?",endereco.getPredioF())
					.where("endereco.nivel >= ?",endereco.getNivelI())
					.where("endereco.nivel <= ?",endereco.getNivelF())
					.where("endereco.apto >= ?",endereco.getAptoI())
					.where("endereco.apto <= ?",endereco.getAptoF())
					.where("endereco.area = ?",endereco.getArea())
					.where("endereco.enderecofuncao = ?",endereco.getEnderecofuncao())
					.where("enderecofuncao = ?", endereco.getEnderecofuncao())
					.where("endereco.tipoestrutura = ?", endereco.getTipoestrutura())
					.where("endereco.tipoendereco = ?", endereco.getTipoendereco())
					.where("endereco.larguraexcedente = ?", endereco.getLarguraexcedente())
					.where("endereco.enderecostatus = ?", endereco.getEnderecostatus())
					.where("endereco.tipopalete=?", endereco.getTipopalete())
					.orderBy("enderecofuncao.cdenderecofuncao,area.codigo,endereco.endereco")
					.setUseTranslator(false);
			
			if(endereco.getLado() != null) {
				if(endereco.getLado())
					query
						.where("mod(endereco.predio, 2) = 0");
				
				else if(!endereco.getLado())
					query
						.where("mod(endereco.predio, 2) <> 0");
			}
			
			if(enderecoFiltro != null){
				query
				.where("enderecofuncao = ?", enderecoFiltro.getEnderecofuncao())
				.where("tipoestrutura = ?", enderecoFiltro.getTipoestrutura())
				.where("tipoendereco = ?", enderecoFiltro.getTipoendereco())
				.where("tipopalete = ?", enderecoFiltro.getTipopalete())
				.where("endereco.larguraexcedente = ?", enderecoFiltro.getLarguraexcedente())
				.where("enderecostatus = ?", enderecoFiltro.getEnderecostatus());
			}
		return query.unique();		
	}
	
	/**
	 * 
	 * Busca o endereco de uma determinada área
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @param enderecoFiltro 
	 * @return
	 */
	public List<Endereco> findForReportEtiquetaEnderecos(Endereco endereco, EnderecoFiltro enderecoFiltro) {
		
		if(endereco == null)
			throw new WmsException("O endereço não deve ser nulo.");
		
		QueryBuilder<Endereco> query = query();
			query
					.select("endereco.cdendereco,endereco.endereco,endereco.rua,endereco.predio," +
							"endereco.nivel,endereco.apto,area.cdarea,area.codigo,enderecofuncao.cdenderecofuncao," +
							"listaDadologistico.cddadologistico,produto.cdproduto,produto.codigo,produto.descricao," +
							"produto.complementocodigobarras,pp.cdproduto,pp.codigo,pp.descricao")
					.join("endereco.area area")
					.join("endereco.enderecofuncao enderecofuncao")
					.leftOuterJoin("endereco.tipoestrutura tipoestrutura")
					.leftOuterJoin("endereco.tipoendereco tipoendereco")
					.leftOuterJoin("endereco.tipopalete tipopalete")
					.leftOuterJoin("endereco.enderecostatus enderecostatus")
					.leftOuterJoin("endereco.listaDadologistico listaDadologistico")
					.leftOuterJoin("listaDadologistico.produto produto")
					.leftOuterJoin("produto.produtoprincipal pp")
					.where("endereco.rua >= ?",endereco.getRuaI())
					.where("endereco.rua <= ?",endereco.getRuaF())
					.where("endereco.predio >= ?",endereco.getPredioI())
					.where("endereco.predio <= ?",endereco.getPredioF())
					.where("endereco.nivel >= ?",endereco.getNivelI())
					.where("endereco.nivel <= ?",endereco.getNivelF())
					.where("endereco.apto >= ?",endereco.getAptoI())
					.where("endereco.apto <= ?",endereco.getAptoF())
					.where("endereco.area = ?",endereco.getArea())
					.where("endereco.enderecofuncao = ?",endereco.getEnderecofuncao())
					.where("enderecofuncao = ?", endereco.getEnderecofuncao())
					.where("endereco.tipoestrutura = ?", endereco.getTipoestrutura())
					.where("endereco.tipoendereco = ?", endereco.getTipoendereco())
					.where("endereco.larguraexcedente = ?", endereco.getLarguraexcedente())
					.where("endereco.enderecostatus = ?", endereco.getEnderecostatus())
					.where("endereco.tipopalete=?", endereco.getTipopalete())
					.orderBy("enderecofuncao.cdenderecofuncao,area.codigo,endereco.endereco")
					.list();
			
			if(endereco.getLado() != null) {
				if(endereco.getLado())
					query
						.where("mod(endereco.predio, 2) = 0");
				
				else if(!endereco.getLado())
					query
						.where("mod(endereco.predio, 2) <> 0");
				
			}
			
			if(enderecoFiltro != null){
				query
				.where("enderecofuncao = ?", enderecoFiltro.getEnderecofuncao())
				.where("tipoestrutura = ?", enderecoFiltro.getTipoestrutura())
				.where("tipoendereco = ?", enderecoFiltro.getTipoendereco())
				.where("tipopalete = ?", enderecoFiltro.getTipopalete())
				.where("endereco.larguraexcedente = ?", enderecoFiltro.getLarguraexcedente())
				.where("enderecostatus = ?", enderecoFiltro.getEnderecostatus());
			}
		return query.list();
	}
	
	/**
	 * Busca os números de enderecos do depósito logado
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @return
	 */
	public Long getNumeroEnderecosDeposito() {
		return newQueryBuilderWithFrom(Long.class)
			   .select("count( distinct endereco.cdendereco )")
			   .join("endereco.area area")
			   .join("area.deposito deposito")
			   .where("deposito = ?",WmsUtil.getDeposito())
			   .setUseTranslator(false)
			   .unique();
	}
	
	/**
	 * Busca o número de endereços válidos do lote
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventariolote
	 * @return
	 */
	public Long getNumeroEnderecosLote(IntervaloEndereco inventariolote) {
		return newQueryBuilderWithFrom(Long.class)
					   .select("count( distinct endereco.cdendereco )")
					   .join("endereco.area area")
					   .where("area = ?",inventariolote.getArea())
					   .whereIntervalMatches("endereco.rua", "endereco.rua", inventariolote.getRuainicial(), inventariolote.getRuafinal())
					   .whereIntervalMatches("endereco.predio", "endereco.predio", inventariolote.getPredioinicial(), inventariolote.getPrediofinal())
					   .whereIntervalMatches("endereco.nivel", "endereco.nivel", inventariolote.getNivelinicial(), inventariolote.getNivelfinal())
					   .whereIntervalMatches("endereco.apto", "endereco.apto", inventariolote.getAptoinicial(), inventariolote.getAptofinal())
					   .setUseTranslator(false)
					   .unique();
	}
	
	/**
	 * Busca todos os endereços do lote
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param inventariolote
	 * @return
	 */
	public List<Endereco> getEnderecosByInventarioLote(Inventariolote inventariolote) {
		return query()
					.select("endereco.cdendereco,endereco.endereco,endereco.larguraexcedente," +
							"motivobloqueio.cdmotivobloqueio,area.cdarea,area.codigo, " +
							"endereco.apto,enderecoproduto.cdenderecoproduto, produto.cdproduto, " +
							"dadologistico.cddadologistico, dadologistico.larguraexcedente, " +
							"deposito.cddeposito ")
					.join("endereco.area area")		
					.leftOuterJoin("endereco.motivobloqueio motivobloqueio")
					.leftOuterJoin("endereco.listaEnderecoproduto enderecoproduto")
					.leftOuterJoin("enderecoproduto.produto produto")
					.leftOuterJoin("produto.listaDadoLogistico dadologistico")
					.leftOuterJoin("dadologistico.deposito deposito")					
				    .where("area = ?",inventariolote.getArea())
					.whereIntervalMatches("endereco.rua", "endereco.rua", inventariolote.getRuainicial(), inventariolote.getRuafinal())
				    .whereIntervalMatches("endereco.predio", "endereco.predio", inventariolote.getPredioinicial(), inventariolote.getPrediofinal())
				    .whereIntervalMatches("endereco.nivel", "endereco.nivel", inventariolote.getNivelinicial(), inventariolote.getNivelfinal())
				    .whereIntervalMatches("endereco.apto", "endereco.apto", inventariolote.getAptoinicial(), inventariolote.getAptofinal())
				    .orderBy("endereco.endereco")
				    .list()
					;
		
	}
	
	/**
	 * Encontra o endereço com os dados fornecidos
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @param area
	 * @return
	 */
	public Endereco findEndereco(String endereco, Area area) {
		if(endereco == null || endereco.equals("") || area == null || area.getCdarea() == null)
			throw new WmsException("A área nao deve ser nula.");
		return query()
				.select("endereco.cdendereco,endereco.endereco,endereco.larguraexcedente," +
						"endereco.rua,endereco.predio,endereco.nivel,endereco.apto," +
						"motivobloqueio.cdmotivobloqueio,enderecostatus.cdenderecostatus")
				.join("endereco.enderecostatus enderecostatus")
				.join("endereco.area area")
				.leftOuterJoin("endereco.motivobloqueio motivobloqueio")
			    .where("area = ?",area)
			    .where("endereco.endereco = ?",endereco)
			    .setMaxResults(1)
			    .unique();
	}
	
	/**
	 * Método que carrega o endereço a partir dos paramentros area, rua, predio, nivel e apto.
	 * 
	 * @param endereco
	 * @return
	 * @author Giovane Freitas
	 */
	public Endereco findEndereco(Endereco endereco, Deposito deposito) {
		if((endereco == null) || (endereco.getArea() == null) ||
				(endereco.getRua() == null) || (endereco.getPredio() == null) || 
				(endereco.getNivel() == null) || (endereco.getApto() == null))
			throw new WmsException("Parâmetros inválidos.");
		
		QueryBuilder<Endereco> queryBuilder = query()
				.select("endereco.cdendereco, endereco.endereco, endereco.larguraexcedente, " +
						"area.cdarea, area.codigo, area.avaria, area.box, area.virtual, " +
						"enderecostatus.cdenderecostatus, listaDadologistico.cddadologistico, produto.cdproduto, " +
						"enderecofuncao.cdenderecofuncao,endereco.rua,endereco.predio,endereco.nivel,endereco.apto")
				.join("endereco.area area")
				.join("endereco.enderecostatus enderecostatus")
				.leftOuterJoin("endereco.enderecofuncao enderecofuncao")
				.leftOuterJoin("endereco.listaDadologistico listaDadologistico")
				.leftOuterJoin("listaDadologistico.produto produto")
				.where("endereco.rua = ?", endereco.getRua())
				.where("endereco.predio = ?", endereco.getPredio())
				.where("endereco.nivel = ?", endereco.getNivel())
				.where("endereco.apto = ?", endereco.getApto())
				.where("area.deposito = ?", deposito);
		
		if (endereco.getArea().getCdarea() != null)
			queryBuilder.where("area = ?", endereco.getArea());
		else
			queryBuilder.where("area.codigo = ?", endereco.getArea().getCodigo());

		return queryBuilder.unique();
	}
	
	/**
	 * 
	 * Encontra um endereço.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return Endereco
	 * 
	 */
	public Endereco findEndereco(Endereco filtro) {
		if(!EnderecoAux.isEnderecoCompleto(filtro))
			throw new WmsException("Parâmetros inválidos.");
	
		return query()
					.select("endereco.cdendereco, endereco.endereco, endereco.larguraexcedente, " +
							"endereco.rua,endereco.predio,endereco.nivel, endereco.apto, area.cdarea, " +
							"area.codigo, area.box, area.avaria, area.virtual, " +
							"enderecofuncao.cdenderecofuncao, dadologistico.cddadologistico," +
							"produto.cdproduto,produto.descricao,enderecostatus.cdenderecostatus")
					.join("endereco.area area")
					.leftOuterJoin("endereco.listaDadologistico dadologistico")
					.leftOuterJoin("dadologistico.produto produto")
					.join("endereco.enderecofuncao enderecofuncao")
					.join("endereco.enderecostatus enderecostatus")
					.join("area.deposito deposito")
					.where("area.codigo = ?", filtro.getArea().getCodigoAE())
					.where("deposito = ?", WmsUtil.getDeposito())
					.where("endereco.rua = ?", filtro.getRuaI())
					.where("endereco.predio = ?", filtro.getPredioI())
					.where("endereco.nivel = ?", filtro.getNivelI())
					.where("endereco.apto = ?", filtro.getAptoI())
					.unique();
	}
	
	/**
	 * Carrega o endereço
	 * @param endereco
	 * @return
	 * @author Cíntia Nogueira
	 */
	public Endereco loadEndereco(Endereco endereco ){
		
		if(endereco==null || endereco.getArea()==null || endereco.getArea().getCodigoAE()==null ||
				endereco.getRuaI()==null || endereco.getPredioI()==null || endereco.getNivelI()==null||
				endereco.getAptoI()==null){
			throw new WmsException("Parâmetros inválidos em EnderecoDAO");
		}
		return query()
		.select("endereco.cdendereco, endereco.endereco, area.cdarea, area.codigo, endereco.larguraexcedente," +
				"endereco.rua, endereco.predio, endereco.nivel, endereco.apto,enderecofuncao.cdenderecofuncao, " +
				"enderecofuncao.nome, enderecostatus.cdenderecostatus, enderecostatus.nome, " +
				"enderecostatus.bloqueado,tipoestrutura.cdtipoestrutura,tipoestrutura.nome,area.virtual,area.box")
		.join("endereco.area area")
		.join("endereco.enderecofuncao enderecofuncao")
		.join("endereco.enderecostatus enderecostatus")
		.join("endereco.tipoestrutura tipoestrutura")
		.where("area.codigo = ?", endereco.getArea().getCodigoAE())		
		.where("endereco.rua = ?", endereco.getRuaI())
		.where("endereco.predio = ?", endereco.getPredioI())
		.where("endereco.nivel = ?", endereco.getNivelI())
		.where("endereco.apto = ?", endereco.getAptoI())
		.where("area.deposito = ?", WmsUtil.getDeposito())
		.unique();
	}
	
	/**
	 * Atualiza o status do endereço
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 */
	public void updateStatusEndereco(Endereco endereco) {
		if(endereco == null || endereco.getCdendereco() == null || endereco.getEnderecostatus() == null 
		   || endereco.getEnderecostatus().getCdenderecostatus() == null)
			throw new WmsException("O endereço não deve ser nulo.");
		getHibernateTemplate().bulkUpdate("update Endereco endereco set endereco.enderecostatus = ? where endereco = ?",
										new Object[]{endereco.getEnderecostatus(),endereco});
	}
	
	/**
	 * Atualiza o motivo do bloqueio do endereço do endereço
	 * 
	 * Obs: Este método aceita que o motivo seja nulo
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 */
	public void updateMotivoBloqueioEndereco(Endereco endereco) {
		if(endereco == null || endereco.getCdendereco() == null)
			throw new WmsException("O endereço não deve ser nulo.");
		
		String query = "update Endereco endereco set endereco.motivobloqueio = ? where endereco.id = ?";
		Object[] objects = new Object[]{endereco.getMotivobloqueio(),endereco.getCdendereco()};
		
		if(endereco.getMotivobloqueio() == null || endereco.getMotivobloqueio().getCdmotivobloqueio() == null){
			query = "update Endereco endereco set endereco.motivobloqueio = null where endereco.id = ?";
			objects = new Object[]{endereco.getCdendereco()};
		}
		
		
		getHibernateTemplate().bulkUpdate(query,objects);
	}
	
	/**
	 * Busca um endereco do prédio( o primeiro ou o ultimo)
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param primeiro
	 * @return
	 */
	public Endereco findEnderecoPredio(Integer rua,Integer predio,Area area,boolean primeiro) {
		if(rua == null || predio == null || area == null || area.getCdarea() == null)
			throw new WmsException("Dados inválidos para executar a função findEnderecoPredio");
		
		QueryBuilder<Endereco> query = query()
											.select("endereco.cdendereco,endereco.endereco,endereco.rua,endereco.predio,endereco.nivel," +
											"endereco.apto,enderecofuncao.cdenderecofuncao")
											.join("endereco.area area")
											.join("endereco.enderecofuncao enderecofuncao")
											.where("area = ?",area)
											.where("endereco.rua = ?",rua)
											.where("endereco.predio = ?",predio)
											.where("enderecofuncao = ?",Enderecofuncao.BLOCADO)
											.setMaxResults(1);
		
		if(primeiro)
			query.orderBy("endereco.endereco asc");
		else
			query.orderBy("endereco.endereco desc");
		
		return query.unique();
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Endereco> query) {
		query.joinFetch("endereco.tipoendereco tipoendereco");
		query.joinFetch("endereco.enderecofuncao enderecofuncao");
		query.joinFetch("endereco.enderecostatus enderecostatus");
		query.joinFetch("endereco.tipoestrutura tipoestrutura");
		query.joinFetch("endereco.area area");
		query.joinFetch("area.deposito deposito");
		super.updateEntradaQuery(query);
	}
	
	/**
	 * 
	 * Recupera uma lista de endereços para o ajuste de estoque.<br/>
	 * Ante de executar a consulta será feito um teste para validar o número de registros que 
	 * serão retornados, caso o parâmetro limite seja maior que zero, será feito um select count(*),
	 * se o count for maior que o limite uma exceção será lançada.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @param limite O número máximo de registros que será permitido pela consulta.
	 * @return List<Endereco>
	 * 
	 */
	public List<Endereco> findByEnderecoProduto(ConsultarEstoqueFiltro filtro, int limite) {
		if (filtro == null)
			throw new WmsException("É necessário informar um filtro.");
			
		if(filtro.getArea() == null && filtro.getProduto() == null && filtro.getEtiquetaUma() == null){
			if (filtro.getRuainicial() == null)
				throw new WmsException("É necessário informar no mínimo a área e a rua inicial ou o produto.");
			else
				throw new WmsException("É necessário informar a área.");
		}
		
		
		QueryBuilder<Endereco> queryBuilder = query().select("area.cdarea, area.codigo, area.box, endereco.endereco," +
				"endereco.cdendereco, endereco.rua, endereco.predio, endereco.nivel, endereco.apto," +
				"enderecoproduto.cdenderecoproduto, enderecoproduto.qtde, enderecoproduto.qtdereservadaentrada, " +
				"enderecoproduto.qtdereservadasaida, enderecoproduto.dtentrada, enderecoproduto.uma, " +
				"produto.cdproduto, produto.codigo, produto.descricao, enderecofuncao.cdenderecofuncao, enderecofuncao.nome," +
				"produtoprincipal.cdproduto,produtoprincipal.descricao,enderecostatus.cdenderecostatus")
			.leftOuterJoin("endereco.listaEnderecoproduto enderecoproduto")
			.join("endereco.area area")
			.leftOuterJoin("enderecoproduto.produto produto")
			.leftOuterJoin("produto.produtoprincipal produtoprincipal")
			.join("endereco.enderecofuncao enderecofuncao")
			.join("endereco.enderecostatus enderecostatus")
			.where("area = ?", filtro.getArea())
			.where("enderecoproduto.cdenderecoproduto = ?", filtro.getEtiquetaUma())
			.where("area.deposito=?", WmsUtil.getDeposito())
			.openParentheses()
				.where("produto = ?", filtro.getProduto())
				.or()
				.where("produtoprincipal = ?", filtro.getProduto())
			.closeParentheses();

		if (filtro.getRuafinal() != null){
			queryBuilder.where("endereco.rua >= ?", filtro.getRuainicial())
				.where("endereco.rua <= ?", filtro.getRuafinal())
				.where("endereco.predio >= ?", filtro.getPredioinicial())
				.where("endereco.predio <= ?", filtro.getPrediofinal())
				.where("endereco.nivel >= ?", filtro.getNivelinicial())
				.where("endereco.nivel <= ?", filtro.getNivelfinal())
				.where("endereco.apto >= ?", filtro.getAptoinicial())
				.where("endereco.apto <= ?", filtro.getAptofinal());
		}else{
			queryBuilder.where("endereco.rua = ?", filtro.getRuainicial())
				.where("endereco.predio = ?", filtro.getPredioinicial())
				.where("endereco.nivel = ?", filtro.getNivelinicial())
				.where("endereco.apto = ?", filtro.getAptoinicial());
		}
		
		if (filtro.getEnderecolado() != null){
			if (filtro.getEnderecolado().equals(Enderecolado.PAR))
				queryBuilder.where("mod(endereco.predio, 2) = 0");
			else
				queryBuilder.where("mod(endereco.predio, 2) <> 0");
		}

		
		if (limite > 0 && WmsUtil.getNumeroRegistros(queryBuilder) > limite)
			throw new WmsException("Informe mais detalhes para o filtro, o número de endereços retornados excedeu o limite.");
			
		return queryBuilder.orderBy("area.codigo, endereco.endereco").list();

	}
	
	/**
	 * Busca um endereço não bloqueado através dos parâmetros passados. <br/>Caso o parâmetro <b>enderecofuncao</b> seja igual a <i>BLOCADO</i>
	 * a consulta será feita tratando especialmente este tipo de endereço, para
	 * qualquer outro tipo ou para <code>null</code> a consulta irá procurar
	 * por um endereço comun comparando <i>RUA.PREDIO.NIVEL.APTO</i>.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param enderecoComPonto
	 * @param codigoArea
	 * @param deposito
	 * @param enderecofuncao Permite realizar uma consulta especial para o caso de endereços blocados.
	 * 
	 * @return
	 */
	public Endereco findByEndereco(String endereco, Long codigoArea,Deposito deposito, Enderecofuncao enderecofuncao) {
		if(endereco == null || codigoArea == null)
			throw new WmsException("O endereço não deve ser nulo.");
		
		QueryBuilder<Endereco> builder = query().select("endereco.cdendereco,endereco.endereco,endereco.larguraexcedente," +
					 		"endfunc.cdenderecofuncao,endprod.cdenderecoproduto,enderecostatus.cdenderecostatus" +
					 		"produto.cdproduto,area.cdarea,area.codigo,endereco.rua,endereco.predio")
					 .join("endereco.enderecofuncao endfunc")
					 .join("endereco.area area")
					 .join("area.deposito deposito")
					 .join("endereco.enderecostatus enderecostatus")
					 .leftOuterJoin("endereco.listaEnderecoproduto endprod")
					 .leftOuterJoin("endprod.produto produto")
					 .leftOuterJoin("produto.listaDadoLogistico dl")
					 .where("enderecostatus <> ?",Enderecostatus.BLOQUEADO)
					 .where("area.codigo = ?",codigoArea)
					 .where("deposito = ?",deposito)
					 .setMaxResults(1);
		
		
		if (Enderecofuncao.BLOCADO.equals(enderecofuncao)){
			EnderecoAux enderecoAux = new EnderecoAux(codigoArea.toString(), endereco);
			builder.where("endereco.rua = ?", Integer.parseInt(enderecoAux.getRu()));
			builder.where("endereco.predio = ?", Integer.parseInt(enderecoAux.getPr()));
		}else
			builder.where("endereco.endereco = ?",endereco);
	
		return builder.unique();
	}
	
	/**
	 * Busca um endereco blocado para o endereçamento UMA 
	 * de acordo com a regra
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @param restricao
	 * @return
	 */
	public Endereco findBlocadoForUMA(Endereco endereco, int restricao) {
		if(endereco == null || endereco.getRua() == null || endereco.getPredio() == null
		   || endereco.getArea() == null || endereco.getArea().getCdarea() == null)
			throw new WmsException("O endereco e a área não devem ser nulos.");
		
		return query()
				.select("endereco.cdendereco,endereco.endereco,endereco.larguraexcedente," +
					 		"endfunc.cdenderecofuncao,endprod.cdenderecoproduto," +
					 		"produto.cdproduto,area.cdarea,area.codigo")
				 .join("endereco.enderecofuncao endfunc")
				 .join("endereco.area area")
				 .join("endereco.enderecostatus enderecostatus")
				 .leftOuterJoin("endereco.listaEnderecoproduto endprod")
				 .leftOuterJoin("endprod.produto produto")
				 .where("enderecostatus <> ?",Enderecostatus.BLOQUEADO)
				 .where("endereco.rua = ?",endereco.getRua())
				 .where("endereco.predio = ?",endereco.getPredio())
				 .where("area = ?",endereco.getArea())
				 .orderBy("enderecostatus.id, apto desc, nivel")
				 .setMaxResults(1)
				 .unique();
	}
	
	/**
	 * Chama a procedure para atualizar o endereço
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param endereco
	 * @throws SQLException 
	 */
	public void pAtualizarEndereco(final Endereco endereco) {
		if(endereco == null || endereco.getCdendereco() == null)
			throw new WmsException("Erro ao executar a procedure ATUALIZAR_ENDERECO.");
		
		try{
			getHibernateTemplate().execute(new HibernateCallback(){

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					session.getNamedQuery("atualizar_endereco").setInteger(0, endereco.getCdendereco()).executeUpdate();
					
					return null;
				}
				
			});
		}catch(Exception e){
			e.printStackTrace();

			Pattern padrao = Pattern.compile(".*###(.*)###.*",Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
			Matcher matcher = padrao.matcher(e.getMessage());
			String msgRetorno;
			if (matcher.matches()) {
				msgRetorno = matcher.group(1);
			}
			else {
				msgRetorno = e.getMessage();
			}

			
			throw new WmsException("Erro ao atualizar o endereço.\n" + msgRetorno);
		}
	}
	
	/**
	 * 
	 * Método que recupera uma lista de endereços de acordo com o filtro. 
	 * Faz chamada à procedure ENDERECODISPONIVELPRODUTO no banco de dados.
	 * 
	 * @author Cíntia Nogueira
	 * 
	 * @param filtro
	 * @return List<Endereco>
	 * @throws SQLException 
	 * 
	 */
	public List<Endereco> recuperarEnderecosDestino(BuscarEnderecoPopupFiltro filtro) throws SQLException {
		if((filtro == null) || 
		   (filtro.getArea() == null) || 
		   (filtro.getRuaI() == null) || (filtro.getPredioI() == null) || (filtro.getNivelI() == null) || (filtro.getAptoI() == null) || 
		   (filtro.getRuaF() == null) || (filtro.getPredioF() == null) || (filtro.getNivelF() == null) || (filtro.getAptoF() == null) ||
		   (filtro.getProduto() == null) || (filtro.getProduto().getCdproduto() == null) ||
		   (filtro.getDeposito() == null) || (filtro.getDeposito().getCddeposito() == null))
				throw new WmsException("Parâmetros inválidos.");
		
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement cstmt = null;  
		ResultSet cursor = null;
		
		List<Endereco> listaEndereco = new ArrayList<Endereco>();
		
		try {
			String sql = "BEGIN PKG_WMS.ENDERECODISPONIVELPRODUTO(:1, :2, :3, :4, :5, :6, :7, :8, :9, :10, :11, :12); END;";
			cstmt = (CallableStatement) conn.prepareCall(sql);
			
			
			cstmt.setInt(2, filtro.getProduto().getCdproduto());
			cstmt.setInt(3, filtro.getDeposito().getCddeposito());
			cstmt.setLong(4, filtro.getArea());
			cstmt.setInt(5, filtro.getRuaI());
			cstmt.setInt(6, filtro.getRuaF());
			cstmt.setInt(7, filtro.getPredioI());
			cstmt.setInt(8, filtro.getPredioF());
			cstmt.setInt(9, filtro.getNivelI());
			cstmt.setInt(10, filtro.getNivelF());
			cstmt.setInt(11, filtro.getAptoI());
			cstmt.setInt(12, filtro.getAptoF());
			
			
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			
			cstmt.execute();
			
			// return the result set
			cursor = (ResultSet) cstmt.getObject(1);
			
			while(cursor.next()) {
				Endereco enderecodestino = new Endereco();
		    	enderecodestino.setCdendereco(cursor.getInt(1));
		    	 
		    	Area area = new Area();
		    	area.setCdarea(cursor.getInt(2));
		    	area.setCodigo(cursor.getLong(5));
		    	 
		    	enderecodestino.setArea(area);
		    	
		    	enderecodestino.setEndereco(cursor.getString(3));
		    	enderecodestino.setLarguraexcedente(cursor.getBoolean(4));
		    	 
		    	Enderecostatus enderecostatus = new Enderecostatus(cursor.getInt(6));
		    	enderecostatus.setNome(cursor.getString(7));
		    	enderecodestino.setEnderecostatus(enderecostatus);
		    	 
		    	Enderecofuncao enderecofuncao = new Enderecofuncao(cursor.getInt(8));
		    	enderecofuncao.setNome(cursor.getString(9));
		    	enderecodestino.setEnderecofuncao(enderecofuncao);
		    	 
		    	listaEndereco.add(enderecodestino);
			}
			
		} catch (Exception e) {
			throw new WmsException("Erro ao executar a procedure ENDERECODISPONIVELPRODUTO." + e.getMessage());
			
		} finally {
			cursor.close();
			cstmt.close();
			conn.close();
		}
		
		return listaEndereco;
	}

	/**
	 * Método que busca o endereço de avaria a partir da área. 
	 * Pode ter a situação remota de ter 2 endereços de avaria para a mesma area por isso foi colocado o setMaxResults
	 * 
	 * @param area
	 * @return
	 * @author Tomás Rabelo
	 */
	public Endereco getEnderecoAvariaByArea(String area) {
		if(area==null || area.equals(""))
			throw new WmsException("A area não pode ser nula.");
		
		return query()
			.select("endereco.cdendereco, endereco.rua, endereco.predio, endereco.nivel, endereco.apto")
			.join("endereco.area area")
			.where("area.codigo = ?", new Long(area))
			.where("area.avaria = ?", Boolean.TRUE)
			.setMaxResults(1)
			.unique();
	}

	/**
	 * Método que retorna primeiro registro de avaria cadastrado no sistema.
	 * 
	 * @return
	 * @author Tomás Rabelo
	 */
	public Endereco getEnderecoAvariaPrimeiroCadastrado() {
		return query()
			.select("endereco.cdendereco, endereco.rua, endereco.predio, endereco.nivel, endereco.apto, area.codigo")
			.join("endereco.area area")
			.where("area.avaria = ?", Boolean.TRUE)
			.orderBy("endereco.cdendereco")
			.setMaxResults(1)
			.unique();
	}

	/**
	 * Verifica se se um {@link Endereco} é o endereço de picking de um {@link Produto} 
	 * no depósito atual (o depósito que o usuário logou).
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param endereco
	 * @return
	 */
	public Boolean isEnderecoPicking(Produto produto, Endereco endereco) {
		return isEnderecoPicking(produto, endereco, WmsUtil.getDeposito());
	}
	
	/**
	 * Verifica se se um {@link Endereco} é o endereço de picking de um {@link Produto} 
	 * no depósito atual (o depósito que o usuário logou).
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param endereco
	 * @return
	 */
	public Boolean isEnderecoPicking(Produto produto, Endereco endereco, Deposito deposito) {
		if(endereco==null || endereco.getCdendereco()==null){
			throw new WmsException("O endereco não pode ser nulo.");
		}
		if(produto==null || produto.getCdproduto()==null){
			throw new WmsException("O produto não pode ser nulo.");
		}

		QueryBuilder<Endereco> query = query()
			.select("endereco.cdendereco")
			.join("endereco.listaDadologistico dadologistico")
			.where("dadologistico.produto=?", produto)
			.where("dadologistico.endereco=?", endereco)
			.where("dadologistico.deposito=?", deposito)
			.setUseTranslator(false);
		
		List<Endereco> list = query.list();
		
		return list != null && list.size() > 0;
	}
	public Endereco loadProdutoPicking(Endereco endereco) {
		 return query()
		 		.select("endereco.cdendereco,produto.cdproduto, produto.codigo, produto.descricao ")
		 		.join("endereco.listaDadologistico dadologistico")	
		 		.join("endereco.enderecofuncao enderecofuncao")
		 		.leftOuterJoin("dadologistico.produto produto")
		 		.where("enderecofuncao=?", Enderecofuncao.PICKING)
		 		.where("dadologistico.endereco=?", endereco)
		 		.where("dadologistico.deposito=?", WmsUtil.getDeposito())
		 		.unique();
	}
	
	/**
	 * Localiza o endereço de picking de um determinado produto.
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param deposito
	 * @return
	 */
	public Endereco findPicking(Produto produto, Deposito deposito) {
		 return query()
		 		.join("endereco.listaDadologistico dadologistico")	
		 		.where("dadologistico.produto=?", produto)
		 		.where("dadologistico.deposito=?", deposito)
		 		.unique();
	}

	/**
	 * Localiza o endereço de avaria de um determinado depósito.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public Endereco findEnderecoAvaria(Deposito deposito) {
		return query()
			.join("endereco.area area")
			.where("area.deposito = ?", deposito)
			.where("area.avaria=1")
			.setMaxResults(1)
			.unique();
	}
	
	/**
	 * Localiza o endereço de Box de um determinado depósito.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public Endereco findEnderecoBox(Deposito deposito) {
		return query()
		.join("endereco.area area")
		.where("area.deposito = ?", deposito)
		.where("area.box=1")
		.setMaxResults(1)
		.unique();
	}
	
	/**
	 * Localiza o endereço virtual de um determinado depósito.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public Endereco findEnderecoVirtual(Deposito deposito) {
		return query()
			.join("endereco.area area")
			.where("area.deposito = ?", deposito)
			.where("area.virtual=1")
			.setMaxResults(1)
			.unique();
	}

	/**
	 * Verifica se existem endereços que irão satisfazer os filtros do lote de inventário.
	 * 
	 * @author Giovane Freitas
	 * @param lote
	 * @return
	 */
	public boolean existeEndereco(IntervaloEndereco lote, boolean validarProduto) {
		if (lote == null)
			throw new WmsException("O parâmetro não deve ser nulo");

		QueryBuilder<Endereco> sentenca = query()
				.join("endereco.area area")
				.where("area = ?", lote.getArea())
				.whereIntervalMatches("endereco.rua", "endereco.rua", lote.getRuainicial(), lote.getRuafinal())
				.whereIntervalMatches("endereco.predio", "endereco.predio", lote.getPredioinicial(), lote.getPrediofinal())
				.whereIntervalMatches("endereco.nivel", "endereco.nivel", lote.getNivelinicial(), lote.getNivelfinal())
				.whereIntervalMatches("endereco.apto", "endereco.apto", lote.getAptoinicial(), lote.getAptofinal());

		if (lote.getEnderecolado() != null) {
			if (Enderecolado.PAR.equals(lote.getEnderecolado()))
				sentenca.where("mod(endereco.predio, 2) = 0");
			else
				sentenca.where("mod(endereco.predio, 2) <> 0");
		}

		if (lote.getEnderecofuncao() != null)
			sentenca.where("endereco.enderecofuncao=?", lote.getEnderecofuncao());
		
		if (validarProduto && lote.getProduto() != null && lote.getProduto().getCdproduto() != null){
			if (lote.getEnderecofuncao() != null && lote.getEnderecofuncao().equals(Enderecofuncao.PICKING)){
				sentenca.leftOuterJoin("endereco.listaDadologistico dl with dl.deposito.id = " + WmsUtil.getDeposito().getCddeposito());
				sentenca.where("dl.produto = ? ", lote.getProduto());
			}else if (lote.getEnderecofuncao() != null){
				sentenca.leftOuterJoin("endereco.listaEnderecoproduto ep ");
				sentenca.where("ep.produto = ? ", lote.getProduto());				
			}else{
				sentenca.leftOuterJoin("endereco.listaDadologistico dl with dl.deposito.id = " + WmsUtil.getDeposito().getCddeposito())
					.leftOuterJoin("endereco.listaEnderecoproduto ep ")
					.openParentheses()
						.where("dl.produto = ? ", lote.getProduto())
						.or()
						.where("ep.produto = ? ", lote.getProduto())
					.closeParentheses();
			}
		}

		List<Endereco> list = sentenca.setMaxResults(1).list();//busco apenas um endereço para evitar consumo de memória desnecessário

		if (list != null && list.size() > 0)
			return true;
		else
			return false;
	}

	/**
	 * Carrega o Endereco e os dados logísticos associados.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @return 
	 */
	public Endereco loadEnderecoAndDadosLogisticos(Endereco endereco) {
		 return query().select("endereco.cdendereco, endereco.endereco,dadologistico.cddadologistico, " +
		 		"produto.cdproduto,produto.codigo,produto.descricao,enderecofuncao.cdenderecofuncao")
	 		.join("endereco.enderecofuncao enderecofuncao")	
	 		.leftOuterJoin("endereco.listaDadologistico dadologistico")	
	 		.leftOuterJoin("dadologistico.produto produto")	
	 		.where("endereco=?", endereco)
	 		.unique();
	}

	/**
	 * <p>Carrega o endereço a partir da área, rua, prédio, nível e apto.
	 * Caso o nível e a rua sejam igual a -1 será procurado o primeiro 
	 * endereço de um um prédio blocado filtrando por area, rua, prédio e função do endereço.</p>
	 * <p>Também irá filtra eliminando os endereços que estão bloqueados.</p>
	 * 
	 * @author Giovane Freitas
	 * @param area Obrigatório. Código da área.
	 * @param rua Obrigatório. Número da rua.
	 * @param predio Obrigatório. Número do prédio
	 * @param nivel Opcional.
	 * @param apto Opcional.
	 * @param deposito Obrigatório.
	 * @return
	 */
	public Endereco load(Endereco endereco, Deposito deposito) {
		if (deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("O depósito não deve ser nulo.");
		
		QueryBuilder<Endereco> builder = query()
					 .joinFetch("endereco.enderecofuncao endfunc")
					 .joinFetch("endereco.area area")
					 .joinFetch("endereco.enderecostatus enderecostatus")
					 .where("area.codigo = ?",endereco.getArea().getCodigo())
					 .where("area.deposito = ?", deposito)
					 .where("endereco.rua = ?", endereco.getRua())
					 .where("endereco.predio = ?", endereco.getPredio());
		
		boolean isBlocado = false;
		if (endereco.getNivel() == -1 && endereco.getApto() == -1)
			isBlocado = true;
		if (endereco.getEnderecofuncao() != null && endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO))
			isBlocado = true;
		
		if (!isBlocado){
			builder.where("endereco.nivel = ?", endereco.getNivel());
			builder.where("endereco.apto = ?", endereco.getApto());
		}else
			builder.where("endereco.enderecofuncao = ?", Enderecofuncao.BLOCADO);
	
		return builder.setMaxResults(1).unique();
	}
	
	/**
	 * <p>Carrega o endereço a partir da área, rua, prédio, nível e apto.</p>
	 * <p>Também irá filtra eliminando os endereços que estão bloqueados.</p>
	 * 
	 * @author Tomás Rabelo
	 * @param area Obrigatório. Código da área.
	 * @param rua Obrigatório. Número da rua.
	 * @param predio Obrigatório. Número do prédio
	 * @param nivel Obrigatório.
	 * @param apto Obrigatório.
	 * @param deposito Obrigatório.
	 * @return
	 */
	public Endereco loadExato(Endereco endereco, Deposito deposito) {
		if (deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("O depósito não deve ser nulo.");
		
		QueryBuilder<Endereco> builder = query()
					 .joinFetch("endereco.enderecofuncao endfunc")
					 .joinFetch("endereco.area area")
					 .joinFetch("endereco.enderecostatus enderecostatus")
					 .where("area.codigo = ?",endereco.getArea().getCodigo())
					 .where("area.deposito = ?", deposito)
					 .where("endereco.rua = ?", endereco.getRua())
					 .where("endereco.predio = ?", endereco.getPredio())
					 .where("endereco.nivel = ?", endereco.getNivel())
					 .where("endereco.apto = ?", endereco.getApto());
		
		return builder.setMaxResults(1).unique();
	}
	
	/**
	 * Busca os dados para o relatório de ocupação atual.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public TotalVO getOcupacaoAtual(Deposito deposito) {
		StringBuilder sql = new StringBuilder();

		
		sql.append("SELECT trunc(Sum(qtde * buscar_valormedioproduto (?, CASE WHEN cdprodutoprincipal IS NULL THEN cdproduto ELSE cdprodutoprincipal END, null) ) * 100) AS valor_total, "); 
		sql.append("    Sum(qtde * cubagemunitaria_produto(CASE WHEN cdprodutoprincipal IS NULL THEN cdproduto ELSE cdprodutoprincipal END)) AS cubagem_total, ");
		sql.append("    Sum(qtde) AS total_itens, ");
		sql.append("    Sum(volumes) AS total_volumes ");
		sql.append("FROM ( ");
		sql.append("		  SELECT cdprodutoprincipal, CASE WHEN cdprodutoprincipal IS NULL THEN cdproduto ELSE NULL END AS cdproduto , Min(qtde) AS qtde, Sum(qtde) AS volumes FROM ( "); 
		sql.append("		    SELECT p.cdprodutoprincipal, ep.cdproduto, Sum(ep.qtde) AS qtde ");
		sql.append("		    FROM enderecoproduto ep ");
		sql.append("		      join endereco e ON e.cdendereco = ep.cdendereco "); 
		sql.append("		      join area a ON a.cdarea = e.cdarea ");
		sql.append("		      join produto p ON p.cdproduto = ep.cdproduto "); 
		sql.append("		    WHERE a.cddeposito = ? ");
		sql.append("		    GROUP BY p.cdprodutoprincipal, ep.cdproduto "); 
		sql.append("		  ) ");
		sql.append("		  GROUP BY cdprodutoprincipal, CASE WHEN cdprodutoprincipal IS NULL THEN cdproduto ELSE NULL END "); 
		sql.append("		) ");

		Integer[] args = new Integer[]{deposito.getCddeposito(), deposito.getCddeposito()};
		
		return (TotalVO) getJdbcTemplate().query(sql.toString() , args, new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()){
					TotalVO ocupacaoAtualVO = new TotalVO();
					ocupacaoAtualVO.setValorTotal(new Money(rs.getLong("valor_total"), true));
					ocupacaoAtualVO.setCubagemTotal(rs.getDouble("cubagem_total"));
					ocupacaoAtualVO.setQtdeTotal(rs.getLong("total_itens"));
					ocupacaoAtualVO.setNumeroVolumes(rs.getLong("total_volumes"));
					return ocupacaoAtualVO;
				}else{
					return new TotalVO();
				}
			}
			
		});
	}
	
	/**
	 * Busca a movimentação total de um dia.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public TotalVO getTotalRecebido(Deposito deposito, java.util.Date inicio, java.util.Date termino) {
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("SELECT sum(nfp.qtde) as total_itens, ");  
		sql.append("	sum(nfp.qtde * case when (p.qtdevolumes = 0) or (p.qtdevolumes is null) then 1 else p.qtdevolumes end) as total_volumes, ");
		sql.append("	sum(nfp.qtde * pesounitario_produto(nfp.cdproduto)) as peso_total,   ");
		sql.append("	sum(nfp.qtde * nfp.valor)/100 as valor_total,   ");
		sql.append("	Sum(nfp.qtde * cubagemunitaria_produto(nfp.cdproduto)) AS Cubagem_total ");
		sql.append("FROM recebimento r   ");
		sql.append("	join recebimentonotafiscal rnf on (r.cdrecebimento = rnf.cdrecebimento) "); 
		sql.append("	join notafiscalentradaproduto nfp on (nfp.cdnotafiscalentrada = rnf.cdnotafiscalentrada) "); 
		sql.append("	join deposito d on r.cddeposito = d.cddeposito  ");
		sql.append("	join produto p on (nfp.cdproduto = p.cdproduto)  ");
		sql.append("	join dadologistico dl on (p.cdproduto = dl.cdproduto and dl.cddeposito = r.cddeposito) "); 
		sql.append(" 	join linhaseparacao ls ON ls.cdlinhaseparacao = dl.cdlinhaseparacao  ");
		sql.append("where ");

		if (termino == null){
			sql.append("	trunc(r.dtfinalizacao) = trunc(?) ");
			args.add(inicio);
		}else{
			sql.append("	trunc(r.dtfinalizacao) >= trunc(?) ");
			sql.append("	and trunc(r.dtfinalizacao) <= trunc(?) ");			
			args.add(inicio);
			args.add(termino);
		}
		
		sql.append("	and r.cddeposito = ?  ");
		args.add(deposito.getCddeposito());

		return (TotalVO) getJdbcTemplate().query(sql.toString() , args.toArray(), new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()){
					TotalVO ocupacaoAtualVO = new TotalVO();
					ocupacaoAtualVO.setValorTotal(new Money(rs.getLong("valor_total"), true));
					ocupacaoAtualVO.setCubagemTotal(rs.getDouble("cubagem_total"));
					ocupacaoAtualVO.setQtdeTotal(rs.getLong("total_itens"));
					ocupacaoAtualVO.setNumeroVolumes(rs.getLong("total_volumes"));
					return ocupacaoAtualVO;
				}else{
					return new TotalVO();
				}
			}
			
		});
	}

	/**
	 * Busca a movimentação total de um dia.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public TotalVO getTotalExpedido(Deposito deposito, java.util.Date inicio, java.util.Date termino) {
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append("select sum(ci.qtdeconfirmada) AS total_itens, ");  
		sql.append("    sum(ci.qtdeconfirmada * p.peso) as peso_total, ");  
		sql.append("    sum(ci.qtdeconfirmada * pp.valor) as valor_total, ");  
		sql.append("    sum(ci.qtdeconfirmada * case when (p.qtdevolumes = 0) or (p.qtdevolumes is null) then 1 else p.qtdevolumes end) as total_volumes, "); 
		sql.append("    Sum(ci.qtdeconfirmada * p.cubagem) AS cubagem_total  ");
		sql.append("from carregamento c join carregamentoitem ci on (ci.cdcarregamento = c.cdcarregamento) "); 
		sql.append("  join pedidovendaproduto pp on (ci.cdpedidovendaproduto = pp.cdpedidovendaproduto)  ");
		sql.append("  join produto p on (pp.cdproduto = p.cdproduto)  ");
		sql.append("  join dadologistico dl on (p.cdproduto = dl.cdproduto and dl.cddeposito = pp.cddeposito) "); 
		sql.append("  join deposito d on c.cddeposito = d.cddeposito  ");
		sql.append("  join tipooperacao tpo ON tpo.cdtipooperacao = pp.cdtipooperacao "); 
		sql.append("where ");  
		
		if (termino == null){
			sql.append("	Trunc(c.dtfimcarregamento) = trunc(?) ");
			args.add(inicio);
		}else{
			sql.append("	Trunc(c.dtfimcarregamento) >= trunc(?) ");
			sql.append("	and Trunc(c.dtfimcarregamento) <= trunc(?) ");			
			args.add(inicio);
			args.add(termino);
		}
		
		sql.append("	and c.cddeposito = ? ");
		args.add(deposito.getCddeposito());

		return (TotalVO) getJdbcTemplate().query(sql.toString() , args.toArray(), new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()){
					TotalVO ocupacaoAtualVO = new TotalVO();
					ocupacaoAtualVO.setValorTotal(new Money(rs.getLong("valor_total"), true));
					ocupacaoAtualVO.setCubagemTotal(rs.getDouble("cubagem_total"));
					ocupacaoAtualVO.setQtdeTotal(rs.getLong("total_itens"));
					ocupacaoAtualVO.setNumeroVolumes(rs.getLong("total_volumes"));
					return ocupacaoAtualVO;
				}else{
					return new TotalVO();
				}
			}
			
		});
	}
}