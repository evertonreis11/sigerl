package br.com.linkcom.wms.util;

import java.lang.reflect.Method;

import javax.persistence.Id;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.OracleSQLErrorCodeSQLExceptionTranslator.ForeignKeyException;
import br.com.linkcom.neo.util.ReflectionCache;
import br.com.linkcom.neo.util.ReflectionCacheFactory;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo.view.ajax.View;

public class CrudController<FILTRO extends FiltroListagem, FORMBEAN, BEAN> extends br.com.linkcom.neo.controller.crud.CrudController<FILTRO, FORMBEAN, BEAN>{
	
	@Override
	protected ModelAndView getEntradaModelAndView(WebRequestContext request,FORMBEAN form) {
		return new ModelAndView("crud/"+getBeanName()+"Entrada");
	}
	
	@Override
	public ModelAndView doCriar(WebRequestContext request, FORMBEAN form)throws CrudException {
		return super.doCriar(request, form);
	}

	@Override
	protected void excluir(WebRequestContext request, BEAN bean) throws Exception {
		String itens = request.getParameter("itenstodelete");
		if(itens != null && !itens.equals("")){
			ReflectionCache reflectionCache = ReflectionCacheFactory.getReflectionCache();
			Method[] methods = reflectionCache.getMethods(beanClass);
			Method methodId = null;
			for (Method method : methods) {
				if(reflectionCache.isAnnotationPresent(method, Id.class)){
					methodId = method;
					break;
				}
			}
			
			String propertyFromGetter = Util.beans.getPropertyFromGetter(methodId.getName());
			methodId = Util.beans.getSetterMethod(beanClass, propertyFromGetter);
			String[] codes = itens.split(",");
			for (String code : codes) {
				if(!"".equals(code) && code != null){
					BEAN obj = beanClass.newInstance();
					methodId.invoke(obj, Integer.parseInt(code));
					if(validaExcluirEmMassa(request, obj)){
							try {
								genericService.delete(obj);
							} catch (ForeignKeyException e) {
								throw new WmsException("O(s) registro(s) não pode(m) ser excluído(s), já possui(em) referências em outros registros do sistema.");
							}
					}
				}
			}
			
			request.addMessage("Registro(s) excluído(s) com sucesso.");
		} else {
			try {
				super.excluir(request, bean);
			} catch (ForeignKeyException da) {
				throw new WmsException("O(s) registro(s) não pode(m) ser excluído(s), já possui(em) referências em outros registros do sistema.");
			}	
		}
	}
		
	@Override
	public ModelAndView doEditar(WebRequestContext request, FORMBEAN form)throws CrudException {
		return super.doEditar(request, form);
	}

	@Override
	protected ModelAndView getSalvarModelAndView(WebRequestContext request, BEAN bean) {
		if("true".equals(request.getParameter("fromInsertOne"))){
			Object id = Util.beans.getId(bean);
			String description = Util.strings.toStringDescription(bean);
			View.getCurrent()
				.println("<html>" +
						"<script language=\"JavaScript\" src=\""+request.getServletRequest().getContextPath()+"/resource/js/util.js\"></script>" +
						"<script language=\"JavaScript\">selecionar('"+id+"', '"+description+"', true);</script>" +
						"</html>");
			return null;
		} else {
			try {
				request.setAttribute(CONSULTAR, true);
				return doEditar(request, beanToForm(bean));
			} catch (CrudException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	@Override
	protected void salvar(WebRequestContext request, BEAN bean) throws Exception {
		genericService.saveOrUpdate(bean);
		request.addMessage("Registro salvo com sucesso.");
	}
	
	

	public Boolean validaExcluirEmMassa(WebRequestContext request, BEAN bean) {
		return true;
	}

}
