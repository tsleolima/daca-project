package com.ufcg.br.listapramim.model;

public class ItemVenda {
	
	private String nomeProduto;
	private String nomeLocalVenda;
	private double preco;
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public String getNomeLocalVenda() {
		return nomeLocalVenda;
	}
	
	public void setNomeLocalVenda(String nomeLocalVenda) {
		this.nomeLocalVenda = nomeLocalVenda;
	}
	
	public double getPreco() {
		return preco;
	}
	
	public void setPreco(double preco) {
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeLocalVenda == null) ? 0 : nomeLocalVenda.hashCode());
		result = prime * result + ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		long temp;
		temp = Double.doubleToLongBits(preco);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemVenda other = (ItemVenda) obj;
		if (nomeLocalVenda == null) {
			if (other.nomeLocalVenda != null)
				return false;
		} else if (!nomeLocalVenda.equals(other.nomeLocalVenda))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (Double.doubleToLongBits(preco) != Double.doubleToLongBits(other.preco))
			return false;
		return true;
	}

}
