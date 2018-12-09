package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Manifesto;
import br.com.linkcom.wms.geral.bean.Manifestohistorico;
import br.com.linkcom.wms.geral.bean.Manifestostatus;
import br.com.linkcom.wms.geral.bean.Tipomanifestohistorico;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.vo.Auditoria;
import br.com.linkcom.wms.geral.dao.ManifestohistoricoDAO;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ManifestohistoricoService extends GenericService<Manifestohistorico>{

	private ManifestohistoricoDAO manifestohistoricoDAO;
	
	public void setManifestohistoricoDAO(ManifestohistoricoDAO manifestohistoricoDAO) {
		this.manifestohistoricoDAO = manifestohistoricoDAO;
	}

	/**
	 * Método que cria log's das alterações nos manifestos.
	 * 
	 * @param manifesto
	 * @param msgMotivoDefault
	 * @param manifestostatus
	 */
	public void criarHistorico(Manifesto manifesto, String msgMotivoDefault, Manifestostatus manifestostatus, Usuario usuario, Tipomanifestohistorico tipo) {
		
		Manifestohistorico manifestohistorico = new Manifestohistorico();
			manifestohistorico.setDtalteracao(new Timestamp(System.currentTimeMillis()));
			manifestohistorico.setUsuario(WmsUtil.getUsuarioLogado(usuario));
			manifestohistorico.setMotivo(msgMotivoDefault);
			manifestohistorico.setManifestostatus(manifestostatus);
			manifestohistorico.setManifesto(manifesto);
			manifestohistorico.setTipomanifestohistorico(tipo);
		this.saveOrUpdate(manifestohistorico);
		
	}
	
	/**
	 * 
	 * @param manifesto
	 * @param manifestostatus
	 * @param auditoria
	 */
	public void criarHistoricoWithUsuario(Manifesto manifesto, Manifestostatus manifestostatus, Auditoria auditoria, Tipomanifestohistorico tipo) {
		
		Manifestohistorico manifestohistorico = new Manifestohistorico();
			manifestohistorico.setDtalteracao(new Timestamp(System.currentTimeMillis()));
			manifestohistorico.setUsuario(auditoria.getUsuario());
			manifestohistorico.setMotivo(auditoria.getMotivo());
			manifestohistorico.setManifestostatus(manifestostatus);
			manifestohistorico.setManifesto(manifesto);
			manifestohistorico.setTipomanifestohistorico(tipo);
		this.saveOrUpdate(manifestohistorico);
		
	}
	
	/**
	 * 
	 * @param manifesto
	 * @return
	 */
	public List<Manifestohistorico> findByManifesto(Manifesto manifesto) {
		return manifestohistoricoDAO.findByManifesto(manifesto);
	}
	
	/**
	 * 
	 * @param manifesto
	 */
	public void deleteByManifesto(String whereIn) {
		manifestohistoricoDAO.deleteByManifesto(whereIn);
	}
	
	private static ManifestohistoricoService instance;
	public static ManifestohistoricoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ManifestohistoricoService.class);
		}
		return instance;

	}
	
}