package br.com.linkcom.wms.util.expedicao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.types.Money;
import br.com.linkcom.wms.geral.bean.Cliente;
import br.com.linkcom.wms.geral.bean.Praca;
import br.com.linkcom.wms.geral.bean.Rota;
import br.com.linkcom.wms.geral.bean.Tipooperacao;
import br.com.linkcom.wms.geral.bean.Vformacaocarga;
import br.com.linkcom.wms.geral.service.VformacaocargaService;
import br.com.linkcom.wms.modulo.expedicao.controller.process.filtro.CarregamentoFiltro;

public class AgrupaRotas {
	
	public enum NivelComparacao {ROTA, PRACA, TIPO_OPERACAO, CLIENTE_OU_FILIAL_ENTREGA}
	
	private CarregamentoFiltro filtro;
	private StringBuilder listaPais = new StringBuilder();
	private StringBuilder jsonElements = new StringBuilder();
	private String formacaoCarga = "";
	private Double pesoTotal = 0.0;
	private Double cubagemTotal = 0.0;
	private Double valorTotal = 0.0;
	private Long totalProdutos = 0L;
	private Integer totalEntrega = 0;
	
	/*--------Getters-----------*/
	
	public Double getPesoTotal() {
		return pesoTotal;
	}
	
	public Double getCubagemTotal() {
		return cubagemTotal;
	}
	
	public Double getValorTotal() {
		return valorTotal;
	}
	
	public Long getTotalProdutos() {
		return totalProdutos;
	}
	
	public StringBuilder getListaPais() {
		return listaPais;
	}
	
	public StringBuilder getJsonElements() {
		return jsonElements;
	}
	
	public Integer getTotalEntrega() {
		return totalEntrega;
	}
	
	/**
	 * Retorna um array com os index dos elementos do TreeGrid
	 * @author Leonardo Guimarães
	 * @return
	 */
	public Integer[] getMapaPais() {
		String[] aux = listaPais.toString().split(",");
		Integer[] map = new Integer[aux.length];
		for (int i = 0; i < aux.length; i++) {
			try{
				map[i] = Integer.parseInt(aux[i]);
			}catch (Exception e) {
				map[i] = 0;
			}
		}
		return map;
	}
	
	/*----------------------------*/
	
	
	public AgrupaRotas(CarregamentoFiltro filtro) {
		if(filtro == null)
			filtro = new CarregamentoFiltro();
		this.filtro = filtro;
	}
	

