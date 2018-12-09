<input type="text" name="${tag.name}" id="${tag.id}"
	   autocomplete="off"
	   value="${tag.escapeValueToString}"
	   onkeypress="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){reais(this,event);}" 
	   onkeydown="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){backspace(this,event);}" 
	   onblur="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){ $(this).change(); if($(this).val() != '') { $(this).val(demaskvalue(this.value,true).formatCurrency()); } } ${tag.dynamicAttributesMap['onblur']}"
	   onchange="${tag.reloadOnChangeString}"
	   ${tag.dynamicAttributesToString}
/>