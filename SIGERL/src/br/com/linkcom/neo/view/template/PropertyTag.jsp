<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<n:property name="${Tproperty.name}">
	${Tproperty.idConfig} <%-- Le os atributos da propriedade para saber se é ID --%>
	<c:choose>
		<c:when test="${Tproperty.renderAs == 'column'}">
			<%-- column --%>		
			<n:column>
				<c:if test="${Tproperty.entityId}">
					<n:header style="width: 1%; padding-right: 3px;${Tproperty.headerStyle}" class="${Tproperty.headerStyleClass}">
						${Tproperty.header}
					</n:header>
				</c:if>
				<c:if test="${!Tproperty.entityId}">
					<n:header style="${Tproperty.headerStyle}" class="${Tproperty.headerStyleClass}">
						${Tproperty.header}
					</n:header>
				</c:if>				
				<n:body align="${Tproperty.columnAlign}" style="${Tproperty.bodyStyle}" class="${Tproperty.bodyStyleClass}">
					<c:if test="${Tproperty.mode == 'input'}">
						
						<n:input pattern="${Tproperty.pattern}" insertOnePathParameters="${Tproperty.insertOnePathParameters}" selectOnePathParameters="${Tproperty.selectOnePathParameters}" insertPath="${Tproperty.insertPath}" holdValue="${Tproperty.holdValue}" showRemoverButton="${Tproperty.showRemoverButton}" optionalParams="${Tproperty.optionalParams}" transientFile="${Tproperty.transientFile}" autoSugestUniqueItem="${Tproperty.autoSugestUniqueItem}" onLoadItens="${Tproperty.onLoadItens}" useAjax="${Tproperty.useAjax}" showLabel="${Tproperty.showLabel}"  type="${Tproperty.type}" reloadOnChange="${Tproperty.reloadOnChange}" onlyPositiveNumbers="${Tproperty.onlyPositiveNumbers}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" selectOnePath="${Tproperty.selectOnePath}" selectOneWithAddChangeFuncition="${Tproperty.selectOneWithAddChangeFuncition}" itens="${Tproperty.itens}"
							label="${Tproperty.label}" autocompleteGetterLabel="${Tproperty.autocompleteGetterLabel}" autocompleteLabelProperty="${Tproperty.autocompleteLabelProperty}" autocompleteMatchProperty="${Tproperty.autocompleteMatchProperty}" autocompleteOnExcluir="${Tproperty.autocompleteOnExcluir}" selectLabelProperty="${Tproperty.selectLabelProperty}" includeBlank="${Tproperty.includeBlank}" blankLabel="${Tproperty.blankLabel}" cols="${Tproperty.cols}" rows="${Tproperty.rows}" write="${Tproperty.write}" dynamicAttributesMap="${Tproperty.dynamicAttributesMap}" showFileLink="${Tproperty.showFileLink}" order="${Tproperty.order}" ignoreRequired="${Tproperty.ignoreRequired}">
							<n:doBody />
						</n:input>
						<c:if test="${Tproperty.dynamicAttributesMap['comment'] != null && Tproperty.dynamicAttributesMap['comment'] != ''}">
		 					<span class="inputComment">${Tproperty.dynamicAttributesMap['comment']}</span>
		 				</c:if>
					</c:if>
					<c:if test="${Tproperty.mode == 'output'}">
						<n:output pattern="${Tproperty.pattern}" style="${Tproperty.dynamicAttributesMap['style']}" styleClass="${Tproperty.dynamicAttributesMap['styleclass']}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" truncate="${Tproperty.truncate}" escapeHTML="false"/>
					</c:if>
				</n:body>
			</n:column>
		</c:when>
		<c:when test="${Tproperty.entityId && empty value}">
			<%-- 
				Se a propriedade for ID e for nula, não escrever nada... 
				Se for ID mas tiver valor, cai nas outras opções 
			<n:panel colspan="${Tproperty.colspan}" style="${Tproperty.panelStyle}" class="${Tproperty.panelStyleClass}">
			</n:panel>
			--%>
		</c:when>
		<c:when test="${Tproperty.renderAs == 'double'}">
			<%-- double --%>
			<c:if test="${(empty Tproperty.showLabel) || Tproperty.showLabel}">
				<n:panel style="${Tproperty.labelStyle}" class="${Tproperty.labelStyleClass}" >
					<label for="${compId}"><n:output pattern="${Tproperty.pattern}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" value="${Tproperty.label}" /></label>
				</n:panel>
			</c:if>
			<n:panel colspan="${Tproperty.colspan}" style="${Tproperty.panelStyle}" class="${Tproperty.panelStyleClass}">
				<c:if test="${Tproperty.mode == 'input'}">
					<%-- Vou passar showLabel="false" porque o label já foi impresso no panel acima, se era para imprimir --%>
					<n:input pattern="${Tproperty.pattern}" insertOnePathParameters="${Tproperty.insertOnePathParameters}" selectOnePathParameters="${Tproperty.selectOnePathParameters}" insertPath="${Tproperty.insertPath}"  holdValue="${Tproperty.holdValue}" showRemoverButton="${Tproperty.showRemoverButton}" optionalParams="${Tproperty.optionalParams}"  transientFile="${Tproperty.transientFile}" autoSugestUniqueItem="${Tproperty.autoSugestUniqueItem}" onLoadItens="${Tproperty.onLoadItens}" useAjax="${Tproperty.useAjax}" showLabel="false" type="${Tproperty.type}" reloadOnChange="${Tproperty.reloadOnChange}" onlyPositiveNumbers="${Tproperty.onlyPositiveNumbers}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" selectOnePath="${Tproperty.selectOnePath}" selectOneWithAddChangeFuncition="${Tproperty.selectOneWithAddChangeFuncition}" itens="${Tproperty.itens}"
						label="${Tproperty.label}" id="${compId}" selectLabelProperty="${Tproperty.selectLabelProperty}" includeBlank="${Tproperty.includeBlank}" blankLabel="${Tproperty.blankLabel}" cols="${Tproperty.cols}" rows="${Tproperty.rows}" write="${Tproperty.write}" dynamicAttributesMap="${Tproperty.dynamicAttributesMap}"
						 autocompleteGetterLabel="${Tproperty.autocompleteGetterLabel}" autocompleteLabelProperty="${Tproperty.autocompleteLabelProperty}" autocompleteMatchProperty="${Tproperty.autocompleteMatchProperty}" autocompleteOnExcluir="${Tproperty.autocompleteOnExcluir}" showFileLink="${Tproperty.showFileLink}" order="${Tproperty.order}" ignoreRequired="${Tproperty.ignoreRequired}">
						<n:doBody />
					</n:input>
					<c:if test="${Tproperty.dynamicAttributesMap['comment'] != null && Tproperty.dynamicAttributesMap['comment'] != ''}">
		 				<span class="inputComment">${Tproperty.dynamicAttributesMap['comment']}</span>
		 			</c:if>
				</c:if>
				<c:if test="${Tproperty.mode == 'output'}">
					<n:output truncate="${Tproperty.truncate}"  pattern="${Tproperty.pattern}" styleClass="${Tproperty.dynamicAttributesMap['styleclass']}" style="${Tproperty.dynamicAttributesMap['style']}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" escapeHTML="true"/>
				</c:if>
			</n:panel>
		</c:when>
		<c:otherwise>
			<%-- single --%>
			<n:panel colspan="${Tproperty.colspan}" style="${Tproperty.panelStyle}" class="${Tproperty.panelStyleClass}">
				<c:if test="${Tproperty.mode == 'input'}">
					<n:input pattern="${Tproperty.pattern}" insertOnePathParameters="${Tproperty.insertOnePathParameters}" selectOnePathParameters="${Tproperty.selectOnePathParameters}" insertPath="${Tproperty.insertPath}"  holdValue="${Tproperty.holdValue}" showRemoverButton="${Tproperty.showRemoverButton}" optionalParams="${Tproperty.optionalParams}"  transientFile="${Tproperty.transientFile}" autoSugestUniqueItem="${Tproperty.autoSugestUniqueItem}" onLoadItens="${Tproperty.onLoadItens}" useAjax="${Tproperty.useAjax}" labelStyle="${Tproperty.labelStyle}" labelStyleClass="${Tproperty.labelStyleClass}" showLabel="${Tproperty.showLabel}" type="${Tproperty.type}" reloadOnChange="${Tproperty.reloadOnChange}"  onlyPositiveNumbers="${Tproperty.onlyPositiveNumbers}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" selectOnePath="${Tproperty.selectOnePath}" selectOneWithAddChangeFuncition="${Tproperty.selectOneWithAddChangeFuncition}" itens="${Tproperty.itens}"
						label="${Tproperty.label}&nbsp;${labelseparator}" selectLabelProperty="${Tproperty.selectLabelProperty}" includeBlank="${Tproperty.includeBlank}" blankLabel="${Tproperty.blankLabel}" cols="${Tproperty.cols}" rows="${Tproperty.rows}" write="${Tproperty.write}" id="${compId}" dynamicAttributesMap="${Tproperty.dynamicAttributesMap}"
						 autocompleteGetterLabel="${Tproperty.autocompleteGetterLabel}" autocompleteLabelProperty="${Tproperty.autocompleteLabelProperty}" autocompleteMatchProperty="${Tproperty.autocompleteMatchProperty}" autocompleteOnExcluir="${Tproperty.autocompleteOnExcluir}" showFileLink="${Tproperty.showFileLink}" order="${Tproperty.order}" ignoreRequired="${Tproperty.ignoreRequired}">
						<n:doBody />
		 			</n:input>
		 			<c:if test="${Tproperty.dynamicAttributesMap['comment'] != null && Tproperty.dynamicAttributesMap['comment'] != ''}">
		 				<span class="inputComment">${Tproperty.dynamicAttributesMap['comment']}</span>
		 			</c:if>
				</c:if>
				<c:if test="${Tproperty.mode == 'output'}">
					<c:if test="${Tproperty.showLabel}">
						<n:panel>
						<n:output pattern="${Tproperty.pattern}" value="${Tproperty.label} ${labelseparator}" style="${Tproperty.labelStyle}" styleClass="${Tproperty.labelStyleClass}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" escapeHTML="false"/>															
						<n:output truncate="${Tproperty.truncate}" pattern="${Tproperty.pattern}" styleClass="${Tproperty.dynamicAttributesMap['styleclass']}" style="${Tproperty.dynamicAttributesMap['style']}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" escapeHTML="true"/>										
						</n:panel>
					</c:if>
					<c:if test="${!Tproperty.showLabel}">
						<n:output truncate="${Tproperty.truncate}" pattern="${Tproperty.pattern}" styleClass="${Tproperty.dynamicAttributesMap['styleclass']}" style="${Tproperty.dynamicAttributesMap['style']}" trueFalseNullLabels="${Tproperty.trueFalseNullLabels}" escapeHTML="true"/>					
					</c:if>					
				</c:if>
			</n:panel>
		</c:otherwise>
	</c:choose>
</n:property>