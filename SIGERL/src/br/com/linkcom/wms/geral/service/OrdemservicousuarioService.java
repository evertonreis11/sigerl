package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;
import br.com.linkcom.wms.geral.dao.OrdemservicousuarioDAO;

public class OrdemservicousuarioService extends br.com.linkcom.wms.util.neo.persistence.GenericService<OrdemservicoUsuario> {
	
	private OrdemservicousuarioDAO ordemservicousuarioDAO;
	
	public void setOrdemservicousuarioDAO(OrdemservicousuarioDAO ordemservicousuarioDAO) {
		this.ordemservicousuarioDAO = ordemservicousuarioDAO;
	}
	
	/**
	 * 
	 * Método de referência ao DAO.
	 * Carrega uma lista ordem de serviço usuário pelo recebimento. Esta lista é tratada de tal forma que retorne uma String de nomes de 
	 * usuários.
	 * 
	 * @param recebimento
	 * @return String
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicousuarioDAO#loadByRecebimento(Recebimento)
	 * 
	 * @author Arantes
	 * 
	 */
	public String findByRecebimento(Recebimento recebimento) {
		List<OrdemservicoUsuario> listaOrdemservicoUsuario = ordemservicousuarioDAO.findByRecebimento(recebimento);
		Set<String> nomes = new HashSet<String>();
		
		if(listaOrdemservicoUsuario != null) {
			for (OrdemservicoUsuario ordemservicoUsuario : listaOrdemservicoUsuario) {
				if((ordemservicoUsuario.getUsuario() != null) && (ordemservicoUsuario.getUsuario().getNome() != null))
					nomes.add(ordemservicoUsuario.getUsuario().getNome());
			}
		}
		
		StringBuilder result = new StringBuilder();
		Iterator<String> iterator = nomes.iterator();
		while (iterator.hasNext()){
			result.append(iterator.next());
			if (iterator.hasNext())
				result.append(", ");
		}
		
		return result.toString();
	}
	
	/**
	 * Método de referência ao DAO.
	 * 
	 * Carrega a ordem de serviço usuário a partir da ordem de serviço.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.OrdemservicousuarioDAO#loadByOS(Ordemservico ordemservico)
	 * @param recebimento
	 * @param usuario
	 * @author Pedro Gonçalves
	 * @return
	 */
	public OrdemservicoUsuario loadByOS(Ordemservico ordemservico){
		return ordemservicousuarioDAO.loadByOS(ordemservico);
	}
	
	/*Singleton*/
	private static OrdemservicousuarioService instance;
	public static OrdemservicousuarioService getInstance(){
		if(instance == null)
			instance = Neo.getObject(OrdemservicousuarioService.class);
		
		return instance;
	}
	
	/**
	 * Associa o usuário a ordem de serviço
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param usuario
	 * @param os
	 */
	public OrdemservicoUsuario associarUsuario(Usuario usuario, Ordemservico os) {
		OrdemservicoUsuario osu = new OrdemservicoUsuario();// Faz a ligação da ordem de serviço com o usuario
		Timestamp timestamp = new Timestamp(System.currentTimeMillis()); 
		osu.setDtinicio(timestamp); // Como as ordens sao ligadas ao salvar o endereçamento, a data inicial e final são iguais
		osu.setDtfim(timestamp);
		osu.setOrdemservico(os);
		osu.setUsuario(usuario);
		saveOrUpdateNoUseTransaction(osu);
		
		return osu;
	}

	/**
	 * Remove a associação do usuário com a ordem de serviço.
	 * 
	 * @see OrdemservicousuarioDAO#desassociarUsuario(Usuario, Ordemservico)
	 * @author Giovane Freitas
	 * @param usuario
	 * @param ordemservico
	 */
	public void desassociarUsuario(Usuario usuario, Ordemservico ordemservico) {
		ordemservicousuarioDAO.desassociarUsuario(usuario, ordemservico);
	}

	/**
	 * Atualiza a hora de término de execução da ordem de serviço.
	 * 
	 * @see OrdemservicousuarioDAO#atualizarHoraFim(Usuario, Ordemservico, Timestamp)
	 * @author Giovane Freitas
	 * @param usuario
	 * @param ordemservico
	 * @param horaFim
	 */
	public void atualizarHoraFim(Usuario usuario, Ordemservico ordemservico, Timestamp horaFim) {
		ordemservicousuarioDAO.atualizarHoraFim(usuario, ordemservico, horaFim);
	}

	/**
	 * Localiza o último {@link OrdemservicoUsuario} a partir da ordem de serviço.
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @return
	 */
	public OrdemservicoUsuario findByOrdemservico(Ordemservico ordemservico) {
		return ordemservicousuarioDAO.findByOrdemservico(ordemservico);
	}

