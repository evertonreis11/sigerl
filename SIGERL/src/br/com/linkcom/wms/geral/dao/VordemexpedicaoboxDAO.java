package br.com.linkcom.wms.geral.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vordemexpedicaobox;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VordemexpedicaoboxDAO extends GenericDAO<Vordemexpedicaobox>{


	/**
	 * Lista os itens para a formação de conferência referente aos carregamentos de um box
	 * 
	 * @author Giovane Freitas
	 * @param box
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<Vordemexpedicaobox> findByBox(Box box) {
		if(box == null || box.getCdbox() == null)
			throw new WmsException("Box inválido.");

		return query()
			.joinFetch("vordemexpedicaobox.linhaseparacao linhaseparacao")
			.joinFetch("vordemexpedicaobox.carregamento carregamento")
			.joinFetch("vordemexpedicaobox.cliente cliente")
			.joinFetch("vordemexpedicaobox.tipooperacao tipooperacao")
			.whereIn("vordemexpedicaobox.carregamento.id", Util.collections.listAndConcatenate(box.getListaCarregamentos(), "cdcarregamento", ","))
			.orderBy("vordemexpedicaobox.box.id, vordemexpedicaobox.carregamento.id, vordemexpedicaobox.cliente.id, vordemexpedicaobox.tipooperacao.id, " +
					"vordemexpedicaobox.linhaseparacao.usacheckout,vordemexpedicaobox.linhaseparacao.nome, vordemexpedicaobox.produtoprincipal.id, vordemexpedicaobox.volume.id")
			.list();
	}

	public String getFiliaisClientes(String itensOrdem, Carregamento carregamento) {
		String[] strings = itensOrdem.split(",");
		StringBuilder chaves = new StringBuilder();
		boolean first = true;
		for (String chave : strings){
			if (!first)
				chaves.append(", ");
			else
				first = false;
			chaves.append("'").append(chave).append("'");
		}
		
		QueryBuilder<String> queryBuilder = new QueryBuilder<String>(getHibernateTemplate())
			.select("distinct cliente.nome")
			.from(Vordemexpedicaobox.class)
			.join("vordemexpedicaobox.cliente cliente")
			.whereIn("chave", chaves.toString())
			.orderBy("cliente.nome")
			.setUseTranslator(false);
		
		Iterator<String> iterator = queryBuilder.list().iterator();
		
		return StringUtils.join(iterator, "<br/>");
	}

	public String getTiposPedidos(String itensOrdem, Carregamento carregamento) {
		String[] strings = itensOrdem.split(",");
		StringBuilder chaves = new StringBuilder();
		boolean first = true;
		for (String chave : strings){
			if (!first)
				chaves.append(", ");
			else
				first = false;
			chaves.append("'").append(chave).append("'");
		}
		
		QueryBuilder<String> queryBuilder = new QueryBuilder<String>(getHibernateTemplate())
			.select("distinct tipooperacao.nome")
			.from(Vordemexpedicaobox.class)
			.join("vordemexpedicaobox.tipooperacao tipooperacao")
			.whereIn("chave", chaves.toString())
			.orderBy("tipooperacao.nome")
			.setUseTranslator(false);
		
		Iterator<String> iterator = queryBuilder.list().iterator();
		
		return StringUtils.join(iterator, "<br/>");
	}
	
}
