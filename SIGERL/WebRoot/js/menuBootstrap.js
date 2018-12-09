// Globals
var _cmItemList = new Array ();		// a simple list of items


//////////////////////////////////////////////////////////////////////
//
// Drawing Functions and Utility Functions
//
//////////////////////////////////////////////////////////////////////

//
// return the property string for the menu item
//
function cmActionItem (item)
{
	// var index = _cmItemList.push (item) - 1;
	_cmItemList[_cmItemList.length] = item;
	var index = _cmItemList.length - 1;
	return ' onmouseup="cmItemMouseUp (this,' + index + ')"';
}

//
// draw the sub menu recursively
//
function cmDrawSubMenu (subMenu)
{
	var str = '<ul class="dropdown-menu">';

	var item;

	var i;

	for (i = 5; i < subMenu.length; ++i)
	{
		item = subMenu[i];
		
		if (!item)
			continue;

		str += cmDrawItensMenu(item);
	}

	str += '</ul>';
	
	return str;
}

//
// The function that builds the menu inside the specified element id.
//
// @param	id	id of the element
//		orient	orientation of the menu in [hv][ab][lr] format
//		menu	the menu object to be drawn
//		nodeProperties	properties for each menu node
//
function cmDraw (id, menu){
	var obj = cmGetObject (id);

	var str = '<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">';
	
	str += '<ul class="nav navbar-nav">';

	var i;
	var item;

	for (i = 0; i < menu.length; ++i){
		item = menu[i];

		if (!item)
			continue;

		str += cmDrawItensMenu(item);
	
	}
	
	str += '</ul></div>';
	
	obj.innerHTML = str;
}



function cmDrawItensMenu(item, hasChild){
	var subStr = '';
	
	var hasChild = (item.length > 5);
	
	if (hasChild){
		subStr += '<li class="dropdown">';
		subStr += '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">';
		subStr += item[1];
		subStr += '<span class="caret"></span></a>';
		subStr += cmDrawSubMenu (item);
		subStr += '</li>';
	}else{
		subStr += '<li><a href="#" ';
		subStr += cmActionItem (item) + '>';
		subStr += item[1];
		subStr += '</a></li>';
	}
	
	return subStr;
}

//////////////////////////////////////////////////////////////////////
//
// Mouse Event Handling Functions
//
//////////////////////////////////////////////////////////////////////

//
// action should be taken for mouse button up at a menu item
//
function cmItemMouseUp (obj, index)
{
	var item = _cmItemList[index];

	var link = null, target = '_self';

	if (item.length > 2)
		link = item[2];
	if (item.length > 3 && item[3])
		target = item[3];

	if (link != null)
	{
		window.open (link, target);
	}

	var prefix = obj.cmPrefix;
	var thisMenu = cmGetThisMenu (obj, prefix);

	var hasChild = (item.length > 5);
	if (!hasChild)
	{
		if (cmIsDefaultItem (item))
		{
			if (obj.cmIsMain)
				obj.className = prefix + 'MainItem';
			else
				obj.className = prefix + 'MenuItem';
		}
		cmHideMenu (thisMenu, null, prefix);
	}
	else
	{
		if (cmIsDefaultItem (item))
		{
			if (obj.cmIsMain)
				obj.className = prefix + 'MainItemHover';
			else
				obj.className = prefix + 'MenuItemHover';
		}
	}
}

//
// returns the object baring the id
//
function cmGetObject (id)
{
	if (document.all)
		return document.all[id];
	return document.getElementById (id);
}
