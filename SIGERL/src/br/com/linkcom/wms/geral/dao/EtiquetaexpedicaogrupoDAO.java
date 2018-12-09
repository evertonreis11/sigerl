package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Etiquetaexpedicao;
import br.com.linkcom.wms.geral.bean.Etiquetaexpedicaogrupo;
import br.com.linkcom.wms.util.WmsException;

public class EtiquetaexpedicaogrupoDAO extends
		br.com.linkcom.wms.util.neo.persistence.GenericDAO<Etiquetaexpedicaogrupo> {

	/**
	 * M�todo que carrega a lista de etiqueta expedi��o de uma etiqueta expedi��o
	 * 
	 * @param etiquetaexpedicao
	 * @return
	 * @author Tom�s Rabelo
	 */
	public List<Etiquetaexpedicaogrupo> carregaListaEtiquetaExpedicaoGrupo(Etiquetaexpedicao etiquetaexpedicao) {
		if(etiquetaexpedicao == null || etiquetaexpedicao.getCdetiquetaexpedicao() == null)
			throw new WmsException("Par�metros inv�lidos.");
		
		return query()
			.select("etiquetaexpedicaogrupo.cdetiquetaexpedicaogrupo,carregamentoitem.cdcarregamentoitem," +
					"carregamentoitem.qtdeconfirmada,pedidovendaproduto.cdpedidovendaproduto," +
					"pedidovendaproduto.qtde,pedidovenda.cdpedidovenda,produto.cdproduto")
			.join("etiquetaexpedicaogrupo.etiquetaexpedicao etiquetaexpedicao")
			.join("etiquetaexpedicaogrupo.carregamentoitem carregamentoitem")
			.join("carregamentoitem.pedidovendaproduto pedidovendaproduto")
			.join("pedidovendaproduto.produto produto")
			.join("pedidovendaproduto.pedidovenda pedidovenda")
			.where("etiquetaexpedicao = ?", etiquetaexpedicao)
			.list();
	}

}
