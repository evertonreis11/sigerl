package br.com.linkcom.wms.geral.service;

import java.util.Set;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariolinhaseparacao;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class UsuariolinhaseparacaoService extends GenericService<Usuariolinhaseparacao>{

	public void carregaLinhaSeparacao(Usuario usuario) {
		Set<Linhaseparacao> listaLinhaSeparacao = new ListSet<Linhaseparacao>(Linhaseparacao.class);
		for(Usuariolinhaseparacao usuariolinhaseparacao : usuario.getListaUsuarioLinhaSeparacao()){
			listaLinhaSeparacao.add(usuariolinhaseparacao.getLinhaseparacao());
		}
		usuario.setListaLinhaSeparacao(listaLinhaSeparacao);
	}

}
