import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class EchoServer {

	private final static int PORT = 8080;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();
			
			//Time-wait 상태에서 서버 재실행이 가능하게 끔 함
			serverSocket.setReuseAddress( true );
			
			// 2. binding
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind( new InetSocketAddress( localhostAddress, PORT ) );
			System.out.println( "[server] binding " + localhostAddress + ":" + PORT );
			
			//3. accpet 연결 요청 기다림
			while( true ) {
				Socket socket = serverSocket.accept(); //blocking
				Thread thread = new EchoServerReceiveThread( socket );
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 자원정리
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
