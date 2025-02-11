package ru.itis.inf301.semestrovka2.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.inf301.semestrovka2.client.ClientService;
import ru.itis.inf301.semestrovka2.controller.util.FXMLLoaderUtil;

import java.util.function.UnaryOperator;

public class ConnectToLobbyPageController implements RootPane {

    @FXML
    public Pane rootPane;

    @FXML
    public TextField textField;

    @FXML
    public HBox errorContainer;

    @FXML
    public void initialize() {
        setupNumericField();
    }

    @FXML
    public void back() {

        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-menu.fxml", rootPane);
    }

    @FXML
    public void connect() {
        String lobbyId = textField.getText();

        if (!lobbyId.isEmpty() && lobbyId.length() < 7) {
            ClientService clientService = new ClientService(lobbyId);
            rootPane.getChildren().clear();
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/lobby.fxml", rootPane, clientService);
        } else {
            Text error = new Text("Некорректный код лобби");
            error.getStyleClass().add("error");
            errorContainer.getChildren().add(error);
        }


    }


    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

    public void setupNumericField() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Разрешены только цифры
                return change;
            }
            return null; // Отменяет ввод
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
}
