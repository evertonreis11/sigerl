<%@page import="br.com.linkcom.wms.geral.service.DepositoService"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- <%
	List lista = DepositoService.getInstance().findAtivosIndex();
	request.setAttribute("listaDP",lista);
%> --%>

<HTML>
	<HEAD>
		<script language="JavaScript" src="${ctx}/js/jquery.js"></script>	
		<script type="text/javascript" src="${ctx}/js/bootstrap.min.js"></script>
		<script  src="${ctx}/js/jquery-ui.js"></script>
		
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
		
		<link href="${ctx}/css/${tema_wms}/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<link rel="stylesheet"      href="${ctx}/css/${tema_wms}/jquery-ui.css">	
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/base.css" type="text/css">		
		
		
	<%-- 	<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/app-ie.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/theme.css" type="text/css">
		<link rel="StyleSheet"        href="${ctx}/css/${tema_wms}/default.css" type="text/css"> --%>
		 
		
	   <style type="text/css">
			.custom-combobox {
			  position: relative;
			  display: inline-block;
			}
			.custom-combobox-toggle {
			    position: absolute;
			    top: 0;
			    bottom: 0;
			    margin-left: -1px;
			    padding: 0;
		  	}
		  	.custom-combobox-input {
		    	margin: 0;
		    	padding: 5px 10px;
		  	}
		  	.modal-content{
		  		width:80%;
		  	}
		</style>
		
		<TITLE><fmt:message key="aplicacao.titulo"/></TITLE>
	</HEAD>
	
	<BODY leftmargin="0" topmargin="0" rightmargin="0" style="padding:0px; margin:0px; background-color: #F2F5A9" style=" width: 100%;">
			 <div class="container" style=" margin-top: 6%;>
			 	<div class="row">
			 		<div class="col-md-3"></div>
			 		<div class="col-md-6">
				 		<div class="panel panel-primary">
				 			<div class="panel-heading"><h4>Gerencimento de Retirada de Produtos na Loja</h4></div>
				 			
				 			<div class="panel-body">
				 			
				 				<!-- Login Form -->
			            		<form name="loginForm" action="${ctx}/adm/neo_security_manager" method="post">
			            			<div class="row" >
						            	<div class="form-group col-lg-12">
						              		<c:if test="${!empty login_error && empty habilitaModal}">
												<div class="alert alert-danger" id="error">Login / senha inv&aacute;lidos.</div> 
											</c:if>
											<c:if test="${!empty login_error && !empty erro_dep && habilitaModal == 0}">
												<div class="alert alert-danger" id="error">${erro_dep}</div>
											</c:if>
							            </div>
						            </div>
						            
						            <!-- Username Field -->
					               <div class="row">
					                  <div class="form-group col-lg-11">
					                    <label for="username"><h3><span class="text-danger" style="margin-right:5px;padding-left:8%;">*</span>Login:</h3></label>
					                        <div class="input-group" style="padding-left:2%;">
					                            <input class="form-control input-lg" id="username" name="username" placeholder="Login" value="${username}"  required/>
					                            <span class="input-group-btn">
					                                <label class="btn btn-success btn-lg"><span class="glyphicon glyphicon-user" aria-hidden="true"></label>
					                            </span>
					                        </div>
					                    </div>
					                </div>
					                
					                <!-- password Field -->
					                <div class="row">
					                    <div class="form-group col-lg-11">
					                        <label for="password"><h3><span class="text-danger" style="margin-right:5px;padding-left:8%;">*</span>Senha:</h3></label>
					                        <div class="input-group" style="padding-left:2%;">
					                            <input class="form-control input-lg" id="password" type="password" name="password" placeholder="Senha" value="${password}" required/>
					                            <span class="input-group-btn">
					                                <label class="btn btn-success btn-lg"><span class="glyphicon glyphicon-lock" aria-hidden="true"></label>
					                            </span>
					                        </div>
					                    </div>
					                </div>
					                
					                <!-- Login Button -->
					                <div class="row">
					                   <div class="col-lg-4"></div>
					                   <div class="form-group col-lg-8" >
					                        <button class="btn btn-success btn-lg" type="submit" id="buttonSubmit">Entrar</button>
					                    </div> 
					                </div>
					                
						           <!-- Modal de depósito -->
								   <button id="buttonModal" style="display: none;" type="button" class="btn" data-toggle="modal" data-target="#modalDeposito"></button>
								   <input type="hidden" id="habilitaModal" name="habilitaModal" value="${habilitaModal}"/>
								   
								   <div class="modal fade" id="modalDeposito">
									    <div class="modal-dialog ">
									      <div class="modal-content">
									        
									        <div class="modal-header">
									          <button type="button" class="close" data-dismiss="modal">&times;</button>
									          <h4 class="modal-title">Selecionar depósito</h4>
									        </div>
									        
									        <div class="modal-body">
									        	<c:if test="${!empty erro_dep}">
									        		<div class="alert alert-danger">
									        			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
									    				<c:out value="${erro_dep}" />  	
									  				</div>
												</c:if>
									           <label for="deposito">Selecione o depósito:</label>
								                <select name="deposito" id="deposito" class="form-control required">
								                   <option value="<null>"></option>
								                   <c:forEach items="${listaDP}" var="dep">
								                     <option value="${dep.cddeposito}">${dep.nome}</option>
								                   </c:forEach>
								                </select>
									        </div>
									        
									        <div class="modal-footer">
									          <button type="button" class="btn btn-default" data-dismiss="modal">Enviar</button>
									        </div>
									        
									      </div>
									    </div>
									</div> 
									
									<!-- Modal de troca de senha -->
									<button id="buttonTrocaSenha" style="display: none;" type="button" class="btn" data-toggle="modal" data-target="#modalTrocaSenha"></button>
									<input type="hidden" id="trocaSenha" name="trocaSenha" value="${trocaSenha}"/>
									
									<div class="modal fade" id="modalTrocaSenha" role="dialog">
									    <div class="modal-dialog">
									      <div class="modal-content">
									        <div class="modal-header">
									          <button type="button" class="close" data-dismiss="modal">&times;</button>
									          <h4 class="modal-title">Será necessário efetuar a troca de senha para esse usuário</h4>
									        </div>
									        <div class="modal-body">
									        	
									        	<div class="alert alert-danger" style="display: none;" id="alertSenhaDiferente">
							    				   A nova senha e a confirmação devem ser exatamente iguais.   	
							  				    </div>
							  				    
							  				    <div class="alert alert-danger" style="display: none;" id="alertCampoObrigatorio">
							    				 	O preenchimento dos campos é obrigatório.  	
							  				    </div>
							  				    
							  				    <div class="alert alert-danger" style="display: none;" id="alertSenhaIgual">
							    				 	A nova senha não pode ser igual a senha antiga. 	
							  				    </div>
									           
									            <label class="control-label col-sm-2">Login:</label>
											    <div>
											      <label class="control-label">${username}</label>
											    </div>
											    
									           	<br />
									           					
												<label class="control-label" for="novaSenha">Nova Senha:</label>
												<input class="form-control required" name="novaSenha"  id="novaSenha" type="password" placeholder="Digite a nova Senha" onkeypress="$w.permiteNumeros(event);" maxlength="15"/>
												<br>
											    <label class="control-label" for="confirmarSenha" >Confirmar Senha:</label>
												<input class="form-control required" id="confirmarSenha" type="password" placeholder="Confirme a Senha" onkeypress="$w.permiteNumeros(event);" maxlength="15"/>
												<input type="hidden" id="depositoTrocaSenha" name="depositoTrocaSenha" value="${depositoTrocaSenha}"/>
											      
									        </div>
									        <div class="modal-footer">
									          <button type="button" class="btn btn-default" data-dismiss="modal" >Enviar</button>
									        </div>
									      </div>
									    </div>
									  </div>
						            
			            		</form>
				                
				 			</div>
				 		</div>
			 		</div>
			 	</div>
			</div>
	</BODY>
