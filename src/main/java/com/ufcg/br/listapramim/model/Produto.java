package com.ufcg.br.listapramim.model;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ufcg.br.listapramim.model.enums.Categoria;
import com.ufcg.br.listapramim.model.enums.Tipo;

@Document(collection="produto")
public class Produto {

	private static final String VALORPADRAO = "1";

	@Id
	@NotEmpty(message = "O ID não pode ser vazio")
	public ObjectId _id;
	
	@NotEmpty(message = "O nome não pode ser vazio")
	private String nome;
	
	@NotNull
	private Categoria categoria;
	
	@NotNull
	private Tipo tipo;
	
	private ArrayList<ItemVenda> mapaDePrecos;
	
	private String quantidade;
	
	public Produto () {}
	
	public Produto (String nome, String categoria,String tipo) {
		this.nome = nome;
		this.categoria = Categoria.valueOf(categoria);
		this.tipo = Tipo.valueOf(tipo);
		this.mapaDePrecos = new ArrayList<ItemVenda>();
		this.quantidade = VALORPADRAO;
	}

	public Produto (String nome, String categoria,String tipo, String quantidade) {
		this.nome = nome;
		this.categoria = Categoria.valueOf(categoria);
		this.tipo = Tipo.valueOf(tipo);
		this.mapaDePrecos = new ArrayList<ItemVenda>();
		this.quantidade = quantidade;
	}
	
	public ObjectId getId() {
		return _id;
	}

	public void setId(ObjectId id) {
		this._id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public ArrayList<ItemVenda> getMapaDePrecos() {
		return mapaDePrecos;
	}

	public void setMapaDePrecos(ArrayList<ItemVenda> mapaDePrecos) {
		this.mapaDePrecos = mapaDePrecos;
	}
	
	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Produto other = (Produto) obj;
		if (categoria != other.categoria)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}

}
