package br.com.linkcom.wms.geral.service;

import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Rotapraca;
import br.com.linkcom.wms.geral.dao.RotapracaDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.crud.filtro.PracaFiltro;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */
public class RotapracaService extends GenericService<Rotapraca> {

	private RotapracaDAO rotapracaDAO;
	private TransactionTemplate transactionTemplate;
	
	public void setRotapracaDAO(RotapracaDAO rotapracaDAO) {
		this.rotapracaDAO = rotapracaDAO;
	}
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * 
	 * @param rota
	 */
	public void deleteByRota(Rota rota) {
		rotapracaDAO.deleteByRota(rota);
	}

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public Boolean vincularPracasEmRota(final PracaFiltro filtro){
		transactionTemplate.execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status){
				String[] pracas = filtro.getPracasSelecionadas().split(",");
				for (String strCdpraca : pracas) {
					//Inicalizando os Beans...
					Rotapraca rotapraca = new Rotapraca();
					Praca praca = new Praca(Integer.parseInt(strCdpraca));
					Rota rota = new Rota(filtro.getCdrota());
					Integer ordem = findUltimaOrdemByRota(filtro.getCdrota());
					//Setando os valores e salvando...
					rotapraca.setPraca(praca);
					rotapraca.setRota(rota);
					rotapraca.setOrdem(ordem);
					saveOrUpdate(rotapraca);
				}
				return null;
			}
		});
		return null;
	}
	
	/**
	 * 
	 * @param cdrota
	 * @return
	 */
	public Integer findUltimaOrdemByRota(Integer cdrota) {
		Rotapraca rotapraca = rotapracaDAO.findUltimaOrdemByRota(cdrota);
		if(rotapraca!=null && rotapraca.getOrdem()!=null){
			return rotapraca.getOrdem()+1;
		}else{
			return 1;
		}
	}

	/**
	 * 
	 * @param filtro
	 */
	public void desvincularPracasEmRota(PracaFiltro filtro) {
		Rotapraca rotapraca = findByPracaRota(filtro.getCdpraca(),filtro.getCdrota());
		if(rotapraca!=null)
			delete(rotapraca);
		else
			throw new WmsException("Não foi possivel localiza a praça, por favor, tente novamente");
	}

	/**
	 * 
	 * @param cdpraca
	 * @param cdrota
	 * @return
	 */
	public Rotapraca findByPracaRota(Integer cdpraca, Integer cdrota) {
		return rotapracaDAO.findByPracaRota(cdpraca,cdrota);
	}

	/**
	 * 
	 * @param pracasSelecionadas
	 * @return
	 */
	public List<Rotapraca> findPracasByWhereInPraca(String whereIn) {
		return rotapracaDAO.findPracasByWhereInPraca(whereIn);
	}
	
}
