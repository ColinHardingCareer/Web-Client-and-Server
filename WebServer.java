import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.sound.midi.Soundbank;

import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

/** 
    A single-threaded web server (i.e., it can serve one client at a time)
    that echoes the HTTP request back to the client.

    Usage: 
     Start with "java WebServer".  
     Once the "Starting server" message appears, use Firefox (not other
       browsers) or your Java-based web client to visit any URL beginning 
       with 
         http://localhost:8080
     The HTTP response from the server will contain an HTML document
     containing a copy of the HTTP request received by the server.
     Alternatively, you can test using telnet from a command prompt:
         telnet localhost 8080
         > GET /path HTTP/1.1
         > host: localhost
         > 
     Notice the blank line at the end. You should then see an HTTP
     response from the server that has a body which is an HTML document
     containing the HTTP request you entered.

    Note: You must terminate the server (e.g., close the terminal window)
       before starting another copy of it, since only one program can hold
       a port.
 */
public class WebServer {
 
    public static void main (String args[]) {

	// Announce that the server is starting.
	System.out.println("Starting server on port 8080.");
	
        try {
            // Create server socket bound to port 8080
            ServerSocket mySocket = new ServerSocket(8080);

            // Repeat until someone kills us (Ctrl-C at the console)
		while (true) {

			// Listen for a connection
			Socket yourSocket = mySocket.accept();

			// If we reach this line, someone connected to our port
		// and has sent us an HTTP request!
		System.out.println("Connection opened.");

		// Create the connection input and output objects.
		// socketIn allows us to read the request from the client
		// a line at a time by calls to its readLine() method.
		// socketOut allows us to write the response to the client
		// a line at a time by calls to its println() method.
		BufferedReader socketIn = 
		    new BufferedReader(
 		        new InputStreamReader(yourSocket.getInputStream()));
		PrintWriter socketOut = 
		    new PrintWriter(yourSocket.getOutputStream());

		// Read the HTTP request until the blank line following
		// the header fields is reached. Add each line read to
		// the ArrayList inLines.
		ArrayList<String> inLines = new ArrayList<String>();
		String oneLine;
		oneLine = socketIn.readLine();
		inLines.add(oneLine);


		boolean iF= true;
		while(iF){
			iF = false;
			String line = socketIn.readLine();
			if(!line.isBlank()){
				inLines.add(line);
				iF = true;
			}
		}
	
		// Begin sending the HTTP response to the client by writing
		// the with the status line to the TCP connection.
		socketOut.println("HTTP/1.1 200 OK");

                // Send the header fields of the response followed by
		// a blank line.  We will send only two header fields:
		// Date and Content-Type.  The value of the Date field
		// will be the current date and time in a format suitable
		// for HTTP.  The Content-Type is used with HTML files.
                SimpleDateFormat formatter = 
                    new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zzz",
                                         new DateFormatSymbols(Locale.US));
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                String dateTime = formatter.format(new Date());
		socketOut.println("Date: " + dateTime);
		socketOut.println("Content-Type: text/html");
		socketOut.println();

		// Begin to send the body of the response to the
		// client.  This is going to be an HTML document. So
		// the response body will begin with some HTML.
		socketOut.println("<!doctype html>");
		socketOut.println("<html lang='en'>");
		socketOut.println("  <head><meta charset='utf-8'><title>Echo</title></head>");
		socketOut.println("  <body><pre>");

		// The next part of the body of the response will be a
		// copy of the entire HTTP request that was stored
		// earlier in inLines.  Send these lines to the client.
	
		for(String out : inLines) socketOut.println(out);
		
	
		// The response body ends with some HTML.
		socketOut.println("</pre></body></html>");

                // Done with this connection. Make sure that the
		// entire response has been sent, then close the I/O
		// objects and the TCP connection.
		socketOut.flush();
		socketOut.close();
		socketIn.close();
                yourSocket.close();
		System.out.println("Connection closed.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
