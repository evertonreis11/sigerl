package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Lancarcorte;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.OrdemservicoUsuario;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.LancarcorteFiltro;
import br.com.linkcom.wms.util.expedicao.ItemCorteVO;

public class LancarcorteService extends GenericService<Lancarcorte> {
	private OrdemservicoService ordemservicoService;
	private OrdemservicoprodutoService ordemservicoprodutoService;
	private CarregamentoService carregamentoService;
	private OrdemservicousuarioService ordemservicousuarioService;
	private CarregamentoitemService carregamentoitemService;
	
	public void setOrdemservicoService(OrdemservicoService ordemservicoService) {
		this.ordemservicoService = ordemservicoService;
	}
	
	public void setOrdemservicoprodutoService(OrdemservicoprodutoService ordemservicoprodutoService) {
		this.ordemservicoprodutoService = ordemservicoprodutoService;
	}
	
	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}
	
	public void setOrdemservicousuarioService(OrdemservicousuarioService ordemservicousuarioService) {
		this.ordemservicousuarioService = ordemservicousuarioService;
	}

	public void setCarregamentoitemService(CarregamentoitemService carregamentoitemService) {
		this.carregamentoitemService = carregamentoitemService;
	}
	
	
	/**
	 * 
	 * Método que verifica se a quantidade coletada é maior que a quantidade esperada
	 * 
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#findByListaOrdemservico(String)
	 * @see br.com.linkcom.wms.geral.service.CarregamentoService#construirMap(String, String)
	 * 
	 * @author Arantes
	 * 
	 * @param request
	 * @param ordemservico
	 * @return Boolean
	 * 
	 */
	public Boolean validarQtdeColetada(WebRequestContext request, Ordemservico ordemservico) {
		String listaQtdeColetada = (String) request.getParameter("listaQtdeColetada");
		String listaOSProduto = (String) request.getParameter("listaOs");

		if((listaOSProduto != null) && (listaQtdeColetada != null)) {
			Map<String, Long> map = carregamentoService.construirMap(listaQtdeColetada, listaOSProduto);
			
			List<ItemCorteVO> listaItemCorteVO = ordemservicoService.getListaItemCorteVO(ordemservico);
			
			for (ItemCorteVO item : listaItemCorteVO) {
				String key = item.getProduto().getCdproduto() + "-" + item.getCdpedidovenda();
				if(map.containsKey(key)) {
					Integer qtde = item.getQtdeEsperada();
					Long qtdeConfirmada = map.get(key);
					
					if(qtdeConfirmada.longValue() > qtde.longValue()) {
						return Boolean.FALSE;
						
					}
				}
			}
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * Atualiza a quantidade confirmada da etiqueta expedição e o status da ordem de serviço bem como o status da ordem de serviço produto
	 * 
	 * @see br.com.linkcom.wms.geral.service.CarregamentoService#atualizarQtdeconfirmadaetiqueta(Map, List)
	 * @see br.com.linkcom.wms.geral.service.CarregamentoService#atualizarStatusordemservicoproduto(List)
	 * @see br.com.linkcom.wms.geral.service.OrdemservicoService#definirStatusordemservico(List)
	 * @see br.com.linkcom.wms.geral.service.CarregamentoService#construirMap(String, String)
	 * @see br.com.linkcom.wms.geral.service.CarregamentoService#validarListaetiquetaexpedicao(List, Map)
	 * 
	 * @author Arantes
	 * 
	 * @param request
	 * @param ordemservico
	 * 
	 */
	public void atualizarQtdeConfirmada(WebRequestContext request, Ordemservico ordemservico) {
		String listaQtdeColetada = (String) request.getParameter("listaQtdeColetada");
		String listaItensPedido = (String) request.getParameter("itensPedido");
		
		if((listaItensPedido != null) && (listaQtdeColetada != null)) {
			List<Carregamentoitem> listaCarregamentoitem = carregamentoitemService.findForConfirmacaoCorte(ordemservico);
						
			Map<String, Long> map = carregamentoService.construirMap(listaQtdeColetada, listaItensPedido);
			
			// atualiza a quantidade confirmada da etiqueta
			carregamentoitemService.atualizarQtdeconfirmadaCorte(listaCarregamentoitem, map);
			
			// atualiza o status da ordem de serviço produto
			ordemservicoprodutoService.atualizarStatusordemservicoproduto(ordemservico);

			// atualiza o status da ordem de serviço
			List<Ordemservico> listaOrdemservico = ordemservicoService.findByListaOrdemservico(ordemservico.getCdordemservico().toString());			
			ordemservicoService.definirStatusordemservico(listaOrdemservico);
		}
	}
	
	/**
	 * 
	 * Verifica se o lançamento de corte possui uma data/hora.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return Timestamp 
	 * 
	 */
	public static Timestamp carregarDataHora(Date data, Hora hora) {	
		Timestamp timestamp = null;
		
		if(data != null) {
			Calendar calendarfinal = Calendar.getInstance();
			
			Calendar calendardata = Calendar.getInstance();
			calendardata.setTime(data);
			calendarfinal.set(Calendar.YEAR, calendardata.get(Calendar.YEAR));
			calendarfinal.set(Calendar.MONTH, calendardata.get(Calendar.MONTH));
			calendarfinal.set(Calendar.DAY_OF_MONTH, calendardata.get(Calendar.DAY_OF_MONTH));
			calendarfinal.set(Calendar.HOUR_OF_DAY, 0);
			calendarfinal.set(Calendar.MINUTE, 0);
			calendarfinal.set(Calendar.SECOND, 0);
			calendarfinal.set(Calendar.MILLISECOND, 0);
			
			if(hora != null) {
				Calendar calendarhora = Calendar.getInstance();
				calendarhora.setTimeInMillis(hora.getTime());
				calendarfinal.set(Calendar.HOUR_OF_DAY, calendarhora.get(Calendar.HOUR_OF_DAY));
				calendarfinal.set(Calendar.MINUTE, calendarhora.get(Calendar.MINUTE));
				calendarfinal.set(Calendar.SECOND, 0);
				calendarfinal.set(Calendar.MILLISECOND, 0);
			}
			
			timestamp = new Timestamp(calendarfinal.getTimeInMillis());			
		}		
		return timestamp;		
	}
		
	/**
	 * 
	 * Carrega o responsável pelo corte.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @param ordemservico
	 * @return OrdemservicoUsuario
	 * 
	 */
	public OrdemservicoUsuario carregarResponsavel(LancarcorteFiltro filtro, Ordemservico ordemservico) {		
		OrdemservicoUsuario osUsuarioAux = ordemservicousuarioService.loadByOS(ordemservico);
		
		OrdemservicoUsuario ordemservicoUsuario = (osUsuarioAux == null) ? new OrdemservicoUsuario() : osUsuarioAux; 
		
		ordemservicoUsuario.setOrdemservico(ordemservico);
		ordemservicoUsuario.setUsuario(filtro.getUsuario());
		
		Timestamp dataIni = this.carregarDataHora(filtro.getData(), filtro.getHrinicio());
		Timestamp dataFim = this.carregarDataHora(filtro.getData(), filtro.getHrfim());
		
		ordemservicoUsuario.setDtinicio(dataIni != null ? dataIni : null);
		ordemservicoUsuario.setDtfim(dataFim != null ? dataFim : null);
		
		return ordemservicoUsuario;
	}
}
