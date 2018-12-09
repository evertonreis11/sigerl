package br.com.linkcom.wms.geral.predicate;

import org.apache.commons.collections.Predicate;

import br.com.linkcom.wms.geral.bean.Conferenciaordemrecebimento;

public class ConferenciaOrdemRecebimentoPredicate implements Predicate {
	
	private Integer cdProdutoEmbalagem;
	private Boolean isColetaAvaria;
	
	public ConferenciaOrdemRecebimentoPredicate(Integer cdProdutoEmbalagem, Boolean isColetaAvaria) {
		this.cdProdutoEmbalagem = cdProdutoEmbalagem;
		this.isColetaAvaria = isColetaAvaria;
	}

	@Override
	public boolean evaluate(Object object) {
		Conferenciaordemrecebimento conferenciaordemrecebimento = (Conferenciaordemrecebimento) object;
		return conferenciaordemrecebimento.getProdutoembalagem().getCdprodutoembalagem().equals(cdProdutoEmbalagem)
				&& conferenciaordemrecebimento.getIscoletaavaria().equals(isColetaAvaria);
	}

}
