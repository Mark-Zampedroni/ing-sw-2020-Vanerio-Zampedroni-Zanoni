package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.rules.*;
import it.polimi.ingsw.rules.gods.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Playable gods, each {@link it.polimi.ingsw.model.player.Player player} can select one
 */
public enum Gods implements Serializable {
    APOLLO("a") { public GodRules createRules() { return new ApolloRules(); } },
    ARTEMIS("a") { public GodRules createRules() { return new ArtemisRules(); } },
    ATHENA("a") { public GodRules createRules() { return new AthenaRules(); } },
    ATLAS("a") { public GodRules createRules() { return new AtlasRules(); } },
    DEMETER("a") { public GodRules createRules() { return new DemeterRules(); } },
    HEPHAESTUS("a") { public GodRules createRules() { return new HephaestusRules(); } },
    MINOTAUR("a") { public GodRules createRules() { return new MinotaurRules(); } },
    PAN("a") { public GodRules createRules() { return new PanRules(); } },
    PROMETHEUS("a") { public GodRules createRules() { return new PrometheusRules(); } },
    ZEUS("a") { public GodRules createRules() { return new ZeusRules(); } },
    TRITON("a") { public GodRules createRules() { return new TritonRules(); } },
    POSEIDON("a") { public GodRules createRules() { return new PoseidonRules(); } },
    HESTIA("a") { public GodRules createRules() { return new HestiaRules(); } },
    HERA("a") { public GodRules createRules() { return new HeraRules(); } };

    String newLine;
    String returnLine;
    String description;
    Gods (String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public String getGodsList(){
        for(Gods god: Arrays.asList(Gods.values())) {
            newLine = "Name: " + god.toString()+ "description: " + god.getDescription();
            returnLine= returnLine + newLine;
        }
        return returnLine;
    }

    /**
     * Creates the set of {@link GodRules rules} for the selected god
     *
     * @return the set of {@link GodRules rules} of the caller
     */
    public abstract GodRules createRules();

}
