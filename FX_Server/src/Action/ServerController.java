package Action;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import MultiServer.TcpMultiServer;
import MultiServer.TcpSingleServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerController implements Initializable {

	@FXML private TextArea textArea;
	// ===============================================================
	@FXML private Button btn_Option;
	@FXML private Button btn_Start;
	@FXML private RadioButton opt_Multiple;	
	// ===============================================================
	@FXML private ScrollPane scrollPane;
	@FXML private VBox scr_vBox;
	@FXML private ProgressBar progressBar;
	// ===============================================================
	@FXML private RadioButton opt_Single;
	
	@FXML private HBox hBox_Single;
	@FXML private Label lb_Target;
	@FXML private TextField text_Send;
	@FXML private Button btn_Send;
	// ===============================================================
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btn_Option.setOnAction(event->hendlerBtnOption(event));
		btn_Start.setOnAction(event->hendlerBtnStart(event));
	}
	
	public void hendlerBtnOption(ActionEvent event) {
		
		try {
			
			Stage stage = new Stage();
			
			Parent option = (Parent)FXMLLoader.load(getClass().getResource("../Option.fxml"));
			Scene scene = new Scene(option);
			
			stage.setTitle("Server"); stage.setScene(scene); stage.show();
			
		} catch (IOException e) {
			System.out.println("ServerController hendlerBtnOption Err -> " + e);
		}

	}
	
	public void hendlerBtnStart(ActionEvent event) {
		
		String[] data = new String[3];
		FileManagement fm = new FileManagement();
		
		if(fm.fileReader(data)) {
			
			int Port = Integer.parseInt(data[1]);
			
			if(opt_Multiple.isSelected()) {
				
				data[2] = "true"; fm.fileWrite(data);
				
				TcpMultiServer multiThread = new TcpMultiServer(Port);
				multiThread.controllerSet(this.textArea, this.scr_vBox, this.progressBar);
				multiThread.start();
				
			}
			else if(opt_Single.isSelected()) {
				
				data[2] = "true"; fm.fileWrite(data);
				hBox_Single.setVisible(true);
				
				TcpSingleServer singleThread = new TcpSingleServer(Port);
				singleThread.controllerSet(textArea, hBox_Single, lb_Target, btn_Send, progressBar, scr_vBox, text_Send);
				singleThread.start();
				
			}
		}
		
	}
	
}
