package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Agendajanela;
import br.com.linkcom.wms.geral.bean.Agendajanelaclasse;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class AgendajanelaclasseDAO extends GenericDAO<Agendajanelaclasse> {
	
	public List<Agendajanelaclasse> findByAgendajanela(Agendajanela agendajanela){
		return
		query()
		.select("agendajanelaclasse.cdagendajanelaclasse," +
				"produtoclasse.cdprodutoclasse, produtoclasse.nome, produtoclasse.numero ")
		.join("agendajanelaclasse.produtoclasse produtoclasse")
		.where("agendajanelaclasse.agendajanela = ?", agendajanela)
		.orderBy("produtoclasse.nome")
		.list()
		;
		
	}
	
}
