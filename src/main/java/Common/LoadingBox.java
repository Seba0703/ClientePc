package Common;

import javafx.animation.Animation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadingBox {

    private Stage window;

    public LoadingBox(){
        window = new Stage();
    }

    public void display(String title, String message, String path) {

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(270);
        window.setMinHeight(200);

        window.setOnCloseRequest(Event::consume);

        Label label = new Label();
        label.setText(message);
        label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));

        Image image = new Image("file:" + path);
        ImageView view = new ImageView(image);

        VBox layout = new VBox(10);
        layout.getChildren().addAll( label, view);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    public void close() {
        window.close();
    }

}
