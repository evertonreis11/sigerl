package br.com.linkcom.wms.geral.dao;
 
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.Cnpj;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipodeposito;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.DepositoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("deposito.nome")
public class DepositoDAO extends GenericDAO<Deposito> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Deposito> query,FiltroListagem _filtro) {
		DepositoFiltro filtro = (DepositoFiltro) _filtro;
		query
			.select("deposito.cddeposito,deposito.nome,deposito.endereco,deposito.tamanho,deposito.ativo")
			.whereLikeIgnoreAll("deposito.nome",filtro.getNome());
	}
	
	/**
	 * Retorna os depósitos dos usuários
	 * @param usuario
	 * @return
	 * @author Cíntia Nogueira
	 */
	public List<Deposito> findDepositos(Usuario usuario){
		if(usuario==null || usuario.getCdpessoa()==null){
			throw new WmsException("Usuário não pode ser nulo em usuárioDAO");
		}
		return query()
				.select("deposito.cddeposito,deposito.nome")
				.join("deposito.listaUsuarioDeposito listaUsuarioDeposito")
				.where("listaUsuarioDeposito.usuario=?", usuario)
				.list();
	}	
	
	/**
	 * 
	 * @param cnpj
	 * @return
	 */
	public Deposito findByCNPJ(Cnpj cnpj){
		if(cnpj == null){
			throw new WmsException("O CNPJ não deve ser nulo");
		}
		return query()
		.select("deposito.cddeposito, deposito.cnpj ")
		.where("deposito.cnpj = ? ", cnpj)
		.unique();
	}
	
	/**
	 * Depositos ativos nos quais o usuário tem permissão
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivos(Usuario usuario){
		if(usuario==null || usuario.getCdpessoa()==null){
			throw new WmsException("Usuário não pode ser nulo em usuárioDAO");
		}
		return query()
			.select("deposito.cddeposito,deposito.nome")
			.join("deposito.listaUsuarioDeposito listaUsuarioDeposito")
			.join("listaUsuarioDeposito.deposito depositoUsuario")
			.where("listaUsuarioDeposito.usuario=?", usuario)
			.where("depositoUsuario.ativo=1")
			.list();
	}
	
	/**
	 * Depositos ativos nos quais o usuário tem permissão
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivosIndex(){
		return query()
			.select("deposito.cddeposito,deposito.nome")
			.where("deposito.ativo=1")
			.where("deposito.tmsativo = 0")
			.list();
	}
	
	/**
	 * Depositos ativos nos quais o usuário tem permissão
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivosIndexByUsuario(Usuario usuario){
		return query()
				.select("deposito.cddeposito,deposito.nome")
				.join("deposito.listaUsuarioDeposito listaUsuarioDeposito")
				.where("listaUsuarioDeposito.usuario=?", usuario)
				.where("deposito.ativo=1")
				// Comentado devido a aplicação passar a ter acesso ao todos os depósitos.
				// Everton Reis das Dores - 14/04/2016
//				.where("deposito.tmsativo = 0")
				.list();
	}
	
	/**
	 * Retorna a empresa vinculada ao deposito selecionado.
	 * 
	 * @author Filipe Santos
	 * @param deposito
	 * @return Deposito com Empresa.
	 */
	public Deposito findEmpresaByDeposito(Deposito deposito) {
		if(deposito==null || deposito.getCddeposito()==null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
			.joinFetch("deposito.empresa empresa")
			.where("deposito = ?",deposito)
			.unique();
	}

	/**
	 * Depositos ativos
	 * @author Filipe Santos
	 * @return Lista de Deposito
	 */
	public List<Deposito> findAtivos(){
		return query()
			.select("deposito.cddeposito,deposito.nome")
			.where("deposito.ativo=1")
			.list();			
	}

	/**
	 * 
	 * @return
	 */
	public List<Deposito> findAllCdsAtivo() {
		return query()
			.select("deposito.cddeposito,deposito.nome")
			.join("deposito.tipodeposito tipodeposito")
			.where("deposito.ativo=1")
			.where("tipodeposito = ?",Tipodeposito.CD)
		.list();	
	}

	/**
	 * 
	 * @return
	 */
	public List<Deposito> findAllCdasAtivo() {
		return query()
			.select("deposito.cddeposito,deposito.nome")
			.join("deposito.tipodeposito tipodeposito")
			.where("deposito.ativo=1")
			.where("tipodeposito = ?",Tipodeposito.CDA)
		.list();	
	}
}
