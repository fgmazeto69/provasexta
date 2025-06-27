package ibge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiIBGEService {
    private List<Noticia> cacheNoticias = new ArrayList<>();

    public List<Noticia> buscarNoticias() {
        try {
            URL url = new URL("https://servicodados.ibge.gov.br/api/v3/noticias/?qtd=100");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null) {
                response.append(linha);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray itens = json.getJSONArray("items");

            cacheNoticias.clear();

            for (int i = 0; i < itens.length(); i++) {
                JSONObject item = itens.getJSONObject(i);
                Noticia noticia = new Noticia(
                    String.valueOf(item.getInt("id")),
                    item.getString("titulo"),
                    item.getString("introducao"),
                    item.getString("data_publicacao"),
                    item.getString("link"),
                    item.optString("tipo", "")
                );
                cacheNoticias.add(noticia);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar notícias: " + e.getMessage());
        }
        return cacheNoticias;
    }

    public List<Noticia> buscarPorTermo(String termo) {
        List<Noticia> resultados = new ArrayList<>();
        try {
            String encoded = URLEncoder.encode(termo, "UTF-8");
            URL url = new URL("https://servicodados.ibge.gov.br/api/v3/noticias/?busca=" + encoded + "&qtd=100");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null) {
                response.append(linha);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray itens = json.getJSONArray("items");

            for (int i = 0; i < itens.length(); i++) {
                JSONObject item = itens.getJSONObject(i);
                Noticia noticia = new Noticia(
                    String.valueOf(item.getInt("id")),
                    item.getString("titulo"),
                    item.getString("introducao"),
                    item.getString("data_publicacao"),
                    item.getString("link"),
                    item.optString("tipo", "")
                );
                resultados.add(noticia);
                cacheNoticias.add(noticia);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar por termo: " + e.getMessage());
        }
        return resultados;
    }

    public List<Noticia> buscarPorIds(Set<String> ids) {
        if (cacheNoticias.isEmpty()) {
            buscarNoticias();
        }
        
        return cacheNoticias.stream()
            .filter(n -> ids.contains(n.getId()))
            .collect(Collectors.toList());
    }

    public static int lerOpcaoSegura(Scanner scanner, String mensagem) {
        int opcao = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
        return opcao;
    }
}