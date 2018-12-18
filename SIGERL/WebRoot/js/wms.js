function WmsUtil(){}

/*
 * Fun��o que permite digitar apenas n�meros
 */
WmsUtil.prototype.permiteNumeros = function(e) {
	// IE
	if(window.event) {
		var tecla = window.event.keyCode;
		tecla = String.fromCharCode(tecla);
						  
		if(!((tecla >= "0") && (tecla <= "9"))) {
			window.event.keyCode = 0;
		}
	
	// Mozilla
	} else if(e.which) {
		var teclaBkspace = -1;
		var tecla = e.which;
		
		if(tecla == 8)	{
			teclaBkspace = tecla;				
		}

		tecla = String.fromCharCode(tecla);
		if((teclaBkspace != 8) && !((tecla >= "0") && (tecla <= "9"))) {				
			e.preventDefault();
		}
	}
} 

/**
* Fun��o respons�vel por remover letras da string.
**/
WmsUtil.prototype.removeCaracteres = function (str){
	str = new String(str);
	var chrPrt = "0";
	var strRet = "";
	var j = 0;
	for (var i = 0; i < str.length; i++) {
		chrPrt = str.substring(i, i + 1);
		if (chrPrt.match(/\d/)) {
			if (j == 0) {
				strRet = chrPrt;
				j = 1;
			} else {
				strRet = strRet.concat(chrPrt);
			}
		}
	}
	return strRet;
}

/**
 * Mostra a janela de avisos
 */
WmsUtil.prototype.showNoticeMessage = function (texto,focus){
	$("#dynamic_messages").empty().html("<div class='messageblock' id='messageBlock'><ul><li class='info'>"+texto+"</li></ul></div>").show();
	if(focus) this.getElement(focus).focus();
}

/**
 * Esconde a janela de avisos
 */
WmsUtil.prototype.hideNoticeMessage = function (){
	$("#dynamic_messages").html("").hide();
}

/**
 * Mostra a janela de alert
 */
WmsUtil.prototype.showAlertMessage = function (texto,timer,focus){
	$("#dynamic_messages").empty().html("<table align='center' width='100%'><tr><td align='center'><br><div class='messageblock' id='messageBlock'><ul><li class='error'>"+texto+"</li></ul></div><br></td></tr></table>").show();
	if(focus) this.getElement(focus).focus();
	if(timer) setTimeout("$w.hideAlertMessage()",timer);
}

/**
 * Esconde a janela de alert
 */
WmsUtil.prototype.hideAlertMessage = function (){
	$("#dynamic_messages").html("").hide();
}

/**
 * Fun��o respons�vel por verificar os e-mails
 */
WmsUtil.prototype.checkMail = function (mail){	
    var er = new RegExp(/^[A-Za-z0-9_\-\.]+@[A-Za-z0-9_\-\.]{2,}\.[A-Za-z0-9]{2,}(\.[A-Za-z0-9])?/);
    if(typeof(mail) == "string"){
    	if(mail=="") return true;
        if(er.test(mail)){ return true; }
    } else if(typeof(mail) == "object"){
    	if(mail.value=="") return true;
        if(er.test(mail.value)){ return true; }
	}	    
	return false;
}

/**
 * Retorna o objeto a partir de um id
 */
WmsUtil.prototype.getElement = function (field){
	return document.getElementById(field);
}

/**
 * Retorna o valor do objeto a partir de um id
 */
WmsUtil.prototype.getElementValue = function (field){
	return this.getElement(field).value;
}

/**
 * Retira a sele��o de um componete select ao pressionar a tecla DELETE.
 */
WmsUtil.prototype.limpaCombo = function (evento,select){
    tecla = evento.keyCode;
    if(tecla == 0) tecla = evento.charCode;	    
    if (tecla == 46) {
    	select.selectedIndex = null;
    	select.onchange();
    }
}

/**
 * Verifica se a string � um n�mero
 */
WmsUtil.prototype.isNumber = function (numExp){
	if (numExp != ""){
		if (isNaN(numExp) || (numExp.length == 0)){
			return false;
		}  
	} 
	return true;
}

