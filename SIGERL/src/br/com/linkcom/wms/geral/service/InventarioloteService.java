package br.com.linkcom.wms.geral.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.validation.BindException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Ordemservico;
import br.com.linkcom.wms.geral.bean.vo.EnderecoLoteVO;
import br.com.linkcom.wms.geral.bean.vo.LeituraDivergenteInventarioVO;
import br.com.linkcom.wms.geral.dao.InventarioloteDAO;
import br.com.linkcom.wms.geral.filter.IntervaloEndereco;
import br.com.linkcom.wms.util.WmsUtil;

public class InventarioloteService extends GenericService<Inventariolote>{
	
	protected InventarioloteDAO inventarioloteDAO;
	protected EnderecoService enderecoService;
	protected ProdutoService produtoService;
	
	public void setInventarioloteDAO(InventarioloteDAO inventarioloteDAO) {
		this.inventarioloteDAO = inventarioloteDAO;
	}
	
	public void setEnderecoService(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}
	
	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}
	
	/**
	 * Verifica os lotes do inventário cobrem todos
	 * os endereços do depósito
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param listaInventariolote
	 * @param errors
	 */
	public void validateAllEnderecos(Collection<IntervaloEndereco> listaInventariolote,BindException errors) {
		Long numeroEnderecosDeposito = enderecoService.getNumeroEnderecosDeposito();
		Long numeroEnderecosInventario = 0L;
		Long aux = null;
		for (IntervaloEndereco inventariolote : listaInventariolote) {
			 aux = enderecoService.getNumeroEnderecosLote(inventariolote);
			 if(aux != null){
				 numeroEnderecosInventario += aux;
			 }
		}
		
		if(!numeroEnderecosInventario.equals(numeroEnderecosDeposito))
			errors.reject("001","Há endereços não cobertos pelo inventário.");
		
	}
	
	/**
	 * Valida se o lote cobre todo o prédio quando a característica é blocado.
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.geral.service.EnderecoService#findEnderecoPredio(Integer rua, Integer predio, Area area, boolean primeiro)
	 * @see #inicializaEnderecobyInventarioLote(Inventariolote)
	 * @param listaInventariolote
	 * @param errors
	 */
	public void validateStatusBlocado(List<IntervaloEndereco> listaInventariolote,BindException errors) {
		for (int i = 0; i < listaInventariolote.size(); i++) {
			IntervaloEndereco inventariolote = listaInventariolote.get(i);
			if(inventariolote .getEnderecofuncao() == null || inventariolote.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
				if(inventariolote.getEnderecofuncao()==null){
					Endereco endereco = inicializaEnderecobyInventarioLote(inventariolote);
					List<Endereco> listaEndereco=enderecoService.recuperarEnderecosCadastrados(endereco, null);	
					if(!enderecoService.HasBlocado(listaEndereco)){
						continue;
					}
				}
				Endereco enderecoInicial = enderecoService.findEnderecoPredio(inventariolote.getRuainicial(),inventariolote.getPredioinicial(),inventariolote.getArea(),true);
				Endereco enderecoFinal = enderecoService.findEnderecoPredio(inventariolote.getRuafinal(),inventariolote.getPrediofinal(),inventariolote.getArea(),false);
				if((enderecoInicial != null && enderecoInicial.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)) || (enderecoFinal != null && enderecoFinal.getEnderecofuncao().equals(Enderecofuncao.BLOCADO))){
					
					long endInicial = WmsUtil.concatenateNumbers(inventariolote.getRuainicial(),inventariolote.getPredioinicial(),inventariolote.getNivelinicial(),inventariolote.getAptoinicial());
					long endFinal = WmsUtil.concatenateNumbers(inventariolote.getRuafinal(),inventariolote.getPrediofinal(),inventariolote.getNivelfinal(),inventariolote.getAptofinal());
					
					long auxInicial = 0;
					long auxFinal = 0;
					if(enderecoInicial != null)
						auxInicial = WmsUtil.concatenateNumbers(enderecoInicial.getRua(),enderecoInicial.getPredio(),enderecoInicial.getNivel(),enderecoInicial.getApto());
					if(enderecoFinal != null)
						auxFinal = WmsUtil.concatenateNumbers(enderecoFinal.getRua(),enderecoFinal.getPredio(),enderecoFinal.getNivel(),enderecoFinal.getApto());
					
					if((enderecoInicial != null && endInicial > auxInicial) || (enderecoFinal != null && endFinal < auxFinal)){
						errors.reject("001","O lote " + (i + 1) + " deve cobrir todo prédio blocado.");
						break;
					}
					
				}
			}
		}
	}
	
	/**
	 * Inicializa endereco atraves do lote
	 * @param inventariolote
	 * @return
	 * @author Cíntia Nogueira
	 */
	private Endereco inicializaEnderecobyInventarioLote(IntervaloEndereco inventariolote){
		Endereco endereco = new Endereco();
		endereco.setArea(inventariolote.getArea());
		endereco.setAptoI(inventariolote.getAptoinicial());
		endereco.setAptoF(inventariolote.getAptofinal());
		endereco.setNivelF(inventariolote.getNivelfinal());
		endereco.setNivelI(inventariolote.getNivelinicial());
		endereco.setPredioI(inventariolote.getPredioinicial());
		endereco.setPredioF(inventariolote.getPredioinicial());
		endereco.setRuaI(inventariolote.getRuainicial());
		endereco.setRuaF(inventariolote.getRuafinal());
		
		return endereco;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param ordemservico
	 * @return
	 */
	public Inventariolote findByOS(Ordemservico ordemservico) {
		return inventarioloteDAO.findByOS(ordemservico);
	}
	
	/* singleton */
	private static InventarioloteService instance;
	public static InventarioloteService getInstance() {
		if(instance == null){
			instance = Neo.getObject(InventarioloteService.class);
		}
		return instance;
	}

	/**
	 * Método que gera lotes em massa
	 * 
	 * @param inventariolote
	 * @return
	 * @author Tomás Rabelo
	 */
	public List<Inventariolote> prepareInventarioEmMassaToSave(Inventariolote inventariolote) {
		List<Inventariolote> listaInventariolote = new ArrayList<Inventariolote>();
		
		List<EnderecoLoteVO> listaEnderecoLote = this.findEnderecoLote(inventariolote);
		
		Inventariolote loteCriado = null;
		for (EnderecoLoteVO loteVO : listaEnderecoLote) {
			
			//Somente será reaproveitado o lote se endereço for blocado
			if (!loteVO.isBlocado())
				loteCriado = null;
			else if (loteCriado != null && loteCriado.getRuainicial().equals(loteVO.getRua()) 
					&& loteCriado.getEnderecolado().equals(loteVO.getEnderecolado())) {
				//o endereço é blocado e é da mesma rua e do mesmo lado, então não será gerado novo lote de inventário
				
				if (loteCriado.getPredioinicial() > loteVO.getPredioInicial())
					loteCriado.setPredioinicial(loteVO.getPredioInicial());

				if (loteCriado.getPrediofinal() < loteVO.getPredioFinal())
					loteCriado.setPrediofinal(loteVO.getPredioFinal());
				
				if (loteCriado.getNivelinicial() > loteVO.getNivel())
					loteCriado.setNivelinicial(loteVO.getNivel());

				if (loteCriado.getNivelfinal() < loteVO.getNivel())
						loteCriado.setNivelfinal(loteVO.getNivel());
				
				if (loteCriado.getAptoinicial() > loteVO.getAptoInicial())
						loteCriado.setAptoinicial(loteVO.getAptoInicial());

				if (loteCriado.getAptofinal() < loteVO.getAptoFinal())
						loteCriado.setAptofinal(loteVO.getAptoFinal());
				
			} else {
				loteCriado = null;
			}
			
			if (loteCriado == null){
				loteCriado = new Inventariolote();
				listaInventariolote.add(loteCriado);
				
				loteCriado.setArea(inventariolote.getArea());
				loteCriado.setEnderecofuncao(inventariolote.getEnderecofuncao());
				loteCriado.setProduto(inventariolote.getProduto());
				loteCriado.setFracionada(inventariolote.getFracionada());

				loteCriado.setEnderecolado(loteVO.getEnderecolado());
				
				loteCriado.setRuainicial(loteVO.getRua());
				loteCriado.setRuafinal(loteVO.getRua());

				loteCriado.setPredioinicial(loteVO.getPredioInicial());
				loteCriado.setPrediofinal(loteVO.getPredioFinal());
				
				loteCriado.setNivelinicial(loteVO.getNivel());
				loteCriado.setNivelfinal(loteVO.getNivel());
				
				loteCriado.setAptoinicial(loteVO.getAptoInicial());
				loteCriado.setAptofinal(loteVO.getAptoFinal());
			}

		}
		return listaInventariolote;
	}

	/**
	 * Monta uma lista com todas as leitura divergentes de um lote de inventário, para realizar
	 * o ajuste de estoque dos endereços.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public List<LeituraDivergenteInventarioVO> findLeiturasDivergentes(Inventariolote inventariolote){
		return inventarioloteDAO.findLeiturasDivergentes(inventariolote);
	}

	/**
	 * Conta quantos lotes gerão gerados pela criação automática de lotes em massa.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public int contarLotesEmMassa(Inventariolote inventariolote) {
		return inventarioloteDAO.contarLotesEmMassa(inventariolote);
	}
	
	/**
	 * Busca os endereços para montagem automática dos lotes de contagem de inventário.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public List<EnderecoLoteVO> findEnderecoLote(Inventariolote inventariolote){
		return inventarioloteDAO.findEnderecoLote(inventariolote);
	}
	
}
