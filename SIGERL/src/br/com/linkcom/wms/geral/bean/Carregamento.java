package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_carregamento", sequenceName = "sq_carregamento")
@NamedNativeQueries(value={
	@NamedNativeQuery(name="gerar_reabastecimento", query="{ call GERAR_REABASTECIMENTO(?) }", resultSetMapping="scalar"),
	@NamedNativeQuery(name="gerar_reabastecimento_box", query="{ call GERAR_REABASTECIMENTO_BOX(?) }", resultSetMapping="scalar"),
	@NamedNativeQuery(name="baixar_estoque_carregamento", query="{ call BAIXAR_ESTOQUE_CARREGAMENTO(?) }", resultSetMapping="scalar"),
	@NamedNativeQuery(name="enderecar_mapaseparacao", query="{ call ENDERECAR_MAPASSEPARACAO(?) }", resultSetMapping="scalar")
})

@SqlResultSetMapping(name="scalar")
public class Carregamento {

	protected Integer cdcarregamento;
	protected Deposito deposito;
	protected Transportador transportador;
	protected Motorista motorista;
	protected Veiculo veiculo;
	protected Integer paletesdisponiveis;
	protected Box box;
	protected Carregamentostatus carregamentostatus;	
	protected Timestamp dtcarregamento;
	protected Timestamp dtfimcarregamento;
	protected String listaRota;
	protected List<Carregamentoitem> listaCarregamentoitem;
	protected Set<Ordemservico> listaOrdemservico = new ListSet<Ordemservico>(Ordemservico.class);
	protected Set<Carregamentohistorico> listaCarregamentohistorico = new ListSet<Carregamentohistorico>(Carregamentohistorico.class);
	
	protected Cliente filialretirada;
	protected Tipooperacao tipooperacaoretirada;
	protected Expedicao expedicao;
	
	protected Integer cdusuariocancela;
	protected Integer cdusuariogera; 
	protected Timestamp dtcancela;
	
	protected Boolean prioridade = Boolean.FALSE;
	protected Tipocarregamento tipocarregamento;
	protected String observacao;
	
	//Transientes
	protected Boolean houveConflitos = false;
	protected String listaNotasFiscais;
	protected Long qtdeItens;
	protected List<Ordemservico> listaConferencias = new ListSet<Ordemservico>(Ordemservico.class);
	
	public Carregamento(Integer cd) {
		this.cdcarregamento = cd;
	}
	
	public Carregamento() {
	}
	
	@Id
	@DescriptionProperty
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cddeposito")
	@Required
	public Deposito getDeposito() {
		return deposito;
	}

	@DisplayName("Transportador")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdtransportador")
	public Transportador getTransportador() {
		return transportador;
	}

	@DisplayName("Motorista")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdmotorista")
	public Motorista getMotorista() {
		return motorista;
	}

	@DisplayName("Veículo")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdveiculo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	@MaxLength(10)
	@DisplayName("Paletes disponíveis")
	public Integer getPaletesdisponiveis() {
		return paletesdisponiveis;
	}
	
	@DisplayName("Box")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdbox")
	@Required
	public Box getBox() {
		return box;
	}
	
	@DisplayName("Status")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdcarregamentostatus")
	@Required
	public Carregamentostatus getCarregamentostatus() {
		return carregamentostatus;
	}
	
	@OneToMany(mappedBy="carregamento")
	@IndexColumn(name="cdcarregamentoitem")
	@CollectionOfElements
	public List<Carregamentoitem> getListaCarregamentoitem() {
		return listaCarregamentoitem;
	}
	
	@OneToMany(mappedBy="carregamento")
	public Set<Ordemservico> getListaOrdemservico() {
		return listaOrdemservico;
	}
	
	@OneToMany(mappedBy="carregamento")
	public Set<Carregamentohistorico> getListaCarregamentohistorico() {
		return listaCarregamentohistorico;
	}

	public void setCdcarregamento(Integer id) {
		this.cdcarregamento = id;
	}
	
	@DisplayName("Montada em")
	public Timestamp getDtcarregamento() {
		return dtcarregamento;
	}
	
	@DisplayName("Finalizada em")
	public Timestamp getDtfimcarregamento() {
		return dtfimcarregamento;
	}

