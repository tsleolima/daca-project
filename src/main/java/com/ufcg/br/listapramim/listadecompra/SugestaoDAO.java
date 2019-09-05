package com.ufcg.br.listapramim.listadecompra;

import java.util.ArrayList;
import java.util.List;

import com.ufcg.br.listapramim.produto.ItemVenda;

public class SugestaoDAO {
	
	private String nomeLocalVenda; 
	
	private double precoTotal;
	
	private ArrayList<ItemVenda> itensaVenda;
		
	public SugestaoDAO(String nomeLocalVenda, double precoTotal, List<ItemVenda> itensaVenda) {
		this.nomeLocalVenda = nomeLocalVenda;
		this.precoTotal = precoTotal;
		this.itensaVenda = (ArrayList<ItemVenda>) itensaVenda;
	}
	
	public String getNomeLocalVenda() {
		return nomeLocalVenda;
	}
	public void setNomeLocalVenda(String nomeLocalVenda) {
		this.nomeLocalVenda = nomeLocalVenda;
	}
	
	public double getPrecoTotal() {
		return precoTotal;
	}
	
	public void setPrecoTotal(double precoTotal) {
		this.precoTotal = precoTotal;
	}
	
	public ArrayList<ItemVenda> getItensaVenda() {
		return itensaVenda;
	}
	
	public void setItensaVenda(ArrayList<ItemVenda> itensaVenda) {
		this.itensaVenda = itensaVenda;
	}

}
