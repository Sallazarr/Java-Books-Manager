package Services;

import Models.Book;
import Models.BookJson;
import Models.Item;
import Utils.FileManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class BookService {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Scanner sc = new Scanner(System.in);
    public void buscarLivro(){
        List<Book> booksList = FileManager.loadBooks();

        while (true) {
            System.out.println("Digite o nome do livro ou '1' para sair (para resultados mais precisos, insira o nome completo do livro):");
            String searchBook = sc.nextLine();

            if (searchBook.equals("1")) {
                System.out.println("Voltando para o menu");
                break;
            }

            String search = URLEncoder.encode(searchBook, StandardCharsets.UTF_8);

            System.out.println("Buscando: " + searchBook);

            GBooksConnection connection = new GBooksConnection();
            String response = connection.searchBook(search);

            try {
                BookJson bookJson = gson.fromJson(response, BookJson.class);

                if (bookJson.items() != null && !bookJson.items().isEmpty()) {
                    System.out.println("Livros encontrados:");
                    for (int i = 0; i < bookJson.items().size(); i++) {
                        System.out.println((i + 1) + " - " + bookJson.items().get(i).volumeInfo().title() + bookJson.items().get(i).volumeInfo().authors() + "\n");
                    }
                    System.out.println("Selecione o número do livro ou 0 para cancelar:");
                    int escolha = Integer.parseInt(sc.nextLine());

                    if (escolha == 0) {
                        System.out.println("Cancelado.");
                        continue;
                    }

                    Item item = bookJson.items().get(escolha - 1);
                    Book bookInfo = Book.fromItem(item);
                    System.out.println("\n\nInformações do livro selecionado:");
                    System.out.println("Título do livro:\n" + bookInfo.getTitulo());
                    System.out.println("Autores:\n" + bookInfo.getAutores());
                    System.out.println("Número de páginas:\n" + bookInfo.getPaginas());
                    System.out.println("Siopse do livro:\n" + bookInfo.getSinopse());

                    if (FileManager.bookAlreadyOnList(booksList, bookInfo)){
                        System.out.println("Livro já está na lista");
                    }else {


                        System.out.println("\nDeseja adicionar esse livro na lista?");
                        System.out.println("1 - Sim");
                        System.out.println("2 - Não");
                        String addBook = sc.nextLine();
                        switch (addBook){
                            case "1":
                                bookInfo.setAndamento("Quero ler");
                                booksList.add(bookInfo);
                                FileManager.saveBooks(booksList);
                                System.out.println("Livro adicionado na sua lista");
                                break;
                            case "2":
                                System.out.println("Livro não foi adicionado a lista");
                        }

                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar os dados do livro.");
                e.printStackTrace();
            }
        }
    }

    public void listarLivros(){
        List<Book> books = FileManager.loadBooks();
        if(books.isEmpty()){
            System.out.println("A lista está vazia");
            return;
        }else {
            System.out.println("----- Seus livros -----");
            for (int i = 0; i < books.size(); i++) {
                System.out.println((i + 1) + " - " + books.get(i).getTitulo() + " - " + books.get(i).getAutores());
            }
            System.out.println("Selecione o livro desejado ou digite 0 para voltar para o menu:");
            String input = sc.nextLine();

            try{
                int index = Integer.parseInt(input);
                if (index == 0){
                    System.out.println("Voltando para o menu...");
                    return;
                }

                if (index > 0 && index <= books.size()){
                    Book selectedBook = books.get(index - 1);
                    System.out.println("----- Informações do Livro -----");
                    System.out.println(selectedBook);
                    String subOption = "";

                    while(!subOption.equals("5")) {
                        System.out.println("\n----- Menu do Livro " + selectedBook.getTitulo() + " -----");
                        System.out.println("Página atual " + selectedBook.getPaginaAtual() + "/" + selectedBook.getPaginas());
                        System.out.println("Páginas restantes " + selectedBook.getPaginaRestantes());
                        System.out.println("Tempo médio por página: " + selectedBook.getTempoPorPaginaFormatado());
                        System.out.println("Tempo estimado para finalizar leitura: " + selectedBook.getTempoEstimadoFormatado());
                        System.out.println("Status de leitura: " + selectedBook.getAndamento());
                        System.out.println("1 - Atualizar progresso");
                        System.out.println("2 - Atualizar tempo médio de leitura");
                        System.out.println("3 - Alterar o status do livro");
                        System.out.println("4 - Remover livro da lista");
                        System.out.println("5 - Voltar");
                        subOption = sc.nextLine();

                        switch (subOption) {
                            case "1":
                                System.out.println("Informe em qual página você está (Total: " + selectedBook.getPaginas() + "):");
                                int paginaAtual = Integer.parseInt(sc.nextLine());
                                if (paginaAtual > selectedBook.getPaginas()){
                                    System.out.println("Página inserida ultrapassa o número de páginas do livro");
                                }else {
                                    selectedBook.setPaginaAtual(paginaAtual);
                                    FileManager.saveBooks(books);
                                }
                                break;
                            case "2":
                                System.out.println("Leia 5 páginas do livro e informe o tempo no formato minutos:segundos (Ex.: 2:24):");
                                String tempoStr = sc.nextLine();

                                try {
                                    String[] partes = tempoStr.split(":");

                                    if (partes.length != 2) {
                                        System.out.println("Formato inválido. Use o formato minutos:segundos (Ex.: 2:24)");
                                        break;
                                    }

                                    int minutos = Integer.parseInt(partes[0].trim());
                                    int segundos = Integer.parseInt(partes[1].trim());

                                    if (minutos < 0 || segundos < 0 || segundos >= 60) {
                                        System.out.println("Valores inválidos. Verifique se segundos estão entre 0 e 59.");
                                        break;
                                    }

                                    double tempoTotal = minutos + (segundos / 60.0);
                                    double tempoPorPagina = tempoTotal / 5;
                                    tempoPorPagina = Math.round(tempoPorPagina * 100.0) / 100.0;

                                    selectedBook.setTempoPorPagina(tempoPorPagina);
                                    FileManager.saveBooks(books);

                                    System.out.println("Tempo médio por página atualizado\n");
                                } catch (NumberFormatException e) {
                                    System.out.println("Entrada inválida. Use apenas números no formato minutos:segundos (Ex.: 2:24).");
                                }
                                break;
                            case "3":
                                System.out.println("Altere o status do livro: " + selectedBook.getTitulo());
                                System.out.println("1 - Lendo");
                                System.out.println("2 - Lido");
                                System.out.println("3 - Quero ler");
                                System.out.println("4 - Abandonado");
                                String statusOption = sc.nextLine();
                                switch (statusOption){
                                    case "1":
                                        selectedBook.setAndamento("Lendo");
                                        System.out.println("Status atualizado para lendo");
                                        FileManager.saveBooks(books);
                                        break;
                                    case "2":
                                        selectedBook.setAndamento("Lido");
                                        System.out.println("Status atualizado para lido");
                                        FileManager.saveBooks(books);
                                        break;
                                    case "3":
                                        selectedBook.setAndamento("Quero ler");
                                        System.out.println("Status atualizado para quero ler");
                                        FileManager.saveBooks(books);
                                        break;
                                    case "4":
                                        selectedBook.setAndamento("Abandonado");
                                        System.out.println("Status atualizado para abandonado");
                                        FileManager.saveBooks(books);
                                        System.out.println("Deseja remover" + selectedBook.getTitulo() + "da lista ?");
                                        System.out.println("1 - Manter livro");
                                        System.out.println("2 - Remover livro");
                                        String statusRemove = sc.nextLine();
                                        switch (statusRemove){
                                            case "1":
                                                System.out.println("Livro foi mantido na lista");
                                                break;
                                            case "2":
                                                System.out.println(selectedBook.getTitulo() + " foi removido da lista");
                                                books.remove(selectedBook);
                                                FileManager.saveBooks(books);
                                                subOption = "5";
                                                break;
                                            default:
                                                System.out.println("Digito inválido, tente novamente");
                                        }
                                        break;
                                }
                                break;
                            case "4":
                                System.out.println("Deseja mesmo remover o livro " + selectedBook.getTitulo() + "?");
                                System.out.println("1 - Não, manter livro");
                                System.out.println("2 - Sim, remover livro");
                                String removeBook = sc.nextLine();
                                switch (removeBook){
                                    case "1":
                                        System.out.println(selectedBook.getTitulo() + " foi mantido");
                                        break;
                                    case "2":
                                        System.out.println(selectedBook.getTitulo() + " foi removido da lista");
                                        books.remove(selectedBook);
                                        FileManager.saveBooks(books);
                                        subOption = "5";
                                        break;
                                }

                                break;

                            case "5":
                                System.out.println("Voltando para menu");
                                break;
                            default:
                                System.out.println("Digito inválido, tente novamente");
                        }
                    }
                }else {
                    System.out.println("Número inválido");
                }

            }catch (NumberFormatException e){
                System.out.println("Entrada inválida, digite um número");
            }
        }
    }
}
