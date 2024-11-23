import java.util.LinkedList;
import java.util.Queue;

public class RateLimitedAPI {
    private static final int LIMIT = 15;  // Max calls per minute
    private static final int WINDOW = 60000;  // One minute in milliseconds
    private static final Queue<Long> callTimestamps = new LinkedList<>();

    public static synchronized String safeCallMe(String input) throws InterruptedException {
        long currentTime = System.currentTimeMillis();

        // Remove timestamps older than one minute
        while (!callTimestamps.isEmpty() && currentTime - callTimestamps.peek() >= WINDOW) {
            callTimestamps.poll();
        }

        // Check if within limit
        if (callTimestamps.size() >= LIMIT) {
            long waitTime = WINDOW - (currentTime - callTimestamps.peek());
            System.out.println("Rate limit reached. Waiting " + waitTime + "ms...");
            Thread.sleep(waitTime);
        }

        // Make the call and log timestamp
        callTimestamps.add(System.currentTimeMillis());
        return callMe(input);
    }

    public static String callMe(String input) {
        // Simulated API call
        return "Response for: " + input;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 20; i++) {
            System.out.println(safeCallMe("Request " + i));
        }
    }
}
