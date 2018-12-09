package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
public class Cliente extends Pessoa {
	
	private Pessoaendereco pessoaendereco;
	private Boolean filial = false;
	private Long codigoerp;
	private List<Pedidovenda> listaPedidoVenda = new ArrayList<Pedidovenda>();
	private List<Vformacaocarga> listaFormacaoCarga = new ArrayList<Vformacaocarga>();
	private Set<Pedidovendaproduto> listaPedidoVendaProduto = new ListSet<Pedidovendaproduto>(Pedidovendaproduto.class);
	private Set<Depositofilial> listaDepositofilial = new ListSet<Depositofilial>(Depositofilial.class); 
	private Empresa empresa;

	
	//Transients
	private String clienteEndereco;
	
	public Cliente(Integer cdpessoa, String pessoanome) {
		this.cdpessoa = cdpessoa;
		this.nome = pessoanome;
	}

	public Cliente(Integer cdpessoa) {
		this.cdpessoa = cdpessoa;
	}
	
	public Cliente(){
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoaendereco")
	@DisplayName("Endereço do cliente")
	public Pessoaendereco getPessoaendereco() {
		return pessoaendereco;
	}
	@OneToMany(mappedBy="cliente")
	public List<Pedidovenda> getListaPedidoVenda() {
		return listaPedidoVenda;
	}
	@OneToMany(mappedBy="pessoa")
	public List<Vformacaocarga> getListaFormacaoCarga() {
		return listaFormacaoCarga;
	}
	@OneToMany(mappedBy="filialEntrega")
	public Set<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}
	@MaxLength(20)
	public Long getCodigoerp() {
		return codigoerp;
	}
	public Boolean getFilial() {
		return filial;
	}
	@Transient
	public String getClienteEndereco() {
		return clienteEndereco;
	}
	@OneToMany(mappedBy="filial")
	public Set<Depositofilial> getListaDepositofilial() {
		return listaDepositofilial;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdempresa")
	public Empresa getEmpresa() {
		return empresa;
	}

	
	
	public void setPessoaendereco(Pessoaendereco pessoaendereco) {
		this.pessoaendereco = pessoaendereco;
	}
	public void setListaPedidoVenda(List<Pedidovenda> listaPedidoVenda) {
		this.listaPedidoVenda = listaPedidoVenda;
	}
	public void setListaFormacaoCarga(List<Vformacaocarga> listaFormacaoCarga) {
		this.listaFormacaoCarga = listaFormacaoCarga;
	}
	public void setListaPedidoVendaProduto(Set<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}
	public void setFilial(Boolean filial) {
		this.filial = filial;
	}
	public void setCodigoerp(Long codigoerp) {
		this.codigoerp = codigoerp;
	}
	public void setClienteEndereco(String clienteEndereco) {
		this.clienteEndereco = clienteEndereco;
	}
	public void setListaDepositofilial(Set<Depositofilial> listaDepositofilial) {
		this.listaDepositofilial = listaDepositofilial;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	
}
