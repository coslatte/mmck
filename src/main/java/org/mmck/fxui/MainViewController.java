package org.mmck.fxui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.mmck.annotations.ValidationProxy;
import org.mmck.service.FileManager;
import org.mmck.service.FileService;

import java.io.File;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainViewController {

    private static final String DARK_THEME = Objects.requireNonNull(
            MainViewController.class.getResource("styles/dark-theme.css")).toExternalForm();
    private static final String LIGHT_THEME = Objects.requireNonNull(
            MainViewController.class.getResource("styles/light-theme.css")).toExternalForm();

    @FXML private TextField directoryField;
    @FXML private TextField filenameField;
    @FXML private TextField baseFileField;
    @FXML private TextField targetFileField;

    @FXML private VBox resultsCard;
    @FXML private VBox diffList;
    @FXML private Label summaryPill;
    @FXML private Label statusLabel;
    @FXML private Button themeToggleButton;

    private boolean darkTheme = true;

    private final FileService fileService = (FileService) Proxy.newProxyInstance(
            FileManager.class.getClassLoader(),
            new Class<?>[]{FileService.class},
            new ValidationProxy(new FileManager())
    );


    @FXML
    private void onToggleTheme() {
        darkTheme = !darkTheme;
        Scene scene = themeToggleButton.getScene();
        scene.getStylesheets().set(0, darkTheme ? DARK_THEME : LIGHT_THEME);
        themeToggleButton.setText(darkTheme ? "Tema: oscuro" : "Tema: claro");
    }


    @FXML
    private void onBrowseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleccionar directorio de mods");
        File dir = chooser.showDialog(directoryField.getScene().getWindow());
        if (dir != null) {
            directoryField.setText(dir.getAbsolutePath());
            setStatus("Directorio seleccionado: " + dir.getAbsolutePath(), Status.OK);
        }
    }

    @FXML
    private void onBrowseBase() {
        FileChooser chooser = fileChooser("Seleccionar archivo base");
        File file = chooser.showOpenDialog(baseFileField.getScene().getWindow());
        if (file != null) {
            baseFileField.setText(file.getAbsolutePath());
            setStatus("Archivo base seleccionado: " + file.getAbsolutePath(), Status.OK);
        }
    }

    @FXML
    private void onBrowseTarget() {
        FileChooser chooser = fileChooser("Seleccionar archivo de destino");
        File file = chooser.showOpenDialog(targetFileField.getScene().getWindow());
        if (file != null) {
            targetFileField.setText(file.getAbsolutePath());
            setStatus("Archivo destino seleccionado: " + file.getAbsolutePath(), Status.OK);
        }
    }

    @FXML
    private void onExport() {
        String filename = filenameField.getText();
        String directory = directoryField.getText();
        try {
            fileService.exportDirectoryListing(filename, directory);
            setStatus("Lista exportada correctamente — " + resolveExportPath(filename, directory), Status.OK);
        } catch (Exception e) {
            showError("No se pudo exportar la lista", e.getMessage());
        }
    }

    @FXML
    private void onCompare() {
        String base = baseFileField.getText();
        String target = targetFileField.getText();

        if (base.isBlank() || target.isBlank()) {
            showError("Comparación incompleta", "Debes seleccionar ambos archivos (base y destino) antes de comparar.");
            return;
        }

        Path basePath = Paths.get(base);
        Path targetPath = Paths.get(target);

        try {
            Set<String> missing = fileService.compareModFiles(basePath, targetPath);
            renderResults(missing);
        } catch (Exception e) {
            showError("No se pudo comparar", e.getMessage());
        }
    }


    private void renderResults(Set<String> missing) {
        diffList.getChildren().clear();

        List<String> ordered = missing.stream()
                .sorted(Comparator.naturalOrder())
                .toList();

        if (ordered.isEmpty()) {
            resultsCard.setVisible(true);
            resultsCard.setManaged(true);
            summaryPill.getStyleClass().removeAll("pill-blue", "pill-red", "pill-green");
            summaryPill.getStyleClass().add("pill-green");
            summaryPill.setText("Sin mods faltantes");
            diffList.getChildren().add(okItem());
            setStatus("Comparación completada: sin diferencias", Status.OK);
            return;
        }

        resultsCard.setVisible(true);
        resultsCard.setManaged(true);
        summaryPill.getStyleClass().removeAll("pill-blue", "pill-red", "pill-green");
        summaryPill.getStyleClass().add("pill-red");
        summaryPill.setText(ordered.size() + " mods faltantes");

        for (int i = 0; i < ordered.size(); i++) {
            diffList.getChildren().add(diffItem(i + 1, ordered.get(i)));
        }

        setStatus("Comparación completada: " + ordered.size() + " mods faltantes en el destino.", Status.WARN);
    }

    private HBox diffItem(int index, String modName) {
        Label idx = new Label(String.format("%02d", index));
        idx.getStyleClass().add("index");
        Label name = new Label(modName);
        name.getStyleClass().add("mod-name");
        name.setWrapText(true);
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Label badge = new Label("falta");
        badge.getStyleClass().add("pill-red");

        HBox row = new HBox(10, idx, name, spacer, badge);
        row.getStyleClass().add("diff-item");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return row;
    }

    private HBox okItem() {
        Label ok = new Label("OK");
        ok.getStyleClass().add("pill-green");
        Label text = new Label("No hay diferencias: todos los mods del archivo base existen en el destino.");
        text.getStyleClass().add("mod-name");
        HBox row = new HBox(10, ok, text);
        row.getStyleClass().add("ok-item");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return row;
    }


    private FileChooser fileChooser(String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Listas de mods (*.txt)", "*.txt"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        return chooser;
    }

    private String resolveExportPath(String filename, String directory) {
        String dir = (directory == null || directory.isBlank())
                ? Paths.get(System.getenv("APPDATA"), ".minecraft", "mods").toString()
                : directory;
        String name = (filename == null || filename.isBlank()) ? "file-" + FileManager.generateHashCode() : filename;
        return Paths.get(dir, name + ".txt").toString();
    }

    private enum Status { OK, WARN, ERROR }

    private void setStatus(String message, Status status) {
        statusLabel.setText(message);
        switch (status) {
            case OK -> statusLabel.setStyle("-fx-text-fill: #22c55e;");
            case WARN -> statusLabel.setStyle("-fx-text-fill: #f59e0b;");
            case ERROR -> statusLabel.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    private void showError(String title, String content) {
        setStatus(title, Status.ERROR);
        Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle("mmck · Error");
        alert.setHeaderText(title);
        alert.showAndWait();
    }
}