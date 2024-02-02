package com.nicholastcc.datainventor.service;

import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.model.SensetiveDataModel;
import com.nicholastcc.datainventor.repository.DominioRepository;
import com.nicholastcc.datainventor.repository.SensetiveDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void inventor(String domain){

        List<String> dataSensetive = InvetorDataSensetive(domain);

        String finalDomain = domain.replaceFirst("^(https?://)?", "");

        DominioModel dominioIdExist = dominioRepository.findByDominio(finalDomain)
                .orElseGet(() -> {
                    DominioModel newdomain = new DominioModel();
                    newdomain.setDominio(finalDomain);
                    return dominioRepository.save(newdomain);
                });

        Optional<DominioModel> dominio = dominioRepository.findByDominio(domain);

        // Pega o id de referencia para referenciar o dado
        Long dominioID = dominioIdExist.getId() != null ? dominioIdExist.getId() : dominio.get().getId() ;

        for (String dado : dataSensetive ){
            SensetiveDataModel sensitiveDataModel = new SensetiveDataModel();

            DominioModel domainReference = new DominioModel();
            domainReference.setId(dominioID);

            sensitiveDataModel.setDominio(domainReference);
            sensitiveDataModel.setSensitive(dado);

            sensetiveDataRepository.save(sensitiveDataModel);
        }
    }

    private List<String> InvetorDataSensetive(String domain) {

        List<String> dadosColetados = new ArrayList<>();

        dadosColetados.add("Teste 2");
        dadosColetados.add("Teste 3");


        return dadosColetados;
    }

}
