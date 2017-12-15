package Server;

import java.sql.Date;
import java.sql.Time;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest.Request;
import rest.Response;

public class Worker implements Runnable{
  private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

  private final int id;
  MasterNode masterNode;
  public Worker(int id, MasterNode masterNode) {
    this.id = id;
    this.masterNode = masterNode;
  }

  @Override
  public void run() {

  }

  public void workerHandleRequest(Request req){
    LOGGER.info("Worker with ID: " + getId() + " working...");

    executeWork(req);
  }
  private void executeWork(Request req){
    long taskStarted = System.currentTimeMillis();

    switch (req.getFunctionName()){
      case "418Oracle": oracle();
      case  "tellmenow": tellMeNow();
      case "countPrimes": countPrimes(req.getArgs());
    }

    long taskFinished = System.currentTimeMillis();

    //calculate length of task running on Server
    long taskLength = taskFinished - taskStarted;

    //caclulate time the Server is running
    long serverUpTime = taskFinished - masterNode.getServerStarted();

    //calculate percentage of time our task took on the server in relation to the whole time the server is running
    double rawTaskPercentage = (double) taskLength / (double) serverUpTime;

    //round to the last 2 decimals
    String taskPercentage = (String.format(Locale.ROOT,"%.3f", (rawTaskPercentage*100)) + "%");

    LOGGER.info("Percentage of Task" + req.getFunctionName() + "running: " + taskPercentage);

    //create response
    Response response = new Response(getId(), taskPercentage);

    //send response to Master
    workerSendResponse(response);
    LOGGER.info("Worker " + getId() + " finished");
  }
  private void workerSendResponse(Response resp){
    masterNode.handleWorkerResponse(this, resp);
  }

  private void oracle (){
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void tellMeNow (){
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void countPrimes (int arg){
    try {
      Thread.sleep(10*arg);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public int getId() {
    return id;
  }
}
