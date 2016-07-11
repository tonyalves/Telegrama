package telegrama.main;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaoCommand implements ICommand{
	private static final int KNOWN_LATE = 2;
	private List<String> pessoas = Arrays.asList("Afonso", 
			"Jr", "Samuel", "JP", "Glauber",
			"Brendo", "Milhome", "Alves", "Quevedo");
	 

	private Map<Integer, String> diaPessoa = new HashMap<>();
	
	public PaoCommand() {
		generateDays();
	}
	
	public void generateDays(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
		System.out.println(dayOfYear);
		for (int i = 1, j = 0; i < 366; i++) {
			diaPessoa.put(i, pessoas.get(j));
			j++;
			if(j > 8) j = 0;
		}
	}
	
	public String verify(int offset){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek > 1 && dayOfWeek < 7){
			Calendar c1 = Calendar.getInstance();
			c1.setTime(new Date());
			int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
			return diaPessoa.get((dayOfYear - KNOWN_LATE) + offset);			
		}
		
		return "Ninguém";
	}
	
	@Override
	public String doAction(Update update) {
		String offset = null;
		try{
			offset = update.getMessage().getText().split(" ")[1];
		}catch(Exception e){
		}
		
		if(offset != null)
			return verify(Integer.valueOf(offset));
		return verify(0);
	}

}
