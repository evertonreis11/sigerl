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
			<br />
			<c:if test="${not empty filtro.recebimentoRetiraLoja}">
				<div class="row">
					<div class="form-group col-md-12">
						<t:property name="recebimentoRetiraLoja.cdRecebimentoRetiraLoja" type="hidden" mode="input"/>
						<t:property name="avaria" type="hidden" mode="input"/>
							
						<div class="col-md-3 col-md-offset-9">
							<button class="btn btn-primary btn-md" type="submit" id="buttonLimpar" onclick="clearForm();">Limpar</button>
							<button class="btn btn-warning btn-md" type="button" id="buttonAvaria" onclick="setAvaria();">Avaria</button>
							<button class="btn btn-success btn-md" type="button" id="buttonFinalizar" onclick="finalizarRecebimento();" data-toggle="confirmation">Finalizar</button>
						</div>
					</div>
				</div>
			</c:if>
			<br />
			<div class="panel panel-default">
				<div class="table-responsive">
					<n:dataGrid itens="recebimentoRetiraLoja.listaRecebimentoRetiraLojaProduto" id="tabelaId" itemType="<%=RecebimentoRetiraLojaProduto.class %>" var="recebimentoProduto" styleClass="table table-striped table-bordered">
						<t:property name="produto.codigo" label="Código" mode="output"/>
						<t:property name="produto.descricao" label="Produto" mode="output"/>
						<t:property name="notaFiscalSaida.numeropedido" label="Pedido" mode="output"/>
						<n:column header="Situação">
						 	<t:property name="tipoEstoque.cdTipoEstoque" type="hidden" mode="input"/>
						 	
						  	<c:if test="${recebimentoProduto.tipoEstoque.cdTipoEstoque eq 1}">
							 	<button type="button" class="btn btn-success" data-toggle="tooltip" title="Conferido"> 
				     				<span class="glyphicon glyphicon-glyphicon-ok-sign" aria-hidden="true"/>
				    			</button>
							</c:if>
							<c:if test="${recebimentoProduto.tipoEstoque.cdTipoEstoque eq 2}">
								<button type="button" class="btn btn-warning" data-toggle="tooltip"  title="Avariado"> 
					     			<span class="glyphicon glyphicon-glyphicon-exclamation-sign" aria-hidden="true" />
					    		</button>
							</c:if>
							<c:if test="${recebimentoProduto.tipoEstoque.cdTipoEstoque eq 3}">
								<button type="button" class="btn btn-danger" data-toggle="tooltip" title="Extraviado"> 
						     		 <span class="glyphicon glyphicon-glyphicon-remove-sign" aria-hidden="true"/>
						    	</button>
	                        </c:if>
						</n:column>
				 	</n:dataGrid>
				</div>
			</div>
		</n:bean>
	</n:form>
</div>

<script type="text/javascript">
	$(document).ready(function(){
		$("#buttonFinalizar").confirmation({
			  rootSelector: '[data-toggle=confirmation]',
			  onConfirm: function(value) {
				$("#buttonAvaria").removeAttr("disabled");
				$("#valorInicial").removeAttr("readOnly");
				submitFinalizar();
			  },
			  onCancel: function() {
			    $("#buttonAvaria").removeAttr("disabled");
				$("#valorInicial").removeAttr("readOnly");
			  },
			  trigger: 'manual',
			  btnOkLabel:'Sim',
			  btnOkClass:'btn-success',
			  btnCancelLabel:'Não',
			  btnCancelClass:'btn-danger',
			  title:'Finalizar Recebimento:',
			  content: 'Alguns produtos ainda não foram confirmados, ao finalizar estes produtos serão considerados extraviados, deseja continuar?'
			});
	});

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
	
	function finalizarRecebimento(){
		var quantExtraviados = 0;
		
		$("input[name*='cdTipoEstoque']").each(function(){
			if ($(this).val() == 3){
				quantExtraviados += 1;
				return false;
			}
		});
		
		if (quantExtraviados > 0){
			$("#buttonFinalizar").confirmation('show');
			$("#buttonAvaria").attr("disabled","disabled");
			$("#valorInicial").attr("readOnly","readOnly");
		}else{
			submitFinalizar();
		}
	}

	function submitFinalizar(){
		form.ACAO.value ='finalizar';
		form.validate = 'false'; 
		submitForm(); 
	};

	function clearForm(){
		form.ACAO.value ='limpar';
		form.validate = 'false'; 
		submitForm(); 
	}

</script>

