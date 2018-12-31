<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<%@page import="br.com.linkcom.neo.core.standard.Neo"%>
<%@page import="br.com.linkcom.neo.util.Util"%>
<c:set var="PODE_EXCLUIR" scope="page" value="<%=new Boolean(Neo.getApplicationContext().getAuthorizationManager().isAuthorized(Util.web.getFirstUrl(), \"excluir\", Neo.getRequestContext().getUser())) %>" />

<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Reversão de Pedidos</h3>
	<n:form validate="false">
		<n:validation>
	     	<t:janelaFiltro>
				<div class="panel panel-default" style="border: 0px;" >
					<div class="panel-body">
						<div class="col-md-2 pull-right" style="text-align: -webkit-right;">
							<n:link class="btn btn-info btn-sm" action="criar" id="btn_novo" checkPermission="true">Novo</n:link>
							<n:link url="#" class="btn btn-danger btn-sm" onclick="javascript:excluirItensSelecionados();" id="btn_excluir">Excluir</n:link>
						</div>		
			        </div>
				</div>
				
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
						 	<t:property name="cdmanifesto" label="Núm. do Manifesto" mode="input" showLabel="true" class="form-control input-md"/>
						</div>
					</div>
				</div>
				
			</t:janelaFiltro>
			
			<div class="panel panel-default" style="border: 0px;" >
				<div class="panel-body">
					<div class="col-md-4 pull-right" style="text-align: -webkit-right;">
						<n:input name="resetCurrentPage" type="hidden" write="false"/>
					    <n:link url="#" class="btn btn-default" onclick="javascript:$n.clearForm(\"form\");setDefaultDeposito();" id="btn_limpar">Limpar filtro</n:link>
						<n:link url="#" onclick="javascript:doFilter();resetPage();" id="btn_filtro" class="btn btn-success btn-md" >Filtrar</n:link>		
					</div>		
		        </div>
			</div>
			
			<br />

			<div class="panel panel-default" style="border: 0px !important;">
				<div class="panel-body">
					<div class="table-responsive">
						<t:tabelaResultados> 
							<t:property name="cdmanifesto" label="Manifesto" mode="output"/>
							<t:property name="manifestostatus" mode="output"/>
							<t:property name="tipoentrega" mode="output"/>
							<t:property name="dtemissao" mode="output"/>
							<t:property name="veiculo" mode="output"/>
							<t:property name="transportador" mode="output"/>
							<t:property name="motorista" mode="output"/>
						</t:tabelaResultados>
					</div>
				</div>
			</div>
		
		</n:validation>
	</n:form>
 	
</div>

<script type="text/javascript">
	
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
	}
	
	function validaFiltro(){
		
		var isError = false;
		var msg = "";
		
		if(form['deposito'].value == "<null>"){
			msg += "O campo Depósito é obrigatório";
			isError = true;
		}

		//TODO validações diversas.
		if(isError==true){
			alert(msg);
			return false;
		}else{
			return true;	
		}
	}
	
</script>
