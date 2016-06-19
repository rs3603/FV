public class CheckRace extends Thread {
private static int cnt = 0; // shared state
public void run() {
int y = cnt;
cnt = y + 1;
}
public static void main(String args[]) {
Thread t1 = new CheckRace();
Thread t2 = new CheckRace();
t1.start();
t2.start();
}
}