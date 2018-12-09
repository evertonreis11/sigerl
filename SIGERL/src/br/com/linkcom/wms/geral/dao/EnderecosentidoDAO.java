package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Enderecosentido;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EnderecosentidoDAO extends GenericDAO<Enderecosentido> {	
	/**
	 * 
	 * Método que recupera uma lista de todos os registros da tab endereço sentido ordenado pelo código da área e pelo sentido da rua
	 * 
	 * @author Arantes
	 * 
	 * @return List<Enderecosentido>
	 * 
	 */
	public List<Enderecosentido> findAll() {
		return query()
					.select("enderecosentido.cdenderecosentido, area.cdarea, area.nome, area.codigo, enderecosentido.rua, enderecosentido.sentido")
					.join("enderecosentido.area area")
					.where("enderecosentido.area.deposito=?", WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
					.orderBy("area.cdarea, enderecosentido.rua")
					.list();
	}
}