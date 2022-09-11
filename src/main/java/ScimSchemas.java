import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.charon3.core.attributes.SCIMCustomAttribute;
import org.wso2.charon3.core.config.SCIMCustomSchemaExtensionBuilder;
import org.wso2.charon3.core.config.SCIMUserSchemaExtensionBuilder;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.schema.AttributeSchema;
import org.wso2.charon3.core.schema.SCIMResourceSchemaManager;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScimSchemas {

    private static final Log LOG =  LogFactory.getLog(ScimSchemas.class);

    public static void main(String[] args) {

        LOG.info("====Attributes of default user schema===");
        // Print default user schema attributes.
        SCIMResourceTypeSchema userResourceSchema = SCIMResourceSchemaManager.getInstance().getUserResourceSchema();
        userResourceSchema.getAttributesList().stream().forEach(e->System.out.print(e.getURI() + "\n"));

        try {
            // Option 1
            LOG.info("====Attributes of extended schema using file===");
            // Change the absolute path to the attribute definition file in your setup
            String schemaFilePath = "/home/anuradhak/schema-extension/schema-extension/src/main/resources/custom-schema";
            SCIMUserSchemaExtensionBuilder.getInstance().buildUserSchemaExtension(schemaFilePath);
            AttributeSchema extensionSchema = SCIMUserSchemaExtensionBuilder.getInstance().getExtensionSchema();
            extensionSchema.getSubAttributeSchemas().stream().forEach(e->System.out.print(e.getURI() + "\n"));


            // Option 2
            SCIMCustomSchemaExtensionBuilder.getInstance().setURI("urn:scim:custom:extended:schemas");
            LOG.info("====Attributes of extended schema using custom attributes===");
            // Sub attribute of the schema.
            Map<String, String> subAttributeProperties = new HashMap<>();
            subAttributeProperties.put("attributeURI","urn:scim:custom:extended:schemas:vehicleNo");
            subAttributeProperties.put("attributeName","vehicleNo");
            subAttributeProperties.put("dataType","string");
            subAttributeProperties.put("multiValued","false");
            subAttributeProperties.put("description","User's vehicle number");
            subAttributeProperties.put("required","false");
            subAttributeProperties.put("caseExact","false");
            subAttributeProperties.put("mutability","readWrite");
            subAttributeProperties.put("returned","default");
            subAttributeProperties.put("uniqueness","none");
            subAttributeProperties.put("canonicalValues","[]");
            SCIMCustomAttribute scimCustomAttribute = new SCIMCustomAttribute();
            scimCustomAttribute.setProperties(subAttributeProperties);

            // Schema object.
            Map<String, String> schemaObjectProperties = new HashMap<>();
            schemaObjectProperties.put("attributeURI","urn:scim:custom:extended:schemas");
            schemaObjectProperties.put("attributeName","urn:scim:custom:extended:schemas");
            schemaObjectProperties.put("dataType","complex");
            schemaObjectProperties.put("multiValued","false");
            schemaObjectProperties.put("description","Extended custom schema");
            schemaObjectProperties.put("required","false");
            schemaObjectProperties.put("caseExact","false");
            schemaObjectProperties.put("mutability","readWrite");
            schemaObjectProperties.put("returned","default");
            schemaObjectProperties.put("uniqueness","none");
            schemaObjectProperties.put("subAttributes","vehicleNo");
            schemaObjectProperties.put("canonicalValues","[]");
            SCIMCustomAttribute schemaAttribute = new SCIMCustomAttribute();
            schemaAttribute.setProperties(schemaObjectProperties);

            List<SCIMCustomAttribute> attributeList = new ArrayList<>();
            attributeList.add(scimCustomAttribute);
            attributeList.add(schemaAttribute);
            AttributeSchema customExtendedSchema = SCIMCustomSchemaExtensionBuilder.getInstance().buildUserCustomSchemaExtension(attributeList);
            customExtendedSchema.getSubAttributeSchemas().stream().forEach(e->System.out.print(e.getURI() + "\n"));
        } catch (CharonException e) {
            throw new RuntimeException(e);
        } catch (InternalErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
