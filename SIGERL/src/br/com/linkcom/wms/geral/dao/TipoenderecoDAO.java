package br.com.linkcom.wms.geral.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipoendereco;
import br.com.linkcom.wms.geral.bean.vo.TipoenderecoVO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("tipoendereco.nome")
public class TipoenderecoDAO extends GenericDAO<Tipoendereco> {
	/**
	 * 
	 * Recupera os tipos de enderecos de acordo com o deposito logado na aplicacao
	 * 
	 * @param filtro
	 * @return uma lista do tipo endereco
	 * 
	 * @author Arantes
	 */
	public List<Tipoendereco> findByDeposito(Deposito filtro) {
		if(filtro == null)
			throw new WmsException("O parâmetro filtro não pode ser nulo.");
		
		return query()
					.select("tipoendereco.cdtipoendereco, tipoendereco.nome, deposito.cddeposito, tipoendereco.ordem")
					.join("tipoendereco.deposito deposito")
					.where("deposito=?", filtro)
					.orderBy("tipoendereco.ordem")
					.list();
	}
	
	/**
	 * 
	 * Salva a lista de enderecos
	 * 
	 * @param deposito
	 * 
	 * @author Pedro Gonçalves
	 */
	public void saveList(Deposito deposito){
		 new SaveOrUpdateStrategy(getHibernateTemplate(),deposito)
			 .saveOrUpdateManaged("listaTipoendereco")		 	
			 .execute();
	}
	
	/**
	 * Coleta estatísticas dos tipos de endereços.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TipoenderecoVO> findEstatisticas(Deposito deposito) {
		String sql = "SELECT te.nome, Count(e.cdendereco) enderecos, " +
						"Sum(CASE WHEN e.cdenderecostatus IN (3,4,5) THEN 1 ELSE 0 END) AS ocupados " +
			"FROM tipoendereco te " +
			"  left join endereco e ON e.cdtipoendereco = te.cdtipoendereco " +
			"WHERE te.cddeposito = ? " +
			"GROUP BY te.nome " +
			"ORDER BY te.nome ";
		
		return getJdbcTemplate().query(sql, new Integer[]{deposito.getCddeposito()} , new RowMapper(){
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoenderecoVO tipoenderecoVO = new TipoenderecoVO();
				tipoenderecoVO.setNome(rs.getString("nome"));
				tipoenderecoVO.setTotalEnderecos(rs.getInt("enderecos"));
				tipoenderecoVO.setOcupados(rs.getInt("ocupados"));
				return tipoenderecoVO;
			}
		});
	}

}
