package Server;

import Client.Client;
import java.util.ArrayDeque;
import java.util.HashMap;
import rest.Request;
import rest.Response;

public class MasterNode implements Runnable{

  ArrayDeque<Worker> workerQueue;
  HashMap<Integer, Client> clientWorkerInfo;

  public MasterNode() {
    this.workerQueue = new ArrayDeque();
    clientWorkerInfo = new HashMap<Integer, Client>();
  }

  @Override
  public void run() {
    initializeWorkers();
  }

  private void initializeWorkers() {
    for (int i = 0; i < 10; i++) {
      Worker worker = new Worker(0);
      workerQueue.add(worker);
    }
  }


  public void handleClientRequest(Client client, Request req){
    while (!workerQueue.isEmpty()){
      try {
        this.wait();
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

  public void sendClientResponse(Client client, Response resp){
    client.handleServerResponse(resp);
  }

  public void sendRequestToWorker(Worker worker, Request req){
    Thread workerThread = new Thread(worker);
    workerThread.start();

    worker.workerHandleRequest(req);
  }
}
