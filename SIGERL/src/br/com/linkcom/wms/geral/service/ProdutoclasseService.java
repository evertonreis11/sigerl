package br.com.linkcom.wms.geral.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Agendaverba;
import br.com.linkcom.wms.geral.bean.Agendaverbafinanceiro;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.bean.vo.ResumoAgendaverba;
import br.com.linkcom.wms.geral.dao.ProdutoclasseDAO;
import br.com.linkcom.wms.modulo.recebimento.controller.process.filtro.AgendaverbaFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ProdutoclasseService extends GenericService<Produtoclasse> {

	private ProdutoclasseDAO produtoclasseDAO;
	private AgendaverbafinanceiroService agendaverbafinanceiroService;
	private RecebimentoService recebimentoService;
	private AgendaService agendaService;
	private PedidocompraService pedidocompraService;
	
	public void setProdutoclasseDAO(ProdutoclasseDAO produtoclasseDAO) {
		this.produtoclasseDAO = produtoclasseDAO;
	}
	
	public void setAgendaverbafinanceiroService(AgendaverbafinanceiroService agendaverbafinanceiroService) {
		this.agendaverbafinanceiroService = agendaverbafinanceiroService;
	}
	
	public void setRecebimentoService(RecebimentoService recebimentoService) {
		this.recebimentoService = recebimentoService;
	}
	
	public void setAgendaService(AgendaService agendaService) {
		this.agendaService = agendaService;
	}
	
	public void setPedidocompraService(PedidocompraService pedidocompraService) {
		this.pedidocompraService = pedidocompraService;
	}
	
	/**
	 * Localiza as classes de produto para a tela de agenda de verbas.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public List<Produtoclasse> findForAgendaverba() {
		return produtoclasseDAO.findForAgendaverba();
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.ProdutoclasseDAO#findByNumero
	 *
	 * @param numero
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Produtoclasse findByNumero(String numero){
		return produtoclasseDAO.findByNumero(numero);
	}

	
	/* singleton */
	private static ProdutoclasseService instance;
	public static ProdutoclasseService getInstance() {
		if(instance == null){
			instance = Neo.getObject(ProdutoclasseService.class);
		}
		return instance;
	}
	
	public void prepararAgendaVerba(AgendaverbaFiltro filtro, Produtoclasse produtoclasse){
		Agendaverbafinanceiro agendaverbafinanceiro;		                 
		List<Agendaverbafinanceiro> lista = new ListSet<Agendaverbafinanceiro>(Agendaverbafinanceiro.class);
		List<ResumoAgendaverba> listaResumoRecebimento = recebimentoService.findResumoRecebimentoVerbaNovo(filtro);
		List<ResumoAgendaverba> listaResumoAgenda = agendaService.findResumoAgendaFinanceiro(filtro.getDeposito(), 
				WmsUtil.firstDateOfMonth(filtro.getExercicio()), 
				WmsUtil.lastDateOfMonth(WmsUtil.addMesData(filtro.getExercicio(), 5)), true, null);
			
			
			/*agendaService.findResumoAgendaFinanceiro2(filtro.getDeposito(), 
				WmsUtil.firstDateOfMonth(filtro.getExercicio()), 
				WmsUtil.lastDateOfMonth(WmsUtil.addMesData(filtro.getExercicio(), 5)), null);*/
		
		List<ResumoAgendaverba> listaResumoAgendaRecebimento = agendaService.findResumoAgenda(filtro.getDeposito(), 
				WmsUtil.firstDateOfMonth(filtro.getExercicio()), 
				WmsUtil.lastDateOfMonth(filtro.getExercicio()), false, null, null);
		if(produtoclasse.getAgendaverbaControle()!=null){
			List<Agendaverbafinanceiro> listaAgendaverbafinanceiro = agendaverbafinanceiroService.verbaFinanceiroByVerbaPeriodo(filtro.getDeposito(), produtoclasse, filtro.getExercicio());
			produtoclasse.getAgendaverbaControle().setValorAgendado(this.getValor(listaResumoAgendaRecebimento, produtoclasse, WmsUtil.addMesData(filtro.getExercicio(), 0)));
			produtoclasse.getAgendaverbaControle().setValorRecebido(this.getValor(listaResumoRecebimento, produtoclasse, WmsUtil.addMesData(filtro.getExercicio(), 0)));
			produtoclasse.getAgendaverbaControle().setValorDisponivel((produtoclasse.getAgendaverbaControle().getVerba() != null ? produtoclasse.getAgendaverbaControle().getVerba() : new Money(0.0)).subtract(
					produtoclasse.getAgendaverbaControle().getValorAgendado() != null ? produtoclasse.getAgendaverbaControle().getValorAgendado()  : new Money(0.0))
					.subtract(produtoclasse.getAgendaverbaControle().getValorRecebido() != null ? produtoclasse.getAgendaverbaControle().getValorRecebido(): new Money(0.0)));
			for(int i=0; i < 5; i++){
				List<ResumoAgendaverba> listaResumoRecebimentoFinanceiro = recebimentoService.findResumoRecebimentoFinanceiroNovo(filtro.getDeposito(), filtro.getExercicio(), WmsUtil.addMesData(filtro.getExercicio(), i));
				agendaverbafinanceiro = this.verificaDadosFinanceiros(listaAgendaverbafinanceiro, WmsUtil.addMesData(filtro.getExercicio(), i));
				agendaverbafinanceiro.setDtagendaverba(WmsUtil.addMesData(filtro.getExercicio(), i));
				
				agendaverbafinanceiro.setAgendado(this.getValor(listaResumoAgenda, produtoclasse, WmsUtil.addMesData(filtro.getExercicio(), i)));
				agendaverbafinanceiro.setRecebido(this.getValor(listaResumoRecebimentoFinanceiro, produtoclasse, WmsUtil.addMesData(filtro.getExercicio(), i)));
				agendaverbafinanceiro.setDisponivel((agendaverbafinanceiro.getVerba() != null ? agendaverbafinanceiro.getVerba() : new Money(0.0)).subtract(
														agendaverbafinanceiro.getAgendado() != null ? agendaverbafinanceiro.getAgendado() : new Money(0.0))
														.subtract(agendaverbafinanceiro.getRecebido() != null ? agendaverbafinanceiro.getRecebido() : new Money(0.0)));
				lista.add(agendaverbafinanceiro);
			}
			produtoclasse.getAgendaverbaControle().setListaAgendaverbafinanceiroControle(lista);
		}else{
			for(int i=0; i < 5; i++){
				Agendaverbafinanceiro avf = new Agendaverbafinanceiro();
				avf.setDtagendaverba(WmsUtil.addMesData(filtro.getExercicio(), i));
				lista.add(avf);
			}
			
			produtoclasse.setAgendaverbaControle(new Agendaverba());
			produtoclasse.getAgendaverbaControle().setListaAgendaverbafinanceiroControle(lista);
		}
	//	agendaverba.setListaAgendaverbafinanceiro(lista);
	//	agendaverba.setListaAgendaverbafinanceiroControle(lista);
	}
	
	public Agendaverbafinanceiro verificaDadosFinanceiros(List<Agendaverbafinanceiro> lista, Date data){
		if(lista != null && lista.size() > 0){
			Map<Date, Money> map = new HashMap<Date, Money>();
			Money verba;
			for (Agendaverbafinanceiro avf : lista) {
				//System.out.println(avf.getAgendaverba().getProdutoclasse().getNumero()+" "+avf.getDtagendaverba()+" "+ avf.getVerba());
				if(WmsUtil.firstDateOfMonth(avf.getDtagendaverba()).equals(WmsUtil.firstDateOfMonth(data))){
					verba = new Money(0);
					if(map.containsKey(WmsUtil.firstDateOfMonth(avf.getDtagendaverba()))){
						verba = map.get(WmsUtil.firstDateOfMonth(avf.getDtagendaverba()));
						map.put(WmsUtil.firstDateOfMonth(avf.getDtagendaverba()), verba.add(avf.getVerba()==null ? new Money(0) : avf.getVerba()));
					}else{
						map.put(WmsUtil.firstDateOfMonth(avf.getDtagendaverba()), avf.getVerba()==null ? new Money(0) : avf.getVerba());
					}
					
					/*if(avf.getVerba()==null) avf.setVerba(new Money(0));
					return avf;*/
				}
			}
			Agendaverbafinanceiro agendaverbafinanceiro;
			Set<Entry<Date, Money>> mp = map.entrySet();
			for(Entry<Date, Money> entry : mp) {
				if(WmsUtil.firstDateOfMonth(entry.getKey()).equals( WmsUtil.firstDateOfMonth(data))){
					agendaverbafinanceiro = new Agendaverbafinanceiro();
					agendaverbafinanceiro.setDtagendaverba(entry.getKey());
					agendaverbafinanceiro.setVerba(entry.getValue());
					return agendaverbafinanceiro;
				}
			   
			}
			
		}
		return new Agendaverbafinanceiro();
		
	}
	
/*	public Agendaverbafinanceiro verificaDadosFinanceiros(List<Agendaverbafinanceiro> lista, Date data){
		if(lista != null && lista.size() > 0){
			for (Agendaverbafinanceiro avf : lista) {
				if(WmsUtil.firstDateOfMonth(avf.getDtagendaverba()).equals( WmsUtil.firstDateOfMonth(data))){
					if(avf.getVerba()==null) avf.setVerba(new Money(0));
					return avf;
				}
			}
		}
		return new Agendaverbafinanceiro();
	}
*/	
	
	public Money getValor(List<ResumoAgendaverba> lista, Produtoclasse produtoclasse, Date data){
		for (ResumoAgendaverba resumo : lista) {
			if(resumo.getClasseproduto().equals(produtoclasse.getNumero()) && 
					WmsUtil.firstDateOfMonth(resumo.getMes()).equals(WmsUtil.firstDateOfMonth(data))){
				return resumo.getValor();
			}
		}
		return new Money(0);
	}	
	
	public Produtoclasse loadWithV_produtoclasse(Produtoclasse produtoclasse) {
		return produtoclasseDAO.loadWithV_produtoclasse(produtoclasse);
	}

	/**
	 * 
	 * @return
	 */
	public List<Produtoclasse> findByDepartamentoGerenciador() {
		return produtoclasseDAO.findByDepartamentoGerenciador();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Produtoclasse> findAllByControleVerba() {
		return produtoclasseDAO.findAllByControleVerba();
	}
	
}
