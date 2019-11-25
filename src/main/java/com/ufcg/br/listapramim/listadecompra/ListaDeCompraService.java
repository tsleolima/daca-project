package com.ufcg.br.listapramim.listadecompra;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.br.listapramim.produto.ItemVenda;
import com.ufcg.br.listapramim.produto.Produto;
import com.ufcg.br.listapramim.produto.ProdutoService;
import com.ufcg.br.listapramim.usuario.Users;

@Service
public class ListaDeCompraService {

	@Autowired
	private ListaDeCompraRepository listaDeCompraRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	public List<ListaDeCompra> getListas(Users user) {
		List<ListaDeCompra> listas = this.listaDeCompraRepository.findAll();
		List<ListaDeCompra> listasUser = new ArrayList<ListaDeCompra>();
		for (int s = 0; s < listas.size(); s++) {
			if (listas.get(s).getUser().getId().equals(user.getId())) {
				listasUser.add(listas.get(s));
			}
		}
		return listasUser;
	}

	public ListaDeCompra cadastrarLista(Users user, ListaDeCompra lista) {
		ArrayList<ListaDeCompra> listasUser = (ArrayList<ListaDeCompra>) getListas(user);
		if(findProdutoByDescritor(listasUser, lista.getDescritor()) == null) {
			if(lista.getCompras() == null) lista.setCompras(new ArrayList<Compra>());
			
			lista.setUser(user);
			return this.listaDeCompraRepository.save(lista);
		} 
		return null;
	}

	public ListaDeCompra findProdutoByDescritor(ArrayList<ListaDeCompra> listasUser, String descritor) {
		ListaDeCompra lista = null;
		for (ListaDeCompra listaDeCompra : listasUser) {
			if(listaDeCompra.getDescritor().toLowerCase().equals(descritor.toLowerCase())){
				lista = listaDeCompra;
			}
		}
		return lista;
	}

	public ListaDeCompra atualizarLista(Users user, ObjectId id, ListaDeCompra lista) {
		ListaDeCompra listaBuscada = findListaBy_id(user, id);
		if(listaBuscada != null) {
			listaBuscada.setCompras(lista.getCompras());
			listaBuscada.setDescritor(lista.getDescritor());
			listaBuscada.setLocalCompra(lista.getLocalCompra());
			listaBuscada.setValorFinal(lista.getValorFinal());
			ListaDeCompra listaatt = this.listaDeCompraRepository.save(listaBuscada);
			return listaatt;
		} else return listaBuscada;
		
	}

	public ListaDeCompra removerLista(Users user,ObjectId id) {
		ListaDeCompra lista = findListaBy_id(user, id);
		if(lista != null) this.listaDeCompraRepository.delete(lista);
		return lista;
	}
	
	

//	public ListaDeCompra getListaCompra(Users user,ObjectId id) {
//		
//		ListaDeCompra listaBuscada = findListaBy_id(user,id);		
//		ArrayList<Compra> comprasOrdenadas = new ArrayList<Compra>();
//		
//		comprasOrdenadas.addAll(produtosHigienePessoal(listaBuscada.getCompras()));
//		comprasOrdenadas.addAll(produtosLimpeza(listaBuscada.getCompras()));
//		comprasOrdenadas.addAll(produtosIndustrializados(listaBuscada.getCompras()));
//		comprasOrdenadas.addAll(produtosNaoIndustrializados(listaBuscada.getCompras()));
//			
//		listaBuscada.setCompras(comprasOrdenadas);		
//		return listaBuscada;
//	}
	
	private ListaDeCompra findListaBy_id(Users user,ObjectId id) {
		ArrayList<ListaDeCompra> listasUser = (ArrayList<ListaDeCompra>) getListas(user);
		for (ListaDeCompra lista: listasUser) {
			if(lista.get_id().equals(id) && lista.getUser().getId().equals(user.getId())) {
				return lista;
			}
		}	
		return null;
	}

//	public ArrayList<Compra> produtosIndustrializados(ArrayList<Compra> compras) {
//		return this.produtoService.produtosIndustrializados(compras);
//	}
//
//	public ArrayList<Compra> produtosNaoIndustrializados(ArrayList<Compra> compras) {
//		return this.produtoService.produtosNaoIndustrializados(compras);
//	}
//	
//	public ArrayList<Compra> produtosLimpeza(ArrayList<Compra> compras) {
//		return this.produtoService.produtosLimpeza(compras);
//	}
//	
//	public ArrayList<Compra> produtosHigienePessoal(ArrayList<Compra> compras) {
//		return this.produtoService.produtosHigienePessoal(compras);
//	}
	

