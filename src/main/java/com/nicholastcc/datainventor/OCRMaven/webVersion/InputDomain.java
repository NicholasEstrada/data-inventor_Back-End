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

    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private final List<String> visitedArchives = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10); // Limite de 10 threads
    private static final int TIMEOUT = 6000;

    private static final int MAX_DEPTH = 4;
    private final String DOMINIO_DEPTH;

    public int quantityLinkVisited;

    public InputDomain(String dominio) {
        this.DOMINIO_DEPTH = dominio;
    }

    public static void main(String[] args) {
        String domain = "ifc.edu.br"; // Substitua pelo domínio do site que você deseja vasculhar
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
        }
    }

    private List<String> FounderPDF(String startDomain) throws UnsupportedEncodingException, InterruptedException {
        Deque<String> stack = new ArrayDeque<>();
        Set<String> stackSet = Collections.synchronizedSet(new HashSet<>()); // Conjunto para rastrear URLs na pilha
        stack.push(startDomain);
        stackSet.add(startDomain); // Adicionar o domínio inicial ao conjunto
        List<String> result = new ArrayList<>();

        while (!stack.isEmpty()) {
            String currentDomain = stack.pop();
            stackSet.remove(currentDomain); // Remover da pilha após processar

            if (visitedUrls.contains(currentDomain)) {
                continue;
            }

            visitedUrls.add(currentDomain);

            System.out.println("#######################################################");
            System.out.println("visitedUrls.size():" + visitedUrls.size());
            System.out.println("normalizarUrl(currentDomain): " + normalizarUrl(currentDomain));
            System.out.println("currentDomain: " + currentDomain);
            System.out.println("visitedArchives: " + visitedArchives.size());

            try {
                URI urlURI = new URI(currentDomain);
                String url = urlURI.toASCIIString();
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);

                int connectionApli = connection.getResponseCode();

                if (connectionApli == HttpURLConnection.HTTP_OK) {
                    String contentType = connection.getContentType();
                    if (contentType != null && contentType.equals("application/pdf")) {
                        visitedArchives.add(currentDomain + "," + currentDomain);
                    }

                    Document doc = Jsoup.parse(connection.getInputStream(), null, url);
                    Elements links = doc.select("a[href]");

                    for (Element link : links) {
                        String href = link.attr("abs:href");
                        href = normalizarUrl(href);

                        if (!ValidateDataFormat.isSupportedProtocol(href)) {
                            continue;
                        }

                        if (ValidateDataFormat.isPDF(href) || ValidateDataFormat.isImage(href)) {
                            if (!visitedArchives.contains(currentDomain + "," + href)) {
                                System.out.println("PDFACHADO: " + href + " DOMAIN: " + currentDomain);
                                visitedArchives.add(currentDomain + "," + href);
                            }
                        } else if (href.contains(this.DOMINIO_DEPTH) &&
                                !visitedUrls.contains(href) && !stackSet.contains(href)) {
                            stack.push(href);  // Adiciona o link para processar posteriormente
                            stackSet.add(href); // Adiciona ao conjunto de URLs na pilha
                            System.out.println("ADDTOLISTENER: " + href);
                        }
                    }
                } else {
                    System.out.println("Falha ao conectar à URL: " + url);
                }
            } catch (IOException | URISyntaxException e) {
                logError("Erro ao processar a URL: " + currentDomain, e);
            }
            //ERif(visitedUrls.size()/3 >= visitedArchives.size()) return new ArrayList<>(visitedArchives);
        }

        return new ArrayList<>(visitedArchives);
    }


    public List<String> InvetorDataSensetive(String domain, int depth) throws UnsupportedEncodingException, InterruptedException {
        if (!domain.startsWith("https://") || !domain.startsWith("http://")) domain = "https://" + domain;

        List<String> processarArquivos = FounderPDF(domain);
        visitedArchives.clear();
        quantityLinkVisited = visitedUrls.size();
        System.out.println("QTD URL: "+visitedUrls.size());
        visitedUrls.clear();
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
            // if (arquivoBase.arquivo.canExecute()) { -> da bug no linux
                SensitiveDataFinder sensitiveDataFinder = new SensitiveDataFinder(arquivoBase);
                String resultado = sensitiveDataFinder.resultado;
                sensitiveDataFinder.close();
                return resultado;
            // }
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

    private String normalizarUrl(String url) {
        // Remover fragmentos (tudo após #)
        int indexFragment = url.indexOf("#");
        if (indexFragment != -1) {
            url = url.substring(0, indexFragment);
        }
        indexFragment = url.indexOf("&");
        if (indexFragment != -1) {
            url = url.substring(0, indexFragment);
        }
        indexFragment = url.indexOf("?");
        if (indexFragment != -1) {
            url = url.substring(0, indexFragment);
        }
        indexFragment = url.indexOf(";");
        if (indexFragment != -1) {
            url = url.substring(0, indexFragment);
        }
        indexFragment = url.indexOf(",");
        if (indexFragment != -1) {
            url = url.substring(0, indexFragment);
        }

        // Remover parâmetros irrelevantes para evitar duplicidade
        url = url.replaceAll("(tab_files|tab_details|do|ns|image)=[^&]*&?", "");

        // Remover & ou ? no final caso fiquem pendentes após a substituição
        url = url.replaceAll("[&?]$", "");

        return url;
    }


    private static void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
