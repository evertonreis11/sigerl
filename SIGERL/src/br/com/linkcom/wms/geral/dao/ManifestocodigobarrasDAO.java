package br.com.linkcom.wms.geral.dao;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestocodigobarras;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ManifestocodigobarrasDAO extends GenericDAO<Manifestocodigobarras>{

	/**
	 * 
	 * @param manifesto
	 */
	public void desativaCodigoAntigo(Manifesto manifesto) {
		
		if(manifesto==null || manifesto.getCdmanifesto()==null){
			throw new WmsException("Parâmetros inválidos.");
		}
		
		getJdbcTemplate().execute("update manifestocodigobarras set ativo = 0, dt_alteracao = sysdate where cdmanifesto = "+manifesto.getCdmanifesto());
	}

	/**
	 * 
	 * @param numeroManifesto
	 * @return
	 */
	public Manifestocodigobarras findByCodigo(String codigo, Deposito deposito, Manifestostatus manifestostatus) {
		
		QueryBuilder<Manifestocodigobarras> query = query();
			
			query.select("manifestocodigobarras.cdmanifestocodigobarras, manifestocodigobarras.codigo, manifestocodigobarras.ativo, " +
						"manifestocodigobarras.dt_inclusao, manifestocodigobarras.dt_alteracao, manifesto.cdmanifesto, manifestostatus.cdmanifestostatus," +
						"manifestostatus.nome, transportador.cdpessoa, transportador.nome, deposito.cddeposito, deposito.nome, veiculo.cdveiculo," +
						"veiculo.placa, motorista.cdmotorista, motorista.nome, manifesto.lacrelateral, manifesto.lacretraseiro");
			query.join("manifestocodigobarras.manifesto manifesto");
			query.join("manifesto.manifestostatus manifestostatus");
			query.join("manifesto.transportador transportador");
			query.join("manifesto.deposito deposito");
			query.join("manifesto.veiculo veiculo");
			query.join("manifesto.motorista motorista");
			query.whereLikeIgnoreAll("manifestocodigobarras.codigo", codigo);
			query.where("manifestocodigobarras.ativo = 1");
			query.where("deposito = ?",deposito);
			query.where("manifestostatus = ?", manifestostatus);
			
		return query.unique();
	}

	/**
	 * 
	 * @param manifesto
	 */
	public Manifestocodigobarras findCodigoByManifesto(Manifesto manifesto) {
		
		QueryBuilder<Manifestocodigobarras> query = query();
		
		query.select("manifestocodigobarras.cdmanifestocodigobarras, manifestocodigobarras.codigo")
			.join("manifestocodigobarras.manifesto manifesto")
			.where("manifesto = ?",manifesto);
		
		return query.unique();
	}
	
}