	/**
	 * Prepara uma lista de CarregamentoVO propícia para 
	 * a montagem da árvore na tela de montagem de carregamento
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see #groupPracas(List, List, CarregamentoVO, int)
	 * @see #makeJsonObjects(CarregamentoVO)
	 * @see #isNextEquals(List, int, Vformacaocarga)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CarregamentoVO> prepararListaCarregamentoVO(){
		List<String> entregas = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<CarregamentoVO> listaCarregamentoVO = new ArrayList<CarregamentoVO>();
		List<Vformacaocarga> listaCarregamento = VformacaocargaService.getInstance().findForCarregamento(filtro,formacaoCarga);
		int i = 0;
		if(listaCarregamento != null)
			while (i < listaCarregamento.size()){
				Vformacaocarga vformacaocarga = listaCarregamento.get(i);
				CarregamentoVO carregamentoVO = new CarregamentoVO();
				carregamentoVO.setRota(new Rota(vformacaocarga.getCdrota(),vformacaocarga.getRotanome()));
				carregamentoVO.setPai(0);
				carregamentoVO.setNivel(1);
				carregamentoVO.setDatavenda( sdf.format(vformacaocarga.getDatavenda()) );
				listaPais.append(0+",");
				listaCarregamentoVO.add(carregamentoVO);
				carregamentoVO.setIndex(listaCarregamentoVO.size());
				carregamentoVO.setDescricao("RO: "+vformacaocarga.getRotanome());
				List<Vformacaocarga> listaAux = new ArrayList<Vformacaocarga>();
				Boolean condition = Boolean.TRUE;
				
				
				do{
					//Contando o número de entregas
					Vformacaocarga formacaocargaAux = listaCarregamento.get(i);
					String key;
					if (Tipooperacao.TRANSFERENCIA_FILIAL_ENTREGA.equals(formacaocargaAux))
						key = formacaocargaAux.getCep() + "-" + formacaocargaAux.getCdfilialentrega();
					else
						key = formacaocargaAux.getCep() + "-" + formacaocargaAux.getCdpessoa();
					
					if (!entregas.contains(key))
						entregas.add(key);
					///////////////////////////////

					listaAux.add(listaCarregamento.get(i));
					i++;
					
					condition = isNextEquals(listaCarregamento, i,vformacaocarga,null);
					
				}while(condition);
				groupPracas(listaCarregamentoVO, listaAux, carregamentoVO, listaCarregamentoVO.size());
				somatorio(carregamentoVO);
			}
		jsonElements.append("[");
		int idx = 0;
		for (CarregamentoVO carregamentoVO : listaCarregamentoVO) {
			makeJsonObjects(carregamentoVO);
			if(!"collapsed".equals(carregamentoVO.getCollapsed())){
				if(idx % 2 == 0){
					carregamentoVO.setStyleClass("even");
				}
				idx ++;
			}
		}
		jsonElements.append("]");
		if(listaPais.length() > 0)
			listaPais.delete(listaPais.lastIndexOf(","), listaPais.length());
		
		totalEntrega = entregas.size();

		return listaCarregamentoVO;
	}
	
	/**
	 * Agrupa as praças da rota
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.util.expedicao.AgrupaRotas#setaCarregamentoVO(CarregamentoVO carregamentoVO, CarregamentoVO carregamentoVOAux, int pai, int index)
	 * @see br.com.linkcom.wms.util.carregamento.AgrupaRotas#agrupaTP(List<CarregamentoVO> listaCarregamentoVO, CarregamentoVO carregamentoVO, int pai)
	 * @see br.com.linkcom.wms.util.expedicao.AgrupaRotas#setaCarregamentoVODadosFilhos(CarregamentoVO carregamentoVOPai, CarregamentoVO carregamentoVOFilho)
	 * 
	 * @param listaCarregamentoVO
	 * @param listaCarregamento
	 * @param rota
	 */
	private void groupPracas(List<CarregamentoVO> listaCarregamentoVO,List<Vformacaocarga> listaCarregamento, CarregamentoVO carregamentoVO, int pai){
		int i= 0;
		while (i < listaCarregamento.size()){
			Vformacaocarga vformacaocarga = listaCarregamento.get(i);
			CarregamentoVO carregamentoVOAux = new CarregamentoVO();
			listaCarregamentoVO.add(carregamentoVOAux);
			setaCarregamentoVO(carregamentoVO, carregamentoVOAux, pai, listaCarregamentoVO.size());
			if(vformacaocarga.getPraca() == null)
				vformacaocarga.setPraca(new Praca());
			carregamentoVOAux.setPraca(new Praca(vformacaocarga.getPraca().getCdpraca(),vformacaocarga.getPracanome()));
			carregamentoVOAux.setDescricao("PR: "+vformacaocarga.getPracanome());
			listaPais.append(pai+",");
			carregamentoVOAux.setNivel(2);
			if(filtro.getExpandirAte() != null && filtro.getExpandirAte() > 4)
				carregamentoVOAux.setCollapsed("collapsed");
			List<Vformacaocarga> listaAux = new ArrayList<Vformacaocarga>();
			Boolean condition = Boolean.FALSE;
			do{
				listaAux.add(listaCarregamento.get(i));
				i++;
				
				condition = isNextEquals(listaCarregamento, i, vformacaocarga, NivelComparacao.PRACA);
			}while(condition);
			
			agrupaTP(listaCarregamentoVO,carregamentoVOAux,listaAux,listaCarregamentoVO.size());
			setaCarregamentoVODadosFilhos(carregamentoVO, carregamentoVOAux);
		}
	}
	
	
	/**
	 * Agrupa os tipos de pedidos da praça
	 * 
	 * @author Leonardo Guimarães
	 *  
	 * @see br.com.linkcom.wms.util.expedicao.AgrupaRotas#setaCarregamentoVO(CarregamentoVO carregamentoVO, CarregamentoVO carregamentoVOAux, int pai, int index)
	 * @see #agrupaCliente(List, CarregamentoVO, List, int)
	 * @see br.com.linkcom.wms.util.expedicao.AgrupaRotas#setaCarregamentoVODadosFilhos(CarregamentoVO carregamentoVOPai, CarregamentoVO carregamentoVOFilho)
	 *
	 * @param listaCarregamentoVO
	 * @param carregamentoVO
	 * @param listaCarregamento
	 * @param pai
	 */
	private void agrupaTP(List<CarregamentoVO> listaCarregamentoVO,CarregamentoVO carregamentoVO,List<Vformacaocarga> listaCarregamento,int pai){
		int i = 0;
		while (i < listaCarregamento.size()){
			Vformacaocarga vformacaocarga = listaCarregamento.get(i);
			CarregamentoVO carregamentoVOAux = new CarregamentoVO();
			carregamentoVOAux.setDescricao("TP: "+vformacaocarga.getTipooperacaonome());
			listaCarregamentoVO.add(carregamentoVOAux);
			if(filtro.getExpandirAte() != null && filtro.getExpandirAte() > 3)
				carregamentoVOAux.setCollapsed("collapsed");
			
			listaPais.append(pai+",");
			carregamentoVOAux.setNivel(3);
			setaCarregamentoVO(carregamentoVO, carregamentoVOAux,pai,listaCarregamentoVO.size());
			carregamentoVOAux.setTipooperacao(vformacaocarga.getTipooperacao());
			carregamentoVOAux.setNivel(3);
			List<Vformacaocarga> listaAux = new ArrayList<Vformacaocarga>();
			Boolean condition = Boolean.FALSE;
			do{
				listaAux.add(listaCarregamento.get(i));
				i++;
				condition = isNextEquals(listaCarregamento, i, vformacaocarga, NivelComparacao.TIPO_OPERACAO);
			}while(condition);
			agrupaCliente(listaCarregamentoVO, carregamentoVOAux, listaAux, listaCarregamentoVO.size());
			setaCarregamentoVODadosFilhos(carregamentoVO, carregamentoVOAux);
		}
	}
	
