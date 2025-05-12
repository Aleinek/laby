import javafx.scene.paint.Color;
import java.io.Serializable;

/**
 * Represents a general geometric shape (circle, rectangle, or polygon).
 * Supports drawing, moving, scaling, and rotating the shape.
 */
public class Shape implements Serializable {
    /**
     * Serial version UID for ensuring compatibility during deserialization.
     */
    private static final long serialVersionUID = 1L;

    /** The type of the shape (Circle, Rectangle, Polygon). */
    private String type;

    /** The X-coordinate of the shape's center. */
    private double x;

    /** The Y-coordinate of the shape's center. */
    private double y;

    /** The width of the shape. */
    private double width;

    /** The height of the shape. */
    private double height;

    /** Array of X-coordinates for the shape's points (for polygons). */
    private double[] xPoints;

    /** Array of Y-coordinates for the shape's points (for polygons). */
    private double[] yPoints;

    /** Original X-coordinates of the polygon's points. */
    private double[] originalXPoints;

    /** Original Y-coordinates of the polygon's points. */
    private double[] originalYPoints;

    /** Number of points in the shape (for polygons). */
    private int pointCount;

    /** The color of the shape in HEX format. */
    private String colorHex;

    /** The rotation angle of the shape, in degrees. */
    private double rotationAngle = 0.0;

    /**
     * Constructs a shape of type circle or rectangle.
     *
     * @param type The type of the shape (Circle or Rectangle).
     * @param x The X-coordinate of the shape's center.
     * @param y The Y-coordinate of the shape's center.
     * @param width The width of the shape.
     * @param height The height of the shape.
     * @param color The color of the shape.
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
     * Constructs a polygon shape.
     *
     * @param type The type of the shape (Polygon).
     * @param xPoints Array of X-coordinates for the points of the polygon.
     * @param yPoints Array of Y-coordinates for the points of the polygon.
     * @param pointCount The number of points in the polygon.
     * @param color The color of the polygon.
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

    // --- Getters and Setters with Javadoc ---

    /**
     * Returns the type of the shape.
     *
     * @return The type of the shape.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the X-coordinate of the shape's center.
     *
     * @return The X-coordinate of the shape's center.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y-coordinate of the shape's center.
     *
     * @return The Y-coordinate of the shape's center.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the width of the shape.
     *
     * @return The width of the shape.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the shape.
     *
     * @return The height of the shape.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the array of X-coordinates for the shape's points (for polygons).
     *
     * @return Array of X-coordinates for the shape's points.
     */
    public double[] getXPoints() {
        return xPoints;
    }

    /**
     * Returns the array of Y-coordinates for the shape's points (for polygons).
     *
     * @return Array of Y-coordinates for the shape's points.
     */
    public double[] getYPoints() {
        return yPoints;
    }

    /**
     * Returns the number of points in the shape (for polygons).
     *
     * @return The number of points in the shape.
     */
    public int getPointCount() {
        return pointCount;
    }

    /**
     * Returns the color of the shape.
     *
     * @return The color of the shape.
     */
    public Color getColor() {
        return hexToColor(colorHex);
    }

    /**
     * Returns the rotation angle of the shape.
     *
     * @return The rotation angle of the shape.
     */
    public double getRotationAngle() {
        return rotationAngle;
    }

    /**
     * Sets the rotation angle of the shape.
     * If the shape is a polygon, updates the rotation.
     *
     * @param angle The new rotation angle in degrees.
     */
    public void setRotationAngle(double angle) {
        this.rotationAngle = angle % 360;
        if ("Polygon".equals(type)) {
            rotatePolygon();
        }
    }

    /**
     * Checks if a point is inside the shape.
     *
     * @param px The X-coordinate of the point.
     * @param py The Y-coordinate of the point.
     * @return True if the point is inside the shape; false otherwise.
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
            default:
                return false;
        }
    }

    /**
     * Moves the shape by a specified offset in the X and Y directions.
     *
     * @param dx The offset in the X direction.
     * @param dy The offset in the Y direction.
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
     * Scales the shape by a specified factor.
     * Ensures the width and height remain above a minimum size.
     *
     * @param scaleFactor The scaling factor.
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
     * Updates the color of the shape.
     *
     * @param color The new color of the shape.
     */
    public void setColor(Color color) {
        this.colorHex = colorToHex(color);
    }

    // --- Helper Methods ---

    /**
     * Rotates the polygon based on the current rotation angle.
     */
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

    /**
     * Updates the centroid of the polygon based on its points.
     */
    private void updateCentroid() {
        double sumX = 0, sumY = 0;
        for (int i = 0; i < pointCount; i++) {
            sumX += xPoints[i];
            sumY += yPoints[i];
        }
        this.x = sumX / pointCount;
        this.y = sumY / pointCount;
    }

    /**
     * Converts a color to its HEX string representation.
     *
     * @param color The color to convert.
     * @return The HEX string representation of the color.
     */
    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Converts a HEX string representation of a color to a Color object.
     *
     * @param hex The HEX string representation of the color.
     * @return The Color object.
     */
    private Color hexToColor(String hex) {
        return Color.web(hex);
    }
}