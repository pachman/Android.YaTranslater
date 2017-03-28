package com.example.alexander.yatranslater;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {VehicleModule.class})
public interface VehicleComponent {

    Vehicle provideVehicle();

}
