package org.example.helpers;

import com.networknt.schema.Error;
import com.networknt.schema.InputFormat;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.List;

public class SchemaValidationHelper {

    public static void validate(Response response, String schemaPath) {
        SchemaRegistry registry = SchemaRegistry.withDefaultDialectId(
                "http://json-schema.org/draft-04/schema#"
        );
        Schema schema = registry.getSchema(
                SchemaLocation.of("classpath:" + schemaPath)
        );
        List<Error> errors =
                schema.validate(response.asString(), InputFormat.JSON);
        Assert.assertTrue(
                errors.isEmpty(),
                "Schema validation failed: " + errors
        );
    }
}
