<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>

<form method="${tag.method}" name="${tag.name}" enctype="${tag.enctype}" action="${tag.url}" ${tag.dynamicAttributesToString} onsubmit="return false;">
	<input type="hidden" name="${tag.actionParameter}" value="${tag.action}"/>
	<input type="hidden" name="suppressValidation" value="false"/>
	<input type="hidden" name="suppressErrors" value="false"/>
	<script language="javascript">
		var ${tag.name} = document.forms["${tag.name}"];
		${tag.name}.validate ='${tag.validate}';
		function ${tag.submitFunction}() {
			var validar = ${tag.name}.validate;
			try {
				${tag.validateFunction};
			} catch (e) {
				validar = false;
			}
			try {
				clearMessages();//limpa as mensagens que vieram do servidor
			} catch(e){
			}
			if(validar == 'true') {
				var valid = ${tag.validateFunction}();
				if(valid) {
					${tag.name}.submit();
				}
			} else {
				${tag.name}.submit();
			}
		}
	</script>
	<n:doBody/>
</form>