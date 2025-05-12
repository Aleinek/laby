import javafx.scene.paint.Color;

import java.io.Serializable;

public class Shape implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private double x, y;
    private double width, height;
    private double[] xPoints, yPoints;
    private double[] originalXPoints, originalYPoints;
    private int pointCount;
    private String colorHex; // Store color as a hex string for serialization
    private double rotationAngle = 0.0; // Rotation angle in degrees

    // Constructor for Circle and Rectangle
    public Shape(String type, double x, double y, double width, double height, Color color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorHex = colorToHex(color);
    }

    // Constructor for Polygon
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

    public String getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double[] getXPoints() {
        return xPoints;
    }

    public double[] getYPoints() {
        return yPoints;
    }

    public int getPointCount() {
        return pointCount;
    }

    public Color getColor() {
        return hexToColor(colorHex);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle % 360; // Keep it within 0-360 degrees
        if ("Polygon".equals(type)) {
            rotatePolygon();
        }
    }

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
                rotatePolygon(); // Reapply current rotation
                break;
        }
    }

    private void rotatePolygon() {
        double radians = Math.toRadians(rotationAngle);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);

        // Oblicz środek ciężkości z oryginalnych punktów
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

    public void setColor(Color color) {
        this.colorHex = colorToHex(color);
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
