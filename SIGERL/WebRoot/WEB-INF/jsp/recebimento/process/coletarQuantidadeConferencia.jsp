<%@page import="br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro"%>
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
	            <label>Conferência de Recebimento</label>  
	        </div>
		</div>
		<div class="row">
			<div class="col-xs-9 col-xs-offset-3" >
	            <label>${filtro.tipoColeta} - O.S.: ${filtro.ordemServico.cdordemservico}</label>  
	        </div>
		</div>
	</div>
</div>

<div id="componentes" style="margin-top: 4%;">
	<n:form method="post" action="coletarQuantidade">
	
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
	        
	        <div class="form-group col-xs-12" style="margin-top: 2%;">
	            
	        	<div class="col-xs-4 col-xs-offset-5" style="">
	            	<button class="btn btn-primary btn-lg" type="button" id="buttonLimpar" onclick="clearForm();">Limpar</button>
	            </div>
	        </div> 
	     </div>
	</n:form>
</div>

<script type="text/javascript">

$(document).ready(function() {
	$('#valorInicial').prop('type', 'number');
});

function clearForm(){
	$("#valorInicial").val('');
	$("#valorInicial").focus();
	clearMessages();
}

</script>
