package com.ufcg.br.listapramim.util;

import java.util.Comparator;

import com.ufcg.br.listapramim.model.ItemVenda;

public class ItemVendaPrecoComparator implements Comparator<ItemVenda>{

	@Override
	public int compare(ItemVenda o1, ItemVenda o2) {
		if(o1.getPreco() < o2.getPreco()) return -1;
		else if (o1.getPreco() > o2.getPreco()) return 1;
		else return 0;
	}

}
