$.fn.fastSerialize = function(ignoreDisabled) {
    if(ignoreDisabled == undefined){
    	ignoreDisabled = false;
    }
    var a = [];
    $('input,textarea,select,button', this).each(function() {
        var n = this.name;
        var t = this.type;
        var ignore = ignoreDisabled ? false : this.disabled;
        if ( !n || ignore || t == 'reset' ||
            (t == 'checkbox' || t == 'radio') && !this.checked ||
            (t == 'submit' || t == 'image' || t == 'button') && this.form.clicked != this ||
            this.tagName.toLowerCase() == 'select' && this.selectedIndex == -1)
            return;
        if (t == 'image' && this.form.clicked_x)
            return a.push(
                {name: n+'_x', value: this.form.clicked_x},
                {name: n+'_y', value: this.form.clicked_y}
            );
        if (t == 'select-multiple') {
            $('option:selected', this).each( function() {
                a.push({name: n, value: $(this).val()});
            });
            return;
        }
        a.push({name: n, value: $(this).val()});
    });
    return a;
};

$.fn.clearForm = function() {
    $('input,textarea,select,button', this).each(function() {
        var n = this.name;
        var t = this.type;
        var c = this.className
        if(c != "keep")
        	  $(this).val("");
      
    });
};