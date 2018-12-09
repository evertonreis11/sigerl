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
package br.com.linkcom.wms.util.neo.persistence;

import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;



/**
 * Classe que deve ser extendida pelas classes que necessitam fazer acesso ao banco de dados
 * @author rogelgarcia
 *
 * @param <BEAN>
 */
public class GenericDAO<BEAN> extends br.com.linkcom.neo.persistence.GenericDAO<BEAN> {
	
	/**
	 * Executa um saveOrUpdate sem criar transação com o banco.
	 * 
	 * @param bean
	 * @author Hugo Ferreira
	 */
	public void saveOrUpdateNoUseTransaction(BEAN bean) {
		SaveOrUpdateStrategy save = save(bean);
		save.useTransaction(false);
		updateSaveOrUpdate(save);
		save.execute();
		getHibernateTemplate().flush();
	}

}
