<%@page import="br.com.ricardoeletro.coletor.modulo.recebimento.process.filtro.ConferenciaRecebimentoFiltro"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<n:form method="post" action="${Ttela.formAction}" validateFunction="validarFormulario">
	<jsp:include page="../../inputTextAreaPage.jsp"></jsp:include>
	
	<div class="row">
		<n:bean name="filtro" valueType="<%=ConferenciaRecebimentoFiltro.class%>">
			<t:property name="iniciarColeta" type="hidden" />
			<t:property name="recebimento" type="hidden" />
			<t:property name="veiculo" type="hidden" />
			<t:property name="ordemServico" type="hidden" />
			<t:property name="ordemservicoUsuario" type="hidden" />
		 	<t:property name="tipoColeta" type="hidden" />
        </n:bean>
        
        <div class="form-group col-xs-12" style="margin-top: 2%;">
            
        	<div class="col-xs-9 col-xs-offset-3" style="">
            	<button class="btn btn-primary btn-lg" type="button" id="buttonLimpar" onclick="clearForm();">Limpar</button>
            	<button class="btn btn-primary btn-lg" type="submit" id="buttonColeta" onclick="iniciarConferencia();" style="margin-left: 5%" disabled="disabled">Iniciar Conferência</button>
            </div>
        </div> 
     </div>
</n:form>


<script type="text/javascript">

$(document).ready(function() {
	$('#valorInicial').prop('type', 'number');
	if ($("input[name=iniciarColeta]").val() != '' && $("input[name=iniciarColeta]").val() == 'true'){
		$("#buttonColeta").removeAttr('disabled');
		$("input[name=iniciarColeta]").val('');
		
	}else{
		$("#buttonColeta").attr('disabled','disabled');
	}

});

function clearForm(){
	$("#valorInicial").val('');
	$("#resultado").val('');
	$("#valorInicial").focus();
	$("#buttonColeta").attr('disabled','disabled');
	$("input[name=iniciarColeta]").val('');
	clearMessages();
}

function iniciarConferencia() {
	form.ACAO.value ='iniciarConferencia';
	form.validate = 'false'; 
	submitForm();
}

</script>