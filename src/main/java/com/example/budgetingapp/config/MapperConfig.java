package com.example.budgetingapp.config;

import com.example.budgetingapp.constants.config.ConfigConstants;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;

@org.mapstruct.MapperConfig(
        componentModel = ConfigConstants.COMPONENT_MODEL,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public class MapperConfig {
}
