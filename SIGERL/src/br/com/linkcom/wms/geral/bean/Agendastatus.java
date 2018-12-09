package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

/**
 * 
 * @author Arantes
 *
 */
@Entity
@SequenceGenerator(name = "sq_agendastatus", sequenceName = "sq_agendastatus")
public class Agendastatus {
	// ---------------------------- constantes ----------------------------
	public static final Agendastatus AGENDADO = new Agendastatus(1);
	public static final Agendastatus FINALIZADO = new Agendastatus(2);
	public static final Agendastatus CANCELADO = new Agendastatus(3);
	
	// ---------------------------- variáveis de instância ----------------------------
	protected Integer cdagendastatus;
	protected String nome;
	
	// ---------------------------- construtores ----------------------------
	public Agendastatus() {
		
	}
	
	public Agendastatus(Integer cd) {
		this.cdagendastatus = cd;
	}

	// ---------------------------- métodos get´s e set´s ----------------------------
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_agendastatus")
	public Integer getCdagendastatus() {
		return cdagendastatus;
	}

	@DescriptionProperty
	@DisplayName("nome")
	@MaxLength(20)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setCdagendastatus(Integer cdagendastatus) {
		this.cdagendastatus = cdagendastatus;
	}
	
	// ---------------------------- métodos ----------------------------
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Agendastatus) { 
			Agendastatus agendastatus = (Agendastatus) obj;
			
			return agendastatus.getCdagendastatus().equals(this.cdagendastatus);
		}
		
		return super.equals(obj);
	}
}
