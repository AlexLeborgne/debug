package dbg;

public class Power {

    public Power() {
    }

    public double power(int x, int power) {
        double powerX = Math.pow(x, power);
        return powerX;
    }

    public void printPower(int x, int power) {
        double powerX = power(x, power);
        System.out.println(powerX);
    }
}