	/**
	 * Agrupa os clientes do tipo de pedido
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @see br.com.linkcom.wms.util.expedicao.AgrupaRotas#setaCarregamentoVO(CarregamentoVO carregamentoVO, CarregamentoVO carregamentoVOAux, int pai, int index)
	 * @see br.com.linkcom.wms.util.expedicao.AgrupaRotas#setaCarregamentoVODadosFilhos(CarregamentoVO carregamentoVOPai, CarregamentoVO carregamentoVOFilho)
	 * 
	 * @param listaCarregamentoVO
	 * @param carregamentoVO
	 * @param listaCarregamento
	 * @param pai
	 */
	private void agrupaCliente(List<CarregamentoVO> listaCarregamentoVO,CarregamentoVO carregamentoVO,List<Vformacaocarga> listaCarregamento, int pai){
		int i = 0;
		int soma = 0;
		double peso = 0.0;
		Double cubagem = 0.0;
		Money valor = new Money();
		while(i < listaCarregamento.size()){
			Vformacaocarga vformacaocarga = listaCarregamento.get(i);
			CarregamentoVO carregamentoVOAux = new CarregamentoVO();
			listaCarregamentoVO.add(carregamentoVOAux);
			listaPais.append(pai+",");
			
			if(filtro.getExpandirAte() != null && filtro.getExpandirAte() > 2)
				carregamentoVOAux.setCollapsed("collapsed");

			setaCarregamentoVO(carregamentoVO, carregamentoVOAux, pai, listaCarregamentoVO.size());
			carregamentoVOAux.setNivel(4);
			carregamentoVOAux.setCep(vformacaocarga.getCep() != null ? vformacaocarga.getCep() : "");
			carregamentoVOAux.setCdformacaocarga(vformacaocarga.getCdformacaocarga());
			carregamentoVOAux.setTotalItens(vformacaocarga.getQtde());
			carregamentoVOAux.setPeso(vformacaocarga.getPeso() == null ? 0 : vformacaocarga.getPeso());
			carregamentoVOAux.setCubagem(vformacaocarga.getCubagem());
			carregamentoVOAux.setValor(vformacaocarga.getValor());
			
			String depositoTransbordo;
			if (vformacaocarga.getDepositotransbordonome() != null)
				depositoTransbordo = vformacaocarga.getDepositotransbordonome() + " / ";
			else
				depositoTransbordo = "";
			
			if (Tipooperacao.TRANSFERENCIA_CD_CD_CLIENTE.equals(carregamentoVO.tipooperacao)){
				carregamentoVOAux.setFilialEntrega(new Cliente(vformacaocarga.getCdfilialtransbordo(), vformacaocarga.getFilialtransbordonome()));
				carregamentoVOAux.setDescricao("FL: " + depositoTransbordo + vformacaocarga.getFilialtransbordonome());
			}else if (Tipooperacao.TRANSFERENCIA_FILIAL_ENTREGA.equals(carregamentoVO.tipooperacao)){
				carregamentoVOAux.setFilialEntrega(new Cliente(vformacaocarga.getCdfilialentrega(), vformacaocarga.getFilialentreganome()));
				carregamentoVOAux.setDescricao("FL: " + vformacaocarga.getFilialentreganome());			
			}else if (Tipooperacao.MONSTRUARIO_LOJA.equals(carregamentoVO.tipooperacao)){
				carregamentoVOAux.setDescricao("FL: " + depositoTransbordo + vformacaocarga.getPessoanome());
			}else{
				carregamentoVOAux.setDescricao("CL: " + depositoTransbordo + vformacaocarga.getPessoanome());
			}
			
			carregamentoVOAux.setCliente(new Cliente(vformacaocarga.getCdpessoa(), vformacaocarga.getPessoanome()));
			
			soma += vformacaocarga.getQtde();
			peso += vformacaocarga.getPeso() == null ? 0 : vformacaocarga.getPeso();
			cubagem += carregamentoVOAux.getCubagem() == null ? 0 : carregamentoVOAux.getCubagem();
			valor = valor.add(vformacaocarga.getValor());
			
			List<Vformacaocarga> listaAux = new ArrayList<Vformacaocarga>();
			Boolean condition = Boolean.FALSE;
			do{
				listaAux.add(listaCarregamento.get(i));
				i++;
				condition = isNextEquals(listaCarregamento, i, vformacaocarga,NivelComparacao.CLIENTE_OU_FILIAL_ENTREGA);
			}while(condition);
			agrupaPedido(listaCarregamentoVO, carregamentoVOAux, listaAux, listaCarregamentoVO.size());
			setaCarregamentoVODadosFilhos(carregamentoVO, carregamentoVOAux);
		}
	}
	
	
	/**
	 * Agrupa os pedidos em cada cliente
	 * 
	 * @author Giovane Freitas
	 * 
	 * @param listaCarregamentoVO
	 * @param carregamentoVO
	 * @param listaCarregamento
	 * @param pai
	 */
	private void agrupaPedido(List<CarregamentoVO> listaCarregamentoVO,
			CarregamentoVO carregamentoVO, List<Vformacaocarga> listaCarregamento,
			int pai) {

		int i = 0;
		int soma = 0;
		double peso = 0.0;
		Double cubagem = 0.0;
		Money valor = new Money();
		while(i < listaCarregamento.size()){
			Vformacaocarga vformacaocarga = listaCarregamento.get(i);
			CarregamentoVO carregamentoVOAux = new CarregamentoVO();
			listaCarregamentoVO.add(carregamentoVOAux);
			listaPais.append(pai+",");
			
			if(filtro.getExpandirAte() != null && filtro.getExpandirAte() > 1)
				carregamentoVOAux.setCollapsed("collapsed");

			setaCarregamentoVO(carregamentoVO, carregamentoVOAux, pai, listaCarregamentoVO.size());
			carregamentoVOAux.setCliente(new Cliente(vformacaocarga.getCdpessoa(),vformacaocarga.getPessoanome()));
			carregamentoVOAux.setNivel(5);
			carregamentoVOAux.setDescricao("Pedido: "+vformacaocarga.getNumero());
			carregamentoVOAux.setNomedepositotransbordo(vformacaocarga.getDepositotransbordonome());
			carregamentoVOAux.setCdpedidovenda(vformacaocarga.getCdpedidovenda());
			carregamentoVOAux.setCep(vformacaocarga.getCep() != null ? vformacaocarga.getCep() : "");
			carregamentoVOAux.setCdformacaocarga(vformacaocarga.getCdformacaocarga());
			carregamentoVOAux.setTotalItens(vformacaocarga.getQtde());
			carregamentoVOAux.setPeso(vformacaocarga.getPeso() == null ? 0 : vformacaocarga.getPeso());
			carregamentoVOAux.setCubagem(vformacaocarga.getCubagem());
			carregamentoVOAux.setValor(vformacaocarga.getValor());
			carregamentoVOAux.setNumeropedido(vformacaocarga.getNumero());
			carregamentoVOAux.setFilialemissao(vformacaocarga.getFilialemissao());
			carregamentoVOAux.setPrioridade(vformacaocarga.getPrioridade());
			
			if (vformacaocarga.getCdfilialtransbordo() != null)
				carregamentoVOAux.setFilialEntrega(new Cliente(vformacaocarga.getCdfilialtransbordo(), vformacaocarga.getFilialtransbordonome()));
			else if (vformacaocarga.getCdfilialentrega() != null)
				carregamentoVOAux.setFilialEntrega(new Cliente(vformacaocarga.getCdfilialentrega(), vformacaocarga.getFilialentreganome()));
			
			soma += vformacaocarga.getQtde();
			peso += vformacaocarga.getPeso() == null ? 0 : vformacaocarga.getPeso();
			cubagem += carregamentoVOAux.getCubagem() == null ? 0 : carregamentoVOAux.getCubagem();
			valor = valor.add(vformacaocarga.getValor());
			i++;
		}
		carregamentoVO.setTotalItens(soma);
		carregamentoVO.setPeso(peso);
		carregamentoVO.setCubagem(cubagem);
		carregamentoVO.setValor(valor);

	}

