import Client.Client;
import Server.MasterNode;

/**
 * Created by Lucas on 29.11.2017.
 */
public class Main {

  public static void main(String[] args){

    MasterNode slaveCreator = new MasterNode();
    Client grumpyClient = new Client(slaveCreator, 0);

    Thread mainThread = new Thread(grumpyClient);

    mainThread.start();
  }
}
