package br.com.linkcom.wms.geral.service;

import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Notafiscalentrada;
import br.com.linkcom.wms.geral.bean.Notafiscalentradaproduto;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Recebimento;
import br.com.linkcom.wms.geral.dao.NotafiscalentradaprodutoDAO;
import br.com.linkcom.wms.util.neo.persistence.GenericService;


public class NotafiscalentradaprodutoService extends GenericService<Notafiscalentradaproduto> {

	@SuppressWarnings("unused")
	private NotafiscalentradaprodutoDAO notafiscalentradaprodutoDAO;
	
	public void setNotafiscalentradaprodutoDAO(NotafiscalentradaprodutoDAO notafiscalentradaprodutoDAO) {
		this.notafiscalentradaprodutoDAO = notafiscalentradaprodutoDAO;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaprodutoDAO#recuperarNotafiscalentradaproduto(Recebimento)
	 * @param recebimento
	 * @return
	 * @author Arantes
	 */
	public long calcularQtdProdutos(Recebimento recebimento) {
		long result = 0;
		List<Notafiscalentradaproduto> listaNotafiscalentradaproduto = this.recuperarNotafiscalentradaproduto(recebimento);
		
		if(listaNotafiscalentradaproduto != null) {
			for (Notafiscalentradaproduto notafiscalentradaproduto : listaNotafiscalentradaproduto) {
				result += notafiscalentradaproduto.getQtde().longValue();
			}
		}
		
		return result;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.service.NotafiscalentradaprodutoService#recuperarNotafiscalentradaproduto(Recebimento)
	 * @param recebimento
	 * @return
	 * @author Arantes
	 */
	public double calcularPesoProdutos(Recebimento recebimento) {
		double result = 0;
		List<Notafiscalentradaproduto> listaNotafiscalentradaproduto = this.recuperarNotafiscalentradaproduto(recebimento);
		
		if(listaNotafiscalentradaproduto != null) {
			for (Notafiscalentradaproduto notafiscalentradaproduto : listaNotafiscalentradaproduto) {
				if(notafiscalentradaproduto.getProduto().getPesounitario() != null) {
					result += (notafiscalentradaproduto.getQtde().longValue() * notafiscalentradaproduto.getProduto().getPesounitario().doubleValue());					
				}
			}
		}
				
		return result;
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @see br.com.linkcom.wms.geral.dao.NotafiscalentradaprodutoDAO#recuperarNotafiscalentradaproduto(Recebimento) 
	 * @param recebimento
	 * @return
	 * 
	 * @author Arantes
	 * 
	 */
	private List<Notafiscalentradaproduto> recuperarNotafiscalentradaproduto(Recebimento recebimento) {
		return notafiscalentradaprodutoDAO.recuperarNotafiscalentradaproduto(recebimento);
		
	}
	
	/**
	 * 
	 * Método de referência ao DAO
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Notafiscalentradaproduto>
	 * @see br.com.linkcom.wms.geral.service.NotafiscalentradaprodutoService#findByListaNotafiscalentrada(List) 
	 * 
	 */
	public List<Notafiscalentradaproduto> findByListaNotafiscalentrada(List<Notafiscalentrada> listaNotafiscalentradaOrdenada) {
		
		String listaNotafiscalentrada = "";
		for (Notafiscalentrada notafiscalentrada : listaNotafiscalentradaOrdenada) {
			listaNotafiscalentrada += notafiscalentrada.getCdnotafiscalentrada() + ",";
		}
		
		return notafiscalentradaprodutoDAO.findByListaNotafiscalentrada(listaNotafiscalentrada);
	}
	
	/**
	 * Método de referência ao DAO
	 * 
	 * @author Filipe Santos
	 * 
	 * @param filtro
	 * @return List<Notafiscalentradaproduto>
	 * @see br.com.linkcom.wms.geral.service.NotafiscalentradaprodutoService#findByListaNotafiscalentrada(List) 
	 * 
	 */
	public List<Notafiscalentradaproduto> findByListaNotafiscalAgendadas (List<Notafiscalentrada> listaNotafiscalentradaOrdenada) {
		String listaNotafiscalentrada = "";
		for (Notafiscalentrada notafiscalentrada : listaNotafiscalentradaOrdenada) {
			listaNotafiscalentrada += notafiscalentrada.getCdnotafiscalentrada() + ",";
		}
		
		return notafiscalentradaprodutoDAO.findByListaNotafiscalAgendadas(listaNotafiscalentrada);
	}
	
	/**
	 * Método que a notafiscalentradaproduto
	 * 
	 * @param recebimento
	 * @return
	 * @author Cíntia Nogueira
	 * @see  br.com.linkcom.wms.geral.dao.NotafiscalentradaprodutoDAO#loadByRecebimentoProduto(Recebimento, Produto)
	 */
	public Notafiscalentradaproduto loadByRecebimentoProduto(Recebimento recebimento, Produto produto) {
		return notafiscalentradaprodutoDAO.loadByRecebimentoProduto(recebimento, produto);
	}
	
	/**
	 * Altera a data de validade e o lote
	 * @param bean
	 * @author Cíntia Nogueira
	 * @see  br.com.linkcom.wms.geral.dao.NotafiscalentradaprodutoDAO#updateLoteValidade(Notafiscalentradaproduto)
	 */
	public void updateLoteValidade(Notafiscalentradaproduto bean){
		notafiscalentradaprodutoDAO.updateLoteValidade(bean);
	}
	
	/**
	 * Reseta a validade e o lote do produto
	 * @param notafiscalentradaproduto
	 * @author Cíntia Nogueira
	 */
	public void resetarLoteDataValidade(Notafiscalentradaproduto notafiscal){
		notafiscalentradaprodutoDAO.resetarLoteDataValidade(notafiscal);
	}
	
	/**
	 * Reseta o lote e a data de validade 
	 * @param recebimento
	 * @author Cíntia Nogueira
	 */
	public void resetarLoteDataValidade(Recebimento recebimento){
		List<Notafiscalentradaproduto> lista = notafiscalentradaprodutoDAO.recuperarNotafiscalentradaproduto(recebimento);
		notafiscalentradaprodutoDAO.resetarLoteDataValidade(lista);
	}
	
	/* singleton */
	private static NotafiscalentradaprodutoService instance;
	public static NotafiscalentradaprodutoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(NotafiscalentradaprodutoService.class);
		}
		return instance;
	}
	
	/**
	 * Faz referência ao DAO.
	 * @param notafiscalentrada
	 * @return
	 */
	public List<Notafiscalentradaproduto> findNotaProdutos(Notafiscalentrada notafiscalentrada){
		return notafiscalentradaprodutoDAO.findNotaProdutos(notafiscalentrada);
	}

	/**
	 * 
	 * @param notas
	 * @return
	 */
	public List<Notafiscalentradaproduto> findProdutosByNotafiscal(List<Notafiscalentrada> notas) {
		String listaNotafiscalentrada = "";
		for (Notafiscalentrada notafiscalentrada : notas) {
			listaNotafiscalentrada += notafiscalentrada.getCdnotafiscalentrada() + ",";
		}
		return notafiscalentradaprodutoDAO.findProdutosByNotafiscal(listaNotafiscalentrada.substring(0,listaNotafiscalentrada.length()-1));
	}
}
