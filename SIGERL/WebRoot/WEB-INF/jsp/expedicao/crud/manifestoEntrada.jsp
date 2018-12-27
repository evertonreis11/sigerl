<%@page import="br.com.linkcom.wms.geral.bean.vo.Auditoria"%>
<%@page import="br.com.linkcom.wms.geral.bean.Manifesto"%>
<%@page import="br.com.linkcom.wms.geral.bean.Manifestonotafiscal"%>
<%@page import="br.com.linkcom.wms.geral.bean.Logintegracaoae"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="w" uri="wms"%>

<style>
	.no-close .ui-dialog-titlebar-close{
		display: none 
	}
	.ui-dialog-titlebar{
    	display:none;
	}
	#msgPorcentagemThread{
	}
	span.labelcolumn {
	  font-weight: bold;
	  font-family: Verdana;
	  font-size: 10px;
	}
</style>

<t:entrada titulo="Manifesto de Carga ${manifesto.cdmanifesto}" submitConfirmationScript="validarCadastro()" showDeleteLink="${isEmElaboracao && !isAutorizado}" showEditLink="${isEmElaboracao && !isAutorizado}">
	
	<jsp:attribute name="linkArea">
		<c:if test="${!isEmElaboracao && consultar}">
			<c:choose>
				<c:when test="${isImpresso}">
					<a id="btn_excluir"  onmouseover="Tip('Cancelar Manifesto')" href="javascript:openDialogAuditoria()">Cancelar</a>
				</c:when>
				<c:otherwise>
					<span>Excluir</span> 
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${isEmElaboracao || (!isImpresso && isAutorizado && consultar)}">
			<c:if test="${isEmElaboracao && consultar}">
				|&nbsp;&nbsp;<a id="btn_imprimir" onmouseover="Tip('Imprimir Manifesto')" href="javascript:imprimirManifesto()">Imprimir Manifesto</a>
			</c:if>
			<c:if test="${isAutorizado && consultar}">
				|&nbsp;&nbsp;<a id="btn_excluir"  onmouseover="Tip('Cancelar Manifesto')" href="javascript:openDialogAuditoria()">Cancelar</a>
			</c:if>
		</c:if>
		<c:if test="${isImpresso && consultar}">
			|&nbsp;&nbsp;<a id="btn_imprimir" onmouseover="Tip('re-imprimir Manifesto')" href="javascript:openDialogReImpressao();">Re-Imprimir Manifesto</a>
		</c:if>
	</jsp:attribute>
	
	<jsp:body>
		<t:janelaEntrada>
			<t:tabelaEntrada>
				<n:tabPanel id="tabId">
					<n:panel title="Edição">			
						<t:property name="cdmanifesto" id="cdmanifestoId" type="hidden" write="false" label="" />
						<t:property name="deposito" id="depositoId" type="hidden" write="false" label=""/>
						<t:property name="manifestostatus" id="manifestostatusId" type="hidden" write="false" label=""/>
						<t:property name="dtemissao" type="hidden" write="false" label=""/>
						<t:property name="usuarioemissor" type="hidden" write="false" label=""/>
						<t:property name="cdae" type="hidden" write="false" label=""/>						
						<t:property name="selectCdnotafiscalsaida" type="hidden" write="false" label=""/>		
						<t:property name="isSolicitarAprovacao" type="hidden" write="false" label=""/>
						<t:property name="selectCdmanifesto" type="hidden" write="false" label=""/>
						<t:property name="filialreferencia" type="hidden" write="false" label=""/>
						<t:property name="manifestopai" type="hidden" write="false" label=""/>
						<w:tableGroup columns="7" panelgridWidth="80%">
							<t:property name="cdmanifesto" label="Manifesto" type="text" disabled="disabeld" class="disabled" style="width:60px;"/>
							<t:property name="deposito" type="text" disabled="disabeld" class="disabled" style="width:120px;"/>
							<t:property name="manifestostatus" type="text" disabled="disabeld" class="disabled"/>
							<t:property name="dtemissao" type="text" disabled="disabeld" class="disabled" style="width:110px;"/>
							<t:property name="usuarioemissor" type="text" disabled="disabeld" class="disabled" style="width:220px;"/>
							<t:property name="cdae" type="text" disabled="disabeld" class="disabled" style="width:80px;"/>
							<t:property name="manifestopai.cdmanifesto" type="text" disabled="disabeld" class="disabled" style="width:80px;" label="Agrupado"/>
						</w:tableGroup><br>
						<w:tableGroup columns="2" panelgridWidth="100%">
							<n:group legend="Detalhes Transportador" style="width:81%">
								<w:tableGroup columns="3" panelgridWidth="81%">
								<c:choose>
									<c:when test="${consultar}">
										<t:property name="transportador.nome" label="Transportador"/>
										<t:property name="motorista.nome" label="Motorista"/>
										<t:property name="veiculo.placa" label="Veículo"/>
									</c:when>
									<c:otherwise>
										<t:property name="transportador" 
													type="autocomplete" 
													itens="transportadorService.findForAutocompleteWithDepositoLogado" 
													autocompleteGetterLabel="getIdNomeDocumento" 
													autocompleteLabelProperty="transportador.cdpessoa,transportador.nome,transportador.documento"
													autocompleteOnExcluir="carregaMotoristaVeiculo()"
													onselect="carregaMotoristaVeiculo()" 
													style="width:300px;"/>
										<t:property name="motorista" id="motoristaId" disabled="disabled" class="disabled" style="width:250px;"/>
										<t:property name="veiculo" id="veiculoId" disabled="disabled" class="disabled" style="width:100px;"/>
									</c:otherwise>
								</c:choose>
								</w:tableGroup>		
							</n:group>					
							<n:group legend="Tipo de Manifesto" style="width:19%">
								<c:choose>
									<c:when test="${manifesto.tipoentrega.cdtipoentrega == null}">
										<t:property name="tipoentrega" label="Selecione 1 opção" renderAs="double" onclick="showHideBotaoInclusao()"/>
									</c:when>
									<c:otherwise>
										<t:property name="tipoentrega" id="tipoentregaHideId" type="hidden" write="false" label="" renderAs="double"/>										
										<t:property name="tipoentrega" label="" disabled="disabled" renderAs="double"/>
									</c:otherwise>
								</c:choose>
							</n:group>
						</w:tableGroup>
						<br>
						<n:group legend="Dados Complementares">
							<w:tableGroup columns="4" panelgridWidth="100%">
								<t:property name="lacrelateral" style="width:100px;"/>
								<t:property name="lacretraseiro" style="width:100px;"/>
								<t:property name="box"/>
								<br>
							<w:tableGroup columns="1" panelgridWidth="100%">
								<t:property name="observacao" rows="2" style="width:580px;"/>
							</w:tableGroup>
							</w:tableGroup>
						</n:group>
						
						<n:group legend="Gerenciamento de Risco">
							<w:tableGroup columns="6" panelgridWidth="100%">
								<t:property type="autocomplete" name="rotagerenciadora" itens="rotagerenciadoraService.findByAutocomplete" style="width:300px;"/>
								
								<c:if test="${!consultar}">
									<n:panel>
										<br>
										<a href="#" onclick="javascript:atualizarRotaGerenciadora();" style="text-transform: none;">Atualizar</a>&nbsp;&nbsp;&nbsp;
									</n:panel>			
								</c:if>		
							     <n:panel>
										<t:property name="datainicio" id="datainicio" label = "DATA/HORA INICIO" />
										<t:property name="horainicio" id="horainicio" renderAs="single" showLabel="false"/>	
								 </n:panel>
								  <n:panel>
								  <br/>&nbsp;&nbsp;&nbsp;&nbsp;
								  	<c:if test="${consultar}">
										<a href="#" class="btn_engrenagem" onclick="javascript:solicitarAnaliseConsult();" style="text-transform: none;">Solicitar Análise</a>
									</c:if>
									<c:if test="${!consultar}">
										<a href="#" class="btn_engrenagem" onclick="javascript:solicitarAnaliseEdit();" style="text-transform: none;">Solicitar Análise</a>
									</c:if>
									&nbsp;|&nbsp;
									<a href="#" class="btn_listagem" onclick="javascript:visularRetorno();" style="text-transform: none;">Visualizar Retorno</a>
							     </n:panel>
							</w:tableGroup>
						</n:group>
						<c:if test="${!consultar}">
							<div style="text-align: right; padding-top: 10px; padding-right: 5px;font-size: 11px">
								<a href="#1" class="btn_novo" id="incluirNotaId" onclick="javascript:incluirNotas();" style="text-transform: none;">Incluir Notas</a>
								<c:if test="${manifesto.filialreferencia.cdfilial != null}">
									<a href="#1" class="btn_editar" id="incluirManifestoId" onclick="javascript:openDialogFilialReferencia();" style="text-transform: none;">Alterar a Filial Referência</a>|&nbsp;
								</c:if>								
								<a href="#1" class="btn_novo" id="incluirManifestoId" onclick="javascript:incluirManifesto();" style="text-transform: none;">Incluir Manifesto</a>
							</div>
						</c:if>
						
						<br>
						<br>
						<w:tableGroup columns="1" panelgridWidth="100%">
							<t:detalhe name="listaManifestonotafiscal" id="listaManifestonotafiscalId" showBotaoNovaLinha="false" cellspacing="0" showBotaoRemover="${!isAgrupamento}">
								<n:column header="" style="width:1%">
									<t:property name="cdmanifestonotafiscal" type="hidden" write="false"/>
									<t:property name="praca.cdpraca" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cdnotafiscalsaida" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.numero" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.serie" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.dtemissao" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.numeropedido" type="hidden" write="false"/> 
									<t:property name="notafiscalsaida.lojapedido" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cliente.cdpessoa" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cliente.nome" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.listaRotapraca[0].rota.cdrota" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.listaRotapraca[0].rota.nome" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.listaRotapraca[0].rota.temDepositoTransbordo" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.listaRotapraca[0].rota.depositotransbordo" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.cdpraca" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.qtdeitens" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.vlrtotalnf" type="hidden" write="false"/>
									<t:property name="usuario.cdpessoa" type="hidden" write="false"/>
									<t:property name="usuario.nome" type="hidden" write="false"/>	
									<t:property name="dt_inclusao" type="hidden" write="false"/>
									<t:property name="temDepositoTransbordo" type="hidden" write="false"/>
									<t:property name="depositotransbordo" type="hidden" write="false"/>
								</n:column>
								<n:column header="Nro. Nota">
									<t:property name="notafiscalsaida.numero" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Série">
									<t:property name="notafiscalsaida.serie" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Dt. Emissão">
									<t:property name="notafiscalsaida.dtemissao" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Nro. Pedido">
									<t:property name="notafiscalsaida.numeropedido" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Nro. Loja">
									<t:property name="notafiscalsaida.lojapedido" mode="output" style="width:80px;"/>
								</n:column>					
								<n:column header="Cliente">
									<span id="clientenome${index}" class="labelcolumn"></span>									
								</n:column>
								<n:column header="Rota" style="width:70px;">
									<span id="rotanome${index}" class="labelcolumn"></span>
								</n:column>
								 <c:choose>
									<c:when test="${!consultar && !isAgrupamento}">
										<n:column header="Transbordo" id="transbordoHeaderID">
											<a id="btn_editar" onmouseover="Tip('Definir Transbordo')" href="javascript:openDialogTransbordo(${index})">
												<span id="isTransbordo${index}"></span>
											</a>
										</n:column>
									</c:when>
									<c:otherwise>
										<n:column header="Transbordo">
											<span id="isTransbordo${index}"></span>
										</n:column>
									</c:otherwise>
								</c:choose>
								
