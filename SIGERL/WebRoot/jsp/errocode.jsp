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
	<script language="JavaScript" src="${ctx}/js/jquery.js"></script>	
	<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/bootstrap-confirmation.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/wms.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.serialize.js"></script>
		
		
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
	
	<link href="${ctx}/css/${tema_wms}/bootstrap.min.css" rel="stylesheet" media="screen"/>
	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/menu.css" type="text/css">
	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/base.css" type="text/css">
</head>
<body>

	<div align="center">
		<div id="corpo">
			<nav class="navbar navbar-default">
				  <div class="container-fluid">
				    <div class="navbar-header">
				      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				        <span class="sr-only">Navegar</span>
				      </button>
				      <a class="navbar-brand" href="${ctx}/adm/index">Retira Loja</a>
				    </div>
				  </div>
			</nav>
		<br><br><br>
		<br><br><br>
		<table align="center" width="500"><tr><td>
		<div class="dialog">
			<div class="flash_alert" style="height: 50px; vertical-align: middle; border:1px solid #CCCCCC; font-size:12px;margin-bottom:12px;padding:10px 5px 5px 30px;text-align:left;">
				<c:choose>
					<c:when test="<%=pageContext.getErrorData().getStatusCode() == 404%>">
						<h4>Página não encontrada.</h4>
					</c:when>
					<c:when test="<%=pageContext.getErrorData().getStatusCode() == 403%>">
						<h4>Acesso negado.</h4>
					</c:when>
				</c:choose>
			</div>
			<a href="javascript:history.back();" style="color: #f91700; font-weight: bold;">Retornar para o sistema</a><br>
		</div>
		</td></tr></table>
	</div>
</body>

</html>
