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

    static String extractOpiniaoPolitica(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:conservador|liberal|socialista|comunista|anarquista|progressista|reacionário|centrista|libertário|monarquista|republicano|democrata|fascista|nacionalista|internacionalista|capitalista|anticapitalista|ambientalista|feminista|igualitário|populista|globalista|autoritário|totalitário|marxista|leninista|trotskista|maoísta|stalinista|social-democrata|centro-direita|centro-esquerda|direita radical|esquerda radical|extrema-direita|extrema-esquerda|partido político|filiação partidária|partido democrata|partido republicano|partido comunista|partido socialista|partido verde|partido liberal|partido conservador|partido trabalhista|partido progressista|partido nacionalista|partido ambientalista|partido feminista|partido igualitário|partido populista|partido globalista|partido autoritário|partido totalitário|partido marxista|partido leninista|partido trotskista|partido maoísta|partido stalinista|partido social-democrata)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("OpiniaoPolitica:" + matcher.group());
        }

        for (String dado : results) {
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

    static String extractOrigemRacial(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:branco|negro|pardo|asiático|indígena|latino|europeu|africano|árabe|mulato|caucasiano|afrodescendente|afroamericano|indiano|oriental|mestizo|nativo|aborígene|ameríndio|hispânico|moreno|rom|roma)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("OrigemRacial:" + matcher.group());
        }

        for (String dado : results) {
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

    static String extractConviccaoReligiosa(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:cristão|muçulmano|judeu|budista|hinduísta|ateu|agnóstico|espírita|umbandista|sikh|protestante|católico|ortodoxo|evangélico|presbiteriano|batista|luterano|anglicano|mórmon|taoísta|xintoísta|zoroastriano|jainista|paganista|wiccano|druida|confucionista|candomblecista|baha'i|rastafári)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("ConviccaoReligiosa:" + matcher.group());
        }

        for (String dado : results) {
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

    static String extractDadoBiometrico(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:impressão digital|íris escaneada|voz reconhecida|retina mapeada|DNA registrado|geometria da mão|face reconhecida|veias escaneadas|padrão de digitação|passos reconhecidos|reconhecimento facial|impressão da palma|padrão de íris|reconhecimento de voz|exame de retina|DNA armazenado|mapeamento facial|geometria das veias|assinatura digital|padrão de fala|escaneamento da íris|mapeamento de retina|análise de voz|leitura de impressão|registro de dna|reconhecimento de face|varredura da mão|identificação de veias|análise de passos|padrão vocal|leitura de impressão digital|identificação de íris|padrão de impressão)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("DadoBiometrico:" + matcher.group());
        }

        for(String dado : results){
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

    static String extractDadoGenetico(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:dna alterado|mutação genética|cromossomo extra|polimorfismo|síndrome genética|doença hereditária|marcador genético|predisposição genética|gene recessivo|gene dominante|genoma sequenciado|mutação BRCA1|gene CFTR|alelos distintos|DNA mitocondrial|mutação somática|doença monogênica|epigenética alterada|haplótipo raro|heterozigose|homozigose|mutação germinativa|deleção genética|inserção genética|duplicação genética|translocação cromossômica|inversão cromossômica|mosaico genético|mutação no gene TP53|polimorfismo no gene APOE|sequenciamento de DNA|análise de DNA|teste genético|análise de genoma|mutação de gene|alteração cromossômica)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("DadoGenetico:" + matcher.group());
        }

        for (String dado : results) {
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

    static String extractDadoSaude(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:diabético|hipertenso|asmático|obeso|com depressão|ansioso|com hepatite|com câncer|alérgico|com AIDS|cardíaco|epiléptico|bipolar|hipertenso|autista|com TDAH|com artrite|com lúpus|esquizofrênico|celíaco|com esclerose|com endometriose|com fibromialgia|com Alzheimer|com leucemia|com anemia|com Parkinson|com osteoporose|com distrofia muscular|com síndrome de Down|com asma|com hipertensão|com colesterol alto|com diabetes tipo 1|com diabetes tipo 2|com doença celíaca|com intolerância à lactose|com hipotiroidismo|com hipertiroidismo|com hepatite B|com hepatite C|com cirrose|com insuficiência renal|com insuficiência cardíaca|com angina|com arritmia|com glaucoma|com catarata|com psoríase|com vitiligo|com rosácea|com lupus|com artrite reumatoide|com doença de Crohn|com colite ulcerativa|com esclerose múltipla|com miastenia gravis|com doença de Huntington)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("DadoSaude:" + matcher.group());
        }

        for (String dado : results) {
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

    static String extractVidaSexual(String text) {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder resultsFinal = new StringBuilder();
        String regex = "\\b(?:homossexual|heterossexual|bissexual|assexual|pansexual|demissexual|poliamoroso|monogâmico|celibatário|transgênero|intersexual|não-binário|queer|genderqueer|gênero fluido|arromântico|heteroflexível|homorromântico|biromântico|demirromântico|lithromântico|andrógino|bigênero|pangênero|genderflux|polissexual|graysexual|androsexual|ginesexual|sapiosexual|autosexual|reciprosexual|akiosexual|skoliosexual|novosexual|autochorissexual|fictosexual|objectumsexual|omnisexual|polysexual|spectrosexual|dextrosexual|sinistrosexual|quoisexual|variromântico|pomorromântico|platônico|antiromântico|autonomantic)\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            results.add("VidaSexual:" + matcher.group());
        }

        for (String dado : results) {
            resultsFinal.append(",").append(dado);
        }
        resultsFinal = new StringBuilder(resultsFinal.toString().replaceFirst(",", ""));
        return resultsFinal.toString();
    }

}
