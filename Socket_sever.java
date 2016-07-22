package hahah;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
//import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;  
import java.net.ServerSocket;  
import java.net.Socket;

//import javax.swing.JOptionPane;

  
public class Socket_sever {  
    private static final int PORT = 60000;  
    private static String line;
    
    public static void main(String[] args) {  
        try {  
        	 File file_acc = new File("C:/Users/Black_Cater/Desktop/out.txt");
             if(!file_acc.exists()){
             	file_acc.createNewFile();
             }
//             JOptionPane.showMessageDialog(null, file_acc.getAbsolutePath());
             FileWriter fileWritter = new FileWriter(file_acc.getAbsolutePath(),true);
             @SuppressWarnings("resource")
			 ServerSocket server = new ServerSocket(PORT);  
             @SuppressWarnings("resource")
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
             while(true){
            	 Socket socket = server.accept();
            	 // 获得输入流
            	 BufferedReader br = new BufferedReader(
            	 		new InputStreamReader(socket.getInputStream()));
            	 line = br.readLine();
            	 System.out.println(line);
                 bufferWritter.write(line.toCharArray());
                 bufferWritter.flush();
            	 br.close();
             }
        }catch (IOException e) {  
            e.printStackTrace();  
        }  
  
    }  
  
}  