package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.neo.types.Money;

public class ResumoMovimentacaoVO {

	private int numeroQuadro;
	private String grupo;
	private String deposito;
	private String tipooperacao;
	private Long qtde = 0L;
	private Double cubagem = 0.0;
	private Money valor = new Money(0);
	private int fator = 1;

	public int getNumeroQuadro() {
		return numeroQuadro;
	}
	
	public String getGrupo() {
		return grupo;
	}
	
	public String getDeposito() {
		return deposito;
	}

	public String getTipooperacao() {
		return tipooperacao;
	}

	public Long getQtde() {
		return qtde;
	}

	public Double getCubagem() {
		return cubagem;
	}

	public Money getValor() {
		return valor;
	}
	
	public int getFator() {
		return fator;
	}

	public void setNumeroQuadro(int numeroQuadro) {
		this.numeroQuadro = numeroQuadro;
	}
	
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public void setTipooperacao(String tipooperacao) {
		this.tipooperacao = tipooperacao;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}

	public void setCubagem(Double cubagem) {
		this.cubagem = cubagem;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}

	public void setFator(int fator) {
		this.fator = fator;
	}
	
}
