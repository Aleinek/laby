public enum DwuParametroweFigury implements FiguraDwuParametrowa {
        PROSTOKAT(1, 1) {
            @Override
            public double obliczPole() {
                return parametr1 * parametr2;
            }

            @Override
            public double obliczObwod() {
                return 2 * (parametr1 + parametr2);
            }

            @Override
            public String podajNazwe() {
                return "Prostokąt";
            }
        },
        ROMB(1, 1) {
            @Override
            public double obliczPole() {
                return parametr1 * parametr1 * Math.sin(Math.toRadians(parametr2));
            }

            @Override
            public double obliczObwod() {
                return 4 * parametr1;
            }

            @Override
            public String podajNazwe() {
                return "Romb";
            }
        };

        protected double parametr1;
        protected double parametr2;

        DwuParametroweFigury(double parametr1, double parametr2) {
            this.parametr1 = parametr1;
            this.parametr2 = parametr2;
        }

        public void setParametry(double parametr1, double parametr2) {
            this.parametr1 = parametr1;
            this.parametr2 = parametr2;
        }
    }