var ca

/**
 * Verifica se a string � composta apenas de letras
 */
WmsUtil.prototype.isString = function (stringExp){
	var re = new RegExp;
	re = /^(([a-zA-Z�-� -])+)$/;
	if (stringExp != ""){
	  var arr = re.exec(stringExp);
	  if (arr == null){
	  	return false; 
	  }
	}
	return true;
}

/**
 * Verifica se a string � composta por n�meros, espa�o e h�fen
 */
WmsUtil.prototype.isStringNumber = function (stringExp){
	var er = new RegExp(/([^0-9\- ]+)/);
	if (stringExp != ""){
	  if(er.test(stringExp)){ return false; }
	}return true;
}

/**
 * Esconde a mensagem de carregando
 */
WmsUtil.prototype.hideLoading = function(){
	$("#loadmsg").fadeOut();
}

/**
 * Mostra a mensagem de carregando
 */
WmsUtil.prototype.showLoading = function(){
	$("#loadmsg").fadeIn();
}

/**
 * Mostra a mensagem de carregando
 */
WmsUtil.prototype.clearForm = function(name){
	$("form[name="+name+"] input").each(function(){
		var el = $(this);
		var type = el.attr("type");
		var indexOf = el.attr("name").indexOf("_label");
		if((type == "text")||(type == "date")) {
			if(type == "text" && indexOf > 0 )
				$("form[name="+name+"] input[name="+el.attr("name").substring(0,indexOf)+"]").val("<null>");
			el.val("");
		}
	})
	
	$("form[name="+name+"] select").each(function(){
		var el = $(this);
		el.selectOptions('<null>');		
	});
}

/**
* retira os espa�os em branco da esquerda e da direita
*/ 
WmsUtil.prototype.trim = function(s){
	if(!s)
		return '';
	 
	while(s.substring(0, 1) == ' ') {
    	s = s.substring(1, s.length);	    	
  	}
  	
  	while(s.substring(s.length - 1, s.length) == ' ') {
    	s = s.substring(0, s.length - 1);	    	
  	}
  	
  	return s;
}
	
/**
* Faz a valida��o do c�digo de barras
*/ 
WmsUtil.prototype.validaCodigoDeBarras = function (elemento){
	var numero = trim(elemento.value);
	var numlen = numero.length;
	$w.validaTipo(numlen,elemento);
	if ((numlen != 13) && (numlen != 14)){
		alert("O n�mero deve possuir 13 (EAN/UCC-13) ou 14 (DUN-14) d�gitos");
		return;
	}
	fator = 3;
	soma = 0;
	for(index = numlen-1; index > 0; --index){
		soma = soma + numero.substring (index-1, index) * fator;
    	fator = 4 - fator;
  	}
	var dv = ((1000 - soma) % 10);
	if((numero.substring(numlen-1,numlen)) != dv){
		alert("C�digo de barras inv�lido.");
	}
	$(elemento).val(numero);
}

//Valida se o codigo de barras � ean ou dun
WmsUtil.prototype.validaTipo = function(numlen,elemento){
	var posicao = $w.getPosicao(elemento);
	if(numlen == 13){
		$("input[name='listaProdutoEan\[" + posicao + "\].tipo']").val("EAN");
	}
	else{
		if(numlen == 14){
			$("input[name='listaProdutoEan\[" + posicao + "\].tipo']").val("DUN");
		}
		else{
			$("input[name='listaProdutoEan\[" + posicao + "\].tipo']").val("");
		}
	}
}
	
//Retorna a linha em que o elemento se encontra
WmsUtil.prototype.getPosicao = function(elemento){
	var nome = elemento.name;
	return nome.substring(nome.indexOf("[")+1,nome.indexOf("]"));
}

/**
 * Abre um popup
 */
WmsUtil.prototype.openPopup = function(url,width,height){
	var opcao = ("width="+width+",height="+height+",toolbar=no, location=no,left=0,top=0,directories=no, status=no, menubar=no ,scrollbars=yes, resizable=no, fullscreen=no");
		
	newWindow = window.open(url,"WMS",opcao);
	
	if(newWindow) return false;
}

