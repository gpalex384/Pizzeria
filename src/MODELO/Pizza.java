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
    private static int contTicket = 0;
    Date date = new Date();
    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

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

    public Pizza() {
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

    public String composicion() {
        double porcentaje = (PRECIO.getPrecio(tamano) - 1) * 100;
        String pedido = "MASA: " + masa + String.format(" - %.2f", PRECIO.getPrecio(masa))
                + "€\n" + "TIPO: " + tipo + String.format(" - %.2f", PRECIO.getPrecio(tipo)) + "€\n"
                + "INGREDIENTES EXTRA: " + ingredientes.toString() + " - " + String.format("%.2f", precioIngr) + "€\n"
                + "TAMAÑO: " + tamano + " + " + String.format("%.0f", porcentaje) + "%\n"
                + "GRATINAR (+2%): " + gratinada + "\n"
                + "BEBIDA (2€): " + bebida + "\n";
        return pedido;
    }

    public double calcularPrecio() {
        double precioTot;
        precioIngr = 0.00;
        if (ingredientes.isEmpty() == false) {
            ingredientes.forEach(ingrediente -> {
                precioIngr += PRECIO.getPrecio(ingrediente);
            });
        }
        precioTot = (PRECIO.getPrecio(masa) + PRECIO.getPrecio(tipo) + precioIngr
                + PRECIO.getPrecio(bebida)) * PRECIO.getPrecio(tamano) * PRECIO.getPrecio(gratinada);
        return precioTot;
    }

    public boolean generarTicket(Path ruta) {
        try (
                 OutputStream os = new FileOutputStream(ruta.toFile());  BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
            contTicket++;
            double porcentaje = (PRECIO.getPrecio(tamano) - 1) * 100;
            bw.write("" + fecha.format(date) + " - " + hora.format(date));
            bw.newLine();
            bw.newLine();
            bw.write("Nº DE TICKET " + contTicket);
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
            bw.write("BEBIDA (2€): " + bebida);
            bw.newLine();
            bw.newLine();
            bw.write("TOTAL: " + String.format("%.2f", calcularPrecio()) + " €");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
