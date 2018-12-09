package br.com.linkcom.wms.geral.dao;

import java.sql.Timestamp;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class OrdemservicousuarioDAO extends GenericDAO<OrdemservicoUsuario>{
	
	/**
	 * 
	 * Carrega uma ordem de serviço usuário pelo recebimento.
	 * 
	 * @param filtro
	 * @return List<OrdemservicoUsuario>
	 * 
	 * @author Arantes
	 * 
	 */
	public List<OrdemservicoUsuario> findByRecebimento(Recebimento filtro) {
		if(filtro == null || filtro.getCdrecebimento() == null)
			throw new WmsException("O filtro não deve ser nulo");
		
		return query()
					.leftOuterJoin("ordemservicoUsuario.ordemservico ordemservico")
					.leftOuterJoin("ordemservico.recebimento recebimento")
					.joinFetch("ordemservicoUsuario.usuario usuario")
					.where("recebimento = ?", filtro)
					.list();
	}
	
	/**
	 * 
	 * Carrega o último {@link OrdemservicoUsuario} de uma determinada ordem de serviço.
	 * 
	 * @param filtro
	 * @return List<OrdemservicoUsuario>
	 * 
	 * @author Giovane Freitas
	 * 
	 */
	public OrdemservicoUsuario findByOrdemservico(Ordemservico filtro) {
		if(filtro == null || filtro.getCdordemservico() == null)
			throw new WmsException("O filtro não deve ser nulo");
		
		return query()
					.join("ordemservicoUsuario.ordemservico ordemservico")
					.joinFetch("ordemservicoUsuario.usuario usuario")
					.where("ordemservico = ?", filtro)
					.orderBy("ordemservicoUsuario.cdordemservicousuario desc")
					.setMaxResults(1)
					.unique();
	}
	
	/**
	 * Carrega a ordem de serviço usuário a partir da ordem de serviço.
	 *  
	 * @param recebimento
	 * @param usuario
	 * @author Pedro Gonçalves
	 * @return
	 */
	public OrdemservicoUsuario loadByOS(Ordemservico ordemservico){
		if(ordemservico == null || ordemservico.getCdordemservico() == null )
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
				.select("ordemservicousuario.cdordemservicousuario,usuario.cdpessoa, ordemservicousuario.paletes," +
						"ordemservico.cdordemservico, ordemservicousuario.dtinicio,ordemservicousuario.dtfim," +
						"usuario.nome")
				.from(OrdemservicoUsuario.class,"ordemservicousuario")
				.leftOuterJoin("ordemservicousuario.usuario usuario")
				.leftOuterJoin("ordemservicousuario.ordemservico ordemservico")
				.where("ordemservico = ?",ordemservico)
				.setMaxResults(1)
				.unique();
	}

	/**
	 * Remove a associação do usuário com a ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param usuario
	 * @param ordemservico
	 */
	public void desassociarUsuario(Usuario usuario, Ordemservico ordemservico) {
		if (usuario == null || usuario.getCdpessoa() == null)
			throw new WmsException("O usuário não deve ser nulo.");
		if (ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de serviço não deve ser nula.");
		
		getHibernateTemplate().bulkUpdate("delete from " + OrdemservicoUsuario.class.getName() + 
				" osu where osu.usuario = ? and osu.ordemservico = ? ", new Object[]{usuario, ordemservico});
	}

	/**
	 * Atualiza a hora de término de execução da ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param usuario
	 * @param ordemservico
	 * @param horaFim
	 */
	public void atualizarHoraFim(Usuario usuario, Ordemservico ordemservico, Timestamp horaFim) {
		if (usuario == null || usuario.getCdpessoa() == null)
			throw new WmsException("O usuário não deve ser nulo.");
		if (ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("A ordem de serviço não deve ser nula.");
		
		getHibernateTemplate().bulkUpdate("update " + OrdemservicoUsuario.class.getName() + 
				" osu set osu.dtfim = ? where osu.usuario = ? and osu.ordemservico = ? ", 
				new Object[]{horaFim, usuario, ordemservico});
	}

	public void atualizaPaletes(OrdemservicoUsuario osu) {
		if (osu == null || osu.getCdordemservicousuario() == null)
			throw new WmsException("Parâmetro inválido.");
		
		getHibernateTemplate().bulkUpdate("update " + OrdemservicoUsuario.class.getName() + 
				" osu set osu.paletes = ?, dtfim = sysdate where osu.id = ? ", 
				new Object[]{osu.getPaletes(), osu.getCdordemservicousuario()});
	}

}