/**
 * Cria uma requisi��o ajax retornando json
 */
 WmsUtil.prototype.getJSON = function(url,data,callback){ 		
	 $.ajax({
			type: "POST",
			url: url,
			data: data,
			contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			success: function(data){
					if (data != ""){
						data = eval("(" +data + ")");
						callback(data);
					}
				}
		});
}
 
/**
 * Cria uma requisi��o ajax retornando json
 */
 WmsUtil.prototype.getJSONSync = function(url,data,callback){ 		
	 $.ajax({
			type: "POST",
			url: url,
			data: data,
			async: false,
			contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			success: function(data){
					if (data != ""){
						// comentado por incompatibilidade com a novo modelo
						// Everton Reis - 17/12/2018
						//data = eval("(" +data + ")");
						callback(data);
					}
				}
		});
}
 
WmsUtil.prototype.validaHora = function (campo){
	var hour = campo.value;
	situacao = 1;
	hora = (hour.substring(0,2)); 
	minutos = (hour.substring(3,5)); 
	ponto = (hour.substring(2,3));
	
	var retorno = false;
	if(hora>=24) {
		situacao = 0;
		if(minutos>=60) {
			situacao = 0;
			if(ponto != ':') {
				situacao = 0;
				if(situacao==0) {
					//alert('Hora inv�lida! Exemplo de hora v�lida: 09:30');
					hour.value='';
					retorno = false;
				}
				retorno = true;
			}
		}
	} 
	
	if(hora >= 24 || minutos >= 60) {
		situacao = 0;
	}
	
	if(!retorno && situacao == 0){
		alert("Hora inv�lida.")
		setTimeout(function(){campo.focus()},50);
		campo.value = '';
		return false;
	}
	return true;
}

/*
 * Valida se � uma data v�lida
 */
WmsUtil.prototype.validaData = function (campo){
	var data = campo.value;
	var situacao = '';
	var incomplete = '';
	if (data.length == 0) {
		return true;
	}

	if (data.length != 10) {
		incomplete = 'true';
	}
	else {
		mes = (data.substring(3,5));
	
		// verifica se o mes e valido
		if (mes < 1 || mes > 12 ) {
			situacao = 'falsa';
		}
		else {
			dia = (data.substring(0,2));

			// Verifica se o dia � v�lido para cada m�s, exceto fevereiro.
			if (dia < 1 || dia > 31 || (dia > 30 && (mes == 4 || mes == 6 || mes == 9 || mes == 11))) {
				situacao = 'falsa';
			}
			
			ano = (data.substring(6,10));
			// Verifica se o dia � v�lido para o m�s de fevereiro.
			if (mes == 2 && (dia < 1 || dia > 29 || (dia > 28 && (ano%4 != 0)))) {
				situacao = 'falsa';
			}
		}
	}

	if(incomplete == 'true' || situacao == 'falsa'){
		alert("Data inv�lida.")
		setTimeout(function(){campo.focus()},50);
		campo.value = '';
		return false;
	}
	return true;
}

/*
 * Caso a data 2 seja maior que a data 1, retorna 1;
 * Caso a data 2 seja menor que a data 1, retorna -1;
 * Caso sejam iguais, retorna 0;
 * Caso ou a data 1 ou a data 2 seja vazia, retorna 2;
 */
WmsUtil.prototype.comparaData = function (data1,data2) {	
	if(data1 == "" || data2 == ""){
		return 2;
	}
	
	if(data1 == data2){
		return 0;
	}
	var splitDt2 = data2.split( "/" );
	var splitDt1 = data1.split( "/" );
	if ( parseInt(splitDt2[2].toString() + splitDt2[1].toString() + splitDt2[0].toString() ) 
			> parseInt( splitDt1[2].toString() + splitDt1[1].toString() + splitDt1[0].toString() ) ){
		//maior	
		return 1;
	} else {
		//menor
		return -1;
	}
}

