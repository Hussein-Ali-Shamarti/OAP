package model;

import java.util.List;

/**
 * Represents information about a product, including its code, name, scale, vendor, description,
 * stock quantity, buy price, and MSRP (Manufacturer's Suggested Retail Price).
 * 
 * This class serves as an entity class for the "product" entity and encapsulates basic information about a product.
 * 
 * @author Ole
 */
public class Products {

    private String productCode;
    private String productName;
    private String productLine;
    private String productScale;
    private String productVendor;
    private String productDescription;
    private int quantityInStock;
    private double buyPrice;
    private double msrp;

    /**
     * Constructor to create a Product object with the specified attributes.
     *
     * @param productCode       The product code.
     * @param productName       The product name.
     * @param productLine       The product line.
     * @param productScale      The product scale.
     * @param productVendor     The product vendor.
     * @param productDescription The product description.
     * @param quantityInStock   The quantity in stock.
     * @param buyPrice          The buy price.
     * @param msrp              The Manufacturer's Suggested Retail Price.
     */
    public Products(String productCode, String productName, String productLine, String productScale, String productVendor,
            String productDescription, int quantityInStock, double buyPrice, double msrp) {
        this.productCode = productCode;
        this.productName = productName;
        this.productLine = productLine;
        this.productScale = productScale;
        this.productVendor = productVendor;
        this.productDescription = productDescription;
        this.quantityInStock = quantityInStock;
        this.buyPrice = buyPrice;
        this.msrp = msrp;
    }
    
    /**
     * Retrieves a list of all product names from the database.
     * This method is intended to fetch the names of all products and return them in a list.
     *
     * @return A List of String containing the names of all products.
     *         Returns null if the product names could not be retrieved.
     */
    
    public List<String> getAllProductNames() {
        return null;
    }
    
    /**
     * Retrieves the name of the product associated with a given product code.
     * This method queries the database to find the product name corresponding to the specified product code.
     *
     * @param productCode The product code for which the product name is to be retrieved.
     * @return The name of the product associated with the given product code.
     *         Returns an empty string if no product is found or if an error occurs.
     */

    public String getProductNameByCode(String productCode) {
        return "";
    }

    /**
     * Get the product code.
     *
     * @return The product code.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Set the product code.
     *
     * @param productCode The product code to set.
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Get the product name.
     *
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Set the product name.
     *
     * @param productName The product name to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Get the product line.
     *
     * @return The product line.
     */
    public String getProductLine() {
        return productLine;
    }

    /**
     * Set the product line.
     *
     * @param productLine The product line to set.
     */
    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    /**
     * Get the product scale.
     *
     * @return The product scale.
     */
    public String getProductScale() {
        return productScale;
    }

    /**
     * Set the product scale.
     *
     * @param productScale The product scale to set.
     */
    public void setProductScale(String productScale) {
        this.productScale = productScale;
    }

    /**
     * Get the product vendor.
     *
     * @return The product vendor.
     */
    public String getProductVendor() {
        return productVendor;
    }

    /**
     * Set the product vendor.
     *
     * @param productVendor The product vendor to set.
     */
    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    /**
     * Get the product description.
     *
     * @return The product description.
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Set the product description.
     *
     * @param productDescription The product description to set.
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Get the quantity in stock.
     *
     * @return The quantity in stock.
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Set the quantity in stock.
     *
     * @param quantityInStock The quantity in stock to set.
     */
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Get the buy price.
     *
     * @return The buy price.
     */
    public double getBuyPrice() {
        return buyPrice;
    }

    /**
     * Set the buy price.
     *
     * @param buyPrice The buy price to set.
     */
    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    /**
     * Get the MSRP (Manufacturer's Suggested Retail Price).
     *
     * @return The MSRP.
     */
    public double getMsrp() {
        return msrp;
    }

    /**
     * Set the MSRP (Manufacturer's Suggested Retail Price).
     *
     * @param msrp The MSRP to set.
     */
    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }
}