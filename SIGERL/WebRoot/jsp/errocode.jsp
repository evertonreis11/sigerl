<%@page import="br.com.linkcom.wms.util.WmsUtil"%>
<%@page import="java.io.File"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>


<%
	String basePath = getServletContext().getRealPath(File.separator+"css"+File.separator);
	String clienteFromUrl = WmsUtil.getClienteFromUrl((HttpServletRequest) request);
	File file = new File(basePath + File.separator + clienteFromUrl);
	if (file.exists())
		request.setAttribute("tema_wms", clienteFromUrl);
	else
		request.setAttribute("tema_wms", "default");
%>

<head>
	<title>Erro</title>
	<n:head searchJsDir="false" searchCssDir="false" includeDefaultCss="false" includeThemeCss="false" includeAutocomplete="false" includeUtilJs="false"/>
	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app.css" type="text/css">
	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app-ie.css" type="text/css">
	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/theme.css" type="text/css">
	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/default.css" type="text/css">

	<script language="JavaScript" src="${ctx}/resource/js/autocomplete/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/tabela.js"></script>
	<script type="text/javascript" src="${ctx}/js/wms.js"></script>
</head>
<body>

	<div align="center">
		<div id="corpo">
		<div class="cabecalho">
			<div class="logo"><img src="${ctx}/imagens/${tema_wms}/sys/logo_wave_teste.gif"></div>
		</div>
		<br><br><br>
		<br><br><br>
		<table align="center" width="500"><tr><td>
		<div class="dialog">
			<div class="flash_alert" style="height: 20px; vertical-align: middle; border:1px solid #CCCCCC; font-size:12px;margin-bottom:12px;padding:5px 5px 5px 30px;text-align:left;">
				<c:choose>
					<c:when test="<%=pageContext.getErrorData().getStatusCode() == 404%>">
						Página não encontrada.
					</c:when>
					<c:when test="<%=pageContext.getErrorData().getStatusCode() == 403%>">
						Acesso negado.
					</c:when>
				</c:choose>
			</div>
			<a href="javascript:history.back();">Retornar para o sistema</a><br>
		</div>
		</td></tr></table>
	</div>
</body>

</html>
