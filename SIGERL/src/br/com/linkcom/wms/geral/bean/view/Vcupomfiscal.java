package br.com.linkcom.wms.geral.bean.view;

public class Vcupomfiscal {

	private Long nro_loja;
	private Integer nro_nf;
	private Long nro_cupom;
	private Long nro_seq_nf;
	private String serie_nf;
	private String dt_emissao_nf;
	private String nome_razao_social_nf;
	private String endereco_dest;
	private String observacao;
	private Boolean ind_avulso;
	private Integer nro_manifesto;
	private Integer qtde_itens_nf;
	private Long vr_total_nf;
	private Integer nro_carga_veiculo;
	private String cliente;
	private Boolean isCadastardo = Boolean.FALSE;
	private Boolean isVinculado = Boolean.FALSE;
	
	
	//Get's
	public Long getNro_loja() {
		return nro_loja;
	}
	public Integer getNro_nf() {
		return nro_nf;
	}
	public Long getNro_cupom() {
		return nro_cupom;
	}
	public Long getNro_seq_nf() {
		return nro_seq_nf;
	}
	public String getSerie_nf() {
		return serie_nf;
	}
	public String getDt_emissao_nf() {
		return dt_emissao_nf;
	}
	public String getNome_razao_social_nf() {
		return nome_razao_social_nf;
	}
	public String getEndereco_dest() {
		return endereco_dest;
	}
	public String getObservacao() {
		return observacao;
	}
	public Boolean getInd_avulso() {
		return ind_avulso;
	}
	public Integer getNro_manifesto() {
		return nro_manifesto;
	}
	public Integer getQtde_itens_nf() {
		return qtde_itens_nf;
	}
	public Long getVr_total_nf() {
		return vr_total_nf;
	}
	public Integer getNro_carga_veiculo() {
		return nro_carga_veiculo;
	}
	public String getCliente() {
		return cliente;
	}
	public Boolean getIsCadastardo() {
		return isCadastardo;
	}
	public Boolean getIsVinculado() {
		return isVinculado;
	}
	
	
	//Set's
	public void setNro_loja(Long nroLoja) {
		nro_loja = nroLoja;
	}
	public void setNro_nf(Integer nroNf) {
		nro_nf = nroNf;
	}
	public void setNro_cupom(Long nroCupom) {
		nro_cupom = nroCupom;
	}
	public void setNro_seq_nf(Long nroSeqNf) {
		nro_seq_nf = nroSeqNf;
	}
	public void setSerie_nf(String serieNf) {
		serie_nf = serieNf;
	}
	public void setDt_emissao_nf(String dtEmissaoNf) {
		dt_emissao_nf = dtEmissaoNf;
	}
	public void setNome_razao_social_nf(String nomeRazaoSocialNf) {
		nome_razao_social_nf = nomeRazaoSocialNf;
	}
	public void setEndereco_dest(String enderecoDest) {
		endereco_dest = enderecoDest;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public void setInd_avulso(Boolean indAvulso) {
		ind_avulso = indAvulso;
	}
	public void setNro_manifesto(Integer nroManifesto) {
		nro_manifesto = nroManifesto;
	}
	public void setQtde_itens_nf(Integer qtdeItensNf) {
		qtde_itens_nf = qtdeItensNf;
	}
	public void setVr_total_nf(Long vrTotalNf) {
		vr_total_nf = vrTotalNf;
	}
	public void setNro_carga_veiculo(Integer nroCargaVeiculo) {
		nro_carga_veiculo = nroCargaVeiculo;
	}	
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public void setIsCadastardo(Boolean isCadastardo) {
		this.isCadastardo = isCadastardo;
	}
	public void setIsVinculado(Boolean isVinculado) {
		this.isVinculado = isVinculado;
	}

}
