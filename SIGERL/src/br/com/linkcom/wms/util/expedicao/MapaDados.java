package br.com.linkcom.wms.util.expedicao;

import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.util.logistica.EnderecoAux;

public class MapaDados {	
		
	private Produto produto;
	private Long qtde;
	private EnderecoAux endereco;
	
	
	
	public Produto getProduto() {
		return produto;
	}
	public Long getQtde() {
		return qtde;
	}
	public EnderecoAux getEndereco() {
		return endereco;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public void setEndereco(EnderecoAux endereco) {
		this.endereco = endereco;
	}
	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
	

}
