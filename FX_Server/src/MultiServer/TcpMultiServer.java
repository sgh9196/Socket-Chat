package MultiServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

// 멀티 채팅 Class
public class TcpMultiServer extends Thread {

	HashMap<String, DataOutputStream> clientMap;
	ArrayList<Button> btn_Names;
	
	ServerSocket serverSocket = null;
	Socket socket = null;
	
	TextArea textArea;
	VBox scr_vBox;
	ProgressBar progressBar;
	
	private int Port;
	
	public TcpMultiServer(int Port) {
		clientMap = new HashMap<String, DataOutputStream>();
		btn_Names = new ArrayList<Button>();
		Collections.synchronizedMap(clientMap);
		this.Port = Port;
	}
	
	public void controllerSet(TextArea textArea, VBox scr_vBox, ProgressBar progressBar) {
		this.textArea = textArea;
		this.scr_vBox = scr_vBox;
		this.progressBar = progressBar;

		
	}
	
	public void sendAllMsg(String msg) {
		
		Iterator it = clientMap.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				DataOutputStream dOut = (DataOutputStream) clientMap.get(it.next());
				//System.out.println(clientMap.get(it));
				dOut.writeUTF(msg);
			} catch(Exception e) {
				System.out.println("sendAllMsg Err -> " + e);
			}
		}
		
	}
	
	@Override
	public void run() {
		
		try {
			
			// 서버 연결
			serverSocket = new ServerSocket(Port);
			this.textArea.setText("Multi Server Open\n");
			
			while(true) {
				// Client 대기
				socket = serverSocket.accept();
				textArea.appendText(socket.getInetAddress() + " : " + socket.getPort() + "\n");
				
				Thread rec = new TcpMultiServerRec(socket);
				rec.start();
			}
			
		} catch(Exception e) {
			System.out.println("TcpMultiServer run Err -> " + e);
		}
		
	}
	
	class TcpMultiServerRec extends Thread {
		
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		
		String name = "";
		
		public TcpMultiServerRec(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch(Exception e) {}

		}
		
		// 클라이언트 리스트 추가
		public void clientListAdd() {
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					
					Button btn = new Button(name);
					btn.setId(name);
					btn.setMaxWidth(105);
					btn.setBackground(null);
					btn_Names.add(btn);
					
					scr_vBox.getChildren().clear();
					scr_vBox.getChildren().addAll(btn_Names);
					
				}
			});
			
		}
		
		// 클라이언트 리스트 삭제
		public void clientListDelete() {
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					for(int i=0; i<btn_Names.size(); i++) {
						if(btn_Names.get(i).getId().equals(name)) {
							btn_Names.remove(i);
							scr_vBox.getChildren().clear();
							scr_vBox.getChildren().addAll(btn_Names);
							return;
						}
					}
				}
			});
			
		}
		
		@Override
		public void run() {
			
			try {
				
				name = in.readUTF();
				sendAllMsg(name + "님이 입장하셨습니다.");
				clientMap.put(name, out);
				
				clientListAdd();
				
				// 현재 접속자 수
				progressBar.setProgress(progressBar.getProgress()+0.1);
				
				while(in!=null) {
					sendAllMsg(in.readUTF());
				}
				
			} catch(Exception e) {
				System.out.println("TcpMultiServerRec run Err -> " + e);
			} finally {
				clientMap.remove(name);
				sendAllMsg(name + "님이 퇴장하셨습니다.");
				textArea.appendText("[" + name + "]님 퇴장\n");
				// 현재 접속자 수
				progressBar.setProgress(progressBar.getProgress()-0.1);
				
				clientListDelete();
			}
			
		}
		
	}
	
}
