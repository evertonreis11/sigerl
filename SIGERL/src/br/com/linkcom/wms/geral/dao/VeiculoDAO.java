package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Veiculo;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.VeiculoFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

@DefaultOrderBy("veiculo.placa")
public class VeiculoDAO extends GenericDAO<Veiculo> {
	
	@Override
	public void updateListagemQuery(QueryBuilder<Veiculo> query, FiltroListagem _filtro) {
		VeiculoFiltro filtro = (VeiculoFiltro) _filtro;
		query
			.select("veiculo.cdveiculo, veiculo.placa, veiculo.modelo, veiculo.placaPrimeiraCarreta, "
					+ "veiculo.placaSegundaCarreta, tipoveiculo.cdtipoveiculo, tipoveiculo.nome, veiculo.disponivel")
			.leftOuterJoin("veiculo.transportador transportador")
			.leftOuterJoin("veiculo.tipoveiculo tipoveiculo")
			.where("veiculo.deposito=?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.whereLikeIgnoreAll("veiculo.placa", filtro.getPlaca())
			.whereLikeIgnoreAll("veiculo.modelo", filtro.getModelo())
			.where("tipoveiculo = ?",filtro.getTipoveiculo())
			.where("veiculo.disponivel = ?",filtro.getDisponivel())
			.where("transportador = ?",filtro.getTransportador());
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Veiculo> query) {
		query
			.leftOuterJoinFetch("veiculo.deposito deposito")
			.leftOuterJoin("veiculo.transportador transportador")
			.leftOuterJoin("veiculo.tipoveiculo tipoveiculo")
			.leftOuterJoin("veiculo.tipopalete tipopalete");
	}

	/**
	 * Encontra todos os veículos a partir do tipo.
	 * 
	 * @param tipoveiculo
	 * @param disponivel - Caso necessite apenas dos veículos disponíveis, este campo deverá estar como true.
	 * 					 - Este parâmetro pode ser nulo.
	 * @return
	 * 
	 * @author Pedro Gonçalves
	 */
	public List<Veiculo> findBy(Tipoveiculo tipoveiculo, Boolean disponivel){
		if(tipoveiculo == null || tipoveiculo.getCdtipoveiculo() == null || disponivel == null)
			throw new WmsException("Parâmetros incorretos.");
		
		if(disponivel != null && !disponivel)
			disponivel = null;
		
		QueryBuilder<Veiculo> query = makeDefaultQueryVeiculo();
		
		return query
				.select("veiculo.cdveiculo, veiculo.placa")
				.join("veiculo.tipoveiculo tipoveiculo")
				.where("veiculo.disponivel=? ",disponivel)
				.where("tipoveiculo=?",tipoveiculo)
				.list();
	}
	
	/**
	 * Busca todos os dados do veículo
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param veiculo
	 * @return
	 */
	public Veiculo findByVeiculo(Veiculo veiculo) {
		if(veiculo == null || veiculo.getCdveiculo() == null)
			throw new WmsException("O veiculo não deve ser nulo.");
		
		QueryBuilder<Veiculo> query = makeDefaultQueryVeiculo();
		
		return query
					.select("veiculo.cdveiculo")
					.where("veiculo = ?",veiculo)
					.unique();
	}
	
	/**
	 * Busca os veículos para o combo da tela de carregamento
	 *
	 * Obs: Os parâmetros podem ser nulos pois pode ocorrer de que
	 * todos os veiculos sejam listados no combo
	 *
	 * @author Leonardo Guimarães
	 * 
	 * @param transportador
	 * @param onlyDisponiveis
	 * 
	 * @return
	 */
	public List<Veiculo> findForCarregamentoCombo(Transportador transportador) {
		QueryBuilder<Veiculo> query = makeDefaultQueryVeiculo()
			   .select("veiculo.cdveiculo,veiculo.placa")
			   .where("transportador = ?",transportador)
			   .where("disponivel = ?",Boolean.TRUE)
			   .orderBy("veiculo.placa");
		return query.list();
	}
	
	@Override
	protected void updateFindByQuery(QueryBuilder<Veiculo> query) {
		super.updateFindByQuery(query);
		if (Neo.isInApplicationContext())
			query.where("veiculo.deposito = " + WmsUtil.getDeposito().getCddeposito());
	}
	
	@Override
	public List<Veiculo> findForCombo(String orderby, String... extraFields) {
		QueryBuilder<Veiculo> query = makeDefaultQueryVeiculo();
		
		return query
				.select("veiculo.cdveiculo, veiculo.placa")
				.where("veiculo.deposito =?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
				.orderBy("veiculo.placa")
				.list();
	}

	/**
	 * Método que carrega o veiculo qnd selecionado na tela carregamentoFlex
	 * 
	 * @param veiculo
	 * @return
	 */
	public Veiculo carregar(Veiculo veiculo) {
		QueryBuilder<Veiculo> query = makeDefaultQueryVeiculo();
		
		return query
				.select("veiculo.cdveiculo, veiculo.capacidadepalete,veiculo.capacidadepeso," +
						"veiculo.altura,veiculo.largura,veiculo.profundidade")
				.entity(veiculo)
				.unique();
	}

	/**
	 * Método que carrega os veículos pelo tipo
	 * 
	 * @param tipoveiculo
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Veiculo> findVeiculosByTipoVeiculo(Tipoveiculo tipoveiculo) {
		QueryBuilder<Veiculo> query = makeDefaultQueryVeiculo();
		
		return query
			.select("veiculo.cdveiculo, veiculo.placa")
			.join("veiculo.tipoveiculo tipoveiculo")
			.where("tipoveiculo = ?", tipoveiculo)
			.orderBy("veiculo.placa")
			.list();
	}
	
	/**
	 * 
	 * @return
	 */
	private QueryBuilder<Veiculo> makeDefaultQueryVeiculo(){
		QueryBuilder<Veiculo> query = query();
		return query
			.join("veiculo.transportador transportador")
			.join("transportador.listaTransDeposito transportadordeposito")
			.join("transportadordeposito.deposito deposito")
			.where("deposito = ?",WmsUtil.getDeposito(), WmsUtil.isFiltraDeposito())
			.where("veiculo.disponivel = 1")
			.where("deposito = veiculo.deposito")
			.where("transportador.ativo = 1");
	}

	/**
	 * 
	 * @param transportador
	 * @param deposito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Veiculo> findForComboByTransportador(Transportador transportador, Deposito deposito) {
		if(transportador == null || transportador.getCdpessoa() == null){
			return null;
		}
		
		QueryBuilder query = query();
		query
			.select("veiculo.cdveiculo, veiculo.placa")
			.join("veiculo.transportador transportador")
			.join("transportador.listaTransDeposito transportadordeposito")
			.join("transportadordeposito.deposito deposito")
			.join("veiculo.deposito deposito2")
			.where("transportador = ?", transportador)
			.where("deposito = ?", deposito)
			.where("deposito2 = ?",deposito)
			.orderBy("veiculo.placa");
		
		return query.list();
	}

}