	/**
	 * Passa os dados de um carregamentoVO para carregamentoVOAux
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * Obs: Este método tem que ser executado antes que se
	 * setar qualquer coisa em carregamentoVOAux, pois
	 * as propriedades setadas anteriormente podem ser modificadas
	 * 
	 * @param carregamentoVOAux
	 * @param carregamentoVO
	 */
	public void setaCarregamentoVO(CarregamentoVO carregamentoVO, CarregamentoVO carregamentoVOAux, int pai, int index){
		carregamentoVOAux.setRota(carregamentoVO.getRota());
		carregamentoVOAux.setPraca(carregamentoVO.getPraca());
		carregamentoVOAux.setTipooperacao(carregamentoVO.getTipooperacao());
		carregamentoVOAux.setCliente(carregamentoVO.getCliente());
		carregamentoVOAux.setCdpedidovenda(carregamentoVO.getCdpedidovenda());
		carregamentoVOAux.setProduto(carregamentoVO.getProduto());
		carregamentoVOAux.setPai(pai);
		carregamentoVOAux.setIndex(index);
		carregamentoVOAux.setDatavenda(carregamentoVO.getDatavenda());
	}
	
	/**
	 * Seta os dados que devem ser somados do filho no pai
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoVOPai
	 * @param carregamentoVOFilho
	 */
	private void setaCarregamentoVODadosFilhos(CarregamentoVO carregamentoVOPai, CarregamentoVO carregamentoVOFilho) {
		carregamentoVOPai.setTotalItens(carregamentoVOPai.getTotalItens() + carregamentoVOFilho.getTotalItens());
		carregamentoVOPai.setPeso(carregamentoVOPai.getPeso() + carregamentoVOFilho.getPeso());
		carregamentoVOPai.setCubagem(carregamentoVOPai.getCubagem() + carregamentoVOFilho.getCubagem());
		carregamentoVOPai.setValor(carregamentoVOPai.getValor().add(carregamentoVOFilho.getValor()));	
		if(carregamentoVOFilho.getDate() != null && (carregamentoVOPai.getDate() == null || carregamentoVOPai.getDate().after(carregamentoVOFilho.getDate())))
			carregamentoVOPai.setDate(carregamentoVOFilho.getDate());
	}
	
