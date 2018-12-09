package br.com.linkcom.wms.modulo.expedicao.controller.process.filtro;

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentostatus;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Motorista;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Produtoclasse;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Transportador;
import br.com.linkcom.wms.geral.bean.Turnodeentrega;
import br.com.linkcom.wms.geral.bean.Veiculo;

public class CarregamentoFiltro extends FiltroListagem {

	public static enum TipoTroca {SIM, NAO, TODOS};
	public static enum TipoCorte {TODOS, SIM, NAO};
	
	protected List<Rota> rotas;
	protected Praca praca;
	protected Tipooperacao tipooperacao;
	protected String nomecliente;
	protected String pedido;
	protected Date dtentregainicio;
	protected Date dtentregafim;	
	protected Date dtmontageminicial;
	protected Date dtmontagemfinal;
	protected Date dtfinalizadainicial;
	protected Date dtfinalizadafinal;
	protected Date datavendainicial;
	protected Date datavendafinal;
	protected TipoTroca troca;
	protected TipoCorte corte;
	protected Turnodeentrega turnodeentrega;
	protected Produto produto;
	protected Produtoclasse produtoclasse;	
	protected Motorista motorista;
	
	protected Integer expandirAte;
	protected Integer paletesdisponiveis;
	protected Carregamentostatus carregamentostatus;
	
	//dados do veiculo
	protected Transportador transportador;
	protected Veiculo veiculo;
	protected Boolean somenteDisponiveis;
	
	//dados expedição
	protected Box box;
	
	protected Carregamento carregamento;
	protected Integer cdcarregamento;
	protected Integer cdexpedicao;
	
	protected Cliente filialretirada;
	protected Tipooperacao tipooperacaoretirada;
	protected Boolean trocarlocal = false;
	
	protected String itensRemovidos;
	protected boolean validarConcorrencia = false;
	
	public List<Rota> getRotas() {
		return rotas;
	}
	
	@DisplayName("Praça")
	public Praca getPraca() {
		return praca;
	}
	
	@DisplayName("Tipo de pedido")
	public Tipooperacao getTipooperacao() {
		return tipooperacao;
	}
	
	@MaxLength(50)
	@DisplayName("Cliente/Filial")
	public String getNomecliente() {
		return nomecliente;
	}
	
	@MaxLength(18)
	public String getPedido() {
		return pedido;
	}
	
	public Date getDtentregainicio() {
		return dtentregainicio;
	}

	public Date getDtentregafim() {
		return dtentregafim;
	}
	
	@DisplayName("Expandir até")
	public Integer getExpandirAte() {
		return expandirAte;
	}
	
	@DisplayName("Transportador")
	public Transportador getTransportador() {
		return transportador;
	}
	
	@DisplayName("Veículo")
	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	@DisplayName("Somente disponíveis")
	public Boolean getSomenteDisponiveis() {
		return somenteDisponiveis;
	}

	public Carregamento getCarregamento() {
		return carregamento;
	}
	
	@DisplayName("Número da expedição")
	public Integer getCdexpedicao() {
		return cdexpedicao;
	}
	
	@DisplayName("Status do carregamento")
	public Carregamentostatus getCarregamentostatus() {
		return carregamentostatus;
	}
	
	public Box getBox() {
		return box;
	}
	
	@DisplayName("Número do carregamento")
	public Integer getCdcarregamento() {
		return cdcarregamento;
	}
	
	public Date getDtmontageminicial() {
		return dtmontageminicial;
	}

	public Date getDtmontagemfinal() {
		return dtmontagemfinal;
	}

	public Date getDtfinalizadainicial() {
		return dtfinalizadainicial;
	}

	public Date getDtfinalizadafinal() {
		return dtfinalizadafinal;
	}
	
	@DisplayName("Turno")
	public Turnodeentrega getTurnodeentrega() {
		return turnodeentrega;
	}
	
	@DisplayName("Pedidos de troca")
	public TipoTroca getTroca() {
		return troca;
	}
	
	public TipoCorte getCorte() {
		return corte;
	}