	public void atualizaPaletes(OrdemservicoUsuario osu) {
		ordemservicousuarioDAO.atualizaPaletes(osu);
	}

	/**
	 * Verifica se um determinado usuário possui o perfil necessário para ser
	 * associado a uma determinada ordem de serviço.
	 * 
	 * <TABLE  BORDER="1" CELLPADDING="2" CELLSPACING="0">
	 * <TR><TD><b>Ordem</b></TD><TD><b>Perfil</b></TD></TR>
	 * <TR><TD>Confer&ecirc;ncia de recebimento</TD><TD>Conferente de recebimento</TD></TR>
	 * <TR><TD>Reconfer&ecirc;ncia de recebimento</TD><TD>Conferente de recebimento</TD></TR>
	 * <TR><TD>Mapa de separa&ccedil;&atilde;o</TD><TD>Separador</TD></TR>
	 * <TR><TD>Confer&ecirc;ncia deexpedi&ccedil;&atilde;o</TD><TD>Conferente de expedi&ccedil;&atilde;o</TD></TR>
	 * <TR><TD>Reconfer&ecirc;ncia deexpedi&ccedil;&atilde;o</TD><TD>Conferente de expedi&ccedil;&atilde;o</TD></TR>
	 * <TR><TD>Reabastecimento de picking</TD><TD>Repositor</TD></TR>
	 * <TR><TD>Transfer&ecirc;ncia</TD><TD>Estoquista</TD></TR>
	 * <TR><TD>Endere&ccedil;amento avariado</TD><TD>Operador de transpaleteira ou Estoquista</TD></TR>
	 * <TR><TD>Endere&ccedil;amento fracionado</TD><TD>Operador de transpaleteira ou Estoquista</TD></TR>
	 * <TR><TD>Endere&ccedil;amento padr&atilde;o</TD><TD>Operador de empilhadeira ou Estoquista</TD></TR>
	 * <TR><TD>Contagem de invent&aacute;rio</TD><TD>Estoquista</TD></TR>
	 * <TR><TD>Recontagem</TD><TD>Estoquista</TD></TR>
	 * </TABLE>
	 * 
	 * @author Giovane Freitas
	 * @param ordemservico
	 * @param usuario
	 * @return
	 */
	public boolean isAssociacaoValida(Ordemtipo ordemtipo, Usuario usuario) {
		List<Usuariopapel> papeis = UsuariopapelService.getInstance().findBy(usuario, "papel.cdpapel");
		
		if (ordemtipo.equals(Ordemtipo.CONFERENCIA_RECEBIMENTO) && papeis.contains(Papel.CONFERENTE_DE_RECEBIMENTO)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.RECONFERENCIA_RECEBIMENTO) && papeis.contains(Papel.CONFERENTE_DE_RECEBIMENTO)){
			return true;		
		} else if (ordemtipo.equals(Ordemtipo.MAPA_SEPARACAO) && papeis.contains(Papel.SEPARADOR)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.CONFERENCIA_EXPEDICAO_1) && papeis.contains(Papel.CONFERENTE_EXPEDICAO)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.RECONFERENCIA_EXPEDICAO_1) && papeis.contains(Papel.CONFERENTE_EXPEDICAO)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.CONFERENCIA_EXPEDICAO_2) && papeis.contains(Papel.CONFERENTE_EXPEDICAO)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.CONFERENCIA_CHECKOUT) && papeis.contains(Papel.CONFERENTE_EXPEDICAO)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.REABASTECIMENTO_PICKING) && papeis.contains(Papel.REPOSITOR)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.TRANSFERENCIA) && papeis.contains(Papel.ESTOQUISTA)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.ENDERECAMENTO_AVARIADO) && (papeis.contains(Papel.OPERADOR_TRANSPALETEIRA) || papeis.contains(Papel.ESTOQUISTA))){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.ENDERECAMENTO_FRACIONADO) && (papeis.contains(Papel.OPERADOR_TRANSPALETEIRA) || papeis.contains(Papel.ESTOQUISTA))){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.ENDERECAMENTO_PADRAO) && (papeis.contains(Papel.OPERADOR_EMPILHADEIRA) || papeis.contains(Papel.ESTOQUISTA))){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.CONTAGEM_INVENTARIO) && papeis.contains(Papel.ESTOQUISTA)){
			return true;
		} else if (ordemtipo.equals(Ordemtipo.RECONTAGEM_INVENTARIO) && papeis.contains(Papel.ESTOQUISTA)){
			return true;
		}

		
		return false;
	}
	
}