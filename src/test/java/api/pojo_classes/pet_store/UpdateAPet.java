package api.pojo_classes.pet_store;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class UpdateAPet {
    /**
         * {
         * "id": 5,
         * "category": {
         * "id": 4,
         * "name": "fish"
         * },
         * "name": "nemo",
         * "photoUrls": [
         * "nemoURL"
         * ],
         * "tags": [
         * {
         * "id": 50,
         * "name": "red"
         * }
         * ],
         * "status": "available"
         * }
         */

        private int id;
        private Category category;
        private String name;
        private List<String> photoUrls;
        private List<Tags> tags;
        private String status;
}

