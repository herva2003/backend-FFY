package com.puccampinas.backendp5noname.controllers.auth;


import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredient")
public class NutrientController {
    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/")
    public ResponseEntity<List<IngredientVO>> getAllNutrients() {
        List<IngredientVO> ingredients = ingredientService.allIngredients()
                .stream()
                .map(IngredientVO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }
}
