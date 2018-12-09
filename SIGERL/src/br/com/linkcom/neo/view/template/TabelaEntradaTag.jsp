<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<n:panel title="${TtabelaEntrada.title}" colspan="${TtabelaEntrada.colspan}">
	<n:panelGrid columns="${TtabelaEntrada.columns}" style="${TtabelaEntrada.style}" styleClass="${TtabelaEntrada.styleClass}" rowStyleClasses="${TtabelaEntrada.rowStyleClasses}" rowStyles="${TtabelaEntrada.rowStyles}"
		columnStyleClasses="${TtabelaEntrada.columnStyleClasses}" columnStyles="${TtabelaEntrada.columnStyles}" colspan="${TtabelaEntrada.colspan}" propertyRenderAsDouble="${TtabelaEntrada.propertyRenderAsDouble}"
		dynamicAttributesMap="${TtabelaEntrada.dynamicAttributesMap}" cellpadding="1" cellspacing="1">
		<t:propertyConfig mode="input" showLabel="${tag.propertyShowLabel}" disabled="${consultar}">
			<n:doBody />
		</t:propertyConfig>
	</n:panelGrid>
</n:panel>