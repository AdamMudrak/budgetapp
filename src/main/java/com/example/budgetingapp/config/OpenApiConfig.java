package com.example.budgetingapp.config;

import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.AccountControllerConstants.ACCOUNT_API_NAME;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.CATEGORY_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.CategoryControllerConstants.CATEGORY_API_NAME;
import static com.example.budgetingapp.constants.controllers.transactions.TransactionsCommonConstants.TRANSACTION_API_DESCRIPTION;
import static com.example.budgetingapp.constants.controllers.transactions.TransactionsCommonConstants.TRANSACTION_API_NAME;
import static com.example.budgetingapp.constants.security.SecurityConstants.SERVER_PATH;

import com.example.budgetingapp.constants.config.ConfigConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value(SERVER_PATH) private String serverPath;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .addTagsItem(new Tag().name(TRANSACTION_API_NAME)
                        .description(TRANSACTION_API_DESCRIPTION))
                .addTagsItem(new Tag().name(CATEGORY_API_NAME)
                        .description(CATEGORY_API_DESCRIPTION))
                .addTagsItem(new Tag().name(ACCOUNT_API_NAME)
                        .description(ACCOUNT_API_DESCRIPTION))
                .components(new Components()
                        .addSecuritySchemes(ConfigConstants.SECURITY_SCHEME_KEY,
                            new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(ConfigConstants.SECURITY_SCHEME)
                                .bearerFormat(ConfigConstants.BEARER_FORMAT)))
                                .addSecurityItem(new SecurityRequirement()
                                .addList(ConfigConstants.SECURITY_SCHEME_KEY))
                                .addServersItem(new Server().url(serverPath));
    }
}
