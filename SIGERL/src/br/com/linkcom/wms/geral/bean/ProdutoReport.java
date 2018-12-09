package br.com.linkcom.wms.geral.bean;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.types.GenericBean;

public class ProdutoReport {
	protected String descricao;
	protected String codigo;
	protected List<GenericBean> campos = new ArrayList<GenericBean>();
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public List<GenericBean> getCampos() {
		return campos;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public void setCampos(List<GenericBean> campos) {
		this.campos = campos;
	}
	
	public void addField(String campo){
		GenericBean bean = new GenericBean();
		bean.setId(campo);
		campos.add(bean);
	}
}
