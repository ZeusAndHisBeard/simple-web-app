package com.zeus.examples.simplewebapp.controller;

import com.zeus.examples.simplewebapp.db.FoodItem;
import com.zeus.examples.simplewebapp.service.FridgeService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fridges")
public class FridgeController {
    @Autowired
    FridgeService fridgeService;

    @GetMapping(value = "/food/{foodItemId}")
    @Timed
    public ResponseEntity<FoodItem> getFoodItem(@PathVariable UUID foodItemId) {
        log.info("checking to see if food: " + foodItemId.toString() + " is in the fridges...");
        Optional<FoodItem> foodItem = fridgeService.getFood(foodItemId);
        if (foodItem.isPresent()){
            log.info("foodItem: " + foodItemId.toString() + " found.");
            return ResponseEntity.ok(foodItem.get());
        }
        log.info("foodItem: " + foodItemId.toString() + " is not in any fridge.");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> postFood(@RequestBody FoodItem foodItem) {
        log.info("putting " + foodItem.getFoodType().toString() + " into fridge: " + foodItem.getFridgeName() + "...");
        fridgeService.storeFood(foodItem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/food/{foodItemId}")
    @Timed
    public ResponseEntity<Void> deleteFood(@PathVariable UUID foodItemId) {
        if (getFoodItem(foodItemId).getStatusCode().equals(HttpStatus.OK)){
            fridgeService.removeFood(foodItemId);
            log.info("removing foodItem "+ foodItemId.toString()+ " from the fridge.");
        } else {
            log.info("nothing to delete: unable to find foodItem "+ foodItemId.toString()+ " in any fridge.");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
