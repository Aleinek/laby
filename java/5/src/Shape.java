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

    public double getRotationAngle() {
        return rotationAngle;
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
                // Simplified rectangle contains logic, doesn't account for rotation
                double halfWidth = width / 2.0;
                double halfHeight = height / 2.0;
                double minX = x - halfWidth;
                double maxX = x + halfWidth;
                double minY = y - halfHeight;
                double maxY = y + halfHeight;
                return px >= minX && px <= maxX && py >= minY && py <= maxY;
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

    // Resize method to scale the shape
    public void resize(double scaleFactor) {
        switch (type) {
            case "Circle":
            case "Rectangle":
                // Ensure minimum width and height to avoid disappearing shapes
                if (width * scaleFactor > 5 && height * scaleFactor > 5) { // Minimum size constraint
                    width *= scaleFactor;
                    height *= scaleFactor;
                }
                break;
            case "Polygon":
                if (xPoints != null && yPoints != null) {
                    for (int i = 0; i < xPoints.length; i++) {
                        // Scale each point relative to the center, ensure it doesn't collapse
                        double dx = xPoints[i] - x;
                        double dy = yPoints[i] - y;
                        xPoints[i] = x + dx * scaleFactor;
                        yPoints[i] = y + dy * scaleFactor;
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