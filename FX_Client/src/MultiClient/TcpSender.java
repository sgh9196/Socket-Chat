package MultiClient;

import java.io.DataOutputStream;
import java.net.Socket;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TcpSender extends Thread {

	Socket socket;
	DataOutputStream out;
	String name;
	
	TextField textField;
	TextArea textArea2;
	Button btn_Send;
	
	public TcpSender(Socket socket, String name) {
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			this.name = name;
		} catch(Exception e) {}
	}
	
	public void controlerSet(TextField textField, TextArea textArea2, Button btn_Send) {
		this.textField = textField;
		this.textArea2 = textArea2;
		this.btn_Send = btn_Send;
	}
	
	@Override
	public void run() {
		
		try {
			out.writeUTF(name);
		} catch(Exception e) {
			System.out.println("TcpSender run Err 1 -> " + e);
		}
		
		while(out!=null) {
		
			btn_Send.setOnAction(event->{
				try {
					String text = textField.getText();
					out.writeUTF(name + " => " + text);
					textArea2.appendText(text + "\n");
					textField.clear();
				} catch(Exception e) {
					System.out.println("TcpServer run Err 2 -> " + e);
				}
			});
		
		}
		
	}
	
}
