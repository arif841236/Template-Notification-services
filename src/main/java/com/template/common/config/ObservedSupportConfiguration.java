//package com.template.common.config;
//
//import io.micrometer.observation.ObservationRegistry;
//import io.micrometer.observation.aop.ObservedAspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//@Configuration(proxyBeanMethods = false)
//public class ObservedSupportConfiguration {
//
//
//    // To have the @Observed support we need to register this aspect
//	@Bean
//	ObservationRegistry observationRegistry(@Autowired LosObservationHandler observationHandler)
//	{
//		ObservationRegistry observationRegistry = ObservationRegistry.create();
//
//		observationRegistry
//		  .observationConfig()
//		  .observationHandler(observationHandler);
//
//
//		return observationRegistry;
//	}
//    @Bean
//    @DependsOn("observationRegistry")
//    ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
//        return new ObservedAspect(observationRegistry);
//    }
//}
//
