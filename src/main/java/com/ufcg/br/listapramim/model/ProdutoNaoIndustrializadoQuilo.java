package com.ufcg.br.listapramim.model;

public class ProdutoNaoIndustrializadoQuilo extends Produto{
	
	private int quilos;
	
	public ProdutoNaoIndustrializadoQuilo(int quilos) {
		this.quilos = quilos;
	}

	@Override
	public String toString() {
		// 87. Queijo minas Dali, alimentos industrializados, Preco: <Baratao, R$ 4,30>
		return this.getId() + ". " + this.getNome() + ", " + "alimentos industrializados, Preco: < >";
	}

	public int getQuilos() {
		return quilos;
	}

	public void setQuilos(int quilos) {
		this.quilos = quilos;
	}
	
}
