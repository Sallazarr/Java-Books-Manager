import Models.Book;
import Services.MenuService;
import Utils.FileManager;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        MenuService menuService = new MenuService();

        menuService.exibirMenuPrincipal();

    }
}
