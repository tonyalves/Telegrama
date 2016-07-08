package telegrama.main;

import java.io.IOException;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class Update {
    private static final String UPDATEID_FIELD = "update_id";
    private static final String MESSAGE_FIELD = "message";
   
    private static final String EDITEDMESSAGE_FIELD = "edited_message";
    @JsonProperty(UPDATEID_FIELD)
    private Integer updateId;
    @JsonProperty(MESSAGE_FIELD)
    private Message message; ///< Optional. New incoming message of any kind — text, photo, sticker, etc.
    @JsonProperty(EDITEDMESSAGE_FIELD)
    private Message editedMessage; ///< Optional. New version of a message that is known to the bot and was edited

    public Update() {
        super();
    }

    public Update(JSONObject jsonObject) {
        super();
        
        this.updateId = jsonObject.getInt(UPDATEID_FIELD);
        if (jsonObject.has(MESSAGE_FIELD)) {
            this.message = new Message(jsonObject.getJSONObject(MESSAGE_FIELD));
        }
      
        if (jsonObject.has(EDITEDMESSAGE_FIELD)){
            editedMessage = new Message(jsonObject.getJSONObject(EDITEDMESSAGE_FIELD));
        }
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public Message getMessage() {
        return message;
    }

    public Message getEditedMessage() {
        return editedMessage;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public boolean hasEditedMessage() {
        return editedMessage != null;
    }

    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(UPDATEID_FIELD, updateId);
        if (message != null) {
            gen.writeObjectField(MESSAGE_FIELD, message);
        }
        if (editedMessage != null) {
            gen.writeObjectField(EDITEDMESSAGE_FIELD, editedMessage);
        }
        gen.writeEndObject();
        gen.flush();
    }

    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(gen, serializers);
    }

    @Override
    public String toString() {
        return "Update{" +
                "updateId=" + updateId +
                ", message=" + message +
                ", editedMessage=" + editedMessage +
                '}';
    }
}
