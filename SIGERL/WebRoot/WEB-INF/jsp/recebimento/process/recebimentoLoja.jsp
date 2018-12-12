<%@page import="br.com.ricardoeletro.sigerl.recebimento.process.filtro.RecebimentoLojaFiltro"%>
<%@page import="br.com.linkcom.wms.geral.bean.RecebimentoRetiraLojaProduto"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Recebimento</h3>
	<n:form method="post" action="${Ttela.formAction}" validateFunction="validarFormulario">
		<n:bean name="filtro" valueType="<%=RecebimentoLojaFiltro.class%>">
			<jsp:include page="../../inputPage.jsp"></jsp:include>
			<br>
			<c:if test="${empty filtro.recebimentoRetiraLoja}">
				<div class="row">
					<div class="form-group col-md-12>
						<t:property name="recebimentoRetiraLoja" type="hidden" mode="input"/>
						<t:property name="avaria" type="hidden" mode="input"/>
							
						<div class="col-md-3 col-md-offset-9">
							<button class="btn btn-danger btn-md" type="submit" id="buttonCancelar">Cancelar</button>
							<button class="btn btn-warning btn-md" type="button" id="buttonAvaria" onclick="setAvaria();">Avaria</button>
							<button class="btn btn-success btn-md" type="submit" id="buttonFinalizar">Finalizar</button>
						</div>
					</div>
				</div>
			</c:if>
			<br>
			<div class="panel panel-default">
				<div class="table-responsive">
					<n:dataGrid itens="recebimentoRetiraLoja.listaRecebimentoRetiraLojaProduto" id="tabelaId" itemType="<%=RecebimentoRetiraLojaProduto.class %>" var="recebimentoProduto" styleClass="table table-striped table-bordered">
						<t:property name="produto.codigo" label="Código"/>
						<t:property name="produto.descricao" label="Produto"/>
						<t:property name="notaFiscalSaida.pedidovenda.numero" label="Pedido"/>
						<t:property name="tipoEstoque.descricao" label="Situação"/>
				 	</n:dataGrid>
				</div>
			</div>
		</n:bean>
	</n:form>
</div>

<script type="text/javascript">
	function executarAcao(){
		form.ACAO.value ='consultar';
		form.validate = 'false'; 
		submitForm();
	}
	
	function setAvaria(){
		if ($("input[name='avaria']").val() == "true"){
			$("input[name='avaria']").val("false");
			$("#buttonAvaria").css("color", "white");
		}else{
			$("input[name='avaria']").val("true");
			$("#buttonAvaria").css("color", "black");
		}
	}

</script>
