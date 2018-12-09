package br.com.linkcom.wms.geral.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Embalagemexpedicao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class EmbalagemexpedicaoDAO extends GenericDAO<Embalagemexpedicao>{

	
	/**
	 * Exclui todas as embalagens associadas a uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 */
	public void deleteByOrdem(Ordemservico ordemservico) {
		String hql = "delete from Embalagemexpedicao ee where ee.ordemservico.id = ? ";
		getHibernateTemplate().bulkUpdate(hql, ordemservico.getCdordemservico());
	}

	/**
	 * Localiza a última embalagem que ainda não foi finalizada.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public Embalagemexpedicao findEmbalagemAberta(Ordemservico ordemservico) {
		return query()
			.leftOuterJoinFetch("embalagemexpedicao.listaEmbalagemexpedicaoproduto embalagemproduto")
			.where("embalagemexpedicao.ordemservico = ?", ordemservico)
			.where("embalagemexpedicao.lacre is null")
			.unique();
	}

	/**
	 * Localiza as embalagens de uma determinada ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return 
	 */
	public List<Embalagemexpedicao> findByOrdem(Ordemservico ordemservico) {
		return query()
			.joinFetch("embalagemexpedicao.ordemservico ordemservico")
			.joinFetch("ordemservico.expedicao expedicao")
			.joinFetch("expedicao.box box")
			.where("embalagemexpedicao.ordemservico = ?", ordemservico)
			.list();
	}
	
	/**
	 * Localiza as embalagens de produtos que usam checkout que vão pra o mesmo cliente.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public List<Embalagemexpedicao> findByPrimeiraConferencia(Ordemservico ordemservico){
		if (ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		if(ordemservico.getCarregamento()==null || ordemservico.getCarregamento().getCdcarregamento()==null){
			List<Embalagemexpedicao> list = new ArrayList<Embalagemexpedicao>();
			return list;
		}	
		
		if(ordemservico.getTipooperacao() != null && ordemservico.getTipooperacao().getSeparacliente() != null && ordemservico.getTipooperacao().getSeparacliente()==true){
			return query()
			.join("embalagemexpedicao.ordemservico ordemservico")
			.where("ordemservico.ordemtipo = ?", Ordemtipo.CONFERENCIA_CHECKOUT)
			.where("ordemservico.carregamento = ?", ordemservico.getCarregamento())
			.where("ordemservico.clienteExpedicao = ?", ordemservico.getClienteExpedicao())
			.where("ordemservico.expedicao = ?", ordemservico.getExpedicao())
			.where("ordemservico <> ?", ordemservico)
			.list();
		}else{
			return query()
			.join("embalagemexpedicao.ordemservico ordemservico")
			.where("ordemservico.ordemtipo = ?", Ordemtipo.CONFERENCIA_CHECKOUT)
			.where("ordemservico.carregamento = ?", ordemservico.getCarregamento())
			.where("ordemservico.tipooperacao = ?", ordemservico.getTipooperacao())
			.where("ordemservico.expedicao = ?", ordemservico.getExpedicao())
			.where("ordemservico <> ?", ordemservico)
			.list();
		}
	}
	
	/**
	 * Verifica se existem mais de uma embalagem aberta sem lacre na OS.
	 * 
	 * @author Filipe Santos
	 * @param ordemservico
	 * @since 30/01/2012
	 * @return
	 */	
	public List<Embalagemexpedicao> findEmabalgemAbertas(Ordemservico ordemservico){
		if (ordemservico == null || ordemservico.getCdordemservico() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.join("embalagemexpedicao.ordemservico ordemservico")			
			.where("embalagemexpedicao.lacre is null")
			.where("ordemservico = ?",ordemservico)
			.list();
	}
	
	/**
	 * Retorna a embalagem com o lacre a ordemservico correspondente.
	 * 
	 * @author Filipe
	 * @param embalagem
	 * @return {@link Embalagemexpedicao}
	 */
	public Embalagemexpedicao findByLacre(Embalagemexpedicao embalagem){
		if (embalagem == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.leftOuterJoinFetch("embalagemexpedicao.ordemservico ordemservico")
			.leftOuterJoinFetch("embalagemexpedicao.listaEmbalagemexpedicaoproduto listaEmbalagemexpedicaoproduto")
			.where("embalagemexpedicao.lacre = ?",embalagem.getLacre())
			.where("embalagemexpedicao.ordemservico = ?",embalagem.getOrdemservico())
			.unique();
	}
}