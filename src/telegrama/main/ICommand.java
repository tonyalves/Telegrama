package telegrama.main;

public interface ICommand {
	String doAction(Update update);
}
