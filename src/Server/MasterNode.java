package Server;

import Client.Client;
import java.util.ArrayDeque;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.Request;
import rest.Response;

public class MasterNode implements Runnable{
  private static final Logger LOGGER = LoggerFactory.getLogger(MasterNode.class);

  ArrayDeque<Worker> workerQueue;
  HashMap<Integer, Client> clientWorkerInfo;
  private final long serverStarted;

  public MasterNode() {
    this.workerQueue = new ArrayDeque();
    clientWorkerInfo = new HashMap<Integer, Client>();
    serverStarted = System.currentTimeMillis();
  }

  @Override
  public void run() {
    initializeWorkers();
  }

  private void initializeWorkers() {
    for (int i = 0; i < 10; i++) {
      Worker worker = new Worker(i, this);
      workerQueue.add(worker);
    }
  }

  public void handleClientRequest(Client client, Request req){
      while (workerQueue.isEmpty()){
        try {
          Thread.sleep(0,1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      Worker worker = workerQueue.poll();
      clientWorkerInfo.put(worker.getId(),client);
      sendRequestToWorker(worker, req);
  }

  public void handleWorkerResponse(Worker worker, Response resp){
    workerQueue.add(worker);
    Client client = clientWorkerInfo.get(worker.getId());
    clientWorkerInfo.remove(worker.getId());
    sendClientResponse(client, resp);
  }

  private void sendClientResponse(Client client, Response resp){
    client.handleServerResponse(resp);
  }

  private void sendRequestToWorker(Worker worker, Request req){
    Thread workerThread = new Thread(worker);
    workerThread.start();

    worker.workerHandleRequest(req);
  }

  public long getServerStarted() {
    return serverStarted;
  }
}
