package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.NutritionalValues;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesStringDTO;
import com.puccampinas.backendp5noname.repositories.NutritionalValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutritionalValuesService {


    @Autowired
    private NutritionalValuesRepository nutritionalValuesRepository;



    public NutritionalValues saveNutritionalValues(NutritionalValuesStringDTO data) {
        return nutritionalValuesRepository.save(new NutritionalValues(data));
    }



}
