package br.com.linkcom.wms.geral.bean.auxiliar;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;

public interface IMapaseparacao {

	public String getChave();
	public Carregamento getCarregamento();
	public Carregamentoitem getCarregamentoitem();
	public Tipooperacao getTipooperacao();
	public Cliente getCliente();
	public Produto getProduto();
	public Produto getProdutoprincipal();
	public Linhaseparacao getLinhaseparacao();
	public Double getPeso();
	public Long getCubagem();
	public Long getQtde();
}
