package com.example.ooad_project.Parasite.Children;

import com.example.ooad_project.Events.ParasiteDamageEvent;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Plant.Plant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;


// Aphids class with a 10% miss chance
public class Aphids extends Parasite {
    private static final double MISS_CHANCE = 0.10;  // 10% chance to miss
    private Random random = new Random();
    private static final Logger logger = LogManager.getLogger("PesticideSystemLogger");

    public Aphids(String name, int damage, String imageName, ArrayList<String> affectedPlants) {
        super(name, damage, imageName, affectedPlants);
    }

    @Override
    public void affectPlant(Plant plant) {
        if (random.nextDouble() >= MISS_CHANCE) {
            // If not missed, apply the damage
            int oldHealth = plant.getCurrentHealth();
            int newHealth = Math.max(0, plant.getCurrentHealth() - this.getDamage());
            super.publishDamageEvent(new ParasiteDamageEvent(plant.getRow(),plant.getCol(), this.getDamage()));

            plant.setCurrentHealth(newHealth);
            logger.info("Aphid has successfully damaged the plant {} at position ({}, {}). Old health: {}. New health: {}",
                    plant.getName(), plant.getRow(), plant.getCol(), oldHealth, newHealth);

        } else {
            // If missed, do nothing
            logger.info("Aphid attempted to damage the plant {} at position ({}, {}) but missed.",
                    plant.getName(), plant.getRow(), plant.getCol());
        }
    }
}