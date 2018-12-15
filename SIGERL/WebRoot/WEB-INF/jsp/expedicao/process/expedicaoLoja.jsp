<%@page import="br.com.linkcom.wms.geral.bean.ExpedicaoRetiraLojaProduto"%>
<%@page import="br.com.ricardoeletro.sigerl.expedicao.process.filtro.ExpedicaoLojaFiltro"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Confirmação de Entrega</h3>
	<n:form method="post" action="${Ttela.formAction}" validateFunction="validarFormulario">
		<n:bean name="filtro" valueType="<%=ExpedicaoLojaFiltro.class%>">
			<jsp:include page="../../inputPage.jsp"></jsp:include>
			<br />
			
			<t:property name="expedicaoImprimirTermo" type="hidden" mode="input"/>
			
			<c:if test="${not empty filtro.expedicaoRetiraLoja}">
				<div class="row">
				
					<t:property name="expedicaoRetiraLoja.cdExpedicaoRetiraLoja" type="hidden" mode="input"/>
					<t:property name="expedicaoRetiraLoja.notaFiscalSaida.cdnotafiscalsaida" type="hidden" mode="input"/>
					<t:property name="codigoBarras" type="hidden" mode="input"/>
					
					<div class="form-group col-md-12">
						<div class="col-md-6 col-md-offset-3">
							<div class="panel panel-default corpo-pagina" style="background-color: rgba(184, 234, 206, 0.5);">
								<label style="padding-left: 5%;color: black;">
									Cliente:<t:property name="expedicaoRetiraLoja.notaFiscalSaida.cliente.nome" type="hidden" mode="output"/>
								</label>
									<br />
								<label style="padding-left: 5%;color: black;">
									Nota Fiscal:<t:property name="expedicaoRetiraLoja.notaFiscalSaida.numero" type="hidden" mode="output"/>
									- <t:property name="expedicaoRetiraLoja.notaFiscalSaida.serie" type="hidden" mode="output"/>
								</label>
							</div>
						</div>	
					</div>
				</div>
			</c:if>
			
			<br />
			
			 <div class="panel panel-default">
				<div class="table-responsive">
					<n:dataGrid itens="expedicaoRetiraLoja.listaExpedicaoRetiraLojaProduto" id="tabelaId" itemType="<%=ExpedicaoRetiraLojaProduto.class %>" var="expedicaoProduto" styleClass="table table-striped table-bordered">
						<n:column header="Código">
							<c:choose>
								<c:when test="${expedicaoProduto.conferenciaExpedicaoRetiraLojaStatus.cdConferenciaExpedicaoRetiraLojaStatus eq 1}">
									<t:property name="codigoBarrasConferencia" mode="input" class="form-control input-lg" 
											data-toggle="tooltip" title="Informe o EAN do produto para conferencia"  onchange="conferirProduto(this.value);"/>
								</c:when>
								<c:otherwise>
									<t:property name="produto.codigo" mode="output" label="Código"/>
								</c:otherwise>
							</c:choose>
						</n:column>
					    
						<t:property name="produto.descricao" label="Descrição" mode="output"/>
						
						<n:column header="Situação">
						 	<t:property name="conferenciaExpedicaoRetiraLojaStatus.cdConferenciaExpedicaoRetiraLojaStatus" type="hidden" mode="input"/>
							
							<c:if test="${expedicaoProduto.conferenciaExpedicaoRetiraLojaStatus.cdConferenciaExpedicaoRetiraLojaStatus eq 1}">
								<button type="button" class="btn btn-warning" data-toggle="tooltip" title="Aguardando Conferência"> 
					     			<span class="glyphicon glyphicon-glyphicon-exclamation-sign" aria-hidden="true" />
					    		</button>
							</c:if>
						 	
						  	<c:if test="${expedicaoProduto.conferenciaExpedicaoRetiraLojaStatus.cdConferenciaExpedicaoRetiraLojaStatus eq 2}">
							 	<button type="button" class="btn btn-success" data-toggle="tooltip" title="Conferido"> 
				     				<span class="glyphicon glyphicon-glyphicon-ok-sign" aria-hidden="true"/>
				    			</button>
							</c:if>
						</n:column> 
				 	</n:dataGrid>
				</div>
			</div>
			<br />
			<c:if test="${not empty filtro.expedicaoRetiraLoja}">
				<div class="row">
					<div class="form-group col-md-12">
						<div class="col-md-2 col-md-offset-10">
							<button class="btn btn-success btn-lg" type="submit" id="buttonConfirmar" data-toggle="confirmation" onclick="finalizarExpedicao()">Confirmar Entrega</button>
						</div>
					</div>
				</div>
			</c:if>
		</n:bean>
	</n:form>
</div>


<script type="text/javascript">
	$(document).ready(function (){
		if($("input[name='expedicaoImprimirTermo']").val() != '<null>' 
			&& $("input[name='expedicaoImprimirTermo']").val() != ''){

			$("input[name='expedicaoImprimirTermo']").val('<null>');
			
				
		}
	});
	
	function executarAcao(){
		form.ACAO.value ='consultar';
		form.validate = 'false'; 
		submitForm();
	}

	function conferirProduto(codigoEan){
		$("input[name=codigoBarras]").val(codigoEan);

		form.ACAO.value ='conferirProduto';
		form.validate = 'false'; 
		submitForm();
	}

	function clearForm(){
		form.ACAO.value ='limpar';
		form.validate = 'false'; 
		submitForm(); 
	}

	function finalizarExpedicao(){

		form.ACAO.value ='finalizar';
		form.validate = 'false'; 
		submitForm(); 

	}
	
</script>