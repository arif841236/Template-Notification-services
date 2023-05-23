package com.template.common.config;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.stream.StreamSupport;

@Component
public   class LosObservationHandler implements io.micrometer.observation.ObservationHandler<Observation.Context> {


       private static final Logger log = LoggerFactory.getLogger(LosObservationHandler.class);


       @Override
       public void onStart(Observation.Context context) {
           log.info("Before running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
       }


       @Override
       public void onStop(Observation.Context context) {
           log.info("After running the observation for context [{}], userType [{}]", context.getName(), getUserTypeFromContext(context));
       }


       @Override
       public boolean supportsContext(@NotNull Observation.Context context) {
           return true;
       }


       private String getUserTypeFromContext(Observation.Context context) {
           return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                   /*.filter(keyValue -> "api-name".equals(keyValue.getKey()))*/
                   .map(KeyValue::getValue)
                   .findFirst()
                   .orElse("UNKNOWN");
       }
   }
