import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.HttpURLConnection;
import java.util.Random;
import java.net.InetAddress;
import java.io.InputStreamReader;
import java.io.BufferedReader;


public class OcuServer {
public Map<String,String> events = new HashMap<String,String>();
public final String PAGESTART = "<html><body><center><h1>Folder Contents</h1></center>><body><center><table>";
public final String PAGEEND = "</table></center></body></html>";

public class Handler implements HttpHandler {
public void handle(HttpExchange xchg) throws IOException {
	URI url = xchg.getRequestURI();
	OcuServer obj = new OcuServer();
	String command = "ls -Rl"; // list all the files in the directory
	String output = obj.executeCommand(command);
	
	synchronized(events) 
	{ 
	events.clear();
	events.put(url.toString(), output); // open browser and type localhost:8080 to see contents of folder    
	}
	
  if (xchg.getRequestMethod().equalsIgnoreCase("GET")) {


	try {
	  StringBuffer sb = new StringBuffer(PAGESTART);
	  synchronized(events) {
		for(String s: events.keySet()) 
		  sb.append("<pre>" + events.get(s) + "</pre>");
	  }
	  String s = sb.toString();
	  xchg.sendResponseHeaders(HttpURLConnection.HTTP_OK, s.length());
	  OutputStream os = xchg.getResponseBody();
	  os.write(s.getBytes());
	  os.close();
	  xchg.close();
	  return;
	} catch(Exception e) {
	  System.out.println("Error " + e);
	  return;
	}
  }
	  
  if (xchg.getRequestMethod().equalsIgnoreCase("POST")) 
  {
	try {
	  Headers requestHeaders = xchg.getRequestHeaders();

  // expect POST as a JSON
	  
	  String kind = requestHeaders.getFirst("Content-type");
	  if(!"application/json".equals(kind)) {
  System.out.println("bad application kind " + kind);
		xchg.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
		xchg.close();
		return;
	  } 
  // expect URI in IFTTT format
	  String[] parts = url.toString().split("/");
	  if(parts.length < 0) {
  System.out.println("bad application uri " + url);
		xchg.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
		xchg.close();
		return;
	  } 
 
  // obtain POST text as a string
	  int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
	  InputStream is = xchg.getRequestBody();
	  byte[] data = new byte[contentLength];
	  int length = is.read(data);
	  String s = new String(data);
	  
	  //
	  // payload recived in following format (id + " " + fileName + " " + JSONString)
	  
	  // seperated the white space and isolate the required information

	  int firstSpace = s.indexOf(" ");
	  int secondSpace = s.indexOf(" ", firstSpace + 1);
	 

	  String idName = s.substring(0,firstSpace);  
	  String fileName = s.substring(firstSpace + 1,secondSpace); 
	  String JSONString = s.substring(secondSpace + 1, s.length());  

	    //System.out.println("Id Name: " + idName); 
	    //System.out.println("File Name: " + fileName); 
	    //System.out.println("JSON String: " + JSONString);
	      System.out.println(fileName + ".json" + " received for " + idName);
	  
	  File folder = new File(idName); //create folder
	  folder.mkdirs();
      File file = new File(folder, fileName + ".json");
      String copy = fileName;
	  int count = 1;
	  while(file.exists()) // checks if file exists, assign new file name (e.g file --> file(1))
	  {
	  	 
		 fileName = copy + "(" + count + ")";
		 file = new File(folder, fileName + ".json");
		 count++;
	  }
	  file.createNewFile(); 
	  FileWriter fw = new FileWriter(file, false);
	  fw.write(JSONString); // fill file with contents
	  fw.close();
	 
	  // update the map
	  xchg.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
	  xchg.close();

	  } catch (Exception e) {
		System.out.println("Error " + e);
	  }
  }
}

}

public void serve(int port) throws IOException {
HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
Handler handler = new Handler();
server.createContext("/", new Handler());
server.start();
}
private String executeCommand(String command) {

	StringBuffer output = new StringBuffer();

	Process p;
	try {
		p = Runtime.getRuntime().exec(command);
		p.waitFor();
		BufferedReader reader = 
						new BufferedReader(new InputStreamReader(p.getInputStream()));

					String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return output.toString();


}
public static void main(String[] args) throws IOException {
final int PORT = 8080;
int port = PORT;
switch(args.length) {
case 0:
  break;
case 1:
  port = Integer.parseInt(args[0]);
  break;
default :
  System.out.println("Usage: OcuServer [port]");
  System.exit(1);
}
(new OcuServer()).serve(port);
/*NOTREACHED*/
}
}
