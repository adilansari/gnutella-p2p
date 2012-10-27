This programming assignment is the first phase of the project.

The program to be written is called Echoer, whose features are described below.
•	Echoer takes as arguments a tcp port and a udp port to which it will listen to incoming connection requests and incoming udp packets, respectively:
		java Echoer <tcp-port> <udp-port>

•	Echoer is able to handle up to 7 outgoing tcp connections at the same time. By out-going connection, we mean the connection which this process initiated. Feel free to increase your limit to be more than 7. We will only test your program for up to 7 outgoing connections.

•	Echoer operates somewhat like a Unix shell. It keeps taking user's commands, at the same time watches incoming connection requests and data packets. When appropriate, Echoer prints out diagnostic information to standard output and then goes back to the prompt mode to accept user's inputs.


•	below is a description of all commands Echoer  is supposed to handle, along with the description of associated actions : 
o	info: print out its IP address, host name, TCP (listening) port, UDP port. For example:
	IP address         hostname           udp port      tcp port
	
	192.168.0.3 (funny.cse.buffalo.edu)     4892           43444

o	connect <ip-address> <tcp-port>: try to establish a tcp connection to <ip-address> at port <tcp-port>. For example
		connect 192.168.0.3 99999
both sides then report that the connection is indeed established.
•	show: show all existing outgoing tcp connections in the following format:
conn. ID |      IP       |     hostname       | local port | remote port
-----------------------------------------------------------------------
     1     | 192.168.0.1   | abc.cse.buffalo.edu  | 1234       | 1235
     2     | 192.168.0.2   | def.cse.buffalo.edu  | 1453       | 98234
     3     | 192.168.0.3   | ghi.cse.buffalo.edu  | 1233       | 09823
     4     | 192.168.0.4   | xyz.cse.buffalo.edu  | 1235       | 0823

o	send <conn-id> <message>: send <message> to the connection whose id is <conn-id>. Here, <message> is the entire text (including blank spaces) that follows the first argument. For example,
		send 3 Oh! This assignment is a piece of cake.

o	sendto <ip-address> <udp-port> <message>: send <message> as a udp datagram to <ip-address> at <udp-poprt>.

o	disconnect <conn-id>: disconnect the tcp connection whose id is <conn-id>.


