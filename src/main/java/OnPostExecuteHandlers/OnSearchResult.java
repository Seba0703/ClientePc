package OnPostExecuteHandlers;

import Common.AlertBox;
import Common.Consts;
import Common.LoadingBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import static javafx.application.Platform.runLater;

public class OnSearchResult implements OnPostExecute {

    private LoadingBox loadingBox;
    private VBox searchV;
    private String path;

    public OnSearchResult(VBox search, LoadingBox loadingBox, String path) {
        this.loadingBox = loadingBox;
        searchV = search;
        this.path = path;
    }

    @Override
    public void onSucced(String json) {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(Consts.RESULT);

        ObservableList<ProductTransaction> products = FXCollections.observableArrayList();
        System.out.println(json);

        boolean changeView = path.equals(Consts.MAT_TRANSACTIONS_CHANGE);

        for( int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonO =  jsonArray.getJSONObject(i);
            String nameProd = jsonO.getString(Consts.MATERIALS);
            int quantity = jsonO.getInt(Consts.QUANTITY);

            String transDateS = String.valueOf(jsonO.getInt(Consts.TRANSACTION_DATE));
            String transDateProp = transDateS.substring(6) + "-" + transDateS.substring(4,6) + "-" + transDateS.substring(0, 4);
            String destiny = jsonO.getString(Consts.DESTINY);
            String dueDateS = String.valueOf(jsonO.getInt(Consts.DUE_DATE));
            System.out.println(dueDateS);
            String dueDateProp = dueDateS.substring(6) + "-" + dueDateS.substring(4,6) + "-" + dueDateS.substring(0, 4);


            String transType = jsonO.getString(Consts.TRANSACTION_TYPE);
            String userName = jsonO.getString(Consts.USER);
            double price = jsonO.getDouble(Consts.PRICE);

            if (changeView) {
                String newName = jsonO.getString(Consts.MATERIALSnew);
                int newQuantity = jsonO.getInt(Consts.QUANTITYnew);
                String newTransDateS = String.valueOf( jsonO.getInt(Consts.TRANS_DATEnew) );
                String newDueDateS = String.valueOf(jsonO.getInt(Consts.DUE_DATEnew));
                String newDueDateProp = newDueDateS.substring(6) + "-" + newDueDateS.substring(4,6) + "-" + newDueDateS.substring(0, 4);
                String newTransDateProp = newTransDateS.substring(6) + "-" + newTransDateS.substring(4,6) + "-" + newTransDateS.substring(0, 4);
                String newUserName = jsonO.getString(Consts.USERnew);
                double newPrice = jsonO.getDouble(Consts.PRICEnew);

                //nuevo
                products.add(new ProductTransaction(newName, newUserName, newQuantity, destiny, newTransDateProp, newDueDateProp,
                        transType.toUpperCase(), newPrice, Consts.GREEN_CHANGE));
                //viejo
                products.add(new ProductTransaction(nameProd, userName, quantity, destiny, transDateProp, dueDateProp,
                        transType.toUpperCase(), price, Consts.RED_CHANGE));
            } else {
                products.add(new ProductTransaction(nameProd, userName, quantity, destiny, transDateProp, dueDateProp,
                        transType.toUpperCase(), price, Consts.WHITE_CHANGE));
            }
        }

        runLater(() -> {

            TableColumn<ProductTransaction, String> nameColumn = new TableColumn<>("Insumo");
            nameColumn.setMinWidth(300);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<ProductTransaction, Integer> qColumn = new TableColumn<>("Cantidad");
            qColumn.setMinWidth(100);
            qColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            TableColumn<ProductTransaction, String> destinyColumn = new TableColumn<>("Destino");
            destinyColumn.setMinWidth(100);
            destinyColumn.setCellValueFactory(new PropertyValueFactory<>("destiny"));

            TableColumn<ProductTransaction, String> transDateColumn = new TableColumn<>("Fecha de transacción");
            transDateColumn.setMinWidth(100);
            transDateColumn.setCellValueFactory(new PropertyValueFactory<>("transDate"));

            TableColumn<ProductTransaction, String> transTypeColumn = new TableColumn<>("Tipo de transacción");
            transTypeColumn.setMinWidth(100);
            transTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transType"));

            TableColumn<ProductTransaction, String> dueDateColumn = new TableColumn<>("Fecha de vencimiento");
            dueDateColumn.setMinWidth(100);
            dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

            TableColumn<ProductTransaction, Double> priceColumn = new TableColumn<>("Precio");
            priceColumn.setMinWidth(100);
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<ProductTransaction, String> userColumn = new TableColumn<>("Usuario");
            userColumn.setMinWidth(100);
            userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

            TableView<ProductTransaction> table = new TableView<>();
            VBox.setVgrow(table, Priority.ALWAYS);
            table.setItems(products);
            table.getColumns().addAll(nameColumn, destinyColumn, userColumn, transTypeColumn, transDateColumn, dueDateColumn, qColumn, priceColumn);

            //saco sort sino se desordenan las filas
            if(changeView) {
                priceColumn.setSortable(false); dueDateColumn.setSortable(false); nameColumn.setSortable(false);
                destinyColumn.setSortable(false); userColumn.setSortable(false); transTypeColumn.setSortable(false);
                transDateColumn.setSortable(false);qColumn.setSortable(false);
            }


            table.setRowFactory(new Callback<TableView<ProductTransaction>, TableRow<ProductTransaction>>() {
                @Override
                public TableRow<ProductTransaction> call(TableView<ProductTransaction> tableView) {
                    final TableRow<ProductTransaction> row = new TableRow<ProductTransaction>() {
                        @Override
                        protected void updateItem(ProductTransaction row, boolean empty) {
                            super.updateItem(row, empty);

                            if (!empty) {
                                if (row.getChange() == Consts.WHITE_CHANGE) {
                                    setStyle("-fx-control-inner-background: antiquewhite  ;\n" +
                                            "  -fx-accent: derive(-fx-control-inner-background, -40%);\n" +
                                            "  -fx-cell-hover-color: derive(-fx-control-inner-background, -20%);");
                                } else if (row.getChange() == Consts.GREEN_CHANGE) {
                                    setStyle("-fx-control-inner-background: forestgreen ;\n" +
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

            //si ya tiene una table borra la anterior y pone una nueva.
            if (searchV.getChildren().size() != 5) {
                searchV.getChildren().remove(5);
            }

            searchV.getChildren().add(table);

            loadingBox.close();
        });
    }

    @Override
    public void onSucced() {}

    @Override
    public void onFail(int statusCode) {
        runLater(() -> {
            loadingBox.close();
            AlertBox.display("ERROR", "No se puedo realizar la operación.");
        });
    }
}
