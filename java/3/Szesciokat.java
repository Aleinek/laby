class Szesciokat implements Figura {
    private double bok;

    public Szesciokat(double bok) {
        this.bok = bok;
    }

    @Override
    public double obwod() {
        return 6 * bok;
    }

    @Override
    public double pole() {
        return (3 * Math.sqrt(3) * bok * bok) / 2;
    }

    @Override
    public String nazwa() {
        return "Sześciokąt foremny";
    }
}