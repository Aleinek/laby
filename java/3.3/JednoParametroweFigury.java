public enum JednoParametroweFigury implements FiguraJednoParametrowa {
    KOLO(1) {
        @Override
        public double obliczPole() {
            return Math.PI * parametr * parametr;
        }

        @Override
        public double obliczObwod() {
            return 2 * Math.PI * parametr;
        }

        @Override
        public String podajNazwe() {
            return "Koło";
        }
    },
    KWADRAT(1) {
        @Override
        public double obliczPole() {
            return parametr * parametr;
        }

        @Override
        public double obliczObwod() {
            return 4 * parametr;
        }

        @Override
        public String podajNazwe() {
            return "Kwadrat";
        }
    },
    PIECIOKAT(1) {
        @Override
        public double obliczPole() {
            return (5 * parametr * parametr) / (4 * Math.tan(Math.PI / 5));
        }

        @Override
        public double obliczObwod() {
            return 5 * parametr;
        }

        @Override
        public String podajNazwe() {
            return "Pięciokąt foremny";
        }
    },
    SZESCIOKAT(1) {
        @Override
        public double obliczPole() {
            return (3 * Math.sqrt(3) * parametr * parametr) / 2;
        }

        @Override
        public double obliczObwod() {
            return 6 * parametr;
        }
        
        @Override
        public String podajNazwe() {
            return "Sześciokąt foremny";
        }
    };

    protected double parametr;

    JednoParametroweFigury(double parametr) {
        this.parametr = parametr;
    }

    public void setParametr(double parametr) {
        this.parametr = parametr;
    }
}