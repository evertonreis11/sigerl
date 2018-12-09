package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;


@Entity
@Table(name="statusrav")
public class Acompanhamentoveiculostatus {
	
	public static final Acompanhamentoveiculostatus GERADO = new Acompanhamentoveiculostatus(0,"Rav Gerado","RAV cadastrado.");  
	public static final Acompanhamentoveiculostatus ENTRADA = new Acompanhamentoveiculostatus(1,"Entrada Portaria","Entrada autorizada.");  
	public static final Acompanhamentoveiculostatus RECEBIMENTO_GERADO = new Acompanhamentoveiculostatus(2,"Recebimento Gerado","Recebimento vinculado ao RAV.");
	public static final Acompanhamentoveiculostatus RECEBIMENTO_FINALIZADO = new Acompanhamentoveiculostatus(3,"Recebimento Finalizado","Recebimento Finalizado.");
	public static final Acompanhamentoveiculostatus SAIDA = new Acompanhamentoveiculostatus(4,"Saída Portaria","Saída autorizada.");
	public static final Acompanhamentoveiculostatus LIBERADO = new Acompanhamentoveiculostatus(5,"Veículo Liberado","Liberação confirmada.");
	public static final Acompanhamentoveiculostatus AGUARDANDO_SENHA = new Acompanhamentoveiculostatus(6,"Rav aguardando senha","Rav aguardando a senha (agenda).");
	public static final Acompanhamentoveiculostatus CANCELADO = new Acompanhamentoveiculostatus(7,"Rav Cancelada","Rav cancelada devido a dias de inatividade.");
	

	private Integer cdstatusrav;
	private String nome;
	private String descricao;
	
	public Acompanhamentoveiculostatus(){}
	
	public Acompanhamentoveiculostatus(Integer cdstatusrav){
		if(cdstatusrav==0){
			this.cdstatusrav = 0;
			this.nome = "Rav Gerado";
			this.descricao = "RAV cadastrado.";  
		}else if(cdstatusrav==1){
			this.cdstatusrav = 1;
			this.nome = "Entrada Portaria";
			this.descricao = "Entrada autorizada.";
		}else if(cdstatusrav==2){
			this.cdstatusrav = 2;
			this.nome = "Recebimento Gerado";
			this.descricao = "Recebimento vinculado ao RAV.";
		}else if(cdstatusrav==3){
			this.cdstatusrav = 3;
			this.nome = "Recebimento Finalizado";
			this.descricao = "Recebimento Finalizado.";
		}else if(cdstatusrav==4){
			this.cdstatusrav = 4;
			this.nome = "Saída Portaria";
			this.descricao = "Saída autorizada.";
		}else if(cdstatusrav==5){
			this.cdstatusrav = 5;
			this.nome = "Veículo Liberado";
			this.descricao = "Liberação confirmada.";
		}else if(cdstatusrav==7){
			this.cdstatusrav = 7;
			this.nome = "Rav Cancelada";
			this.descricao = "Rav cancelada devido a 3 dias de inatividade.";
		}
	}
	
	public Acompanhamentoveiculostatus(Integer cdstatusrav, String nome, String descricao) {
		this.cdstatusrav = cdstatusrav;
		this.nome = nome;
		this.descricao = descricao;
	}
	
	@Id
	public Integer getCdstatusrav() {
		return cdstatusrav;
	}
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
	public void setCdstatusrav(Integer cdstatusrav) {
		this.cdstatusrav = cdstatusrav;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Acompanhamentoveiculostatus){
			Acompanhamentoveiculostatus avs = (Acompanhamentoveiculostatus) obj;
			try {
				if(avs.getCdstatusrav().equals(this.getCdstatusrav()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
