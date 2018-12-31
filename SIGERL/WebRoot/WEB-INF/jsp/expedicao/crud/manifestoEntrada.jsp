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
	.table-info {
		display: table;
	    width: 100%;
	    max-width: 100%;
	    margin-bottom: 20px;
	    font-size: 11px;
	    border-spacing: 0;
	    border-collapse: collapse;
	    background-color: transparent;
	}
	.table-info th{
		background-color: #4cb46b;
		text-align: left;
	}
	.dataGrid{
     	border-spacing: 0;
    	border-collapse: collapse;
    	font-family: tahoma;
		font-size: 10px;	
		color: #000000;
		background-color: #FFFFFF;
	}
</style>


<div class="container corpo-pagina">
	<h3 class="titulo-pagina">Reversão de Pedidos</h3>
	<n:form validateFunction="validarFormulario" enctype="${entradaTag.formEnctype}">
		<n:validation functionName="validateForm">
	     	<c:if test="${consultar}">
				<input type="hidden" name="forcarConsulta" value="true"/>
				<style>input, select, textarea, .required {background-color:#ffffff; color:#000000;}</style>
			</c:if>
			
			<div class="panel panel-default" style="border: 0px;" >
				<div class="panel-body">
					<div class="col-md-6 pull-right" style="text-align: -webkit-right;">
						<c:if test="${consultar}">
							<c:if test="${(empty entradaTag.showListagemLink) || entradaTag.showListagemLink}">
								<n:link action="listagem" id="btn_voltar"  checkPermission="true" description="Retornar à listagem" class="btn btn-default btn-sm">Retornar à listagem</n:link>&nbsp;|&nbsp;
							</c:if>
							
							<n:link action="criar"  id="btn_novo" class="btn btn-info btn-sm"  checkPermission="true" description="Novo">Novo</n:link>&nbsp;|&nbsp;
							
							<c:if test="${(isEmElaboracao || isAguardandoLiberacao) && !isAutorizado}">								
								<n:link action="editar" id="btn_editar" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" class="btn .btn-primary btn-sm" checkPermission="true" description="Editar">Editar</n:link>&nbsp;|&nbsp;
							</c:if>
							
							<c:if test="${(isEmElaboracao || isAguardandoLiberacao) && !isAutorizado}">
								<n:link action="excluir" id="btn_excluir" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" confirmationMessage="Você tem certeza que deseja excluir este registro?" class="btn btn-danger btn-sm"  checkPermission="true" description="Excluir">Excluir</n:link>
							</c:if>
						</c:if>
						<c:if test="${!consultar}">
							<n:link action="listagem" id="btn_voltar" confirmationMessage="Deseja retornar à listagem sem salvar as alterações?" class="btn btn-default btn-sm" checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>					
							<n:submit type="link" id="btn_gravar" title="Gravar" action="salvar" validate="true" confirmationScript="${entradaTag.dynamicAttributesMap['submitconfirmationscript']}" class="btn btn-success btn-sm" checkPermission="true" description="Salvar">Salvar</n:submit>
						</c:if>
					</div>		
		        </div>
			</div>
			
		</n:validation>
	</n:form>
</div>

<%-- <t:entrada titulo="Manifesto de Carga ${manifesto.cdmanifesto}" submitConfirmationScript="validarCadastro()" showDeleteLink="" showEditLink="">
	
	<jsp:attribute name="linkArea">
		<c:if test="${!isEmElaboracao && !isAguardandoLiberacao && consultar}">
			<c:choose>
				<c:when test="${isImpresso}">
					<a id="btn_excluir"  onmouseover="Tip('Cancelar Manifesto')" href="javascript:openDialogAuditoria()">Cancelar</a>
				</c:when>
				<c:otherwise>
					<span>Excluir</span> 
				</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${isEmElaboracao || isAguardandoLiberacao || (!isImpresso && isAutorizado && consultar)}">
			<c:if test="${isEmElaboracao && consultar}">
				|&nbsp;&nbsp;<a id="btn_imprimir" onmouseover="Tip('Imprimir Manifesto')" href="javascript:imprimirManifesto()">Imprimir Manifesto</a>
			</c:if>
			<c:if test="${isAguardandoLiberacao && consultar}">
				|&nbsp;&nbsp;<a id="btn_associar" onmouseover="Tip('Autorizar Notas')" href="javascript:autorizarNotas()">Autorizar Notas</a>
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
						<t:property name="selectCdImportacaoCarga" type="hidden" write="false" label=""/>		
						<t:property name="isSolicitarAprovacao" type="hidden" write="false" label=""/>
						<t:property name="selectCdmanifesto" type="hidden" write="false" label=""/>
						<t:property name="filialreferencia" type="hidden" write="false" label=""/>
						<t:property name="manifestopai" type="hidden" write="false" label=""/>
						<t:property name="tipoentrega.cdtipoentrega" type="hidden" write="false" label=""/>
						<t:property name="temTransbordo" type="hidden" write="false" label=""/>
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
													autocompleteOnSelect="carregaMotoristaVeiculo()"
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
								<a href="#1" class="btn_novo" id="incluirPackingListId" onclick="javascript:incluirPackingList();" style="text-transform: none;">Incluir packing list</a>
							</div>
						</c:if>
						
						<br>
						<br>
						<w:tableGroup columns="1" panelgridWidth="100%" >
							<t:detalhe name="listaManifestonotafiscal" id="listaManifestonotafiscalId" showBotaoNovaLinha="false" cellspacing="0" showBotaoRemover="${!isAgrupamento}">
								<n:column header="" style="width:1%">
									<t:property name="cdmanifestonotafiscal" type="hidden" write="false"/>
									<t:property name="praca.cdpraca" type="hidden" write="false"/>
									<t:property name="existeFreteClienteNota" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cdnotafiscalsaida" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.notaautorizada" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.numero" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.serie" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.dtemissao" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.numeropedido" type="hidden" write="false"/> 
									<t:property name="notafiscalsaida.lojapedido" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cliente.cdpessoa" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cliente.nome" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.notafiscaltipo.cdnotafiscaltipo" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.listaRotapraca[0].rota.cdrota" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.listaRotapraca[0].rota.nome" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.cdpraca" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.praca.nome" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.qtdeitens" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.temtroca" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.vlrtotalnf" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.importacaocarga.cdcarga" type="hidden" write="false"/>
									<t:property name="usuario.cdpessoa" type="hidden" write="false"/>
									<t:property name="usuario.nome" type="hidden" write="false"/>	
									<t:property name="dt_inclusao" type="hidden" write="false"/>
									<t:property name="temDepositoTransbordo" type="hidden" write="false"/>
									<t:property name="depositotransbordo" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.cdnotafiscalsaidareferencia" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.valorfretecliente" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.tipovenda" type="hidden" write="false"/>
									<t:property name="notafiscalsaida.tipovenda.cdtipovenda" type="hidden" write="false"/>
								</n:column>
								<n:column header="Nro. Nota">
									<t:property name="notafiscalsaida.numero" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Série">
									<t:property name="notafiscalsaida.serie" mode="output" style="width:70px;"/>
								</n:column>
								<n:column header="Dt. Emissão">
									<t:property name="notafiscalsaida.dtemissao" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Pedido">
									<t:property name="notafiscalsaida.numeropedido" mode="output" style="width:80px;"/>
								</n:column>
								<n:column header="Loja">
									<t:property name="notafiscalsaida.lojapedido" mode="output" style="width:80px;"/>
								</n:column>					
								<n:column header="Carga">
									<t:property name="notafiscalsaida.importacaocarga.cdcarga" mode="output" style="width:80px;"/>									
								</n:column>
								<n:column header="Cliente">
									<span id="clientenome${index}" class="labelcolumn"></span>									
								</n:column>
								<n:column header="Rota" style="width:70px;">
									<span id="rotanome${index}" class="labelcolumn"></span>
								</n:column>
								<n:column header="Praça" style="width:70px;">
									<span id="pracanome${index}" class="labelcolumn"></span>
								</n:column>

								<n:column header="Transbordo">
									<span id="isTransbordo${index}"></span>
								</n:column>
								
								<n:column header="Qtde. Itens">
									<t:property name="notafiscalsaida.qtdeitens" mode="output" style="width:50px;"/>
								</n:column>
								<n:column header="Valor total">
									<t:property name="notafiscalsaida.vlrtotalnf" mode="output" style="width:70px;"/>
								</n:column>
								<n:column header="Site" id="novoSiteId">
									<strong>
									<c:if test="${manifestonotafiscal.notafiscalsaida.tipovenda.cdtipovenda != 2}">Não</c:if>						
									<c:if test="${manifestonotafiscal.notafiscalsaida.tipovenda.cdtipovenda == 2}">Sim</c:if>						
									</strong>							
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

<div id="auditoria-nota-devolucao-dialog" style="display:none;" title="Nota de Devolução">
	<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="false">
		<t:propertyConfig mode="input" renderAs="double">
			<n:bean name="auditoriaVO" valueType="<%= Auditoria.class %>">
				<t:property name="login" id="loginId3" class="required" style="width:200px;"/>
				<t:property name="senha" id="senhaId3" class="required" style="width:200px;"/>
				<t:property name="motivo" id="motivoId3" rows="6" class="required" style="width:200px;" label="Descreva no campo abaixo, <br>o motivo da retirada <br> do produto da carga."/>
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


<div id="info-nota-devolucao" style="display:none;" title="Manifesto">
	<p>As notas da devolução abaixo são notas que tiveram retorno e foram automaticamente incluidas no 
	manifesto para recolhimento, favor anexar as notas ao manifesto ou retira-lás.</p>
	<table class="table-info">
		<tr>
			<th>Nota</th>
			<th>Cliente</th>
		<tr>
		<c:forEach items="${ manifesto.listaNotaFiscalDevolucao }" var="item" varStatus="loop">
			<tr>
				<td>${ item.numero }</td>
				<td>
				${ item.cliente.nome }
				<input type="hidden" name="excluirNota[]" id="excluirNota" value="[${ item.cdnotafiscalsaida }]"/>
				</td>
			<tr>
		</c:forEach>
	</table>
</div>


<div id="autorizarNotas-dialog" style="display:none;" title="Autorizar Notas">
	<form method="POST" name="formAutorizarNotas" id="formAutorizarNotas" action="${ctx}/expedicao/crud/Manifesto" onsubmit="return false;">
		<input type="hidden" name="ACAO" value="liberarNotas"/>
		<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="true">
			<t:propertyConfig mode="input" renderAs="double">
				<n:bean name="manifesto" valueType="<%= Manifesto.class %>">
					<input name="cdmanifesto" id="cdmanifestoID" type="hidden" write="false" value="${manifesto.cdmanifesto}"/>
					<br/>
					<t:detalhe name="listaNotaFiscalSemFrete" id="listaNotaFiscalSemFreteDialog" var="manifestoNotaFiscal">
						<n:column header="">
							<t:property name="cdmanifestonotafiscal" type="hidden" write="false"/>
						</n:column>
						<n:column header="Nro. Nota">
							<t:property name="notafiscalsaida.numero" mode="output"/>
						</n:column>
						<n:column header="Série">
							<t:property name="notafiscalsaida.serie" mode="output"/>
						</n:column>
						<n:column header="Dt. Emissão">
							<t:property name="notafiscalsaida.dtemissao" mode="output"/>
						</n:column>
						<n:column header="Pedido">
							<t:property name="notafiscalsaida.numeropedido" mode="output"/>
						</n:column>
						<n:column header="Loja">
							<t:property name="notafiscalsaida.lojapedido" mode="output"/>
						</n:column>
						<n:column header="Senha P/ Autorização">
							<t:property name="senhaAutorizacao" mode="input"/>
						</n:column>
					</t:detalhe>
				</n:bean>
			</t:propertyConfig>
		</n:panelGrid>
	</form>	
</div>

<div id="notasTransbordo-dialog" style="display:none;" title="Notas com opção de Transbordo">
	<form method="POST" name="formCriarTransbordo" id="formCriarTransbordo" action="${ctx}/expedicao/crud/Manifesto" onsubmit="return false;">
		<input type="hidden" name="ACAO" value="incluirTransbordoNotas"/>
		<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="true">
			<t:propertyConfig mode="input" renderAs="double">
				<n:bean name="manifesto" valueType="<%= Manifesto.class %>">
					<input name="cdmanifesto" id="cdmanifestoTranbordoID" type="hidden" write="false" value="${manifesto.cdmanifesto}"/>
					<label style="font-weight: bold; font-size: 16px;">Foi detectado neste manifesto que será necessário fazer transbordo da carga em outro local. Favor validar!</label>
					<br/>
					<br/>
					<t:detalhe name="listaNotasTransbordo" id="listaNotasTransbordoDialog" var="notasTransbordo">
						<n:column header="">
							<t:property name="cdManifestoNotaFiscal" type="hidden" write="false"/>
						</n:column>
						<n:column header="Nro. Nota">
							<t:property name="numero" mode="output"/>
						</n:column>
						<n:column header="Série">
							<t:property name="serie" mode="output"/>
						</n:column>
						<n:column header="Dt. Emissão">
							<t:property name="dtEmissao" mode="output"/>
						</n:column>
						<n:column header="Pedido">
							<t:property name="numeroPedido" mode="output"/>
						</n:column>
						<n:column header="Loja">
							<t:property name="lojaPedido" mode="output"/>
						</n:column>
						<n:column header="Rota">
							<t:property name="rota" mode="output"/>
						</n:column>
						<n:column header="Depósito de Transbordo">
							<t:property name="depositoTransbordo" mode="input" itens="${LISTA_DEPOSITO_TRANSBORDO}"/>
						</n:column>
					</t:detalhe>
				</n:bean>
			</t:propertyConfig>
		</n:panelGrid>
	</form>	
</div>
<div id="motivodeExcessao-dialog">
	<form class="margin-top: 67px;" method="POST" name="formMotivodeExcessao" id="formMotivodeExcessao" action="${ctx}/expedicao/crud/Manifesto" onsubmit="return false;">
		<input type="hidden" name="ACAO" value="inserirMotivo"/>
		<h3>Informe no campo abaixo,<br> o motivo do excesso de valores na carga.</h3>
		<input type="hidden" name="ACAO" value="liberarManifesto"/>
		<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="true">
			<n:bean name="manifesto" valueType="<%= Manifesto.class %>">
				<input name="cdmanifesto" id="id_cdmanifesto_" type="hidden" write="false" value="${manifesto.cdmanifesto}"/>
				<textarea name="motivo" rows="6" cols="15" style="width: 300px;padding: 0.7em; -webkit-border-radius: 5px" required></textarea>
			</n:bean>
		</n:panelGrid>
	</form>	
</div>
<div id="autorizarManifesto-dialog" title="Token de Autorização">
	<form class="margin-top: 67px;" method="POST" name="formAutorizarManifesto" id="formAutorizarManifesto" action="${ctx}/expedicao/crud/Manifesto" onsubmit="return false;">
		<h3>Favor informar a senha de autorização para prosseguir com a liberação.</h3>
		<input type="hidden" name="ACAO" value="liberarManifesto"/>
		<n:panelGrid columns="1" columnStyleClasses="labelColumn,propertyColumn"  propertyRenderAsDouble="true">
			<t:propertyConfig mode="input" renderAs="double">
				<n:bean name="manifesto" valueType="<%= Manifesto.class %>">
					<input name="cdmanifesto" id="id_cdmanifesto" type="hidden" write="false" value="${manifesto.cdmanifesto}"/>
					<input type="password" name="senhaAutorizacao" style="width: 300px;padding: 0.7em; -webkit-border-radius: 5px" required>
				</n:bean>
			</t:propertyConfig>
		</n:panelGrid>
	</form>	
</div>--%>

<script type="text/javascript">

	//caso seja alterada a função validation ela será chamada após a validacao do formulario
	var validation;
	
	function validarFormulario(){
		var valido = validateForm();
		if(validation){
			valido = validation(valido);
		}
		return valido;
	}

	function alertExclude(){
		confirm("Você tem certeza que deseja excluir este registro?");
	}
	
	function alertCancel(){
		return confirm("Deseja retornar à consulta sem salvar as alterações?");
	}

/* LIBERADO = 0;
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
		$("#auditoria-nota-devolucao-dialog").dialog("destroy");
		$("#auditoria-nota-devolucao-dialog").dialog({
			autoOpen: false,
			height: 400,
			width: 249,
			modal: true,
			close: function( event, ui ) {
				$("#loginId3").val(null);
				$("#senhaId3").val(null);
				$("#motivoId3").val(null);
				$("#excluirNota").val(null);
			},buttons: {
				'Cancelar': function() {
					$("#auditoria-nota-devolucao-dialog").dialog('close');
				},
				'Confirmar': function() {
					naoIncluirNotaDevolucao();
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

		$("#info-nota-devolucao").dialog("destroy");
		$("#info-nota-devolucao").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 300,
			width: 500,		
			buttons: {
				'Incluir': function() {
					$("#info-nota-devolucao").dialog('close');
				},				
				'Não incluir': function() {
					$("#info-nota-devolucao").dialog('close');
					$("#auditoria-nota-devolucao-dialog").dialog("open");
				}			
			}
		});	

		$("#autorizarNotas-dialog").dialog("destroy");
		$("#autorizarNotas-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 280,
			width: 550,
			buttons: {
				'Fechar': function() {
					$("#autorizarNotas-dialog").dialog('close');
				},
				'Autorizar': function() {
					liberarNotas();
				}
			}
		});	
		$("#notasTransbordo-dialog").dialog("destroy");
		$("#notasTransbordo-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 700,
			width: 800,
			open: function(event, ui) {
		        $(this).parents(".ui-dialog:first").find(".ui-dialog-titlebar-close").remove();
		    },
			buttons: {
				'Confirmar': function() {
					confirmarTranbordoNotas();
					$("#notasTransbordo-dialog").dialog("close");
					$("#dialogLoading-dialog").dialog("open");
				}
			}
		});	
		$("#autorizarManifesto-dialog").dialog("destroy");
		$("#autorizarManifesto-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 280,
			width: 550,
			buttons: {
				'Fechar': function() {
					$("#autorizarManifesto-dialog").dialog('close');
				},
				'Autorizar': function() {
					liberarManifesto();
				}
			}
		});	
		$("#motivodeExcessao-dialog").dialog("destroy");
		$("#motivodeExcessao-dialog").dialog({
			autoOpen: false,
			modal: true,
			closeOnEscape: false,
			closeText: '',
			height: 280,
			width: 550,
			buttons: {
				'Fechar': function() {
					$("#motivodeExcessao-dialog").dialog('close');
				},
				'Confirmar': function() {
					enviarMotivodeExcessao();
				}
			}
		});	
		//
		
		if (${manifesto.temNotaDevolucao}){
			openDialogInfoNotaDevolucao();
		}
		
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
		quebralinhaPraca();
		quebralinhaCliente();
		showHideBotaoInclusao();
		
		if('${showLogList}' == 'true'){
			$("#log-dialog").dialog("open");
		}

		verificaNotasSemFrete();

	

		// se nota tem transbordo, exibe modal para confirmação de transbordo nas notas.		
		if($("input[name*='temTransbordo']").val() == 'true'){
			$("#notasTransbordo-dialog").dialog("open");
			$("select[name*=depositoTransbordo]").removeAttr("disabled");
			$("input[name*=cdManifestoNotaFiscal]").removeAttr("disabled");
		}		
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

	function naoIncluirNotaDevolucao(){
		if($("#motivoId3").val().trim()==""){
			alert("O campo motivo é obrigatório.");			
		}else{
			var motivo = $("#motivoId3").val();
			var login = $("#loginId3").val();
			var senha = $("#senhaId3").val();
			
			$w.getJSONSync("${ctx}/expedicao/crud/Manifesto",{ACAO:'naoIncluirNotaDevolucao','motivo':motivo,'login':login,'senha':senha},				
				function(json){
					if(json.error!=''){
						$("#loginId3").val(null);
						$("#senhaId3").val(null);
						alert(json.error);
					} else {
						var notasDevolucao = buscaNotasDevolucao();
						var notasReferencia = buscaReferenciaDasNotas(notasDevolucao);
						excluirNotas(notasDevolucao,notasReferencia);
						$("#auditoria-nota-devolucao-dialog").dialog("close");
						alert('Operação realizada com sucesso!');
					}
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
	
	function inserirNotas(notas, nomeAtributo){
		form[nomeAtributo].value = notas;
		$("#dialogLoading-dialog").dialog("open");
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
				form.action = '${ctx}/expedicao/report/Emitirmanifesto?ACAO=gerar&cdmanifesto='+$("#cdmanifestoId").val() 
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
			                                                      			'&auditoriaVO.motivo='+motivo
			
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
	
	function populaInfoColunaTransbordo(){
		var count = 0;

		$('input[name$=notafiscalsaida.numero]').each(function(){
			if($('input[name="listaManifestonotafiscal['+count+'].temDepositoTransbordo"]').val() == "true"){
				$("#isTransbordo"+count).html('Sim');
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
	
	function makeRow(i, log) {
		var row = "<tr class='" + (i % 2 == 0 ? "dataGridBody1" : "dataGridBody2") + "'>";
			row += "<td>" + log.cderro + "</td>";
			row += "<td>" + log.dserro + "</td>";
		row += "</tr>";
		
		return row;		
	}
	
	function salvarForCDAE(){
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
	function quebralinhaPraca(){
		for(var i=0;i<=$("#listaManifestonotafiscalId tr").length;i++){
			var pracaNome = $('input[name="listaManifestonotafiscal['+i+'].notafiscalsaida.praca.nome"]').val();
			var id = "#pracanome"+i
			try{
				$(id).html($w.quebralinhaDetalhe(10,pracaNome));
			}catch(err){
				$(id).html('');
			}
		}
	}
	
	function reindexaRota(){
		$("#listaManifestonotafiscalId tr td span[id*='rotanome']").each(function (i) {
			$(this).attr('id',"rotanome"+i);
			
		});

	}	
	function reindexaCliente(){
		$("#listaManifestonotafiscalId tr td span[id*='clientenome']").each(function (i) {
			$(this).attr('id',"clientenome"+i);
			
		});

	}	

	function reindex(form, removedIndexedProperty){
		if(form==null){
			alert("reindex(): O form fornecido ? null   \n\n@author rogelgarcia");
			return;
		}
		if(removedIndexedProperty==null){
			alert("reindex(): O removedIndexedProperty fornecido ? null   \n\n@author rogelgarcia");
			return;
		} else {
			if(!removedIndexedProperty.match("\\w*\\[\\d*\\]")){
				alert("reindex(): O removedIndexedProperty fornecido ? inv?lido ("+removedIndexedProperty+")\nO formato deve ser propriedade[indice]");
				return;
			}
		}
		var property = removedIndexedProperty.substring(0,removedIndexedProperty.indexOf("["));
		var excludedNumber = extrairNumeroDeIndexedProperty(removedIndexedProperty);
		for(i = 0; i < form.elements.length; i++){
			var element = form.elements[i];
			if(element.name == null) continue;
			
			var elementReducedProperty = element.name;
			var indexBrackets = null;
			var liorp = elementReducedProperty.indexOf("[");
			if(liorp > 0){
				elementReducedProperty = elementReducedProperty.substring(0,liorp);
				indexBrackets = element.name.substring(element.name.indexOf('['), element.name.indexOf(']')+1);
			}
			
			if(elementReducedProperty == property || elementReducedProperty == "_"+property){
				var elementName = elementReducedProperty + indexBrackets;
				//alert('before '+element.name);
				var elementSubproperties = element.name.substring(elementName.length, element.name.length);
				//alert(indexBrackets);
				var open = elementName.indexOf("[");
				var close = elementName.indexOf("]");
				var number = extrairNumeroDeIndexedProperty(elementName);
				//alert(number);
				if(number>excludedNumber){
					number--;
					var reindexedName = elementName.substring(0,open)+"["+number+"]"+ elementSubproperties;
					//alert(element.name + " -> "+reindexedName);
					element.name = reindexedName;
					//alert('after'+element.name);
				}
			}
		}
	}
	
	function quebralinhaCliente(){
		for(var i=0;i<=$("#listaManifestonotafiscalId tr").length;i++){
			var clienteNome = $('input[name="listaManifestonotafiscal['+i+'].notafiscalsaida.cliente.nome"]').val();
			var id = "#clientenome"+i;		
			try{
				$(id).html($w.quebralinhaDetalhe(14,clienteNome));
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
			$("#incluirPackingListId").hide();
		}else if(cdtipoentrega == "4"){
			$("#incluirPackingListId").show();
			$("#incluirNotaId").hide();
			$("#incluirManifestoId").hide();			
		}else{
			$("#incluirManifestoId").show();
			$("#incluirNotaId").hide();
			$("#incluirPackingListId").hide();
		}
		
	}
	
	function incluirManifesto(){
		$w.openPopup("${ctx}/expedicao/crud/Manifesto?ACAO=buscarManifesto&cdmanifesto",1028,605);
	} 

	function incluirPackingList(){
		$w.openPopup("${ctx}/expedicao/crud/Manifesto?ACAO=buscarPackingList&cdmanifesto",1028,605);
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


	function verificaNotasSemFrete(){
		var exibeModal = false;
		
		$("input[name*=existeFreteClienteNota]").each(function () {
			if ($(this).val() == 'false'){
				$(this).parent().parent().css("color", "red");
				exibeModal = true;
			}
			
		});

		if (exibeModal){$("div.messageblock > ul > li.trace").css("font-size", "14px");}

	}

	function openDialogInfoNotaDevolucao(){
		$("#info-nota-devolucao").dialog("open");
	}
	
	function autorizarNotas(){
		$("#autorizarNotas-dialog").dialog("open");
		$("input[name*=senhaAutorizacao]").removeAttr("disabled");
		$("input[name*=cdmanifestonotafiscal]").removeAttr("disabled");
	}

	function liberarNotas(){
		document.forms["formAutorizarNotas"].submit();	
	}	
	function excluirLinhaPorNome(nome, ignoreMessage){ 
		if(!ignoreMessage){
			if (!confirm('Tem certeza que deseja excluir este item?')) {
			   return false;
			}
		}

		var open = nome.lastIndexOf("[");
		var close = nome.lastIndexOf("]");
		var prop = nome.substring(open+1,close);
		var virgula = prop.lastIndexOf(",");
		var table_id = prop.substring(9,virgula);
		var indice = prop.substring(virgula+9, prop.length);
		var i = indice - 1;
        var refdevolucao= "listaManifestonotafiscal["+ i +"].notafiscalsaida.cdnotafiscalsaidareferencia";				
        var devolucao= "listaManifestonotafiscal["+ i +"].notafiscalsaida.cdnotafiscalsaida";

        var codigoDevolucao= $('input[name='+devolucao+']').val();
        var codigoReferente = $('input[name='+refdevolucao+']').val();
		if(codigoReferente > 0){
			excluirNotas([codigoDevolucao], [codigoReferente]);
			return false;
		} else {
			var name = "listaManifestonotafiscal["+ i +"].notafiscalsaida.cdnotafiscalsaida";

			var codigo = $('input[name='+name+']').val()

			var notas = buscaRelacionadas(codigo);
			
			if( notas.length > 0){
				excluirNotas([codigo], notas)
				return false;
			} else {
				removeRow(table_id, indice);
				reindexButtons(table_id, indice);
				reindexaRota();
				reindexaCliente();
			}
		}
		return true;
	}

	function buscaRelacionadas(codigo){
		var ref = [];
		$("#listaManifestonotafiscalId tr td :input:hidden").each(function (){
			if($(this).attr("name").indexOf("cdnotafiscalsaidareferencia") != -1){

				var name = $(this).attr('name');

				var codigoref = $('input[name='+name+']').val();

				if(codigoref == codigo){

					var open_ = name.lastIndexOf("[");
					var close_ = name.lastIndexOf("]");
					var i = name.substring(open_+1,close_);

	                var obj = "listaManifestonotafiscal["+i+"].notafiscalsaida.cdnotafiscalsaida";	

                	ref.push($('input[name='+obj+']').val());	
				}
		    }
		})
		return ref;
	}	
	function buscaNotasDevolucao(){
		var notas = [];
		$('input[name^="excluirNota"]').each(function() {
			var obj = JSON.parse((this).value);
			notas.push(obj[0].toString());
		});	
		return notas;
	}
	function buscaReferenciaDasNotas(notas){
		console.log("buscaReferenciaDasNotas: " + notas)
		var ref = [];
		$("#listaManifestonotafiscalId tr td :input:hidden").each(function (){
			if($(this).attr("name").indexOf("cdnotafiscalsaida") != -1 && $(this).attr("name").indexOf("cdnotafiscalsaidareferencia") == -1 ){

				var name = $(this).attr('name');

				var open_ = name.lastIndexOf("[");
				var close_ = name.lastIndexOf("]");
				var indice = name.substring(open_+1,close_);

				var codigo = $('input[name='+name+']').val();
				
				if( $.inArray(codigo, notas) >= 0 ){
                    var referencia= "listaManifestonotafiscal["+indice+"].notafiscalsaida.cdnotafiscalsaidareferencia";				
                    ref.push($('input[name='+referencia+']').val());	
                }		
		    }
		})
		return ref;
	}
	function excluirNotas(devolucao, referencia){

		LIBERADO = 1;

		var notas = $.merge(devolucao, referencia);
		console.log(notas)

		$("#listaManifestonotafiscalId tr td :input:hidden").each(function (){
			if($(this).attr("name").indexOf("cdnotafiscalsaida") != -1 && $(this).attr("name").indexOf("cdnotafiscalsaidareferencia") == -1 ){

				var name = $(this).attr('name');

				var open_ = name.lastIndexOf("[");
				var close_ = name.lastIndexOf("]");
				var i = name.substring(open_+1,close_);

                var obj= "listaManifestonotafiscal["+i+"].notafiscalsaida.cdnotafiscalsaida";				

				var codigo = $('input[name='+obj+']').val();
				
				if($.inArray(codigo, notas) >= 0){
					var indice = $(this).closest('tr')[0].rowIndex;
					removeRow("listaManifestonotafiscalId", indice);
					reindexButtons("listaManifestonotafiscalId", indice);
					reindexaRota();
					reindexaCliente();
					//console.log("I: "+i)
					indice--;
					reindex(document.form, "listaManifestonotafiscal"+'['+indice+']');
					//quebralinhaRota();
				}
		    }
		})
	}

	function countNotasComReferencia(referencia){
		var count = 0;
		$("#listaManifestonotafiscalId tr td :input:hidden").each(function (){
			if($(this).attr("name").indexOf("cdnotafiscalsaidareferencia") != -1 ){
				var name = $(this).attr('name');

				var codigo = $('input[name='+name+']').val();
				
				if(codigo > 0 && $.inArray(codigo, referencia)){
					count++;
				}
		    }
		})

		return count;
	}

	function removeRow(tableId, rowNumber){
		var table = document.getElementById(tableId);
		table.deleteRow(rowNumber);
		organizarCSS(table);	
	}	

	function reindexFormPorNome(nome, form, indexedProperty, considerHeader){

		if(LIBERADO == 0){
			//alert(nome);
			var open = nome.lastIndexOf("[");
			var close = nome.lastIndexOf("]");
			var prop = nome.substring(open+1,close);
			var virgula = prop.lastIndexOf(",");
			var table_id = prop.substring(9,virgula);
			var indice = prop.substring(virgula+9, prop.length);
			if(considerHeader){
				indice--;
			}
			reindex(form, indexedProperty+'['+indice+']');
		}
		
	}


	function confirmarTranbordoNotas(){
		document.forms["formCriarTransbordo"].submit();	
	}

	 */
</script>