</HTML>


<script type="text/javascript">
	
	$(document).ready(function(){
		// exibe modal de seleção de deposito.
		if($('#habilitaModal').val()  == 1){
			$('#buttonModal').click();	
		}else{
			$('#username').focus();
		}
		
		// exibe modal de troca de senha.
		if($('#trocaSenha').val() == 1){
			$('#buttonTrocaSenha').click();	
		}
	});

	
	// executa função após fechar modal de seleção de deposito
	$('#modalDeposito').on('hidden.bs.modal', function(){
    	$('#buttonSubmit').click();     
	}) ; 

 	// autocomplete combo box deposito.
 	// code: https://jqueryui.com/autocomplete/#combobox
 	(function( $ ) {
 	    $.widget( "custom.combobox", {
 	      _create: function() {
 	        this.wrapper = $( "<span>" )
 	          .addClass( "custom-combobox" )
 	          .insertAfter( this.element );
 	 
 	        this.element.hide();
 	        this._createAutocomplete();
 	        this._createShowAllButton();
 	      },
 	 
 	      _createAutocomplete: function() {
 	        var selected = this.element.children( ":selected" ),
 	          value = selected.val() ? selected.text() : "";
 	 
 	        this.input = $( "<input>" )
 	          .appendTo( this.wrapper )
 	          .val( value )
 	          .attr( "title", "" )
 	          .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
 	          .autocomplete({
 	            delay: 0,
 	            minLength: 0,
 	            source: $.proxy( this, "_source" ),
 	           	appendTo: modalDeposito
 	          })
 	          .tooltip({
 	            tooltipClass: "ui-state-highlight"
 	          });
 	 
 	        this._on( this.input, {
 	          autocompleteselect: function( event, ui ) {
 	            ui.item.option.selected = true;
 	            this._trigger( "select", event, {
 	              item: ui.item.option
 	            });
 	          },
 	 
 	          autocompletechange: "_removeIfInvalid"
 	        });
 	      },
 	 
 	      _createShowAllButton: function() {
 	        var input = this.input,
 	          wasOpen = false;
 	 
 	        $( "<a>" )
 	          .attr( "tabIndex", -1 )
 	          .attr( "title", "Mostrar todos os itens" )
 	          .tooltip()
 	          .appendTo( this.wrapper )
 	          .button({
 	            icons: {
 	              primary: "ui-icon-triangle-1-s"
 	            },
 	            text: false
 	          })
 	          .removeClass( "ui-corner-all" )
 	          .addClass( "custom-combobox-toggle ui-corner-right" )
 	          .mousedown(function() {
 	            wasOpen = input.autocomplete( "widget" ).is( ":visible" );
 	          })
 	          .click(function() {
 	            input.focus();
 	 
 	            // Close if already visible
 	            if ( wasOpen ) {
 	              return;
 	            }
 	 
 	            // Pass empty string as value to search for, displaying all results
 	            input.autocomplete( "search", "" );
 	          });
 	      },
 	 
 	      _source: function( request, response ) {
 	        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
 	        response( this.element.children( "option" ).map(function() {
 	          var text = $( this ).text();
 	          if ( this.value && ( !request.term || matcher.test(text) ) )
 	            return {
 	              label: text,
 	              value: text,
 	              option: this
 	            };
 	        }) );
 	      },
 	 
 	      _removeIfInvalid: function( event, ui ) {
 	 
 	        // Selected an item, nothing to do
 	        if ( ui.item ) {
 	          return;
 	        }
 	 
 	        // Search for a match (case-insensitive)
 	        var value = this.input.val(),
 	          valueLowerCase = value.toLowerCase(),
 	          valid = false;
 	        this.element.children( "option" ).each(function() {
 	          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
 	            this.selected = valid = true;
 	            return false;
 	          }
 	        });
 	 
 	        // Found a match, nothing to do
 	        if ( valid ) {
 	          return;
 	        }
 	 
 	        // Remove invalid value
 	        this.input
 	          .val( "" )
 	          .attr( "title", value + " não é um valor encontrado na lista." )
 	          .tooltip( "open" );
	          
 	       this.element.children( "option" ).each(function() {
  	          if ( $( this ).value() == '<null>' ) {
  	            this.selected = valid = true;
  	            return false;
  	          }
  	        });
 	        
 	        this._delay(function() {
 	          this.input.tooltip( "close" ).attr( "title", "" );
 	        }, 2500 );
 	        this.input.autocomplete( "instance" ).term = "";
 	      },
 	 
 	      _destroy: function() {
 	        this.wrapper.remove();
 	        this.element.show();
 	      }
 	    });
 	  })( jQuery );

 	 $(function() {
 	    $("#deposito").combobox();
 	  });
 	  
 	 // executa função antes de fechar modal de seleção de deposito
	$("#modalTrocaSenha").on('hide.bs.modal', function(){
		var novaSenha = $('#novaSenha').val();
		var confirmarSenha = $('#confirmarSenha').val();
		var senha = $('input[name=password]').val();

		/* realiza validações de igualdade de senhas antiga e nova, 
		   se a senha nova e a confirmação são iguais
		   e se os campos estão preenchidos.
		*/
		if (novaSenha == '' || confirmarSenha == ''){
			$('#alertCampoObrigatorio').show();
			$('#alertSenhaDiferente').hide();
			$('#alertSenhaIgual').hide();
			return false;
		}else if(senha == novaSenha){
			$('#alertCampoObrigatorio').hide();
			$('#alertSenhaDiferente').hide();
			$('#alertSenhaIgual').show();
			return false;
		}else if(novaSenha != confirmarSenha){
			$('#alertCampoObrigatorio').hide();
			$('#alertSenhaDiferente').show();
			$('#alertSenhaIgual').hide();
			return false;	
		}else{
			$('#alertCampoObrigatorio').hide();
			$('#alertSenhaDiferente').hide();
			$('#alertSenhaIgual').hide();
			//openDialog();
    		$('#buttonSubmit').click();     
		}

	}) ;
	
	
</script>
