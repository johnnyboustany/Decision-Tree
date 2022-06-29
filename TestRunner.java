package decisiontree;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


/*
 *
 *
 * This class is used to run all of the JUnit tests, which can be found in
 * the MyID3Test.java file.
 * 
 * One way of running the tests is to right-click on this class (TestRunner.java) in the
 * Eclipse package explorer and select Run As -> Java Application.
 *
 */
public class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(MyID3Test.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      if(result.wasSuccessful()) {
        System.out.println("all tests passed!");
      }
   }
}
