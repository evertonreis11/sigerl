package br.com.linkcom.wms.geral.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.validation.BindException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Area;
import br.com.linkcom.wms.geral.bean.Dadologistico;
import br.com.linkcom.wms.geral.bean.Deposito;
import br.com.linkcom.wms.geral.bean.Endereco;
import br.com.linkcom.wms.geral.bean.EnderecoLinhaseparacao;
import br.com.linkcom.wms.geral.bean.Enderecofuncao;
import br.com.linkcom.wms.geral.bean.Enderecoproduto;
import br.com.linkcom.wms.geral.bean.Enderecostatus;
import br.com.linkcom.wms.geral.bean.Inventario;
import br.com.linkcom.wms.geral.bean.Inventariolote;
import br.com.linkcom.wms.geral.bean.Inventariostatus;
import br.com.linkcom.wms.geral.bean.Linhaseparacao;
import br.com.linkcom.wms.geral.bean.Motivobloqueio;
import br.com.linkcom.wms.geral.bean.Produto;
import br.com.linkcom.wms.geral.bean.vo.TotalVO;
import br.com.linkcom.wms.geral.dao.EnderecoDAO;
import br.com.linkcom.wms.geral.filter.IntervaloEndereco;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.AjustarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.BuscarEnderecoPopupFiltro;
import br.com.linkcom.wms.modulo.logistica.controller.process.filtro.ConsultarEstoqueFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.EnderecoDestinoFiltro;
import br.com.linkcom.wms.modulo.logistica.crud.filtro.EnderecoFiltro;
import br.com.linkcom.wms.util.CollectionsUtils;
import br.com.linkcom.wms.util.InsercaoInvalidaException;
import br.com.linkcom.wms.util.WmsException;
import br.com.linkcom.wms.util.WmsUtil;
import br.com.linkcom.wms.util.armazenagem.ConfiguracaoVO;
import br.com.linkcom.wms.util.logistica.EnderecoAux;
import br.com.linkcom.wms.util.neo.persistence.GenericService;
import br.com.linkcom.wms.util.recebimento.EtiquetasVO;
import br.com.linkcom.wmsconsole.window.EnderecoInvalidoException;

public class EnderecoService extends GenericService<Endereco> {

	private EnderecoDAO enderecoDAO;	
	private InventarioService inventarioService;
	private EnderecofuncaoService enderecofuncaoService;
	private TipoestruturaService tipoestruturaService;
	private TipoenderecoService tipoenderecoService;
	private TipopaleteService tipopaleteService;
	private EnderecostatusService enderecostatusService;
	private AreaService areaService;
	
	public void setEnderecoDAO(EnderecoDAO enderecoDAO) {
		this.enderecoDAO = enderecoDAO;
	}	
	
	public void setInventarioService(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	public void setEnderecofuncaoService(
			EnderecofuncaoService enderecofuncaoService) {
		this.enderecofuncaoService = enderecofuncaoService;
	}
	public void setTipoestruturaService(
			TipoestruturaService tipoestruturaService) {
		this.tipoestruturaService = tipoestruturaService;
	}
	public void setTipoenderecoService(TipoenderecoService tipoenderecoService) {
		this.tipoenderecoService = tipoenderecoService;
	}
	public void setTipopaleteService(TipopaleteService tipopaleteService) {
		this.tipopaleteService = tipopaleteService;
	}
	public void setEnderecostatusService(
			EnderecostatusService enderecostatusService) {
		this.enderecostatusService = enderecostatusService;
	}
	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}
		
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findByEnderecoPicking(String enderecoPicking)
	 * 
	 * @param enderecoPicking
	 * @param cdarea
	 * @return
	 */
	public Endereco findByEnderecoPicking(String enderecoPicking,Integer cdarea) {
		return enderecoDAO.findByEnderecoPicking(enderecoPicking,cdarea);
	}
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO. 
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @return Boolean
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#hasInterseccaoEndereco(Endereco)
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#hasInterseccaoEnderecofiltro(EnderecoFiltro)
	 * 
	 */
	public Boolean hasInterseccaoEndereco(Object obj) {
		if (obj instanceof Endereco) {
			Endereco endereco = (Endereco) obj;
			
			return enderecoDAO.hasInterseccaoEndereco(endereco);			
		}
		
		EnderecoFiltro enderecofiltro = (EnderecoFiltro) obj;
		return enderecoDAO.hasInterseccaoEnderecofiltro(enderecofiltro);
	}
	
	/**
	 *  
	 * Aplica a a��o que o usu�rio realizou sobre o registro de endere�os. 
	 * Configura a mensagem a ser exibida para o usu�rio logo ap�s a a��o ser executada.
	 *  
	 * @author Arantes
	 * 
	 * @param form
	 * @see br.com.linkcom.wms.geral.service.EnderecoService#acaoSobreEnderecos(WebRequestContext, Endereco, String) 
	 * 
	 */
	public void realizarAcaoEnderecos(WebRequestContext request, Endereco form) {
			
		String acao = form.getEstado();
		
		if(acao == null)
			acao = "";
		
		this.acaoSobreEnderecos(request, form, acao);
		
		if(acao.equalsIgnoreCase(Endereco.INSERIR) || acao.equalsIgnoreCase(Endereco.ALTERAR))
			request.addMessage("Registro salvo com sucesso.");
		
		else if(acao.equalsIgnoreCase(Endereco.EXCLUIR))
			request.addMessage("Registro(s) exclu�do(s) com sucesso.");
		
		if(acao != null) {
			request.getSession().setAttribute(Endereco.RESETFILTER, Boolean.TRUE);
			
		}
	}
	
	/**
	 * Retorna a lista com todos os objetos existente na listaA
	 * que n�o existe na listaB
	 * @param listaA
	 * @param listaB
	 * @return
	 * @author C�ntia Nogueira
	 */
	public List<Endereco> getListaNaoContem(List<Endereco> listaA, List<Endereco> listaB){
		List<Endereco> listaRetorno = new ArrayList<Endereco>();
		for(Endereco endereco: listaA){
			if(!listaB.contains(endereco)){
				listaRetorno.add(endereco);
			}
		}
		
		return listaRetorno;
	}

	/**
	 * Verifica se a todos os endere�os possuem a caracter�stica
	 * @param listaEndereco
	 * @param enderecofuncao
	 * @return
	 * @author C�ntia Nogueira
	 */
	public boolean isAllCaracterisca(List<Endereco> listaEndereco, Enderecofuncao enderecofuncao){
		//FIXME : tentar fazer isso apenas com uma consulta no Banco, buscar todos os endere�os para a mem�ria � muito pesado
		
		for(Endereco endereco: listaEndereco){
			  if(!(endereco.getEnderecofuncao().getCdenderecofuncao().equals(enderecofuncao.getCdenderecofuncao()))){
				  listaEndereco = new ArrayList<Endereco>();
				  listaEndereco.add(endereco);
				  
				  return false;
			  }
		  }
		  
		  return true;
	}
	
	/**
	 * Verifica se todos os endere�os s�o blocados
	 * @param listaEndereco
	 * @return
	 * @see #isAllCaracterisca(List, Enderecofuncao)
	 * @author C�ntia Nogueira
	 */
	  public boolean isAllBlocado(List<Endereco> listaEndereco){
		  return isAllCaracterisca(listaEndereco, Enderecofuncao.BLOCADO);
	  }
  
	  /**
	   * Verifica se h� pelos menos um endere�o
	   * blocado
	   * @param listaEndereco
	   * @return
	   * @author C�ntia Nogueira
	   */
	  public boolean HasBlocado(List<Endereco> listaEndereco){
		  for(Endereco endereco: listaEndereco){
			  if((endereco.getEnderecofuncao().getCdenderecofuncao().equals(Enderecofuncao.BLOCADO.getCdenderecofuncao()))){
				  listaEndereco = new ArrayList<Endereco>();
				  listaEndereco.add(endereco);
				  return true;
			  }
		  }
		  
		  return false;
	  }
	  
	  /**
	   * Verifica se algum da lista de endere�o foi bloqueado para
	   * invent�rio
	   * @param listaEndereco
	   * @return
	   * @author C�ntia Nogueira
	   */
	  private boolean isBloqueadoInventario(List<Endereco> listaEndereco){
		  for(Endereco endereco: listaEndereco){
			  if(endereco.getEnderecostatus().getCdenderecostatus().
					  equals(Enderecostatus.BLOQUEADO.getCdenderecostatus())){
				  if(endereco.getMotivobloqueio()!=null 
					  && endereco.getMotivobloqueio().getCdmotivobloqueio().
					  equals(Motivobloqueio.INVENTARIO.getCdmotivobloqueio())){	
					  listaEndereco = new ArrayList<Endereco>();
					  listaEndereco.add(endereco);
					  return true;
				  }
			  }
		  }
		  return false;
	  }
	  
