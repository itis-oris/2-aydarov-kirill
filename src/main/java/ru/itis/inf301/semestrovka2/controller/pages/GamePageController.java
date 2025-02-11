package ru.itis.inf301.semestrovka2.controller.pages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import ru.itis.inf301.semestrovka2.client.Client;
import ru.itis.inf301.semestrovka2.client.ClientService;
import ru.itis.inf301.semestrovka2.controller.util.FXMLLoaderUtil;
import ru.itis.inf301.semestrovka2.model.Board;


@Getter
@Setter
public class GamePageController implements RootPane {
    private ClientService clientService;
    private Client client;

    @FXML
    public Pane rootPane;

    @FXML
    public Text textStep;

    @FXML
    public GridPane gridPane;

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
        this.client = clientService.getClient();
        startGame();
    }

    public void renderBoard() {
        Platform.runLater(() -> {
            Board board = client.getBoard();
            boolean reverse = client.getClient_index() == 1;
            gridPane.getChildren().clear();
            textStep.setText(board.getStep() == client.getClient_index() ? "Ваш ход!" : "Ход соперника!");
            for (int row = 0; row < 17; row++) {
                for (int col = 0; col < 17; col++) {
                    Rectangle rect;
                    Circle circle = null;
                    if (row % 2 == 1 && col % 2 == 1) {
                        rect = new Rectangle(10, 10, Color.valueOf("#FAE7B5"));
                    } else if (row % 2 == 1) {
                        int finalRow = reverse ? 7 - row / 2 : row / 2;
                        ;
                        int finalCol = col / 2;
                        if (board.getHorizontal()[finalRow][finalCol] == 1) {
                            rect = new Rectangle(60, 10, Color.valueOf("#79553D"));
                        } else {
                            rect = new Rectangle(60, 10, Color.valueOf("#FAE7B5"));
                        }
                        rect.setOnMouseClicked(event -> {
                            putHorizontalWall(finalRow, finalCol);
                        });
                    } else if (col % 2 == 1) {
                        int finalRow = reverse ? 8 - row / 2 : row / 2;
                        int finalCol = col / 2;
                        if (board.getVertical()[finalRow][finalCol] == 1) {
                            rect = new Rectangle(10, 60, Color.valueOf("#79553D"));
                        } else {
                            rect = new Rectangle(10, 60, Color.valueOf("#FAE7B5"));
                        }
                        rect.setOnMouseClicked(event -> {
                            putVerticalWall(finalRow, finalCol);
                        });
                    } else {
                        int finalRow = reverse ? 8 - row / 2 : row / 2;
                        int finalCol = col / 2;
                        rect = new Rectangle(60, 60, Color.valueOf("#FFCA86"));

                        Color userColor = Color.valueOf("#007FFF");
                        Color opponentColor = Color.valueOf("#C41E3A");

                        if (board.getUser0_x() == finalRow && board.getUser0_y() == finalCol) {
                            circle = new Circle(30, 30, 25, reverse ? opponentColor : userColor);
                        } else if (board.getUser1_x() == finalRow && board.getUser1_y() == finalCol) {
                            circle = new Circle(30, 30, 25, reverse ? userColor : opponentColor);
                        }

                        rect.setOnMouseClicked(event -> {
                            move(finalRow, finalCol);
                        });
                    }
                    gridPane.add(rect, col, row);
                    if (circle != null) {
                        gridPane.add(circle, col, row);
                        GridPane.setHalignment(circle, HPos.CENTER);
                        GridPane.setValignment(circle, VPos.CENTER);
                    }
                }
            }
        });
    }


    public void move(int finalRow, int finalCol) {

        client.move(finalRow, finalCol);
    }

    public void putVerticalWall(int finalRow, int finalCol) {

        client.putVerticalWall(finalRow, finalCol);
    }

    public void putHorizontalWall(int finalRow, int finalCol) {

        client.putHorizontalWall(finalRow, finalCol);
    }

    public void startGame() {
        new Thread(() -> {
            boolean flag = true;
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                int result = client.checkResult();
                if (result == -1) renderBoard();
                else {
                    flag = false;
                    redirectToWinPage(result == 1);
                }
            }

        }).start();
    }

    public void redirectToWinPage(boolean result) {
        Platform.runLater(() -> {
            rootPane.getChildren().clear();
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/win.fxml", rootPane, result);
        });
    }


    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }
}


