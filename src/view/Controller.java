package view;

import impl.Logic;
import impl.Node;
import impl.Relation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller {

    int x = 50;
    int y = 300;
    int deltaX = 150;
    int radius = 8;
    public static int numbOfNodes;
    ArrayList<Double> positionsX;
    ArrayList<Double> positionsY;
    ArrayList<Node> nodes;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button constructButton;

    @FXML
    private TextField fromField;

    @FXML
    private Button plotButton;

    @FXML
    public ScrollPane pane;

    @FXML
    public Pane paneInPane;

    @FXML
    private TextField textPane;

    @FXML
    private TextField toField;
    @FXML
    private TextField weightField;
    @FXML
    private TextArea weightTextBox;

    int globalStartX;
    int globalStartY;
    int globalEndX;
    int globalEndY;
    int offset = 30;

    @FXML
    void initiallize() {
        paneInPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("55");
            }
        });
    }
    CubicCurve c;
    Line l;

    @FXML
    void globals(MouseEvent event) {
        globalStartX =(int) event.getX();
        globalStartY =(int) event.getY();
    }

    @FXML
    void drawLineByDrag(MouseEvent event) {
        globalStartX = (int)event.getX() - 10;
        globalStartY = (int)event.getY();
        for(int i = 0; i < positionsX.size(); i++) {
            double diffX = positionsX.get(i) - globalStartX;
            double diffY = positionsY.get(i) - globalStartY;
            if(diffX > -offset && diffX < offset && diffY > -offset && diffY < offset)  {
                fromField.setText((char)(97+i) + "");
            }
        }
        paneInPane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                Arc k = new Arc();
                paneInPane.getChildren().remove(c);
                paneInPane.getChildren().remove(l);
                if(mouseEvent.getX() - globalStartX < 170 && mouseEvent.getX() - globalStartX > 0) {
                    l = new Line();
                    l.setStroke(Color.BLACK);
                    l.setStrokeWidth(2);
                    l.setStartX(globalStartX);
                    l.setStartY(globalStartY);
                    l.setEndX(mouseEvent.getX());
                    l.setEndY(mouseEvent.getY());
                    paneInPane.getChildren().add(l);
                } else {
                    c = new CubicCurve();
                    c.setFill(Color.TRANSPARENT);
                    c.setStroke(Color.BLACK);
                    c.setStrokeWidth(2);
                    c.setStartX(globalStartX);
                    c.setStartY(globalStartY);

                    c.setEndX(mouseEvent.getX());
                    c.setEndY(mouseEvent.getY());

                    c.setControlX1(globalStartX + (mouseEvent.getX() - globalStartX) / 3);
                    c.setControlX2(globalStartX + 2 * (mouseEvent.getX() - globalStartX) / 3);
                    if (mouseEvent.getX() > globalStartX) {
                        c.setControlY2(globalStartY - 80);
                        c.setControlY1(globalStartY - 80);
                    } else {
                        c.setStartX(globalStartX + 20);
                        c.setControlY2(globalStartY + 80);
                        c.setControlY1(globalStartY + 80);
                        c.setStroke(Color.RED);
                    }

                    paneInPane.getChildren().add(c);
                }
            }
        });

        paneInPane.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                paneInPane.getChildren().remove(c);
                paneInPane.getChildren().remove(l);

                globalEndX = (int)mouseEvent.getX();
                globalEndY = (int)mouseEvent.getY();
                for(int i = 0; i < positionsX.size(); i++) {
                    double diffX = positionsX.get(i) - globalEndX;
                    double diffY = positionsY.get(i) - globalEndY;
                    if(diffX > - offset && diffX < offset && diffY > -offset && diffY < offset)  {
                        toField.setText((char)(97+i) + "");
                    }
                }
                if(weightField.getText() == "") {
                    weightField.setText("1");
                }
                if(fromField.getText().equals("") || toField.getText().equals("")) {
                    return;
                }
                else if((fromField.getText().charAt(0) < 'a' && fromField.getText().charAt(0) > 'z') || (toField.getText().charAt(0) < 'a' && toField.getText().charAt(0) > 'z')) {}
                else {
                    plotButton.fire();
                }
            }
        });

    }

    @FXML
    void drawFinalAfterDrag(MouseEvent event) {
        paneInPane.getChildren().remove(c);
        globalEndX = (int)event.getX();
        globalEndY = (int)event.getY();
        for(int i = 0; i < positionsX.size(); i++) {
            double diffX = positionsX.get(i) - globalEndX;
            double diffY = positionsY.get(i) - globalEndY;
            if(diffX > - offset && diffX < offset && diffY > -offset && diffY < offset)  {
                toField.setText((char)(97+i) + "");
            }
        }
        if(weightField.getText() == "") {
            weightField.setText("1");
        }
        if(fromField.getText().equals("") || toField.getText().equals("")) {
            return;
        }
        else if((fromField.getText().charAt(0) < 'a' && fromField.getText().charAt(0) > 'z') || (toField.getText().charAt(0) < 'a' && toField.getText().charAt(0) > 'z')) {}
        else {
            plotButton.fire();
        }
    }
    @FXML
    void drawNodes(MouseEvent event) {
        nodes = new ArrayList<Node>();
        weightTextBox.clear();
        String numberOfNodes = textPane.getText();
        int numOfNodes = Integer.parseInt(numberOfNodes);
        numbOfNodes = numOfNodes;
        for (int i = 0; i < numOfNodes; i++) {
            Node node = new Node((char) (i + 97) + "");
            nodes.add(node);
        }
        paneInPane.getChildren().clear();
        x = 50;
        positionsX = new ArrayList();
        positionsY = new ArrayList();
        for (int i = 0; i < numOfNodes; i++) {
            Circle circle = new Circle();
            Circle circle2 = new Circle();
            circle2.setFill(Color.WHITE);
            circle.setFill(Color.BLACK);
            circle2.setLayoutX(x + deltaX);
            circle.setLayoutX(x + deltaX);
            positionsX.add(1.0 * x + deltaX);
            positionsY.add(1.0 * y);

            Label numLabel = new Label();
            numLabel.setText((char) (96 + i + 1) + "");
            numLabel.setLayoutX(x + deltaX - 5);
            numLabel.setLayoutY(y + 10);

            x += deltaX;
            circle.setLayoutY(y);
            circle.setRadius(radius);
            circle2.setLayoutY(y);
            circle2.setRadius(radius - 2);
            paneInPane.getChildren().add(circle);
            paneInPane.getChildren().add(circle2);
            paneInPane.getChildren().add(numLabel);

        }

    }
    @FXML
    void drawLines(ActionEvent event) {
        fromField.setText(String.valueOf((Integer.valueOf(fromField.getText().charAt(0)) - 96)));
        toField.setText(String.valueOf((Integer.valueOf(toField.getText().charAt(0)) - 96)));
        if (weightField.getText().equals("")) {
            weightField.setText("1");
        }

        int numOfInitNode = Integer.valueOf(fromField.getText());
        int numOfFinalNode = Integer.valueOf(toField.getText());
        int differenceBetweenNodes = numOfFinalNode - numOfInitNode;
        if (differenceBetweenNodes == 0) {
            Circle c = new Circle();
            c.setLayoutX(positionsX.get(numOfInitNode - 1));
            c.setLayoutY(positionsY.get(numOfInitNode - 1) - 30);
            c.setRadius(30);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.gray(0.1));
            c.setStrokeWidth(2);
            paneInPane.getChildren().add(c);

            String weight = weightField.getText();
            Label weightLabel = new Label();
            Font f;
            f = new Font(20);
            weightLabel.setLayoutY(positionsY.get(numOfInitNode - 1) - 90);
            weightLabel.setFont(f);
            weightLabel.setText(weight);
            weightLabel.setTextFill(Color.GREEN);
            weightLabel.setLayoutX(positionsX.get(numOfInitNode - 1));

            Line l1 = new Line();
            l1.setStartX(positionsX.get(numOfInitNode - 1));
            l1.setStartY(positionsY.get(numOfInitNode - 1) - 60);
            l1.setEndX(positionsX.get(numOfInitNode - 1) - 10);
            l1.setEndY(positionsY.get(numOfInitNode - 1) - 65);
            l1.setStrokeWidth(2);
            l1.setStroke(Color.gray(0.1, 0.5));

            Line l2 = new Line();
            l2.setStartX(positionsX.get(numOfInitNode - 1));
            l2.setStartY(positionsY.get(numOfInitNode - 1) - 60);
            l2.setEndX(positionsX.get(numOfInitNode - 1) - 9);
            l2.setEndY(positionsY.get(numOfInitNode - 1) - 53);
            l2.setStrokeWidth(2);
            l2.setStroke(Color.gray(0.1, 0.5));
            paneInPane.getChildren().add(l1);
            paneInPane.getChildren().add(l2);
            paneInPane.getChildren().add(weightLabel);

            Relation relation = new Relation(Float.valueOf(weight), nodes.get(numOfFinalNode - 1));
            nodes.get(numOfInitNode - 1).relations.add(relation);
        } else {
            CubicCurve arc = new CubicCurve();
            //arc.setFill(Color.gray(0.96));
            arc.setFill(Color.TRANSPARENT);

            arc.setStrokeWidth(2);

            double startX = positionsX.get(numOfInitNode - 1);
            double endX = positionsX.get(numOfFinalNode - 1);

            double startY = positionsY.get(numOfInitNode - 1);
            double endY = positionsY.get(numOfFinalNode - 1);


            double Height = (endX - startX) / 4;
            if (differenceBetweenNodes == 1) {
                Line arcc = new Line();
                arcc.setFill(Color.TRANSPARENT);
                arcc.setStrokeWidth(2);
                arcc.setStartX(startX);
                arcc.setEndX(endX);
                arcc.setStartY(startY);
                arcc.setEndY(endY);
                paneInPane.getChildren().add(arcc);
                drawArrow((startX + endX) / 2 + 5, (startY + endY) / 2);

                Circle c = new Circle();
                c.setFill(Color.BLACK);
                c.setStroke(Color.BLACK);
                c.setRadius(3);
                c.setLayoutX(startX);
                c.setLayoutY(startY);
                paneInPane.getChildren().add(c);

            } else {
                if (differenceBetweenNodes < 0) {
                    endX += 7;
                    endY += 6;
                    arc.setStroke(Color.RED);
                    Circle c = new Circle();
                    c.setFill(Color.RED);
                    c.setStroke(Color.RED);
                    c.setRadius(3);
                    c.setLayoutX(startX);
                    c.setLayoutY(startY);
                    paneInPane.getChildren().add(c);
                } else if (differenceBetweenNodes > 0) {
                    endX -= 7;
                    endY -= 6;
                    arc.setStroke(Color.gray(0.1));
                    Circle c = new Circle();
                    c.setFill(Color.BLACK);
                    c.setStroke(Color.BLACK);
                    c.setRadius(3);
                    c.setLayoutX(startX);
                    c.setLayoutY(startY);
                    paneInPane.getChildren().add(c);
                }

                arc.setStartX(startX);
                arc.setEndX(endX);
                arc.setStartY(startY);
                arc.setEndY(endY);
                arc.setControlX1(startX + (endX - startX) / 3);
                arc.setControlY1(startY - Height);
                arc.setControlX2(endX - (endX - startX) / 3);
                arc.setControlY2(startY - Height);
                paneInPane.getChildren().add(arc);
                if (differenceBetweenNodes > 0) {
                    drawArrowRight(endX, endY);
                } else if (differenceBetweenNodes < 0) {
                    drawArrowLeft(endX, endY);
                }
            }


            String weight = weightField.getText();
            Relation relation = new Relation(Float.valueOf(weight), nodes.get(numOfFinalNode - 1));
            nodes.get(numOfInitNode - 1).relations.add(relation);
            Label weightLabel = new Label();
            Font f;
            if (differenceBetweenNodes == 1) {
                f = new Font(18);
                weightLabel.setLayoutY(startY - Height + 5);
            } else if (differenceBetweenNodes == 2) {
                f = new Font(20);
                weightLabel.setLayoutY(startY - Height - 11);
            } else {
                f = new Font(25);
                weightLabel.setLayoutY(startY - Height - 8);
            }
            weightLabel.setFont(f);
            weightLabel.setText(weight);
            weightLabel.setTextFill(Color.GREEN);
            weightLabel.setLayoutX(startX + (endX - startX) / 2);
            paneInPane.getChildren().add(weightLabel);

        }
        weightTextBox.setFont(new Font(18));
        weightTextBox.appendText((char) (numOfInitNode + 96) + "      ⟿");
        weightTextBox.setStyle("-fx-text-fill: blue;");
        weightTextBox.appendText(weightField.getText());
        weightTextBox.appendText("⟿      " + (char) (numOfFinalNode + 96) + "\n");
        fromField.clear();
        toField.clear();
        weightField.clear();
    }
    @FXML
    void calculateTransferFunction(MouseEvent event) {
        Logic s = new Logic(nodes);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Overall function : " + String.valueOf(Logic.transferFunction), ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            //do stuff
        }
    }
    @FXML
    void doSteps(ActionEvent event) {

        AnchorPane secondaryLayout = new AnchorPane();

        Scene secondScene = new Scene(secondaryLayout, 485, 470);

        Stage newWindow = new Stage();
        newWindow.setTitle("Steps");
        newWindow.setScene(secondScene);
        newWindow.setX(300);
        newWindow.setY(100);

        Label loopsLabel = new Label("All Loops");
        loopsLabel.setStyle("-fx-font-size: 1.8em;");
        loopsLabel.setTextFill(Color.BLUE);
        loopsLabel.setLayoutX(65);
        loopsLabel.setLayoutY(10);

        TextArea loopsArea = new TextArea();
        loopsArea.setPrefHeight(400);  //sets height of the TextArea to 400 pixels
        loopsArea.setPrefWidth(200);
        loopsArea.setLayoutX(30);
        loopsArea.setLayoutY(50);
        loopsArea.setEditable(false);
        loopsArea.setStyle("-fx-font-size: 1.5em;");
        secondaryLayout.getChildren().add(loopsArea);
        secondaryLayout.getChildren().add(loopsLabel);

        Label ForwardPathsLabel = new Label("Forward Paths");
        ForwardPathsLabel.setTextFill(Color.BLUE);
        ForwardPathsLabel.setStyle("-fx-font-size: 1.8em;");
        ForwardPathsLabel.setLayoutX(275);
        ForwardPathsLabel.setLayoutY(10);

        TextArea FPArea = new TextArea();
        FPArea.setPrefHeight(400);  //sets height of the TextArea to 400 pixels
        FPArea.setPrefWidth(200);
        FPArea.setLayoutX(260);
        FPArea.setLayoutY(50);
        FPArea.setEditable(false);
        FPArea.setStyle("-fx-font-size: 1.5em;");
        secondaryLayout.getChildren().add(FPArea);
        secondaryLayout.getChildren().add(ForwardPathsLabel);


        if(Logic.loops != null) {
            for (int i = 0; i < Logic.loops.size(); i++) {
                loopsArea.appendText("L" + String.valueOf(i + 1) + ": " + Logic.loops.get(i).getName() + "\n");
            }
        }
        if(Logic.forwardPaths != null) {
            for (int i = 0; i < Logic.forwardPaths.size(); i++) {
                FPArea.appendText("F" + String.valueOf(i + 1) + ": " + Logic.forwardPaths.get(i).getName() + "\n");
            }
        }
        newWindow.show();
    }
    private void drawArrow(double v, double v1) {
        Line l1 = new Line();
        l1.setStartX(v);
        l1.setStartY(v1);
        l1.setEndX(v - 10);
        l1.setEndY(v1 - 5);
        paneInPane.getChildren().add(l1);

        Line l2 = new Line();
        l2.setStartX(v);
        l2.setStartY(v1);
        l2.setEndX(v - 10);
        l2.setEndY(v1 + 5);
        paneInPane.getChildren().add(l2);
    }
    private void drawArrowLeft(double endX, double endY) {
        Line l1 = new Line();
        l1.setStartX(endX);
        l1.setStartY(endY);
        l1.setEndX(endX + 10);
        l1.setEndY(endY - 1);
        l1.setStrokeWidth(2);
        l1.setStroke(Color.RED);

        Line l2 = new Line();
        l2.setStartX(endX);
        l2.setStartY(endY);
        l2.setEndX(endX + 8);
        l2.setEndY(endY + 13);
        l2.setStrokeWidth(2);
        l2.setStroke(Color.RED);

        paneInPane.getChildren().add(l1);
        paneInPane.getChildren().add(l2);
    }
    private void drawArrowRight(double endX, double endY) {
        Line l1 = new Line();
        l1.setStartX(endX);
        l1.setStartY(endY);
        l1.setEndX(endX - 10);
        l1.setEndY(endY - 15);
        l1.setStrokeWidth(2);
        l1.setStroke(Color.gray(0.1, 0.5));

        Line l2 = new Line();
        l2.setStartX(endX);
        l2.setStartY(endY);
        l2.setEndX(endX - 15);
        l2.setEndY(endY);
        l2.setStrokeWidth(2);
        l2.setStroke(Color.gray(0.1, 0.5));

        paneInPane.getChildren().add(l1);
        paneInPane.getChildren().add(l2);

    }
}