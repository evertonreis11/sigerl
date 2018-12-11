
<%@page import="br.com.linkcom.wms.util.WmsUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<n:head searchJsDir="false" searchCssDir="false" includeDefaultCss="false" includeThemeCss="false" includeAutocomplete="true"/> 
		
		<script language="JavaScript" src="${ctx}/js/jquery.js"></script>	
		<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
		<script  src="${ctx}/js/jquery-ui.js"></script>
		<script language="JavaScript" src="${ctx}/js/wz_tooltip.js"></script>
		
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
		
		<link href="${ctx}/css/${tema_wms}/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<link rel="stylesheet"      href="${ctx}/css/${tema_wms}/jquery-ui.css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/menu.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/base.css" type="text/css">
		
		 <%-- <n:head searchJsDir="false" searchCssDir="false" includeDefaultCss="false" includeThemeCss="false" includeAutocomplete="true"/> --%>
		<%--<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app-ie.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/theme.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/default.css" type="text/css">
		<link rel="stylesheet" 		  href="${ctx}/css/jquery-ui-1.7.3.custom.css" type="text/css"/>

		<title><fmt:message key="aplicacao.titulo"/> - Módulo <%=WmsUtil.getNomeModulo()%></title>
		<script type="text/javascript" src="${ctx}/js/tabela.js"></script>
		<script type="text/javascript" src="${ctx}/js/wms.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.serialize.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-ui-1.7.3.custom.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jqBarGraph.js"></script> 

		--%>
		<script type="text/javascript">
			$(document).ready(function() {
			    $('.navbar a.dropdown-toggle').on('click', function(e) {
			        var $el = $(this);
			        var $parent = $(this).offsetParent(".dropdown-menu");
			        $(this).parent("li").toggleClass('open');
			
			        if(!$parent.parent().hasClass('nav')) {
			            $el.next().css({"top": $el[0].offsetTop, "left": $parent.outerWidth() - 4});
			        }
			
			        $('.nav li.open').not($(this).parents("li")).removeClass("open");
			
			        return false;
			    });
			});
		</script>

		
	</head>
	<body>
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
		<div class="wrapper" role="main" id="principal">
			<div class="container">
				<div class="row">
					<div id="conteudo" class="col-md-12"> 
						<n:hasMessages>
							<w:messages/>
						</n:hasMessages>
						<div id="dynamic_messages"></div>
						<jsp:include page="${bodyPage}" />
					</div>
				</div>
			</div>
		</div>
		
		<footer style="background-color: rgb(243,247,129); color: #f91700;" id="footer">
			<div class="container">
				<div class="row">
					<div id="rodape" class="col-md-9 col-md-offset-3"> 
							Você está logado como <b><%=WmsUtil.getUsuarioLogado().getNomeForBase()%></b> no depósito <b><%=WmsUtil.getDeposito().getNomeForBase()%></b>
					</div>
				</div>
			</div>
		</footer>
		
		
		
		
		<%-- <div align="center">
			<div id="corpo">
				<div class="cabecalho">
					<div class="logo"><a href="${ctx}/adm/index"><img src="${ctx}/imagens/${tema_wms}/sys/logo_wave_teste.gif"></a></div>
					<div id="cabecalho" class="right" align="right">
						
					</div>
				</div>
				<div class="conteudo">
					<n:hasMessages>
						<table align="center" width="100%"><tr><td align="center"><br><n:messages/><br></td></tr></table>
					</n:hasMessages>
					<div id="dynamic_messages"></div>
					<jsp:include page="${bodyPage}" />
				<footer> <!-- Aqui e a area do footer -->
					<div class="container">
						<div class="row">
							<div id="linksImportantes" class="col-md-3"> teste1</div> <!-- Aqui e a area dos links importantes -->
							<div id="redesSociais" class="col-md-3"> teste 2</div> <!-- Aqui e a area das redes sociais -->
							<div id="logoFooter" class="col-md-offset-3 col-md-3"> teste 3</div> <!-- Aqui e a area da logo do rodape -->
						</div>
					</div>
				</footer>
				</div>
				
			</div>
		</div> --%>
	</body>
</html>
<script>
	/* var plus = {help : {}};
	
	$.getScript("http://suporte.linkcom.com.br/image.php?plus=1", function(){
  		if (typeof plus_ajuda_status != 'undefined' && plus_ajuda_status){
  			document.getElementById("suporteImage").src = "${ctx}/imagens/icone/ico_chat_on.png";
  		}else{
  			document.getElementById("suporteImage").src = "${ctx}/imagens/icone/ico_chat_off.png";
  		}
	}); */
</script>
