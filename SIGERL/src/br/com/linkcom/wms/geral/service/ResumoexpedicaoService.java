package br.com.linkcom.wms.geral.service;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.wms.geral.bean.Resumoexpedicao;
import br.com.linkcom.wms.geral.bean.vo.ResumoexpedicaoVO;
import br.com.linkcom.wms.geral.dao.ResumoexpedicaoDAO;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ResumoexpedicaoFiltro;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.neo.persistence.GenericService;

public class ResumoexpedicaoService extends GenericService<Resumoexpedicao>{

	private static ResumoexpedicaoService instance;
	private ResumoexpedicaoDAO resumoexpedicaoDAO;

	public static ResumoexpedicaoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ResumoexpedicaoService.class);
		}
		return instance;
	}
	
	public void setResumoexpedicaoDAO(ResumoexpedicaoDAO resumoexpedicaoDAO) {
		this.resumoexpedicaoDAO = resumoexpedicaoDAO;
	}

	public SqlRowSet getDadosExportacao(ResumoexpedicaoFiltro filtro) {
		return resumoexpedicaoDAO.getDadosExportacao(filtro);
	}

	public Resource getDadosPlanilhaDetalhada(ResumoexpedicaoFiltro filtro) {
		
		StringBuilder csv = new StringBuilder();
		List<ResumoexpedicaoVO> lista = resumoexpedicaoDAO.getDadosPlanilhaDetalhada(filtro);
		
		csv.append("Depósito").append(";");
		csv.append("Tipo de Operação").append(";");
		csv.append("Transportador").append(";");
		csv.append("Placa (Veículo)").append(";");
		csv.append("Linha de Separação").append(";");
		csv.append("Nº dos Pedidos").append(";");
		csv.append("Montagem").append(";");
		csv.append("Fechamento").append(";");
		csv.append("Nº do Carregamento").append(";");
		csv.append("Código do Produto").append(";");
		csv.append("Descrição do Produto").append(";");
		csv.append("Valor Cortado").append(";");
		csv.append("Valor Esperado").append(";");
		csv.append("Qtde Esperada").append(";");
		csv.append("Peso Esperado").append(";");
		csv.append("Qtde Confirmada").append(";");
		csv.append("Qtde Cortada").append(";");
		csv.append("Peso Confirmado").append(";");
		csv.append("Nº de Volumes Confirmados").append(";");
		csv.append("Cubagem Confirmada").append(";");
		csv.append("Tipo de Veiculo").append(";");
		
		if(lista!=null && !lista.isEmpty()){
			for (ResumoexpedicaoVO re : lista) {
				csv.append(re.getDeposito()!=null?re.getDeposito():"").append(";");
				csv.append(re.getTipoOperacao()!=null?re.getTipoOperacao():"").append(";");
				csv.append(re.getTransportador()!=null?re.getTransportador():"").append(";");
				csv.append(re.getVeiculoPlaca()!=null?re.getVeiculoPlaca():"").append(";");
				csv.append(re.getLinhaSeparacao()!=null?re.getLinhaSeparacao():"").append(";");
				csv.append(re.getNumeroPedido()!=null?re.getNumeroPedido():"").append(";");
				csv.append(re.getMontagem()!=null?re.getMontagem():"").append(";");
				csv.append(re.getFechamento()!=null?re.getFechamento():"").append(";");
				csv.append(re.getCdcarregamento()!=null?re.getCdcarregamento():"").append(";");
				csv.append(re.getCodigo()!=null?re.getCodigo():"").append(";");
				csv.append(re.getDescricao()!=null?re.getDescricao():"").append(";");
				csv.append(re.getValorCortado()!=null?re.getValorCortado():"").append(";");
				csv.append(re.getValorEsperado()!=null?re.getValorEsperado():"").append(";");
				csv.append(re.getQtdeEsperada()!=null?re.getQtdeEsperada():"").append(";");
				csv.append(re.getPesoConfirmado()!=null?re.getPesoConfirmado():"").append(";");
				csv.append(re.getQtdeConfirmada()!=null?re.getQtdeConfirmada():"").append(";");
				csv.append(re.getQtdeCortada()!=null?re.getQtdeCortada():"").append(";");
				csv.append(re.getPesoConfirmado()!=null?re.getPesoConfirmado():"").append(";");
				csv.append(re.getNumeroVolumeConfirmado()!=null?re.getNumeroVolumeConfirmado():"").append(";");
				csv.append(re.getCubagemConfirmada()!=null?re.getCubagemConfirmada():"").append(";");
				csv.append(re.getTipoVeiculo()!=null?re.getTipoVeiculo():"").append(";");
				csv.append("\n");
			}
		}
		
		Resource resource = new Resource();
		resource.setContentType("text/csv");
		resource.setFileName("WMS_Resumo_Expedicao_Detalhada_" + WmsUtil.getDeposito().getNome() + ".csv");
		resource.setContents(csv.toString().getBytes());
		return resource;
	}
	
}
