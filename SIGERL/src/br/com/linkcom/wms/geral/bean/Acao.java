package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_acao", sequenceName = "sq_acao")
@DisplayName("Ação")
public class Acao {

	public static String EXECUTAR_TRANSFERENCIA_MANUAL = "EXECUTAR_TRANSFERENCIA_MANUAL";
	public static String MONTAR_CARGA_PEDIDO_TROCA = "MONTAR_CARGA_PEDIDO_TROCA";
	public static String FINALIZAR_AGENDAMENTO = "FINALIZAR_AGENDAMENTO";
	public static String IMPRIMIR_ETIQUETA_SEPARACAO = "IMPRIMIR_ETIQUETA_SEPARACAO";
	public static String AJUSTAR_ESTOQUE = "AJUSTAR_ESTOQUE";
	public static String EDITAR_VERBA = "EDITAR_VERBA";
	public static String FINALIZAR_PICKING = "FINALIZAR_PICKING";
	public static String LIBERAR_PEDIDO_VENDA_CARREGAMENTO = "LIBERAR_PEDIDO_VENDA_CARREGAMENTO";
	public static String APROVA_PRE_DISP = "APROVA_PRE_DISP";
	public static String CANCELAR_MANIFESTO = "CANCELAR_MANIFESTO";
	public static String RE_IMPRIMIR_MANIFESTO = "RE_IMPRIMIR_MANIFESTO";
	public static String EDITAR_PRESTACAO_CONTA_FINALIZADA = "EDITAR_PRESTACAO_CONTA_FINALIZADA";
	public static String ACRESCIMO_FINANCEIRO_MANIFESTO = "ACRESCIMO_FINANCEIRO_MANIFESTO";
	public static String GERAR_BORDERO_MANIFESTO = "GERAR_BORDERO_MANIFESTO";
	public static String VINCULAR_PAPEL_USUARIO = "VINCULAR_PAPEL_USUARIO";
	public static String DESCONTO_FINANCEIRO_MANIFESTO = "DESCONTO_FINANCEIRO_MANIFESTO";
	public static String TRAVAR_PEDIDOS = "TRAVAR_PEDIDOS";
	public static String LIBERAR_PEDIDOS = "LIBERAR_PEDIDOS";
	public static String EXCLUIR_PEDIDOS = "EXCLUIR_PEDIDOS";
	
	protected Integer cdacao;
	protected String descricao;
	protected String key;	
	
	public Acao(){
		
	}
	
	public Acao(Integer cdacao, String descricao, String key){
		this.cdacao = cdacao;
		this.descricao = descricao;
		this.key = key;
	}
	
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_acao")
	public Integer getCdacao() {
		return cdacao;
	}
		
	@MaxLength(100)
	@DisplayName("Descrição")
	@Required
	public String getDescricao() {
		return descricao;
	}
	
	
	@MaxLength(50)
	@DisplayName("Chave")
	@Required
	public String getKey() {
		return key;
	}

	public void setCdacao(Integer cdacao) {
		this.cdacao = cdacao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Acaopapel){
			Acaopapel acaopapel = (Acaopapel) obj;
			return acaopapel.getAcao().getCdacao().equals(this.getCdacao());
		}
		return super.equals(obj);
	}
	
}
