<%@page import="br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro"%>
<%@page import="br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro.TipoColeta"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<div class="row" style="background-color: #385687; color: white; border-radius:5px;">
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-9 col-xs-offset-3" style="color:yellow;">
	            <label>WMS CONSOLE - ${filtro.recebimento.cdrecebimento}</label>  
	        </div>
		</div>
		<div class="row">
			<div class="col-xs-9 col-xs-offset-3">
	            <label>Reconferência de Recebimento</label>  
	        </div>
		</div>
		<div class="row">
			<div class="col-xs-9 col-xs-offset-3" >
	            <label><span id="cabecalhoTipoColeta">${filtro.tipoColeta}</span> - O.S.: ${filtro.ordemServico.cdordemservico}</label>  
	        </div>
		</div>
	</div>
</div>

<div id="componentes" style="margin-top: 4%;">
	<n:form method="post" action="coletarProduto">
	
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<label for="resultado" style="margin-top: 3%; margin-left: 3%;">Produto:</label>
			</div>
		</div>
		 
		<jsp:include page="../../textAreaInputPage.jsp"></jsp:include>
		
		<div class="row">
			<n:bean name="filtro" valueType="<%=ConferenciaRecebimentoFiltro.class%>">
				<t:property name="iniciarColeta" type="hidden" />
				<t:property name="recebimento" type="hidden" />
				<t:property name="veiculo" type="hidden" />
				<t:property name="ordemServico" type="hidden" />
				<t:property name="ordemservicoUsuario" type="hidden" />
			 	<t:property name="tipoColeta" type="hidden" />
			 	<t:property name="ordemprodutohistorico" type="hidden" />
			 	<t:property name="codigoBarrasConferencia" type="hidden" />
	        </n:bean>
	        
	      <div class="form-group col-md-12" style="margin-top: 2%;">
	            
	        	<div class="col-xs-9 col-xs-offset-3" style="">
            		<button class="btn btn-primary btn-lg" type="button" id="buttonLimpar" onclick="clearForm();">Limpar</button>
            		<button class="btn btn-primary btn-lg" type="button" id="buttonTrocarProduto" onclick="executarAcao('trocarProduto');" style="margin-left: 4%">Trocar Produto</button>
	            </div>
	        	<div class="col-xs-9 col-xs-offset-3" style="margin-top:2%;">
            		<button class="btn btn-primary btn-lg" type="button" id="buttonAcoes" data-toggle="modal" data-target="#modalAcoes">Ações</button>
            		<button class="btn btn-primary btn-lg" type="button" id="buttonAltColeta" data-toggle="modal" data-target="#modalAlterarColeta" style="margin-left: 5%">Alterar Coleta</button>
	            </div>
	        </div> 
	     </div>
	     
		<div class="modal fade" id="modalAlterarColeta" style="width: 90%; height:90%">
		    <div class="modal-dialog modal-sm">
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal" id="fecharModal" style="display: none;"></button>
		          <h3 class="modal-title"><label>Selecione o tipo de coleta</label></h3>
		        </div>
		        <div class="modal-body">
			        <c:set var="enumValues" value="<%=TipoColeta.values()%>"/>
	                <c:forEach items="${enumValues}" var="tipoColeta">
	                   <div class="radio" style="margin-top: 5%;">
						  <label>
						  	<input type="radio" name="tipoColetaRadio" value="${tipoColeta}" onchange="selecionaTipoColeta(this);" style="width:2em; height:2em;">
						 	 <div style="margin-left: 2em;"><h3>${tipoColeta}<h3></div>
						 </label>
					   </div>
	                 </c:forEach>
		        </div>
		        <div class="modal-footer">
		        </div>
		      </div>
		    </div>
		  </div>
		  
		<div class="modal fade" id="modalAcoes" style="width: 95%; height:95%">
		    <div class="modal-dialog modal-lg">
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal" id="fecharModalAcoes" style="opacity: 1;"><h3>&times;</h3></button>
		          <h3 class="modal-title"><label>Ações</label></h3>
		        </div>
		        <div class="modal-body">
		        	<div class="row" style="margin-top: 2%; margin-left: 20%">
	            		<button class="btn btn-primary btn-lg" type="button" id="buttonTrocar" onclick="executarAcao('trocarCarregamento');" style="height: 80px;"> 1 - Trocar de Recebimento</button>
	            	</div>
		        	<div class="row" style="margin-top: 10%; margin-left: 20%">
	            		<button class="btn btn-primary btn-lg" type="button" id="buttonCancelar" onclick="executarAcao('cancelarCarregamento');" style="height: 80px;">2 - Cancelar Conferência</button>
	            	</div>
	            	<div class="row" style="margin-top: 10%; margin-left: 20%">
	            		<button class="btn btn-primary btn-lg" type="button" id="buttonFinalizar" onclick="executarAcao('finalizarCarregamento');" style="height: 80px;">3 - Finalizar Conferência</button>
            		</div>
		        </div>
		        <div class="modal-footer">
		        </div>
		      </div>
		    </div>
		  </div>
	    </div>
	</n:form>
</div>

<script type="text/javascript">


function clearForm(){
	$("#valorInicial").val('');
	$("#valorInicial").focus();
	clearMessages();
}

function selecionaTipoColeta(campo){
	$("input[name='tipoColeta']").val(campo.value);
	$("#cabecalhoTipoColeta").html(campo.value);
	$("#fecharModal").click();
	
}

function executarAcao(acao){
	$("#fecharModalAcoes").click();
	form.ACAO.value = acao;
	form.validate = 'false'; 
	submitForm();
}

</script>
