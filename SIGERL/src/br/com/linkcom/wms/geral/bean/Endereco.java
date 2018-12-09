package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;
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

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MaxValue;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.EnderecoFiltro;
import br.com.linkcom.wms.util.WmsUtil;

@Entity
@SequenceGenerator(name = "sq_endereco", sequenceName = "sq_endereco")
@NamedNativeQueries(value={
	@NamedNativeQuery(name="atualizar_endereco", query="{ call atualizar_endereco(?) }", resultSetMapping="scalar")
})
public class Endereco {
	// constantes
	public static final String INSERIR = "inserir";
	public static final String ALTERAR = "alterar";
	public static final String EXCLUIR = "excluir";
	public static final String BLOQUEIO = "bloqueio";
	public static final String PAR = "par";
	public static final String IMPAR = "impar";
	public static final String RESETFILTER = "resetfilter";
	
	// variáveis de instância
	protected Integer cdendereco;
	protected String endereco;
	protected Tipoendereco tipoendereco;
	protected Area area;
	protected Tipoestrutura tipoestrutura;
	protected Integer peso;	
	protected Integer rua;
	protected Integer predio;
	protected Integer nivel;
	protected Integer apto;
	protected Enderecofuncao enderecofuncao;
	protected Boolean larguraexcedente;
	protected Enderecostatus enderecostatus;
	protected Tipopalete tipopalete;
	protected Motivobloqueio motivobloqueio;
	protected Set<Ordemservicoprodutoendereco> listaOrdemservicoprodutoenderecoOrigem = new ListSet<Ordemservicoprodutoendereco>(Ordemservicoprodutoendereco.class);
	protected Set<Ordemservicoprodutoendereco> listaOrdemservicoprodutoenderecoDestino = new ListSet<Ordemservicoprodutoendereco>(Ordemservicoprodutoendereco.class);
	protected Set<Enderecoproduto> listaEnderecoproduto = new ListSet<Enderecoproduto>(Enderecoproduto.class);	
	protected Set<Transferenciaitem> listaTransferenciaitemenderecoOrigem = new ListSet<Transferenciaitem>(Transferenciaitem.class);
	protected Set<Transferenciaitem> listatransferenciaitemenderecoDestino = new ListSet<Transferenciaitem>(Transferenciaitem.class);	
	protected Set<Dadologistico> listaDadologistico = new ListSet<Dadologistico>(Dadologistico.class);
	protected List<EnderecoLinhaseparacao> listaEnderecoLinhaseparacao = new ArrayList<EnderecoLinhaseparacao>();
	
	// Transient
	protected Integer ruaI;
	protected Integer ruaF;
	protected Integer predioI;
	protected Integer predioF;
	protected Integer nivelI;
	protected Integer nivelF;
	protected Integer aptoI;
	protected Integer aptoF;
	protected Boolean lado;
	protected String estado;
	protected Endereco enderecovizinho;
	protected Boolean hasVizinhoExcedente=false;
	protected EnderecoFiltro enderecoFiltro;
	protected Integer qtde;
	protected List<Linhaseparacao> listaLinhaseparacao = new ArrayList<Linhaseparacao>();
	protected boolean bloqueado;
	
	// Para consultar enderecos indisponiveis (Transient)
	protected Integer reservaentrada;
	protected Integer reservasaida;
	

	// Construtores
	public Endereco() {

	}
	
	public Endereco(Integer cd) {
		this.cdendereco = cd;
	}
	
	public Endereco(Integer cd, String endereco) {
		this.cdendereco = cd;
		this.endereco = endereco;
	}
	
	public Endereco(Integer cdendereco, String endereco, Boolean larguraexcedenteEndereco, Area area) {
		this.cdendereco = cdendereco;
		this.endereco = endereco;
		this.larguraexcedente = larguraexcedenteEndereco;
		this.area = area;
	}
	
