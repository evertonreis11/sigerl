package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;

@Entity
public class Manifestostatus {
	
	public static Manifestostatus EM_ELABORACAO = new Manifestostatus(1,"Em Elaboração");
	public static Manifestostatus IMPRESSO = new Manifestostatus(2,"Impresso");
	public static Manifestostatus CANCELADO = new Manifestostatus(3,"Cancelado");
	public static Manifestostatus FINALIZADO = new Manifestostatus(4,"Finalizado");
	public static Manifestostatus ENTREGA_EM_ANDAMENTO = new Manifestostatus(5,"Entrega em Andamento");
	public static Manifestostatus PRESTACAO_CONTAS_FINALIZADO = new Manifestostatus(6,"Prest. Contas Finalizada");
	public static Manifestostatus FATURADO = new Manifestostatus(7,"Faturado");
	public static Manifestostatus AGUARANDO_PRESTACAO = new Manifestostatus(8,"Aguardando Prest. Contas");
	
	private Integer cdmanifestostatus;
	private String nome;
	
	public Manifestostatus(){
		
	}
	
	public Manifestostatus (Integer cdmanifestostatus, String nome){
		this.cdmanifestostatus = cdmanifestostatus;
		this.nome = nome;
	}
	
	@Id
	public Integer getCdmanifestostatus() {
		return cdmanifestostatus;
	}
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public void setCdmanifestostatus(Integer cdmanifestostatus) {
		this.cdmanifestostatus = cdmanifestostatus;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Manifestostatus){
			Manifestostatus ms = (Manifestostatus) obj;
			try {
				if(ms.getCdmanifestostatus().equals(this.getCdmanifestostatus()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
