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
	 * Verifica os lotes do invent�rio cobrem todos
	 * os endere�os do dep�sito
	 * 
	 * @author Leonardo Guimar�es
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
			errors.reject("001","H� endere�os n�o cobertos pelo invent�rio.");
		
	}
	
	/**
	 * Valida se o lote cobre todo o pr�dio quando a caracter�stica � blocado.
	 * 
	 * @author Leonardo Guimar�es
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
						errors.reject("001","O lote " + (i + 1) + " deve cobrir todo pr�dio blocado.");
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
	 * @author C�ntia Nogueira
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
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
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
	 * M�todo que gera lotes em massa
	 * 
	 * @param inventariolote
	 * @return
	 * @author Tom�s Rabelo
	 */
	public List<Inventariolote> prepareInventarioEmMassaToSave(Inventariolote inventariolote) {
		List<Inventariolote> listaInventariolote = new ArrayList<Inventariolote>();
		
		List<EnderecoLoteVO> listaEnderecoLote = this.findEnderecoLote(inventariolote);
		
		Inventariolote loteCriado = null;
		for (EnderecoLoteVO loteVO : listaEnderecoLote) {
			
			//Somente ser� reaproveitado o lote se endere�o for blocado
			if (!loteVO.isBlocado())
				loteCriado = null;
			else if (loteCriado != null && loteCriado.getRuainicial().equals(loteVO.getRua()) 
					&& loteCriado.getEnderecolado().equals(loteVO.getEnderecolado())) {
				//o endere�o � blocado e � da mesma rua e do mesmo lado, ent�o n�o ser� gerado novo lote de invent�rio
				
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
	 * Monta uma lista com todas as leitura divergentes de um lote de invent�rio, para realizar
	 * o ajuste de estoque dos endere�os.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public List<LeituraDivergenteInventarioVO> findLeiturasDivergentes(Inventariolote inventariolote){
		return inventarioloteDAO.findLeiturasDivergentes(inventariolote);
	}

	/**
	 * Conta quantos lotes ger�o gerados pela cria��o autom�tica de lotes em massa.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public int contarLotesEmMassa(Inventariolote inventariolote) {
		return inventarioloteDAO.contarLotesEmMassa(inventariolote);
	}
	
	/**
	 * Busca os endere�os para montagem autom�tica dos lotes de contagem de invent�rio.
	 * 
	 * @author Giovane Freitas
	 * @param inventariolote
	 * @return
	 */
	public List<EnderecoLoteVO> findEnderecoLote(Inventariolote inventariolote){
		return inventarioloteDAO.findEnderecoLote(inventariolote);
	}
	
}
