package com.audioorder;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.enums.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ButtonType;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.scene.control.PasswordField;

public class MainApp extends Application {
    private Stage primaryStage;
    private Connection connection;
    private BorderPane mainLayout;
    private VBox menuBox;
    private MFXTableView<Map<String, Object>> tableView;
    private Label tableTitle;
    private String currentTable = null;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showDbConnectWindow();
    }

    private void showDbConnectWindow() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(32));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Підключення до бази даних");
        title.setFont(Font.font("System", FontWeight.BOLD, 22));

        MFXTextField hostField = new MFXTextField();
        hostField.setPromptText("Сервер (наприклад, localhost:1433)");
        hostField.setText("localhost:1433");
        MFXTextField dbField = new MFXTextField();
        dbField.setPromptText("Назва бази даних");
        MFXTextField userField = new MFXTextField();
        userField.setPromptText("Користувач");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Пароль");

        MFXButton connectBtn = new MFXButton("Підключитись");
        connectBtn.setOnAction(e -> {
            String url = "jdbc:sqlserver://" + hostField.getText().trim() + ";databaseName=" + dbField.getText().trim();
            String user = userField.getText().trim();
            String pass = passField.getText();
            try {
                connection = DriverManager.getConnection(url, user, pass);
                showMainWindow();
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR, "Не вдалося підключитись до БД: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(title, hostField, dbField, userField, passField, connectBtn);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Підключення до БД");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainWindow() {
        mainLayout = new BorderPane();
        menuBox = new VBox(8);
        menuBox.setPadding(new Insets(16));
        menuBox.setStyle("-fx-background-color: #f5f5f5;");
        menuBox.setPrefWidth(220);

        Label menuTitle = new Label("Таблиці БД");
        menuTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        menuBox.getChildren().add(menuTitle);

        List<String> tables = getAllTableNames();
        for (String table : tables) {
            MFXButton btn = new MFXButton(table.replace('_', ' '));
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> showTable(table));
            menuBox.getChildren().add(btn);
        }

        mainLayout.setLeft(menuBox);

        VBox centerBox = new VBox(8);
        centerBox.setPadding(new Insets(16));
        tableTitle = new Label("Оберіть таблицю для перегляду");
        tableTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        centerBox.getChildren().add(tableTitle);
        mainLayout.setCenter(centerBox);

        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setTitle("Система замовлень аудіотехніки");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<String> getAllTableNames() {
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                tables.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    private void showTable(String tableName) {
        currentTable = tableName;
        VBox centerBox = new VBox(8);
        centerBox.setPadding(new Insets(16));
        tableTitle = new Label("Таблиця: " + tableName.replace('_', ' '));
        tableTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        HBox topBar = new HBox(8);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().add(tableTitle);
        MFXButton structureBtn = new MFXButton("Структура");
        structureBtn.setOnAction(e -> showTableStructure(tableName));
        topBar.getChildren().add(structureBtn);
        centerBox.getChildren().add(topBar);

        // Таблиця для перегляду даних
        tableView = new MFXTableView<>();
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "]")) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            tableView.getTableColumns().clear();
            for (int i = 1; i <= colCount; i++) {
                String colName = meta.getColumnName(i);
                MFXTableColumn<Map<String, Object>> col = new MFXTableColumn<>(colName);
                col.setRowCellFactory(column -> new MFXTableRowCell<>(cellData -> {
                    Object value = cellData == null ? "" : cellData.get(colName);
                    return value == null ? "" : value.toString();
                }));
                tableView.getTableColumns().add(col);
            }
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                data.add(row);
            }
            tableView.setItems(data);
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, "Помилка при завантаженні таблиці: " + e.getMessage());
            alert.showAndWait();
        }
        centerBox.getChildren().add(tableView);

        // CRUD-кнопки
        HBox crudBar = new HBox(8);
        crudBar.setAlignment(Pos.CENTER_LEFT);
        MFXButton addBtn = new MFXButton("Додати");
        MFXButton editBtn = new MFXButton("Редагувати");
        MFXButton delBtn = new MFXButton("Видалити");
        MFXButton refreshBtn = new MFXButton("Оновити");
        crudBar.getChildren().addAll(addBtn, editBtn, delBtn, refreshBtn);
        centerBox.getChildren().add(crudBar);

        addBtn.setOnAction(e -> showAddEditDialog(tableName, null));
        editBtn.setOnAction(e -> {
            Map<String, Object> selected = tableView.getSelectionModel().getSelectedValue();
            if (selected != null) showAddEditDialog(tableName, selected);
        });
        delBtn.setOnAction(e -> {
            Map<String, Object> selected = tableView.getSelectionModel().getSelectedValue();
            if (selected != null) deleteRow(tableName, selected);
        });
        refreshBtn.setOnAction(e -> showTable(tableName));

        mainLayout.setCenter(centerBox);
    }

    private void showTableStructure(String tableName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Структура таблиці: " + tableName.replace('_', ' '));
        VBox content = new VBox(8);
        content.setPadding(new Insets(16));
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] WHERE 1=0")) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String colName = meta.getColumnName(i);
                String colType = meta.getColumnTypeName(i);
                Label l = new Label(colName + " — " + colType);
                content.getChildren().add(l);
            }
        } catch (SQLException e) {
            content.getChildren().add(new Label("Помилка: " + e.getMessage()));
        }
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showAddEditDialog(String tableName, Map<String, Object> rowData) {
        Dialog<Map<String, Object>> dialog = new Dialog<>();
        dialog.setTitle(rowData == null ? "Додати запис" : "Редагувати запис");
        VBox content = new VBox(8);
        content.setPadding(new Insets(16));
        List<TextField> fields = new ArrayList<>();
        List<String> colNames = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM [" + tableName + "] WHERE 1=0")) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String colName = meta.getColumnName(i);
                colNames.add(colName);
                TextField tf = new TextField();
                tf.setPromptText(colName);
                if (rowData != null && rowData.get(colName) != null) tf.setText(rowData.get(colName).toString());
                fields.add(tf);
                content.getChildren().add(new HBox(8, new Label(colName + ":"), tf));
            }
        } catch (SQLException e) {
            content.getChildren().add(new Label("Помилка: " + e.getMessage()));
        }
        dialog.getDialogPane().setContent(content);
        ButtonType okType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> {
            if (bt != null && bt.equals(okType)) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < colNames.size(); i++) {
                    map.put(colNames.get(i), fields.get(i).getText());
                }
                return map;
            }
            return null;
        });
        Optional<Map<String, Object>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            if (rowData == null) insertRow(tableName, data);
            else updateRow(tableName, rowData, data);
            showTable(tableName);
        });
    }

    private void insertRow(String tableName, Map<String, Object> data) {
        StringBuilder sql = new StringBuilder("INSERT INTO [" + tableName + "] (");
        StringBuilder vals = new StringBuilder();
        List<String> cols = new ArrayList<>(data.keySet());
        for (int i = 0; i < cols.size(); i++) {
            sql.append("[").append(cols.get(i)).append("]");
            vals.append("?");
            if (i < cols.size() - 1) {
                sql.append(", ");
                vals.append(", ");
            }
        }
        sql.append(") VALUES (").append(vals).append(")");
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < cols.size(); i++) {
                ps.setString(i + 1, data.get(cols.get(i)) == null ? null : data.get(cols.get(i)).toString());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, "Помилка при додаванні: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateRow(String tableName, Map<String, Object> oldData, Map<String, Object> newData) {
        StringBuilder sql = new StringBuilder("UPDATE [" + tableName + "] SET ");
        List<String> cols = new ArrayList<>(newData.keySet());
        for (int i = 0; i < cols.size(); i++) {
            sql.append("[").append(cols.get(i)).append("] = ?");
            if (i < cols.size() - 1) sql.append(", ");
        }
        sql.append(" WHERE ");
        for (int i = 0; i < cols.size(); i++) {
            sql.append("[").append(cols.get(i)).append("] = ?");
            if (i < cols.size() - 1) sql.append(" AND ");
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int idx = 1;
            for (String col : cols) ps.setString(idx++, newData.get(col) == null ? null : newData.get(col).toString());
            for (String col : cols) ps.setString(idx++, oldData.get(col) == null ? null : oldData.get(col).toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, "Помилка при оновленні: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void deleteRow(String tableName, Map<String, Object> data) {
        StringBuilder sql = new StringBuilder("DELETE FROM [" + tableName + "] WHERE ");
        List<String> cols = new ArrayList<>(data.keySet());
        for (int i = 0; i < cols.size(); i++) {
            sql.append("[").append(cols.get(i)).append("] = ?");
            if (i < cols.size() - 1) sql.append(" AND ");
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < cols.size(); i++) {
                ps.setString(i + 1, data.get(cols.get(i)) == null ? null : data.get(cols.get(i)).toString());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR, "Помилка при видаленні: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 