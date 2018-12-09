package br.com.linkcom.wms.geral.bean;

import java.util.Date;
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
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.validation.annotation.MaxLength;

/**
 * @author Guilherme Arantes de Oliveira
 *
 */

@Entity
@SequenceGenerator(name = "sq_rota", sequenceName = "sq_rota")
@DisplayName("Rotas")
public class Rota {	
	
	public final static Rota ROTA_NAO_ENCONTRADA = new Rota(-1, "Rota não encontrada");
	
	private Integer cdrota;
	private String nome;
	private Deposito deposito;
	private Tiporota tiporota;
	private Date dtproximaentrega;
	private Integer frequenciarota;
	private Integer prazoextra; 
	private Set<Rotaturnodeentrega> listaRotaturnodeentrega;
	private Set<Rotaturnoextra> listaRotaturnoextra;
	private Set<Rotapraca> listaRotapraca;
//	private List<EnumDiasDaSemana> listaEnumDiasDaSemana;
	private Boolean segunda;
	private Boolean terca;
	private Boolean quarta;
	private Boolean quinta;
	private Boolean sexta;
	private Boolean sabado;
	private Boolean domingo;
	private Boolean turnoextra;
	private Boolean areaderisco = Boolean.FALSE;
	private Money valorfreteporentrega;
	private Integer tentativaCorte;
	private Boolean isCortado;
	private Tiporotapraca tiporotapraca;
	private Boolean temDepositoTransbordo;
	private Deposito depositotransbordo;
	private Integer cdtiporotapraca;

	
	public Rota() {
	}

	public Rota(Integer cdrota, String rotanome) {
		this.cdrota = cdrota;
		this.nome = rotanome;
	}

	public Rota(Integer cdrota) {
		this.cdrota = cdrota;
	}
	
	//Get's
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_rota")
	public Integer getCdrota() {
		return cdrota;
	}
	
	@DescriptionProperty
	@DisplayName("Nome")
	@MaxLength(50)
	public String getNome() {
		return nome;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	public Deposito getDeposito() {
		return deposito;
	}
	
	@OneToMany(mappedBy="rota")
	public Set<Rotapraca> getListaRotapraca() {
		return listaRotapraca;
	}
	
	@DisplayName("Tipo rota")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtiporota")
	public Tiporota getTiporota() {
		return tiporota;
	}
	
	@DisplayName("Área de risco")
	public boolean getAreaderisco() {
		return areaderisco;
	}
	
	@OneToMany(mappedBy="rota")
	public Set<Rotaturnodeentrega> getListaRotaturnodeentrega() {
		return listaRotaturnodeentrega;
	}
	
	@OneToMany(mappedBy="rota")
	public Set<Rotaturnoextra> getListaRotaturnoextra() {
		return listaRotaturnoextra;
	}

	public Date getDtproximaentrega() {
		return dtproximaentrega;
	}

	public Integer getFrequenciarota() {
		return frequenciarota;
	}

	public Integer getPrazoextra() {
		return prazoextra;
	}	
	
	@Transient
	public Boolean getSegunda() {
		return segunda;
	}
	
	@Transient
	public Boolean getTerca() {
		return terca;
	}
	
	@Transient
	public Boolean getQuarta() {
		return quarta;
	}

	@Transient
	public Boolean getQuinta() {
		return quinta;
	}

	@Transient
	public Boolean getSexta() {
		return sexta;
	}

	@Transient
	public Boolean getSabado() {
		return sabado;
	}
	
	@Transient
	public Boolean getDomingo() {
		return domingo;
	}
		
	public Boolean getTurnoextra() {
		return turnoextra;
	}
	
	@DisplayName("Valor do Frete por Entrega")	
	public Money getValorfreteporentrega() {
		return valorfreteporentrega;
	}

	@Transient	
	public Integer getTentativaCorte() {
		return tentativaCorte;
	}
	
	@Transient
	public Boolean getIsCortado() {
		return isCortado;
	}

	public void setIsCortado(Boolean isCortado) {
		this.isCortado = isCortado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtiporotapraca")
	public Tiporotapraca getTiporotapraca() {
		return tiporotapraca;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddepositotransbordo")
	public Deposito getDepositotransbordo() {
		return depositotransbordo;
	}
	
	public Boolean getTemDepositoTransbordo() {
		return temDepositoTransbordo;
	}

	
	//Set's
	public void setAreaderisco(Boolean areaderisco) {
		this.areaderisco = areaderisco;
	}
	
	public void setTentativaCorte(Integer tentativaCorte) {
		this.tentativaCorte = tentativaCorte;
	}

	public void setSegunda(Boolean segunda) {
		this.segunda = segunda;
	}

	public void setTerca(Boolean terca) {
		this.terca = terca;
	}

	public void setQuarta(Boolean quarta) {
		this.quarta = quarta;
	}

	public void setQuinta(Boolean quinta) {
		this.quinta = quinta;
	}

	public void setSexta(Boolean sexta) {
		this.sexta = sexta;
	}

	public void setSabado(Boolean sabado) {
		this.sabado = sabado;
	}

	public void setDomingo(Boolean domingo) {
		this.domingo = domingo;
	}
	
	public void setListaRotaturnodeentrega(Set<Rotaturnodeentrega> listaRotaturnodeentrega) {
		this.listaRotaturnodeentrega = listaRotaturnodeentrega;
	}
	
	public void setListaRotaturnoextra(Set<Rotaturnoextra> listaRotaturnoextra) {
		this.listaRotaturnoextra = listaRotaturnoextra;
	}

	public void setDtproximaentrega(Date dtproximaentrega) {
		this.dtproximaentrega = dtproximaentrega;
	}
	
	public void setTiporota(Tiporota tiporota) {
		this.tiporota = tiporota;
	}

	public void setFrequenciarota(Integer frequenciarota) {
		this.frequenciarota = frequenciarota;
	}

	public void setPrazoextra(Integer prazoextra) {
		this.prazoextra = prazoextra;
	}

	public void setCdrota(Integer cdrota) {
		this.cdrota = cdrota;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setListaRotapraca(Set<Rotapraca> listaRotapraca) {
		this.listaRotapraca = listaRotapraca;
	}
	
	public void setTurnoextra(Boolean turnoextra) {
		this.turnoextra = turnoextra;
	}

	public void setValorfreteporentrega(Money valorfreteporentrega) {
		this.valorfreteporentrega = valorfreteporentrega;
	}
	
	public void setTiporotapraca(Tiporotapraca tiporotapraca) {
		this.tiporotapraca = tiporotapraca;
	}

	public void setDepositotransbordo(Deposito depositotransbordo) {
		this.depositotransbordo = depositotransbordo;
	}
	
	public void setTemDepositoTransbordo(Boolean temDepositoTransbordo) {
		this.temDepositoTransbordo = temDepositoTransbordo;
	}

	
	public boolean equals(Object obj) {
		if (obj instanceof Rota) {
			Rota rota = (Rota) obj;
			
			return rota.getCdrota().intValue() == this.getCdrota().intValue();
		}
		
		return super.equals(obj);
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
