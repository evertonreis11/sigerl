package br.com.linkcom.wms.geral.bean.view;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Immutable;

import br.com.linkcom.wms.geral.bean.Produtoclasse;


@Entity
@Immutable
public class V_produtoclasse {

	private Integer cdprodutoclasse;
	private Produtoclasse produtoclasse;
	
	@Id
	public Integer getCdprodutoclasse() {
		return cdprodutoclasse;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cdclasse", updatable=false, insertable=false)
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}

	public void setCdprodutoclasse(Integer cdprodutoclasse) {
		this.cdprodutoclasse = cdprodutoclasse;
	}

	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}
	
}