<%--								<n:column header="Transbordo">--%>
<%--									<c:if test="${!consultar && !isAgrupamento}">--%>
<%--										<a id="btn_editar"  onmouseover="Tip('Cancelar Manifesto')" href="javascript:openDialogTransbordo(${index})">--%>
<%--											<span id="isTransbordo${index}"></span>--%>
<%--										</a>--%>
<%--									</c:if>--%>
<%--									<c:if test="${consultar || isAgrupamento}">--%>
<%--										<span id="isTransbordo${index}"></span>--%>
<%--									</c:if>--%>
<%--								</n:column>--%>
								
								<n:column header="Qtde. de Itens">
									<t:property name="notafiscalsaida.qtdeitens" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Valor total">
									<t:property name="notafiscalsaida.vlrtotalnf" mode="output" style="width:80px;"/>
								</n:column>
							</t:detalhe>
						</w:tableGroup>
						
						<br><br>
							<table style="width:100%;padding-left:150px;">
								<tr>
									<th sytle="width:80%"> Total de NF's 	</th>
									<th sytle="width:20%"> Total de Itens 	</th>
									<th sytle="width:20%"> Valor Total		</th>
								</tr>
								<tr>
									<td sytle="width:80%">	<span id="totalNfId" class="labelcolumn">${qtdeNotas}</span>	</td>
									<td sytle="width:20%">	<span id="totalNfId" class="labelcolumn">${qtdeItens}</span>	</td>
									<td sytle="width:20%">	<span id="totalNfId" class="labelcolumn">${valorTotal}</span>	</td>
								</tr>
							</table>
						
					</n:panel>
					<n:panel title="Histórico">
						<t:detalhe name="listaManifestohistorico" showBotaoNovaLinha="false" showBotaoRemover="false" showColunaAcao="false">
							<n:column header="" style="width:1%">
								<t:property name="cdmanifestohistorico" type="hidden" write="false"/>
								<t:property name="dtalteracao" type="hidden" write="false"/>
								<t:property name="usuario.cdpessoa" type="hidden" write="false"/>
								<t:property name="usuario.nome" type="hidden" write="false"/>
								<t:property name="manifestostatus.cdmanifestostatus" type="hidden" write="false"/>
								<t:property name="manifestostatus.nome" type="hidden" write="false"/>
								<t:property name="motivo" type="hidden" write="false"/>
							</n:column>
							<t:property name="dtalteracao" mode="output" pattern="dd/MM/yyyy HH:mm" headerStyle="width:150px;"/>
							<t:property name="usuario.nome" mode="output" label="Responsável" headerStyle="width:200px;"/>
							<t:property name="manifestostatus.nome" label="Status" mode="output"/>
							<t:property name="motivo" mode="output"/>
						</t:detalhe>
					</n:panel>
				</n:tabPanel>
			</t:tabelaEntrada>
		</t:janelaEntrada>
	</jsp:body>		
	
