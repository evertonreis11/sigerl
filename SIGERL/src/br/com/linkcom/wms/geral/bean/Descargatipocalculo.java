package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_descargatipocalculo", sequenceName = "sq_descargatipocalculo")
public class Descargatipocalculo {
	
	protected Integer cddescargatipocalculo;
	protected String nome;
	
	// constantes
	public static final Descargatipocalculo VEICULO = new Descargatipocalculo(1);
	public static final Descargatipocalculo QUANTIDADE = new Descargatipocalculo(2);
	public static final Descargatipocalculo TONELADA = new Descargatipocalculo(3);
	
	public Descargatipocalculo() {
	}
	
	public Descargatipocalculo(Integer cddescargatipocalculo) {
		this.cddescargatipocalculo = cddescargatipocalculo;
	}

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_descargatipocalculo")
	public Integer getCddescargatipocalculo() {
		return cddescargatipocalculo;
	}
	
	@DescriptionProperty
	@MaxLength(20)
	@Required
	@DisplayName("Nome")
	public String getNome() {
		return nome;
	}
	
	public void setCddescargatipocalculo(Integer cddescargatipocalculo) {
		this.cddescargatipocalculo = cddescargatipocalculo;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Descargatipocalculo) {
			Descargatipocalculo ins = (Descargatipocalculo) obj;
			return ins.getCddescargatipocalculo().equals(this.getCddescargatipocalculo());
			
		}
		
		return super.equals(obj);
	}
}