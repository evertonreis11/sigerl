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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.AuthorizationDAO;
import br.com.linkcom.neo.authorization.AuthorizationItem;
import br.com.linkcom.neo.authorization.AuthorizationModule;
import br.com.linkcom.neo.authorization.AuthorizationProcessItemFilter;
import br.com.linkcom.neo.authorization.HasAccessAuthorizationModule;
import br.com.linkcom.neo.authorization.Permission;
import br.com.linkcom.neo.authorization.PermissionLocator;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.ControlMapping;
import br.com.linkcom.neo.controller.ControlMappingLocator;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.standard.ApplicationContext;
import br.com.linkcom.neo.core.web.DefaultWebRequestContext;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.view.menu.MenuTag;

@SuppressWarnings("unchecked")
public class DefaultAuthorizationProcess extends MultiActionController {
	
	AuthorizationDAO authorizationDAO;
	TransactionTemplate transactionTemplate;
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public void setAuthorizationDAO(AuthorizationDAO authorizationDAO) {
		this.authorizationDAO = authorizationDAO;
	}
	
	@Action("salvar")
	@Input("list")
	public ModelAndView salvar(final WebRequestContext request, final AuthorizationProcessFilter authorizationFilter) {
		final Role role = authorizationFilter.getRole();
		if (role != null) {
			Enumeration<?> parameterNames = request.getServletRequest().getParameterNames();
			while(parameterNames.hasMoreElements()){
				System.out.println(parameterNames.nextElement());
			}
			PermissionLocator permissionLocator = request.getWebApplicationContext().getConfig().getPermissionLocator();
			synchronized (permissionLocator) {
				permissionLocator.clearCache();
				Collection<List<AuthorizationProcessItemFilter>> values = authorizationFilter.getGroupAuthorizationMap().values();
				final List<AuthorizationProcessItemFilter> authorizationItemFilters = new ArrayList<AuthorizationProcessItemFilter>();
				for (List<AuthorizationProcessItemFilter> value : values) {
					authorizationItemFilters.addAll(value);
				}
				transactionTemplate.execute(new TransactionCallback(){

					public Object doInTransaction(TransactionStatus status) {
						ControlMappingLocator controlMappingLocator = request.getWebApplicationContext().getConfig().getControlMappingLocator();
						for (AuthorizationProcessItemFilter filter : authorizationItemFilters) {
							ControlMapping controlMapping = controlMappingLocator.getControlMapping(filter.getPath());
							AuthorizationModule authorizationModule = controlMapping.getAuthorizationModule();
							Map<String, String> defaultPermissionMap = getDefaultPermissionMap(authorizationModule);
							Map<String, String> permissionMap = filter.getPermissionMap();
							Set<String> defaultKeySet = defaultPermissionMap.keySet();
							for (String string : defaultKeySet) {
								if(permissionMap.get(string) == null){
									permissionMap.put(string, defaultPermissionMap.get(string));
								}
							}
							authorizationDAO.savePermission(filter.getPath(), role, permissionMap);
						}
						return null;
					}});

			}
		}
		//reseta os menus
		request.getSession().setAttribute(MenuTag.MENU_CACHE_MAP, null);
		((DefaultWebRequestContext)request).setLastAction("");
		return list(request, authorizationFilter);
	}

