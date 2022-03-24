/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class AddActivityCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField id;

    @FXML
    private TextField image_path;

    @FXML
    private TextField title;

    @FXML
    private TextField consumption;

    @FXML
    private TextField source;

    @Inject
    public AddActivityCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void back() {
        clearFields();
        mainCtrl.showAdmin();
    }

    public void cancel() {
        clearFields();
    }

    public void ok() {
        try {
            server.addActivity(getActivity());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showAdmin();
    }

    private Activity getActivity() {
        try {
            var idText = id.getText();
            var imagePathText = image_path.getText();
            var titleText = title.getText();
            if (titleText.length() > 140) return null;
            var consumptionInt = Long.parseLong(consumption.getText());
            if (consumptionInt == 0) return null;
            var sourceText = source.getText();
            return new Activity(idText, imagePathText, titleText, consumptionInt, sourceText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void clearFields() {
        id.clear();
        image_path.clear();
        title.clear();
        consumption.clear();
        source.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                back();
                break;
            default:
                break;
        }
    }
}
