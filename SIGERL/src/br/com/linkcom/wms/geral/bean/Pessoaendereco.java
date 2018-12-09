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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Cep;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_pessoaendereco", sequenceName = "sq_pessoaendereco")
public class Pessoaendereco {
	
	protected Integer cdpessoaendereco;
	protected Pessoa pessoa;
	protected String municipio;
	protected String uf;
	protected String logradouro;
	protected String bairro;
	protected Cep cep;
	protected String caixapostal;
	protected String numero;
	protected String complemento;
	protected Long codigoERP;
	protected String referencia;
	protected Set<Pedidovendaproduto> listaPedidoVendaProduto = new ListSet<Pedidovendaproduto>(Pedidovendaproduto.class);
	
	public Pessoaendereco() {

	}

	public Pessoaendereco(int cdpessoaendereco) {
		this.cdpessoaendereco = cdpessoaendereco;
	}

	public Pessoaendereco(Integer cdpessoaendereco, String logradouro, String uf, String bairro, String caixapostal, String numero, String complemento, String municipio, Long cep) {
		this.cdpessoaendereco = cdpessoaendereco;
		this.logradouro = logradouro;
		this.uf = uf;
		this.bairro = bairro;
		this.caixapostal = caixapostal;
		this.numero = numero;
		this.complemento = complemento;
		this.municipio = municipio;
		if(cep != null && cep.longValue() != 0)
			this.cep = new Cep(cep.toString());
	}

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_pessoaendereco")
	public Integer getCdpessoaendereco() {
		return cdpessoaendereco;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cdpessoa")
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	@MaxLength(50)
	@DisplayName("Município")
	public String getMunicipio() {
		return municipio;
	}
	
	@DisplayName("UF")
	@MaxLength(2)
	public String getUf() {
		return uf;
	}
	@MaxLength(100)
	public String getLogradouro() {
		return logradouro;
	}
	@MaxLength(100)
	public String getBairro() {
		return bairro;
	}
	@DisplayName("CEP")
	@MaxLength(10)
	@Required
	public Cep getCep() {
		return cep;
	}
	
	@DisplayName("Caixa postal")
	@MaxLength(20)
	public String getCaixapostal() {
		return caixapostal;
	}
	@MaxLength(10)
	@DisplayName("Número")
	public String getNumero() {
		return numero;
	}
	@MaxLength(50)
	public String getComplemento() {
		return complemento;
	}
	
	public Long getCodigoERP() {
		return codigoERP;
	}
	
	public String getReferencia() {
		return referencia;
	}
	
	@OneToMany(mappedBy="pessoaendereco")
	public Set<Pedidovendaproduto> getListaPedidoVendaProduto() {
		return listaPedidoVendaProduto;
	}
	public void setCdpessoaendereco(Integer cdpessoaendereco) {
		this.cdpessoaendereco = cdpessoaendereco;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public void setCep(Cep cep) {
		this.cep = cep;
	}
	public void setCaixapostal(String caixapostal) {
		this.caixapostal = caixapostal;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public void setCodigoERP(Long codigoERP) {
		this.codigoERP = codigoERP;
	}
	
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public void setListaPedidoVendaProduto(Set<Pedidovendaproduto> listaPedidoVendaProduto) {
		this.listaPedidoVendaProduto = listaPedidoVendaProduto;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(logradouro !=  null ? logradouro : "");
	
		if (numero != null && !numero.trim().isEmpty()){
			builder.append(", ");
			builder.append(numero);
		}
		
		if (complemento !=  null && !complemento.trim().isEmpty()){
			builder.append(", ");
			builder.append(complemento);
		}
		
		builder.append(". ");
		builder.append(bairro != null ? bairro : "");
		builder.append(". ");
		builder.append(municipio != null ? municipio : "");
		builder.append(" - ");
		builder.append(uf != null ? uf : "");
		builder.append(". CEP: ");
		builder.append(cep != null ? cep : "");
		
		return builder.toString();
	}
	
}
