package Action;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import MultiClient.TcpReceiver;
import MultiClient.TcpSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ClientController implements Initializable {

	@FXML private SplitPane splitPane;

	// ===================================================
	@FXML private AnchorPane anchorPane1;
	// ===================================================
	@FXML private TextField text_IP;
	@FXML private TextField text_Port;
	@FXML private TextField text_Nice;

	@FXML private Button btn_Connect;
	@FXML private Button btn_Cancel;

	// ***************************************************
	@FXML private BorderPane borderPane;
	// ***************************************************
	@FXML private TextArea textArea1;
	@FXML private TextArea textArea2;

	@FXML private TextField textField;

	@FXML private Button btn_Stop;
	@FXML private Button btn_Send;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btn_Connect.setOnAction(event->hendlerBtnConnect(event));
		btn_Cancel.setOnAction(event->hendlerBtnCancel(event));
	}

	public void hendlerBtnConnect(ActionEvent event) {
		
		if(!text_IP.getText().equals("") && !text_Port.getText().equals("") && !text_Nice.getText().equals("")) {
			
			String[] data = new String[3];
			
			if(fileReader(data)) {
				if(text_IP.getText().equals(data[0])) {
					if(text_Port.getText().equals(data[1])) {
						
						//anchorPane1.setDisable(true); 
						borderPane.setVisible(true);
						
						try {
							
							Socket socket = new Socket(data[0], Integer.parseInt(data[1]));
							textArea1.setText("채팅방에 입장하였습니다.\n");
							textArea2.setText("채팅방에 입장하였습니다.\n");
				
							TcpSender sender = new TcpSender(socket, text_Nice.getText()); 
							TcpReceiver receiver = new TcpReceiver(socket);
							
							sender.controlerSet(textField, textArea2, btn_Send);
							receiver.controlerSet(textArea1);
							
							sender.start();
							receiver.start();
							
						} catch(Exception e) {
							System.out.println("ClientController Connect Err -> " + e);
						}
						
					}
				}
			}
		}
		
	}
	
	public void hendlerBtnCancel(ActionEvent event) {
		//((Stage)splitPane.getScene().getWindow()).close(); 
		System.exit(0);
	}
	
	// IP, Port 정보 얻기
	public boolean fileReader(String[] data) {
		try {
			
			int a = 0;
			
			BufferedReader in = new BufferedReader(new FileReader("../FX_Server/data.txt"));
			String s;

			while ((s = in.readLine()) != null) {
				data[a] = s; a++;
			}
			in.close();
			
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
