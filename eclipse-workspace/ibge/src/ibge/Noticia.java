package ibge;

import java.io.Serializable;

public class Noticia implements Serializable {
    private String id;
    private String titulo;
    private String introducao;
    private String dataPublicacao;
    private String link;
    private String tipo;
    private String fonte = "IBGE";

    public Noticia(String id, String titulo, String introducao, String dataPublicacao, String link, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.introducao = introducao;
        this.dataPublicacao = dataPublicacao;
        this.link = link;
        this.tipo = tipo;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getIntroducao() { return introducao; }
    public String getDataPublicacao() { return dataPublicacao; }
    public String getLink() { return link; }
    public String getTipo() { return tipo; }

    @Override
    public String toString() {
        return "\nTítulo: " + titulo +
               "\nIntrodução: " + introducao +
               "\nData: " + dataPublicacao +
               "\nLink: " + link +
               "\nTipo: " + tipo +
               "\nFonte: " + fonte;
    }
}