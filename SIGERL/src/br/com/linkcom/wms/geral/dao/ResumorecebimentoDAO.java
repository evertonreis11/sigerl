package br.com.linkcom.wms.geral.dao;

import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Resumorecebimento;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.ResumorecebimentoFiltro;
import br.com.linkcom.wms.util.DateUtil;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.neo.persistence.GenericDAO;

public class ResumorecebimentoDAO extends GenericDAO<Resumorecebimento> {

	@Override
	public void updateListagemQuery(QueryBuilder<Resumorecebimento> query, FiltroListagem _filtro) {
		ResumorecebimentoFiltro filtro = (ResumorecebimentoFiltro) _filtro;
		
		if (filtro.getDeposito() == null || filtro.getDeposito().getCddeposito() == null)
			throw new WmsException("O campo dep�sito � obrigat�rio.");
		else
			query.where("cddeposito = ? ", filtro.getDeposito().getCddeposito());
		
		if (filtro.getDatainicio() == null )
			throw new WmsException("O campo data inicial � obrigat�rio.");
		else
			query.where("fechamento >= ?", filtro.getDatainicio());
		
		query.where("fechamento <= ?", filtro.getDatafim());
		
	}
	
	public SqlRowSet getDadosExportacao(ResumorecebimentoFiltro filtro) {
		if (filtro.getDeposito() == null || filtro.getDeposito().getCddeposito() == null)
			throw new WmsException("O campo dep�sito � obrigat�rio.");
		
		if (filtro.getDatainicio() == null )
			throw new WmsException("O campo data inicial � obrigat�rio.");
		
		StringBuilder sql = new StringBuilder();
		
		Date datafim = filtro.getDatafim();
		if (datafim != null)
			datafim = DateUtil.incrementaDia(datafim, 1);
		else if (filtro.getDetalhada())
			datafim = DateUtil.incrementaDia(new Date(), 1);
		
		if (filtro.getDetalhada() && DateUtil.daysBetween(filtro.getDatainicio(), datafim) > 31){
			throw new WmsException("Para exportar uma planilha detalhada o intervalo n�o pode ser superior a um m�s.");
		}

		if (!filtro.getDetalhada()){
			sql.append("select deposito as \"Dep�sito\", fechamento as \"Fechamento\", ");
			sql.append("linhaseparacao as \"Linha de separa��o\", qtde as \"Qtde.\", peso as \"Peso\", ");
			sql.append("valor as \"Valor\", cubagem as \"Cubagem\" ");
			sql.append("from V_RESUMORECEBIMENTO where cddeposito = ? and fechamento >= ? ");
			if (filtro.getDatafim() != null)
				sql.append(" and fechamento <= ?");
		} else {
			sql.append("select d.nome as \"Dep�sito\", ls.nome AS \"Linha de separa��o\", trunc(r.dtrecebimento) AS \"Montagem\", "); 
			sql.append("  trunc(r.dtfinalizacao) AS \"Fechamento\", r.cdrecebimento AS \"N� Recebimento\", nft.nome as \"Tipo de nota\", ");
			sql.append("  p.codigo AS \"C�digo\", p.descricao AS \"Descri��o\",  ");
			sql.append("  sum(nfep.qtde) as \"Qtde\",  ");
			sql.append("  sum(nfep.qtde * p.peso) as \"Peso total\", "); 
			sql.append("  sum(nfep.qtde * nfep.valor)/100 as \"Valor total\", "); 
			sql.append("  sum(nfep.qtde * case when (p.qtdevolumes = 0) or (p.qtdevolumes is null) then 1 else p.qtdevolumes end) as \"N� de volumes\", ");
			sql.append("  Sum(nfep.qtde * p.cubagem) AS \"Cubagem total\" ");
			sql.append("from recebimento r ");
			sql.append("  join recebimentonotafiscal rnf on rnf.cdrecebimento = r.cdrecebimento ");
			sql.append("  join notafiscalentradaproduto nfep on nfep.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
			sql.append("  join deposito d on d.cddeposito = r.cddeposito ");
			sql.append("  join produto p ON p.cdproduto = nfep.cdproduto ");
			sql.append("  join dadologistico dl ON dl.cdproduto = p.cdproduto AND dl.cddeposito = r.cddeposito ");
			sql.append("  join linhaseparacao ls ON ls.cdlinhaseparacao = dl.cdlinhaseparacao ");
			sql.append("  join notafiscalentrada nfe on nfe.cdnotafiscalentrada = rnf.cdnotafiscalentrada ");
			sql.append("  join notafiscaltipo nft on nft.cdnotafiscaltipo = nfe.cdnotafiscaltipo ");
			sql.append("where ");
			sql.append("  r.cddeposito = ? ");
			sql.append("  and r.dtrecebimento >= ? ");
			sql.append("  and r.dtrecebimento < ? ");
			sql.append("  and r.dtfinalizacao is not null ");
			sql.append("group by d.nome, ls.nome, trunc(r.dtrecebimento), trunc(r.dtfinalizacao), r.cdrecebimento, nft.nome, p.codigo, p.descricao ");
			sql.append("order by 1,2,3 ");
		}
		
		Object[] args;

		if (datafim != null){
			args = new Object[]{filtro.getDeposito().getCddeposito(), filtro.getDatainicio(), filtro.getDatafim()};
		}else
			args = new Object[]{filtro.getDeposito().getCddeposito(), filtro.getDatainicio()};
		
		return getJdbcTemplate().queryForRowSet(sql.toString(), args);
	}
	
}
