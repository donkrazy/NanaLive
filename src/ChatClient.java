import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_IP = "192.168.1.188";
	private static final int SERVER_PORT = 8080;
	public static void main(String[] args) {
		Scanner scanner = new Scanner(  System.in  );
		Socket socket = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( SERVER_IP, SERVER_PORT ) );
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			PrintWriter pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			while( true ) {
				String message = scanner.nextLine();
				Thread thread = new ChatClientProcessThread(br);
				thread.start();
				if( "exit".equals( message ) ) {
					break;
				}
				pw.println( message );
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
			if(socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}