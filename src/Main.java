import Client.Client;
import Server.MasterNode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.Request;


public class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args){

    //delete old Output File if it still exists and creates a fresh one
    deleteOldOutputFile();

    MasterNode masterNode = new MasterNode();
    Thread masterNodeThread = new Thread(masterNode);
    masterNodeThread.start();

    ArrayList<Client>  clients = initializeClients(masterNode);
    ArrayList<String[]> clientRequests = readRequests("./data/client_requests.txt");

    //initialize requestId, needed for requests
    int requestId = 0;

    //create and send all requests
    for(String[] requestLine : clientRequests){
      // initialize variables for request
      int clientId = Integer.parseInt(requestLine[0]);
      String functionName = requestLine[1];
      int arguments = Integer.parseInt(requestLine[2]);

      //get client
      Client client = clients.get(clientId-1);
      //create request
      Request request = new Request(requestId, functionName, arguments);

      //send request to Server
      client.sendRequestToServer(client, request);
      //increment requestID
      requestId++;
    }

  }

  public static ArrayList<String[]> readRequests (String path){
    ArrayList<String[]> clientRequests = new ArrayList<>();
    String lineSeperator = ",";

    BufferedReader br = null;
    try{
      br = new BufferedReader(new FileReader(path));
      int i = 0;
      ArrayList arrayList = new ArrayList();

      //read file line by line, seperate by ","
      String contentLine = br.readLine();

      //skip first line (it's just the description)
      contentLine = br.readLine();
      while (contentLine != null) {
        String[] line = contentLine.split(lineSeperator);
        clientRequests.add(line);

        contentLine = br.readLine();
        i++;
      }
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    finally
    {
      try {
        if (br != null)
          br.close();
      }
      catch (IOException ioe)
      {
        System.out.println("Error in closing the BufferedReader");
      }
    }
    return clientRequests;
  }

  public static ArrayList<Client> initializeClients (MasterNode masterNode){
    ArrayList<Client> clients = new ArrayList<>();

    for (int i = 1; i <= 10 ; i++) {
      Client client = new Client(masterNode, i);
      Thread clientThread = new Thread(client);
      clientThread.start();

      clients.add(client);
    }
    return clients;
  }

  public static void deleteOldOutputFile (){
    //path of output file
    String path = "./data/taskDocumentationOutput.txt";
    File file = new File(path);
    try {
      //delete old file if it exists
      Files.deleteIfExists(file.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    BufferedWriter bw = null;
    FileWriter fw = null;

    try {
      //create new file
      File outputFile = new File(path);
      outputFile.createNewFile();

      //initializy Buffered Writer
      fw = new FileWriter(file.getAbsoluteFile(), true);
      bw = new BufferedWriter(fw);
    //write first line into new file
    bw.write("WorkerID,Client_handled_id,Percentage_Of_Load");
    bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bw != null)
          bw.close();
        if (fw != null)
          fw.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
