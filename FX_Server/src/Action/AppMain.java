package Action;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent server = (Parent)FXMLLoader.load(getClass().getResource("../Server.fxml"));
		Scene scene = new Scene(server);
		
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);	
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
// 클라이언트
// 다이얼로그로 ID, IP, PORT