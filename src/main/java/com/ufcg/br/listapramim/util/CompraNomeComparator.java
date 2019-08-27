package com.ufcg.br.listapramim.util;

import java.util.Comparator;

import com.ufcg.br.listapramim.model.Compra;

public class CompraNomeComparator implements Comparator<Compra>{
	
	@Override
	public int compare(Compra o1, Compra o2) {
        return o1.getNomeProduto().compareToIgnoreCase(o2.getNomeProduto());
	}

}
