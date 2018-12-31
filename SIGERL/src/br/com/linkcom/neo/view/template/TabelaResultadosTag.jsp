<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<t:acao></t:acao><%-- Resetando a tag ação --%>
<n:dataGrid itens="${TtabelaResultados.itens}" var="${TtabelaResultados.name}" width="100%" cellspacing="0" 
	rowondblclick="javascript:$dg.editarRegistro(this)"
	rowonclick="javascript:$dg.coloreLinha('tabelaResultados',this)" 
	rowonmouseover="javascript:$dg.mouseonOverTabela('tabelaResultados',this)" 
	rowonmouseout="javascript:$dg.mouseonOutTabela('tabelaResultados',this)" 
	id="tabelaResultados" varIndex="index" styleClass="table table-striped table-bordered">
	
	<n:bean name="${TtabelaResultados.name}" valueType="${TtabelaResultados.valueType}">
		<c:if test="${TtabelaResultados.showExcluirLink}">
			<n:column>
				<n:header style="width: 1%;"><input type="checkbox" class="checkBoxClass" name="selectAll" id="selectAll" onclick="javascript:$dg.changeCheckState();"></n:header>
				<n:body><input class="checkBoxClass" type="checkbox" name="selecteditens" value="${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}"></n:body>
			</n:column>
		</c:if>
		<t:propertyConfig mode="output" renderAs="column">
			<n:doBody />
		</t:propertyConfig>
		<c:if test="${(!empty acoes) || (TtabelaResultados.showEditarLink) || (TtabelaResultados.showExcluirLink) || (TtabelaResultados.showConsultarLink) || (param.IMPRIMIRSELECIONAR)}">
			<n:column header="Ação" style="white-space: nowrap; padding-right: 3px;">
				<c:if test="${!pos_acao_ladoDireito}">
					${acoes}
				</c:if>
				<script language="javascript">
					imprimirSelecionar(new Array(${n:hierarchy(TtabelaResultados.valueType)}), "${n:escape(n:valueToString(n:reevaluate(TtabelaResultados.name, pageContext)))}", "${n:escape(n:descriptionToString(n:reevaluate(TtabelaResultados.name, pageContext)))}");
				</script>
				<c:if test="${TtabelaResultados.showConsultarLink}">
					<n:link action="consultar" description="Visualizar" parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" class="activation">
						<img src="${ctx}/resource/img/consultar_icon.gif" border="0" alt="Visualizar"/>
					</n:link>
				</c:if>
				<c:if test="${TtabelaResultados.showEditarLink}">
					<n:link action="editar" description="Editar" parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}">
						<img src="${ctx}/resource/img/btnEditar.png" border="0" alt="Editar"/>
					</n:link>
				</c:if>
				<c:if test="${pos_acao_ladoDireito}">
					${acoes}
				</c:if>
			</n:column>
		</c:if>
	</n:bean>
</n:dataGrid>
<table width="100%">
	<tr>
		<td class="paginacao" align="left">
			<n:pagging currentPage="${currentPage}" totalNumberOfPages="${numberOfPages}" selectedClass="pageSelected" unselectedClass="pageUnselected" />
		</td>
		<c:if test="${filtro.numberOfResults != 0}">
			<td align="right"><b>${filtro.currentPage * filtro.pageSize + 1}</b> - <b>${n:gettotalpage(filtro)}</b> de <b>${filtro.numberOfResults}</b></td>
		</c:if>
		<c:if test="${filtro.numberOfResults == 0}">
			<td align="right"><b>Não existem registros!</b></td>		
		</c:if>
	</tr>
</table>
<script>
	<c:if test="${TtabelaResultados.showExcluirLink}">
		function excluirItensSelecionados(){
			if($dg.validateSelectedValues()){
				document.location = '?ACAO=excluir&itenstodelete='+$dg.getSelectedValues();
			}
		}
	</c:if>
	<c:if test="${!TtabelaResultados.showExcluirLink}">
		$("#btn_excluir").hide();
	</c:if>
</script>