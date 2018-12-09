package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@SequenceGenerator(name = "sq_carregamentostatus", sequenceName = "sq_carregamentostatus")
public class Carregamentostatus {
	
	// constantes
	public static final Carregamentostatus MONTADO = new Carregamentostatus(1,"Montado","Carga Montada.");
	public static final Carregamentostatus EM_SEPARACAO = new Carregamentostatus(2,"Em Separação","Carga em processo de Separação.");
	public static final Carregamentostatus CONFERIDO = new Carregamentostatus(3, "Conferido","Carga Conferida.");
	public static final Carregamentostatus FINALIZADO = new Carregamentostatus(4, "Finalizado","Carga Finalizada.");
	public static final Carregamentostatus EM_MONTAGEM = new Carregamentostatus(5, "Em Montagem","Carga em processo de Montagem");
	public static final Carregamentostatus FATURADO = new Carregamentostatus(6, "Faturado","Carga Faturada");
	public static final Carregamentostatus CANCELADO = new Carregamentostatus(7, "Cancelado","Carga Cancelada.");
	public static final Carregamentostatus AGUARDANDO_SEPARACAO = new Carregamentostatus(8, "Aguardando separação","Carga Aguardando a Separação (WIS).");
	public static final Carregamentostatus PRIMEIRA_CONFERENCIA = new Carregamentostatus(9, "Primeira Conferência Feita","Primeira Conferencia concluída (WIS).");
	public static final Carregamentostatus MANIFESTO = new Carregamentostatus(10, "Manifesto","Carga Manifestada.");
	
	// variaveis de instancia
	protected Integer cdcarregamentostatus;
	protected String nome;
	protected String descricao;
	
	// construtores
	public Carregamentostatus(int cd, String nome, String descricao) {
		this.cdcarregamentostatus = cd;
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public Carregamentostatus(int cd, String nome) {
		this.cdcarregamentostatus = cd;
		this.nome = nome;
	}
	
	public Carregamentostatus(int cd) {
		this.cdcarregamentostatus = cd;
	}
	
	public Carregamentostatus() {
	}
	
	// metodos
	@Id
	@DisplayName("Id")
	@GeneratedValue(generator = "sq_carregamentostatus", strategy = GenerationType.AUTO)
	public Integer getCdcarregamentostatus() {
		return cdcarregamentostatus;
	}
	
	@MaxLength(30)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@Transient
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCdcarregamentostatus(Integer cdcarregamentostatus) {
		this.cdcarregamentostatus = cdcarregamentostatus;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Carregamentostatus) {
			Carregamentostatus carregamentostatus = (Carregamentostatus) obj;
			return carregamentostatus.getCdcarregamentostatus().equals(this.cdcarregamentostatus);
			
		}
		
		return super.equals(obj);
	}
}