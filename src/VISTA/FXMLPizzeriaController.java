package VISTA;

import MODELO.Pizza;
import MODELO.Precios;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLPizzeriaController implements Initializable {

    @FXML
    private ToggleGroup masa;
    @FXML
    private RadioButton btMasaNormal;
    @FXML
    private RadioButton btMasaIntegral;
    @FXML
    public TextArea taPedido;
    @FXML
    private Label lbTotal;
    @FXML
    private ComboBox<String> comboTipos;
    @FXML
    private ListView<String> taIngredExtra;
    @FXML
    private Spinner<String> spTamaño;
    @FXML
    private Spinner<Integer> spBebidas;
    @FXML
    private CheckBox cbGratinar;
    @FXML
    private CheckBox cbBebida;
    @FXML
    private Label lbFechaHora;

    private double precioFinal = 0.00;
    private Pizza pizza = new Pizza();
    private Precios precio = new Precios();
    Date date = new Date();
    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

    ObservableList<String> tamaños
            = FXCollections.observableArrayList("pequeña", "mediana", "familiar");

    ObservableList<Integer> bebidas
            = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    ObservableList<String> tiposList
            = FXCollections.observableArrayList(
                    "Básica",
                    "Cuatro quesos",
                    "Barbacoa",
                    "Mexicana"
            );

    ObservableList<String> ingList
            = FXCollections.observableArrayList(
                    "jamón",
                    "queso",
                    "tomate",
                    "cebolla",
                    "olivas"
            );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alertaPrecios();
        lbFechaHora.setText("" + fecha.format(date) + " - " + hora.format(date));
        btMasaNormal.setSelected(true);
        comboTipos.setItems(tiposList);
        comboTipos.setValue("Básica");
        taIngredExtra.setItems(ingList);
        taIngredExtra.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListSpinnerValueFactory<String> valores = new ListSpinnerValueFactory(tamaños);
        spTamaño.setValueFactory(valores);
        ListSpinnerValueFactory<Integer> valores2 = new ListSpinnerValueFactory(bebidas);
        spBebidas.setValueFactory(valores2);
        calcularTotal();
    }
    
    private void alertaPrecios() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Proyecto Pizzeria");
        confirmacion.setHeaderText("Necesita cargar los precios");
        confirmacion.setContentText("¿Qué precios desea cargar?");

        Stage stage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:resources/images/icon.png"));

        ButtonType buttonDefault = new ButtonType("Por defecto");
        ButtonType buttonArchivo = new ButtonType("Archivo de texto");

        confirmacion.getButtonTypes().setAll(buttonDefault, buttonArchivo);

        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.get() == buttonDefault) {
            precio.setPrecios();
        } else if (result.get() == buttonArchivo) {
            Path archivo = abrirPrecios();
            precio.cargaPrecios(archivo);
        }
    }
    
    @FXML
    private void calcularTotal() {
        taPedido.setText("");
        setMasa();
        setTipo();
        setIngredientes();
        setTamano();
        setBebida();
        setGratinada();
        precioFinal = pizza.calcularPrecio();
        lbTotal.setText(String.format("%.2f", precioFinal) + " €");
        taPedido.setText(pizza.composicion());
    }


    private void setMasa() {
        if (btMasaNormal.isSelected()) {
            pizza.setMasa("Normal");
        } else if (btMasaIntegral.isSelected()) {
            pizza.setMasa("Integral");
        }
    }

    private void setTipo() {
        pizza.setTipo(comboTipos.getValue());
    }

    private void setIngredientes() {
        Set<String> SelectIngredSet = new HashSet<>();
        ObservableList<String> seleccionadosList = taIngredExtra.getSelectionModel().getSelectedItems();

        for (int i = 0; i < seleccionadosList.size(); i++) {
            SelectIngredSet.add(seleccionadosList.get(i));
        }
        pizza.setIngredientes(SelectIngredSet);
    }

    private void setTamano() {
        pizza.setTamano(spTamaño.getValue());
    }

    private void setBebida() {
        if (cbBebida.isSelected()) {
            pizza.setBebida("Con bebida");
            pizza.setNumBebidas(spBebidas.getValue());
        } else {
            pizza.setBebida("Sin bebida");
            pizza.setNumBebidas(0);
        }
    }

    private void setGratinada() {
        if (cbGratinar.isSelected()) {
            pizza.setGratinada("Gratinada");
        } else {
            pizza.setGratinada("Sin gratinar");
        }
    }

    @FXML                                                  
    private void generarTicket(ActionEvent event) {
        pizza.setNumTicket();
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("tickets"));
        File file = directoryChooser.showDialog(null);
        String fileName = file.toString() + "/ticket" + pizza.getNumTicket() + ".txt";
        File ruta = new File(fileName);
        if (ruta.exists() == false) {
            pizza.generarTicket(ruta.toPath());
            taPedido.appendText("Ticket guardado --> " + ruta.getPath() + "\n");
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Proyecto Pizzeria");
            alert.setHeaderText(null);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:resources/images/icon.png"));
            alert.setContentText("Ya existe un ticket con el nombre " + "ticket" + pizza.getNumTicket() + ".txt\nElige otro nombre \n");
            alert.showAndWait();      
            guardarComo();
        }
    }

    private void guardarComo() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("tickets/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        Path archivo = file.toPath();
        pizza.generarTicket(archivo);
    }

    private Path abrirPrecios() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("precios/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            return file.toPath();
        }
        return null;
    }

    @FXML
    private void abrirTicket(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("tickets/"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        Path archivo = file.toPath();

        try ( Stream<String> datos = Files.lines(archivo)) {
            Iterator<String> it = datos.iterator();
            taPedido.setText("");
            while (it.hasNext()) {
                String linea = it.next();
                taPedido.appendText(linea + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Error en la lectura del archivo");
        }

    }

}
