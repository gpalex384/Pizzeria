package pizzeria;

import MODELO.Pizza;
import MODELO.Precios;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

public class FXMLPizzeriaController implements Initializable {

    @FXML
    private Tab tabMasa;
    @FXML
    private ToggleGroup masa;
    @FXML
    private Tab tabTipo;
    @FXML
    private Tab tabIngExtra;
    @FXML
    private Tab tabTamano;
    @FXML
    private Button btGenerarTicket;
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
    private CheckBox cbGratinar;
    @FXML
    private CheckBox cbBebida;

    private double precioFinal = 0.00;
    private Pizza pizza = new Pizza();
    private Precios precio = new Precios();
    private static int contTicket = 0;

    ObservableList<String> tamaños
            = FXCollections.observableArrayList("pequeña", "mediana", "familiar");

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
        btMasaNormal.setSelected(true);
        comboTipos.setItems(tiposList);
        comboTipos.setValue("Básica");
        taIngredExtra.setItems(ingList);
        taIngredExtra.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListSpinnerValueFactory<String> valores = new ListSpinnerValueFactory(tamaños);
        spTamaño.setValueFactory(valores);
        calcularTotal();
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
        } else {
            pizza.setBebida("Sin bebida");
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

    @FXML
    private void generarTicket(ActionEvent event) {
        contTicket++;
        Path ruta = Paths.get("tickets/ticket" + contTicket + ".txt");
        File acceso = ruta.toFile();
        if (acceso.exists() == false) {
            pizza.generarTicket(ruta);
        } else {
            taPedido.appendText("Ya existe un ticket con el numero " + contTicket + "\n");
        }
    }

    private void alertaPrecios() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Proyecto Pizzeria");
        confirmacion.setHeaderText("Necesita cargar los precios");
        confirmacion.setContentText("¿Qué precios desea cargar?");

        ButtonType buttonDefault = new ButtonType("Por defecto");
        ButtonType buttonArchivo = new ButtonType("Archivo de texto");

        confirmacion.getButtonTypes().setAll(buttonDefault, buttonArchivo);

        Optional<ButtonType> result = confirmacion.showAndWait();
        if (result.get() == buttonDefault) {
            precio.setPrecios();
        } else if (result.get() == buttonArchivo) {
            Path archivo = Paths.get("precios/CartaPrecios.txt");
            precio.cargaPrecios(archivo);
        }
    }

}