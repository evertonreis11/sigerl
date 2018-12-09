package br.com.linkcom.wms.geral.filter;

import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecolado;
import br.com.linkcom.wms.geral.bean.Produto;

public interface IntervaloEndereco {

	Area getArea();

	Integer getRuainicial();

	Integer getRuafinal();

	Integer getPredioinicial();

	Integer getPrediofinal();

	Integer getNivelinicial();

	Integer getNivelfinal();

	Integer getAptoinicial();

	Integer getAptofinal();

	Enderecolado getEnderecolado();

	Enderecofuncao getEnderecofuncao();

	Produto getProduto();

	String getCodigo();

	void setProduto(Produto produto);
	
}
