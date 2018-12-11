<%@page import="br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Recebimento</h3>
	<n:form>
		<jsp:include page="../../inputPage.jsp"></jsp:include>
		<br>
		<div class="panel panel-default">
			<div class="table-responsive">
				<n:dataGrid itens="${filtro.recebimentoRetiraLoja.listaRecebimentoRetiraLojaProduto}" id="tabelaId" itemType="<%=RecebimentoRetiraLojaProduto.class %>" var="recebimentoProduto" styleClass="table table-striped table-bordered">
					<t:property name="produto.codigo" label="Código"/>
					<t:property name="produto.descricao" label="Produto"/>
					<t:property name="numeroPedido" label="Pedido"/>
					<t:property name="tipoEstoque.descricao" label="Situação"/>
			 	</n:dataGrid>
			</div>
		</div>
	</n:form>
</div>
