import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.util.Base64;  

public class JSONEncrypterDecrypter {

    public static String maskedFileName = "json-with-masked-attribute-ENCRYPTED.json";
    public static String unmaskedFileName = "json-with-unmasked-attribute-DECRYPTED.json";
    public static String attribute = "name";

    public static void main(String ... args) throws JSONException, IOException {

        File f = new File("file.json");
        if (f.exists()){
            InputStream is = new FileInputStream("file.json");
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            
            JSONObject json = new JSONObject(jsonTxt);       

            JSONObject jsonWithTHeMaskedAttribute = encryptField(json, attribute); 
            System.out.println("Below is the JSON with the " + attribute + " attribute MASKED");
            System.out.println(jsonWithTHeMaskedAttribute);


            InputStream iis = new FileInputStream(maskedFileName);
            String jsonWIthMaskedTxt = IOUtils.toString(iis, "UTF-8");
            JSONObject jsonWithTHeAttributeUnmasked = new JSONObject(jsonWIthMaskedTxt);   
            jsonWithTHeAttributeUnmasked = decryptField(jsonWithTHeAttributeUnmasked, attribute); 
            System.out.println("Below is the JSON with the " + attribute + " attribute UNMASKED");
            System.out.println(jsonWithTHeAttributeUnmasked);
        }
        else {
            System.out.println("No file named found with name file.json");
        }
       
    }

    public static JSONObject encryptField(JSONObject json, String attributeToMask) throws JSONException, IOException {
        
        
        Base64.Encoder encoder = Base64.getEncoder();        
        String value = json.getString(attributeToMask);
        String encodedValue = encoder.encodeToString(value.getBytes()); 

        JSONObject jsonWithMaskedAttribute = new JSONObject(json.toString());
        jsonWithMaskedAttribute.put(attributeToMask, encodedValue);

        // Writing to a file
        FileWriter file = new FileWriter(System.getProperty("user.dir") + "/" + maskedFileName);
        file.write(jsonWithMaskedAttribute.toString());
        file.flush();
        file.close();
    
        return jsonWithMaskedAttribute;
    }

    public static JSONObject decryptField(JSONObject json, String maskedAttribute) throws JSONException, IOException {
        Base64.Decoder decoder = Base64.getDecoder(); 
        String value = json.getString(maskedAttribute);
        String decodedValue = new String(decoder.decode(value));  
       
        JSONObject jsonWithAttributeUnmasked = new JSONObject(json.toString());
        jsonWithAttributeUnmasked.put(maskedAttribute, decodedValue);
       
         // Writing to a file
         FileWriter file = new FileWriter(System.getProperty("user.dir") + "/" + unmaskedFileName);
         file.write(jsonWithAttributeUnmasked.toString());
         file.flush();
         file.close();

        return jsonWithAttributeUnmasked;
    }

}

