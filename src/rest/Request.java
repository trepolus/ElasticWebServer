package rest;

import java.util.ArrayList;

/**
 * Created by Lucas on 14.12.2017.
 */
public class Request extends Message {

  ArrayList args;

  public Request(int id, String functionName, ArrayList args) {
    super(id, functionName);
    this.args = args;
  }

  public ArrayList getArgs() {
    return args;
  }

  public void setArgs(ArrayList args) {
    this.args = args;
  }
}
