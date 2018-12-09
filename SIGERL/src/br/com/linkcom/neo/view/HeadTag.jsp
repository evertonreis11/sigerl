<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<% request.setAttribute("app", request.getContextPath()); %>
<script language="JavaScript" src="${app}/resource/js/autocomplete/jquery.js"></script>	
<script language="JavaScript" src="${app}/resource/js/jquery.maskedinput.js"></script>
<script language="JavaScript" src="${app}/resource/js/jquery.selectboxes.js"></script>
<script language="JavaScript" src="${app}/resource/js/ajax.js"></script>
<script language="JavaScript" src="${app}/resource/js/input.js"></script>
<script language="JavaScript" src="${app}/resource/js/validate.js"></script>
<script language="JavaScript" src="${app}/resource/js/tabela.js"></script>
<script language="JavaScript" src="${app}/resource/js/calendar.js"></script>

<c:if test="${tag.includeUtilJs}">
	<script language="JavaScript" src="${app}/resource/js/util.js"></script>
</c:if>
<script language="JavaScript" src="${app}/resource/js/dynatable.js"></script>
<script language="JavaScript" src="${app}/js/menuBootstrap.js"></script>

<c:if test="${includeAutocomplete}">
	<script language="JavaScript">
		var contextoAutoComplete = '${app}';
	</script>

	<script language="JavaScript" src="${app}/resource/js/autocomplete/jquery.ajaxQueue.js"></script>	
	<script language="JavaScript" src="${app}/resource/js/autocomplete/jquery.bgiframe.min.js"></script>	
	<script language="JavaScript" src="${app}/resource/js/autocomplete/thickbox-compressed.js"></script>	
	<script language="JavaScript" src="${app}/resource/js/autocomplete/jquery.autocomplete.js"></script>	
	<script language="JavaScript" src="${app}/resource/js/autocomplete/neo.autocomplete.js"></script>	
	
	<link rel="stylesheet" type="text/css" href="${app}/resource/js/autocomplete/thickbox.css" />
	<link rel="stylesheet" type="text/css" href="${app}/resource/js/autocomplete/jquery.autocomplete.css" />
</c:if>

<%-- DEFAULT CSS --%>
<c:if test="${tag.includeDefaultCss}">
<link rel="StyleSheet"        href="${app}/resource/css/default.css" type="text/css">	
</c:if>

<%-- CSS JS DA APLICAÇÃO --%>
<c:if test="${searchCssDir == true}">
	<c:forEach items="${csss}" var="css">
	<link rel="StyleSheet"        href="${app}${css}" type="text/css">	
	</c:forEach>
</c:if>

<c:if test="${searchJsDir == true}">
	<c:forEach items="${jss}" var="js">
	<script language="JavaScript" src="${app}${js}"></script>	
	</c:forEach>
</c:if>

<%-- INICIALIZAÇÃO DO MENU --%>
<script language="JavaScript">
	//menu
	var cmThemeOfficeBase = '${app}/resource/menu/';
	var tb_pathToImage = "${app}/resource/js/autocomplete/loadingAnimation.gif";
</script>

<%-- MENU 
<c:if test="${tag.includeThemeCss}">
	<link rel="StyleSheet"        href="${app}/resource/menu/theme.css" type="text/css">
</c:if>
<script language="JavaScript" src="${app}/resource/menu/theme.js"></script>--%>

<%-- Inicialização da página --%>
<script type="text/javascript">			
	//htmlarea
	try {
		preparaHtmlArea('${app}/resource/htmlarea/');	
	} catch(e){}// se não conseguiu achar o javascript não dar pau
	
	/* function applyMask(){
		$("input[mask=cep]").mask("99999-999");
		$("input[mask=cpf]").mask("999.999.999-99");
		$("input[mask=cnpj]").mask("99.999.999/9999-99");
		$("input[mask=date]").mask("99/99/9999",{alertNotCompleted:"Formato da data inválido."});
		$("input[mask=time]").mask("99:99",{alertNotCompleted:"Formato da hora inválido."});
		$("input[mask=timestamp]").mask("99/99/9999 99:99",{alertNotCompleted:"Formato de data e hora inválido.",completed:function(){
			if(!$n.validaTimestamp(this.val())) {
				this.val("");
				this.focus();
			}
		}});

		$("input[mask=mesano]").mask("99/9999",{completed:function(){
			if(!$n.validaDataMesAno(this.val())) {
				this.val("");
				this.focus();
			}
		}});
		$("input[mask=telefone]").mask("(99)9999-9999");
		$("input[mask=inscricaoestadual]").mask("999999999999-9");
		$("input[mask=placa]").mask("aaa-9999");
		$("input[mask=percentual]").mask("99",{alertNotCompleted:"Formato de porcentagem inválido.",completed:function(){
			alert($n.toFloat(this.val()));
			if($n.toFloat(this.val()) > 100){
				alert("Valor não pode ser maior que 100%.");
				this.val("");
				this.focus();
			}else if($n.toFloat(this.val()) > -100){
				alert("Valor não pode ser menor que -100%.");
				this.val("");
				this.focus();
			}
		}});
	} */
	
	$(document).ready(function(){
		//applyMask();
		<c:if test="${includeAutocomplete}">
		applyAutocomplete();
		</c:if>
		$("input:disabled").css("color","#000000");
	});
</script>