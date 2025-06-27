package ibge;

import java.io.*;

public class JsonPersistenciaService {
    private static final String CAMINHO = "usuario.dat";

    public void salvarUsuario(Usuario usuario) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CAMINHO))) {
            oos.writeObject(usuario);
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuário.");
        }
    }

    public Usuario carregarUsuario() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CAMINHO))) {
            return (Usuario) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}