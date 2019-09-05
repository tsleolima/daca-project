package com.ufcg.br.listapramim.listadecompra;

import java.util.Comparator;

public class CompraNomeComparator implements Comparator<Compra>{
	
	@Override
	public int compare(Compra o1, Compra o2) {
        return o1.getNomeProduto().compareToIgnoreCase(o2.getNomeProduto());
	}

}
