class Romb extends Czworokat {
    public Romb(double bok1, double kat) {
        super(bok1, bok1, bok1, bok1, kat);
    }

    @Override
    public double obwod() {
        return 4 * bok1;
    }

    @Override
    public double pole() {
        return bok1 * bok1 * Math.sin(Math.toRadians(kat));
    }

    @Override
    public String nazwa() {
        return "Romb";
    }
}