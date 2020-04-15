package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.rules.*;
import it.polimi.ingsw.rules.gods.*;

import java.io.Serializable;

/**
 * Playable gods, each {@link it.polimi.ingsw.model.player.Player player} can select one
 */
public enum Gods implements Serializable {
    APOLLO { public GodRules createRules() { return new ApolloRules(); } },
    ARTEMIS { public GodRules createRules() { return new ArtemisRules(); } },
    ATHENA { public GodRules createRules() { return new AthenaRules(); } },
    ATLAS { public GodRules createRules() { return new AtlasRules(); } },
    DEMETER { public GodRules createRules() { return new DemeterRules(); } },
    HEPHAESTUS { public GodRules createRules() { return new HephaestusRules(); } },
    MINOTAUR { public GodRules createRules() { return new MinotaurRules(); } },
    PAN { public GodRules createRules() { return new PanRules(); } },
    PROMETHEUS { public GodRules createRules() { return new PrometheusRules(); } },
    ZEUS { public GodRules createRules() { return new ZeusRules(); } },
    TRITON { public GodRules createRules() { return new TritonRules(); } },
    POSEIDON { public GodRules createRules() { return new PoseidonRules(); } },
    HESTIA { public GodRules createRules() { return new HestiaRules(); } },
    HERA { public GodRules createRules() { return new HeraRules(); } };

    /**
     * Creates the set of {@link GodRules rules} for the selected god
     *
     * @return the set of {@link GodRules rules} of the caller
     */
    public abstract GodRules createRules();


}
