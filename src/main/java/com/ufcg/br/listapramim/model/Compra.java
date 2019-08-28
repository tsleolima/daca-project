package com.ufcg.br.listapramim.model;

import org.bson.types.ObjectId;

public class Compra {
	
	private int quantidade;
	private String nomeProduto;
	private ObjectId idProduto;
	
	public Compra(int quantidade, ObjectId idProduto, String nomeProduto) {
		this.quantidade = quantidade;
		this.idProduto = idProduto;
		this.nomeProduto = nomeProduto;
	}

	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public ObjectId getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(ObjectId idProduto) {
		this.idProduto = idProduto;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result + quantidade;
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
		Compra other = (Compra) obj;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		if (quantidade != other.quantidade)
			return false;
		return true;
	}

}
