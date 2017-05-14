package fr.mgargadennec.blossom.autoconfigure.ui.api;

import fr.mgargadennec.blossom.ui.api.HomeApiController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(HomeApiController.class)
public class WebApiAutoConfiguration {

    @Bean
    public HomeApiController homeApiController() {
        return new HomeApiController();
    }

}
