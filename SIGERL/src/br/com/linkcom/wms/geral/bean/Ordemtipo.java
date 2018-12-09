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
@SequenceGenerator(name = "sq_ordemtipo", sequenceName = "sq_ordemtipo")
public class Ordemtipo {

	protected Integer cdordemtipo;
	protected String nome;
	
	//Transient
	protected boolean checked;
	
	//Constantes
	public static final Ordemtipo CONFERENCIA_RECEBIMENTO = new Ordemtipo(1);
	public static final Ordemtipo RECONFERENCIA_RECEBIMENTO = new Ordemtipo(2);

	//Ordens de expedição
	public static final Ordemtipo MAPA_SEPARACAO = new Ordemtipo(3);
	public static final Ordemtipo CONFERENCIA_EXPEDICAO_1 = new Ordemtipo(4);
	public static final Ordemtipo CONFERENCIA_EXPEDICAO_2 = new Ordemtipo(14);
	public static final Ordemtipo RECONFERENCIA_EXPEDICAO_1 = new Ordemtipo(5);
	public static final Ordemtipo RECONFERENCIA_EXPEDICAO_2 = new Ordemtipo(16);
	public static final Ordemtipo REABASTECIMENTO_PICKING = new Ordemtipo(6);
	public static final Ordemtipo CONFERENCIA_CHECKOUT = new Ordemtipo(15);
	public static final Ordemtipo RETORNO_BOX = new Ordemtipo(17);
	
	public static final Ordemtipo TRANSFERENCIA = new Ordemtipo(7);
	public static final Ordemtipo ENDERECAMENTO_AVARIADO = new Ordemtipo(8);
	public static final Ordemtipo ENDERECAMENTO_FRACIONADO = new Ordemtipo(9);
	public static final Ordemtipo ENDERECAMENTO_PADRAO = new Ordemtipo(10);
	
	public static final Ordemtipo CONTAGEM_INVENTARIO = new Ordemtipo(11);
	public static final Ordemtipo RECONTAGEM_INVENTARIO = new Ordemtipo(12);
	public static final Ordemtipo AJUSTE_ESTOQUE = new Ordemtipo(13);
	public static final Ordemtipo ACOMPANHAMENTO_VEICULO = new Ordemtipo(18);
	
	public static final Ordemtipo MANIFESTO = new Ordemtipo(19);
	
	public Ordemtipo() {
	}
	
	public Ordemtipo(Integer cd) {
		this.cdordemtipo = cd;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ordemtipo")
	public Integer getCdordemtipo() {
		return cdordemtipo;
	}
	public void setCdordemtipo(Integer id) {
		this.cdordemtipo = id;
	}
	
	@MaxLength(30)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@Transient
	@DisplayName("Permitido")
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdordemtipo == null) ? 0 : cdordemtipo.hashCode());
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
		final Ordemtipo other = (Ordemtipo) obj;
		if (cdordemtipo == null) {
			if (other.cdordemtipo != null)
				return false;
		} else if (!cdordemtipo.equals(other.cdordemtipo))
			return false;
		return true;
	}
}
