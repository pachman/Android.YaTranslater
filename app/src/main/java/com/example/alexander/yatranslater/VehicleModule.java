package com.example.alexander.yatranslater;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class VehicleModule {

    @Provides
    @Singleton
    Motor provideMotor(){
        return new Motor();
    }

    @Provides @Singleton
    Vehicle provideVehicle(){
        return new Vehicle(new Motor());
    }
}
