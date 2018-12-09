package br.com.linkcom.wms.geral.bean.vo;

import java.util.List;

public class VolumeDivergenteVO {

	private int sq;
	private Integer cdprodutoprincipal;
	private String codigo;
	private String descricao; 
	private String endereco;
	private Integer qtde;
	private Integer sobra;
	private Integer cdordemtipo;
	private Integer volumes;
	
	

	public Integer getVolumes() {
		return volumes;
	}

	public void setVolumes(Integer volumes) {
		this.volumes = volumes;
	}

	public Integer getCdprodutoprincipal() {
		return cdprodutoprincipal;
	}

	public void setCdprodutoprincipal(Integer cdprodutoprincipal) {
		this.cdprodutoprincipal = cdprodutoprincipal;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	public Integer getSobra() {
		return sobra;
	}

	public void setSobra(Integer sobra) {
		this.sobra = sobra;
	}

	public Integer getCdordemtipo() {
		return cdordemtipo;
	}

	public void setCdordemtipo(Integer cdordemtipo) {
		this.cdordemtipo = cdordemtipo;
	}

	public int getSq() {
		return sq;
	}

	public void setSq(int sq) {
		this.sq = sq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final VolumeDivergenteVO other = (VolumeDivergenteVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	/**
	 * Retorna o indice do VolumeDivergenteVO que seja o mesmo endereço e o mesmo codigo
	 * @param volumesFiltrados
	 * @param vd
	 * @return
	 */
	public static int indexOfComMesmoEndereco(List<VolumeDivergenteVO> volumesFiltrados, VolumeDivergenteVO vd) {
		for (int i = 0; i < volumesFiltrados.size(); i++) {
			List<VolumeDivergenteVO> sublista = volumesFiltrados.subList(i, volumesFiltrados.size());
			int index = sublista.indexOf(vd);
			if(index!=-1){
				VolumeDivergenteVO vdNaLista = sublista.get(index);
				if(vdNaLista.getEndereco().equals(vd.getEndereco()))
					return i+index;
				else i+=index;
			}	
		}
		return -1;
	}

	
}
