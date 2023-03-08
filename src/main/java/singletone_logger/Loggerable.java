package singletone_logger;

public interface Loggerable {
    String log_message(LogLevel log_level, String message);
    int get_level_message_count(LogLevel log_level);
}
