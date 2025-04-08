class Kwadrat extends Czworokat {
    public Kwadrat(double bok) {
        super(bok, bok, bok, bok, 90);
    }

    @Override
    public double obwod() {
        return 4 * bok1;
    }

    @Override
    public double pole() {
        return bok1 * bok1;
    }

    @Override
    public String nazwa() {
        return "Kwadrat";
    }
}