</t:entrada>

<div id="auditoria-emitirmanifesto-dialog" style="display:none;" title="Cancelamento de Manifesto">
	<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="false">
		<t:propertyConfig mode="input" renderAs="double">
			<n:bean name="auditoriaVO" valueType="<%= Auditoria.class %>">
				<t:property name="login" id="loginId" class="required" style="width:200px;"/>
				<t:property name="senha" id="senhaId" class="required" style="width:200px;"/>
				<t:property name="motivo" id="motivoId" rows="6" class="required" style="width:200px;" label="Informe no campo abaixo,<br>o motivo do cancelamento do<br>manifesto de carga"/>
			</n:bean>
		</t:propertyConfig>
	</n:panelGrid>
</div>

<div id="auditoria-emitirmanifesto-dialog2" style="display:none;" title="Re-Impressão de Manifesto">
	<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="false">
		<t:propertyConfig mode="input" renderAs="double">
			<n:bean name="auditoriaVO" valueType="<%= Auditoria.class %>">
				<t:property name="login" id="loginId2" class="required" style="width:200px;"/>
				<t:property name="senha" id="senhaId2" class="required" style="width:200px;"/>
				<t:property name="motivo" id="motivoId2" rows="6" class="required" style="width:200px;" label="Descreva o mótivo da <br>re-impressão."/>
			</n:bean>
		</t:propertyConfig>
	</n:panelGrid>
