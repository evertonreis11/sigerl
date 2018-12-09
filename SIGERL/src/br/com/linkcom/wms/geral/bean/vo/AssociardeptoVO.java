package br.com.linkcom.wms.geral.bean.vo;

import br.com.linkcom.wms.geral.bean.Departamentogerenciadora;
import br.com.linkcom.wms.geral.bean.Produtoclasse;

public class AssociardeptoVO {

	private Produtoclasse produtoclasse;
	private Departamentogerenciadora departamentogerenciadora;
		
	//Get's
	public Produtoclasse getProdutoclasse() {
		return produtoclasse;
	}
	public Departamentogerenciadora getDepartamentogerenciadora() {
		return departamentogerenciadora;
	}
	
	//Set's
	public void setProdutoclasse(Produtoclasse produtoclasse) {
		this.produtoclasse = produtoclasse;
	}
	public void setDepartamentogerenciadora(Departamentogerenciadora departamentogerenciadora) {
		this.departamentogerenciadora = departamentogerenciadora;
	}
	
	
}
