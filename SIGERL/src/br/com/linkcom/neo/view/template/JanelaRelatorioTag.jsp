<%@ taglib prefix="n" uri="neo"%>
<n:bean name="filtro">
	<table width="100%" align="center" class="window inputWindow"  cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<n:doBody />
				<div class="actionBar">
					<n:submit action="${TJanelaRelatorio.submitAction}" validate="true" parameters="${TJanelaRelatorio.parameters}" confirmationScript="${TJanelaRelatorio.submitConfirmationScript}">${TJanelaRelatorio.submitLabel}</n:submit>
				</div>
			</td>
		</tr>
	</table>
</n:bean>