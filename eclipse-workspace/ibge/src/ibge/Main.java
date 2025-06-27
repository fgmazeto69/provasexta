package ibge;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonPersistenciaService persistencia = new JsonPersistenciaService();
        ApiIBGEService apiService = new ApiIBGEService();

        Usuario usuario = persistencia.carregarUsuario();
        if (usuario == null) {
            System.out.print("Digite seu nome/apelido: ");
            String nome = scanner.nextLine();
            usuario = new Usuario(nome);
        }

        Menu menu = new Menu(persistencia, apiService, usuario);
        menu.iniciar();
    }
}