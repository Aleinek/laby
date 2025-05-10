import javafx.scene.paint.Color;

import java.io.Serializable;

public class Shape implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private double x, y;
    private double width, height;
    private double[] xPoints, yPoints;
    private int pointCount;
    private String colorHex; // Store color as a hex string for serialization

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
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.pointCount = pointCount;
        this.colorHex = colorToHex(color);
    }

    // Getters
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

    // Setters for moving
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXPoints(double[] xPoints) {
        this.xPoints = xPoints;
    }

    public void setYPoints(double[] yPoints) {
        this.yPoints = yPoints;
    }

    // Contains method to check if a point is inside the shape
    public boolean contains(double px, double py) {
        switch (type) {
            case "Circle":
                double radius = width / 2.0;
                double centerX = x;
                double centerY = y;
                return Math.pow(px - centerX, 2) + Math.pow(py - centerY, 2) <= Math.pow(radius, 2);
            case "Rectangle":
                return px >= x - width / 2.0 && px <= x + width / 2.0 &&
                        py >= y - height / 2.0 && py <= y + height / 2.0;
            case "Polygon":
                // Use point-in-polygon algorithm (simplified for convex polygons)
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

    // Move method to update the position of the shape
    public void move(double dx, double dy) {
        switch (type) {
            case "Circle":
            case "Rectangle":
                x += dx;
                y += dy;
                break;
            case "Polygon":
                if (xPoints != null && yPoints != null) {
                    for (int i = 0; i < xPoints.length; i++) {
                        xPoints[i] += dx;
                        yPoints[i] += dy;
                    }
                }
                break;
        }
    }

    // Convert Color to Hex String
    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // Convert Hex String to Color
    private Color hexToColor(String hex) {
        return Color.web(hex);
    }
}