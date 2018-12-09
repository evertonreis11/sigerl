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
	 * Encontra todas as configurações a partir de um depósito válido
	 * e ordena a partir do código da configuração.
	 * 
	 * @author Leonardo Guimarães
	 * @param deposito
	 * @return
	 */
	public List<Configuracao> findByDeposito(Deposito deposito){
		if(deposito == null || deposito.getCddeposito() == null){
			throw new WmsException("O depósito deve ser nulo");
		}
		return query()
					.select("configuracao.cdconfiguracao,configuracao.nome,deposito.cddeposito,configuracao.valor")
					.join("configuracao.deposito deposito")
					.where("deposito=?",deposito)
					.orderBy("configuracao.cdconfiguracao")
					.list();
	}
	
	/**
	 * Método que recupera o valor do percentual de cooperativa de descarga de um recebimento
	 * 
	 * @param recebimento
	 * @return
	 * 
	 * @author Arantes
	 * @author Pedro Gonçalves - correção da maneira que estava pegando a configuração.
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
	 * Retorna o valor de uma configração com base no depósito e no nome
	 * 
	 * @param deposito
	 * @param nome
	 * @return
	 * @author Fabrício Silva
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
	 * Obtém uma configuração por nome. <br/>
	 * Utilize este método para obter as configurações que são independentes de depósito. 
	 * Se você usar este método para consultar uma configuração que depende de depósito, 
	 * uma exceção poderá ser lançada caso exista dois registros com o mesmo nome de configuração.
	 * 
	 * @author Giovane Freitas
	 * @param nomeConfig
	 * @return
	 */
	public String getConfigByName(String nomeConfig, Deposito deposito) {
		if(nomeConfig == null || nomeConfig.isEmpty()){
			throw new WmsException("O nome da configuração é obrigatório.");
		}
		
		return loadByName(deposito, nomeConfig);
	}
	
	/**
	 * Retorna o valor de uma configração com base no depósito e no nome
	 * 
	 * @param deposito
	 * @param nome
	 * @return
	 * @author Fabrício Silva
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
