package br.com.linkcom.wms.util.logistica;

import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.util.WmsException;

/**
 * 
 * @author Leonardo Guimarães
 *
 */
@Deprecated
public class EnderecoAux{
	
	private String endereco;
	private String enderecoComPonto;
	private String ar; 
	private String pr; 
	private String ru; 
	private String ni; 
	private String ap;
	private Enderecofuncao enderecofuncao;
	
	public EnderecoAux(String area,String endereco){
		this(endereco);
		
		if(area == null || area.length() > 2)
			throw new WmsException("Dados invalidos para criação de enderecoAux.");
		
		this.ar = area.length() < 2 ? "0"+area : area;
	}
	
	public EnderecoAux(String endereco){
		if(endereco == null || ((endereco.length() != 14) && (endereco.length() != 7)))
			throw new WmsException("Dados invalidos para criação de enderecoAux.");
		this.endereco = endereco.replace(".","");
		this.enderecoComPonto = endereco;
		
		this.ru = this.endereco.substring(0,3);
		this.pr = this.endereco.substring(3,6);
		
		if(endereco.length() == 14) {
			this.ni = this.endereco.substring(6,8);
			this.ap = this.endereco.substring(8,11);
		} else {
			this.ni = "";
			this.ap = "";
		}
	}
	
	public EnderecoAux(){
	}
	
	public String getEndereco() {
		return endereco;
	}
	public String getEnderecoComPonto() {
		return enderecoComPonto;
	}
	public String getEnderecoCompleto() {
		return getAr()+"."+enderecoComPonto;
	}
	public String getAr() {
		return ar;
	}
	public String getPr() {
		return pr;
	}
	public String getRu() {
		return ru;
	}
	public String getNi() {
		return ni;
	}
	public String getAp() {
		return ap;
	}
	public Enderecofuncao getEnderecofuncao() {
		return enderecofuncao;
	}
	public void setAr(String ar) {
		this.ar = ar;
	}
	public void setPr(String pr) {
		this.pr = pr;
	}
	public void setRu(String ru) {
		this.ru = ru;
	}
	public void setNi(String ni) {
		this.ni = ni;
	}
	public void setAp(String ap) {
		this.ap = ap;
	}
	public void setEnderecofuncao(Enderecofuncao enderecofuncao) {
		this.enderecofuncao = enderecofuncao;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public void setEnderecoComPonto(String enderecoComPonto) {
		this.enderecoComPonto = enderecoComPonto;
	}
	
	/**
	 * Retorna o número do endereco que é vizinho a esse endereço
	 * 
	 * Obs: caso o endereço seja o 00, retorna ""
	 * 
	 * @return endereço
	 */
	public String getEnderecoVizinho(){
		Integer ap = Integer.valueOf(this.ap);
		String prefix = ru+"."+pr+"."+ni+".";
		if(ap != 0){
			if(ap%2 == 0 ){
				return prefix+String.format("%03d", ap-1);
			}
			else{
				return prefix+String.format("%03d", ap+1);
			}
		}
		return "";
	}
	
	/**
	 * 
	 * Método que preenche os campos de endereço com zeros a esquerda
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @return String
	 * 
	 */
	public static String formatarEndereco(Endereco endereco) {
		String doisZeros = "%02d";
		String tresZeros = "%03d";
		String ponto = ".";
		
		String codigoArea = String.format(doisZeros, endereco.getArea().getCodigoAE());
		String rua = String.format(tresZeros, endereco.getRuaI());
		String predio = String.format(tresZeros, endereco.getPredioI());
		String nivel = String.format(doisZeros, endereco.getNivelI());
		String apto = String.format(tresZeros, endereco.getAptoI());
		
		return codigoArea + ponto + rua + ponto + predio + ponto + nivel + ponto + apto;
	}

	/**
	 * Verifica que o endereço possui todas as informações necessárias para carregá-lo.
	 * 
	 * @author Giovane Freitas
	 * @param endereco
	 * @return
	 */
	public static boolean isEnderecoCompleto(Endereco endereco) {
		if ((endereco == null) || (endereco.getArea() == null) || (endereco.getArea().getCodigoAE() == null) || 
		   (endereco.getRuaI() == null) || (endereco.getPredioI() == null) || (endereco.getNivelI() == null) || (endereco.getAptoI() == null))
			return false;
		else
			return true;
	}
	
}