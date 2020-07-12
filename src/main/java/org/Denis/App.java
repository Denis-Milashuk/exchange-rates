package org.Denis;

import hibernateClasses.HibernateHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Currency currentCurrency;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        boolean tryOneMoreTime = true;
        while (tryOneMoreTime) {
            try {
                UpdateController updateController = UpdateController.getInstance();
                updateController.updateAllData();
                tryOneMoreTime = false;
            } catch (IOException exception) {
                runErrorStage();
            }
        }
        Thread autoUpdateThread = new Thread(new UpdateController.AutoUpdater());
        autoUpdateThread.start();
        startMainStage(stage);
    }

    private void runErrorStage() {
        Stage updateErrorStage = new Stage();
        updateErrorStage.setTitle("Update Error");
        Label errorMessage = new Label("Не удалось обновить данные курсов");
        Button updateRepeat = new Button("Повторить попытку");
        updateRepeat.setOnAction(x -> {
            updateErrorStage.close();
        });
        Button exit = new Button("Выход");
        exit.setOnAction(x -> {
            System.exit(0);
        });
        HBox hBoxForButtons = new HBox(updateRepeat, exit);
        hBoxForButtons.setAlignment(Pos.CENTER);
        hBoxForButtons.setPadding(new Insets(10));
        hBoxForButtons.setSpacing(10);
        VBox vBox = new VBox(errorMessage, hBoxForButtons);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        Scene errorScene = new Scene(vBox, 400, 150);
        updateErrorStage.setScene(errorScene);
        updateErrorStage.showAndWait();
    }

    private void startMainStage(Stage mainStage) {
        mainStage.setTitle("Currency rates");
        Label label = new Label("Выберите валюту");
        ChoiceBox<Currency> currencyChoiceBox = new ChoiceBox<Currency>(Currency.getObservableList());
        Button showReportForTenDays = new Button("Отчет 10 дней");
        Button showReportForThirtyDays = new Button("Отчет за 30 дней");
        Button exit = new Button("Выход");
        HBox hBoxForButtons = new HBox(showReportForTenDays, showReportForThirtyDays);
        hBoxForButtons.setAlignment(Pos.CENTER);
        hBoxForButtons.setPadding(new Insets(10));
        hBoxForButtons.setSpacing(10);
        VBox vBox = new VBox(label, currencyChoiceBox, hBoxForButtons, exit);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        Scene mainScene = new Scene(vBox, 400, 150);
        currencyChoiceBox.setOnAction(x -> {
            currentCurrency = currencyChoiceBox.getValue();
        });
        currencyChoiceBox.setValue(Currency.USD);
        showReportForTenDays.setOnAction(x -> {
            mainStage.setScene(getSceneForTenDaysReport(mainStage, mainScene));
        });
        showReportForThirtyDays.setOnAction(x -> {
            mainStage.setScene(getSceneForThirtyDaysReport(mainStage, mainScene));
        });
        exit.setOnAction(x -> {
            System.exit(0);
        });
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    private Scene getSceneForTenDaysReport(Stage mainStage, Scene mainScene) {
        Label reportForTenDays = new Label();
        reportForTenDays.setText(HibernateHelper.getInstance().getReportForNDaysByCurrency(currentCurrency,10));
        Button back = new Button("Назад");
        VBox reportVBox = new VBox(reportForTenDays, back);
        reportVBox.setAlignment(Pos.CENTER);
        reportVBox.setPadding(new Insets(10));
        reportVBox.setSpacing(10);
        Scene reportScene = new Scene(reportVBox, 300, 250);
        back.setOnAction(x1 -> {
            mainStage.setScene(mainScene);
        });
        return reportScene;
    }

    private Scene getSceneForThirtyDaysReport(Stage mainStage, Scene mainScene) {
        Label reportForThirtyDays = new Label();
        String report = HibernateHelper.getInstance().getReportForNDaysByCurrency(currentCurrency,30);
        CSVFileWriter.getInstance().WriteReportInCSVFileByCurrency(report,currentCurrency);
        reportForThirtyDays.setText(report);
        Button back = new Button("Назад");
        VBox reportVBox = new VBox(reportForThirtyDays, back);
        reportVBox.setAlignment(Pos.CENTER);
        reportVBox.setPadding(new Insets(10));
        reportVBox.setSpacing(10);
        Scene reportScene = new Scene(reportVBox, 300, 600);
        back.setOnAction(x1 -> {
            mainStage.setScene(mainScene);
        });
        return reportScene;
    }
}