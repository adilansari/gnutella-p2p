import java.net.*;
import java.io.*;
import java.util.*;


public class Echoer implements Runnable {
	int i,backlog;
	public Socket tcpClient= null;
	public ServerSocket tcpSocket= null;
	static boolean isConnect= false;
	public Echoer(int tcpPort, int udpPort, int backlog) {
			try {
				ServerSocket tcpSocket = new ServerSocket(tcpPort, backlog);
				System.out.println("Listening on "+ InetAddress.getLocalHost().getHostAddress() + "on TCP port " + tcpSocket.getLocalSocketAddress());
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
			ConnectionList.maintainList(tcpClient);
			//maintain a list here now to have index ipaddr, hostaddr, localport, remoteport;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while (true) {
			try {
				isConnect=false;
				Socket sock1= tcpSocket.accept();
				//Client(InetAddress.getLocalHost().getHostAddress(),tcpSocket.getLocalPort());
				System.out.println("got Conn. request from "+sock1.getInetAddress().getHostAddress() +" "+ sock1.getLocalSocketAddress());
				System.out.println("Connection accepted");
				Thread t4= new Thread ((Runnable)new Messaging(sock1));
				t4.start();
				//new ConnectionList(sock1);
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
	String[] cl_ip=new String[20];
	String[] cl_host=new String[20];
	int[] cl_port=new int[20];
	int[] cl_remote=new int[20];
	int index=0;
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
					//ConnectionList cl= new ConnectionList();
				}
				if (token[0].equalsIgnoreCase("connect")) {
					ConnectionList.AcceptConn(token[1],Integer.parseInt(token[2]));	
					//new Echoer().Client(token[1],Integer.parseInt(token[2]));
				}
				else if (token[0].equalsIgnoreCase("show")) {
					ConnectionList.show();
					//show()
				}
				else if (token[0].equalsIgnoreCase("send")) {
					int k=2;
					String s= "";
					while (token[k] != null) {
						s = s + token[k] + " ";
						k++;
					}
					ConnectionList.Msg(Integer.parseInt(token[1]), s); //send()
				}
				else if (token[0].equalsIgnoreCase("sendto")) {
					//sendto()
				}
				else if (token[0].equalsIgnoreCase("disconnect")) {
					ConnectionList.disconnect(Integer.parseInt(token[1]));
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
	Socket s1,s2;
	String msg;
	public Messaging (Socket s1, String msg) {
		this.s1=s1; //socket from accept();
		this.msg= msg;
		send();
	}
	public Messaging (Socket s2) {
		this.s2=s2;
	}
	void send(){
		try {
			DataOutputStream dout= new DataOutputStream(s1.getOutputStream());
			dout.writeUTF(msg);
			dout.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void run() {
		while (true) {
			try {
				DataInputStream din = new DataInputStream(s2.getInputStream()); 
				String str= din.readUTF();
				System.out.println(str);
				//if (str==null) {
				//din.close();
				//}
			}
			catch (IOException e) {
				e.printStackTrace();
				//din.close();
			}
		}
	}
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

class ConnectionList extends Thread implements Runnable {
	static int index=0;
	static String msg= null;
	static Socket[] tcpClient= new Socket[20];
	static int a=0;
	static void AcceptConn (String ip_addr,int port) {
		new Echoer().Client(ip_addr,port);
	}
	static void Msg (int index1, String msg1) {
		//index= index1;
		//msg= msg1;
		if (tcpClient[index1] != null) {
			new Messaging(tcpClient[index1], msg1);
		}
		else {
			System.out.println("Enter a valid connection id");
		}
	}
	public ConnectionList(Socket sock3) {
		//Echoer.isConnect= sock3.isConnected();
		tcpClient[a]= sock3; //recvd client sock from accept now update array
		a++;
	}
	static void maintainList(Socket sock4){
		System.out.println("now were onset");
		index++;
		tcpClient[index]=sock4;
	}
	static void show() {
		int p=1;
		if (tcpClient[p] != null) {
			System.out.println("Conn ID |	IP	|   Hostname Port |  Local Port | Remote |");
			for (; p<20; p++) {
				if (tcpClient[p] != null) {
					System.out.println(p + "     |     "+ tcpClient[p].getInetAddress().getHostAddress() + "   |    " + tcpClient[p].getInetAddress().getHostName() + "  | " + " " + tcpClient[p].getPort() + "  | " + " " + tcpClient[p].getLocalPort() );
				}
				/*System.out.println("IP address= "+ tcpClient[p].getInetAddress().getHostAddress());
				System.out.println("Host Name= "+ tcpClient[p].getInetAddress().getHostName());
				System.out.println("Local Port= "+ tcpClient[p].getPort());
				System.out.println("Remote= "+ tcpClient[p].getLocalPort());*/
			}
			/*else {
				System.out.println("Not Connected");
			}*/
		}
	}
	static void disconnect (int k) {
		int id;
		System.out.println("index to delete-"+k);
		for (id=k; id<19; id++) {
			try{
				if (tcpClient[id+1] != null) {
					tcpClient[id]=tcpClient[id+1];
				} 
				else {
					tcpClient[id]=null;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	index--;
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
