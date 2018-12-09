package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemservicoprodutoerrado;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class OrdemservicoprodutoerradoDAO extends GenericDAO<Ordemservicoprodutoerrado> {

	/**
	 * Localiza os bloqueios pendentes de autorização para uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Ordemservicoprodutoerrado> findBloqueiosAtivos(Ordemservico ordemservico) {
		return query()
			.joinFetch("ordemservicoprodutoerrado.usuariobloqueio usuariobloqueio")
			.leftOuterJoinFetch("ordemservicoprodutoerrado.produto produto")
			.leftOuterJoinFetch("produto.produtoprincipal produtoprincipal")
			.where("ordemservicoprodutoerrado.ordemservico = ?", ordemservico)
			.where("ordemservicoprodutoerrado.usuariolibera is null")
			.list();
	}

	/**
	 * Excluir todos os registros associados a um determinado carregamento.
	 * 
	 * @author Giovane Freitas
	 * @param carregamento
	 */
	public void deleteByCarregamento(Carregamento carregamento) {
		String hql = "delete from Ordemservicoprodutoerrado ospe " +
				"where ospe.ordemservico.id in (select os.id from Ordemservico os where os.carregamento.id = ?)";
		
		getHibernateTemplate().bulkUpdate(hql, carregamento.getCdcarregamento());
	}

}
