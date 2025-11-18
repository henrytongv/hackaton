package artifact1;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData {
	public List<BalanceItem> balances;
}
