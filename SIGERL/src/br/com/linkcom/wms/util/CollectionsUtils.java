package br.com.linkcom.wms.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionsUtils {

	/**
	 * Cria uma combinação de elementos a partir de uma lista.
	 * 
	 * @param <T>
	 * @param elementType O tipo do elemento, usado para criar os arrays.
	 * @param elementos A lista de elementos que devem ser combinados.
	 * @param n O tamanho da combinação.
	 * @return
	 */
	public static <T> List<T[]> combination(Class<T> elementType, Collection<T> elementos, int n) {
		List<T[]> combinacoes = new ArrayList<T[]>();

		@SuppressWarnings("unchecked")
		T[] elementosArray = (T[]) Array.newInstance(elementType, elementos.size());
		elementosArray = elementos.toArray(elementosArray);

		int[] indices;
		CombinationGenerator x = new CombinationGenerator(elementosArray.length, n);
		while (x.hasMore()) {
			@SuppressWarnings("unchecked")
			T[] combination = (T[]) Array.newInstance(elementType, n);

			indices = x.getNext();
			for (int i = 0; i < indices.length; i++) {
				combination[i] = elementosArray[indices[i]];
			}
			
			combinacoes.add(combination);
		}

		return combinacoes;
	}
}
