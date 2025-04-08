class Pieciokat implements Figura {
    private double bok;

    public Pieciokat(double bok) {
        this.bok = bok;
    }

    @Override
    public double obwod() {
        return 5 * bok;
    }

    @Override
    public double pole() {
        return (5 * bok * bok) / (4 * Math.tan(Math.PI / 5));
    }

    @Override
    public String nazwa() {
        return "Pięciokąt foremny";
    }
}