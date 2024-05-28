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


    public void inventor(String domain){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UsuarioModel usuarioModel = usuarioRepository.findByUsername(username);
        Optional<UsuarioModel> usuario = usuarioRepository.findById(usuarioModel.getId());

        String finalDomain = domain.replaceFirst("^(https?://)?", "");
        DominioModel dominioIdExist = dominioRepository.findByDominio(finalDomain)
                .orElseGet(() -> {
                    DominioModel newdomain = new DominioModel();
                    newdomain.setDominio(finalDomain);
                    return dominioRepository.save(newdomain);
                });
        // Pega o id de referencia para referenciar o dado
        DominioModel domainReference = dominioRepository.getById(dominioIdExist.getId());

        List<String> dataSensetive = InvetorDataSensetive(domain);
        for (String dado : dataSensetive ){
            if (usuario.isPresent()) {

                String[] dadosEmDado = dado.split("\\|");

                String[] finalDadosEmDado = dadosEmDado;
                PathLocationModel pathIdExist = pathLocationRepository.findByPathLocation(usuario.get().getId()+"|"+dadosEmDado[0])
                        .orElseGet(() -> {
                            PathLocationModel newpath = new PathLocationModel();
                            newpath.setPathLocation(usuario.get().getId()+"|"+finalDadosEmDado[0]);
                            newpath.setTipoDeArquivo(finalDadosEmDado[2]);
                            newpath.setProcessamento(finalDadosEmDado[3]);
                            return  pathLocationRepository.save(newpath);
                        });

                PathLocationModel path = pathLocationRepository.getById(pathIdExist.getId());

                dadosEmDado = dadosEmDado[1].split(",");

                for(String sensetive : dadosEmDado){
                    SensetiveDataModel sensitiveDataModel = new SensetiveDataModel();

                    try{
                        //System.out.println(path);
                        sensitiveDataModel.setPathLocation(path);

                        // atualizar logica ao inserir modelo real de InvetorDataSensetive()
                        String[] type = sensetive.split(":");
                        sensitiveDataModel.setTipo(type[0]);

                        // seta o dado
                        sensitiveDataModel.setSensitive(type[1]);

                        // seta o dominio de referência
                        sensitiveDataModel.setDominio(dominioIdExist);

                        // aplica o usuario de referencia
                        sensitiveDataModel.setUsuario(usuario.get()); // ## issue consertar referencia de FK
                        sensetiveDataRepository.save(sensitiveDataModel);
                    }catch (Exception e){
                        System.out.println("ERRO-------------------MARDITO: "+e.getMessage());
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

        // dadosColetados.add("https://teste.com/links0/arquivo.pdf|CPF:119.920.479-76,CPF:123.456.789-12,EMAIL:tewste@test.com,EMAIL:tewste@test.com");
        // dadosColetados.add("https://teste.com/links02/arquivo2.pdf|CPF:219.920.479-76,CPF:223.456.789-12,EMAIL:tewste@test.com,EMAIL:tewste@test.com");
        // dadosColetados.add("C:\\Users\\Nicholas\\Desktop\\Dados Para Análise\\Camera Roll\\Caps.png|e-mail:ninicoco@ggg.co,e-mail:aspammer@website.com,e-mail:edududu@edu.if,CPF:119.920.749-76,CPF:297.251.780-68,CPF:119.920.749-76,|png|OCR");


        return listadepdf;
    }
}
