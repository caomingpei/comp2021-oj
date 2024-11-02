package hk.edu.polyu.comp.comp2021.cvfs;

import hk.edu.polyu.comp.comp2021.cvfs.model.CVFS;
import hk.edu.polyu.comp.comp2021.cvfs.view.ConsoleView;

public class Application {

    public static void main(String[] args) {
        CVFS cvfs = new CVFS();
        // Initialize and utilize the system
        ConsoleView consoleView = new ConsoleView();
        consoleView.welcome();
    }
}
