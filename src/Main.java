import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainMenu = FXMLLoader.load(getClass().getResource("view/menu.fxml"));
        stage.setTitle("CHESS");
        stage.setScene(new Scene(mainMenu, 1200, 800));
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
