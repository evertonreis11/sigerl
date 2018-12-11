package br.com.linkcom.wms.geral.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Bordero;
import br.com.linkcom.wms.geral.bean.Empresa;
import br.com.linkcom.wms.geral.bean.vo.ExtratoBorderoVO;
import br.com.linkcom.wms.geral.dao.BorderoDAO;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.BorderoFiltro;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class BorderoService extends GenericService<Bordero>{
	
	private BorderoDAO borderoDAO;
	
	public void setBorderoDAO(BorderoDAO borderoDAO) {
		this.borderoDAO = borderoDAO;
	}
	
	public IReport findByReport(BorderoFiltro filtro) {
		Report report = new Report("RelatorioExtratoBordero");
		List<ExtratoBorderoVO> listaExtratoFinanceiroVO = borderoDAO.findByRelatorioExtratoFinanceiro(filtro);
		report.setDataSource(listaExtratoFinanceiroVO);
		return report;
	}

	/**
	 * 
	 * @param cdmanifestos
	 * @return
	 */
	public List<Bordero> ratearBorderoPorEmpresa(Integer cdbordero) {
		
	return null;

		/*Connection connection = null;
		CallableStatement cs = null;
		
		try {
			connection = IntegradorSqlUtil.getNewConnection();
			
	        cs = (CallableStatement) connection.prepareCall("{ call PRC_AJUSTA_BORDERO_EMPRESA(?,?,?,?,?,?) }");
	        
	        if(cdbordero!=null){
	        	cs.setInt(1,cdbordero);
	        }else{
	        	return null;
	        }
	        
	        cs.registerOutParameter(2,Types.VARCHAR);
	        cs.registerOutParameter(3,Types.VARCHAR);
	        cs.registerOutParameter(4,Types.VARCHAR);
	        cs.registerOutParameter(5,Types.INTEGER);
	        cs.registerOutParameter(6,Types.INTEGER);
	        
	        cs.execute();
	        
	        String valor1 = cs.getString(2);
	        String valor2 = cs.getString(3);
	        String resposta = cs.getString(4);
	        Integer empresa1 = cs.getInt(5);
	        Integer empresa2 = cs.getInt(6);
	        
	        List<Bordero> listaBorderos = new ArrayList<Bordero>();	      
	        Empresa empresa = new Empresa();
	        
	        if(resposta.equals("OK")){
	        	
	        	if(valor1!=null && !valor1.isEmpty()){
	        		Bordero bordero1 = new Bordero();
	        		valor1 = valor1.replace(".", "").replace(",", ".");
	        		Money money = new Money(valor1);
	        		if(empresa1!=null){
	        			empresa = new Empresa(empresa1);
	        			bordero1.setEmpresa(empresa);
	        		}
	        		bordero1.setValortotal(money);
	        		listaBorderos.add(bordero1);
	        	}
	        	
	        	if(valor2!=null && !valor2.isEmpty()){
	        		Bordero bordero2 = new Bordero();
	        		valor2 = valor2.replace(".", "").replace(",", ".");
	        		Money money = new Money(valor2);
	        		if(empresa2!=null){
	        			empresa = new Empresa(empresa2);
	        			bordero2.setEmpresa(empresa);
	        		}
	        		bordero2.setValortotal(money);
	        		listaBorderos.add(bordero2);
	        	}	   
	        	
	        	return listaBorderos;
	        	
	        }else if (resposta.equals("NI")){
				return null;
	        }else{
	        	return null;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
			//durante a fase de implantação vai retornar true, depois tem que voltar pra false
			return null;
		}
		finally {
			try {
				cs.close();
				connection.close();
			}
			catch (Exception e) {
				System.out.println("Erro ao fechar a conexção do banco.\n");
				e.printStackTrace();
			}
		}*/
	}
	
}
