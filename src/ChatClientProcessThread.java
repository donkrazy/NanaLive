import java.io.BufferedReader;
import java.io.IOException;

public class ChatClientProcessThread extends Thread {
	public BufferedReader br;
	public ChatClientProcessThread(BufferedReader br) {
		this.br = br;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String data = br.readLine();
				if (data == null) {
					break;
				}
				System.out.println(data);
			}
		} catch ( IOException ex) {
			ex.printStackTrace();
		}
	}
	

}
