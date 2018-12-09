package br.com.linkcom.wms.util.logistica;

public class ProdutividadeVO {
	private Integer cdpessoa;
	private String dataInicio;
	private String dataFim;
	private String pessoaNome;
	private String ordemTipoNome;
	private Double peso;
	private Double volume;
	private Long palete;
	
	public String getDataInicio() {
		return dataInicio;
	}
	
	public String getDataFim() {
		return dataFim;
	}
	
	public String getPessoaNome() {
		return pessoaNome;
	}
	
	public Integer getCdpessoa() {
		return cdpessoa;
	}
	
	public String getOrdemTipoNome() {
		return ordemTipoNome;
	}
	
	public Double getPeso() {
		return peso;
	}
	
	public Double getVolume() {
		return volume;
	}
	
	public Long getPalete() {
		return palete;
	}
	
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
	
	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}
	
	public void setCdpessoa(Integer cdpessoa) {
		this.cdpessoa = cdpessoa;
	}
	
	public void setOrdemTipoNome(String ordemTipoNome) {
		this.ordemTipoNome = ordemTipoNome;
	}
	
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public void setPalete(Long palete) {
		this.palete = palete;
	}
}
