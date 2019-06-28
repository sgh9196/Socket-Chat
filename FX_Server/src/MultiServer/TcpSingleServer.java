package MultiServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TcpSingleServer extends Thread  {
	
// =======================================================
	HashMap<String, DataOutputStream> clientMap;
	ArrayList<Button> btn_Names;
// =======================================================	
	TextArea textArea;
	VBox scr_vBox;
	HBox hBox_Single;
	Label lb_Target;
	TextField text_Send;
	Button btn_Send;
	ProgressBar progressBar;
// =======================================================	
	int Port = 0;
// =======================================================

	
	public TcpSingleServer(int Port) {
		clientMap = new HashMap<String, DataOutputStream>();
		btn_Names = new ArrayList<Button>();
		Collections.synchronizedMap(clientMap);
		this.Port = Port;
	}
	
	public void controllerSet(TextArea textArea, HBox hBox_Single, Label lb_Target,
									Button btn_Send, ProgressBar progressBar, VBox scr_vBox, TextField text_Send) {
		this.textArea = textArea;
		this.hBox_Single = hBox_Single;
		this.lb_Target = lb_Target;
		this.btn_Send = btn_Send;
		this.progressBar = progressBar;
		this.scr_vBox = scr_vBox;
		this.text_Send = text_Send;
	}

	
	public void sendSelectMsg(String msg) {
		
		//Iterator it = clientMap.keySet().iterator();
		try {
			System.out.println("@@ > " + lb_Target.getText());
			clientMap.get(lb_Target.getText()).writeUTF(msg);
		} catch(Exception e) {
			System.out.println("TcpSingleServer sendSelectMsg Err -> " + e);
		}
	}
	
	@Override
	public void run() {
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			
			serverSocket = new ServerSocket(Port);
			this.textArea.setText("Single Server Open\n");
			
			while(true) {
				socket = serverSocket.accept();
				textArea.appendText(socket.getInetAddress() + " : " + socket.getPort() + "\n");
			
				Thread rec = new TcpSingleServerRec (socket);
				rec.start();
			}
			
		} catch(Exception e) {
			System.out.println("TcpSingleServer run Err -> " + e);
		}
		
	}
	
	class TcpSingleServerRec extends Thread {
		
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		
		String name = "";
	
		public TcpSingleServerRec(Socket socket) {
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
					
					btn.setOnAction(event-> {
						lb_Target.setText(name);
					});
					
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
				clientMap.put(name, out);
				textArea.appendText(name + "님이 서버와 연결되었습니다.\n");
				clientListAdd();
				progressBar.setProgress(progressBar.getProgress()+0.1);
				
				// --
				
				while(in!=null) {
					
					String tmp = in.readUTF();
					textArea.appendText(tmp + "\n");
				
					btn_Send.setOnAction(event-> {
						try {
							String text = "Server >> " + text_Send.getText();
							sendSelectMsg(text);
							lb_Target.setText("");
							text_Send.clear();
						} catch (Exception e) { }
					});
				
				}
				
			} catch(Exception e) {
				System.out.println("TcpSingleServerRec run Err -> " + e);
			} finally {
				clientMap.remove(name);
				//sendAllMsg(name + "님이 퇴장하셨습니다.");
				textArea.appendText("[" + name + "]님 퇴장\n");
				// 현재 접속자 수
				progressBar.setProgress(progressBar.getProgress()-0.1);
				clientListDelete();
			}
			
		}
		
	}
	
}
