<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="file" id="${tag.id}" name="${tag.name}" onchange = "${tag.fileOnChange}" value="${tag.fileValue}" ${tag.dynamicAttributesToString}/> 
${tag.arquivoLink}
<input type="hidden" name="${tag.name}_excludeField" id="${tag.name}_excludeField">
<c:if test="${tag.showRemoverButton}">
<button id="${tag.name}_removerbtn" type="button" onclick="document.getElementById('${tag.name}_excludeField').value='true'; document.getElementById('${tag.name}_div').style.textDecoration = 'line-through'" ${tag.showRemoverBtn}>Remover</button>
</c:if>