	@MaxLength(5)
	public Integer getPaletesdisponiveis() {
		return paletesdisponiveis;
	}

	public Date getDatavendainicial() {
		return datavendainicial;
	}

	public void setDatavendainicial(Date datavendainicial) {
		this.datavendainicial = datavendainicial;
	}

	public Date getDatavendafinal() {
		return datavendafinal;
	}

	public Produto getProduto() {
		return produto;
	}

	@DisplayName("Classe do Produto")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}

	public void setDatavendafinal(Date datavendafinal) {
		this.datavendafinal = datavendafinal;
	}

	public void setRotas(List<Rota> rotas) {
		this.rotas = rotas;
	}
	
	public void setPraca(Praca praca) {
		this.praca = praca;
	}
	
	public void setTipooperacao(Tipooperacao tipooperacao) {
		this.tipooperacao = tipooperacao;
	}
	
	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}
	
	public void setPedido(String pedido) {
		this.pedido = pedido;
	}
	
	public void setDtentregainicio(Date dtentregainicio) {
		this.dtentregainicio = dtentregainicio;
	}
	
	public void setDtentregafim(Date dtentregafim) {
		this.dtentregafim = dtentregafim;
	}
	
	public void setExpandirAte(Integer expandirAte) {
		this.expandirAte = expandirAte;
	}
	
	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}
	
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}
	
	public void setCdexpedicao(Integer cdexpedicao) {
		this.cdexpedicao = cdexpedicao;
	}

	public void setSomenteDisponiveis(Boolean somenteDisponiveis) {
		this.somenteDisponiveis = somenteDisponiveis;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public void setCarregamentostatus(Carregamentostatus carregamentostatus) {
		this.carregamentostatus = carregamentostatus;
	}
	
	public void setCdcarregamento(Integer cdcarregamento) {
		this.cdcarregamento = cdcarregamento;
	}
	
	public void setDtmontageminicial(Date dtmontageminicial) {
		this.dtmontageminicial = dtmontageminicial;
	}

	public void setDtmontagemfinal(Date dtmontagemfinal) {
		this.dtmontagemfinal = dtmontagemfinal;
	}

	public void setDtfinalizadainicial(Date dtfinalizadainicial) {
		this.dtfinalizadainicial = dtfinalizadainicial;
	}

	public void setDtfinalizadafinal(Date dtfinalizadafinal) {
		this.dtfinalizadafinal = dtfinalizadafinal;
	}
	
	public void setTroca(TipoTroca troca) {
		this.troca = troca;
	}
	
	public void setCorte(TipoCorte corte) {
		this.corte = corte;
	}

	public void setPaletesdisponiveis(Integer paletesdisponiveis) {
		this.paletesdisponiveis = paletesdisponiveis;
	}
	
	@DisplayName("Trocar Local Retirada")
	public Boolean getTrocarlocal() {
		return trocarlocal;
	}
	public void setTrocarlocal(Boolean trocarlocal) {
		this.trocarlocal = trocarlocal;
	}

	@DisplayName("Filial de retirada")
	public Cliente getFilialretirada() {
		return filialretirada;
	}

	@DisplayName("Tipo de pedido")
	public Tipooperacao getTipooperacaoretirada() {
		return tipooperacaoretirada;
	}

	public void setFilialretirada(Cliente filialretirada) {
		this.filialretirada = filialretirada;
	}

	public void setTipooperacaoretirada(Tipooperacao tipooperacaoretirada) {
		this.tipooperacaoretirada = tipooperacaoretirada;
	}

	public String getItensRemovidos() {
		return itensRemovidos;
	}

	public void setItensRemovidos(String itensRemovidos) {
		this.itensRemovidos = itensRemovidos;
	}

	public boolean isValidarConcorrencia() {
		return validarConcorrencia;
	}

	public void setValidarConcorrencia(boolean validarConcorrencia) {
		this.validarConcorrencia = validarConcorrencia;
	}

	public void setTurnodeentrega(Turnodeentrega turnodeentrega) {
		this.turnodeentrega = turnodeentrega;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	
}