	/**
	 * Concatena ao objeto jsonElements os valores convertidos para json.
	 * 
	 * @author Pedro Gonçalves
	 * @param carregamentoVO
	 */
	private void makeJsonObjects(CarregamentoVO carregamentoVO){
		Integer cdpessoa = null;
		String nomeFilial = null;
		if (carregamentoVO.getFilialEntrega() != null){
			cdpessoa = carregamentoVO.getFilialEntrega().getCdpessoa();
			nomeFilial = carregamentoVO.getFilialEntrega().getNome();
			if (nomeFilial != null)
				nomeFilial = nomeFilial.replace("\\", "").replace("'", "\\'");
		}
		
		String nomePraca = carregamentoVO.getPraca().getNome();
		if (nomePraca != null)
			nomePraca = nomePraca.replace("\\", "").replace("'", "\\'");
		
		String nomeRota = carregamentoVO.getRota().getNome();
		if (nomeRota != null)
			nomeRota = nomeRota.replace("\\", "").replace("'", "\\'");
		
		String nomeCliente = carregamentoVO.getCliente().getNome();
		if (nomeCliente != null){
			nomeCliente = nomeCliente.replace("\\", "");
			nomeCliente = nomeCliente.replace("'", "\\'");
		}
		
		String nomeTP = carregamentoVO.getTipooperacao().getNome();
		String sigla = carregamentoVO.getTipooperacao().getSigla();
		
		jsonElements.append("{'praca':{'cdpraca':'"+carregamentoVO.getPraca().getCdpraca()+"','nome':'"+nomePraca+"'}," +
		 		"'rota':{'cdrota':'"+carregamentoVO.getRota().getCdrota()+"','nome':'"+nomeRota+"'}," +
		 		"'tipooperacao':{'cdtipooperacao':'"+carregamentoVO.getTipooperacao().getCdtipooperacao()+"','sigla':'"+sigla+"','nome':'"+nomeTP+"'}," +
		 		"'cliente':{'cdpessoa':'"+carregamentoVO.getCliente().getCdpessoa()+"','nome':'"+nomeCliente+"'}," +
		 		"'filialentrega':{'cdpessoa':'"+cdpessoa+"','nome':'"+nomeFilial+"'}," +
		 		"'totalItens':'"+carregamentoVO.getTotalItens()+"','peso':'"+(carregamentoVO.getPeso() == null ? 0 : carregamentoVO.getPeso())+"','cubagem':'"+(carregamentoVO.getCubagem() == null ? 0 : carregamentoVO.getCubagem())+"'," +
		 		"'cep':'"+carregamentoVO.getCep()+"',"+"'nomedepositotransbordo':'"+carregamentoVO.getNomedepositotransbordo()+"',"+
		 		"'cdformacaocarga':'"+(carregamentoVO.getCdformacaocarga() == null ? "" : carregamentoVO.getCdformacaocarga())+"',"+
		 		"'numeropedido':'"+carregamentoVO.getNumeropedido()+"',"+
		 		"'prioridade':'"+carregamentoVO.getPrioridade()+"',"+
		 		"'filialemissao':'"+carregamentoVO.getFilialemissao()+"',"+
		 		"'cdpedidovenda':'"+carregamentoVO.getCdpedidovenda()+"',"+
		 		"'datavenda':'"+carregamentoVO.getDatavenda()+"',"+
		 		"'valor':'"+carregamentoVO.getValor()+"'},");
	}
	
