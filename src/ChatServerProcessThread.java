import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class ChatServerProcessThread extends Thread {
	private Socket socket;
	public LinkedList<ChatServerProcessThread> brothers;
	private PrintWriter pw;
	public ChatServerProcessThread( Socket socket, LinkedList<ChatServerProcessThread> brothers) {
		this.socket = socket;
		this.brothers = brothers;
	}

	@Override
	public void run() {
		InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		String ip = remoteAddress.getAddress().toString();
		int port = remoteAddress.getPort();
		consoleLog(  ip + ":" + port + " joined.");
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			String data = null;
			setName(br.readLine());
			consoleLog(ip + " set nickname as " + getName());
			broadcast( " just joined.");
			while( true ) {
				data = br.readLine();
				if( data == null ) {
					consoleLog(getName() + " leaved.");
					System.out.println(data);
					break;
				}
				if( data.equals("exit")){
					consoleLog(getName() + " leaved.");
					brothers.remove(this);
					break;
				}
				consoleLog( this.getName() + ": " + data );
				// data 전파
				synchronized (brothers) {
					for(ChatServerProcessThread thread : brothers){
						thread.broadcast(": " + data);
					}
				}
			}
		} catch( SocketException ex ) {
			consoleLog(getName()+": 비정상적으로 클라이언트가 종료 되었습니다." );
			for(ChatServerProcessThread thread : brothers){
				thread.broadcast( thread.getName() + " leaved" );
			}
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
	

	public void broadcast(String data){
		synchronized (brothers) {
			for(ChatServerProcessThread thread : brothers){
				thread.pw.println(getName() + data);
			}
		}
	}
	
	public static void consoleLog(String str){
		System.out.println("[server] " + str);
	}
	
}
