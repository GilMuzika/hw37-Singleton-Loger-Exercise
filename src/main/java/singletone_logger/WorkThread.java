package singletone_logger;

import java.lang.ref.PhantomReference;

public class WorkThread implements Runnable {


    private String _info;
    private Logger _logger;
    private LogLevel _loggerLevel;

    WorkThread(Logger logger, LogLevel loggerLevel, String info) {
        _info = info;
        _logger = logger;
        _loggerLevel = loggerLevel;
    }
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        _logger.log_message(_loggerLevel, _info);
    }
}
