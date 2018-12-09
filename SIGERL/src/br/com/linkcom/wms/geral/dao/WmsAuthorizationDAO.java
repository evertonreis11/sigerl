package br.com.linkcom.wms.geral.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateSystemException;

import br.com.linkcom.neo.authorization.Permission;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.authorization.impl.AuthorizationDAOHibernate;
import br.com.linkcom.neo.exception.AuthorizationException;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.wms.geral.bean.Papel;
import br.com.linkcom.wms.geral.bean.Permissao;
import br.com.linkcom.wms.geral.bean.Tela;
import br.com.linkcom.wms.geral.bean.Usuario;
import br.com.linkcom.wms.geral.bean.Usuariopapel;

public class WmsAuthorizationDAO extends AuthorizationDAOHibernate {
	
	private static final Log log = LogFactory.getLog(WmsAuthorizationDAO.class);

	public User findUserByLogin(String login) {
		return new QueryBuilder<Usuario>(getHibernateTemplate())
				.from(Usuario.class)
				.where("login = ?", login)
				.unique();
	}
	
    public Role[] findUserRoles(User user) {
        List<Role> lista = new QueryBuilder<Role>(getHibernateTemplate())
				.select("papel")
				.from(Usuariopapel.class)
				.join("usuariopapel.papel papel")
				.where("usuariopapel.usuario= ?", user)
				.list();
        return lista.toArray(new Role[lista.size()]);
	}

	public Permissao findPermission(Role role, String controlName) {
		try {
            return new QueryBuilder<Permissao>(getHibernateTemplate())
                        .from(Permissao.class)
                        .joinFetch("permissao.papel papel")
                        .joinFetch("permissao.tela tela")
                        .where("tela.path = ?", controlName)
                        .where("papel = ?", role)
                        .unique();
        } catch (HibernateSystemException e) {
            log.error("Erro: Existe mais de uma função cadastrada com o caminho \"" + controlName + "\".", e);
            throw e;
        }
	}

	public Permission savePermission(String controlName, Role role, Map<String, String> permissionMap) {        
		Permissao permissao;
		Tela tela = new QueryBuilder<Tela>(getHibernateTemplate())
						.from(Tela.class)
						.where("tela.path = ?", controlName)
						.unique();
		if(tela == null){
			tela = new Tela();
			tela.setPath(controlName);
			if (controlName.contains("/")) {
				tela.setDescricao(controlName.substring(controlName.lastIndexOf('/') + 1));
			} else {
				tela.setDescricao(controlName);
			}
			hibernateTemplate.save(tela);
		}
		{
			//verificar se já existe essa permissao no banco
			permissao = new QueryBuilder<Permissao>(getHibernateTemplate())
					.from(Permissao.class)
					.where("permissao.tela.path = ?", controlName)
					.where("permissao.papel = ?", role)
					.unique();
		}
		if(permissao == null){
			//criar a permissao
			permissao = new Permissao();
			Papel papel = (Papel) role;
			permissao.setPapel(papel);
			permissao.setTela(tela);
			permissao.setPermissionMap(permissionMap);
			hibernateTemplate.save(permissao);
			
		} else {
			//atualizar a permissao
			Papel papel = (Papel) role;
			permissao.setPapel(papel);
			permissao.setTela(tela);
			permissao.setPermissionMap(permissionMap);
			hibernateTemplate.update(permissao);
		}
		return permissao;
    }
	
	@Override
	public Role[] findAllRoles() {
		try {
			 List<Papel> listaPapel = new QueryBuilder<Papel>(getHibernateTemplate())
			 .select("papel.cdpapel,papel.nome")
			 .from(Papel.class)
			 .where("papel.administrador is false")
			 .list();
			Role[] toArray = listaPapel.toArray(new Role[listaPapel.size()]);
			return (Role[]) toArray;
		} catch (Exception e) {
			throw new AuthorizationException("Problema ao excecutar query na classe "+AuthorizationDAOHibernate.class.getName()+". Query: from "+Role.class.getName());
		}
	}
}