	@DefaultAction
	@Input("")
	public ModelAndView list(WebRequestContext request, AuthorizationProcessFilter authorizationFilter) {
		authorizationFilter.setGroupAuthorizationMap(new HashMap<String, List<AuthorizationProcessItemFilter>>());
		request.setAttribute("roles", authorizationDAO.findAllRoles());
		request.setAttribute("filtro", authorizationFilter);
		
		if(authorizationFilter.getRole() != null){
			Map<String, AuthorizationModule> mapaGroupModule = new HashMap<String, AuthorizationModule>();
			Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap = authorizationFilter.getGroupAuthorizationMap();
			
			Class[] controllerClasses = findControllerClasses(request.getWebApplicationContext());
			for (Class<?> controllerClass : controllerClasses) {
				Controller controller = controllerClass.getAnnotation(Controller.class);
				try {
					AuthorizationModule authorizationModule = controller.authorizationModule().newInstance();
					mapaGroupModule.put(authorizationModule.getAuthorizationGroupName(), authorizationModule);
					if(!(authorizationModule instanceof HasAccessAuthorizationModule)){
						AuthorizationProcessItemFilter[] authorizationProcessItemFilters = getAuthorizationProcessItemFilter(authorizationFilter.getRole(), controller, authorizationModule);
						for (AuthorizationProcessItemFilter authorizationProcessItemFilter : authorizationProcessItemFilters) {
							AuthorizationProcessItemFilter authorizationItemFilter = authorizationProcessItemFilter;
							List<AuthorizationProcessItemFilter> list = getAuthorizationListForModule(groupAuthorizationMap, authorizationModule);
							list.add(authorizationItemFilter);													
						}

					}
				} catch (InstantiationException e) {
					throw new RuntimeException("Não foi possível instanciar o módulo de autorização", e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Não foi possível instanciar o módulo de autorização", e);
				}
			}
			request.setAttribute("mapaGroupModule", mapaGroupModule);
		}
		request.setAttribute("authorizationProcessItemFilterClass", AuthorizationProcessItemFilter.class);
		return new ModelAndView("process/autorizacao");
	}

	protected List<AuthorizationProcessItemFilter> getAuthorizationListForModule(Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap, AuthorizationModule authorizationModule) {
		String authorizationGroupName = authorizationModule.getAuthorizationGroupName();
		List<AuthorizationProcessItemFilter> list = groupAuthorizationMap.get(authorizationGroupName);
		if(list == null){
			list = new ArrayList<AuthorizationProcessItemFilter>();
			groupAuthorizationMap.put(authorizationGroupName, list);
		}
		return list;
	}

	protected AuthorizationProcessItemFilter[] getAuthorizationProcessItemFilter(Role role, Controller controller, AuthorizationModule authorizationModule) {
		String[] paths = controller.path();
		List<AuthorizationProcessItemFilter> authorizationItemFilters = new ArrayList<AuthorizationProcessItemFilter>();
		for (String path : paths) {
			Permission permission = authorizationDAO.findPermission(role, path);
			AuthorizationProcessItemFilter authorizationItemFilter = new AuthorizationProcessItemFilter();
			authorizationItemFilter.setAuthorizationModule(authorizationModule);
			authorizationItemFilter.setDescription(translatePath(path));
			authorizationItemFilter.setPath(path);
			if(permission == null){			
				permission = authorizationDAO.savePermission(path, role, getDefaultPermissionMap(authorizationModule));
			}
			authorizationItemFilter.setPermissionMap(permission.getPermissionmap());
			authorizationItemFilters.add(authorizationItemFilter);
		}
		return authorizationItemFilters.toArray(new AuthorizationProcessItemFilter[authorizationItemFilters.size()]);
	}
	
	protected String translatePath(String string) {
		return string;
	}

	private Map<String, String> getDefaultPermissionMap(AuthorizationModule authorizationModule) {
		AuthorizationItem[] authorizationItens = authorizationModule.getAuthorizationItens();
		Map<String, String> defaultPermissionMap = new HashMap<String, String>();
		
		for (AuthorizationItem item : authorizationItens) {
			String id = item.getId();
			if(item.getValores()== null || item.getValores().length == 0) throw new IllegalArgumentException("Os valores possíveis de um item de autorização não pode ser um array vazio ou null");
			String valorMaisRestritivo = item.getValores()[item.getValores().length-1];
			defaultPermissionMap.put(id, valorMaisRestritivo);
		}
		return defaultPermissionMap;
	}

	protected Class[] findControllerClasses(ApplicationContext applicationContext) {
		return applicationContext.getClassManager().getClassesWithAnnotation(Controller.class);
	}

}
