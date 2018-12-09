/*
 * Copyright (c) 2007 Josh Bush (digitalbush.com)
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:

 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * Version: 1.1.1
 * Release: 2007-10-02
 */
(function($) {
	//Helper Functions for Caret positioning
	function getCaretPosition(ctl){
		var res = {begin: 0, end: 0 };
		if (ctl.setSelectionRange){
			res.begin = ctl.selectionStart;
			res.end = ctl.selectionEnd;
		}else if (document.selection && document.selection.createRange){
			var range = document.selection.createRange();
			res.begin = 0 - range.duplicate().moveStart('character', -100000);
			res.end = res.begin + range.text.length;
		}
		return res;
	};

	function setCaretPosition(ctl, pos){
		if(ctl.setSelectionRange){
			ctl.focus();
			ctl.setSelectionRange(pos,pos);
		}else if (ctl.createTextRange){
			var range = ctl.createTextRange();
			range.collapse(true);
			range.moveEnd('character', pos);
			range.moveStart('character', pos);
			range.select();
		}
	};

	//Predefined character definitions
	var charMap={
		'9':"[0-9]",
		'a':"[A-Za-z]",
		'*':"[A-Za-z0-9]"
	};

	//Helper method to inject character definitions
	$.mask={
		addPlaceholder : function(c,r){
			charMap[c]=r;
		}
	};

	$.fn.unmask=function(){
		return this.trigger("unmask");
	};
	
	$.fn.clearCache = function(){
		return this.trigger("clearcache");
	};

	//Main Method
	$.fn.mask = function(mask,settings) {
		var readonly = $(this).attr("readonly");
		if(readonly)
			return false;

		settings = $.extend({
			placeholder: "_",
			completed: null,
			notCompletedEvent: null,
			alertNotCompleted: null
		}, settings);

		//Build Regex for format validation
		var re = new RegExp("^"+
		$.map( mask, function(c,i){
		  c=c||mask.charAt(i);
		  return charMap[c]||((/[A-Za-z0-9]/.test(c)?"":"\\")+c);
		}).join('')+
		"$");

		return this.each(function(){
			var input=$(this);
			var buffer=new Array(mask.length);
			var locked=new Array(mask.length);
			var valid=false;
			var ignore=false;  			//Variable for ignoring control keys
			var firstNonMaskPos=null;

			//Build buffer layout from mask & determine the first non masked character
			$.each( mask, function(i,c){
				c=c||mask.charAt(i);
				locked[i]=(charMap[c]==null);

				buffer[i]=locked[i]?c:settings.placeholder;
				if(!locked[i] && firstNonMaskPos==null)
					firstNonMaskPos=i;
			});

			/* Event Functions */
			function focusEvent(){
				checkVal();
				writeBuffer();
				setTimeout(function(){
					setCaretPosition(input[0],valid?mask.length:firstNonMaskPos);
				},0);
			};

			function keydownEvent(e){
				var pos=getCaretPosition(this);
				var k = e.keyCode;
				ignore=(k < 16 || (k > 16 && k < 32 ) || (k > 32 && k < 41));

				//delete selection before proceeding
				if((pos.begin-pos.end)!=0 && (!ignore || k==8 || k==46)){
					clearBuffer(pos.begin,pos.end);
				}
				//backspace and delete get special treatment
				if(k==8){//backspace
					while(pos.begin-->=0){
						if(!locked[pos.begin]){
							buffer[pos.begin]=settings.placeholder;
							if($.browser.opera){
								//Opera won't let you cancel the backspace, so we'll let it backspace over a dummy character.
								s=writeBuffer();
								input.val(s.substring(0,pos.begin)+" "+s.substring(pos.begin));
								setCaretPosition(this,pos.begin+1);
							}else{
								writeBuffer();
								setCaretPosition(this,Math.max(firstNonMaskPos,pos.begin));
							}
							return false;
						}
					}
				}else if(k==46){//delete
					clearBuffer(pos.begin,pos.begin+1);
					writeBuffer();
					setCaretPosition(this,Math.max(firstNonMaskPos,pos.begin));
					return false;
				}else if (k==27){//escape
					clearBuffer(0,mask.length);
					writeBuffer();
					setCaretPosition(this,firstNonMaskPos);
					return false;
				}
			};
			
			function clearCacheEvent(){					
				clearBuffer(0,mask.length);
			};

			function keypressEvent(e){
				if(ignore){
					ignore=false;
					return;
				}
				e=e||window.event;
				var k=e.charCode||e.keyCode||e.which;
				var pos=getCaretPosition(this);

				if(e.ctrlKey || e.altKey){//Ignore
					return true;
				}else if ((k>=41 && k<=122) ||k==32 || k>186){//typeable characters
					var p=seekNext(pos.begin-1);
					if(p<mask.length){
						if(new RegExp(charMap[mask.charAt(p)]).test(String.fromCharCode(k))){
							buffer[p]=String.fromCharCode(k);
							writeBuffer();
							var next=seekNext(p);
							setCaretPosition(this,next);
							if(settings.completed && next == mask.length)
								settings.completed.call(input);
						}
					}
				} else if(k == 9 ) {
				}
				return false;
			};

			/*Helper Methods*/
			function clearBuffer(start,end){
				for(var i=start;i<end;i++){
					if(!locked[i])
						buffer[i]=settings.placeholder;
				}
			};

			function isBufferEmpty(start,end){
				for(var i=start;i<end;i++){
					if(!locked[i] && buffer[i]!=settings.placeholder)
						return false;
				}
				return true;
			};

			function writeBuffer(){
				return input.val(buffer.join('')).val();
			};
			
			function checkVal(){
				//try to place charcters where they belong
				var test=input.val();
				var pos=0;
				if(isBufferEmpty(0,mask.length)){

					for(var i=0;i<mask.length;i++){
						if(!locked[i]){
							while(pos++<test.length){
								//Regex Test each char here.
								var reChar=new RegExp(charMap[mask.charAt(i)]);
								if(test.charAt(pos-1).match(reChar)){
									buffer[i] = test.charAt(pos-1);
									break;
								}
							}
						}
					}

				}
				var s=writeBuffer();
				if(!s.match(re) || test == ""){
					var ntest = input.val();
					var afocus = false;
					if(settings.notCompletedEvent != null && test == ntest){
						settings.notCompletedEvent.call(input);
					}
					if(settings.alertNotCompleted != null && test!= "" && ntest != "" && !isBufferEmpty(0,mask.length)){
						alert(settings.alertNotCompleted);
						afocus = true;
					}
					input.val("");
					
					clearBuffer(0,mask.length);
					
					valid=false;
					if(afocus) setTimeout(function(){input.get(0).focus()},100);
				}else
					valid=true;
			};

			function seekNext(pos){
				while(++pos<mask.length){
					if(!locked[pos])
						return pos;
				}
				return mask.length;
			};

			/*Event Bindings*/
			input.one("unmask",function(){
				input.unbind("focus",focusEvent);
				input.unbind("blur",checkVal);
				input.unbind("keydown",keydownEvent);
				input.unbind("keypress",keypressEvent);
				input.unbind("clearcache",clearCacheEvent);
				if ($.browser.msie)
					this.onpaste= null;
				else if ($.browser.mozilla)
					this.removeEventListener('input',checkVal,false);
			});
			input.bind("clearcache",clearCacheEvent);
			input.bind("focus",focusEvent);
			input.bind("blur",checkVal);
			input.bind("keydown",keydownEvent);
			input.bind("keypress",keypressEvent);
			//Paste events for IE and Mozilla thanks to Kristinn Sigmundsson
			if ($.browser.msie)
				this.onpaste= function(){setTimeout(checkVal,0);};
			else if ($.browser.mozilla)
				this.addEventListener('input',checkVal,false);

			checkVal();//Perform initial check for existing values
		});
	};
})(jQuery);