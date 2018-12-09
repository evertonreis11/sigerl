<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<c:set var="panelWidth" value="${n:default('100%',TableGroupTag.panelgridWidth)}"/>

<n:panel colspan="${TableGroupTag.panelColspan}">
	<n:panelGrid columns="${TableGroupTag.columns}" columnStyles="padding-left: 4px" border="0" cellspacing="0" cellpadding="0" width="${panelWidth}" columnStyleClasses="propertyColumn">
 		<t:propertyConfig renderAs="doubleline">
 			<n:doBody />
 		</t:propertyConfig>
 	</n:panelGrid>
 </n:panel>

 					