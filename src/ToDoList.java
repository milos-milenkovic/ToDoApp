import java.util.*;
import java.text.*;
import java.io.*;

public class ToDoList{


  public static void main(String[] args) throws ParseException{
    System.out.println("*** Welcome to 2Doist ***");
    ArrayList<Task> toDoList = (ArrayList<Task>) readFromFileIntoList2("taskList.txt");
    // create a scanner so we can read the command-line input
    Scanner sc = new Scanner(System.in);
    String choice;
    String listChoice;
    String title;
    String sDate;
    Date dueDate = null;
    String project;
    String sTaskId;
    String currentStatus;
    int completeTasks;
    int incompleteTasks;
    // accept the choice and call the appropriate methods until the user types 6 to quit
    do {
      System.out.println();
      completeTasks = getNumberOfCompleteTasks(toDoList);
      incompleteTasks = getNumberOfIncompleteTasks(toDoList);
      if (completeTasks + incompleteTasks != 0) {
        System.out.println("You have " + incompleteTasks + " incomplete and " + completeTasks + " complete tasks!");
      }
      else {
        System.out.println("To Do List is empty!");
      }
      printMenu();
      choice = sc.nextLine();
      switch(choice){
        case "1":
          System.out.print("List all tasks by (D)ate or (P)roject?: ");
          do {
            listChoice = sc.nextLine();
            if (listChoice.equals("Q")) {
              break;
            }
            if (listChoice.matches("D|P")){
              listAllTasks(listChoice, toDoList);
              pressEnterToContinue();
            }
            else {
              System.out.print("Please enter (D) or (P) to list tasks by Date or Project or (Q) "
                               + "to Quit and return to main menu: ");
            }
          }
          while (!listChoice.matches("D|P"));
          System.out.println();
          break;
        case "2":
          System.out.print("Enter title: ");
          title = sc.nextLine();
          System.out.print("Enter due date (e.g. 2019-09-25): ");
          sDate = sc.nextLine();

          do {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (isValidDate(sDate)) {
              dueDate = dateFormat.parse(sDate);
            }
            else {
              System.out.print("Please enter date in a valid format (e.g. 2019-09-25) or (Q) "
                                 + "to Quit and return to main menu: ");
              sDate = sc.nextLine();
            }
            if (sDate.equals("Q")) {
              break;
            }
          }
          while (!isValidDate(sDate));

          if (sDate.equals("Q")) {
            break;
          }

          System.out.print("Enter Project: ");
          project = sc.nextLine();
          Task t = new Task(getNextTaskId(toDoList), title, dueDate, project);
          toDoList.add(t);
          System.out.println("Task successfully added to the list!");
          System.out.println();
          pressEnterToContinue();
          break;
        case "3":
          System.out.print("Enter Id of the task you want to edit: ");
          sTaskId = sc.nextLine();
          Task t1 = getTaskById(sTaskId, toDoList);
          if (t1 == null) {
            System.out.println("Task with that Id is not in the list!");
            break;
          }

          System.out.print("Enter new title or press Enter (Return) to keep current one: ");
          //sc.nextLine();
          title = sc.nextLine();
          if (title.isEmpty()) {
            title = t1.getTitle();
          }
          System.out.print("Enter new due date (e.g. 2019-09-25) or press Enter to keep current one: ");
          sDate = sc.nextLine();

          do {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (isValidDate(sDate) && !sDate.isEmpty()) {
              dueDate = dateFormat.parse(sDate);
            }
            else if (sDate.isEmpty()) {
              dueDate = t1.getDueDate();
              break;
            }
            else {
              System.out.print("Please enter date in a valid format (e.g. 2019-09-25) or "
                               + "press Enter to keep current one: ");
              sDate = sc.nextLine();
            }

          }
          while (!isValidDate(sDate));

          System.out.print("Enter new project or press Enter to keep current one:");
          project = sc.nextLine();
          if (project.isEmpty()) {
            project = t1.getProject();
          }

          if (title.isEmpty() && sDate.isEmpty() && project.isEmpty()) {
            System.out.print("No changes were made!");
          }
          else {
            t1.updateTask(title, dueDate, project);
            System.out.print("Task updated!");
          }
          System.out.println();
          break;
        case "4":
          System.out.print("Enter Id of the task you want to mark as done: ");
          sTaskId = sc.nextLine();
          Task t2 = getTaskById(sTaskId, toDoList);
          if (t2 == null) {
            System.out.println("Task with that Id is not in the list!");
            break;
          }

          currentStatus = t2.getStatus();
          //currentStatus = "Not Done";

          if (currentStatus.equals("Not Done")) {
            t2.markAsDone();
            System.out.println("Task marked as Done!");
          }
          else {
            System.out.println("Task is already Done!");
          }

          System.out.println();
          break;
        case "5":
          System.out.print("Enter Id of the task you want to remove from the list: ");
          sTaskId = sc.nextLine();
          Task t3 = getTaskById(sTaskId, toDoList);
          if (t3 == null) {
            System.out.println("Task with that Id is not in the list!");
            break;
          }
          toDoList.remove(t3);
          System.out.println("Task was successfully removed from the list!");
          break;
        case "6":
          writeToFile2("taskList.txt", toDoList);
          System.out.println("Bye bye!");
          break;
        default:
          System.out.println("Please enter a valid option!");
          System.out.println();
      }
    }
    while(!choice.equals("6"));
  }

