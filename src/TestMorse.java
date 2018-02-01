import java.io.IOException;
import java.util.Scanner;

public class TestMorse {
    public static void main(String args[]){
        System.out.print("enter a letter > ");
        Scanner s= new Scanner(System.in);
        String x = s.next();
        System.out.println(MorseCode.valueOf(x).getPattern());
    }

}
