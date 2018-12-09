<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demo</title>

<!-- In head section we should include the style sheet for the grid -->
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/css/temas/grid.css" />

<!-- Of course we should load the jquery library -->
<script src="${ctx}/js/jqGrid/jquery.js" type="text/javascript"></script>

<!-- and at end the jqGrid Java Script file -->
<script src="${ctx}/js/jqGrid/jquery.jqGrid.js" type="text/javascript"></script>

<script type="text/javascript">
// We use a document ready jquery function.
jQuery(document).ready(function(){
jQuery("#list2").jqGrid({
    url:'teste.jsp?nd='+new Date().getTime(),
    datatype: "json",
    colNames:['Data', 'Cliente', 'Amount','Tax','Total','Notes'],
    colModel:[
        {name:'invdate',index:'invdate', width:90},
    	{name:'name',index:'name asc, invdate', width:100},
        {name:'amount',index:'amount', width:80, align:"right"},
        {name:'tax',index:'tax', width:80, align:"right"},		
        {name:'total',index:'total', width:80,align:"right"},		
        {name:'note',index:'note', width:150, sortable:false}		
    ],
    pager: jQuery('#pager2'),
    imgpath: ${ctx}'/js/jqGrid',
    sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    loadonce:true
});
});

//adicionar
function add(){
	var dtm = {"id":"16","cell":["2007-10-06","Client 3","1000.00","0.00","1000.00",null]};
	jQuery("#list2").addRowData($("#pos").val(),dtm);
}
//adicionar
function remover(){
	jQuery("#list2").delRowData($("#pos").val());
}
function get(){
	var id = jQuery("#list2").getSelectedRow(); 
	if (id) { 
		var ret = jQuery("#list2").getRowData(id);
		alert("id="+ret.id+" invdate="+ret.invdate+"..."); 
	} else { alert("Please select row");}
}
</script>
</head>
<body>
<!-- the grid definition in html is a table tag with class 'scroll' -->
<table id="list2" class="scroll" cellpadding="0" cellspacing="0"></table>

<!-- pager definition. class scroll tels that we want to use the same theme as grid -->
<div id="pager2" class="scroll" style="text-align:center;"></div>
<input id="pos"/>
<a href="#" onclick="add()">Adicionar</a> | 
<a href="#" onclick="remover()">Remover</a> | 
<a href="#" onclick="get()">Capturar</a>
</body>
</html>