package br.com.linkcom.wms.geral.bean.auxiliar;

import br.com.linkcom.wms.geral.bean.Box;
import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Carregamentoitem;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.Tipooperacao;

public interface IOrdemexpedicaobox {

	public String getChave();
	public Box getBox();
	public Cliente getCliente();
	public Carregamento getCarregamento();
	public Tipooperacao getTipooperacao();
	public Deposito getDeposito();
	public Produto getProdutoprincipal();
	public Produto getVolume();
	public Carregamentoitem getCarregamentoitem();
	public Linhaseparacao getLinhaseparacao();
	public Long getQtde();
	
}
