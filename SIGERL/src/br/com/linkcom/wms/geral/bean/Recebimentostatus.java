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
@SequenceGenerator(name = "sq_recebimentostatus", sequenceName = "sq_recebimentostatus")
public class Recebimentostatus {

	protected Integer cdrecebimentostatus;
	protected String nome;
	
	
	//constantes
	public static final Recebimentostatus DISPONIVEL = new Recebimentostatus(1);
	public static final Recebimentostatus EM_ANDAMENTO = new Recebimentostatus(2);
	public static final Recebimentostatus CONCLUIDO = new Recebimentostatus(3);
	public static final Recebimentostatus CONCLUIDO_COM_DIVERGENCIAS = new Recebimentostatus(4);
	public static final Recebimentostatus CANCELADO = new Recebimentostatus(5);
	public static final Recebimentostatus REJEITADO = new Recebimentostatus(6);
	public static final Recebimentostatus EM_ENDERECAMENTO = new Recebimentostatus(7);
	public static final Recebimentostatus ENDERECADO = new Recebimentostatus(8);
	public static final Recebimentostatus AGUARDANDO_CONFERENCIA = new Recebimentostatus(9);

	public Recebimentostatus() {
		
	}
	
	public Recebimentostatus(Integer cdrecebimentostatus) {
		this.cdrecebimentostatus = cdrecebimentostatus;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_recebimentostatus")
	public Integer getCdrecebimentostatus() {
		return cdrecebimentostatus;
	}
	public void setCdrecebimentostatus(Integer id) {
		this.cdrecebimentostatus = id;
	}

	
	@Required
	@MaxLength(30)
	@DescriptionProperty
	@DisplayName("Situação")
	public String getNome() {
		return nome;
	}

	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Recebimentostatus) {
			Recebimentostatus recebimentostatus = (Recebimentostatus) obj;
			return recebimentostatus.getCdrecebimentostatus().equals(getCdrecebimentostatus());
		}
		return super.equals(obj);
	}

}
