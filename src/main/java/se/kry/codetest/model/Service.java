package se.kry.codetest.model;

public class Service {

    String name;
    String hostname;
    Integer port;
    String url;
    String status;
    String created;

    public Service(String name, String hostname, Integer port, String url, String status, String created) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.url = url;
        this.status = status;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
