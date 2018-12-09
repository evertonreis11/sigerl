package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.MotoristaFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class MotoristaDAO extends GenericDAO<Motorista>{

	@Override
	public void updateListagemQuery(QueryBuilder<Motorista> query,FiltroListagem _filtro) {
		
		MotoristaFiltro filtro = (MotoristaFiltro)_filtro;
		
		query
			.select("motorista.cdmotorista, motorista.nome, motorista.cpf, motorista.apelido, " +
					"transportador.nome, veiculo.modelo, equipamento.nome, deposito.cddeposito, deposito.nome")
			.leftOuterJoin("motorista.transportador transportador")
			.leftOuterJoin("motorista.veiculo veiculo")
			.leftOuterJoin("motorista.equipamento equipamento")
			.leftOuterJoin("motorista.deposito deposito")
			.where("deposito = ?",WmsUtil.getDeposito())
			.whereLikeIgnoreAll("motorista.nome", filtro.getNome())
			.where("motorista.cpf = ?",filtro.getCpf())
			.where("motorista.equipamento = ?",filtro.getEquipamento())
			.where("motorista.transportador = ?",filtro.getTransportador());
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Motorista> query) {
		
		query
			.select("motorista.cdmotorista, motorista.nome, motorista.cpf, motorista.apelido, transportador.cdpessoa," +
					"transportador.nome, equipamento.cdequipamento, equipamento.nome, deposito.cddeposito, deposito.nome," +
					"veiculo.cdveiculo, veiculo.placa")
			.leftOuterJoin("motorista.transportador transportador")
			.leftOuterJoin("motorista.veiculo veiculo")
			.leftOuterJoin("motorista.equipamento equipamento")
			.leftOuterJoin("motorista.deposito deposito");
		
	}

	/**
	 * 
	 * @param transportador
	 * @param deposito 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Motorista> findForComboByTransportador(Transportador transportador, Deposito deposito){
		
		if(transportador==null || transportador.getCdpessoa()==null || deposito==null)
			return null;
		
		QueryBuilder query = query(); 
		query
			.select("motorista.cdmotorista,motorista.nome")
			.join("motorista.transportador transportador")
			.join("motorista.deposito deposito")
			.where("transportador = ?",transportador)
			.where("deposito = ?",deposito)
			.where("transportador.ativo = 1")
			.orderBy("motorista.nome");
		
		return query.list();
	}

	/**
	 * 
	 * @param motorista
	 * @return
	 */
	public List<Motorista> findForCarregamentoComboByMotorista(Motorista motorista) {
		
		QueryBuilder<Motorista> query = query();
		
		query
			.select("motorista.cdmotorista,motorista.nome, veiculo.cdveiculo, veiculo.placa")
			.join("motorista.veiculo veiculo")
			.where("cdmotorista = ?",motorista.getCdmotorista());
		
		return query.list();
	}
	
}
