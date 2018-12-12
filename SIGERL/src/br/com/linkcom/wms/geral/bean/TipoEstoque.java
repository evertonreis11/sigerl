package br.com.linkcom.wms.geral.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

// TODO: Auto-generated Javadoc
/**
 * The Class TipoEstoque.
 */
@Entity
public class TipoEstoque {
	
	/** The cd tipo estoque. */
	private Integer cdTipoEstoque;
	
	/** The descricao. */
	private String descricao;
	
	/** The permite expedicao. */
	private Boolean permiteExpedicao;
	
	/** The perfeito. */
	public static TipoEstoque PERFEITO = new TipoEstoque(1, "PERFEITO", Boolean.TRUE);
	
	/** The avariado. */
	public static TipoEstoque AVARIADO = new TipoEstoque(2, "AVARIADO", Boolean.FALSE);
	
	/** The extraviado. */
	public static TipoEstoque EXTRAVIADO = new TipoEstoque(3, "EXTRAVIADO", Boolean.FALSE);
	
	/**
	 * Instantiates a new tipo estoque.
	 */
	// default constructor
	public TipoEstoque() {}
	
	/**
	 * Instantiates a new tipo estoque.
	 *
	 * @param cdTipoEstoque the cd tipo estoque
	 * @param descricao the descricao
	 * @param permiteExpedicao the permite expedicao
	 */
	public TipoEstoque(Integer cdTipoEstoque, String descricao, Boolean permiteExpedicao) {
		this.cdTipoEstoque = cdTipoEstoque;
		this.descricao = descricao;
		this.permiteExpedicao = permiteExpedicao;
	}

	/**
	 * Gets the cd tipo estoque.
	 *
	 * @return the cd tipo estoque
	 */
	@Id
	public Integer getCdTipoEstoque() {
		return cdTipoEstoque;
	}
	
	/**
	 * Sets the cd tipo estoque.
	 *
	 * @param cdTipoEstoque the new cd tipo estoque
	 */
	public void setCdTipoEstoque(Integer cdTipoEstoque) {
		this.cdTipoEstoque = cdTipoEstoque;
	}
	
	/**
	 * Gets the descricao.
	 *
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	
	/**
	 * Sets the descricao.
	 *
	 * @param descricao the new descricao
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * Gets the permite expedicao.
	 *
	 * @return the permite expedicao
	 */
	public Boolean getPermiteExpedicao() {
		return permiteExpedicao;
	}
	
	/**
	 * Sets the permite expedicao.
	 *
	 * @param permiteExpedicao the new permite expedicao
	 */
	public void setPermiteExpedicao(Boolean permiteExpedicao) {
		this.permiteExpedicao = permiteExpedicao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof TipoEstoque){
			TipoEstoque tipoEstoque = (TipoEstoque) obj;
			try {
				if(tipoEstoque.getCdTipoEstoque().equals(this.getCdTipoEstoque()))
					return true;				
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
}
