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
		Mono<ListaDeCompra> listasUser = listaDeCompraRepository.findByDescritorAndUser(lista.getDescritor(),user);		
		if(lista.getCompras() == null) lista.setCompras(new ArrayList<Compra>());
		lista.setUser(user);
		return listasUser.filter( p -> p != null ).switchIfEmpty(this.listaDeCompraRepository.save(lista));
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
		}).switchIfEmpty(Mono.empty());
	}

	public Mono<Void> removerLista(Users user,ObjectId id) {
		Mono<ListaDeCompra> lista = getListaCompra(user, id);
		return lista.flatMap(p -> listaDeCompraRepository.deleteById(id))
				.switchIfEmpty(Mono.empty());
	}
	
	

	public Mono<ListaDeCompra> getListaCompra(Users user,ObjectId id) {
		
		Mono<ListaDeCompra> listaBuscada = listaDeCompraRepository.findBy_id(id);
		Mono<ListaDeCompra> listaUser = listaBuscada.filter( l -> l.getUser().getId().equals(user.getId()));
		
		try {
			ArrayList<Compra> compras = listaUser.block().getCompras();
			ArrayList<Compra> comprasOrdenadas = new ArrayList<Compra>();
			comprasOrdenadas.addAll(produtosHigienePessoal(compras));
			comprasOrdenadas.addAll(produtosLimpeza(compras));
			comprasOrdenadas.addAll(produtosIndustrializados(compras));
			comprasOrdenadas.addAll(produtosNaoIndustrializados(compras));
			listaUser.block().setCompras(comprasOrdenadas);
			return listaUser;
		} catch (Exception e) {
			return Mono.empty();
		}
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
				.filter(p -> p.getIdProduto().equals(idProduto))
				.map( l -> c))
				.switchIfEmpty(Flux.empty());
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
		
		return ultimaLista.flatMap( u -> {
			ListaDeCompra novaLista = new ListaDeCompra();
			novaLista.setCompras(u.getCompras());
			novaLista.setDescritor("Lista Automatica Data" + " " + formatDateList(formatDateObjectId(u)));
			Mono<ListaDeCompra> listAdd = this.cadastrarLista(user,novaLista);
			return listAdd;
		}).switchIfEmpty(Mono.empty());
	
	}

	public Mono<ListaDeCompra> gerarListaItemCompra(Users user, ObjectId idProduto) {
		Flux<ListaDeCompra> listasComProduto = buscarListaProduto(user, idProduto);
		
		try {
			ListaDeCompra newList =  new ListaDeCompra();
			ListaDeCompra ultimaList = listasComProduto.blockLast();
			newList.setDescritor("Lista Automatica Produto" + " " + formatDateList(formatDateObjectId(ultimaList)));
			newList.setCompras(ultimaList.getCompras());
			Mono<ListaDeCompra> listAdd = this.cadastrarLista(user, newList);
			return listAdd;
		} catch (Exception e) {
			return Mono.empty();
		}		
	}

	public Flux<ListaDeCompra> getListaDescritor(Users user, String descritor) {
		Flux<ListaDeCompra> listas = getListas(user);

		return listas.filter( l -> l.getDescritor().equals(descritor))
				.switchIfEmpty(Mono.empty());
	}

	public Mono<ListaDeCompra> gerarListaProdutosMaisFrequentes(Users user) {
		Flux<ListaDeCompra> listas = getListas(user);
		ListaDeCompra listaFinal = new ListaDeCompra();
		listaFinal.setCompras(new ArrayList<Compra>());
		
		Flux<Compra> totalCompras = listas.flatMap( c -> Flux.fromIterable(c.getCompras()));
		
        Map<ObjectId,List<Compra>> mapCompras = new HashMap<>();
        mapCompras = totalCompras.toStream()
        		.collect(Collectors.groupingBy(Compra::getIdProduto));
    
        for (Entry<ObjectId, List<Compra>> entry : mapCompras.entrySet()) {
        	int numeroDeCompras = entry.getValue().size();
    		SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
    		Date date = new Date(); 
        	listaFinal.setDescritor("Lista Automatica Compras" + " " + formatter.format(date).toString());
        	if(numeroDeCompras >= (listas.count().block().intValue()/2)) {
        		int quantidade = mediaDaQuantidadeCompra(entry.getValue());
        		Compra compraMaisFrequente = new Compra(quantidade, entry.getValue().get(0).getIdProduto(), entry.getValue().get(0).getNomeProduto());
        		ArrayList<Compra> comprasListaFinal = listaFinal.getCompras();
        		comprasListaFinal.add(compraMaisFrequente);
        		listaFinal.setCompras(comprasListaFinal);
        	}
        	
      
        }
        return this.cadastrarLista(user,listaFinal);
	}

	private int mediaDaQuantidadeCompra(List<Compra> compras) {
		int quantidade = 0;
		for (Compra compra : compras) {
			quantidade += compra.getQuantidade();
		}
		int media = Math.floorDiv(quantidade,compras.size());
		return media;
	}

	public Flux<SugestaoDAO> sugerirLocalDeCompra(Users user,ObjectId id) {
		
		Mono<ListaDeCompra> lista = this.listaDeCompraRepository.findBy_id(id);
		
		try {
			
			Flux<Produto> produtosComPreco = this.produtoService.getProdutosComPreco(user,lista.block());
			
			ArrayList<SugestaoDAO> sugestoes = new ArrayList<SugestaoDAO>();
			
			if(produtosComPreco.count().block().intValue() > 0) {
				Flux<ItemVenda> itensAvenda = produtosComPreco
						.flatMap( c -> Flux.fromIterable(c.getMapaDePrecos()));

		        Map<String,List<ItemVenda>> mapItem = new HashMap<>();
		        mapItem = itensAvenda.toStream()
		        		.collect(Collectors.groupingBy(ItemVenda::getNomeLocalVenda));
		        
		        for (Entry<String, List<ItemVenda>> entry : mapItem.entrySet()) {
		        	double precoFinal = 0;
		        	for (ItemVenda itemVenda : entry.getValue()) {
						precoFinal += itemVenda.getPreco();
					}
		        	SugestaoDAO s = new SugestaoDAO(entry.getKey(),precoFinal,entry.getValue());
		        	sugestoes.add(s);
		        }
				return Flux.fromStream(sugestoes.stream());
			}
			
			return Flux.empty();
			
		} catch (Exception e) {
			return Flux.empty();
		}
	}

}
