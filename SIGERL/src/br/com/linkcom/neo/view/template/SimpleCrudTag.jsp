<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<table class="outterTable" cellspacing="0" align="center">
	<tr class="outterTableHeader">
				<td>
					<span class="outterTableHeaderLeft">
						${titulo}
					</span>
					<span class="outterTableHeaderRight">
						<n:link url="#" id="btn_excluir"><img src="${ctx}/resource/img/excluir.gif"></n:link>
					</span>
				</td>
			</tr>
	<tr>
		<td>
			<n:doBody />
		</td>
	</tr>
</table>