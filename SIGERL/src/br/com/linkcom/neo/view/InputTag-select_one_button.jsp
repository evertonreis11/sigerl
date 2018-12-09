<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="text" 
	   name="${tag.name}_label" 
	   id="${tag.id}" 
	   onchange="${tag.reloadOnChangeString}" 
	   readonly="true"
	   value="${tag.descriptionToString}"
	   ${tag.dynamicAttributesToString}/>
	   
<input type="hidden" 
	   name="${tag.name}" 
	   id="input_select_one_button"
	   onchange="${tag.selectOneWithAddChangeFuncition}"
	   value="${tag.valueWithDescriptionToString}"
	   style="padding" />

<c:if test="${!consultar}">
	<button id="${tag.name}_btn" 
			name="${tag.name}_btn" 
			type="button" onclick="${tag.selectOneButtonOnClick}" 
			class="btnApp" style='${tag.selectOneButtonStyle}'>Selecionar</button>
			
	<button id="${tag.name}_btnUnselect" 
			name="${tag.name}_btnUnselect" 
			type="button" 
			onclick="document.getElementsByName('${tag.name}_label')[0].value = ''; document.getElementsByName('${tag.name}')[0].value = '<null>'; document.getElementById('${tag.name}_btn').style.display=''; document.getElementById('${tag.name}_btnUnselect').style.display='none';${tag.selectOneWithAddChangeFuncition}"  
			class="btnApp" 
			style='${tag.selectOneUnselectButtonStyle}'>Limpar</button>
</c:if>