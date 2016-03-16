import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public EchoServerReceiveThread( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//4. 연결 성공
		InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		//String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
		String remoteHostAddress = remoteAddress.getHostName();
		int remoteHostPort = remoteAddress.getPort();
		System.out.println( "[server] 연결됨  from " + remoteHostAddress + ":" + remoteHostPort );
		
		//5. IOStream 받아 오기
		try {
			BufferedReader br = 
					new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			PrintWriter pw = 
					new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );
			
			String data = null;
			while( true ) {
				//6. 데이터 읽기
				data = br.readLine();
				if( data == null ) {
					//정상종료
					System.out.println( "[server] closed by client");
					break;
				}
				System.out.println( "[server] received : " + data );
				
				//7. 쓰기
				pw.println( data );
			}
		} catch( SocketException ex ) {
			System.out.println( "[server] 비정상적으로 클라이언트가 종료 되었습니다." );
		} catch( IOException ex ) {
			ex.printStackTrace();
		} finally {
			//8. 자원정리
			if( socket != null && socket.isClosed() == false ) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
