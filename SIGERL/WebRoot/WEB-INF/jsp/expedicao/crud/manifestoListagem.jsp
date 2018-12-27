<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<%@page import="br.com.linkcom.neo.core.standard.Neo"%>
<%@page import="br.com.linkcom.neo.util.Util"%>
<c:set var="PODE_EXCLUIR" scope="page" value="<%=new Boolean(Neo.getApplicationContext().getAuthorizationManager().isAuthorized(Util.web.getFirstUrl(), \"excluir\", Neo.getRequestContext().getUser())) %>" />

<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Manifestos</h3>
	<n:form validate="false" enctype="${listagemTag.formEnctype}">
		<n:validation>
	     	<t:janelaFiltro>
				
				<div class="row">
					<div class="form-group col-md-12">
						<div class="col-md-3">
							<t:property name="deposito" itens="${LISTA_DEPOSITOS}" class="form-control" mode="input" showLabel="true"/>
						</div>
						<div class="col-md-3">
							<t:property name="numeronota" class="form-control input-md" mode="input" showLabel="true"/>
						</div>
						<div class="col-md-3">
						 	<div class="row">
				                <label class="col-xs-12">Período de Emissão</label>
				            </div>
				            <div class="row">
				                <div class="col-md-5" style="padding-right: 1%;">
				                    <t:property name="dtemissaoinicio" renderAs="single" showLabel="false" class="form-control input-md" mode="input"/> 
				                </div>
				                <div class="col-md-1" style="padding: 2% 0;">
				                    <label>até</label> 
				                </div>
				                <div class="col-md-5" style="padding-left: 1%;">
				                    <t:property name="dtemissaofim" renderAs="single" showLabel="false" class="form-control input-md" mode="input"/>
				                </div>
							</div>
						</div>
						<div class="col-md-3">
							<t:property name="usuario" class="form-control input-md" mode="input" showLabel="true" type="autocomplete"/>
						</div>
					</div>
				</div>
				
				 <div class="row">
					<div class="form-group col-md-12">
						<div class="col-md-4">
							<t:property name="codigoProduto" class="form-control input-md" mode="input" showLabel="true"/>
						</div>
						<div class="col-md-6">
							<t:property name="nomeCliente" class="form-control input-md" mode="input" showLabel="true"/>
						</div>
					</div>
				</div>
				
			   <%--<div class="modal fade" id="infoModal" role="dialog">
			     <div class="modal-dialog">
			       <div class="modal-content">
			        <div class="modal-header">
			          	<button type="button" class="close" data-dismiss="modal">&times;</button>
			          	<h4 class="modal-title">Informar Problema</h4>
			        </div>
			        <div class="modal-body">
			        	<c:forEach items="${filtro.problemasPedido}" var="problemaPedido" >
			        		<div class="radio">
							  <label><input type="radio" name="cdProblemaPedidoLoja" value="${problemaPedido.cdProblemaPedidoLoja}">${problemaPedido.descricao}</label>
							</div>
			        	</c:forEach>
			        </div>
			        <div class="modal-footer">
				        <button type="button" class="btn btn-success" data-dismiss="modal" onclick="executarInformacaoProblema();">Finalizar</button>
				    </div>
			      </div>
			    </div>
			  </div> --%>
			  
			</t:janelaFiltro>
		
		</n:validation>
	</n:form>
		
 	
 	<br />

	
	<%-- <div class="panel panel-default" style="border: 0px;" >
		<div class="panel-body">
           <div class="col-md-3 panel corpo-pagina" style="background-color: #f5f5f5;" align="center">
	           <a href="javascript:informarProblema();" style="font-size: large ; color: #f91700">
					<span class="glyphicon glyphicon-remove-circle" style="padding-right: 2%"></span>Informar um problema
				</a>
           </div>
					
			<div class="col-md-2 col-md-offset-7">
				<button class="btn btn-primary btn-md" type="button" id="buttonLimpar" onclick="limparFiltros();">Limpar</button>
				<button class="btn btn-success btn-md" type="button" id="buttonFiltrar" onclick="doFilter();resetPage();">Filtrar</button>
			</div>		
           </div>
	</div>
	
		
	<div class="panel panel-default" style="border: 0px !important;">
		<div class="panel-body">
			<div class="table-responsive" >
				<n:dataGrid name="registros" id="tabelaResultados" itens="${lista}" itemType="<%=GestaoPedidoVO.class%>" var="registro" styleClass="table table-striped table-bordered">
					<n:column>
						<n:header>
							<input type="checkbox" class="checkBoxClass" name="selectAll" id="selectAll" onclick="javascript:$dg.changeCheckState();"/>
						</n:header>
						<n:body>
							<input class="checkBoxClass" type="checkbox" name="selecteditens" value="${registro.numeroNota}">
						</n:body>
					</n:column>
					<t:property name="numeroNota" label="Num. Nota" mode="output"/>
					<t:property name="numeroPedido" label="Num. Pedido" mode="output"/>
					<t:property name="dataPedido" label="Data Pedido" mode="output"/>
					<t:property name="dataChegada" label="Dt. Chegada Loja" mode="output"/>
					<t:property name="situacao" label="Situação" mode="output"/>
					<t:property name="cliente"  label="Cliente" mode="output"/>
					
					<n:column header="Detalhes" style="text-align: -webkit-center;" >
						<a href="javascript:openDialogPedidos(${registro.numeroPedido})" data-toggle="tooltip" title="Exibir log do pedido ${registro.numeroPedido}" style="font-size: x-large;">
							<span class="glyphicon glyphicon-modal-window" style="color:#545454"/>
						</a>
					</n:column>
				</n:dataGrid> 
			</div>
		</div>
	</div> --%>
