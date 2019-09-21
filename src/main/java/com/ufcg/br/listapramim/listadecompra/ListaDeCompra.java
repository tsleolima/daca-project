package com.ufcg.br.listapramim.listadecompra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ufcg.br.listapramim.usuario.Users;

@Document(collection="listacompra")
public class ListaDeCompra {

	@Id
	private ObjectId _id;
	
	@NotEmpty(message = "O descritor não pode ser vazio")
	private String descritor;
	
	private double valorFinal;
	
	private String localCompra;
	
	@DBRef
	private Users user;
	
	private ArrayList<Compra> compras;
	
	public ListaDeCompra(String descritor) {
		this.descritor = descritor;
		this.compras = new ArrayList<Compra>();
	}
	
	public ListaDeCompra() {
	}

	public ObjectId get_id() {
		return _id;
	}
	
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	
	public String getDescritor() {
		return descritor;
	}
	
	public void setDescritor(String descritor) {
		this.descritor = descritor;
	}

	public ArrayList<Compra> getCompras() {
		return compras;
	}

	public void setCompras(ArrayList<Compra> compras) {
		this.compras = compras;
	}
	
	public double getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(double valorFinal) {
		this.valorFinal = valorFinal;
	}

	public String getLocalCompra() {
		return localCompra;
	}

	public void setLocalCompra(String localCompra) {
		this.localCompra = localCompra;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((compras == null) ? 0 : compras.hashCode());
		result = prime * result + ((descritor == null) ? 0 : descritor.hashCode());
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
		ListaDeCompra other = (ListaDeCompra) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (compras == null) {
			if (other.compras != null)
				return false;
		} else if (!compras.equals(other.compras))
			return false;
		if (descritor == null) {
			if (other.descritor != null)
				return false;
		} else if (!descritor.equals(other.descritor))
			return false;
		return true;
	}

	public boolean validaProdutosNaLista() {
		Set<Compra> set = new HashSet<Compra>(this.compras);
		if(set.size() != this.compras.size()) return false;
		else return true;
	}
	
}
