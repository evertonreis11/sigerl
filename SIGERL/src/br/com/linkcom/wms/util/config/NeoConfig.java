package br.com.linkcom.wms.util.config;

import br.com.linkcom.neo.core.config.DefaultConfig;


public class NeoConfig extends DefaultConfig {
	@Override
	public String getRequiredRenderType() {
		return "addclass";
	}
}
