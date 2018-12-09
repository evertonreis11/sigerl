package br.com.linkcom.wms.util.tag;

import br.com.linkcom.neo.view.template.TemplateTag;


public class TableGroupTag extends TemplateTag {
	protected Integer columns = 2;
	protected Integer panelColspan;
	protected String panelgridWidth;
	
	@Override
	protected void doComponent() throws Exception {
		pushAttribute("TableGroupTag", this);
		includeJspTemplate();
		popAttribute("TableGroupTag");
	}
	
	public Integer getColumns() {
		return columns;
	}
	public Integer getPanelColspan() {
		return panelColspan;
	}
	public String getPanelgridWidth() {
		return panelgridWidth;
	}
	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	public void setPanelColspan(Integer panelColspan) {
		this.panelColspan = panelColspan;
	}
	public void setPanelgridWidth(String panelgridWidth) {
		this.panelgridWidth = panelgridWidth;
	}
}
