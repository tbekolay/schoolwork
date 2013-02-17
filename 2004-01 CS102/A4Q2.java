import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class A4Q2
{

  static int[][][] patterns = {
      { { 1, 2 }, { 2, 3 }, { 3, 1 }, { 3, 2 }, { 3, 3 } },
      { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 }, { 6, 6 }, { 7, 7 },
          { 8, 8 } }, { { 2, 2 }, { 2, 3 }, { 3, 2 }, { 3, 3 } } };

  public static void main(String[] args)
  {

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

    runConsole();
    // runGUI();

    System.out.println("\nSystem completed normally.");
  }

  public static void runConsole()
  {
    Life life;
    int counter;
    int counter2;

    for (counter = 0; counter < patterns.length; counter++)
    {
      System.out.println("\nProcessing pattern: " + counter);
      life = new Life(patterns[counter]);
      life.printLife();
      for (counter2 = 0; counter2 < 5; counter2++)
      {
        life.runLife(1);
        life.printLife();
      }
    }
  }

  public static void runGUI()
  {
    new DisplayLife(patterns);
  }

}


/** ************************************************************** */


class DisplayLife extends JFrame implements ActionListener
{

  JFrame frame1;
  DisplayLifePanel panel1;
  JPanel buttonPanel;
  JButton button1 = new JButton();
  JButton button2 = new JButton();
  JButton button3 = new JButton();

  Life life;
  int[][][] patterns;
  int[][] lifeCells;

  int genCount;
  int patternCounter;

  final int pointSize = 8;
  final int genSteps = 1;

  public DisplayLife(int[][][] patterns)
  {

    this.patterns = patterns;
    setUpGUI();

    patternCounter = 0;
    life = new Life(patterns[patternCounter]);
    lifeCells = life.printLife();

    panel1.displayCells(genCount, lifeCells);
  }


  public void setUpGUI()
  {

    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    frame1 = new JFrame();
    frame1.getContentPane().setLayout(new FlowLayout());
    panel1 = new DisplayLifePanel();
    panel1.setBorder(BorderFactory.createEtchedBorder());
    buttonPanel = new JPanel();
    panel1.setPreferredSize(new Dimension(300, 290));
    frame1.getContentPane().add(panel1);
    frame1.setSize(340, 370);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame1.getSize();
    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    frame1.setLocation((screenSize.width - frameSize.width) / 2,
        (screenSize.height - frameSize.height - 25) / 2);

    button1.setText("Next");
    button1.addActionListener(this);
    buttonPanel.add(button1);

    button2.setText("Pattern");
    button2.addActionListener(this);
    buttonPanel.add(button2);

    button3.setText("Quit");
    button3.addActionListener(this);
    buttonPanel.add(button3);

    panel1.setLayout(null);
    frame1.getContentPane().add(buttonPanel);

    frame1.validate();
    frame1.setVisible(true);
  }


  public void actionPerformed(ActionEvent aE)
  {
    Object buttonPressed = aE.getSource();
    if (buttonPressed == button1)
    {
      button1_actionPerformed(aE);
    }
    else if (buttonPressed == button2)
    {
      button2_actionPerformed(aE);
    }
    else if (buttonPressed == button3)
    {
      button3_actionPerformed(aE);
    }
  }


  public void button1_actionPerformed(ActionEvent aE)
  {
    for (int times = 0; times < genSteps; times++)
    {
      genCount++;
      lifeCells = life.runLife(1);
    }
    panel1.displayCells(genCount, lifeCells);
  }


  public void button2_actionPerformed(ActionEvent aE)
  {
    genCount = 0;
    patternCounter++;
    if (patternCounter >= patterns.length)
    {
      patternCounter = 0;
    }
    life = new Life(patterns[patternCounter]);
    lifeCells = life.printLife();
    panel1.displayCells(genCount, lifeCells);
  }


  public void button3_actionPerformed(ActionEvent aE)
  {
    System.out.println("All done.");
    System.exit(0);
  }


  public class DisplayLifePanel extends JPanel
  {

    int[][] currentGeneration;
    int genCount;

    public void displayCells(int genCount, int[][] currentGeneration)
    {
      this.genCount = genCount;
      this.currentGeneration = currentGeneration;
      repaint();
    }


    public void paint(Graphics g)
    {
      int row, col, maxRows, maxCols;
      super.paint(g);
      if (currentGeneration == null) return;
      g.drawString("Generation " + genCount, 10, 255);
      maxRows = currentGeneration.length;
      maxCols = currentGeneration[0].length;
      for (row = 0; row < maxRows; row++)
      {
        for (col = 0; col < maxCols; col++)
        {
          int temp = currentGeneration[row][col];
          if (temp == 1)
          {
            g.drawRect(col * pointSize, row * pointSize, pointSize, pointSize);
            g.fillRect(col * pointSize, row * pointSize, pointSize, pointSize);
          }
        }
      }
    }

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
