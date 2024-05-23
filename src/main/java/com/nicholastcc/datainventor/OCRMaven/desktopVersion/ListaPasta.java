package com.nicholastcc.datainventor.OCRMaven.desktopVersion;

import com.nicholastcc.datainventor.OCRMaven.ArquivoBase;
import com.nicholastcc.datainventor.OCRMaven.SensitiveDataFinder;

import java.io.File;
import java.io.IOException;

public class ListaPasta {
	public static String leitura;

	public ListaPasta(String diretorio) throws IOException {
		File file = new File(diretorio);
		File afile[] = file.listFiles();

		int i = 0;
		for (int j = afile.length; i < j; i++) {
			File arquivos = afile[i];
			recursivo(arquivos, diretorio);
		}
	}

	public static void recursivo(File arquivos, String diretorio) {
		if (arquivos.isDirectory()) {
			String diretorio1 = diretorio + File.separator + arquivos.getName();

			File file1 = new File(diretorio1);
			File afile1[] = file1.listFiles();

			int i1 = 0;
			for (File arquivos1 : afile1) {
				if (arquivos1.isDirectory()) {
					recursivo(arquivos1, diretorio1);
				} else {
					processaArquivo(arquivos1, diretorio1);
				}
			}
		} else {
			processaArquivo(arquivos, diretorio);
		}
	}

	private static void processaArquivo(File arquivo, String diretorio) {
		String nomeArquivo = diretorio + File.separator + arquivo.getName();
		if (tipoAceito(nomeArquivo)) {
			try {
				ArquivoBase arquivoBase = new ArquivoBase(arquivo, "", diretorio);
				SensitiveDataFinder lerArquivo = new SensitiveDataFinder(arquivoBase);
				leitura = lerArquivo.resultado;
				System.out.println(leitura);
			} catch (IOException e) {
				System.err.println("Erro ao processar o arquivo " + nomeArquivo + ": " + e.getMessage());
			}
		} else {
			System.out.println("Tipo de arquivo não aceito: " + nomeArquivo);
		}
	}

	private static boolean tipoAceito(String name) {
		String extensao = name.toLowerCase();
		return 	extensao.endsWith(".pdf") ||
				extensao.endsWith(".tif") ||
				extensao.endsWith(".png") ||
				extensao.endsWith(".jpg");
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// try {
		// ListaPasta v = new ListaPasta("test");
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
