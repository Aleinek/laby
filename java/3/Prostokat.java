class Prostokat extends Czworokat {
    public Prostokat(double bok1, double bok2) {
        super(bok1, bok2, bok1, bok2, 90);
    }

    @Override
    public double obwod() {
        return 2 * (bok1 + bok2);
    }

    @Override
    public double pole() {
        return bok1 * bok2;
    }

    @Override
    public String nazwa() {
        return "Prostokąt";
    }
}