/*
 * Formata a data do formato aaaa-mm-dd para dd/mm/aaaa
 */
WmsUtil.prototype.formatarData = function (data) {	
	if(data == ""){
		return data;
	}
	
	var splitDt = data.split( "-" );
	return splitDt[2] + "/"+ splitDt[1] + "/" + splitDt[0];
}

/* Retira zeros a esquerda */
WmsUtil.prototype.retirarZeroEsquerda = function(sStr) {
	 for(var i=0; i<sStr.length; i++) {
	      if(sStr.charAt(i) != '0') {
	         return sStr.substring(i);
	      } 
	   }
	   
	   return sStr;
}

/*
 * Caso a hora 2 seja maior que a hora 1, retorna 1;
 * Caso a hora 2 seja menor que a hora 1, retorna -1;
 * Caso sejam iguais, retorna 0;
 * Caso ou a hora 1 ou a hora 2 seja vazia, retorna 2;
 */
WmsUtil.prototype.comparaHora = function (hora1, hora2) {
	if(hora1 == "" || hora2 == "")
		return 2
		
	if(hora1 == hora2)
		return 0;
	
	var splitHr1 = hora1.split(":");
	var splitHr2 = hora2.split(":");
	
	splitHr1[0] = this.retirarZeroEsquerda(splitHr1[0]);
	splitHr2[0] = this.retirarZeroEsquerda(splitHr2[0])
	
	if(parseInt(splitHr2[0].toString() + splitHr2[1].toString()) > 
	   parseInt(splitHr1[0].toString() + splitHr1[1].toString())) {
	    //maior	
		return 1;
		
	} else {
		//menor
		return -1;
	}
}

/*Elimina os zeros contidos � esquerda de um n�mero*/
WmsUtil.prototype.eliminaZerosEsquerda = function (numero){
	
	if($(numero).val() != ''){
		var aux = $(numero).val();
		while(parseInt(aux.substring(0,1)) == 0){
			aux = aux.substring(1,aux.length);			
		}
		$(numero).val(aux);
	}
}
/*
* Permite que seja selecionado somente um chekbox entre todos os
* que terminem com o nome indicado
*/
WmsUtil.prototype.selectOnlyOne = function (elemento,stringFinalDoNome){
		$("input[name$=\'"+stringFinalDoNome+"\']").each(function(){
			if($(this).attr("checked") && this.name != elemento.name){
				$(this).removeAttr("checked","checked");
				return;
			}
		});
}

/* Metodo que retorna o value de um select */
WmsUtil.prototype.recuperarValueSelect = function(obj) {
	var valueSel = null;
	
	valueSel = (typeof obj == 'object') ? $(obj).val() : obj;
	value = valueSel.substring(valueSel.indexOf("=") + 1, valueSel.indexOf("]"));

	return value;	
}


/* Metodo que converte uma string em um valor int */
WmsUtil.prototype.converterNumeroInt = function(elem) {
	if((elem == null) || (elem == "")) {
		return parseInt(0);
	}
	
	if(isNaN(elem))
		return parseInt(0);
	
	return parseInt(elem);
}

var debugActive = true;
var debugTextAreaId = 'debugTextArea';
// function to show messages to the users
WmsUtil.prototype.debug = function(message, showMessage) {	
	try {
		if(typeof message != 'string')
			message = String(message);

		if(typeof message != 'string') {
			if(debugActive)
				alert('The parameter "message" isn\'t a string!');
			return false;
		}

		if((showMessage == undefined) || (showMessage == null) || (typeof showMessage != 'boolean'))
			showMessage = false;

		if(debugActive || showMessage) 	{
			var textArea = document.getElementById(debugTextAreaId);
			if(textArea != null) {
				textArea.value += message + '\n';
			} else {
				alert(message);
			}
		}
		
		return true;
	}
	
	catch (e) {
		if(debugActive)
			alert('Line: ' + e.lineNumber + ' Desc: ' + e.description + ' : ' + message + ' : ' + showMessage);
	
		return false;
	}
}

/**
 * Obt�m o id do valor selecionado no combo
 */
