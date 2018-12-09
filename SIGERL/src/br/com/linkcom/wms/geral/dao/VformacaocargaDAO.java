package br.com.linkcom.wms.geral.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.BasicTransformerAdapter;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.Money;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Pedidovenda;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Vformacaocarga;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro.TipoTroca;
import br.com.linkcom.wms.util.WmsUtil;

public class VformacaocargaDAO extends GenericDAO<Vformacaocarga>{
		
	/* singleton */
	private static VformacaocargaDAO instance;
	public static VformacaocargaDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(VformacaocargaDAO.class);
		}
		return instance;
	}
	
	/**
	 * Busca todos os dados da view
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param filtro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Vformacaocarga> findForCarregamento(CarregamentoFiltro filtro,String cds) {
 
		final QueryBuilder<Vformacaocarga> query = query()
				.where("vformacaocarga.numero = ?",filtro.getPedido())
				.where("vformacaocarga.praca = ?",filtro.getPraca())
				.where("vformacaocarga.tipooperacao = ?",filtro.getTipooperacao());
				if(filtro.getNomecliente()!=null && !filtro.getNomecliente().isEmpty()){
					query.whereLike("vformacaocarga.pessoanome",Util.strings.tiraAcento(filtro.getNomecliente()).toUpperCase());
				}
				query.where("vformacaocarga.deposito = ?",WmsUtil.getDeposito());
		
		if (TipoTroca.SIM.equals(filtro.getTroca()))
			query.where("vformacaocarga.troca is true");
		else if (TipoTroca.NAO.equals(filtro.getTroca()))
			query.where("vformacaocarga.troca is false");

		if(filtro.getTipooperacao()!=null && filtro.getTipooperacao().getCdtipooperacao().equals(Tipooperacao.MONSTRUARIO_LOJA.getCdtipooperacao())){
			if(filtro.getProduto()!=null && filtro.getProduto().getCdproduto()!=null){
				query.where(" EXISTS (select 1 " +
										"from 	br.com.linkcom.wms.geral.bean.Pedidovendaproduto pp " +
										"where 	pp.pedidovenda.cdpedidovenda = vformacaocarga.cdpedidovenda " +
										"and 	pp.deposito.cddeposito = vformacaocarga.deposito.cddeposito " +
										"and pp.produto.cdproduto = "+filtro.getProduto().getCdproduto()+") ");
			}
			if(filtro.getProdutoclasse()!=null && filtro.getProdutoclasse().getNumero()!=null){
				query.where(" EXISTS (select 1 " +
										"from 	br.com.linkcom.wms.geral.bean.Pedidovendaproduto pp, " +
										"		br.com.linkcom.wms.geral.bean.Produtoclasse pc, " +
										"		br.com.linkcom.wms.geral.bean.Produto pr " +
										"where 	substr(pr.produtoclasse.numero,1,3) = ? " +
										"and 	pp.produto.cdproduto = pr.cdproduto " +
										"and 	pp.deposito.cddeposito = vformacaocarga.deposito.cddeposito " +
										"and 	pp.pedidovenda.cdpedidovenda = vformacaocarga.cdpedidovenda " +
										"and 	pc.controlaverba = 1)",filtro.getProdutoclasse().getNumero());
			}
			
		}
		
		if (filtro.getRotas() != null && filtro.getRotas().size() > 0){
			if (filtro.getRotas().contains(Rota.ROTA_NAO_ENCONTRADA)){
				filtro.getRotas().remove(Rota.ROTA_NAO_ENCONTRADA);
				
				query.openParentheses();
					query.where("vformacaocarga.cdrota is null");
					query.or();
					query.whereIn("vformacaocarga.cdrota",CollectionsUtil.listAndConcatenate(filtro.getRotas(),"cdrota", ","));
				query.closeParentheses();
			}else
				query.whereIn("vformacaocarga.cdrota",CollectionsUtil.listAndConcatenate(filtro.getRotas(),"cdrota", ","));
		}
		 
		if(filtro.getTurnodeentrega() != null && filtro.getTurnodeentrega().getCdturnodeentrega()!=null){
			query.where("vformacaocarga.cdturnodeentrega = ?",filtro.getTurnodeentrega().getCdturnodeentrega());
		}
		
		if(cds != null && !"".equals(cds)){
			query.where("vformacaocarga.cdformacaocarga not in ("+cds+")");
		}
		
			query.where("vformacaocarga.datavenda >= ?" , filtro.getDatavendainicial())
			.where("vformacaocarga.datavenda <= ?" , filtro.getDatavendafinal());
		//Se vai filtrar por data então terei e sobrescrever o select pra recalcular as quantidades
		if (filtro.getDtentregafim() != null || filtro.getDtentregainicio() != null){
			final List<String> campos = new ArrayList<String>();
			campos.add("vformacaocarga.cdformacaocarga");
			campos.add("vformacaocarga.cdrota");
			campos.add("vformacaocarga.rotanome");
			campos.add("vformacaocarga.ordem");
			campos.add("vformacaocarga.praca.id");
			campos.add("vformacaocarga.pracanome");
			campos.add("vformacaocarga.tipooperacao.id");
			campos.add("vformacaocarga.tipooperacaonome");
			campos.add("vformacaocarga.cep");
			campos.add("vformacaocarga.deposito.id");
			campos.add("vformacaocarga.pessoanome");
			campos.add("vformacaocarga.numero");
			campos.add("vformacaocarga.datavenda");
			campos.add("vformacaocarga.cdpessoa");
			campos.add("vformacaocarga.cdpedidovenda");
			campos.add("vformacaocarga.cdfilialentrega");
			campos.add("vformacaocarga.filialentreganome");
			campos.add("vformacaocarga.troca");
			campos.add("vformacaocarga.cddepositotransbordo");
			campos.add("vformacaocarga.cdfilialtransbordo");
			campos.add("vformacaocarga.depositotransbordonome");
			campos.add("vformacaocarga.filialtransbordonome");
			campos.add("vformacaocarga.filialemissao");
			campos.add("vformacaocarga.prioridade");
			
			query.select(StringUtils.join(campos.iterator(), ",") + 
					", sum(pvp.qtde) as qtde " +
					", sum(nvl(trunc(pesounitario_produto(pvp.produto.id)*1000),0) * pvp.qtde) as peso" +
					", sum(nvl(trunc(cubagemunitaria_produto(pvp.produto.id)*1000),0) * pvp.qtde) as cubagem" +
					", sum(pvp.valor * pvp.qtde) as valor")
				.join("vformacaocarga.pedidovenda pedidovenda")
				.join("pedidovenda.listaPedidoVendaProduto pvp")
				.where("trunc(pvp.dtprevisaoentrega) <= ?",filtro.getDtentregafim())
				.where("trunc(pvp.dtprevisaoentrega) >= ?",filtro.getDtentregainicio())
				.where("pvp.dtmarcacao is null")
				.where("pvp.carregado is false")
				.where("pvp.numeronota is null")
				.groupBy(StringUtils.join(campos.iterator(), ","))
				.orderBy("vformacaocarga.deposito.id, vformacaocarga.rotanome, vformacaocarga.ordem, " +
						"vformacaocarga.pracanome, vformacaocarga.tipooperacaonome, vformacaocarga.cep, " +
						"vformacaocarga.depositotransbordonome, " +
						"vformacaocarga.filialentreganome, vformacaocarga.pessoanome");

			return getHibernateTemplate().executeFind(new HibernateCallback(){
				public Object doInHibernate(final Session session)throws HibernateException, SQLException {
					Query hqlQuery = session.createQuery(query.getQuery());
					for (int i = 0; i < query.getWhere().getParameters().size(); i++) {
						hqlQuery.setParameter(i, query.getWhere().getParameters().get(i));
					}
					hqlQuery.setResultTransformer(new BasicTransformerAdapter(){
						private static final long serialVersionUID = 5314245709735542018L;

						public Object transformTuple(Object[] tuple, String[] aliases) {
							Vformacaocarga result = new Vformacaocarga();
							
							String prefixo = "vformacaocarga.";
							
							result.setCdformacaocarga((Long) tuple[campos.indexOf(prefixo+"cdformacaocarga")]);
							result.setCdrota((Integer) tuple[campos.indexOf(prefixo+"cdrota")]);
							result.setRotanome((String) tuple[campos.indexOf(prefixo+"rotanome")]);
							result.setOrdem((Integer) tuple[campos.indexOf(prefixo+"ordem")]);
							result.setPraca(new Praca((Integer) tuple[campos.indexOf(prefixo+"praca.id")]));
							result.setPracanome((String) tuple[campos.indexOf(prefixo+"pracanome")]);
							result.setTipooperacao((Tipooperacao) session.get(Tipooperacao.class, (Integer) tuple[campos.indexOf(prefixo+"tipooperacao.id")]));
							result.setTipooperacaonome((String) tuple[campos.indexOf(prefixo+"tipooperacaonome")]);
							result.setCep((String) tuple[campos.indexOf(prefixo+"cep")]);
							result.setPessoa(new Cliente((Integer)tuple[campos.indexOf(prefixo+"cdpessoa")], (String) tuple[campos.indexOf(prefixo+"pessoanome")]));
							result.setPessoanome((String) tuple[campos.indexOf(prefixo+"pessoanome")]);
							result.setQtde(((Long) tuple[campos.size()]).intValue());
							result.setPeso(((Long) tuple[campos.size() + 1]).doubleValue()/1000);
							result.setCubagem(((Long) tuple[campos.size() + 2]).doubleValue()/1000);
							result.setValor(tuple[campos.size() + 3] != null ? new Money((Long)tuple[campos.size() + 3], true) : new Money(0));
							result.setDeposito(new Deposito((Integer)tuple[campos.indexOf(prefixo+"deposito.id")]));
							result.setPedidovenda(new Pedidovenda((Integer) tuple[campos.indexOf(prefixo+"cdpedidovenda")]));
							result.setNumero((String) tuple[campos.indexOf(prefixo+"numero")]);
							result.setDatavenda((java.sql.Date) tuple[campos.indexOf(prefixo+"datavenda")] );
							result.setCdpessoa((Integer) tuple[campos.indexOf(prefixo+"cdpessoa")]);
							result.setCdpedidovenda((Integer) tuple[campos.indexOf(prefixo+"cdpedidovenda")]);
							result.setCdfilialentrega((Integer) tuple[campos.indexOf(prefixo+"cdfilialentrega")]);
							result.setFilialentreganome((String) tuple[campos.indexOf(prefixo+"filialentreganome")]);
							result.setTroca((Boolean) tuple[campos.indexOf(prefixo+"troca")]);
							result.setCddepositotransbordo((Integer) tuple[campos.indexOf(prefixo+"cddepositotransbordo")]);
							result.setCdfilialtransbordo((Integer) tuple[campos.indexOf(prefixo+"cdfilialtransbordo")]);
							result.setDepositotransbordonome((String) tuple[campos.indexOf(prefixo+"depositotransbordonome")]);
							result.setFilialtransbordonome((String) tuple[campos.indexOf(prefixo+"filialtransbordonome")]);
							result.setFilialemissao((String)tuple[campos.indexOf(prefixo+"filialemissao")]);
							result.setPrioridade((Boolean)tuple[campos.indexOf(prefixo+"prioridade")]);
							
							return result;
						}
					});
					return hqlQuery.list();
				}
			});
			
		}else
			return	query.list();
	}
	
}
