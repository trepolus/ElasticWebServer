package Client;

import Server.MasterNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.Request;
import rest.Response;

public class Client implements Runnable{
  private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

  private MasterNode masterNode;
  private int id;

  public Client(MasterNode masterNode) {
    this.masterNode = masterNode;
  }

  @Override
  public void run() {

  }

  public void handleServerResponse (Response resp){
    //get response parameters
    int workerID = resp.getId();
    int clientID = getId();
    String taskPercentage = resp.getFunctionName();

    //write info to output file
    String path = "./data/taskDocumentationOutput.txt";
    String outputContent = workerID + "," + clientID + "," + taskPercentage;
    writeResponseToFile(path, outputContent);
  }

  //sends request to server
  public void sendRequestToServer (Client client, Request req){
    masterNode.handleClientRequest(client, req);
  }

  private void writeResponseToFile(String path, String response){
      BufferedWriter bw = null;
      FileWriter fw = null;

      try {
        File file = new File(path);

        fw = new FileWriter(file.getAbsoluteFile(), true);
        bw = new BufferedWriter(fw);

        //write response line to existing file
        bw.write(response);
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
