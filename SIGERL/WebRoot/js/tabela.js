function DataGridUtil (){
	var selectedIndex = null;
} 

/**
 * Acessa o link que tem a classe activation
 */
DataGridUtil.prototype.editarRegistro = function (obj){
	var acv = $(".activation",obj);
	if(acv.size() > 0)
		window.location = $(".activation",obj).attr("href");
}

/**
 * Colore a linha que está o mouse no DG
 */
DataGridUtil.prototype.coloreLinha = function (tabelaId,elementoSelecionado){
	var tabela = document.getElementById(tabelaId);
	var cellRows = tabela.rows;
	
	for(i = 0; i< cellRows.length ; i++){
		cellRows[i].style.backgroundColor = "";
	}
	
	elementoSelecionado.style.backgroundColor = "#ffff80";
	this.selectedIndex = elementoSelecionado.rowIndex;
}

/**
 * Controla o evento de entrada
 */
DataGridUtil.prototype.mouseonOverTabela = function (tabelaId,elementoSelecionado){
	if(elementoSelecionado.rowIndex != this.selectedIndex)
		elementoSelecionado.style.backgroundColor = '#ffffb3';
}

/**
 * Controla o evento de saída
 */
DataGridUtil.prototype.mouseonOutTabela = function (tabelaId,elementoSelecionado){
	if(elementoSelecionado.rowIndex != this.selectedIndex)
		elementoSelecionado.style.backgroundColor = '';
}

DataGridUtil.prototype.changeCheckState = function(id){
	if(!id)	id = "tabelaResultados";
		
	var check = $("#selectAll").attr("checked");
	
	$("#"+id+" input[type=checkbox][name=selecteditens]").each(function(){
		if(check) $(this).attr("checked",check);
		else $(this).removeAttr("checked");
	});
}

DataGridUtil.prototype.getSelectedValues = function(id){
	if(!id)	id = "tabelaResultados";
	var selectedValues = "";
	$("#"+id+" input[name=selecteditens]:checked").each(function(){
		selectedValues += $(this).val()+",";
	});
	if(selectedValues != ""){
		selectedValues = selectedValues.substr(0,(selectedValues.length -1));
	}
	return selectedValues;
}

DataGridUtil.prototype.validateSelectedValues = function(){
	if(this.getSelectedValues() == ""){
		alert("Nenhum item foi selecionado.");
		return false;
	} else {
		if(confirm("Você deseja excluir os itens selecionados?"))
			return true;
		else
			return false;
	}
}

DataGridUtil.prototype.changeCheckStateagendamentoparcial = function(id) {	
	if(!id)
		id = 'dataGrid';
	
	var check = $("#" + id + " thead tr th").find("input[type=checkbox][name=selectAll]").attr("checked");
	
	$("#"+id+" input[type=checkbox][id=check]").each(function(){
		if(check) {
			$(this).attr("checked", check);
			$(this).parent().parent().find("input[type=text]").removeAttr("disabled");
			$(this).parent().parent().find("input[type=text]").attr("style","text-align: right;");
		
		} else {
			$(this).removeAttr("checked");
			$(this).parent().parent().find("input[type=text]").attr("disabled","disabled");
			$(this).parent().parent().find("input[type=text]").attr("style","background-color: #dadada; text-align: right;");
			
		}
	});
}

DataGridUtil.prototype.getArraySelectedValues = function(id){
	if(!id)	id = "tabelaResultados";
	var selectedValues = new Array();
	$("#"+id+" input[name=selecteditens]:checked").each(function(){
		selectedValues.push(this.value);
	});
	return selectedValues;
}

var $dg = new DataGridUtil();