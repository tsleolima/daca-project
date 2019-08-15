package com.ufcg.br.listapramim.util;

import java.util.Comparator;

import com.ufcg.br.listapramim.model.Produto;

public class ProdutoNomeComparator implements Comparator<Produto>{

	@Override
	public int compare(Produto o1, Produto o2) {
        return o1.getNome().compareToIgnoreCase(o2.getNome());
		}
}

