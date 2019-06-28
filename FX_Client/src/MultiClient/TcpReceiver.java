package MultiClient;

import java.io.DataInputStream;
import java.net.Socket;

import javafx.scene.control.TextArea;

public class TcpReceiver extends Thread {
	
	Socket socket;
	DataInputStream in;
	
	TextArea textArea1;
	
	public TcpReceiver(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch(Exception e) { }
	}
	
	public void controlerSet(TextArea textArea1) {
		this.textArea1 = textArea1;
	}
	
	@Override
	public void run() {
		
		while(in!=null) {
			try {
				textArea1.appendText(in.readUTF() + "\n");
			} catch(Exception e) {
				System.out.println("TcpReceiver run Err -> " + e);
			}
		}
		
	}
	
}
