package rest;

public class Request extends Message {

  int args;

  public Request(int id, String functionName, int args) {
    super(id, functionName);
    this.args = args;
  }

  public int getArgs() {
    return args;
  }
}
