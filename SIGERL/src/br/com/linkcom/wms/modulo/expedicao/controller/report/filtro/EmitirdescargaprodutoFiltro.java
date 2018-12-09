package br.com.linkcom.wms.modulo.expedicao.controller.report.filtro;

public class EmitirdescargaprodutoFiltro {
	
	private Integer cdmanifesto;
	private String codigobarras;

	public Integer getCdmanifesto() {
		return cdmanifesto;
	}
	public String getCodigobarras() {
		return codigobarras;
	}

	public void setCdmanifesto(Integer cdmanifesto) {
		this.cdmanifesto = cdmanifesto;
	}
	public void setCodigobarras(String codigobarras) {
		this.codigobarras = codigobarras;
	}
}