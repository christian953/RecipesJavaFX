package com.example.recipesjavafx;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class StepCellFactory implements Callback<ListView<Step>, ListCell<Step>> {

    private final boolean editbale;

    public StepCellFactory(boolean editable) {
        super();
        this. editbale = editable;
    }

    @Override
    public ListCell<Step> call(ListView<Step> list) {
        return new ListCell<Step>() {

            @Override
            protected void updateItem(Step step, boolean empty) {
                super.updateItem(step, empty);
                if (step == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label stepNumberLabel = new Label("Schritt " + (step.stepNumber() + 1));
                    TextArea stepDescriptionTextArea = new TextArea(step.text());
                    Label timeLabel = new Label("Benötigte Zeit");
                    TextField timeTextField = new TextField(step.time());
                    VBox vbox = new VBox(stepNumberLabel, stepDescriptionTextArea, timeLabel, timeTextField);
                    setGraphic(vbox);
                    timeTextField.setEditable(editbale);
                    stepDescriptionTextArea.setEditable(editbale);

                    stepDescriptionTextArea.setOnKeyTyped(event -> {
                        step.setText(stepDescriptionTextArea.getText());
                    });

                    timeTextField.setOnKeyTyped(event -> {
                        step.setTime(timeTextField.getText());
                    });

                }

            }
        };
    }
}
