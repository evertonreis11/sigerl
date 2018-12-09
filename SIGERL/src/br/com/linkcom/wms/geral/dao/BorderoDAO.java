package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Bordero;
import br.com.linkcom.wms.geral.bean.vo.ExtratoBorderoVO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.BorderoFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class BorderoDAO extends GenericDAO<Bordero>{
	
	@Override
	public void updateListagemQuery(QueryBuilder<Bordero> query, FiltroListagem _filtro) {
		
		BorderoFiltro filtro = (BorderoFiltro) _filtro;
		
		query.select(" bordero.cdbordero, bordero.periodoinicial, bordero.periodofinal, bordero.qtdentregas, bordero.dtbordero, " +
					 " bordero.valortotal, tipoentrega.cdtipoentrega, tipoentrega.nome, transportador.cdpessoa, transportador.nome," +
					 " deposito.cddeposito, deposito.nome, empresa.cdempresa, empresa.nome ");
		query.join("bordero.transportador transportador");
		query.leftOuterJoin("bordero.tipoentrega tipoentrega");
		query.leftOuterJoin("bordero.deposito deposito");
		query.leftOuterJoin("bordero.empresa empresa");
		query.where("tipoentrega = ?",filtro.getTipoentrega());
		query.where("bordero.periodoinicial >= ?",filtro.getDtemissaoInicio());
		query.where("bordero.periodofinal <= ?",filtro.getDtemissaoFim());
		query.where("bordero.transportador = ?",filtro.getTransportador());
		query.where("bordero.cdbordero = ?",filtro.getNumeroBordero());
		query.where("deposito = ?",filtro.getDeposito());
		query.where("empresa = ?",filtro.getEmpresa());
		if(filtro.getNumeroManifesto()!=null && filtro.getNumeroManifesto()!=null){
			query.join("bordero.listaManifestobordero manifestobordero");
			query.join("manifestobordero.manifesto manifesto");
			query.where("manifesto.cdmanifesto = ?",filtro.getNumeroManifesto());
		}
		query.orderBy("bordero.cdbordero desc");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaManifestobordero");
	}
	
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ExtratoBorderoVO> findByRelatorioExtratoFinanceiro(BorderoFiltro filtro) {
		
		StringBuilder sql = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		
		sql.append(" select b.cdbordero as cdbordero, b.dtbordero as dtbordero, uc.nome as emitente, ");
		sql.append(" 		pt.cdpessoa as cdtransportador, pt.nome as transportadornome, pt.documento as cnpj, ");
		sql.append("		to_date(b.periodoinicial,'dd/MM/yyyy')||' ate '||to_date(b.periodofinal,'dd/MM/yyyy') as periodoFechamento, ");
		sql.append("		te.nome as tipoentrega, b.valortotal as valorTotal, b.qtdentregas as qtdentregas, ");
		sql.append("		e.nome as empresa, d.nome as deposito, ");
		sql.append(" 		(	select count(distinct nfs.cdpessoaendereco) ");
		sql.append(" 			from bordero b2 ");
		sql.append(" 			join manifestobordero mb on mb.cdbordero = b2.cdbordero ");
		sql.append(" 			join manifestonotafiscal mnf on mnf.cdmanifesto = mb.cdmanifesto ");
		sql.append(" 			join notafiscalsaida nfs on nfs.cdnotafiscalsaida = mnf.cdnotafiscalsaida ");
		sql.append("			where b2.cdbordero = b.cdbordero ");
		sql.append("			and  mnf.cdstatusconfirmacaoentrega = 2 ) as totalrealizado ");
		sql.append(" from bordero b ");
		sql.append(" left join deposito d on d.cddeposito = b.cddeposito ");
		sql.append(" left join empresa e on e.cdempresa = b.cdempresa ");
		sql.append(" join pessoa uc on uc.cdpessoa = b.cdusuariocriacao ");
		sql.append(" join pessoa pt on pt.cdpessoa = b.cdtransportador ");
		sql.append(" left join tipoentrega te on te.cdtipoentrega = b.cdtipoentrega ");
		sql.append(" where b.cdbordero = ").append(filtro.getCdbordero());
		
		return getJdbcTemplate().query(sql.toString(), args.toArray(), new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExtratoBorderoVO extratoBorderoVO = new ExtratoBorderoVO();
					extratoBorderoVO.setCdbordero(rs.getString("cdbordero"));
					extratoBorderoVO.setDtbordero(WmsUtil.stringToDefaulDateFormat(rs.getString("dtbordero"),"yyyy-MM-dd hh:mm:ss.SSS"));
					extratoBorderoVO.setEmitente(rs.getString("emitente"));
					extratoBorderoVO.setCdtransportador(rs.getString("cdtransportador"));
					extratoBorderoVO.setTransportadornome(rs.getString("transportadornome"));
					extratoBorderoVO.setCnpj(rs.getString("cnpj"));
					extratoBorderoVO.setPeriodoFechamento(rs.getString("periodoFechamento"));
					extratoBorderoVO.setTipoentrega(rs.getString("tipoentrega"));
					extratoBorderoVO.setValorTotal(rs.getString("valorTotal")==null ? "" : WmsUtil.formatarStringToMoney(rs.getString("valorTotal")));
					extratoBorderoVO.setQtdentregas(rs.getString("qtdentregas"));
					extratoBorderoVO.setDeposito(rs.getString("deposito"));
					extratoBorderoVO.setEmpresa(rs.getString("empresa"));
					extratoBorderoVO.setTotalrealizado(rs.getString("totalrealizado"));
				return extratoBorderoVO;
			}
		});
	}

	@Override
	public void delete(Bordero bean) {
		getHibernateTemplate().bulkUpdate("delete from Manifestobordero mb where mb.bordero.id = ?",new Integer[]{bean.getCdbordero()});
		super.delete(bean);
	}
	
}