  private static void printMenu() {
    System.out.println("Pick an option:");
    System.out.println("1 - Show Task List (by Date or Project)");
    System.out.println("2 - Add New Task");
    System.out.println("3 - Edit Task");
    System.out.println("4 - Mark Task as Done");
    System.out.println("5 - Remove Task from Task List");
    System.out.println("6 - Save and Exit the Program");
    System.out.println("Your choice: ");
  }

  private static boolean isValidDate(String s) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(s.trim());
    } catch (ParseException pe) {
      return false;
    } catch(NullPointerException npe) {
      return false;
    }
    return true;
  }

  private static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch(NumberFormatException nfe) {
      return false;
    } catch(NullPointerException npe) {
      return false;
    }
    return true;
  }

  private static int getNextTaskId(ArrayList<Task> list) {
    int nextId = 1;
    //get max id
    for (Task t : list) {
      if (t.getId() >= nextId) {
        nextId = t.getId() + 1;
      }
    }
    return nextId;
  }

  private static Task getTaskById(String sTaskId, ArrayList<Task> list) {
    int taskId = -1;
    Scanner sc = new Scanner(System.in);
    do {
      if (isInteger(sTaskId)) {
        taskId = Integer.parseInt(sTaskId);
      }
      else {
        System.out.print("Please enter existing task Id or (Q) to return to main menu: ");
        sTaskId = sc.nextLine();
      }
      if (sTaskId.equals("Q")) {
        break;
      }
    }
    while(!isInteger(sTaskId));

    for (Task t : list) {
      if (t.getId() == taskId) {
        return t;
      }
    }
    return null;
  }

  private static void listAllTasks(String choice, ArrayList<Task> list) {
    ArrayList<Task> orderedList = list;
    System.out.println("Id|Title|Due Date|Project|Status");
    if (choice.equals("D")) {
      orderedList.sort(Comparator.comparing(Task::getDueDate).reversed());
    }
    else {
      orderedList.sort(Comparator.comparing(Task::getProject));
    }

    for (Task t : orderedList) {
      System.out.println(t.toString());
    }
  }

  private static ArrayList<Task> readFromFileIntoList(String fileName) {
    ArrayList<Task> list = new ArrayList<Task>();
    try {
      Scanner s = new Scanner(new File(fileName));
      while (s.hasNextLine()) {
        String[] taskFields = s.nextLine().split("\\|");
        int id = Integer.parseInt(taskFields[0]);
        String title = taskFields[1];
        Date dueDate = null;
        try {
          dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(taskFields[2]);
        }
        catch (ParseException pe) {
          //ignore since date is always in a good format
        }
        String project = taskFields[3];
        String status = taskFields[4];
        list.add(new Task(id, title, dueDate, project, status));
      }
      s.close();
    }
    catch (FileNotFoundException fnfe) {
      //ignore, if there's no file it's OK to have empty ArrayList
    }
    return list;
  }

  private static ArrayList readFromFileIntoList2(String fileName) {
    ArrayList list = new ArrayList();
    try
    {
      FileInputStream fis = new FileInputStream(fileName);
      ObjectInputStream ois = new ObjectInputStream(fis);

      list = (ArrayList) ois.readObject();

      ois.close();
      fis.close();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    catch (ClassNotFoundException c)
    {
      System.out.println("Class not found");
      c.printStackTrace();
    }
    return list;
  }

  private static void writeToFile(String fileName, ArrayList list) {
    BufferedWriter output = null;
    try {
      FileWriter fw = new FileWriter(fileName);
      output = new BufferedWriter(fw);
      for (Object o : list) {
        output.write(o.toString());
        output.newLine();
      }
      System.out.println("Task list was successfully saved!");
    }
    catch (IOException ioe) {
      System.out.println("Task list was NOT saved!");
    }
    finally {
      if (output != null) {
        try {
          output.close();
        }
        catch (IOException ioe) {
          System.out.println("Couldn't close connection to file!");
        }
      }
    }
  }

  private static  void writeToFile2(String fileName, ArrayList list) {
    try
    {
      FileOutputStream fos = new FileOutputStream(fileName);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(list);
      oos.close();
      fos.close();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
  }

  private static void pressEnterToContinue() {
    System.out.println();
    System.out.println("Press Enter/Return key to continue...");
    try {
      System.in.read();
    }
    catch(Exception e) {
    }
  }

  private static int getNumberOfCompleteTasks(ArrayList<Task> list) {
    int c = 0;
    for (Task t : list) {
      if (t.getStatus().equals("Done")) {
        c++;
      }
    }
    return c;
  }

  private static int getNumberOfIncompleteTasks(ArrayList<Task> list) {
    int c = 0;
    for (Task t : list) {
      if (t.getStatus().equals("Not Done")) {
        c++;
      }
    }
    return c;
  }
}
