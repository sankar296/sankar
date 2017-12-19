package com.hexaware.ftp08.util;
import java.util.Scanner;

import com.hexaware.ftp08.model.Employee;
import com.hexaware.ftp08.model.LeaveDetails;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Class CliMain provides the command line interface to the leavemanagement
 * application.
 */
public class CliMain {
  private Scanner option = new Scanner(System.in, "UTF-8");

  private void mainMenu() {
    System.out.println("Leave Management System");
    System.out.println("-----------------------");
    System.out.println("1. List All Employees Info");
    System.out.println("2. Display Employee Info");
    System.out.println("3. Apply for Leave");
    System.out.println("4. Show leave history");
    System.out.println("5. View pending leave applications");
    System.out.println("6. Approve or Deny Application");
    System.out.println("7. Exit");
    System.out.println("Enter your choice:");
    int menuOption = option.nextInt();
    mainMenuDetails(menuOption);
  }
  private void mainMenuDetails(final int selectedOption) {
    switch (selectedOption) {
      case 1:
        listEmployeesDetails();
        break;
      case 2:
        listEmployeeDetail();
        break;
      case 3:
        apply();
        break;
      case 4:
        showHistory();
        break;
      case 5:
        showPending();
        break;
      case 6:
        approveDeny();
        break;
      case 7:
        // halt since normal exit throws a stacktrace due to jdbc threads not responding
        Runtime.getRuntime().halt(0);
      default:
        System.out.println("Choose either 1, 2 or 3");
    }
    mainMenu();
  }
  private void listEmployeeDetail() {
    System.out.println("Enter an Employee Id");
    int empId = option.nextInt();
    Employee employee = Employee.listById(empId);
    if (employee == null) {
      System.out.println("Sorry, No such employee");
    } else {
      System.out.println(employee.getEmpId() + " " + employee.getEmpName() + " " + employee.getEmpEmail() + " "
              + employee.getEmpMobNo() + " " + employee.getEmpDptName() + " "
              + employee.getEmpMgrId() + " " + employee.getEmpLeaveBalance());
    }
  }
  private void listEmployeesDetails() {
    Employee[] employee = Employee.listAll();
    for (Employee e : employee) {
      System.out.println(e.getEmpId() + " " + e.getEmpName() + " " + e.getEmpEmail() + " "
              + e.getEmpMobNo() + " " + e.getEmpDptName() + " "
              + e.getEmpMgrId() + " " + e.getEmpLeaveBalance());
    }
  }

  private void apply() {
    System.out.println("Enter an Employee Id");
    String s = option.next();
    int empId = 0;
    try {
      empId = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Please enter correct id");
      apply();
    }

    Employee employee = Employee.listById(empId);

    if (employee == null) {
      System.out.println("Sorry, No such employee");
    } else if (employee.getEmpLeaveBalance() > 0) {
      //System.out.println(employee.getEmpLeaveBalance());

      String startDate = enterStartDate();
      String endDate = enterEndDate();
      java.sql.Date sDate = null;
      java.sql.Date eDate = null;
      try {
        java.util.Date dt1 = new SimpleDateFormat("yyyy/MM/dd").parse(startDate);
        sDate = new java.sql.Date(dt1.getTime());
        java.util.Date dt2 = new SimpleDateFormat("yyyy/MM/dd").parse(endDate);
        eDate = new java.sql.Date(dt2.getTime());
      } catch (ParseException e) {
        System.out.println(e);
      }
      //System.out.println("Enter End Date in this format =>(yyyy-mm-dd)");
      //String endDate = option.next();
      LeaveDetails[] leaDetails = LeaveDetails.listLeaveDetails(empId);
      for (LeaveDetails l : leaDetails) {
        if ((sDate.before(l.getLeaEndDate()) || sDate.equals(l.getLeaEndDate()))
            && (eDate.after(l.getLeaStartDate()) || eDate.equals(l.getLeaStartDate()))) {
          System.out.println("Dates are overlapping, you are already applied in this range");
          mainMenu();
        }
      }

      System.out.println("Enter the Reason");
      String reason1 = option.nextLine();
      String reason2 = option.nextLine();
      String reason = reason1 + reason2;
      System.out.println("Enter the number of Days");
      String s1 = option.next();
      int noOfDays = 0;
      try {
        noOfDays = Integer.parseInt(s1);
      } catch (NumberFormatException e) {
        System.out.println("number of days should be a number");
        apply();
      }
      int leaveBal = employee.getEmpLeaveBalance();
      int newLeaveBal = leaveBal - noOfDays;
      if (employee.getEmpMgrId() == 0 && noOfDays < employee.getEmpLeaveBalance()) {
        Employee.applyLeave(startDate, endDate, reason, empId, noOfDays, newLeaveBal);
      } else if (noOfDays < employee.getEmpLeaveBalance()) {
        Employee.applyLeave(startDate, endDate, reason, empId, noOfDays, newLeaveBal);
      } else {
        System.out.println("Sorry, No sufficient leave Balance available");
      }
    }
  }

  private String enterStartDate() {
    System.out.println("Enter Start Date in this format =>(yyyy/mm/dd)");
    String startDate = option.next();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    try {
      sdf.setLenient(false);
      sdf.parse(startDate);
    } catch (ParseException e) {
      System.out.println("Invalid start date");
      enterStartDate();
    }
    return startDate;
  }

  private String enterEndDate() {
    System.out.println("Enter End Date in this format =>(yyyy/mm/dd)");
    String endDate = option.next();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    try {
      sdf.setLenient(false);
      sdf.parse(endDate);
    } catch (ParseException e) {
      System.out.println("Invalid start date");
      enterEndDate();
    }
    return endDate;
  }

