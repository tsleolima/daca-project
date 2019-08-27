package com.ufcg.br.listapramim.service;

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

import com.ufcg.br.listapramim.model.Compra;
import com.ufcg.br.listapramim.model.ListaDeCompra;
import com.ufcg.br.listapramim.repository.ListaDeCompraRepository;

@Service
public class ListaDeCompraService {

	@Autowired
	private ListaDeCompraRepository listaDeCompraRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	public List<ListaDeCompra> getListas() {
		return this.listaDeCompraRepository.findAll();
	}

	public ListaDeCompra cadastrarLista(ListaDeCompra lista) {
		if(this.listaDeCompraRepository.findProdutoByDescritor(lista.getDescritor()) == null) {
			return this.listaDeCompraRepository.save(lista);
		} 
		return null;
	}

	public ListaDeCompra atualizarLista(ObjectId id, ListaDeCompra lista) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findListaBy_id(id);
		if(listaBuscada != null) {
			listaBuscada.setCompras(lista.getCompras());
			listaBuscada.setDescritor(lista.getDescritor());
			listaBuscada.setLocalCompra(lista.getLocalCompra());
			listaBuscada.setValorFinal(lista.getValorFinal());
			ListaDeCompra listaatt = this.listaDeCompraRepository.save(listaBuscada);
			return listaatt;
		} else return listaBuscada;
		
	}

	public ListaDeCompra removerLista(ObjectId id) {
		ListaDeCompra lista = this.listaDeCompraRepository.findListaBy_id(id);
		if(lista != null) this.listaDeCompraRepository.delete(lista);
		return lista;
	}

	public ListaDeCompra getListaCompra(ObjectId id) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findListaBy_id(id);		
		ArrayList<Compra> comprasOrdenadas = new ArrayList<Compra>();
		comprasOrdenadas.addAll(produtosHigienePessoal(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosLimpeza(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosIndustrializados(listaBuscada.getCompras()));
		comprasOrdenadas.addAll(produtosNaoIndustrializados(listaBuscada.getCompras()));
		
		listaBuscada.setCompras(comprasOrdenadas);		
		return listaBuscada;
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
	
	public ListaDeCompra buscarListaDescritor(String descritor) {
		ListaDeCompra listaBuscada = this.listaDeCompraRepository.findProdutoByDescritor(descritor);
		return listaBuscada;
	}
	
	public ArrayList<ListaDeCompra> buscarListaProduto(ObjectId idProduto) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
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

	public ArrayList<ListaDeCompra> buscarListaData(String data) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
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

	public ListaDeCompra gerarUltimaListaFinalizada() {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
		if (listas.size() > 0) {
			ListaDeCompra ultimaLista = listas.get(listas.size()-1);
			ultimaLista.setLocalCompra("");
			ultimaLista.setValorFinal(0);
			ultimaLista.setDescritor("Lista Automatica Data" + " " + ultimaLista.getDescritor() + " " + formatDateList(formatDateObjectId(ultimaLista)));
			this.cadastrarLista(ultimaLista);
			return ultimaLista;
		}
		return null;
	}

	public ListaDeCompra gerarListaItemCompra(ObjectId idProduto) {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
		for (int i = listas.size()-1; i >= 0 ; i--) {
			for (Compra compra : listas.get(i).getCompras()) {
				if(compra.getIdProduto().equals(idProduto)) {
					ListaDeCompra ultimaListaComProduto = listas.get(listas.size()-1);
					ultimaListaComProduto.setLocalCompra("");
					ultimaListaComProduto.setValorFinal(0);
					ultimaListaComProduto.setDescritor("Lista Automatica Produto" + " " + ultimaListaComProduto.getDescritor() + " " + formatDateList(formatDateObjectId(ultimaListaComProduto)));
					this.cadastrarLista(ultimaListaComProduto);
					return ultimaListaComProduto;
				}
			}
		}
		return null;
	}

	public ListaDeCompra gerarListaProdutosMaisFrequentes() {
		ArrayList<ListaDeCompra> listas = (ArrayList<ListaDeCompra>) this.listaDeCompraRepository.findAll();
		ArrayList<Compra> totalCompras = new ArrayList<Compra>();
		ListaDeCompra listaFinal = new ListaDeCompra();
		for (ListaDeCompra lista : listas) {
			totalCompras.addAll(lista.getCompras());
		}

        Map<ObjectId,List<Compra>> mapCompras = new HashMap<>();
        
        for(Compra c : totalCompras){
            if(!mapCompras.containsKey(c.getIdProduto())){
                mapCompras.put(c.getIdProduto(), new ArrayList<Compra>());                
            }
            mapCompras.get(c.getIdProduto()).add(c);
        }
        
        mapCompras = totalCompras.stream()
        		.collect(Collectors.groupingBy(Compra::getIdProduto));

    
        for (Entry<ObjectId, List<Compra>> entry : mapCompras.entrySet()) {
        	int numeroDeCompras = entry.getValue().size();
        	int mediaDeCompras  = numeroDeCompras / listas.size();
        	if(mediaDeCompras >= (listas.size()/2)) {
        		SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
        		Date date = new Date();        				
        		listaFinal.setDescritor("Lista Automatica Compras" + " " + formatter.format(date).toString());
        		int quantidade = mediaDaQuantidadeCompra(entry.getValue());
        		Compra compraMaisFrequente = new Compra(quantidade, entry.getValue().get(0).getIdProduto());
        		listaFinal.getCompras().add(compraMaisFrequente);
        	}
        }
        
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

}
