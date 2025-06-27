package ibge;

import java.util.*;
import java.util.stream.Collectors;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final JsonPersistenciaService persistencia;
    private final ApiIBGEService apiService;
    private final Usuario usuario;

    public Menu(JsonPersistenciaService persistencia, ApiIBGEService apiService, Usuario usuario) {
        this.persistencia = persistencia;
        this.apiService = apiService;
        this.usuario = usuario;
    }

    public void iniciar() {
        apiService.buscarNoticias();
        
        while (true) {
            System.out.println("\n--- BLOG DE NOTÍCIAS DO IBGE ---");
            System.out.println("Usuário: " + usuario.getNome());
            System.out.println("1. Buscar notícias");
            System.out.println("2. Ver favoritos");
            System.out.println("3. Ver lidas");
            System.out.println("4. Ver para ler depois");
            System.out.println("5. Sair");
            int opcao = ApiIBGEService.lerOpcaoSegura(scanner, "Escolha: ");

            switch (opcao) {
                case 1 -> buscarNoticias();
                case 2 -> exibirLista(usuario.getFavoritos(), "FAVORITOS");
                case 3 -> exibirLista(usuario.getLidas(), "LIDAS");
                case 4 -> exibirLista(usuario.getParaLerDepois(), "PARA LER DEPOIS");
                case 5 -> {
                    persistencia.salvarUsuario(usuario);
                    System.out.println("Dados salvos. Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void buscarNoticias() {
        System.out.print("Digite termo (título, palavra ou data YYYY-MM-DD): ");
        String termo = scanner.nextLine();

        List<Noticia> resultados = apiService.buscarPorTermo(termo);

        if (resultados.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada.");
            return;
        }

        exibirComOpcoes(resultados);
    }

    private void exibirComOpcoes(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getTitulo));

        for (int i = 0; i < noticias.size(); i++) {
            System.out.println("[" + i + "] " + noticias.get(i).getTitulo());
        }

        int escolha = ApiIBGEService.lerOpcaoSegura(scanner, "Escolha o número da notícia para ver detalhes ou -1 para voltar: ");
        if (escolha >= 0 && escolha < noticias.size()) {
            Noticia n = noticias.get(escolha);
            System.out.println(n);
            exibirOpcoesNoticia(n);
        }
    }

    private void exibirLista(Set<String> listaIds, String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        
        if (listaIds.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada.");
            return;
        }

        List<Noticia> noticias = apiService.buscarPorIds(listaIds);
        
        if (noticias.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada no cache. Atualizando cache...");
            apiService.buscarNoticias();
            noticias = apiService.buscarPorIds(listaIds);
            
            if (noticias.isEmpty()) {
                System.out.println("Ainda não foi possível encontrar as notícias.");
                return;
            }
        }

        System.out.println("Ordenar por:\n1. Título\n2. Data\n3. Tipo\nOutro: Sem ordenação");
        int ordem = ApiIBGEService.lerOpcaoSegura(scanner, "Escolha: ");

        switch (ordem) {
            case 1 -> noticias.sort(Comparator.comparing(Noticia::getTitulo));
            case 2 -> noticias.sort(Comparator.comparing(Noticia::getDataPublicacao));
            case 3 -> noticias.sort(Comparator.comparing(Noticia::getTipo));
        }

        for (int i = 0; i < noticias.size(); i++) {
            System.out.println("[" + i + "] " + noticias.get(i).getTitulo());
        }

        int escolha = ApiIBGEService.lerOpcaoSegura(scanner, 
            "Escolha o número da notícia para ver detalhes ou -1 para voltar: ");
        
        if (escolha >= 0 && escolha < noticias.size()) {
            Noticia n = noticias.get(escolha);
            System.out.println(n);
            exibirOpcoesNoticia(n);
        }
    }

    private void exibirOpcoesNoticia(Noticia n) {
        System.out.println("1. Favoritar/Desfavoritar\n2. Marcar como lida\n3. Ler depois\n0. Voltar");
        int acao = ApiIBGEService.lerOpcaoSegura(scanner, "Escolha: ");
        switch (acao) {
            case 1 -> {
                if (usuario.getFavoritos().contains(n.getId())) {
                    usuario.desfavoritar(n.getId());
                    System.out.println("Notícia removida dos favoritos.");
                } else {
                    usuario.favoritar(n.getId());
                    System.out.println("Notícia adicionada aos favoritos.");
                }
                persistencia.salvarUsuario(usuario);
            }
            case 2 -> {
                usuario.marcarComoLida(n.getId());
                System.out.println("Notícia marcada como lida.");
                persistencia.salvarUsuario(usuario);
            }
            case 3 -> {
                if (usuario.getParaLerDepois().contains(n.getId())) {
                    usuario.removerParaLerDepois(n.getId());
                    System.out.println("Notícia removida da lista 'Para ler depois'.");
                } else {
                    usuario.adicionarParaLerDepois(n.getId());
                    System.out.println("Notícia adicionada à lista 'Para ler depois'.");
                }
                persistencia.salvarUsuario(usuario);
            }
            default -> {}
        }
    }
}