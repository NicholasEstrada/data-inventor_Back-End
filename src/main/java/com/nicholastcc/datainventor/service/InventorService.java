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

        return listadepdf;
    }
}
