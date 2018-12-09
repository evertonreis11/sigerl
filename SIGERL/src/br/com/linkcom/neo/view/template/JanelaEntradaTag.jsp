<%@ taglib prefix="n" uri="neo"%>
<table width="100%" align="center" class="janelaEntrada" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<n:tabPanel id="janelaEntrada" onSelectTab="listenerTab">
				<n:doBody />
			</n:tabPanel>
		</td>
	</tr>
</table>
<script>
	function listenerTab(id,idx){
		var btn = $("#btn_editar");
		if(btn.size() > 0){
			var url = btn.attr("href");
			if(url.indexOf('TABPANEL_'+id) > 0) {
				var split = url.split("?");
				var action = split[0];
				var parameters = split[1].split("&");
				var i = 0;
				var total = parameters.length;
				for(i=0; i<total;i++){
					if(parameters[i].indexOf('TABPANEL_') >= 0){
						parameters[i] = "TABPANEL_"+ id + "="+idx;
					}
				}
				var nurl = "?";
				for(i=0; i<total;i++){
					nurl += parameters[i];
					if(i != (total-1)){
						nurl += "&";
					}
				}
				btn.attr("href",nurl);
			} else {
				btn.attr("href",url+"&TABPANEL_"+ id + "="+idx);
			}
		}
	}
</script>