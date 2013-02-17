import java.sql.*;

public class DBTest
{
  Connection c1;
  Statement st1;
  int updateCount;
  java.sql.ResultSet rs1;

  public static void main(String[] args)
  {
     DBTest database = new DBTest();
     database.testDB();
     database.closeDB();
  } // end of main processing

  public DBTest()
  {
     openDB();
  } // end of DBTest

  public void openDB()
  {
  	 String url;
     try
     {
		//Load the hsqldb driver
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		url = "jdbc:hsqldb:Students"; // stored on disk mode
		c1 = DriverManager.getConnection(url, "SA", "");
		st1 = c1.createStatement();
		System.out.println("\nOpened HSQLDB database.");

     	/*
     	//Load the Access driver
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        url = "jdbc:odbc:Students";
        c1 = DriverManager.getConnection(url,"userid", "userpassword");
        st1 = c1.createStatement();
		System.out.println("\nOpened Access database.");
        */

		/*
		//Load the MySQL driver
		Class.forName("com.mysql.jdbc.Driver");
		url = "jdbc:mysql://localhost/Students";
		c1 = DriverManager.getConnection(url, "root", "");
        st1 = c1.createStatement();
		System.out.println("\nOpened MYSQL database.");
		*/

     } catch(Exception e) {processSQLError(e);}

  }  // end of openDB

  public void closeDB()
  {
     try
     {
        c1.close();
     } catch(Exception e) {processSQLError(e);}
     System.out.println("Database closed successfully.");
  }  // end of closeDB

   public void testDB()
   {
      System.out.println("\nStarting database test.\n");
      selectRelation("SELECT * FROM Students");
      selectRelation("SELECT * FROM Courses");
      selectRelation("SELECT * FROM Enrolled");

      updateRelation("INSERT INTO Courses VALUES ('COMP 4060', 'Topics in CS')");
      selectRelation("SELECT * FROM Courses");
      updateRelation("DELETE FROM Courses WHERE courseID='COMP 4060'");
      selectRelation("SELECT * FROM Courses");

      updateRelation("INSERT INTO Students VALUES(8000000, 'James Bond')");
      selectRelation("SELECT * FROM Students");
      updateRelation("UPDATE Students SET studentName='Jesse James' WHERE studentID=8000000");
      selectRelation("SELECT * FROM Students");
      updateRelation("DELETE FROM Students WHERE studentID = 8000000");
      updateRelation("DELETE FROM Students WHERE studentID = 9000000");
      selectRelation("SELECT * FROM Students");

	  selectRelation("SELECT Students.studentName, Courses.courseName, Enrolled.grade FROM Students, Courses, Enrolled WHERE Students.studentID=Enrolled.studentID AND Courses.courseID=Enrolled.courseID");

      System.out.println("Stopping database test.\n");

   } // end of testDB processing

  public void selectRelation(String cmdString)
  {
     try
     {
        System.out.println(cmdString);
        rs1 = st1.executeQuery(cmdString);
        java.sql.ResultSetMetaData md2 = rs1.getMetaData();
        while(rs1.next()) {
           System.out.print("Tuple: | ");
           for(int i=1; i<= md2.getColumnCount(); i++) {
              String value=rs1.getString(i);
              if (value == null) value = "****";
              System.out.print(value + " | ");
              }
           System.out.println();
           }
        System.out.println();
        rs1.close();
     } catch(Exception e) {processSQLError(e);}
  } // end of selectRelation

  public void updateRelation(String cmdString)
  {
     try
     {
        System.out.println(cmdString);
        updateCount = st1.executeUpdate(cmdString);
        System.out.println("Tuples updated: " +updateCount);
        System.out.println();
     } catch(Exception e) {processSQLError(e);}
  }  // end of updateRelation

  public void processSQLError(Exception e)
  {
     System.out.println("*** Error: " +e.getMessage());
     //e.printStackTrace();
  }  //end of processSQLError

} // end of class DB
