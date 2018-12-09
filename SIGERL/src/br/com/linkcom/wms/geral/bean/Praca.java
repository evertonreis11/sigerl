package br.com.linkcom.wms.geral.bean;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */
@Entity
@SequenceGenerator(name="sq_praca", sequenceName="sq_praca")
@DisplayName("Praças vinculadas à faixa de CEP")
public class Praca {
	
	private Integer cdpraca;
	private String nome;
	private Cep cepinicio;
	private Cep cepfim;
	private Cep cep;
	//private List<Rotapraca> listaRotapraca = new ArrayList<Rotapraca>();
	private Money vlrfretenormal;
	private Money vlrfreteagendado;
	private Money valorfreteporentrega;
	private Deposito deposito;
	private Tiporotapraca tiporotapraca;
	private Set<Rotapraca> listaRotapraca = new ListSet<Rotapraca>(Rotapraca.class);
	
	//Transients
	private String mascara;
	private String RotaPraca;
	private Filial filial;
	private Cep cepinicioView;
	private Cep cepfimView;
	private Integer cdtiporotapraca;

	
	public Praca(Integer cdpraca, String pracanome) {
		this.cdpraca = cdpraca;
		this.nome = pracanome;
	}

	public Praca() {
	}

	public Praca(Integer cd) {
		this.cdpraca = cd;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_praca")
	public Integer getCdpraca() {
		return cdpraca;
	}
	
	@DisplayName("Nome")
	@MaxLength(50)
	@Required
	public String getNome() {
		return nome;
	}
	
	@DisplayName("CEP inicial")	
	@MaxLength(9)
	@Required
	public Cep getCepinicio() {
		return cepinicio;
	}
	
	@DisplayName("CEP final")
	@MaxLength(9)
	@Required
	public Cep getCepfim() {
		return cepfim;
	}
	
	@DisplayName("CEP")
	@MaxLength(9)
	@Transient
	public Cep getCep() {
		return cep;
	}
	
	@Required
    @DisplayName("Tipo da Praca")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtiporotapraca")	
	public Tiporotapraca getTiporotapraca() {
		return tiporotapraca;
	}

	@OneToMany(mappedBy="praca")
	public Set<Rotapraca> getListaRotapraca() {
		return listaRotapraca;
	}
	
	@Transient
	@DescriptionProperty(usingFields={"nome","cepinicio","cepfim"})
	public String getMascara() {
		String cepInicio = "";
		String cepFim = "";
		if(getCepinicio() != null)
			cepInicio = " - " + getCepinicio();
		if(getCepfim() != null)
			cepFim = " - " + getCepfim();
		
		mascara = nome + cepInicio + cepFim;
		return mascara; 
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	@DisplayName("Depósito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@DisplayName("Valor do Frete normal")
	public Money getVlrfretenormal() {
		return vlrfretenormal;
	}
	@DisplayName("Valor do Frete agendado")
	public Money getVlrfreteagendado() {
		return vlrfreteagendado;
	}
	
	@DisplayName("Valor do Frete por Entrega")	
	public Money getValorfreteporentrega() {
		return valorfreteporentrega;
	}

	public void setValorfreteporentrega(Money valorfreteporentrega) {
		this.valorfreteporentrega = valorfreteporentrega;
	}

	public void setCdpraca(Integer cdpraca) {
		this.cdpraca = cdpraca;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setCepinicio(Cep cepinicio) {
		this.cepinicio = cepinicio;
	}
	
	public void setCepfim(Cep cepfim) {
		this.cepfim = cepfim;
	}
	
	public void setCep(Cep cep) {
		this.cep = cep;
	}
	
	public void setListaRotapraca(Set<Rotapraca> listaRotapraca) {
		this.listaRotapraca = listaRotapraca;
	}
	
	public void setMascara(String mascara) {
		this.mascara = mascara;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setTiporotapraca(Tiporotapraca tiporotapraca) {
		this.tiporotapraca = tiporotapraca;
	}
	
	public void setVlrfretenormal(Money vlrfretenormal) {
		this.vlrfretenormal = vlrfretenormal;
	}

	public void setVlrfreteagendado(Money vlrfreteagendado) {
		this.vlrfreteagendado = vlrfreteagendado;
	}

	public boolean equals(Object obj) {		
		if(obj instanceof Praca) {
			Praca praca = (Praca) obj;
			
			return praca.getCdpraca().intValue() == this.getCdpraca().intValue();
		}
		
		return super.equals(obj);
	}
	
	@Transient
	public String getRotaPraca() {
		return RotaPraca;
	}

	public void setRotaPraca(String rotaPraca) {
		RotaPraca = rotaPraca;
	}
	
	@Transient
	public Cep getCepinicioView() {
		return cepinicioView;
	}

	public void setCepinicioView(Cep cepinicioView) {
		this.cepinicioView = cepinicioView;
	}
	
	@Transient
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	@Transient
	public Cep getCepfimView() {
		return cepfimView;
	}

	public void setCepfimView(Cep cepfimView) {
		this.cepfimView = cepfimView;
	}

	@Transient
	public Integer getCdtiporotapraca() {
		if(tiporotapraca!=null && tiporotapraca.getCdtiporotapraca()!=null){
			return tiporotapraca.getCdtiporotapraca();
		}
		return cdtiporotapraca;
	}

	public void setCdtiporotapraca(Integer cdtiporotapraca) {
		this.cdtiporotapraca = cdtiporotapraca;
	}
	
	
	
	
}
