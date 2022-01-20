

public class Main {

    public static void main(String argv[]) {
        Main m = new Main();
        try {
            m.run();
        } catch (Exception e) {
            m.log(e.toString());
        }
    }

    private void run() {
        log("Hello World!");
        int a = 1 / 0;
    }

    private void log(String s) {
        System.out.println(s);
    }


    

}
