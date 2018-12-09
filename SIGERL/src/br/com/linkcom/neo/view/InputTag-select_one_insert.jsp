<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<span style="white-space: nowrap; width: ${tag.dynamicAttributesMap['spanwidth']};">
	<table border="0" style="width: ${tag.dynamicAttributesMap['spanwidth']};" cellpadding="0" cellspacing="0" id="${tag.id}_table">
	<tr>
		<td id="selectoneinsert_td_${tag.name}">
			<select name="${tag.name}" id="${tag.id}" onchange="${tag.reloadOnChangeString}" ${tag.dynamicAttributesToString}>${tag.selectoneblankoption}${tag.selectItensString}</select>
		</td>
		<c:if test="${!consultar}">
			<td style="padding-left: 3px; padding-top: 3px;" id="selectoneinsert_td_button_${tag.name}">
				<button id="btn_insert_one" 
						name="${tag.name}_btn" 
						type="button" 
						onclick="${tag.selectOneInsertOnClick}" 
						onmouseover="Tip('Novo')" 
						style='border: 0px; background-color: transparent; text-transform: none; height: 19px;'/>
			</td>
		</c:if>
	</tr>
	</table>
</span>