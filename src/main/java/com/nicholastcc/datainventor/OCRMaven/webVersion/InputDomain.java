package com.nicholastcc.datainventor.OCRMaven.webVersion;

import com.nicholastcc.datainventor.OCRMaven.ArquivoBase;
import com.nicholastcc.datainventor.OCRMaven.SensitiveDataFinder;
import com.nicholastcc.datainventor.OCRMaven.ValidateDataFormat;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputDomain implements ValidateDataFormat {

    private static final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private static final List<String> visitedArchives = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10); // Limite de 10 threads
    private static final int TIMEOUT = 5000;

    private static final int MAX_DEPTH = 4;
    private final String DOMINIO_DEPTH;

    public InputDomain(String dominio) {
        this.DOMINIO_DEPTH = dominio;
    }

    public static void main(String[] args) {
        /*String domain = "camarapoa.rs.gov.br"; // Substitua pelo domínio do site que você deseja vasculhar
        try {
            InputDomain inputDomain = new InputDomain(domain);

            List<String> listadepdf = inputDomain.InvetorDataSensetive(domain, 0);
            threadPool.shutdown();

            System.out.println("Total de arquivos encontrados: " + listadepdf.size());
            for (String content : listadepdf) {
                System.out.println("Arquivo encontrado: " + content);
            }
        } catch (UnsupportedEncodingException | InterruptedException e) {
            System.out.println("Erro no processamento: " + e.getMessage());
        }*/
    }

    private List<String> FounderPDF(String domain, int depth) throws UnsupportedEncodingException, InterruptedException {
        if (depth > MAX_DEPTH || !visitedUrls.add(domain)) {
            return Collections.emptyList();
        }

        try {
            URI urlURI = new URI(domain);
            String url = urlURI.toASCIIString();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String contentType = connection.getContentType();
                if (contentType != null && contentType.equals("application/pdf")) visitedArchives.add(domain + "," + domain);

                Document doc = Jsoup.parse(connection.getInputStream(), null, url);
                Elements links = doc.select("a[href]");

                for (Element link : links) {
                    String href = link.attr("abs:href");

                    if (!ValidateDataFormat.isSupportedProtocol(href)) {
                        continue;
                    }

                    if ((ValidateDataFormat.isPDF(href) || (ValidateDataFormat.isImage(href)) && href.contains(domain.replaceAll("https?://", "")))) {
                        if (!visitedArchives.contains(href)) {
                            System.out.println("AGAREF:"+href + " DOMAINN " + domain);
                            visitedArchives.add(domain + "," + href);
                        }
                    } else if (href.contains(this.DOMINIO_DEPTH)) {
                        FounderPDF(href, depth + 1);
                    }
                }
            } else {
                System.out.println("Falha ao conectar à URL: " + url);
            }
        } catch (IOException | URISyntaxException e) {
            logError("Erro ao processar a URL: " + domain, e);
        }

        return new ArrayList<>(visitedArchives);
    }

    public List<String> InvetorDataSensetive(String domain, int depth) throws UnsupportedEncodingException, InterruptedException {
        if (!domain.startsWith("https://") || !domain.startsWith("http://")) domain = "https://" + domain;

        List<String> processarArquivos = FounderPDF(domain, depth);
        visitedArchives.clear();
        List<String> dadosColetados = Collections.synchronizedList(new ArrayList<>());

        CountDownLatch latch = new CountDownLatch(processarArquivos.size());
        for (String href : processarArquivos) {
            threadPool.submit(() -> {
                try {
                    String resultado = processArchive(href.split(",")[1]);
                    if (!resultado.isEmpty()) {
                        dadosColetados.add(href.split(",")[0] + "," + resultado);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        return dadosColetados;
    }

    private static String processArchive(String href) {
        try {
            File file = downloadArchive(href);
            if (file == null || !file.exists()) {
                throw new IllegalArgumentException("The file does not exist or could not be downloaded: " + href);
            }
            ArquivoBase arquivoBase = new ArquivoBase(file, "", href);
            if (arquivoBase.arquivo.canExecute()) {
                SensitiveDataFinder sensitiveDataFinder = new SensitiveDataFinder(arquivoBase);
                String resultado = sensitiveDataFinder.resultado;
                sensitiveDataFinder.close();
                return resultado;
            }
        } catch (Exception e) {
            logError("Erro ao processar o arquivo: " + href, e);
        }
        return "";
    }

    private static File downloadArchive(String url) {
        try {
            URL validUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) validUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logError("Failed to download the file. HTTP response code: " + responseCode, null);
                return null;
            }

            try (InputStream in = connection.getInputStream()) {
                byte[] fileBytes = IOUtils.toByteArray(in);
                Path tempFilePath = Files.createTempFile("tempFile", ValidateDataFormat.extractFileExtension(url));
                Files.write(tempFilePath, fileBytes);
                return tempFilePath.toFile();
            }
        } catch (IOException e) {
            logError("Erro ao baixar o arquivo: " + url, e);
            return null;
        }
    }

    private static void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
