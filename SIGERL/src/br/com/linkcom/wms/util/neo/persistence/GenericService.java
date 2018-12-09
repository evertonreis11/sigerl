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

import br.com.linkcom.wms.util.WmsException;



public class GenericService<BEAN> extends br.com.linkcom.neo.service.GenericService<BEAN> {


	/**
	 * Executa um saveOrUpdate sem criar transação com o banco.
	 * 
	 * @param bean
	 * @author Hugo Ferreira
	 */
	public void saveOrUpdateNoUseTransaction(BEAN bean) {
		if (!(genericDAO instanceof GenericDAO)) {
			throw new WmsException("Seu DAO não estende o GenericDAO da aplicação. " +
					"Por favor, estenda o DAO de br.com.linkcom.sined.util.neo.persistence");
		}

		((GenericDAO<BEAN>)genericDAO).saveOrUpdateNoUseTransaction(bean);
	}

	
}
