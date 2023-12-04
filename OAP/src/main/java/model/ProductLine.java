package model;

/**
 * Represents a product line and encapsulates its attributes.
 * @author 7120
 */

public class ProductLine  {

    private String productLine;
    private String textDescription;
    private String htmlDescription;
    private String image;

    /**
     * Constructs a ProductLine object with the specified attributes.
     *
     * @param productLine     The product line name.
     * @param textDescription The text description of the product line.
     * @param htmlDescription The HTML description of the product line.
     * @param image           The image URL or file path for the product line.
     */
    public ProductLine(String productLine, String textDescription, String htmlDescription, String image) {
        this.productLine = productLine;
        this.textDescription = textDescription;
        this.htmlDescription = htmlDescription;
        this.image = image;
    }

    /**
     * Gets the product line name.
     *
     * @return The product line name.
     */
    public String getProductLine() {
        return productLine;
    }

    /**
     * Sets the product line name.
     *
     * @param productLine The product line name to set.
     */
    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    /**
     * Gets the text description of the product line.
     *
     * @return The text description of the product line.
     */
    public String getTextDescription() {
        return textDescription;
    }

    /**
     * Sets the text description of the product line.
     *
     * @param textDescription The text description to set.
     */
    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    /**
     * Gets the HTML description of the product line.
     *
     * @return The HTML description of the product line.
     */
    public String getHtmlDescription() {
        return htmlDescription;
    }

    /**
     * Sets the HTML description of the product line.
     *
     * @param htmlDescription The HTML description to set.
     */
    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    /**
     * Gets the image URL or file path for the product line.
     *
     * @return The image URL or file path for the product line.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image URL or file path for the product line.
     *
     * @param image The image URL or file path to set.
     */
    public void setImage(String image) {
        this.image = image;
    }
}