package Server;

import Client.Client;
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

  public MasterNode() {
    this.workerQueue = new ConcurrentLinkedDeque();
    serverStarted = System.currentTimeMillis();
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

  public long getServerStarted() {
    return serverStarted;
  }
}
