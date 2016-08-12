package Common;

import InternetTools.InternetClient;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;

public class TaskCreator {

    private Service<Void> service;

    public TaskCreator(InternetClient client, LoadingBox loadingBox) {

        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            client.connect();
                        }catch (IOException err) {
                            Platform.runLater(() -> {
                                loadingBox.close();
                                AlertBox.displayServerOffError();
                            });
                        }
                        return null;
                    }
                };
            }
        };
    }

    public void start() {
        service.start();
    }

}
