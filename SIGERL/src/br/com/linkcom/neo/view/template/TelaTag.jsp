<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<c:if test="${Ttela.includeForm}">
<n:form validate="true" method="${Ttela.formMethod}" action="${Ttela.formAction}" validateFunction="validarFormulario" enctype="${Ttela.formEnctype}">
	<n:validation functionName="validateForm">
		<script language="javascript">
			// caso seja alterada a função validation ela será chamada após a validacao do formulario
			var validation;
			function validarFormulario(){
				var valido = validateForm();
				if(validation){
					valido = validation(valido);
				}
				return valido;
			}
		</script>
		<table class="outterTable" cellspacing="0" align="center">
			<tr class="outterTableHeader">
				<td>
					<span class="outterTableHeaderLeft">${Ttela.titulo}</span>
					<c:if test="${!empty Ttela.dynamicAttributesMap['botoes']}">
						<span class="outterTableHeaderRight">${Ttela.dynamicAttributesMap['botoes']}</span>
					</c:if>
				</td>
			</tr>
			<tr>
				<td>
					<n:doBody />
				</td>
			</tr>
		</table>
	</n:validation>
</n:form>
</c:if>
<c:if test="${!Ttela.includeForm}">
	<table class="outterTable" cellspacing="0" align="center">
		<tr class="outterTableHeader">
			<td>
				<span class="outterTableHeaderLeft">${Ttela.titulo}</span>
			</td>
		</tr>
		<tr>
			<td>
				<n:doBody />
			</td>
		</tr>
	</table>
</c:if>