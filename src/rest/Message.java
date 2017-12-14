package rest;

public class Message {

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
}
