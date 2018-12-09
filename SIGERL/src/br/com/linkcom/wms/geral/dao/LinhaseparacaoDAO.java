package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.LinhaseparacaoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class LinhaseparacaoDAO extends GenericDAO<Linhaseparacao> {

	@Override
	public void updateListagemQuery(QueryBuilder<Linhaseparacao> query,FiltroListagem _filtro) {
		if(_filtro==null){
			throw new WmsException("O filtro não pode ser nulo");
		}
		LinhaseparacaoFiltro filtro=(LinhaseparacaoFiltro) _filtro;
		query
			.select("linhaseparacao.cdlinhaseparacao,linhaseparacao.nome,linhaseparacao.usacheckout")
			.join("linhaseparacao.deposito deposito")
			.whereLikeIgnoreAll("linhaseparacao.nome",filtro.getNome())
			.where("linhaseparacao.usacheckout=?",filtro.getUsacheckout())
			.where("deposito = ?",WmsUtil.getDeposito())
			.orderBy("linhaseparacao.nome");
	}
	
	@Override
	public List<Linhaseparacao> findForCombo(String orderby, String... extraFields) {
		return query()
					.select("linhaseparacao.cdlinhaseparacao, linhaseparacao.nome")
					.join("linhaseparacao.deposito deposito")
					.where("deposito = ?",WmsUtil.getDeposito())
					.orderBy("linhaseparacao.nome")
					.list();
	}
	
	/**
	 * Busca as linhas de separação associadas ao usuário
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param usuario
	 * @param deposito 
	 * @return
	 */
	public List<Linhaseparacao> findByUsuario(Usuario usuario, Deposito deposito) {
		if(usuario == null || usuario.getCdpessoa() == null)
			throw new WmsException("O usuário não deve ser nulo.");
		return query()
					.select("linhaseparacao.cdlinhaseparacao,linhaseparacao.nome")
					.join("linhaseparacao.listaUsuarioLinhaSeparacao uls")
					.join("uls.usuario usuario")
					.join("linhaseparacao.deposito deposito")
					.where("deposito = ?", deposito)					
					.where("usuario = ?",usuario)
					.list();
	}
	
	/**
	 * Busca as linhas de separação associadas ao depósito
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param deposito
	 * @return
	 */
	public List<Linhaseparacao> findByDeposito(Deposito deposito) {
		return query()
					.select("linhaseparacao.cdlinhaseparacao, linhaseparacao.nome")
					.join("linhaseparacao.deposito deposito")
					.where("deposito = ?", deposito)
					.orderBy("linhaseparacao.nome")
					.list();
	}

	/**
	 * Método que carrega linha de saparação a partide da ordem de serviço
	 * 
	 * @param ordemservico
	 * @return
	 * @auhtor Tomás Rabelo
	 */
	public Linhaseparacao findByOrdemservico(Ordemservico ordemservico) {
		return query()
			.select("linhaseparacao.cdlinhaseparacao, linhaseparacao.nome")
			.where("linhaseparacao.id in (select ls.id from Ordemprodutoligacao opl join opl.ordemservico os join opl.ordemservicoproduto osp join osp.produto p join p.listaDadoLogistico ldl join ldl.linhaseparacao ls join ldl.deposito d where d.id = "+WmsUtil.getDeposito().getCddeposito()+" and os.id = "+ordemservico.getCdordemservico()+")")
			.unique();
	}
	
}
