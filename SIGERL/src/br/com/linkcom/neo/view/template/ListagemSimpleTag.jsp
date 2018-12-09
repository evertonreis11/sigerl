<%@ taglib prefix="n" uri="neo"%>
<n:hasAuthorization action="listagem">
	<n:form validate="false" name="listagem" enctype="${listagemTag.formEnctype}">
		<n:validation>
			<input type="hidden" name="notFirstTime" value="true"/>
			<table class="outterTable" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td>
						<n:doBody />
					</td>
				</tr>
			</table>
		</n:validation>
	</n:form>
</n:hasAuthorization>