	public ArrayList<ListaDeCompra> buscarListaProduto(Users user, ObjectId idProduto) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) getListas(user);
		ArrayList<ListaDeCompra> listasBuscadas = new ArrayList<ListaDeCompra>();
		for (ListaDeCompra listaDeCompra : listas) {
			for ( Compra compra : listaDeCompra.getCompras()) {
				if(compra.getIdProduto().equals(idProduto)) {
					listasBuscadas.add(listaDeCompra);
				}
			}
		}
		return listasBuscadas;
	}

	public ArrayList<ListaDeCompra> buscarListaData(Users user, String data) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) getListas(user);
		ArrayList<ListaDeCompra> listasBuscadas = new ArrayList<ListaDeCompra>();
		for (ListaDeCompra lista : listas) {
			String dateFormat = formatDateObjectId(lista);
			if(data.equals(formatDateList(dateFormat))) {
				listasBuscadas.add(lista);
			}
		}
		
		return listasBuscadas;
	}
	
	private String formatDateObjectId(ListaDeCompra lista) {
		String[] splitedDateList = lista.get_id().getDate().toString().split(" ");
		String dateFormat = splitedDateList[1] + " " + splitedDateList[2] + " " + splitedDateList[5];
		return dateFormat;
	}
	
	private String formatDateList(String string) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(string, formatter);
		return date.toString();
	}

	public ListaDeCompra gerarUltimaListaFinalizada(Users user) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) getListas(user);
		if (listas.size() > 0) {
			ListaDeCompra ultimaLista = listas.get(listas.size()-1);
			ListaDeCompra novaLista = new ListaDeCompra();
			novaLista.setCompras(ultimaLista.getCompras());
			novaLista.setDescritor("Lista Automatica Data" + " " + formatDateList(formatDateObjectId(ultimaLista)));
			this.cadastrarLista(user,novaLista);
			return novaLista;
		}
		return null;
	}

	public ListaDeCompra gerarListaItemCompra(Users user, ObjectId idProduto) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) getListas(user);
		for (int i = listas.size()-1; i >= 0 ; i--) {
			for (Compra compra : listas.get(i).getCompras()) {
				if(compra.getIdProduto().equals(idProduto)) {
					ListaDeCompra ultimaListaComProduto = listas.get(i);
					ListaDeCompra novaLista = new ListaDeCompra();
					novaLista.setCompras(ultimaListaComProduto.getCompras());
					novaLista.setDescritor("Lista Automatica Produto" + " " + formatDateList(formatDateObjectId(ultimaListaComProduto)));
					this.cadastrarLista(user,novaLista);
					return novaLista;
				}
			}
		}
		return null;
	}

	public ListaDeCompra gerarListaProdutosMaisFrequentes(Users user) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) getListas(user);
		ArrayList<Compra> totalCompras = new ArrayList<Compra>();
		ListaDeCompra listaFinal = new ListaDeCompra();
		listaFinal.setCompras(new ArrayList<Compra>());
		for (ListaDeCompra lista : listas) {
			totalCompras.addAll(lista.getCompras());
		}

        Map<ObjectId,List<Compra>> mapCompras = new HashMap<>();
        mapCompras = totalCompras.stream()
        		.collect(Collectors.groupingBy(Compra::getIdProduto));
    
        for (Entry<ObjectId, List<Compra>> entry : mapCompras.entrySet()) {
        	int numeroDeCompras = entry.getValue().size();
    		SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
    		Date date = new Date(); 
        	listaFinal.setDescritor("Lista Automatica Compras" + " " + formatter.format(date).toString());
        	if(numeroDeCompras >= (listas.size()/2)) {
        		int quantidade = mediaDaQuantidadeCompra(entry.getValue());
        		Compra compraMaisFrequente = new Compra(quantidade, entry.getValue().get(0).getIdProduto(), entry.getValue().get(0).getNomeProduto());
        		ArrayList<Compra> comprasListaFinal = listaFinal.getCompras();
        		comprasListaFinal.add(compraMaisFrequente);
        		listaFinal.setCompras(comprasListaFinal);
        	}
        }
        this.cadastrarLista(user,listaFinal);
        return listaFinal;
	}

	private int mediaDaQuantidadeCompra(List<Compra> compras) {
		int quantidade = 0;
		for (Compra compra : compras) {
			quantidade += compra.getQuantidade();
		}
		int media = Math.floorDiv(quantidade,compras.size());
		return media;
	}

//	public ArrayList<SugestaoDAO> sugerirLocalDeCompra(Users user,ObjectId id) {
//		ListaDeCompra lista = this.listaDeCompraRepository.findListaBy_id(id);
//		ArrayList<Produto> produtosComPreco = this.produtoService.getProdutosComPreco(user,lista);
//		
//		ArrayList<SugestaoDAO> sugestoes = new ArrayList<SugestaoDAO>();
//		
//		if(produtosComPreco.size() > 0) {
//			ArrayList<ItemVenda> itensAvenda = new ArrayList<ItemVenda>();
//			for (Produto p : produtosComPreco) {
//				itensAvenda.addAll(p.getMapaDePrecos());
//			}
//
//	        Map<String,List<ItemVenda>> mapItem = new HashMap<>();
//	        mapItem = itensAvenda.stream()
//	        		.collect(Collectors.groupingBy(ItemVenda::getNomeLocalVenda));
//	        
//	        for (Entry<String, List<ItemVenda>> entry : mapItem.entrySet()) {
//	        	double precoFinal = 0;
//	        	for (ItemVenda itemVenda : entry.getValue()) {
//					precoFinal += itemVenda.getPreco();
//				}
//	        	SugestaoDAO s = new SugestaoDAO(entry.getKey(),precoFinal,entry.getValue());
//	        	sugestoes.add(s);
//	        }
//			return sugestoes;
//		}else return null;
//	}

}
