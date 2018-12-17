package simpleClientServerExamples;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleTCPClient
{
	public static void main(String[] args)
	{
		String host = "localhost";
		int port = 55555;

		/*
		 * !!! Wer try-with-resources verwendet muss sich nicht um
		 * das Schließen der Streams und Sockets kümmern !
		 * 
		 * Sie werden automatisch beim Verlassen des try-blocks geschlossen
		 */

		try (Socket s = new Socket(host, port);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream()))
		{

			// sage oder frage den server etwas (hier)
			
			/*
			 * bsp.
			 * oos.writeObject(new String("Hallo server"));
			 * oos.flush();
			 * 
			 * String reply = (String)ois.readObject();
			 * 
			 */

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
