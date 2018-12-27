<%@page import="br.com.linkcom.wms.geral.bean.ProblemaPedidoLoja"%>
<%@page import="br.com.linkcom.wms.geral.bean.PedidoPontoControle"%>
<%@page import="br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Gestão de pedidos</h3>
	<n:form method="post" action="${Ttela.formAction}" validateFunction="validarFormulario">
		<n:input name="resetCurrentPage" type="hidden" write="false"/>
		<n:input name="notasInfoProblema" type="hidden" write="false"/>
		
		<t:janelaFiltro>
			<div class="row">
				<div class="form-group col-md-12">
					<div class="col-md-3">
						<t:property name="loja" itens="${lista_deposito}" class="form-control" mode="input" showLabel="true" readonly="readonly" tabindex="-1" aria-disabled="true"/>
					</div>
					<div class="col-md-3">
						<t:property name="numeroPedido" class="form-control input-md" mode="input" showLabel="true"/>
					</div>
					<div class="col-md-3">
						<t:property name="numeroNota" class="form-control input-md" mode="input" showLabel="true"/>
					</div>
					<div class="col-md-3">
					 	<div class="row">
			                <label class="col-xs-12">Período de Emissão</label>
			            </div>
			            <div class="row">
			                <div class="col-md-5" style="padding-right: 1%;">
			                    <t:property name="dtChegadaInicial" renderAs="single" showLabel="false" class="form-control input-md" mode="input"/> 
			                </div>
			                <div class="col-md-1" style="padding: 2% 0;">
			                    <label>até</label> 
			                </div>
			                <div class="col-md-5" style="padding-left: 1%;">
			                    <t:property name="dtChegadaFinal" renderAs="single" showLabel="false" class="form-control input-md" mode="input"/>
			                </div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="form-group col-md-12">
					<div class="col-md-4">
						<t:property name="codigoProduto" class="form-control input-md" mode="input" showLabel="true"/>
					</div>
					<div class="col-md-6">
						<t:property name="nomeCliente" class="form-control input-md" mode="input" showLabel="true"/>
					</div>
				</div>
			</div>
			
		   <div class="modal fade" id="infoModal" role="dialog">
		     <div class="modal-dialog">
		       <div class="modal-content">
		        <div class="modal-header">
		          	<button type="button" class="close" data-dismiss="modal">&times;</button>
		          	<h4 class="modal-title">Informar Problema</h4>
		        </div>
		        <div class="modal-body">
		        	<c:forEach items="${filtro.problemasPedido}" var="problemaPedido" >
		        		<div class="radio">
						  <label><input type="radio" name="cdProblemaPedidoLoja" value="${problemaPedido.cdProblemaPedidoLoja}">${problemaPedido.descricao}</label>
						</div>
		        	</c:forEach>
		        </div>
		        <div class="modal-footer">
			        <button type="button" class="btn btn-success" data-dismiss="modal" onclick="executarInformacaoProblema();">Finalizar</button>
			    </div>
		      </div>
		    </div>
		  </div>
		  
		</t:janelaFiltro>
 	</n:form>
 	
 	<br />

	
	<div class="panel panel-default" style="border: 0px;" >
		<div class="panel-body">
           <div class="col-md-3 panel corpo-pagina" style="background-color: #f5f5f5;" align="center">
	           <a href="javascript:informarProblema();" style="font-size: large ; color: #f91700">
					<span class="glyphicon glyphicon-remove-circle" style="padding-right: 2%"></span>Informar um problema
				</a>
           </div>
					
			<div class="col-md-2 col-md-offset-7">
				<button class="btn btn-primary btn-md" type="button" id="buttonLimpar" onclick="limparFiltros();">Limpar</button>
				<button class="btn btn-success btn-md" type="button" id="buttonFiltrar" onclick="doFilter();resetPage();">Filtrar</button>
			</div>		
           </div>
	</div>
	
		
	<div class="panel panel-default" style="border: 0px !important;">
		<div class="panel-body">
			<div class="table-responsive" >
				<n:dataGrid name="registros" id="tabelaResultados" itens="${lista}" itemType="<%=GestaoPedidoVO.class%>" var="registro" styleClass="table table-striped table-bordered">
					<n:column>
						<n:header>
							<input type="checkbox" class="checkBoxClass" name="selectAll" id="selectAll" onclick="javascript:$dg.changeCheckState();"/>
						</n:header>
						<n:body>
							<input class="checkBoxClass" type="checkbox" name="selecteditens" value="${registro.numeroNota}">
						</n:body>
					</n:column>
					<t:property name="numeroNota" label="Num. Nota" mode="output"/>
					<t:property name="numeroPedido" label="Num. Pedido" mode="output"/>
					<t:property name="dataPedido" label="Data Pedido" mode="output"/>
					<t:property name="dataChegada" label="Dt. Chegada Loja" mode="output"/>
					<t:property name="situacao" label="Situação" mode="output"/>
					<t:property name="cliente"  label="Cliente" mode="output"/>
					
					<n:column header="Detalhes" style="text-align: -webkit-center;" >
						<a href="javascript:openDialogPedidos(${registro.numeroPedido})" data-toggle="tooltip" title="Exibir log do pedido ${registro.numeroPedido}" style="font-size: x-large;">
							<span class="glyphicon glyphicon-modal-window" style="color:#545454"/>
						</a>
					</n:column>
				</n:dataGrid> 
			</div>
		</div>
	</div>
