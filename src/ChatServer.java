import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

	private final static int PORT = 8080;
	
	public static void main(String[] args) {
		ArrayList<ChatServerProcessThread> brothers = new ArrayList<ChatServerProcessThread>();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress( true );
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind( new InetSocketAddress( localhostAddress, PORT ) );
			System.out.println( "[server] binding " + localhostAddress + ":" + PORT );
			while( true ) {
				Socket socket = serverSocket.accept();
				ChatServerProcessThread thread = new ChatServerProcessThread( socket, brothers);
				brothers.add(thread);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if( serverSocket != null && serverSocket.isClosed() == false ) {
					serverSocket.close();
				}
			} catch( IOException ex ) {
				ex.printStackTrace();
			}
		}
	}
}
