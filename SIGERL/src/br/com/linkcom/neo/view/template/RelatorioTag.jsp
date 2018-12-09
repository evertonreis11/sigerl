<%@ taglib prefix="n" uri="neo"%>
<n:form validate="true" enctype="${Ttela.formEnctype}">
	<n:validation>
		<table class="outterTable" cellspacing="0" cellpadding="0" align="center">
			<tr style="background: url('${pageContext.request.contextPath}/resource/img/titulo.gif')" class="outterTableHeader">
				<td>
					<span class="outterTableHeaderLeft">
					${Ttela.titulo}
					</span>
				</td>
			</tr>
			<tr>
				<td colspan="1">
					<n:doBody />
				</td>
			</tr>
		</table>
	</n:validation>
</n:form>