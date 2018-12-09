<input type="date"
	   id="${tag.id}"
	   name="${tag.name}"
	   value="${tag.valueToString}" 
	   maxlength="10"
	   size="11" 
	   mask="mesano"
	   ${tag.dynamicAttributesToString}/>
<input type="hidden" name="${tag.name}_datePattern" value="${tag.pattern}"/>