	public Endereco(Integer cdendereco, Area area, Integer rua, Integer predio, Integer nivel, Integer apto) {
		this.cdendereco = cdendereco;
		this.area = area;
		this.rua = rua;
		this.predio = predio;
		this.nivel = nivel;
		this.apto = apto;
	}

	public Endereco(Area area, Integer rua, Integer predio, Integer nivel, Integer apto) {
		this.area = area;
		this.rua = rua;
		this.predio = predio;
		this.nivel = nivel;
		this.apto = apto;
	}
	
	// Métodos get e set
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_endereco")
	public Integer getCdendereco() {
		return cdendereco;
	}
	
	@DisplayName("Endereço")
	@MaxLength(15)
	@DescriptionProperty
	public String getEndereco() {
		return endereco;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoendereco")
	@DisplayName("Tipo de endereço")
	public Tipoendereco getTipoendereco() {
		return tipoendereco;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdarea")
	@DisplayName("Área de armazenagem")
	public Area getArea() {
		return area;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipoestrutura")
	@DisplayName("Estrutura de armazenagem")
	public Tipoestrutura getTipoestrutura() {
		return tipoestrutura;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdenderecostatus")
	@DisplayName("Status")
	public Enderecostatus getEnderecostatus() {
		return enderecostatus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdenderecofuncao")
	@DisplayName("Característica")
	public Enderecofuncao getEnderecofuncao() {
		return enderecofuncao;
	}
	
	@DisplayName("Rua")
	@MaxValue(999)
	@MaxLength(3)
	public Integer getRua() {
		return rua;
	}
	
	@DisplayName("Prédio")
	@MaxValue(999)
	@MaxLength(3)
	public Integer getPredio() {
		return predio;
	}
	
	@DisplayName("Nível")
	@MaxValue(99)
	@MaxLength(2)
	public Integer getNivel() {
		return nivel;
	}
	
	@DisplayName("Apto")
	@MaxValue(999)
	@MaxLength(3)
	public Integer getApto() {
		return apto;
	}
	
	@DisplayName("L. excedente")	
	public Boolean getLarguraexcedente() {
		return larguraexcedente;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtipopalete")
	@DisplayName("Tipo de palete")
	public Tipopalete getTipopalete() {
		return tipopalete;
	}
	
	public Integer getPeso() {
		return peso;
	}
	
	@OneToMany(mappedBy="enderecoorigem")
	public Set<Ordemservicoprodutoendereco> getListaOrdemservicoprodutoenderecoOrigem() {
		return listaOrdemservicoprodutoenderecoOrigem;
	}
	
	@OneToMany(mappedBy="enderecodestino")
	public Set<Ordemservicoprodutoendereco> getListaOrdemservicoprodutoenderecoDestino() {
		return listaOrdemservicoprodutoenderecoDestino;
	}
	
	@OneToMany(mappedBy="endereco")
	public Set<Enderecoproduto> getListaEnderecoproduto() {
		return listaEnderecoproduto;
	}
	
	@OneToMany(mappedBy="enderecoorigem")
	public Set<Transferenciaitem> getListaTransferenciaitemenderecoOrigem() {
		return listaTransferenciaitemenderecoOrigem;
	}
	
	@OneToMany(mappedBy="enderecodestino")
	public Set<Transferenciaitem> getListatransferenciaitemenderecoDestino() {
		return listatransferenciaitemenderecoDestino;
	}
	
	@OneToMany(mappedBy="endereco")
	public Set<Dadologistico> getListaDadologistico() {
		return listaDadologistico;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdmotivobloqueio")
	public Motivobloqueio getMotivobloqueio() {
		return motivobloqueio;
	}
	
	public void setCdendereco(Integer cdendereco) {
		this.cdendereco = cdendereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public void setTipoendereco(Tipoendereco tipoendereco) {
		this.tipoendereco = tipoendereco;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public void setTipoestrutura(Tipoestrutura tipoestrutura) {
		this.tipoestrutura = tipoestrutura;
	}
	
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public void setEnderecostatus(Enderecostatus enderecostatus) {
		this.enderecostatus = enderecostatus;
	}
	
	public void setEnderecofuncao(Enderecofuncao enderecofuncao) {
		this.enderecofuncao = enderecofuncao;
	}
	
	public void setRua(Integer rua) {
		this.rua = rua;
	}

	public void setPredio(Integer predio) {
		this.predio = predio;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public void setApto(Integer apto) {
		this.apto = apto;
	}

	public void setLarguraexcedente(Boolean larguraexcedente) {
		this.larguraexcedente = larguraexcedente;
	}
	
	public void setTipopalete(Tipopalete tipopalete) {
		this.tipopalete = tipopalete;
	}
	
	public void setListaOrdemservicoprodutoenderecoOrigem(Set<Ordemservicoprodutoendereco> listaOrdemservicoprodutoenderecoOrigem) {
		this.listaOrdemservicoprodutoenderecoOrigem = listaOrdemservicoprodutoenderecoOrigem;
	}
	
	public void setListaOrdemservicoprodutoenderecoDestino(Set<Ordemservicoprodutoendereco> listaOrdemservicoprodutoenderecoDestino) {
		this.listaOrdemservicoprodutoenderecoDestino = listaOrdemservicoprodutoenderecoDestino;
	}
	
	public void setListaEnderecoproduto(Set<Enderecoproduto> listaEnderecoproduto) {
		this.listaEnderecoproduto = listaEnderecoproduto;
	}
	
	public void setListaTransferenciaitemenderecoOrigem(Set<Transferenciaitem> listaTransferenciaitemenderecoOrigem) {
		this.listaTransferenciaitemenderecoOrigem = listaTransferenciaitemenderecoOrigem;
	}
	
	public void setListatransferenciaitemenderecoDestino(Set<Transferenciaitem> listatransferenciaitemenderecoDestino) {
		this.listatransferenciaitemenderecoDestino = listatransferenciaitemenderecoDestino;
	}
	
	public void setListaDadologistico(Set<Dadologistico> listaDadologistico) {
		this.listaDadologistico = listaDadologistico;
	}
	
	// -------------------------- Métodos de variáveis transientes -------------------------------
	@Transient
	@MaxLength(3)
	@DisplayName("Rua início")
	@Deprecated
	public Integer getRuaI() {
		return ruaI;
	}
	
	@Transient
	@MaxLength(3)
	@DisplayName("Rua fim")
	@Deprecated
	public Integer getRuaF() {
		return ruaF;
	}
	
	@Transient
	@MaxLength(3)
	@DisplayName("Prédio início")
	@Deprecated
	public Integer getPredioI() {
		return predioI;
	}
	
	@Transient
	@MaxLength(3)
	@DisplayName("Prédio fim")
	@Deprecated
	public Integer getPredioF() {
		return predioF;
	}
	
	@Transient
	@MaxLength(2)
	@DisplayName("Nível início")
	@Deprecated
	public Integer getNivelI() {
		return nivelI;
	}
	
	@Transient
	@MaxLength(2)
	@DisplayName("Nível fim")
	@Deprecated
	public Integer getNivelF() {
		return nivelF;
	}
	
	@Transient
	@MaxLength(3)
	@DisplayName("Apto início")
	@Deprecated
	public Integer getAptoI() {
		return aptoI;
	}
	
	@Transient
	@MaxLength(3)
	@DisplayName("Apto fim")
	@Deprecated
	public Integer getAptoF() {
		return aptoF;
	}
	
	@Transient
	public Boolean getLado() {
		return lado;
	}
	
	@Transient
	public String getEstado() {
		return estado;
	}
	
	@Transient
	public Endereco getEnderecovizinho() {
		return enderecovizinho;
	}

	@Transient
	public Boolean getHasVizinhoExcedente() {
		return hasVizinhoExcedente;
	}
	
	public void setRuaI(Integer ruaI) {
		this.ruaI = ruaI;
	}
	
	public void setRuaF(Integer ruaF) {
		this.ruaF = ruaF;
	}
	
	public void setPredioI(Integer predioI) {
		this.predioI = predioI;
	}
	
	public void setPredioF(Integer predioF) {
		this.predioF = predioF;
	}
	
	public void setNivelI(Integer nivelI) {
		this.nivelI = nivelI;
	}
	
	public void setNivelF(Integer nivelF) {
		this.nivelF = nivelF;
	}
	
	public void setAptoI(Integer aptoI) {
		this.aptoI = aptoI;
	}
	
	public void setAptoF(Integer aptoF) {
		this.aptoF = aptoF;
	}
	
	public void setLado(Boolean lado) {
		this.lado = lado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setMotivobloqueio(Motivobloqueio motivobloqueio) {
		this.motivobloqueio = motivobloqueio;
	}
	
	/**
	 * Retorna o endereco concatenado com a área
	 * Caso ele seja blocado, retorna o endereço até o nivel de prédio
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @return
	 */
	@Transient
	public String getEnderecoArea(){
		if(area != null && area.getCodigo() != null && this.endereco != null){
			String endereco = this.endereco;
			if(this.enderecofuncao != null && this.enderecofuncao.equals(Enderecofuncao.BLOCADO))
				endereco = endereco.substring(0,7);
			if(area.getCodigo() < 10)
				return "0" + area.getCodigo()+"."+endereco;
			else
				return area.getCodigo()+"."+endereco;
		}
		return this.endereco;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Endereco other = (Endereco) obj;
		
		if (cdendereco == null && other.cdendereco == null)
			return this == obj;
		else if (cdendereco == null) {
			if (other.cdendereco != null)
				return false;
		} else if (!cdendereco.equals(other.cdendereco))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdendereco == null) ? 0 : cdendereco.hashCode());
		return result;
	}

	

	public void setEnderecovizinho(Endereco enderecovizinho) {
		this.enderecovizinho = enderecovizinho;
	}

	public void setHasVizinhoExcedente(Boolean hasVizinhoExcedente) {
		this.hasVizinhoExcedente = hasVizinhoExcedente;
	}

	@OneToMany(mappedBy="endereco")
	public List<EnderecoLinhaseparacao> getListaEnderecoLinhaseparacao() {
		return listaEnderecoLinhaseparacao;
	}

	public void setListaEnderecoLinhaseparacao(List<EnderecoLinhaseparacao> listaEnderecoLinhaseparacao) {
		this.listaEnderecoLinhaseparacao = listaEnderecoLinhaseparacao;
	}

	@Transient
	public List<Linhaseparacao> getListaLinhaseparacao() {
		return listaLinhaseparacao;
	}

	public void setListaLinhaseparacao(List<Linhaseparacao> listaLinhaseparacao) {
		this.listaLinhaseparacao = listaLinhaseparacao;
	}

	@Transient
	public EnderecoFiltro getEnderecoFiltro() {
		return enderecoFiltro;
	}
	
	public void setEnderecoFiltro(EnderecoFiltro enderecoFiltro) {
		this.enderecoFiltro = enderecoFiltro;
	}


	@Transient
	public String getListaLinhas(){
		return WmsUtil.concatenateWithLimit(getListaEnderecoLinhaseparacao(),"linhaseparacao.nome", 5);
	}

	@Transient
	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloquear) {
		this.bloqueado = bloquear;
	}
	@Transient
	public Integer getReservaentrada() {
		return reservaentrada;
	}

	public void setReservaentrada(Integer reservaentrada) {
		this.reservaentrada = reservaentrada;
	}
	@Transient
	public Integer getReservasaida() {
		return reservasaida;
	}

	public void setReservasaida(Integer reservasaida) {
		this.reservasaida = reservasaida;
	}
	@Transient
	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	
	

}
