package com.nicholastcc.datainventor.OCRMaven;

import java.io.File;

public class ArquivoBase {
    // File args, String type
    public File arquivo;
    public String extensaoArquivo;
    public String tipoProcessamento;
    public String pathLocation;

    public ArquivoBase(File arquivo, String tipoProcessamento, String pathLocation) {
        this.arquivo = arquivo;
        this.tipoProcessamento = tipoProcessamento; // via OCR em caso de imagens e etc
        this.pathLocation = pathLocation + "\\" +arquivo.getName();//.replaceAll("\\\\tempFile\\d+", ""); // localizacao do arquivo

        String nomeArquivo = arquivo.getName();
        int posicaoPonto = nomeArquivo.lastIndexOf('.');
        if (posicaoPonto > 0) {
            this.extensaoArquivo = nomeArquivo.substring(posicaoPonto + 1);
        } else {
            throw new IllegalArgumentException("O arquivo não possui uma extensão.");
        }
    }
}
