package MODELO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class Precios {

    private static final Map<String, Double> precios = new HashMap<>();

    public Precios() {
    }

    public void setPrecios() {  // precios por defecto
        precios.put("Normal", 3.00);
        precios.put("Integral", 3.50);
        precios.put("Básica", 3.00);
        precios.put("Cuatro quesos", 5.00);
        precios.put("Barbacoa", 7.00);
        precios.put("Mexicana", 8.50);
        precios.put("jamón", 0.50);
        precios.put("queso", 0.75);
        precios.put("tomate", 1.50);
        precios.put("cebolla", 0.75);
        precios.put("olivas", 1.00);
        precios.put("pequeña", 1.00);
        precios.put("mediana", 1.15);
        precios.put("familiar", 1.30);
        precios.put("Con bebida", 2.00);
        precios.put("Sin bebida", 0.00);
        precios.put("Gratinada", 1.02);
        precios.put("Sin gratinar", 1.00);
    }

    public void cargaPrecios(Path archivo) {
        String line;
        File file = archivo.toFile();
        try ( BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2) {
                    String key = parts[0];
                    Double value = Double.parseDouble(parts[1]);
                    precios.put(key, value);
                }
            }
        } catch (IOException ex) {
            setPrecios();
        }
    }

    public Map<String, Double> getPrecios() {
        return precios;
    }

    public double getPrecio(String ing) {
        return precios.get(ing);
    }

}
