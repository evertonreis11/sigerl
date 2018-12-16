<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Termo de Entrega</h3>
	<n:form method="post" action="gerar" validateFunction="validarFormulario" >
		<t:janelaFiltro>
			<div class="row">
				<div class="form-group col-md-12">
					<div class="col-md-6 col-md-offset-3">
						<div class="input-group">
					       <label for="valorInicial" style="margin-top: 1%; margin-left: 30%;">Informe a chave da nota fiscal:</label>  
					       <t:property name="chaveNotaFiscal" id="cdExpedicaoLoja" mode="input"
					       				style="width: 100%;" class="form-control input-lg" onchange="executarAcao();"/>
					       
					       <div class="input-group-btn" style="padding-top: 5.3%;padding-left: 1%;">
					       		<button class="btn btn-success btn-lg" type="submit" id="buttonGerar" onclick="submitForm();">Gerar</button>
					       </div>
						</div>
					</div>
				</div>
			</div>
		</t:janelaFiltro>
	</n:form>
</div>


<script type="text/javascript">
	$(document).ready(function() {
		$("#cdExpedicaoLoja").focus();
	});

	function clearForm(){
		$("#cdExpedicaoLoja").val('');
		$("#cdExpedicaoLoja").focus();
		clearMessages();
	}

</script>	