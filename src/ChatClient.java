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
		Socket socket = null;
		Scanner scanner = null;
		try {
			socket = new Socket();
			socket.connect( new InetSocketAddress( SERVER_IP, SERVER_PORT ) );
			System.out.println("Successfully connected to chatting server.");
			System.out.println("type 'exit' to leave this room");
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			PrintWriter pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			System.out.print("What is your nicknamne? : ");
			scanner = new Scanner(  System.in  );
			String nickname = scanner.nextLine();
			pw.println(nickname);
			while( true ) {
				Thread thread = new ChatClientProcessThread(br);
				thread.start();
				String message = scanner.nextLine();
				// thread는 server로 부터 메시지를 받아 print한다.
				if( "exit".equals( message ) ) {
					pw.println("exit");
					br.close();
					break;
				}
				pw.println( message );
			}
		} catch(IOException ex) {
			System.out.println("here ");
			ex.printStackTrace();
		} finally {
			scanner.close();
			if(socket != null && socket.isClosed() == false) {
				try {
					socket.close();
					System.out.println("Chatting succeesfully finished.");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}