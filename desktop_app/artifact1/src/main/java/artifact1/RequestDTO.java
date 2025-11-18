package artifact1;

public class RequestDTO {
    public RequestDTO(String inputCode) {
		super();
		this.inputCode = inputCode;
	}

	private String inputCode;

    // Standard getters and setters
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }
}
