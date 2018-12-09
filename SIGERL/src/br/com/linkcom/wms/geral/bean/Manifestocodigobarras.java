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
import br.com.linkcom.wms.util.WmsUtil;

@Entity
@SequenceGenerator(name = "sq_manifestocodigobarras", sequenceName = "sq_manifestocodigobarras")
public class Manifestocodigobarras {
	
	private Integer cdmanifestocodigobarras;
	private Manifesto manifesto;
	private String codigo;
	private Boolean ativo;
	private Timestamp dt_inclusao;
	private Timestamp dt_alteracao;
	
	public Manifestocodigobarras(){
		
	}
	
	public Manifestocodigobarras(Manifesto manifesto){
		this.manifesto = manifesto;
		this.dt_inclusao = new Timestamp(System.currentTimeMillis());
		this.ativo = Boolean.TRUE;
		this.codigo = gerarCodigo(manifesto);
	}
	
	//Get's
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_manifestocodigobarras")
	public Integer getCdmanifestocodigobarras() {
		return cdmanifestocodigobarras;
	}
    @DisplayName("Manifesto")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cdmanifesto")
	public Manifesto getManifesto() {
		return manifesto;
	}
	public String getCodigo() {
		return codigo;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public Timestamp getDt_inclusao() {
		return dt_inclusao;
	}
	public Timestamp getDt_alteracao() {
		return dt_alteracao;
	}
	
	//Set'a
	public void setCdmanifestocodigobarras(Integer cdmanifestocodigobarras) {
		this.cdmanifestocodigobarras = cdmanifestocodigobarras;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public void setDt_inclusao(Timestamp dtInclusao) {
		dt_inclusao = dtInclusao;
	}
	public void setDt_alteracao(Timestamp dtAlteracao) {
		dt_alteracao = dtAlteracao;
	}
	
	/**
	 * Método gerador do código randomico da Etiqueta de Impressão.
	 * 
	 * @param manifesto
	 */
	public String gerarCodigo(Manifesto manifesto){
		try{
			StringBuilder codigo = new StringBuilder();
			codigo.append(manifesto.getCdmanifesto());
			codigo.append(WmsUtil.randInt(10000, 999999));
			codigo.append(WmsUtil.currentDate().toString().replace("-", ""));
			return codigo.toString();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("O cdmanifesto não pode ser nulo.");
			return null;
		}
	}
	
}
