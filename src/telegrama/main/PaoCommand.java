package telegrama.main;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaoCommand implements ICommand{
	
	private List<String> pessoas = Arrays.asList("Alves", "Afonso", "Diego");
	
	private Map<Integer, String> diaPessoa = new HashMap<>();
	
	public PaoCommand() {
		generateDays();
	}
	
	public void generateDays(){
		for (int i = 1, j = 0; i < 366; i++) {
			diaPessoa.put(i, pessoas.get(j));
			j++;
			if(j > 2) j = 0;
		}
	}
	
	public String verify(){
	
		int dayOfWeek = Calendar.DAY_OF_WEEK;
		if(dayOfWeek > 1 && dayOfWeek < 7){
			return diaPessoa.get(Calendar.DAY_OF_YEAR + 1);			
		}
		
		return diaPessoa.get(Calendar.DAY_OF_YEAR);
	}
	@Override
	public String doAction(Update update) {
		return verify();
	}

}
