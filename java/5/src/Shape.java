import javafx.scene.paint.Color;
import java.io.Serializable;

/**
 * Reprezentuje ogólną figurę geometryczną (koło, prostokąt lub wielokąt).
 * Obsługuje rysowanie, przesuwanie, skalowanie oraz obrót figury.
 */
public class Shape implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private double x, y;
    private double width, height;
    private double[] xPoints, yPoints;
    private double[] originalXPoints, originalYPoints;
    private int pointCount;
    private String colorHex;
    private double rotationAngle = 0.0;

    /**
     * Konstruktor figury koła lub prostokąta.
     * 
     * @param type Typ figury ("Circle" lub "Rectangle")
     * @param x Współrzędna X środka
     * @param y Współrzędna Y środka
     * @param width Szerokość figury
     * @param height Wysokość figury
     * @param color Kolor figury
     */
    public Shape(String type, double x, double y, double width, double height, Color color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorHex = colorToHex(color);
    }

    /**
     * Konstruktor figury typu wielokąt.
     * 
     * @param type Typ figury ("Polygon")
     * @param xPoints Tablica współrzędnych X punktów
     * @param yPoints Tablica współrzędnych Y punktów
     * @param pointCount Liczba punktów
     * @param color Kolor figury
     */
    public Shape(String type, double[] xPoints, double[] yPoints, int pointCount, Color color) {
        this.type = type;
        this.xPoints = xPoints.clone();
        this.yPoints = yPoints.clone();
        this.originalXPoints = xPoints.clone();
        this.originalYPoints = yPoints.clone();
        this.pointCount = pointCount;
        this.colorHex = colorToHex(color);
        updateCentroid();
    }

    // --- Gettery i Settery z Javadoc ---

    /** @return Typ figury */
    public String getType() {
        return type;
    }

    /** @return Współrzędna X środka figury */
    public double getX() {
        return x;
    }

    /** @return Współrzędna Y środka figury */
    public double getY() {
        return y;
    }

    /** @return Szerokość figury */
    public double getWidth() {
        return width;
    }

    /** @return Wysokość figury */
    public double getHeight() {
        return height;
    }

    /** @return Tablica współrzędnych X punktów figury (dla wielokąta) */
    public double[] getXPoints() {
        return xPoints;
    }

    /** @return Tablica współrzędnych Y punktów figury (dla wielokąta) */
    public double[] getYPoints() {
        return yPoints;
    }

    /** @return Liczba punktów figury (dla wielokąta) */
    public int getPointCount() {
        return pointCount;
    }

    /** @return Kolor figury */
    public Color getColor() {
        return hexToColor(colorHex);
    }

    /** @return Kąt obrotu figury */
    public double getRotationAngle() {
        return rotationAngle;
    }

    /**
     * Ustawia kąt obrotu figury.
     * 
     * @param angle Kąt w stopniach
     */
    public void setRotationAngle(double angle) {
        this.rotationAngle = angle % 360;
        if ("Polygon".equals(type)) {
            rotatePolygon();
        }
    }

    /**
     * Sprawdza, czy punkt znajduje się wewnątrz figury.
     * 
     * @param px Współrzędna X punktu
     * @param py Współrzędna Y punktu
     * @return true jeśli punkt znajduje się wewnątrz, w przeciwnym razie false
     */
    public boolean contains(double px, double py) {
        switch (type) {
            case "Circle":
                double radius = width / 2.0;
                return Math.pow(px - x, 2) + Math.pow(py - y, 2) <= Math.pow(radius, 2);
            case "Rectangle":
                double halfWidth = width / 2.0;
                double halfHeight = height / 2.0;
                double cosTheta = Math.cos(Math.toRadians(rotationAngle));
                double sinTheta = Math.sin(Math.toRadians(rotationAngle));
                double localX = cosTheta * (px - x) + sinTheta * (py - y);
                double localY = -sinTheta * (px - x) + cosTheta * (py - y);
                return localX >= -halfWidth && localX <= halfWidth && localY >= -halfHeight && localY <= halfHeight;
            case "Polygon":
                boolean inside = false;
                for (int i = 0, j = pointCount - 1; i < pointCount; j = i++) {
                    if ((yPoints[i] > py) != (yPoints[j] > py) &&
                            (px < (xPoints[j] - xPoints[i]) * (py - yPoints[i]) / (yPoints[j] - yPoints[i]) + xPoints[i])) {
                        inside = !inside;
                    }
                }
                return inside;
        }
        return false;
    }

    /**
     * Przesuwa figurę o podany wektor.
     * 
     * @param dx Przesunięcie w osi X
     * @param dy Przesunięcie w osi Y
     */
    public void move(double dx, double dy) {
        switch (type) {
            case "Circle":
            case "Rectangle":
                x += dx;
                y += dy;
                break;
            case "Polygon":
                for (int i = 0; i < pointCount; i++) {
                    xPoints[i] += dx;
                    yPoints[i] += dy;
                    originalXPoints[i] += dx;
                    originalYPoints[i] += dy;
                }
                x += dx;
                y += dy;
                break;
        }
    }

    /**
     * Skaluje figurę według współczynnika.
     * 
     * @param scaleFactor Współczynnik skalowania
     */
    public void resize(double scaleFactor) {
        switch (type) {
            case "Circle":
            case "Rectangle":
                if (width * scaleFactor > 5 && height * scaleFactor > 5) {
                    width *= scaleFactor;
                    height *= scaleFactor;
                }
                break;
            case "Polygon":
                for (int i = 0; i < pointCount; i++) {
                    double dx = originalXPoints[i] - x;
                    double dy = originalYPoints[i] - y;
                    originalXPoints[i] = x + dx * scaleFactor;
                    originalYPoints[i] = y + dy * scaleFactor;
                }
                rotatePolygon();
                break;
        }
    }

    /**
     * Ustawia kolor figury.
     * 
     * @param color Nowy kolor
     */
    public void setColor(Color color) {
        this.colorHex = colorToHex(color);
    }

    // --- Metody pomocnicze ---

    private void rotatePolygon() {
        double radians = Math.toRadians(rotationAngle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        double centroidX = 0;
        double centroidY = 0;
        for (int i = 0; i < pointCount; i++) {
            centroidX += originalXPoints[i];
            centroidY += originalYPoints[i];
        }
        centroidX /= pointCount;
        centroidY /= pointCount;

        for (int i = 0; i < pointCount; i++) {
            double dx = originalXPoints[i] - centroidX;
            double dy = originalYPoints[i] - centroidY;
            xPoints[i] = centroidX + (dx * cosTheta - dy * sinTheta);
            yPoints[i] = centroidY + (dx * sinTheta + dy * cosTheta);
        }

        x = centroidX;
        y = centroidY;
    }

    private void updateCentroid() {
        double sumX = 0, sumY = 0;
        for (int i = 0; i < pointCount; i++) {
            sumX += xPoints[i];
            sumY += yPoints[i];
        }
        this.x = sumX / pointCount;
        this.y = sumY / pointCount;
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Color hexToColor(String hex) {
        return Color.web(hex);
    }
}