<input type="text" id="${tag.id}" name="${tag.name}" value="${tag.valueToString}" maxlength="${tag.dynamicAttributesMap['maxlength']}" mask="timestamp" size="${tag.dynamicAttributesMap['size']}" onchange="${tag.reloadOnChangeString}" ${tag.dynamicAttributesToString}/>
<input type="hidden" name="${tag.name}_datePattern" value="${tag.pattern}"/>