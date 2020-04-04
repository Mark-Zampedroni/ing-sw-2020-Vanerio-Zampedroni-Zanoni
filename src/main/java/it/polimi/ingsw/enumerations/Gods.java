package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.rules.*;
import it.polimi.ingsw.rules.gods.*;

public enum Gods {
    APOLLO, ARTEMIS, ATHENA, ATLAS, DEMETER, HEPHAESTUS, MINOTAUR, PAN, PROMETHEUS, ZEUS, TRITON, POSEIDON, HESTIA, HERA;

    public static GodRules create(Gods god) {
        switch(god) {
            case APOLLO: return new ApolloRules();
            case PAN: return new PanRules();
            case ATLAS: return new AtlasRules();
            case ATHENA: return new AthenaRules();
            case ARTEMIS: return new ArtemisRules();
            case DEMETER: return new DemeterRules();
            case MINOTAUR: return new MinotaurRules();
            case HEPHAESTUS: return new HephaestusRules();
            case PROMETHEUS: return new PrometheusRules();
            case ZEUS: return new ZeusRules();
            case TRITON: return new TritonRules();
            case POSEIDON: return new PoseidonRules();
            case HESTIA: return new HestiaRules();
            case HERA: return new HeraRules();
            default: return new CommonRules(); // Can't occur
        }
    }


}