</div>

<%-- <t:listagem titulo="Manifesto de Carga" validateFunction="validarFormulario" >
	<t:janelaFiltro>
		<t:tabelaFiltro showSubmit="false">
			<w:tableGroup columns="4" panelgridWidth="90%">
				
				<n:panel class="labelColumn">
					Período de Emissão<br>
					<t:property name="" renderAs="single" showLabel="false" style="width:80px;"/>
					até
					<t:property name="" renderAs="single" showLabel="false" style="width:80px;"/>
				</n:panel>
				<t:property name="" type="autocomplete" includeConfirmExcludeAutocomplete="false" style="width:250px;"/>
			</w:tableGroup>
			<w:tableGroup columns="3" panelgridWidth="75%">
				<t:property name="transportador" 
							type="autocomplete" 
							itens="transportadorService.findForAutocompleteWithDepositoLogado" 
							autocompleteGetterLabel="getIdNomeDocumento" 
							autocompleteLabelProperty="transportador.cdpessoa,transportador.nome,transportador.documento"
							autocompleteOnExcluir="carregaMotoristaVeiculo()"
							onselect="carregaMotoristaVeiculo()" 
							style="width:340px;"/>
				<t:property name="motorista" id="motoristaId" disabled="disabled" class="disabled" style="width:300px;"/>
				<t:property name="veiculo" id="veiculoId" disabled="disabled" class="disabled"/>
			</w:tableGroup>
			<w:tableGroup columns="4" panelgridWidth="50%">
				<t:property name="cdcarregamento" label="Núm. da Carga"  style="width:100px;"/>
				<t:property name="cdmanifesto" label="Núm. do Manifesto" style="width:100px;"/>
				<t:property name="manifestostatus" label="Status do Manifesto" style="width:200px;"/>
				<t:property name="tipoentrega" label="Tipo de Entrega" style="width:150px;"/>
			</w:tableGroup>
			<t:property name="defaultValues" type="hidden" write="false" label=""/>
		</t:tabelaFiltro>
		<table width="100%">
			<tr align="right">
				<td>
					<n:input name="resetCurrentPage" type="hidden" write="false"/>
					<n:link url="#" onclick="javascript:doFilter();resetPage();" id="btn_filtro">Filtrar</n:link>		
					|&nbsp;&nbsp;
					<n:link url="#" onclick="javascript:$n.clearForm(\"form\");setDefaultDeposito();" id="btn_limpar">Limpar filtro</n:link>
				</td>
			</tr>
		</table>
	</t:janelaFiltro>
	<t:janelaResultados>
		<t:tabelaResultados> 
			<t:property name="cdmanifesto" label="Núm. Manifesto"/>
			<t:property name="manifestostatus"/>
			<t:property name="tipoentrega"/>
			<t:property name="dtemissao"/>
			<t:property name="veiculo"/>
			<t:property name="transportador"/>
			<t:property name="motorista"/>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem> --%>

