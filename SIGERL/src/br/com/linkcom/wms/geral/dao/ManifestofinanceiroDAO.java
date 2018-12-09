package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestofinanceiro;
import br.com.linkcom.wms.geral.bean.vo.ExtratoFinanceiroVO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ManifestofinanceiroDAO extends GenericDAO<Manifestofinanceiro>{

	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public Manifestofinanceiro findByManifesto(Manifesto manifesto) {
		
		QueryBuilder<Manifestofinanceiro> query = query();
		
			query.where("manifestofinanceiro.manifesto = ?" ,manifesto);
		
		return query.unique();
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ExtratoFinanceiroVO> findByRelatorioExtratoFinanceiro(Manifesto manifesto) {
		
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
			
			sql.append(" select m.cdmanifesto as cdmanifesto, m.dtemissao as dtemissao, pt.nome as transportador, ");
			sql.append(" 		m.dtfinalizacao as dtfinalizacao, m.dtprestacaocontas as dtprestacao,  ");
			sql.append(" 		mf.entregasprevista as entregasprevistas, mf.valorcalculado as valorapagar, ");
			sql.append(" 		mf.acrescimo as valoradicional, mf.entregasrealizadas as entregasrealizadas, ");
			sql.append(" 		mf.entregasexcluida as entregasexcluida, mf.entregasretorno as entregasretorno, ");
			sql.append(" 		mf.total as valortotal, m.dtretornoveiculo as dtretornoveiculo,	r.nome as rotapraca, ");
			sql.append(" 		m.kminicial as kminicial, m.kmfinal as kmfinal, v.placa as veiculo, mt.nome as nomemotorista, ");
			sql.append("		sum( mnf.valorentrega) as valorentrega, count (nfs.numero) as numeronotas, ");
			sql.append("		(select count(distinct nfs2.cdpessoaendereco) from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2 ");
			sql.append("			where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
			sql.append("			and nfm2.cdmanifesto = m.cdmanifesto ");
			sql.append("			and nfs2.cdpraca = rp2.cdpraca ");
			sql.append("			and rp2.cdrota = r.cdrota) as ent_previstas, ");
			sql.append("		(select count(distinct nfs2.cdpessoaendereco) from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2 ");
			sql.append("			where nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
			sql.append("			and nfm2.cdmanifesto = m.cdmanifesto ");
			sql.append("			and nfs2.cdpraca = rp2.cdpraca ");
			sql.append("			and rp2.cdrota = r.cdrota ");
			sql.append("			and nfm2.cdstatusconfirmacaoentrega = 2) as ent_confirmadas, ");
			sql.append("		(select count(distinct nfs2.cdpessoaendereco) from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2 ");
			sql.append("			where  nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
			sql.append("			and nfm2.cdmanifesto = m.cdmanifesto  ");
			sql.append("			and nfs2.cdpraca =rp2.cdpraca  ");
			sql.append("			and rp2.cdrota = r.cdrota   ");
			sql.append("			and nfm2.cdstatusconfirmacaoentrega = 3) as retornos,   ");
			sql.append("		(select count(distinct nfs2.cdpessoaendereco) from manifestonotafiscal nfm2, notafiscalsaida nfs2, rotapraca rp2 ");
			sql.append("			where  nfs2.cdnotafiscalsaida = nfm2.cdnotafiscalsaida ");
			sql.append("			and nfm2.cdmanifesto = m.cdmanifesto  ");
			sql.append("			and nfs2.cdpraca =rp2.cdpraca  ");
			sql.append("			and rp2.cdrota = r.cdrota   ");
			sql.append("			and nfm2.cdstatusconfirmacaoentrega = 4) as exclusoes   ");			
			sql.append(" from manifesto m ");
			sql.append(" join manifestonotafiscal mnf on mnf.cdmanifesto = m.cdmanifesto ");
			sql.append(" left join manifestofinanceiro mf on mf.cdmanifesto = m.cdmanifesto ");
			sql.append(" join pessoa pt on pt.cdpessoa = m.cdtransportador ");
			sql.append(" join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
			sql.append(" join statusconfirmacaoentrega sce on sce.cdstatusconfirmacaoentrega = mnf.cdstatusconfirmacaoentrega ");
			sql.append(" join pessoa pc on pc.cdpessoa = nfs.cdcliente ");
			sql.append(" join praca p on p.cdpraca = nfs.cdpraca ");
			sql.append(" join rotapraca rp on rp.cdpraca = p.cdpraca ");
			sql.append(" join rota r on r.cdrota = rp.cdrota ");
			sql.append(" left join veiculo v on v.cdveiculo = m.cdveiculo ");
			sql.append(" left join motorista mt on mt.cdmotorista = m.cdmotorista ");
			sql.append(" where m.cdmanifesto =  ").append(manifesto.getCdmanifesto());
			sql.append(" group by m.cdmanifesto, m.dtemissao, pt.nome, m.dtfinalizacao, m.dtprestacaocontas,  ");
			sql.append(" 	mf.entregasprevista, mf.valorcalculado, mf.acrescimo, mf.entregasrealizadas, ");
			sql.append(" 	mf.entregasexcluida, mf.entregasretorno, mf.total, m.dtretornoveiculo,r.nome, ");
			sql.append(" 	r.cdrota, m.kminicial , m.kmfinal,v.placa, mt.nome  ");
			
			return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					ExtratoFinanceiroVO extratoFinanceiro = new ExtratoFinanceiroVO();
						extratoFinanceiro.setCdmanifesto(rs.getString("cdmanifesto") == null ? " " : rs.getString("cdmanifesto"));
//						extratoFinanceiro.setCliente(rs.getString("cliente") == null ? " " : rs.getString("cliente"));
						extratoFinanceiro.setDtemissao(rs.getString("dtemissao") == null ? " " : WmsUtil.stringToDefaulDateFormat(rs.getString("dtemissao"), "yyyy-MM-dd hh:mm:ss"));
//						extratoFinanceiro.setDtentrega(rs.getString("dataentrega") == null ? " " : WmsUtil.stringToDefaulDateFormat(rs.getString("dataentrega"), "yyyy-MM-dd hh:mm:ss"));
						extratoFinanceiro.setDtfechamento(rs.getString("dtfinalizacao") == null ? " " : WmsUtil.stringToDefaulDateFormat(rs.getString("dtfinalizacao"), "yyyy-MM-dd hh:mm:ss"));
						extratoFinanceiro.setDtprestacao(rs.getString("dtprestacao") == null ? " " : WmsUtil.stringToDefaulDateFormat(rs.getString("dtprestacao"), "yyyy-MM-dd hh:mm:ss"));
						extratoFinanceiro.setEntregasrealizadas(rs.getString("entregasrealizadas")  == null ? " " : rs.getString("entregasrealizadas"));
						extratoFinanceiro.setEntregasexcluida(rs.getString("entregasexcluida") == null ? " " : rs.getString("entregasexcluida"));
						extratoFinanceiro.setEntregasprevistas(rs.getString("entregasprevistas") == null ? " " : rs.getString("entregasprevistas"));
						extratoFinanceiro.setEntregasretorno(rs.getString("entregasretorno") == null ? " " : rs.getString("entregasretorno"));
//						extratoFinanceiro.setLojapedido(rs.getString("lojapedido") == null ? " " : rs.getString("lojapedido"));
//						extratoFinanceiro.setNumeronota(rs.getString("numeronota") == null ? " " : rs.getString("numeronota"));
//						extratoFinanceiro.setNumeropedido(rs.getString("numeropedido") == null ? " " : rs.getString("numeropedido"));
						extratoFinanceiro.setRotapraca(rs.getString("rotapraca") == null ? " " : rs.getString("rotapraca"));
//						extratoFinanceiro.setStatusentrega(rs.getString("statusentrega") == null ? " " : rs.getString("statusentrega"));
						extratoFinanceiro.setTransportador(rs.getString("transportador") == null ? " " : rs.getString("transportador"));
						extratoFinanceiro.setValoradicional(rs.getString("valoradicional") == null ? " " : WmsUtil.formatarStringToMoney(rs.getString("valoradicional")));
						extratoFinanceiro.setValorapagar(rs.getString("valorapagar") == null ? " " : WmsUtil.formatarStringToMoney(rs.getString("valorapagar")));
						extratoFinanceiro.setValorentregas(rs.getString("valorentrega") == null ? " " : WmsUtil.formatarStringToMoney(rs.getString("valorentrega")));
						extratoFinanceiro.setValortotal(rs.getString("valortotal") == null ? " " : WmsUtil.formatarStringToMoney(rs.getString("valortotal")));
						extratoFinanceiro.setKminicial(rs.getString("kminicial") == null ? " " : rs.getString("kminicial"));
						extratoFinanceiro.setKmfinal(rs.getString("kmfinal") == null ? " " : rs.getString("kmfinal"));
						extratoFinanceiro.setEntregaRotaConfirmada(rs.getString("ent_confirmadas") == null ? " " : rs.getString("ent_confirmadas"));
						extratoFinanceiro.setEntregaRotaRetorno(rs.getString("retornos") == null ? " " : rs.getString("retornos"));
						extratoFinanceiro.setEntregaRotaExcluidas(rs.getString("exclusoes") == null ? " " : rs.getString("exclusoes"));
						extratoFinanceiro.setEntregaRotaPrevista(rs.getString("ent_previstas") == null ? " " : rs.getString("ent_previstas"));
						extratoFinanceiro.setMotorista(rs.getString("nomemotorista") == null ? " " : rs.getString("nomemotorista"));
						extratoFinanceiro.setVeiculo(rs.getString("veiculo") == null ? " " : rs.getString("veiculo"));
						
					return extratoFinanceiro;
				}
			});
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param valorMonetario
	 */
	public void updateAcrescimo(Manifesto manifesto, Money valorMonetario) {
		
		Integer acrescimo = 0;
		
		if(valorMonetario!=null){
			String valor = valorMonetario.toString().replace(",","").replace(".","");		
			acrescimo = Integer.parseInt(valor); 
		}
		
		StringBuilder sql = new StringBuilder();
			sql.append(" update Manifestofinanceiro mf set mf.acrescimo = "+acrescimo+" where mf.cdmanifesto = ").append(manifesto.getCdmanifesto()); 
		getJdbcTemplate().execute(sql.toString());
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param valorMonetario
	 */
	public void updateDesconto(Manifesto manifesto, Money valorMonetario) {
		Integer desconto = 0;
		
		if(valorMonetario!=null){
			String valor = valorMonetario.toString().replace(",", "").replace(".", "");
			 desconto = Integer.parseInt(valor); 
		}
		
		StringBuilder sql = new StringBuilder();
			sql.append(" update Manifestofinanceiro mf set mf.desconto = "+desconto+" where mf.cdmanifesto = ").append(manifesto.getCdmanifesto()); 
		getJdbcTemplate().execute(sql.toString());
	}
}
