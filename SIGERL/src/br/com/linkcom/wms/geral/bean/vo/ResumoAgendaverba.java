package br.com.linkcom.wms.geral.bean.vo;

import java.util.Date;

import br.com.linkcom.neo.types.Money;

public class ResumoAgendaverba {

	private String classeproduto;
	private Date mes;
	private Money valor;

	public String getClasseproduto() {
		return classeproduto;
	}

	public Date getMes() {
		return mes;
	}

	public Money getValor() {
		return valor;
	}

	public void setClasseproduto(String classeproduto) {
		this.classeproduto = classeproduto;
	}

	public void setMes(Date mes) {
		this.mes = mes;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classeproduto == null) ? 0 : classeproduto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ResumoAgendaverba other = (ResumoAgendaverba) obj;
		if (classeproduto == null) {
			if (other.classeproduto != null)
				return false;
		} else if (!classeproduto.equals(other.classeproduto))
			return false;
		return true;
	}
	
	

}
