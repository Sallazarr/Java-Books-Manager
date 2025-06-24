package Utils;
import Models.Book;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;


public class FileManager {

    private static final String FILE_PATH = "MyBooks.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // 🔽 Carregar livros do JSON
    public static List<Book> loadBooks() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, listType);
            return books != null ? books : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean bookAlreadyOnList(List<Book> bookList, Book newBook){
        return bookList.stream()
                .anyMatch(book -> book.getId().equalsIgnoreCase(newBook.getId()));
    }


    // 🔼 Salvar livros no JSON
    public static void saveBooks(List<Book> booksList) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(booksList, writer);
            System.out.println("Arquivo MyBooks.json atualizado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo.");
            e.printStackTrace();
        }
    }
}


