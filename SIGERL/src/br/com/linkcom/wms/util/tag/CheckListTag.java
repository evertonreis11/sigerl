package br.com.linkcom.wms.util.tag;

import java.util.Collection;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo.view.BaseTag;
import br.com.linkcom.neo.view.BeanTag;
import br.com.linkcom.neo.view.DataGridTag;
import br.com.linkcom.neo.view.template.TemplateTag;

@SuppressWarnings("unchecked")
public class CheckListTag extends TemplateTag {
	
	
	protected Object itens;
	protected String propertyName;
	protected String propertyDescription;
	protected String itensField;
	protected String renderas;
	protected String prefix;
	protected String inputOnChange;
	protected String inputOnClick;
	protected String name;
	protected String label;
	
	
	@Override
	protected void doComponent() throws Exception {
		
		BeanTag beanTag = findParent(BeanTag.class);
		String order = "";
		if(beanTag != null){
			order = Util.strings.uncaptalize(beanTag.getName())+".";
		}
		order += name;
		
		beanTag.getBeanDescriptor().getDescriptionPropertyName();
		
		String displayName = beanTag.getBeanDescriptor().getPropertyDescriptor(name).getDisplayName();
		
		if (label != null && !"".equals(label)) {
			displayName = label;
		}
		
		String descriptionPropertyName = "";
		if (itens != null && !((Collection)itens).isEmpty())
			descriptionPropertyName = Neo.getApplicationContext().getBeanDescriptor(((Collection)itens).iterator().next()).getDescriptionPropertyName();
		
		if (propertyDescription != null && !"".equals(propertyDescription)) 
			descriptionPropertyName = propertyDescription;
				
		String propertyPrefix = beanTag.getPropertyPrefix();
		String propertyIndex = beanTag.getPropertyIndex();
		String prefix = "";
		if(propertyPrefix != null){
			prefix = propertyPrefix;
		} 
		if(propertyIndex != null){
			prefix += "["+propertyIndex+"]";
		} 
		if(prefix.length()!=0 && name != null && !name.equals("this")){
			prefix += ".";
		}

		String fullName = prefix+name;
		
		
		BaseTag findFirst = findFirst(DataGridTag.class);
		if (findFirst instanceof DataGridTag) {
			renderas = "column";
		}		
		
		pushAttribute("name", fullName);
		pushAttribute("property", order);
		pushAttribute("label", displayName);
		pushAttribute("itens", itens);
		pushAttribute("itens", itens);
		pushAttribute("renderas", renderas);
		pushAttribute("prefix", prefix);
		pushAttribute("propertyDescription", descriptionPropertyName);
		pushAttribute("inputOnChange", inputOnChange);
		pushAttribute("inputOnClick", inputOnClick);		
		includeJspTemplate();
		popAttribute("itens");
		popAttribute("prefix");
		popAttribute("propertyDescription");
		popAttribute("renderas");
		popAttribute("label");
		popAttribute("name");
		popAttribute("property");
		popAttribute("inputOnChange");
		popAttribute("inputOnClick");
	}


	public Object getItens() {
		return itens;
	}


	public String getItensField() {
		return itensField;
	}


	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name;
	}


	public String getPropertyDescription() {
		return propertyDescription;
	}


	public String getPropertyName() {
		return propertyName;
	}
	
	public String getRenderas() {
		return renderas;
	}
	
	public String getPrefix() {
		return prefix;
	}


	public String getInputOnChange() {
		return inputOnChange;
	}
	
	public String getInputOnClick() {
		return inputOnClick;
	}
	
	public void setItens(Object itens) {
		this.itens = itens;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setRenderas(String renderas) {
		this.renderas = renderas;
	}

	public void setItensField(String itensField) {
		this.itensField = itensField;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setPropertyDescription(String propertyDescription) {
		this.propertyDescription = propertyDescription;
	}


	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public void setInputOnChange(String inputOnChange) {
		this.inputOnChange = inputOnChange;
	}
	
	public void setInputOnClick(String inputOnClick) {
		this.inputOnClick = inputOnClick;
	}
}

