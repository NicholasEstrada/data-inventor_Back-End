package com.nicholastcc.datainventor.service;

import com.nicholastcc.datainventor.OCRMaven.webVersion.InputDomain;
import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.model.PathLocationModel;
import com.nicholastcc.datainventor.model.SensetiveDataModel;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import com.nicholastcc.datainventor.repository.DominioRepository;
import com.nicholastcc.datainventor.repository.PathLocationRepository;
import com.nicholastcc.datainventor.repository.SensetiveDataRepository;
import com.nicholastcc.datainventor.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class InventorService {

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private SensetiveDataRepository sensetiveDataRepository;

    @Autowired
    private PathLocationRepository pathLocationRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10); // Limite de 10 threads

    @Transactional
    public void inventor(String domain) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UsuarioModel usuarioModel = usuarioRepository.findByUsername(username);
        Optional<UsuarioModel> usuario = usuarioRepository.findById(usuarioModel.getId());

        String finalDomain = domain.replaceFirst("^(https?://)?", "");
        DominioModel dominioIdExist = dominioRepository.findByDominio(finalDomain)
                .orElseGet(() -> {
                    DominioModel newdomain = new DominioModel();
                    newdomain.setDominio(finalDomain);
                    newdomain.setUsuario(usuarioModel); // Certifique-se de que o domínio seja atribuído ao usuário
                    return dominioRepository.save(newdomain);
                });

        // Pega o id de referencia para referenciar o dado
        DominioModel domainReference = dominioRepository.getById(dominioIdExist.getId());

        List<String> dataSensetive = InvetorDataSensetive(domain);
        for (String dado : dataSensetive) {
            if (usuario.isPresent()) {
                String[] dadosEmDado = dado.split("\\|");

                String[] finalDadosEmDado = dadosEmDado;
                PathLocationModel pathIdExist = pathLocationRepository.findByPathLocation(usuario.get().getId() + "|" + dadosEmDado[0])
                        .orElseGet(() -> {
                            PathLocationModel newpath = new PathLocationModel();
                            newpath.setPathLocation(usuario.get().getId() + "|" + finalDadosEmDado[0]);
                            newpath.setTipoDeArquivo(finalDadosEmDado[2]);
                            newpath.setProcessamento(finalDadosEmDado[3]);
                            return pathLocationRepository.save(newpath);
                        });

                PathLocationModel path = pathLocationRepository.getById(pathIdExist.getId());

                dadosEmDado = dadosEmDado[1].split(",");

                for (String sensetive : dadosEmDado) {
                    String[] type = sensetive.split(":");
                    String tipo = type[0];
                    String sensitive = type[1];

                    // Verifique se o dado já existe
                    List<SensetiveDataModel> existingData = sensetiveDataRepository.findByPathLocationAndSensitiveAndUsuario(path, sensitive, usuario.get());
                    if (existingData.isEmpty()) {
                        SensetiveDataModel sensitiveDataModel = new SensetiveDataModel();
                        try {
                            sensitiveDataModel.setPathLocation(path);
                            sensitiveDataModel.setTipo(tipo);
                            sensitiveDataModel.setSensitive(sensitive);
                            sensitiveDataModel.setDominio(dominioIdExist);
                            sensitiveDataModel.setUsuario(usuario.get()); // Aplica o usuário de referência
                            sensetiveDataRepository.save(sensitiveDataModel);
                        } catch (Exception e) {
                            System.out.println("ERRO-------------------MARDITO: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private List<String> InvetorDataSensetive(String domain) {
        List<String> listadepdf = new ArrayList<>();

        try {
            InputDomain inputDomain = new InputDomain(domain);

            listadepdf = inputDomain.InvetorDataSensetive(domain, 0);
            threadPool.shutdown();

            System.out.println("Total de arquivos encontrados: " + listadepdf.size());
            for (String content : listadepdf) {
                System.out.println("Arquivo encontrado: " + content);
            }
        } catch (UnsupportedEncodingException | InterruptedException e) {
            System.out.println("Erro no processamento: " + e.getMessage());
        }

        /*listadepdf.add("https://saobentodosul.ifc.edu.br/wp-content/blogs.dir/19/files/sites/19/2022/12/Guia-do-Estudante-2020_compressed.pdf|e-mail:suporte.sbs@ifc.edu.br,e-mail:cti.sbs@ifc.edu.br,e-mail:gabinete.sbs@ifc.edu.br,e-mail:cecom.sbs@ifc.edu.br,e-mail:dde.sbs@ifc.edu.br,e-mail:registroacademico.sbs@ifc.edu.br,e-mail:atendimentoestudantil.sbs@ifc.edu.br,e-mail:cge.sbs@ifc.edu.br,e-mail:automacao.tec.sbs@ifc.edu.br,e-mail:informatica.tec.sbs@ifc.edu.br,e-mail:seguranca.tec.sbs@ifc.edu.br,e-mail:computacao.grad.sbs@ifc.edu.br,e-mail:automacao.grad.sbs@ifc.edu.br,e-mail:pesquisa.sbs@ifc.edu.br,e-mail:extensao.sbs@ifc.edu.br,e-mail:napne.sbs@ifc.edu.br,e-mail:nupe.sbs@ifc.edu.br,e-mail:biblioteca.sbs@ifc.edu.br,e-mail:dap.sbs@ifc.edu.br,,|pdf|PDFText");
        listadepdf.add("https://saobentodosul.ifc.edu.br/wp-content/blogs.dir/19/files/sites/19/2023/06/IFC-SBS-Uniforme.pdf|,,|pdf|PDFText");
        listadepdf.add("https://acessoainformacao.ifc.edu.br/wp-content/uploads/sites/26/2022/10/Portaria-Normativa-005-2017-de-12-04-17-Comite-de-Gestao-de-Risco-Consolidada-sem-texto-tachado.pdf|,,|pdf|PDFText");
        listadepdf.add("https://governanca.ifc.edu.br/wp-content/uploads/sites/81/2024/04/Memorando-Eletronico-SIPAC-Convocacao-2a-reuniao.pdf|,,|pdf|PDFText");
        listadepdf.add("https://governanca.ifc.edu.br/wp-content/uploads/sites/81/2024/04/Memorando-Eletronico-SIPAC-Convocacao-1a-reuniao.pdf|,,|pdf|PDFText");
        listadepdf.add("https://governanca.ifc.edu.br/wp-content/uploads/sites/81/2024/04/Sumula-da-1a-Reuniao-do-Comite-de-Governanca-Riscos-e-Controles.pdf|,,|pdf|PDFText");
        listadepdf.add("https://governanca.ifc.edu.br/wp-content/uploads/sites/81/2024/04/Memorando-Eletronico-SIPAC-Convocacao-3a-reuniao.pdf|,,|pdf|PDFText");
        listadepdf.add("https://governanca.ifc.edu.br/wp-content/uploads/sites/81/2024/04/Sumula-da-2a-Reuniao-do-Comite-de-Governanca-Riscos-e-Controles.pdf|,,|pdf|PDFText");
        listadepdf.add("https://governanca.ifc.edu.br/wp-content/uploads/sites/81/2024/04/Sumula-da-3a-Reuniao-do-Comite-de-Governanca-Riscos-e-Controles.pdf|,,|pdf|PDFText");
        listadepdf.add("https://luzerna.ifc.edu.br/wp-content/uploads/2023/12/REGULAMENTO-.pdf|e-mail:brigada.luzerna@ifc.edu.br,,|pdf|PDFText");
        listadepdf.add("http://www.camboriu.ifc.edu.br/wp-content/uploads/2021/06/Organiza%C3%A7%C3%A3o-Did%C3%A1tica-do-Cursos-do-IFC-diagramada.pdf|,,|pdf|PDFText");*/

        return listadepdf;
    }
}
