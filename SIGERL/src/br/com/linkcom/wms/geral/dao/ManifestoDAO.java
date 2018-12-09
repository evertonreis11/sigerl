package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.Notafiscaltipo;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.DescargaprodutoVO;
import br.com.linkcom.wms.geral.bean.vo.ManifestoVO;
import br.com.linkcom.wms.geral.bean.vo.PedidosmanifestoVO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.ManifestoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.BorderoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.ManifestoPlanilhaFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.ManifestoPlanilhaFiltro.TipoRelatorio;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirPedidosManifestoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.report.filtro.EmitirdescargaprodutoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ManifestoDAO extends GenericDAO<Manifesto>{

	@Override
	public void updateListagemQuery(QueryBuilder<Manifesto> query, FiltroListagem _filtro) {
		ManifestoFiltro filtro = (ManifestoFiltro) _filtro;
		
		makeQuery(query);
		
		query.where("manifesto.cdmanifesto = ?",filtro.getCdmanifesto());
		query.where("deposito = ?",filtro.getDeposito()!=null?filtro.getDeposito():WmsUtil.getDeposito());
		query.where("trunc(manifesto.dtemissao) >= ?",filtro.getDtemissaoinicio());
		query.where("trunc(manifesto.dtemissao) <= ?",filtro.getDtemissaofim());
		query.where("usuarioemissor = ?",filtro.getUsuario());
		query.where("transportador = ?",filtro.getTransportador());
		query.where("manifestostatus = ?",filtro.getManifestostatus());
		query.where("tipoentrega = ?",filtro.getTipoentrega());
		
		if(filtro.getNumeronota()!=null){
			query.join("manifesto.listaManifestonotafiscal manifestonotafiscal");
			query.join("manifestonotafiscal.notafiscalsaida notafiscalsaida");
			query.where("notafiscalsaida.numero = ?",filtro.getNumeronota().toString());
		}
		
		if(filtro.getTransportador()!=null){
			query.where("motorista = ?",filtro.getMotorista());
			query.where("veiculo = ?",filtro.getVeiculo());
		}
		
		if(filtro.getCdcarregamento()!=null){
			if(filtro.getNumeronota()!=null){
				query.join("notafiscalsaida.carregamento carregamento");
			}else{
				query.join("manifesto.listaManifestonotafiscal manifestonotafiscal");
				query.join("manifestonotafiscal.notafiscalsaida notafiscalsaida");
				query.join("notafiscalsaida.carregamento carregamento");
			}
			query.where("carregamento.cdcarregamento = ?",filtro.getCdcarregamento());
		}
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Manifesto> query) {
		makeQuery(query);
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaManifestonotafiscal");
	}
	
	/**
	 * @param query
	 */
	private void makeQuery(QueryBuilder<Manifesto> query) {
		query
			.select("manifesto.cdmanifesto, manifesto.observacao, manifesto.lacrelateral, manifesto.lacretraseiro, manifesto.dtemissao, " +
					"manifestostatus.cdmanifestostatus, manifestostatus.nome, deposito.cddeposito, deposito.nome, usuarioemissor.cdpessoa, " +
					"usuarioemissor.nome, usuarioemissor.login, transportador.cdpessoa, transportador.nome, motorista.cdmotorista, motorista.nome, " +
					"veiculo.cdveiculo, veiculo.placa, conferenteExpedicao.cdpessoa, conferenteExpedicao.nome, conferenteInspetoria.cdpessoa, " +
					"conferenteInspetoria.nome, tipoentrega.cdtipoentrega, tipoentrega.nome, box.cdbox, box.nome, manifesto.cdae, " +
					"manifesto.datainicio,manifesto.horainicio, rotagerenciadora.cdrotagerenciadora,rotagerenciadora.descricao,  " +
					"filialreferencia.cdfilial, filialreferencia.nome, manifestopai.cdmanifesto ")
			.join("manifesto.manifestostatus manifestostatus ")
			.join("manifesto.deposito deposito")
			.join("manifesto.usuarioemissor usuarioemissor")
			.leftOuterJoin("manifesto.transportador transportador")
			.leftOuterJoin("manifesto.motorista motorista")
			.leftOuterJoin("manifesto.veiculo veiculo")
			.leftOuterJoin("manifesto.conferenteExpedicao conferenteExpedicao")
			.leftOuterJoin("manifesto.conferenteInspetoria conferenteInspetoria")
			.leftOuterJoin("manifesto.tipoentrega tipoentrega")
			.leftOuterJoin("manifesto.box box")
			.leftOuterJoin("manifesto.rotagerenciadora rotagerenciadora")
			.leftOuterJoin("manifesto.filialreferencia filialreferencia")
			.leftOuterJoin("manifesto.manifestopai manifestopai");
		
	}
	
	/**
	 * SQL que realiza um update para mudança de stauts do Manifesto.
	 * 
	 * @param manifesto
	 */
	public void  cancelarManifesto(Manifesto manifesto) {
		getJdbcTemplate().execute("update manifesto m set m.cdmanifestostatus = 3 where m.cdmanifesto in ("+manifesto.getCdmanifesto()+")");
	}
	
	/**
	 * SQL que realiza a busca de todos os 
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Manifesto> findManifestoStatusByWhereIn(String whereIn){
		
		QueryBuilder<Manifesto> query = query();
		
		query
			.select("manifesto.cdmanifesto, manifestostatus.cdmanifestostatus, manifestostatus.nome")
			.leftOuterJoin("manifesto.manifestostatus manifestostatus")
			.whereIn("manifesto.cdmanifesto", whereIn);
		
		return query.list();
	}

	/**
	 * 
	 * @param manifesto
	 * @param manifestostatus 
	 */
	public void updateManifestoStatus(Manifesto manifesto, Manifestostatus manifestostatus){
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update manifesto m set m.cdmanifestostatus = ");
			sql.append(manifestostatus.getCdmanifestostatus());
			sql.append("where m.cdmanifesto in ( ").append(manifesto.getCdmanifesto()).append(" ) "); 
		
		getJdbcTemplate().execute(sql.toString());
				
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ManifestoVO> findForReport(Manifesto manifesto) {
		
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append(" select ta.*,rownum as contador from( ");
		sql.append(" 	select distinct d.nome as filial, pt.nome as transportador, mo.nome as motorista, v.placa as placa, pu.nome as usuario, ");
		sql.append("			m.cdmanifesto as manifesto, mcb.codigo as codigo, nfs.qtdeitens as qtdeVol, nfs.lojapedido as loja, sysdate as dataatual, ");
		sql.append("			trim(pc.nome) as clientenome, pec.logradouro||', '||pec.numero as enderecocliente, nfs.numero as nronota, mo.cpf as cpf, ");
		sql.append("			(select count(*) from manifestonotafiscal mnf2 where mnf2.cdmanifesto = m.cdmanifesto) as qtde, ");
		sql.append("			nft.cdnotafiscaltipo as cdnotafiscaltipo, nft.nome as notafiscaltipo, mnf.ind_avulso as notaavulsa, m.dtemissao as datahora, ");
		sql.append(" 			tv.nome as tipoveiculo, m.lacretraseiro, m.lacrelateral, m.observacao, te.nome as tipoentrega, NVL(bx.nome,' ') as box ");
		sql.append(" 	from manifesto m	");
		sql.append(" 	left join box bx on bx.cdbox = m.cdbox ");
		sql.append(" 	join tipoentrega te on te.cdtipoentrega = m.cdtipoentrega ");
		sql.append(" 	join deposito d on d.cddeposito = m.cddeposito	");
		sql.append(" 	join pessoa pt on pt.cdpessoa =  m.cdtransportador	");
		sql.append(" 	join veiculo v on v.cdveiculo = m.cdveiculo	");
		sql.append(" 	join tipoveiculo tv on tv.cdtipoveiculo = v.cdtipoveiculo	");
		sql.append(" 	join motorista mo on mo.cdmotorista = m.cdmotorista	");
		sql.append(" 	join manifestocodigobarras mcb on mcb.cdmanifesto = m.cdmanifesto and mcb.ativo = 1	");
		sql.append(" 	join manifestonotafiscal mnf on mnf.cdmanifesto = m.cdmanifesto	");
		sql.append(" 	join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida	");
		sql.append(" 	join notafiscaltipo nft on nft.cdnotafiscaltipo = nfs.cdtiponf	");
		sql.append(" 	join pessoa pc on pc.cdpessoa = nfs.cdcliente	");
		sql.append(" 	join cliente cl on cl.cdpessoa = pc.cdpessoa	");
		sql.append(" 	join pessoaendereco pec on pec.cdpessoa = pc.cdpessoa and pec.cdpessoaendereco = nfs.cdpessoaendereco ");
		sql.append(" 	join pessoa pu on pu.cdpessoa = m.cdusuarioemissor	");
		sql.append(" 	where m.cdmanifesto = ").append(manifesto.getCdmanifesto());
		sql.append(" 	and mcb.ativo = 1 ");
		sql.append(" 	group by pt.nome, mo.nome, v.placa, pu.nome, m.dtemissao, m.cdmanifesto, mcb.codigo, pc.nome, bx.nome, d.nome, nfs.lojapedido, ");
		sql.append("			pec.logradouro || ', ' || pec.numero, nfs.numero, mo.cpf, nfs.qtdeitens, mnf.cdmanifestonotafiscal,rownum,  ");
		sql.append("			nft.cdnotafiscaltipo, nft.nome, mnf.ind_avulso, tv.nome, m.lacretraseiro, m.lacrelateral, m.observacao, te.nome ");
		sql.append(" 	order by trim(pc.nome)");
		sql.append(" ) ta order by rownum");
		
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ManifestoVO manifestoVO = new ManifestoVO();
					manifestoVO.setFilial(rs.getString("filial") == null ? " " : rs.getString("filial"));
					manifestoVO.setTransportador(rs.getString("transportador") == null ? " " : rs.getString("transportador"));
					manifestoVO.setMotorista(rs.getString("motorista") == null ? " " : rs.getString("motorista"));
					manifestoVO.setPlaca(rs.getString("placa") == null ? " " : rs.getString("placa"));
					manifestoVO.setUsuario(rs.getString("usuario") == null ? " " : rs.getString("usuario"));
					manifestoVO.setDatahora(rs.getString("datahora") == null ? " " : WmsUtil.stringToDefaulDateFormat(rs.getString("datahora"),"yyyy-MM-dd hh:mm:ss"));
					manifestoVO.setManifesto(rs.getString("manifesto") == null ? " " : rs.getString("manifesto"));
					manifestoVO.setCodigo(rs.getString("codigo") == null ? " " : rs.getString("codigo"));
					manifestoVO.setQtde(rs.getString("qtde") == null ? " " : rs.getString("qtde"));
					manifestoVO.setNomecliente(rs.getString("clientenome") == null ? " " : rs.getString("clientenome"));
					manifestoVO.setEnderecocliente(rs.getString("enderecocliente") == null ? " " : rs.getString("enderecocliente"));
					manifestoVO.setNronota(rs.getString("nronota") == null ? " " : rs.getString("nronota"));
					manifestoVO.setQtdeVol(rs.getInt("qtdeVol"));
					manifestoVO.setLoja(rs.getString("loja") == null ? " " : rs.getString("loja"));
					manifestoVO.setDataatual(rs.getString("dataatual") == null ? " " : WmsUtil.stringToDefaulDateFormat(rs.getString("dataatual"),"yyyy-MM-dd hh:mm:ss"));
					manifestoVO.setNotafiscaltipo(rs.getString("notafiscaltipo") == null ? " " : rs.getString("notafiscaltipo"));
					manifestoVO.setCdnotafiscaltipo(rs.getInt("cdnotafiscaltipo"));
					manifestoVO.setNotaavulsa(rs.getInt("notaavulsa")==1 ? true : false);
					manifestoVO.setContador(rs.getInt("contador"));		
					manifestoVO.setCpf(rs.getString("cpf") == null ? " " : rs.getString("cpf"));
					manifestoVO.setTipoveiculo(rs.getString("tipoveiculo") == null ? " " : rs.getString("tipoveiculo"));
					manifestoVO.setLacrelateral(rs.getString("lacrelateral") == null ? " " : rs.getString("lacrelateral"));
					manifestoVO.setLacretraseiro(rs.getString("lacretraseiro") == null ? " " : rs.getString("lacretraseiro"));
					manifestoVO.setObservacao(rs.getString("observacao") == null ? " " : rs.getString("observacao"));
					manifestoVO.setTipoentrega(rs.getString("tipoentrega") == null ? " " : rs.getString("tipoentrega"));
					manifestoVO.setBox(rs.getString("box") == null ? " " : rs.getString("box"));
				return manifestoVO;
			}
		});
		
	}
	
	/**
	 * 
	 * @param numeroManifesto
	 * @return
	 */
	public Manifesto findByCodigoBarras(String codigo, Deposito deposito, Manifestostatus manifestostatus) {
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("manifestocodigobarras.cdmanifestocodigobarras, manifestocodigobarras.codigo, manifestocodigobarras.ativo, " +
						 "manifestocodigobarras.dt_inclusao, manifestocodigobarras.dt_alteracao, manifesto.cdmanifesto, transportador.nome, " +
						 "motorista.nome, veiculo.placa, manifesto.lacrelateral, manifesto.lacretraseiro, manifesto.dtemissao, " +
						 "tipoentrega.cdtipoentrega, tipoentrega.nome, motorista.cdmotorista, transportador.cdpessoa, veiculo.cdveiculo, " +
						 "manifesto.kminicial, manifesto.kmfinal");

			query.join("manifesto.listaManifestocodigobarra manifestocodigobarras");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifesto.transportador transportador");
			query.join("manifesto.deposito deposito");
			query.join("manifesto.tipoentrega tipoentrega");
			query.leftOuterJoin("manifesto.veiculo veiculo");
			query.leftOuterJoin("manifesto.motorista motorista");
			query.where("manifestocodigobarras.codigo = ?", codigo);
			query.where("manifestocodigobarras.ativo = 1");
			query.where("manifestostatus = ?",manifestostatus);
			query.where("deposito = ?",deposito);
			
		return query.unique();
		
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findByPrestacaoConta(String codigo, Manifesto manifesto) {
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("manifestocodigobarras.cdmanifestocodigobarras, manifestocodigobarras.codigo, manifestocodigobarras.ativo, " +
						 "manifestocodigobarras.dt_inclusao, manifestocodigobarras.dt_alteracao, manifesto.cdmanifesto, manifesto.lacrelateral, " +
						 "manifesto.lacretraseiro, deposito.cddeposito, deposito.nome, veiculo.cdveiculo, veiculo.placa, motorista.cdmotorista, " +
						 "motorista.nome, transportador.cdpessoa, transportador.nome, manifesto.dtfinalizacao, conferenteInspetoria.cdpessoa," +
						 "conferenteInspetoria.nome, manifestostatus.cdmanifestostatus, manifestostatus.nome, usuarioliberador.cdpessoa," +
						 "usuarioliberador.nome, usuariofinalizador.cdpessoa, usuariofinalizador.nome, manifesto.dtsaidaveiculo," +
						 "manifesto.cubagem, tipoentrega.cdtipoentrega, tipoentrega.nome, manifestopai.cdmanifesto ");
			query.join("manifesto.listaManifestocodigobarra manifestocodigobarras");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifesto.transportador transportador");
			query.join("manifesto.deposito deposito");
			query.join("manifesto.tipoentrega tipoentrega");
			query.leftOuterJoin("manifesto.veiculo veiculo");
			query.leftOuterJoin("manifesto.motorista motorista");
			query.leftOuterJoin("manifesto.conferenteInspetoria conferenteInspetoria");
			query.leftOuterJoin("manifesto.usuarioliberador usuarioliberador");
			query.leftOuterJoin("manifesto.usuariofinalizador usuariofinalizador");
			query.leftOuterJoin("manifesto.manifestopai manifestopai");
			query.where("manifestocodigobarras.codigo = ?", codigo);
			query.where("manifesto = ?", manifesto);
			query.where("manifestocodigobarras.ativo = 1");
			query.openParentheses();
				query.where("manifestostatus = ?",Manifestostatus.AGUARANDO_PRESTACAO);
				query.or();
				query.where("manifestostatus = ?",Manifestostatus.IMPRESSO);
				query.or();
				query.where("manifestostatus = ?",Manifestostatus.ENTREGA_EM_ANDAMENTO);
			query.closeParentheses();
			
		return query.unique();
		
	}

	/**
	 * 
	 * @param manifesto
	 * @param kminicial 
	 */
	public void updateKmInicialManifesto(Manifesto manifesto, Long kminicial) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" update Manifesto m set m.kminicial = ").append(kminicial);
		sql.append(" where m.cdmanifesto = ").append(manifesto.getCdmanifesto()); 
	
		getJdbcTemplate().execute(sql.toString());
	}

	/**
	 * 
	 * @param manifesto
	 * @param kmfinal
	 */
	public void updateKmFinalManifesto(Manifesto manifesto, Long kmfinal) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" update Manifesto m set m.dtretornoveiculo = sysdate, m.kmfinal = ").append(kmfinal);
		sql.append(" where m.cdmanifesto = ").append(manifesto.getCdmanifesto()); 
	
		getJdbcTemplate().execute(sql.toString());
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto loadWithManifestostatus(String codigo) {
		return query()
			.select("manifesto.cdmanifesto, manifestostatus.cdmanifestostatus")
			.join("manifesto.manifestostatus manifestostatus")
			.join("manifesto.manifestocodigobarras manifestocodigobarras")
			.where("manifestocodigobarras.ativo = 1 and manifestocodigobarras.codigo = ?",codigo)
			.unique();
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findForFechamentoFinanceiro(String codigo, Manifesto manifesto) {
		
		List<Manifestostatus> listaStatus = new ArrayList<Manifestostatus>();
			listaStatus.add(Manifestostatus.PRESTACAO_CONTAS_FINALIZADO);
			listaStatus.add(Manifestostatus.FINALIZADO);
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifesto.cdmanifesto, manifestostatus.cdmanifestostatus, manifestostatus.nome, tipoentrega.cdtipoentrega, " +
						"tipoentrega.nome, transportador.cdpessoa, transportador.nome, veiculo.cdveiculo, veiculo.placa, motorista.cdmotorista, " +
						"motorista.nome, manifesto.dtsaidaveiculo, manifesto.dtfinalizacao, manifesto.dtretornoveiculo, "+
						"usuariofinalizador.cdpessoa, usuariofinalizador.nome, manifestopai.cdmanifesto ")
				.join("manifesto.manifestostatus manifestostatus")
				.join("manifesto.tipoentrega tipoentrega")
				.leftOuterJoin("manifesto.transportador transportador")
				.leftOuterJoin("manifesto.veiculo veiculo")
				.leftOuterJoin("manifesto.motorista motorista")
				.leftOuterJoin("manifesto.usuariofinalizador usuariofinalizador")
				.leftOuterJoin("manifesto.listaManifestocodigobarra manifestocodigobarras")
				.leftOuterJoin("manifesto.manifestopai manifestopai")
				.openParentheses()
					.where("manifestostatus = ?",Manifestostatus.PRESTACAO_CONTAS_FINALIZADO)
					.or()
					.where("manifestostatus = ?",Manifestostatus.FINALIZADO)
				.closeParentheses()
				.where("manifestocodigobarras.ativo = 1 and manifestocodigobarras.codigo = ?",codigo)
				.where("manifesto = ?",manifesto);
		
		return query.unique();
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Manifesto> findForBordero(BorderoFiltro filtro){
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("distinct manifesto.cdmanifesto, manifesto.vrtotalmanifesto, veiculo.cdveiculo, veiculo.placa, " +
						 "transportador.cdpessoa, transportador.nome, motorista.cdmotorista, motorista.nome, " +
						 "tipoentrega.cdtipoentrega, tipoentrega.nome, manifestofinanceiro.total ");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifesto.manifestofinanceiro manifestofinanceiro");
			query.join("manifesto.veiculo veiculo");
			query.join("manifesto.motorista motorista");
			query.join("manifesto.transportador transportador");
			query.join("manifesto.deposito deposito");
			query.join("manifesto.tipoentrega tipoentrega");
			query.join("manifesto.listaManifestonotafiscal manifestonotafiscal");
			query.join("manifestonotafiscal.notafiscalsaida notafiscalsaida");
			query.join("notafiscalsaida.filialfaturamento filialfaturamento");
			query.join("filialfaturamento.empresa empresa");
			query.where("deposito = ?",filtro.getDeposito());
			query.where("transportador = ?",filtro.getTransportador());
			query.where("veiculo = ?",filtro.getVeiculo());
			query.where("motorista = ?",filtro.getMotorista());
			query.where("trunc(manifesto.dtemissao) >= ?",filtro.getDtemissaoInicio());
			query.where("trunc(manifesto.dtemissao) <= ?",filtro.getDtemissaoFim());
			query.where("manifesto.cdmanifesto = ?",filtro.getNumeroManifesto());
			query.where("empresa = ?",filtro.getEmpresa());
			query.where("manifestostatus = ?",Manifestostatus.FINALIZADO);
			query.where("tipoentrega  = ?",filtro.getTipoentrega());
			
		return query.list();
	}
	
	/**
	 * 
	 * @param whereIn
	 * @return
	 */
	public List<Manifesto> findByWhereIn(String whereIn) {
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifesto.cdmanifesto, transportador.cdpessoa");
			query.join("manifesto.transportador transportador");
			query.join("manifesto.manifestostatus manifestostatus");
			query.where("manifestostatus = ?",Manifestostatus.FINALIZADO);
			query.whereIn("manifesto.cdmanifesto",whereIn);
		
		return query.list();
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosExportacao(ManifestoPlanilhaFiltro filtro) {
		
		if (filtro == null || filtro.getTipoRelatorio() == null){
			throw new WmsException("O campo 'Tipo' é obrigatório.");
		}
			
		StringBuilder sql = new StringBuilder();
		
		if(filtro.getTipoRelatorio().equals(TipoRelatorio.Analítico)){
			sql = getSqlAnalitico(filtro);
		}else if(filtro.getTipoRelatorio().equals(TipoRelatorio.Sintético)){
			sql = getSqlSintetico(filtro);
		}else{
			throw new WmsException("O campo 'Tipo do Relatório' são obrigatório.");
		}
		
		return getJdbcTemplate().queryForRowSet(sql.toString());
	}

	/**
	 * 
	 * @param filtro 
	 * @return
	 */
	private StringBuilder getSqlSintetico(ManifestoPlanilhaFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("	SELECT 	m.cdmanifesto as \"Núm. Manifesto\", pt.nome as \"Transportador\", m.dtemissao as \"Dt. do Cadastro\",	");
		sql.append("			m.dtretornoveiculo as \"Dt. da Confirmação\", m.dtfinalizacao as \"Dt. do Fechamento\", 	ms.nome as \"Status\", 	");
		sql.append("			m.vrtotalmanifesto as \"Valor Total\" ");
		sql.append("	FROM manifesto m	");
		sql.append("	JOIN pessoa pt on pt.cdpessoa = m.cdtransportador	");		
		sql.append("	JOIN manifestostatus ms on ms.cdmanifestostatus = m.cdmanifestostatus	");
		sql.append("	WHERE 1 = 1	");
		
		if(filtro.getTransportador()!=null && filtro.getTransportador().getCdpessoa()!=null){
			sql.append("	AND pt.cdpessoa = ").append(filtro.getTransportador().getCdpessoa());
		}
		if(filtro.getMotorista()!=null && filtro.getMotorista().getCdmotorista()!=null){
			sql.append("	AND pt.cdpessoa = ").append(filtro.getTransportador().getCdpessoa());
		}
		if(filtro.getDtemissaoInicio()!=null){
			sql.append("	AND m.dtemissao <= TO_DATE('").append(filtro.getDtemissaoInicio().toString().trim()).append("', 'yyyy-MM-dd')");
		}
		if(filtro.getDtemissaoFim()!=null){
			sql.append("	AND m.dtemissao <= TO_DATE('").append(filtro.getDtemissaoFim().toString().trim()).append("', 'yyyy-MM-dd')");
		}		
		if(filtro.getCdmanifesto()!=null){
			sql.append("	AND m.cdmanifesto = ").append(filtro.getCdmanifesto());
		}
		if(filtro.getDeposito()!=null && filtro.getDeposito().getCddeposito()!=null){
			sql.append("	AND m.cddeposito = ").append(filtro.getDeposito().getCddeposito());
		}
		
		return sql;
	}

	/**
	 * 
	 * @param filtro 
	 * @return
	 */
	private StringBuilder getSqlAnalitico(ManifestoPlanilhaFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
			
			sql.append("	SELECT 	m.cdmanifesto as \"Núm. Manifesto\", pt.nome as \"Transportador\", mo.nome as \"Motorista\",	");
			sql.append("			r.nome as \"Rota\", p.nome as \"Praca\", nfs.numeropedido as \"Núm. Pedidos\",					");
			sql.append("			nfs.lojapedido as \"Loja do Pedido\", pc.nome as \"Cliente\" 								");
			sql.append("	FROM manifesto m	");
			sql.append("	JOIN pessoa pt on pt.cdpessoa = m.cdtransportador	");
			sql.append("	JOIN motorista mo on mo.cdmotorista = m.cdmotorista");
			sql.append("	JOIN manifestonotafiscal mnf on mnf.cdmanifesto = m.cdmanifesto");
			sql.append("	JOIN notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
			sql.append("	JOIN praca p on p.cdpraca = nfs.cdpraca	");
			sql.append("	JOIN rotapraca rp on rp.cdpraca = p.cdpraca ");
			sql.append("	JOIN rota r on r.cdrota = rp.cdrota			");
			sql.append("	JOIN pessoa pc on pc.cdpessoa = nfs.cdcliente ");
			sql.append("	WHERE 1 = 1");
		
		if(filtro.getTransportador()!=null && filtro.getTransportador().getCdpessoa()!=null){
			sql.append("	AND m.cdmanifesto = ").append(filtro.getCdmanifesto());
		}
		if(filtro.getMotorista()!=null && filtro.getMotorista().getCdmotorista()!=null){
			sql.append("	AND mo.cdmotorista = ").append(filtro.getMotorista().getCdmotorista());
		}
		if(filtro.getDtemissaoInicio()!=null){
			sql.append("	AND m.dtemissao >= TO_DATE('").append(filtro.getDtemissaoInicio().toString().trim()).append("', 'yyyy-MM-dd')");
		}
		if(filtro.getDtemissaoFim()!=null){
			sql.append("	AND m.dtemissao <= TO_DATE('").append(filtro.getDtemissaoFim().toString().trim()).append("', 'yyyy-MM-dd')");
		}	
		if(filtro.getLojapedido()!=null){
			sql.append("	AND nfs.lojapedido = ").append(filtro.getLojapedido());
		}
		if(filtro.getRota()!=null && filtro.getRota().getCdrota()!=null){
			sql.append("	AND r.cdrota = ").append(filtro.getRota().getCdrota());
		}
		if(filtro.getCdmanifesto()!=null){
			sql.append("	AND m.cdmanifesto = ").append(filtro.getCdmanifesto());
		}
		if(filtro.getDeposito()!=null && filtro.getDeposito().getCddeposito()!=null){
			sql.append("	AND m.cddeposito = ").append(filtro.getDeposito().getCddeposito());
		}
		
		return sql;
	}

	/**
	 * 
	 * @param manifesto
	 */
	public void updateDtprestacaocontas(Manifesto manifesto) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update Manifesto m set m.dtprestacaocontas = sysdate where m.cdmanifesto = ").append(manifesto.getCdmanifesto()); 
	
		getJdbcTemplate().execute(sql.toString());
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param usuario 
	 */
	public void updateDtfinalizacao(Manifesto manifesto, Usuario usuario) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update Manifesto m set m.dtfinalizacao = sysdate, ");
			sql.append(" m.cdusuariofinalizador = ").append(usuario.getCdpessoa());
			sql.append(" where m.cdmanifesto = ").append(manifesto.getCdmanifesto());
			
		getJdbcTemplate().execute(sql.toString());
	}

	/**
	 * 
	 * @param manifesto
	 * @param usuario
	 */
	public void updateDtsaidaveiculo(Manifesto manifesto, Usuario usuario) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update Manifesto m set m.dtsaidaveiculo = sysdate, m.cdusuarioliberador = ").append(usuario.getCdpessoa());
			sql.append(" where m.cdmanifesto =  ").append(manifesto.getCdmanifesto());
		
		getJdbcTemplate().execute(sql.toString());
	}

	/**
	 * 
	 * @param cdae
	 * @param cdmanifesto
	 * @return
	 */
	public void updateCDAE(Integer cdae, Integer cdmanifesto) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update Manifesto m set m.cdae = ").append(cdae).append(" where m.cdmanifesto =  ").append(cdmanifesto);
	
		getJdbcTemplate().execute(sql.toString());
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findStatusForReimpressaoByCodigobarras(String codigo) {
		
		String statusvalidos = "4,6,7";
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifesto.cdmanifesto, manifestostatus.cdmanifestostatus");
			query.join("manifesto.listaManifestocodigobarra manifestocodigobarra");
			query.join("manifesto.manifestostatus manifestostatus");
			query.whereIn("manifestostatus",statusvalidos);
			query.where("manifestocodigobarra.codigo = ?",codigo);
			query.where("manifestocodigobarra.ativo = 1");
	
		return query.unique();
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public Manifesto findStatusForExtratoByCodigobarras(String codigo) {
		
		String statusvalidos = "4,6";
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifesto.cdmanifesto, manifestostatus.cdmanifestostatus");
			query.join("manifesto.listaManifestocodigobarra manifestocodigobarra");
			query.join("manifesto.manifestostatus manifestostatus");
			query.whereIn("manifestostatus",statusvalidos);
			query.where("manifestocodigobarra.codigo = ?",codigo);
			query.where("manifestocodigobarra.ativo = 1");
	
		return query.unique();
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public SqlRowSet getDadosListagem(ManifestoPlanilhaFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select relatorio.*, NVL((relatorio.acrescimo-relatorio.desconto+relatorio.valor_frete),0) as valor_total from ( ");
		sql.append(" 	select distinct ma.cdmanifesto as manifesto, ");
		sql.append(" 			ma.dtemissao as emisssao, ");
		sql.append("			sta.nome as status, ");
		sql.append("			tpe.nome as tipo_manifesto, ");
		sql.append("			pt.nome as transportadora, ");
		sql.append("			mo.nome as motorista, ");
		sql.append("			ve.placa as placa, ");
		sql.append("			ro.nome as rota, ");
		sql.append("			ma.dtprestacaocontas as Dt_Prestacao_Contas, ");
		sql.append("			ma.dtfinalizacao as Dt_Fechamento_Financeiro, ");
		sql.append("			b.dtbordero as Dt_Bordero, ");
		sql.append("			b.cdbordero as Nro_Bordero, ");
		sql.append("			(select count(distinct nfs2.cdpessoaendereco) ");
		sql.append("				from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2, rota ro2 ");
		sql.append("				where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
		sql.append("				and nfm2.cdmanifesto = ma.cdmanifesto ");
		sql.append("				and nfs2.cdpraca = rp2.cdpraca ");
		sql.append("				and ro2.cdrota = rp2.cdrota ");
		sql.append("				and ro2.cdrota = ro.cdrota ");
		sql.append("				and nfs2.cdpraca = rp2.cdpraca) as Qtdes_Entregas_Manifestadas, ");
		sql.append("			(select count(distinct nfs2.cdpessoaendereco) ");
		sql.append("				from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2, rota ro2 ");
		sql.append("				where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
		sql.append("				and nfm2.cdmanifesto = ma.cdmanifesto ");
		sql.append("				and nfs2.cdpraca = rp2.cdpraca ");
		sql.append("				and ro2.cdrota = rp2.cdrota ");
		sql.append("				and ro2.cdrota = ro.cdrota ");
		sql.append("				and nfm2.cdstatusconfirmacaoentrega = 2) as Qtde_Entregas_Realizadas, ");
		sql.append("			(select count(distinct nfs2.cdpessoaendereco) ");
		sql.append("				from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2, rota ro2 ");
		sql.append("				where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
		sql.append("				and nfm2.cdmanifesto = ma.cdmanifesto ");
		sql.append("				and nfs2.cdpraca = rp2.cdpraca ");
		sql.append("				and ro2.cdrota = rp2.cdrota ");
		sql.append("				and ro2.cdrota = ro.cdrota ");
		sql.append("				and nfm2.cdstatusconfirmacaoentrega = 3) as Qtde_Entregas_Retornadas, ");
		sql.append("			(select count(distinct nfs2.cdpessoaendereco) ");
		sql.append("				from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2, rota ro2 ");
		sql.append("				where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
		sql.append("				and nfm2.cdmanifesto = ma.cdmanifesto ");
		sql.append("				and nfs2.cdpraca = rp2.cdpraca ");
		sql.append("				and ro2.cdrota = rp2.cdrota ");
		sql.append("				and ro2.cdrota = ro.cdrota ");
		sql.append("				and nfm2.cdstatusconfirmacaoentrega = 4) as Qtde_Entregas_Excluidas, ");
		sql.append("			(select NVL(round(sum(nfm2.valorentrega)/100,2),0) ");
		sql.append("				from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2, rota ro2 ");
		sql.append("				where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
		sql.append("				and nfm2.cdmanifesto = ma.cdmanifesto ");
		sql.append("				and nfs2.cdpraca = rp2.cdpraca ");
		sql.append("				and ro2.cdrota = rp2.cdrota ");
		sql.append("				and ro2.cdrota = ro.cdrota) as Valor_frete, ");
		sql.append("			(select round(sum(nfs2.vlrtotalnf)/100,2) ");
		sql.append("				from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2, rota ro2 ");
		sql.append("				where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
		sql.append("				and nfm2.cdmanifesto = ma.cdmanifesto ");
		sql.append("				and nfs2.cdpraca =rp2.cdpraca ");
		sql.append("				and ro2.cdrota = rp2.cdrota ");
		sql.append("				and ro2.cdrota = ro.cdrota) as Valor_notas, ");
		sql.append("			BUSCAR_ACRESCIMO(ma.cdmanifesto) as Acrescimo, ");
		sql.append("			BUSCAR_DESCONTO(ma.cdmanifesto) as Desconto, ");
		sql.append("			 (select SUBSTR(motivo,INSTR(motivo,':',1)+1) ");
		sql.append("				from (select max(mh.motivo) as motivo, mh.cdmanifesto as cdmanifesto ");
		sql.append("						from manifestohistorico mh ");
		sql.append("						where mh.cdtipomanifestohistorico = 2 ");
		sql.append("						group by mh.cdmanifesto) ");
		sql.append("			 	where cdmanifesto = ma.cdmanifesto and rownum = 1) as MOTIVO_ACRESCIMO, ");
		sql.append("			 (select SUBSTR(motivo,INSTR(motivo,':',1)+1) ");
		sql.append("				from (select max(mh.motivo) as motivo, mh.cdmanifesto as cdmanifesto ");
		sql.append("						from manifestohistorico mh ");
		sql.append("						where mh.cdtipomanifestohistorico = 3 ");
		sql.append("						group by mh.cdmanifesto) ");
		sql.append("				 where cdmanifesto = ma.cdmanifesto and rownum = 1) as MOTIVO_DESCONTO ");	
		sql.append(" 	from manifesto ma ");
		sql.append(" 	left join manifestofinanceiro mf on mf.cdmanifesto = ma.cdmanifesto ");
		sql.append(" 	join manifestostatus sta on sta.cdmanifestostatus = ma.cdmanifestostatus ");
		sql.append(" 	join manifestonotafiscal mnf on mnf.cdmanifesto = ma.cdmanifesto ");
		sql.append(" 	join pessoa pt on pt.cdpessoa = ma.cdtransportador ");
		sql.append(" 	join veiculo ve on ve.cdveiculo = ma.cdveiculo ");
		sql.append(" 	join motorista mo on mo.cdmotorista = ma.cdmotorista ");
		sql.append(" 	join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" 	join rotapraca rp on rp.cdpraca = nfs.cdpraca ");
		sql.append(" 	join rota ro on ro.cdrota = rp.cdrota ");
		sql.append(" 	join tipoentrega tpe on tpe.cdtipoentrega = ma.cdtipoentrega ");
		sql.append(" 	left join manifestobordero mb on mb.cdmanifesto = ma.cdmanifesto ");
		sql.append(" 	left join bordero b on b.cdbordero = mb.cdbordero ");
		sql.append(" 	where TRUNC(ma.dtemissao) >= TO_DATE('").append(filtro.getDtemissaoInicio().toString().trim()).append("', 'yyyy-MM-dd')");
		sql.append(" 	and TRUNC(ma.dtemissao) <= TO_DATE('").append(filtro.getDtemissaoFim().toString().trim()).append("', 'yyyy-MM-dd')");
		
		if(filtro.getTransportador()!=null && filtro.getTransportador().getCdpessoa() !=null){
			sql.append(" and ma.cdtransportador = ").append(filtro.getTransportador().getCdpessoa());
		}
		if(filtro.getMotorista()!=null && filtro.getMotorista().getCdmotorista()!=null){
			sql.append(" and ma.cdmotorista = ").append(filtro.getMotorista().getCdmotorista());
		}
		if(filtro.getDeposito()!=null && filtro.getDeposito().getCddeposito()!=null){
			sql.append(" and ma.cddeposito = ").append(filtro.getDeposito().getCddeposito());
		}
		if(filtro.getTipoentrega()!=null && filtro.getTipoentrega().getCdtipoentrega()!=null){
			sql.append(" and ma.cdtipoentrega = ").append(filtro.getTipoentrega().getCdtipoentrega());
		}
		
		sql.append(" 	group by ro.nome, ma.cdmanifesto, ma.dtemissao, sta.nome, tpe.nome, pt.nome, mo.nome, ve.placa, mnf.valorentrega, ");
		sql.append(" 			 ro.cdrota, ma.dtprestacaocontas, ma.dtfinalizacao, b.dtbordero, b.cdbordero ");
		
		sql.append(" ) relatorio ");
		
		return getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DescargaprodutoVO> findByDescargaProdutos(EmitirdescargaprodutoFiltro filtro){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select p.codigoerp as codigo, p.descricao as descricao, substr(p.codigo,length(p.codigo),1) as voltagem, ");
		sql.append(" 		tp.nome as status ,sum(nfsp.qtde) as qtde, m.cdmanifesto as manifesto ");
		sql.append(" from manifesto m ");
		sql.append(" join manifestonotafiscal mnf on mnf.cdmanifesto = m.cdmanifesto ");
		sql.append(" join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" join notafiscalsaidaproduto nfsp on nfsp.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" join manifestocodigobarras mcb on mcb.cdmanifesto = m.cdmanifesto");
		sql.append(" join produto p on p.cdproduto = nfsp.cdproduto ");
		sql.append(" join pedidovendaproduto pvp on pvp.cdpedidovendaproduto = nfsp.cdpedidovendaproduto ");
		sql.append(" join tipopedido tp on tp.cdtipopedido = pvp.cdtipopedido ");
		sql.append(" where mcb.ativo = 1 ");
		
		if(filtro.getCdmanifesto()!=null)
			sql.append(" and m.cdmanifesto = ").append(filtro.getCdmanifesto());
		if(filtro.getCodigobarras()!=null)
			sql.append(" and mcb.codigo = ").append(filtro.getCodigobarras());
		
		sql.append(" group by p.codigoerp, p.codigo, p.descricao, tp.nome, m.cdmanifesto ");
		
		return getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				DescargaprodutoVO descargaprodutoVO = new DescargaprodutoVO();
					
					descargaprodutoVO.setCodigo(rs.getString("codigo"));
					descargaprodutoVO.setDescricao(rs.getString("descricao"));
					descargaprodutoVO.setVoltagem(rs.getString("voltagem"));
					descargaprodutoVO.setStatus(rs.getString("status"));
					descargaprodutoVO.setQtde(rs.getLong("qtde"));
					descargaprodutoVO.setManifesto(rs.getString("manifesto"));
					
				return descargaprodutoVO;
			}
		});
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PedidosmanifestoVO> findByPedidosManifesto(EmitirPedidosManifestoFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select nfs.cdnotafiscalsaida, nfs.lojapedido as loja, nfs.numeropedido as numero, ");
		sql.append(" 		tp.nome as tipoentrega, p.codigo as codigo, p.descricao as descricao, ");
		sql.append(" 		nfsp.qtde as qtde, nspr.dataemissao as dataemissao, nspr.notareferencia as notareferencia, ");
		sql.append(" 		nfsp.cdnotafiscalsaidaproduto as cdnotafiscalsaidaproduto, ");
		sql.append(" 		nspr.cdnotasaidaprodutoreferencia as cdnotasaidaprodutoreferencia ");
		sql.append(" from manifesto m ");
		sql.append(" join deposito d on d.cddeposito = m.cddeposito ");
		sql.append(" join manifestonotafiscal mnf on mnf.cdmanifesto = m.cdmanifesto ");
		sql.append(" join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" join notafiscalsaidaproduto nfsp on nfsp.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" join pedidovendaproduto pvp on pvp.cdpedidovendaproduto = nfsp.cdpedidovendaproduto and pvp.cdtipooperacao in (1,3) ");
		sql.append(" left join deposito dd on mnf.cddepositotransbordo = dd.cddeposito ");
		sql.append(" left join notasaidaprodutoreferencia nspr on nspr.cdnotafiscalsaidaproduto = nfsp.cdnotafiscalsaidaproduto ");
		sql.append(" join manifestocodigobarras mcb on mcb.cdmanifesto = m.cdmanifesto and mcb.ativo = 1 ");
		sql.append(" join produto p on p.cdproduto = nfsp.cdproduto ");
		sql.append(" join tipoentrega tp on tp.cdtipoentrega = m.cdtipoentrega ");
		sql.append(" where tp.cdtipoentrega = 1 and m.cdmanifestostatus <> 3");
		
		if(filtro.getCdmanifesto()!=null){
			sql.append(" and m.cdmanifesto = ").append(filtro.getCdmanifesto());
		}if(filtro.getCodigo()!=null && !filtro.getCodigo().isEmpty()){
			sql.append(" and mcb.codigo = ").append(filtro.getCodigo());
		}if(filtro.getNumeroLoja()!=null && filtro.getNumeroPedido()!=null && !filtro.getNumeroPedido().isEmpty()){
			sql.append(" and nfs.numeropedido = ").append(filtro.getNumeroPedido());
			sql.append(" and nfs.lojapedido = ").append(filtro.getNumeroLoja());
		}if(filtro.getDataInicio()!=null && filtro.getDataFim()!=null){
			sql.append(" and m.dtemissao >= to_date('").append(filtro.getDataInicio()).append("','yyyy-MM-dd')");
			sql.append(" and m.dtemissao <= to_date('").append(filtro.getDataFim()).append("','yyyy-MM-dd')");;
		}if(filtro.getDepositoOrigem()!=null && filtro.getDepositoOrigem().getCddeposito()!=null){
			sql.append(" and d.cddeposito = ").append(filtro.getDepositoOrigem().getCddeposito());
		}if(filtro.getDepositoDestino()!=null && filtro.getDepositoDestino().getCddeposito()!=null){
			sql.append(" and dd.cddeposito = ").append(filtro.getDepositoDestino().getCddeposito());
		}
		
		return getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PedidosmanifestoVO pedidosmanifestoVO = new PedidosmanifestoVO();
				
					if(rs.getInt("cdnotasaidaprodutoreferencia")==0){
						pedidosmanifestoVO.setCdnotasaidaprodutoreferencia(null);
					}else{
						pedidosmanifestoVO.setCdnotasaidaprodutoreferencia(rs.getInt("cdnotasaidaprodutoreferencia"));
					}
					
					pedidosmanifestoVO.setCdnotafiscalsaidaproduto(rs.getInt("cdnotafiscalsaidaproduto"));
					pedidosmanifestoVO.setLoja(rs.getString("loja"));
					pedidosmanifestoVO.setPedido(rs.getString("numero"));
					pedidosmanifestoVO.setTipoentrega(rs.getString("tipoentrega"));
					pedidosmanifestoVO.setCodigo(rs.getString("codigo"));
					pedidosmanifestoVO.setDescricao(rs.getString("descricao"));
					pedidosmanifestoVO.setQtde(rs.getLong("qtde"));
					pedidosmanifestoVO.setNumeroNota(rs.getLong("notareferencia"));
					pedidosmanifestoVO.setDataNota(rs.getDate("dataemissao"));
				
				return pedidosmanifestoVO;
			}
		});
		
	}
	
	/**
	 * 
	 * @param notafiscalsaida
	 * @param whereInNotas
	 * @return
	 */
	public List<Manifesto> findAllManifestosVinculados(Notafiscalsaida notafiscalsaida, String whereInNotas){
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("distinct manifesto.cdmanifesto")
				.join("manifesto.listaManifestonotafiscal listaManifestonotafiscal")
				.join("manifesto.manifestostatus manifestostatus")
				.join("listaManifestonotafiscal.notafiscalsaida notafiscalsaida")
				.whereIn("notafiscalsaida.cdnotafiscalsaida",whereInNotas)
				.where("manifesto.tipoentrega in (1,2)")
				.where("manifestostatus = ?",Manifestostatus.IMPRESSO);
		
		return query.list();
	}
	
	/**
	 * 
	 * @param manifesto 
	 * @param whereIManifesto
	 */
	public void updateManifestosFilhos(Manifesto manifesto, String whereIManifesto) {
		
		StringBuilder sql = new StringBuilder();
		
			sql.append(" update Manifesto m set m.cdmanifestopai = ")
				.append(manifesto.getCdmanifesto())
				.append(" where m.cdmanifesto in (")
				.append(whereIManifesto).append(") ");
		
		getJdbcTemplate().execute(sql.toString());		
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param whereIn 
	 */
	public void desvincularManifestoFilho(Manifesto manifesto, String whereIn) {
		
		StringBuilder sql = new StringBuilder();
		
		if(manifesto!=null && manifesto.getCdmanifesto()!=null){
			sql.append(" update Manifesto m set m.cdmanifestopai = null")
				.append(" where m.cdmanifestopai = ")
				.append(manifesto.getCdmanifesto());
			getJdbcTemplate().execute(sql.toString());
		}else if(whereIn!=null && !whereIn.isEmpty()){
			sql.append(" update Manifesto m set m.cdmanifestopai = null")
				.append(" where m.cdmanifestopai in ( ")
				.append(whereIn).append(" )");
			getJdbcTemplate().execute(sql.toString());
		}
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public Manifesto findTipoEntregaForManifesto(Manifesto manifesto) {
		
		QueryBuilder<Manifesto> query = query();
		
		query.select("manifesto.cdmanifesto, tipoentrega.cdtipoentrega, tipoentrega.nome")
			.join("manifesto.tipoentrega tipoentrega")
			.where("manifesto = ?",manifesto);
		
		return query.unique();
	}

	/**
	 * 
	 * @param manifesto
	 * @param manifestostatus
	 * @param msg
	 * @param usuario
	 */
	public void updateManifestoFilhoStatus(Manifesto manifesto, Usuario usuario) {
		
		StringBuilder sqlEntregaCliente = new StringBuilder();
		
			sqlEntregaCliente.append(" update Manifesto m set m.dtsaidaveiculo = sysdate, m.cdmanifestostatus = 5, m.cdusuarioliberador = ");
			sqlEntregaCliente.append(usuario.getCdpessoa()).append(" where m.cdtipoentrega = 2 and m.cdmanifestopai = ").append(manifesto.getCdmanifesto());

		StringBuilder sqlTransferencia = new StringBuilder();

			sqlTransferencia.append(" update Manifesto m set m.dtsaidaveiculo = sysdate, m.cdmanifestostatus = 7, m.cdusuarioliberador = ");
			sqlTransferencia.append(usuario.getCdpessoa()).append(" where m.cdtipoentrega = 1 and m.cdmanifestopai = ").append(manifesto.getCdmanifesto());
			
		getJdbcTemplate().execute(sqlEntregaCliente.toString());
		getJdbcTemplate().execute(sqlTransferencia.toString());
	}

	/**
	 * 
	 * @param codigobarras
	 * @param deposito
	 * @param manifestostatus
	 * @return
	 */
	public Manifesto findByCodigoBarrasByAgrupamento(String codigobarras, Deposito deposito, Manifestostatus manifestostatus) {
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifestocodigobarras.cdmanifestocodigobarras, manifestocodigobarras.codigo, manifestocodigobarras.ativo, " +
						 "manifestocodigobarras.dt_inclusao, manifestocodigobarras.dt_alteracao, manifesto.cdmanifesto, transportador.nome, " +
						 "motorista.nome, veiculo.placa, manifesto.lacrelateral, manifesto.lacretraseiro, manifesto.dtemissao, " +
						 "tipoentrega.cdtipoentrega, tipoentrega.nome, motorista.cdmotorista, transportador.cdpessoa, veiculo.cdveiculo," +
						 "manifestostatus.cdmanifestostatus, manifestostatus.nome ");
			query.join("manifesto.listaManifestocodigobarra manifestocodigobarras");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifesto.transportador transportador");
			query.join("manifesto.deposito deposito");
			query.join("manifesto.tipoentrega tipoentrega");
			query.leftOuterJoin("manifesto.veiculo veiculo");
			query.leftOuterJoin("manifesto.motorista motorista");
			query.where("manifestocodigobarras.codigo = ?", codigobarras);
			query.where("manifestocodigobarras.ativo = 1");
			query.where("manifestostatus = ?",manifestostatus);
			query.where("deposito = ?",deposito);
			query.where("manifesto.manifestopai.cdmanifesto is null");
		
		return query.unique();
	}

	/**
	 * 
	 * @param whereIn
	 * @param cdmanifesto 
	 * @return
	 */
	public List<Manifesto> validarNotasVinculadas(String whereIn, Integer cdmanifesto){
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("manifesto.cdmanifesto");
			query.join("manifesto.listaManifestonotafiscal manifestonotafiscal");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifestonotafiscal.notafiscalsaida notafiscalsaida");
			query.whereIn("notafiscalsaida.cdnotafiscalsaida",whereIn);
			query.where("manifesto.cdmanifesto <> ?",cdmanifesto);
			query.where("manifestostatus <> ?",Manifestostatus.CANCELADO);
			
		return query.list();
	}

	/**
	 * 
	 * @param whereIn
	 * @param cdmanifesto
	 * @param whereInManifestos 
	 * @return
	 */
	public List<Manifesto> validarNotasVinculadasAgrupamento(String whereIn, Integer cdmanifesto, String whereInManifestos) {
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifesto.cdmanifesto");
			query.join("manifesto.listaManifestonotafiscal manifestonotafiscal");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifestonotafiscal.notafiscalsaida notafiscalsaida");
			query.whereIn("notafiscalsaida.cdnotafiscalsaida",whereIn);
			query.where("manifesto.cdmanifesto <> ?",cdmanifesto);
			query.where("manifesto.cdmanifesto not in ("+whereInManifestos+")");
			query.where("manifestostatus <> ?",Manifestostatus.CANCELADO);
			query.where("manifesto.manifestopai.cdmanifesto is null");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DescargaprodutoVO> findByDescargaProdutosByLoja(EmitirdescargaprodutoFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select p.codigoerp as codigo, p.descricao as descricao, substr(p.codigo,length(p.codigo),1) as voltagem, ");
		sql.append(" 		tp.nome as status ,sum(nfsp.qtde) as qtde, p.nome as loja, p.cdpessoa as cdcliente, m.cdmanifesto as manifesto ");
		sql.append(" from manifesto m ");
		sql.append(" join manifestonotafiscal mnf on mnf.cdmanifesto = m.cdmanifesto ");
		sql.append(" join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" join notafiscalsaidaproduto nfsp on nfsp.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append(" join pessoa p on p.cdpessoa = nfs.cdcliente ");
		sql.append(" join manifestocodigobarras mcb on mcb.cdmanifesto = m.cdmanifesto");
		sql.append(" join produto p on p.cdproduto = nfsp.cdproduto ");
		sql.append(" join pedidovendaproduto pvp on pvp.cdpedidovendaproduto = nfsp.cdpedidovendaproduto ");
		sql.append(" join tipopedido tp on tp.cdtipopedido = pvp.cdtipopedido ");
		sql.append(" where mcb.ativo = 1 ");
		
		if(filtro.getCdmanifesto()!=null)
			sql.append(" and m.cdmanifesto = ").append(filtro.getCdmanifesto());
		if(filtro.getCodigobarras()!=null)
			sql.append(" and mcb.codigo = ").append(filtro.getCodigobarras());
		
		sql.append(" group by p.codigoerp, p.codigo, p.descricao, tp.nome, p.nome, p.cdpessoa, m.cdmanifesto ");
		
		return getJdbcTemplate().query(sql.toString(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				DescargaprodutoVO descargaprodutoVO = new DescargaprodutoVO();
					
					descargaprodutoVO.setCodigo(rs.getString("codigo"));
					descargaprodutoVO.setDescricao(rs.getString("descricao"));
					descargaprodutoVO.setVoltagem(rs.getString("voltagem"));
					descargaprodutoVO.setStatus(rs.getString("status"));
					descargaprodutoVO.setQtde(rs.getLong("qtde"));
					descargaprodutoVO.setLoja(rs.getString("loja"));
					descargaprodutoVO.setManifesto(rs.getString("manifesto"));
					
				return descargaprodutoVO;
			}
		});
		
	}

	/**
	 * 
	 * @param cdmanifesto
	 * @return
	 */
	public Manifesto findCodigoByManifesto(Integer cdmanifesto) {
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("manifesto.cdmanifesto, manifestocodigobarra.codigo")
				.join("manifesto.listaManifestocodigobarra manifestocodigobarra")
				.where("manifesto.cdmanifesto = ?",cdmanifesto)
				.where("manifestocodigobarra.ativo = 1");
			
		return query.unique();
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public Manifesto findForExisteNotaTransferencia(Manifesto manifesto) {
		return query()
			.select("manifesto.cdmanifesto")
			.join("manifesto.listaManifestonotafiscal manifestonotafiscal")
			.join("manifestonotafiscal.notafiscalsaida notafiscalsaida")
			.join("notafiscalsaida.notafiscaltipo notafiscaltipo")
			.openParentheses()
				.where("notafiscaltipo = ?",Notafiscaltipo.TRANSFERENCIA)
				.or()
				.where("notafiscaltipo = ?",Notafiscaltipo.OUTROS)
			.closeParentheses()
			.whereIn("notafiscalsaida.cdnotafiscalsaida",CollectionsUtil.listAndConcatenate(manifesto.getListaManifestonotafiscal(), "notafiscalsaida.cdnotafiscalsaida", ","))
			.unique();
	}

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Manifesto> findAllManifestosFilhos(Manifesto manifesto) {
		
		QueryBuilder<Manifesto> query = query();
			
			query.select("manifesto.cdmanifesto, tipoentrega.cdtipoentrega, tipoentrega.nome");
			query.join("manifesto.tipoentrega tipoentrega");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifesto.manifestopai manifestopai");
			query.where("manifestostatus = ?",Manifestostatus.IMPRESSO);
			query.where("manifestopai = ?",manifesto);
			
		return query.list();
	}

	/**
	 * 
	 * @param codigoBarras
	 * @return
	 */
	public Manifesto findManifestoPaiWhenTransferencia(String codigoBarras) {
		
		QueryBuilder<Manifesto> query = query();
		
			query.select("manifesto.cdmanifesto, manifestopai.cdmanifesto, tipoentrega.cdtipoentrega");
			query.join("manifesto.tipoentrega tipoentrega");
			query.join("manifesto.manifestopai manifestopai");
			query.join("manifesto.listaManifestocodigobarra manifestocodigobarra");
			query.where("tipoentrega.cdtipoentrega = 1");
			query.where("manifestocodigobarra.ativo = 1");
			query.where("manifestocodigobarra.codigo = ?",codigoBarras);
		
		return query.unique();
	}
	
}