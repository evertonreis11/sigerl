<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<div class="col-md-6 pull-right" style="text-align: -webkit-right;">
	<c:if test="${consultar}">
		<c:if test="${(empty entradaTag.showListagemLink) || entradaTag.showListagemLink}">
			<n:link action="listagem" id="btn_voltar"  checkPermission="true" description="Retornar à listagem" class="btn btn-default btn-sm">Retornar à listagem</n:link>&nbsp;|&nbsp;
		</c:if>
		
		<n:link action="criar"  id="btn_novo" class="btn btn-info btn-sm"  checkPermission="true" description="Novo">Novo</n:link>&nbsp;|&nbsp;
		
		<c:if test="${(isEmElaboracao || isAguardandoLiberacao) && !isAutorizado}">								
			<n:link action="editar" id="btn_editar" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" class="btn btn-primary btn-sm" checkPermission="true" description="Editar">Editar</n:link>&nbsp;|&nbsp;
		</c:if>
		
		<c:if test="${(isEmElaboracao || isAguardandoLiberacao) && !isAutorizado}">
			<n:link action="excluir" id="btn_excluir" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" confirmationMessage="Você tem certeza que deseja excluir este registro?" class="btn btn-danger btn-sm"  checkPermission="true" description="Excluir">Excluir</n:link>
		</c:if>
	</c:if>
	<c:if test="${!consultar}">
		<n:link action="listagem" id="btn_voltar" confirmationMessage="Deseja retornar à listagem sem salvar as alterações?" class="btn btn-default btn-sm" checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>					
		<n:submit type="link" id="btn_gravar" title="Gravar" action="salvar" validate="true" confirmationScript="${entradaTag.dynamicAttributesMap['submitconfirmationscript']}" class="btn btn-success btn-sm" checkPermission="true" description="Salvar" data-toggle="tooltip">Salvar</n:submit>
	</c:if>
	
	
	<c:if test="${!isEmElaboracao && !isAguardandoLiberacao && consultar}">
		<c:choose>
			<c:when test="${isImpresso}">
				<a id="btn_excluir"  onmouseover="Tip('Cancelar Manifesto')" href="javascript:openDialogAuditoria()" class="btn btn-danger btn-sm">Cancelar</a>
			</c:when>
			<c:otherwise>
				<span>Excluir</span> 
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${isEmElaboracao || isAguardandoLiberacao || (!isImpresso && isAutorizado && consultar)}">
		<c:if test="${isEmElaboracao && consultar}">
			<a id="btn_imprimir" onmouseover="Tip('Imprimir Manifesto')" href="javascript:imprimirManifesto()" class="btn btn-success btn-sm">Imprimir Manifesto</a>
		</c:if>
		<c:if test="${isAguardandoLiberacao && consultar}">
			<a id="btn_associar" onmouseover="Tip('Autorizar Notas')" href="javascript:autorizarNotas()" class="btn btn-success btn-sm">Autorizar Notas</a>
		</c:if>
		<c:if test="${isAutorizado && consultar}">
			<a id="btn_excluir"  onmouseover="Tip('Cancelar Manifesto')" href="javascript:openDialogAuditoria()" class="btn btn-danger btn-sm">Cancelar</a>
		</c:if>
	</c:if>
	<c:if test="${isImpresso && consultar}">
		<a id="btn_imprimir" onmouseover="Tip('re-imprimir Manifesto')" href="javascript:openDialogReImpressao();" class="btn btn-success btn-sm">Re-Imprimir Manifesto</a>
	</c:if>
</div>	