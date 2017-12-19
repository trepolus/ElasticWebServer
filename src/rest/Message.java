package rest;

public class Message {

  /***
   * This class holds basic message infos and is the super class of Request and Response
   */

  private int id;
  private String functionName;

  public Message(int id, String functionName) {
    this.id = id;
    this.functionName = functionName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", functionName='" + functionName + '\'' +
        '}';
  }
}
