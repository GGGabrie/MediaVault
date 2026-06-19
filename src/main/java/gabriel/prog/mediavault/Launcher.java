package gabriel.prog.mediavault;

import gabriel.prog.mediavault.application.MediaVaultApplication;
import gabriel.prog.mediavault.dao.DataBaseConnection;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        try {
            DataBaseConnection.getInstance().getConnection();
            System.out.println("Database connection successful!");
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        Application.launch(MediaVaultApplication.class, args);
    }
}
