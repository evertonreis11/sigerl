<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<textarea id="resultado" name="resultado" readonly="readonly" style="width: 100%;" cols="300" rows="5" class="form-control input-lg">${filtro.resultado}</textarea>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
	       <label for="valorInicial" style="margin-top: 3%; margin-left: 3%;">${filtro.labelDinamico}</label>  
		   <input id="valorInicial" name="valorInicial" type="text" value="${filtro.valorInicial}" style="width: 100%;" class="form-control input-lg" onchange="submitForm();" required autocomplete="off"/>
		</div>
	</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#valorInicial").focus();
	});

	function clearForm(){
		$("#valorInicial").val('');
		$("#resultado").val('');
		$("#valorInicial").focus();
		clearMessages();
	}
	
</script>	
