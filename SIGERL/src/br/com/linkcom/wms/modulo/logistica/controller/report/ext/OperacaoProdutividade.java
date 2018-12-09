package br.com.linkcom.wms.modulo.logistica.controller.report.ext;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Id;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.wms.geral.bean.Ordemtipo;
/**
 * Classe cria para filtra corretamente o
 * relat�rio de Produtividade em rela��o a opera��o
 * @author C�ntia Nogueira
 *
 */
public class OperacaoProdutividade {
	//Atributos
	protected Integer cdoperacaoprodutividade;
	protected String nome;
	protected ArrayList<Ordemtipo> listaOrdemTipo= new ArrayList<Ordemtipo>();
	
	//Atributos est�ticos
	public static OperacaoProdutividade CONFERENCIA_DE_RECEBIMENTO = criaConfRecebimento();
	public static OperacaoProdutividade CONFERENCIA_DE_EXPEDICAO = criaConfExpedicao();
	public static OperacaoProdutividade MAPA_SEPARACAO = criaMapa();
	public static OperacaoProdutividade REABASTECIMENTO = criaReabastecimento();
	public static OperacaoProdutividade TRANSFERENCIA = criaTransferencia();
	public static OperacaoProdutividade ENDERECAMENTO = criaEnderecamento();
	public static OperacaoProdutividade CONTAGEM_DE_INVENTARIO = criaContagem();
	

	//Fun��es para criar os tipos 
	
	/**
	 * Cria o a opera��o Confer�ncia de recebimento com 
	 * todos os campos prenchidos
	 * @author C�ntia Nogueira	 
	 * @return 
	 */
	public static OperacaoProdutividade criaConfRecebimento(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(1);
		op.setNome("Confer�ncia de recebimento");
		op.getListaOrdemTipo().add(new Ordemtipo(1));
		op.getListaOrdemTipo().add(Ordemtipo.RECONFERENCIA_RECEBIMENTO);
		return op;
		
	}
	
	/**
	 * Cria o a opera��o Confer�ncia de expedi��o com 
	 * todos os campos prenchidos
	 * @author C�ntia Nogueira	 	
	 * @return 
	 */
	public static OperacaoProdutividade criaConfExpedicao(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(2);
		op.setNome("Confer�ncia de expedi��o");
		op.getListaOrdemTipo().add(Ordemtipo.CONFERENCIA_EXPEDICAO_1);
		op.getListaOrdemTipo().add(Ordemtipo.RECONFERENCIA_EXPEDICAO_1);
		return op;		
	}
	
	/**
	 * Cria o a opera��o mapa de separa��o com 
	 * todos os campos prenchidos
	 * @author C�ntia Nogueira	 	
	 * @return 
	 */
	public static OperacaoProdutividade criaMapa(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(3);
		op.setNome("Mapa de separa��o");
		op.getListaOrdemTipo().add(Ordemtipo.MAPA_SEPARACAO);		
		return op;
		
	}
	
	/**
	 * Cria o a opera��o reabastecimento com 
	 * todos os campos prenchidos
	 * @author C�ntia Nogueira	 	
	 * @return 
	 */
	public static OperacaoProdutividade criaReabastecimento(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(4);
		op.setNome("Reabastecimento");
		op.getListaOrdemTipo().add(Ordemtipo.REABASTECIMENTO_PICKING);		
		return op;
		
	}
	
	/**
	 * Cria o a opera��o transfer�ncia com 
	 * todos os campos prenchidos
	 * @author C�ntia Nogueira	 	
	 * @return 
	 */
	public static OperacaoProdutividade criaTransferencia(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(5);
		op.setNome("Transfer�ncia");
		op.getListaOrdemTipo().add(Ordemtipo.TRANSFERENCIA);		
		return op;
		
	}
	
	
	/**
	 * Cria o a opera��o endere�amento com 
	 * todos os campos prenchidos
	 * @author C�ntia Nogueira	 	
	 * @return 
	 */
	public static OperacaoProdutividade criaEnderecamento(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(6);
		op.setNome("Endere�amento");
		op.getListaOrdemTipo().add(Ordemtipo.ENDERECAMENTO_AVARIADO);
		op.getListaOrdemTipo().add(Ordemtipo.ENDERECAMENTO_FRACIONADO);
		op.getListaOrdemTipo().add(Ordemtipo.ENDERECAMENTO_PADRAO);
		return op;
		
	}
	
