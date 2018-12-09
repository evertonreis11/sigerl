package br.com.linkcom.wms.geral.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.File;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;


@Entity
@SequenceGenerator(name = "sq_arquivo", sequenceName = "sq_arquivo")
public class Arquivo implements File {

	protected Long cdarquivo;
	protected String nome;
	protected String tipoconteudo;
	protected Long tamanho;
	protected Timestamp dtmodificacao;
	
	protected byte[] content;

	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_arquivo")
	public Long getCdarquivo() {
		return cdarquivo;
	}
	public void setCdarquivo(Long id) {
		this.cdarquivo = id;
	}

	
	@Required
	@MaxLength(100)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@Required
	@MaxLength(50)
	public String getTipoconteudo() {
		return tipoconteudo;
	}
	
	public Long getTamanho() {
		return tamanho;
	}
	
	public Timestamp getDtmodificacao() {
		return dtmodificacao;
	}

	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setTipoconteudo(String tipoconteudo) {
		this.tipoconteudo = tipoconteudo;
	}
	
	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}
	
	public void setDtmodificacao(Timestamp dtmodificacao) {
		this.dtmodificacao = dtmodificacao;
	}
	
// -------------------------------------------------------
	
	@Transient
	public byte[] getContent() {
		return this.content;
	}

	@Transient
	public String getName() {
		return getNome();
	}

	@Transient
	public Long getCdfile() {
		if(getCdarquivo() == null){
			return null;
		}
		return new Long(getCdarquivo().intValue());
	}

	@Transient
	public String getContenttype() {
		return getTipoconteudo();
	}

	@Transient
	public Long getSize() {
		return getTamanho();
	}

	@Transient
	public Timestamp getTsmodification() {
		return getDtmodificacao();
	}

	public void setContenttype(String contenttype) {
		setTipoconteudo(contenttype);
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setCdfile(Long cdfile) {
		setCdarquivo(cdfile);
	}

	public void setName(String name) {
		setNome(name);
	}

	public void setSize(Long size) {
		setTamanho(size);
	}

	public void setTsmodification(Timestamp tsmodification) {
		setDtmodificacao(tsmodification);
	}

}
