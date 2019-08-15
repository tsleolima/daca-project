package com.ufcg.br.listapramim.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class ListaDeCompra {

	@Id
	private ObjectId _id;
	private String descritor;
	private Date dataCompra;
	
	private ArrayList<Compra> compras;
	
	public ListaDeCompra(String descritor) {
		this.descritor = descritor;
		this.dataCompra = new Date();
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
	
	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public ArrayList<Compra> getCompras() {
		return compras;
	}

	public void setCompras(ArrayList<Compra> compras) {
		this.compras = compras;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((compras == null) ? 0 : compras.hashCode());
		result = prime * result + ((dataCompra == null) ? 0 : dataCompra.hashCode());
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
		if (dataCompra == null) {
			if (other.dataCompra != null)
				return false;
		} else if (!dataCompra.equals(other.dataCompra))
			return false;
		if (descritor == null) {
			if (other.descritor != null)
				return false;
		} else if (!descritor.equals(other.descritor))
			return false;
		return true;
	}

	public boolean isValida() {
		Set<Compra> set = new HashSet<Compra>(this.compras);
		if(set.size() != this.compras.size()) return false;
		else return true;
	}
	
}
