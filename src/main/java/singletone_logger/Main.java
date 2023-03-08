package singletone_logger;

import javax.swing.plaf.TableHeaderUI;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    //This list needed to have a reference to and therefore control of all threads after their creation
    private static ArrayList<Thread> _allThreads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
      Logger lg = Logger.getInstance();
      Random rnd = new Random();
      for(int i = 0; i < 100; i++) {
          Thread t = new Thread(new WorkThread(lg, LogLevel.values()[rnd.nextInt(5)], "-= The message in thread NUmber " + (i + 1) + "=-"));
          t.setName("Thread " + i);
          t.start();
          _allThreads.add(t);
      }

      //This block of code makes the main thread wait until all the other threads are finished
      for(Thread one : _allThreads) {
        while(one.isAlive()){}
      }


      System.out.println("\n-----------------------------------------------\n");
      for(LogLevel ll : LogLevel.values()) {
          System.out.printf("%s messages of level %s \n", lg.get_level_message_count(ll), ll);
      }

    }

}