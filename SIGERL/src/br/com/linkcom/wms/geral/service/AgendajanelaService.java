package br.com.linkcom.wms.geral.service;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Agenda;
import br.com.linkcom.wms.geral.bean.Agendajanela;
import br.com.linkcom.wms.geral.bean.Agendajanelaclasse;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.dao.AgendajanelaDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.crud.filtro.AgendajanelaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class AgendajanelaService  extends GenericService<Agendajanela>{

	private static AgendajanelaService instance;
	private AgendajanelaDAO agendajanelaDAO;
	private AgendajanelaclasseService agendajanelaclasseService;
	
	public void setAgendajanelaclasseService(AgendajanelaclasseService agendajanelaclasseService) {
		this.agendajanelaclasseService = agendajanelaclasseService;
	}

	public static AgendajanelaService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(AgendajanelaService.class);
		}
		return instance;
	}
	
	public void setAgendajanelaDAO(AgendajanelaDAO agendajanelaDAO) {
		this.agendajanelaDAO = agendajanelaDAO;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void saveOrUpdate(Agendajanela bean) {
		if (bean.getListaAgendajanelaclasse() == null || bean.getListaAgendajanelaclasse().isEmpty())
			throw new WmsException("É necessário incluir pelo menos uma classe de produto.");
		
		Collections.sort(bean.getListaAgendajanelaclasse(), new BeanComparator("produtoclasse.cdprodutoclasse"));
		Iterator<Agendajanelaclasse> iterator = bean.getListaAgendajanelaclasse().iterator();
		Agendajanelaclasse itemAnterior = null;
		while (iterator.hasNext()){
			Agendajanelaclasse itemAtual = iterator.next();
			if (itemAnterior == null){
			} else {
				if (itemAnterior.getProdutoclasse().getCdprodutoclasse().equals(itemAtual.getProdutoclasse().getCdprodutoclasse()))
					iterator.remove();
			}
				
			itemAnterior = itemAtual;
		}
		
		super.saveOrUpdate(bean);
	}

	/**
	 * Verifica se existe uma janela de agendamento que atenda a todas as classes listadas.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param listaProdutoclasse
	 * @return
	 */
	public boolean existeJanela(Deposito deposito, List<String> listaProdutoclasse) {
		return agendajanelaDAO.existeJanela(deposito, listaProdutoclasse);
	}

	/**
	 * Lista o número de janelas ocupadas para cada classe de produto principal
	 * em um determinado dia.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @param data
	 * @param agenda 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Agendajanela> getAgendasPorClasse(Deposito deposito, Date data, Agenda agenda) {
		return agendajanelaDAO.getAgendasPorClasse(deposito, data, agenda);
	}

	/**
	 * Busca as janelas com a informação de quantas estão disponíveis, para fazer validação.
	 * 
	 * @param filtro
	 * @param agenda 
	 * @return
	 */
	public List<Agendajanela> findForValidacao(AgendajanelaFiltro filtro, Agenda agenda) {
		filtro.setPageSize(Integer.MAX_VALUE);
		
	//	ListagemResult<Agendajanela> listagemResult = this.findForListagem(filtro); 

	//	List<Agendajanela> listaJanelas = listagemResult.list();
		
		List<Agendajanela> listaJanelas = AgendajanelaService.getInstance().getAgendasPorClasse(filtro.getDeposito(), filtro.getDataAgenda(), agenda);
		
		if(listaJanelas != null && listaJanelas.size() > 0){
			for (Agendajanela item : listaJanelas) {
				item.setListaAgendajanelaclasse(agendajanelaclasseService.findByAgendajanela(item));
			}
		}
		
	/*	Map<String, List<Agendajanela>> mapJanelaPorClasse = new HashMap<String, List<Agendajanela>>();
		
		for (Agendajanela aj : listaJanelas){
			aj.setDisponivel(aj.getOcorrencias());
			
			for (Agendajanelaclasse ajc : aj.getListaAgendajanelaclasse()){
				List<Agendajanela> janelas = null;
				if (mapJanelaPorClasse.containsKey(ajc.getProdutoclasse().getNumero())){
					janelas = mapJanelaPorClasse.get(ajc.getProdutoclasse().getNumero());
				} else {
					janelas = new ArrayList<Agendajanela>();
					mapJanelaPorClasse.put(ajc.getProdutoclasse().getNumero(), janelas);
				}
				
				janelas.add(aj);
			}
		}
		
		for (Entry<String, Integer> ocupacao : janelasOcupadas.entrySet()){
			if (mapJanelaPorClasse.containsKey(ocupacao.getKey())) {
				List<Agendajanela> janelas = mapJanelaPorClasse.get(ocupacao.getKey());
				Collections.sort(janelas, new Comparator<Agendajanela>(){
					public int compare(Agendajanela o1, Agendajanela o2) {
						int sizeO1 = o1.getListaAgendajanelaclasse().size();
						int sizeO2 = o2.getListaAgendajanelaclasse().size();
						return (sizeO1< sizeO2 ? -1 : (sizeO1 == sizeO2 ? 0 : 1));
					}});
				
				int numeroAgendas = ocupacao.getValue();
				for (Agendajanela aj : janelas){
					if (numeroAgendas <= 0 )
						break;
					
					if (aj.getDisponivel() > 0){
						if (aj.getDisponivel() > numeroAgendas){
							aj.setDisponivel(aj.getDisponivel() - numeroAgendas);
							numeroAgendas = 0;
						} else {
							numeroAgendas = numeroAgendas - aj.getDisponivel();
							aj.setDisponivel(0);
						}
					}
				}
			}
		}*/
		
		return listaJanelas;
	}
	
}
