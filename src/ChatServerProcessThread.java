import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatServerProcessThread extends Thread {
	private Socket socket;
	public ArrayList<ChatServerProcessThread> brothers;
	private PrintWriter pw;
	public ChatServerProcessThread( Socket socket, ArrayList<ChatServerProcessThread> brothers) {
		this.socket = socket;
		this.brothers = brothers;
	}

	@Override
	public void run() {
		InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress = remoteAddress.getHostName();
		int remoteHostPort = remoteAddress.getPort();
		System.out.println( "[server] 연결됨  from " + remoteHostAddress + ":" + remoteHostPort );
		broadcast(socket, brothers);
	}
	

	public void broadcast(Socket socket, ArrayList<ChatServerProcessThread> brothers){
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			String data = null;
			while( true ) {
				data = br.readLine();
				if( data == null ) {
					System.out.println( "[server] closed by client");
					break;
				}
				System.out.println( "[server]" + this.getId() + "thread "  + socket.getInetAddress()+ "번: " + data );
				//pw.println( "당신: "+ data );
				for(ChatServerProcessThread thread : brothers){
					thread.say("[brodcast from server]"+data);
				}
			}
		} catch( SocketException ex ) {
			System.out.println( "[server] 비정상적으로 클라이언트가 종료 되었습니다." );
		} catch( IOException ex ) {
			ex.printStackTrace();
		} finally {
			if( socket != null && socket.isClosed() == false ) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void say(String data){
		pw.println(data);
	}
}
