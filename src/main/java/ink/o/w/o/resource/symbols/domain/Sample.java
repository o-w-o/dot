package ink.o.w.o.resource.symbols.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Data
@Document(indexName = "symbol", type = "sample")
public class Sample {
    @Id
    String id;
    String name;
    String message;
    String type;
}