<script type="text/javascript">
	
	$(document).ready(function() {
		//setDefaultDeposito();
	});
	
	function setDefaultDeposito(){
		form['deposito'].value = 'br.com.linkcom.wms.geral.bean.Deposito[cddeposito=${DEPOSITO_LOGADO.cddeposito}]';
	}
	
	function doFilter(){
		if(validaFiltro()){
			form.ACAO.value ='${TabelaFiltroTag.submitAction}';
			form.action = '';
			form.validate = '${TabelaFiltroTag.validateForm}';
			submitForm();
		}
	}
	
	function resetPage(){
		form.resetCurrentPage.value = 'true';
		resertMotoristaVeiculo();
	}
	
	function validaFiltro(){
		
		var isError = false;
		var msg = "";
		
		if(form['deposito'].value == "<null>"){
			msg += "O campo Depósito é obrigatório";
			isError = true;
		}

		form['defaultValues'].value = false;
		
		//TODO validações diversas.
		if(isError==true){
			alert(msg);
			return false;
		}else{
			return true;	
		}
	}
	
	function resertMotoristaVeiculo(){
		$(form['motorista']).removeOption(/./);
		$(form['motorista']).addOption("<null>"," ");
		
		$(form['veiculo']).removeOption(/./);
		$(form['veiculo']).addOption("<null>"," ");
	}
	
	function carregaMotoristaVeiculo(){	
		var transportador = form['transportador'].value;
		var deposito = form['deposito'].value;
		$w.getJSON("${ctx}/expedicao/crud/Manifesto",{ACAO:'carregaMotoristaVeiculoListagem','transportador':transportador,'deposito':deposito},function(json){
			if(json!=null){
				refreshComboMotorista(json.listaMotorista);
				refreshComboVeiculo(json.listaVeiculo);
			}
		});
	}
	
	function refreshComboMotorista(listaMotorista){
		if(listaMotorista!=null && listaMotorista.length > 0){
			$("#motoristaId").removeAttr("disabled");
			$("#motoristaId").removeClass("disabled");
			$(form['motorista']).removeOption(/./);
			var i= 0;
			$(form['motorista']).addOption("<null>"," ");
			for(i=0; i< listaMotorista.length; i++){
				$(form['motorista']).addOption("br.com.linkcom.wms.geral.bean.Motorista[cdmotorista="+listaMotorista[i].cdmotorista+"]",listaMotorista[i].nome,false);
			}
		}else{
			$("#motoristaId").attr("disabled","true");
			$("#motoristaId").addClass("disabled");
			$(form['motorista']).removeOption(/./);
		}
	}
	
	function refreshComboVeiculo(listaVeiculo){
		if(listaVeiculo!=null && listaVeiculo.length > 0){
			$("#veiculoId").removeAttr("disabled");			
			$("#veiculoId").removeClass("disabled");
			$(form['veiculo']).removeOption(/./);
			var i= 0;
			$(form['veiculo']).addOption("<null>"," ");
			for(i=0; i< listaVeiculo.length; i++){
				$(form['veiculo']).addOption("br.com.linkcom.wms.geral.bean.Veiculo[cdveiculo="+listaVeiculo[i].cdveiculo+"]",listaVeiculo[i].placa,false);
			}
		}else{
			$("#veiculoId").attr("disabled","true");
			$("#veiculoId").addClass("disabled");
			$(form['veiculo']).removeOption(/./);
		}
	}
		
</script>
