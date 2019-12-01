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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ListaDeCompraService {

	@Autowired
	private ListaDeCompraRepository listaDeCompraRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	public Flux<ListaDeCompra> getListas(Users user) {
		Flux<ListaDeCompra> listas = this.listaDeCompraRepository.findByUser(user);		
		return listas;
	}

	public Mono<ListaDeCompra> cadastrarLista(Users user, ListaDeCompra lista) {
		Flux<ListaDeCompra> listasUser = listaDeCompraRepository.findByDescritorAndUser(lista.getDescritor(),user);		
		if(lista.getCompras() == null) lista.setCompras(new ArrayList<Compra>());
		lista.setUser(user);
		
		return listasUser.then(listaDeCompraRepository.save(lista))
				.switchIfEmpty(Mono.just(null));
	
	}

	public Mono<ListaDeCompra> atualizarLista(Users user, ObjectId id, ListaDeCompra lista) {
		Mono<ListaDeCompra> listaBuscada = getListaCompra(user,id);
		
		return listaBuscada.flatMap( l -> {
			l.setCompras(lista.getCompras());
			l.setDescritor(lista.getDescritor());
			l.setLocalCompra(lista.getLocalCompra());
			l.setValorFinal(lista.getValorFinal());
			Mono<ListaDeCompra> updated = listaDeCompraRepository.save(l);
			return updated;
		}).switchIfEmpty(Mono.just(null));
	}

	public Mono<Void> removerLista(Users user,ObjectId id) {
		Mono<ListaDeCompra> lista = getListaCompra(user, id);
		return lista.flatMap(p -> listaDeCompraRepository.deleteById(id))
				.switchIfEmpty(Mono.just(null));
	}
	
	

	public Mono<ListaDeCompra> getListaCompra(Users user,ObjectId id) {
		
		Mono<ListaDeCompra> listaBuscada = listaDeCompraRepository.findBy_id(id);
		Mono<ListaDeCompra> listaUser = listaBuscada.filter( l -> l.getUser().getId().equals(user.getId()));
		
		ArrayList<Compra> compras = listaUser.block().getCompras();
		ArrayList<Compra> comprasOrdenadas = new ArrayList<Compra>();
		comprasOrdenadas.addAll(produtosHigienePessoal(compras));
		comprasOrdenadas.addAll(produtosLimpeza(compras));
		comprasOrdenadas.addAll(produtosIndustrializados(compras));
		comprasOrdenadas.addAll(produtosNaoIndustrializados(compras));
		listaUser.block().setCompras(comprasOrdenadas);
		return listaUser;
	}

	public ArrayList<Compra> produtosIndustrializados(ArrayList<Compra> compras) {
		return this.produtoService.produtosIndustrializados(compras);
	}

	public ArrayList<Compra> produtosNaoIndustrializados(ArrayList<Compra> compras) {
		return this.produtoService.produtosNaoIndustrializados(compras);
	}
	
	public ArrayList<Compra> produtosLimpeza(ArrayList<Compra> compras) {
		return this.produtoService.produtosLimpeza(compras);
	}
	
	public ArrayList<Compra> produtosHigienePessoal(ArrayList<Compra> compras) {
		return this.produtoService.produtosHigienePessoal(compras);
	}
	

	public Flux<ListaDeCompra> buscarListaProduto(Users user, ObjectId idProduto) {
		Flux<ListaDeCompra> listas = getListas(user);
		
		return listas.flatMap( c -> Flux.fromIterable(c.getCompras())
				.filter(p -> p.getIdProduto().equals(idProduto)).cast(ListaDeCompra.class));
	}

	public Flux<ListaDeCompra> buscarListaData(Users user, String data) {
		Flux<ListaDeCompra> listas = getListas(user);
		return listas.filter( l -> data.equals(formatDateList(formatDateObjectId(l))));
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

	public Mono<ListaDeCompra> gerarUltimaListaFinalizada(Users user) {
		Flux<ListaDeCompra> listas = getListas(user);
		Mono<ListaDeCompra> ultimaLista = listas.elementAt(listas.count().block().intValue()-1);
		
		return ultimaLista.map( u -> {
			ListaDeCompra novaLista = new ListaDeCompra();
			novaLista.setCompras(u.getCompras());
			novaLista.setDescritor("Lista Automatica Data" + " " + formatDateList(formatDateObjectId(u)));
			this.cadastrarLista(user,novaLista);
			return novaLista;
		}).switchIfEmpty(null);
	
	}

	public ListaDeCompra gerarListaItemCompra(Users user, ObjectId idProduto) {
		Flux<ListaDeCompra> listas = getListas(user);

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

	public ArrayList<SugestaoDAO> sugerirLocalDeCompra(Users user,ObjectId id) {
		ListaDeCompra lista = this.listaDeCompraRepository.findListaBy_id(id);
		ArrayList<Produto> produtosComPreco = this.produtoService.getProdutosComPreco(user,lista);
		
		ArrayList<SugestaoDAO> sugestoes = new ArrayList<SugestaoDAO>();
		
		if(produtosComPreco.size() > 0) {
			ArrayList<ItemVenda> itensAvenda = new ArrayList<ItemVenda>();
			for (Produto p : produtosComPreco) {
				itensAvenda.addAll(p.getMapaDePrecos());
			}

	        Map<String,List<ItemVenda>> mapItem = new HashMap<>();
	        mapItem = itensAvenda.stream()
	        		.collect(Collectors.groupingBy(ItemVenda::getNomeLocalVenda));
	        
	        for (Entry<String, List<ItemVenda>> entry : mapItem.entrySet()) {
	        	double precoFinal = 0;
	        	for (ItemVenda itemVenda : entry.getValue()) {
					precoFinal += itemVenda.getPreco();
				}
	        	SugestaoDAO s = new SugestaoDAO(entry.getKey(),precoFinal,entry.getValue());
	        	sugestoes.add(s);
	        }
			return sugestoes;
		}else return null;
	}

}
