<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<n:hasAuthorization action="${TEMPLATE_authorization}">
	<n:form validate="false" validateFunction="validarFormulario"  enctype="${entradaTag.formEnctype}">
		<n:validation  functionName="validateForm">
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
			<c:if test="${param.fromInsertOne == 'true'}">
				<input type="hidden" name="fromInsertOne" value="true"/>
			</c:if>
			<table class="outterTable" cellspacing="0" align="center">
				<tr>
					<td>
						<n:bean name="${TEMPLATE_beanName}">
							<n:doBody />
						</n:bean>
					</td>
				</tr>
			</table>
		</n:validation>
	</n:form>
</n:hasAuthorization>