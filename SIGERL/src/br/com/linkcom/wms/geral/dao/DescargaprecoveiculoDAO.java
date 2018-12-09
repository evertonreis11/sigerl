package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Descargapreco;
import br.com.linkcom.wms.geral.bean.Descargaprecoveiculo;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

/**
 * M�todo que recupera uma lista de descarga pre�o ve�culo para um determinado descarga pre�o
 * 
 * @param descargapreco
 * @return
 *  
 * @author Arantes
 */
public class DescargaprecoveiculoDAO extends GenericDAO<Descargaprecoveiculo> {
	public List<Descargaprecoveiculo> findByDescargapreco(Descargapreco descargapreco) {
		return query()
				.select("descargaprecoveiculo.cddescargaprecoveiculo, tipoveiculo.nome, tipoveiculo.cdtipoveiculo,tipoveiculo.nome")
				.join("descargaprecoveiculo.tipoveiculo tipoveiculo")
				.join("descargaprecoveiculo.descargapreco descargapreco")
				.where("descargapreco = ?", descargapreco)
				.list();					
	}
}
