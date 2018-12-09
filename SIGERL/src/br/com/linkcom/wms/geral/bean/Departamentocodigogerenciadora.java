package br.com.linkcom.wms.geral.bean;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@Table(name="DEPTO_X_CODGERENCIADORA")
@SequenceGenerator(name = "sq_depto_x_codgerenciadora", sequenceName = "sq_depto_x_codgerenciadora")
public class Departamentocodigogerenciadora {

	private Integer cddepto;
	private Produtoclasse produtoclasse;
	private Departamentogerenciadora departamentogerenciadora;
	private String descdepto;
	private String descdeptogerenciadora;
	private Date dtinclusao;
	private Date dtalteracao;
	
	private List<Departamentocodigogerenciadora> listaDepartamentocodigogerenciadora;
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_depto_x_codgerenciadora")
	public Integer getCddepto() {
		return cddepto;
	}
    @DisplayName("Classe do Produto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdprodutoclasse")
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}
    @DisplayName("Depto. Gerenciadora")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cddeptogerenciadora")    
	public Departamentogerenciadora getDepartamentogerenciadora() {
		return departamentogerenciadora;
	}
	public String getDescdepto() {
		return descdepto;
	}
	public String getDescdeptogerenciadora() {
		return descdeptogerenciadora;
	}
	public Date getDtinclusao() {
		return dtinclusao;
	}
	public Date getDtalteracao() {
		return dtalteracao;
	}
	
	
	//Set's
	public void setCddepto(Integer cddepto) {
		this.cddepto = cddepto;
	}
	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}
	public void setDepartamentogerenciadora(Departamentogerenciadora departamentogerenciadora) {
		this.departamentogerenciadora = departamentogerenciadora;
	}
	public void setDescdepto(String descdepto) {
		this.descdepto = descdepto;
	}
	public void setDescdeptogerenciadora(String descdeptogerenciadora) {
		this.descdeptogerenciadora = descdeptogerenciadora;
	}
	public void setDtinclusao(Date dtinclusao) {
		this.dtinclusao = dtinclusao;
	}
	public void setDtalteracao(Date dtalteracao) {
		this.dtalteracao = dtalteracao;
	}
	
	
	//Transient's
	@Transient
	public List<Departamentocodigogerenciadora> getListaDepartamentocodigogerenciadora() {
		return listaDepartamentocodigogerenciadora;
	}
	public void setListaDepartamentocodigogerenciadora(List<Departamentocodigogerenciadora> listaDepartamentocodigogerenciadora) {
		this.listaDepartamentocodigogerenciadora = listaDepartamentocodigogerenciadora;
	}	
	
}
