package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariodeposito;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class UsuariodepositoDAO extends GenericDAO<Usuariodeposito> {

	/**
	 * Método usado para se obter uma lista de usuariodeposito atravez de um usuario
	 * @author Leonardo Guimarães
	 * @param usuario
	 * @return
	 */
	public List<Usuariodeposito> findByUser(Usuario usuario){
		if(usuario==null || usuario.getCdpessoa() == null){
			throw new WmsException("O parâmetro usuario não deve ser nulo");
		}
		return query()
				.select("usuariodeposito.cdusuariodeposito,usuario.cdpessoa, usuario.nome," +
						"deposito.cddeposito,deposito.nome,deposito.endereco,deposito.tamanho")
				.join("usuariodeposito.usuario usuario")
				.leftOuterJoin("usuariodeposito.deposito deposito")
				.where("usuario=?",usuario)
				.list();
	}	
}
