<%@page import="br.com.linkcom.wms.util.WmsUtil"%>
<%@page import="br.com.linkcom.wms.util.WmsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<n:head searchJsDir="false" searchCssDir="false" includeDefaultCss="false" includeThemeCss="false"/>
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app-ie.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/theme.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/default.css" type="text/css">

		<title>Erro</title>
		<script language="JavaScript" src="${ctx}/resource/js/autocomplete/jquery.js"></script>	
		<script type="text/javascript" src="${ctx}/js/tabela.js"></script>
		<script type="text/javascript" src="${ctx}/js/wms.js"></script>
		
		<script>
	
			function mostraErro(){
				if($("#mostra").attr("checked")) {
					$("#scroll").fadeIn();
				}else{
					$("#scroll").fadeOut();
				}
			}
	
		</script>
	</head>

<body>
	<div id="loadmsg">
		<span class="message">Carregando...</span>
	</div>
	<script language="JavaScript" src="${ctx}/js/wz_tooltip.js"></script>
	<div align="center">
		<div id="corpo">
			<div class="cabecalho">
				<div class="logo"><img src="${ctx}/imagens/${tema_wms}/sys/logo_wave_teste.gif"></div>
				<div class="right" align="right">
					<div class="menu"><n:menu menupath="/WEB-INF/menu/menu.xml"/></div>
					<div class="notificacao"><c:catch>Você está logado como <b><%=WmsUtil.getUsuarioLogado().getNomeForBase()%></b> no depósito <b><%=WmsUtil.getDeposito().getNomeForBase()%></b></c:catch><span class="spacer">&nbsp;</span></div>
				</div>
			</div>
			<div align="center">
				<br><br>
				<br><br>
				<br><br>
				<div class="dialog">
				
					<div class="flash_alert" style="height: 20px; vertical-align: middle; border:1px solid #CCCCCC; font-size:12px;margin-bottom:12px;padding:5px 5px 5px 30px;text-align:left;">
						<c:choose>
							<c:when test="<%=pageContext.getException().getMessage() == null%>">
								Erro no processamento da página.
							</c:when>
							<c:when test="<%=pageContext.getException().getMessage() != null && pageContext.getException().getMessage().contains(\"java.sql.SQLException\")%>">
								Não foi possível estabelecer conexão com o Banco de Dados.
							</c:when>
							<c:when test="<%=pageContext.getException().getMessage() != null && pageContext.getException() instanceof WmsException %>">
								<%=pageContext.getException().getMessage() %>
							</c:when>
							<c:otherwise>
								Erro no processamento da página.
							</c:otherwise>
						</c:choose>
					</div>
					<a href="javascript:history.back()">Retornar para o sistema</a><br>
					<input type="checkbox" onclick="mostraErro()" id="mostra">Mostrar erro</input>
			
					<div id="scroll" style="display:none;">
						<%=pageContext.getException().getMessage()%>
					</div>
				</div>
				</div>
			</div>
		</div>
	</body>
	
</html>