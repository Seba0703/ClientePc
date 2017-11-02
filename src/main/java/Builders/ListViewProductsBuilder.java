package Builders;

import Common.AlertBox;
import Common.Consts;
import OS_Command.WindowsCommand;
import OnPostExecuteHandlers.Product;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ListViewProductsBuilder {

    private VBox mainVbox;

    public ListViewProductsBuilder(VBox primaryStage) {
        mainVbox = primaryStage;
    }

    /**llamar siempre en el thread de javaFX
     */
    public void build(ObservableList<Product> products) {

        TableColumn<Product, String> nameColumn = new TableColumn<>("Insumo");
        nameColumn.setMinWidth(250);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> toBuyColumn = new TableColumn<>("A comprar");
        toBuyColumn.setMinWidth(100);
        toBuyColumn.setCellValueFactory(new PropertyValueFactory<>("toBuy"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Stock actual");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Integer> stockMaxColumn = new TableColumn<>("Stock máximo");
        stockMaxColumn.setMinWidth(100);
        stockMaxColumn.setCellValueFactory(new PropertyValueFactory<>("stockMax"));

        TableView<Product> table = new TableView<>();
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setItems(products);
        table.getColumns().addAll(nameColumn, toBuyColumn, quantityColumn, stockMaxColumn);

        table.setRowFactory(new Callback<TableView<Product>, TableRow<Product>>() {
            @Override
            public TableRow<Product> call(TableView<Product> tableView) {
                final TableRow<Product> row = new TableRow<Product>() {
                    @Override
                    protected void updateItem(Product row, boolean empty) {
                        super.updateItem(row, empty);

                        if (!empty) {
                            if (row.getResult() == Consts.WHITE) {
                                setStyle("-fx-control-inner-background: antiquewhite  ;\n" +
                                        "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                        "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                            } else if (row.getResult() == Consts.YELLOW) {
                                setStyle("-fx-control-inner-background: orange;\n" +
                                        "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                        "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                            } else {
                                setStyle("-fx-control-inner-background: firebrick  ;\n" +
                                        "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                        "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                            }
                        }else {
                            setStyle("-fx-control-inner-background: antiquewhite;\n" +
                                    "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                    "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                        }
                    }
                };
                return row;
            }});

        VBox vBox = new VBox(10);

        HBox hRef = new HBox(10);
        hRef.setAlignment(Pos.CENTER);

        Label danger = new Label("Peligro: ");
        Label careful = new Label("Cuidado: ");
        Label ok = new Label("Bueno: ");

        Rectangle yellow = new Rectangle(20,10);
        yellow.setStroke(Color.ORANGE);
        yellow.setFill(Color.ORANGE);
        yellow.setStrokeWidth(3);

        Rectangle red = new Rectangle(20,10);
        red.setStroke(Color.FIREBRICK);
        red.setFill(Color.FIREBRICK);
        red.setStrokeWidth(3);

        Rectangle white = new Rectangle(20,10);
        white.setStroke(Color.ANTIQUEWHITE);
        white.setFill(Color.ANTIQUEWHITE);
        white.setStrokeWidth(3);

        Hyperlink help = new Hyperlink("¿Ayuda puntual?");
        help.setOnAction(e-> WindowsCommand.goPDF(20));
        Label ref = new Label("Referencias: ");
        hRef.getChildren().addAll(ref, ok, white, careful, yellow, danger, red, help);

        HBox hRefBut = new HBox(10);
        hRefBut.setAlignment(Pos.CENTER);
        Button goExcel = new Button("Exportar a Excel");
        goExcel.setOnAction(e -> {
            String title = "Nombre;A comprar;Stock Actual;Stock máximo\n";
            String username = System.getProperty("user.name");
            String path = Consts.ROOT_PATH + username + Consts.NEXT_ROOT_PATH + Consts.INSUMOS_STOCK;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
                out.write(title.getBytes());

                for (Product product : products){

                    String row = product.getName() + ";" + product.getToBuy() + ";" + product.getQuantity() + ";" + product.getStockMax() + "\n";
                    out.write(row.getBytes());
                }

                out.close();
                WindowsCommand.goExcel(path);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                AlertBox.display("ERROR", "No se pudo realizar la operación.");
            } catch (IOException e1) {
                AlertBox.display("ERROR", "No se pudo realizar la operación.");
            }
        });

        hRefBut.getChildren().addAll(goExcel);

        vBox.getChildren().addAll(table,hRef,hRefBut);

        //elimina si tiene uno en su lugar
        if (mainVbox.getChildren().size() == 2) {
            mainVbox.getChildren().remove(1);
        }

        mainVbox.getChildren().add(vBox);

    }

}
