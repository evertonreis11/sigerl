<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

	<div class="row">
		<div class="col-md-6 col-md-offset-3">
	       <label for="valorInicial" style="margin-top: 1%; margin-left: 30%;">${filtro.labelDinamico}</label>  
	       <t:property name="valorInicial" id="valorInicial" mode="input"
	       				style="width: 100%;" class="form-control input-lg" onchange="executarAcao();"/>
		</div>
	</div>
	
<script type="text/javascript">
	$(document).ready(function() {
		$("#valorInicial").focus();
	});

	function clearForm(){
		$("#valorInicial").val('');
		$("#valorInicial").focus();
		clearMessages();
	}

</script>	