	@DisplayName("Rotas")
	@Column(insertable=false,updatable=false)
	public String getListaRota() {
		return listaRota;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdfilialretirada")
	public Cliente getFilialretirada() {
		return filialretirada;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdexpedicao")
	public Expedicao getExpedicao() {
		return expedicao;
	}
	
	public void setExpedicao(Expedicao expedicao) {
		this.expedicao = expedicao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipooperacaoretirada")
	public Tipooperacao getTipooperacaoretirada() {
		return tipooperacaoretirada;
	}

	public Boolean getPrioridade() {
		return prioridade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipocarregamento")
	@Required
	public Tipocarregamento getTipocarregamento() {
		return tipocarregamento;
	}

	@DisplayName("Observação")
	public String getObservacao() {
		return observacao;
	}

	@Transient
	public Boolean getHouveConflitos() {
		return houveConflitos;
	}
	
	@Transient
	public String getListaNotasFiscais() {
		return listaNotasFiscais;
	}
	@Transient
	public Long getQtdeItens() {
		return qtdeItens;
	}

	public void setQtdeItens(Long qtdeItens) {
		this.qtdeItens = qtdeItens;
	}
	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public void setPaletesdisponiveis(Integer paletesdisponiveis) {
		this.paletesdisponiveis = paletesdisponiveis;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public void setCarregamentostatus(Carregamentostatus carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	
	public void setListaCarregamentoitem(List<Carregamentoitem> listaCarregamentoitem) {
		this.listaCarregamentoitem = listaCarregamentoitem;
	}
	
	public void setListaOrdemservico(Set<Ordemservico> listaOrdemservico) {
		this.listaOrdemservico = listaOrdemservico;
	}
	
	public void setDtcarregamento(Timestamp dtcarregamento) {
		this.dtcarregamento = dtcarregamento;
	}

	public void setDtfimcarregamento(Timestamp dtfimcarregamento) {
		this.dtfimcarregamento = dtfimcarregamento;
	}
	
	public void setHouveConflitos(Boolean houveConflitos) {
		this.houveConflitos = houveConflitos;
	}
	
	public void setListaNotasFiscais(String listaNotasFiscais) {
		this.listaNotasFiscais = listaNotasFiscais;
	}
	
	public void setFilialretirada(Cliente filialretirada) {
		this.filialretirada = filialretirada;
	}

	public void setTipooperacaoretirada(Tipooperacao tipooperacaoretirada) {
		this.tipooperacaoretirada = tipooperacaoretirada;
	}

	public void setListaRota(String listaRota) {
		this.listaRota = listaRota;
	}
	
	public void setPrioridade(Boolean prioridade) {
		this.prioridade = prioridade;
	}

	public void setTipocarregamento(Tipocarregamento tipocarregamento) {
		this.tipocarregamento = tipocarregamento;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	

	@Transient
	public List<Ordemservico> getListaConferencias() {
		return listaConferencias;
	}
	
	public void setListaConferencias(List<Ordemservico> listaConferencias) {
		this.listaConferencias = listaConferencias;
	}
	
	public void setCdusuariocancela(Integer cdusuariocancela) {
		this.cdusuariocancela = cdusuariocancela;
	}
	
	public void setCdusuariogera(Integer cdusuariogera) {
		this.cdusuariogera = cdusuariogera;
	}
	public Integer getCdusuariocancela() {
		return cdusuariocancela;
	}
	
	public Integer getCdusuariogera() {
		return cdusuariogera;
	}
	
	public void setDtcancela(Timestamp dtcancela) {
		this.dtcancela = dtcancela;
	}
	
	public Timestamp getDtcancela() {
		return dtcancela;
	}
	
	public void setListaCarregamentohistorico(Set<Carregamentohistorico> listaCarregamentohistorico) {
		this.listaCarregamentohistorico = listaCarregamentohistorico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdcarregamento == null) ? 0 : cdcarregamento.hashCode());
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
		final Carregamento other = (Carregamento) obj;
		if (cdcarregamento == null) {
			if (other.cdcarregamento != null)
				return false;
		} else if (!cdcarregamento.equals(other.cdcarregamento))
			return false;
		return true;
	}
	
	
}
