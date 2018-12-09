package br.com.linkcom.wms.geral.dao;

import java.util.List;

import br.com.linkcom.wms.geral.bean.Carregamento;
import br.com.linkcom.wms.geral.bean.Vmapaseparacaocm;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class VmapaseparacaocmDAO extends GenericDAO<Vmapaseparacaocm>{

	public List<Vmapaseparacaocm> findAllBy(Carregamento carregamento) {
		if(carregamento == null || carregamento.getCdcarregamento() == null)
			throw new WmsException("Parâmetros inválidos.");
		
		return query()
				.select("tipooperacao.cdtipooperacao, carregamento.cdcarregamento, carregamentoitem.cdcarregamentoitem, cliente.cdpessoa, " +
						"produto.cdproduto, produtoprincipal.cdproduto, vmapaseparacaocm.ordem, linhaseparacao.cdlinhaseparacao, " +
						"produtoembalagem.cdprodutoembalagem, vmapaseparacaocm.peso, vmapaseparacaocm.cubagem, vmapaseparacaocm.qtde, " +
						"vmapaseparacaocm.chave ")
				.join("vmapaseparacaocm.carregamento carregamento")
				.join("vmapaseparacaocm.carregamentoitem carregamentoitem")
				.join("vmapaseparacaocm.cliente cliente")
				.join("vmapaseparacaocm.produto produto")				
				.join("vmapaseparacaocm.linhaseparacao linhaseparacao")
				.join("vmapaseparacaocm.produtoembalagem produtoembalagem")
				.join("vmapaseparacaocm.tipooperacao tipooperacao")
				.leftOuterJoin("vmapaseparacaocm.produtoprincipal produtoprincipal")
				.where("vmapaseparacaocm.qtde > 0")				
				.where("carregamento.cdcarregamento=?",carregamento.getCdcarregamento())
				.list();	
	}

}
