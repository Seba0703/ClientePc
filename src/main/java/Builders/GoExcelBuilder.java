package Builders;

import Common.AlertBox;
import Common.Consts;
import OS_Command.WindowsCommand;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public abstract class GoExcelBuilder implements Builder{

    private String headerExcel;
    Pane panel;
    String titleFile;

    public GoExcelBuilder(String headerExcel, String titleFile, Pane panel) {
        this.headerExcel = headerExcel;
        this.panel = panel;
        this.titleFile = titleFile;
    }

    @Override
    public void build() {
        Button goExcel = new Button("Exportar a Excel");
        goExcel.setOnAction(e -> {

            String username = System.getProperty("user.name");
            String path = Consts.ROOT_PATH + username + Consts.NEXT_ROOT_PATH + titleFile + Consts.INSUMOS_STOCK;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
                out.write(headerExcel.getBytes());

                fillExcel(out);

                out.close();
                WindowsCommand.goExcel(path);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                AlertBox.display("ERROR", "No se pudo realizar la operación.");
            } catch (IOException e1) {
                AlertBox.display("ERROR", "No se pudo realizar la operación.");
            }
        });

        HBox hBttnBox = new HBox();
        hBttnBox.setSpacing(10);
        hBttnBox.getChildren().add(goExcel);
        hBttnBox.setAlignment(Pos.CENTER);

        this.panel.getChildren().addAll(hBttnBox);
    }

    public void addCell(StringBuilder row, String value){
        row.append(value);
        row.append(";");
    }

    public void endRow(StringBuilder row) {
        row.append("\n");
    }

    public abstract void fillExcel( FileOutputStream out) throws IOException;

    @Override
    public void bindSuggestions(List<String> suggestions) {

    }
}
