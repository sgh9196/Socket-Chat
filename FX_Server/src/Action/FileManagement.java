package Action;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManagement {
	
	public boolean fileReader(String[] data) {

		int a = 0;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("./data.txt"));
			String s;

			while ((s = in.readLine()) != null) {
				data[a] = s;
				a++;
			}
			in.close();
			
			return true;
			
		} catch (IOException e) {
			return false;
		}
		
	}
	
	public boolean fileWrite(String[] data) {
		
		try {
			File file = new File("./data.txt");
			FileWriter fw = new FileWriter(file);	
			
			for(int i=0; i<data.length; i++) {
				fw.write(data[i] + "\n");
				fw.flush();
			}
	
			fw.close();
			
			return true;
			
		} catch(Exception et) { 
			return false;
		}
		
		
	}
	
}
