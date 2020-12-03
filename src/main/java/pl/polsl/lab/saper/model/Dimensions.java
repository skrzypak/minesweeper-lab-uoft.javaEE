package pl.polsl.lab.saper.model;

/**
 * Class used define board dimensions
 *
 * @author Konrad Skrzypczyk
 * @version 1.0
 */
public class Dimensions {
    private final Integer height;  // Contain index of row
    private final Integer width;  // Contain index of col

    /**
     * Class constructor.
     *
     * @param height height value
     * @param width  width value
     */
    public Dimensions(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Get height integer.
     *
     * @return the integer
     */
    public Integer getHeight() {
        return this.height;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Integer getWidth() {
        return this.width;
    }

}
