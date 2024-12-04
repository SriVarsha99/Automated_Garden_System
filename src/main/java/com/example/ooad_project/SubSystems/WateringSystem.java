package com.example.ooad_project.SubSystems;

import com.example.ooad_project.GardenGrid;
import com.example.ooad_project.Plant.Plant;
import com.example.ooad_project.ThreadUtils.EventBus;
import com.example.ooad_project.Events.RainEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.ooad_project.Events.SprinklerEvent;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class WateringSystem implements Runnable {
    private final AtomicBoolean rainRequested = new AtomicBoolean(false);
    private static final Logger logger = LogManager.getLogger("WateringSystemLogger");
    private int rainAmount = 0;
    private final GardenGrid gardenGrid;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Check every second
            } catch (InterruptedException e) {
                logger.error("Watering System interrupted");
                return; // Exit if interrupted
            }
        }
    }

    public WateringSystem() {
        logger.info("Watering System Initialized");
//        So our watering system is subscribed to the RainEvent
//        When a rain event is published, the watering system will handle it
        EventBus.subscribe("RainEvent", event -> handleRain((RainEvent) event));
        //EventBus.subscribe("SprinklerActivationEvent", event -> sprinkle());
//        Get the garden grid instance
//        This is the grid that holds all the plants
        this.gardenGrid = GardenGrid.getInstance();
    }

//    This method is called when a rain event is published
//    It waters all the plants in the garden grid
//    The amount of water each plant gets is the same
    private void handleRain(RainEvent event) {

        for (int i = 0; i < gardenGrid.getNumRows(); i++) {
            for (int j = 0; j < gardenGrid.getNumCols(); j++) {
                Plant plant = gardenGrid.getPlant(i, j);
                if (plant != null) {
                    plant.addWater(event.getAmount());


//                    Publish Event anytime rain adds water

//                    EventBus.publish(new PlantWateredEvent(plant, event.getAmount()));


                    logger.info("Watered {} at position ({}, {}) with {} water from rain", plant.getName(), i, j, event.getAmount());
                }
            }
        }

    }



//    This method is called when the sprinklers are activated
//    It waters all the plants in the garden grid
//    The amount of water each plant gets depends on how much water it needs
    private void sprinkle() {
//        System.out.println("Sprinklers activated!");
        logger.info("Sprinklers activated!");
        int counter = 0; // Counter to keep track of how many plants are watered

        for (int i = 0; i < gardenGrid.getNumRows(); i++) {
            for (int j = 0; j < gardenGrid.getNumCols(); j++) {
                Plant plant = gardenGrid.getPlant(i, j);
                if (plant != null && !plant.getIsWatered()) {
                    int waterNeeded = plant.getWaterRequirement() - plant.getCurrentWater();
                    if (waterNeeded > 0) {

//                        Publish water needed later

                        EventBus.publish("SprinklerEvent", new SprinklerEvent(plant.getRow(), plant.getCol(), waterNeeded));


                        plant.addWater(waterNeeded);
//                        Want to specify that the water is from sprinklers
                        logger.info("Sprinkled {} at position ({}, {}) with {} water from sprinklers", plant.getName(), i, j, waterNeeded);
                        counter++;
                    }else {
                        logger.info("{} at position ({}, {}) does not need water", plant.getName(), i, j);
                    }
                }
            }
        }

        logger.info("In total Sprinkled {} plants", counter);
//        gardenGrid.printAllPlantStats();
    }
}


