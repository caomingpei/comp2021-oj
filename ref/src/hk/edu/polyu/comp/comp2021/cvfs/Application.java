package hk.edu.polyu.comp.comp2021.cvfs;

import hk.edu.polyu.comp.comp2021.cvfs.model.CVFS;
import hk.edu.polyu.comp.comp2021.cvfs.view.ConsoleView;
import hk.edu.polyu.comp.comp2021.cvfs.controller.CVFSController;



public class Application {

    public static void main(String[] args) {
        CVFS cvfs = new CVFS();
        ConsoleView view = new ConsoleView();
        CVFSController control = new CVFSController(cvfs, view);
        // Initialize and utilize the system
        view.start();
        while (true) {
            control.terminal();
        }
    }
}
