package com.nicholastcc.datainventor.OCRMaven.desktopVersion;

import com.nicholastcc.datainventor.OCRMaven.ArquivoBase;
import com.nicholastcc.datainventor.OCRMaven.SensitiveDataFinder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class InputFile extends JFrame{
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	JTextField textField1;
	JButton mybutton;

public InputFile() {
            JFileChooser choose = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Aceito PDF, PNG e Derivados",
                "jpg", "pdf","tif","jpeg", "png"
                );
            choose.setFileFilter(filter);
            int retorno = choose.showOpenDialog(null);
            if (retorno == JFileChooser.APPROVE_OPTION){
                String caminho = choose.getSelectedFile().getAbsolutePath();
                try {
                    File file = new File(caminho);
                    ArquivoBase arquivoBase = new ArquivoBase(file, "", "");
                    SensitiveDataFinder le = new SensitiveDataFinder(arquivoBase);

                    JOptionPane.showMessageDialog(null, le.resultado);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
}

	public static void main(String args[]) {
		new InputFile();
	}
}
