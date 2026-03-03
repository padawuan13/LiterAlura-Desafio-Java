package com.aluracursos.literalura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ConsultaTraductor {
    public static String obtenerTraduccion(String texto, String idiomas) {
        ConsumoAPI consumo = new ConsumoAPI();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String textoCodificado = URLEncoder.encode(texto, StandardCharsets.UTF_8);

            String url = "https://api.mymemory.translated.net/get?q=" + textoCodificado + "&langpair=" + idiomas;

            String json = consumo.obtenerDatos(url);
            JsonNode node = mapper.readTree(json);
            String traduccion = node.get("responseData").get("translatedText").asText();

            return traduccion.replaceAll("^\\d+\\.\\s*", "").trim();
        } catch (Exception e) {
            return texto;
        }
    }
}