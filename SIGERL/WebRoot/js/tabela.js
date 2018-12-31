function DataGridUtil (){
	var selectedIndex = null;
} 

DataGridUtil.prototype.changeCheckState = function(id){
	if(!id)	id = "tabelaResultados";
		
	var check = $("#selectAll").is(":checked");
	
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
		if(confirm("Voc� deseja excluir os itens selecionados?"))
			return true;
		else
			return false;
	}
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