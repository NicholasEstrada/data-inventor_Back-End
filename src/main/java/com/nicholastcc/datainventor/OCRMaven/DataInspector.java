package com.nicholastcc.datainventor.OCRMaven;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/*
 +  FUNÇÕES DE BUSCA DE DADOS SENSIVEIS DENTRO DOS ARQUIVOS
 */
public interface DataInspector extends ValidateDataFormat {

    static String procuraEmail(String result, int inicio) {
        List<String> emails = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");
        Matcher matcher = pattern.matcher(result.substring(inicio));

        while (matcher.find()) {
            String email = matcher.group();
            emails.add(email);
            inicio += matcher.end(); // Move the starting point for the next search
            matcher = pattern.matcher(result.substring(inicio));
        }

        // Concatenate emails with the desired format
        return emails.stream()
                .map(e -> "e-mail:" + e)
                .collect(Collectors.joining(","));
    }

    static String procuraCPF(String result, int inicio) {

        if (result == null) {
            return "";
        }

        int i;
        StringBuilder cpfs_achados = new StringBuilder();
        String regex_0 = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        String regex_1 = "\\d{9}-\\d{2}";
        String regex_2 = "\\d{11}";
        String[] regexes = { regex_0, regex_1, regex_2 };

        for (i = 0; i < regexes.length; i++) {
            String matchCPF = FounderRecursiveCPF(regexes[i], result, inicio);
            if (matchCPF != null && !matchCPF.isEmpty()) {
                if (!cpfs_achados.isEmpty()) {
                    cpfs_achados.append(",");
                }
                cpfs_achados.append(matchCPF);
            }
        }

        return cpfs_achados.toString();

    }

    private static String FounderRecursiveCPF(String regex, String texto, int inicio) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        StringBuilder result = new StringBuilder();
        boolean hasMatch = false;

        while (matcher.find(inicio)) {
            String cpf = matcher.group();

            if (!cpf.isEmpty()) {
                if (ValidateDataFormat.validarCPF(cpf)) {
                    cpf = ValidateDataFormat.formatarCPF(cpf);
                } else {
                    cpf = "";
                }

                if (hasMatch) {
                    if(!cpf.isEmpty()) result.append(",");
                }
                if(!cpf.isEmpty()) result.append("CPF:").append(cpf);

                inicio = matcher.end();
                hasMatch = true;
            }
        }

        if (!hasMatch) {
            return null;
        }

        return result.toString();
    }

    static String ProcuraOpiniaoPolitica(String input){
        return "";
    }
}