WmsUtil.prototype.getComboIdSelected = function(combo){
	var value = combo.options[combo.selectedIndex].value;
	if(value != "<null>")
		return this.getValueId(value); 
	else
		return value;
}

WmsUtil.prototype.getValueId = function(value){
	return value.substring(value.lastIndexOf("=")+1,value.lastIndexOf("]"));
}

WmsUtil.prototype.getAutocompleteId = function(value){
	var inicio = value.indexOf('=')+1;
	var fim = value.indexOf(',',inicio);
	return value.substring(inicio,fim);
}

WmsUtil.prototype.getAutocompleteAttribute = function(value,attr){
	var inicio = value.indexOf(attr+'=')+attr.length+1;
	var fim = -1;
	if(value.indexOf(',',inicio) != -1){
		fim = value.indexOf(',',inicio)
	}else if(value.indexOf(']',inicio) != -1){
		fim = value.indexOf(']',inicio);
	}
	return value.substring(inicio,fim);
}

WmsUtil.prototype.quebralinhaDetalhe = function quebralinhaDetalhe(maxLenght,value){
	for(var c=1;c<value.length;c++){
		if(c%maxLenght==0){
			value = value.slice(0,c)+'<br>'+value.slice(c,value.length);
		}
	}
	return value;
}

/*
	Remove escape da string 
*/
WmsUtil.prototype.removeScape = function(description){
	var string = '';
	for(var i = 0; i < description.length; i++){  
		if(description.charAt(i) == '/')
			if(description.charAt(i-1) != '/')
				continue;
		
		string += description.charAt(i);
	}
	return string;
}

/**
*	Valida periodo de datas.
*	Verifica se a data inicial � menor que a data final.
*/
WmsUtil.prototype.periodoValido = function(datainicio,datafim){
	if( datainicio != "" && datafim != ""){
		if(parseInt( datainicio.split( "/" )[2].toString() + datainicio.split( "/" )[1].toString() + datainicio.split( "/" )[0].toString() ) 
		> parseInt( datafim.split( "/" )[2].toString() + datafim.split( "/" )[1].toString() + datafim.split( "/" )[0].toString() ))
		{
			return false;
		}
	}
	return true;
}

WmsUtil.prototype.toFloat = function (valor) {
	if(typeof valor == "string"){
		valor = valor.replace(".","","g");
		valor = valor.replace(",",".","g");
		var floatNum = eval(valor);
		return floatNum;
	}
	if(typeof valor == "object"){
		var floatNum = valor.value;
		var floatNum = parseFloat(value);
		return floatNum;
	}
	return parseFloat(valor);
}

