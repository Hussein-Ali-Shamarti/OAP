package model;

/**
 * 
 * File: Products.java
 * Description: This is an entity class for "product" and encapsulates the basic information of a product.
 * This class represents information about a product's attributes, such as code, name, scale, vendor, description, stock quantity, buy price, and MSRP.
 * @author Ole
 * @version 09.11.2023
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
    * get the product code
    * @return productCode
    */

    public String getProductCode() {
        return productCode;
    }

    /**
     * set the productCode
     * @param productCode
     */

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
    * get the productName  
    * @return productName
    */

    public String getProductName() {
        return productName;
    }

    /**
     * set the productName
     * @param productName
     */

    public void setProductName(String productName) {
        this.productName = productName;
    }
    /**
     * get the productLine  
     * @return productLine
     */

     public String getProductLine() {
         return productLine;
     }

     /**
      * set the productLine
      * @param productLine
      */

     public void setProductLine(String productLine) {
         this.productLine = productLine;
     }

    /**
     * get the productScale
     * @return productScale
     */
  

    public String getProductScale() {
        return productScale;
    }

    /**
     * set the productScale
     * @param productScale
     */

    public void setProductScale(String productScale) {
        this.productScale = productScale;
    }

    /**
     * get the productVendor
     * @return productVendor
     */ 

    public String getProductVendor() {
        return productVendor;
    }

    /**
     * set the productVendor
     * @param productVendor
     */

    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }

    /**
     * get the productDescription
     * @return productDescription
     */

    public String getProductDescription() {
        return productDescription;
    } 
    
    /**
     * set the productDescription
     * @param productDescription
     */


    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }   

    /**
     * get the quantityInStock
     * @return quantityInStock
     */


    public int getQuantityInStock() {
        return quantityInStock;
    } 
    
    /**
     * set the quantityInStock
     * @param quantityInStock
     */

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }   

    /**
     * get the buyPrice
     * @return buyPrice
     */

    public double getBuyPrice() {
        return buyPrice;
    }   

    /**
     * set the buyPrice
     * @param buyPrice
     */

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }   

    /**
     * get the msrp
     * @return msrp
     */

    public double getMsrp() {
        return msrp;
    }  
    
    /**
     * set the msrp
     * @param msrp
     */

    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }   
}