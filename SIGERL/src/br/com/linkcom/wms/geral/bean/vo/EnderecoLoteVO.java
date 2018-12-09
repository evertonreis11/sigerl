package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.wms.geral.bean.Enderecolado;

public class EnderecoLoteVO {

	private int cdarea;
	private int rua;
	private int nivel;
	private boolean ladoImpar;
	private boolean blocado;
	private int predioInicial;
	private int predioFinal;
	private int aptoInicial;
	private int aptoFinal;

	public int getCdarea() {
		return cdarea;
	}

	public void setCdarea(int cdarea) {
		this.cdarea = cdarea;
	}

	public int getRua() {
		return rua;
	}

	public void setRua(int rua) {
		this.rua = rua;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public boolean isLadoImpar() {
		return ladoImpar;
	}

	public void setLadoImpar(boolean ladoImpar) {
		this.ladoImpar = ladoImpar;
	}

	public boolean isBlocado() {
		return blocado;
	}

	public void setBlocado(boolean blocado) {
		this.blocado = blocado;
	}

	public int getPredioInicial() {
		return predioInicial;
	}

	public void setPredioInicial(int predioInicial) {
		this.predioInicial = predioInicial;
	}

	public int getPredioFinal() {
		return predioFinal;
	}

	public void setPredioFinal(int predioFinal) {
		this.predioFinal = predioFinal;
	}

	public int getAptoInicial() {
		return aptoInicial;
	}

	public void setAptoInicial(int aptoInicial) {
		this.aptoInicial = aptoInicial;
	}

	public int getAptoFinal() {
		return aptoFinal;
	}

	public void setAptoFinal(int aptoFinal) {
		this.aptoFinal = aptoFinal;
	}

	public Enderecolado getEnderecolado() {
		if (this.isLadoImpar())
			return Enderecolado.IMPAR;
		else
			return Enderecolado.PAR;
		
	}

}
