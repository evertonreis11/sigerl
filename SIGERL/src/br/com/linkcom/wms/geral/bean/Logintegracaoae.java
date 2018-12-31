package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_logintegracaoae", sequenceName = "sq_logintegracaoae")
public class Logintegracaoae {

	private Integer cdlogintegracaoae;
	private Manifesto manifesto;
	private Integer cderro;
	private String dserro;
	private Integer cdae;
	
	public Logintegracaoae(Integer cdae, Manifesto manifesto, String dserro, Integer cderro) {

		this.cdae = cdae;
		this.manifesto = manifesto;
		this.dserro = dserro;
		this.cderro = cderro;

	}
	
	
	public Logintegracaoae() {}


	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_logintegracaoae")
	public Integer getCdlogintegracaoae() {
		return cdlogintegracaoae;
	}
    @DisplayName("Manifesto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifesto")	
	public Manifesto getManifesto() {
		return manifesto;
	}
	public Integer getCderro() {
		return cderro;
	}
	public String getDserro() {
		return dserro;
	}
	public Integer getCdae() {
		return cdae;
	}
	
	
	//Set's
	public void setCdlogintegracaoae(Integer cdlogintegracaoae) {
		this.cdlogintegracaoae = cdlogintegracaoae;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	public void setCderro(Integer cderro) {
		this.cderro = cderro;
	}
	public void setDserro(String dserro) {
		this.dserro = dserro;
	}
	public void setCdae(Integer cdae) {
		this.cdae = cdae;
	}
	
}
