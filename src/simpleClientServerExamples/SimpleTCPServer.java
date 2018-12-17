package simpleClientServerExamples;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTCPServer
{
	public static void main(String[] args)
	{
		int port = 55555;
		while (true)
		{
			/*
			 * !!! Wer try-with-resources verwendet muss sich nicht um
			 * das Schließen der Streams und Sockets kümmern !
			 * 
			 * Sie werden automatisch beim Verlassen des try-blocks geschlossen
			 */
			try (ServerSocket ss = new ServerSocket(port))
			{
				while (true)
				{
					try (Socket s = ss.accept();
							ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
							ObjectInputStream ois = new ObjectInputStream(s.getInputStream()))
					{
						//serve the client here ! 
					}

				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