/*********************** MASCARA FLOAT *****************************/	
	/* Metodo que trata a tecla backspace no IE */
	WmsUtil.prototype.backspace = function(elem, settings, e) {	
		var  isIE = (document.all) ? true : false;
		
		if(isIE) {
			var input = $(elem);

			e = e || window.event;
			
			var k = e.charCode || e.keyCode || e.which;
			
			if (k == 8) {
				$w.preventDefault(e);
				var x = input.val().substring(0, input.val().length);
				input.val($w.maskValue(x, settings));
				
				return false;
			}

			var key = String.fromCharCode(k);  // Valor para o c�digo da Chave
			input.val($w.maskValue(input.val() + key, settings));
		}		
	}	
	
	// m�scara para formatar campo float
	WmsUtil.prototype.maskFloat = function(elem, settings, e) {		
		return $(elem).each(function(){
			var input = $(this);			
			$w.formatMask(input, settings, e);
			
		});
	}
	
	/* Metodo que verifica qual o evento que foi acionado e qual tecla que foi precionada para aplicar 
	 * a m�scara de float
	 */
	WmsUtil.prototype.formatMask = function(input, settings, e) {
		e = e || window.event;
		
		var k = e.charCode || e.keyCode || e.which;
		
		if (k == 8) {
			$w.preventDefault(e);
			var x = input.val().substring(0, input.val().length - 1);
			input.val($w.maskValue(x, settings));
			return false;
		}
		
		if((k < 48 || k > 57)){
			$w.preventDefault(e);
			return true;
		}
		
		var key = String.fromCharCode(k);  // Valor para o c�digo da Chave
		$w.preventDefault(e);
		input.val($w.maskValue(input.val() + key, settings));
	}
	
	/* M�todo que cancela a propagacao de eventos */
	WmsUtil.prototype.preventDefault = function(e) {
		if (e.preventDefault) { //standart browsers
			e.preventDefault()

		} else { // internet explorer
			e.returnValue = false
			
		}
	}
	
	/* Metodo que implementa a formatacao de mascara no campo input */
	WmsUtil.prototype.maskValue = function(v, settings) {
		var a = '';
		var strCheck = '0123456789';
		var len = v.length;
		var t = "";
		
		if (len == 0) {
			t = "0,000";
		}
		
		for(var i = 0; i < len; i++)
			if ((v.charAt(i) != '0') && (v.charAt(i) != settings.decimal)) 
				break;

		for(; i < len; i++) {
			if (strCheck.indexOf(v.charAt(i))!= -1) 
				a += v.charAt(i);
		}
		
		if(a.length == 0) {
			t = "0,000";
		
		} else if (a.length == 1) {
			t = "0,00" + a;
		
		} else if (a.length == 2) {
			t = "0,0" + a;
		
		} else if (a.length == 3) {
			t = "0," + a;
		
		} else {
			var part1 = a.substring(0, a.length - 3);
			var part2 = a.substring(a.length - 3);
			t = part1 + "," + part2;
		
		}
		var p, d = (t = t.split(","))[1].substr(0, 3);
		
		if(a.length <= settings.maxNumber) {
			for(p = (t = t[0]).length; (p -= 3) >= 1;) {
				t = t.substr(0, p) + t.substr(p);
			}
			
			var masc = t + settings.decimal + d + Array(3 - d.length).join(0);			
			return masc;
		}
	}
	
	/** Javascript para trabalhar com tipos financeiros **/
	WmsUtil.prototype.sumMoney = function(o1,o2){
		o1 = $w.prepareObject(o1);
		o2 = $w.prepareObject(o2);
		num = parseFloat(o1) + parseFloat(o2);
		return $w.float2moeda(num);
	}
	
	WmsUtil.prototype.subtractMoney = function(o1,o2){
		o1 = $w.prepareObject(o1);
		o2 = $w.prepareObject(o2);
		num = parseFloat(o1) - parseFloat(o2);
		return $w.float2moeda(num);
	}
	
	WmsUtil.prototype.prepareObject = function(o1){
		while(String(o1).indexOf(".") != -1){
			o1 = String(o1).replace(".", "");
		}
		o1 = String(o1).replace(",",".");
		return o1;
	}
	
	String.prototype.replaceAll = function(de, para){
    	var str = this;
    	var pos = str.indexOf(de);
    	while (pos > -1){
			str = str.replace(de, para);
			pos = str.indexOf(de);
		}
    	return (str);
	}
	
	WmsUtil.prototype.float2moeda = function(num) {
		x = 0;
		
		if(num<0) {
		   num = Math.abs(num);
		   x = 1;
		}
		
		if(isNaN(num)) num = "0";
		   cents = Math.floor((num*100+0.5)%100);
		
		num = Math.floor((num*100+0.5)/100).toString();
		
		if(cents < 10) cents = "0" + cents;
		   for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		      num = num.substring(0,num.length-(4*i+3))+'.'
		            +num.substring(num.length-(4*i+3));
		
		ret = num + ',' + cents;
		
		if (x == 1) ret = ' - ' + ret;
		return ret;
	}
	
	WmsUtil.prototype.validationCPF = function(cpf){
		var i;
		s = cpf;
		s = s.replace(/\D/g,"");
		
		var c = s.substr(0,9);
		var dv = s.substr(9,2);
		var d1 = 0; 
		
		for(i=0;i<=9; i++){
			var cpfRepetido;
			 cpfRepetido =i+''+i+''+i+''+i+''+i+''+i+''+i+''+i+''+i+''+i+''+i;			
			if(s==(cpfRepetido)){
			return false;
			}
			
		}
		for (i = 0; i < 9; i++){
			d1 += c.charAt(i)*(10-i);
		}
		if (d1 == 0){
		 	return false;
		}
		d1 = 11 - (d1 % 11);
		if (d1 > 9) d1 = 0;
		if (dv.charAt(0) != d1){
			return false;
		}
		d1 *= 2;
		for (i = 0; i < 9; i++){
			d1 += c.charAt(i)*(11-i);
		}
		d1 = 11 - (d1 % 11);
		if (d1 > 9) d1 = 0;
		if (dv.charAt(1) != d1){
			return false;
		}
		return true;
}

	
/**
*	Verifica se um CNPJ � v�lido.
*/
WmsUtil.prototype.validationCNPJ = function(cnpjString){
	var numeros, digitos, soma, i, resultado, pos, tamanho, digitos_iguais;
	digitos_iguais = 1;
	var cnpj = cnpjString.replace(/\D/g,"");
	if (cnpj.length < 14 && cnpj.length < 15)
	      return false;
	for (i = 0; i < cnpj.length - 1; i++)
	      if (cnpj.charAt(i) != cnpj.charAt(i + 1))
	            {
	            digitos_iguais = 0;
	            break;
	            }
	if (!digitos_iguais)
	      {
	      tamanho = cnpj.length - 2
	      numeros = cnpj.substring(0,tamanho);
	      digitos = cnpj.substring(tamanho);
	      soma = 0;
	      pos = tamanho - 7;
	      for (i = tamanho; i >= 1; i--)
	            {
	            soma += numeros.charAt(tamanho - i) * pos--;
	            if (pos < 2)
	                  pos = 9;
	            }
	      resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	      if (resultado != digitos.charAt(0))
	            return false;
	      tamanho = tamanho + 1;
	      numeros = cnpj.substring(0,tamanho);
	      soma = 0;
	      pos = tamanho - 7;
	      for (i = tamanho; i >= 1; i--)
	            {
	            soma += numeros.charAt(tamanho - i) * pos--;
	            if (pos < 2)
	                  pos = 9;
	            }
	      resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
	      if (resultado != digitos.charAt(1))
	            return false;
	      return true;
	      }
	else
	      return false;
} 



