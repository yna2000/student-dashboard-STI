import java.util.Scanner;
import java.util.concurrent.*;
public class StudentDashboard {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=============================================");
            System.out.println("       Welcome to Student Dashboard");
            System.out.println("=============================================");
            System.out.print("Enter your student number (YYYY-MM-DD): ");
            String input = readInputWithTimeout(scanner);
            if (input == null) {
                System.out.println("\033[0;31mInput timeout. Please try again.\033[0m");
                return;
            }
            StudentNumberValidator<String> validator = new StudentNumberValidator<>();
            validator.setStudentNumber(input);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CompletionStage<Boolean> isValidFuture = CompletableFuture.supplyAsync(validator::isValidStudentNumber, executor);
            isValidFuture.thenAccept(isValid -> {
                if (isValid) {
                    System.out.println("\n\033[0;32mWaiting Authentication .\033[0m");
                    animateLoginScreen();
                } else {
                    System.out.println("\n\033[0;31mError: Invalid student number format.\033[0m");
                }
            }).exceptionally(ex -> {
                System.err.println("\n\033[0;31mError occurred during validation: " + ex.getMessage() + "\033[0m");
                return null;
            }).thenRun(executor::shutdown);
        } catch (TimeoutException e) {
            System.err.println("\n\033[0;31mInput timeout: " + e.getMessage() + "\033[0m");
        } catch (Exception e) {
            System.err.println("\n\033[0;31mAn error occurred: " + e.getMessage() + "\033[0m");
        }
    }
    private static String readInputWithTimeout(Scanner scanner) throws TimeoutException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = executor.submit(scanner::nextLine);
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new TimeoutException("Input timeout");
        } finally {
            executor.shutdownNow();
        }
    }
    private static void animateLoginScreen() {
        System.out.println("\n\033[0;36mInitializing virtual environment...\033[0m");
        displayProgressBar(100);
        System.out.println("\033[0;35mRendering holographic dashboard...\033[0m");
        displayProgressBar(100);
        System.out.println("\033[0;32mSyncing with central database...\033[0m");
        displayProgressBar(100);
        System.out.println("\033[0;33mLoading user preferences...\033[0m");
        displayProgressBar(100);
        System.out.println("\n\033[0;32mAuthentication successful.\033[0m");
        System.out.println("\033[0;32mWelcome to Apex industries!\033[0m");
        System.out.println("\033[0;33mPlease make sure to log out in time after school hours.\033[0m");
    }
    private static void displayProgressBar(int percent) {
        int width = 30;
        int progressChars = (int) Math.ceil(width * (percent / 100.0));
        progressChars = Math.min(progressChars, width);
        System.out.print("\033[0;32m[");
        for (int i = 0; i <= progressChars; i++) {
            System.out.print("=");
            System.out.flush();
            sleep(50);
        }
        for (int i = progressChars + 1; i < width; i++) {
            System.out.print(" ");
        }
        System.out.print("] " + percent + "%\r");
        System.out.flush();
    }
    private static void sleep(int i) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}