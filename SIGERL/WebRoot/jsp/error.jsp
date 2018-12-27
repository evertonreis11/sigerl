<%@page import="br.com.linkcom.wms.util.WmsUtil"%>
<%@page import="br.com.linkcom.wms.util.WmsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
		
		<script>
	
			function mostraErro(){
				if($("#mostra").is(":checked")) {
					$("#scroll").fadeIn();
				}else{
					$("#scroll").fadeOut();
				}
			}
	
		</script>
	</head>

<body>
	<div align="center">
		<div id="corpo">
			<nav class="navbar navbar-default">
				  <div class="container-fluid">
				    <div class="navbar-header">
				      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				        <span class="sr-only">Navegar</span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				      </button>
				      <a class="navbar-brand" href="${ctx}/adm/index">Retira Loja</a>
				    </div>
				    <div class="menu"><w:menuBootstrap menupath="/WEB-INF/menu/menu.xml"/>
				  </div>
			</nav>
			<div align="center">
				<br><br>
				<br><br>
				<br><br>
				<div class="dialog">
				
					<div class="flash_alert" style="height: 50px; vertical-align: middle; border:1px solid #CCCCCC; font-size:12px;margin-bottom:12px;padding:5px 5px 5px 30px;text-align:left;">
						<c:choose>
							<c:when test="<%=pageContext.getException().getMessage() == null%>">
								<h4>Erro no processamento da página.</h4>
							</c:when>
							<c:when test="<%=pageContext.getException().getMessage() != null && pageContext.getException().getMessage().contains(\"java.sql.SQLException\")%>">
								<h4>Não foi possível estabelecer conexão com o Banco de Dados.</h4>
							</c:when>
							<c:when test="<%=pageContext.getException().getMessage() != null && pageContext.getException() instanceof WmsException %>">
								<h4><%=pageContext.getException().getMessage() %></h4>
							</c:when>
							<c:otherwise>
								<h4>Erro no processamento da página.</h4>
							</c:otherwise>
						</c:choose>
					</div>
					<a href="javascript:history.back()" style="color: #f91700; font-weight: bold;">Retornar para o sistema</a><br>
					<div class="checkbox">
					  	<label><input type="checkbox" onclick="mostraErro()" id="mostra" class="checkbox-inline">Mostrar erro</label>
					</div>
					
					<div id="scroll" style="display:none;">
						<%=pageContext.getException().getMessage()%>
					</div>
				</div>
				</div>
			</div>
		</div>
	</body>
	
</html>