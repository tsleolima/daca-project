package com.ufcg.br.listapramim.model;

public class ProdutoQtdFixa extends Produto{

	private int quantidade;

	public ProdutoQtdFixa(int quantidade) {
		super();
		this.quantidade = quantidade;
	}
	
	@Override
	public String toString() {
		// 219. Algodão Clemer, higiene pessoal, 300 gramas, Preco: <Supermercado BaratoD+, R$ 2,33;  Baratão, R$ 2,30>
		return this.getId() + ". " + this.getNome() + ", " + this.getCategoria().toString() + ", " + this.getQuantidade()
		+ ", Preco: < >";
	}

	private long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	

	
}
