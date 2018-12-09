/*
 * Neo Framework http://www.neoframework.org
 * Copyright (C) 2007 the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * You may obtain a copy of the license at
 * 
 *     http://www.gnu.org/copyleft/lesser.html
 * 
 */
package br.com.linkcom.wms.util.tag;

import br.com.linkcom.neo.exception.NeoException;
import br.com.linkcom.neo.view.BaseTag;
import br.com.linkcom.neo.view.LogicalTag;
import br.com.linkcom.wms.util.WmsUtil;

/**
 * Verifica se o usuário atual possui permissão para acessar a permissão
 * 
 * @author Pedro Gonçalves
 * @since 15/04/2008
 * @version 1.0
 */
public class HasAuthorizationTag extends BaseTag implements LogicalTag {

	protected String acao;
	protected boolean hasNotAuthorization = false;

	@Override
	protected void doComponent() throws Exception {
		if (!hasNotAuthorization) {
			if (acao != null && acao.contains(",")) {
				String[] split = acao.split(",");
				boolean hasAuthorization = false;
				for (String string : split) {
					if (hasAuthorization(string))
						hasAuthorization = true;
				}
				if (hasAuthorization)
					doBody();

			} else if (hasAuthorization(acao)) {
				doBody();
			}
		} else {
			if (acao != null && acao.contains(",")) {
				String[] split = acao.split(",");
				boolean hasAuthorization = false;
				for (String string : split) {
					if (hasAuthorization(string))
						hasAuthorization = true;
				}
				if (!hasAuthorization)
					doBody();

			} else if (!hasAuthorization(acao)) {
				doBody();
			}
		}
	}

	private boolean hasAuthorization(String action) {
		try {
			return WmsUtil.isUserHasAction(action);
		} catch (Exception e) {
			throw new NeoException("Problema ao verificar autorização", e);
		}
	}

	public String getAcao() {
		return acao;
	}
	
	public boolean isHasNotAuthorization() {
		return hasNotAuthorization;
	}

	public void setHasNotAuthorization(boolean hasNotAuthorization) {
		this.hasNotAuthorization = hasNotAuthorization;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

}
