package fr.mgargadennec.blossom.ui.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.ui.stereotype.BlossomApiController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomApiController
public class HomeApiController {
    @GetMapping
    public List<Object> root() {
        return Lists.newArrayList();
    }
}
