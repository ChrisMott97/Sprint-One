package org.gsep.carousel;

import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Carousel.
 *
 * @author  Chris Mott.
 * @version 2.00, March 2019.
 */
public class Carousel extends TilePane{

    private static final int amountContainers = 5;

    private ItemModel iModel;
    private ItemContainerModel icModel;

    @FXML
    protected ItemContainer icOne;

    private List<ItemContainer> containers = new ArrayList<>();

    /**
     * Constructor for Carousel
     */
    public Carousel(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CarouselView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Acts like a constructor and runs when the controller is created.
     */
    public void initialize() {
        //May need to be moved elsewhere (Model?)
        double tileWidth = (this.getPrefWidth()/amountContainers);
        this.setPrefTileWidth(tileWidth);
        this.setPrefTileHeight(tileWidth);

        //Keeps track of containers
        for (Node node :
                this.getChildren()) {
            ItemContainer ic = (ItemContainer) node;
            containers.add(ic);
            int ind = containers.indexOf(ic);
            containers.get(ind).setInitialPosition(ind+1);
        }


    }

    /**
     * Links the necessary models to the carousel.
     *
     * @param itemModel the Item Model to be linked.
     * @param itemContainerModel the Item Container Model to be linked.
     */
    public void linkModels(ItemModel itemModel, ItemContainerModel itemContainerModel){
        this.iModel = itemModel;
        this.icModel = itemContainerModel;
    }

    /**
     * Loads a list of items to the Item Model and loads the necessary items into the containers.
     *
     * @param items the items to be given to the models.
     */
    public void ingest(List<? extends Item> items){
        //TODO: Handle no songs found
        Collections.sort(items); //Sort alphabetically
        iModel.loadData(items);
        icModel.loadData(containers);
        icModel.map(items);
    }

    /**
     * Cycles all item and item container lists to the next intended item.
     * Only works if there are no current animations running.
     */
    public void next(){
        if(icOne.getStatus() == Animation.Status.RUNNING)
            return;
        iModel.next();
        icModel.next();
        icModel.map(iModel.getVisible());
    }

    /**
     * Cycles all item and item container lists to the previous intended item.
     * Only works if there are no current animations running.
     */
    public void previous(){
        //TODO: Create better way to check if animation is still going, or queue input.
        if(icOne.getStatus() == Animation.Status.RUNNING)
            return;
        iModel.previous();
        icModel.previous();
        icModel.map(iModel.getVisible());
    }

}
