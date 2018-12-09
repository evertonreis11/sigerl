<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<c:set var="prefix" value="item.${propertyDescription}"/>

<c:if test="${!empty itens}" >
	<c:if test="${renderas=='double'}">
		<n:panel>${label}</n:panel>
		<n:panel>
			<n:panelGrid columns="2" columnStyles="text-align:left; padding-right:30px;" columnStyleClasses="propertyColumn,propertyColumn">
				<c:forEach var="item" items="${itens}" varStatus="status">
					<n:panel class="checklistlabel">
						<label for="cl_${name}${status.count}">
							<n:input name="${name}" id="cl_${name}${status.count}" onchange="${inputOnChange}" onclick="${inputOnClick}" type="checklist" value="${item}" itens="${n:reevaluate(property,pageContext)}"/>${n:reevaluate(prefix,pageContext)}
						</label>
					</n:panel>
				</c:forEach>
			</n:panelGrid>
		</n:panel>
	</c:if>
	<c:if test="${renderas=='doubleline'}">
		<n:panel>
			<n:panel>${label}</n:panel>
			<n:panelGrid columns="5" columnStyles="text-align:left; padding-right:5px;" columnStyleClasses="propertyColumn,propertyColumn">
				<c:forEach var="item" items="${itens}" varStatus="status">
					<n:panel class="checklistlabel">
						<label for="cl_${name}${status.count}">
							<n:input name="${name}" id="cl_${name}${status.count}" onchange="${inputOnChange}" onclick="${inputOnClick}" type="checklist" value="${item}" itens="${n:reevaluate(property,pageContext)}"/>${n:reevaluate(prefix,pageContext)}
						</label>
					</n:panel>
				</c:forEach>
			</n:panelGrid>
		</n:panel>
	</c:if>
	<c:if test="${renderas=='column'}">
		<n:column>
			<n:header>
				${label}
			</n:header>				
			<n:body>		
				<n:panelGrid columns="1" columnStyles="text-align:left; padding-right:30px;" columnStyleClasses="checklistdetalhe">
					<c:forEach var="item" items="${itens}" varStatus="status">
						<n:panel><label for="cl_${name}${status.count}"><n:input name="${name}" id="cl_${name}${status.count}" type="checklist" value="${item}" itens="${n:reevaluate(property,pageContext)}"/>${n:reevaluate(prefix,pageContext)}</label></n:panel>
					</c:forEach>
				</n:panelGrid>
			</n:body>
		</n:column>
	</c:if>
	<c:if test="${empty renderas}">
		<n:panel>${label}</n:panel>
		<n:panel>
			<n:panelGrid columns="1" columnStyles="text-align:left; padding-right:30px;" columnStyleClasses="propertyColumn,propertyColumn">
				<c:forEach var="item" items="${itens}" varStatus="status">
					<n:panel class="checklistlabel">
						<label for="cl_${name}${status.count}">
							<n:input name="${name}" id="cl_${name}${status.count}" onchange="${inputOnChange}" onclick="${inputOnClick}" type="checklist" value="${item}" itens="${n:reevaluate(property,pageContext)}"/>${n:reevaluate(prefix,pageContext)}
						</label>
					</n:panel>
				</c:forEach>
			</n:panelGrid>
		</n:panel>
	</c:if>
</c:if>
