package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Money;

@Entity
@SequenceGenerator(name = "sq_manifestofinanceiro", sequenceName = "sq_manifestofinanceiro")
public class Manifestofinanceiro {
	
	private Integer cdmanifestofinanceiro;
	private Integer entregasprevista;
	private Integer entregasexcluida;
	private Integer entregasretorno;
	private Integer entregasrealizadas;
	private Money valorcalculado;
	private Money acrescimo;
	private Money desconto;	
	private Money total;
	private Manifesto manifesto;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestofinanceiro")
	public Integer getCdmanifestofinanceiro() {
		return cdmanifestofinanceiro;
	}
	@DisplayName("Entr. Presvistas")
	public Integer getEntregasprevista() {
		return entregasprevista;
	}
	@DisplayName("Entr. Excluidas")
	public Integer getEntregasexcluida() {
		return entregasexcluida;
	}
	@DisplayName("Entr. Retornadas")
	public Integer getEntregasretorno() {
		return entregasretorno;
	}
	@DisplayName("Entr. Realizadas")
	public Integer getEntregasrealizadas() {
		return entregasrealizadas;
	}
	@DisplayName("Valor Calculado")
	public Money getValorcalculado() {
		return valorcalculado;
	}
	@DisplayName("Acréscimo")
	public Money getAcrescimo() {
		return acrescimo;
	}
	@DisplayName("Total a Pagar")
    public Money getTotal() {
		return total;
	}
	@DisplayName("Manifesto")
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cdmanifesto", referencedColumnName="cdmanifesto")
	public Manifesto getManifesto() {
		return manifesto;
	}
	@DisplayName("Desconto")
	public Money getDesconto() {
		return desconto;
	}
	
	
	//Set's
	public void setCdmanifestofinanceiro(Integer cdmanifestofinanceiro) {
		this.cdmanifestofinanceiro = cdmanifestofinanceiro;
	}
	public void setEntregasprevista(Integer entregasprevista) {
		this.entregasprevista = entregasprevista;
	}
	public void setEntregasexcluida(Integer entregasexcluida) {
		this.entregasexcluida = entregasexcluida;
	}
	public void setEntregasretorno(Integer entregasretorno) {
		this.entregasretorno = entregasretorno;
	}
	public void setEntregasrealizadas(Integer entregasrealizadas) {
		this.entregasrealizadas = entregasrealizadas;
	}
	public void setValorcalculado(Money valorcalculado) {
		this.valorcalculado = valorcalculado;
	}
	public void setAcrescimo(Money acrescimo) {
		this.acrescimo = acrescimo;
	}
	public void setTotal(Money total) {
		this.total = total;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	public void setDesconto(Money desconto) {
		this.desconto = desconto;
	}
	
}