  private void showHistory() {
    System.out.println("Enter your emp id");
    String s = option.next();
    int  empId = 0;
    try {
      empId = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Please enter correct employee id");
      showHistory();
    }

    Employee employee = Employee.listById(empId);

    if (employee == null) {
      System.out.println("Sorry, No such employee with employee id:" + empId + "exists");
      mainMenu();
    }

    LeaveDetails[] leaDetails = LeaveDetails.listLeaveDetails(empId);

    try {
      if (leaDetails.length == 0) {
        throw new IllegalArgumentException("No leave history is available");
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e);
      mainMenu();
    }

    for (LeaveDetails ld : leaDetails) {

      System.out.println(ld.toString());
      System.out.println("-----------------------------------------------------");
    }


  }

  private void showPending() {
    System.out.println("Enter your Id as Manager");
    String s = option.next();
    int  empId = 0;
    try {
      empId = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Please enter correct employee id");
      showPending();
    }
    Employee employee = Employee.listById(empId);
    if (employee == null) {
      System.out.println("Sorry, No such employee");
      mainMenu();
    }

    Employee listManagers = Employee.listManager(empId);
    if (listManagers == null) {
      System.out.println("You are not a manager");
      showPending();
    }

    LeaveDetails[] leaDetails = LeaveDetails.listPendingApplications(empId);

    try {
      if (leaDetails.length == 0) {
        throw new IllegalArgumentException("No applications are Pending");
      } else {
        for (LeaveDetails ld : leaDetails) {
          if (ld.getEmplId() != empId) {
            System.out.println(ld.toString());
            System.out.println("-----------------------------------------------------");
          }
        }
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e);
      mainMenu();
    }
  }

  private void approveDeny() {
    System.out.println("Enter your Id as Manager");
    String s = option.next();
    int empId = 0;
    try {
      empId = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Please enter correct manager id");
      approveDeny();
    }


    Employee employee = Employee.listById(empId);
    LeaveDetails[] leaDetails = LeaveDetails.listPendingApplications(empId);
    //int leaveBal = employee.getEmpLeaveBalance();
    if (employee == null) {
      System.out.println("Sorry, No such employee");
    }

    Employee listManagers = Employee.listManager(empId);
    if (listManagers == null) {
      System.out.println("You are not a manager");
      approveDeny();
    }

    try {
      if (leaDetails.length == 0) {
        throw new IllegalArgumentException("No applications are Pending");
      } else {
        for (LeaveDetails ld : leaDetails) {
          if (ld.getEmplId() != empId) {
            System.out.println(ld.toString());
            System.out.println("-----------------------------------------------------");
          }
        }
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e);
      mainMenu();
    }
    System.out.println("1. Approve the application ");
    System.out.println("2. Deny the Application");
    int menuOption = option.nextInt();
    menuDetails(menuOption);

  }

  private void menuDetails(final int menuOption) {
    switch (menuOption) {
      case 1:
        approve();
        break;
      case 2:
        deny();
        break;
      default:
        System.out.println("Choose either 1, 2");
    }

    mainMenu();
  }

  private void approve() {
    System.out.println("Enter the Leave id of the application you want to approve");
    int leaId = option.nextInt();

    LeaveDetails l = LeaveDetails.listByLeaveId(leaId);
    if (l == null) {
      System.out.println("Sorry, No Such Leave Application exists");
    } else {
      System.out.println("Enter the Employee id for that leave id");
      int employeeId = option.nextInt();
      Employee employee = Employee.listById(employeeId);
      if (employee == null) {
        System.out.println("Sorry, No such employee");
        mainMenu();
      }
      //Employee employee = Employee.listById(employeeId);
      //int leaveBal = employee.getEmpLeaveBalance();
      System.out.println("Enter your comments here");
      String mgrComments1 = option.nextLine();
      String mgrComments2 = option.nextLine();
      String mgrComments = mgrComments1 + mgrComments2;
      //int newLeaveBal = leaveBal - l.getLeaNoOfDays();
      LeaveDetails.approveLeave(mgrComments, leaId, employeeId);
    }
  }

  private void deny() {
    System.out.println("Enter the Leave id of the application you want to deny");
    int leaId = option.nextInt();

    LeaveDetails l = LeaveDetails.listByLeaveId(leaId);
    if (l == null) {
      System.out.println("Sorry, No Such Leave Application exists");
    } else {
      System.out.println("Enter the Employee id for that leave id");
      int employeeId = option.nextInt();
      Employee employee = Employee.listById(employeeId);
      //int employeeId = option.nextInt();
      //Employee employee = Employee.listById(employeeId);
      if (employee == null) {
        System.out.println("Sorry, No such employee with id: " + employeeId + "exists");
        mainMenu();
      }
      int leaveBal = employee.getEmpLeaveBalance();
      int newLeaveBal = leaveBal + l.getLeaNoOfDays();
      System.out.println("Enter your comments here");
      String mgrComments1 = option.nextLine();
      String mgrComments2 = option.nextLine();
      String mgrComments = mgrComments1 + mgrComments2;
      LeaveDetails.denyLeave(mgrComments, leaId, employeeId, newLeaveBal);
    }


  }

  /**
   * The main entry point.
   * @param ar the list of arguments
   */
  public static void main(final String[] ar) {
    final CliMain mainObj = new CliMain();
    mainObj.mainMenu();
  }
}
