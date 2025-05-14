package com.nicholastcc.datainventor.OCRMaven;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class SensitiveDataFinder implements Closeable, DataInspector {

	// verificar melhor metodo de retorno deste procurador de dados sensiveis
	// #1 FECHAR CLASSE APOS PROCURA
	// #2 MELHORAR RASPAGEM PARA APLICAR THREADS EM VALIDAÇÕES CONCORRENTE E QUE POSSAM SER CONCORRENTES
	public String resultado;

	public SensitiveDataFinder( ArquivoBase arquivoBase) throws IOException{

		if (arquivoBase.tipoProcessamento.isEmpty()) {

			String result = "";

            try{
                result = extractTextFromStream(arquivoBase.arquivo);
				arquivoBase.tipoProcessamento = "PDFText";
            }catch(Exception e){
                System.err.println(e.getMessage());
            }

            if (result.isEmpty()) {
                arquivoBase.tipoProcessamento = "OCR";
            }else{
                resultado = resultadosDaBusca(arquivoBase, result);
            }
        }

		if ( arquivoBase.tipoProcessamento.equals("OCR") ) {
			Tesseract tess4j = new Tesseract();
			// tess path location ATUALIZAR EM CASO DE TROCA DE AREA DE DESENVOLVIMENTO
			File tessDataFolder = new File("tessdata");
			tess4j.setDatapath(tessDataFolder.getAbsolutePath());
			tess4j.setTessVariable("user_defined_dpi", "96");
			tess4j.setTessVariable("debug_file", "/dev/null");
			try {
				if(arquivoBase.arquivo.length() > 0) {
					String result = tess4j.doOCR(arquivoBase.arquivo); // at JavaTCC.OCRMaven.SensitiveDataFinder.<init>(SensitiveDataFinder.java:50)
					resultado = resultadosDaBusca(arquivoBase, result);
				}else{
					System.err.println("Arquivo PDF vazio ou corrompido: " + arquivoBase.arquivo.getName());
				}
			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			} finally {
				tess4j = null;
			}
		}

		// FINALIZAR O ARQUIVO FECHAR File arquivoBase.arquivo
		arquivoBase.fileFinalizer();
	}

	private static String extractTextFromStream(File file) throws IOException {
		try (PDDocument document = PDDocument.load(file)) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String text = pdfStripper.getText(document);
			return text.trim();
		}
	}

	private static String resultadosDaBusca(ArquivoBase arquivoBase, String result) throws IOException {
		try {
			return arquivoBase.pathLocation.replaceAll("\\\\tempFile.*", "") +
					"|" + DataInspector.procuraEmail(result, 0) +
					"," + DataInspector.procuraCPF(result, 0) +
					"," + DataInspector.extractOpiniaoPolitica(result) +
					"," + DataInspector.extractVidaSexual(result) +
					"," + DataInspector.extractDadoSaude(result) +
					"," + DataInspector.extractDadoGenetico(result) +
					"," + DataInspector.extractDadoBiometrico(result) +
					"," + DataInspector.extractConviccaoReligiosa(result) +
					"," + DataInspector.extractOrigemRacial(result) +
					"|" + arquivoBase.extensaoArquivo +
					"|" + arquivoBase.tipoProcessamento;
		}catch (Exception e){
			System.err.println(e.getMessage());
		}
		return "";
	}


	@Override
	public void close() throws IOException {
	}

}
