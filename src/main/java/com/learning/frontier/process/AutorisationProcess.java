package com.learning.frontier.process;

import com.learning.frontier.model.transport.NewAutorisationInput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AutorisationProcess {

    private ArrayList<NewAutorisationInput> autorisations;

    public AutorisationProcess() {
        this.autorisations = new ArrayList<>();
    }

    public void addNewAutorisation(NewAutorisationInput newAutorisationInput) {
        int index = this.autorisations.indexOf(newAutorisationInput);
        if(index != -1) {
            this.autorisations.add(index, newAutorisationInput);
        } else {
            this.autorisations.add(newAutorisationInput);
        }
    }

    public ArrayList<NewAutorisationInput> getAutorisations() {
        return autorisations;
    }
}
