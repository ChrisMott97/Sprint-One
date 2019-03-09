package org.gsep.carousel;

/*
 * Item.
 *
 * @author  Chris Mott.
 * @version 2.00, March 2019.
 */
public abstract class Item {

    private int id;

    private String name;

    private String imageURL;

    private String prefix;

    /**
     * Constructor for an Item with no image.
     */
    public Item(){

    }

    /**
     * Constructor for an Item with a title and image.
     *
     * @param name name of the item.
     * @param imageURL the relative path to the item image.
     */
    public Item(String name, String imageURL){
        this.name = name;
        this.imageURL = imageURL;
    }//TODO: Change to new item based on ID

    /**
     * Getter for name.
     *
     * @return name.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Setter for name
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for ID, mainly for JSON reading.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the ID, mainly for JSON writing.
     *
     * @param id the id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for Image Url if not initially set.
     *
     * @param imageURL relative path to image.
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Getter for Image Url.
     *
     * @return relative path to image.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Gets the correct file prefix for finding images.
     *
     * @return the string prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the correct file prefix for images.
     *
     * @param prefix the prefix to set.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return name;
    }
}