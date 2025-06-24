package Models;

import java.util.List;

public class Book {
    private String id;
    private String titulo;
    private List<String> autores;
    private String sinopse;
    private Integer paginas;
    private Integer paginaAtual;
    private double tempoPorPagina;
    private String andamento;


    public Book(String id, String titulo, List<String> autores, String sinopse, Integer paginas) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.sinopse = sinopse;
        this.paginas = paginas;
        this.paginaAtual = 0;
        this.tempoPorPagina = 0.0;
    }

    public static Book fromItem(Item item) {
        return new Book(
                item.id(),
                item.volumeInfo().title(),
                item.volumeInfo().authors(),
                item.volumeInfo().description(),
                item.volumeInfo().pageCount() != null ? item.volumeInfo().pageCount() : 0
        );
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getAutores() {
        return autores;
    }

    public String getSinopse() {
        return sinopse;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public Integer getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(Integer paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    public double getTempoPorPagina() {
        return tempoPorPagina;
    }

    public String getTempoPorPaginaFormatado() {
        double tempo = getTempoPorPagina();
        int minutos = (int) tempo;
        int segundos = (int) Math.round((tempo - minutos) * 60);

        String minutoStr = minutos == 1 ? "minuto" : "minutos";
        String segundoStr = segundos == 1 ? "segundo" : "segundos";

        return minutos + " " + minutoStr + " e " + segundos + " " + segundoStr;
    }

    public void setTempoPorPagina(double tempoPorPagina) {
        this.tempoPorPagina = tempoPorPagina;

    }

    public Integer getPaginaRestantes() {
         int restantes = paginas - paginaAtual;
        return Math.max(restantes, 0);
    }

    public double getTempoEstimado() {
        return getPaginaRestantes() * tempoPorPagina;
    }

    public String getTempoEstimadoFormatado() {
        double tempo = getTempoEstimado();
        int horas = (int) (tempo / 60);
        int minutos = (int) (tempo % 60);
        return horas + " horas e " + minutos + " minutos";
    }

    public String getAndamento() {
        return andamento;
    }

    public void setAndamento(String andamento) {
        this.andamento = andamento;
    }

    @Override
    public String toString() {
        int horas = (int) (getTempoEstimado() / 60);
        int minutos = (int) (getTempoEstimado() % 60);

        return "Titulo do livro: " + titulo +
                "\nAutor do livro: " + autores +
                "\nSinopse do livro: " + sinopse +
                "\nTotal de páginas: " + paginas +
                "\nPágina atual: " + paginaAtual +
                "\nPáginas restantes: " + getPaginaRestantes() +
                "\nTempo de leitura restante: " + horas + " horas e " + minutos + " minutos" +
                "\nStatus de leitura do livro: " + getAndamento();
    }
}