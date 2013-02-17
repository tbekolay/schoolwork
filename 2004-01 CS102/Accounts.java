/**
 * Accounts and Account; Classes used in a series of banking transactions.
 * 74.101 SECTION L02
 * INSTRUCTOR Terry Andres
 * ASSIGNMENT Assignment 3, question 1
 * @author Trevor Bekolay, 6796723
 * @version March 5, 2004
 */


// This is needed to use vectors and decimalFormat.
import java.util.*;
import java.text.*;

public class Accounts
{

  private Vector accounts;
  private int accountNum;

  /**
   * PURPOSE: Accounts constructor. Creates a new vector.
   */

  public Accounts()
  {
    accounts = new Vector();
    accountNum = 0;
  }

  /**
   * PURPOSE: addAccount adds an Account object into the accounts vector in the
   * Accounts object.
   * @param Account account - The Account object to be added.
   * @return VOID
   */

  public void addAccount(Account account)
  {

    Account tempAccount;
    boolean found = false;

    // This for loop checks the accounts vector to ensure that the account to be
    // added does not share the same number as one of the Account objects in
    // accounts.
    for (int count = 0; count < accountNum; count++)
    {
      tempAccount = (Account) accounts.elementAt(count);
      if (tempAccount.accountNumber.equals(account.accountNumber))
      {
        System.out.println("Error!  Duplicate account number!");
        found = true;
      }
    }
    // The found boolean is used to determine if a match has been found.
    if (!found) accounts.add(accountNum++, account);
  }

  /**
   * PURPOSE: printAccounts uses the toString method in the Account object to
   * output account information.
   * @return VOID
   */

  public void printAccounts()
  {

    Account tempAccount;

    // This for loop outputs the toString() of all Account objects in the
    // accoutns vector.
    for (int count = 0; count < accountNum; count++)
    {
      tempAccount = (Account) accounts.elementAt(count);
      System.out.println(tempAccount.toString());
    }
  }

  /**
   * PURPOSE: accountUpdate is a method that controls the transactions of the
   * accounts by using the appropriate method in the Account objects.
   * @param String transactionType - The type of transaction; IE Deposit. String
   *          accountNumber - The account number to be manipulated, if needed.
   *          double accountAmount - The amount that will be deposited,
   *          withdrawn or transferred. String toAccountNumber - In a transfer,
   *          this is the account that will be deposited to.
   * @return VOID
   */

  public void accountUpdate(String transactionType, String accountNumber,
      double accountAmount, String toAccountNumber)
  {

    Account currentAccount = null;

    // The if-else loop construct is used to determine what type of transaction
    // will be taking place.
    if (transactionType.equalsIgnoreCase("Deposit"))
    {
      // If a deposit is to take place, we must make sure that the account
      // number is valid. A null object is outputted if we cannot find the
      // accountNumber, so we use this to determine if the account number is
      // valid.
      currentAccount = findAccount(accountNumber);
      if (currentAccount == null) System.out
          .println("Error!  Invalid account number.");
      else
        // The deposit method of the Account object is used to manipulate the
        // account.
        currentAccount.deposit(accountAmount);
    }
    else if (transactionType.equalsIgnoreCase("Withdraw"))
    {
      // We must again check for a valid account number.
      currentAccount = findAccount(accountNumber);
      if (currentAccount == null) System.out
          .println("Error!  Invalid account number.");
      else
        // The withdrawal method of the Accoutn object is used to manipulate the
        // account.
        currentAccount.withdrawal(accountAmount);
    }
    // If the transaction is Interest, then we must calculate interest for all
    // of the Accounts in the accounts vector.
    else if (transactionType.equalsIgnoreCase("Interest"))
    {
      // This for loop makes sure that all Accounts in the accounts vector have
      // their interest calculated.
      for (int count = 0; count < accountNum; count++)
      {
        // We must find which subclass currentAccount belongs to. We use the
        // instanceof operator to do so.
        currentAccount = (Account) accounts.elementAt(count);
        if (currentAccount instanceof Chequing)
        {
          Chequing chequingAccount = (Chequing) currentAccount;
          chequingAccount.interest();
        }
        else if (currentAccount instanceof Savings)
        {
          Savings savingsAccount = (Savings) currentAccount;
          savingsAccount.interest();
        }
        else if (currentAccount instanceof BonusChequing)
        {
          BonusChequing bonusChequingAccount = (BonusChequing) currentAccount;
          bonusChequingAccount.interest();
        }
        else if (currentAccount instanceof BonusSavings)
        {
          BonusSavings bonusSavingsAccount = (BonusSavings) currentAccount;
          bonusSavingsAccount.interest();
        }
      }
    }
    // If a transfer is to occur, we basically perform a withdrawal from the
    // outgoing account and a deposit into the ingoing account.
    else if (transactionType.equalsIgnoreCase("Transfer"))
    {
      currentAccount = findAccount(accountNumber);
      currentAccount.withdrawal(accountAmount);
      currentAccount = findAccount(toAccountNumber);
      currentAccount.deposit(accountAmount);
    }

  }

  /**
   * PURPOSE: findAccount searches the accounts vector for an element with a
   * certain accountNumber.
   * @param String accountNumber - The accountNumber we are searching for.
   * @return Account desiredAccount - The account that we are trying to find. If
   *         no account is found, we output a null object, which is used in
   *         other methods.
   */

