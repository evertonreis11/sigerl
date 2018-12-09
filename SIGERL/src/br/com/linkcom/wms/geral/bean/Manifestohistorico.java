package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.persistence.DefaultOrderBy;

@Entity
@SequenceGenerator(name = "sq_manifestohistorico", sequenceName = "sq_manifestohistorico")
@DefaultOrderBy("dtalteracao")
public class Manifestohistorico {
	
	public static String CRIAR = "Manifesto criado. ";
	public static String EDITAR = "Manifesto editado. ";
	public static String CANCELAR = "Manifesto cancelado. Motivo: ";
	public static String IMPRESSO = "Manifesto impresso.";
	public static String RE_IMPRESSO = "Manifesto re-impresso. Motivo: ";
	public static String ENTREGA_EM_ANDAMENTO = "Manifesto liberado para realizar as entregas.";
	public static String PRESTACAO_CONTAS_EDICAO = "Prestação de contas: Alteração nos status das notas.";
	public static String PRESTACAO_CONTAS_FINALIZAR = "Prestação de contas: Finalizado.";
	public static String FECHAMENTO_FINANCEIRO_FINALIZADO = "Fechamento financeiro finalizado.";
	public static String FATURADO = "Manifesto faturado.";
	public static String AGUARDANDO_PRESTACAO = "Manifesto disponível para realização da Prestação de Contas.";
	
	private Integer cdmanifestohistorico;
	private Manifesto manifesto;
	private Manifestostatus manifestostatus;
	private Usuario usuario;
	private Timestamp dtalteracao;
	private String motivo;
	private Tipomanifestohistorico tipomanifestohistorico;
	
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestohistorico")
	public Integer getCdmanifestohistorico() {
		return cdmanifestohistorico;
	}
    @DisplayName("Núm. Manifesto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifesto")
	public Manifesto getManifesto() {
		return manifesto;
	}
    @DisplayName("Status")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifestostatus")
	public Manifestostatus getManifestostatus() {
		return manifestostatus;
	}
    @DisplayName("Usúario")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdusuario")
	public Usuario getUsuario() {
		return usuario;
	}
    @DisplayName("Dt. de Alteração")
	public Timestamp getDtalteracao() {
		return dtalteracao;
	}
    @DisplayName("Motivo")
	public String getMotivo() {
		return motivo;
	}
    @DisplayName("Tipo de Histótico")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdtipomanifestohistorico")
	public Tipomanifestohistorico getTipomanifestohistorico() {
		return tipomanifestohistorico;
	}
	
	
	//Set's
	public void setCdmanifestohistorico(Integer cdmanifestohistorico) {
		this.cdmanifestohistorico = cdmanifestohistorico;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	public void setManifestostatus(Manifestostatus manifestostatus) {
		this.manifestostatus = manifestostatus;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setDtalteracao(Timestamp dtalteracao) {
		this.dtalteracao = dtalteracao;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public void setTipomanifestohistorico(Tipomanifestohistorico tipomanifestohistorico) {
		this.tipomanifestohistorico = tipomanifestohistorico;
	}
	
}