package singletone_logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    @DisplayName("Checks if the number of messages of each log level was as expected")
    public void ShouldCheckMessagesLevelCount() {
        System.out.println(String.format("%s method is executed now", Thread.currentThread().getStackTrace()[1].getMethodName()));

        Random rnd = new Random();
        ArrayList<LogLevel> levels = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            levels.add(LogLevel.values()[rnd.nextInt(5)]);
        }

        HashMap<LogLevel, Integer> loglevelsCount = new HashMap<>();
        for(LogLevel ll : LogLevel.values()) {
            loglevelsCount.put(ll, 0);
        }
        for(LogLevel ll : levels) {
            int logLevelCount = loglevelsCount.get(ll);
            logLevelCount++;
            loglevelsCount.replace(ll, logLevelCount);
        }

        Logger logger = Logger.getInstance();
        int count  = 1;
        for(LogLevel ll : levels){
            logger.log_message(ll, String.format("Message Number %s on the log level %s", count, ll));
            count++;
        }

        System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
        for(Map.Entry<LogLevel, Integer> entry : loglevelsCount.entrySet()) {
            int expectedMessagesOfCurrentLevelNum = entry.getValue();
            int actualMessagesOfCurrentLevelNum = logger.get_level_message_count(entry.getKey());
            Assertions.assertEquals(expectedMessagesOfCurrentLevelNum, actualMessagesOfCurrentLevelNum);

            System.out.printf("Messages of the level %s was expected %s, actually was %s  \n",
                    entry.getKey(),
                    expectedMessagesOfCurrentLevelNum,
                    actualMessagesOfCurrentLevelNum
                );
        }

    }

    @Test
    @DisplayName("Checks if Logger.getInstance() method always returns the same instance")
    public void ShouldCheckInstanceEquality() {
        System.out.println(String.format("%s method is executed now", Thread.currentThread().getStackTrace()[1].getMethodName()));

        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        Assertions.assertTrue(logger1 == logger2);
    }

    @ParameterizedTest
    @MethodSource("logLevelsProviderSourceMethod")
    @DisplayName("Checks if the logger message contains the expected level")
    public void ShouldCheckIfTheLogMessageContainsTheExpectedLogLevel(LogLevel expectedLevel) throws InterruptedException {
        Logger logger = Logger.getInstance();
        String finalMessage = logger.log_message(expectedLevel, "THE_MESSAGE");

        Assertions.assertTrue(finalMessage.contains(expectedLevel.toString()));

        Thread.sleep(2500);

        // This part of th test checks if the message from the logger was dispatched not before 2 seconds ago
        String finalMessageSubstring = finalMessage.substring(finalMessage.indexOf("\n") + 1);
        String finalMessageDateAndTime = finalMessageSubstring.substring(0, finalMessageSubstring.indexOf("\n")); //8/2/2023, 1:56:25
        String finalMessageSeconds = finalMessageDateAndTime.substring(finalMessageDateAndTime.lastIndexOf(':') + 1);
        String today = new Date().toString(); // Wed Mar 08 01:52:00 IST 2023
        String todaySubstr = today.substring(today.lastIndexOf(':') + 1);
        String currentSeconds = todaySubstr.substring(0, todaySubstr.indexOf(" "));

        int finalMessageSecondsInt = Integer.parseInt(finalMessageSeconds);
        int currentSecondsInt = Integer.parseInt(currentSeconds);


        Assertions.assertTrue(currentSecondsInt - finalMessageSecondsInt <= 2);

    }
    private static ArrayList<LogLevel> logLevelsProviderSourceMethod(){
        return new ArrayList<LogLevel>() {
            {
                add(LogLevel.ERROR);
                add(LogLevel.VERBOSE);
                add(LogLevel.INFO);
                add(LogLevel.DEBUG);
                add(LogLevel.WARNING);
            }
        };
    }




}