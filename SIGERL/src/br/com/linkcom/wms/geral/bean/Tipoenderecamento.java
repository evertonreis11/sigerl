package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name="sq_tipoenderecamento", sequenceName="sq_tipoenderecamento")
public class Tipoenderecamento {
	
	public static final Tipoenderecamento NAO_DEFINIDO = new Tipoenderecamento(1);
	public static final Tipoenderecamento AUTOMATICO = new Tipoenderecamento(2);
	public static final Tipoenderecamento MANUAL = new Tipoenderecamento(3);
	
	protected Integer cdtipoenderecamento;
	protected String nome;
	
	public Tipoenderecamento() {
	}
	
	public Tipoenderecamento(Integer cdTipoEnderecamento) {
		this.cdtipoenderecamento = cdTipoEnderecamento;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_tipoenderecamento", strategy = GenerationType.AUTO)
	public Integer getCdtipoenderecamento() {
		return cdtipoenderecamento;
	}
	
	@MaxLength(30)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdtipoenderecamento(Integer cdtipoenderecamento) {
		this.cdtipoenderecamento = cdtipoenderecamento;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tipoenderecamento) {
			Tipoenderecamento tipoenderecamento = (Tipoenderecamento) obj;
			
			return tipoenderecamento.getCdtipoenderecamento().equals(this.getCdtipoenderecamento());
		}
		
		return super.equals(obj);
	}
	
}
