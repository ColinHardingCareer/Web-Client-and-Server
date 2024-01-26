import java.net.URL;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
   Program for visiting a web server and echoing the HTTP response.
   Only up to ten lines of any HTTP body are echoed.

 */
public class WebClient {

    public static void main(String[] args) {

        try {

            // Obtain from the command line a URL that the user has
	    // specified and use it to create a Java URL object.
	    // See https://docs.oracle.com/javase/8/docs/api/java/net/URL.html
	    // for methods that URL objects support.
            URL url = new URL(args[0]);

	    // Create a TCP connection to the server having the fully
	    // qualified domain name that is part of this URL at the
	    // specified port (or at the default port for the URL scheme
	    // used, if no port is specified in the URL).
	    // Use methods of URL class to obtain server and port (or
	    // default port) from the URL specified on the command line.

	    String server = null;
	    int port = -1;
	    
		server = url.getHost();
		port = url.getPort() ;
		if(port < 0) port = url.getDefaultPort();
		
	    Socket connection = new Socket(server, port);

	    // Create an HTTP GET request from the specified URL and
	    // send it to the server. First line consists of the HTTP
	    // method (GET), the path to the resource (/ if no path is
	    // specified in the URL), and the version of HTTP being used
	    // (HTTP/1.1).
	    
	    // Sending the request is accomplished by calling
	    // println() on the connection object's output stream
	    // (variable out) and then flushing to ensure that
	    // everything in the print buffer is sent immediately.
	    PrintWriter out = 
		new PrintWriter(connection.getOutputStream());
	    String requestURI = url.getFile();
	    if (requestURI.length() == 0) {
		requestURI = "/"; // If no path in URL, must send '/' to server
	    }
	    out.println("GET " + requestURI + " HTTP/1.1");
	    out.println("Host: " + url.getHost()); // Required header
	    out.println("Connection: close");      // Tells server to close
	                                           // TCP connection when done
	                                           // with its HTTP response
	    out.println(); // Must follow header fields with blank line
	    out.flush();

	    // Create object "in" that can be used to read the server's
	    // HTTP response by calling readLine() method, which returns
	    // one line at a time and null when there are no more lines.
	    // If the string returned by readLine() has length 0, this
	    // indicates that a blank line has been read.
	    BufferedReader in = 
		new BufferedReader(
		    new InputStreamReader(connection.getInputStream()));

	    // Read and print to the console the first line.
	    String firstLine = in.readLine();
	    System.out.println(firstLine);

	    // Read and print all of the header fields to the
	    // console. Follow this with the blank line that
	    // terminates the header fields and precedes the body.

		boolean iF= true;
		while(iF){
			iF = false;
			String line = in.readLine();
			if(!line.isBlank()){
				System.out.println(line);
				iF = true;
			}
		}
		System.out.println();
	    

	    // Output the first ten lines of the body of the response
	    // (or as many lines as the body contains, if it contains fewer
	    // than ten).

		int i =1;
		while(i<11 && in.ready()){
			System.out.println(in.readLine());
			i++;
		}



        }
        catch (Exception e) {
            e.printStackTrace(); // Print any exception messages
        }
        return;
    }
}
