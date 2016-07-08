package telegrama.main;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Chat {
	@JsonProperty(ID_FIELD)
	private Long id; ///< Unique identifier for this chat, not exciding 1e13 by absolute value
    @JsonProperty(TYPE_FIELD)
    private String type; ///< Type of the chat, one of “private”, “group” or “channel”
    @JsonProperty(TITLE_FIELD)
    private String title; ///< Optional. Title of the chat, only for channels and group chat
    @JsonProperty(FIRSTNAME_FIELD)
    private String firstName; ///< Optional. Username of the chat, only for private chats and channels if available
    @JsonProperty(LASTNAME_FIELD)
    private String lastName; ///< Optional. Interlocutor's first name for private chats
    @JsonProperty(USERNAME_FIELD)
    private String userName; ///< Optional. Interlocutor's last name for private chats
	
	public Chat(JSONObject json){
		parse(json);
	}
	
	private void parse(JSONObject jsonObject) {
		this.id = jsonObject.getLong(ID_FIELD);
        this.type = jsonObject.getString(TYPE_FIELD);
        if (jsonObject.has(TITLE_FIELD)) {
            this.title = jsonObject.getString(TITLE_FIELD);
        }
        if (jsonObject.has(FIRSTNAME_FIELD)) {
            this.firstName = jsonObject.getString(FIRSTNAME_FIELD);
        }
        if (jsonObject.has(LASTNAME_FIELD)) {
            this.lastName = jsonObject.getString(LASTNAME_FIELD);
        }
        if (jsonObject.has(USERNAME_FIELD)) {
            this.userName = jsonObject.getString(USERNAME_FIELD);
        }
	}

	@Override
    public String toString() {
		 return "Chat{" +
	                "id=" + id +
	                ", type='" + type + '\'' +
	                ", title='" + title + '\'' +
	                ", firstName='" + firstName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", userName='" + userName + '\'' +
	                '}';
    }
	private static final String ID_FIELD = "id";
    private static final String TYPE_FIELD = "type";
    private static final String TITLE_FIELD = "title";
    private static final String FIRSTNAME_FIELD = "first_name";
    private static final String LASTNAME_FIELD = "last_name";
    private static final String USERNAME_FIELD = "username";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    
}
