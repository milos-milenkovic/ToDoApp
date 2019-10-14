import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

public class Task implements Serializable {
  private int id;
  private String title;
  private Date dueDate;
  private String project;
  private String status;

  public Task(int id, String title, Date dueDate, String project) {
    this.id = id;
    this.title = title;
    this.dueDate = dueDate;
    this.project = project;
    this.status = "Not Done";
  }

  public Task(int id, String title, Date dueDate, String project, String status) {
    this.id = id;
    this.title = title;
    this.dueDate = dueDate;
    this.project = project;
    this.status = status;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getDueDate() {
    return this.dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public String getProject() {
    return this.project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void updateTask(String title, Date dueDate, String project) {
    setTitle(title);
    setDueDate(dueDate);
    setProject(project);
  }

  public void markAsDone() {
    setStatus("Done");
  }

  @Override
  public String toString() {
    String sDueDate = new SimpleDateFormat("yyyy-MM-dd").format(dueDate);
    return String.format(id + "|" + title + "|" + sDueDate + "|" + project + "|" + status);
  }
}