	/**
	 * Faz os somatórios para calcular os totais
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param carregamentoVO contendo os dados que serão somados
	 */
	private void somatorio(CarregamentoVO carregamentoVO){
		pesoTotal += carregamentoVO.getPeso();
		cubagemTotal += carregamentoVO.getCubagem();
		valorTotal += Double.valueOf(carregamentoVO.getValor().getValue().toString());
		totalProdutos += carregamentoVO.getTotalItens();
	}
	
	/**
	 * Verifica se o vformacaocarga é o ultimo da lista ou se 
	 * o próximo vformacaocarga é igual ao atual
	 * 
	 * @author Leonardo Guimarães
	 * 
	 * @param listaCarregamento
	 * @param i
	 * @param vformacaocarga
	 * @param nivelComparacao O nível de comparação desejada, se for null irá considerar apenas a rota.
	 * @return
	 */
	private Boolean isNextEquals(List<Vformacaocarga> listaCarregamento, int i,	Vformacaocarga vformacaocarga,NivelComparacao nivelComparacao) {
		Boolean condition;
		if(i < listaCarregamento.size())
			if(nivelComparacao == null || nivelComparacao.equals(NivelComparacao.ROTA))
				if(vformacaocarga.getCdrota() != null)
					condition = vformacaocarga.getCdrota().equals(listaCarregamento.get(i).getCdrota());
				else
					condition = listaCarregamento.get(i).getCdrota() == null;
			else
				if(NivelComparacao.PRACA.equals(nivelComparacao)){
					if(vformacaocarga.getPraca() != null && vformacaocarga.getPraca().getCdpraca() != null){
						if(listaCarregamento.get(i).getPraca() == null)
							return false;
						condition = vformacaocarga.getPraca().getCdpraca().equals(listaCarregamento.get(i).getPraca().getCdpraca());
					}
					else
						condition = listaCarregamento.get(i).getPraca() == null || listaCarregamento.get(i).getPraca().getCdpraca() == null;
				}else if(NivelComparacao.TIPO_OPERACAO.equals(nivelComparacao)){
					if(vformacaocarga.getTipooperacao().getCdtipooperacao() != null)
						condition = vformacaocarga.getTipooperacao().getCdtipooperacao().equals(listaCarregamento.get(i).getTipooperacao().getCdtipooperacao());
					else
						condition = listaCarregamento.get(i).getTipooperacao().getCdtipooperacao() == null;
				}else{
					if(vformacaocarga.getCdfilialentrega() != null)
						condition = vformacaocarga.getCdfilialentrega().equals(listaCarregamento.get(i).getCdfilialentrega());
					else if(vformacaocarga.getPessoa().getCdpessoa() != null)
						condition = vformacaocarga.getPessoa().getCdpessoa().equals(listaCarregamento.get(i).getPessoa().getCdpessoa());
					else
						condition = listaCarregamento.get(i).getPessoa().getCdpessoa() == null;
				}
		else condition = Boolean.FALSE;
		return condition;
	}
	
}
