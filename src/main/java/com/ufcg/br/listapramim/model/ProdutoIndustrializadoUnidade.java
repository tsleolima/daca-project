package com.ufcg.br.listapramim.model;

public class ProdutoIndustrializadoUnidade extends Produto{

	private int unidades;
	
	public ProdutoIndustrializadoUnidade(int unidades) {
		this.unidades = unidades;
	}
	
	@Override
	public String toString() {
		// 34. Chuchu, alimentos nao industrializados, Preco por quilo: <Supermercado BaratoD+, R$ 1,34;  Baratão, R$ 1,30>
		return this.getId() + ". " + this.getNome() + ", " + "alimentos nao industrializados, Preco por quilo: < >";
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	
}