</div>

  <div class="modal fade" id="logModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          	<button type="button" class="close" data-dismiss="modal">&times;</button>
          	<h4 class="modal-title">Detalhes do Pedido</h4>
        </div>
        <div class="modal-body">
         	<n:dataGrid itens="${listaLog}" var="log" itemType="<%=PedidoPontoControle.class%>" id="tabelaLog" styleClass="table table-striped table-bordered">
				<n:column header="Dt. Inclusão">
					<t:property name="numeroNota" mode="output"/>
				</n:column>
				<n:column header="Observação">
					<t:property name="statusTransito" mode="output"/>
				</n:column>
			</n:dataGrid>
        </div>
      </div>
    </div>
  </div>

<script type="text/javascript">
	function limparFiltros(){
		$('#tabelaResultados tbody').empty();
		$('#messageBlock').empty();
		$('#messageBlock').hide();
		$('#btn_confirmar').hide();

		form.ACAO.value ='limpar';
		form.validate = 'false'; 
		submitForm(); 
	}

	function doFilter(){
		form.ACAO.value ='filtrar';
		form.action = '';
		form.validate = 'false';
		submitForm();
	}
	
	function resetPage(){
		form.resetCurrentPage.value = 'true';
	}

	function openDialogPedidos(numeroPedido){

		var row = "";
		
		$w.getJSONSync("${ctx}/expedicao/process/gestaopedido",{ACAO:'getInfoPedido','numeroPedido':numeroPedido},			
			function(json){
				for(var i=0; i< json.listaLog.length; i++){
					row += makeRows(i,json.listaLog[i]);
				}			
			}
		);	
		
		$("#tabelaLog tbody").empty().append(row);
		$("#logModal").modal();
		
	}

	function makeRows(i, log){

		var row = ""; 
		
		row += "<tr class='" + (i % 2 == 0 ? "dataGridBody1" : "dataGridBody2") + "'>";
			row += "<td>" + log.dtInclusao + "</td>";
			row += "<td>" + log.observacao + "</td>";
		row += "</tr>";
	
		return row;
				
	}

	function informarProblema(){
		if($dg.getSelectedValues() != ""){
			$('input[name=notasInfoProblema]').val($dg.getSelectedValues());
			$("#infoModal").modal();
			
		
		}else{
			alert("É obrigatório selecionar ao menos 1 pedido antes de prosseguir com a essa ação");
		}
	}

	function executarInformacaoProblema(){
		form['ACAO'].value ='informarProblemaPedido';
		form.action = '${ctx}/expedicao/crud/Pedidovendaproduto';
		form.validate = 'false';
		submitForm();
	}
</script>