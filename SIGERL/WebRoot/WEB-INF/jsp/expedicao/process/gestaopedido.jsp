<%@page import="br.com.linkcom.wms.geral.bean.vo.GestaoPedidoVO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>
<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Confirmação de Entrega</h3>
	<n:form method="post" action="${Ttela.formAction}" validateFunction="validarFormulario">
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
		</t:janelaFiltro>
 	</n:form>
 	
 	<br />
	
	<div class="row">
		<div class="col-md-2 col-md-offset-10">
			<button class="btn btn-primary btn-md" type="button" id="buttonLimpar" onclick="limparFiltros();">Limpar</button>
			<button class="btn btn-success btn-md" type="button" id="buttonFiltrar" onclick="doFilter();resetPage();">Filtrar</button>
		</div>
	</div>	
	
	<br />	
		
	<div class="panel panel-default">
		<div class="table-responsive">
			<n:dataGrid name="registros" id="registrosId" itens="${lista}" itemType="<%=GestaoPedidoVO.class%>" var="registro">
				
				<t:property name="numeroNota" label="Num. Nota" mode="output"/>
				<t:property name="numeroPedido" label="Num. Pedido" mode="output"/>
				<t:property name="dataPedido" label="Data Pedido" mode="output"/>
				<t:property name="dataChegada" label="Dt. Chegada Loja" mode="output"/>
				<t:property name="situacao" label="Situação" mode="output"/>
				<t:property name="cliente"  label="Cliente" mode="output"/>
				
				<n:column header="Detalhes">
					<%-- <t:acao>
					
					</t:acao> --%>
				</n:column>
			</n:dataGrid> 
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
</script>