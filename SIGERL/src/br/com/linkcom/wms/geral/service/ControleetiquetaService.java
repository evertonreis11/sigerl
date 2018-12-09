package br.com.linkcom.wms.geral.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.service.GenericService;
import br.com.linkcom.wms.geral.bean.Controleestoque;
import br.com.linkcom.wms.geral.bean.Controleestoqueproduto;
import br.com.linkcom.wms.geral.bean.Controleestoquestatus;
import br.com.linkcom.wms.geral.bean.Controleetiqueta;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.SaldoProdutoVO;
import br.com.linkcom.wms.geral.dao.ControleestoqueDAO;
import br.com.linkcom.wms.geral.dao.ControleetiquetaDAO;
import br.com.linkcom.wms.modulo.logistica.controller.report.filtro.ControleestoqueFiltro;
import br.com.linkcom.wms.util.WmsUtil;

public class ControleetiquetaService extends GenericService<Controleetiqueta> {

	private static ControleetiquetaService instance;
	private ControleetiquetaDAO controleetiquetaDAO;

	public void setControleetiquetaDAO(ControleetiquetaDAO controleetiquetaDAO) {
		this.controleetiquetaDAO = controleetiquetaDAO;
	}

	public static ControleetiquetaService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ControleetiquetaService.class);
		}
		return instance;
	}
	
	
}
