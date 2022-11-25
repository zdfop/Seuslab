import java.util.Scanner;

public class Input {

    public static void main(String[] args) {
        try {
            System.out.print("Enter the sequence in hex: ");
            Scanner scan = new Scanner(System.in);
            String sequence = scan.next();
            sequence = sequence.toUpperCase();
            if (!Utils.isNumeric(sequence)) {
                System.out.println("Wrong hex number format!");
                return;
            }
            int index = new Finder().findIndex(sequence);
            if (index > -1) {
                System.out.println("Index:" + index);
            } else {
                System.out.println("Index not found.");
            }
        } catch (InterruptedCalculateException e) {
            System.out.println("Timeout expired.");
        } catch (Exception e) {
            System.out.println("Something went wrong: ");
            e.printStackTrace(System.out);
        }
    }
}
