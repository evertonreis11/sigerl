package br.com.linkcom.wms.geral.service;

import java.util.Date;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agendaverba;
import br.com.linkcom.wms.geral.bean.Agendaverbafinanceiro;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.dao.AgendaverbaDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.AgendaverbaFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendaverbaService extends GenericService<Agendaverba> {

	private static AgendaverbaService instance;
	private AgendaverbaDAO agendaverbaDAO;

	public static AgendaverbaService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(AgendaverbaService.class);
		}
		return instance;
	}
	
	public void setAgendaverbaDAO(AgendaverbaDAO agendaverbaDAO) {
		this.agendaverbaDAO = agendaverbaDAO;
	}
	
	/**
	 * Busca os exercícios cadastrados, retornando uma lista de datas para os meses de Janeiro e Julho, indicando o 1º ou 2° semestre.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public List<Date> findExercicios(Deposito deposito){
		return agendaverbaDAO.findExercicios(deposito);
	}

	/**
	 * Localiza os agendamentos de verbas para a tela de edição.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<Agendaverba> findForEdicao(AgendaverbaFiltro filtro) {
		return agendaverbaDAO.findForEdicao(filtro);
	}
	
	public List<Agendaverba> findAgendaverba(AgendaverbaFiltro filtro) {
		return agendaverbaDAO.findAgendaverba(filtro);
	}
	
	/**
	 * Localiza os agendamentos de verbas para a tela de edição.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public List<Agendaverba> findResumoGeral(Date exercicio) {
		return agendaverbaDAO.findResumoGeral(exercicio);
	}

	/**
	 * Busca a soma das verbas de recebimento de acordo comos parâmetros.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.AgendaverbaDAO#findListaVerba
	 *
	 * @param deposito
	 * @param produtoclasse
	 * @param dtagenda
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Money findVerbaRecebimento(Deposito deposito, Produtoclasse produtoclasse, Date dtagenda) {
		List<Agendaverba> listaAgendaVerba = agendaverbaDAO.findListaVerba(deposito, produtoclasse, dtagenda);
		Money total = new Money();
		for (Agendaverba agendaverba : listaAgendaVerba) {
			if(agendaverba.getVerba() != null){
				total = total.add(agendaverba.getVerba());
			}
		}
		return total;
	}
	
	@Override
	public void saveOrUpdate(Agendaverba bean) {
		if (bean.getVerba() == null)
			throw new WmsException("O valor da verba é obrigatório.");
		
		Money verbaTotal = new Money(0L);
		//Pego o valor antigo da verba da agenda e substituo pelo novo valor temporario, para que sejam realizados os testes corretos...
		if(bean.getCdagendaverba() !=null){
			Agendaverba agendaverba = loadForEntrada(bean);
			Agendaverba av = getVerbaLiberadaTotalDoMes(agendaverba);
			verbaTotal = av.getVerba();
			verbaTotal = verbaTotal.subtract(agendaverba.getVerba());
			verbaTotal = verbaTotal.add(bean.getVerba()); 	//verbatotal é a nova verba com o valor temporario...
		}
		
		Money total = new Money(0);
		if (bean.getListaAgendaverbafinanceiro() != null){
			for (Agendaverbafinanceiro avf : bean.getListaAgendaverbafinanceiro()){
				if (avf.getVerba() == null)
					throw new WmsException("O valor do vencimento financeiro é obrigatório.");
				total = total.add(avf.getVerba());
			}
		}
		
		if(bean.getValorLiberado().toLong() > bean.getVerba().toLong()){
			throw new WmsException("O valor da verba informada não pode ser menor que o valor liberado.");
		}
		
		if(bean.getValorLiberadoTotal().toLong() > verbaTotal.toLong()){
			throw new WmsException("O valor da verba total não pode ser menor que o valor liberado total.");
		}
		
		if (total.toLong() != bean.getVerba().toLong() )
			throw new WmsException("O valor da verba informada não corresponde ao somatório do vencimento financeiro.");
		
		super.saveOrUpdate(bean);
	}

	/**
	 * Verifica se já existe um controle de verba criado para o exercício/depósito escolhidos.
	 * 
	 * @author Giovane Freitas
	 * @param filtro
	 * @return
	 */
	public boolean existeExercicio(AgendaverbaFiltro filtro) {
		return agendaverbaDAO.existeExercicio(filtro);
	}
	
	public Boolean editarVerbaHasAuthorization(){
		return WmsUtil.isUserHasAction("EDITAR_VERBA");
	}
	
	/*public List<ResumoVerbaFinanceiro> findVerbaFinanceiro(Date exercicio){
		return agendaverbaDAO.findVerbaFinanceiro(exercicio);
	}*/
	
	public long getVerbaFinanceiro(Date exercicio, Produtoclasse produtoclasse, Deposito deposito) {
		return agendaverbaDAO.getVerbaFinanceiro(exercicio, produtoclasse, deposito);
	}
	
	/**
	 * 
	 * @param agendaverba
	 * @return
	 */
	public Agendaverba getVerbaLiberadaTotalDoMes(Agendaverba agendaverba){
		Date exercicio = new Date();
		int mes = WmsUtil.getMes(agendaverba.getDtagendaverba());
		int ano = WmsUtil.getAno(agendaverba.getDtagendaverba());
		String numero = agendaverba.getProdutoclasse().getNumero();
		if(mes<7){
			String data = "01/01/"+ano;
			exercicio = WmsUtil.getData(data);
		}else{
			String data = "01/07/"+ano;
			exercicio = WmsUtil.getData(data);				
		}
		List<Agendaverba> listaAgenda = findResumoGeral(exercicio);
		for (Agendaverba av : listaAgenda) {
			int mesAux = WmsUtil.getMes(av.getDtagendaverba());
			String numeroAux = av.getProdutoclasse().getNumero();
			if(mesAux==mes && numero.equals(numeroAux))
				return av;
		}
		return null;
	}
}
