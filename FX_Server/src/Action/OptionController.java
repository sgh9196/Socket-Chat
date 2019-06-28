package Action;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OptionController implements Initializable {
	
	@FXML private VBox vBox;
	
	@FXML private TextField text_IP;
	@FXML private TextField text_Port;
	@FXML private Button btn_Insert;
	@FXML private Button btn_Cancel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btn_Insert.setOnAction(event -> {
			try { btn_Insert_Action(event); } catch (IOException e) { e.printStackTrace(); }
		});
		btn_Cancel.setOnAction(event-> ((Stage)vBox.getScene().getWindow()).close());
	}

	public void btn_Insert_Action(ActionEvent event) throws IOException {

		String ip = this.text_IP.getText();
		String port = this.text_Port.getText();

		if (ip.equals("") && port.equals("")) { noticeMSG("IP 와 Port 를 입력해주세요."); return; } 
		else if (ip.equals("")) { noticeMSG("IP를 입력해주세요."); return; }
		else if (port.equals("")) { noticeMSG("Port를 입력해주세요."); return; } 
		else {

			String[] data = new String[3];
			
			data[0] = ip; data[1] = port; data[2] = "false";

			new FileManagement().fileWrite(data);
			
			((Stage)vBox.getScene().getWindow()).close(); 
			noticeMSG("등록 완료");
		}

	}
	
	public void noticeMSG(String text) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("알림"); alert.setContentText(text);
		alert.setHeaderText(null); alert.show();
	}

}