  public Account findAccount(String accountNumber)
  {

    Account tempAccount;
    Account desiredAccount = null;

    // This for loops looks through all of the Account objects in the accounts
    // vector and determines if the accountNumber matches the accountNumber we
    // are seeking.
    for (int count = 0; count < accountNum; count++)
    {
      tempAccount = (Account) accounts.elementAt(count);
      if (tempAccount.accountNumber.equals(accountNumber)) desiredAccount = tempAccount;
    }

    return desiredAccount;

  }

}

// The Account class is noted as abstract because we do not make an instance of
// an Account object, every Account will have a more specific title attached to
// it.
abstract class Account
{

  protected String accountNumber;
  protected double accountBalance;
  protected double interest;
  protected double serviceCharge;
  protected double overdraft;

  final protected double BASIC_INTEREST = 0.01;

  /**
   * PURPOSE: Account constructor. While only two variables are set, more are
   * set based on the subclass that it belongs to.
   * @param String accountNumber - The account number of the account. double
   *          accountBalance - The amount of money in the account.
   */

  public Account(String accountNumber, double accountBalance)
  {
    this.accountNumber = accountNumber;
    this.accountBalance = accountBalance;
  }

  /**
   * PURPOSE: toString uses the DecimalFormat class to output account
   * information properly rounded.
   * @return String that contains the account number and balance of an Account
   *         object.
   */

  public String toString()
  {
    DecimalFormat twoDecimals = new DecimalFormat("0.00");

    return "Account Number " + accountNumber + "\tBalance: "
        + twoDecimals.format(accountBalance);
  }

  /**
   * PURPOSE: deposit adds money to an account.
   * @param double accountAmount - The amount of money to be added.
   * @return VOID
   */

  public void deposit(double accountAmount)
  {
    // If the amount to be deposited is negative, we output an error.

    if (accountAmount < 0) System.out
        .println("Error!  Cannot deposit moneys totalling less than 0.");
    // Otherwise, a new balance is calculated using the accountAmount and the
    // object's serviceCharge.

    else
      accountBalance = accountBalance + accountAmount - serviceCharge;
  }

  /**
   * PURPOSE: withdrawal takes money out of an account if applicable.
   * @param double accountAmount - The amount of money to be taken out.
   * @return VOID
   */

  public void withdrawal(double accountAmount)
  {
    // The new balance is calculated.
    accountBalance = accountBalance - accountAmount - serviceCharge;
    // If this new balance is less than the overdraft priveleges, an error is
    // outputted and the old balance is reinstated.
    if (accountBalance < (overdraft * -1))
    {
      accountBalance = accountBalance + accountAmount + serviceCharge;
      System.out
          .println("Error!  Cannot withdrawal when final balance would be "
              + (overdraft * -1) + " or less!");
    }
  }

  /**
   * PURPOSE: interest calculates interest using the general forumala for
   * interest
   * @return VOID
   */

  public void interest()
  {
    accountBalance += (accountBalance * interest);
  }

}

class Savings extends Account
{

  /**
   * PURPOSE: The Savings constructor. The superclass's constructor is called,
   * then the elements unique to a Savings account are manipulated.
   */

  public Savings(String accountNumber, double accountBalance)
  {
    super(accountNumber, accountBalance);
    interest = BASIC_INTEREST;
    serviceCharge = 1.5;
    overdraft = 0;
  }

}

class Chequing extends Account
{

  /**
   * PURPOSE: The Chequing constructor. The superclass's constructor is called,
   * then the elements unique to a Chequing account are manipulated.
   */

  public Chequing(String accountNumber, double accountBalance)
  {
    super(accountNumber, accountBalance);
    interest = 0;
    serviceCharge = 0;
    overdraft = 500.0;
  }

}

class BonusSavings extends Account
{

  /**
   * PURPOSE: The BonusSavings constructor. The superclass's constructor is
   * called, then the elements unique to a BonusSavings account are manipulated.
   */

  public BonusSavings(String accountNumber, double accountBalance)
  {
    super(accountNumber, accountBalance);
    interest = BASIC_INTEREST;
    overdraft = 0;
    serviceCharge = 2.0;
  }

  /**
   * PURPOSE: interest calculates interest using rules specific to a
   * BonusSavings account.
   * @return VOID
   */

  public void interest()
  {
    super.interest();
    if (accountBalance > 500) accountBalance = accountBalance
        + (accountBalance * interest)
        + ((accountBalance - 500) * (interest * 2));
  }
}

class BonusChequing extends Account
{

  /**
   * PURPOSE: The BonusChequing constructor. The superclass's constructor is
   * called, then the elements unique to a BonusChequing account are
   * manipulated.
   */

  public BonusChequing(String accountNumber, double accountBalance)
  {
    super(accountNumber, accountBalance);
    interest = 0.005;
    overdraft = 1000.0;
    serviceCharge = 0;
  }

  /**
   * PURPOSE: interest calculates interest using rules specific to a
   * BonusChequing account.
   * @return VOID
   */

  public void interest()
  {
    if (accountBalance > 1000) accountBalance += ((accountBalance - 1000) * interest);
  }
}
