package br.com.linkcom.wms.geral.service;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import br.com.linkcom.wms.geral.bean.Notafiscalsaida;
import br.com.linkcom.wms.geral.bean.view.Vcupomfiscal;
import br.com.linkcom.wms.geral.dao.VcupomfiscalDAO;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.ricardoeletro.sigerl.expedicao.crud.filtro.ManifestoFiltro;

public class VcupomfiscalService extends GenericService<Vcupomfiscal> {
	
	private VcupomfiscalDAO vcupomfiscalDAO;
	private NotafiscalsaidaService notafiscalsaidaService;
	private ManifestonotafiscalService manifestonotafiscalService;
	
	public void setVcupomfiscalDAO(VcupomfiscalDAO vcupomfiscalDAO) {
		this.vcupomfiscalDAO = vcupomfiscalDAO;
	}
	public void setNotafiscalsaidaService(NotafiscalsaidaService notafiscalsaidaService) {
		this.notafiscalsaidaService = notafiscalsaidaService;
	}
	public void setManifestonotafiscalService(ManifestonotafiscalService manifestonotafiscalService) {
		this.manifestonotafiscalService = manifestonotafiscalService;
	}
	
//	/**
//	 * Retorna a lista de Cupons Fiscias emitidos nas lojas.
//	 * Cria uma conexão direta com a base do sistema MVLojas.
//	 * Número máximo de retorno: 30
//	 * 
//	 * @author Filipe Santos
//	 * @param filtro
//	 * @return
//	 */
//	public List<Vcupomfiscal> findByFiltro (ManifestoFiltro filtro){
//		Connection connection = getNewConnectionReloh();
//		List<Vcupomfiscal> lista = vcupomfiscalDAO.findByFiltro(filtro,connection);
//		setarFlagCadastradoVinculado(lista);
//		return lista;
//	}
//	
//	/**
//	 * Seta os flags mediante a comparação das lista 'Cupons Fiscais' e 'Notas Fiscais'
//	 * 
//	 * @author Filipe Santos
//	 * @param lista
//	 */
//	private void setarFlagCadastradoVinculado(List<Vcupomfiscal> lista) {
//		if(lista!=null && !lista.isEmpty()){
//			List<Notafiscalsaida> listaNotas = notafiscalsaidaService.findByCupom(lista);
//			if(listaNotas!=null && !listaNotas.isEmpty()){
//				for (Vcupomfiscal cf : lista) {
//					for (Notafiscalsaida nfs : listaNotas) {
//						if((cf.getNro_seq_nf().intValue() == nfs.getCodigoerp()) && nfs.getVinculado()){
//							cf.setIsCadastardo(Boolean.TRUE);
//							cf.setIsVinculado(Boolean.TRUE);
//						}else if(cf.getNro_seq_nf().intValue() == nfs.getCodigoerp()){
//							cf.setIsCadastardo(Boolean.TRUE);
//							cf.setIsVinculado(Boolean.FALSE);							
//						}else{
//							continue;
//						}
//					}
//				}
//			}
//		}
//	}
	
	/**
	 * Gerador de conexão com a base do RELO/RELOH
	 * 
	 * @author Filipe Santos
	 * @return Connection
	 */
	private Connection getNewConnectionReloh(){
		
		String jndiBD = "java:/mvreloh_OracleDS";
		Connection connection = null;
		Context context;
		
		try {
			context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup(jndiBD);
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception e) {
			throw new WmsException(e);
		}

		return connection;
	}
	
	/**
	 * Método responsável pela chamada ao Webservice: URL_BPEL_BUSCA_NF_TMS_RE 
	 * 
	 * @author Filipe Santos
	 * @param listaCuponsNaoCadastrados
	 */
	public String inserirNotaCupom(List<Vcupomfiscal> listaCuponsNaoCadastrados){
		
		String cuponsComRetornoNegativo = "";
		
		if(listaCuponsNaoCadastrados!=null && !listaCuponsNaoCadastrados.isEmpty()){
			
			for (Vcupomfiscal vcp : listaCuponsNaoCadastrados) {
				
				Integer cddeposito = WmsUtil.getDeposito().getCddeposito();
				String serienfe = vcp.getSerie_nf();
				Long numeronfe = vcp.getNro_cupom();
				Long codigoerp = vcp.getNro_loja();
				Date dataemissao = null;
				String chavenfe = null;
				
				if(vcp.getDt_emissao_nf()!=null){
					try {
						dataemissao = WmsUtil.stringToDate(vcp.getDt_emissao_nf());
					} catch (ParseException e) {
						System.out.println("Erro ao converter a data durante a chamada da PRC_BUSCA_NOTAS_MVLOJAS");
						e.printStackTrace();
					}
				}
				
				Boolean retorno = manifestonotafiscalService.findNotasSaidaByProcedure(cddeposito,chavenfe,numeronfe,dataemissao,codigoerp,serienfe);
				
				if(!retorno)
					cuponsComRetornoNegativo += "O cupom (código erp: "+vcp.getNro_seq_nf()+") não foi inserido conforme o esperado.\n";
			}
		}
		
		return cuponsComRetornoNegativo;
	}
	
}