	  /**
	   * Faz a string do endere�o de pr�dio blocado
	   * @param endereco
	   * @return
	   */
	  private String enderecoPredioBlocado(Endereco endereco){
		  Endereco enderecoErro = load(endereco);
			String end= "";
			if(endereco.getArea().getCodigo()<9){
				end="0" + endereco.getArea().getCodigo() +" ";
			}else{
				end =endereco.getArea().getCodigo()+" ";
			}
			end += enderecoErro.getEndereco().substring(0,7);
			return end;
	  }
	  
	  
	
	/**
	 * 
	 * Faz inser��o, exclus�o ou atualiza��o no registro de endere�os
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @param form
	 * @param acao
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#atualizarEndereco(Endereco)
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#excluirEndereco(Endereco)
	 * @see  br.com.linkcom.wms.geral.dao.EnderecoDAO#findEnderecoByPredio(Area, Integer, Integer, Integer, Integer)
	 * @see #recuperarEnderecosCadastrados(Endereco)
	 * @see #isAllBlocado(List)
	 * @see #HasBlocado(List)
	 * 
	 */
	private void acaoSobreEnderecos(WebRequestContext request, Endereco endereco, String acao) {
		if(acao.equalsIgnoreCase(Endereco.INSERIR)) {
			List<Endereco> listaEndereco =enderecoDAO.findEnderecoByPredio(endereco.getArea(), endereco.getRuaI(), 
					endereco.getRuaF(), endereco.getPredioI(), endereco.getPredioF(), endereco.getLado());
			if(listaEndereco!=null){
				if(endereco.getEnderecofuncao().getCdenderecofuncao().
						equals(Enderecofuncao.BLOCADO.getCdenderecofuncao())){
					if(!isAllBlocado(listaEndereco)){					
						throw new WmsException("Pr�dio " +enderecoPredioBlocado(listaEndereco.get(0)) +" n�o � blocado.");	
					}else{
						if(isBloqueadoInventario(listaEndereco)){							
							throw new WmsException("O pr�dio " +enderecoPredioBlocado(listaEndereco.get(0))  +" est� bloqueado para invent�rio.");	
						}
					}
				}else{
					if(HasBlocado(listaEndereco)){ 							
						
						throw new WmsException("Pr�dio " +enderecoPredioBlocado(listaEndereco.get(0))  +" � blocado.");	
					}
				}
			}
			saveEnderecos(request, endereco);
			
		} else if(acao.equalsIgnoreCase(Endereco.ALTERAR)) {
				
				List<Endereco> enderecosModificados=recuperarEnderecosCadastrados(endereco, endereco.getEnderecoFiltro());
				List<Endereco> listaComparacao = getListaNaoContem(enderecoDAO.findEnderecoByPredio(endereco.getArea(), endereco.getRuaI(), 
						endereco.getRuaF(), endereco.getPredioI(), endereco.getPredioF(), endereco.getLado()), enderecosModificados);
				
				if(endereco.getEnderecofuncao().getCdenderecofuncao().
						equals(Enderecofuncao.BLOCADO.getCdenderecofuncao())){
					if(!isAllBlocado(listaComparacao)){
						Endereco enderecoErro = load(listaComparacao.get(0));
						String end= "";
						if(listaComparacao.get(0).getArea().getCodigo()<9){
							end="0" + listaComparacao.get(0).getArea().getCodigo() +" ";
						}else{
							end =listaComparacao.get(0).getArea().getCodigo()+" ";
						}
						end += enderecoErro.getEndereco().substring(0,7);	
						
						throw new WmsException("Pr�dio " +end +" n�o � blocado.");						
					}					
				}else{
					if(HasBlocado(listaComparacao)){
						Endereco enderecoErro = load(listaComparacao.get(0));
						String end= "";
						if(listaComparacao.get(0).getArea().getCodigo()<9){
							end="0" + listaComparacao.get(0).getArea().getCodigo() +" ";
						}else{
							end =listaComparacao.get(0).getArea().getCodigo()+" ";
						}
						end += enderecoErro.getEndereco().substring(0,7);						
						
						throw new WmsException("Pr�dio " +end +" � blocado.");						
					}
				}
					
				enderecoDAO.atualizarEndereco(endereco, enderecosModificados);

		} else if(acao.equalsIgnoreCase(Endereco.EXCLUIR)) {
			setValoresFiltro(endereco);
			enderecoDAO.excluirEndereco(endereco, endereco.getEnderecoFiltro());
		} else if (acao.equalsIgnoreCase(Endereco.BLOQUEIO)){
			List<Endereco> enderecosModificados = recuperarEnderecosCadastrados(endereco, endereco.getEnderecoFiltro());
			
			enderecoDAO.alterarBloqueioEndereco(endereco, enderecosModificados);			
		}
	}
	
	/**
	 * Salva todas as combina��es de endere�os
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param request
	 * @param endereco
	 */
	private void saveEnderecos(WebRequestContext request, Endereco endereco) {
		
		int ruaI = endereco.getRuaI().intValue();
		int ruaF = endereco.getRuaF().intValue();
		int predioI = endereco.getPredioI().intValue();
		int predioF = endereco.getPredioF().intValue();
		int nivelI = endereco.getNivelI().intValue();
		int nivelF = endereco.getNivelF().intValue();
		int aptoI = endereco.getAptoI().intValue();
		int aptoF = endereco.getAptoF().intValue();		
		
		int totalPredio = (predioF - predioI + 1);
		if(endereco.getLado() != null){
			totalPredio = (int)Math.floor(totalPredio / 2);
			if(endereco.getLado()){
				if(predioF % 2 == 0 && predioI % 2 == 0)
					totalPredio++;
			}else{
				if(predioF % 2 != 0 && predioI % 2 != 0)
					totalPredio++;
			}
		}
		
		int totalEnderecos = (ruaF - ruaI + 1) * (totalPredio) * (nivelF - nivelI + 1) * (aptoF - aptoI + 1);
		String maxEnderecos = WmsUtil.getConfig(ConfiguracaoVO.MAX_ENDERECOS_CRIADOS_POR_VEZ);
		if (maxEnderecos != null && !maxEnderecos.isEmpty()){
			if(totalEnderecos + 1 > Integer.valueOf(maxEnderecos)){
				request.addError("Devido a configura��es do sistema, o m�ximo de endere�os cadastrados n�o deve ser maior que "+ maxEnderecos + ".");
				return;
			}
		}
		
		for (int r=ruaI; r<=ruaF; r++) {
			endereco.setRua(r);
			
			for (int p=predioI; p<=predioF; p++) {
				if(!carregarLadoPredio(endereco, p)) {
					continue;
				}
				
				for (int n=nivelI; n<=nivelF; n++) {
					endereco.setNivel(n);
						
					for (int a=aptoI; a<=aptoF; a++) {
						endereco.setApto(a);	
						
						if (endereco.isBloqueado())
							endereco.setEnderecostatus(Enderecostatus.BLOQUEADO);
						else
							endereco.setEnderecostatus(Enderecostatus.DISPONIVEL);
						
						this.saveOrUpdate(endereco);
						
						if (endereco.getListaLinhaseparacao() != null){
							for (Linhaseparacao linha : endereco.getListaLinhaseparacao()){
								EnderecoLinhaseparacao enderecoLinha = new EnderecoLinhaseparacao();
								enderecoLinha.setEndereco(endereco);
								enderecoLinha.setLinhaseparacao(linha);
								EnderecoLinhaseparacaoService.getInstance().saveOrUpdate(enderecoLinha);
							}
						}
						
						endereco.setCdendereco(null);
					}
				}
			}
		}
	}
 
	/**
	 * 
	 * Carrega o valor do pr�dio de acordo com o lado: par / �mpar / todos
	 * 
	 * @author Arantes
	 * 
	 * @param lado
	 * @param p
	 * @return int
	 * 
	 */
	private boolean carregarLadoPredio(Endereco endereco, int p) {
		boolean result = false;
		
		if(endereco.getLado() != null) {
			if(endereco.getLado()) {
				if((p % 2) == 0) {
					endereco.setPredio(p);
					result = true;
				}
				
			} else {
				if((p % 2) != 0) {
					endereco.setPredio(p);
					result = true;
				}
			}
			
		} else {
			endereco.setPredio(p);
			result = true;
			
		}
		
		return result;
	}

	/**
	 * 
	 * M�todo de refer�ncia ao DAO. 
	 * 
	 * @author Arantes
	 * @param enderecoFiltro 
	 * 
	 * @param filtro
	 * @return List<Endereco>
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#recuperarEnderecosCadastrados(Endereco)
	 * 
	 */
	public List<Endereco> recuperarEnderecosCadastrados(Endereco form, EnderecoFiltro enderecoFiltro) {
		return enderecoDAO.recuperarEnderecosCadastrados(form, enderecoFiltro);
	}
	
