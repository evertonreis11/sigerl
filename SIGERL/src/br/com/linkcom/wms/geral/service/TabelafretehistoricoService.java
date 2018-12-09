package br.com.linkcom.wms.geral.service;

import java.sql.Timestamp;
import java.util.List;

import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Tabelafrete;
import br.com.linkcom.wms.geral.bean.Tabelafretehistorico;
import br.com.linkcom.wms.geral.bean.Tabelafretepraca;
import br.com.linkcom.wms.geral.bean.Tabelafreterota;
import br.com.linkcom.wms.geral.bean.Tipoentrega;
import br.com.linkcom.wms.geral.bean.Tipotabelafrete;
import br.com.linkcom.wms.geral.bean.Tipoveiculo;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class TabelafretehistoricoService extends GenericService<Tabelafretehistorico>{

	private TransportadorService transportadorService;
	private TipoentregaService tipoentregaService;
	private DepositoService depositoService;
	private TipotabelafreteService tipotabelafreteService;
	private TipoveiculoService tipoveiculoService;
	private PracaService pracaService;

	public void setTransportadorService(TransportadorService transportadorService) {
		this.transportadorService = transportadorService;
	}
	public void setTipoentregaService(TipoentregaService tipoentregaService) {
		this.tipoentregaService = tipoentregaService;
	}
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	public void setTipotabelafreteService(TipotabelafreteService tipotabelafreteService) {
		this.tipotabelafreteService = tipotabelafreteService;
	}
	public void setTipoveiculoService(TipoveiculoService tipoveiculoService) {
		this.tipoveiculoService = tipoveiculoService;
	}
	public void setPracaService(PracaService pracaService) {
		this.pracaService = pracaService;
	}
	
	
	/**
	 * 
	 * @param tabelafrete
	 */
	public void criarHistorico(Tabelafrete tabelafrete,Tabelafrete tabelafreteAntiga){
		
		String motivoDescricaoHistorico = getMotivoHistorico(tabelafrete,tabelafreteAntiga);
		
		if(motivoDescricaoHistorico!=null && motivoDescricaoHistorico.length() >= 4000){
			motivoDescricaoHistorico = motivoDescricaoHistorico.substring(0, 3999);
		}
		
		Tabelafretehistorico tabelafretehistorico = new Tabelafretehistorico();
			tabelafretehistorico.setDtalteracao(new Timestamp(System.currentTimeMillis()));
			tabelafretehistorico.setMotivo(motivoDescricaoHistorico);
			tabelafretehistorico.setTabelafrete(tabelafrete);
			tabelafretehistorico.setUsuario(WmsUtil.getUsuarioLogado());
		this.saveOrUpdate(tabelafretehistorico);
	}

	/**
	 * Método que executa comparações diversas afim de rastrear as mudanças realizada na tabela de frete.
	 * 
	 * @param tabelafrete
	 * @param tabelafreteAntiga
	 * @return
	 */
	private String getMotivoHistorico(Tabelafrete tabelafrete, Tabelafrete tabelafreteAntiga) {
		
		StringBuilder motivo = new StringBuilder();
		
		if(tabelafreteAntiga!=null && tabelafreteAntiga.getListaTabelafretehistorico()!=null && !tabelafreteAntiga.getListaTabelafretehistorico().isEmpty()){
			
			motivo.append("Tabela de Frete editada.<br>");
			
			if(tabelafreteAntiga.getValidadeinicial()!=null || tabelafrete.getValidadeinicial()!=null){
				
				String validadeInicialAntiga = "N/S";
				String validadeInicial = "N/S";
				
				if(tabelafreteAntiga.getValidadeinicial()!=null)
					validadeInicialAntiga = tabelafreteAntiga.getValidadeinicial().toString(); 
				if(tabelafrete.getValidadeinicial()!=null)
					validadeInicial = tabelafrete.getValidadeinicial().toString();
				if(!validadeInicialAntiga.equals(validadeInicial))
					motivo.append("\nA validade inicial foi alterada de "+validadeInicialAntiga+" para "+validadeInicial+".<br>");
			}

			if(tabelafreteAntiga.getValidadefinal()!=null || tabelafrete.getValidadefinal()!=null){
				
				String validadeFinalAntiga = "N/S";
				String validadeFinal = "N/S";
				
				if(tabelafreteAntiga.getValidadefinal()!=null)
					validadeFinalAntiga = tabelafreteAntiga.getValidadeinicial().toString(); 
				if(tabelafrete.getValidadefinal()!=null)
					validadeFinal = tabelafrete.getValidadeinicial().toString();
				if(!validadeFinalAntiga.equals(validadeFinal))
					motivo.append("\nA validade final foi alterada de "+validadeFinalAntiga+" para "+validadeFinal+".<br>");
			}

			if(tabelafrete.getTransportador()!=null || tabelafreteAntiga.getTransportador()!=null){
				
				String transportadorAntigo = "N/S";
				String transportador = "N/S";
				
				if(tabelafrete.getTransportador()!=null && tabelafrete.getTransportador().getCdpessoa()!=null){
					Transportador t = transportadorService.load(tabelafrete.getTransportador());
					transportador = t.getNome();
				}
				if(tabelafreteAntiga.getTransportador()!=null && tabelafreteAntiga.getTransportador().getNome()!=null){
					transportadorAntigo = tabelafreteAntiga.getTransportador().getNome();
				}
				if(!transportadorAntigo.equals(transportador)){
					motivo.append("\nO transportador foi alterado de "+transportadorAntigo+" para "+transportador+".<br>");
				}
			}
			
			if(tabelafrete.getTipoentrega()!=null || tabelafreteAntiga.getTipoentrega()!=null){
				
				String tipoentregaAntigo = "N/S";
				String tipoentrega = "N/S";
				
				if(tabelafrete.getTipoentrega()!=null && tabelafrete.getTipoentrega().getCdtipoentrega()!=null){
					Tipoentrega te = tipoentregaService.load(tabelafrete.getTipoentrega());
					tipoentrega = te.getNome();
				}
				if(tabelafreteAntiga.getTipoentrega()!=null && tabelafreteAntiga.getTipoentrega().getNome()!=null){
					tipoentregaAntigo = tabelafreteAntiga.getTipoentrega().getNome();
				}
				if(!tipoentregaAntigo.equals(tipoentrega)){
					motivo.append("\nO Tipo de Entrega foi alterado de "+tipoentregaAntigo+" para "+tipoentrega+".<br>");
				}				
			}
			
			if(tabelafrete.getDeposito()!=null || tabelafreteAntiga.getDeposito()!=null){
				
				String depositoAntigo = "N/S";
				String deposito = "N/S";
				
				if(tabelafrete.getDeposito()!=null && tabelafrete.getDeposito().getCddeposito()!=null){
					Deposito d = depositoService.load(tabelafrete.getDeposito());
					deposito = d.getNome();
				}
				if(tabelafreteAntiga.getDeposito()!=null && tabelafreteAntiga.getDeposito().getNome()!=null){
					depositoAntigo = tabelafreteAntiga.getDeposito().getNome();
				}
				if(!depositoAntigo.equals(deposito)){
					motivo.append("\nO Depósito foi alterado de "+depositoAntigo+" para "+deposito+".<br>");
				}				
			}
			
			if(tabelafrete.getTipotabelafrete()!=null || tabelafreteAntiga.getTipotabelafrete()!=null){
				
				String tipotabelafreteAntigo = "N/S";
				String tipotabelafrete = "N/S";
				
				if(tabelafrete.getTipotabelafrete()!=null && tabelafrete.getTipotabelafrete().getCdtipotabelafrete()!=null){
					Tipotabelafrete ttf = tipotabelafreteService.load(tabelafrete.getTipotabelafrete());
					tipotabelafrete = ttf.getNome();
				}
				if(tabelafreteAntiga.getTipotabelafrete()!=null && tabelafreteAntiga.getTipotabelafrete().getNome()!=null){
					tipotabelafreteAntigo = tabelafreteAntiga.getTipotabelafrete().getNome();
				}
				if(!tipotabelafreteAntigo.equals(tipotabelafrete)){
					motivo.append("\nO Tipo de Tabela Frete foi alterado de "+tipotabelafreteAntigo+" para "+tipotabelafrete+".<br>");
				}				
			}
			
			if(tabelafrete.getTipoveiculo()!=null || tabelafreteAntiga.getTipoveiculo()!=null){
				
				String tipoveiculoAntigo = "N/S";
				String tipoveiculo = "N/S";
				
				if(tabelafrete.getTipoveiculo()!=null && tabelafrete.getTipoveiculo().getCdtipoveiculo()!=null){
					Tipoveiculo tv = tipoveiculoService.load(tabelafrete.getTipoveiculo());
					tipoveiculo = tv.getNome();
				}
				if(tabelafreteAntiga.getTipoveiculo()!=null && tabelafreteAntiga.getTipoveiculo().getNome()!=null){
					tipoveiculoAntigo = tabelafreteAntiga.getTipoveiculo().getNome();
				}
				if(!tipoveiculoAntigo.equals(tipoveiculo)){
					motivo.append("\nO Tipo de Veiculo foi alterado de "+tipoveiculoAntigo+" para "+tipoveiculo+".<br>");
				}				
			}
			
			if(tabelafrete.getListaTabelafreterota()!=null || tabelafreteAntiga.getListaTabelafreterota()!=null){
				
				String valorNovo = "0,00";
				
				for (Tabelafreterota tabelafreterotaAntiga : tabelafreteAntiga.getListaTabelafreterota()) {
					Boolean isExcluido = Boolean.TRUE;
					Boolean isValorEditado = Boolean.FALSE;
					for (Tabelafreterota tabelafreterota : tabelafrete.getListaTabelafreterota()) {
						try {
							if(tabelafreterota.getRota().getCdrota().equals(tabelafreterotaAntiga.getRota().getCdrota())){
								isExcluido = false;
								if(!tabelafreterota.getValorentrega().getValue().equals((tabelafreterotaAntiga.getValorentrega().getValue()))){
									isValorEditado = Boolean.TRUE;
									valorNovo = tabelafreterota.getValorentrega().toString();
								}
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
							throw new WmsException("Ocorreu um erro durante a análise de alteração nas rotas.");
						}
					}
					if(isExcluido){
						motivo.append("\nA rota "+tabelafreterotaAntiga.getRota().getNome()+" foi excluída.<br>");
					}
					if(isValorEditado){
						motivo.append("\nO valor da rota "+tabelafreterotaAntiga.getRota().getNome()+" foi alterado de "+tabelafreterotaAntiga.getValorentrega()+" para "+valorNovo+".<br>");
					}
				}
				
				if(tabelafrete.getListaTabelafreterota()!=null){
					for (Tabelafreterota tabelafreterota : tabelafrete.getListaTabelafreterota()) {
						Boolean isNovo = Boolean.TRUE;
						for (Tabelafreterota tabelafreterotaAntiga : tabelafreteAntiga.getListaTabelafreterota()) {
							try {
								if(tabelafreterota.getRota().getCdrota().equals(tabelafreterotaAntiga.getRota().getCdrota())){
									isNovo = false;
									break;
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new WmsException("Ocorreu um erro durante a análise de alteração nas rotas.");
							}
						}
						if(isNovo){
							motivo.append("\nA rota "+tabelafreterota.getRota().getNome()+" foi inserida.<br>");
						}
					}
				}
			}
		
		}else{
			motivo.append("Tabela de Frete criada.");
		}
		return motivo.toString();
	}
	
	/**
	 * 
	 * @param tabelafreterota
	 * @param tabelafreterotaAux
	 */
	public void criarHistoricoPraca(Tabelafreterota tabelafreterota, Tabelafreterota tabelafreterotaAntiga){
		
		String motivoDescricaoHistorico = getMotivoHistoricoPraca(tabelafreterota, tabelafreterotaAntiga);
		
		Tabelafretehistorico tabelafretehistorico = new Tabelafretehistorico();
			tabelafretehistorico.setDtalteracao(new Timestamp(System.currentTimeMillis()));
			tabelafretehistorico.setMotivo(motivoDescricaoHistorico);
			tabelafretehistorico.setTabelafrete(tabelafreterotaAntiga.getTabelafrete());
			tabelafretehistorico.setUsuario(WmsUtil.getUsuarioLogado());
		this.saveOrUpdate(tabelafretehistorico);
		
		
		
	}
	/**
	 * @param tabelafreterota
	 * @param tabelafreterotaAntiga
	 */
	private String getMotivoHistoricoPraca(Tabelafreterota tabelafreterota, Tabelafreterota tabelafreterotaAntiga) {
		
		StringBuilder motivo = new StringBuilder();
		
		motivo.append("Tabela de Frete editada.<br>");
		
		if(tabelafreterota.getListaTabelafretepraca()!=null || tabelafreterotaAntiga.getListaTabelafretepraca()!=null){
			
			String pracaAntigo = "N/S";
			String praca = "N/S";
			String valorAntigo = "0,00";
			String valor = "0,00";
			Boolean isMaiorListaAntiga;
			
			List<Tabelafretepraca> listaMaior;
			List<Tabelafretepraca> listaMenor;
			
			if(tabelafreterota.getListaTabelafretepraca().size() > tabelafreterotaAntiga.getListaTabelafretepraca().size()){
				listaMaior = tabelafreterota.getListaTabelafretepraca();
				listaMenor = tabelafreterotaAntiga.getListaTabelafretepraca();
				isMaiorListaAntiga = Boolean.FALSE;
			}else{
				listaMaior = tabelafreterotaAntiga.getListaTabelafretepraca(); 
				listaMenor = tabelafreterota.getListaTabelafretepraca();
				isMaiorListaAntiga = Boolean.TRUE;
			}
			
			for(Integer i=0;i<listaMaior.size();i++){
				
				//Inicializando os valores da Lista Maior, validando se a maior é a lista nova ou a antiga.
				try {
					if(isMaiorListaAntiga){
						pracaAntigo = listaMaior.get(i).getPraca().getNome();
						valorAntigo = listaMaior.get(i).getValorentrega().toString();
					}else{
						Praca p = pracaService.load(listaMaior.get(i).getPraca());
						praca = p.getNome();
						valor = listaMaior.get(i).getValorentrega().toString();
					}
				} catch (Exception e){
					if(isMaiorListaAntiga){
						pracaAntigo = "N/S";
						valorAntigo = "0,00";
					}else{
						praca = "N/S";
						valor = "0,00";
					}
				}
				
				//Inicializando os valores da Lista Menor, validando se a menor é a lista nova ou a antiga.
				try {
					if(!isMaiorListaAntiga){
						pracaAntigo = listaMenor.get(i).getPraca().getNome();
						valorAntigo = listaMenor.get(i).getValorentrega().toString();
					}else{
						Praca p = pracaService.load(listaMenor.get(i).getPraca());
						praca = p.getNome();
						valor = listaMenor.get(i).getValorentrega().toString();
					}
				} catch (Exception e) {
					if(!isMaiorListaAntiga){
						pracaAntigo = "N/S";
						valorAntigo = "0,00";
					}else{
						praca = "N/S";
						valor = "0,00";
					}
				}
				
				if(!pracaAntigo.equals(praca)){
					motivo.append("\nA praça foi alterada de "+pracaAntigo+" para "+praca+".<br>");
				}				
				if(!valorAntigo.equals(valor)){
					motivo.append("\nO valor da praça "+praca+" foi alterado de "+valorAntigo+" para "+valor+".<br>");
				}
				
			}
		}
		
		return motivo.toString();
		
	}
	
}