WmsUtil.prototype.formataData = function(campo){
	  	v= campo.value;	   
        v=v.replace(/\D/g,"") 
        v=v.replace(/(\d{2})(\d)/,"$1/$2") 
        v=v.replace(/(\d{2})(\d)/,"$1/$2") 
      	campo.value= v;        
       
 }
 
WmsUtil.prototype.daysBetween = function (dateStr1, dateStr2) {
	var splitStr1 = dateStr1.split('/');
	var splitStr2 = dateStr2.split('/');
	var date1 = new Date(splitStr1[2], splitStr1[1], splitStr1[0]);
	var date2 = new Date(splitStr2[2], splitStr2[1], splitStr2[0]);
	
    // The number of milliseconds in one day
    var ONE_DAY = 1000 * 60 * 60 * 24
    // Convert both dates to milliseconds
    var date1_ms = date1.getTime()
    var date2_ms = date2.getTime()
    // Calculate the difference in milliseconds
    var difference_ms = Math.abs(date1_ms - date2_ms)
    // Convert back to days and return
    return Math.round(difference_ms/ONE_DAY)
}

WmsUtil.prototype.dataAtual = function(){
	var today=new Date()
	var todayd=today.getDate()
	var todaym=today.getMonth()+1
	var todayy=today.getFullYear()
	var mes;
	var dia;
	
	if(todaym<10){ 
		mes="0"+todaym;
	}else{
		mes = todaym;
	}
	
	if(todayd<10){
		dia="0"+todayd;
	}else{
		dia=todayd;
	}					
	
	return   dia+ "/" + mes + "/" + todayy;
}

/***************************************************/
	
/****************************************************/

//declara a biblioteca
var $w = new WmsUtil();
