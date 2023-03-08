package singletone_logger;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Logger implements Loggerable {

    private static Logger _INSTANCE = null;
    private static ConcurrentHashMap<LogLevel, Integer> _loglevelsCount = new ConcurrentHashMap<>();

    private Logger(){}

    public static Logger getInstance(){
        if (_INSTANCE == null){
            synchronized (Logger.class){
                if (_INSTANCE == null) {
                    _INSTANCE = new Logger();
                    return _INSTANCE;
                }

            }
        }
        return _INSTANCE;
    }

    @Override
    public String log_message(LogLevel log_level, String message) {
        StringBuilder sb_final_message = new StringBuilder();
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        String currentDateAndTime = String.format("%s/%s/%s, %s:%s:%s", day, month, year, hour, minutes, seconds);
        sb_final_message.append(message + "\n");
        sb_final_message.append(currentDateAndTime + "\n");
        sb_final_message.append(String.format("LOG LEVEL: %s \n", log_level));
        Thread currentThread = Thread.currentThread();
        sb_final_message.append(String.format(
                "Current thread details:\nID: %s, Name: %s, State: %s, Group: %s, Priority: %s",
                currentThread.getId(), currentThread.getName(), currentThread.getState(), currentThread.getThreadGroup(), currentThread.getPriority()
                ));

        String finalMessage = sb_final_message.toString().replace(log_level.toString(), colorizeLogLevel(log_level));
        System.out.println(finalMessage);

        if(log_level.equals(LogLevel.ERROR)) {
            writeMessageToFile(sb_final_message.toString(), "ERROR_MESSAGE_FILE.txt");
        }

        countLogLevel(log_level);

        return finalMessage;
    }

    @Override
    public int get_level_message_count(LogLevel log_level) {
        return _loglevelsCount.get(log_level) == null ? 0 : _loglevelsCount.get(log_level);
    }

    private static void countLogLevel(LogLevel level) {
        if(_loglevelsCount.get(level) == null) {
            _loglevelsCount.put(level, 1);
        } else {
            int logLevelCount = _loglevelsCount.get(level);
            logLevelCount++;
            _loglevelsCount.replace(level, logLevelCount);
        }
    }

    private String colorizeLogLevel(LogLevel logLevel){
        StringBuilder sb = new StringBuilder();
        switch (logLevel) {
            case ERROR:
                sb.append(ConsoleColors.colorizeConsoleString(logLevel.toString(), ConsoleColors.RED));
                break;
            case DEBUG:
                sb.append(ConsoleColors.colorizeConsoleString(logLevel.toString(), ConsoleColors.GREEN));
                break;
            case WARNING:
                sb.append(ConsoleColors.colorizeConsoleString(logLevel.toString(), ConsoleColors.PURPLE));
                break;
            case INFO:
                sb.append(ConsoleColors.colorizeConsoleString(logLevel.toString(), ConsoleColors.BLUE));
                break;
            case VERBOSE:
                sb.append(ConsoleColors.colorizeConsoleString(logLevel.toString(), ConsoleColors.BLUE_BOLD));
                break;
        }
        return sb.toString();
    }

    private void writeMessageToFile(String text, String fileName) {
        synchronized (Logger.class) {
            try {
                Path filePath = Paths.get(fileName);
                FileWriter fw = new FileWriter(fileName, Files.exists(filePath));
                fw.write(text);
                fw.write("\n-----------------------------------------------------------\n\n");
                fw.close();

                } catch (Exception ex) {

            }
        }
    }
}
