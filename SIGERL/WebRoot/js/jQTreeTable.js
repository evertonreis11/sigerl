/*          Copyright Paul Hanlon (paulhan@eircom.net)
            Licence MIT, BSD, same as jQuery;
Please leave the above copyright notice with this code,
if for no other reason than people wanting to get in touch for help/support.
Not much here in the way of documentation. I'm hoping the demo on the website at
http://www.hanpau.com/jquery/unobtrusivetreetable.php will explain most of it,
particularly how the map is constructed.
The options are mostly the images you need to render the tree properly, with the
last five being:-
collapse:
    Either false or an array of parents to collapse. Just put in the highest
    level nodes, this will recurse down through the children and collapse them,
    without them having to be specified as well. Even if you only set one parent
    to be collapsed, it must be in [] brackets.
column:
    A zero based number of the column you want to render the tree in.
stripe:
    Set to true if you want to have striping on alternate rows. False returns a
    plain treetable. An "even" class needs to be set in CSS for background-color.
highlight:
    Set to true if you want to highlight the row that is being hovered over.
    An "over" class needs to be set in CSS for background-color.
onselect:
    Callback function where you can set what you want to happen when a td is selected
    Forces highlighting on td even if there are other elements within. Links and events
    still work though.
*/
(function(jq){
	var mapa = [],mapOriginal = [], mapb = [], num=0,selectedRow=null,
	opts = {
		openImg: "tv-collapsable.gif",
		shutImg: "tv-expandable.gif",
		leafImg: "tv-item.gif",
		lastOpenImg: "tv-collapsable-last.gif",
		lastShutImg: "tv-expandable-last.gif",
		lastLeafImg: "tv-item-last.gif",
		vertLineImg: "vertline.gif",
		blankImg: "blank.gif",
		collapse: false,
		column: 0,
		striped: false,
		highlight: false,
		onselect: false,
		enableVisualizar: false,
		disableVisualizar: false,
		showCarregando: false,
		hideCarregando: false,
	};
  
	jq.fn.jqTreeTable = function(map, options){
		alert("entrou");
		mapOriginal = map;
		for (var i in opts){//Sort out the options
			opts[i] = (options[i])? options[i]: opts[i];
    	}
    	for (var x=0,xl=map.length; x<xl;x++){//From map of parents, get map of kids
			num = map[x];
			if (!mapa[num]){
        		mapa[num] = [];
      		}
      		mapa[num].push(x+1);
    	}
    	
    	//buildText(0, "");
    	
    	/*jq("tr", this).each(function(i){//Inject the images into the column to make it work
      		//jq(this).children("td").eq(opts.column).prepend(mapb[i]);
      		this.setAttribute("index",i+1);
    	});*/
    	/*
    	if (opts.collapse instanceof Array){//Set nodes to collapsed
			for (var y=0, yl=opts.collapse.length; y<yl; y++){
        		collapseKids(opts.collapse[y]);
      		}
    	}*/
    	//if(opts.striped){jq("#treetable tr:even").addClass("even")};//Stripe all even rows
    	
    	//Otimizar isso aqui
    	
    	/*jq(".parimg", this).each(function(i){
      		var $this = jq(this);
      		$this.click(function(){
        		var jqo = jq(this);
        		var num = this.getAttribute("id").substr(1);//Number of the row
        		if (jqo.parents("tr").next().is(".collapsed")){//Then expand immediate children
					expandKids(num);
        		}else{//Collapse all and set image to opts.shutImg or opts.lastShutImg on parents
          			collapseKids(num);
        		}
        		if(opts.striped){stripe(1)};//Restripe all the rows under this one
      		});
   		});
    	*/
    	/*
    	if (opts.highlight){//This is where it highlights the rows
	      	jq("tr", this).hover(function(){
	        	jq(this).addClass("over");
	      	},
	      	function(){
	        	jq(this).removeClass("over");
	      	});
    	}*/
    	jq("#treetable").click( function( event ) {//This looks after selecting td's
      		var $target = (event.target.tagName == "TD")?jq(event.target):jq(event.target).parent();
   			if (typeof opts.onselect == "function"){
     			var parentRow = $target.parent();
     			jq("tr.selected", this).removeClass("selected");
     			parentRow.addClass("selected");
     			selectedRow = parentRow;
    			opts.onselect($target);
   			};
   		 });
    
    	jq("#treetable").dblclick( function( event ) {//This looks after selecting td's
      		var $target = (event.target.tagName == "TD")?jq(event.target):jq(event.target).parent();
      		if (typeof opts.onselect == "function"){
        		var parentRow = $target.parent();
        		jq("tr.selected", this).removeClass("selected");
        		parentRow.addClass("selected");
        		selectedRow = parentRow;
        		if(event.target.tagName == "TD")
        			parentRow.find("img[id^=r]").click()
         
        		opts.onselect($target);
      		};
    	});
    	$(window).unload(function () {
			jq(this).unbind();
			jq(".parimg", this).each(function(i){
	      		var $this = jq(this);
	      		$this.unbind();
	   		});
	   		jq("#treetable").unbind("click");
	   		jq("#treetable").unbind("dblclick");
		});
	};
	
	
  
	buildText = function(parno, preStr){//Recursively build up the text for the images that make it work
    	var mp = mapa[parno], ro=0, pre="", pref, img;
		for (var y=0, yl=mp.length;y<yl;y++){
   			ro = mp[y];
   			if (mapa[ro]){//It's a parent as well. Build it's string and move on to it's children
     			if (y==yl-1){//It's the last child, It's child will have a blank field behind it
					pre = opts.blankImg;
					img = opts.lastOpenImg;
     			}else{
					pre = opts.vertLineImg;
					img = opts.openImg;
     			}
     			mapb[ro-1] = preStr + '<img src="'+img+'" class="parimg" id="r'+ro+'">';
     			pref = preStr + '<img src="'+pre+'" class="preimg">';
     			buildText(ro, pref);
			} else {//it's a child
       			img = (y==yl-1)? opts.lastLeafImg: opts.leafImg;//It's the last child, It's child will have a blank field behind it
       			mapb[ro-1] = preStr + '<img src="'+img+'" class="ttimage">';
    		}
   		}
	};
	
	jq.fn.jqTreeTable.getSelectedRow = function(){
		return selectedRow;
	}
	
	jq.fn.jqTreeTable.getLevelOfSelectedRow = function(){
		return selectedRow.find("td:eq("+opts.column+")").find("img").size();;
	}
	
	jq.fn.jqTreeTable.getLevel = function(row){
		return row.find("td:eq("+opts.column+")").find("img").size();;
	}
	
	jq.fn.jqTreeTable.unBuildText = function(){//Recursively build up the text for the images that make it work
    	jq("#treetable tr").find("td:eq("+opts.column+")").find("img").remove();
	};
	
	jq.fn.jqTreeTable.getBuildTreeColumn = function(){//Recursively build up the text for the images that make it work
    	return opts.column;
	};
	
	jq.fn.jqTreeTable.stripe = function(){//Recursively build up the text for the images that make it work
    	stripe(1);	
	};
	
	jq.fn.jqTreeTable.getFather = function(pos){//Recursively build up the text for the images that make it work
    	return jq("#treetable").find("tr[index="+mapOriginal[pos-1]+"]");
	};
	
	jq.fn.unloadtree=function(){
		jq(".parimg", this).each(function(i){
      		var $this = jq(this);
      		$this.unbind("click");
   		});
		return null;
	};
	
	jq.fn.jqTreeTable.getSon = function(pos){//Recursively build up the text for the images that make it work
    	var filhos = [],i=0;
    	for(i=0;i<mapOriginal.length;i++){
    		if(mapOriginal[i] == pos){
    			filhos.push(i+1);
    		}
    	}
    	return filhos;
	};
	
	jq.fn.jqTreeTable.getRow = function(pos){
    	return jq("#treetable").find("tr[index="+pos+"]");
	};
	
	jq.fn.jqTreeTable.expandKids = function(num){
    	expandKids(num);
	};
	
	jq.fn.jqTreeTable.collapseKids = function(num){
    	collapseKids(num);
	};
	
	expandKids = function(num){//Expands immediate children
		jq("#r"+num).attr("src", function(){
			return (this.src.substr(this.src.lastIndexOf("/")+1)==opts.lastShutImg)? opts.lastOpenImg: opts.openImg;
    	});
    	
    	for (var x=0, xl=mapa[num].length;x<xl;x++){
			jq("#treetable tr").eq(mapa[num][x]-1).removeClass("collapsed");
    	}
  	};
  	
	collapseKids = function(num){//Recursively collapses all children and their children and changes
		var elm = document.getElementById("r"+num);
		if (elm && elm.src.substr(elm.src.lastIndexOf("/")+1)!=opts.lastShutImg){
        	elm.src = (elm.src.substr(elm.src.lastIndexOf("/")+1)==opts.lastOpenImg)? opts.lastShutImg: opts.shutImg;
      	}

		for (var x=0, xl=mapa[num].length;x<xl;x++){
			mapnumx = mapa[num][x];
			var selector = jq("tr",document.getElementById("treetable")).eq(mapnumx-1);
			
			if(!selector.hasClass("collapsed")){
				selector.addClass("collapsed");
			
				if (mapa[mapnumx]){
					collapseKids(mapnumx);
				}
			}
    	}
	};
	
	stripe = function(num){//Dynamically stripes rows after expanding/collapsing
		var isStriped = (jq("#treetable tr").eq(num).attr("class")=="even over")? 0:1;
		jq("#treetable tr:gt("+num+")").not(".collapsed").each(function(i){
			if ((i+isStriped) % 2==0){
				jq(this).removeClass("even");
			}else{
				jq(this).addClass("even");
			}
		});
	};
	
	return this;
	
})(jQuery);
