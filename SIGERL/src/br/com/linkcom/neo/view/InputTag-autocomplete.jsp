<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%request.setAttribute("app", request.getContextPath());%>
<span id="spanAutocomplete_${tag.name}">
	<input type="text" 
	name="${tag.name}_label" 
	funcao="autocomplete" 
	getterLabel="${tag.autocompleteGetterLabel}"
	propertyLabel="${tag.autocompleteLabelProperty}"
	propertyMatch="${tag.autocompleteMatchProperty}"
	loadFunctionAutocomplete="${tag.loadFunctionAutocomplete}" 
	beanName="${tag.beanName}"
	value="${tag.autocompleteDescriptionLabel}"  
	${tag.dynamicAttributesToString} />
	<input type="hidden" id="${tag.id}" autocompleteId="${tag.name}_value" name="${tag.name}" value="${tag.autocompleteIdWithDescription}"/>
	<c:if test="${!consultar}">
		<span autocompleteId="${tag.name}_NaoSelecionado" style="display: none;">
			<img title="Não selecionado" alt="NÃO SELECIONADO" src="${app}/resource/js/autocomplete/autocomplete_exclamacao.png"/>
			<img title="Não selecionado" border="0" alt="EXCLUIR" src="${app}/resource/js/autocomplete/autocomplete_excluir.png"/>
		</span>
		<span autocompleteId="${tag.name}_Selecionado" style="display: none;">
			<img title="Selecionado" alt="SELECIONADO" src="${app}/resource/js/autocomplete/autocomplete_selecionado.png"/>
			<a href="javascript:excluirAutocomplete('${tag.name}');${tag.autocompleteOnExcluir}"><img title="Remover valor" border="0" alt="EXCLUIR" src="${app}/resource/js/autocomplete/autocomplete_excluir.png"/></a>
		</span>
		<c:if test="${tag.insertPath != null && tag.insertPath != ''}">
			<span style="padding-left: 5px; cursor: pointer;" onclick="${tag.insertOneButtonOnClickAutocomplete}">
				<img border="0" src="${app}/imagens/icone/btnNovo.gif" onmouseover="Tip('Novo')" />
			</span>
		</c:if>
	</c:if>
</span>
