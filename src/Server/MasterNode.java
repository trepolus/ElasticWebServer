package Server;

import Client.Client;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.Request;
import rest.Response;

public class MasterNode implements Runnable{
  private static final Logger LOGGER = LoggerFactory.getLogger(MasterNode.class);

  //holds workers that are idle
  ConcurrentLinkedDeque<Worker> workerQueue;
  //defines time (in seconds) when server gets started
  private final long serverStarted;
  private HashMap<Integer, Long> workerTimeList;

  public MasterNode() {
    this.workerQueue = new ConcurrentLinkedDeque();
    serverStarted = System.currentTimeMillis();
    workerTimeList = new HashMap<>();

    for (int i = 0; i < 10; i++) {
      workerTimeList.put(i,0L);
    }
  }

  @Override
  public void run() {
    LOGGER.info("Server started......");
    initializeWorkers();
  }

  private void initializeWorkers() {

    //creates 10 workers
    for (int i = 0; i < 10; i++) {
      Worker worker = new Worker(i, this);
      workerQueue.add(worker);
    }
  }

  public void handleClientRequest(Client client, Request req){

    //waits as long as there are no workers in the queue
      while (workerQueue.isEmpty()){
        try {
          Thread.sleep(0,1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      //get free worker out of queue and a sign new task
      Worker worker = workerQueue.poll();
      sendRequestToWorker(worker, req, client);
  }

  public void handleWorkerResponse(Worker worker, Response resp, Client client){
    //worker is done with his task, add him back to the queue
    workerQueue.add(worker);
    //send the response to the client
    sendClientResponse(client, resp);
  }

  private void sendClientResponse(Client client, Response resp){
    client.handleServerResponse(resp);
  }

  private void sendRequestToWorker(Worker worker, Request req, Client client){
    //initialize new request and client variables in worker class
    worker.setCurrentRequest(req);
    worker.setCurrentClient(client);

    //start the task
    Thread workerThread = new Thread(worker);
    workerThread.start();
  }

  public void addTaskLength(int workerID, long taskLength){
    long currentTasklength = workerTimeList.get(workerID);
    this.workerTimeList.put(workerID,taskLength+currentTasklength);
  }

  public void calculateStatistic (){
    BufferedWriter bw = null;
    FileWriter fw = null;

    String path = "./data/statistic.txt";
    ArrayList<String> statisticOutput = new ArrayList<>();

    Long allTaskLength = 0L;

    //calculate tasklength of all tasks
    for (int i = 0; i < this.workerTimeList.size(); i++) {
      allTaskLength += workerTimeList.get(i);
    }

    for (int i = 0; i < this.workerTimeList.size(); i++) {
      Long currentTaskLength = workerTimeList.get(i);

      //calculate percentage of time our task took on the server in relation to the whole time the server is running
      double rawTaskPercentage = (double) currentTaskLength / (double) allTaskLength;

      //round to the last 2 decimals
      String taskPercentage = (String.format(Locale.ROOT,"%.1f", (rawTaskPercentage*100)) + "%");

      //add statistic to outputLine
      String outputLine = "Worker " + i + " has worked for a percentage of: " + taskPercentage + " and a time of " + currentTaskLength + " ms";
      statisticOutput.add(outputLine);
    }

    try {
      File file = new File(path);

      try {
        //delete old file if it exists
        Files.deleteIfExists(file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }

      File outputFile = new File(path);
      outputFile.createNewFile();

      fw = new FileWriter(outputFile.getAbsoluteFile(), true);
      bw = new BufferedWriter(fw);

      //write response line to existing file
      for (String outputLine : statisticOutput){
        bw.write(outputLine);
        bw.newLine();
      }

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

  public long getServerStarted() {
    return serverStarted;
  }
}
