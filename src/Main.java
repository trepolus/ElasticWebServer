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

    LOGGER.info("Program started..........");


    //delete old Output File if it still exists and creates a fresh one
    deleteOldOutputFile();

    //initialize masterNode (Server)
    MasterNode masterNode = new MasterNode();
    Thread masterNodeThread = new Thread(masterNode);
    masterNodeThread.start();

    //define Path
    String path = "./data/client_requests.txt";

    //send all requests from client to the Server
    sendAllRequests(path, masterNode);
  }

  public static ArrayList<String[]> sendAllRequests (String path, MasterNode masterNode){
    ArrayList<String[]> clientRequests = new ArrayList<>();
    String lineSeperator = ",";

    BufferedReader br = null;
    try{
      br = new BufferedReader(new FileReader(path));
      int i = 0;

      //read file line by line, seperate by ","
      String contentLine = br.readLine();

      //skip first line (it's just the description)
      contentLine = br.readLine();
      while (contentLine != null) {
        String[] line = contentLine.split(lineSeperator);
        clientRequests.add(line);

        //parse information out of read string
        int clientId = Integer.parseInt(line[0]);
        String functionName = line[1];
        int arguments = Integer.parseInt(line[2]);

        //initialize client
        Client client = new Client(masterNode, clientId);

        //start client thread
        Thread clientThread = new Thread(client);
        clientThread.start();

        //initialize request
        Request request = new Request(i, functionName, arguments);

        //send request from Client to Server
        client.sendRequestToServer(client, request);

        //read next line
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
