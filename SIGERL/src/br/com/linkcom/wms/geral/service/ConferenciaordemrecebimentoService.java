package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Conferenciaordemrecebimento;
import br.com.linkcom.wms.geral.bean.Ordemprodutohistorico;
import br.com.linkcom.wms.geral.bean.Produtoembalagem;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.dao.ConferenciaordemrecebimentoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ConferenciaordemrecebimentoService extends GenericService<Conferenciaordemrecebimento> {
	/* singleton */
	private static ConferenciaordemrecebimentoService instance;
	public static ConferenciaordemrecebimentoService getInstance(){
		if(instance == null){
			instance = Neo.getObject(ConferenciaordemrecebimentoService.class);
		}
		return instance;
	}
	
	public List<Conferenciaordemrecebimento> recuperaConferenciasPorRecebimento(Recebimento recebimento){
		return getDAO().recuperaConferenciasPorRecebimento(recebimento);
	}
	
	/**
	 * Creates the conferencia ordem.
	 *
	 * @param ordemProdutoHistorico the ordem produto historico
	 * @param recebimento the recebimento
	 * @param qtde the qtde
	 * @param isAvaria the is avaria
	 * @param embalagem the embalagem
	 * @return the conferenciaordemrecebimento
	 */
	public Conferenciaordemrecebimento createConferenciaOrdem(Ordemprodutohistorico ordemProdutoHistorico, 
			Recebimento recebimento, Long qtde, Boolean isAvaria, Produtoembalagem embalagem){
		
		Conferenciaordemrecebimento conferencia = new Conferenciaordemrecebimento();
		conferencia.setOrdemprodutohistorico(ordemProdutoHistorico);
		conferencia.setRecebimento(recebimento);
		conferencia.setQtde(qtde);
		conferencia.setIscoletaavaria(isAvaria);
		conferencia.setProdutoembalagem(embalagem);
		conferencia.setConferenciafinalizada(Boolean.FALSE);
		saveOrUpdateNoUseTransaction(conferencia);
		
		return conferencia;
	}

	/**
	 * Limpa as conferencia realizadas até o momento para este recebimento.
	 *
	 * @param recebimento the recebimento
	 */
	public void resetarQuantidades(Recebimento recebimento) {
		getDAO().excluiConferenciasPorRecebimento(recebimento);
		
	}
	
	private ConferenciaordemrecebimentoDAO getDAO(){
		return ((ConferenciaordemrecebimentoDAO)getGenericDAO());
	}
	
}
