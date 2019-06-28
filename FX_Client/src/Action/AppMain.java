package Action;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("../Client.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setOnCloseRequest(event->{ System.exit(0);});
		
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}