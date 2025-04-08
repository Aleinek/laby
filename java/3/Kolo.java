class Kolo implements Figura {
    private double promien;

    public Kolo(double promien) {
        this.promien = promien;
    }

    @Override
    public double obwod() {
        return 2 * Math.PI * promien;
    }

    @Override
    public double pole() {
        return Math.PI * promien * promien;
    }

    @Override
    public String nazwa() {
        return "Koło";
    }
}