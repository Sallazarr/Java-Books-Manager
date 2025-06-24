package Services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Scanner;


public class MenuService {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Scanner sc = new Scanner(System.in);
    private BookService bookService = new BookService();
    public void exibirMenuPrincipal(){
    String option = "";

        while (!option.equals("3")) {
        System.out.println(" ----- Menu ----- ");
        System.out.println("1 - Buscar livro");
        System.out.println("2 - Listar livros");
        System.out.println("3 - Sair");
        option = sc.nextLine();


        switch (option) {
            case "1":
                bookService.buscarLivro();
                break;
            case "2":
                bookService.listarLivros();
                break;
            case "3":
                System.out.println("Finalizando programa...");
                break;
            default:
                System.out.println("Opção inválida, tente novamente");
        }
    }

}
}
