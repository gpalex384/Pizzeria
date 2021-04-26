package MODELO;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Pizza {

    private String masa = "";
    private String tipo = "";
    private String tamano = "";
    private String gratinada = "";
    private String bebida = "";
    private Set<String> ingredientes = new HashSet<>();
    private final Precios PRECIO = new Precios();
    private double precioIngr = 0.00;
    private double precioBebidas = 0.00;
    private int numBebidas = 0;
    private static int contTicket = 0;
    Date date = new Date();
    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

    public Pizza() {

    }

    public int getNumBebidas() {
        return numBebidas;
    }

    public void setNumBebidas(int numBebidas) {
        this.numBebidas = numBebidas;
    }

    public String getGratinada() {
        return gratinada;
    }

    public void setGratinada(String gratinada) {
        this.gratinada = gratinada;
    }

    public String getBebida() {
        return bebida;
    }

    public void setBebida(String bebida) {
        this.bebida = bebida;
    }

    public String getMasa() {
        return masa;
    }

    public void setMasa(String masa) {
        this.masa = masa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public Set<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(Set<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getNumTicket() {
        return contTicket;
    }

    public void setNumTicket() {
        contTicket++;
    }

    public double calcularPrecio() {
        double precioTot = 0.00;
        precioIngr = 0.00;
        precioBebidas = PRECIO.getPrecio("Con bebida") * numBebidas;
        if (ingredientes.isEmpty() == false) {
            ingredientes.forEach(ingrediente -> {
                precioIngr += PRECIO.getPrecio(ingrediente);
            });
        }
        if ("Sin bebida".equalsIgnoreCase(getBebida())) {
            precioTot = ((PRECIO.getPrecio(masa) + PRECIO.getPrecio(tipo)
                    + precioIngr) * PRECIO.getPrecio(tamano) * PRECIO.getPrecio(gratinada));
        } else if ("Con bebida".equalsIgnoreCase(getBebida())) {
            precioTot = ((PRECIO.getPrecio(masa) + PRECIO.getPrecio(tipo)
                    + precioIngr) * PRECIO.getPrecio(tamano) * PRECIO.getPrecio(gratinada)) + precioBebidas;
        }
        return precioTot;
    }

    public String composicion() {
        double porcentaje = (PRECIO.getPrecio(tamano) - 1) * 100;
        String pedido = "MASA: " + masa + String.format(" - %.2f", PRECIO.getPrecio(masa))
                + "€\n" + "TIPO: " + tipo + String.format(" - %.2f", PRECIO.getPrecio(tipo)) + "€\n"
                + "INGREDIENTES EXTRA: " + ingredientes.toString() + " - " + String.format("%.2f", precioIngr) + "€\n"
                + "TAMAÑO: " + tamano + " + " + String.format("%.0f", porcentaje) + "%\n"
                + "GRATINAR (+2%): " + gratinada + "\n"
                + "BEBIDA (2€): " + bebida + " X" + numBebidas + String.format(" - %.2f", precioBebidas) + "€\n";
        return pedido;
    }

    public boolean generarTicket(Path ruta) {
        try (
                 OutputStream os = new FileOutputStream(ruta.toFile());  BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
            double porcentaje = (PRECIO.getPrecio(tamano) - 1) * 100;
            bw.write("" + fecha.format(date) + " - " + hora.format(date));
            bw.newLine();
            bw.newLine();
            bw.write("Nº DE TICKET " + getNumTicket());
            bw.newLine();
            bw.newLine();
            bw.write("MASA: " + masa + String.format(" - %.2f", PRECIO.getPrecio(masa)) + "€");
            bw.newLine();
            bw.write("TIPO: " + tipo + String.format(" - %.2f", PRECIO.getPrecio(tipo)) + "€");
            bw.newLine();
            bw.write("INGREDIENTES EXTRA: " + ingredientes.toString() + " - " + String.format("%.2f", precioIngr) + "€");
            bw.newLine();
            bw.write("TAMAÑO: " + tamano + " + " + String.format("%.0f", porcentaje) + "%");
            bw.newLine();
            bw.write("GRATINAR (+2%): " + gratinada);
            bw.newLine();
            if ("Sin bebida".equalsIgnoreCase(getBebida())) {
                bw.write("BEBIDA (2€): " + bebida + " - 0.00 €");
            } else if ("Con bebida".equalsIgnoreCase(getBebida())) {
                bw.write("BEBIDA (2€): " + bebida + " X" + numBebidas + String.format(" - %.2f", precioBebidas) + "€");
            }
            bw.newLine();
            bw.newLine();
            bw.write("TOTAL: " + String.format("%.2f", calcularPrecio()) + " €");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
