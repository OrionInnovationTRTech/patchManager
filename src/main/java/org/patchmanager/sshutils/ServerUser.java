package org.patchmanager.sshutils;

public class ServerUser {
  private String username = "";
  private String ip = "";
  private String password = "";
  private int port = 22;

  /**
   * An object to ease the process of connecting a server, default port is 22
   * @param username
   * @param ip
   * @param password
   * @param port
   */
  public ServerUser(String username, String ip, String password, int port){
    this.setUsername(username);
    this.setIp(ip);
    this.setPassword(password);
    this.setPort(port);
  }

  /**
   * An object to ease the process of connecting a server, default port is 22,
   * another constructor with port is also available
   * @param username
   * @param ip
   * @param password
   */
  public ServerUser(String username, String ip, String password){
    this.setUsername(username);
    this.setIp(ip);
    this.setPassword(password);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
