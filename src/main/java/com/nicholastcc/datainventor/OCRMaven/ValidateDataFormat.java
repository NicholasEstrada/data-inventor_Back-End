package com.nicholastcc.datainventor.OCRMaven;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public interface ValidateDataFormat {
    static final int TIMEOUT = 50;

    static boolean validarCPF(String cpf) {
        // Remover caracteres especiais
        String cpfNumeros = cpf.replaceAll("[^0-9]", "");

        // Verificar se possui 11 dígitos
        if (cpfNumeros.length() != 11) {
            return false;
        }

        // Verificar se todos os dígitos são iguais
        if (cpfNumeros.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Validar primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpfNumeros.charAt(i)) * (10 - i);
        }
        int digitoVerificador1 = 11 - (soma % 11);
        if (digitoVerificador1 > 9) {
            digitoVerificador1 = 0;
        }
        if (Character.getNumericValue(cpfNumeros.charAt(9)) != digitoVerificador1) {
            return false;
        }

        // Validar segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpfNumeros.charAt(i)) * (11 - i);
        }
        int digitoVerificador2 = 11 - (soma % 11);
        if (digitoVerificador2 > 9) {
            digitoVerificador2 = 0;
        }
        if (Character.getNumericValue(cpfNumeros.charAt(10)) != digitoVerificador2) {
            return false;
        }

        return true;
    }

    static String formatarCPF(String cpf) {
        DecimalFormat formatador = new DecimalFormat("00000000000");

        try {
            String cpfFormatado = formatador.format(Long.parseLong(cpf));
            return cpfFormatado.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } catch (NumberFormatException e) {
            return cpf;
        }
    }

    static String extractFileExtension(String url) {
        try {
            URL urlObj = new URL(url);
            String path = urlObj.getPath();
            int lastDotIndex = path.lastIndexOf('.');

            if (lastDotIndex != -1) {
                return path.substring(lastDotIndex);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Trate exceções adequadamente em um ambiente de produção
        }

        return ""; // Se não encontrar a extensão, retorna uma string vazia ou outra indicação apropriada
    }

    static URL Codifier(String url) {
        try {

            // Use URLEncoder para codificar a URL inteira, incluindo o caminho para o arquivo
            String encodedURL = URLEncoder.encode(url, StandardCharsets.UTF_8).replace("+", "%20");

            // Construa a URI com a URL codificada
            URL uri = new URL(encodedURL
                    .replaceAll("%2F", "/")
                    .replaceAll("%3A", ":")
                    .replaceAll("%25", "%")
            );
            return uri;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean isPDF(String url) {
        return url.toLowerCase().endsWith(".pdf");
        /*
        if(url.toLowerCase().endsWith(".pdf")) return true;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            String contentType = connection.getContentType();
            return contentType != null && contentType.equals("application/pdf");
        } catch (IOException e) {
            System.out.println("Erro ao verificar o tipo de conteúdo: " + url + e);
            return false;
        }*/
    }

    static boolean isSupportedProtocol(String url) {
        try {
            URI uri = new URI(url);
            String protocol = uri.getScheme();
            return protocol != null && (protocol.equals("http") || protocol.equals("https"));
        } catch (URISyntaxException e) {
            System.out.println("Erro ao verificar o protocolo da URL: " + url + e);
            return false;
        }
    }

    static boolean isImage(String url) {

        List<String> IMAGE_EXTENSIONS = Arrays.asList(
                ".bmp", ".gif", ".jpg", ".jpeg", ".png", ".tiff", ".tif", ".ico", ".webp"
        );

        String lowercaseUrl = url.toLowerCase();

        for (String extension : IMAGE_EXTENSIONS) {
            if (lowercaseUrl.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