</div>

<div id="transbordo-dialog" style="display:none;" title="Adicionar / Remover CD Transbordo">
	<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="true">
		<t:propertyConfig mode="input" renderAs="double">
			<n:bean name="manifestonotafiscal" valueType="<%= Manifestonotafiscal.class %>">
				<t:property name="temDepositoTransbordo" id="temDepositoTransbordoDialog" onchange="showHideDeposito()"/>
				<n:panel id="painelDepositoTransbordo">
					<span id="depositotransbordoLabelDialog">Depósito de Transbordo</span><br/><br/>
					<t:property name="depositotransbordo" id="depositotransbordoDialog" itens="${LISTA_DEPOSITO_TRANSBORDO}" label=""/>
				</n:panel>
				<input name="indexParam" id="indexParamId" type="hidden"/>
			</n:bean>
		</t:propertyConfig>
	</n:panelGrid>
</div>

<div id="dialogLoading-dialog" >
	<br>
	<div align="center" style="position:relative;">
  		<b><span id="msgPorcentagemThread" style="text-align: center">Esse processo pode levar alguns minutos.<br>Por favor, aguarde...<br><br></span></b>
  		<img src="${ctx}/imagens/loading-bar.gif">
  	</div>
</div>

<div id="log-dialog" style="display:none;" title="Lista de Retorno">
	<n:dataGrid itens="${listaLog}" var="log" itemType="<%=Logintegracaoae.class%>" id="tabelaLog">
		<n:column header="Código">
			<t:property name="cderro" mode="output"/>
		</n:column>
		<n:column header="Mensagem">
			<t:property name="dserro" mode="output"/>
		</n:column>
	</n:dataGrid>
</div>

<div id="filialReferencia-dialog" align="center" style="display:none;" title="Filial de Referência">
	<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="false">
		<t:propertyConfig mode="input" renderAs="double">
			<n:bean name="manifestoDialog" valueType="<%= Manifesto.class %>">
				<t:property name="filialreferencia" id="filialreferenciaDialogId" type="autocomplete" itens="filialService.findForAutocompleteByEmpresa"
							class="required" style="width:320px;" label="Selecione uma Filial de Referência antes de salvar."/>
			</n:bean>
		</t:propertyConfig>
	</n:panelGrid>
</div>

