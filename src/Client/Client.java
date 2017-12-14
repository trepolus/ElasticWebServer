package Client;

import Server.MasterNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import rest.Request;
import rest.Response;

public class Client implements Runnable{

  private ArrayList<String> codeToExcecute;
  private MasterNode masterNode;

  public Client(MasterNode masterNode) {
    this.codeToExcecute = new ArrayList();
    this.masterNode = masterNode;
  }

  @Override
  public void run() {

    Thread slaveCreator = new Thread(masterNode);
    slaveCreator.start();

    readFile("./data/test.txt");

    for (String s : codeToExcecute){
      System.out.println(s);
    }
  }



  public void readFile (String path){
    BufferedReader br = null;
    try{
      br = new BufferedReader(new FileReader(path));
      int i = 0;
      ArrayList arrayList = new ArrayList();

      //One way of reading the file
      String contentLine = br.readLine();
      while (contentLine != null) {
        Request request = new Request(i, contentLine, arrayList);

        codeToExcecute.add(contentLine);
        masterNode.handleClientRequest(this, request);

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
  }

  public void handleServerResponse (Response resp){

  }
}