	/**
	 * Cria o a opera��o contagem de invent�rio
	 *  com todos os campos prenchidos
	 * @author C�ntia Nogueira	 	
	 * @return 
	 */
	public static OperacaoProdutividade criaContagem(){
		OperacaoProdutividade op = new OperacaoProdutividade();
		op.setCdoperacaoprodutividade(7);
		op.setNome("Contagem de invent�rio");
		op.getListaOrdemTipo().add(new Ordemtipo(11));
		
		return op;
		
	}
	
	/**
	 * Cria a lista de todos os tipos
	 * ordenada por nome da opera��o ( usada para combo)
	 * @return
	 * @author C�ntia Nogueira
	 * @see #criaConfExpedicao()
	 * @see #criaConfRecebimento()
	 * @see #criaContagem()
	 * @see #criaEnderecamento()
	 * @see #criaMapa()
	 * @see #criaReabastecimento()
	 * @see #criaTransferencia()
	 */
	public static ArrayList<OperacaoProdutividade> criaLista(){
		ArrayList<OperacaoProdutividade> lista = new ArrayList<OperacaoProdutividade>();
		lista.add(OperacaoProdutividade.criaConfExpedicao());
		lista.add(OperacaoProdutividade.criaConfRecebimento());
		lista.add(OperacaoProdutividade.criaContagem());
		lista.add(OperacaoProdutividade.criaEnderecamento());
		lista.add(OperacaoProdutividade.criaMapa());
		lista.add(OperacaoProdutividade.criaReabastecimento());
		lista.add(OperacaoProdutividade.criaTransferencia());
		return lista;
	}
	
	/**
	 * Cria um hashMap de todos os tipos
	 * com a chave de busca igual a id da Opera��oProdutividade,
	 * usada para localizar um elemento via id com os campos todos
	 * carregados
	 * @return
	 * @author C�ntia Nogueira
	 * @see #criaConfExpedicao()
	 * @see #criaConfRecebimento()
	 * @see #criaContagem()
	 * @see #criaEnderecamento()
	 * @see #criaMapa()
	 * @see #criaReabastecimento()
	 * @see #criaTransferencia()
	 */
	public static HashMap<Integer, OperacaoProdutividade> criaHashMap(){
		HashMap<Integer,OperacaoProdutividade> tabelaOP= new HashMap<Integer, OperacaoProdutividade>();
		tabelaOP.put(2,OperacaoProdutividade.criaConfExpedicao());
		tabelaOP.put(1, OperacaoProdutividade.criaConfRecebimento());
		tabelaOP.put(7,OperacaoProdutividade.criaContagem());
		tabelaOP.put(6,OperacaoProdutividade.criaEnderecamento());
		tabelaOP.put(3, OperacaoProdutividade.criaMapa());
		tabelaOP.put(4, OperacaoProdutividade.criaReabastecimento());
		tabelaOP.put(5,OperacaoProdutividade.criaTransferencia());
		return tabelaOP;
	}
	
	public OperacaoProdutividade() {
		
	}
	
	@Id
	public Integer getCdoperacaoprodutividade() {
		return cdoperacaoprodutividade;
	}
	
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	public ArrayList<Ordemtipo> getListaOrdemTipo() {
		return listaOrdemTipo;
	}
	
	public void setCdoperacaoprodutividade(Integer cdoperacaoprodutividade) {
		this.cdoperacaoprodutividade = cdoperacaoprodutividade;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setListaOrdemTipo(ArrayList<Ordemtipo> listaOrdemTipo) {
		this.listaOrdemTipo = listaOrdemTipo;
	}
	
	

}