	/**
	 * 
	 * Recupera a quantidade de endere�os candidatos a atualiza��o
	 * 
	 * @author Arantes
	 * 
	 * @param form
	 * @return int
	 * 
	 */
	public int recuperarQtdEnderecosFornecida(Endereco form) {
		int ruaI = form.getRuaI().intValue();
		int ruaF = form.getRuaF().intValue();
		int predioI = form.getPredioI().intValue();
		int predioF = form.getPredioF().intValue();
		int nivelI = form.getNivelI().intValue();
		int nivelF = form.getNivelF().intValue();
		int aptoI = form.getAptoI().intValue();
		int aptoF = form.getAptoF().intValue();
		
		int qtdEnderecosCandidatos = 0;
		
		for (int r=ruaI; r<=ruaF; r++) {
			for (int p=predioI; p<=predioF; p++) {
				for (int n=nivelI; n<=nivelF; n++) {
					for (int a=aptoI; a<=aptoF; a++) {
						qtdEnderecosCandidatos++;
					}
				}
			}
		}
		
		return qtdEnderecosCandidatos;
	}
	
	/**
	 * 
	 * M�todo que valida a obrigatoriedade dos campos
	 * 
	 * @author Arantes
	 * 
	 * @param bean
	 * @param errors
	 * 
	 */
	public Boolean validarCamposObrigatorios(Endereco bean, BindException errors) {
		Boolean result = Boolean.TRUE;

		if(bean != null) {			
			if((bean.getArea() == null) || (bean.getArea().getCdarea() == null)) {
				errors.reject("001", "O campo �rea � obrigat�rio.");
				result = Boolean.FALSE;
			}
			
			if(bean.getRuaI() == null) {
				errors.reject("002", "O campo Rua in�cio � obrigat�rio.");
				result = Boolean.FALSE;
			}
			
			if(bean.getRuaF() == null) {
				errors.reject("003", "O campo Rua fim � obrigat�rio.");
				result = Boolean.FALSE;
			}
			
			if(bean.getPredioI() == null) {
				errors.reject("004", "O campo Pr�dio in�cio � obrigat�rio.");
				result = Boolean.FALSE;
			}

			if(bean.getPredioF() == null) {
				errors.reject("005", "O campo Pr�dio fim � obrigat�rio.");
				result = Boolean.FALSE;
			}
			
			if(bean.getAptoI() == null) {
				errors.reject("006", "O campo Apto in�cio � obrigat�rio.");
				result = Boolean.FALSE;
			}
			
			if(bean.getAptoF() == null) {
				errors.reject("007", "O campo Apto fim � obrigat�rio.");
				result = Boolean.FALSE;
			}
			
			if((bean.getEstado().equals(Endereco.INSERIR)) || (bean.getEstado().equals(Endereco.ALTERAR))) {
				if((bean.getTipoestrutura() == null) || (bean.getTipoestrutura().getCdtipoestrutura() == null)) {
					errors.reject("008", "O campo Estrutura de armazenagem � obrigat�rio.");
					result = Boolean.FALSE;
				}
				
				if((bean.getEnderecofuncao() == null) || (bean.getEnderecofuncao().getCdenderecofuncao() == null)) {
					errors.reject("009", "O campo Caracter�stica � obrigat�rio.");
					result = Boolean.FALSE;
				}
				
				if((bean.getTipoendereco() == null) || (bean.getTipoendereco().getCdtipoendereco() == null)) {
					errors.reject("010", "O campo Tipo de endere�o � obrigat�rio.");
					result = Boolean.FALSE;
				}
				
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * M�todo que valida a consist�ncia dos n�meros que comp�em o endere�o
	 * 
	 * @author Arantes
	 * 
	 * @param bean
	 * @return Boolean
	 * 
	 */
	public Boolean validarConsistenciaEndereco(Endereco bean) {
		try {
			Integer.parseInt(bean.getRuaI().toString());
			Integer.parseInt(bean.getRuaF().toString());
			Integer.parseInt(bean.getPredioI().toString());
			Integer.parseInt(bean.getPredioF().toString());
			Integer.parseInt(bean.getNivelI().toString());
			Integer.parseInt(bean.getNivelF().toString());
			Integer.parseInt(bean.getAptoI().toString());
			Integer.parseInt(bean.getAptoF().toString());
			
		} catch (NumberFormatException e) {
			return Boolean.FALSE;
			
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * 
	 * M�todo que verifica as vari�veis de inst�ncia n�vel inicial e final. Caso alguma seja null, configura o seu valor para 0. 
	 * 
	 * @author Arantes
	 * 
	 * @param form
	 * 
	 */
	public void configurarNivel(Object form) {		
		if (form instanceof Endereco) {
			Endereco endereco = (Endereco) form;
			
			if(endereco.getNivelI() == null)
				endereco.setNivelI(new Integer(0));
			
			if(endereco.getNivelF() == null)
				endereco.setNivelF(new Integer(0));
			
		} else if (form instanceof EnderecoFiltro) {
			EnderecoFiltro endereco = (EnderecoFiltro) form;
			
			if(endereco.getNivelI() == null)
				endereco.setNivelI(new Integer(0));
			
			if(endereco.getNivelF() == null)
				endereco.setNivelF(new Integer(0));
			
		}
	}
	
	/**
	 * 
	 * M�todo que reinicia algumas vari�veis de inst�ncia do bean endere�o para quando for inserir novos dados.
	 * 
	 * @author Arantes
	 * 
	 * @param form
	 */
	public void reiniciarCaracteristicasintevalo(Endereco form) {
		form.setTipoestrutura(null);
		form.setTipopalete(null);
		form.setTipoendereco(null);
		form.setEnderecostatus(null);
		form.setEnderecofuncao(null);
		form.setLarguraexcedente(Boolean.FALSE);
	}
	
	/**
	 * 
	 * M�todo de refer�ncia ao DAO.
	 * Recupera os endere�os de destino.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#recuperarEnderecosDestino(EnderecoDestinoFiltro)
	 * 
	 * @param filtro
	 * @return
	 * @throws SQLException 
	 */
	public List<Endereco> recuperarEnderecosDestino(EnderecoDestinoFiltro filtro) throws SQLException {
		return enderecoDAO.recuperarEnderecosDestino(filtro);
	}
	
	/**
	 * Valida se os valores iniciais e finais s�o v�lidos.
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return String
	 * 
	 */
	public String validarRangenumeros(EnderecoDestinoFiltro filtro) {
		String valida = "";
		
		if(filtro.getRuaI() > filtro.getRuaF())
			valida += "O valor final da rua n�o pode ser menor que o valor inicial.\n";
		
		if(filtro.getPredioI() > filtro.getPredioF())
			valida += "O valor final do pr�dio n�o pode ser menor que o valor inicial.\n";
			
		if(filtro.getNivelI() > filtro.getNivelF())
			valida += "O valor final do n�vel n�o pode ser menor que o valor inicial.\n";
		
		if(filtro.getAptoI() > filtro.getAptoF())
			valida += "O valor final do apto n�o pode ser menor que o valor inicial.\n";
		
		return valida;
	}
	/**
	 * Verifica se o predio existe na lista
	 * @param listaendereco
	 * @param endereco
	 * @return
	 * @author C�ntia Nogueira
	 */
	private boolean existePredio(List<Endereco> listaendereco, Endereco endereco){
		for(Endereco enderecoLista: listaendereco){
			if(enderecoLista.getArea().getCdarea().equals(endereco.getArea().getCdarea()) 
					&& (enderecoLista.getRua().equals(endereco.getRua())) 
					&&(enderecoLista.getPredio().equals(endereco.getPredio())) ){
				return true;
			}
		}
		return false;
	}
	/**
	 * Remove os pr�dios blocados que est�o repetidos na lista
	 * @param listaEndereco
	 * @return
	 * @see #existePredio(List, Endereco)
	 * @author C�ntia Nogueira
	 */
	private List<Endereco> removeEnderecospredioBlocadoRepetido(List<Endereco> listaEndereco){
		List<Endereco> listaEnderecoAuxiliar= new ArrayList<Endereco>();
		
		for(Endereco endereco:listaEndereco){
		 if(!endereco.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
			 listaEnderecoAuxiliar.add(endereco);
		 }else{
			 if(!existePredio(listaEnderecoAuxiliar, endereco)){
				 listaEnderecoAuxiliar.add(endereco);
			 }
		 }
		}
		
		return listaEnderecoAuxiliar;
	}
	/**
	 * Gera o relat�rio de endere�amento dos dep�sitos
	 * 
	 * @author Leonardo Guimar�es
	 * @author C�ntia Nogueira
	 * 
	 * @see #getEnderecosByIntervalo(Endereco)
	 * @see #setCodigoEtiqueta(Endereco, EtiquetasVO)
	 * @see #removeEnderecospredioBlocadoRepetido(List)	
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findfindForReportEtiquetaEnderecosCount(Endereco)
	 * @see #findForReportEtiquetaEnderecos(Endereco)
	 * 
	 * @param endereco
	 * @return
	 */
	public IReport generateReportEtiquetasDepositos(Endereco endereco) {
		Report report = new Report("RelatorioEtiquetaEnderecoDeposito");
		setValoresFiltro(endereco);
		Long numResultados = enderecoDAO.findfindForReportEtiquetaEnderecosCount(endereco, endereco.getEnderecoFiltro());
		if(numResultados > 5000){
			throw new WmsException("Ser�o geradas mais de 5.000 etiquetas. Favor, usar o " +
					"filtro para reduzir a quantidade.");
		}
		List<Endereco> listaEndereco = findForReportEtiquetaEnderecos(endereco, endereco.getEnderecoFiltro());
		listaEndereco=removeEnderecospredioBlocadoRepetido(listaEndereco);
		List<EtiquetasVO> listaEtiquetas = new ArrayList<EtiquetasVO>();
		
		for(Endereco end : listaEndereco){
			
			EtiquetasVO etiquetasVO = new EtiquetasVO();			
			EnderecoAux enderecoAux = new EnderecoAux(end.getArea().getCodigo().toString(),end.getEndereco());
			etiquetasVO.setCodigobarras(makeCodigoForEtiquetaEnderecos(end));
			etiquetasVO.setCaracteristicaCodigo(end.getEnderecofuncao().getCdenderecofuncao().toString());
			if(end.getEnderecofuncao().equals(Enderecofuncao.PICKING) || end.getEnderecofuncao().equals(Enderecofuncao.PULMAO)){
				etiquetasVO.setDescricaoEndereco("�rea.Rua.Pr�dio.N�vel.Apto");
				etiquetasVO.setEndereco(enderecoAux.getAr() + "." + enderecoAux.getEnderecoComPonto());
				etiquetasVO.setCodigobarras(makeCodigoForEtiquetaEnderecos(end));
				if(end.getEnderecofuncao().equals(Enderecofuncao.PICKING)){
					setCodigoEtiqueta(end, etiquetasVO);
					etiquetasVO.setCaracteristica("PICKING");
				}
				else{
					etiquetasVO.setCaracteristica("PULM�O");
				}
			}else{
				if(end.getEnderecofuncao().equals(Enderecofuncao.BLOCADO)){
					etiquetasVO.setCodigobarras(makeCodigoForEtiquetaEnderecosBlocado(end));
					etiquetasVO.setDescricaoEndereco("�rea.Rua.Pr�dio");
					etiquetasVO.setCaracteristica("BLOCADO");
					etiquetasVO.setEndereco(enderecoAux.getAr() + "." + enderecoAux.getEnderecoComPonto().substring(0,7));
				}
			}
			
			listaEtiquetas.add(etiquetasVO);
			
		}
		
		report.setDataSource(listaEtiquetas);
		
		return report;
	}
	
	/**
	 * Seta o c�digo e a descri��o do produto na etiqueta caso algum produto possua o endere�o de picking especificado
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param end
	 * @param etiquetasVO
	 */
	private void setCodigoEtiqueta(Endereco end, EtiquetasVO etiquetasVO) {
		if(end.getListaDadologistico() != null && !end.getListaDadologistico().isEmpty()){
			Dadologistico dadologistico = end.getListaDadologistico().iterator().next();
			String string = dadologistico.getProduto().getProdutoprincipal() != null ? dadologistico.getProduto().getProdutoprincipal().getCodigo() : dadologistico.getProduto().getCodigo();
			string += dadologistico.getProduto().getComplementocodigobarras() != null ? " - " + dadologistico.getProduto().getComplementocodigobarras() : "";
			etiquetasVO.setCodProduto(string);
			etiquetasVO.setDescricao(dadologistico.getProduto().getProdutoprincipal() != null ? dadologistico.getProduto().getProdutoprincipal().getDescricao() : dadologistico.getProduto().getDescricao());
		}
	}
	
	/**
	 * Gera o c�digo de barras para as etiquetas de endere�os dos dep�sitos
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param end
	 * @return
	 */
	private String makeCodigoForEtiquetaEnderecos(Endereco end) {
		String codigo = "";
		codigo += end.getEnderecofuncao().getCdenderecofuncao();
		String endereco = end.getEndereco().replace(".", "");
		codigo += end.getArea().getCodigo() < 9 ? "0" + end.getArea().getCodigo() : end.getArea().getCodigo();
		codigo += endereco.substring(0,3);
		codigo += endereco.substring(3,6);
		codigo += endereco.substring(6,8);
		codigo += endereco.substring(8,11);
		
		return codigo;
	}
	
	/**
	 * Gera o c�digo de barras para as etiquetas de endere�os blocado
	 * @param end
	 * @return
	 * @author C�ntia Nogueira
	 */
	private String makeCodigoForEtiquetaEnderecosBlocado(Endereco end) {
	
		String codigo = "";
		
		codigo += end.getEnderecofuncao().getCdenderecofuncao();		
		String endereco = end.getEndereco().replace(".", "");
		codigo += end.getArea().getCodigo() < 9 ? "0" + end.getArea().getCodigo() : end.getArea().getCodigo();
		codigo += endereco.substring(0,3);
		codigo += endereco.substring(3,6);		
		
		return codigo;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findForReportEtiquetaEnderecos(Endereco endereco)
	 * 
	 * @param enderecoNew
	 * @param enderecoFiltro 
	 * @return
	 */
	private List<Endereco> findForReportEtiquetaEnderecos(Endereco enderecoNew, EnderecoFiltro enderecoFiltro) {
		return enderecoDAO.findForReportEtiquetaEnderecos(enderecoNew, enderecoFiltro);
	}	
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see #br.com.linkcom.wms.geral.dao.DepositoDAO.getNumeroEnderecos()
	 * 
	 * @return
	 */
	public Long getNumeroEnderecosDeposito() {
		return enderecoDAO.getNumeroEnderecosDeposito();
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * @param inventariolote
	 * @return
	 */
	public Long getNumeroEnderecosLote(IntervaloEndereco inventariolote) {
		return enderecoDAO.getNumeroEnderecosLote(inventariolote);
	}
	
	/**
	 * Verifica se o endere�o de largura excedente tem produto com largura excedente
	 * @param endereco
	 * @return true or false
	 * @author C�ntia Nogueira
	 */
	private boolean hasProdutoLargura(Endereco endereco, Deposito deposito){
		if(endereco.getApto()%2==0){
			return false;
		}
		for(Enderecoproduto enderecoproduto: endereco.getListaEnderecoproduto()){
			if(enderecoproduto!=null &&  enderecoproduto.getProduto()!=null &&
					enderecoproduto.getProduto().getListaDadoLogistico()!=null){
				for(Dadologistico dado:enderecoproduto.getProduto().getListaDadoLogistico()){
					if(dado.getDeposito()!=null && 
							dado.getDeposito().getCddeposito().equals( (deposito!=null ? deposito : WmsUtil.getDeposito().getCddeposito()) )
							&& dado.getLarguraexcedente()){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Desbloqueia os endere�os seguindo a regra para o
	 * cancelamento do inventario
	 * 
	 * @author Leonardo Guimar�es
	 * @author C�ntia Nogueira
	 * 
	 * @param inventario
	 */
	public void desbloquearEnderecos(Inventario inventario, Inventariostatus inventariostatus, Deposito deposito) {
		if(inventario == null || inventario.getCdinventario() == null || inventario.getListaInventariolote() == null)
			throw new WmsException("Dados inv�lidos para execu��o da fun��o \"desbloquearEnderecos\"");
		for (Inventariolote inventariolote : inventario.getListaInventariolote()) {
			List<Endereco> listaEnderecos = getEnderecosByInventarioLote(inventariolote);
			if(listaEnderecos!=null && !listaEnderecos.isEmpty()){			
				boolean atualizouVizinhoExcedente=false;
				for(Endereco endereco: listaEnderecos){
					if(endereco.getMotivobloqueio() != null && endereco.getMotivobloqueio().equals(Motivobloqueio.INVENTARIO)){
						endereco.setMotivobloqueio(null);
						
						if(!atualizouVizinhoExcedente){
							if(endereco.getLarguraexcedente() && endereco.getApto()%2!=0 && hasProdutoLargura(endereco, deposito)){								
								EnderecoAux enderecoAux = new EnderecoAux(endereco.getEndereco());								
								enderecoAux.setAp(endereco.getApto()+"");
								Endereco vizinho = findEndereco(enderecoAux.getEnderecoVizinho(), endereco.getArea());
								vizinho.setEnderecostatus(Enderecostatus.EMPRESTIMO);
								updateStatusEndereco(vizinho);
								vizinho.setMotivobloqueio(null);
								updateMotivoBloqueioEndereco(vizinho);
								updateMotivoBloqueioEndereco(endereco);
								atualizouVizinhoExcedente=true;
								pAtualizarEndereco(endereco);
							}else{
								updateMotivoBloqueioEndereco(endereco);
								pAtualizarEndereco(endereco);
								atualizouVizinhoExcedente=false;
							}
						
						}else{
							atualizouVizinhoExcedente=false;
							
						}
					}
				}
			}
		
		}
			
		if(inventariostatus.equals(Inventariostatus.CANCELADO)) {
			inventario.setInventariostatus(Inventariostatus.CANCELADO);
			inventarioService.updateInventarioStatus(inventario);
		}
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#updateMotivoBloqueioEndereco(Endereco endereco)
	 * 
	 * @param endereco
	 */
	private void updateMotivoBloqueioEndereco(Endereco endereco) {
		enderecoDAO.updateMotivoBloqueioEndereco(endereco);
	}

	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param endereco
	 */
	public void updateStatusEndereco(Endereco endereco) {
		enderecoDAO.updateStatusEndereco(endereco);
	}

	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findEndereco(String endereco, Area area)
	 * 
	 * @param enderecoVisinho
	 * @param area
	 * @return
	 */
	public Endereco findEndereco(String endereco, Area area) {
		return enderecoDAO.findEndereco(endereco,area);
	}

	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#getEnderecosByInventarioLote(Inventariolote inventariolote)
	 * 
	 * @param inventariolote
	 * @return
	 */
	private List<Endereco> getEnderecosByInventarioLote(Inventariolote inventariolote) {
		return enderecoDAO.getEnderecosByInventarioLote(inventariolote);
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findEnderecoPredio(Integer rua, Integer predio, Area area, boolean primeiro)
	 * 
	 * @param area 	  
	 * @param primeiro
	 * @return
	 */
	public Endereco findEnderecoPredio(Integer rua, Integer predio, Area area, boolean primeiro){
		return enderecoDAO.findEnderecoPredio(rua, predio,area, primeiro);
	}
	
	/**
	 * 
	 * Encontra o endere�o com os dados fornecidos
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findEndereco(Endereco)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return Endereco
	 * 
	 */
	public Endereco findEndereco(Endereco endereco) {
		return enderecoDAO.findEndereco(endereco);
	}
	
	/**
	 * 
	 * Recupera uma lista de endere�os para o ajuste de estoque.
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findByEnderecoProduto(AjustarEstoqueFiltro)
	 * 
	 * @author Arantes
	 * 
	 * @param filtro
	 * @return List<Endereco>
	 * 
	 */
	public List<Endereco> findByEnderecoProduto(ConsultarEstoqueFiltro filtro) {
		int limite = 0;
		String maxEnderecos = WmsUtil.getConfig(ConfiguracaoVO.MAX_ENDERECOS_CRIADOS_POR_VEZ);
		if (maxEnderecos != null && !maxEnderecos.isEmpty()){
			limite = Integer.valueOf(maxEnderecos);
		}
		
		if (limite <= 0)
			limite = Integer.MAX_VALUE;
		
		return enderecoDAO.findByEnderecoProduto(filtro, limite);
	}
	
	public Endereco loadProdutoPicking(Endereco enderecoAux){
		return enderecoDAO.loadProdutoPicking(enderecoAux);
	}
		
	/**
	 * 
	 * M�todo que recupera a listagem de ajuste de estoque da lista de endere�os recuperada.
	 * 
	 * @author Arantes
	 * 
	 * @param listaEnderecos
	 * @return List<AjustarEstoqueFiltro>
	 * 
	 */
	public Set<AjustarEstoqueFiltro> recuperarListaAjustarEstoque(List<Endereco> listaEnderecos) {
		Set<AjustarEstoqueFiltro> listaAjustarEstoque = new ListSet<AjustarEstoqueFiltro>(AjustarEstoqueFiltro.class);
		
		if((listaEnderecos != null) && (!listaEnderecos.isEmpty())) {
			for (Endereco enderecoAux : listaEnderecos) {
				AjustarEstoqueFiltro ajustarEstoque = null;
				
				if(enderecoAux != null) {
					if((enderecoAux.getListaEnderecoproduto() == null) || (enderecoAux.getListaEnderecoproduto().isEmpty())) {
						ajustarEstoque = new AjustarEstoqueFiltro();
						
						Area area = new Area(enderecoAux.getArea().getCdarea());
						area.setCodigo(enderecoAux.getArea().getCodigo());
						area.setCodigoAE(enderecoAux.getArea().getCodigo());
						area.setBox(enderecoAux.getArea().getBox());
						
						Endereco endereco = new Endereco(enderecoAux.getCdendereco());
						endereco.setEndereco(enderecoAux.getEndereco());
						endereco.setArea(area);
						endereco.setRuaI(enderecoAux.getRua());
						endereco.setPredioI(enderecoAux.getPredio());
						endereco.setNivelI(enderecoAux.getNivel());
						endereco.setAptoI(enderecoAux.getApto());
						endereco.setEnderecostatus(enderecoAux.getEnderecostatus());
						endereco.setEnderecofuncao(enderecoAux.getEnderecofuncao());
						Enderecoproduto enderecoProduto = new Enderecoproduto();
						enderecoProduto.setIsPicking(enderecoAux.getEnderecofuncao().equals(Enderecofuncao.PICKING)? true:false);
						enderecoProduto.setDtentrada(new Date(System.currentTimeMillis()));	
						//enderecoProduto.setQtde(0L);
						ajustarEstoque.setEndereco(endereco);
						ajustarEstoque.setEnderecoproduto(enderecoProduto);
						
						if(enderecoAux.getEnderecofuncao().equals(Enderecofuncao.PICKING)){
							Endereco end = enderecoDAO.loadProdutoPicking(enderecoAux);
							if (end != null){
								for(Dadologistico dado: end.getListaDadologistico()){
									if(dado.getProduto()!=null){
									 ajustarEstoque.getEnderecoproduto().setProduto(dado.getProduto());
									 break;
									}
								}
							}
						}						
						listaAjustarEstoque.add(ajustarEstoque);
						
					} else {
						for (Enderecoproduto enderecoproduto : enderecoAux.getListaEnderecoproduto()) {
							ajustarEstoque = new AjustarEstoqueFiltro();
							
							Area area = new Area(enderecoAux.getArea().getCdarea());
							area.setCodigoAE(enderecoAux.getArea().getCodigo());
							area.setCodigo(enderecoAux.getArea().getCodigo());
							area.setBox(enderecoAux.getArea().getBox());
							
							Endereco endereco = new Endereco(enderecoAux.getCdendereco());
							endereco.setArea(area);
							endereco.setEndereco(enderecoAux.getEndereco());
							endereco.setRuaI(enderecoAux.getRua());
							endereco.setPredioI(enderecoAux.getPredio());
							endereco.setNivelI(enderecoAux.getNivel());
							endereco.setAptoI(enderecoAux.getApto());
							endereco.setEnderecofuncao(enderecoAux.getEnderecofuncao());
							
							ajustarEstoque.setEndereco(endereco);
												
							//Marcando os endere�os de picking, para bloquear a edi��o na p�gina.
							enderecoproduto.setIsPicking(enderecoAux.getEnderecofuncao().equals(Enderecofuncao.PICKING)? true:false);
							
							ajustarEstoque.setEnderecoproduto(enderecoproduto);
							
							listaAjustarEstoque.add(ajustarEstoque);
						}
					}
				}
			}
		}
		
		return listaAjustarEstoque;
	}

	/**
	 * 
	 * M�todo que valida se os campos que formam o endere�o est�o todos preenchidos.
	 * 
	 * @author Arantes
	 * 
	 * @param listaAjustarEstoque
	 * @return Boolean
	 * 
	 */
	public String validarCamposEnderecosAjax(Set<AjustarEstoqueFiltro> listaAjustarEstoque) {
		String erro = "Existe(m) endere�o(s) inv�lido(s).\n";
		
		for (AjustarEstoqueFiltro ajustarEstoque : listaAjustarEstoque) {			
			//So valida se tudo estiver preenchido
			if(ajustarEstoque.getEnderecoproduto() != null && ajustarEstoque.getEnderecoproduto().getProduto() != null && 
			   ajustarEstoque.getEnderecoproduto().getProduto().getCdproduto() != null && ajustarEstoque.getEnderecoproduto().getQtde() != null){
				if(ajustarEstoque.getEndereco() == null)
					return erro;
				
				if((ajustarEstoque.getEndereco().getArea() == null) || (ajustarEstoque.getEndereco().getArea().getCodigoAE() == null))
					return erro;
				
				if(ajustarEstoque.getEndereco().getRuaI() == null)
					return erro;
				
				if(ajustarEstoque.getEndereco().getPredioI() == null)
					return erro;
				
				if(ajustarEstoque.getEndereco().getNivelI() == null)
					return erro;
				
				if(ajustarEstoque.getEndereco().getAptoI() == null)
					return erro;
			}
		}
		
		return "";
	}
	
	/**
	 * 
	 * Valida se h� endere�os inexistentes na base de dados.
	 * 
	 * @see  br.com.linkcom.wms.geral.service.EnderecoService#findEndereco(Endereco)
	 * 
	 * @author Arantes
	 * 
	 * @param listaAjustarEstoque
	 * @return Boolean
	 * 
	 */
	public String validarEnderecosExistentesAjax(Set<AjustarEstoqueFiltro> listaAjustarEstoque) {
		StringBuilder erros = new StringBuilder();
		
		for (AjustarEstoqueFiltro ajustarEstoque : listaAjustarEstoque) {
			//So valida se tudo estiver preenchido
			if(ajustarEstoque.getEnderecoproduto() != null && ajustarEstoque.getEnderecoproduto().getProduto() != null && 
			   ajustarEstoque.getEnderecoproduto().getProduto().getCdproduto() != null && ajustarEstoque.getEnderecoproduto().getQtde() != null){
			
				Endereco endereco = ajustarEstoque.getEndereco();
				
				Endereco enderecoAux = this.findEndereco(endereco);
				if(EnderecoAux.isEnderecoCompleto(endereco) && enderecoAux == null) {
					erros.append("O endere�o \"");
					erros.append(EnderecoAux.formatarEndereco(endereco));
					erros.append("\" n�o existe.\n");
				} else if (enderecoAux != null && enderecoAux.getArea().getBox()){
					erros.append("O endere�o \"");
					erros.append(EnderecoAux.formatarEndereco(endereco));
					erros.append("\" pertence a uma �rea de box. S� � permitido ajustar estoque atrav�s de invent�rio.\n");					
				} else if (enderecoAux != null && enderecoAux.getEnderecostatus().equals(Enderecostatus.BLOQUEADO)){
					erros.append("O endere�o \"");
					erros.append(EnderecoAux.formatarEndereco(endereco));
					erros.append("\" est� bloqueado.\n");					
				}
			}
		}
		
		return erros.toString();
	}
	
	/**
	 * M�todo de refer�ncia ao DAO <br/>Caso o par�metro <b>enderecofuncao</b> seja igual a <i>BLOCADO</i>
	 * a consulta ser� feita tratando especialmente este tipo de endere�o, para
	 * qualquer outro tipo ou para <code>null</code> a consulta ir� procurar
	 * por um endere�o comun comparando <i>RUA.PREDIO.NIVEL.APTO</i>.
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findByEndereco(String
	 *      enderecoComPonto, Area area)
	 * 
	 * @param enderecoComPonto
	 * @param codigoArea
	 * @param deposito
	 * @param enderecofuncao Permite realizar uma consulta especial para o caso de endere�os blocados.
	 * 
	 * @return
	 */
	public Endereco findByEndereco(String enderecoComPonto, Long codigoArea,Deposito deposito, Enderecofuncao enderecofuncao) {
		return enderecoDAO.findByEndereco(enderecoComPonto,codigoArea,deposito, enderecofuncao);
	}
	
	/* singleton */
	private static EnderecoService instance;
	public static EnderecoService getInstance() {
		if(instance == null){
			instance = Neo.getObject(EnderecoService.class);
		}
		return instance;
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findBlocadoForUMA(Endereco endereco, int restricao)
	 * 
	 * @param endereco
	 * @param restricao
	 * @return
	 */
	public Endereco findBlocadoForUMA(Endereco endereco, int restricao) {
		return enderecoDAO.findBlocadoForUMA(endereco,restricao);
	}
	
	/**
	 * Verifica se um endere�o � igual ao outro usando somente at� o n�vel de pr�dio
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param endereco1
	 * @param endereco2
	 * @return
	 */
	public boolean predioEquals(Endereco endereco1, Endereco endereco2) {
		if(endereco1 != null && endereco2 != null){
			try{
				if(endereco1.getArea().equals(endereco2.getArea())){
					return endereco1.getRua().equals(endereco2.getRua()) && endereco1.getPredio().equals(endereco2.getPredio());
				}
			}catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Seta o status do endere�o vizinho do endere�o mostrado como par�metro, caso ele exista
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param endereco
	 */
	public void emprestarEnderecoVisinho(Endereco endereco,Deposito deposito) {
		try { 
			EnderecoAux aux = new EnderecoAux(endereco.getEndereco());
			Endereco end = findByEndereco(aux.getEnderecoVizinho(), endereco.getArea().getCodigo(), deposito, null);
			if(end != null  && !end.getEnderecostatus().equals(Enderecostatus.OCUPADO)){
				end.setEnderecostatus(Enderecostatus.EMPRESTIMO);
				updateStatusEndereco(end);
			}
		} catch (Exception e) {
			throw new WmsException("N�o foi poss�vel executar a fun��o ocuparEnderecoVisinho.");
		}
	}
	
	/**
	 * M�todo de refer�ncia ao DAO
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param enderecodestino
	 * @throws SQLException 
	 */
	public void pAtualizarEndereco(Endereco endereco) {
		enderecoDAO.pAtualizarEndereco(endereco);
	}
	
	/**
	 * Retorna todos os endere�os de um pr�dio
	 * @param area
	 * @param rua
	 * @param predio
	 * @param orderby
	 * @return
	 * @author C�ntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#findEnderecoByPredio(Area, Integer, Integer, String)
	 */
	public List<Endereco> findEnderecoByPredio(Area area,Integer rua, Integer predio, String orderby){
		return enderecoDAO.findEnderecoByPredio(area, rua, predio, orderby);
	}
	
	/**
	 * Carrega o endere�o
	 * @param endereco
	 * @return
	 * @author C�ntia Nogueira
	 * @see br.com.linkcom.wms.geral.dao.EnderecoDAO#loadEndereco(Endereco)
	 */
	public Endereco loadEndereco(Endereco endereco ){
		return enderecoDAO.loadEndereco(endereco);
	}
	
	/**
	 * 
	 * M�todo que recupera uma lista de endere�os de acordo com o filtro. 
	 * Faz chamada � procedure ENDERECODISPONIVELPRODUTO no banco de dados.
	 * 
	 * @author C�ntia Nogueira
	 * 
	 * @param filtro
	 * @return List<Endereco>
	 * @throws SQLException 
	 * 
	 */
	public List<Endereco> recuperarEnderecosDestino(BuscarEnderecoPopupFiltro filtro) throws SQLException {
		return enderecoDAO.recuperarEnderecosDestino(filtro);
	}

	/**
	 * Localiza o endere�o de avaria de um determinado dep�sito.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public Endereco findEnderecoAvaria(Deposito deposito) {
		return enderecoDAO.findEnderecoAvaria(deposito);
	}
	
	/**
	 * Localiza o endere�o de Box de um determinado dep�sito.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public Endereco findEnderecoBox(Deposito deposito) {
		return enderecoDAO.findEnderecoBox(deposito);
	}
	
	/**
	 * Localiza o endere�o virtual de um determinado dep�sito.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public Endereco findEnderecoVirtual(Deposito deposito) {
		return enderecoDAO.findEnderecoVirtual(deposito);
	}
	
	/**
	 * Localiza o endere�o de picking de um determinado produto.
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param deposito
	 * @return
	 */
	public Endereco findPicking(Produto produto, Deposito deposito) {
		return enderecoDAO.findPicking(produto, deposito);
	}

	/**
	 * Verifica se existem endere�os que ir�o satisfazer os filtros.
	 * 
	 * Este m�todo � utilizado para validar o invent�rio e o reabastecimento preventivo.
	 * 
	 * @author Giovane Freitas
	 * @param lote
	 * @return
	 */
	public boolean existeEndereco(IntervaloEndereco lote, boolean validarProduto) {
		return enderecoDAO.existeEndereco(lote, validarProduto);
	}

	/**
	 * Valida se um determinado produto pode ser inserido em um dado endere�o.
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param endereco
	 */
	public void validarInsercao(Produto produto, Endereco endereco) throws InsercaoInvalidaException {
		Endereco enderecoAux = enderecoDAO.loadEnderecoAndDadosLogisticos(endereco);
		
		//Se o endere�o existe
		//Se � um endere�o de picking
		//Se n�o est� associado a um produto ou se est� associado a um produto diferente do desejado
		//Ent�o lan�a erro, pois s� � poss�vel inserir Enderecoproduto para o endere�o de picking do produto em quest�o
		if (enderecoAux != null && Enderecofuncao.PICKING.getCdenderecofuncao().equals(enderecoAux.getEnderecofuncao().getCdenderecofuncao()) 
				&& (enderecoAux.getListaDadologistico() == null || enderecoAux.getListaDadologistico().size() == 0
				|| !enderecoAux.getListaDadologistico().iterator().next().getProduto().equals(produto))){
			
			throw new InsercaoInvalidaException("O endereco \"" + enderecoAux.getEndereco() + "\" � picking e n�o est� associado ao produto \"" +
					produto.getDescriptionProperty() + "\"");
		}
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param endereco
	 * @return
	 * @author Giovane Freitas
	 */
	public Endereco findEndereco(Endereco endereco, Deposito deposito) {
		return enderecoDAO.findEndereco(endereco, deposito);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param area
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Endereco getEnderecoAvariaByArea(String area) {
		return enderecoDAO.getEnderecoAvariaByArea(area);
	}
	
	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Endereco getEnderecoAvariaPrimeiroCadastrado() {
		return enderecoDAO.getEnderecoAvariaPrimeiroCadastrado();
	}

	/**
	 * <p>Carrega o endere�o a partir da �rea, rua, pr�dio, n�vel e apto.
	 * Caso o n�vel e a rua sejam igual a -1 ser� procurado o primeiro 
	 * endere�o de um um pr�dio blocado filtrando por area, rua, pr�dio e fun��o do endere�o.</p>
	 * <p>Tamb�m ir� filtra eliminando os endere�os que est�o bloqueados.</p>
	 * 
	 * @author Giovane Freitas
	 * @param area Obrigat�rio. C�digo da �rea.
	 * @param rua Obrigat�rio. N�mero da rua.
	 * @param predio Obrigat�rio. N�mero do pr�dio
	 * @param nivel Opcional.
	 * @param apto Opcional.
	 * @param deposito Obrigat�rio.
	 * @return
	 */
	public Endereco load(Endereco endereco, Deposito deposito) {
		return enderecoDAO.load(endereco, deposito);
	}
	
	/**
	 * Carrega um endere�o a partir do c�digo de barras da etiqueta de endere�o.
	 * 
	 * @author Giovane Freitas
	 * @param codigoEtiqueta
	 *            O c�digo de barras da etiqueta lida.
	 * @param deposito
	 *            O Dep�sito atual.
	 * @return Um objeto {@link Endereco} ou <code>null</code> caso n�o
	 *         encontre o endere�o no dep�sito especificado.
	 */
	public Endereco loadEnderecoByCodigoEtiqueta(String codigoEtiqueta, Deposito deposito) {
		Endereco endereco = prepareLoadEnderecoByCodigoEtiqueta(codigoEtiqueta, deposito);
		return this.load(endereco, deposito);
	}
	
	/**
	 * Carrega um endere�o exato a partir do c�digo de barras da etiqueta de endere�o.
	 * 
	 * @author Tom�s Rabelo
	 * @param codigoEtiqueta
	 *            O c�digo de barras da etiqueta lida.
	 * @param deposito
	 *            O Dep�sito atual.
	 * @return Um objeto {@link Endereco} ou <code>null</code> caso n�o
	 *         encontre o endere�o no dep�sito especificado.
	 */
	public Endereco loadEnderecoByCodigoEtiquetaExato(String codigoEtiqueta, Deposito deposito) {
		Endereco endereco = prepareLoadEnderecoByCodigoEtiqueta(codigoEtiqueta, deposito);
		return this.loadExato(endereco, deposito);
	}

	/**
	 * M�todo com refer�ncia no DAO
	 * 
	 * @param endereco
	 * @param deposito
	 * @return
	 * @author Tom�s Rabelo
	 */
	public Endereco loadExato(Endereco endereco, Deposito deposito) {
		return enderecoDAO.loadExato(endereco, deposito);
	}

	public Endereco prepareLoadEnderecoByCodigoEtiqueta(String codigoEtiqueta, Deposito deposito) {
		if (codigoEtiqueta == null || codigoEtiqueta.isEmpty() || deposito == null || deposito.getCddeposito() == null)
			throw new WmsException("O C�digo de Barras da etiqueta e o Dep�sito s�o obrigat�rios.");

		boolean isBlocado = false;
		
		if (codigoEtiqueta.length() == 8 || codigoEtiqueta.length() == 9)
			isBlocado = true;
			
		if (isBlocado){
			if (codigoEtiqueta.length() == 8)
				codigoEtiqueta = Enderecofuncao.BLOCADO.getCdenderecofuncao() + codigoEtiqueta;
			
			for (int i = codigoEtiqueta.length(); i < 14; i++)
				codigoEtiqueta += "0";
		}
		
		// 14 para endere�os completos e 9 para endere�os blocados
		// 13 para endere�os sem fun��o e 8 para endere�os blocados sem fun��o
		if (codigoEtiqueta.length() != 14 && codigoEtiqueta.length() != 9 && codigoEtiqueta.length() != 13 
				&& codigoEtiqueta.length() != 8)
			throw new EnderecoInvalidoException("Codigo inv�lido para a execu��o da fun��o translateEtiquetaEndereco.");

		// Aceita c�digo iniciando com fun��o pra manter compatibilidade
		// Mas o correto � informar <area><rua><predio><nivel><apto> como por
		// exemplo 1212312312123
		// Se for informado o c�digo contendo a fun��o (lengh ==14/9) eu
		// elimino o primeiro d�gito
		if (codigoEtiqueta.length() == 14 || codigoEtiqueta.length() == 9)
			codigoEtiqueta = codigoEtiqueta.substring(1);

		int rua;
		int predio;
		int nivel = -1;
		int apto = -1;
		long area;
		Area area2;
		
		try {
			String endereco = codigoEtiqueta.substring(2, codigoEtiqueta.length());

			rua = Integer.parseInt(endereco.substring(0, 3));
			predio = Integer.parseInt(endereco.substring(3, 6));

			// se n�o � endere�o blocado
			if (codigoEtiqueta.length() > 8) {
				nivel = Integer.parseInt(endereco.substring(6, 8));
				apto = Integer.parseInt(endereco.substring(8, 11));
			}

			area = Integer.parseInt(codigoEtiqueta.substring(0, 2));
			area2 = areaService.findByCodigo(area, deposito);
		} catch (Exception e) {
			throw new WmsException("O c�digo de barras informado � inv�lido.");
		}
		
		Endereco endereco = new Endereco(area2, rua, predio, nivel, apto);
		if (isBlocado)
			endereco.setEnderecofuncao(Enderecofuncao.BLOCADO);
			
		return endereco;
	}

	/**
	 * M�todo que carrega os dados escolhidos no filtro do endere�o
	 * 
	 * @param form
	 * @Author Tom�s Rabelo
	 */
	public void setValoresFiltro(Endereco form) {
		form.setEnderecoFiltro(new EnderecoFiltro());
		if(form.getEnderecofuncao() != null && form.getEnderecofuncao().getCdenderecofuncao() != null)
			form.getEnderecoFiltro().setEnderecofuncao(enderecofuncaoService.load(form.getEnderecofuncao()));
		if(form.getTipoestrutura() != null && form.getTipoestrutura().getCdtipoestrutura() != null)
			form.getEnderecoFiltro().setTipoestrutura(tipoestruturaService.load(form.getTipoestrutura()));
		if(form.getTipoendereco() != null && form.getTipoendereco().getCdtipoendereco() != null)
			form.getEnderecoFiltro().setTipoendereco(tipoenderecoService.load(form.getTipoendereco()));
		if(form.getTipopalete() != null && form.getTipopalete().getCdtipopalete() != null)
			form.getEnderecoFiltro().setTipopalete(tipopaleteService.load(form.getTipopalete()));
		if(form.getEnderecostatus() != null && form.getEnderecostatus().getCdenderecostatus() != null)
			form.getEnderecoFiltro().setEnderecostatus(enderecostatusService.load(form.getEnderecostatus()));
		form.getEnderecoFiltro().setLarguraexcedente(form.getLarguraexcedente());
	}
	
	/**
	 * Reserva o endere�o, somente se ele n�o for da �rea de avaria ou virtual.
	 * @param endereco
	 */
	public void reservarEndereco(Endereco endereco, boolean usarLarguraExcedente){
		Area area = areaService.findByEndereco(endereco);
		if (!area.getAvaria() && !area.getVirtual()){
			endereco.setEnderecostatus(Enderecostatus.RESERVADO);
			this.updateStatusEndereco(endereco);
			
			//Se o endereco for do tipo largura excedente e estiver alocado com um produto 
			//com esta mesma caracteristica, ent�o atualizar o status do endereco vizinho
			String enderecoVizinhoStr = calcularEnderecoVizinho(endereco);
			if (endereco.getLarguraexcedente() && usarLarguraExcedente && enderecoVizinhoStr != null){
				Endereco vizinho = findEndereco(enderecoVizinhoStr, endereco.getArea());
				if (vizinho != null && !vizinho.getEnderecostatus().equals(Enderecostatus.OCUPADO)){
					vizinho.setEnderecostatus(Enderecostatus.EMPRESTIMO);
					this.updateStatusEndereco(vizinho);
				}
			}

		}
	}
	
	/**
	 * Retorna o n�mero do endereco que � vizinho a esse endere�o
	 * 
	 * @return endere�o
	 */
	public static String calcularEnderecoVizinho(Endereco endereco){
		if (endereco == null)
			return null;
		
		if(endereco.getApto() != null){
			String doisZeros = "%02d";
			String tresZeros = "%03d";
			
			String rua = String.format(tresZeros, endereco.getRua());
			String predio = String.format(tresZeros, endereco.getPredio());
			String nivel = String.format(doisZeros, endereco.getNivel());
		
			String prefix = rua + "." + predio + "." + nivel + ".";
			if(endereco.getApto() % 2 == 0 ){
				return prefix+String.format(tresZeros, endereco.getApto() - 1);
			}
			else{
				return prefix+String.format(tresZeros, endereco.getApto() + 1);
			}
		}else
			return null;
	}
	
	/**
	 * Valida se o mesmo endere�o � repetido em mais de um lote.
	 * 
	 * Este m�todo � utilizado para validar o invent�rio e o reabastecimento preventivo.
	 * 
	 * @author Giovane Freitas
	 * @param listaInventariolote
	 * @param errors
	 * @param validarProduto 
	 */
	public void validaEnderecoRepetido(List<IntervaloEndereco> lotes, BindException errors, boolean validarProduto) {
		if (lotes == null || lotes.size() <= 1)
			return;
		
		//fa�o uma combina��o de 2 elementos, para que cada par seja comparado apenas uma vez.
		List<IntervaloEndereco[]> combinations = CollectionsUtils.combination(IntervaloEndereco.class, lotes, 2);
		
		for (IntervaloEndereco[] par : combinations){
			IntervaloEndereco lote1 = par[0];
			IntervaloEndereco lote2 = par[1];
			
			//Se � para validar o produto e se os dois lotes possuem produtos diferentes ent�o pode passar
			if (validarProduto && lote1.getProduto() != null && lote2.getProduto() != null & !lote1.getProduto().equals(lote2.getProduto()))
				continue;

			//se a �rea, o lado ou a caracter�stica for diferente ent�o n�o h� problema.
			//por�m se um lote tiver lado/caracter�stica definido e outro n�o, a� terei de validar as faixa de endere�o
			if (!lote1.getArea().equals(lote2.getArea()) || (lote1.getEnderecolado() != null && lote2.getEnderecolado() != null && !lote1.getEnderecolado().equals(lote2.getEnderecolado()))
					|| (lote1.getEnderecofuncao() != null && lote2.getEnderecofuncao() != null && !lote1.getEnderecofuncao().equals(lote2.getEnderecofuncao())))
				continue;
			
			// Lote 1:    01 02 03 04 05
			// Lote 2:          03 04 05 06 07
			boolean ruaConflitante = (lote1.getRuainicial() <= lote2.getRuainicial() && lote2.getRuainicial() <= lote1.getRuafinal()) || (lote2.getRuainicial() <= lote1.getRuainicial() && lote1.getRuainicial() <= lote2.getRuafinal());
			boolean predioConflitante = (lote1.getPredioinicial() <= lote2.getPredioinicial() && lote2.getPredioinicial() <= lote1.getPrediofinal()) || (lote2.getPredioinicial() <= lote1.getPredioinicial() && lote1.getPredioinicial() <= lote2.getPrediofinal());
			boolean nivelConflitante = (lote1.getNivelinicial() <= lote2.getNivelinicial() && lote2.getNivelinicial() <= lote1.getNivelfinal()) || (lote2.getNivelinicial() <= lote1.getNivelinicial() && lote1.getNivelinicial() <= lote2.getNivelfinal());
			boolean aptoConflitante = (lote1.getAptoinicial() <= lote2.getAptoinicial() && lote2.getAptoinicial() <= lote1.getAptofinal()) || (lote2.getAptoinicial() <= lote1.getAptoinicial() && lote1.getAptoinicial() <= lote2.getAptofinal());

			if (ruaConflitante && predioConflitante && nivelConflitante && aptoConflitante) {
				int posLote1 = lotes.indexOf(lote1) + 1;
				int posLote2 = lotes.indexOf(lote2) + 1;
				errors.reject("002", "O lote " + posLote1 + " e o lote " + posLote2 + " possuem endere�os em comum.");
			} 
		}
	}
	
	/**
	 * Retorna erro se a caracter�stica selecionada � diferente dos endere�os.
	 * 
	 * Este m�todo � utilizado para validar o invent�rio e o reabastecimento preventivo.
	 * 
	 * @param listaInventariolote
	 * @param errors
	 * @author C�ntia Nogueira
	 * @see EnderecoService#recuperarEnderecosCadastrados(Endereco)
	 * @see EnderecoService#isAllCaracterisca(List, Enderecofuncao)
	 * @see EnderecoService#isAllBlocado(List)
	 * @see #inicializaEnderecobyInventarioLote(Inventariolote)
	 */
	public void validaExistenciaEndereco(List<IntervaloEndereco> lotes,BindException errors, boolean validarProduto) {
	
		for (int i = 0; i < lotes.size(); i++) {
			IntervaloEndereco lote = lotes.get(i);
			if(!existeEndereco(lote, validarProduto)){
				
				String fimMsg = (validarProduto && lote.getProduto() != null) ? " para o produdo especificado." : ".";
				
				if (lote.getEnderecofuncao() == null)
					errors.reject("001","N�o h� endere�os no lote " + (i + 1) + fimMsg);
				else if (lote.getEnderecofuncao().equals(Enderecofuncao.PICKING))
					errors.reject("001","N�o h� endere�os de picking no lote " + (i + 1) + fimMsg);
				else if (lote.getEnderecofuncao().equals(Enderecofuncao.PULMAO))
					errors.reject("001","N�o h� endere�os de pulm�o no lote " + (i + 1) + fimMsg);
				else if (lote.getEnderecofuncao().equals(Enderecofuncao.BLOCADO))
					errors.reject("001","N�o h� pr�dios blocados no lote " + (i + 1) + fimMsg);
			}
		}
	}

	/**
	 * Verifica se os endere�os iniciais s�o menores que os finais.
	 * 
	 * Este m�todo � utilizado para validar o invent�rio e o reabastecimento preventivo.
	 * 
	 * @author Leonardo Guimar�es
	 * 
	 * @param listaInventariolote
	 * @param errors
	 */
	public void validateEnderecos(List<IntervaloEndereco> lotes, BindException errors) {
		long enderecoinicial = 0;
		long enderecofinal = 0;
		for (int i = 0; i < lotes.size(); i++) {
			IntervaloEndereco lote = lotes.get(i);
			enderecoinicial = WmsUtil.concatenateNumbers(lote.getRuainicial(),lote.getPredioinicial(),lote.getNivelinicial(),lote.getAptoinicial());
			enderecofinal = WmsUtil.concatenateNumbers(lote.getRuafinal(),lote.getPrediofinal(),lote.getNivelfinal(),lote.getAptofinal());
			if(enderecofinal < enderecoinicial){
				errors.reject("001","O lote " + (i + 1) + " possui o endere�o inicial maior que o endere�o final.");
				break;
			}
		}
	}
	
	/**
	 * 
	 * M�todo que preenche os campos de endere�o com zeros a esquerda
	 * 
	 * @author Arantes
	 * 
	 * @param endereco
	 * @return String
	 * 
	 */
	public static String formatarEndereco(Endereco endereco) {
		if (endereco == null)
			return "";
		
		String doisZeros = "%02d";
		String tresZeros = "%03d";
		String ponto = ".";
		
		String codigoArea = "";
		if (endereco.getArea() != null && endereco.getArea().getCodigo() != null)
			codigoArea = String.format(doisZeros, endereco.getArea().getCodigo()) + ponto;
		
		String rua = String.format(tresZeros, endereco.getRua());
		String predio = String.format(tresZeros, endereco.getPredio());
		String nivel = String.format(doisZeros, endereco.getNivel());
		String apto = String.format(tresZeros, endereco.getApto());
		
		return codigoArea + rua + ponto + predio + ponto + nivel + ponto + apto;
	}

	/**
	 * Busca os dados para o relat�rio de ocupa��o atual.
	 * 
	 * @author Giovane Freitas
	 * @param deposito
	 * @return
	 */
	public TotalVO getOcupacaoAtual(Deposito deposito) {
		return enderecoDAO.getOcupacaoAtual(deposito);
	}

	/**
	 * Busca a movimenta��o total de um dia.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public TotalVO getTotalRecebido(Deposito deposito, java.util.Date inicio, java.util.Date termino) {
		return enderecoDAO.getTotalRecebido(deposito, inicio, termino);
	}

	/**
	 * Busca a movimenta��o total de um dia.
	 * 
	 * @author Giovane Freitas
	 * @return
	 */
	public TotalVO getTotalExpedido(Deposito deposito, java.util.Date inicio, java.util.Date termino) {
		return enderecoDAO.getTotalExpedido(deposito, inicio, termino);
	}
	
	public List<Endereco> recuperarEnderecosIndisponiveis(Inventario inventario) throws SQLException {
		return enderecoDAO.recuperarEnderecosIndisponiveis(inventario);
	}
	
	/**
	 * Verifica se se um {@link Endereco} � o endere�o de picking de um {@link Produto} 
	 * no dep�sito atual (o dep�sito que o usu�rio logou).
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param endereco
	 * @return
	 */
	public Boolean isEnderecoPicking(Produto produto, Endereco endereco) {
		return enderecoDAO.isEnderecoPicking(produto, endereco);
	}

	/**
	 * Verifica se se um {@link Endereco} � o endere�o de picking de um {@link Produto} 
	 * no dep�sito atual (o dep�sito que o usu�rio logou).
	 * 
	 * @author Giovane Freitas
	 * @param produto
	 * @param endereco
	 * @return
	 */
	public Boolean isEnderecoPicking(Produto produto, Endereco endereco, Deposito deposito) {
		return enderecoDAO.isEnderecoPicking(produto, endereco, deposito);
	}
}