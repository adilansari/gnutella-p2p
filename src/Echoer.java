import java.net.*;
import java.io.*;
import java.util.*;


public class Echoer implements Runnable {
	int i,backlog;
	public Socket tcpClient= null;
	public ServerSocket tcpSocket= null;
	public Echoer(int tcpPort, int udpPort, int backlog) {
			try {
				ServerSocket tcpSocket = new ServerSocket(tcpPort, backlog);
				System.out.println("Server connected to "+ InetAddress.getLocalHost().getHostAddress() + "on TCP port " + tcpSocket.getLocalPort() + "and UDP port " + udpPort );
				this.backlog= backlog;
				listening(tcpSocket);
			}
			catch (SocketTimeoutException s) {
				System.out.println("timeout");
			}
			catch (IOException ioe) {
				System.out.println("could not listen on port 10009");
				System.exit(-1);
			}
	}
	public Echoer (ServerSocket s) {
		this.tcpSocket= s;
	}
	public Echoer () {
	
	}
	void listening(ServerSocket s){
			try {
					//i++;
					System.out.println();
					
					Thread t1 = new Thread((Runnable) new Echoer(s));
					t1.start();
					Thread t2= new Thread((Runnable) new AcceptInput());
					t2.start();
					//tcpSocket.accept();
					//System.out.println("Connection accepted");
					//messaging();
				}
				catch (Exception e) {
					System.out.println("Cannot accept connection");
				}
			}
	
	public void Client(String addr, int port) 
	{
		System.out.println("address= "+ addr+ "port= "+ port);
		try {
			tcpClient = new Socket(addr,port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while (true) {
			try {
				System.out.println("Listening on "+ InetAddress.getLocalHost().getHostAddress() + "on TCP port " + tcpSocket.getLocalSocketAddress());
				Socket sock1= tcpSocket.accept();
				//Client(InetAddress.getLocalHost().getHostAddress(),tcpSocket.getLocalPort());
				System.out.println("Connection accepted");
				System.out.println("got Conn. request from "+sock1.getInetAddress().getHostAddress());
				Thread t3= new Thread((Runnable)new Messaging(sock1));
				t3.start();
				//new Messaging(sock1);
				//Now start the messaging thread nad pass this sock1 to tcpClient
				/*String line;
				System.out.println("Write a message");
				DataInputStream din= new DataInputStream(tcpClient.getInputStream());
				line= din.readUTF();
				if (line == null) {
					din.close();
					tcpClient.close();
					}
				System.out.println("Recvd message:" + line);*/
				if (sock1 != null) {
				tcpSocket.close();
				}
			}
			catch (IOException o) {
				System.out.println("Read Failed");
				}
			}
		}
			
		/*catch (IOException i) {
			System.out.println("Last statement");
		}
	}*/
	public static void main(String[] args) {
		new Echoer(Integer.parseInt(args[0]),Integer.parseInt(args[1]),5);
		
	}
}

class AcceptInput implements Runnable {
	String cmd;
	int i,j;
	StringTokenizer st= null;
	
	public void run () {
		//String cmd;
		//int i,j=0;
		//StringTokenizer st= null;
		System.out.println("t2");
		//insert while loop

		try {
			while (true) {
				BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
				cmd= br.readLine();
				i= cmd.length();
				tokenize(cmd,i);
				}
			}
		catch (IOException e) {
				e.printStackTrace();
			}
	}
			void tokenize(String cmd, int i) {
				j=0;
				String[] token= new String[i];
				st= new StringTokenizer(cmd," ");	
				while (st.hasMoreElements()) {
					token [j]=	st.nextToken();
					j++;
				}
				if (token[0].equalsIgnoreCase("connect")) {
					Runnable r1= new ConnectionList(token[1],Integer.parseInt(token[2]));	
					Thread t4= new Thread(r1);
					t4.start();				
					//new Echoer().Client(token[1],Integer.parseInt(token[2]));
					}
				else if (token[0].equalsIgnoreCase("show")) {
					//show()
				}
				else if (token[0].equalsIgnoreCase("send")) {
					int k=2;
					String s=null;
					while (st.hasMoreTokens()) {
						s = s + token[k] + " ";
						k++;
					}
					new ConnectionList(Integer.parseInt(token[1]),s);
					//send()
				}
				else if (token[0].equalsIgnoreCase("sendto")) {
					//sendto()
				}
				else if (token[0].equalsIgnoreCase("disconnect")) {
					
				}
				else if (token[0].equalsIgnoreCase("exit")) {
					System.exit(-1);
				}
				else {
					System.out.println("No such Commands available, valid commands:");
					System.out.println("connect <ip-address> <tcp-port>");
					System.out.println("show");
					System.out.println("send <conn-id> <message>");
					System.out.println("sendto <ip-address> <udp-port> <message>");
					System.out.println("disconnect <conn-id>");
					System.out.println("exit");
					System.out.println();
				}
		}
}
	
class Messaging implements Runnable {
	Socket sock2;
	public Messaging (Socket s) {
		this.sock2=s; //socket from accept();
	}
	public void run() {
		//get socket from token[0].send loop via connectionlist class 
		//call a rcvd and print method
	}
	
	/*void send() {
		try {
			din= new DataInputStream(s.getInputStream());
			recvd=din.readUTF();
			System.out.println("Received msg: " + recvd);
			dout= new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}

class ConnectionList extends Thread implements Runnable {
	int index=0;
	String msg= null;
	public ConnectionList (String ip_addr,int port) {
		new Echoer().Client(ip_addr,port);
		System.out.println("Connected to:");
	}
	public ConnectionList (int index, String msg) {
		this.index= index;
		this.msg= msg;
	}
	public void run () {
		
	}
}
/*class Client implements Runnable {
	Socket tcpClient;
	Client (String addr, int port) throws IOException 
	{
		System.out.println("address= "+ addr+ "port= "+ port);
		tcpClient = new Socket(addr,port);
	}
}*/
