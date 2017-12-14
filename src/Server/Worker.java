package Server;

import java.util.ArrayList;
import rest.Request;
import rest.Response;

public class Worker implements Runnable{

  private final int id;

  public Worker(int id) {
    this.id = id;
  }

  @Override
  public void run() {

  }

  public void workerNodeInit(ArrayList params){

  }
  public void workerHandleRequest(Request req){
    if (!req.getArgs().isEmpty()){
      workerNodeInit(req.getArgs());
    }

  }
  public void executeWork(Request req, Response resp){

  }
  public void workerSendResponse(Response resp){

  }

  public int getId() {
    return id;
  }
}
