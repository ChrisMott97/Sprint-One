package org.gsep.play;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.gsep.controller.Button;
import org.gsep.select.MusicItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.gsep.ButtonNames;
import org.gsep.ButtonNumbers;

/**
 * Play mode
 *
 * @author Örs Barkanyi
 * @author Abigail Lilley
 */
public class Play {
    static final int CANVASWIDTH = 950;
    static final int CANVASHEIGHT = 700;
    private Scene scene;
    private NoteHighwayModel model;
    private NoteHighwayView view;
    private NoteHighwayController controller;
    private File midiFile;
    private Map<Integer, Note[]> songSequence;

    /**
     * @author Örs Barkanyi
     * @author Abigail Lilley
     * Constructor for Play Mode
     *
     * @param musicItem the object representing the music resources to be played
     * @param module reference to the play module
     */
    Play(MusicItem musicItem, PlayModule module){
                                                                                                  /* Initialise scene */
        Group root = new Group();
        this.scene = new Scene(root);

        root.getChildren().add(createBackground());

                                                                                                        /* Set up MVC */
        this.model = new NoteHighwayModel();
        this.view = new NoteHighwayView(root);
        this.controller = new NoteHighwayController(model, view);

        linkGuitar(module);

                                                                                                        /* Find files */
        try{
            this.songSequence = readFile(musicItem.getNoteFile());
        } catch (Exception e) {
            System.out.println("Note file not found or invalid");
            e.printStackTrace();
            System.exit(1);
        }

        try{
            this.midiFile = musicItem.getMidiFile();
        } catch (Exception e){
            System.out.println("MIDI file Not found");
            e.printStackTrace();
            System.exit(1);
        }
    }


    public Scene getScene() {
        return scene;
    }

    /**
     * @author Örs Barkanyi
     * Invokes the start play mode
     */
    public void play(){
        view.startRender();
        try{
            controller.play(songSequence,midiFile);
        } catch (Exception e){
            System.out.println("Couldn't play MIDI file");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @author humzahmalik
     * Setting bakckground image as the fret board
     */
    private ImageView createBackground(){
        File file = new File(getClass().getResource("/play/highway.png").getFile());
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(CANVASWIDTH);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    /**
     * @author humzahmalik
     * Method that reads notes file into an array
     * @return
     * @throws IOException
     *
     */
    private LinkedHashMap readFile(File f) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(f));
        String str;

        //Create ArrayList to hold the lists of notes
        //Create dictionary
        LinkedHashMap mapA = new LinkedHashMap();

        //While there is a line, add it to the list
        while((str = in.readLine()) != null){

            //Create dictionary value list
            Note[] dictValue;

            //split string
            String[] split = str.split(",");
            //Create dictioanry value list
            dictValue = new Note[] {null, null, null};
            dictValue[0]=checkNote(Integer.parseInt(split[1]));

            dictValue[1]=checkNote(Integer.parseInt(split[2]));

            dictValue[2]=checkNote(Integer.parseInt(split[3]));

            //add to dictionary
            mapA.put(Integer.parseInt(split[0]), dictValue);
        }
        return mapA;
    }

    /**
     * @author humzahmalik
     * Method checking whether number corresponds to black, white or empty note
     * @return Note value
     */
    private Note checkNote(int num) {
        Note type = null;

        if(num==0) {
            type= Note.OPEN;
        }
        if(num==1) {
            type= Note.BLACK;
        }
        if(num==2) {
            type= Note.WHITE;
        }

        return type;

    }

    /**
     * Connect to the guitar and start a thread to listen to each button.
     * Compatible across Macintosh, Unix, and Windows system.
     *
     * @author Abigail Lilley
     * @param module        Instance of the current PlayModule
     */
    private void linkGuitar(PlayModule module) {
        try {
            int[] buttonNums;
            String osName = System.getProperty("os.name");
            if (osName == null) {
                throw new IOException("os.name not found");
            }
            osName = osName.toLowerCase(Locale.ENGLISH);
            if (osName.contains("windows")) {                           /* Set button numbers according to current OS */
                buttonNums = ButtonNumbers.WINDOWSNUMBERS.getNumbers();
            } else if (osName.contains("linux")
                    || osName.contains("mpe/ix")
                    || osName.contains("freebsd")
                    || osName.contains("irix")
                    || osName.contains("digital unix")
                    || osName.contains("unix")) {
                buttonNums = ButtonNumbers.UNIXNUMBERS.getNumbers();
            } else if (osName.contains("mac os")) {
                buttonNums = ButtonNumbers.MACNUMBERS.getNumbers();
            } else {
                throw new IOException("os.name not supported");
            }

            ControllerEnvironment cenv = ControllerEnvironment.getDefaultEnvironment();
            Controller[] ctrls = cenv.getControllers();
            GuitarEventHandler guitarEventHandler = new GuitarEventHandler(controller, module);

            Button[] buttons = new Button[ButtonNames.BUTTONNAMES.getNames().length];
            for (int i = 0; i < buttons.length; i = i + 1) {
                buttons[i] = new Button(ButtonNames.BUTTONNAMES.getNames()[i], buttonNums[i]);
                buttons[i].addButtonListener(guitarEventHandler);/* Adding listeners to Buttons depending on the mode */
                Thread buttonThread = new Thread(buttons[i]);
                buttonThread.start();                                           /* Starting a thread for each Button */
            }
    } catch (IOException ex) {                                                  /* OS not identified, can't run game */
            ex.getMessage();
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
