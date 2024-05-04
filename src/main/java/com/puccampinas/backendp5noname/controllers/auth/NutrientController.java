package com.puccampinas.backendp5noname.controllers.auth;


import com.puccampinas.backendp5noname.domain.Nutrient;
import com.puccampinas.backendp5noname.services.NutrientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nutrient")
public class NutrientController {
    @Autowired
    private NutrientService nutrientService;


    //soon
//    @GetMapping("/")
//    public ResponseEntity<List<Nutrient>> getAllNutrients() {
//        return new ResponseEntity<List<Nutrient>>(nutrientService.allNutrients(),HttpStatus.OK);
//    }
}
