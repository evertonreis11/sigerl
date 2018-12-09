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
package br.com.linkcom.wms.authorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.linkcom.neo.authorization.AuthorizationProcessItemFilter;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.wms.geral.bean.Acaopapel;

public class AuthorizationProcessFilter {

	Role role;
	
	Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap = new HashMap<String, List<AuthorizationProcessItemFilter>>();
	protected Set<Acaopapel> listaAcaoPapel = new ListSet<Acaopapel>(Acaopapel.class);

	public Map<String, List<AuthorizationProcessItemFilter>> getGroupAuthorizationMap() {
		return groupAuthorizationMap;
	}

	public void setGroupAuthorizationMap(
			Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap) {
		this.groupAuthorizationMap = groupAuthorizationMap;
	}

	@DisplayName("Nível")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Set<Acaopapel> getListaAcaoPapel() {
		return listaAcaoPapel;
	}

	public void setListaAcaoPapel(Set<Acaopapel> listaAcaoPapel) {
		this.listaAcaoPapel = listaAcaoPapel;
	}
	
	
}
