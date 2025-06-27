package ibge;

import java.io.Serializable;
import java.util.*;

public class Usuario implements Serializable {
    private String nome;
    private Set<String> favoritos = new HashSet<>();
    private Set<String> lidas = new HashSet<>();
    private Set<String> paraLerDepois = new HashSet<>();

    public Usuario(String nome) { this.nome = nome; }

    public String getNome() { return nome; }
    public Set<String> getFavoritos() { return favoritos; }
    public Set<String> getLidas() { return lidas; }
    public Set<String> getParaLerDepois() { return paraLerDepois; }

    public void favoritar(String id) { favoritos.add(id); }
    public void desfavoritar(String id) { favoritos.remove(id); }
    public void marcarComoLida(String id) { lidas.add(id); }
    public void adicionarParaLerDepois(String id) { paraLerDepois.add(id); }
    public void removerParaLerDepois(String id) { paraLerDepois.remove(id); }
}