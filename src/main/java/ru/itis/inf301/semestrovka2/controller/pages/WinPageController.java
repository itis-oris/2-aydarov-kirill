package ru.itis.inf301.semestrovka2.controller.pages;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.itis.inf301.semestrovka2.controller.util.FXMLLoaderUtil;

import java.util.Objects;


public class WinPageController implements RootPane {
    private Pane rootPane;

    @FXML
    Text resultText;

    @FXML
    ImageView imageView;

    @FXML
    public void back() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-menu.fxml", rootPane);
    }

    public void setWinText(boolean result) {
        resultText.setText(result ? "Победа!" : "Поражение :(");

        Image image = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(result ? "/assets/images/emoji_win.png" : "/assets/images/emoji_lose.png")));
        imageView.setImage(image);
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.seconds(6));
        rotateTransition.setByAngle(360);  // Вращение на 360 градусов
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);  // Бесконечно повторяется
        rotateTransition.setAutoReverse(true);  // Вращение в обе стороны
        rotateTransition.setNode(imageView);  // Применяем анимацию к кнопке

        rotateTransition.play();
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

}
