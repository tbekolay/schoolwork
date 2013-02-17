import java.io.*;
import java.util.*;


public class A3Q1
{

  public static void main(String[] parms)
  {

    Accounts accounts;
    TeePrintStream ts;
    ts = null;

    try
    {
      ts = new TeePrintStream(System.out, "out.txt");
      System.setOut(ts); // divert output to System.out from the console to ts
    }
    catch (Exception e)
    {
      System.out.println("IO Error.");
    }

    accounts = createAccounts();
    accounts.printAccounts();

    processTransactions(accounts);
    accounts.printAccounts();

    System.out.println("\nProgram completed normally.");

    try
    {
      ts.close();
    }
    catch (Exception e)
    {
      System.out.println("IO Error.");
    }
  }


  public static Accounts createAccounts()
  {
    Accounts accounts;
    Account newAccount;

    FileReader fileReaderIn;
    BufferedReader fileIn;

    String inputLine;

    accounts = new Accounts();

    System.out.println("\nCreating Accounts:");
    try
    {
      fileReaderIn = new FileReader("accounts.txt");
      fileIn = new BufferedReader(fileReaderIn);

      inputLine = fileIn.readLine(); // remove header line
      inputLine = fileIn.readLine();
      while (inputLine != null)
      {
        System.out.println(inputLine);
        newAccount = createAccount(inputLine);
        accounts.addAccount(newAccount);
        inputLine = fileIn.readLine();
      }
      fileIn.close();
    }
    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }
    catch (NumberFormatException nfe)
    {
      System.out.println("Invalid number entered.");
    }

    return accounts;
  }


  public static Account createAccount(String inputLine)
  {
    Account newAccount;
    String[] result;

    String accountNumber;
    String accountType;
    double accountBalance;

    newAccount = null;

    result = inputLine.split("\\s+");
    accountNumber = result[0];
    accountType = result[1];
    accountBalance = Double.parseDouble(result[2]);
    if (accountType.equalsIgnoreCase("Savings"))
    {
      newAccount = (Account) new Savings(accountNumber, accountBalance);
    }
    else if (accountType.equalsIgnoreCase("Chequing"))
    {
      newAccount = (Account) new Chequing(accountNumber, accountBalance);
    }
    else if (accountType.equalsIgnoreCase("BonusSavings"))
    {
      newAccount = (Account) new BonusSavings(accountNumber, accountBalance);
    }
    else if (accountType.equalsIgnoreCase("BonusChequing"))
    {
      newAccount = (Account) new BonusChequing(accountNumber, accountBalance);
    }
    else
    {
      System.out.println("Invalid account type: " + accountType);
    }
    return newAccount;
  }


  public static void processTransactions(Accounts accounts)
  {
    FileReader fileReaderIn;
    BufferedReader fileIn;

    String inputLine = "";

    System.out.println("\nProcessing Transactions:");
    try
    {
      fileReaderIn = new FileReader("transactions.txt");
      fileIn = new BufferedReader(fileReaderIn);

      inputLine = fileIn.readLine(); // remove header line
      inputLine = fileIn.readLine();
      while (inputLine != null)
      {
        System.out.println(inputLine);
        processTransaction(accounts, inputLine);
        inputLine = fileIn.readLine();
      }
      fileIn.close();
    }
    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
      ioe.printStackTrace();
    }
    return;
  }

  public static boolean processTransaction(Accounts accounts, String inputLine)
  {
    String[] result;

    String transactionType;
    String accountNumber;
    double accountAmount;
    String toAccountNumber;

    toAccountNumber = "";
    accountNumber = "";
    accountAmount = 0.0;

    result = inputLine.split("\\s+");
    transactionType = result[0];
    if (!transactionType.equals("Interest"))
    {
      accountNumber = result[1];
      accountAmount = Double.parseDouble(result[2]);
      if (transactionType.equals("Transfer"))
      {
        toAccountNumber = result[3];
      }
    }
    accounts.accountUpdate(transactionType, accountNumber, accountAmount,
        toAccountNumber);
    return true;
  }

}

/*
 * The following code was taken from:
 * http://javacook.darwinsys.com/javacook/index-bychapter.html
 * 
 * TeePrintStream tees all PrintStream operations into a file, rather like the
 * UNIX tee(1) command. It is a PrintStream subclass. The expected usage would
 * be something like the following: ... TeePrintStream ts = new
 * TeePrintStream(System.err, "err.log"); System.setErr(ts); // ...lots of code
 * that occasionally writes to System.err... ts.close(); ... I only override
 * Constructors, the write(), check() and close() methods, since any of the
 * print() or println() methods must go through these. Thanks to Svante Karlsson
 * for help formulating this. @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id: TeePrintStream.java,v 1.3 2001/04/07 01:35:53 ian Exp $
 */

class TeePrintStream extends PrintStream
{
  protected PrintStream printStream;
  protected String fileName;

  /*
   * Construct a TeePrintStream given an existing PrintStream, an opened
   * OutputStream, and a boolean to control auto-flush. This is the main
   * constructor, to which others delegate via "this".
   */
  public TeePrintStream(PrintStream orig, OutputStream os, boolean flush)
      throws IOException
  {
    super(os, true);
    fileName = "(opened Stream)";
    printStream = orig;
  }

  /*
   * Construct a TeePrintStream given an existing PrintStream and an opened
   * OutputStream.
   */
  public TeePrintStream(PrintStream orig, OutputStream os) throws IOException
  {
    this(orig, os, true);
  }

  /*
   * Construct a TeePrintStream given an existing Stream and a filename.
   */
  public TeePrintStream(PrintStream os, String fn) throws IOException
  {
    this(os, fn, true);
  }

  /*
   * Construct a TeePrintStream given an existing Stream, a filename, and a
   * boolean to control the flush operation.
   */
  public TeePrintStream(PrintStream orig, String fn, boolean flush)
      throws IOException
  {
    this(orig, new FileOutputStream(fn), flush);
  }

  /** Return true if either stream has an error. */
  public boolean checkError()
  {
    return printStream.checkError() || super.checkError();
  }

  /** override write(). This is the actual "tee" operation. */
  public void write(int x)
  {
    printStream.write(x); // "write once;
    super.write(x); // write somewhere else."
  }

  /** override write(). This is the actual "tee" operation. */
  public void write(byte[] x, int o, int l)
  {
    printStream.write(x, o, l); // "write once;
    super.write(x, o, l); // write somewhere else."
  }

  /** Close both streams. */
  public void close()
  {
    printStream.close();
    super.close();
  }

  /** Flush both streams. */
  public void flush()
  {
    printStream.flush();
    super.flush();
  }
}
