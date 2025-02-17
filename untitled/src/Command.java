import java.util.ArrayList;
import java.util.List;

class Command {
    private String commandName;
    public String getCommandName() {
        return commandName;
    }
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

}

class ListaPizze {

    List<String> listaPizza = new ArrayList<String>();

    public List<String> getListaPizza() {
        return listaPizza;
    }

    public void setListaPizza(List<String> listaPizza) {
        this.listaPizza = listaPizza;
    }
}