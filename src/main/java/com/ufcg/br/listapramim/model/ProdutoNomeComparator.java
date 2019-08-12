package com.ufcg.br.listapramim.model;

import java.util.Comparator;

public class ProdutoNomeComparator implements Comparator<Produto>{

	@Override
	public int compare(Produto o1, Produto o2) {
        return o1.getNome().compareToIgnoreCase(o2.getNome());
		}
}

