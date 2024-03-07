package com.nicholastcc.datainventor.service;

import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.model.SensetiveDataModel;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import com.nicholastcc.datainventor.repository.DominioRepository;
import com.nicholastcc.datainventor.repository.SensetiveDataRepository;
import com.nicholastcc.datainventor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventorService {

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private SensetiveDataRepository sensetiveDataRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void inventor(String domain){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<String> dataSensetive = InvetorDataSensetive(domain);

        String finalDomain = domain.replaceFirst("^(https?://)?", "");

        DominioModel dominioIdExist = dominioRepository.findByDominio(finalDomain)
                .orElseGet(() -> {
                    DominioModel newdomain = new DominioModel();
                    newdomain.setDominio(finalDomain);
                    return dominioRepository.save(newdomain);
                });

        // Pega o id de referencia para referenciar o dado
        DominioModel domainReference = dominioRepository.getById(dominioIdExist.getId());

        UsuarioModel usuarioModel = usuarioRepository.findByUsername(username);
        Optional<UsuarioModel> usuario = usuarioRepository.findById(usuarioModel.getId());


        for (String dado : dataSensetive ){

            if (usuario != null) {
                SensetiveDataModel sensitiveDataModel = new SensetiveDataModel();

                // atualizar logica ao inserir modelo real de InvetorDataSensetive()
                sensitiveDataModel.setPathLocation("https://tester.com/arquivo.pdf");

                // atualizar logica ao inserir modelo real de InvetorDataSensetive()
                sensitiveDataModel.setTipo("PDF");

                // seta o dominio de referência
                sensitiveDataModel.setDominio(domainReference);

                // seta o dado
                sensitiveDataModel.setSensitive(dado);

                // aplica o usuario de referencia
                sensitiveDataModel.setUsuario(usuario.get()); // ## issue consertar referencia de FK

                sensetiveDataRepository.save(sensitiveDataModel);
            }

        }
    }

    private List<String> InvetorDataSensetive(String domain) {

        List<String> dadosColetados = new ArrayList<>();

        dadosColetados.add("Teste 2");
        dadosColetados.add("Teste 3");


        return dadosColetados;
    }
}
