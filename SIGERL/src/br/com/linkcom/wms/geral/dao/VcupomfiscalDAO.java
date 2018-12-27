package br.com.linkcom.wms.geral.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.wms.geral.bean.view.Vcupomfiscal;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;
import br.com.ricardoeletro.sigerl.expedicao.crud.filtro.ManifestoFiltro;

public class VcupomfiscalDAO extends GenericDAO<Vcupomfiscal>{
	
	/**
	 * Retorna a lista de Cupons Fiscias emitidos nas lojas.
	 * Cria uma conexão direta com a base do sistema MVLojas.
	 * Número máximo de retorno: 30
	 * 
	 * @author Filipe Santos
	 * @param filtro
	 * @param connection
	 * @return
	 */
	public List<Vcupomfiscal> findByFiltro (ManifestoFiltro filtro, Connection connection){
		
		PreparedStatement statementView = null;
		ResultSet resultSetView = null;
		List<Vcupomfiscal> lista = new ArrayList<Vcupomfiscal>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT NRO_LOJA, NRO_NF, NRO_CUPOM, NRO_SEQ_NF, SERIE_NF, ");
		sql.append(" 		TO_DATE(TRUNC(DT_EMISSAO_NF),'dd/MM/rrrr') AS DT_EMISSAO_NF, ");
		sql.append(" 		NOME_RAZAO_SOCIAL_NF, ENDERECO_DEST, OBSERVACAO, IND_AVULSO, NRO_MANIFESTO, ");
		sql.append("		QTDE_ITENS_NF, VR_TOTAL_NF, NRO_CARGA_VEICULO, CLIENTE ");
		sql.append(" FROM V_TMS_NOTAS ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND ROWNUM <= 30 ");
		
		if(filtro.getNumeroCupom()!=null && !filtro.getNumeroCupom().isEmpty())
			sql.append(" AND NRO_CUPOM = ").append(filtro.getNumeroCupom()).append(" ");
		
		if(filtro.getNumeroNotaCupom()!=null && !filtro.getNumeroNotaCupom().isEmpty())
			sql.append(" AND NRO_NF = ").append(filtro.getNumeroNotaCupom()).append(" ");
		
		if(filtro.getCuponsSelecionados()!=null && !filtro.getCuponsSelecionados().isEmpty())
			sql.append(" AND NRO_SEQ_NF IN (").append(filtro.getCuponsSelecionados()).append(") "); 
		
		if(filtro.getDtEmissaoCupomInicio()!=null && filtro.getDtEmissaoCupomFim()!=null){
			sql.append(" AND DT_EMISSAO_NF BETWEEN ")
				.append(" TO_DATE('").append(filtro.getDtEmissaoCupomInicio()).append("','yyyy-MM-dd') ")
				.append(" AND ")
				.append(" TO_DATE('").append(filtro.getDtEmissaoCupomFim()).append("','yyyy-MM-dd') ");
		}
		
		if(filtro.getFilialCupom()!=null && filtro.getFilialCupom().getCdpessoa()!=null)
			sql.append(" AND NRO_LOJA_DESTINO = ").append(filtro.getFilialCupom().getCdpessoa()).append(" ");
		
		try {
			statementView = connection.prepareStatement(sql.toString());
			resultSetView = statementView.executeQuery();
			
			while(resultSetView.next()){
				
				Vcupomfiscal vcupomfiscal = new Vcupomfiscal();
				
					if(resultSetView.getLong("NRO_NF")!=0)
						vcupomfiscal.setNro_cupom(resultSetView.getLong("NRO_NF"));
					else if(resultSetView.getInt("NRO_CUPOM")!=0)
						vcupomfiscal.setNro_cupom(resultSetView.getLong("NRO_CUPOM"));
					
					if(resultSetView.getDate("DT_EMISSAO_NF")!=null)
						vcupomfiscal.setDt_emissao_nf(DateUtil.formataData(resultSetView.getDate("DT_EMISSAO_NF")));
					
					vcupomfiscal.setNro_loja(resultSetView.getLong("NRO_LOJA"));
					vcupomfiscal.setSerie_nf(resultSetView.getString("SERIE_NF"));
					vcupomfiscal.setNome_razao_social_nf(resultSetView.getString("NOME_RAZAO_SOCIAL_NF"));
					vcupomfiscal.setEndereco_dest(resultSetView.getString("ENDERECO_DEST"));
					vcupomfiscal.setObservacao(resultSetView.getString("OBSERVACAO"));
					vcupomfiscal.setInd_avulso(resultSetView.getBoolean("IND_AVULSO"));
					vcupomfiscal.setNro_manifesto(resultSetView.getInt("NRO_MANIFESTO"));
					vcupomfiscal.setQtde_itens_nf(resultSetView.getInt("QTDE_ITENS_NF"));
					vcupomfiscal.setVr_total_nf(resultSetView.getLong("VR_TOTAL_NF"));
					vcupomfiscal.setNro_carga_veiculo(resultSetView.getInt("NRO_CARGA_VEICULO"));
					vcupomfiscal.setCliente(resultSetView.getString("CLIENTE"));
					vcupomfiscal.setNro_seq_nf(resultSetView.getLong("NRO_SEQ_NF"));
					
				lista.add(vcupomfiscal);
			}
		} catch (Exception e) {
			throw new WmsException("Um erro ocorreu durante a geração da lista de Cupons Fiscais. ");
		} finally {
			try {
				resultSetView.close();
			} catch (SQLException e) {
				throw new WmsException("Erro ao fechar o resultSet.", e);
			}
			try {
				statementView.close();
			} catch (SQLException e) {
				throw new WmsException("Erro ao fechar o Statemant.", e);
			}
			try {
				connection.close();
			} catch (SQLException e) {
				throw new WmsException("Erro ao fechar a conexão o banco de dados.", e);
			}
		}
		return lista;
	}

}
