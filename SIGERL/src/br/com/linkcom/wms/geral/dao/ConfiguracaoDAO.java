package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Configuracao;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ConfiguracaoDAO extends GenericDAO<Configuracao> {
	
	
	/**
	 * Encontra todas as configura��es a partir de um dep�sito v�lido
	 * e ordena a partir do c�digo da configura��o.
	 * 
	 * @author Leonardo Guimar�es
	 * @param deposito
	 * @return
	 */
	public List<Configuracao> findByDeposito(Deposito deposito){
		if(deposito == null || deposito.getCddeposito() == null){
			throw new WmsException("O dep�sito deve ser nulo");
		}
		return query()
					.select("configuracao.cdconfiguracao,configuracao.nome,deposito.cddeposito,configuracao.valor")
					.join("configuracao.deposito deposito")
					.where("deposito=?",deposito)
					.orderBy("configuracao.cdconfiguracao")
					.list();
	}
	
	/**
	 * M�todo que recupera o valor do percentual de cooperativa de descarga de um recebimento
	 * 
	 * @param recebimento
	 * @return
	 * 
	 * @author Arantes
	 * @author Pedro Gon�alves - corre��o da maneira que estava pegando a configura��o.
	 */
	public Double loadPercentualConfiguracao() {
		String unique = newQueryBuilderWithFrom(String.class)
				.select("configuracao.valor")
				.leftOuterJoin("configuracao.deposito deposito")
				.where("configuracao.nome = ?", ConfiguracaoVO.PERCENTUAL_RETENCAO_DESCAGA)
				.where("deposito=?",WmsUtil.getDeposito())
				.setUseTranslator(false)	
				.unique();
		return (unique == null) ? new Double(0) : Double.parseDouble(unique);   
	}
	
	/**
	 * Retorna o valor de uma configra��o com base no dep�sito e no nome
	 * 
	 * @param deposito
	 * @param nome
	 * @return
	 * @author Fabr�cio Silva
	 */
	public String loadByName(Deposito deposito, String nome) {
		return newQueryBuilderWithFrom(String.class)
			.select("configuracao.valor")
			.where("configuracao.deposito = ?" , deposito)
			.where("configuracao.nome = ?", nome)
			.setUseTranslator(false)
			.unique();
	}

	/**
	 * Obt�m uma configura��o por nome. <br/>
	 * Utilize este m�todo para obter as configura��es que s�o independentes de dep�sito. 
	 * Se voc� usar este m�todo para consultar uma configura��o que depende de dep�sito, 
	 * uma exce��o poder� ser lan�ada caso exista dois registros com o mesmo nome de configura��o.
	 * 
	 * @author Giovane Freitas
	 * @param nomeConfig
	 * @return
	 */
	public String getConfigByName(String nomeConfig, Deposito deposito) {
		if(nomeConfig == null || nomeConfig.isEmpty()){
			throw new WmsException("O nome da configura��o � obrigat�rio.");
		}
		
		return loadByName(deposito, nomeConfig);
	}
	
	/**
	 * Retorna o valor de uma configra��o com base no dep�sito e no nome
	 * 
	 * @param deposito
	 * @param nome
	 * @return
	 * @author Fabr�cio Silva
	 */
	public Configuracao findByName(Deposito deposito, String nome) {
		QueryBuilder<Configuracao> query = query()
					.where("configuracao.nome = ?", nome);
		
		if (deposito != null){
			query.where("configuracao.deposito = ?" , deposito);
		}else
			query.where("configuracao.deposito is null ");
		
		return query.unique();
	}
	
}
