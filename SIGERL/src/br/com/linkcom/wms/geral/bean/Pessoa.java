package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.Cnpj;
import br.com.linkcom.neo.types.Cpf;
import br.com.linkcom.neo.types.Telefone;
import br.com.linkcom.neo.validation.annotation.Email;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "sq_pessoa", sequenceName = "sq_pessoa")
public class Pessoa {
	// ----------------------- variaveis de instancia -------------------
	protected Integer cdpessoa;
	protected String nome;
	protected String documento;
	protected Pessoanatureza pessoanatureza;
	protected Boolean ativo;
	protected Telefone telefone;
	protected String email;
	protected List<Pessoaendereco> listaPessoaEndereco = new ArrayList<Pessoaendereco>();

	// ---------------------- transientes --------------------------------
	protected List<Tipopessoa> listatipo;
	protected Cpf cpf;
	protected Cnpj cnpj;
	protected List<Usuario> listausuario;
	protected String tipos;

	protected Fornecedor fornecedor;
	protected Fabricante fabricante;
	protected Transportador transportador;

	// --------------------- construtores ---------------------------------
	public Pessoa() {

	}

	public Pessoa(Integer cd) {
		this.cdpessoa = cd;
	}

	public Pessoa(Integer cd, String nome) {
		this.cdpessoa = cd;
		this.nome = nome;
	}

	// -------------------- metodos get e set ----------------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_pessoa")
	public Integer getCdpessoa() {
		return cdpessoa;
	}

	@MaxLength(100)
	@DescriptionProperty
	@Required
	public String getNome() {
		return nome;
	}

	@MaxLength(15)
	public String getDocumento() {
		return documento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdpessoanatureza")
	@DisplayName("Natureza")
	@Required
	public Pessoanatureza getPessoanatureza() {
		return pessoanatureza;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	@Email
	@DisplayName("E-mail")
	@MaxLength(100)
	public String getEmail() {
		return email;
	}

	@DisplayName("Tipo de pessoa")
	@Transient
	public List<Tipopessoa> getListatipo() {
		return listatipo;
	}

	@Transient
	@DisplayName("CPF")
	public Cpf getCpf() {
		return documento != null && documento.length() == 11 ? new Cpf(
				documento) : cpf;
	}

	@Transient
	@Required
	@DisplayName("CNPJ")
	public Cnpj getCnpj() {
		return documento != null && documento.length() == 14 ? new Cnpj(
				documento) : cnpj;
	}

	@Transient
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	@Transient
	public Fabricante getFabricante() {
		return fabricante;
	}

	@Transient
	public Transportador getTransportador() {
		return transportador;
	}

	@Transient
	public List<Usuario> getListausuario() {
		return listausuario;
	}

	@DisplayName("Ativo")
	public Boolean getAtivo() {
		return ativo;
	}

	@OneToMany(mappedBy = "pessoa")
	public List<Pessoaendereco> getListaPessoaEndereco() {
		return listaPessoaEndereco;
	}

	@Transient
	@DisplayName("Tipo")
	public String getTipos() {
		return tipos;
	}

	public void setCdpessoa(Integer id) {
		this.cdpessoa = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setPessoanatureza(Pessoanatureza pessoanatureza) {
		this.pessoanatureza = pessoanatureza;
	}

	public void setListatipo(List<Tipopessoa> listatipo) {
		this.listatipo = listatipo;
	}

	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	public void setCnpj(Cnpj cnpj) {
		this.cnpj = cnpj;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setListausuario(List<Usuario> listausuario) {
		this.listausuario = listausuario;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public void setTransportador(Transportador transportador) {
		this.transportador = transportador;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public void setListaPessoaEndereco(List<Pessoaendereco> listaPessoaEndereco) {
		this.listaPessoaEndereco = listaPessoaEndereco;
	}

	public void setTipos(String tipos) {
		this.tipos = tipos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdpessoa == null) ? 0 : cdpessoa.hashCode());
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
		final Pessoa other = (Pessoa) obj;

		if (cdpessoa == null && other.cdpessoa == null)
			return this == other;
		else if (cdpessoa == null) {
			if (other.cdpessoa != null)
				return false;
		} else if (!cdpessoa.equals(other.cdpessoa))
			return false;
		return true;
	}

}
