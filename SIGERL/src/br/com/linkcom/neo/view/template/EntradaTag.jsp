<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<n:form validate="false" validateFunction="validarFormulario" enctype="${entradaTag.formEnctype}">
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
		<c:if test="${consultar}">
			<input type="hidden" name="forcarConsulta" value="true"/>
			<style>input, select, textarea, .required {background-color:#ffffff; color:#000000;}</style>
		</c:if>
		<c:if test="${param.fromInsertOne == 'true'}">
			<input type="hidden" name="fromInsertOne" value="true"/>
		</c:if>
		<table class="outterTable" cellspacing="0" align="center">
			<tr class="outterTableHeader">
				<td>
					<span class="outterTableHeaderLeft">${entradaTag.titulo}</span>
					<span class="outterTableHeaderRight">
						<c:if test="${!pos_acao_ladoDireito}">
							${acoes}
						</c:if>					
						<c:if test="${consultar}">
							<c:if test="${(empty entradaTag.showListagemLink) || entradaTag.showListagemLink}">
								<n:link action="listagem" id="btn_voltar"  checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>&nbsp;|&nbsp;
							</c:if>
							<c:if test="${(empty entradaTag.showNewLink) || entradaTag.showNewLink}">
								<n:link action="criar"  id="btn_novo" class="outterTableHeaderLink"  checkPermission="true" description="Novo">Novo</n:link>&nbsp;|&nbsp;
							</c:if>
							<c:if test="${(empty entradaTag.showEditLink) || entradaTag.showEditLink}">								
								<n:link action="editar" id="btn_editar" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" class="outterTableHeaderLink" checkPermission="true" description="Editar">Editar</n:link>&nbsp;|&nbsp;
							</c:if>
							<c:if test="${(empty entradaTag.showDeleteLink) || entradaTag.showDeleteLink}">
								<n:link action="excluir" id="btn_excluir" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" confirmationMessage="Você tem certeza que deseja excluir este registro?" class="outterTableHeaderLink"  checkPermission="true" description="Excluir">Excluir</n:link>
							</c:if>
						</c:if>
						<c:if test="${!consultar}">
							<c:if test="${(empty entradaTag.showListagemLink) || entradaTag.showListagemLink}">
								<c:if test="${(empty entradaTag.dynamicAttributesMap['showconfirmationreturn']) || entradaTag.dynamicAttributesMap['showconfirmationreturn']}">
									<n:link action="listagem" id="btn_voltar" confirmationMessage="Deseja retornar à listagem sem salvar as alterações?" class="outterTableHeaderLink" checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>					
								</c:if>
								<c:if test="${(!empty entradaTag.dynamicAttributesMap['showconfirmationreturn']) && !entradaTag.dynamicAttributesMap['showconfirmationreturn']}">
									<n:link action="listagem" id="btn_voltar" class="outterTableHeaderLink" checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>					
								</c:if>
							</c:if>

							<c:if test="${(empty entradaTag.showSaveLink) || entradaTag.showSaveLink}">
								|&nbsp;&nbsp;
								<n:submit type="link" id="btn_gravar" title="Gravar" action="salvar" validate="true" confirmationScript="${entradaTag.dynamicAttributesMap['submitconfirmationscript']}" class="outterTableHeaderLink" checkPermission="true" description="Salvar">Salvar</n:submit>
								
								<c:if test="${n:id(n:reevaluate(TEMPLATE_beanName,pageContext)) ne null }">
									|&nbsp;&nbsp;
									<n:link action="consultar" id="btn_cancelar" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" confirmationMessage="Deseja retornar à consulta sem salvar as alterações?" checkPermission="true" description="Cancelar">Cancelar</n:link>
								</c:if>
							</c:if>
							
						</c:if>
						${entradaTag.invokeLinkArea}
						<c:if test="${pos_acao_ladoDireito}">
							${acoes}
						</c:if>
						<script>
							function alertExclude(){
								confirm("Você tem certeza que deseja excluir este registro?");
							}
							
							function alertCancel(){
								return confirm("Deseja retornar à consulta sem salvar as alterações?");
							}
						</script>				
					</span>				

				</td>
			</tr>
			<tr>
				<td class="tableBody">
					<n:bean name="${TEMPLATE_beanName}">
					<n:doBody />
					</n:bean>
				</td>
			</tr>
		</table>
	</n:validation>
</n:form>