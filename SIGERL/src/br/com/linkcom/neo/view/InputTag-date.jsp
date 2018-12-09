<input type="text"
	   id="${tag.id}"
	   name="${tag.name}"
	   value="${tag.valueToString}" 
	   maxlength="10"
	   size="11" 
	   onKeyUp="mascara_data(this,event,'${tag.pattern}');" 
	   onKeyPress="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){hide_calendar();return valida_tecla_data(this, event,'${tag.pattern}');}"
	   onChange="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){${tag.reloadOnChangeString}${tag.dynamicAttributesMap['change']}}" 
	   onfocus="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){this.select();lcs(this)}" 
	   onclick="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){event.cancelBubble=true;this.select();lcs(this)}"
	   onblur="if($(this).attr('readonly') == null || $(this).attr('readonly') == false){verifica_data(this);${tag.dynamicAttributesMap['blur']}}" ${tag.dynamicAttributesToString}/>
<input type="hidden" name="${tag.name}_datePattern" value="${tag.pattern}"/>