<script type="text/javascript">
	
	$(document).ready(function() {
		
		if('${isRedirectToListagem}'=='true'){
			alert("Esse manifesto não pode ser salvo, as notas estão vinculados a outro manifesto.");
			form.ACAO.value ='listagem';
			form.action = '${ctx}/expedicao/crud/Manifesto'; 
			form.validate = 'false'; 
			submitForm();
		}
		
		habilitarMotoristaVeiculo();
		
		$("#auditoria-emitirmanifesto-dialog2").dialog("destroy");
		$("#auditoria-emitirmanifesto-dialog2").dialog({
			autoOpen: false,
			height: 400,
			width: 249,
			modal: true,
			close: function( event, ui ) {
				$("#loginId").val(null);
				$("#senhaId").val(null);
				$("#motivoId").val(null);
			},buttons: {
				'Cancelar': function() {
					$("#auditoria-emitirmanifesto-dialog2").dialog('close');
				},
				'Confirmar': function() {
					confirmarReImprimirManifesto();
				}
			}
		});
		
		$("#auditoria-emitirmanifesto-dialog").dialog("destroy");
		$("#auditoria-emitirmanifesto-dialog").dialog({
			autoOpen: false,
			height: 400,
			width: 241,
			modal: true,
			close: function( event, ui ) {
				$("#loginId2").val(null);
				$("#senhaId2").val(null);
				$("#motivoId2").val(null);
			},buttons: {
				'Cancelar': function() {
					$("#auditoria-emitirmanifesto-dialog").dialog('close');
				},
				'Confirmar': function() {
					cancelarManifesto();
				}
			}
		});
		
		$("#transbordo-dialog").dialog("destroy");
		$("#transbordo-dialog").dialog({
			autoOpen: false,
			height: 250,
			width: 300,
			modal: true,
			buttons: {
				'Cancelar': function() {
					$("#transbordo-dialog").dialog('close');
				},
				'Confirmar': function() {
					var retorno = atualizarInfoTransbordo();
					if (retorno){
						$("#transbordo-dialog").dialog("close");
						alert('Para confirmar as alterações é necessário salvar.')
					}
				}
			}
		});
		
		$("#dialogLoading-dialog").dialog("destroy");
		$("#dialogLoading-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 80,
			width: 200,
			dialogClass: 'no-close'
		});

		$("#log-dialog").dialog("destroy");
		$("#log-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 300,
			width: 500,
			buttons: {
				'Fechar': function() {
					$("#log-dialog").dialog('close');
				}
			}
		});
		
		$("#filialReferencia-dialog").dialog("destroy");
		$("#filialReferencia-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 150,
			width: 400,
			buttons: {
				'Concluir': function() {
					if($w.getAutocompleteId($("#filialreferenciaDialogId").val()) != ""){
						$("input[name=filialreferencia]").val($("#filialreferenciaDialogId").val());
						$("#filialReferencia-dialog").dialog('close');
					}else{
						alert("O campo 'Filial de Referência' é obrigatório");
					}
				}
			}
		});
		
		//
		
		if('${consultar}' != 'true'){
			if(form['transportador'].value != '<null>'){
				
				var motorista = form['motorista'].value;
				var veiculo = form['veiculo'].value;
				
				carregaMotoristaVeiculo();
				
				$("#veiculoId option").filter(function() {
				    return $(this).val() == veiculo; 
				}).attr('selected', true);
	
				$("#motoristaId option").filter(function() {
				    return $(this).val() == motorista; 
				}).attr('selected', true);
			}
		}
		
		populaInfoColunaTransbordo();
		quebralinhaRota();
		quebralinhaCliente();
		showHideBotaoInclusao();
		
		if('${showLogList}' == 'true'){
			$("#log-dialog").dialog("open");
		}

		$("#transbordoHeaderID").html("Transbordo <button type='button' class='btnApp' onclick='javascript:openDialogTransbordo(\"todos\")' class=''>Todos</button>");
	});
	
	function cancelarManifesto(){
		if($("#motivoId").val().trim()==""){
			alert("O campo motivo é obrigatório.");			
		}else{
			var motivo = $("#motivoId").val();
			var cdmanifesto = $("#cdmanifestoId").val();
			var login = $("#loginId").val();
			var senha = $("#senhaId").val();
			
			$w.getJSONSync("${ctx}/expedicao/crud/Manifesto",{ACAO:'cancelarManifesto','motivo':motivo,'id':cdmanifesto,'login':login,'senha':senha},				
				function(json){
					location.reload(true);
				}
			);			
		}
	}
	
	function openDialogAuditoria(){
		$("#auditoria-emitirmanifesto-dialog").dialog("open");
	}
	
	function carregaMotoristaVeiculo(){	
		var transportador = form['transportador'].value;
		var deposito = $("#depositoId").val();
		$w.getJSONSync("${ctx}/expedicao/crud/Manifesto",{ACAO:'carregaMotoristaVeiculoEntrada','transportador':transportador,'deposito':deposito},
			function(json){
				if(json!=null){
					refreshComboMotorista(json.listaMotorista);
					refreshComboVeiculo(json.listaVeiculo);
				}
			}
		);
	}
	
	function refreshComboMotorista(listaMotorista){
		if(listaMotorista!=null && listaMotorista.length > 0){
			$("#motoristaId").removeAttr("disabled");
			$("#motoristaId").removeClass("disabled");
			$("#motoristaId").addClass("required");
			$(form['motorista']).removeOption(/./);
			var i= 0;
			$(form['motorista']).addOption("<null>"," ");
			for(i=0; i< listaMotorista.length; i++){
				$(form['motorista']).addOption("br.com.linkcom.wms.geral.bean.Motorista[cdmotorista="+listaMotorista[i].cdmotorista+"]",listaMotorista[i].nome,false);
			}
		}else{
			$("#motoristaId").removeClass("required");
			$("#motoristaId").attr("disabled","true");
			$("#motoristaId").addClass("disabled");
			$(form['motorista']).removeOption(/./);
		}
	}
	
	function refreshComboVeiculo(listaVeiculo){
		if(listaVeiculo!=null && listaVeiculo.length > 0){
			$("#veiculoId").removeAttr("disabled");			
			$("#veiculoId").removeClass("disabled");
			$("#veiculoId").addClass("required");
			$(form['veiculo']).removeOption(/./);
			var i= 0;
			$(form['veiculo']).addOption("<null>"," ");
			for(i=0; i< listaVeiculo.length; i++){
				$(form['veiculo']).addOption("br.com.linkcom.wms.geral.bean.Veiculo[cdveiculo="+listaVeiculo[i].cdveiculo+"]",listaVeiculo[i].placa,false);
			}
		}else{
			$("#veiculoId").removeClass("required");
			$("#veiculoId").attr("disabled","true");
			$("#veiculoId").addClass("disabled");
			$(form['veiculo']).removeOption(/./);
		}
	}
	
	function incluirNotas(){
		
		var cdtipoentrega;
		try {
		   	cdtipoentrega = $w.getComboIdSelected(form['tipoentrega']);
		}
		catch(err) {
			cdtipoentrega = $w.getValueId($("#tipoentregaHideId").val());
		}
		if(cdtipoentrega=='undefined' || cdtipoentrega=="<null>" || cdtipoentrega.trim()==""){
			alert("É necessário selecionar o tipo de entrega antes de inlcuir novas notas.");
		}else{
			$w.openPopup("${ctx}/expedicao/crud/Manifesto?ACAO=buscarNotas&tipoentrega.cdtipoentrega="+cdtipoentrega,998,605);
		}
	}
	
	function openDialogReImpressao(){
		$("#auditoria-emitirmanifesto-dialog2").dialog("open");
	}
	
	function inserirNotas(notas){
		form['selectCdnotafiscalsaida'].value = notas;
		form.ACAO.value ='inserirNotas';
		form.action = '${ctx}/expedicao/crud/Manifesto'; 
		form.validate = 'false'; 
		submitForm();
	}
	
	function habilitarMotoristaVeiculo(){
		if(${!consultar}){
			if($("#veiculoId").val()!='<null>'){
				$("#veiculoId").removeAttr("disabled");			
				$("#veiculoId").removeClass("disabled");
			}
			if($("#motoristaId").val()!='<null>'){
				$("#motoristaId").removeAttr("disabled");
				$("#motoristaId").removeClass("disabled");
			}
		}
	}
	
	function imprimirManifesto(){
		if(!isManifestoImpresso()){			
			if(confirm("Uma vez impresso o manifesto não poderá ser editado. Deseja realmente confirmar a operação?")){
				form.action = '${ctx}/expedicao/report/Emitirmanifesto?ACAO=gerar&cdmanifesto='+$("#cdmanifestoId").val()+'&manifestostatus.cdmanifestostatus='+$w.getValueId($("#manifestostatusId").val()) 
				form.validate = 'false'; 
				submitForm();
			}
		}
	}
	
	function isManifestoImpresso(){
		
		var cdmanifesto = $('input[name=cdmanifesto]').val();
		var param = {ACAO:'isManifestoImpresso','cdmanifesto':cdmanifesto};
		var isImpresso = 'false';
		
		$w.showLoading();
		$w.getJSONSync("${ctx}/expedicao/report/Emitirmanifesto",param, function(json){
			isImpresso = json.isImpresso;
		});
		$w.hideLoading();
		
		if(isImpresso=="true"){
			alert('Esse manifesto já foi impresso. Por favor, atualize a página e tente novamente.');
			return true;
		}else{
			return false;
		}
	}
	
	function callConsultar(cdmanifesto){
		form.action = '${ctx}/expedicao/crud/Manifesto?ACAO=consultar&cdmanifesto='+cdmanifesto; 
		form.validate = 'false'; 
		submitForm();
	}
	
	function confirmarReImprimirManifesto(){
		if($("#motivoId2").val().trim()==""){
			alert("O campo motivo é obrigatório.");			
		}else{
			var cdmanifesto = $("#cdmanifestoId").val();
			var motivo = $("#motivoId2").val();
			var login = $("#loginId2").val();
			var senha = $("#senhaId2").val();
			var cdmanifestostatus = $w.getValueId($("#manifestostatusId").val());
			
			form.action = '${ctx}/expedicao/report/Emitirmanifesto?ACAO=gerar&cdmanifesto='+cdmanifesto+
			                                                      			'&auditoriaVO.id='+cdmanifesto+
			                                                      			'&auditoriaVO.login='+login+
			                                                      			'&auditoriaVO.senha='+senha+
			                                                      			'&auditoriaVO.motivo='+motivo+
			                                                      			'&manifestostatus.cdmanifestostatus='+cdmanifestostatus;
			
			form.validate = 'false'; 
			submitForm();	
			$("#auditoria-emitirmanifesto-dialog2").dialog("close");
				
		}
	}
	
	function validarCadastro(){
		
		var msg = "";
		var isAgrupadoComTransferencia = '${isAgrupadoComTransferencia}';
		var filialReferencia = $("input[name=filialreferencia]").val();
		
		if(form['tipoentrega'].value == "<null>")
			msg += "O campo 'Tipo de Entrega' é obrigatório.\n";

		if(typeof form['transportador'] == 'undefined'){
			if ($('input[name=transportador.nome]').val() == null){
		    	msg += "O campo 'Transportador' é obrigatório.\n";
			}
		}else{
			if (form['transportador'].value == "<null>"){
		    	msg += "O campo 'Transportador' é obrigatório.\n";
			}
		}
		
		if($("#motoristaId").val() == "<null>")
			msg += "O campo 'Motorista' é obrigatório.\n";
		if($("#veiculoId").val() == "<null>")
			msg += "O campo 'Veiculo' é obrigatório.\n";			
		
		if(isAgrupadoComTransferencia!=null && isAgrupadoComTransferencia == "false" && filialReferencia == "<null>"){
			$("#filialReferencia-dialog").dialog("open");
			return false;
		}
		
		if(msg != ""){
			alert(msg);
			return false;
		}else{
			return true;
		}
	}
	
	function openDialogTransbordo(detalheIndicie){
		if(detalheIndicie!='todos'){
			populaInfoDialogTransbordo(detalheIndicie);
		}
		showHideDeposito();			
		$("#transbordo-dialog").dialog("open");	
		$("#indexParamId").val(detalheIndicie);
	}
	
	function populaInfoDialogTransbordo(detalheIndicie){
		if($('input[name="listaManifestonotafiscal['+detalheIndicie+'].temDepositoTransbordo"]').val() == "true"){
			$("#temDepositoTransbordoDialog").attr("checked",true);
			$("#temDepositoTransbordoDialog").val("true");
			$("#depositotransbordoDialog").val($('input[name="listaManifestonotafiscal['+detalheIndicie+'].depositotransbordo"]').val());
		}else if($('input[name="listaManifestonotafiscal['+detalheIndicie+'].notafiscalsaida.praca.listaRotapraca[0].rota.temDepositoTransbordo"]').val() == "true"){
			$("#temDepositoTransbordoDialog").attr("checked",true);
			$("#temDepositoTransbordoDialog").val("true");
			$("#depositotransbordoDialog").val($('input[name="listaManifestonotafiscal['+detalheIndicie+'].notafiscalsaida.praca.listaRotapraca[0].rota.depositotransbordo"]').val());
		}
	}
	
	function atualizarInfoTransbordo(){
		
		var temDepositoTransbordo = $("#temDepositoTransbordoDialog").attr("checked");
		var depositotransbordo = $("#depositotransbordoDialog").val();
		var temDepositoTransbordoRota = false;

		if ($('input[name="listaManifestonotafiscal['+$("#indexParamId").val()+'].notafiscalsaida.praca.listaRotapraca[0].rota.depositotransbordo"]').val() != '<null>')
			var temDepositoTransbordoRota = $('input[name="listaManifestonotafiscal['+$("#indexParamId").val()+'].notafiscalsaida.praca.listaRotapraca[0].rota.depositotransbordo"]').val();

		if (temDepositoTransbordoRota && !temDepositoTransbordo){
			alert('Para esta rota, é obrigatorio realizar o transbordo!!!');
			return false;
		}
		
		if($("#indexParamId").val()=='todos'){
			var count = 0;
			$('input[name$=.numero]').each(function(){
				$('input[name=listaManifestonotafiscal['+count+'].temDepositoTransbordo]').val(Boolean(temDepositoTransbordo));
				$('input[name=listaManifestonotafiscal['+count+'].depositotransbordo]').val(depositotransbordo);
				count++;
			});
		}else{
			$('input[name=listaManifestonotafiscal['+$("#indexParamId").val()+'].temDepositoTransbordo]').val(Boolean(temDepositoTransbordo));
			$('input[name=listaManifestonotafiscal['+$("#indexParamId").val()+'].depositotransbordo]').val(depositotransbordo);
			$("#temDepositoTransbordoDialog").removeAttr("checked");
		}

		return true;
	}
	
	function showHideDeposito(){
		if($("#temDepositoTransbordoDialog").attr("checked") == true){
			$("#painelDepositoTransbordo").show();
		}else{
			$("#painelDepositoTransbordo").hide();
			$("#depositotransbordoDialog").val("<null>")
		}
	}
	
	function populaInfoColunaTransbordo(){
		var count = 0;
		$('input[name$=notafiscalsaida.numero]').each(function(){
			if($('input[name="listaManifestonotafiscal['+count+'].temDepositoTransbordo"]').val() == "true"){
				$("#isTransbordo"+count).html('Sim');
			}else if($('input[name="listaManifestonotafiscal['+count+'].notafiscalsaida.praca.listaRotapraca[0].rota.temDepositoTransbordo"]').val() == "true"){
				$("#isTransbordo"+count).html('Sim');
				$('input[name="listaManifestonotafiscal['+count+'].temDepositoTransbordo').val("true");
				$('input[name="listaManifestonotafiscal['+count+'].depositotransbordo"]').val($('input[name="listaManifestonotafiscal['+count+'].notafiscalsaida.praca.listaRotapraca[0].rota.depositotransbordo').val());
			}else{
				$("#isTransbordo"+count).html('Não');
			}
			count++;
		})
	}
	
	function visularRetorno(){
		
		if($('input[name=cdmanifesto]').val() == undefined){
			alert('Antes de realizar está ação é necessário salvar o manifesto.');
			return false;
		}
		
		$("#log-dialog").dialog("open");
	}

	function solicitarAnaliseEdit(){
		if(validarCadastro() && form['rotagerenciadora'].value != "<null>"){
			if(confirm("Será necessário salvar antes de prosseguir.\nUma vez solicitado não será possivel editar o manifesto.\nDeseja continuar?")){
				$('input[name=isSolicitarAprovacao]').val(true);
				form.ACAO.value ='salvar';
				form.action = '${ctx}/expedicao/crud/Manifesto'; 
				form.validate = 'true'; 
				submitForm();
			}
		}
	}
	
	function solicitarAnaliseConsult(){
		if(validarCadastro() && form['rotagerenciadora'].value != "<null>"){
			if(confirm("Será necessário salvar antes de prosseguir.\nUma vez solicitado não será possivel editar o manifesto.\nDeseja continuar?")){
				$('#cdmanifestoId').removeAttr('disabled');
				$('#depositoId').removeAttr('disabled');
				$('input[name=isSolicitarAprovacao]').removeAttr('disabled');
				
				$('input[name=isSolicitarAprovacao]').val(true);
				
				form.ACAO.value ='analisarRota';
				form.action = '${ctx}/expedicao/crud/Manifesto'; 
				form.validate = 'true'; 
				submitForm();
			}
		}
	}
	
	function atualizarRotaGerenciadora(){
		
		var cddeposito = $w.getValueId($("#depositoId").val());
		var param = {ACAO:'atualizarRotaGerenciadora','cddeposito':cddeposito};
		
		$("#dialogLoading-dialog").dialog("open");
		$w.getJSON("${ctx}/expedicao/crud/Manifesto",param, function(json){
			if(json.listaRotagerenciadora==null || json.retorno!="OK"){
				alert(json.retorno);
			}
			$("#dialogLoading-dialog").dialog("close");
		});
	}
	
	/* Cria a linha do Datagrid */
	function makeRow(i, log) {
		var row = "<tr class='" + (i % 2 == 0 ? "dataGridBody1" : "dataGridBody2") + "'>";
			row += "<td>" + log.cderro + "</td>";
			row += "<td>" + log.dserro + "</td>";
		row += "</tr>";
		
		return row;		
	}
	
	function salvarForCDAE(){
		//isSolicitarAprovacao
		
		form.ACAO.value ='salvar';
		form.action = '/wmsre/expedicao/crud/Manifesto'; 
		form.validate = 'true'; 
		submitForm();
	}
	
	function quebralinhaRota(){
		for(var i=0;i<=$("#listaManifestonotafiscalId tr").length;i++){
			var rotaNome = $('input[name="listaManifestonotafiscal['+i+'].notafiscalsaida.praca.listaRotapraca[0].rota.nome"]').val();
			var id = "#rotanome"+i
			try{
				$(id).html($w.quebralinhaDetalhe(17,rotaNome));
			}catch(err){
				$(id).html('');
			}
		}
	}
	
	function quebralinhaCliente(){
		for(var i=0;i<=$("#listaManifestonotafiscalId tr").length;i++){
			var clienteNome = $('input[name="listaManifestonotafiscal['+i+'].notafiscalsaida.cliente.nome"]').val();
			var id = "#clientenome"+i;		
			try{
				$(id).html($w.quebralinhaDetalhe(17,clienteNome));
			}catch(err){
				$(id).html('');
			}
		}
	}
	
	function imprimirDescargaProdutos(){
		if($("#cdmanifestoId").val()!=""){
			form.action = '${ctx}/expedicao/report/Descargaproduto?ACAO=gerar&cdmanifesto='+$("#cdmanifestoId").val(); 
			form.validate = 'false'; 
			submitForm();
		}
	}
	
	function showHideBotaoInclusao(){
		
		var cdtipoentrega;
		
		try {
		   	cdtipoentrega = $w.getComboIdSelected(form['tipoentrega']);
		}
		catch(err) {
			cdtipoentrega = $w.getValueId($("#tipoentregaHideId").val());
		}
		
		if(cdtipoentrega=='undefined' || cdtipoentrega=="<null>" || cdtipoentrega.trim()=="" || cdtipoentrega=="1" || cdtipoentrega=="2"){
			$("#incluirNotaId").show();
			$("#incluirManifestoId").hide();
		}else{
			$("#incluirNotaId").hide();
			$("#incluirManifestoId").show();
		}
		
	}
	
	function incluirManifesto(){
		$w.openPopup("${ctx}/expedicao/crud/Manifesto?ACAO=buscarManifesto&cdmanifesto",998,605);
	} 
	
	function inserirManifestos(cdmanifestos){
		if(form['selectCdmanifesto'].value == "<null>"){
			form['selectCdmanifesto'].value = cdmanifestos;
		}else{
			form['selectCdmanifesto'].value += ','+cdmanifestos;
		}
		form.ACAO.value ='inserirManifestos';
		form.action = '${ctx}/expedicao/crud/Manifesto'; 
		form.validate = 'false'; 
		submitForm();
	}
	
	function openDialogFilialReferencia(){
		$("#filialReferencia-dialog").dialog("open");
	}
	
</script>